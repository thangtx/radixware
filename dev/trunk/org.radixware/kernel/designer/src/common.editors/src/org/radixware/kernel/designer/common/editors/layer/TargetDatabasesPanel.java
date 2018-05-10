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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang.StringEscapeUtils;
import org.radixware.kernel.common.enums.EDatabaseType;
import org.radixware.kernel.common.enums.EOptionMode;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.LayerUtils;
import org.radixware.kernel.designer.common.dialogs.RadixWareDesignerIcon;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;

public class TargetDatabasesPanel extends JPanel {

    private final JTable table;
    private Layer layer;
    private boolean readOnly = false;
    final ListSelectionListener listSelectionListener;

    public TargetDatabasesPanel() {
        setLayout(new BorderLayout());
        table = new JTable();
        final JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
        final JButton addButton = new JButton("Add", RadixWareDesignerIcon.CREATE.ADD.getIcon());
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String type = EDatabaseType.ORACLE.getName();
                if (!Layer.ORG_RADIXWARE_LAYER_URI.equals(layer.getURI())) {
                    final List<String> availableTypes = getAvailableTypesToCreate();
                    if (availableTypes == null || availableTypes.isEmpty()) {
                        return;
                    }
                    final Object selectedType = JOptionPane.showInputDialog(null, "Type", "Select Type", JOptionPane.PLAIN_MESSAGE, null, availableTypes.toArray(), availableTypes.get(0));
                    if (selectedType == null) {
                        return;
                    }
                    type = selectedType.toString();
                }
                final Layer.TargetDatabase targetDb = editTargetDatabase(new Layer.TargetDatabase(layer, type, "", null, null));
                if (targetDb != null) {
                    getModel().addTargetDatabase(targetDb);
                    listSelectionListener.valueChanged(null);
                }
            }
        });
        final JButton removeButton = new JButton("Remove", RadixWareDesignerIcon.DELETE.DELETE.getIcon());
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (table.getSelectedRow() != -1 && JOptionPane.showConfirmDialog(null, "Remove selected target database?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION) {
                    getModel().removeTargetDatabase(table.getSelectedRow());
                }
            }
        });
        final JButton editButton = new JButton("Edit", RadixWareDesignerIcon.EDIT.EDIT.getIcon());
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (table.getSelectedRow() != -1) {
                    final Layer.TargetDatabase editedDb = editTargetDatabase(getSelectedDb());
                    if (editedDb != null) {
                        getModel().setTargetDatabase(editedDb, table.getSelectedRow());
                    }
                }
            }
        });
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listSelectionListener = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                addButton.setEnabled(!readOnly && canAdd());
                editButton.setEnabled(getSelectedDb() != null);
                removeButton.setEnabled(!readOnly && getSelectedDb() != null);
            }

            private boolean canAdd() {
                if (layer == null) {
                    return false;
                }
                if (layer.getURI().equals(Layer.ORG_RADIXWARE_LAYER_URI)) {
                    return true;
                } else {
                    return !getAvailableTypesToCreate().isEmpty();
                }
            }
        };
        listSelectionListener.valueChanged(null);
        table.getSelectionModel().addListSelectionListener(listSelectionListener);
        final JPanel buttonPanel = new JPanel(new MigLayout());
        addButton.setHorizontalAlignment(SwingConstants.LEFT);
        removeButton.setHorizontalAlignment(SwingConstants.LEFT);
        editButton.setHorizontalAlignment(SwingConstants.LEFT);
        buttonPanel.add(addButton, "growx, wrap");
        buttonPanel.add(removeButton, "growx, wrap");
        buttonPanel.add(editButton, "growx, wrap");
        add(buttonPanel, BorderLayout.EAST);
    }

    private List<String> getAvailableTypesToCreate() {
        if (layer == null) {
            return Collections.emptyList();
        }
        final List<String> availableTypes = LayerUtils.getAvailableBaseTargetDbTypes(layer);
        if (availableTypes == null) {
            return Collections.emptyList();
        }
        for (int i = availableTypes.size() - 1; i >= 0; i--) {
            boolean alreadyExists = false;
            for (Layer.TargetDatabase targetDb : layer.getTargetDatabases()) {
                if (targetDb.getType().equals(availableTypes.get(i))) {
                    alreadyExists = true;
                    break;
                }
            }
            if (alreadyExists) {
                availableTypes.remove(i);
            }
        }
        return availableTypes;
    }

    private TableModelImpl getModel() {
        return (TableModelImpl) table.getModel();
    }

    private Layer.TargetDatabase editTargetDatabase(final Layer.TargetDatabase targetDb) {
        final JPanel contentPane = new JPanel(new MigLayout("fill, nogrid"));

        contentPane.add(new JLabel("Database type:"));
        final JTextField tfType = new JTextField(targetDb.getType());
        contentPane.add(tfType, "growx, wrap");

        contentPane.add(new JLabel("Supported versions (comma-separeted list):"));
        final JTextField tfVersions = new JTextField(targetDb.getSupportedVersionsStr());
        contentPane.add(tfVersions, "growx, wrap");

        DefaultListModel model = new DefaultListModel();
        for (Layer.DatabaseOption opt : targetDb.getOptions()) {
            model.addElement(opt);
        }
        final JList optionsList = new JList(model);
        optionsList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                if (value instanceof Layer.DatabaseOption) {
                    final StringBuilder sb = new StringBuilder();
                    final Layer.DatabaseOption option = (Layer.DatabaseOption) value;
                    sb.append(option.getName());
                    if (option.getDescription() != null) {
                        sb.append("   [");
                        sb.append(option.getDescription());
                        sb.append("]");
                    }
                    if (option.getDependencies() != null && !option.getDependencies().isEmpty()) {
                        final List<String> depNames = new ArrayList<>();
                        for (Layer.DatabaseOptionDependency dep : option.getDependencies()) {
                            depNames.add(dep.getOptionName() + "=" + dep.getDefaultMode().getName());
                        }
                        sb.append(" [depends on ");
                        sb.append(listToString(depNames, 1));
                        sb.append("[");
                    }
                    value = sb.toString();
                }
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });
        optionsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        contentPane.add(new JLabel("Options"), "wrap");
        contentPane.add(new JScrollPane(optionsList), "grow");
        final JPanel optButtonsPanel = new JPanel(new MigLayout());
        final JButton addOptButton = new JButton("Add", RadixWareDesignerIcon.CREATE.ADD.getIcon());
        addOptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final Layer.DatabaseOption newOption = editOption(new Layer.DatabaseOption(layer, "", "", null, null, true, null));
                if (newOption != null) {
                    ((DefaultListModel) optionsList.getModel()).addElement(newOption);
                }
            }
        });
        final JButton removeOptButton = new JButton("Remove", RadixWareDesignerIcon.DELETE.DELETE.getIcon());

        removeOptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (optionsList.getSelectedIndex() != -1) {
                    ((DefaultListModel) optionsList.getModel()).remove(optionsList.getSelectedIndex());
                }
            }
        });
        final JButton editOptButton = new JButton("Edit", RadixWareDesignerIcon.EDIT.EDIT.getIcon());
        editOptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (optionsList.getSelectedIndex() != -1) {
                    final Layer.DatabaseOption opt = editOption((Layer.DatabaseOption) optionsList.getSelectedValue());
                    if (opt != null) {
                        ((DefaultListModel) optionsList.getModel()).setElementAt(opt, optionsList.getSelectedIndex());
                    }
                }
            }
        });
        addOptButton.setHorizontalAlignment(SwingConstants.LEFT);
        removeOptButton.setHorizontalAlignment(SwingConstants.LEFT);
        editOptButton.setHorizontalAlignment(SwingConstants.LEFT);
        optButtonsPanel.add(addOptButton, "growx, wrap");
        optButtonsPanel.add(removeOptButton, "growx, wrap");
        optButtonsPanel.add(editOptButton, "growx, wrap");

        final List<JCheckBox> ovrOpts = new ArrayList<>();

        if (Layer.ORG_RADIXWARE_LAYER_URI.equals(layer.getURI())) {
            contentPane.add(optButtonsPanel, "aligny top, wrap, pushy");
        } else {
            contentPane.add(optButtonsPanel, "aligny top, wrap");
            contentPane.add(new JLabel("Base options"), "wrap");
            final JPanel baseOptsPanel = new JPanel(new MigLayout());
            baseOptsPanel.add(new JLabel(getBoldHtml("Option")));
            baseOptsPanel.add(new JLabel(getBoldHtml("Override")), "alignx center");
            baseOptsPanel.add(new JLabel(getBoldHtml("Default value")), " alignx center");
            baseOptsPanel.add(new JLabel(getBoldHtml("Editable")), " alignx center");
            baseOptsPanel.add(new JLabel(getBoldHtml("Default value on upgrade")), " alignx center");
            baseOptsPanel.add(new JLabel(getBoldHtml("Editable on upgrade")), " alignx center, wrap");

            final List<Layer> baseLayers = new ArrayList<>();
            LayerUtils.collectBaseLayers(layer, baseLayers);
            final List<LayerUtils.DependencyInfo> baseOptions = LayerUtils.collectOptions(baseLayers, targetDb.getType(), null);
            Collections.sort(baseOptions, new Comparator<LayerUtils.DependencyInfo>() {
                @Override
                public int compare(LayerUtils.DependencyInfo o1, LayerUtils.DependencyInfo o2) {
                    return o1.getDependency().getOptionName().compareTo(o2.getDependency().getOptionName());
                }
            });
            if (baseOptions != null) {
                for (final LayerUtils.DependencyInfo baseOpt : baseOptions) {
                    final Layer.DatabaseOptionDependency baseOptDep = baseOpt.getDependency();

                    final String INHERIT_DEFAULT_MODE = "INHERIT[" + baseOptDep.getDefaultMode() + "]";
                    final String INHERIT_UPGRADE_MODE = "INHERIT[" + baseOptDep.getDefaultUpgradeMode() + "]";

                    baseOptsPanel.add(new JLabel(baseOptDep.getOptionName()));

                    final JComboBox cbDefaultMode = new JComboBox(getCheckBoxItemsByOptionModesList(baseOpt.getDefaultModeAvailableValues(), INHERIT_DEFAULT_MODE));
                    final JCheckBox cbEditable = new JCheckBox();
                    cbEditable.setSelected(baseOptDep.getEditable());
                    cbEditable.setEnabled(baseOptDep.getEditable());

                    final JComboBox cbDefaultUpgradeMode = new JComboBox(getCheckBoxItemsByOptionModesList(baseOpt.getDefaultUpgradeModeAvailableValues(), INHERIT_UPGRADE_MODE));
                    final JCheckBox cbEditableOnUpgrade = new JCheckBox();
                    cbEditableOnUpgrade.setSelected(baseOptDep.getEditableOnUpgrade());
                    cbEditableOnUpgrade.setEnabled(baseOptDep.getEditableOnUpgrade());

                    final JCheckBox cbOverride = new JCheckBox() {
                        @Override
                        public void setEnabled(boolean b) {
                            if (!b) {
                                cbDefaultMode.setEnabled(b);
                                cbEditable.setEnabled(b);
                                cbDefaultUpgradeMode.setEnabled(b);
                                cbEditableOnUpgrade.setEnabled(b);
                            }
                            super.setEnabled(b);
                        }
                    };
                    final OverrideDependencies depValues = new OverrideDependencies(null, null, null, null);
                    cbOverride.putClientProperty(OverrideDependencies.class, depValues);
                    cbOverride.putClientProperty(Layer.DatabaseOption.class, baseOptDep.getOptionName());

                    Layer.DatabaseOptionDependency overriding = null;
                    if (targetDb.getDependencies() != null) {
                        for (Layer.DatabaseOptionDependency dep : targetDb.getDependencies()) {
                            if (baseOptDep.getOptionName().equals(dep.getOptionName())) {
                                overriding = dep;
                                break;
                            }
                        }
                    }

                    final ItemListener defaultModeItemListener = new ItemListener() {
                        @Override
                        public void itemStateChanged(ItemEvent e) {
                            cbEditable.setEnabled(true);
                            if (((OverrideDependencies) cbOverride.getClientProperty(OverrideDependencies.class)).getEditable() != null) {
                                cbEditable.setSelected(((OverrideDependencies) cbOverride.getClientProperty(OverrideDependencies.class)).getEditable());
                            } else {
                                cbEditable.setSelected(baseOptDep.getEditable());
                            }
                            if (cbOverride.isSelected()) {
                                final Object selectedItem = cbDefaultMode.getSelectedItem();
                                if (cbDefaultMode.getSelectedItem() instanceof EOptionMode) {
                                    ((OverrideDependencies) cbOverride.getClientProperty(OverrideDependencies.class)).setDefaultMode((EOptionMode) selectedItem);
                                } else {
                                    cbEditable.setEnabled(false);
                                    cbEditable.setSelected(baseOptDep.getEditable());
                                    cbDefaultMode.setEnabled(baseOptDep.getEditable());
                                    ((OverrideDependencies) cbOverride.getClientProperty(OverrideDependencies.class)).setDefaultMode(null);
                                    ((OverrideDependencies) cbOverride.getClientProperty(OverrideDependencies.class)).setEditable(null);
                                }
                                if (cbDefaultMode.getItemCount() > baseOpt.getDefaultModeAvailableValues().size()) {
                                    cbDefaultMode.setModel(new DefaultComboBoxModel(getCheckBoxItemsByOptionModesList(baseOpt.getDefaultModeAvailableValues(), INHERIT_DEFAULT_MODE)));
                                    cbDefaultMode.setSelectedItem(selectedItem);
                                }
                            }
                        }
                    };

                    final ItemListener defaultUpgradeModeItemListener = new ItemListener() {
                        @Override
                        public void itemStateChanged(ItemEvent e) {
                            cbEditableOnUpgrade.setEnabled(true);
                            if (((OverrideDependencies) cbOverride.getClientProperty(OverrideDependencies.class)).getEditableOnUpgrade() != null) {
                                cbEditableOnUpgrade.setSelected(((OverrideDependencies) cbOverride.getClientProperty(OverrideDependencies.class)).getEditableOnUpgrade());
                            } else {
                                cbEditableOnUpgrade.setSelected(baseOptDep.getEditableOnUpgrade());
                            }
                            if (cbOverride.isSelected()) {
                                final Object selectedItem = cbDefaultUpgradeMode.getSelectedItem();
                                if (cbDefaultUpgradeMode.getSelectedItem() instanceof EOptionMode) {
                                    ((OverrideDependencies) cbOverride.getClientProperty(OverrideDependencies.class)).setDefaultUpgradeMode((EOptionMode) selectedItem);
                                } else {
                                    cbEditableOnUpgrade.setEnabled(false);
                                    cbEditableOnUpgrade.setSelected(baseOptDep.getEditableOnUpgrade());
                                    cbDefaultUpgradeMode.setEnabled(baseOptDep.getEditableOnUpgrade());
                                    ((OverrideDependencies) cbOverride.getClientProperty(OverrideDependencies.class)).setDefaultUpgradeMode(null);
                                    ((OverrideDependencies) cbOverride.getClientProperty(OverrideDependencies.class)).setEditableOnUpgrade(null);
                                }
                                if (cbDefaultUpgradeMode.getItemCount() > baseOpt.getDefaultUpgradeModeAvailableValues().size()) {
                                    cbDefaultUpgradeMode.setModel(new DefaultComboBoxModel(getCheckBoxItemsByOptionModesList(baseOpt.getDefaultUpgradeModeAvailableValues(), INHERIT_UPGRADE_MODE)));
                                    cbDefaultUpgradeMode.setSelectedItem(selectedItem);
                                }
                            }
                        }
                    };

                    final ActionListener editableSelectedListener = new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (baseOpt.getEditableAvailableValues().size() != 1) {
                                ((OverrideDependencies) cbOverride.getClientProperty(OverrideDependencies.class)).setEditable(cbEditable.isSelected());
                            } else {
                                ((OverrideDependencies) cbOverride.getClientProperty(OverrideDependencies.class)).setEditable(null);
                            }
                        }
                    };

                    final ActionListener editableOnUpgradeSelectedListener = new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (baseOpt.getEditableOnUpgradeAvailableValues().size() != 1) {
                                ((OverrideDependencies) cbOverride.getClientProperty(OverrideDependencies.class)).setEditableOnUpgrade(cbEditableOnUpgrade.isSelected());
                            } else {
                                ((OverrideDependencies) cbOverride.getClientProperty(OverrideDependencies.class)).setEditableOnUpgrade(null);
                            }
                        }
                    };

                    final ActionListener overrideSelectedListener = new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (cbOverride.isSelected()) {
                                cbDefaultMode.setEnabled(baseOptDep.getEditable());
                                cbEditable.setEnabled(baseOptDep.getEditable());
                                cbDefaultMode.setModel(new DefaultComboBoxModel<>(getCheckBoxItemsByOptionModesList(baseOpt.getDefaultModeAvailableValues(), INHERIT_DEFAULT_MODE)));

                                cbDefaultUpgradeMode.setEnabled(baseOptDep.getEditableOnUpgrade());
                                cbEditableOnUpgrade.setEnabled(baseOptDep.getEditableOnUpgrade());
                                cbDefaultUpgradeMode.setModel(new DefaultComboBoxModel<>(getCheckBoxItemsByOptionModesList(baseOpt.getDefaultUpgradeModeAvailableValues(), INHERIT_UPGRADE_MODE)));
                                if (((OverrideDependencies) cbOverride.getClientProperty(OverrideDependencies.class)).getDefaultMode() != null) {
                                    cbDefaultMode.setSelectedItem(((OverrideDependencies) cbOverride.getClientProperty(OverrideDependencies.class)).getDefaultMode());
                                } else {
                                    cbDefaultMode.setSelectedItem(INHERIT_DEFAULT_MODE);
                                }

                                if (((OverrideDependencies) cbOverride.getClientProperty(OverrideDependencies.class)).getDefaultUpgradeMode() != null) {
                                    cbDefaultUpgradeMode.setSelectedItem(((OverrideDependencies) cbOverride.getClientProperty(OverrideDependencies.class)).getDefaultUpgradeMode());
                                } else {
                                    cbDefaultUpgradeMode.setSelectedItem(INHERIT_UPGRADE_MODE);
                                }

                                if (((OverrideDependencies) cbOverride.getClientProperty(OverrideDependencies.class)).getEditable() != null) {
                                    cbEditable.setSelected(((OverrideDependencies) cbOverride.getClientProperty(OverrideDependencies.class)).getEditable());
                                } else {
                                    cbEditable.setSelected(baseOptDep.getEditable());
                                }

                                if (((OverrideDependencies) cbOverride.getClientProperty(OverrideDependencies.class)).getEditableOnUpgrade() != null) {
                                    cbEditableOnUpgrade.setSelected(((OverrideDependencies) cbOverride.getClientProperty(OverrideDependencies.class)).getEditableOnUpgrade());
                                } else {
                                    cbEditableOnUpgrade.setSelected(baseOptDep.getEditableOnUpgrade());
                                }
                                defaultModeItemListener.itemStateChanged(null);
                                defaultUpgradeModeItemListener.itemStateChanged(null);
                                editableSelectedListener.actionPerformed(null);
                                editableOnUpgradeSelectedListener.actionPerformed(null);
                            } else {
                                cbDefaultMode.setModel(new DefaultComboBoxModel(new Object[]{INHERIT_DEFAULT_MODE}));
                                cbDefaultMode.setEnabled(false);
                                cbDefaultUpgradeMode.setModel(new DefaultComboBoxModel(new Object[]{INHERIT_UPGRADE_MODE}));
                                cbDefaultUpgradeMode.setEnabled(false);
                                cbEditable.setSelected(baseOptDep.getEditable());
                                cbEditable.setEnabled(false);
                                cbEditableOnUpgrade.setSelected(baseOptDep.getEditableOnUpgrade());
                                cbEditableOnUpgrade.setEnabled(false);
                            }
                        }
                    };
                    cbDefaultMode.addItemListener(defaultModeItemListener);
                    cbDefaultUpgradeMode.addItemListener(defaultUpgradeModeItemListener);
                    cbEditable.addActionListener(editableSelectedListener);
                    cbEditableOnUpgrade.addActionListener(editableOnUpgradeSelectedListener);
                    cbOverride.addActionListener(overrideSelectedListener);

                    boolean isCorrectValue = true;
                    if (overriding != null) {
                        cbOverride.setSelected(true);
                        cbEditable.setSelected(overriding.getEditable() != null ? overriding.getEditable() : baseOptDep.getEditable());
                        cbEditableOnUpgrade.setSelected(overriding.getEditableOnUpgrade() != null ? overriding.getEditableOnUpgrade() : baseOptDep.getEditableOnUpgrade());

                        cbEditableOnUpgrade.setSelected(overriding.getEditableOnUpgrade() != null ? overriding.getEditableOnUpgrade() : baseOptDep.getEditableOnUpgrade());
                        if (!baseOpt.getDefaultModeAvailableValues().contains(overriding.getDefaultMode())) {
                            isCorrectValue = false;
                            cbDefaultMode.setEnabled(true);
                            final List<Object> tempModel = new ArrayList<>();
                            tempModel.add(getRedHtml(overriding.getDefaultMode().getName()));
                            tempModel.addAll(Arrays.asList(getCheckBoxItemsByOptionModesList(baseOpt.getDefaultModeAvailableValues(), INHERIT_DEFAULT_MODE)));
                            cbDefaultMode.setModel(new DefaultComboBoxModel(tempModel.toArray()));
                            cbDefaultMode.setSelectedItem(tempModel.get(0));
                        } else {
                            cbDefaultMode.setSelectedItem(overriding.getDefaultMode() != null ? overriding.getDefaultMode() : INHERIT_DEFAULT_MODE);
                        }

                        if (!baseOpt.getDefaultUpgradeModeAvailableValues().contains(overriding.getDefaultUpgradeMode())) {
                            isCorrectValue = false;
                            cbDefaultUpgradeMode.setEnabled(true);
                            final List<Object> tempModel = new ArrayList<>();
                            tempModel.add(getRedHtml(overriding.getDefaultUpgradeMode().getName()));
                            tempModel.addAll(Arrays.asList(getCheckBoxItemsByOptionModesList(baseOpt.getDefaultUpgradeModeAvailableValues(), INHERIT_UPGRADE_MODE)));
                            cbDefaultUpgradeMode.setModel(new DefaultComboBoxModel(tempModel.toArray()));
                            cbDefaultUpgradeMode.setSelectedItem(tempModel.get(0));
                        } else {
                            cbDefaultUpgradeMode.setSelectedItem(overriding.getDefaultUpgradeMode() != null ? overriding.getDefaultUpgradeMode() : INHERIT_UPGRADE_MODE);
                        }
                        if (isCorrectValue) {
                            if (cbDefaultMode.getSelectedItem() instanceof EOptionMode) {
                                ((OverrideDependencies) cbOverride.getClientProperty(OverrideDependencies.class)).setDefaultMode((EOptionMode) overriding.getDefaultMode());
                            }
                            if (cbDefaultUpgradeMode.getSelectedItem() instanceof EOptionMode) {
                                ((OverrideDependencies) cbOverride.getClientProperty(OverrideDependencies.class)).setDefaultUpgradeMode((EOptionMode) overriding.getDefaultUpgradeMode());
                            }
                            
                            ((OverrideDependencies) cbOverride.getClientProperty(OverrideDependencies.class)).setEditable(overriding.getEditable() != null ? overriding.getEditable() : baseOptDep.getEditable());
                            ((OverrideDependencies) cbOverride.getClientProperty(OverrideDependencies.class)).setEditableOnUpgrade(overriding.getEditableOnUpgrade() != null ? overriding.getEditableOnUpgrade() : baseOptDep.getEditableOnUpgrade());
                        }
                    } else {
                        cbOverride.setSelected(false);
                    }
                    
                    if (isCorrectValue) {
                        overrideSelectedListener.actionPerformed(null);
                    }

                    baseOptsPanel.add(cbOverride, "alignx center");
                    baseOptsPanel.add(cbDefaultMode, "alignx center, wmin 140");
                    baseOptsPanel.add(cbEditable, "alignx center");
                    baseOptsPanel.add(cbDefaultUpgradeMode, "alignx center, wmin 140");
                    baseOptsPanel.add(cbEditableOnUpgrade, "alignx center, wrap");
                    ovrOpts.add(cbOverride);
                }
            }

            for (Layer.DatabaseOptionDependency dep : LayerUtils.collectNonExistingDependencies(targetDb)) {
                final JLabel label = new JLabel(getRedHtml(dep.getOptionName()));
                baseOptsPanel.add(label);
                final JCheckBox cb = new JCheckBox();
                cb.setSelected(true);
                cb.putClientProperty(EOptionMode.class, dep.getDefaultMode());
                cb.putClientProperty(Layer.DatabaseOption.class, dep.getOptionName());
                baseOptsPanel.add(cb, "alignx center, wrap");
                cb.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (!cb.isSelected()) {
                            cb.setVisible(false);
                            label.setVisible(false);
                        }
                    }
                });
                ovrOpts.add(cb);
            }

            contentPane.add(new JScrollPane(baseOptsPanel), "grow, wrap, hmin 80, pushy");
        }

        final ModalDisplayer md = new ModalDisplayer(contentPane, "Target Database Parameters");
        final Runnable stateUpdater = new Runnable() {
            @Override
            public void run() {
                tfType.setEnabled(!readOnly && layer != null && Layer.ORG_RADIXWARE_LAYER_URI.equals(layer.getURI()));
                tfVersions.setEnabled(!readOnly);
                addOptButton.setEnabled(!readOnly);
                removeOptButton.setEnabled(!readOnly && optionsList.getSelectedIndex() != -1);
                editOptButton.setEnabled(optionsList.getSelectedIndex() != -1);
                md.getDialogDescriptor().setValid(!readOnly && !tfType.getText().isEmpty() && !tfVersions.getText().trim().isEmpty());
                for (JCheckBox cb : ovrOpts) {
                    cb.setEnabled(!readOnly);
                }
            }
        };
        final DocumentListener docListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                stateUpdater.run();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                stateUpdater.run();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        };
        tfType.getDocument().addDocumentListener(docListener);
        tfVersions.getDocument().addDocumentListener(docListener);
        optionsList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                stateUpdater.run();
            }
        });
        if (optionsList.getModel().getSize() > 0) {
            optionsList.setSelectedIndex(0);
        }
        stateUpdater.run();
        if (md.showModal()) {
            String versionsString = tfVersions.getText();
            final List<Layer.DatabaseOption> options = new ArrayList<>();
            for (int i = 0; i < optionsList.getModel().getSize(); i++) {
                options.add((Layer.DatabaseOption) optionsList.getModel().getElementAt(i));
            }
            final List<Layer.DatabaseOptionDependency> dependencies = new ArrayList<>();
            for (JCheckBox cb : ovrOpts) {
                if (cb.isSelected()) {
                    dependencies.add(
                            new Layer.DatabaseOptionDependency(
                                    (String) cb.getClientProperty(Layer.DatabaseOption.class),
                                    ((OverrideDependencies) cb.getClientProperty(OverrideDependencies.class)).getDefaultMode(),
                                    ((OverrideDependencies) cb.getClientProperty(OverrideDependencies.class)).getEditable(),
                                    ((OverrideDependencies) cb.getClientProperty(OverrideDependencies.class)).getDefaultUpgradeMode(),
                                    ((OverrideDependencies) cb.getClientProperty(OverrideDependencies.class)).getEditableOnUpgrade()
                            )
                    );
                }
            }
            return new Layer.TargetDatabase(layer, tfType.getText(), versionsString, options, dependencies);
        }
        return null;
    }

    private static Object[] getCheckBoxItemsByOptionModesList(List<EOptionMode> modes, Object insteadOfNull) {
        List<Object> result = new ArrayList<>();
        for (EOptionMode mode : modes) {
            result.add(mode == null ? insteadOfNull : mode);
        }
        return result.toArray();
    }

    private static String getRedHtml(final String text) {
        if (text == null) {
            return text;
        }
        return "<html><font color=\"red\">" + StringEscapeUtils.escapeHtml(text) + "</font></html>";
    }

    private static String getBoldHtml(final String text) {
        if (text == null) {
            return text;
        }
        return "<html><b>" + StringEscapeUtils.escapeHtml(text) + "</b></html>";
    }

    private Layer.DatabaseOption editOption(final Layer.DatabaseOption option) {
        final JPanel contentPane = new JPanel(new MigLayout("fill", "[shrink][grow]", "[][]push")) {
        };
        contentPane.add(new JLabel("Name:"));
        final JTextField tfName = new JTextField(option.getName());
        contentPane.add(tfName, "growx, wrap");
        tfName.setEditable(!readOnly);
        contentPane.add(new JLabel("Description:"));
        final JTextField tfDesc = new JTextField(option.getDescription());
        contentPane.add(tfDesc, "growx, wrap");
        tfDesc.setEditable(!readOnly);
        final JComboBox cbDefaultMode = new JComboBox<>(EOptionMode.values());
        if (option.getDefaultMode() != null) {
            cbDefaultMode.setSelectedItem(option.getDefaultMode());
        }
        contentPane.add(new JLabel("Default value:"));
        contentPane.add(cbDefaultMode, "growx, wrap");

        final JComboBox cbDefaultUpgradeMode = new JComboBox<>(EOptionMode.values());
        if (option.getDefaultMode() != null) {
            cbDefaultUpgradeMode.setSelectedItem(option.getDefaultUpgradeMode());
        }
        contentPane.add(new JLabel("Default value on upgrade:"));
        contentPane.add(cbDefaultUpgradeMode, "growx, wrap");

        final JCheckBox cbEditableOnUpgrade = new JCheckBox();
        if (option.getEditableOnUpgrade() != null) {
            cbEditableOnUpgrade.setSelected(option.getEditableOnUpgrade());
        }
        contentPane.add(new JLabel("Editable on upgrade:"));
        contentPane.add(cbEditableOnUpgrade, "growx, wrap");

        final ModalDisplayer md = new ModalDisplayer(contentPane);
        final DocumentListener listener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateState();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateState();
            }

            private void updateState() {
                md.getDialogDescriptor().setValid(!readOnly && !tfName.getText().trim().isEmpty());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        };
        tfDesc.getDocument().addDocumentListener(listener);
        tfName.getDocument().addDocumentListener(listener);
        contentPane.setPreferredSize(new Dimension(Math.max(contentPane.getPreferredSize().width, 400), contentPane.getMinimumSize().height));
        contentPane.setMinimumSize(contentPane.getPreferredSize());
        listener.insertUpdate(null);
        if (md.showModal()) {
            return new Layer.DatabaseOption(option.getLayer(), tfName.getText(), tfDesc.getText().isEmpty() ? null : tfDesc.getText(), (EOptionMode) cbDefaultMode.getSelectedItem(), (EOptionMode) cbDefaultUpgradeMode.getSelectedItem(), cbEditableOnUpgrade.isSelected(), null);
        } else {
            return null;
        }
    }

    public static Layer.DatabaseOptionDependency selectOptionDependency(final List<Layer> fromLayers, final Collection<Layer.DatabaseOptionDependency> alreadySelected, final String dbType) {
        final List<Layer.DatabaseOptionDependency> availableOptions = LayerUtils.collectBaseOptions(fromLayers, dbType);

        if (alreadySelected != null && availableOptions != null) {
            final Iterator<Layer.DatabaseOptionDependency> it = availableOptions.iterator();
            while (it.hasNext()) {
                final String optionName = it.next().getOptionName();
                for (Layer.DatabaseOptionDependency dep : alreadySelected) {
                    if (dep.getOptionName().equals(optionName)) {
                        it.remove();
                        break;
                    }
                }
            }
        }

        final JPanel contentPane = new JPanel(new MigLayout("fill", "[shrink][grow]", "")) {
        };

        contentPane.add(new JLabel("Option:"));
        final JComboBox cbOpt = new JComboBox(availableOptions.toArray());
        contentPane.add(cbOpt, "growx, wrap");
        contentPane.add(new JLabel("Value:"));
        final JComboBox cbOptVal = new JComboBox(EOptionMode.values());
        contentPane.add(cbOptVal, "growx, wrap, pushy");

        final ModalDisplayer md = new ModalDisplayer(contentPane, "Select Option");
        final ItemListener itemListener = new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                final Layer.DatabaseOptionDependency selectedOption = (Layer.DatabaseOptionDependency) cbOpt.getSelectedItem();
                boolean valid = selectedOption != null;
                if (selectedOption != null) {
                    for (Layer.DatabaseOptionDependency alreadySelectedOpt : alreadySelected) {
                        if (alreadySelectedOpt.getOptionName().equals(selectedOption.getOptionName())) {
                            valid = false;
                        }
                    }
                    cbOptVal.setEnabled(true);
                    cbOptVal.setSelectedItem(EOptionMode.ENABLED);
                }

                md.getDialogDescriptor().setValid(valid);
            }
        };
        cbOpt.addItemListener(itemListener);
        cbOpt.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                if (value instanceof Layer.DatabaseOptionDependency) {
                    value = ((Layer.DatabaseOptionDependency) value).getOptionName();
                }
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });
        if (cbOpt.getModel().getSize() == 0) {
            md.getDialogDescriptor().setValid(false);
        }
        itemListener.itemStateChanged(null);

        if (md.showModal()) {
            return new Layer.DatabaseOptionDependency(((Layer.DatabaseOptionDependency) cbOpt.getSelectedItem()).getOptionName(), (EOptionMode) cbOptVal.getSelectedItem());
        }
        return null;
    }

    private Layer.TargetDatabase getSelectedDb() {
        if (table.getSelectedRow() != -1) {
            return getModel().getTargetDbAt(table.getSelectedRow());
        }
        return null;
    }

    public void open(final Layer layer, final boolean readOnly) {
        table.setModel(new TableModelImpl(layer.getTargetDatabases()));
        this.layer = layer;
        this.readOnly = readOnly;
        if (getModel().getRowCount() > 0) {
            table.getSelectionModel().setSelectionInterval(0, 0);
        }
        listSelectionListener.valueChanged(null);
    }

    public void apply() {
        layer.setTargetDatabases(getModel().getTargetDatabases());
    }

    private static String getSupportedVersionsStr(Layer.TargetDatabase targetDb) {
        if (targetDb.getSupportedVersions() == null || targetDb.getSupportedVersions().isEmpty()) {
            return "<any>";
        }
        return targetDb.getSupportedVersionsStr();
    }

    private static class TableModelImpl extends AbstractTableModel {

        private final List<Layer.TargetDatabase> targetDatabases = new ArrayList<>();

        public TableModelImpl(final List<Layer.TargetDatabase> targetDatabases) {
            this.targetDatabases.addAll(targetDatabases);
        }

        @Override
        public int getRowCount() {
            return targetDatabases.size();
        }

        public Layer.TargetDatabase getTargetDbAt(final int row) {
            return targetDatabases.get(row);
        }

        public List<Layer.TargetDatabase> getTargetDatabases() {
            return targetDatabases;
        }

        public void setTargetDatabase(final Layer.TargetDatabase targetDb, final int row) {
            targetDatabases.set(row, targetDb);
            fireTableDataChanged();
        }

        public void addTargetDatabase(final Layer.TargetDatabase targetDb) {
            targetDatabases.add(targetDb);
            fireTableDataChanged();
        }

        public void removeTargetDatabase(final int row) {
            targetDatabases.remove(row);
            fireTableDataChanged();
        }

        @Override
        public int getColumnCount() {
            return 3;
        }

        @Override
        public String getColumnName(int column) {
            switch (column) {
                case 0:
                    return "Database Type";
                case 1:
                    return "Supported Versions";
                case 2:
                    return "Supported Options";
            }
            throw new IllegalArgumentException(String.valueOf(column));
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            final Layer.TargetDatabase targetDb = targetDatabases.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return targetDb.getType();
                case 1:
                    return getSupportedVersionsStr(targetDb);
                case 2:
                    return getOptionsAsString(targetDb);
            }
            throw new IllegalArgumentException("Illegal coordinates " + rowIndex + ":" + columnIndex);
        }

        private String getOptionsAsString(final Layer.TargetDatabase targetDb) {
            final List<String> optNames = new ArrayList<>();
            if (targetDb.getOptions() != null) {
                for (Layer.DatabaseOption opt : targetDb.getOptions()) {
                    optNames.add(opt.getName());
                }
            }
            return listToString(optNames, 2);
        }
    }

    private static String listToString(final List list, final int explicitMaxSize) {
        final StringBuilder sb = new StringBuilder();
        if (list != null) {
            if (list.size() > 0) {
                sb.append(list.get(0));
            }
            for (int i = 1; i < explicitMaxSize && i < list.size(); i++) {
                sb.append(", ");
                sb.append(list.get(i));
            }
            if (list.size() > explicitMaxSize) {
                sb.append(", and ");
                sb.append(list.size() - explicitMaxSize);
                sb.append(" more");
            }
        }
        return sb.toString();
    }

    private static class OverrideDependencies {

        EOptionMode defaultMode;
        Boolean editable;
        EOptionMode defaultUpgradeMode;
        Boolean editableOnUpgrade;

        public OverrideDependencies(EOptionMode defaultMode, Boolean editable, EOptionMode defaultUpgradeMode, Boolean editableOnUpgrade) {
            this.defaultMode = defaultMode;
            this.editable = editable;
            this.defaultUpgradeMode = defaultUpgradeMode;
            this.editableOnUpgrade = editableOnUpgrade;
        }

        public EOptionMode getDefaultMode() {
            return defaultMode;
        }

        public void setDefaultMode(EOptionMode defaultMode) {
            this.defaultMode = defaultMode;
        }

        public Boolean getEditable() {
            return editable;
        }

        public void setEditable(Boolean editable) {
            this.editable = editable;
        }

        public EOptionMode getDefaultUpgradeMode() {
            return defaultUpgradeMode;
        }

        public void setDefaultUpgradeMode(EOptionMode defaultUpgradeMode) {
            this.defaultUpgradeMode = defaultUpgradeMode;
        }

        public Boolean getEditableOnUpgrade() {
            return editableOnUpgrade;
        }

        public void setEditableOnUpgrade(Boolean editableOnUpgrade) {
            this.editableOnUpgrade = editableOnUpgrade;
        }
    }
}
