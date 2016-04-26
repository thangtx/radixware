/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. This Source Code is distributed
 * WITHOUT ANY WARRANTY; including any implied warranties but not limited to
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Mozilla Public License, v. 2.0. for more details.
 */

package org.radixware.kernel.designer.debugger;

import java.awt.Dimension;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import org.openide.util.ChangeSupport;
import org.openide.util.RequestProcessor;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.designer.common.general.filesystem.RadixFileUtil;
import org.radixware.kernel.designer.debugger.breakpoints.BreakpointsStore;


public class ChooseSessionType extends JComboBox implements ChangeListener {

    public ChooseSessionType() {
        super(new ProxyModel());

        //until i found more beautiful way to do this...
        RequestProcessor.getDefault().post(new Runnable() {

            @Override
            public void run() {
                BreakpointsStore.getInstance().initBreakpoints();
            }
        });

        ((ProxyModel) getModel()).addSubModelChangeListener(this);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        revalidate();
    }

    @Override
    public String getToolTipText() {
        return "Choose product component for debugging (Server or Explorer) and debugging profile";
    }

    // should not be stretched in all directions
    @Override
    public Dimension getMaximumSize() {
        return super.getPreferredSize();
    }

    private static final class DebugProfileModel extends AbstractListModel implements ComboBoxModel {

        private enum ESession {

            SERVER("Server"), EXPLORER("Explorer");
            private String name;

            private ESession(String name) {
                this.name = name;
            }

            @Override
            public String toString() {
                return name;
            }

            public static ESession getByName(String name) {
                for (ESession session : values()) {
                    if (session.name.equals(name)) {
                        return session;
                    }
                }
                return null;
            }
        }

        private static class Profile {

            private final ESession session;
            private final String profileName;

            public Profile(ESession session, String profile) {
                this.session = session;
                this.profileName = profile;
            }

            @Override
            public String toString() {
                return String.valueOf(session) + " (" + String.valueOf(profileName) + ")";
            }

            @Override
            public boolean equals(Object obj) {
                if (super.equals(obj)) {
                    return true;
                }
                if (obj instanceof Profile) {
                    Profile profile = (Profile) obj;
                    return Utils.equals(profileName, profile.profileName) && session == profile.session;
                }
                return false;
            }

            @Override
            public int hashCode() {
                int hash = 3;
                hash = 89 * hash + (this.session != null ? this.session.hashCode() : 0);
                hash = 89 * hash + (this.profileName != null ? this.profileName.hashCode() : 0);
                return hash;
            }

            public ESession getSession() {
                return session;
            }

            public String getProfileName() {
                return profileName;
            }
        }
        private Profile profile;

        public DebugProfileModel() {

            updateSelection();
            StartupInfoManager.getInstance().addChangeListener(new ChangeListener() {

                @Override
                public void stateChanged(ChangeEvent e) {
                    updateSelection();
                }
            });
        }

        private void updateSelection() {
            try {
                String profileName = "Default";
                Branch branch = getBranch();
                if (branch != null) {
                    profileName = StartupInfoManager.getInstance().getCurrentProfileName(branch);
                }
                ESession session = ESession.getByName(RadixDebugger.getDefaultDebugee());

                profile = new Profile(session, profileName);
                fireContentsChanged(this, 0, 0);
            } catch (Throwable e) {
            }
        }

        private Branch getBranch() {
            List<Branch> openedBranches = (List<Branch>) RadixFileUtil.getOpenedBranches();
            if (openedBranches != null && !openedBranches.isEmpty()) {
                return openedBranches.get(0);
            }
            return null;
        }

        private List<String> getProfileNameList() {
            try {
                final Branch branch = getBranch();
                if (branch != null) {
                    return StartupInfoManager.getInstance().getProfileNameList(branch);
                }
            } catch (Exception e) {
                //...
            }

            return Collections.<String>emptyList();
        }

        @Override
        public int getSize() {
            return getProfileNameList().size() * 2;
        }

        @Override
        public Object getElementAt(int index) {
            final List<String> profileNameList = getProfileNameList();
            final int size = profileNameList.size();

            if (size * 2 > index) {
                ESession session = index < size ? ESession.SERVER : ESession.EXPLORER;
                String profileName = getProfileNameList().get(index % size);

                return new Profile(session, profileName);
            }
            return null;
        }

        @Override
        public void setSelectedItem(Object anItem) {
            if (anItem instanceof Profile) {
                profile = (Profile) anItem;
                RadixDebugger.setDefaultDebugee(String.valueOf(profile.getSession()));
                StartupInfoManager.getInstance().setCurrentProfile(getBranch(), profile.getProfileName());
            }
        }

        @Override
        public Profile getSelectedItem() {
            return profile;
        }
    }

    private static final class WaitModel implements ComboBoxModel {

        private static final String item = "Loading...";

        @Override
        public void setSelectedItem(Object anItem) {
        }

        @Override
        public Object getSelectedItem() {
            return item;
        }

        @Override
        public int getSize() {
            return 1;
        }

        @Override
        public Object getElementAt(int index) {
            return item;
        }

        @Override
        public void addListDataListener(ListDataListener l) {
        }

        @Override
        public void removeListDataListener(ListDataListener l) {
        }
    }

    private static final class ProxyModel implements ComboBoxModel {

        private ComboBoxModel model;
        private BranchesLoadService service;
        private final List<ListDataListener> listeners = new LinkedList<>();
        private final ListDataListener proxyListener = new ListDataListener() {

            @Override
            public void intervalAdded(ListDataEvent e) {
                contentsChanged(e);
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
                contentsChanged(e);
            }

            @Override
            public void contentsChanged(ListDataEvent e) {
                fireChanges(e);
            }
        };

        public ProxyModel() {
            model = new WaitModel();
            service = new BranchesLoadService(new Runnable() {

                @Override
                public void run() {
                    SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            changeModel(new DebugProfileModel());
                        }
                    });
                }
            });
            service.load();
        }

        @Override
        public void setSelectedItem(Object anItem) {
            model.setSelectedItem(anItem);
        }

        @Override
        public Object getSelectedItem() {
            return model.getSelectedItem();
        }

        @Override
        public int getSize() {
            return model.getSize();
        }

        @Override
        public Object getElementAt(int index) {
            return model.getElementAt(index);
        }

        @Override
        public void addListDataListener(ListDataListener l) {
            listeners.add(l);
        }

        @Override
        public void removeListDataListener(ListDataListener l) {
            listeners.remove(l);
        }

        private void changeModel(ComboBoxModel newModel) {
            if (newModel != null) {
                model.removeListDataListener(proxyListener);

                model = newModel;

                newModel.addListDataListener(proxyListener);
                fireChanges(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, 0));
                fireSubModelChange();
            }
        }

        private void fireChanges(ListDataEvent event) {
            for (ListDataListener listener : listeners) {
                listener.contentsChanged(event);
            }
        }

        public ComboBoxModel getSourceModel() {
            return model;
        }
        private final ChangeSupport subModelChangeSupport = new ChangeSupport(this);

        public void addSubModelChangeListener(ChangeListener listener) {
            subModelChangeSupport.addChangeListener(listener);
        }

        public void removeSubModelChangeListener(ChangeListener listener) {
            subModelChangeSupport.removeChangeListener(listener);
        }

        void fireSubModelChange() {
            subModelChangeSupport.fireChange();
        }
    }
}
