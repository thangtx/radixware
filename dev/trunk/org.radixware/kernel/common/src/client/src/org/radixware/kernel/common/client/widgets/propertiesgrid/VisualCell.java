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

import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.models.items.ModelItem;
import org.radixware.kernel.common.client.views.IPropertiesGroupWidget;
import org.radixware.kernel.common.client.widgets.IModelWidget;
import org.radixware.kernel.common.client.widgets.propertiesgrid.IPropertiesGridCell.ELinkageDirection;


final class VisualCell<L extends IModelWidget, E extends IModelWidget, G extends IPropertiesGroupWidget> implements IPropertiesGridCell<L, E, G> {

    private final IPropertiesGridCell<L, E, G> logicalCell;
    private final boolean isVisible, isEmpty;
    private int column, row, colSpan, rowSpan;

    public VisualCell(final IPropertiesGridCell<L,E,G> logicalCell, 
                             final int column, 
                             final int row, 
                             final int colSpan,
                             final int rowSpan) {
        this.logicalCell = logicalCell;
        this.column = column;
        this.row = row;
        this.colSpan = colSpan;
        this.rowSpan = rowSpan;
        isVisible = logicalCell.isVisible();
        isEmpty = logicalCell.isEmpty();
    }

    @Override
    public boolean isEmpty() {
        return isEmpty;
    }

    @Override
    public boolean isVisible() {
        return isVisible;
    }

    @Override
    public boolean isLinked(final ELinkageDirection direction) {
        return logicalCell.isLinked(direction);
    }

    @Override
    public IPropertiesGridCell<L, E, G> getLinkedCell(final ELinkageDirection direction) {
        return logicalCell.getLinkedCell(direction);
    }

    @Override
    public void linkWith(final IPropertiesGridCell<L, E, G> cell, final ELinkageDirection direction) {
        throw new UnsupportedOperationException("Linking is not supported for VisualCell");
    }

    @Override
    public int getLinkedCellsCount(final ELinkageDirection direction) {
        return logicalCell.getLinkedCellsCount(direction);
    }

    @Override
    public String getDescription(final MessageProvider mp) {
        return logicalCell.getDescription(mp);
    }

    @Override
    public IPropertiesGridCell<L, E, G> createCopy() {
        return new VisualCell<L, E, G>(logicalCell, column, row, colSpan, rowSpan);
    }

    @Override
    public L getPropertyLabel() {
        return logicalCell.getPropertyLabel();
    }

    @Override
    public E getPropertyEditor() {
        return logicalCell.getPropertyEditor();
    }

    @Override
    public G getPropertiesGroupWidget() {
        return logicalCell.getPropertiesGroupWidget();
    }        

    @Override
    public ModelItem getModelItem() {
        return logicalCell.getModelItem();
    }

    @Override
    public boolean isModelItemVisible() {
        return logicalCell.isModelItemVisible();
    }

    @Override
    public boolean isModelItemReadOnly() {
        return logicalCell.isModelItemReadOnly();
    }            

    @Override
    public int getColumn() {
        return column;
    }

    public void setColumn(final int col) {
        column = col;
    }

    @Override
    public int getRow() {
        return row;
    }

    @Override
    public void close(final IPropertiesGridPresenter<L, E, G> presenter) {
        logicalCell.close(presenter);
    }

    public void setRow(final int row) {
        this.row = row;
    }

    @Override
    public int getColumnSpan() {
        return colSpan;
    }

    public void setColumnSpan(final int colSpan) {
        this.colSpan = colSpan;
    }
    
    @Override
    public int getRowSpan() {
        return rowSpan;
    }        
    
    public void setRowSpan(final int rowSpan){
        this.rowSpan = rowSpan;
    }    
}
