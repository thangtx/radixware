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
import org.radixware.kernel.common.client.views.IPropertiesGroupWidget;
import org.radixware.kernel.common.client.widgets.IModelWidget;

final class PropertyCell<L extends IModelWidget, E extends IModelWidget, G extends IPropertiesGroupWidget> extends EditorPageItemCell<L, E, G,Property> {
    
    private final L label;
    private final E editor;
    private boolean binded,closed;

    public PropertyCell(final IPropertiesGridPresenter<L, E, G> presenter,
            final Property property,
            final int column,
            final int row,
            final int colspan,
            final boolean stickToLeft,
            final boolean stickToRight) {
        super(property, column, row, colspan, stickToLeft, stickToRight);
        label = presenter.createPropertyLabel(property);
        editor = presenter.createPropertyEditor(property);
        setVisible(property.isVisible());
    }

    public PropertyCell(final IPropertiesGridPresenter<L, E, G> presenter,
            final RadStandardEditorPageDef.PageItem pageItem,
            final Property property) {
        super(pageItem,property);
        label = presenter.createPropertyLabel(property);
        editor = presenter.createPropertyEditor(property);
        setVisible(property.isVisible());
    }

    private PropertyCell(final PropertyCell<L, E, G> copy) {
        super(copy);
        this.label = copy.label;
        this.editor = copy.editor;
    }

    @Override
    public IPropertiesGridCell<L, E, G> createCopy() {
        return new PropertyCell<>(this);
    }

    @Override
    public String getDescription(final MessageProvider mp) {
        if (mp == null) {
            final String message =
                    "cell for property \"%1$s\" #%2$s (column: %3$s; row: %4$s; span: %5$s)";
            return String.format(message, getModelItem().getTitle(), getModelItem().getId().toString(), getColumn(), getRow(), getColumnSpan());
        }
        final String message =
                mp.translate("TraceMessage", "cell for property \"%1$s\" #%2$s (column: %3$s; row: %4$s; span: %5$s)");
        return String.format(message, getModelItem().getTitle(), getModelItem().getId().toString(), getColumn(), getRow(), getColumnSpan());
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
    public G getPropertiesGroupWidget() {
        return null;
    }

    @Override
    public boolean isModelItemReadOnly() {
        return getModelItem().isReadonly();
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
            getModelItem().unsubscribe((IModelWidget) label);
        }
        if (editor instanceof IModelWidget) {
            getModelItem().unsubscribe((IModelWidget) editor);
        }
    }

    @Override
    public void close(final IPropertiesGridPresenter<L, E, G> presenter) {
        if (!closed){
            closed = true;
            unsubscribe();            
            presenter.destroyWidgets(label, editor, null);
        }
    }

    @Override
    public boolean isModelItemVisible() {
        return getModelItem().isVisible();
    }        
}
