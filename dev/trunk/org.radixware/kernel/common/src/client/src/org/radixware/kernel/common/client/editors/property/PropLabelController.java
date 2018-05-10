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

package org.radixware.kernel.common.client.editors.property;

import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.views.IPropLabel;
import org.radixware.kernel.common.client.widgets.IWidget;


public abstract class PropLabelController<T extends IPropLabel & IWidget> extends PropVisualizerController<T> {

    public PropLabelController(Property property, T widget) {
        super(property, widget);
    }

    public PropLabelController(T widget) {
        super(widget);
    }

    @Override
    protected void updateVisualizer() {
        final Property prop = getProperty();
        if (prop == null) {
            updateLabelText("");
        } else {
            updateLabelText(prop.getTitle() + ":");
            getWidget().setEnabled(prop.isEnabled());
        }
    }

    protected abstract void updateLabelText(String text);
}
