/*
* Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.kernel.common.client.widgets.propertiesgrid;

import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef;
import org.radixware.kernel.common.client.models.items.PropertiesGroupModelItem;
import org.radixware.kernel.common.client.views.IPropertiesGroupWidget;
import org.radixware.kernel.common.client.widgets.IModelWidget;


final class PropertiesGroupCell<L extends IModelWidget, E extends IModelWidget, G extends IPropertiesGroupWidget> extends EditorPageItemCell<L, E, G, PropertiesGroupModelItem> {
    
    private final G propertiesGroupWidget;
    private boolean binded,closed;
    
    public PropertiesGroupCell(final IPropertiesGridPresenter<L, E, G> presenter,
            final PropertiesGroupModelItem propertiesGroup,
            final int column,
            final int row,
            final int colspan,
            final boolean stickToLeft,
            final boolean stickToRight) {
        super(propertiesGroup, column, row, colspan, stickToLeft, stickToRight);
        propertiesGroupWidget = presenter.createPropertiesGroup(propertiesGroup);
        setVisible(propertiesGroup.isVisible());
    }
    
    public PropertiesGroupCell(final IPropertiesGridPresenter<L, E, G> presenter,
            final RadStandardEditorPageDef.PageItem pageItem,
            final PropertiesGroupModelItem propertiesGroup) {
        super(pageItem, propertiesGroup);
        propertiesGroupWidget = presenter.createPropertiesGroup(propertiesGroup);
        setVisible(propertiesGroup.isVisible());
    }    
    
    private PropertiesGroupCell(final PropertiesGroupCell<L, E, G> copy) {
        super(copy);
        this.propertiesGroupWidget = copy.propertiesGroupWidget;
    }    
    
    @Override
    public IPropertiesGridCell<L, E, G> createCopy() {
        return new PropertiesGroupCell<>(this);
    }    

    @Override
    public String getDescription(final MessageProvider mp) {
        if (mp == null) {
            final String message =
                    "cell for properties group \"%1$s\" #%2$s (column: %3$s; row: %4$s; span: %5$s)";
            return String.format(message, getModelItem().getTitle(), getModelItem().getId().toString(), getColumn(), getRow(), getColumnSpan());
        }
        final String message =
                mp.translate("TraceMessage", "cell for properties group \"%1$s\" #%2$s (column: %3$s; row: %4$s; span: %5$s)");
        return String.format(message, getModelItem().getTitle(), getModelItem().getId().toString(), getColumn(), getRow(), getColumnSpan());
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
    public G getPropertiesGroupWidget() {
        return propertiesGroupWidget;
    }

    @Override
    public boolean isModelItemReadOnly() {
        return getModelItem().isAllPropertiesReadOnly();
    }    

    public boolean wasBinded() {
        return binded;
    }

    public void bind() {
        propertiesGroupWidget.bind();
        binded = true;
    }

    private void unsubscribe() {
        getModelItem().unsubscribe(propertiesGroupWidget);
    }

    @Override
    public void close(final IPropertiesGridPresenter<L, E, G> presenter) {
        if (!closed){
            closed = true;
            unsubscribe();            
            presenter.destroyWidgets(null, null, propertiesGroupWidget);
        }
    }

    @Override
    public boolean isModelItemVisible() {
        return getModelItem().isVisible();
    }     

    @Override
    public int getRowSpan() {
        return 1;
    }
    
    public int getVisibleRows(){
        if (wasBinded()){
            return propertiesGroupWidget.getVisibleRowsCount();
        }else{
            return 1;
        }
    }
            
    private List<IPropertiesGridCell<L,E,G>> createRowSpanCells(final int row, final int count, final List<IPropertiesGridCell<L,E,G>> prevRow){
        final List<IPropertiesGridCell<L,E,G>> cells = new LinkedList<>();
        SpanCell<L,E,G,PropertiesGroupModelItem> spanCell;
        for (int col=0; col<count; col++){
            spanCell = new SpanCell<>(this,col,row);
            if (col>0){
                spanCell.linkWith(cells.get(col-1), ELinkageDirection.LEFT);
                cells.get(col-1).linkWith(spanCell, ELinkageDirection.RIGHT);
            }
            prevRow.get(col).linkWith(spanCell, ELinkageDirection.BOTTOM);
            spanCell.linkWith(prevRow.get(col), ELinkageDirection.TOP);
            cells.add(spanCell);
        }
        return cells;
    }
            
}
