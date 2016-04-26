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

import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.models.items.ModelItem;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.widgets.IModelWidget;
import org.radixware.kernel.common.client.widgets.IWidget;


public abstract class PropVisualizerController<T extends IModelWidget & IWidget> {

    private Property property;
    private T widget;

    protected abstract boolean isPropVisualizerInUITree();

    protected abstract void updateSettings();

    public PropVisualizerController(Property property, T widget) {
        this.property = property;
        this.widget = widget;
    }

    public PropVisualizerController(T widget) {
        this(null, widget);
    }

    protected T getWidget() {
        return widget;
    }

    public void setProperty(Property def) {
        synchronized (this) {
            this.property = def;
            //bind();
        }
    }

    public Property getProperty() {
        return property;
    }

    public void bind() {
        if (this.property != null) {
            this.property.subscribe(widget);
            if (getWidget()!=null){
                getWidget().refresh(property);
            }
        }
    }

    protected String getToolTip() {
        if (this.property != null) {
            return property.getHint();
        } else {
            return "";
        }
    }

    public void refresh(ModelItem changedItem) {
        final Property finalProperty = getProperty();

        updateVisualizer();
        widget.setToolTip(getToolTip());

        if (isPropVisualizerInUITree()) {
            widget.setVisible(finalProperty.isVisible());
        } else if (!finalProperty.isVisible()) {
            widget.setVisible(false);
        }
        widget.setEnabled(finalProperty.isEnabled());
        updateSettings();
    }

    public boolean close() {
        if (property != null) {
            property.unsubscribe(widget);
            property = null;
        }
        return true;
    }

    protected final IClientEnvironment getEnvironment() {
        if (property != null) {
            return property.getEnvironment();
        } else {
            return null;
        }
    }

    protected abstract void updateVisualizer();
}
