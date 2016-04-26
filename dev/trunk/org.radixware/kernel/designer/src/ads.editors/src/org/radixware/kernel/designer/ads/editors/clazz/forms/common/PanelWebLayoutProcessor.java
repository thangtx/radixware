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
import java.util.Map;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtWidgetDef;
import org.radixware.kernel.designer.ads.editors.clazz.forms.GraphSceneImpl;
import org.radixware.kernel.designer.ads.editors.clazz.forms.widget.BaseWidget;


public class PanelWebLayoutProcessor extends LayoutProcessor {

    public PanelWebLayoutProcessor(BaseWidget wg, AdsRwtWidgetDef widget) {
        super(wg);
        wg.setLayout(new PanelWebLayout());
        wg.revalidate();
    }

    @Override
    public void locate(DragDropLocator locator, Point localPoint) {
        Map.Entry<AdsRwtWidgetDef.PanelGrid.Row.Cell, Rectangle> e = cellFromPoint(localPoint);

        if (e != null) {
            Rectangle cell = e.getValue();
            Rectangle pos = wg.getGeometry();
            locator.locate(Arrays.asList(new Point(pos.x + cell.x, pos.y + cell.y),
                    new Point(pos.x + cell.x + cell.width, pos.y + cell.y),
                    new Point(pos.x + cell.x + cell.width, pos.y + cell.y + cell.height),
                    new Point(pos.x + cell.x, pos.y + cell.y + cell.height), new Point(pos.x + cell.x, pos.y + cell.y)));

        }
    }

    @Override
    public RadixObject add(RadixObject node, Point localPoint) {
        Map.Entry<AdsRwtWidgetDef.PanelGrid.Row.Cell, Rectangle> e = cellFromPoint(localPoint);
        if (e != null) {
            AdsRwtWidgetDef.PanelGrid.Row.Cell cell = e.getKey();
            if (node instanceof AdsRwtWidgetDef) {
                AdsRwtWidgetDef rwt = (AdsRwtWidgetDef) node;
                cell.setWidget(rwt);
                wg.getSceneImpl().revalidate();
                return rwt;
            }
        }
        return null;
    }

    @Override
    public RadixObject remove(RadixObject node) {
        RadixObject owner = wg.getNode();
        if (owner.isParentOf(node)) {
            node.delete();
            wg.getSceneImpl().revalidate();
            return node;
        } else {
            return null;
        }

    }

    private Map.Entry<AdsRwtWidgetDef.PanelGrid.Row.Cell, Rectangle> cellFromPoint(Point localPoint) {
        return PanelWebLayout.cellFromPoint(wg, localPoint);
//
//        //GraphSceneImpl scene = wg.getSceneImpl();
//        Point p = wg.convertLocalToGeometry(localPoint);
//
//        RadixObject node = wg.getNode();
//        AdsRwtWidgetDef.PanelGrid grid = null;
//        if (node instanceof AdsRwtWidgetDef) {
//            grid = ((AdsRwtWidgetDef) node).getPanelGrid();
//        }
//        if (grid == null) {
//            return null;
//        }
//
//
//        Map<AdsRwtWidgetDef.PanelGrid.Row.Cell, Rectangle> map = PanelWebLayout.computeLayout(grid, wg.getGeometry());
//
//        for (Map.Entry<AdsRwtWidgetDef.PanelGrid.Row.Cell, Rectangle> e : map.entrySet()) {
//            Rectangle r = e.getValue();
//            if (localPoint.x > r.x && localPoint.x < r.x + r.width && localPoint.y > r.y && localPoint.y < r.y + r.height) {
//                return e;
//            }
//        }
//
//        return null;

    }
}
