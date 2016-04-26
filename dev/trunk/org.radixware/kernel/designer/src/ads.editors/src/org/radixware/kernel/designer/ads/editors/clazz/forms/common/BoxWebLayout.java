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
import java.util.LinkedList;
import java.util.List;
import org.netbeans.api.visual.layout.Layout;
import org.netbeans.api.visual.widget.Widget;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty.BooleanProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty.BooleanValueProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty.RectProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtWidgetDef;
import org.radixware.kernel.designer.ads.editors.clazz.forms.GraphSceneImpl;
import org.radixware.kernel.designer.ads.editors.clazz.forms.widget.BaseWidget;


public final class BoxWebLayout implements Layout {

    final boolean vertical;

    public BoxWebLayout(Widget widget, boolean vertical) {
        this.vertical = vertical;
        justify(widget);
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
        GraphSceneImpl scene = (GraphSceneImpl) widget.getScene();
        synchronized (LayoutProcessor.LOCK) {
            BaseWidget w = (BaseWidget) widget;

            Rectangle r = w.getInnerGeometry();

            AdsRwtWidgetDef rw = (AdsRwtWidgetDef) w.getNode();


            List<AdsRwtWidgetDef> expanded = new LinkedList<AdsRwtWidgetDef>();
            int fixedSize = 0;

            for (AdsRwtWidgetDef child : rw.getWidgets()) {
                BooleanProperty bp = (BooleanProperty) AdsUIUtil.getUiProperty(child, "expand");
                if (bp != null && bp.value) {
                    expanded.add(child);
                } else {
                    RectProperty geometry = (RectProperty) AdsUIUtil.getUiProperty(child, "geometry");
                    assert geometry != null;
                    if (vertical) {
                        fixedSize += geometry.height;
                    } else {
                        fixedSize += geometry.width;
                    }
                }
            }
            int sizeForExpanded = vertical ? r.height : r.width;
            sizeForExpanded -= fixedSize;
            if (sizeForExpanded < 0 || expanded.isEmpty()) {
                sizeForExpanded = 0;
            } else {
                sizeForExpanded = Math.round((float) sizeForExpanded / expanded.size());
            }
            int posx, posy;
            int width = 0, height = 0;
            if (vertical) {
                posx = 3;
                width = r.width - 6;
                posy = 3;
            } else {
                posx = 3;
                height = r.height - 6;
                posy = 3;
            }
            for (AdsRwtWidgetDef child : rw.getWidgets()) {
                BaseWidget cw = (BaseWidget) scene.findWidget(child);

                if (cw == null) {
                    continue;
                }
                if (expanded.contains(child)) {
                    if (vertical) {
                        cw.setGeometry(posx + r.x, posy + r.y, width, sizeForExpanded);
                        posy += sizeForExpanded;
                    } else {
                        cw.setGeometry(posx + r.x, posy + r.y, sizeForExpanded, height);
                        posx += sizeForExpanded;
                    }
                } else {
                    RectProperty geometry = (RectProperty) AdsUIUtil.getUiProperty(child, "geometry");
                    if (vertical) {
                        int h = geometry.height;
                        cw.setGeometry(posx + r.x, posy + r.y, width, h);
                        posy += h;
                    } else {
                        int lw = geometry.width;
                        cw.setGeometry(posx + r.x, posy + r.y, lw, height);
                        posx += lw;
                    }
                }
            }
        }
    }
}
