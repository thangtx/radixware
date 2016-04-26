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

package org.radixware.kernel.designer.ads.common.dialogs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javax.swing.*;
import org.openide.util.ChangeSupport;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsTransparentMethodDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.type.AdsAccessFlags;
import org.radixware.kernel.common.defs.ads.xml.IXmlDefinition;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.common.utils.events.IRadixEventListener;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.designer.common.dialogs.components.KernelEnumComboBoxModel;


public class AccessPanel extends javax.swing.JPanel {

    public enum EOptions {
        PUBLISHED, ACCESS, FINAL
    }
    private static final class AccessComboBoxModel extends KernelEnumComboBoxModel<EAccess> {

        public AccessComboBoxModel(EAccess selected) {
            super(EAccess.class, selected);
        }

        public AccessComboBoxModel(Collection<EAccess> exclude, EAccess selected) {
            super(EAccess.class, null, exclude, selected);
        }

    }

    private static final class AccessModel {

        EAccess access;
        boolean isFinal;
        boolean isPublished;
        AdsDefinition definition;
        private final boolean directly;
        ChangeSupport changeSupport;

        public AccessModel(boolean directly) {

            this.directly = directly;
        }

        void apply() {
            if (!directly && definition != null) {
                definition.setAccessMode(access);
                definition.setPublished(isPublished);
                definition.setFinal(isFinal);
            }
        }

        void setFinal(boolean isFinal) {
            if (this.isFinal != isFinal) {
                this.isFinal = isFinal;

                if (directly && definition != null) {
                    definition.setFinal(isFinal);
                }

                changeSupport.fireChange();
            }
        }

        void setPublished(boolean isPublished) {
            if (this.isPublished != isPublished) {
                this.isPublished = isPublished;

                if (directly && definition != null) {
                    definition.setPublished(isPublished);
                }
                changeSupport.fireChange();
            }
        }

        void setAccessMode(EAccess access) {
            if (this.access != access) {
                this.access = access;

                if (directly && definition != null) {
                    definition.setAccessMode(access);
                }

                changeSupport.fireChange();
            }
        }

        void open(AdsDefinition definition, ChangeSupport changeSupport) {

            assert definition != null;

            this.changeSupport = changeSupport;
            this.definition = definition;

            if (definition != null) {
                access = definition.getAccessMode();
                isFinal = definition.isFinal();
                isPublished = definition.isPublished();
            }
        }

        boolean isFinal() {
            if (directly && definition != null) {
                return definition.isFinal();
            }
            return isFinal;
        }

        boolean isPublished() {
            if (directly && definition != null) {
                return definition.isPublished();
            }
            return isPublished;
        }

        EAccess getAccessMode() {
            if (directly && definition != null) {
                return definition.getAccessMode();
            }
            return access;
        }
    }

    private final int GAP = 10;
    private JComboBox<KernelEnumComboBoxModel.Item<EAccess>> comboBox;
    private JCheckBox publishedCheck;
    private JCheckBox finalCheck;
    private AdsDefinition definition;
    private AccessComboBoxModel model;
    private ArrayList<JCheckBox> checks = new ArrayList<>();
    private int checksCount = 2;
    private final ChangeSupport changeSupport = new ChangeSupport(this);
    private final AccessModel accessModel;

    public AccessPanel(boolean directly) {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        comboBox = new JComboBox();
        add(comboBox);
        add(Box.createRigidArea(new Dimension(5, 5)));

//        comboBox.setModel(model);
        comboBox.setEditable(false);
//        comboBox.setRenderer(new Renderer(comboBox.getRenderer()));
//        comboBox.setPrototypeDisplayValue(EAccess.PROTECTED);

        publishedCheck = new CheckBox("Published");
        publishedCheck.setAlignmentX(CENTER_ALIGNMENT);
        publishedCheck.setAlignmentY(CENTER_ALIGNMENT);
        finalCheck = new CheckBox("Final");
        finalCheck.setAlignmentX(CENTER_ALIGNMENT);
        finalCheck.setAlignmentY(CENTER_ALIGNMENT);

        add(publishedCheck);
        add(Box.createRigidArea(new Dimension(GAP, GAP)));
        add(finalCheck);

        checks.add(publishedCheck);
        checks.add(finalCheck);

        this.accessModel = new AccessModel(directly);
    }

    public AccessPanel() {
        this(true);
    }

    public JCheckBox addCheckBox(String text) {
        CheckBox check = new CheckBox(text);
        add(Box.createRigidArea(new Dimension(GAP, GAP)));
        add(check);
        checksCount++;
        checks.add(check);
        return check;
    }

    private class CheckBox extends JCheckBox {

        CheckBox(String text) {
            super(text);
        }

        CheckBox(Action action) {
            super(action);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(super.getPreferredSize().width, comboBox.getPreferredSize().height);
        }

        @Override
        public Dimension getMaximumSize() {
            return new Dimension(super.getMaximumSize().width, comboBox.getMaximumSize().height);
        }

        @Override
        public Dimension getMinimumSize() {
            return new Dimension(super.getMinimumSize().width, comboBox.getMinimumSize().height);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        int h = comboBox.getPreferredSize().height;
        int w = comboBox.getPreferredSize().width + GAP;
        for (JCheckBox box : checks) {
            w += box.getPreferredSize().width + GAP;
        }
        return new Dimension(w, h);
    }

    @Override
    public Dimension getMaximumSize() {
        int h = comboBox.getMaximumSize().height;
        int w = comboBox.getMaximumSize().width + GAP;
        for (JCheckBox box : checks) {
            w += box.getMaximumSize().width + GAP;
        }
        return new Dimension(w, h);
    }

    @Override
    public Dimension getMinimumSize() {
        int h = comboBox.getMinimumSize().height;
        int w = comboBox.getMinimumSize().width + GAP;
        for (JCheckBox box : checks) {
            w += box.getMinimumSize().width + GAP;
        }
        return super.getMinimumSize();
    }

    @Override
    public int getBaseline(int width, int height) {
        return comboBox.getBaseline(getPreferredSize().width, comboBox.getPreferredSize().height) + finalCheck.getY();
    }

    public boolean setEnabled(EOptions key, boolean enabled) {
        if (key == null) {
            return false;
        }

        switch (key) {
            case ACCESS:
                comboBox.setEnabled(enabled);
                return true;
            case PUBLISHED:
                publishedCheck.setEnabled(enabled);
                return true;
            case FINAL:
                finalCheck.setEnabled(enabled);
                return true;
            default:
                return false;
        }
    }

    public void open(AdsDefinition definition) {
        open(definition, new EAccess[0]);
    }
    private IRadixEventListener<RadixEvent> accListener = new IRadixEventListener<RadixEvent>() {

        @Override
        public void onEvent(RadixEvent e) {

            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    if (definition != null) {
                        updateChFinalState(canChangeFinal(false));
                        updateChPublishedState();
                    }
                }
            });
        }
    };
    private final ItemListener itemListener = new ItemListener() {

        @Override
        public void itemStateChanged(ItemEvent e) {
            if (definition != null) {
                accessModel.setAccessMode(model.getSelectedItemSource());
                updateChPublishedState();
                updateChFinalState(canChangeFinal(false));
            }
        }
    };
    private final ActionListener finalListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (definition != null) {
                accessModel.setFinal(finalCheck.isSelected());
                updateChFinalState(canChangeFinal(false));
                updateChPublishedState();
            }
        }
    };
    private final ActionListener publishedListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (definition != null) {
                accessModel.setPublished(publishedCheck.isSelected());
                updateChFinalState(canChangeFinal(false));
                updateChPublishedState();
            }
        }
    };

    public void open(AdsDefinition definition, EAccess... deny) {
        synchronized (this) {

            accessModel.open(definition, getChangeSupport());

            this.definition = definition;
            if (definition != null) {
                finalCheck.setEnabled(true);
                publishedCheck.setEnabled(true);

                comboBox.removeItemListener(itemListener);
                finalCheck.removeActionListener(finalListener);
                publishedCheck.removeActionListener(publishedListener);
                updateChFinalState(canChangeFinal(true));
                comboBox.setEnabled(!definition.isReadOnly() && definition.canChangeAccessMode());
                finalCheck.setSelected(accessModel.isFinal());
                publishedCheck.setSelected(accessModel.isPublished());
                updateChPublishedState();

                final EAccess minAccess = definition.getMinimumAccess();
                model = calculateAccessModel(definition, minAccess, deny);
                comboBox.setModel(model);
                                
                if (definition instanceof AdsTransparentMethodDef){
                    comboBox.setEnabled(minAccess != definition.getAccessMode());
                }

                setEnabled(definition.canChangeAccessMode() && !definition.isReadOnly());
                comboBox.addItemListener(itemListener);
                finalCheck.addActionListener(finalListener);
                publishedCheck.addActionListener(publishedListener);
            } else {
                comboBox.setEnabled(false);
                finalCheck.setEnabled(false);
                publishedCheck.setEnabled(false);
                model = new AccessComboBoxModel(EAccess.PUBLIC);
            }
        }
    }

    private AccessComboBoxModel calculateAccessModel(AdsDefinition definition, final EAccess min, EAccess... deny) {
        final Set<EAccess> exclude = new HashSet<>();
        if (deny != null && deny.length > 0) {
            for (final EAccess access : deny) {
                exclude.add(access);
            }
        }

        for (final EAccess access : EAccess.values()) {
            if (access.isLess(min)) {
                exclude.add(access);
            }
        }
        if ((definition instanceof AdsClassDef && !((AdsClassDef) definition).isNested())
            || definition instanceof AdsEnumDef || definition instanceof IXmlDefinition) {

            exclude.add(EAccess.PROTECTED);
            exclude.add(EAccess.PRIVATE);
        }
        return new AccessComboBoxModel(exclude, definition.getAccessMode());
    }

    private void updateChPublishedState() {
        if (definition != null) {
            publishedCheck.setSelected(accessModel.isPublished());
            publishedCheck.setEnabled(!definition.isReadOnly() && definition.canChangePublishing());
        } else {
            publishedCheck.setEnabled(false);
        }
    }

    private void updateChFinalState(boolean canChangeFinal) {
        if (definition != null) {
            boolean isFinal = accessModel.isFinal();
            boolean isError = isFinal && !canChangeFinal;

            if (isError) {
                finalCheck.setForeground(Color.RED);
                finalCheck.setEnabled(!definition.isReadOnly());
            } else {
                finalCheck.setForeground(UIManager.getColor("CheckBox.foreground"));
                finalCheck.setEnabled(!definition.isReadOnly() && canChangeFinal && definition.canChangeFinality());
            }
            finalCheck.setSelected(isFinal);
        } else {
            finalCheck.setEnabled(false);
        }
    }

    @SuppressWarnings("unchecked")
    private boolean canChangeFinal(boolean openMode) {
        if (definition == null) {
            return false;
        }
        boolean result = definition.canBeFinal();

        if (result || openMode) {
            AdsAccessFlags flags = null;
            if (definition instanceof AdsClassDef) {
                flags = ((AdsClassDef) definition).getAccessFlags();
            } else if (definition instanceof AdsPropertyDef) {
                flags = ((AdsPropertyDef) definition).getAccessFlags();
            } else if (definition instanceof AdsMethodDef) {
                flags = ((AdsMethodDef) definition).getProfile().getAccessFlags();
            }
            if (flags != null) {
                if (openMode) {
                    flags.getAccessFlagsChangesSupport().addEventListener(accListener);
                }
                if (flags.isAbstract()) {
                    return false;
                }
            }
        }
        return result;
    }

    public ChangeSupport getChangeSupport() {
        return changeSupport;
    }

    public void apply() {
        accessModel.apply();
    }
}
