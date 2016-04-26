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

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import org.netbeans.api.visual.layout.Layout;
import org.netbeans.api.visual.widget.Widget;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.defs.ads.ui.AdsWidgetDef;
import org.radixware.kernel.common.defs.ads.ui.AdsWidgetDef.Widgets;
import org.radixware.kernel.designer.ads.editors.clazz.forms.GraphSceneImpl;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.Item;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.WidgetItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.ValEditorItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.widget.BaseWidget;
import org.radixware.kernel.designer.ads.editors.clazz.forms.widget.ValEditorWidget;

public class ValEditorLayout implements Layout {

    private ValEditorLayoutProcessor.Align align;

    public ValEditorLayout(ValEditorLayoutProcessor.Align align) {
        super();

        this.align = align;
    }

    @Override
    public void layout(Widget widget) {
        justify(widget);
    }

    @Override
    public boolean requiresJustification(Widget widget) {
        return true;
    }

    @Override
    public void justify(Widget widget) {
        synchronized (LayoutProcessor.LOCK) {

            final GraphSceneImpl scene = (GraphSceneImpl) widget.getScene();
            final ValEditorWidget editWidget = (ValEditorWidget) widget;
            final AdsWidgetDef node = editWidget.getNode(); //scene.findObject(widget);

            final Rectangle lr = editWidget.getLayoutGeometry();
            final List<Rectangle> geometries = getChildGeometries(node, lr, align);

            if (geometries == null) {
                return;
            }

            final Widgets widgets = node.getWidgets();
            for (int i = 0; i < geometries.size(); i++) {
                final AdsWidgetDef w = widgets.get(i);
                final BaseWidget wgt = (BaseWidget) scene.findWidget(AdsUIUtil.getVisualNode(w));
                wgt.setGeometry(geometries.get(i));
            }
            scene.repaint();
        }
    }

    public static List<Rectangle> getChildGeometries(AdsWidgetDef node, Rectangle layoutGeometry, ValEditorLayoutProcessor.Align align) {

        assert node != null :  "Widget is null";

        final ValEditorItem.ValEditorPropertyCollector collector = (ValEditorItem.ValEditorPropertyCollector) ((WidgetItem)Item.getItem(node)).getPropertyCollector();

        final int btnWidth = ValEditorItem.DEFAULT_BUTTON_WIDTH;
        final int valueWidth = collector.getMinFreeAreaWidth(node, layoutGeometry.width);
        final int childCount = node.getWidgets().size();

        final List<Rectangle> rectList = new ArrayList<>(childCount);

        final int width = layoutGeometry.width, height = layoutGeometry.height,
            x0 = layoutGeometry.x, y0 = layoutGeometry.y;

        int btnAreaStart = x0 + width - childCount * btnWidth + (childCount - 1);

        if (collector.clearButtonEnabled(node)) {
            btnAreaStart -= btnWidth - 1;
        }

        if (btnAreaStart < x0 + valueWidth || align == ValEditorLayoutProcessor.Align.LEFT) {
            btnAreaStart = x0 + valueWidth;
        }

        for (int i = 0; i < childCount; ++i) {
            rectList.add(new Rectangle(btnAreaStart + i * btnWidth - i, y0, btnWidth, height));
        }

        return rectList;
    }
}
