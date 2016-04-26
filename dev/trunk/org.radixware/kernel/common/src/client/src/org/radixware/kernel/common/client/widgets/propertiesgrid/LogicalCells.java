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

package org.radixware.kernel.common.client.widgets.propertiesgrid;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.client.widgets.IModelWidget;
import org.radixware.kernel.common.client.widgets.propertiesgrid.IPropertiesGridCells.CellsFinder;


class LogicalCells<L extends IModelWidget, E extends IModelWidget> implements IPropertiesGridCells<L, E> {

    private static class Row<L extends IModelWidget, E extends IModelWidget> {

        private final Map<Integer, IPropertiesGridCell<L, E>> cells = new HashMap<>();

        public void add(final IPropertiesGridCell<L, E> cell) throws CellIsNotEmptyException, AmbiguousCellLinkingException {
            final int column = cell.getColumn();
            for (int i = column + cell.getColumnSpan() - 1; i >= column; i--) {
                final IPropertiesGridCell<L, E> c = cells.get(Integer.valueOf(i));
                if (c != null) {
                    throw new CellIsNotEmptyException(c);
                }
            }
            cells.put(Integer.valueOf(column), cell);
            final IPropertiesGridCell<L,E> prevCell = cells.get(Integer.valueOf(column - 1));
            if (prevCell != null && needToLink(prevCell, cell)) {
                final IPropertiesGridCell linkedCell = prevCell.getLinkedCell(IPropertiesGridCell.ELinkageDirection.RIGHT);
                if (linkedCell != null) {
                    throw new AmbiguousCellLinkingException(prevCell, cell, linkedCell);
                }
                linkCells(prevCell, cell);
            }
            final IPropertiesGridCell<L,E> nextCell = cells.get(Integer.valueOf(column + 1));
            if (nextCell != null && needToLink(cell, nextCell)) {
                final IPropertiesGridCell linkedCell = nextCell.getLinkedCell(IPropertiesGridCell.ELinkageDirection.LEFT);
                if (linkedCell != null) {
                    throw new AmbiguousCellLinkingException(nextCell, cell, linkedCell);
                }
                linkCells(cell, nextCell);
            }
        }

        public IPropertiesGridCell<L, E> get(final Integer index) {
            return cells.get(index);
        }

        public void remove(final Integer index) {
            final IPropertiesGridCell<L, E> cell = cells.get(index);
            if (cell == null) {
                return;
            }
            {
                final IPropertiesGridCell<L, E> linkedCell = cell.getLinkedCell(IPropertiesGridCell.ELinkageDirection.LEFT);
                if (linkedCell != null) {
                    unlinkCells(linkedCell, cell);
                }
            }
            {
                final IPropertiesGridCell<L, E> linkedCell = cell.getLinkedCell(IPropertiesGridCell.ELinkageDirection.RIGHT);
                if (linkedCell != null) {
                    unlinkCells(cell, linkedCell);
                }
            }
            cells.remove(index);
        }

        public int size() {
            return cells.size();
        }

        private static boolean needToLink(final IPropertiesGridCell left, IPropertiesGridCell right) {
            return left.isLinked(IPropertiesGridCell.ELinkageDirection.RIGHT)
                    || right.isLinked(IPropertiesGridCell.ELinkageDirection.LEFT);
        }

        private void linkCells(final IPropertiesGridCell<L, E> left, IPropertiesGridCell<L, E> right) {
            left.linkWith(right, IPropertiesGridCell.ELinkageDirection.RIGHT);
            right.linkWith(left, IPropertiesGridCell.ELinkageDirection.LEFT);
        }

        private void unlinkCells(final IPropertiesGridCell<L, E> left, IPropertiesGridCell<L, E> right) {
            left.linkWith(null, IPropertiesGridCell.ELinkageDirection.RIGHT);
            right.linkWith(null, IPropertiesGridCell.ELinkageDirection.LEFT);
        }
    };
    private final Map<Integer, Row<L, E>> rows = new HashMap<>();
    private final List<Integer> usedRows = new ArrayList<>();
    private final List<Integer> usedColumns = new ArrayList<>();
    private boolean needToSort;

    private void checkSort() {
        if (needToSort) {
            Collections.sort(usedRows);
            Collections.sort(usedColumns);
        }
    }

    public void set(final IPropertiesGridCell<L, E> cell) throws CellIsNotEmptyException, AmbiguousCellLinkingException {
        Row<L, E> row = rows.get(Integer.valueOf(cell.getRow()));
        if (row == null) {
            row = new Row<>();
            rows.put(Integer.valueOf(cell.getRow()), row);
            usedRows.add(Integer.valueOf(cell.getRow()));
            needToSort = true;
        }
        row.add(cell);
        if (!usedColumns.contains(cell.getColumn())) {
            usedColumns.add(Integer.valueOf(cell.getColumn()));
            needToSort = true;
        }
    }

    private void clear(final Integer column, final Integer row) {
        final Row<L, E> actualRow = rows.get(row);
        if (actualRow != null) {
            actualRow.remove(column);
            if (actualRow.size() == 0) {
                rows.remove(row);
                usedRows.remove(row);
            }
            for (Row<L, E> r : rows.values()) {
                if (r.get(column) != null) {
                    return;
                }
            }
            usedColumns.remove(column);
        }
    }

    public void clear(final IPropertiesGridCell cell) {
        if (cell instanceof MappedCell) {
            checkSort();
            clear(usedColumns.get(cell.getColumn()), usedRows.get(cell.getRow()));
        } else if ((cell instanceof EmptyCell) == false) {
            clear(Integer.valueOf(cell.getColumn()), Integer.valueOf(cell.getRow()));
        }
    }

    public void clear() {
        rows.clear();
        usedColumns.clear();
        usedRows.clear();
    }

    public boolean isVisibleColumn(final int column) {
        checkSort();
        final Integer actualColumn = usedColumns.get(column);
        IPropertiesGridCell cell;
        for (Row<L, E> row : rows.values()) {
            cell = row.get(actualColumn);
            if (cell != null && cell.isVisible()) {
                return true;
            }
        }
        return false;
    }

    public IPropertiesGridCell<L, E> getRightButtomCell() {
        if (usedRows.isEmpty()) {
            return null;
        }
        checkSort();
        final Integer lastCol = usedColumns.get(usedColumns.size() - 1);
        Row<L, E> row;
        IPropertiesGridCell<L, E> cell;
        for (int i = usedRows.size() - 1; i >= 0; i--) {
            row = rows.get(usedRows.get(i));
            cell = row.get(lastCol);
            if (cell != null) {
                return cell;
            }
        }
        return null;
    }

    @Override
    public IPropertiesGridCell<L, E> get(final int column, final int row) {
        checkSort();
        final Row<L, E> actualRow = rows.get(usedRows.get(row));
        final IPropertiesGridCell<L, E> cell = actualRow.get(usedColumns.get(column));
        if (cell==null){
            return new EmptyCell<>(column, row);
        }else{
            return new MappedCell<>(cell, column, row);
        }
    }

    @Override
    public Collection<IPropertiesGridCell<L, E>> find(CellsFinder<L, E> finder) {
        checkSort();
        final Collection<IPropertiesGridCell<L, E>> result = new LinkedList<>();
        Row<L, E> row;
        IPropertiesGridCell<L, E> cell;
        for (int j = usedRows.size() - 1; j >= 0; j--) {
            row = rows.get(usedRows.get(j));
            for (int i = usedColumns.size() - 1; i >= 0; i--) {
                if (finder.isCancelled()) {
                    return Collections.unmodifiableCollection(result);
                }
                cell = row.get(usedColumns.get(i));
                if (cell != null) {
                    cell = new MappedCell<>(cell, i, j);
                    if (finder.isTarged(cell)) {
                        result.add(cell);
                    }
                }
            }
        }
        return Collections.unmodifiableCollection(result);
    }

    @Override
    public boolean isEmptySpaceInRow(final int row, final int fromColumn, final int toColumn) {
        checkSort();
        final Row<L, E> actualRow = rows.get(usedRows.get(row));
        for (int column = fromColumn; column <= toColumn; column++) {
            final IPropertiesGridCell<L, E> cell = actualRow.get(usedColumns.get(column));
            if (cell != null && !cell.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isEmptySpaceInColumn(final int column, final int fromRow, final int toRow) {
        checkSort();
        Row<L, E> actualRow;
        for (int row = fromRow; row <= toRow; row++) {
            actualRow = rows.get(usedRows.get(row));
            final IPropertiesGridCell<L, E> cell = actualRow.get(usedColumns.get(column));
            if (cell != null && !cell.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int getRowsCount() {
        return usedRows.size();
    }

    @Override
    public int getColumnsCount() {
        return usedColumns.size();
    }
    
    public int getLastRowIndexInColumn(final int column){
        checkSort();
        Row<L, E> row;
        IPropertiesGridCell<L, E> cell;
        int lastRowIndex = -1;
        for (int i = usedRows.size() - 1; i >= 0; i--) {
            row = rows.get(usedRows.get(i));
            cell = row.get(column);
            if (cell != null) {
                lastRowIndex = cell.getRow();
            }
        }
        return lastRowIndex;
    }
}
