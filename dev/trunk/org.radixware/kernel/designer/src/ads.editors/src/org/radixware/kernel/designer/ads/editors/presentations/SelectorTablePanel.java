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
 * SelectorTablePanel.java
 *
 * Created on 23.06.2009, 15:25:01
 */
package org.radixware.kernel.designer.ads.editors.presentations;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.text.Collator;
import java.util.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumnModel;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.components.ExtendableTextField;
import org.radixware.kernel.common.components.ExtendableTextField.ExtendableTextChangeEvent;
import org.radixware.kernel.common.defs.*;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef.SelectorColumn;
import org.radixware.kernel.common.defs.ads.clazz.presentation.IAdsPresentableProperty;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinition;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.components.CommonParametersEditorCellLib.LabelWithInsetsRenderer;
import org.radixware.kernel.designer.common.dialogs.components.TunedTable;


public class SelectorTablePanel extends javax.swing.JPanel {

    private SelectorTableModel pModel;
    private TunedTable.TunedComboCellEditor visibilityEditor;
    private PropertyCellEditor propertyEditor;
    private TunedTable.TunedComboCellEditor alignEditor;
    private TunedTable.TunedComboCellEditor sizePolicyEditor;
    private TitleCellEditor titlesEditor;
    private EIsoLanguage[] languages;
    private boolean readonly = false;
    private Color defaultForeground;
    private Layer currentLayer;

    public SelectorTablePanel() {
        initComponents();
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                SelectorTablePanel.this.onFocusEvent();
            }
        });

        ActionListener addListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                SelectorTablePanel.this.onAddButtonPressed();
            }
        };
        addButton.addActionListener(addListener);
        ActionListener removeListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                SelectorTablePanel.this.onRemoveButtonPressed();
            }
        };
        removeButton.addActionListener(removeListener);
        ActionListener upListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                SelectorTablePanel.this.onUpButtonPressed();
            }
        };
        upButton.addActionListener(upListener);
        ActionListener downListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                SelectorTablePanel.this.onDownButtonPressed();
            }
        };
        downButton.addActionListener(downListener);

        ActionListener inheritListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!SelectorTablePanel.this.isUpdate) {
                    boolean isSelected = SelectorTablePanel.this.inheritSelectorCheck.isSelected();
                    SelectorTablePanel.this.presentation.setColumnsInherited(isSelected);
                    SelectorTablePanel.this.update();
                }
            }
        };
        inheritSelectorCheck.addActionListener(inheritListener);

        defaultForeground = inheritSelectorCheck.getForeground();
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(SelectorTablePanel.class, "SelectorTabs-Selector");
    }
    
    private void setupTableUI() {

        if (presentation != null) {
            currentLayer = presentation.getModule().getSegment().getLayer();
            languages = new EIsoLanguage[currentLayer.getLanguages().size()];
            currentLayer.getLanguages().toArray(languages);
            String[] titles = new String[3 + languages.length];
            titles[0] = NbBundle.getMessage(SelectorTablePanel.class, "SelectorVisibility");
            titles[1] = NbBundle.getMessage(SelectorTablePanel.class, "SelectorProperty");
            titles[2] = NbBundle.getMessage(SelectorTablePanel.class, "SelectorAlign");
            for (int i = 0, size = languages.length - 1; i <= size; i++) {
                titles[i + 3] = languages[i].getName() + " Title";
            }

            pModel = new SelectorTableModel(presentation);

            table.setModel(pModel);
            table.setRowSelectionAllowed(true);

            TableColumnModel columns = table.getColumnModel();
            JComboBox visibilityBox = new JComboBox(new SelectorColumnVisibilityModel());
            visibilityEditor = new TunedTable.TunedComboCellEditor(table, visibilityBox);
            columns.getColumn(pModel.VISIBILITY_COLUMN).setCellEditor(visibilityEditor);
            columns.getColumn(pModel.VISIBILITY_COLUMN).setCellRenderer(new CommonTableRenderer());

            propertyEditor = new PropertyCellEditor(table, new javax.swing.JComboBox());
            columns.getColumn(pModel.PROP_COLUMN).setCellEditor(propertyEditor);
            columns.getColumn(pModel.PROP_COLUMN).setCellRenderer(new TablePropertyRenderer());

            JComboBox alignBox = new javax.swing.JComboBox(new SelectorColumnAlignModel());
            alignEditor = new TunedTable.TunedComboCellEditor(table, alignBox);
            columns.getColumn(pModel.ALIGN_COLUMN).setCellEditor(alignEditor);
            columns.getColumn(pModel.ALIGN_COLUMN).setCellRenderer(new CommonTableRenderer());

            JComboBox sizePolicyBox = new javax.swing.JComboBox(new SelectorColumnSizePolicyModel());
            sizePolicyEditor = new TunedTable.TunedComboCellEditor(table, sizePolicyBox);
            columns.getColumn(pModel.SIZE_POLICY_COLUMN).setCellEditor(sizePolicyEditor);
            columns.getColumn(pModel.SIZE_POLICY_COLUMN).setCellRenderer(new CommonTableRenderer());

            
            
            titlesEditor = new TitleCellEditor();
            for (int i = 0, size = languages.length - 1; i <= size; i++) {
                columns.getColumn(i + pModel.SIZE_POLICY_COLUMN + 1).setCellEditor(titlesEditor);
                columns.getColumn(i + pModel.SIZE_POLICY_COLUMN + 1).setCellRenderer(new CommonTableRenderer());
            }

        }

    }
    private AdsSelectorPresentationDef presentation;

    public void open(final AdsSelectorPresentationDef presentation) {
        this.presentation = presentation;

        update();
    }
    private boolean isUpdate = false;

    public void update() {
        isUpdate = true;
        setupTableUI();

        this.readonly = presentation != null && (presentation.isReadOnly() || presentation.isColumnsInherited());
        visibilityEditor.setReadonly(readonly);
        alignEditor.setReadonly(readonly);

        table.setEnabled(!readonly);
        if (presentation != null) {
            inheritSelectorCheck.setSelected(presentation.isColumnsInherited());
            final boolean isInherited = presentation.isColumnsInherited();
            boolean erronious = isInherited
                    && (presentation.getBasePresentationId() == null
                    && presentation.getHierarchy().findOverwritten() == null);
            if (erronious) {
                inheritSelectorCheck.setForeground(Color.RED);
            } else {
                inheritSelectorCheck.setForeground(defaultForeground);
            }
            if (isInherited) {
                inheritSelectorCheck.setEnabled(!presentation.isReadOnly());
            } else {
                inheritSelectorCheck.setEnabled(!presentation.isReadOnly() && !erronious);
            }
        }

        onFocusEvent();
        isUpdate = false;
    }

    public void onFocusEvent() {
        if (pModel != null) {
            addButton.setEnabled(!readonly);
            if (pModel.getRowCount() > 0) {
                if (table.getSelectedRow() != -1) {
                    removeButton.setEnabled(!readonly);
                    upButton.setEnabled(!readonly && (table.getSelectedRow() > 0) && table.getSelectedRowCount() == 1);
                    downButton.setEnabled(!readonly && (table.getSelectedRow() < (pModel.getRowCount() - 1)) && table.getSelectedRowCount() == 1);
                } else {
                    removeButton.setEnabled(false);
                    upButton.setEnabled(false);
                    downButton.setEnabled(false);
                }
            } else {
                removeButton.setEnabled(false);
                upButton.setEnabled(false);
                downButton.setEnabled(false);
            }
        } else {
            addButton.setEnabled(false);
            removeButton.setEnabled(false);
            upButton.setEnabled(false);
            downButton.setEnabled(false);
        }
    }

    private List<PropertyItem> calculateAvailableProps() {
        List<AdsPropertyDef> props = presentation.getOwnerClass().getProperties().get(EScope.ALL);
        PropertyFilter filter = new PropertyFilter();
        List<PropertyItem> result = new ArrayList<>();
        for (AdsPropertyDef p : props) {
            if (filter.isTarget(p)) {
                result.add(new PropertyItem(p));
            }
        }
        Collections.sort(result);
        return result;
    }

    private void onAddButtonPressed() {
        List<PropertyItem> items = calculateAvailableProps();
        List<AdsPropertyDef> props = new LinkedList<>();
        for (PropertyItem i : items) {
            props.add(i.property);
        }
        ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(props);
        cfg.setTypeTitle(NbBundle.getMessage(SelectorTablePanel.class, "SelectorTable-ChooseTitle"));
        List<Definition> defs = ChooseDefinition.chooseDefinitions(cfg);
        if (defs != null && defs.size() > 0) {
            int selectedIndex = table.getSelectedRow();
            int start = selectedIndex + 1;
            if (selectedIndex == pModel.getRowCount()) {
                for (Definition d : defs) {
                    pModel.addRow((AdsPropertyDef) d);
                }
                table.getSelectionModel().setSelectionInterval(start, start);
            } else {
                for (Definition d : defs) {
                    pModel.addRow(start, (AdsPropertyDef) d);
                    start++;
                }
                table.getSelectionModel().setSelectionInterval(selectedIndex + 1, start - 1);
            }
        }
    }

    private void onRemoveButtonPressed() {
        int[] selection = table.getSelectedRows();
        if (selection != null && selection.length > 0) {
            TableCellEditor cellEditor = table.getCellEditor();
            if (cellEditor != null) {
                cellEditor.stopCellEditing();
            }

            int selectionSize = selection.length;
            int oldSize = table.getRowCount();
            int firstSelected = selection[0];
            int lastSelected = selection[selectionSize - 1];

            while (selectionSize > 0 && firstSelected < table.getRowCount()) {
                pModel.removeRow(firstSelected);
                selectionSize--;
            }

            final int rowsCount = table.getRowCount();
            if (rowsCount > 0) {
                if (lastSelected == oldSize) {
                    table.setRowSelectionInterval(rowsCount - 1, rowsCount - 1);
                } else {
                    if (firstSelected == 0) {
                        table.setRowSelectionInterval(0, 0);
                    } else {
                        table.setRowSelectionInterval(firstSelected - 1, firstSelected - 1);
                    }
                }
            }
        }
        onFocusEvent();
    }

    private void onUpButtonPressed() {
        moveRow(true);
    }

    private void onDownButtonPressed() {
        moveRow(false);
    }

    private void moveRow(boolean up) {
        final int row = table.getSelectedRow();
        TableCellEditor editor = table.getCellEditor();
        if (editor != null) {
            editor.stopCellEditing();
        }

        RadixObjects<AdsSelectorPresentationDef.SelectorColumn> columns = presentation.getColumns();
        if (row > -1
                && row < columns.size()
                && row < pModel.getRowCount()) {
            int n_row = pModel.moveRow(row, up);
            table.requestFocusInWindow();
            table.getSelectionModel().setSelectionInterval(n_row, n_row);
        }
    }

    private void onAlignBoxChange() {
        if (!isUpdate) {
            Object selected = alignEditor.getCellEditorValue();
            pModel.changeAlign(table.getSelectedRow(), selected.toString());
        }
    }

    private void onVisibilityBoxChange() {
        if (!isUpdate) {
            Object selected = visibilityEditor.getCellEditorValue();
            pModel.changeVisibility(table.getSelectedRow(), selected.toString());
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        inheritSelectorCheck = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new org.radixware.kernel.designer.common.dialogs.components.TunedTable();
        addButton = new javax.swing.JButton();
        removeButton = new javax.swing.JButton();
        upButton = new javax.swing.JButton();
        downButton = new javax.swing.JButton();

        setAutoscrolls(true);

        inheritSelectorCheck.setText(org.openide.util.NbBundle.getMessage(SelectorTablePanel.class, "InheritSelector")); // NOI18N

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(table);

        addButton.setIcon(RadixWareIcons.CREATE.ADD.getIcon(13, 13));
        addButton.setText(org.openide.util.NbBundle.getMessage(SelectorTablePanel.class, "SelectorTable-Add")); // NOI18N
        addButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        removeButton.setIcon(RadixWareIcons.DELETE.DELETE.getIcon(13, 13));
        removeButton.setText(org.openide.util.NbBundle.getMessage(SelectorTablePanel.class, "SelectorTable-Remove")); // NOI18N
        removeButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        upButton.setIcon(RadixWareIcons.ARROW.MOVE_UP.getIcon(13, 13));
        upButton.setText(org.openide.util.NbBundle.getMessage(SelectorTablePanel.class, "SelectorTable-Up")); // NOI18N
        upButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        downButton.setIcon(RadixWareIcons.ARROW.MOVE_DOWN.getIcon(13, 13));
        downButton.setText(org.openide.util.NbBundle.getMessage(SelectorTablePanel.class, "SelectorTable-Down")); // NOI18N
        downButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(inheritSelectorCheck)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(downButton)
                            .addComponent(removeButton)
                            .addComponent(addButton)
                            .addComponent(upButton))))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {addButton, downButton, removeButton, upButton});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inheritSelectorCheck)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(addButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(removeButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(upButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(downButton))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JButton downButton;
    private javax.swing.JCheckBox inheritSelectorCheck;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton removeButton;
    private org.radixware.kernel.designer.common.dialogs.components.TunedTable table;
    private javax.swing.JButton upButton;
    // End of variables declaration//GEN-END:variables

    private class PropertyItem implements Comparable<PropertyItem> {

        AdsPropertyDef property;

        PropertyItem(AdsPropertyDef property) {
            this.property = property;
        }

        @Override
        public int compareTo(PropertyItem o) {
            int res = Collator.getInstance().compare(property.getName(), o.property.getName());
            return res;
        }
    }

    private class PropertyRenderer extends DefaultListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            int iSize = ((javax.swing.JComboBox) propertyEditor.getComponent()).isPopupVisible() ? 16 : 13;
            if (value != null
                    && value instanceof AdsPropertyDef) {
                AdsPropertyDef asProperty = (AdsPropertyDef) value;
                setText(asProperty.getName());
                setIcon(asProperty.getIcon() != null ? asProperty.getIcon().getIcon(iSize, iSize) : null);
            } else {
                setIcon(RadixObjectIcon.UNKNOWN.getIcon(iSize, iSize));
                setText("<Not Defined>");
            }
            return this;
        }
    }

    private class CommonTableRenderer extends LabelWithInsetsRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            c.setEnabled(!readonly);
            return c;
        }
    }

    private class TablePropertyRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            int iSize = 13;
            if (value != null
                    && value instanceof SelectorColumn) {
                SelectorColumn asColumn = (SelectorColumn) value;
                AdsPropertyDef asProperty = asColumn.findProperty();
                if (asProperty != null) {
                    setText(asProperty.getName());
                    setIcon(asProperty.getIcon() != null ? asProperty.getIcon().getIcon(iSize, iSize) : null);
                } else {
                    setText(((SelectorColumn) value).getPropertyId().toString());
                    setIcon(RadixObjectIcon.UNKNOWN.getIcon(iSize, iSize));
                }
            } else {
                setIcon(RadixObjectIcon.UNKNOWN.getIcon(iSize, iSize));
                setText("<Not Defined>");
            }
            setEnabled(!readonly);
            return this;
        }
    }

    private class TitleCellEditor extends AbstractCellEditor
            implements TableCellEditor {

        private ExtendableTextField editor = new ExtendableTextField() {

            @Override
            protected void onMouseClicked(MouseEvent e) {
                //do nothing - canceling "clear text" button action
            }
        };
        private javax.swing.JButton button;
        private int row;
        private int col;
        private String val;
        private String originalVal;

        public TitleCellEditor() {
            button = editor.addButton(RadixWareIcons.DELETE.CLEAR.getIcon());
            button.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    val = null;
                    editor.setValue("");
                    button.setEnabled(false);
                }
            });
            editor.getChangeSupport().addEventListener(new ExtendableTextField.ExtendableTextChangeListener() {

                @Override
                public void onEvent(ExtendableTextChangeEvent e) {
                    String newval = (String) e.getCurrentTextValue();
                    if (!(val == null && newval.isEmpty())) {
                        val = newval;
                    }
                    button.setEnabled(val != null);
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
                int r, int c) {
            button.setEnabled(value != null);
            this.val = value != null ? value.toString() : null;//value != null ? value.toString() : "";
            this.originalVal = val;
            this.row = r;
            this.col = c;
            editor.setValue(value != null ? value.toString() : "");
            return editor;
        }

        @Override
        public boolean stopCellEditing() {
            if (row > -1 && row < pModel.getRowCount()) {
                if (val != null && (originalVal == null || !originalVal.equals(val))
                        || val == null) {
                    pModel.setValueAt(val, row, col);
                }
                fireEditingStopped();
            }
            return true;
        }

        @Override
        public void cancelCellEditing() {
            if (originalVal == null || !originalVal.equals(val)) {
                if (row > -1 && row < pModel.getRowCount()) {
                    editor.setValue(originalVal);
                    pModel.setValueAt(originalVal, row, col);
                }
                fireEditingCanceled();
            }
            super.cancelCellEditing();
        }

        @Override
        public Object getCellEditorValue() {
            return val;
        }

        @Override
        public boolean isCellEditable(EventObject e) {
            return !SelectorTablePanel.this.readonly;
        }
    }

    private class PropertyCellEditor extends TunedTable.TunedComboCellEditor {

        private javax.swing.JComboBox box;
        private int row;
        private Object value;
        private AdsPropertyDef currentProperty;

        public PropertyCellEditor(JTable table, final javax.swing.JComboBox box) {
            super(table, box);
            this.box = box;
            this.box.setRenderer(new PropertyRenderer());
            ActionListener boxListener = new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    currentProperty = (AdsPropertyDef) box.getSelectedItem();
                    stopCellEditing();
                }
            };
            box.addActionListener(boxListener);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            List<PropertyItem> items = calculateAvailableProps();
            List<AdsPropertyDef> props = new LinkedList<>();
            for (PropertyItem i : items) {
                props.add(i.property);
            }
            this.box.setModel(new DefaultComboBoxModel(props.toArray()));

            this.row = row;
            this.value = value;
            if (value != null && value instanceof SelectorColumn) {
                AdsPropertyDef prop = ((SelectorColumn) value).findProperty();
                if (prop != null) {
                    box.setSelectedItem(prop);
                    currentProperty = prop;
                } else {
                    currentProperty = null;
                }
            }
            return box;
        }

        @Override
        public boolean stopCellEditing() {
            if (row > -1 && row < pModel.getRowCount()) {
                if (value != null && value instanceof SelectorColumn
                        && currentProperty != null) {
                    AdsSelectorPresentationDef.SelectorColumn newColumn = AdsSelectorPresentationDef.SelectorColumn.Factory.newInstance((IAdsPresentableProperty) currentProperty);
                    newColumn.setAlign(((SelectorColumn) value).getAlign());
                    newColumn.setVisibility(((SelectorColumn) value).getVisibility());
                    newColumn.setTitleId(((SelectorColumn) value).getTitleId());
                    pModel.setValueAt(newColumn, row, 1);
                    table.removeEditor();
                }
            }
            return true;
        }

        @Override
        public Object getCellEditorValue() {
            return value;
        }

        @Override
        public boolean isCellEditable(EventObject e) {
            return !SelectorTablePanel.this.readonly;
        }
    }

    private class PropertyFilter implements IFilter {

        @Override
        public boolean isTarget(RadixObject radixObject) {
            if (radixObject instanceof AdsPropertyDef) {
                final AdsPropertyDef property = (AdsPropertyDef) radixObject;

                // exclude EVENT_CODE property
                switch (property.getNature()) {
                    case EVENT_CODE:
                        return false;
                    default:
                }

                EValType typeId = property.getValue().getType().getTypeId();
                return property.getUsageEnvironment().equals(ERuntimeEnvironmentType.SERVER)
                        && !(typeId.equals(EValType.USER_CLASS));
            }
            return false;
        }
    }
}
