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

package org.radixware.kernel.designer.ads.editors.clazz.enumeration;

import java.awt.Component;
import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import org.radixware.kernel.common.defs.ads.AdsValAsStr.IValueController;
import org.radixware.kernel.common.defs.ads.clazz.enumeration.AdsFieldParameterDef;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.designer.common.dialogs.components.AdvanceTable;
import org.radixware.kernel.designer.common.dialogs.components.AdvanceTableModel;
import org.radixware.kernel.designer.common.dialogs.components.TypeEditorComponent.TypeContext;
import org.radixware.kernel.designer.common.dialogs.components.values.NameEditorComponent;
import org.radixware.kernel.designer.common.dialogs.components.values.NameEditorComponent.NamedContext;


public final class AdsEnumClassStructureTable extends AdvanceTable<AdsEnumClassStructTableModel> {

    private final class InitialValueEditor extends AbstractCellEditor implements TableCellEditor {

        private InitialValueEditorComponent editor;

        public InitialValueEditor() {
            super();

            editor = new InitialValueEditorComponent();

        }

        @Override
        public Object getCellEditorValue() {
            return null;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

            if (isSelected) {
                editor.open((IValueController) value);
                return editor;
            }
            return null;
        }
    }

    public AdsEnumClassStructureTable() {
        super();

        setDefaultRenderer(AdsTypeDeclaration.class, new DefaultColorCellRender() {

            @Override
            public void setValue(Object value) {
                setText(((AdsTypeDeclaration) value).getName(getModel().getModelSource()));
            }
        });

        setDefaultRenderer(IValueController.class, new DefaultColorCellRender() {

            @Override
            public void setValue(Object value) {

                if (value == null) {
                    setText("");
                } else {
                    setText(((IValueController) value).getValuePresentation());
                }
            }
        });

        setDefaultEditor(String.class, new StringEditor());
        setDefaultEditor(AdsTypeDeclaration.class, new TypeEditor() {

            @Override
            protected TypeContext getTypeContext(JTable table, Object value, int row, int column) {
                final AdsFieldParameterDef rowSource = ((AdvanceTable<AdsEnumClassStructTableModel>)table).getModel().getRowSource(row);
                return new TypeContext(rowSource.getValue(), rowSource);
            }

            @Override
            protected AdsTypeDeclaration getCurrentValue(JTable table, Object value, int row, int column) {
                final AdsFieldParameterDef rowSource = ((AdvanceTable<AdsEnumClassStructTableModel>)table).getModel().getRowSource(row);
                return rowSource.getValue().getType();
            }
        });

        setDefaultEditor(IValueController.class, new InitialValueEditor());
    }

    @Override
    public void open(AdsEnumClassStructTableModel model) {
        super.open(model);

        getColumnModel().getColumn(0).setCellEditor(new NameEditor() {

            @Override
            protected NamedContext getNamedContext(JTable table, Object value, int row, int column) {
                return new NameEditorComponent.RadixObjectNamedContext(getModel().getRowSource(convertRowIndexToModel(row)));
            }

        });
        setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        getColumnModel().getColumn(0).setPreferredWidth(100);
        getColumnModel().getColumn(1).setPreferredWidth(100);
        getColumnModel().getColumn(2).setPreferredWidth(600);
    }

    @Override
    public ICellPainter getCellPainter() {

        return new EnumClassCellPainter() {

            @Override
            public void paint(Component component, AdvanceTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (getModel().getModelSource().isOverwrite()) {
                    super.paint(component, table, value, isSelected, hasFocus, row, column);
                }
            }
        };
    }

    @Override
    protected AdvanceTableModel getDefauldModel() {
        return new AdsEnumClassStructTableModel(null);
    }
}
