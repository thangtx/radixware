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
import javax.swing.JLabel;
import javax.swing.JTable;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsEnumClassFieldDef;
import org.radixware.kernel.designer.common.dialogs.components.AdvanceTable;
import org.radixware.kernel.designer.common.dialogs.components.AdvanceTable.ICellPainter;
import org.radixware.kernel.designer.common.dialogs.components.AdvanceTableModel;


final class AdsEnumClassFieldValuesTable extends AdvanceTable<AdsEnumClassFieldValuesModel> {

    private final class ValueEditor extends AdvanceCellEditor {

        private FieldValueEditorComponent editor;

        public ValueEditor() {

            editor = new FieldValueEditorComponent();
        }

        @Override
        public Object getCellEditorValue() {
            return null;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (isSelected) {
                editor.open((AdsEnumClassFieldDef.FieldValueController) value);
                return editor;
            }
            return null;
        }
    }

    public AdsEnumClassFieldValuesTable() {
        super();
        
        setDefaultRenderer(AdsEnumClassFieldDef.FieldValueController.class, new DefaultColorCellRender() {

            @Override
            public void setValue(Object value) {

                if (value == null) {
                    setText("");
                } else {
                    setText(((AdsEnumClassFieldDef.FieldValueController) value).getValuePresentation());
                }
            }
        });

        setDefaultEditor(AdsEnumClassFieldDef.FieldValueController.class, new ValueEditor());
    }

    @Override
    public ICellPainter getCellPainter() {
        return new EnumClassCellPainter() {

            @Override
            public void paint(Component component, AdvanceTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
                if (getModel().getModelSource().isOverwrite()) {
                    super.paint(component, table, value, isSelected, hasFocus, row, col);
                }

                if (component instanceof JLabel) {
                    final JLabel lbl = (JLabel) component;
                    if (convertColumnIndexToModel(col) == AdsEnumClassFieldValuesModel.Columns.VALUE
                        && getModel().getModelSource().containsLocal(getModel().getRowSource(row).getId())) {

                        String html = lbl.getText().replaceAll("<", "&lt;").replaceAll(">", "&gt;");
                        lbl.setText("<html><b>" + html);
                    }
                }
            }
        };
    }

    @Override
    public void open(AdsEnumClassFieldValuesModel model) {
        super.open(model);

        setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        getColumnModel().getColumn(0).setPreferredWidth(100);
        getColumnModel().getColumn(1).setPreferredWidth(100);
        getColumnModel().getColumn(2).setPreferredWidth(600);
    }

    @Override
    protected AdvanceTableModel getDefauldModel() {
        return new AdsEnumClassFieldValuesModel(null, null);
    }
}
