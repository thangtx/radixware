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
package org.radixware.kernel.designer.common.editors.layer;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicReference;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import net.miginfocom.swing.MigLayout;
import org.radixware.kernel.common.defs.HierarchyWalker;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.LayerUtils;
import org.radixware.kernel.common.repository.Segment;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Pair;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.designer.common.dialogs.RadixWareDesignerIcon;
import org.radixware.kernel.designer.common.dialogs.radixdoc.RadixdocSelectModulesPanel;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.dialogs.utils.IAcceptor;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;
import org.radixware.kernel.designer.common.dialogs.utils.RadixNbEditorUtils;
import org.radixware.kernel.designer.common.editors.layer.license.LicenseDependenciesTreePanel;

public class DependenciesPanel extends JPanel {

    private static final String ADD_DEP_TOOLTIP = "Add Dependency";
    private static final String REMOVE_DEP_TOOLTIP = "Remove Dependency";

    private final Layer.License license;
    private final boolean readOnly;
    private final LicensesPanel parent;

    private final JList depList = new JList();
    private final JList moduleDepList = new JList();

    private final LicenseDependenciesTreePanel allDepPanel;

    private final JButton addDepBtn = new JButton(RadixWareDesignerIcon.CREATE.ADD.getIcon());
    private final JButton removeDepBtn = new JButton(RadixWareDesignerIcon.DELETE.DELETE.getIcon());

    private final JButton addModuleDepBtn = new JButton(RadixWareDesignerIcon.CREATE.ADD.getIcon());
    private final JButton removeModuleDepBtn = new JButton(RadixWareDesignerIcon.DELETE.DELETE.getIcon());

    private final JButton expandCurrentNodeInAllDepTreeBtn = new JButton(RadixWareDesignerIcon.EDIT.LINK.getIcon());
    private final JToggleButton hideDepModulesBtn = new JToggleButton(RadixWareIcons.LICENSES.HIDE_MODULE_DEPENDENCIES.getIcon());
    private final JToggleButton hideDepLicensesBtn = new JToggleButton(RadixWareIcons.LICENSES.HIDE_LICENSE_DEPENDENCIES.getIcon());

    private final List<Pair<String, Id>> exceptedModules;
    private final List<String> includedLayers;

    public DependenciesPanel(final Layer.License license, final boolean readOnly, final LicensesPanel parent) {
        this.license = license;
        this.readOnly = readOnly;
        this.parent = parent;

        exceptedModules = getExceptedModules(license);
        includedLayers = getIncludedLayers(license);

        allDepPanel = new LicenseDependenciesTreePanel(license, getUnsavedLicensesFromParent());

        initComponents();
    }

    private void initComponents() {
        this.setLayout(new MigLayout("fill", "[][grow]", "[shrink][shrink][shrink][grow]"));
        this.add(new JLabel("License:"));

        final JTextField tfLicense = new JTextField(license.getFullName());
        tfLicense.setEditable(false);
        tfLicense.setMaximumSize(new Dimension(Integer.MAX_VALUE, tfLicense.getPreferredSize().height));
        tfLicense.setMinimumSize(new Dimension(280, tfLicense.getPreferredSize().height));
        this.add(tfLicense, "growx, wrap");

        final JTabbedPane tabsPanel = new JTabbedPane();
        tabsPanel.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int currentTab = tabsPanel.getSelectedIndex();
                addDepBtn.setVisible(currentTab == 0);
                removeDepBtn.setVisible(currentTab == 0);

                addModuleDepBtn.setVisible(currentTab == 1);
                removeModuleDepBtn.setVisible(currentTab == 1);

                expandCurrentNodeInAllDepTreeBtn.setVisible(currentTab == 2);
                hideDepModulesBtn.setVisible(currentTab == 2);
                hideDepLicensesBtn.setVisible(currentTab == 2);

                if (currentTab == 2) {
                    allDepPanel.update(getDependencies(), getRequiredModules());
                }
            }
        });

        final JToolBar toolbar = prepareToolbar(tabsPanel.getSelectedIndex());

        prepareDepList();
        JScrollPane depScroll = new JScrollPane(depList);
        depScroll.setBorder(BorderFactory.createEmptyBorder());

        prepareModulesDepList();
        JScrollPane modulesDepScroll = new JScrollPane(moduleDepList);
        modulesDepScroll.setBorder(BorderFactory.createEmptyBorder());

        tabsPanel.addTab("License Dependencies", depScroll);
        tabsPanel.addTab("Module Dependencies", modulesDepScroll);

        tabsPanel.addTab("View All Dependencies", allDepPanel);

        this.add(new JLabel("Dependencies"), "span, wrap");
        this.add(toolbar, "growx, span, wrap");
        this.add(tabsPanel, "span, grow");
    }

    private JToolBar prepareToolbar(int currenTab) {
        final JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);

        RadixNbEditorUtils.processToolbarButton(addDepBtn);
        addDepBtn.setToolTipText(ADD_DEP_TOOLTIP);
        addDepBtn.setEnabled(!readOnly);
        addDepBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final List<Layer> baseLayers = new ArrayList<>();
                LayerUtils.collectBaseLayers(license.getLayer(), baseLayers);
                final List<Layer.License> rootLicenses = new ArrayList<>();
                final Map<String, Layer.License> fullNameToLicense;
                if (parent != null) {
                    TreeModel licenseTreeModel = parent.getLicenseTree().getModel();
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) licenseTreeModel.getChild(licenseTreeModel.getRoot(), 0);

                    final Layer.License layerLicenseFromParentEditor = parent.getLicense(node);
                    rootLicenses.add(layerLicenseFromParentEditor);
                    fullNameToLicense = layerLicenseFromParentEditor.collectFullNameToLicenseMap();
                } else {
                    rootLicenses.add(license.getLayer().getLicenses());
                    fullNameToLicense = license.getLayer().getLicenses().collectFullNameToLicenseMap();
                }
                for (Layer baseLayer : baseLayers) {
                    rootLicenses.add(baseLayer.getLicenses());
                }
                final Layer.License dep = LicensesPanel.selectLicense(rootLicenses, true, true, new IAcceptor<Layer.License>() {
                    @Override
                    public boolean accept(Layer.License candidate) {
                        if (candidate == null) {
                            return false;
                        }
                        if (license.getFullName().startsWith(candidate.getFullName())) {
                            return false;
                        }

                        if (((DefaultListModel) depList.getModel()).contains(candidate.getFullName())) {
                            return false;
                        }
                        if (isDepends(candidate, license)) {
                            return false;
                        }
                        return true;
                    }

                    private boolean isDepends(final Layer.License who, final Layer.License onWhat) {
                        if (who.getFullName().equals(onWhat.getFullName())) {
                            return true;
                        }
                        if (who.getDependencies() != null) {
                            for (String depFullName : who.getDependencies()) {
                                final Layer.License depLicense = fullNameToLicense.get(depFullName);
                                if (depLicense != null && isDepends(depLicense, onWhat)) {
                                    return true;
                                }
                            }
                        }
                        return false;
                    }
                });
                if (dep != null) {
                    ((DefaultListModel) depList.getModel()).addElement(dep.getFullName());
                }
            }
        });
        toolbar.add(addDepBtn);

        RadixNbEditorUtils.processToolbarButton(removeDepBtn);
        removeDepBtn.setToolTipText(REMOVE_DEP_TOOLTIP);
        removeDepBtn.setEnabled(false);
        removeDepBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (DialogUtils.messageConfirmation("Remove selected item(s)?")) {
                    int[] selectedIndices = depList.getSelectedIndices();
                    for (int i = selectedIndices.length - 1; i >= 0; i--) {
                        ((DefaultListModel) depList.getModel()).remove(selectedIndices[i]);
                    }
                }
            }
        });
        toolbar.add(removeDepBtn);

        RadixNbEditorUtils.processToolbarButton(addModuleDepBtn);
        addModuleDepBtn.setToolTipText("Add Dependency");
        addModuleDepBtn.setEnabled(!readOnly);
        addModuleDepBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RadixdocSelectModulesPanel modulesPanel = new RadixdocSelectModulesPanel(license.getLayer().getBranch(), exceptedModules, includedLayers, true);

                final ModalDisplayer selectModulesDisplayer = new ModalDisplayer(modulesPanel, "Select Modules");
                JButton okButton = getButtonByText(selectModulesDisplayer.getDialog(), "OK");
                modulesPanel.setOkButtonFromDialog(okButton);

                if (selectModulesDisplayer.showModal()) {
                    for (Module module : modulesPanel.getSelectedModules()) {
                        String layerUri = module.getLayer().getURI();
                        ((DefaultListModel) moduleDepList.getModel()).addElement(new RequiredModulesListItem(module.getId(), layerUri, module.getQualifiedName(), module.getIcon().getIcon()));
                        exceptedModules.add(new Pair<>(layerUri, module.getId()));
                    }
                }

            }

            private JButton getButtonByText(Container c, String text) {
                Component[] components = c.getComponents();
                for (Component com : components) {
                    if (com instanceof JButton) {
                        if (((JButton) com).getText().equals(text)) {
                            return (JButton) com;
                        }
                    } else if (com instanceof Container) {
                        JButton b = getButtonByText((Container) com, text);
                        if (b != null) {
                            return b;
                        }
                    }
                }
                return null;
            }
        });
        toolbar.add(addModuleDepBtn);

        RadixNbEditorUtils.processToolbarButton(removeModuleDepBtn);
        removeModuleDepBtn.setToolTipText("Remove Dependency");
        removeModuleDepBtn.setEnabled(false);
        removeModuleDepBtn.setVisible(currenTab == 1);
        removeModuleDepBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (DialogUtils.messageConfirmation("Remove selected item(s)?")) {
                    int[] selectedIndices = moduleDepList.getSelectedIndices();
                    for (int i = selectedIndices.length - 1; i >= 0; i--) {
                        Layer.License.RequiredModule module = (Layer.License.RequiredModule) moduleDepList.getModel().getElementAt(selectedIndices[i]);
                        ((DefaultListModel) moduleDepList.getModel()).remove(selectedIndices[i]);
                        if (exceptedModules != null) {
                            for (int j = exceptedModules.size() - 1; j >= 0; j--) {
                                Pair<String, Id> pair = exceptedModules.get(j);
                                if (pair.getFirst().equals(module.layerUri) && pair.getSecond() == module.id) {
                                    exceptedModules.remove(j);
                                }
                            }
                        }
                    }
                }
            }
        });
        toolbar.add(removeModuleDepBtn);

        RadixNbEditorUtils.processToolbarButton(expandCurrentNodeInAllDepTreeBtn);
        expandCurrentNodeInAllDepTreeBtn.setToolTipText("Expand dependency path");
        expandCurrentNodeInAllDepTreeBtn.setVisible(currenTab == 2);
        expandCurrentNodeInAllDepTreeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                allDepPanel.expandWay();
            }
        });
        toolbar.add(expandCurrentNodeInAllDepTreeBtn);

        ActionListener filterAllDepTreeBtnListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                allDepPanel.applyFilter(hideDepModulesBtn.isSelected(), hideDepLicensesBtn.isSelected());
            }
        };

        RadixNbEditorUtils.processToolbarButton(hideDepModulesBtn);
        hideDepModulesBtn.setToolTipText("Hide modules");
        hideDepModulesBtn.setVisible(currenTab == 2);
        hideDepModulesBtn.addActionListener(filterAllDepTreeBtnListener);
        toolbar.add(hideDepModulesBtn);

        RadixNbEditorUtils.processToolbarButton(hideDepLicensesBtn);
        hideDepLicensesBtn.setToolTipText("Hide licenses");
        hideDepLicensesBtn.setVisible(currenTab == 2);
        hideDepLicensesBtn.addActionListener(filterAllDepTreeBtnListener);
        toolbar.add(hideDepLicensesBtn);

        return toolbar;
    }

    private void prepareDepList() {
        depList.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        depList.setModel(new DefaultListModel());
        depList.setCellRenderer(new DepListCellRenderer());

        if (license.getDependencies() != null) {
            for (String depFullName : license.getDependencies()) {
                ((DefaultListModel) depList.getModel()).addElement(depFullName);
            }
        }

        depList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                removeDepBtn.setEnabled(!readOnly && depList.getSelectedIndices().length > 0);
            }
        });
    }

    public List<String> getDependencies() {
        List<String> dependencies = new ArrayList<>();
        for (int i = 0; i < depList.getModel().getSize(); i++) {
            dependencies.add(depList.getModel().getElementAt(i).toString());
        }

        return dependencies;
    }

    public List<Layer.License.RequiredModule> getRequiredModules() {
        List<Layer.License.RequiredModule> requiredModules = new ArrayList<>();
        DefaultListModel model = (DefaultListModel) moduleDepList.getModel();
        for (int i = 0; i < moduleDepList.getModel().getSize(); i++) {
            Layer.License.RequiredModule item = (Layer.License.RequiredModule) model.getElementAt(i);
            requiredModules.add(item);
        }

        return requiredModules;
    }

    private void prepareModulesDepList() {
        moduleDepList.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        moduleDepList.setModel(new DefaultListModel());
        moduleDepList.setCellRenderer(new DepListCellRenderer());

        if (license.getRequiredModules() != null) {
            for (final Layer.License.RequiredModule module : license.getRequiredModules()) {
                final AtomicReference<String> moduleName = new AtomicReference<>(null);
                final AtomicReference<Icon> moduleIcon = new AtomicReference<>(null);

                Layer.HierarchyWalker.walk(license.getLayer(), new Layer.HierarchyWalker.Acceptor<Object>() {

                    @Override
                    public void accept(HierarchyWalker.Controller<Object> controller, Layer layer) {
                        if (!layer.getURI().equals(module.layerUri)) {
                            return;
                        }

                        if (moduleName.get() == null) {
                            layer.visit(new IVisitor() {

                                @Override
                                public void accept(RadixObject radixObject) {
                                    if (radixObject instanceof Module) {
                                        Module moduleDef = (Module) radixObject;
                                        if (moduleDef.getId() == module.id) {
                                            moduleName.set(moduleDef.getQualifiedName());
                                            moduleIcon.set(moduleDef.getIcon().getIcon());
                                        }
                                    }
                                }
                            }, new VisitorProvider() {

                                @Override
                                public boolean isTarget(RadixObject radixObject) {
                                    return radixObject instanceof Module && ((Module) radixObject).getId() == module.id;
                                }

                                @Override
                                public boolean isContainer(RadixObject radixObject) {
                                    return radixObject instanceof Branch || radixObject instanceof Layer || radixObject instanceof Segment || radixObject instanceof Module;
                                }

                            });
                        }
                    }
                });
                if (Utils.emptyOrNull(moduleName.get())) {
                    moduleName.set("<unknown>");
                }
                ((DefaultListModel) moduleDepList.getModel()).addElement(new RequiredModulesListItem(module.id, module.layerUri, moduleName.get(), moduleIcon.get()));
            }
        }

        moduleDepList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                removeModuleDepBtn.setEnabled(!readOnly && moduleDepList.getSelectedIndices().length > 0);
            }
        });
    }

    private List<Pair<String, Id>> getExceptedModules(Layer.License license) {
        List<Pair<String, Id>> result = new ArrayList<>();
        for (Layer.License.RequiredModule module : license.getRequiredModules()) {
            result.add(new Pair<>(module.layerUri, module.id));
        }

        return result;
    }

    private List<String> getIncludedLayers(Layer.License license) {
        final List<String> result = new ArrayList();

        Layer.HierarchyWalker.walk(license.getLayer(), new Layer.HierarchyWalker.Acceptor<Object>() {
            @Override
            public void accept(HierarchyWalker.Controller<Object> controller, Layer layer) {
                result.add(layer.getURI());
            }
        });

        return result;
    }

    static class RequiredModulesListItem extends Layer.License.RequiredModule {

        final String name;
        final Icon icon;

        public RequiredModulesListItem(Id id, String layerUri, String name, Icon icon) {
            super(id, layerUri);
            this.name = name;
            this.icon = icon;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private Map<String, Layer.License> getUnsavedLicensesFromParent() {
        Map<String, Layer.License> result = new HashMap<>();
        if (parent != null) {
            TreeModel licenseTreeModel = parent.getLicenseTree().getModel();
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) licenseTreeModel.getChild(licenseTreeModel.getRoot(), 0);

            final Layer.License layerLicenseFromParentEditor = parent.getLicense(node);            
            for (Entry<String, Layer.License> candidate : layerLicenseFromParentEditor.collectFullNameToLicenseMap().entrySet()) {
                if (!license.getLayer().getLicenses().collectFullNameToLicenseMap().containsKey(candidate.getKey())) {
                    result.put(candidate.getKey(), candidate.getValue());
                }
            }
        }

        return result;
    }
}
