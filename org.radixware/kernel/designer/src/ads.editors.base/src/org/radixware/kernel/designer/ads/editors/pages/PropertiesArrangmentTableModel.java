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

package org.radixware.kernel.designer.ads.editors.pages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.table.AbstractTableModel;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPageDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.PropertyUsageSupport;
import org.radixware.kernel.common.defs.ads.clazz.presentation.PropertyUsageSupport.PropertyRef;
import org.radixware.kernel.common.types.Id;


public class PropertiesArrangmentTableModel extends AbstractTableModel {

    private AdsEditorPageDef editorPage;
    private int columnsCount = 1;
    public final static Integer MAX_COLUMNS_COUNT = 10;
    private int rowCount = 0;

    public PropertiesArrangmentTableModel(/*int colsCount, */AdsEditorPageDef editorPage) {
        this.editorPage = editorPage;
        setProperties(editorPage.getUsedProperties());
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return PropertyRef.class;
    }

    public int getColumnIndexByProperty(Object obj) {
        if (obj instanceof Id) {
            AdsEditorPageDef.PagePropertyRef ref = editorPage.getProperties().findByPropId((Id) obj);
            if (ref != null) {
                return ref.getColumn();
            }
        }
        return -1;
    }

    public int getRowIndexByProperty(Object obj) {
        if (obj instanceof Id) {
            AdsEditorPageDef.PagePropertyRef ref = editorPage.getProperties().findByPropId((Id) obj);
            if (ref != null) {
                int check = editorPage.getProperties().getRowCount();
                return ref.getRow();
            }
        }
        return -1;
    }

    public AdsDefinition getSelectedProperty(int rowIndex, int columnIndex) {
        final Object value = getValueAt(rowIndex, columnIndex);
        if (value != null) {
            assert (value instanceof PropertyRef);
            return ((PropertyRef) value).findProperty();
        } else {
            return null;
        }
    }

    @Override
    public int getRowCount() {
        return this.rowCount;
    }

    @Override
    public int getColumnCount() {
        return this.columnsCount;
    }

    public void addColumn() {
        if (this.columnsCount < MAX_COLUMNS_COUNT) {
            this.columnsCount++;
            content.put(content.size(), new HashMap<Integer, PropertyRef>());
            fireTableStructureChanged();
        }
    }

    public void removeColumn(int index) {
        if (index > -1 && index <= this.columnsCount) {
            Map<Integer, PropertyRef> column = content.get(index);
            if (column != null) {
                for (int i = 0, size = column.size() - 1; i <= size; i++) {
                    PropertyRef propref = column.get(i);
                    editorPage.getProperties().removeByPropId(propref.getPropertyId());
                }
                column.clear();
                this.columnsCount--;

            }
            for (int i = index + 1, size = content.size() - 1; i <= size; i++) {
                Map<Integer, PropertyRef> nextcolumn = content.get(i);
                if (nextcolumn != null) {
                    for (int n = 0, nextsize = nextcolumn.size() - 1; n <= nextsize; n++) {
                        PropertyRef nextref = nextcolumn.get(n);
                        AdsEditorPageDef.PagePropertyRef nextpageref = editorPage.getProperties().findByPropId(nextref.getPropertyId());
                        if (nextpageref != null) {
                            nextpageref.setColumn(i - 1);
                        }
                    }
                    content.put(i - 1, nextcolumn);
                    content.remove(i);
                }
            }

            content.remove(index);

            fireTableStructureChanged();
        }
    }

    @Override
    public String getColumnName(int column) {
        if (column > -1 && column < content.size()) {
            return Integer.toString(column + 1);
        }
        return "";
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Map<Integer, PropertyRef> column = content.get(columnIndex);
        Object obj = column != null ? column.get(rowIndex) : null;
        if (obj != null) {
            return obj;
        }
        return null;
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        //do nothing
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }
    private Map<Integer, Map<Integer, PropertyRef>> content;

    public void setProperties(PropertyUsageSupport propertyList) {
        Map<Integer, Map<Integer, PropertyRef>> contentClone = new HashMap<Integer, Map<Integer, PropertyRef>>();
        List<Id> contentIds = propertyList.getIds();

        for (Id idx : contentIds) {
            AdsEditorPageDef.PagePropertyRef ref = editorPage.getProperties().findByPropId(idx);
            if (ref != null) {
                int column = ref.getColumn();
                if (!contentClone.containsKey(column)) {
                    contentClone.put(column, new HashMap<Integer, PropertyRef>());
                }
                contentClone.get(column).put(ref.getRow(), editorPage.getUsedProperties().getReference(idx));
            }
        }

        content = new HashMap<Integer, Map<Integer, PropertyRef>>();
        if (contentClone.size() > 0) {
            int lastColumn = (Integer) contentClone.keySet().toArray()[contentClone.size() - 1];
            int i = 0;
            while (i <= lastColumn) {
                Map<Integer, PropertyRef> col = contentClone.get(i);
                if (col != null) {
                    content.put(i, col);
                } else {
                    content.put(i, new HashMap<Integer, PropertyRef>());
                }
                i++;
            }
        }

        this.columnsCount = content.size();
        this.rowCount = calculateRowCount();

        fireTableDataChanged();
    }

    private int calculateRowCount() {
        int rows = 0;
        for (int i = 0, size = content.size() - 1; i <= size; i++) {
            Map<Integer, PropertyRef> column = content.get(i);

            if (column.size() > 0) {
                int max = getMaxRowNumberInColumn(column);

                if (max >= rows) {
                    rows = max + 1;
                }
            }
        }
        return rows;
    }

    public void removeItem(Object item) {
        if (item instanceof Id) {
            removeIdItem((Id) item);
        }
    }

    private void removeIdItem(Id idx) {
        AdsEditorPageDef.PagePropertyRef ref = editorPage.getProperties().findByPropId(idx);
        if (ref != null) {
            Map<Integer, PropertyRef> column = content.get(ref.getColumn());
            if (column != null) {
                final int row = ref.getRow();
                PropertyRef propRef = column.get(row);
                if (propRef != null) {
                    column.remove(row);
                }
                for (int r = row + 1, size = column.size(); r <= size; r++) {
                    Id rId = column.get(r).getPropertyId();
                    AdsEditorPageDef.PagePropertyRef rRef = editorPage.getProperties().findByPropId(rId);
                    if (rRef != null) {
                        rRef.setRow(r - 1);
                        PropertyRef removed = column.remove(r);
                        column.put(r - 1, removed);
                    }
                }
            }
            editorPage.getProperties().removeByPropId(idx);
            rowCount = calculateRowCount();
            fireTableDataChanged();
        }
    }

    public void addItem(int sRow, int sCol, Object[] items) {

        if (sCol < 0) {
            sCol = 0;
        }
        if (!content.containsKey(sCol)) {
            addColumn();
        }

        if (sRow == -1) {
            sRow = getRowCountInColumn(sCol) - 1;
        }

        Map<Integer, PropertyRef> column = content.get(sCol);

        int start = sRow;

        if (column.isEmpty()) {
            start = 0;
        } else if (column.get(start) != null) {
            start++;
        }

        int max = getMaxRowNumberInColumn(column);

        if (start <= max && column.get(start) != null) {
            for (int c = max; c >= start; c--) {
                if (column.get(c) != null) {
                    AdsEditorPageDef.PagePropertyRef rref = editorPage.getProperties().findByPropId(column.get(c).getPropertyId());
                    if (rref != null) {
                        PropertyRef removed = column.remove(c);
                        rref.setRow(c + items.length);
                        column.put(c + items.length, removed);
                    }
                }
            }
        }

        for (Object i : items) {
            AdsDefinition prop = editorPage.getUsedProperties().getReference((Id) i).findProperty();
            if (prop != null) {
                Id idx = (Id) i;
                AdsEditorPageDef.PagePropertyRef propref = editorPage.getProperties().addPropId(idx);
                propref.setColumn(sCol);
                propref.setRow(start);
                start++;
                content.get(sCol).put(propref.getRow(), editorPage.getUsedProperties().getReference(idx));
            }
        }

        rowCount = calculateRowCount();
        fireTableDataChanged();
    }

    public void addItem(Object item) {
        if (item instanceof Id) {
            AdsDefinition prop = editorPage.getUsedProperties().getReference((Id) item).findProperty();
            if (prop != null) {
                addIdItem((Id) item);
            }
        }
    }

    private void addIdItem(Id idx) {
        AdsEditorPageDef.PagePropertyRef propref = editorPage.getProperties().addPropId(idx);
        int column = 0;
        if (!content.containsKey(column)) {
            addColumn();
        }
        content.get(column).put(propref.getRow(), editorPage.getUsedProperties().getReference(idx));

        rowCount = calculateRowCount();

        fireTableDataChanged();
    }

    public void moveItem(PropertyRef ref, int targetRow, int targetColumn) {
        AdsEditorPageDef.PagePropertyRef propRef = editorPage.getProperties().findByPropId(ref.getPropertyId());
        if (propRef != null) {
            int prevRow = propRef.getRow();
            int prevColumn = propRef.getColumn();

            Map<Integer, PropertyRef> column = content.get(prevColumn);
            if (column != null) {
                column.remove(prevRow);

                for (int r = prevRow + 1, size = column.size(); r <= size; r++) {
                    if (column.get(r) != null) {
                        Id rId = column.get(r).getPropertyId();
                        AdsEditorPageDef.PagePropertyRef rRef = editorPage.getProperties().findByPropId(rId);
                        if (rRef != null) {
                            rRef.setRow(r - 1);
                            PropertyRef removed = column.remove(r);
                            column.put(r - 1, removed);
                        }
                    }
                }

                propRef.setRow(targetRow);
                propRef.setColumn(targetColumn);

                Map<Integer, PropertyRef> newColumn = content.get(targetColumn);
                if (newColumn == null) {
                    newColumn = new HashMap<Integer, PropertyRef>();
                    content.put(targetColumn, newColumn);
                    newColumn.put(targetRow, ref);
                } else {

                    if (!newColumn.isEmpty()) {
                        int max = getMaxRowNumberInColumn(newColumn);

                        if (targetRow == max) {
                            targetRow = max + 1;
                            propRef.setRow(targetRow);
                        }

                        for (int r = targetRow, size = max; size >= r; size--) {
                            if (newColumn.get(size) != null) {
                                Id rId = newColumn.get(size).getPropertyId();
                                AdsEditorPageDef.PagePropertyRef rRef = editorPage.getProperties().findByPropId(rId);
                                if (rRef != null) {
                                    rRef.setRow(size + 1);
                                    PropertyRef moved = newColumn.remove(size);
                                    newColumn.put(size + 1, moved);
                                }
                            }
                        }

                    }

                    newColumn.put(targetRow, ref);
                }

                this.columnsCount = content.size();
                this.rowCount = calculateRowCount();

                fireTableDataChanged();
            }
        }
    }

    private int getMaxRowNumberInColumn(Map<Integer, PropertyRef> column) {
        Object[] objKeys = column.keySet().toArray();
        if (objKeys.length > 0) {
            Integer[] keys = new Integer[objKeys.length];
            System.arraycopy(objKeys, 0, keys, 0, objKeys.length);

            List<Integer> keysList = new ArrayList<Integer>();
            Collections.addAll(keysList, keys);
            Collections.sort(keysList);

            return keysList.get(keysList.size() - 1);
        }
        return 0;
    }

    public int getRowCountInColumn(int column) {
        if (column > -1 && column < content.size()) {
            return content.get(column).size();
        }
        return 0;
    }
}
