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

package org.radixware.kernel.designer.ads.editors.clazz.forms.common;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Arrays;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.ui.AdsUIActionDef;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtWidgetDef;
import org.radixware.kernel.designer.ads.editors.clazz.forms.widget.BaseWidget;


public class AbsoluteWebLayoutProcessor extends LayoutProcessor {

    private AdsRwtWidgetDef widget;

    public AbsoluteWebLayoutProcessor(BaseWidget node, AdsRwtWidgetDef widget) {
        super(node);
        this.widget = widget;
        node.setLayout(new AbsoluteWebLayout());
    }

    @Override
    public void locate(DragDropLocator locator, Point localPoint) {
        Rectangle r = wg.getGeometry();
        Point p1 = new Point(r.x, r.y + r.height);
        Point p2 = new Point(r.x, r.y);
        Point p3 = new Point(r.x + r.width, r.y);
        Point p4 = new Point(r.x + r.width, r.y + r.height);
        Point p5 = new Point(r.x, r.y + r.height);
        locator.locate(Arrays.asList(p1, p2, p3, p4, p5));
    }

    @Override
    public RadixObject add(RadixObject node, Point localPoint) {
        if (node instanceof AdsUIActionDef) {
            widget.getActions().add((AdsUIActionDef) node);
            wg.revalidate();
            wg.getScene().revalidate();
            return node;
        }

        AdsRwtWidgetDef w = null;
        if (node instanceof AdsRwtWidgetDef) {
            w = (AdsRwtWidgetDef) node;
        }

        if (w != null) {
            widget.getWidgets().add(w);
            wg.revalidate();
            wg.getScene().revalidate();
        }

        wg.getScene().revalidate();
        return w;
    }

    @Override
    public RadixObject remove(RadixObject node) {
        AdsRwtWidgetDef w = (AdsRwtWidgetDef) node;
        if (w != null && widget.equals(w.getOwnerDefinition())) {
            node.delete();
            wg.revalidate();
            wg.getScene().revalidate();
            return w;
        }
        wg.getScene().revalidate();

        return null;
    }
}
