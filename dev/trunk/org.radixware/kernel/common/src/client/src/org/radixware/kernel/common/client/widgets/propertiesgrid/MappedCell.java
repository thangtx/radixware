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


final class MappedCell<L extends IModelWidget, E extends IModelWidget, G extends IPropertiesGroupWidget> implements IPropertiesGridCell<L, E, G> {

    private final IPropertiesGridCell<L, E, G> actualCell;
    private final int column, row;

    public MappedCell(final IPropertiesGridCell<L, E, G> actualCell, final int column, final int row) {
        this.actualCell = actualCell;
        this.column = column;
        this.row = row;
    }

    @Override
    public boolean isEmpty() {
        return actualCell.isEmpty();
    }

    @Override
    public boolean isVisible() {
        return actualCell.isVisible();
    }

    @Override
    public L getPropertyLabel() {
        return actualCell.getPropertyLabel();
    }

    @Override
    public E getPropertyEditor() {
        return actualCell.getPropertyEditor();
    }

    @Override
    public G getPropertiesGroupWidget() {
        return actualCell.getPropertiesGroupWidget();
    }        

    @Override
    public ModelItem getModelItem() {
        return actualCell.getModelItem();
    }

    @Override
    public boolean isModelItemReadOnly() {
        return actualCell.isModelItemReadOnly();
    }

    @Override
    public boolean isModelItemVisible() {
        return actualCell.isModelItemVisible();
    }

    @Override
    public int getColumn() {
        return column;
    }

    @Override
    public int getRow() {
        return row;
    }

    @Override
    public int getColumnSpan() {
        return actualCell.getColumnSpan();
    }
    
    @Override
    public int getRowSpan() {
        return actualCell.getRowSpan();
    }    

    @Override
    public IPropertiesGridCell<L, E, G> getLinkedCell(ELinkageDirection direction) {
        final IPropertiesGridCell<L, E, G> linkedCell = actualCell.getLinkedCell(direction);
        if (linkedCell == null) {
            return null;
        } else {
            final int mappedCol = direction == ELinkageDirection.LEFT ? column - 1 : column + 1;
            return new MappedCell<L, E, G>(linkedCell, mappedCol, row);
        }
    }

    @Override
    public boolean isLinked(ELinkageDirection direction) {        
        return actualCell.isLinked(direction) && actualCell.getLinkedCell(direction)!=null;
    }

    @Override
    public String getDescription(MessageProvider mp) {
        return actualCell.getDescription(mp);
    }

    @Override
    public void linkWith(IPropertiesGridCell<L, E, G> cell, ELinkageDirection direction) {
        throw new UnsupportedOperationException("Can't link mapped column");
    }

    @Override
    public int getLinkedCellsCount(ELinkageDirection direction) {
        return actualCell.getLinkedCellsCount(direction);
    }

    @Override
    public IPropertiesGridCell<L, E, G> createCopy() {
        return new MappedCell<L, E, G>(actualCell, column, row);
    }

    @Override
    public void close(final IPropertiesGridPresenter<L, E, G> presenter) {
        actualCell.close(presenter);
    }

    public IPropertiesGridCell<L, E, G> getActualCell() {
        return actualCell;
    }
}
