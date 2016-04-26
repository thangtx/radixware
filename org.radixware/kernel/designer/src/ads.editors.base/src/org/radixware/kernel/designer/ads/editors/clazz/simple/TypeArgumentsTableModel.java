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

package org.radixware.kernel.designer.ads.editors.clazz.simple;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration.TypeArgument;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration.TypeArguments;
import org.radixware.kernel.designer.common.dialogs.components.CommonParametersEditorCellLib;
import org.radixware.kernel.designer.common.dialogs.components.CommonParametersEditorCellLib.TypePresentation;


class TypeArgumentsTableModel extends AbstractTableModel {

    private List<CommonParametersEditorCellLib.TypePresentation> typePresentationList = null;
    private List<CommonParametersEditorCellLib.StringCellValue> namePresentationList = null;
    private boolean toShowType;
    private int count;
    //private AdsClassDef context;

    public TypeArgumentsTableModel(AdsClassDef context, AdsClassDef argumentsOwner) {
        super();
        //this.context = context;
        toShowType = false;

        final List<TypeArgument> typeArgumentsList = argumentsOwner.getTypeArguments().getArgumentList();
        count = typeArgumentsList.size();

        if (count > 0) {

            typePresentationList = new ArrayList<CommonParametersEditorCellLib.TypePresentation>(count);
            namePresentationList = new ArrayList<CommonParametersEditorCellLib.StringCellValue>(count);

            for (int i = 0; i < count; ++i) {
                final TypeArgument typeArgument = typeArgumentsList.get(i);
                typePresentationList.add(new CommonParametersEditorCellLib.TypePresentation(typeArgument, context));
                namePresentationList.add(new CommonParametersEditorCellLib.StringCellValue(typeArgument.getName()));
            }
        }
    }

    public void setShowType(boolean show) {
        if (toShowType != show) {
            toShowType = show;
            fireTableDataChanged();
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == nameColumnIndex) {
            return namePresentationList.get(rowIndex);
        } else {
            if (toShowType) {
                return typePresentationList.get(rowIndex);
            } else {
                return "";
            }
        }
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        assert (columnIndex == typeColumnIndex && toShowType);
        assert (value instanceof CommonParametersEditorCellLib.TypePresentation);

        final TypePresentation newTypePresentation = (TypePresentation) value;
        typePresentationList.set(rowIndex, newTypePresentation);

        fireTableCellUpdated(rowIndex, typeColumnIndex);
        fireTableCellUpdated(rowIndex, nameColumnIndex);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == nameColumnIndex) {
            return CommonParametersEditorCellLib.StringCellValue.class;
        } else {
            if (toShowType) {
                return CommonParametersEditorCellLib.TypePresentation.class;
            } else {
                return String.class;
            }
        }
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnIndex == nameColumnIndex
                ? nameColumn
                : typeColumn;
    }

    @Override
    public int getColumnCount() {
        return COLUMNS_COUNT;
    }

    @Override
    public int getRowCount() {
        return count;
    }

    public boolean isComplete() {
        if (!toShowType) {
            return true;
        }

        for (int i = 0, rowCount = getRowCount(); i < rowCount; ++i) {
            if (((TypePresentation) getValueAt(i, typeColumnIndex)).getType() == null) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == typeColumnIndex && toShowType;
    }

    public TypeArguments getCreatedTypeArgs() {
        final TypeArguments result = TypeArguments.Factory.newInstance(null);
        for (int i = 0; i < count; ++i) {
            final TypePresentation typePresentation = typePresentationList.get(i);
            result.add(TypeArgument.Factory.newInstance(typePresentation.getType()));
        }
        return result;
    }
    private static final int COLUMNS_COUNT = 2;
    public static final int nameColumnIndex = 0;
    public static final int typeColumnIndex = 1;
    public static final String nameColumn = "Name";
    public static final String typeColumn = "Type";
}
