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
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.widgets.IModelWidget;
import org.radixware.kernel.common.client.widgets.propertiesgrid.IPropertiesGridCell.ELinkageDirection;


class SpanCell<L extends IModelWidget, E extends IModelWidget> extends AbstractLinkableCell<L, E> {

    private final PropertyCell<L, E> baseCell;
    private final int spanIndex;

    public SpanCell(final PropertyCell<L, E> baseCell, final int spanIndex) {
        this.baseCell = baseCell;
        this.spanIndex = spanIndex;
    }

    private SpanCell(final SpanCell<L, E> copy) {
        super(copy);
        baseCell = copy.baseCell;
        spanIndex = copy.spanIndex;
    }

    @Override
    public boolean isLinked(final ELinkageDirection direction) {
        if (direction == ELinkageDirection.LEFT) {
            return true;
        } else {
            return (spanIndex + 1) == baseCell.getColumnSpan() ? baseCell.isLinked(direction) : true;
        }
    }

    @Override
    public String getDescription(MessageProvider mp) {
        if (mp == null) {
            final String message = "extention %1$s for %2$s";
            return String.format(message, spanIndex, baseCell.getDescription(mp));
        }
        final String message =
                mp.translate("TraceMessage", "extention %1$s for %2$s");
        return String.format(message, spanIndex, baseCell.getDescription(mp));
    }

    @Override
    public IPropertiesGridCell<L, E> createCopy() {
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
    public Property getProperty() {
        return baseCell.getProperty();
    }

    @Override
    public int getColumn() {
        return baseCell.getColumn() + spanIndex;
    }

    @Override
    public int getRow() {
        return baseCell.getRow();
    }

    @Override
    public int getColumnSpan() {
        return 1;
    }

    @Override
    public boolean isVisible() {
        return baseCell.isVisible();
    }

    @Override
    public void close(final IPropertiesGridPresenter<L, E> presenter) {
        baseCell.close(presenter);
    }        

    public PropertyCell<L, E> getPropertyCell() {
        return baseCell;
    }
}
