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
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.widgets.IModelWidget;
import org.radixware.kernel.common.client.widgets.propertiesgrid.IPropertiesGridCells.CellsFinder;


final class VisualCells<L extends IModelWidget, E extends IModelWidget> implements IPropertiesGridCells<L, E> {

    private static class Row<L extends IModelWidget, E extends IModelWidget> {

        private final List<IPropertiesGridCell<L, E>> cells = new ArrayList<>();
        private final int rowNum;

        public Row(final int row) {
            this.rowNum = row;
        }

        public void add(final IPropertiesGridCell<L, E> cell) {
            final int column = cell.getColumn();
            if (column < cells.size()) {
                cells.set(column, cell);
            } else {
                for (int i = cells.size(); i < column; i++) {
                    cells.add(new EmptyCell<L, E>(i, rowNum));
                }
                cells.add(cell);
            }
        }

        public IPropertiesGridCell<L, E> get(final int index) {
            return index < cells.size() ? cells.get(index) : new EmptyCell<L, E>(index, rowNum);
        }

        public int size() {
            return cells.size();
        }
    };
    private final List<Row<L, E>> rows = new ArrayList<>();
    private int columnsCount;

    public void set(final IPropertiesGridCell<L, E> cell) {
        final int r = cell.getRow();
        final Row<L, E> row;
        if (r < rows.size()) {
            row = rows.get(r);
        } else {
            for (int i = rows.size(); i < r; i++) {
                final Row<L,E> newRow = new Row<>(i);
                rows.add(newRow);
            }
            row = new Row<>(r);
            rows.add(row);
        }
        row.add(cell);
        columnsCount = Math.max(columnsCount, cell.getColumn() + 1);
    }

    public void move(final VisualCell<L, E> cell, final int col, final int row) {
        set(new EmptyCell<L, E>(cell.getColumn(), cell.getRow()));
        cell.setColumn(col);
        cell.setRow(row);
        set(cell);
    }

    @Override
    public IPropertiesGridCell<L, E> get(final int column, final int row) {
        final Row<L, E> r = rows.get(row);
        return r.get(column);
    }

    @Override
    public Collection<IPropertiesGridCell<L, E>> find(CellsFinder<L, E> finder) {
        final Collection<IPropertiesGridCell<L, E>> result = new LinkedList<>();
        IPropertiesGridCell<L, E> cell;
        for (Row<L, E> row : rows) {
            for (int i = row.size() - 1; i >= 0; i--) {
                if (finder.isCancelled()) {
                    return Collections.unmodifiableCollection(result);
                }
                cell = row.get(i);
                if (finder.isTarged(cell)) {
                    result.add(cell);
                }
            }
        }
        return Collections.unmodifiableCollection(result);
    }

    @Override
    public boolean isEmptySpaceInRow(final int row, final int fromColumn, final int toColumn) {
        final Row<L, E> r = rows.get(row);
        for (int column = fromColumn; column <= toColumn; column++) {
            if (!r.get(column).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isEmptySpaceInColumn(final int column, final int fromRow, final int toRow) {
        Row<L, E> r;
        for (int row = fromRow; row <= toRow; row++) {
            r = rows.get(row);
            if (!r.get(column).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int getRowsCount() {
        return rows.size();
    }

    @Override
    public int getColumnsCount() {
        return columnsCount;
    }
}