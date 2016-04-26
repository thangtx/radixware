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
import org.radixware.kernel.common.defs.ads.ui.AdsMetaInfo;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtWidgetDef;
import org.radixware.kernel.designer.ads.editors.clazz.forms.GraphSceneImpl;
import org.radixware.kernel.designer.ads.editors.clazz.forms.widget.BaseWidget;


public class BoxWebLayoutProcessor extends LayoutProcessor {

    private boolean vertical = false;
    private AdsRwtWidgetDef widget;

    public BoxWebLayoutProcessor(BaseWidget wg, AdsRwtWidgetDef widget) {
        super(wg);
        vertical = AdsMetaInfo.RWT_VERTICAL_BOX_CONTAINER.equals(widget.getClassName());
        this.widget = widget;
        wg.setLayout(new BoxWebLayout(wg, vertical));
    }

    @Override
    public void locate(DragDropLocator locator, Point localPoint) {
        MatchInfo info = getMatchInfo(localPoint);
        if (info.rect != null) {
            Point p1, p2;
            if (vertical) {
                p1 = new Point(info.rect.x, info.rect.y);
                p2 = new Point(info.rect.x + info.rect.width, info.rect.y);

            } else {
                p1 = new Point(info.rect.x, info.rect.y);
                p2 = new Point(info.rect.x, info.rect.y + info.rect.height);

            }
            locator.locate(Arrays.asList(p1, p2));
            return;
        }
        Rectangle r = wg.getGeometry();



        Point p1 = new Point(r.x, r.y + r.height);
        Point p2 = new Point(r.x, r.y);
        Point p3 = new Point(r.x + r.width, r.y);
        Point p4 = new Point(r.x + r.width, r.y + r.height);
        Point p5 = new Point(r.x, r.y + r.height);
        locator.locate(Arrays.asList(p1, p2, p3, p4, p5));
    }

    private class MatchInfo {

        AdsRwtWidgetDef widget;
        Rectangle rect;
    }

    private MatchInfo getMatchInfo(Point localPoint) {
        Rectangle r = wg.getGeometry();
        GraphSceneImpl scene = wg.getSceneImpl();

        int height = 0;
        int width = 0;
        for (AdsRwtWidgetDef child : widget.getWidgets()) {
            BaseWidget cw = (BaseWidget) scene.findWidget(child);

            if (cw == null) {
                continue;
            }
            Rectangle rect = cw.getGeometry();
            rect.x -= r.x;
            rect.y -= r.y;
            if (rect.x < localPoint.x && rect.x + rect.width > localPoint.x && rect.y < localPoint.y && rect.y + rect.height > localPoint.y) {
                MatchInfo info = new MatchInfo();
                info.widget = child;
                info.rect = rect;
                info.rect.x += r.x;
                info.rect.y += r.y;
                return info;
            }
            height += rect.height;
            width += rect.width;
        }
        MatchInfo defaultResult = new MatchInfo();
        if (vertical) {
            defaultResult.rect = new Rectangle(r.x, r.y + height, r.width, r.height - height);
        } else {
            defaultResult.rect = new Rectangle(r.x + width, r.y, r.width - width, r.height);
        }
        return defaultResult;

    }

    @Override
    public RadixObject add(RadixObject node, Point localPoint) {


        AdsRwtWidgetDef w = null;
        if (node instanceof AdsRwtWidgetDef) {
            w = (AdsRwtWidgetDef) node;
        }

        if (w != null) {
            MatchInfo matchInfo = getMatchInfo(localPoint);
            int index = -1;
            if (matchInfo.widget != null) {
                index = widget.getWidgets().indexOf(matchInfo.widget);
            }
            if (index >= 0) {
                widget.getWidgets().add(index, w);
            } else {
                widget.getWidgets().add(w);
            }
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
