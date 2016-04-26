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

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.table.AbstractTableModel;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsObjectTitleFormatDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsObjectTitleFormatDef.TitleItem;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.utils.TitleItemFormatter;
import org.radixware.kernel.designer.common.dialogs.components.ValAsStrEditPanel;


class PreviewObjectTitleFormatTableModel extends AbstractTableModel {

    private final RadixObjects<AdsObjectTitleFormatDef.TitleItem> items;
    private final List<String> propertyTitles;
    private final List<Object> previewValues;
    private final List<String> previewValuesResults;
    private final Map<TitleItem, Object> titleItem2dataMap;
    private EIsoLanguage previewLanguageForMultilingualItems = EIsoLanguage.ENGLISH; //default

    public void setPreviewLanguageForMultilingualItems(EIsoLanguage previewLanguageForMultilingualItems) {
        this.previewLanguageForMultilingualItems = previewLanguageForMultilingualItems;

        for (int rowIndex = 0, rowsCount = getRowCount(); rowIndex < rowsCount; ++rowIndex) {
            final TitleItem titleItem = items.get(rowIndex);
            if (titleItem.isMultilingual()) {

                final String previewValueResult = TitleItemFormatter.format(titleItem.getPattern(previewLanguageForMultilingualItems), titleItem2dataMap.get(titleItem));
                previewValuesResults.set(rowIndex, previewValueResult);

                fireTableCellUpdated(rowIndex, COLUMN_VALUE);
                fireTableCellUpdated(rowIndex, COLUMN_RESULT);
            }
        }
    }

    private void setRowsData(int rowIndex, TitleItem titleItem) {

        final AdsPropertyDef adsPropertyDef = titleItem.findProperty();

        propertyTitles.set(rowIndex, adsPropertyDef == null ? "" : adsPropertyDef.getName());

        final EValType valType = adsPropertyDef == null ? null : adsPropertyDef.getValue().getType().getTypeId();
        Object data;

        if (valType == EValType.DATE_TIME) {
            data = new Timestamp(System.currentTimeMillis());
        } else if (valType == EValType.INT) {
            data = Long.valueOf(1234);
        } else if (valType == EValType.NUM) {
            data = new BigDecimal("0.1234");
        } else if (valType == EValType.BOOL) {
            data = Boolean.FALSE;
        } else if (valType == EValType.CHAR) {
            data = new Character('a');
        } else { //else use default for all the rest valTypes
            data = "Test";
        }

        setValueAt(data, rowIndex, COLUMN_VALUE);
    }

    public PreviewObjectTitleFormatTableModel(RadixObjects<AdsObjectTitleFormatDef.TitleItem> items) {
        super();

        this.items = items;
        int count = items.size();
        propertyTitles = new ArrayList<String>(count);
        previewValues = new ArrayList<Object>(count);
        previewValuesResults = new ArrayList<String>(count);
        titleItem2dataMap = new HashMap<TitleItem, Object>(count);

        for (int i = 0; i < count; ++i) {

            propertyTitles.add("");
            previewValues.add("");
            previewValuesResults.add("");

            setRowsData(i, items.get(i));
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnsNames[column];
    }

    @Override
    public int getColumnCount() {
        return columnsNames.length;
    }

    @Override
    public int getRowCount() {
        return items.size();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == COLUMN_VALUE;
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {

        if (columnIndex == COLUMN_VALUE) {

            if (value != null) {

                if (value instanceof ValAsStr) {

                    final ValAsStr valAsStr = (ValAsStr) value;

                    final EValType valType = ValAsStrEditPanel.getValTypeForArgument(items.get(rowIndex).findProperty().getValue().getType().getTypeId());
                    final Object data = valAsStr.toObject(valType);
                    /*if (data instanceof Date) {
                     final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd:MM:yyyy");
                     final String str = simpleDateFormat.format((Date) data);
                     previewValues.set(rowIndex, str);
                     } else {*/
                    previewValues.set(rowIndex, data);
                    //}

                    final TitleItem titleItem = items.get(rowIndex);

                    titleItem2dataMap.put(titleItem, data);

                    final String itemsPattern = titleItem.isMultilingual() ? titleItem.getPattern(previewLanguageForMultilingualItems) : titleItem.getPattern();

                    if (itemsPattern != null && data != null) {
                        final String previewValueResult = TitleItemFormatter.format(itemsPattern, data);
                        previewValuesResults.set(rowIndex, previewValueResult);
                    }
                } else {
                    final TitleItem titleItem = items.get(rowIndex);

                    previewValues.set(rowIndex, value);
                    final String itemsPattern = titleItem.isMultilingual() ? titleItem.getPattern(previewLanguageForMultilingualItems) : titleItem.getPattern();

                    if (itemsPattern != null && value != null) {
                        final String previewValueResult = TitleItemFormatter.format(itemsPattern, value);
                        previewValuesResults.set(rowIndex, previewValueResult);
                    } else {
                        previewValuesResults.set(rowIndex, "");
                    }
                }

            } else {
                previewValuesResults.set(rowIndex, "");
            }

            fireTableCellUpdated(rowIndex, COLUMN_VALUE);
            fireTableCellUpdated(rowIndex, COLUMN_RESULT);
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        if (columnIndex == COLUMN_TITLE) {
            return propertyTitles.get(rowIndex);
        } else if (columnIndex == COLUMN_VALUE) {
            return previewValues.get(rowIndex);
        } else {
            return previewValuesResults.get(rowIndex);
        }
    }

    public AdsObjectTitleFormatDef.TitleItem getTitleItem(int rowIndex) {
        return items.get(rowIndex);
    }
    public static final int COLUMN_TITLE = 0, COLUMN_VALUE = 1, COLUMN_RESULT = 2;
    private static final String columnsNames[] = new String[]{
        "Property", "Test Value", "Result"
    };
}
