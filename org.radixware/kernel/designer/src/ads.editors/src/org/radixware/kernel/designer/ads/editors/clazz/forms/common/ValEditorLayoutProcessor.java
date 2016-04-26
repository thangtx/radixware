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

/*
 * 11/1/11 10:21 AM
 */
package org.radixware.kernel.designer.ads.editors.clazz.forms.common;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Arrays;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.ui.AdsLayout;
import org.radixware.kernel.common.defs.ads.ui.AdsMetaInfo;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.defs.ads.ui.AdsWidgetDef;
import org.radixware.kernel.common.defs.ads.ui.AdsWidgetDef.Widgets;
import org.radixware.kernel.designer.ads.editors.clazz.forms.GraphSceneImpl;
import org.radixware.kernel.designer.ads.editors.clazz.forms.widget.BaseWidget;
import org.radixware.kernel.designer.ads.editors.clazz.forms.widget.ValEditorWidget;

/**
 * Layout processor for ValEditorWidget and subclasses
 */
public final class ValEditorLayoutProcessor extends LayoutProcessor {

    public enum Align {
        LEFT, RIGHT
    }
    private AdsWidgetDef editor;
    private Align align;

    public ValEditorLayoutProcessor(BaseWidget wg, AdsWidgetDef valEditor) {
        this(wg, valEditor, Align.RIGHT);
    }

    public ValEditorLayoutProcessor(BaseWidget wg, AdsWidgetDef valEditor, Align align) {
        super(wg);

        assert wg instanceof ValEditorWidget : "Container must be instance of ValEditorWidget";

        this.align = align;
        this.editor = valEditor;
        wg.setLayout(new ValEditorLayout(align));
        wg.revalidate();
    }

    @Override
    public void locate(DragDropLocator locator, Point localPoint) {
        GraphSceneImpl scene = wg.getSceneImpl();
        Point p = wg.convertLocalToGeometry(localPoint);
        Rectangle r = wg.getLayoutGeometry();

        Widgets widgets = editor.getWidgets();
        int size = widgets.size();

        int idx = indexFromPoint(localPoint);
        assert idx >= 0 && idx <= size : "index out of range: " + editor + ", " + localPoint;


        if (size == 0) {
            locator.locate(Arrays.asList(new Point(r.x + r.width, r.y), new Point(r.x + r.width, r.y + r.height)));
        } else if (idx == size) {
            locator.locate(Arrays.asList(new Point(r.x + r.width, r.y), new Point(r.x + r.width, r.y + r.height)));
        } else {
            RadixObject node1 = AdsUIUtil.getVisualNode(widgets.get(idx));
            BaseWidget w1 = (BaseWidget) scene.findWidget(node1);
            Rectangle r1 = w1.getGeometry();

            locator.locate(Arrays.asList(new Point(r1.x, r.y), new Point(r1.x, r.y + r.height)));
        }

    }

    @Override
    public RadixObject add(RadixObject node, Point localPoint) {
        Widgets widgets = editor.getWidgets();
        int size = widgets.size();

        int idx = indexFromPoint(localPoint);
        assert idx >= 0 && idx <= size : "index out of range: " + editor + ", " + node + ", " + localPoint;

        AdsWidgetDef w = null;
        if (node instanceof AdsLayout) {
            w = new AdsWidgetDef(AdsMetaInfo.WIDGET_CLASS);
            w.setLayout((AdsLayout)node);
        } else if (node instanceof AdsWidgetDef) {
            w = (AdsWidgetDef)node;
        }

        if (w != null) {
            double weight = 1./(size + 1);
            for (int i=0; i<size; i++) {
                widgets.get(i).setWeight(widgets.get(i).getWeight() - weight / size);
            }
            widgets.add(idx, w);
            widgets.get(idx).setWeight(weight);
            wg.revalidate();
        }

        return w;
    }

    @Override
    public RadixObject remove(RadixObject node) {
        AdsWidgetDef w = node instanceof AdsLayout ? (AdsWidgetDef)node.getContainer() : (AdsWidgetDef)node;
        if (w != null && editor.equals(w.getOwnerDefinition())) {
            double weight = w.getWeight();
            node.delete();

            Widgets widgets = editor.getWidgets();
            int size = widgets.size();

            for (int i=0; i<size; i++) {
                widgets.get(i).setWeight(widgets.get(i).getWeight() + weight / size);
            }

            wg.revalidate();
            return w;
        }

        return null;
    }

    private int indexFromPoint(Point localPoint) {
        GraphSceneImpl scene = wg.getSceneImpl();
        Point p = wg.convertLocalToGeometry(localPoint);

        Widgets widgets = editor.getWidgets();
        int size = widgets.size();

        for (int i = 0; i < size; ++i) {
            RadixObject node = AdsUIUtil.getVisualNode(widgets.get(i));
            BaseWidget w = (BaseWidget) scene.findWidget(node);
            Rectangle r = w.getGeometry();
            if (i == 0 && r.x + r.width / 2.0 >= p.x) {
                return 0;
            } else if (i == size - 1 && r.x + r.width / 2.0 < p.x) {
                return size;
            } else {
                if (i > 0) {
                    RadixObject node0 = AdsUIUtil.getVisualNode(widgets.get(i - 1));
                    BaseWidget w0 = (BaseWidget) scene.findWidget(node0);
                    Rectangle r0 = w0.getGeometry();
                    if (r0.x + r0.width / 2.0 < p.x && r.x + r.width / 2.0 >= p.x) {
                        return i;
                    }
                }
            }
        }

        return 0;
    }

    private ValEditorWidget getWidget() {
        return (ValEditorWidget) wg;
    }
}
