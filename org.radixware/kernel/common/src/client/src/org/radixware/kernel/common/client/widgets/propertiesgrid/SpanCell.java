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


class SpanCell<L extends IModelWidget, E extends IModelWidget, G extends IPropertiesGroupWidget, I extends ModelItem> extends AbstractLinkableCell<L, E, G> {

    private final EditorPageItemCell<L, E, G, I> baseCell;
    private final int spanRow, spanColumn;

    public SpanCell(final EditorPageItemCell<L, E, G, I> baseCell, final int spanColumn, final int spanRow) {
        this.baseCell = baseCell;        
        this.spanRow = spanRow;
        this.spanColumn = spanColumn;
    }

    private SpanCell(final SpanCell<L, E, G, I> copy) {
        super(copy);
        baseCell = copy.baseCell;
        spanRow = copy.spanRow;
        spanColumn = copy.spanColumn;
    }

    @Override
    public boolean isLinked(final ELinkageDirection direction) {
        switch (direction){
            case LEFT:
                return spanColumn>0;
            case TOP:
                return spanRow>0;
            case RIGHT:
                if (spanColumn+1 >= baseCell.getColumnSpan()){
                    return spanRow>0 ? false : baseCell.isLinked(direction);
                }else{
                    return true;
                }
            case BOTTOM:
                return spanRow+1 < baseCell.getRowSpan();
        }
        return false;
    }

    @Override
    public String getDescription(final MessageProvider mp) {
        if (mp == null) {
            final String message = "extention [%1$s,%2$s]  for %3$s";
            return String.format(message, spanColumn, spanRow,  baseCell.getDescription(mp));
        }
        final String message =
                mp.translate("TraceMessage", "extention [%1$s,%2$s] for %3$s");
        return String.format(message, spanColumn, spanRow, baseCell.getDescription(mp));
    }

    @Override
    public IPropertiesGridCell<L, E, G> createCopy() {
        return new SpanCell<>(this);
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
    public I getModelItem(){
        return baseCell.getModelItem();
    }

    @Override
    public int getColumn() {
        return baseCell.getColumn() + spanColumn;
    }

    @Override
    public int getRow() {
        return baseCell.getRow() + spanRow;
    }

    @Override
    public int getColumnSpan() {
        return 1;
    }

    @Override
    public int getRowSpan() {
        return 1;
    }   
    
    @Override
    public boolean isVisible() {
        return baseCell.isVisible();
    }

    @Override
    public void close(final IPropertiesGridPresenter<L, E, G> presenter) {
        baseCell.close(presenter);
    }        

    public EditorPageItemCell<L, E, G, I> getEditorPageItemCell() {
        return baseCell;        
    }

    @Override
    public G getPropertiesGroupWidget() {
        return null;
    }

    @Override
    public boolean isModelItemVisible() {
        return baseCell.isModelItemVisible();
    }

    @Override
    public boolean isModelItemReadOnly() {
        return true;
    }
}
