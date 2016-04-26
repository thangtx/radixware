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
import org.netbeans.api.visual.layout.Layout;
import org.netbeans.api.visual.widget.Widget;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.ui.AdsLayout;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.defs.ads.ui.AdsWidgetDef;
import org.radixware.kernel.designer.ads.editors.clazz.forms.widget.*;
import org.radixware.kernel.designer.ads.editors.clazz.forms.GraphSceneImpl;


public class GridLayout implements Layout {

    public GridLayout() {
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

            GraphSceneImpl scene = (GraphSceneImpl) widget.getScene();
            RadixObject node = (RadixObject) scene.findObject(widget);

            AdsLayout layout = null;
            if (node instanceof AdsWidgetDef) {
                layout = ((AdsWidgetDef) AdsUIUtil.currentWidget((AdsWidgetDef) node)).getLayout();
            } else if (node instanceof AdsLayout) {
                layout = (AdsLayout) node;
            }

            if (layout == null) {
                System.err.println("error: grid layout can not be null");
                return;
            }

            assert layout != null : "layout cann't be null";
            layout.adjustItems(); // ???

            final BaseWidget wg = (BaseWidget) widget;

            final Rectangle lr = wg.getLayoutGeometry();
            Rectangle[][] ir = LayoutUtil.justifyGridLayout(layout, lr);
            if (ir == null) // empty
            {
                return;
            }

            /*
            for (Item item: layout.getItems()) {
            Rectangle r = null;
            for (int i = 0; i < item.rowSpan; i++) {
            for (int j = 0; j < item.columnSpan; j++) {
            if (r == null)
            r = ir[item.row + i][ item.column + j];
            else
            r = r.union(ir[item.row + i][ item.column + j]);
            }
            }
            BaseWidget w = (BaseWidget)scene.findWidget(AdsUIUtil.getItemNode(item));
            if (w != null && r != null)
            w.setGeometry(r);
            }
             */
            AdsLayout.Item[][] items = layout.getItemsAsArray();
            int rows = items.length;
            int cols = items[0].length;

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if (items[i][j] == null) {
                        continue;
                    }
                    BaseWidget w = (BaseWidget) scene.findWidget(AdsUIUtil.getItemNode(items[i][j]));
                    if (w != null) {
                        w.setGeometry(ir[i][j]);
                    }
                }
            }

            scene.repaint();
        }
    }
}
