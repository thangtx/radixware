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


final class EmptyCell<L extends IModelWidget, E extends IModelWidget, G extends IPropertiesGroupWidget> implements IPropertiesGridCell<L, E, G> {

    final int row, column;

    public EmptyCell(final int column, final int row) {
        this.row = row;
        this.column = column;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public L getPropertyLabel() {
        return null;
    }

    @Override
    public E getPropertyEditor() {
        return null;
    }

    @Override
    public ModelItem getModelItem() {
        return null;
    }

    @Override
    public G getPropertiesGroupWidget() {
        return null;
    }

    @Override
    public boolean isModelItemVisible() {
        return false;
    }

    @Override
    public boolean isModelItemReadOnly() {
        return false;
    }

    @Override
    public int getRowSpan() {
        return 1;
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
        return 1;
    }

    @Override
    public IPropertiesGridCell<L, E, G> getLinkedCell(final ELinkageDirection direction) {
        return null;
    }

    @Override
    public boolean isLinked(final ELinkageDirection direction) {
        return false;
    }

    @Override
    public void linkWith(final IPropertiesGridCell<L, E, G> cell, final ELinkageDirection direction) {
        throw new UnsupportedOperationException("Can't linL empty cell.");
    }

    @Override
    public String getDescription(final MessageProvider mp) {
        if (mp == null) {
            return "empty cell (column: " + String.valueOf(column) + "; row:" + String.valueOf(row) + ")";
        }
        return String.format(mp.translate("TraceMessage", "empty cell (column: %1$s;row: %2$s)"), column, row);
    }

    @Override
    public String toString() {
        return getDescription(null);
    }

    @Override
    public int getLinkedCellsCount(ELinkageDirection direction) {
        return 0;
    }

    @Override
    public IPropertiesGridCell<L, E, G> createCopy() {
        return new EmptyCell<L, E, G>(column, row);
    }

    @Override
    public void close(IPropertiesGridPresenter<L, E, G> presenter) {
        //do nothing
    }        
}
