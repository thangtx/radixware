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

package org.radixware.kernel.designer.common.dialogs.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.ParentRefType;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.types.Pid;
import org.radixware.kernel.designer.common.dialogs.components.valstreditor.ParentRefUtils;


public class PropListValEditorPanel extends JPanel {

    private boolean isReadOnly;
    private Prop2ValTable table;
    private final Prop2ValMapTableModel tableModel = new Prop2ValMapTableModel();
    private ValAsStrTableCellEditor valEditor;
    private ValItemCellRenderer valRenderer;
    private PropItemCellRenderer propRenderer;
    private Definition context;
    private List<Id> propIds;
    private Map<Id, ValAsStr> map = new HashMap<>();
    private boolean allMustBeNotNull = false;

    public PropListValEditorPanel() {
        BorderLayout layout = new BorderLayout();
        this.setLayout(layout);
        layout.setHgap(10);
        table = new Prop2ValTable();
        JScrollPane scroll = new JScrollPane(table);

        add(scroll, BorderLayout.CENTER);
        scroll.setPreferredSize(new Dimension(100, 100));

        tableModel.init();



        valEditor = new ValAsStrTableCellEditor(new ValAsStrTableCellEditor.ValAsStrProvider() {
            @Override
            public ValAsStr getValue(int row, int column) {
                Id id = getIdAtRow(row);
                if (id == null) {
                    return null;
                }
                return map.get(id);
            }

            @Override
            public DdsTableDef findTargetTable(int row, int column) {
                if (getValType(row, column) == EValType.PARENT_REF) {
                    if (context instanceof AdsClassDef) {
                        AdsPropertyDef prop = ((AdsClassDef) context).getProperties().findById(getIdAtRow(row), ExtendableDefinitions.EScope.ALL).get();
                        AdsType type = prop.getValue().getType().resolve(context).get();
                        if (type instanceof ParentRefType) {
                            AdsEntityObjectClassDef clazz = ((ParentRefType) type).getSource();
                            if (clazz != null) {
                                return clazz.findTable(context);
                            }
                        }
                    }
                }
                return null;
            }

            @Override
            public EValType getValType(int row, int column) {
                Id id = getIdAtRow(row);
                if (id == null) {
                    return null;
                }
                if (context instanceof AdsClassDef) {
                    AdsPropertyDef prop = ((AdsClassDef) context).getProperties().findById(id, ExtendableDefinitions.EScope.ALL).get();
                    if (prop != null && prop.getValue().getType() != null) {
                        return prop.getValue().getType().getTypeId();
                    } else {
                        return null;
                    }
                } else if (context instanceof DdsTableDef) {
                    DdsColumnDef tableColumn = ((DdsTableDef) context).getColumns().findById(id, ExtendableDefinitions.EScope.ALL).get();
                    if (tableColumn != null) {
                        return tableColumn.getValType();
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            }

            @Override
            public void setValue(int row, int column, ValAsStr val) {
                Id id = getIdAtRow(row);
                if (id == null) {
                    return;
                }
                map.put(id, val);
                onValueChanged(id, val);
            }
        });

        valRenderer = new ValItemCellRenderer();
        propRenderer = new PropItemCellRenderer();
        table.setRowHeight(28);
        table.setModel(tableModel);
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                onSelectionChanged();
            }
        });

    }

    public boolean isReadOnly() {
        return isReadOnly;
    }

    public void setReadOnly(boolean isReadOnly) {
        this.isReadOnly = isReadOnly;
    }

    public void open(AdsClassDef context, List<Id> propIds, Map<Id, ValAsStr> prop2ValMap) {
        openImpl(context, propIds, prop2ValMap);
        allMustBeNotNull = false;
    }

    public void open(DdsTableDef context, List<Id> propIds, Map<Id, ValAsStr> prop2ValMap) {
        openImpl(context, propIds, prop2ValMap);
        allMustBeNotNull = true;
    }

    private void openImpl(Definition context, List<Id> propIds, Map<Id, ValAsStr> prop2ValMap) {
        this.context = context;
        this.propIds = new ArrayList<>(propIds);
        //  valEditor.setContextObject(context);
        setPropValsById(prop2ValMap);

    }

    public void update() {
        int selection = table.getSelectedRow();
        valEditor.cancelCellEditing();
        tableModel.update();
        if (selection >= tableModel.getRowCount()) {
            selection = tableModel.getRowCount() - 1;
        }
        if (selection >= 0) {
            table.getSelectionModel().setSelectionInterval(selection, selection);
        }
        onSelectionChanged();
    }

    public Map<Id, ValAsStr> getPropValsById() {
        valEditor.stopCellEditing();
        return Collections.unmodifiableMap(map);
    }

    public boolean isComplete(String[] message) {
        if (!allMustBeNotNull) {
            return true;
        } else {
            valEditor.stopCellEditing();
            for (Id id : propIds) {
                if (map.get(id) == null) {
                    message[0] = "Value of property " + getPropDisplayName(id) + " have to be defined";
                    return false;
                }
            }
            return true;
        }
    }

    public void setPropValsById(Map<Id, ValAsStr> propId2Val) {
        this.map.clear();
        if (propId2Val != null) {
            this.map.putAll(propId2Val);
        }
        update();
    }

    private String getPropDisplayName(Id id) {
        if (context instanceof AdsClassDef) {
            AdsPropertyDef prop = ((AdsClassDef) context).getProperties().findById(id, ExtendableDefinitions.EScope.ALL).get();
            if (prop != null) {
                return prop.getName();
            }
        } else if (context instanceof DdsTableDef) {
            DdsColumnDef tableColumn = ((DdsTableDef) context).getColumns().findById(id, ExtendableDefinitions.EScope.ALL).get();
            if (tableColumn != null) {
                return tableColumn.getName();
            }
        } else {
        }
        return id.toString();
    }

    private class PropItemCellRenderer extends JLabel implements TableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setOpaque(true);
            if (value instanceof Id) {
                Id id = (Id) value;
                if (context instanceof AdsClassDef) {
                    AdsPropertyDef prop = ((AdsClassDef) context).getProperties().findById(id, ExtendableDefinitions.EScope.ALL).get();
                    if (prop == null) {
                        setText(id.toString());
                        setForeground(Color.red);
                    } else {
                        this.setIcon(prop.getIcon().getIcon(16, 16));
                        setText(prop.getName());
                        setForeground(isSelected ? Color.WHITE : Color.black);
                    }
                } else if (context instanceof DdsTableDef) {
                    DdsColumnDef tableColumn = ((DdsTableDef) context).getColumns().findById(id, ExtendableDefinitions.EScope.ALL).get();
                    if (tableColumn == null) {
                        setText(id.toString());
                        setForeground(Color.red);
                    } else {
                        this.setIcon(tableColumn.getIcon().getIcon(16, 16));
                        setText(tableColumn.getName());
                        setForeground(isSelected ? Color.WHITE : Color.black);
                    }
                } else {
                    setText("<ERROR!!!>");
                }
            } else {
                setText("<ERROR!!!>");
            }
            if (isSelected) {
                setBackground(table.getSelectionBackground());
            } else {
                setBackground(table.getBackground());
            }
            return this;
        }
    }

    private class ValItemCellRenderer extends JLabel implements TableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setOpaque(true);
            if (value instanceof Id) {
                Id id = (Id) value;
                ValAsStr mapItem = map.get(id);
                if (mapItem == null) {
                    setText("<null>");
                } else {
                    if (context instanceof AdsClassDef) {
                        AdsPropertyDef prop = ((AdsClassDef) context).getProperties().findById(id, ExtendableDefinitions.EScope.ALL).get();
                        if (prop == null) {
                            setText("<Unknown>");
                            setForeground(Color.red);
                        } else {
                            setText(valStr2DisplayString(mapItem, prop.getValue().getType().getTypeId(), prop));
                            setForeground(isSelected ? Color.WHITE : Color.black);
                        }
                    } else if (context instanceof DdsTableDef) {
                        DdsColumnDef tableColumn = ((DdsTableDef) context).getColumns().findById(id, ExtendableDefinitions.EScope.ALL).get();
                        if (tableColumn == null) {
                            setText("<Unknown>");
                            setForeground(Color.red);
                        } else {
                            setText(valStr2DisplayString(mapItem, tableColumn.getValType(), null));
                            setForeground(isSelected ? Color.WHITE : Color.black);
                        }
                    } else {
                        setText("<ERROR!!!>");
                    }
                }
            } else {
                setText("<ERROR!!!>");
            }
            if (isSelected) {
                setBackground(table.getSelectionBackground());
            } else {
                setBackground(table.getBackground());
            }
            return this;
        }

        private String valStr2DisplayString(ValAsStr valAsStr, EValType typeId, AdsPropertyDef prop) {
            if (typeId == EValType.PARENT_REF && prop != null) {
                AdsType type = prop.getValue().getType().resolve(context).get();
                if (type instanceof ParentRefType) {
                    AdsEntityObjectClassDef clazz = ((ParentRefType) type).getSource();
                    if (clazz != null) {
                        DdsTableDef table = clazz.findTable(context);
                        if (table != null) {
                            return ParentRefUtils.getPidDisplayName(table, (Pid) valAsStr.toObject(EValType.PARENT_REF));
                        }
                    }
                }
            }
            return String.valueOf(valAsStr.toObject(typeId));
        }
    }

    private class Prop2ValTable extends JTable {

        @Override
        public TableCellEditor getCellEditor(int row, int column) {
            if (column == 1) {
                return valEditor;
            } else {
                return null;
            }
        }

        @Override
        public TableCellRenderer getCellRenderer(int row, int column) {
            if (column == 0) {
                return propRenderer;
            } else {
                return valRenderer;
            }
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            if (column == 1) {
                return !isReadOnly;
            } else {
                return false;
            }
        }
    }

    protected void onSelectionChanged() {
    }

    protected void onValueChanged(Id id, ValAsStr val) {
    }

    public Id getSelectedId() {
        int index = table.getSelectedRow();
        return getIdAtRow(index);
    }

    private Id getIdAtRow(int index) {

        if (index >= 0 && index < tableModel.getRowCount()) {
            return (Id) tableModel.getValueAt(index, 0);
        } else {
            return null;
        }
    }

    private class Prop2ValMapTableModel extends DefaultTableModel {

        public void init() {
            addColumn("Property");
            addColumn("Value");
        }

        public void update() {
            while (getRowCount() > 0) {
                removeRow(0);
            }
            if (propIds != null) {
                for (Id id : propIds) {
                    addRow(new Object[]{id, id});
                }
            }
        }
    }
}
