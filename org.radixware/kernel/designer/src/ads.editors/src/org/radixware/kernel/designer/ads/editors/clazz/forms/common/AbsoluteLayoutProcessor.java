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
import org.netbeans.api.visual.layout.LayoutFactory;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.ui.*;
import org.radixware.kernel.designer.ads.editors.clazz.forms.widget.BaseWidget;


public class AbsoluteLayoutProcessor extends LayoutProcessor {

    private final ICurrentNodeProvider<? extends AdsUIItemDef> nodeProvider;

    public AbsoluteLayoutProcessor(BaseWidget wg, ICurrentNodeProvider<? extends AdsUIItemDef> nodeProvider) {
        super(wg);

        this.nodeProvider = nodeProvider;
        wg.setLayout(LayoutFactory.createAbsoluteLayout()); // reset layout to default
        AdsUIUtil.layoutToWidgets(getWidget());
        saveGeometry(getWidget());
        wg.setLayout(new AbsoluteLayout());
        wg.revalidate();
    }

    public AbsoluteLayoutProcessor(BaseWidget wg, AdsWidgetDef widget) {
        this(wg, new DefaultCurrentNodeProvider<>(widget));
    }

     public AbsoluteLayoutProcessor(BaseWidget wg) {
        this(wg, (AdsWidgetDef) AdsUIUtil.currentWidget((AdsWidgetDef) wg.getSceneImpl().findObject(wg)));
     }

    @Override
    public void locate(DragDropLocator locator, Point localPoint) {
        Rectangle r = wg.getLayoutGeometry();

        Point p1 = new Point(r.x, r.y + r.height);
        Point p2 = new Point(r.x, r.y);
        Point p3 = new Point(r.x + r.width, r.y);
        Point p4 = new Point(r.x + r.width, r.y + r.height);
        Point p5 = new Point(r.x, r.y + r.height);
        locator.locate(Arrays.asList(p1, p2, p3, p4, p5));
    }

    @Override
    public RadixObject add(RadixObject node, Point localPoint) {
        AdsWidgetDef w = null;
        if (node instanceof AdsLayout) {
            w = new AdsWidgetDef(AdsMetaInfo.WIDGET_CLASS);
            w.setLayout((AdsLayout) node);
        } else if (node instanceof AdsWidgetDef) {
            w = (AdsWidgetDef) node;
        } else if (node instanceof AdsLayout.SpacerItem) {

        }

        if (w != null) {
            getWidget().getWidgets().add(w);
            wg.revalidate();
        }

        return w;
    }

    @Override
    public RadixObject remove(RadixObject node) {
        AdsWidgetDef w = node instanceof AdsLayout ? (AdsWidgetDef) node.getContainer() : (AdsWidgetDef) node;
        if (w != null && getWidget().equals(w.getOwnerDefinition())) {
            node.delete();
            wg.revalidate();
            return w;
        }
        return null;
    }

    private AdsWidgetDef getWidget() {
        assert nodeProvider.getCurrentNode() instanceof AdsWidgetDef;
        return (AdsWidgetDef) nodeProvider.getCurrentNode();
    }
}
