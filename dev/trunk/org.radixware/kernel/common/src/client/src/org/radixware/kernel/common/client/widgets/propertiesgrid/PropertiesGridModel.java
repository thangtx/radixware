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
import org.radixware.kernel.common.client.models.items.PropertiesGroupModelItem;
import org.radixware.kernel.common.client.models.items.ModelItem;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.views.IPropertiesGroupWidget;
import org.radixware.kernel.common.client.widgets.IModelWidget;
import org.radixware.kernel.common.types.Id;


final class PropertiesGridModel<L extends IModelWidget, E extends IModelWidget, G extends IPropertiesGroupWidget> {

    private final LogicalCells<L,E,G> logicalCells = new LogicalCells<>();
    private IPropertiesGridCells<L, E, G> visualCells;
    private final IClientEnvironment environment;

    public PropertiesGridModel(final IClientEnvironment environment) {
        this.environment = environment;
    }

    public PropertyCell<L, E, G> addProperty(final IPropertiesGridPresenter<L, E, G> presenter,
            final RadStandardEditorPageDef.PageItem pageItem,
            final Property property) {
        final PropertyCell<L,E,G> cell = new PropertyCell<>(presenter, pageItem, property);
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

    public PropertyCell<L, E, G> addProperty(final IPropertiesGridPresenter<L, E, G> presenter,
            final Property property) {
        final IPropertiesGridCell<L, E, G> lastCell = logicalCells.getRightButtomCell();
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

    public PropertyCell<L, E, G> addProperty(final IPropertiesGridPresenter<L, E, G> presenter,
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

    public PropertyCell<L, E, G> addProperty(final IPropertiesGridPresenter<L, E, G> presenter,
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
                mp.translate("TraceMessage", "Unable to place editor of property \"%1$s\" #%2$s on editor page in row %3$s and column %4$s with %5$s length, because this space occupied by %6$s");
        final String cellDescription = exception.cell.getDescription(mp);
        final String finalMessage =
                String.format(message, property.getTitle(), property.getId().toString(), row, col, colSpan, cellDescription);
        environment.getTracer().event(finalMessage);
    }

    private void processException(AmbiguousCellLinkingException exception, final Property property) {
        final MessageProvider mp = environment.getMessageProvider();
        final String message =
                mp.translate("TraceMessage", "Unable to place editor of property \"%1$s\" #%2$s on editor page: #%3$s");
        final String finalMessage =
                String.format(message, property.getTitle(), property.getId().toString(), exception.getMessage(mp));
        environment.getTracer().event(finalMessage);
    }

    private PropertyCell<L, E, G> addPropertyImpl(final IPropertiesGridPresenter<L, E, G> presenter,
            final Property property,
            final int column,
            final int row,
            final int colspan,
            final boolean stickToLeft,
            final boolean stickToRight) throws CellIsNotEmptyException, AmbiguousCellLinkingException {
        final PropertyCell<L, E, G> cell =
                new PropertyCell<>(presenter, property, column, row, colspan, stickToLeft, stickToRight);
        addCell(cell);
        return cell;
    }
    
    public PropertiesGroupCell<L, E, G> addPropertiesGroup(final IPropertiesGridPresenter<L, E, G> presenter,
                                                                                          final RadStandardEditorPageDef.PageItem pageItem,
                                                                                          final PropertiesGroupModelItem group){
        final PropertiesGroupCell<L,E,G> cell = new PropertiesGroupCell<>(presenter,pageItem,group);
        try {
            addCell(cell);
            return cell;
        } catch (CellIsNotEmptyException exception) {
            processException(exception, group, pageItem.getColumn(), pageItem.getRow(), pageItem.getColumnSpan());
            return null;
        } catch (AmbiguousCellLinkingException exception) {
            processException(exception, group);
            return null;
        }        
    }
    
    private void processException(final CellIsNotEmptyException exception, final PropertiesGroupModelItem group, final int col, final int row, final int colSpan) {
        final MessageProvider mp = environment.getMessageProvider();
        final String message =
                mp.translate("TraceMessage", "Unable to place properties group \"%1$s\" #%2$s on editor page in row %3$s and column %4$s with %5$s length, because this space occupied by %6$s");
        final String cellDescription = exception.cell.getDescription(mp);
        final String finalMessage =
                String.format(message, group.getTitle(), group.getId().toString(), row, col, colSpan, cellDescription);
        environment.getTracer().event(finalMessage);
    }

    private void processException(AmbiguousCellLinkingException exception, final PropertiesGroupModelItem group) {
        final MessageProvider mp = environment.getMessageProvider();
        final String message =
                mp.translate("TraceMessage", "Unable to place properties group \"%1$s\" #%2$s on editor page: #%3$s");
        final String finalMessage =
                String.format(message, group.getTitle(), group.getId().toString(), exception.getMessage(mp));
        environment.getTracer().event(finalMessage);
    }    

    private void addCell(final PropertyCell<L, E, G> cell) throws CellIsNotEmptyException, AmbiguousCellLinkingException {
        logicalCells.set(cell);
        invalidate();
        for (int i = 1; i < cell.getColumnSpan(); i++) {
            logicalCells.set(new SpanCell<>(cell, i, 0));
        }
    }
    
    @SuppressWarnings("unchecked")
    private void addCell(final PropertiesGroupCell<L, E, G> cell) throws CellIsNotEmptyException, AmbiguousCellLinkingException {
        if (!cell.wasBinded()){
            cell.bind();
        }
        final int rowSpan = cell.getRowSpan();        
        updateVerticalSpanCells(cell, cell, 0, rowSpan);
        logicalCells.set(cell);
        SpanCell spanCell;
        for (int i = 1; i < cell.getColumnSpan(); i++) {
            spanCell = new SpanCell<>(cell, i, 0);
            updateVerticalSpanCells(cell, spanCell, i, rowSpan);
            logicalCells.set(spanCell);
        }
        invalidate();
    }
    
    @SuppressWarnings("unchecked")
    private void updateVerticalSpanCells(final EditorPageItemCell<L, E, G, ? extends ModelItem> baseCell, IPropertiesGridCell<L, E, G> cell, final int colSpan, final int rowSpan){
        SpanCell<L, E, G,? extends ModelItem> spanCell;        
        IPropertiesGridCell<L, E, G> currentLinkedCell;
        for (int row=1; row<rowSpan; row++){
            currentLinkedCell = cell.getLinkedCell(IPropertiesGridCell.ELinkageDirection.BOTTOM);
            if (currentLinkedCell instanceof SpanCell
                && ((SpanCell)currentLinkedCell).getEditorPageItemCell()==baseCell
                && ((SpanCell)currentLinkedCell).getRowSpan()==row
                && ((SpanCell)currentLinkedCell).getColumnSpan()==colSpan
                ){
                continue;
            }else if (currentLinkedCell!=null){
                currentLinkedCell.linkWith(null, IPropertiesGridCell.ELinkageDirection.TOP);
            }
            spanCell = new SpanCell<>(baseCell,colSpan,row);
            cell.linkWith(spanCell, IPropertiesGridCell.ELinkageDirection.BOTTOM);
            spanCell.linkWith(cell, IPropertiesGridCell.ELinkageDirection.TOP);
            cell = spanCell;
        }
        currentLinkedCell = cell.getLinkedCell(IPropertiesGridCell.ELinkageDirection.BOTTOM);
        if (currentLinkedCell!=null){
            currentLinkedCell.linkWith(null, IPropertiesGridCell.ELinkageDirection.TOP);
            cell.linkWith(null, IPropertiesGridCell.ELinkageDirection.BOTTOM);
        }
    }
    
    @SuppressWarnings("unchecked")
    public void updatePropertiesGroupCells(final Id modelItemId){
        final Collection<IPropertiesGridCell<L,E,G>> modelItemCells = logicalCells.find(new IPropertiesGridCells.CellsFinder<L, E, G>(){
            @Override
            public boolean isTarged(IPropertiesGridCell<L, E, G> cell) {
                if (cell instanceof MappedCell){
                    cell = ((MappedCell)cell).getActualCell();
                }
                if (cell instanceof EditorPageItemCell){
                    return ((EditorPageItemCell)cell).getModelItem().getId().equals(modelItemId);
                }else{
                    return false;
                }
            }
        });
        IPropertiesGridCell itemCell;
        for (IPropertiesGridCell cell: modelItemCells){            
            if (cell instanceof MappedCell){
                itemCell = ((MappedCell)cell).getActualCell();
            }else{
                itemCell = cell;
            }
            final int cellSpan = itemCell.getLinkedCellsCount(IPropertiesGridCell.ELinkageDirection.BOTTOM)+1;
            final int newSpan = itemCell.getRowSpan();
            if (cellSpan!=newSpan){
                updateVerticalSpanCells((EditorPageItemCell)itemCell, itemCell, 0, newSpan);
                IPropertiesGridCell currentCell = itemCell;
                for (int c=1; c<itemCell.getColumnSpan(); c++){
                    currentCell = currentCell.getLinkedCell(IPropertiesGridCell.ELinkageDirection.RIGHT);
                    updateVerticalSpanCells((EditorPageItemCell)itemCell, currentCell, c, newSpan);
                }
                invalidate();
            }
            final boolean isVisible = itemCell.isModelItemVisible();
            if (isVisible!=itemCell.isVisible()){
                ((EditorPageItemCell)itemCell).setVisible(isVisible);
                invalidate();
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void clearCell(IPropertiesGridCell<L, E, G> cell) {
        if (cell.getModelItem() == null) {//it is empty cell
            return;
        }
        while (cell instanceof MappedCell) {
            cell = ((MappedCell) cell).getActualCell();
        }
        if (cell instanceof SpanCell) {
            cell = ((SpanCell) cell).getEditorPageItemCell();
        }
        for (int i = 0, colSpan = cell.getColumnSpan(); i < colSpan && cell != null; i++) {
            logicalCells.clear(cell);
            cell = cell.getLinkedCell(IPropertiesGridCell.ELinkageDirection.RIGHT);
        }
        invalidate();
    }

    public void clearRightButtomCell() {
        final IPropertiesGridCell<L, E, G> cell = logicalCells.getRightButtomCell();
        if (cell != null) {
            logicalCells.clear(cell);
            invalidate();
        }
    }

    public void clear() {
        logicalCells.clear();
        invalidate();
    }

    public Collection<IPropertiesGridCell<L, E, G>> findCells(final IPropertiesGridCells.CellsFinder<L, E, G> finder) {
        return logicalCells.find(finder);
    }

    public IPropertiesGridCells<L, E, G> getCells() {
        if (visualCells == null) {
            initVisualCells();
        }
        return visualCells;
    }

    public void invalidate() {
        visualCells = null;
    }

    private void initVisualCells() {
        IPropertiesGridCells<L, E, G> sourceCells = logicalCells;
        VisualCells<L, E, G> cells;
        boolean wasShifting;
        int counter = 0;
        do{
            cells = new VisualCells<>();
            final int[] visualColumns = calcVisualColumns(sourceCells);
            wasShifting = shiftCellsUp(sourceCells, cells, visualColumns);
            if (logicalCells==sourceCells){
                wasShifting = shiftCellsLeft(cells) || wasShifting;
            }else if (wasShifting){
                wasShifting = shiftCellsLeft(cells);
            }
            sourceCells = cells;
            counter++;
        }while (wasShifting && counter<100);
        visualCells = cells;
    }
    
    private int[] calcVisualColumns(IPropertiesGridCells<L, E, G> cells){
        final int colCount = cells.getColumnsCount();
        final int[] visualColumns = new int[colCount];//shifted column indexes
        int invisibleColumnsCount = 0;
        for (int col = 0; col < colCount; col++) {
            if (cells.isVisibleColumn(col)) {
                visualColumns[col] = col - invisibleColumnsCount;
            } else {
                visualColumns[col] = -1;
                invisibleColumnsCount++;
            }
        }
        return visualColumns;
    }
    
    private boolean shiftCellsUp(IPropertiesGridCells<L, E, G> source, VisualCells<L, E, G> target, final int[] visualColumns ){
        int colCount = source.getColumnsCount();
        int rowCount = source.getRowsCount();
        IPropertiesGridCell<L, E, G> currentCell;
        //moving rows to top
        boolean wasRowShifting=false;
        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < colCount; col++) {
                if (visualColumns[col] > -1) {//column has visible items                                                            
                    currentCell = source.get(col, row);
                    if (!currentCell.isEmpty()) {
                        final int visualCol = visualColumns[col];
                        final int visualRow = calcVisualRow(target, visualCol, currentCell);
                        if (currentCell.getRow() != visualRow) {
                            col = placeCellsToRow(target, currentCell, visualColumns, visualRow, col);
                            wasRowShifting = true;
                        } else {
                            final int visualColSpan = currentCell.getColumnSpan();
                            final int visualRowSpan = currentCell.getRowSpan();
                            target.set(new VisualCell<>(currentCell, visualCol, visualRow, visualColSpan, visualRowSpan));                            
                        }
                    }
                }
            }
        }
        return wasRowShifting;
    }
    
    private boolean shiftCellsLeft(VisualCells<L, E, G> cells){
        final int colCount = cells.getColumnsCount();
        final int rowCount = cells.getRowsCount();
        VisualCell<L, E, G> visualCell;
        IPropertiesGridCell<L, E, G> currentCell;
        boolean wasColShifting=false;
        for (int col = 1; col < colCount; col++) {//do not move items in first column
            for (int row = 0; row < rowCount; row++) {
                currentCell = cells.get(col, row);
                if (currentCell instanceof VisualCell && !currentCell.isEmpty()) {
                    visualCell = (VisualCell<L, E, G>) currentCell;
                    final int visualColumn = calcVisualCol(cells, row, visualCell);
                    if (visualColumn != visualCell.getColumn()) {
                        cells.move(visualCell, visualColumn, row);
                    }
                }
            }
        }
        return wasColShifting;
    }

    private int calcVisualRow(final IPropertiesGridCells<L, E, G> cells, final int column, final IPropertiesGridCell<L, E, G> cell) {//NOPMD
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

    private int calcVisualCol(final IPropertiesGridCells<L, E, G> cells, final int row, final VisualCell<L, E, G> cell) {//NOPMD
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

    private int placeCellsToRow(final VisualCells<L, E, G> cells, final IPropertiesGridCell<L, E, G> cell, final int[] visualColumns, final int visualRow, int col) {
        for (IPropertiesGridCell<L, E, G> movingCell = cell; movingCell != null; movingCell = movingCell.getLinkedCell(IPropertiesGridCell.ELinkageDirection.RIGHT)) {
            final int visualCol = visualColumns[col];
            if (visualCol>=0){
                final int visualColSpan = movingCell.getColumnSpan();
                final int visualRowSpan = movingCell.getRowSpan();
                cells.set(new VisualCell<>(movingCell, visualCol, visualRow, visualColSpan, visualRowSpan));
            }
            col++;
        }
        return col - 1;
    }

    public List<Property> getProperties() {
        final IPropertiesGridCells.CellsFinder<L, E, G> finder = new IPropertiesGridCells.CellsFinder<L, E, G>() {
            @Override
            public boolean isTarged(IPropertiesGridCell<L, E, G> cell) {
                return cell.getPropertyEditor() != null;
            }
        };
        final Collection<IPropertiesGridCell<L, E, G>> cells = findCells(finder);
        final List<Property> properties = new LinkedList<>();
        final List<IPropertiesGridCell<L, E, G>> sortedCells = new LinkedList<>(cells);
        Collections.sort(sortedCells, new Comparator<IPropertiesGridCell<L, E, G>>() {
            @Override
            public int compare(IPropertiesGridCell<L, E, G> cell1, IPropertiesGridCell<L, E, G> cell2) {
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
        Property property;
        for (IPropertiesGridCell cell : sortedCells) {
            if (cell.getModelItem() instanceof Property){
                property = (Property)cell.getModelItem();
                if (!properties.contains(property)){
                    properties.add(property);
                }
            }
        }
        return Collections.<Property>unmodifiableList(properties);
    }

    public Collection<PropertyCell<L, E, G>> findCellsForProperty(final Id propertyId) {
        final IPropertiesGridCells.CellsFinder<L, E, G> finder = new IPropertiesGridCells.CellsFinder<L, E, G>() {
            @Override
            public boolean isTarged(IPropertiesGridCell<L, E, G> cell) {
                return cell.getPropertyEditor() != null
                        && cell.getModelItem() != null
                        && cell.getModelItem().getId().equals(propertyId);
            }
        };
        final Collection<PropertyCell<L, E, G>> result = new LinkedList<>();
        final Collection<IPropertiesGridCell<L, E, G>> cells = findCells(finder);
        IPropertiesGridCell<L, E, G> resultCell;
        for (IPropertiesGridCell<L, E, G> cell : cells) {
            resultCell = cell;
            while (resultCell instanceof MappedCell) {
                resultCell = ((MappedCell<L, E, G>) resultCell).getActualCell();
            }
            if (resultCell instanceof PropertyCell) {
                result.add((PropertyCell<L, E, G>) resultCell);
            }
        }
        return result;
    }

    public Collection<IPropertiesGridCell<L, E, G>> findVisibleEditorPageItemCells() {
        final IPropertiesGridCells.CellsFinder<L, E, G> finder = new IPropertiesGridCells.CellsFinder<L, E, G>() {
            @Override
            public boolean isTarged(IPropertiesGridCell<L, E, G> cell) {
                return (cell.getPropertyEditor() != null || cell.getPropertiesGroupWidget()!=null)
                   && cell.getModelItem()!=null
                   && cell.isModelItemVisible();
            }
        };
        return findCells(finder);
    }
    
    public Collection<PropertiesGroupCell<L, E , G>> findVisiblePropertyGroupCells(){
        final IPropertiesGridCells.CellsFinder<L, E, G> finder = new IPropertiesGridCells.CellsFinder<L, E, G>() {
            @Override
            public boolean isTarged(IPropertiesGridCell<L, E, G> cell) {
                return cell.getPropertyEditor() != null
                        && cell.getModelItem() != null
                        && cell.isModelItemVisible();
            }
        };
        final Collection<PropertiesGroupCell<L, E, G>> result = new LinkedList<>();
        final Collection<IPropertiesGridCell<L, E, G>> cells = findCells(finder);
        IPropertiesGridCell<L, E, G> resultCell;
        for (IPropertiesGridCell<L, E, G> cell : cells) {
            resultCell = cell;
            while (resultCell instanceof MappedCell) {
                resultCell = ((MappedCell<L, E, G>) resultCell).getActualCell();
            }
            if (resultCell instanceof PropertiesGroupCell) {
                result.add((PropertiesGroupCell<L, E, G>) resultCell);
            }
        }
        return result;
        
    }
    
    public boolean isSomeCellVisible(){
        final IPropertiesGridCells.CellsFinder<L, E, G> finder = new IPropertiesGridCells.CellsFinder<L, E, G>() {
            private boolean visibleCellFound;
            @Override
            public boolean isTarged(IPropertiesGridCell<L, E, G> cell) {
                visibleCellFound = 
                    (cell.getPropertyEditor() != null || cell.getPropertiesGroupWidget()!=null)
                   && cell.getModelItem()!=null
                   && cell.isModelItemVisible();
                return visibleCellFound;
            }

            @Override
            public boolean isCancelled() {
                return visibleCellFound;
            }                        
        };
        return !findCells(finder).isEmpty();
    }

    @SuppressWarnings("unchecked")
    public Collection<EditorPageItemCell<L, E, G,? extends PropertiesGroupModelItem>> findEditorPageItemCells() {
        final IPropertiesGridCells.CellsFinder<L, E, G> finder = new IPropertiesGridCells.CellsFinder<L, E, G>() {
            @Override
            public boolean isTarged(IPropertiesGridCell<L, E, G> cell) {
                return (cell.getPropertyEditor() != null || cell.getPropertiesGroupWidget()!=null)
                           && cell.getModelItem() != null;
            }
        };
        final Collection<EditorPageItemCell<L, E, G,? extends PropertiesGroupModelItem>> result = new LinkedList<>();
        final Collection<IPropertiesGridCell<L, E, G>> cells = findCells(finder);
        IPropertiesGridCell<L, E, G> resultCell;
        for (IPropertiesGridCell<L, E, G> cell : cells) {
            resultCell = cell;
            while (resultCell instanceof MappedCell) {
                resultCell = ((MappedCell<L,E,G>) resultCell).getActualCell();
            }
            if (resultCell instanceof EditorPageItemCell) {
                result.add((EditorPageItemCell<L, E, G,? extends PropertiesGroupModelItem>) resultCell);
            }
        }
        return result;
    }

    public int getPropetyCellsCount() {
        final IPropertiesGridCells.CellsFinder<L, E, G> finder = new IPropertiesGridCells.CellsFinder<L, E, G>() {
            @Override
            public boolean isTarged(IPropertiesGridCell<L, E, G> cell) {
                return cell.getPropertyEditor() != null
                        && cell.getModelItem() != null;
            }
        };
        return findCells(finder).size();
    }

    public int getLastLogicalColumnIndex() {
        final IPropertiesGridCell<L, E, G> cell = logicalCells.getRightButtomCell();
        return cell == null ? -1 : cell.getColumn();
    }

    public int getLastLogicalRowIndex(final int logicalColumnIndex) {
        return logicalCells.getLastRowIndexInColumn(logicalColumnIndex);
    }
    
    public IClientEnvironment getEnvironment(){
        return environment;
    }
}
