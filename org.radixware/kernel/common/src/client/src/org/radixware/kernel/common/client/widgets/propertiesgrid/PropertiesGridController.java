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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.items.EditorPageModelItem;
import org.radixware.kernel.common.client.models.items.ModelItem;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.views.IPropEditor;
import org.radixware.kernel.common.client.widgets.IModelWidget;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;


public final class PropertiesGridController<L extends IModelWidget, E extends IModelWidget> implements IModelWidget {

    private final PropertiesGridModel<L, E> model;
    private final IPropertiesGridPresenter<L, E> presenter;
    private IPropertiesGridCells<L, E> currentVisualCells = new VisualCells<>();
    private boolean wasBinded;

    public PropertiesGridController(final IPropertiesGridPresenter<L, E> presenter, final IClientEnvironment environment) {
        this.presenter = presenter;
        model = new PropertiesGridModel<>(environment);
    }

    public int getColumnsCount() {
        return model.getCells().getColumnsCount();
    }

    public int getRowsCount() {
        return model.getCells().getRowsCount();
    }

    public IPropertiesGridPresenter.IPresenterItem<L,E> addProperty(final Property property, final int column, final int row) {
        return addProperty(property, column, row, 1);
    }

    public IPropertiesGridPresenter.IPresenterItem<L,E> addProperty(final Property property, final int column, final int row, final int columnSpan) {
        final int actualColumn = column < 0 ? model.getLastLogicalColumnIndex() + 1 : column;
        final int actualRow = row < 0 ? model.getLastLogicalRowIndex(actualColumn) + 1 : row;

        final PropertyCell<L,E> cell = model.addProperty(presenter, property, actualColumn, actualRow, columnSpan < 1 ? 1 : columnSpan);
        if (cell != null && wasBinded) {
            cell.bind();
            property.subscribe(this);
            rebuild(true);
        }
        return cell;
    }

    public IPropertiesGridPresenter.IPresenterItem<L,E> addProperty(final Property property, final int column, final int row, final int columnSpan, boolean stickToLeft, boolean stickToRight) {
        final int actualColumn = column < 0 ? model.getLastLogicalColumnIndex() + 1 : column;
        final int actualRow = row < 0 ? model.getLastLogicalRowIndex(actualColumn) + 1 : row;

        final PropertyCell<L,E> cell = model.addProperty(presenter, property, actualColumn, actualRow, columnSpan < 1 ? 1 : columnSpan, stickToLeft, stickToRight);
        if (cell != null && wasBinded) {
            cell.bind();
            property.subscribe(this);
            rebuild(true);
        }
        return cell;
    }

    public IPropertiesGridPresenter.IPresenterItem<L,E> addProperty(final Property property) {
        IPropertiesGridPresenter.IPresenterItem<L,E> item = addPropertyImpl(property);
        if (item!=null) {
            rebuild(true);
        }
        return item;
    }

    public List<IPropertiesGridPresenter.IPresenterItem<L,E>> addProperties(final Collection<Property> properties) {
        final List<IPropertiesGridPresenter.IPresenterItem<L,E>> items = new LinkedList<>();
        for (Property property : properties) {
            items.add(addPropertyImpl(property));
        }
        if (!items.isEmpty()) {
            rebuild(true);
        }
        return items;
    }

    public List<Property> getProperties() {
        return model.getProperties();
    }

    private IPropertiesGridPresenter.IPresenterItem<L,E> addPropertyImpl(final Property property) {
        final PropertyCell<L,E> cell = model.addProperty(presenter, property);
        if (cell != null) {
            if (wasBinded) {
                cell.bind();
                property.subscribe(this);
            }
        }
        return cell;
    }

    private boolean addPropertyImpl(final RadStandardEditorPageDef.PageItem pageItem, final Property property) {
        final PropertyCell cell = model.addProperty(presenter, pageItem, property);
        if (cell == null) {
            return false;
        }
        cell.bind();
        property.subscribe(this);
        return true;
    }

    public void removeProperty(final Property property) {
        final Collection<PropertyCell<L, E>> cells = model.findCellsForProperty(property.getId());
        if (!cells.isEmpty()) {
            for (PropertyCell<L, E> cell : cells) {
                model.clearCell(cell);
                cell.getProperty().unsubscribe(this);
            }
            rebuild(true);
        }
    }

    public void clear() {
        final List<Property> properties = getProperties();
        final Collection<PropertyCell<L, E>> cells = model.findPropertyCells();
        for (PropertyCell<L, E> cell : cells) {
            cell.close(presenter);
        }
        model.clear();
        for (Property property : properties) {
            property.unsubscribe(this);
        }
        rebuild(true);
    }

    private void rebuild(final boolean destroyUnusedWidgets) {
        if (wasBinded) {
            model.invalidate();
            final IPropertiesGridCells<L, E> cells = model.getCells();
            final int newRowsCount = cells.getRowsCount();
            final int newColumnsCount = cells.getColumnsCount();
            final int oldColumnsCount = currentVisualCells.getColumnsCount();
            final int oldRowsCount = currentVisualCells.getRowsCount();
            presenter.beforeUpdateCellsPresentation(newColumnsCount, newRowsCount);
            try {
                final Map<E, IPropertiesGridCell<L, E>> unusedEditors = new HashMap<>();
                //cleaning current cells
                for (int row = 0; row < oldRowsCount; row++) {
                    for (int col = 0; col < oldColumnsCount;) {
                        final IPropertiesGridCell<L, E> oldCell = currentVisualCells.get(col, row);
                        if (oldCell.getPropertyEditor() != null) {
                            final IPropertiesGridCell<L, E> newCell;
                            if (col < newColumnsCount && row < newRowsCount) {
                                newCell = cells.get(col, row);
                            } else {
                                newCell = null;
                            }
                            if (!theSameCell(oldCell, newCell)) {
                                clearCell(oldCell, false);
                                if (destroyUnusedWidgets) {
                                    unusedEditors.put(oldCell.getPropertyEditor(), oldCell);
                                }
                            }
                        }
                        col += oldCell.getColumnSpan();
                    }
                }
                //presenting new cells and detecting span columns
                final boolean spanColumns[] = new boolean[newColumnsCount];
                for (int col = 0; col < newColumnsCount; col++) {
                    spanColumns[col] = true;
                }
                for (int row = 0; row < newRowsCount; row++) {
                    for (int col = 0; col < newColumnsCount;) {
                        final IPropertiesGridCell<L, E> newCell = cells.get(col, row);
                        final int span = newCell.getColumnSpan();
                        if (newCell.getPropertyEditor() != null) {
                            if (newCell.isVisible()) {
                                final boolean needToPresent;
                                if (col < oldColumnsCount && row < oldRowsCount) {
                                    final IPropertiesGridCell<L, E> oldCell = currentVisualCells.get(col, row);
                                    needToPresent = !oldCell.isVisible() || !theSameCell(oldCell, newCell);
                                } else {
                                    needToPresent = true;
                                }
                                if (needToPresent) {
                                    presenter.presentCell(newCell, newColumnsCount);
                                }
                                spanColumns[col] = false;
                            }
                            if (destroyUnusedWidgets) {
                                unusedEditors.remove(newCell.getPropertyEditor());
                            }
                        }
                        col += span;
                    }
                }
                //setup span columns
                for (int col = 0; col < newColumnsCount; col++) {
                    if (spanColumns[col]) {
                        presenter.presentSpanColumn(col);
                    }
                }
                if (destroyUnusedWidgets) {//destroy unused widgets
                    for (IPropertiesGridCell<L, E> cell : unusedEditors.values()) {
                        cell.close(presenter);
                    }
                }
            } finally {
                presenter.afterUpdateCellsPresentation();
            }
            currentVisualCells = cells;
        }
    }

    private void clearCells(final int colStart, final int colEnd, final int row, final boolean destroy) {
        for (int col = colStart; col < colEnd;) {
            final IPropertiesGridCell<L, E> oldCell = currentVisualCells.get(col, row);
            if (oldCell.isVisible() && oldCell.getPropertyEditor() != null) {
                clearCell(oldCell, destroy);
            }
            col += oldCell.getColumnSpan();
        }
    }

    private void clearCell(final IPropertiesGridCell<L, E> cell, final boolean destroy) {
        presenter.clearCellPresentation(cell);
        if (destroy) {
            cell.close(presenter);
        }
    }

    private boolean theSameCell(final IPropertiesGridCell<L, E> cell1, final IPropertiesGridCell<L, E> cell2) {
        if (cell1 == null || cell2 == null) {
            return false;
        }
        final Id propertyId1 = cell1.getProperty() == null ? null : cell1.getProperty().getId();
        final Id propertyId2 = cell2.getProperty() == null ? null : cell2.getProperty().getId();
        return Utils.equals(propertyId1, propertyId2)
                && cell1.isVisible() == cell2.isVisible()
                && cell1.getColumnSpan() == cell2.getColumnSpan();
    }

    private void syncWithPageModelItem(final EditorPageModelItem pageItem) {
        final int inPageCount = pageItem.getProperties().size();
        final int inGridCount = model.getPropetyCellsCount();
        if (inPageCount > inGridCount) {
            for (int i = inGridCount; i < inPageCount; i++) {
                addPropertyImpl(pageItem.getProperties().get(i));
            }
            rebuild(false);
        } else if (inGridCount > inPageCount) {
            for (int i = inGridCount - 1; i >= inPageCount; i--) {
                model.clearRightButtomCell();
            }
            rebuild(true);
        }
    }

    @Override
    public void refresh(final ModelItem changedItem) {
        if (changedItem instanceof EditorPageModelItem) {
            syncWithPageModelItem((EditorPageModelItem) changedItem);
        } else if (changedItem instanceof Property) {
            final Collection<PropertyCell<L, E>> cells = model.findPropertyCells();            
            boolean needForRebuild = false;
            for (PropertyCell<L, E> cell : cells) {
                final boolean isVisible = cell.getProperty().isVisible();
                if (cell.isVisible() && !isVisible) {
                    cell.setVisible(false);
                    needForRebuild = true;
                } else if (!cell.isVisible() && isVisible) {
                    cell.setVisible(true);
                    needForRebuild = true;
                }
            }
            if (needForRebuild) {
                rebuild(false);
            } else {
                presenter.updateGeometry();
            }
        }
    }

    @Override
    public boolean setFocus(final Property property) {
        if (wasBinded) {
            final Collection<PropertyCell<L, E>> cells = model.findCellsForProperty(property.getId());
            for (PropertyCell<L, E> cell : cells) {
                if (cell.isVisible() && cell.getProperty().isEnabled() && cell.getPropertyEditor() instanceof IModelWidget && ((IModelWidget) cell.getPropertyEditor()).setFocus(property)) {
                    presenter.scrollToCell(cell);
                    return true;
                }
            }
        }
        return false;
    }

    public void finishEdit() {
        final Collection<IPropertiesGridCell<L, E>> cells = model.findVisiblePropertyCells();
        for (IPropertiesGridCell<L, E> cell : cells) {
            if (cell.getPropertyEditor() instanceof IPropEditor) {
                ((IPropEditor) cell.getPropertyEditor()).finishEdit();
            }
        }
    }

    @Override
    public void bind() {
        bind(null);
    }

    public void bind(final EditorPageModelItem pageItem) {
        if (pageItem != null) {
            if (pageItem.def instanceof RadStandardEditorPageDef == false) {
                throw new IllegalUsageError("PropertieasGrid can be used only with standard editor page");
            }
            final RadStandardEditorPageDef pageDef = (RadStandardEditorPageDef) pageItem.def;
            final Collection<RadStandardEditorPageDef.PageItem> pageItems = pageDef.getRootPropertiesGroup().getPageItems();
            final Model owner = pageItem.getOwner();
            Property property;
            for (RadStandardEditorPageDef.PageItem item : pageItems) {
                if (owner.getDefinition().isPropertyDefExistsById(item.getItemId())) {
                    property = owner.getProperty(item.getItemId());
                    if (!(owner instanceof EntityModel) || ((EntityModel) owner).isExists() || property.getDefinition().getType() != EValType.OBJECT) {
                        addPropertyImpl(item, property);
                    }
                }
            }
            pageItem.subscribe(this);
            syncWithPageModelItem(pageItem);
        }
        final Collection<PropertyCell<L, E>> cells = model.findPropertyCells();
        for (PropertyCell cell : cells) {
            if (!cell.wasBinded()) {
                cell.bind();
            }
            cell.setVisible(cell.getProperty().isVisible());
            cell.getProperty().subscribe(this);
        }
        wasBinded = true;
        rebuild(false);
    }

    public void close(final EditorPageModelItem pageItem) {
        final int oldRowsCount = currentVisualCells.getRowsCount();
        final int oldColumnsCount = currentVisualCells.getColumnsCount();
        presenter.beforeUpdateCellsPresentation(0, 0);
        try {
            for (int row = 0; row < oldRowsCount; row++) {
                clearCells(0, oldColumnsCount, row, true);
            }
        } finally {
            presenter.afterUpdateCellsPresentation();
        }
        currentVisualCells = new VisualCells<>();
        clear();
        if (pageItem != null) {
            pageItem.unsubscribe(this);
        }
    }

    public int calcMaxHeight() {
        int maxHeight = 0;
        int cellHeight;
        final Collection<IPropertiesGridCell<L, E>> cells = model.findVisiblePropertyCells();
        for (IPropertiesGridCell<L, E> cell : cells) {
            cellHeight = presenter.getCellHeight(cell);
            if (cellHeight > maxHeight) {
                maxHeight = cellHeight;
            }
        }
        return maxHeight;
    }
}