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
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.ModelWithPages;
import org.radixware.kernel.common.client.models.items.EditorPageModelItem;
import org.radixware.kernel.common.client.models.items.ModelItem;
import org.radixware.kernel.common.client.models.items.PropertiesGroupModelItem;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.utils.ThreadDumper;
import org.radixware.kernel.common.client.views.IPropEditor;
import org.radixware.kernel.common.client.views.IPropertiesGroupWidget;
import org.radixware.kernel.common.client.widgets.IModelWidget;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;


public final class PropertiesGridController<L extends IModelWidget, E extends IModelWidget, G extends IPropertiesGroupWidget> implements IModelWidget {

    private final static class RebuildTask{
        
        private final boolean destroyUnusedWidgets;
        private final Collection<Id> changedItems = new HashSet<>();
        
        public RebuildTask(final boolean destroyUnusedWidgets, final Id changedItemId){
            this.destroyUnusedWidgets = destroyUnusedWidgets;
            if (changedItemId!=null){
                changedItems.add(changedItemId);
            }
        }                

        public boolean isDestroyUnusedWidgets() {
            return destroyUnusedWidgets;
        }

        public Collection<Id> getChangedItems() {
            return Collections.unmodifiableCollection(changedItems);
        }        

        public RebuildTask merge(final boolean destroyUnusedWidgets, final Id changedItemId){
            final RebuildTask task = 
                new RebuildTask(destroyUnusedWidgets || this.destroyUnusedWidgets, changedItemId);
            task.changedItems.addAll(changedItems);
            return task;
        }        
    }
            
    private final PropertiesGridModel<L, E, G> model;
    private final IPropertiesGridPresenter<L, E, G> presenter;
    private final PropertiesGridController<L, E, G> parentGridController;
    private final Id modelItemId;
    private RebuildTask scheduledRebuildTask;    
    private IPropertiesGridCells<L, E, G> currentVisualCells = new VisualCells<>();
    private StringBuilder rebuildCalls = new StringBuilder();
    private boolean wasBinded;
    private boolean wasClosed;

    public PropertiesGridController(final IPropertiesGridPresenter<L, E, G> presenter, 
                                                   final Id modelItemId,
                                                   final PropertiesGridController<L, E, G> parentController, 
                                                   final IClientEnvironment environment) {
        this.presenter = presenter;
        model = new PropertiesGridModel<>(environment);
        parentGridController = parentController;
        this.modelItemId = modelItemId;
    }
    
    public PropertiesGridController(final IPropertiesGridPresenter<L, E, G> presenter, final IClientEnvironment environment) {
        this(presenter,null, null, environment);
    }

    public int getColumnsCount() {
        return model.getCells().getColumnsCount();
    }

    public int getRowsCount() {
        return model.getCells().getRowsCount();
    }

    public IPropertiesGridPresenter.IPresenterItem<L,E,G> addProperty(final Property property, final int column, final int row) {
        return addProperty(property, column, row, 1);
    }

    public IPropertiesGridPresenter.IPresenterItem<L,E,G> addProperty(final Property property, final int column, final int row, final int columnSpan) {
        final int actualColumn = column < 0 ? model.getLastLogicalColumnIndex() + 1 : column;
        final int actualRow = row < 0 ? model.getLastLogicalRowIndex(actualColumn) + 1 : row;

        final PropertyCell<L,E,G> cell = model.addProperty(presenter, property, actualColumn, actualRow, columnSpan < 1 ? 1 : columnSpan);
        if (cell != null && wasBinded) {
            cell.bind();
            property.subscribe(this);
            rebuild(true);
        }
        return cell;
    }

    public IPropertiesGridPresenter.IPresenterItem<L,E,G> addProperty(final Property property, final int column, final int row, final int columnSpan, boolean stickToLeft, boolean stickToRight) {
        final int actualColumn = column < 0 ? model.getLastLogicalColumnIndex() + 1 : column;
        final int actualRow = row < 0 ? model.getLastLogicalRowIndex(actualColumn) + 1 : row;

        final PropertyCell<L,E,G> cell = model.addProperty(presenter, property, actualColumn, actualRow, columnSpan < 1 ? 1 : columnSpan, stickToLeft, stickToRight);
        if (cell != null && wasBinded) {
            cell.bind();
            property.subscribe(this);
            rebuild(true);
        }
        return cell;
    }

    public IPropertiesGridPresenter.IPresenterItem<L,E,G> addProperty(final Property property) {
        IPropertiesGridPresenter.IPresenterItem<L,E,G> item = addPropertyImpl(property);
        if (item!=null) {
            rebuild(true);
        }
        return item;
    }

    public List<IPropertiesGridPresenter.IPresenterItem<L,E,G>> addProperties(final Collection<Property> properties) {
        final List<IPropertiesGridPresenter.IPresenterItem<L,E,G>> items = new LinkedList<>();
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

    private IPropertiesGridPresenter.IPresenterItem<L,E,G> addPropertyImpl(final Property property) {
        final PropertyCell<L,E,G> cell = model.addProperty(presenter, property);
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
    
    private boolean addPropertiesGroupImpl(final RadStandardEditorPageDef.PageItem pageItem, final PropertiesGroupModelItem group) {
        final PropertiesGroupCell cell = model.addPropertiesGroup(presenter, pageItem, group);
        if (cell == null) {
            return false;
        }
        if (wasBinded){
            rebuild(false);
        }
        return true;
    }

    public void removeProperty(final Property property) {
        final Collection<PropertyCell<L, E, G>> cells = model.findCellsForProperty(property.getId());
        if (!cells.isEmpty()) {
            for (PropertyCell<L, E, G> cell : cells) {
                model.clearCell(cell);
                cell.getModelItem().unsubscribe(this);
            }
            rebuild(true);
        }
    }

    public void clear() {
        wasClosed = true;
        final List<Property> properties = getProperties();
        final Collection<EditorPageItemCell<L, E, G,? extends PropertiesGroupModelItem>> cells = 
                model.findEditorPageItemCells();
        for (EditorPageItemCell<L, E, G,? extends PropertiesGroupModelItem> cell : cells) {
            cell.close(presenter);
        }
        model.clear();
        for (Property property : properties) {
            property.unsubscribe(this);
        }
    }    
    
    private void rebuild(final boolean destroyUnusedWidgets){
        rebuild(destroyUnusedWidgets, null);
    }
    
    private void rebuild(final boolean destroyUnusedWidgets, final Id changedItemId) {
        if (wasBinded && !wasClosed) {
            if (scheduledRebuildTask==null){
                scheduledRebuildTask = new RebuildTask(destroyUnusedWidgets, changedItemId);
            }else{
                scheduledRebuildTask = scheduledRebuildTask.merge(destroyUnusedWidgets, changedItemId);
            }
            if (rebuildCalls.length()>0){
                rebuildCalls.append('\n');
                rebuildCalls.append(ThreadDumper.dumpSync());                
                return;
            }
            rebuildCalls.append('\n');
            try{
                RebuildTask currentRebuildTask;
                int counter = 0;
                while(scheduledRebuildTask!=null && counter<20){
                    counter++;
                    currentRebuildTask = scheduledRebuildTask;
                    scheduledRebuildTask = null;
                    rebuild(currentRebuildTask);
                }
                if (counter==20){
                    final String traceMessage = 
                        model.getEnvironment().getMessageProvider().translate("TraceMessage", "Failed to build properties grid:%1$s");
                    model.getEnvironment().getTracer().error(String.format(traceMessage,rebuildCalls.toString()));
                }
            }finally{
                rebuildCalls.setLength(0);
            }
        }
    }

    private void rebuild(final RebuildTask rebuildTask) {
        model.invalidate();
        final IPropertiesGridCells<L, E, G> cells = model.getCells();
        final int newRowsCount = cells.getRowsCount();
        final int newColumnsCount = cells.getColumnsCount();
        final int oldColumnsCount = currentVisualCells.getColumnsCount();
        final int oldRowsCount = currentVisualCells.getRowsCount();
        presenter.beforeUpdateCellsPresentation(newColumnsCount, newRowsCount);
        try {
            final Map<IModelWidget, IPropertiesGridCell<L, E, G>> unusedWidgets = new HashMap<>();
            //cleaning current cells
            for (int row = 0; row < oldRowsCount; row++) {
                for (int col = 0; col < oldColumnsCount;) {
                    final IPropertiesGridCell<L, E, G> oldCell = currentVisualCells.get(col, row);
                    if (oldCell.getPropertyEditor() != null || oldCell.getPropertiesGroupWidget()!=null) {
                        final IPropertiesGridCell<L, E, G> newCell;
                        if (col < newColumnsCount && row < newRowsCount) {
                            newCell = cells.get(col, row);
                        } else {
                            newCell = null;
                        }
                        if (!theSameCell(oldCell, newCell) || isModelItemCell(newCell, rebuildTask.getChangedItems())) {
                            clearCell(oldCell, false);
                            if (rebuildTask.isDestroyUnusedWidgets()) {
                                if (oldCell.getPropertyEditor()==null){
                                    unusedWidgets.put(oldCell.getPropertiesGroupWidget(), oldCell);
                                }else{
                                    unusedWidgets.put(oldCell.getPropertyEditor(), oldCell);
                                }
                            }
                        }
                    }
                    col += oldCell.getColumnSpan();
                }
            }
            //presenting new cells and detecting span columns and spa rows
            final boolean spanColumns[] = new boolean[newColumnsCount];
            final boolean spanRows[] = new boolean[newRowsCount];
            for (int col = 0; col < newColumnsCount; col++) {
                spanColumns[col] = true;
            }
            for (int row = 0; row < newRowsCount; row++) {
                spanRows[row] = true;
            }                
            for (int row = 0; row < newRowsCount; row++) {
                for (int col = 0; col < newColumnsCount;) {
                    final IPropertiesGridCell<L, E, G> newCell = cells.get(col, row);
                    final int span = newCell.getColumnSpan();
                    if (newCell.getPropertyEditor() != null || newCell.getPropertiesGroupWidget() != null) {
                        if (newCell.isVisible()) {
                            final boolean needToPresent;
                            if (col < oldColumnsCount && row < oldRowsCount) {
                                final IPropertiesGridCell<L, E, G> oldCell = currentVisualCells.get(col, row);
                                needToPresent = !oldCell.isVisible() 
                                                           || !theSameCell(oldCell, newCell) 
                                                           || isModelItemCell(newCell, rebuildTask.getChangedItems());
                            } else {
                                needToPresent = true;
                            }
                            if (needToPresent) {
                                presenter.presentCell(newCell, newColumnsCount);
                            }
                            spanColumns[col] = false;
                            spanRows[row] = false;
                        }
                        if (rebuildTask.isDestroyUnusedWidgets() && newCell.getPropertyEditor()!=null) {
                            unusedWidgets.remove(newCell.getPropertyEditor());
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
            for (int row = 0; row < newRowsCount; row++) {
                if (spanRows[row]) {
                    presenter.presentSpanRow(row);
                }
            }
            if (rebuildTask.isDestroyUnusedWidgets()) {//destroy unused widgets
                for (IPropertiesGridCell<L, E, G> cell : unusedWidgets.values()) {
                    cell.close(presenter);
                }
            }
        } finally {
            presenter.afterUpdateCellsPresentation();
        }
        currentVisualCells = cells;
        if (parentGridController!=null){
            parentGridController.childControllerRebuilded(modelItemId);
        }
    }
    
    private boolean isModelItemCell(final IPropertiesGridCell<L, E, G> cell, final Collection<Id> itemIds){
        final ModelItem modelItem = cell==null ? null : cell.getModelItem();
        if (modelItem!=null){
            for (Id itemId: itemIds){
                if (itemId.equals(modelItem.getId())){
                    return true;
                }
            }
        }
        return false;
    }    

    private void clearCells(final int colStart, final int colEnd, final int row, final boolean destroy) {
        for (int col = colStart; col < colEnd;) {
            final IPropertiesGridCell<L, E, G> oldCell = currentVisualCells.get(col, row);
            if (oldCell.isVisible() 
                && oldCell.getPropertyEditor() != null
                && oldCell.getPropertiesGroupWidget()!=null) {
                clearCell(oldCell, destroy);
            }
            col += oldCell.getColumnSpan();
        }
    }

    private void clearCell(final IPropertiesGridCell<L, E, G> cell, final boolean destroy) {
        presenter.clearCellPresentation(cell);
        if (destroy) {
            cell.close(presenter);
        }
    }

    private boolean theSameCell(final IPropertiesGridCell<L, E, G> cell1, final IPropertiesGridCell<L, E, G> cell2) {
        if (cell1 == null || cell2 == null) {
            return false;
        }
        final Id propertyId1 = cell1.getModelItem() == null ? null : cell1.getModelItem().getId();
        final Id propertyId2 = cell2.getModelItem() == null ? null : cell2.getModelItem().getId();
        return propertyId1!=null && Utils.equals(propertyId1, propertyId2)
                && cell1.isVisible() == cell2.isVisible()
                && cell1.getColumnSpan() == cell2.getColumnSpan()
                && cell2.getRowSpan()==cell2.getRowSpan();
    }

    private void syncWithPageModelItem(final ModelItem pageItem) {
        final int inPageCount = getPropertiesCount(pageItem);
        final int inGridCount = model.getPropetyCellsCount();
        if (inPageCount > inGridCount) {
            for (int i = inGridCount; i < inPageCount; i++) {
                addPropertyImpl(getProperty(pageItem, i));
            }
            rebuild(false);
        } else if (inGridCount > inPageCount) {
            for (int i = inGridCount - 1; i >= inPageCount; i--) {
                model.clearRightButtomCell();
            }
            rebuild(true);
        }
    }
    
    private int getPropertiesCount(final ModelItem modelItem){
        if (modelItem instanceof EditorPageModelItem){
            return ((EditorPageModelItem)modelItem).getProperties().size();
        }
        if (modelItem instanceof PropertiesGroupModelItem){
            return ((PropertiesGroupModelItem)modelItem).getProperties().size();
        }
        return 0;
    }
    
    private Property getProperty(final ModelItem modelItem, final int index){
        if (modelItem instanceof EditorPageModelItem){
            return ((EditorPageModelItem)modelItem).getProperties().get(index);
        }
        if (modelItem instanceof PropertiesGroupModelItem){
            return ((PropertiesGroupModelItem)modelItem).getProperties().get(index);
        }
        return null;        
    }    

    @Override
    public void refresh(final ModelItem changedItem) {
        if (changedItem instanceof EditorPageModelItem) {
            syncWithPageModelItem((EditorPageModelItem) changedItem);
        } else if (changedItem instanceof Property) {
            final Collection<EditorPageItemCell<L, E, G,? extends PropertiesGroupModelItem>> cells = 
                model.findEditorPageItemCells();
            boolean needForRebuild = false;
            for (EditorPageItemCell<L, E, G,? extends PropertiesGroupModelItem> cell : cells) {
                final boolean isVisible = cell.isModelItemVisible();
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
            final Collection<PropertyCell<L, E, G>> propertyCells = model.findCellsForProperty(property.getId());
            for (PropertyCell<L, E, G> cell : propertyCells) {
                if (cell.isVisible() 
                    && cell.getModelItem().isEnabled() 
                    && cell.getPropertyEditor() instanceof IModelWidget 
                    && ((IModelWidget) cell.getPropertyEditor()).setFocus(property)) {
                    presenter.scrollToCell(cell);
                    return true;
                }
            }
            final Collection<PropertiesGroupCell<L, E, G>> groupCells = model.findVisiblePropertyGroupCells();
            for (PropertiesGroupCell<L, E, G> cell: groupCells){
                if (cell.isVisible() 
                    && cell.getModelItem().isEnabled() 
                    && cell.getPropertiesGroupWidget()!=null 
                    && cell.getPropertiesGroupWidget().setFocus(property)){
                    return true;
                }
            }
        }
        return false;
    }

    public void finishEdit() {
        final Collection<IPropertiesGridCell<L, E, G>> cells = model.findVisibleEditorPageItemCells();
        for (IPropertiesGridCell<L, E, G> cell : cells) {
            if (cell.getPropertyEditor() instanceof IPropEditor) {
                ((IPropEditor) cell.getPropertyEditor()).finishEdit();
            }else if (cell.getPropertiesGroupWidget() instanceof IPropertiesGroupWidget){
                ((IPropertiesGroupWidget)cell.getPropertiesGroupWidget()).finishEdit();
            }
        }
    }

    @Override
    public void bind() {
        bind(null,null);        
    }

    public void bind(final EditorPageModelItem pageItem) {
        if (pageItem == null) {
            bind (null,null);
        }else{
            if (pageItem.def instanceof RadStandardEditorPageDef == false) {
                throw new IllegalUsageError("PropertieasGrid can be used only with standard editor page");
            }
            final RadStandardEditorPageDef pageDef = (RadStandardEditorPageDef) pageItem.def;
            final Collection<RadStandardEditorPageDef.PageItem> pageItems = pageDef.getRootPropertiesGroup().getPageItems();
            bind(pageItem, pageItems);
        }            
    }
    
    public void bind(final PropertiesGroupModelItem groupItem){
        if (groupItem==null){
            bind(null,null);
        }else{
            bind(groupItem, groupItem.getDefinition().getPageItems());
        }
    }
    
    private void bind(final ModelItem modelItem, final Collection<RadStandardEditorPageDef.PageItem> pageItems){
        if (modelItem!=null && pageItems!=null && !pageItems.isEmpty()){
            final Model owner = modelItem.getOwner();
            Property property;            
            for (RadStandardEditorPageDef.PageItem item : pageItems) {
                if (item.getItemId().getPrefix()==EDefinitionIdPrefix.EDITOR_PAGE_PROP_GROUP){
                    addPropertiesGroupItem(owner,item);
                }else if (owner.getDefinition().isPropertyDefExistsById(item.getItemId())) {
                    property = owner.getProperty(item.getItemId());
                    addPropertyImpl(item, property);
                }
            }
            modelItem.subscribe(this);
            syncWithPageModelItem(modelItem);
        }
        final Collection<EditorPageItemCell<L, E, G,? extends PropertiesGroupModelItem>> cells = 
            model.findEditorPageItemCells();
        for (EditorPageItemCell cell : cells) {
            if (!cell.wasBinded()) {
                cell.bind();
            }
            cell.setVisible(cell.isModelItemVisible());
            cell.getModelItem().subscribe(this);
        }
        wasBinded = true;
        wasClosed = false;
        rebuild(false);
    }
    
    private void addPropertiesGroupItem(final Model owner, final RadStandardEditorPageDef.PageItem groupPageItem){
        if (owner instanceof ModelWithPages){            
            final ModelWithPages model = (ModelWithPages)owner;
            final Id propertiesGroupId = groupPageItem.getItemId();
            if (model.isPropertiesGroupExists(propertiesGroupId)){
                final PropertiesGroupModelItem propGroup = model.getPropertiesGroup(propertiesGroupId);
                addPropertiesGroupImpl(groupPageItem, propGroup);
            }
        }        
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
    
    public boolean isSomeCellVisible(){
        return model.isSomeCellVisible();
    }

    public int calcMaxHeight() {
        int maxHeight = 0;
        int cellHeight;
        final Collection<IPropertiesGridCell<L, E, G>> cells = model.findVisibleEditorPageItemCells();
        for (IPropertiesGridCell<L, E, G> cell : cells) {
            cellHeight = presenter.getCellHeight(cell);
            if (cellHeight > maxHeight) {
                maxHeight = cellHeight;
            }
        }
        return maxHeight;
    }
    
    public int getVisibleRowsCount(){
        final IPropertiesGridCells<L, E, G> cells = model.getCells();        
        IPropertiesGridCell<L, E, G> cell;
        int totalRowsCount = 0;
        for (int col=0, columnsCount=cells.getColumnsCount(); col<columnsCount; col++){
            int visibleRowsInColumn = 0;
            for (int row=0,rowsCount=cells.getRowsCount(); row<rowsCount; row++){
                cell = cells.get(col, row);
                if (cell instanceof PropertiesGroupCell){
                    visibleRowsInColumn+=((PropertiesGroupCell)cell).getVisibleRows();
                }else{
                    visibleRowsInColumn++;
                }
            }
            totalRowsCount = Math.max(totalRowsCount, visibleRowsInColumn);
        }
        return totalRowsCount;
    }
    
    public boolean wasBinded(){
        return wasBinded;
    }
    
    private void childControllerRebuilded(final Id modelItemId){        
        model.updatePropertiesGroupCells(modelItemId);
        rebuild(false,modelItemId);
    }
}