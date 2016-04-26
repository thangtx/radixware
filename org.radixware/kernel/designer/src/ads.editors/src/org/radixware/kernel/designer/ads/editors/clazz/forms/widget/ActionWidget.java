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

package org.radixware.kernel.designer.ads.editors.clazz.forms.widget;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsWidgetDef;
import org.radixware.kernel.designer.ads.editors.clazz.forms.GraphSceneImpl;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.DrawUtil;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.ActionItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.props.UIPropertySupport;


public class ActionWidget extends BaseWidget {

    public ActionWidget(GraphSceneImpl scene, AdsWidgetDef node) {
        super(scene, node);
    }

    @Override
    public boolean isSelfContained() {
        return true;
    }

    @Override
    public Rectangle getGeometry() {
        final Rectangle geometry = super.getGeometry();
        final Dimension size = ActionItem.calcActionSize(getSceneImpl().getGraphics().getFontMetrics(DrawUtil.DEFAULT_FONT), (AdsWidgetDef) getNode());
        return new Rectangle(geometry.x, geometry.y, size.width, size.height);
    }

    @Override
    public boolean isResizable() {
        return false;
    }

    @Override
    protected RadixObject addTo(BaseWidget parent, Point localPoint) {
        if (parent.getNode() instanceof AdsWidgetDef) {
            ((AdsWidgetDef) parent.getNode()).getWidgets().add((AdsWidgetDef) getNode());
        }
        return getNode();
    }

    @Override
    protected RadixObject removeFrom(BaseWidget parent) {
        if (parent.getNode() instanceof AdsWidgetDef) {
            ((AdsWidgetDef) parent.getNode()).getWidgets().remove((AdsWidgetDef) getNode());
        }
        return getNode();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getSource() instanceof UIPropertySupport || evt.getSource() == this) {
            final AdsUIProperty prop = (AdsUIProperty) evt.getNewValue();
            switch (prop.getName()) {
                case "text":
                case "icon":
                    setGeometry(getGeometry());
            }
        }

        super.propertyChange(evt);
    }
}
