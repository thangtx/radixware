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

import org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef;
import org.radixware.kernel.common.client.models.items.ModelItem;
import org.radixware.kernel.common.client.views.IPropertiesGroupWidget;
import org.radixware.kernel.common.client.widgets.IModelWidget;


abstract class EditorPageItemCell <L extends IModelWidget, E extends IModelWidget, G extends IPropertiesGroupWidget, I extends ModelItem> extends AbstractLinkableCell<L, E, G> {
    
    private final I modelItem;
    private final int column, row, colSpan;
    private boolean stickToLeft, stickToRight;
    private boolean isVisible;

    public EditorPageItemCell(final I modelItem,
            final int column,
            final int row,
            final int colspan,
            final boolean stickToLeft,
            final boolean stickToRight) {
        super();
        this.modelItem = modelItem;
        this.column = column;
        this.row = row;
        this.colSpan = colspan;
        this.stickToLeft = stickToLeft;
        this.stickToRight = stickToRight;        
    }
    
    public EditorPageItemCell(final RadStandardEditorPageDef.PageItem pageItem,
            final I modelItem) {
        super();
        this.modelItem = modelItem;
        column = pageItem.getColumn();
        row = pageItem.getRow();
        colSpan = pageItem.getColumnSpan();
        stickToLeft = pageItem.isGlueToLeftItem();
        stickToRight = pageItem.isGlueToRightItem();
    }
    
    protected EditorPageItemCell(final EditorPageItemCell<L, E, G, I> copy) {
        super(copy);
        this.modelItem = copy.modelItem;
        this.column = copy.column;
        this.row = copy.row;
        this.colSpan = copy.colSpan;        
        this.stickToLeft = copy.stickToLeft;
        this.stickToRight = copy.stickToRight;
        this.isVisible = copy.isVisible;
    }
    
    @Override
    public final boolean isLinked(final ELinkageDirection direction) {
        return direction == ELinkageDirection.LEFT ? stickToLeft : stickToRight;
    }

    @Override
    public I getModelItem() {
        return modelItem;
    }
        
    @Override
    public final int getColumn() {
        return column;
    }

    @Override
    public final int getRow() {
        return row;
    }

    @Override
    public final int getColumnSpan() {
        return colSpan;
    }

    @Override
    public int getRowSpan() {
        return 1;
    }
    
    @Override
    public final boolean isVisible() {
        return isVisible;
    }    
    
    public abstract boolean wasBinded();
    
    public abstract void bind();

    public final void setVisible(final boolean flag) {
        isVisible = flag;
    }        
}
