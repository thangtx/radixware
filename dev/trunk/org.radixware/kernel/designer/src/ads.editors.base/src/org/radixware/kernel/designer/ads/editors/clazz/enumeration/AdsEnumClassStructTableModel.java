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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ads.AdsValAsStr.IValueController;
import org.radixware.kernel.common.defs.ads.clazz.AdsEnumClassDef;
import org.radixware.kernel.common.defs.ads.clazz.enumeration.AdsFieldParameterDef;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.designer.common.dialogs.components.AdvanceTableModel;
import org.radixware.kernel.designer.common.dialogs.components.AdvanceTableModel.ERowScope;


public final class AdsEnumClassStructTableModel extends AdvanceTableModel<AdsEnumClassDef, AdsFieldParameterDef> {

    private List<IColumnHandler> columnHandlers;
    private List<AdsFieldParameterDef> rowSources;

    protected AdsEnumClassStructTableModel(AdsEnumClassDef source) {
        super(source);

        initColumnHandlers();
    }

    protected void initColumnHandlers() {

        columnHandlers = new ArrayList<>();

        final AdsEnumClassStructTableModel model = this;

        String colName = NbBundle.getMessage(AdsEnumClassStructTableModel.class, "TableModel-ColumnName");
        columnHandlers.add(new ColumnHandlerAdapter(colName) {

            @Override
            public Object getCellValue(int row, int col, Object param) {
                return model.getRowSource(row).getName();
            }

            @Override
            public Class<?> getColumnClass(int col) {
                return String.class;
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                return model.getRowSourceScope(row) == ERowScope.LOCAL;
            }

            @Override
            public void setCellValue(Object value, int row, int col, Object param) {
            }
        });

        colName = NbBundle.getMessage(AdsEnumClassStructTableModel.class, "EnumClassStructTableModel-Type");
        columnHandlers.add(new ColumnHandlerAdapter(colName) {

            @Override
            public Object getCellValue(int row, int col, Object param) {
                return model.getRowSource(row).getValue().getType();
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                return model.getRowSourceScope(row) == ERowScope.LOCAL;
            }

            @Override
            public Class<?> getColumnClass(int col) {
                return AdsTypeDeclaration.class;
            }

            @Override
            public void setCellValue(Object value, int row, int col, Object param) {
                model.getRowSource(row).getValue().setType((AdsTypeDeclaration) value);
            }
        });

        colName = NbBundle.getMessage(AdsEnumClassStructTableModel.class, "EnumClassStructTableModel-InitValue");
        columnHandlers.add(new ColumnHandlerAdapter(colName) {

            @Override
            public IValueController getCellValue(int row, int col, Object param) {
                return model.getRowSource(row).getValue().getValueController();
            }

            @Override
            public Class<?> getColumnClass(int col) {
                return IValueController.class;
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                return model.getRowSourceScope(row) == ERowScope.LOCAL;
            }

            @Override
            public void setCellValue(Object value, int row, int col, Object param) {
            }
        });
    }

    public void removeParameters(int[] rows) {

        Arrays.sort(rows);
        for (int i = rows.length - 1; i >= 0; --i) {
            removeRow(rows[i]);
        }
    }

    @Override
    public void addRow(AdsFieldParameterDef item) {
        int index = getRowCount();
        getModelSource().getFieldStruct().getLocal().add(item);
        fireTableRowsInserted(index, index);
    }

    @Override
    public void removeRow(int row) {
        AdsFieldParameterDef param = getRowSource(row);
        if (param != null && getModelSource().getFieldStruct().getLocal().remove(param)) {
            fireTableRowsDeleted(row, row);
        }
    }

    @Override
    public void moveRowDown(int row) {
        if (validRowIndex(row)) {
            AdsFieldParameterDef param = getRowSource(row);
            getModelSource().getFieldStruct().getLocal().moveDown(getModelSource().getFieldStruct().getLocal().indexOf(param));
            fireTableRowsUpdated(row, row + 1);
        }
    }

    @Override
    public void moveRowUp(int row) {
        if (validRowIndex(row)) {
            AdsFieldParameterDef param = getRowSource(row);
            getModelSource().getFieldStruct().getLocal().moveUp(getModelSource().getFieldStruct().getLocal().indexOf(param));
            fireTableRowsUpdated(row - 1, row);
        }
    }

    @Override
    public ERowScope getRowSourceScope(AdsFieldParameterDef row) {
        if (row.getOwnerEnumClass() != getModelSource()) {
            return ERowScope.INHERIT;
        }
        return row.isOverwrite() ? ERowScope.OVERWRITE : ERowScope.LOCAL;
    }

    @Override
    public AdsFieldParameterDef getRowSource(int row) {
        List<AdsFieldParameterDef> rows = getModelSource().getFieldStruct().getOrdered();
        return rows.get(row);
    }

    @Override
    protected IColumnHandler getColumnHandler(Object key) {
        return columnHandlers.get((Integer) key);
    }

    @Override
    public int getRowCount() {
        if (getModelSource() != null) {
            return getModelSource().getFieldStruct().getFullSize();
        }
        return 0;
    }

    @Override
    public int getColumnCount() {
        return columnHandlers.size();
    }
}
