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
import org.radixware.kernel.common.defs.Definitions;
import org.radixware.kernel.common.defs.ads.ui.*;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtWidgetDef;
import org.radixware.kernel.designer.ads.editors.clazz.forms.widget.*;
import org.radixware.kernel.designer.ads.editors.clazz.forms.GraphSceneImpl;


public class SplitterLayout implements Layout {

    public SplitterLayout() {
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
            final AdsUIItemDef node = (AdsUIItemDef) ((BaseWidget) widget).getNode();//(AdsWidgetDef) scene.findObject(widget);
            final BaseWidget wg = (BaseWidget) widget;

            final Rectangle lr = wg.getLayoutGeometry();
            Rectangle[] ir = LayoutUtil.justifySplitterLayout(node, lr);
            if (ir == null) {
                return;
            }

            Definitions<? extends AdsUIItemDef> widgets;
            if (node instanceof AdsWidgetDef) {
                widgets = ((AdsWidgetDef) node).getWidgets();
            } else {
                widgets = ((AdsRwtWidgetDef) node).getWidgets();
            }

            for (int idx = 0; idx < ir.length; idx++) {
                AdsUIItemDef w = widgets.get(idx);
                BaseWidget wgt = (BaseWidget) scene.findWidget(AdsUIUtil.getVisualNode(w));
                wgt.setGeometry(ir[idx]);
            }

            scene.repaint();
        }
    }
}
