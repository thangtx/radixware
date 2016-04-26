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
import org.radixware.kernel.common.defs.Definitions;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.ui.*;
import org.radixware.kernel.common.defs.ads.ui.enums.EOrientation;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtWidgetDef;
import org.radixware.kernel.designer.ads.editors.clazz.forms.GraphSceneImpl;
import org.radixware.kernel.designer.ads.editors.clazz.forms.widget.BaseWidget;
import org.radixware.kernel.designer.ads.editors.clazz.forms.widget.SplitterWidget;


public class SplitterLayoutProcessor extends LayoutProcessor {

    private AdsUIItemDef splitter;

    public SplitterLayoutProcessor(BaseWidget wg, AdsWidgetDef splitter) {
        this(wg, (AdsUIItemDef) splitter);
    }

    public SplitterLayoutProcessor(BaseWidget wg, AdsRwtWidgetDef splitter) {
        this(wg, (AdsUIItemDef) splitter);
    }

    private SplitterLayoutProcessor(BaseWidget wg, AdsUIItemDef splitter) {
        super(wg);
        this.splitter = splitter;
        wg.setLayout(new SplitterLayout());
        wg.revalidate();
    }

    public SplitterLayoutProcessor(BaseWidget wg) {
        this(wg, (AdsWidgetDef) AdsUIUtil.currentWidget((AdsWidgetDef) wg.getSceneImpl().findObject(wg)));
    }

    private int indexFromPoint(Point localPoint) {
        GraphSceneImpl scene = wg.getSceneImpl();
        Point p = wg.convertLocalToGeometry(localPoint);

        Definitions<? extends AdsUIItemDef> widgets;
        if (splitter instanceof AdsWidgetDef) {
            widgets = ((AdsWidgetDef) splitter).getWidgets();
        } else {
            widgets = ((AdsRwtWidgetDef) splitter).getWidgets();
        }

        int size = widgets.size();

        AdsUIProperty.EnumValueProperty orientation = (AdsUIProperty.EnumValueProperty) AdsUIUtil.getUiProperty(splitter, "orientation");
        if (EOrientation.Horizontal.equals(orientation.value)) {
            for (int idx = 0; idx < size; idx++) {
                RadixObject node = AdsUIUtil.getVisualNode(widgets.get(idx));
                BaseWidget w = (BaseWidget) scene.findWidget(node);
                Rectangle r = w.getGeometry();
                if (idx == 0 && r.x + r.width / 2 >= p.x) {
                    return 0;
                } else if (idx == size - 1 && r.x + r.width / 2 < p.x) {
                    return size;
                } else {
                    if (idx > 0) {
                        RadixObject node0 = AdsUIUtil.getVisualNode(widgets.get(idx - 1));
                        BaseWidget w0 = (BaseWidget) scene.findWidget(node0);
                        Rectangle r0 = w0.getGeometry();
                        if (r0.x + r0.width / 2 < p.x && r.x + r.width / 2 >= p.x) {
                            return idx;
                        }
                    }
                }
            }
        } else {
            for (int idx = 0; idx < size; idx++) {
                RadixObject node = AdsUIUtil.getVisualNode(widgets.get(idx));
                BaseWidget w = (BaseWidget) scene.findWidget(node);
                Rectangle r = w.getGeometry();
                if (idx == 0 && r.y + r.height / 2 >= p.y) {
                    return 0;
                } else if (idx == size - 1 && r.y + r.height / 2 < p.y) {
                    return size;
                } else {
                    if (idx > 0) {
                        RadixObject node0 = AdsUIUtil.getVisualNode(widgets.get(idx - 1));
                        BaseWidget w0 = (BaseWidget) scene.findWidget(node0);
                        Rectangle r0 = w0.getGeometry();
                        if (r0.y + r0.height / 2 < p.y && r.y + r.height / 2 >= p.y) {
                            return idx;
                        }
                    }
                }
            }

        }

        return 0;
    }

    @Override
    public void locate(DragDropLocator locator, Point localPoint) {
        final GraphSceneImpl scene = wg.getSceneImpl();
//        final Point point = wg.convertLocalToGeometry(localPoint);
        final Rectangle lr = wg.getLayoutGeometry();

        Definitions<? extends AdsUIItemDef> widgets;
        if (splitter instanceof AdsWidgetDef) {
            widgets = ((AdsWidgetDef) splitter).getWidgets();
        } else {
            widgets = ((AdsRwtWidgetDef) splitter).getWidgets();
        }
        int size = widgets.size();

        int idx = indexFromPoint(localPoint);
        assert idx >= 0 && idx <= size : "index out of range: " + splitter + ", " + localPoint;

        AdsUIProperty.EnumValueProperty orientation = (AdsUIProperty.EnumValueProperty) AdsUIUtil.getUiProperty(splitter, "orientation");
        if (EOrientation.Horizontal.equals(orientation.value)) {
            if (idx == 0) {
                locator.locate(Arrays.asList(new Point(lr.x - 2, lr.y), new Point(lr.x - 2, lr.y + lr.height)));
            } else if (idx == size) {
                locator.locate(Arrays.asList(new Point(lr.x + lr.width, lr.y), new Point(lr.x + lr.width, lr.y + lr.height)));
            } else {
                RadixObject node0 = AdsUIUtil.getVisualNode(widgets.get(idx - 1));
                BaseWidget w0 = (BaseWidget) scene.findWidget(node0);
                Rectangle r0 = w0.getGeometry();

                RadixObject node1 = AdsUIUtil.getVisualNode(widgets.get(idx));
                BaseWidget w1 = (BaseWidget) scene.findWidget(node1);
                Rectangle r1 = w1.getGeometry();

                int step = r0.x + r0.width + (r1.x - (r0.x + r0.width)) / 2;
                locator.locate(Arrays.asList(new Point(step, lr.y), new Point(step, lr.y + lr.height)));
            }
        } else {
            if (idx == 0) {
                locator.locate(Arrays.asList(new Point(lr.x, lr.y - 2), new Point(lr.x + lr.width, lr.y - 2)));
            } else if (idx == size) {
                locator.locate(Arrays.asList(new Point(lr.x, lr.y + lr.height), new Point(lr.x + lr.width, lr.y + lr.height)));
            } else {
                RadixObject node0 = AdsUIUtil.getVisualNode(widgets.get(idx - 1));
                BaseWidget w0 = (BaseWidget) scene.findWidget(node0);
                Rectangle r0 = w0.getGeometry();

                RadixObject node1 = AdsUIUtil.getVisualNode(widgets.get(idx));
                BaseWidget w1 = (BaseWidget) scene.findWidget(node1);
                Rectangle r1 = w1.getGeometry();

                int step = r0.y + r0.height + (r1.y - (r0.y + r0.height)) / 2;
                locator.locate(Arrays.asList(new Point(lr.x, step), new Point(lr.x + lr.width, step)));
            }
        }
    }

    @Override
    public RadixObject add(RadixObject node, Point localPoint) {

        final WidgetAdapter splitterAdapter = new WidgetAdapter(splitter);
        final Definitions<AdsUIItemDef> widgets = (Definitions<AdsUIItemDef>) splitterAdapter.getWidgets();
        final int size = widgets.size();

        final int idx = indexFromPoint(localPoint);

        assert idx >= 0 && idx <= size : "index out of range: " + splitter + ", " + node + ", " + localPoint;

        AdsUIItemDef w = null;
        if (node instanceof AdsLayout) {
            AdsWidgetDef widget = new AdsWidgetDef(AdsMetaInfo.WIDGET_CLASS);
            widget.setLayout((AdsLayout) node);
            w = widget;
        } else if (node instanceof AdsWidgetDef || node instanceof AdsRwtWidgetDef) {
            w = (AdsUIItemDef) node;
        }

        if (w != null) {
            final double weight = 1.0 / (size + 1);
            final double[] enlarge = SplitterWidget.enlarge(SplitterWidget.getArray(widgets, null), weight);

            for (int i = 0; i < enlarge.length; ++i) {
                LayoutUtil.setSplitterItemWeight(widgets.get(i), enlarge[i]);
            }

            widgets.add(idx, w);

            LayoutUtil.setSplitterItemWeight(w, weight);
            wg.revalidate();
        }

        return w;
    }

    @Override
    public RadixObject remove(RadixObject node) {
        final AdsUIItemDef widgetNode = (AdsUIItemDef) (node instanceof AdsLayout ? node.getContainer() : node);

        if (widgetNode != null) {
            final WidgetAdapter splitterAdapter = new WidgetAdapter(splitter);

            if (splitter.equals(widgetNode.getOwnerDefinition())) {
                final double weight = LayoutUtil.getSplitterItemWeight(widgetNode);
                node.delete();

                final Definitions<? extends AdsUIItemDef> widgets = splitterAdapter.getWidgets();

                final double[] shrink = SplitterWidget.shrink(SplitterWidget.getArray(widgets, null), weight);
                for (int i = 0; i < shrink.length; ++i) {
                    LayoutUtil.setSplitterItemWeight(widgets.get(i), shrink[i]);
                }

                LayoutUtil.setSplitterItemWeight(widgetNode, 1);

                wg.revalidate();
                return widgetNode;
            }
        }
        return null;
    }
}
