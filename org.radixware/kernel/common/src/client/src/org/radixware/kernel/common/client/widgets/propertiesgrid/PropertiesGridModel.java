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

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.widgets.IModelWidget;
import org.radixware.kernel.common.types.Id;


final class PropertiesGridModel<L extends IModelWidget, E extends IModelWidget> {

    private final LogicalCells<L,E> logicalCells = new LogicalCells<>();
    private IPropertiesGridCells<L, E> visualCells;
    private final IClientEnvironment environment;

    public PropertiesGridModel(final IClientEnvironment environment) {
        this.environment = environment;
    }

    public PropertyCell<L, E> addProperty(final IPropertiesGridPresenter<L, E> presenter,
            final RadStandardEditorPageDef.PageItem pageItem,
            final Property property) {
        final PropertyCell<L,E> cell = new PropertyCell<>(presenter, pageItem, property);
        try {
            addCell(cell);
            return cell;
        } catch (CellIsNotEmptyException exception) {
            processException(exception, property, pageItem.getColumn(), pageItem.getRow(), pageItem.getColumnSpan());
            return null;
        } catch (AmbiguousCellLinkingException exception) {
            processException(exception, property);
            return null;
        }
    }

    public PropertyCell<L, E> addProperty(final IPropertiesGridPresenter<L, E> presenter,
            final Property property) {
        final IPropertiesGridCell<L, E> lastCell = logicalCells.getRightButtomCell();
        final int column, row;
        if (lastCell == null) {
            column = 0;
            row = 0;
        } else {
            column = lastCell.getColumn();
            row = lastCell.getRow() + 1;
        }
        return addProperty(presenter, property, column, row, 1);
    }

    public PropertyCell<L, E> addProperty(final IPropertiesGridPresenter<L, E> presenter,
            final Property property,
            final int column,
            final int row,
            final int colSpan) {
        try {
            return this.addPropertyImpl(presenter, property, column, row, colSpan, false, false);
        } catch (CellIsNotEmptyException exception) {
            processException(exception, property, column, row, 1);
            return null;
        } catch (AmbiguousCellLinkingException exception) {
            processException(exception, property);
            return null;
        }
    }

    public PropertyCell<L, E> addProperty(final IPropertiesGridPresenter<L, E> presenter,
            final Property property,
            final int column,
            final int row,
            final int colSpan,
            boolean stickToLeft,
            boolean stickToRight) {
        try {
            return this.addPropertyImpl(presenter, property, column, row, colSpan, stickToLeft, stickToRight);
        } catch (CellIsNotEmptyException exception) {
            processException(exception, property, column, row, 1);
            return null;
        } catch (AmbiguousCellLinkingException exception) {
            processException(exception, property);
            return null;
        }
    }

    private void processException(final CellIsNotEmptyException exception, final Property property, final int col, final int row, final int colSpan) {
        final MessageProvider mp = environment.getMessageProvider();
        final String message =
                mp.translate("TraceMessage", "Can't place editor of property \"%1$s\" #%2$s on editor page in row %3$s and column %4$s with %5$s length, because this space occupied by %6$s");
        final String cellDescription = exception.cell.getDescription(mp);
        final String finalMessage =
                String.format(message, property.getTitle(), property.getId().toString(), row, col, colSpan, cellDescription);
        environment.getTracer().event(finalMessage);
    }

    private void processException(AmbiguousCellLinkingException exception, final Property property) {
        final MessageProvider mp = environment.getMessageProvider();
        final String message =
                mp.translate("TraceMessage", "Can't place editor of property \"%1$s\" #%2$s on editor page: #%3$s");
        final String finalMessage =
                String.format(message, property.getTitle(), property.getId().toString(), exception.getMessage(mp));
        environment.getTracer().event(finalMessage);
    }

    private PropertyCell<L, E> addPropertyImpl(final IPropertiesGridPresenter<L, E> presenter,
            final Property property,
            final int column,
            final int row,
            final int colspan,
            final boolean stickToLeft,
            final boolean stickToRight) throws CellIsNotEmptyException, AmbiguousCellLinkingException {
        final PropertyCell<L, E> cell =
                new PropertyCell<>(presenter, property, column, row, colspan, stickToLeft, stickToRight);
        addCell(cell);
        return cell;
    }

    private void addCell(final PropertyCell<L, E> cell) throws CellIsNotEmptyException, AmbiguousCellLinkingException {
        logicalCells.set(cell);
        invalidate();
        for (int i = 1; i < cell.getColumnSpan(); i++) {
            logicalCells.set(new SpanCell<>(cell, i));
        }
    }

    @SuppressWarnings("unchecked")
    public void clearCell(IPropertiesGridCell<L, E> cell) {
        if (cell.getProperty() == null) {//it is empty cell
            return;
        }
        while (cell instanceof MappedCell) {
            cell = ((MappedCell) cell).getActualCell();
        }
        if (cell instanceof SpanCell) {
            cell = ((SpanCell) cell).getPropertyCell();
        }
        for (int i = 0, colSpan = cell.getColumnSpan(); i < colSpan && cell != null; i++) {
            logicalCells.clear(cell);
            cell = cell.getLinkedCell(IPropertiesGridCell.ELinkageDirection.RIGHT);
        }
        invalidate();
    }

    public void clearRightButtomCell() {
        final IPropertiesGridCell<L, E> cell = logicalCells.getRightButtomCell();
        if (cell != null) {
            logicalCells.clear(cell);
            invalidate();
        }
    }

    public void clear() {
        logicalCells.clear();
        invalidate();
    }

    public Collection<IPropertiesGridCell<L, E>> findCells(final IPropertiesGridCells.CellsFinder<L, E> finder) {
        return logicalCells.find(finder);
    }

    public IPropertiesGridCells<L, E> getCells() {
        if (visualCells == null) {
            initVisualCells();
        }
        return visualCells;
    }

    public void invalidate() {
        visualCells = null;
    }

    private void initVisualCells() {
        final VisualCells<L, E> cells = new VisualCells<>();
        int colCount = logicalCells.getColumnsCount();
        final int[] visualColumns = new int[colCount];//shifted column indexes
        //shift column indexes to skip columns with no visible items
        int invisibleColumnsCount = 0;
        for (int col = 0; col < colCount; col++) {
            if (logicalCells.isVisibleColumn(col)) {
                visualColumns[col] = col - invisibleColumnsCount;
            } else {
                visualColumns[col] = -1;
                invisibleColumnsCount++;
            }
        }
        int rowCount = logicalCells.getRowsCount();
        IPropertiesGridCell<L, E> currentCell;
        //moving rows to top
        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < colCount; col++) {
                if (visualColumns[col] > -1) {//column has visible items                                                            
                    currentCell = logicalCells.get(col, row);
                    if (!currentCell.isEmpty()) {
                        final int visualCol = visualColumns[col];
                        final int visualRow = calcVisualRow(cells, visualCol, currentCell);
                        if (currentCell.getRow() != visualRow) {
                            col = placeCellsToRow(cells, currentCell, visualColumns, visualRow, col);
                        } else {
                            final int visualSpan = currentCell.getColumnSpan();
                            cells.set(new VisualCell<>(currentCell, visualCol, visualRow, visualSpan));
                        }
                    }
                }
            }
        }
        //moving columns to left
        colCount = cells.getColumnsCount();
        rowCount = cells.getRowsCount();
        VisualCell<L, E> visualCell;
        for (int col = 1; col < colCount; col++) {//do not move items in first column
            for (int row = 0; row < rowCount; row++) {
                currentCell = cells.get(col, row);
                if (currentCell instanceof VisualCell && !currentCell.isEmpty()) {
                    visualCell = (VisualCell<L, E>) currentCell;
                    final int visualColumn = calcVisualCol(cells, row, visualCell);
                    if (visualColumn != visualCell.getColumn()) {
                        cells.move(visualCell, visualColumn, row);
                    }
                }
            }
        }
        visualCells = cells;
    }

    private int calcVisualRow(final IPropertiesGridCells<L, E> cells, final int column, final IPropertiesGridCell<L, E> cell) {//NOPMD
        int row = Math.min(cell.getRow(), cells.getRowsCount());
        if (row == 0 || cell.isEmpty() || cell.getLinkedCell(IPropertiesGridCell.ELinkageDirection.LEFT) != null) {
            return row;//no shifting
        } else {
            final int linkedCount = cell.getLinkedCellsCount(IPropertiesGridCell.ELinkageDirection.RIGHT);
            while (row > 0 && cells.isEmptySpaceInRow(row - 1, column, column + linkedCount)) {
                row--;
            }
        }
        return row;
    }

    private int calcVisualCol(final IPropertiesGridCells<L, E> cells, final int row, final VisualCell<L, E> cell) {//NOPMD
        int column = Math.min(cell.getColumn(), cells.getColumnsCount());
        if (column == 0 || cell.isEmpty()) {
            return column;//no shifting
        } else {
            final int lastRow = cells.getRowsCount() - 1;
            while (column > 0 && cells.isEmptySpaceInColumn(column - 1, row, lastRow)) {
                column--;
            }
        }
        return column;
    }

    private int placeCellsToRow(final VisualCells<L, E> cells, final IPropertiesGridCell<L, E> cell, final int[] visualColumns, final int visualRow, int col) {
        for (IPropertiesGridCell<L, E> movingCell = cell; movingCell != null; movingCell = movingCell.getLinkedCell(IPropertiesGridCell.ELinkageDirection.RIGHT)) {
            final int visualCol = visualColumns[col];
            final int visualSpan = movingCell.getColumnSpan();
            cells.set(new VisualCell<>(movingCell, visualCol, visualRow, visualSpan));
            col++;
        }
        return col - 1;
    }

    public List<Property> getProperties() {
        final IPropertiesGridCells.CellsFinder<L, E> finder = new IPropertiesGridCells.CellsFinder<L, E>() {
            @Override
            public boolean isTarged(IPropertiesGridCell<L, E> cell) {
                return cell.getPropertyEditor() != null;
            }
        };
        final Collection<IPropertiesGridCell<L, E>> cells = findCells(finder);
        final List<Property> properties = new LinkedList<>();
        final List<IPropertiesGridCell<L, E>> sortedCells = new LinkedList<>(cells);
        Collections.sort(sortedCells, new Comparator<IPropertiesGridCell<L, E>>() {
            @Override
            public int compare(IPropertiesGridCell<L, E> cell1, IPropertiesGridCell<L, E> cell2) {
                if (cell1.getColumn() < cell2.getColumn()) {
                    return -1;
                }
                if (cell1.getColumn() > cell2.getColumn()) {
                    return 1;
                }
                if (cell1.getRow() < cell2.getRow()) {
                    return -1;
                }
                if (cell1.getRow() > cell2.getRow()) {
                    return 1;
                }
                return 0;
            }
        });
        for (IPropertiesGridCell cell : sortedCells) {
            if (!properties.contains(cell.getProperty())) {
                properties.add(cell.getProperty());
            }
        }
        return Collections.<Property>unmodifiableList(properties);
    }

    public Collection<PropertyCell<L, E>> findCellsForProperty(final Id propertyId) {
        final IPropertiesGridCells.CellsFinder<L, E> finder = new IPropertiesGridCells.CellsFinder<L, E>() {
            @Override
            public boolean isTarged(IPropertiesGridCell<L, E> cell) {
                return cell.getPropertyEditor() != null
                        && cell.getProperty() != null
                        && cell.getProperty().getId().equals(propertyId);
            }
        };
        final Collection<PropertyCell<L, E>> result = new LinkedList<>();
        final Collection<IPropertiesGridCell<L, E>> cells = findCells(finder);
        IPropertiesGridCell<L, E> resultCell;
        for (IPropertiesGridCell<L, E> cell : cells) {
            resultCell = cell;
            while (resultCell instanceof MappedCell) {
                resultCell = ((MappedCell<L,E>) resultCell).getActualCell();
            }
            if (resultCell instanceof PropertyCell) {
                result.add((PropertyCell<L, E>) resultCell);
            }
        }
        return result;
    }

    public Collection<IPropertiesGridCell<L, E>> findVisualCellsForProperty(final Id propertyId) {
        final IPropertiesGridCells.CellsFinder<L, E> finder = new IPropertiesGridCells.CellsFinder<L, E>() {
            @Override
            public boolean isTarged(IPropertiesGridCell<L, E> cell) {
                return cell.getPropertyEditor() != null
                        && cell.getProperty() != null
                        && cell.getProperty().getId().equals(propertyId);
            }
        };
        return getCells().find(finder);
    }

    public Collection<IPropertiesGridCell<L, E>> findVisiblePropertyCells() {
        final IPropertiesGridCells.CellsFinder<L, E> finder = new IPropertiesGridCells.CellsFinder<L, E>() {
            @Override
            public boolean isTarged(IPropertiesGridCell<L, E> cell) {
                return cell.getPropertyEditor() != null
                        && cell.getProperty() != null
                        && cell.getProperty().isVisible();
            }
        };
        return findCells(finder);
    }

    public Collection<PropertyCell<L, E>> findPropertyCells() {
        final IPropertiesGridCells.CellsFinder<L, E> finder = new IPropertiesGridCells.CellsFinder<L, E>() {
            @Override
            public boolean isTarged(IPropertiesGridCell<L, E> cell) {
                return cell.getPropertyEditor() != null && cell.getProperty() != null;
            }
        };
        final Collection<PropertyCell<L, E>> result = new LinkedList<>();
        final Collection<IPropertiesGridCell<L, E>> cells = findCells(finder);
        IPropertiesGridCell<L, E> resultCell;
        for (IPropertiesGridCell<L, E> cell : cells) {
            resultCell = cell;
            while (resultCell instanceof MappedCell) {
                resultCell = ((MappedCell<L,E>) resultCell).getActualCell();
            }
            if (resultCell instanceof PropertyCell) {
                result.add((PropertyCell<L, E>) resultCell);
            }
        }
        return result;
    }

    public int getPropetyCellsCount() {
        final IPropertiesGridCells.CellsFinder<L, E> finder = new IPropertiesGridCells.CellsFinder<L, E>() {
            @Override
            public boolean isTarged(IPropertiesGridCell<L, E> cell) {
                return cell.getPropertyEditor() != null
                        && cell.getProperty() != null;
            }
        };
        return findCells(finder).size();
    }

    public int getLastLogicalColumnIndex() {
        final IPropertiesGridCell<L, E> cell = logicalCells.getRightButtomCell();
        return cell == null ? -1 : cell.getColumn();
    }

    public int getLastLogicalRowIndex(final int logicalColumnIndex) {
        return logicalCells.getLastRowIndexInColumn(logicalColumnIndex);
    }
}
