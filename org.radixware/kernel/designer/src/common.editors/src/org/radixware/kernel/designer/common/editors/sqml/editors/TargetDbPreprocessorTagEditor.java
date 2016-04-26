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

package org.radixware.kernel.designer.common.editors.sqml.editors;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import net.miginfocom.swing.MigLayout;
import org.radixware.kernel.common.enums.EComparisonOperator;
import org.radixware.kernel.common.enums.EOptionMode;
import org.radixware.kernel.common.repository.DbOptionValue;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.LayerUtils;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.sqml.tags.TargetDbPreprocessorTag;
import org.radixware.kernel.designer.common.dialogs.RadixWareDesignerIcon;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlEditor;
import org.radixware.kernel.designer.common.editors.layer.TargetDatabasesPanel;


public class TargetDbPreprocessorTagEditor extends SqmlTagEditor<TargetDbPreprocessorTag> {

    public static enum EMode {

        EDIT_TAG,
        EDIT_CONFIG
    }
    private static final String TITLE_KEY = "target-db-preprocessor-tag-editor-title";
    private static final String CHECK_VERSION_STR = "Check version";
    private static final String SET_VERSION_STR = "Set version";
    private final JComboBox comboDbType;
    private final JCheckBox cbCheckVer;
    private final JComboBox comboTargetVer;
    private final JComboBox comboVerOperator;
    private final JCheckBox cbCheckOpts;
    private final JTable optionsTable;
    private final JButton btAddOpt;
    private final JButton btRemoveOpt;
    private EMode mode = EMode.EDIT_TAG;

    public TargetDbPreprocessorTagEditor() {
        setLayout(new MigLayout("fill", "[][grow]", ""));
        add(new JLabel("Database type:"));
        comboDbType = new JComboBox();
        add(comboDbType, "growx, wrap");
        final ListCellRenderer defaultRenderer = comboDbType.getRenderer();
        comboDbType.setRenderer(new ListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                if (value instanceof Layer.TargetDatabase) {
                    value = ((Layer.TargetDatabase) value).getType();
                }
                return defaultRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });
        cbCheckVer = new JCheckBox();
        add(cbCheckVer, "wrap");
        add(new JLabel("Target version"));
        comboVerOperator = new JComboBox(EComparisonOperator.values());
        add(comboVerOperator, "growx, split");
        comboTargetVer = new JComboBox();

        add(comboTargetVer, "wrap");
        cbCheckOpts = new JCheckBox("Check options");
        add(cbCheckOpts, "wrap");
        final JPanel optsPanel = new JPanel(new BorderLayout());
        final JToolBar optsToolbar = new JToolBar();
        optsToolbar.setFloatable(false);
        btAddOpt = new JButton(RadixWareDesignerIcon.CREATE.ADD.getIcon());
        optsToolbar.add(btAddOpt);
        btRemoveOpt = new JButton(RadixWareDesignerIcon.DELETE.DELETE.getIcon());
        optsToolbar.add(btRemoveOpt);
        optsPanel.add(optsToolbar, BorderLayout.NORTH);
        optionsTable = new JTable();
        optionsTable.setModel(new OptsModel());
        final JScrollPane scrollPane = new JScrollPane(optionsTable);
        optsPanel.add(scrollPane, BorderLayout.CENTER);
        add(optsPanel, "span, grow, wrap");

        cbCheckVer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateState();
            }
        });

        cbCheckOpts.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateState();
            }
        });

        btAddOpt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final List<Layer> baseLayers = new ArrayList<>();
                LayerUtils.collectBaseLayers(getLayer(), baseLayers);
                baseLayers.add(0, getLayer());
                final Layer.DatabaseOptionDependency dep = TargetDatabasesPanel.selectOptionDependency(baseLayers, getOptionsModel().getOptions(), ((Layer.TargetDatabase) comboDbType.getSelectedItem()).getType());
                if (dep != null) {
                    getOptionsModel().addOption(dep);
                }
            }
        });

        btRemoveOpt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getOptionsModel().removeOption(optionsTable.getSelectedRow());
            }
        });

        optionsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                updateState();
            }
        });
        
        final JComboBox cbMode = new JComboBox(EOptionMode.values());
        optionsTable.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(cbMode));

        comboDbType.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                anotherTargetDbSelected();
            }
        });
    }

    @Override
    protected String getTitle() {
        if (mode == EMode.EDIT_CONFIG) {
            return "Preprocessor parameters";
        }
        return super.getTitle();
    }

    private Layer getLayer() {
        ScmlEditor editor = getOpenInfo().getLookup().lookup(ScmlEditor.class);
        if (editor.getSource().getLayer() != null) {
            return editor.getSource().getLayer();
        }
        return ((Sqml) editor.getSource()).getEnvironment().getContext().getLayer();
    }

    private void updateState() {
        final boolean readOnly = getOpenInfo().isReadOnly() && mode == EMode.EDIT_TAG;
        comboDbType.setEnabled(!readOnly);
        cbCheckVer.setEnabled(getOpenInfo() != null && !readOnly && comboTargetVer.getItemCount() > 0);
        comboVerOperator.setVisible(mode == EMode.EDIT_TAG);
        if (mode == EMode.EDIT_CONFIG) {
            cbCheckOpts.setVisible(false);
            cbCheckOpts.setSelected(true);
            cbCheckVer.setVisible(false);
            cbCheckVer.setSelected(true);
        }
        cbCheckVer.setText(mode == EMode.EDIT_TAG ? CHECK_VERSION_STR : SET_VERSION_STR);
        final boolean editVerEnabled = cbCheckVer.isEnabled() && cbCheckVer.isSelected();
        comboVerOperator.setEnabled(editVerEnabled);
        comboTargetVer.setEnabled(editVerEnabled);
        cbCheckOpts.setEnabled(!readOnly);
        final boolean editOptsEnabled = getOpenInfo() != null && !readOnly && cbCheckOpts.isSelected();
        optionsTable.setEnabled(editOptsEnabled);
        btAddOpt.setEnabled(editOptsEnabled);
        btRemoveOpt.setEnabled(editOptsEnabled && optionsTable.getSelectedRow() >= 0);
        updateOkState();
    }

    private void anotherTargetDbSelected() {
        getOptionsModel().clear();
        comboTargetVer.setModel(new DefaultComboBoxModel(getSupportedVersions().toArray()));
        if (comboTargetVer.getItemCount() == 0) {
            cbCheckVer.setSelected(false);
        }
        try {
            if (getTag().getDbTypeName() != null && getTag().getDbVersion() != null && getTag().getDbTypeName().equals(getSelectedTargetDb() == null ? null : getSelectedTargetDb().getType())) {
                comboTargetVer.setSelectedItem(new BigDecimal(getTag().getDbVersion()));
            }
        } catch (RuntimeException ex) {
            //ignore
        }
        updateState();
    }

    private Layer.TargetDatabase getSelectedTargetDb() {
        if (comboDbType.getSelectedItem() instanceof Layer.TargetDatabase) {
            return (Layer.TargetDatabase) comboDbType.getSelectedItem();
        }
        return null;
    }

    private List<BigDecimal> getSupportedVersions() {
        if (getSelectedTargetDb() != null) {
            return getSelectedTargetDb().getSupportedVersions();
        }
        return Collections.emptyList();
    }

    @Override
    protected String getTitleKey() {
        return TITLE_KEY;
    }

    @Override
    protected boolean tagDefined() {
        return comboDbType.getSelectedItem() != null && !(getOpenInfo() == null && getOpenInfo().isReadOnly());
    }

    @Override
    protected void applyChanges() {
        getTag().setDbTypeName(getSelectedTargetDb().getType());
        getTag().setCheckVersion(cbCheckVer.isSelected());
        getTag().setDbVersion(comboTargetVer.getSelectedItem().toString());
        getTag().setVersionOperator((EComparisonOperator) comboVerOperator.getSelectedItem());
        getTag().setCheckOptions(cbCheckOpts.isSelected());
        getTag().setOptions(getOptionsModel().getOptionValues());
    }

    private OptsModel getOptionsModel() {
        return (OptsModel) optionsTable.getModel();
    }

    @Override
    protected void afterOpen() {
        final EMode requiredMode = getOpenInfo().getLookup().lookup(EMode.class);
        if (requiredMode != null) {
            mode = requiredMode;
        }
        final Layer.TargetDatabase[] targetDatabasesArray = getLayer().getTargetDatabases().toArray(new Layer.TargetDatabase[] {});
        comboDbType.setModel(new DefaultComboBoxModel<>(targetDatabasesArray));
        if (getTag().getDbTypeName() != null) {
            for (Layer.TargetDatabase db : targetDatabasesArray) {
                if (db.getType().equals(getTag().getDbTypeName())) {
                    comboDbType.setSelectedItem(db);
                    break;
                }
            }
        }
        cbCheckVer.setSelected(getTag().isCheckVersion());
        cbCheckOpts.setSelected(getTag().isCheckOptions());
        if (getTag().getVersionOperator() != null) {
            comboVerOperator.setSelectedItem(getTag().getVersionOperator());
        }
        anotherTargetDbSelected();
        if (getTag().getDbOptions() != null) {
            getOptionsModel().setOptions(valsToDeps(getTag().getDbOptions()));
        }
    }

    @Override
    protected void setReadOnly(boolean readOnly) {
        updateState();
    }
    
    private static List<DbOptionValue> depsToValues(final List<Layer.DatabaseOptionDependency> deps) {
        if (deps == null) {
            return null;
        }
        final List<DbOptionValue> vals = new ArrayList<>();
        for (Layer.DatabaseOptionDependency dep : deps) {
            vals.add(new DbOptionValue(dep.getOptionName(), dep.getDefaultMode()));
        }
        return vals;
    }
    
    private static List<Layer.DatabaseOptionDependency> valsToDeps(final List<DbOptionValue> vals) {
        if (vals == null) {
            return null;
        }
        final List<Layer.DatabaseOptionDependency> res = new ArrayList<>();
        for (DbOptionValue val : vals) {
            vals.add(new DbOptionValue(val.getOptionName(), val.getMode()));
        }
        return res;
    }

    private class OptsModel extends AbstractTableModel {

        private final List<Layer.DatabaseOptionDependency> options = new ArrayList<>();

        public void addOption(final Layer.DatabaseOptionDependency option) {
            options.add(option);
            fireTableDataChanged();
        }

        public List<Layer.DatabaseOptionDependency> getOptions() {
            return options;
        }
        
        public List<DbOptionValue> getOptionValues() {
            return depsToValues(getOptions());
        }

        public void clear() {
            options.clear();
            fireTableDataChanged();
        }

        public void removeOption(final int idx) {
            options.remove(idx);
            fireTableDataChanged();
        }

        public void setOptions(final List<Layer.DatabaseOptionDependency> options) {
            this.options.clear();
            this.options.addAll(options);
            fireTableDataChanged();
        }

        @Override
        public int getRowCount() {
            return options.size();
        }

        @Override
        public int getColumnCount() {
            if (mode == EMode.EDIT_CONFIG) {
                return 1;
            }
            return 2;
        }

        @Override
        public String getColumnName(int column) {
            switch (column) {
                case 0:
                    return "Option";
                case 1:
                    return "Value";
            }
            throw new IllegalArgumentException(String.valueOf(column));
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return options.get(rowIndex).getOptionName();
                case 1:
                    return options.get(rowIndex).getDefaultMode();

            }
            throw new IllegalArgumentException(rowIndex + ":" + columnIndex);
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex == 1;
        }
        
        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            if (columnIndex == 1 && aValue instanceof EOptionMode) {
                options.set(rowIndex, new Layer.DatabaseOptionDependency(options.get(rowIndex).getOptionName(), (EOptionMode) aValue));
            }
            super.setValueAt(aValue, rowIndex, columnIndex);
        }
        
    }
}
