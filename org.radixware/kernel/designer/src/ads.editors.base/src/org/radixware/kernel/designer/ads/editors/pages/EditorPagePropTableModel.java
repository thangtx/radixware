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

import java.util.LinkedList;
import java.util.List;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPageDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.IPagePropertyGroup;
import org.radixware.kernel.common.types.Id;


class EditorPagePropTableModel implements TableModel {

    public static class PageItem {

        private AdsEditorPageDef.PagePropertyRef propRef;
        private int index;
        Table table;

        PageItem(AdsEditorPageDef.PagePropertyRef propRef) {
            this.propRef = propRef;
        }

        public int getStartColumn() {
            return this.propRef.getColumn();
        }

        public void setStartColumn(int col) {
            if (this.propRef != null) {
                this.propRef.setColumn(col);
            }
        }

        public void setRow(int row) {
            if (this.propRef != null) {
                this.propRef.setRow(row);
            }
        }

        public int getEndColumn() {
            int start = this.propRef.getColumn();
            int span = this.propRef.getColumnSpan();
            return start + span - 1;
        }

        public int getColSpan() {
            return this.propRef.getColumnSpan();
        }

        public void setColSpan(int span) {
            this.propRef.setColumnSpan(span);
        }

        public int getRow() {
            return this.propRef.getRow();
        }

        public String getDisplayName() {
            if (propRef == null) {
                return "";
            }
            AdsDefinition def = propRef.findItem();
            if (def != null) {
                if (def instanceof IPagePropertyGroup){
                   return  "<html><i>" + def.getName() +"</i></html>";
                }
                return def.getName();
            } else {
                return "#" + propRef.getId().toString();
            }
        }

        public boolean isValid() {
            return propRef == null || propRef.findItem() != null;
        }

        public Id getPropertyId() {
            return propRef == null ? null : propRef.getId();
        }

        public boolean isGlutToLeft() {
            return propRef.isGlueToLeft();
        }

        public boolean isGlutToRight() {
            return propRef.isGlueToRight();
        }

        public void setGlutToLeft(boolean glut) {
            propRef.setGlueToLeft(glut);
        }

        public void setGlutToRight(boolean glut) {
            propRef.setGlueToRight(glut);
        }
        
        public AdsEditorPageDef.PagePropertyRef getPropertyRef(){
            return propRef;
        }
    }

    static class Table {

        private List<List<PageItem>> items = new LinkedList<List<PageItem>>();
        private int rowCount;

        public void open(List<AdsEditorPageDef.PagePropertyRef> properties) {
            items.clear();
            List<PageItem> last = null;

            List<AdsEditorPageDef.PagePropertyRef> lasts = new LinkedList<AdsEditorPageDef.PagePropertyRef>();
            for (AdsEditorPageDef.PagePropertyRef prop : properties) {
                int col = prop.getColumn();
                int row = prop.getRow();
                List<PageItem> column = null;
                if (col < 0 || row < 0) {
                    lasts.add(prop);
                    continue;
                }
                ensureColCount(col + (last == null ? 1 : 2), last);
                column = items.get(col);


                while (rowCount < row + 1) {
                    addRow();
                }

                int lastSign = -1;
                List<PageItem> move = new LinkedList<PageItem>();
                for (int i = rowCount - 1; i >= 0; i--) {
                    PageItem item = column.get(i);
                    if (item != null) {
                        if (item.index >= 0) {
                            lastSign = i;
                            break;
                        } else if (item.index < 0) {
                            move.add(0, item);
                        }
                    }
                }
                if (lastSign < row) {
                    int requredRowCount = row + 1 + move.size();
                    while (rowCount < requredRowCount) {
                        addRow();
                    }
                    int insert = row + 1;
                    for (PageItem item : move) {
                        column.set(insert, item);
                        insert++;
                    }
                }
                column.set(row, new PageItem(prop));
            }

            for (int c = 0; c < getColCount(); c++) {
                for (int r = 0; r < getRowCount(); r++) {
                    PageItem item = get(r, c);
                    if (item == null) {
                        continue;
                    }
                    if (item.getColSpan() > 1) {
                        int continueSpan = item.getColSpan();
                        for (int i = c; i >= 0; i--) {
                            if (get(r, i) == item) {
                                continueSpan--;
                            } else {
                                break;
                            }
                        }

                        if (continueSpan > 0) {
                            if (c + 1 >= getColCount()) {
                                addColumn();
                            }
                            if (get(r, c + 1) == null) {
                                set(r, c + 1, item);
                            } else {//shift down all content
                                List<PageItem> move = new LinkedList<PageItem>();
                                for (int i = r; i < rowCount; i++) {
                                    PageItem pi = get(i, c + 1);
                                    if (pi != null) {
                                        move.add(pi);
                                        set(i, c + 1, null);
                                    }
                                }
                                if (move.size() >= rowCount - (r + 1)) {
                                    addRow();
                                }
                                int ins = r + 1;
                                for (PageItem pi : move) {
                                    set(ins, c + 1, pi);
                                    ins++;
                                }
                            }
                        }
                    }
                }
            }
            if (!lasts.isEmpty()) {
                for (AdsEditorPageDef.PagePropertyRef prop : lasts) {
                    List<PageItem> col;
                    if (getColCount() > 0) {
                        col = items.get(getColCount() - 1);
                    } else {
                        col = addColumn();
                    }
                    int lastNullIndex = -1;
                    for (int i = col.size() - 1; i >= 0; i++) {
                        if (col.get(i) != null) {
                            break;
                        } else {
                            lastNullIndex = i;
                        }
                    }
                    if (lastNullIndex < 0) {
                        addRow();
                    }
                    set(getRowCount() - 1, getColCount() - 1, new PageItem(prop));
                }
            }
            pack();
        }

        boolean isSpanTail(int r, int c) {
            if (c > 0) {
                return get(r, c) != null && get(r, c - 1) == get(r, c);
            } else {
                return false;
            }
        }

        boolean isSpanLast(int r, int c) {
            if (c > 0) {
                if (c + 1 >= getColCount()) {
                    return get(r, c) != null && get(r, c - 1) == get(r, c);
                } else {
                    return get(r, c) != null && get(r, c + 1) != get(r, c) && get(r, c - 1) == get(r, c);
                }
            } else {
                return false;
            }
        }

        boolean isSpanMid(int r, int c) {
            if (c > 0) {
                if (c + 1 >= getColCount()) {
                    return false;
                } else {
                    return get(r, c) != null && get(r, c + 1) == get(r, c) && get(r, c - 1) == get(r, c);
                }
            } else {
                return false;
            }
        }

        boolean isSpanStart(int r, int c) {
            if (c >= 0 && c + 1 < getColCount()) {
                return get(r, c) != null && get(r, c + 1) == get(r, c) && get(r, c - 1) != get(r, c);
            } else {
                return false;
            }
        }

        private int getLastSpanCol(int r, int c) {
            PageItem item = get(r, c);
            int last = c;
            for (int i = c + 1; i < getColCount(); i++) {
                if (get(r, i) == item) {
                    last = i;
                } else {
                    break;
                }

            }
            return last;
        }

        private int getPrefferedRow(int r, int c) {
            PageItem item = get(r, c);
            if (item != null) {
                if (item.isGlutToLeft()) {
                    if (c > 0 && get(r, c - 1) != null) {
                        return r;
                    }
                }
                if (item.isGlutToRight()) {
                    if (c + 1 < getColCount() && get(r, c + 1) != null) {
                        return getPrefferedRow(r, c + 1);
                    }
                }
            }
            int moveTo = r;
            for (int i = r - 1; i >= 0; i--) {
                if (get(i, c) != null) {
                    break;
                } else {
                    moveTo = i;
                }
            }
            return moveTo;
        }

        private int getSpanValue(PageItem item) {
            for (int c = 0; c < getColCount(); c++) {
                for (int r = 0; r < getRowCount(); r++) {
                    if (get(r, c) == item) {
                        return getSpanValue(r, c);
                    }
                }
            }
            return 1;
        }

        private int getSpanValue(int r, int c) {
            int val = 1;
            PageItem item = get(r, c);
            for (int i = c - 1; i >= 0; i--) {
                if (get(r, i) == item) {
                    val++;
                }
            }
            for (int i = c + 1; i < getColCount(); i++) {
                if (get(r, i) == item) {
                    val++;
                }
            }
            return val;
        }

        private void setSpan(PageItem item, int value) {
            if (value < 1) {
                return;
            }
            for (int c = 0; c < getColCount(); c++) {
                for (int r = 0; r < getRowCount(); r++) {
                    if (get(r, c) == item) {
                        int current = getSpanValue(r, c);
                        if (current == value) {
                            return;
                        } else {
                            if (current > value) {
                                int last = getLastSpanCol(r, c);
                                while (current > value) {
                                    set(r, last, null);
                                    current--;
                                }
                                save();
                                break;
                            } else {
                                addRow(r);
                                for (int i = 0; i < c; i++) {
                                    set(r, i, get(r + 1, i));
                                    set(r + 1, i, null);
                                }
                                int last = c + value - 1;

                                for (int i = c; i <= last; i++) {
                                    if (i >= getColCount()) {
                                        addColumn();
                                    }
                                    set(r, i, item);
                                    if (get(r + 1, i) == item) {
                                        set(r + 1, i, null);
                                    }
                                }
                                PageItem prev = get(r + 1, last);
                                for (int i = last + 1; i < getColCount(); i++) {
                                    PageItem cur = get(r + 1, i);
                                    if (cur == prev) {
                                        continue;
                                    }
                                    set(r + 1, i, null);
                                    set(r, i, cur);
                                }
                                save();
                                break;
                            }
                        }

                    }
                }
            }
        }

        private void pack() {
            List<List<PageItem>> nulls = new LinkedList<List<PageItem>>();

            for (int c = 0; c < getColCount(); c++) {
                boolean columnIsNull = true;
                for (int r = 0; r < getRowCount(); r++) {
                    PageItem item = get(r, c);
                    if (item != null) {
                        item.table = this;
                    }
                    if (isSpanTail(r, c)) {
                        columnIsNull = false;
                        continue;
                    } else {
                        if (get(r, c) != null) {
                            columnIsNull = false;
                            int moveTo = getPrefferedRow(r, c);
                            if (moveTo != r) {
                                if (isSpanStart(r, c)) {
                                    int col = getLastSpanCol(r, c);
                                    for (int i = c + 1; i <= col; i++) {
                                        int m = getPrefferedRow(r, i);
                                        if (m > moveTo) {
                                            moveTo = r;
                                        }
                                    }
                                }
                            }
                            if (moveTo != r) {
                                item = get(r, c);
                                for (int i = c; i < getColCount(); i++) {
                                    if (get(r, i) == item && get(moveTo, i) == null) {
                                        set(r, i, null);
                                        assert get(moveTo, i) == null;
                                        set(moveTo, i, item);
                                    } else {
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
                if (columnIsNull) {
                    nulls.add(items.get(c));
                }
            }

            for (List<PageItem> n : nulls) {
                items.remove(n);
            }
            if (items.isEmpty()) {
                rowCount = 0;
            }

            for (int row = 0; row < getRowCount(); row++) {
                for (int c = 0; c < getColCount(); c++) {
                    PageItem item = get(row, c);
                    if (item != null) {
                        if (item.isGlutToRight()) {
                            if (c + 1 < getColCount()) {
                                if (get(row, c + 1) == null) {
                                    item.setGlutToRight(false);
                                }
                            } else {
                                item.setGlutToRight(false);
                            }
                        }
                        if (item.isGlutToLeft()) {
                            if (c - 1 >= 0) {
                                if (get(row, c - 1) == null) {
                                    item.setGlutToLeft(false);
                                }
                            } else {
                                item.setGlutToLeft(false);
                            }
                        }
                    }
                }
            }
            for (int row = 0; row < getRowCount();) {
                boolean rowIsNull = true;
                for (int c = 0; c < getColCount(); c++) {
                    if (get(row, c) != null) {
                        rowIsNull = false;
                        break;
                    }
                }
                if (rowIsNull) {
                    removeRow(row);
                } else {
                    row++;
                }
            }
        }

        private void removeRow(int index) {
            for (List<PageItem> item : items) {
                item.remove(index);
            }
            rowCount--;
        }

        public PageItem get(int row, int col) {
            if (row >= 0 && row < getRowCount() && col >= 0 && col < getColCount()) {
                return items.get(col).get(row);
            } else {
                return null;
            }
        }

        private void set(int row, int col, PageItem value) {
            if (row >= 0 && row < getRowCount() && col >= 0 && col < getColCount()) {
                items.get(col).set(row, value);
            }
        }

        private int findLastNotNull(List<PageItem> column) {

            for (int i = column.size() - 1; i >= 0; i--) {
                if (column.get(i) != null) {
                    return i;
                }
            }
            return -1;
        }

        private void move(PageItem item, int row, int col) {
            for (int c = 0; c < getColCount(); c++) {
                for (int r = 0; r < getRowCount(); r++) {
                    if (get(r, c) == item) {
                        set(r, c, null);
                    }
                }
            }
            add(item, row, col);
        }

        private void add(PageItem item, int row, int col) {
            if (row >= 0 && row < getRowCount() && col >= 0 && col < getColCount()) {
                if (get(row, col) == null) {
                    set(row, col, item);
                    save();
                } else {
                    addRow(row);
                    assert get(row, col) == null;
                    set(row, col, item);
                    save();
                }
            } else {
                List<PageItem> column = null;
                if (getColCount() > 0) {
                    column = items.get(0);
                } else {
                    column = addColumn();
                }
                int index = findLastNotNull(column);
                if (index < 0 || index == rowCount - 1) {
                    addRow();
                }
                assert get(getRowCount() - 1, 0) == null;
                set(getRowCount() - 1, 0, item);
                save();
            }
        }

        private void freeSpaceAtStart(List<PageItem> column, int count) {
            if (count == 0) {
                return;
            }
            List<PageItem> sign = new LinkedList<PageItem>();
            for (PageItem item : column) {
                if (item != null) {
                    sign.add(item);
                }
            }
            for (int i = 0; i < column.size(); i++) {
                column.set(i, null);
            }
            while (sign.size() + count < rowCount) {
                addRow();
            }
            int insplace = count;
            for (PageItem item : sign) {
                column.set(insplace, item);
                insplace++;
            }
        }

        private void save() {
            pack();
            for (int c = 0; c < getColCount(); c++) {
                for (int r = 0; r < getRowCount(); r++) {
                    PageItem item = get(r, c);
                    if (item != null) {
                        if (isSpanMid(r, c) || isSpanLast(r, c)) {
                            continue;
                        } else {
                            item.setRow(r);
                            item.setStartColumn(c);
                            item.setColSpan(getSpanValue(r, c));
                        }
                        if (c == 0 || get(r, c - 1) == null) {
                            item.setGlutToLeft(false);
                        }
                        if (c + 1 >= getColCount() || get(r, c + 1) == null) {
                            item.setGlutToRight(false);
                        }
                    }
                }
            }
        }

        private void removeColumn(int index) {
            if (index >= 0 && index < getColCount()) {
                List<PageItem> column = items.get(index);
                if (index == 0) {
                    if (index + 1 >= items.size()) {
                        return;
                    } else {
                        List<PageItem> move = new LinkedList<PageItem>();
                        for (int i = 0; i < rowCount; i++) {
                            PageItem item = column.get(i);
                            if (item != null) {
                                //check for span
                                for (int c = index + 1; c < getColCount(); c++) {
                                    if (get(i, c) == item) {
                                        set(i, c, null);
                                    }
                                }
                                move.add(item);
                            }
                        }

                        List<PageItem> next = items.get(index + 1);
                        freeSpaceAtStart(next, move.size());
                        for (int i = 0; i < move.size(); i++) {
                            assert next.get(i) == null;
                            next.set(i, move.get(i));
                        }
                        items.remove(index);
                        save();
                    }
                } else {
                    List<PageItem> move = new LinkedList<PageItem>();
                    for (int i = 0; i < rowCount; i++) {
                        PageItem item = column.get(i);
                        if (item != null) {
                            //check for span in following columns
                            for (int c = index + 1; c < getColCount(); c++) {
                                if (get(i, c) == item) {
                                    set(i, c, null);
                                }
                            }
                            if (get(i, index - 1) != item) {//no span from prev column
                                move.add(item);
                            }
                        }
                    }

                    List<PageItem> prev = items.get(index - 1);
                    int lastFreeIndex = -1;
                    for (int i = rowCount - 1; i >= 0; i--) {
                        if (prev.get(i) == null) {
                            lastFreeIndex = i;
                        } else {
                            break;
                        }
                    }
                    if (lastFreeIndex == -1) {//no free space
                        lastFreeIndex = rowCount;
                        for (int i = 0; i < move.size(); i++) {
                            addRow();
                        }
                    } else {
                        int add = rowCount - 1 - lastFreeIndex;
                        for (int i = 0; i < add; i++) {
                            addRow();
                        }
                    }
                    for (PageItem item : move) {
                        assert prev.get(lastFreeIndex) == null;
                        prev.set(lastFreeIndex, item);
                        lastFreeIndex++;
                    }
                    items.remove(index);
                    save();
                }
            }
        }

        private int getColCount() {
            return items.size();
        }

        private void ensureColCount(int count, List<PageItem> before) {
            while (items.size() < count) {
                addColumn(before);
            }
        }

        private List<PageItem> createColumn() {
            List<PageItem> list = new LinkedList<PageItem>();
            for (int i = 0; i < rowCount; i++) {
                list.add(null);
            }
            return list;
        }

        private List<PageItem> addColumn() {
            List<PageItem> list = createColumn();
            items.add(list);
            return list;
        }

        private List<PageItem> addColumn(List<PageItem> before) {
            List<PageItem> list = createColumn();
            if (before != null) {
                int index = items.indexOf(before);
                items.add(index, list);
            } else {
                items.add(list);
            }
            return list;
        }

        private void addRow() {
            for (List<PageItem> list : items) {
                list.add(null);
            }
            rowCount++;
        }

        private void addRow(int index) {
            for (List<PageItem> list : items) {
                list.add(index, null);
            }
            rowCount++;
        }

        public int getRowCount() {
            return rowCount;
        }

        public void remove(Id id) {
            for (int c = 0; c < getColCount(); c++) {
                for (int r = 0; r < getRowCount(); r++) {
                    PageItem item = get(r, c);

                    if (item != null && item.getPropertyId() == id) {
                        set(r, c, null);
                    }
                }
            }
            save();
        }
    }

    public EditorPagePropTableModel() {
    }
    private AdsEditorPageDef editorPage;
    private Table table = new Table();
    private ICurrentGroupListener currentGroupListener;
    
    public void open(AdsEditorPageDef editorPage, IPagePropertyGroup group) {
        this.editorPage = editorPage;
        table.open(group.list());
        fireModelChange();
    }

    @Override
    public int getColumnCount() {
        return table.getColCount();
    }

    @Override
    public String getColumnName(int columnIndex) {
        return String.valueOf(columnIndex);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return PageItem.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return table.get(rowIndex, columnIndex);
    }

    public void setGlutToLeft(PageItem item, boolean set) {
        if (isReadOnly()) {
            return;
        }
        item.setGlutToLeft(set);
        table.save();
        fireModelChange();
    }

    public void setGlutToRight(PageItem item, boolean set) {
        if (isReadOnly()) {
            return;
        }
        item.setGlutToRight(set);
        table.save();
        fireModelChange();
    }

    public int getColSpan(PageItem item) {
        return table.getSpanValue(item);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    }
    private final List<TableModelListener> listeners = new LinkedList<TableModelListener>();

    @Override
    public void addTableModelListener(TableModelListener l) {
        synchronized (listeners) {
            if (!listeners.contains(l)) {
                listeners.add(l);
            }
        }
    }

    @Override
    public int getRowCount() {
        return table.getRowCount();
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
        synchronized (listeners) {
            listeners.remove(l);
        }
    }

    public void addColumn() {
        if (isReadOnly()) {
            return;
        }
        table.addColumn();
        fireModelChange();
    }

    public void removeColumn(int index) {
        if (isReadOnly()) {
            return;
        }
        table.removeColumn(index);
        fireModelChange();
    }

    public void fireModelChange() {
        TableModelEvent event = new TableModelEvent(this);
        for (TableModelListener l : listeners) {
            l.tableChanged(null);
        }
    }

    public void moveItem(PageItem propertyRef, int row, int column) {
        if (isReadOnly()) {
            return;
        }
        table.move(propertyRef, row, column);
        fireModelChange();
    }

    public PageItem addItem(int row, int col, Object[] objects) {
        if (isReadOnly()) {
            return null;
        }
        PageItem lastAdded = null;
        for (Object obj : objects) {
            if (obj instanceof Id) {
                Id id = (Id) obj;
                IPagePropertyGroup group = getCurrentGroup();
                AdsEditorPageDef.PagePropertyRef ref = group.addPropId(id);
                if (ref != null && ref.findItem() != null) {
                    table.add(lastAdded = new PageItem(ref), row, col);
                    row++;
                }
            }
        }
        fireModelChange();
        return lastAdded;
    }

    public void setColumnSpan(PageItem item, int value) {
        if (isReadOnly()) {
            return;
        }
        table.setSpan(item, value);
        fireModelChange();
    }

    public void removeItem(Id id) {
        if (isReadOnly()) {
            return;
        }
        IPagePropertyGroup group = getCurrentGroup();
        group.removeByPropId(id);
        table.remove(id);

        fireModelChange();

    }
    
    public void removeItem(AdsEditorPageDef.PagePropertyRef ref) {
        if (isReadOnly()) {
            return;
        }
        
        if (currentGroupListener != null){
            if (ref.getGroupDef() != null){
                currentGroupListener.removeChildPropertyGroup(ref.getGroupDef());
            }
        }

        fireModelChange();

    }
    
    public void setCurrentGroupListener(ICurrentGroupListener currentGroupListener) {
        this.currentGroupListener = currentGroupListener;
    }

    public IPagePropertyGroup getCurrentGroup() {
        if (currentGroupListener != null){
            return currentGroupListener.getCurrentPagePropertyGroup();
        }
        return editorPage.getProperties();
    }
    
    public boolean isReadOnly(){
        return editorPage == null || editorPage.isReadOnly();
    }
}
