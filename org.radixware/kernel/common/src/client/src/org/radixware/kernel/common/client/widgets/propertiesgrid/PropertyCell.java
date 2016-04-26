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
import org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.widgets.IModelWidget;
import org.radixware.kernel.common.client.widgets.propertiesgrid.IPropertiesGridCell.ELinkageDirection;


final class PropertyCell<L extends IModelWidget, E extends IModelWidget> extends AbstractLinkableCell<L, E> {

    private final Property property;
    private final int column, row, colSpan;
    private boolean stickToLeft, stickToRight;
    private boolean isVisible;
    private final L label;
    private final E editor;
    private boolean binded,closed;

    public PropertyCell(final IPropertiesGridPresenter<L, E> presenter,
            final Property property,
            final int column,
            final int row,
            final int colspan,
            final boolean stickToLeft,
            final boolean stickToRight) {
        super();
        label = presenter.createPropertyLabel(property);
        editor = presenter.createPropertyEditor(property);
        this.property = property;
        this.column = column;
        this.row = row;
        this.colSpan = colspan;
        this.stickToLeft = stickToLeft;
        this.stickToRight = stickToRight;
        this.isVisible = property.isVisible();
    }

    public PropertyCell(final IPropertiesGridPresenter<L, E> presenter,
            final RadStandardEditorPageDef.PageItem pageItem,
            final Property property) {
        super();
        label = presenter.createPropertyLabel(property);
        editor = presenter.createPropertyEditor(property);
        this.property = property;
        isVisible = property.isVisible();
        column = pageItem.getColumn();
        row = pageItem.getRow();
        colSpan = pageItem.getColumnSpan();
        stickToLeft = pageItem.isGlueToLeftItem();
        stickToRight = pageItem.isGlueToRightItem();
    }

    private PropertyCell(final PropertyCell<L, E> copy) {
        super(copy);
        this.label = copy.label;
        this.editor = copy.editor;
        this.property = copy.property;
        this.column = copy.column;
        this.row = copy.row;
        this.colSpan = copy.colSpan;
        this.isVisible = copy.isVisible;
        this.stickToLeft = copy.stickToLeft;
        this.stickToRight = copy.stickToRight;
    }

    @Override
    public IPropertiesGridCell<L, E> createCopy() {
        return new PropertyCell<>(this);
    }

    @Override
    public boolean isLinked(final ELinkageDirection direction) {
        return direction == ELinkageDirection.LEFT ? stickToLeft : stickToRight;
    }

    @Override
    public String getDescription(final MessageProvider mp) {
        if (mp == null) {
            final String message =
                    "cell for property \"%1$s\" #%2$s (column: %3$s; row: %4$s; span: %5$s)";
            return String.format(message, property.getTitle(), property.getId().toString(), column, row, colSpan);
        }
        final String message =
                mp.translate("TraceMessage", "cell for property \"%1$s\" #%2$s (column: %3$s; row: %4$s; span: %5$s)");
        return String.format(message, property.getTitle(), property.getId().toString(), column, row, colSpan);
    }

    @Override
    public L getPropertyLabel() {
        return label;
    }

    @Override
    public E getPropertyEditor() {
        return editor;
    }

    @Override
    public Property getProperty() {
        return property;
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
        return colSpan;
    }

    public boolean wasBinded() {
        return binded;
    }

    public void bind() {
        if (label instanceof IModelWidget) {
            ((IModelWidget) label).bind();
        }
        if (editor instanceof IModelWidget) {
            ((IModelWidget) editor).bind();
        }
        binded = true;
    }

    private void unsubscribe() {
        if (label instanceof IModelWidget) {
            property.unsubscribe((IModelWidget) label);
        }
        if (editor instanceof IModelWidget) {
            property.unsubscribe((IModelWidget) editor);
        }
    }

    @Override
    public boolean isVisible() {
        return isVisible;
    }

    @Override
    public void close(final IPropertiesGridPresenter<L, E> presenter) {
        if (!closed){
            closed = true;
            unsubscribe();            
            presenter.destroyWidgets(label, editor);
        }
    }

    public void setVisible(final boolean flag) {
        isVisible = flag;
    }
}
