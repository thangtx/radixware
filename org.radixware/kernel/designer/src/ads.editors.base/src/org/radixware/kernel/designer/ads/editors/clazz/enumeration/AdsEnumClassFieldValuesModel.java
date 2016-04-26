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
import java.util.List;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ads.clazz.AdsEnumClassDef;
import org.radixware.kernel.common.defs.ads.clazz.enumeration.AdsFieldParameterDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsEnumClassFieldDef;
import org.radixware.kernel.designer.common.dialogs.components.AdvanceTableModel;
import org.radixware.kernel.designer.common.dialogs.components.AdvanceTableModel.ERowScope;


final class AdsEnumClassFieldValuesModel extends AdvanceTableModel<AdsEnumClassFieldDef, AdsFieldParameterDef> {

    public interface Columns {

        final int NAME = 0;
        final int TYPE = 1;
        final int VALUE = 2;
    }

    private final List<IColumnHandler> columnHandlers = new ArrayList<>();
    private AdsEnumClassDef def;

    public AdsEnumClassFieldValuesModel(AdsEnumClassDef def, AdsEnumClassFieldDef modelSource) {
        super(modelSource);

        this.def = def;
        initColumnHandlers();
    }

    protected void initColumnHandlers() {


        String colName = NbBundle.getMessage(AdsEnumClassFieldValuesModel.class, "TableModel-ColumnName");
        columnHandlers.add(new ColumnHandlerAdapter(colName) {

            @Override
            public Object getCellValue(int row, int col, Object param) {
                return getRowSource(row).getName();
            }

            @Override
            public Class<?> getColumnClass(int col) {
                return String.class;
            }
        });

        colName = NbBundle.getMessage(AdsEnumClassFieldValuesModel.class, "EnumClassStructTableModel-Type");
        columnHandlers.add(new ColumnHandlerAdapter(colName) {

            @Override
            public Object getCellValue(int row, int col, Object param) {
                AdsFieldParameterDef p = getRowSource(row);
                return p.getValue().getType().getName(p);
            }

            @Override
            public Class<?> getColumnClass(int col) {
                return String.class;
            }
        });

        colName = NbBundle.getMessage(AdsEnumClassStructTableModel.class, "TableModel-ColumnValue");
        columnHandlers.add(new ColumnHandlerAdapter(colName) {

            @Override
            public Object getCellValue(int row, int col, Object param) {
                return getModelSource().getValueController(getRowSource(row));
            }

            @Override
            public Class<?> getColumnClass(int col) {
                return AdsEnumClassFieldDef.FieldValueController.class;
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                if (getModelSource().isOverwrite()) {
                    return getModelSource().getOwnerEnumClass().getFieldStruct().getLocal().contains(getRowSource(row));
                }
                return true;
            }

            @Override
            public void setCellValue(Object value, int row, int col, Object param) {
            }
        });
    }

    @Override
    public ERowScope getRowSourceScope(AdsFieldParameterDef row) {
        if (def.getFieldStruct().getLocal().contains(row)) {
            return ERowScope.LOCAL;
        }
        return ERowScope.INHERIT;
    }

    @Override
    public int getRowCount() {
        if (def == null) {
            return 0;
        }
        return def.getFieldStruct().getFullSize();
    }

    @Override
    public AdsFieldParameterDef getRowSource(int row) {
        if (def == null) {
            return null;
        }
        return def.getFieldStruct().getOrdered().get(row);
    }

    @Override
    protected IColumnHandler getColumnHandler(Object key) {
        return columnHandlers.get((Integer) key);
    }

    @Override
    public void updateModel() {
    }

    @Override
    public int getColumnCount() {
        return columnHandlers.size();
    }
}
