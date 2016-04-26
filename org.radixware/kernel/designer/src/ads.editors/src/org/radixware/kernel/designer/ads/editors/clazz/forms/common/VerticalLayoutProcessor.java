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
import org.radixware.kernel.common.defs.ads.ui.AdsLayout;
import org.radixware.kernel.common.defs.ads.ui.AdsMetaInfo;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.defs.ads.ui.AdsWidgetDef;
import org.radixware.kernel.designer.ads.editors.clazz.forms.GraphSceneImpl;
import org.radixware.kernel.designer.ads.editors.clazz.forms.widget.BaseWidget;


public class VerticalLayoutProcessor extends LayoutProcessor {

    private AdsLayout layout;

    public VerticalLayoutProcessor(BaseWidget wg, AdsWidgetDef widget) {
        super(wg);
        layout = widget.getLayout();
        if (layout == null || !layout.getClassName().equals(AdsMetaInfo.VERTICAL_LAYOUT_CLASS)) {
            wg.setLayout(LayoutFactory.createAbsoluteLayout()); // reset layout to default
            AdsUIUtil.layoutToWidgets(widget);
            AdsUIUtil.widgetsToLayout(widget, layout = new AdsLayout(AdsMetaInfo.VERTICAL_LAYOUT_CLASS));
            saveGeometry(widget);
        }
        wg.setLayout(new VerticalLayout());
        wg.revalidate();
    }

    public VerticalLayoutProcessor(BaseWidget wg) {
        this(wg, ((AdsWidgetDef) AdsUIUtil.currentWidget((AdsWidgetDef) wg.getSceneImpl().findObject(wg))));
    }

    public VerticalLayoutProcessor(BaseWidget wg, AdsLayout layout) {
        super(wg);
        this.layout = layout;
        wg.setLayout(new VerticalLayout());
        wg.revalidate();
    }

    private int indexFromPoint(Point localPoint) {
        GraphSceneImpl scene = wg.getSceneImpl();
        Point p = wg.convertLocalToGeometry(localPoint);

        AdsLayout.Items items = layout.getItems();
        int size = items.size();

        for (int idx = 0; idx < size; idx++) {
            RadixObject node = AdsUIUtil.getItemNode(items.get(idx));
            BaseWidget w = (BaseWidget) scene.findWidget(node);
            Rectangle r = w.getGeometry();
            if (idx == 0 && r.y + r.height / 2 >= p.y) {
                return 0;
            } else if (idx == size - 1 && r.y + r.height / 2 < p.y) {
                return size;
            } else {
                if (idx > 0) {
                    RadixObject node0 = AdsUIUtil.getItemNode(items.get(idx - 1));
                    BaseWidget w0 = (BaseWidget) scene.findWidget(node0);
                    Rectangle r0 = w0.getGeometry();
                    if (r0.y + r0.height / 2 < p.y && r.y + r.height / 2 >= p.y) {
                        return idx;
                    }
                }
            }
        }

        return 0;
    }

    @Override
    public void locate(DragDropLocator locator, Point localPoint) {
        GraphSceneImpl scene = wg.getSceneImpl();
        Point p = wg.convertLocalToGeometry(localPoint);
        Rectangle lr = wg.getLayoutGeometry();

        AdsLayout.Items items = layout.getItems();
        int size = items.size();

        int idx = indexFromPoint(localPoint);
        assert idx >= 0 && idx <= size : "index out of range: " + layout + ", " + localPoint;

        if (idx == 0) {
            locator.locate(Arrays.asList(new Point(lr.x, lr.y - 2), new Point(lr.x + lr.width, lr.y - 2)));
        } else if (idx == size) {
            locator.locate(Arrays.asList(new Point(lr.x, lr.y + lr.height), new Point(lr.x + lr.width, lr.y + lr.height)));
        } else {
            RadixObject node0 = AdsUIUtil.getItemNode(items.get(idx - 1));
            BaseWidget w0 = (BaseWidget) scene.findWidget(node0);
            Rectangle r0 = w0.getGeometry();

            RadixObject node1 = AdsUIUtil.getItemNode(items.get(idx));
            BaseWidget w1 = (BaseWidget) scene.findWidget(node1);
            Rectangle r1 = w1.getGeometry();

            int step = r0.y + r0.height + (r1.y - (r0.y + r0.height)) / 2;
            locator.locate(Arrays.asList(new Point(lr.x, step), new Point(lr.x + lr.width, step)));
        }
    }

    @Override
    public RadixObject add(RadixObject node, Point localPoint) {
        int size = layout.getItems().size();
        int idx = indexFromPoint(localPoint);
        assert idx >= 0 && idx <= size : "index out of range: " + layout + ", " + node + ", " + localPoint;

        AdsLayout.Item item = null;
        if (node instanceof AdsLayout) {
            item = new AdsLayout.LayoutItem((AdsLayout) node);
        } else if (node instanceof AdsWidgetDef) {
            item = new AdsLayout.WidgetItem((AdsWidgetDef) node);
        } else if (node instanceof AdsLayout.SpacerItem) {
            item = (AdsLayout.SpacerItem) node;
        }

        assert item != null : "item can't be null: " + layout + ", " + node + ", " + localPoint;
        layout.getItems().add(idx, item);

        wg.getSceneImpl().revalidate();
        return item;
    }

    @Override
    public RadixObject remove(RadixObject node) {
        AdsLayout.Item item = null;
        if (node instanceof AdsLayout.SpacerItem) {
            item = (AdsLayout.SpacerItem) node;
        } else if (node.getContainer() instanceof AdsLayout.Item) {
            item = (AdsLayout.Item) node.getContainer();
        }

        if (item != null && layout.equals(item.getOwnerLayout())) {
            node.delete();
            wg.getSceneImpl().revalidate();
            return item;
        }

        return null;
    }
}
