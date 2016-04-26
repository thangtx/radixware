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
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtWidgetDef;
import org.radixware.kernel.designer.ads.editors.clazz.forms.GraphSceneImpl;
import org.radixware.kernel.designer.ads.editors.clazz.forms.widget.BaseWidget;


public class GridWebLayout implements Layout {

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
            BaseWidget w = (BaseWidget) widget;
            final Rectangle r = w.getGeometry();

            GraphSceneImpl scene = (GraphSceneImpl) widget.getScene();
            AdsRwtWidgetDef rw = (AdsRwtWidgetDef) w.getNode();
            for (AdsRwtWidgetDef rwt : rw.getWidgets()) {


                BaseWidget cw = (BaseWidget) scene.findWidget(rwt);


                AdsUIProperty.RectProperty prop = (AdsUIProperty.RectProperty) AdsUIUtil.getUiProperty(rwt, "geometry");
                Rectangle g = prop.getRectangle();

                g.x += r.x;
                g.y += r.y;
                AdsUIProperty.AnchorProperty anchor = (AdsUIProperty.AnchorProperty) rwt.getProperties().getByName("anchor");
                if (anchor != null) {
                    if (anchor.getLeft() != null) {
                        g.x = Math.round(r.width * anchor.getLeft().part) + anchor.getLeft().offset;
                    }
                    if (anchor.getTop() != null) {
                        g.y = +Math.round(r.height * anchor.getTop().part) + anchor.getTop().offset;
                    }
                    if (anchor.getRight() != null) {
                        g.width = Math.round(r.width * anchor.getRight().part) + anchor.getRight().offset - g.x;
                    }
                    if (anchor.getBottom() != null) {
                        g.height = Math.round(r.height * anchor.getBottom().part) + anchor.getBottom().offset - g.y;
                    }
                }
                cw.setGeometry(g);


            }

            scene.repaint();
        }
    }
}
