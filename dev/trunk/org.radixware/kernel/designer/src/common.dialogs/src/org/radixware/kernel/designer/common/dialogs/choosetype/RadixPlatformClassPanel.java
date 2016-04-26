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

/*
 * RadixPlatformClassList.java
 *
 * Created on 20.05.2009, 14:59:45
 */
package org.radixware.kernel.designer.common.dialogs.choosetype;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import org.openide.util.ChangeSupport;
import org.openide.util.Mutex;
import org.openide.util.NbBundle;
import org.openide.util.RequestProcessor;
import org.radixware.kernel.common.compiler.core.lookup.LookupUtils;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.IEnvDependent;
import org.radixware.kernel.common.defs.ads.enumeration.AdsPlatformEnumDef;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.platform.PlatformLib;
import org.radixware.kernel.common.defs.ads.platform.RadixPlatformClass;
import org.radixware.kernel.common.defs.ads.platform.RadixPlatformEnum;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.ads.AdsSegment;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDialogUtils;
import org.radixware.kernel.designer.common.dialogs.components.state.StateDisplayer;
import org.radixware.kernel.designer.common.dialogs.components.state.StateManager;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;
import org.radixware.kernel.designer.common.dialogs.utils.SearchFieldAdapter;

public final class RadixPlatformClassPanel extends javax.swing.JPanel {

    private static class RadixPlatformClassDisplayer extends ModalDisplayer implements ChangeListener {

        private RadixPlatformClassPanel list;

        public RadixPlatformClassDisplayer(RadixPlatformClassPanel list, JPanel container) {
            super(container);
            this.list = list;
            container.setMinimumSize(new Dimension(200, 200));
            this.list.addChangeListener(this);
            getDialogDescriptor().setValid(list.isComplete());
            setTitle(NbBundle.getMessage(RadixPlatformClassPanel.class, "ClassChoose-Title"));
        }

        @Override
        protected void apply() {
        }
        private String selected;

        @Override
        public void stateChanged(ChangeEvent e) {
            if (list != null) {
                getDialogDescriptor().setValid(list.isComplete());
                selected = list.getSelectedClassName();
            }
        }

        public String getSelected() {
            return selected;
        }
    }

    private static JPanel putToContainer(RadixPlatformClassPanel list) {
        JPanel container = new JPanel();
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        StateDisplayer sd = new StateDisplayer();
        container.setLayout(gbl);

        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        gbl.setConstraints(list, c);
        c.gridy = 1;
        c.weighty = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0, 10, 0, 0);
        gbl.setConstraints(sd, c);
        container.add(list);
        container.add(sd);
        return container;
    }

    public static String choosePlatformCLass(AdsModule module, ERuntimeEnvironmentType usage) {
        RadixPlatformClassPanel panel = new RadixPlatformClassPanel();
        panel.open(module, usage, false);
        return chooseType(panel);
    }

    @Deprecated
    private static String choosePlatformCLass(AdsDefinition context) {
        RadixPlatformClassPanel panel = new RadixPlatformClassPanel();
        panel.open(context, null);

        return chooseType(panel);
    }

    public static String choosePlatformClass(AdsDefinition context, String initFilterText) {
        RadixPlatformClassPanel panel = new RadixPlatformClassPanel();
        panel.open(context, initFilterText);

        return chooseType(panel);
    }

    @Deprecated
    private static String choosePlatformEnum(AdsDefinition context) {
        RadixPlatformClassPanel panel = new RadixPlatformClassPanel();
        panel.open(context, null);
        panel.setChooseOnluEnums(true);

        return chooseType(panel);
    }

    public static String choosePlatformEnum(AdsModule module, ERuntimeEnvironmentType usage) {
        RadixPlatformClassPanel panel = new RadixPlatformClassPanel();
        panel.open(module, usage, true);

        return chooseType(panel);
    }

    private static String chooseType(RadixPlatformClassPanel panel) {
        panel.setMinimumSize(new Dimension(250, 250));
        RadixPlatformClassDisplayer displayer = new RadixPlatformClassDisplayer(panel, putToContainer(panel));
        if (displayer.showModal()) {
            return displayer.getSelected();
        }
        return null;
    }
    private final static String FILTER_DESC = NbBundle.getMessage(RadixPlatformClassPanel.class, "Search-Filter");
    private RadixPlatformClassListModel globalModel;
    private Document searchDoc = new PlainDocument();
    private Document filterDoc;
    private volatile boolean noModsToFilter = false;
    private Definition context;
    private Module module;
    private ERuntimeEnvironmentType usage;
    private boolean chooseOnlyEnums = false;
    private String initSearchText;

    private void open(Module module, ERuntimeEnvironmentType usage, boolean onlyEnums) {
        this.module = module;
        this.usage = usage;
        this.chooseOnlyEnums = onlyEnums;
        this.initSearchText = null;
        init();
    }

    private void open(Definition context, String initSearchText) {
        this.context = context;
        this.initSearchText = initSearchText;
        init();
    }

    private void init() {
        initComponents();
        searchField.setText(FILTER_DESC);
        filterDoc = searchField.getDocument();

        classList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        classList.setCellRenderer(new RadixPlatformClassRenderer(classList));
        classList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                changeSupport.fireChange();
            }
        });

        if (initSearchText != null) {
            searchField.setText(initSearchText);
        }
        loadAvailableClasses();

        DocumentListener doclistener = new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                insertUpdate(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                insertUpdate(e);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                if (e.getDocument().equals(filterDoc)) {
                    if (!noModsToFilter) {
                        try {
                            String change = e.getDocument().getText(e.getOffset(), e.getLength());
                            searchField.setDocument(searchDoc);
                            searchField.setText(change);
                            search();
                        } catch (BadLocationException ex) {
                            searchField.setDocument(searchDoc);
                            search();
                        }
                    }
                } else {
                    search();
                }
            }
        };
        filterDoc.addDocumentListener(doclistener);
        searchDoc.addDocumentListener(doclistener);

        searchField.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                if (searchField.getDocument().equals(filterDoc)) {
                    searchField.getCaret().setVisible(false);
                } else {
                    searchField.getCaret().setVisible(true);
                }
            }
        });
        searchField.addFocusListener(new FocusListener() {
            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()
                        && searchField.getDocument().equals(searchDoc)) {
                    searchField.setDocument(filterDoc);
                    noModsToFilter = true;
                    searchField.setText(FILTER_DESC);
                    noModsToFilter = false;
                }
            }

            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getDocument().equals(filterDoc)) {
                    searchField.selectAll();
                }
            }
        });

        SearchFieldAdapter.exchangeCommands(SearchFieldAdapter.LIST_NAVIGATION_COMMANDS,
                classList, searchField);
    }

    private void setChooseOnluEnums(boolean chooseOnlyEnums) {
        this.chooseOnlyEnums = chooseOnlyEnums;
    }

    private void loadAvailableClasses() {
        searchField.setEnabled(false);
        classList.setEnabled(false);
        final String lastsearch = searchField.getText();
        //classList.setModel(ChooseDialogUtils.getListWaitModel());
        ((CardLayout) content.getLayout()).show(content, "card3");
        searchField.setText(ChooseDialogUtils.WAIT);
        noModsToFilter = true;
        REQUEST_PROCESSOR.post(new Runnable() {
            @Override
            public void run() {
                final List<String> result = getAvailableClassNames();
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        if (result != null && !result.isEmpty()) {
                            classList.setEnabled(true);
                            globalModel = new RadixPlatformClassListModel(result);
                            classList.setModel(globalModel);
                            ((CardLayout) content.getLayout()).show(content, "card4");
                        } else {
                            //classList.setModel(ChooseDialogUtils.getListEmptyModel());
                            ((CardLayout) content.getLayout()).show(content, "card2");
                        }
                        changeSupport.fireChange();
                        searchField.setEnabled(true);
                        searchField.setText(lastsearch);
                        if (!lastsearch.equals(FILTER_DESC)) {
                            searchField.setDocument(searchDoc);
                            searchField.setText(lastsearch);
                            search();
                            searchField.getCaret().setDot(0);
                            searchField.getCaret().moveDot(searchField.getText().length());
                        } else {
                            searchField.selectAll();
                        }

                        searchField.requestFocusInWindow();
                        noModsToFilter = false;
                    }
                });
            }
        });
    }
    private RequestProcessor.Task searchTask;
    private static final RequestProcessor REQUEST_PROCESSOR = new RequestProcessor(RadixPlatformClassPanel.class);

    private void search() {
        if (searchTask != null) {
            searchTask.cancel();
            searchTask = null;
        }
        final String text = searchField.getText();
        if (text.length() == 0) {
            //classList.setModel(globalModel != null ? globalModel : ChooseDialogUtils.getListEmptyModel());
            if (globalModel != null) {
                classList.setModel(globalModel);
                ((CardLayout) content.getLayout()).show(content, "card4");
            } else {
                classList.setModel(new DefaultListModel());
                ((CardLayout) content.getLayout()).show(content, "card2");
            }
            classList.setSelectedIndex(0);
        } else {
            //classList.setModel(ChooseDialogUtils.getListWaitModel());
            ((CardLayout) content.getLayout()).show(content, "card3");
            searchTask = REQUEST_PROCESSOR.post(new Runnable() {
                @Override
                public void run() {
                    final Set<RadixPlatformClassListItem> res = globalModel != null ? globalModel.findMatches(text) : new HashSet<RadixPlatformClassListItem>();
                    Mutex.EVENT.readAccess(new Runnable() {
                        @Override
                        public void run() {
                            if (!text.equals(searchField.getText())) {
                                return;
                            }

                            classList.setModel(new RadixPlatformClassListModel(res));
                            changeSupport.fireChange();
                            if (!res.isEmpty()) {
                                ((CardLayout) content.getLayout()).show(content, "card4");
                            } else {
                                ((CardLayout) content.getLayout()).show(content, "card2");
                            }

                            int index = res.isEmpty() ? -1 : 0;
                            classList.setSelectedIndex(index);
                        }
                    });
                }
            });
        }
    }

    private List<String> getAvailableClassNames() {
        final Set<String> result = new HashSet<String>();

        if (context != null) {
            final Layer layer = context.getLayer();
            if (layer != null) {
                ERuntimeEnvironmentType env = ERuntimeEnvironmentType.COMMON;
                if (context instanceof IEnvDependent) {
                    env = ((IEnvDependent) context).getUsageEnvironment();
                }
                addClasses(result, layer, env);
            }
        } else if (module != null) {
            final Layer layer = module.getLayer();
            if (layer != null) {
                addClasses(result, layer, usage);
            }
        }
        return new ArrayList<String>(result);
    }

    private void addClasses(Set<String> res, Layer lib, ERuntimeEnvironmentType env) {
        if (!chooseOnlyEnums) {
            List<String> byUsage = LookupUtils.collectLibraryClasses(lib, env);
            res.addAll(byUsage);
        } else {
            Set<String> byUsage = LookupUtils.collectLibraryEnumerations(lib).keySet();
            res.addAll(byUsage);
        }
    }

    public String getSelectedClassName() {
        if (classList.getSelectedIndex() != -1) {
            RadixPlatformClassListItem item = (RadixPlatformClassListItem) classList.getSelectedValue();
            return item.getFullName();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        searchField = new javax.swing.JTextField();
        content = new javax.swing.JPanel();
        empty = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        search = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        classList = new javax.swing.JList();

        content.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        content.setLayout(new java.awt.CardLayout());

        empty.setLayout(new java.awt.BorderLayout());

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText(org.openide.util.NbBundle.getMessage(RadixPlatformClassPanel.class, "EmptyClassList")); // NOI18N
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        empty.add(jLabel1, java.awt.BorderLayout.CENTER);

        content.add(empty, "card2");

        search.setLayout(new java.awt.BorderLayout());

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText(org.openide.util.NbBundle.getMessage(RadixPlatformClassPanel.class, "WaitList")); // NOI18N
        jLabel2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        search.add(jLabel2, java.awt.BorderLayout.CENTER);

        content.add(search, "card3");

        jScrollPane1.setViewportView(classList);

        content.add(jScrollPane1, "card4");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(content, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE)
                    .addComponent(searchField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(content, javax.swing.GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList classList;
    private javax.swing.JPanel content;
    private javax.swing.JPanel empty;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel search;
    private javax.swing.JTextField searchField;
    // End of variables declaration//GEN-END:variables
    private final StateManager stateManager = new StateManager(this);

    private boolean isComplete() {
        if (getSelectedClassName() != null) {
            if (!isLoadableClass(getSelectedClassName())) {
                stateManager.error(NbBundle.getMessage(RadixPlatformClassPanel.class, "TypeEditErrors-ClassLoad") + getSelectedClassName());
                return false;
            }
            stateManager.ok();
            return true;
        } else {
            stateManager.error("");
            return false;
        }
    }

    private boolean isLoadableClass(String name) {
        PlatformLib kernelLib = null;
        if (context != null) {
            ERuntimeEnvironmentType env = ERuntimeEnvironmentType.COMMON;
            if (context instanceof IEnvDependent) {
                env = ((IEnvDependent) context).getUsageEnvironment();
            }
            kernelLib = ((AdsSegment) context.getModule().getSegment()).getBuildPath().getPlatformLibs().getKernelLib(env);
        } else {
            kernelLib = ((AdsSegment) module.getSegment()).getBuildPath().getPlatformLibs().getKernelLib(usage);
        }
        if (!chooseOnlyEnums) {
            RadixPlatformClass cl = kernelLib.findPlatformClass(name);
            if (cl != null) {
                return true;
            }
        } else {
            RadixPlatformEnum en = AdsPlatformEnumDef.findPlatformEnum(context == null ? module : context, name, null);
            if (en != null) {
                return true;
            }
        }
        return false;
    }
    private ChangeSupport changeSupport = new ChangeSupport(this);

    public final void addChangeListener(ChangeListener l) {
        changeSupport.addChangeListener(l);
    }

    public final void removeChangeListener(ChangeListener l) {
        changeSupport.removeChangeListener(l);
    }
}
