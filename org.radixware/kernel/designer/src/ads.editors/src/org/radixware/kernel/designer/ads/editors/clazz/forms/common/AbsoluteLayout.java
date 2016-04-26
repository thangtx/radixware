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

import java.util.ArrayList;
import java.util.List;
import org.netbeans.api.visual.layout.Layout;
import org.netbeans.api.visual.widget.Widget;
import org.radixware.kernel.common.defs.ads.ui.AdsMetaInfo;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.defs.ads.ui.AdsWidgetDef;
import org.radixware.kernel.designer.ads.editors.clazz.forms.GraphSceneImpl;
import org.radixware.kernel.designer.ads.editors.clazz.forms.widget.BaseWidget;


public class AbsoluteLayout implements Layout {

    public AbsoluteLayout() {
    }

    @Override
    public void layout(Widget wg) {
        justify(wg);
    }

    @Override
    public boolean requiresJustification(Widget wg) {
        return true;
    }

    @Override
    public void justify(Widget wg) {
        synchronized (LayoutProcessor.LOCK) {

            GraphSceneImpl scene = (GraphSceneImpl) wg.getScene();
            AdsWidgetDef def = (AdsWidgetDef) ((BaseWidget)wg).getNode();//scene.findObject(wg);

            List<AdsWidgetDef> widgets;
            String uiClassName = AdsUIUtil.getUiClassName(def);
            if (AdsMetaInfo.TAB_WIDGET_CLASS.equals(uiClassName)
                || AdsMetaInfo.STACKED_WIDGET_CLASS.equals(uiClassName)
                || AdsMetaInfo.SCROLL_AREA_CLASS.equals(uiClassName)) {

                widgets = def.getWidgets().list();
            } else {
                widgets = new ArrayList<>();
                widgets.add(def);
            }

            for (AdsWidgetDef w : widgets) {
                justify(scene, w);
            }

            scene.repaint();
        }
    }

    private void justify(GraphSceneImpl scene, AdsWidgetDef widget) {
        if (!(widget != null && widget.getLayout() == null)) {
            System.err.println("error: absolute layout must be null");
            return;
        }

        assert widget != null && widget.getLayout() == null : "layout must be null";

        AdsWidgetDef.Widgets widgets = widget.getWidgets();
        if (widgets.size() == 0) {
            return;
        }

        for (AdsWidgetDef w : widgets) {
            BaseWidget wgt = (BaseWidget) scene.findWidget(AdsUIUtil.getVisualNode(w));
            if (wgt != null && !wgt.isSelfContained()) {
                wgt.restoreGeometry();
            }
        }
    }
}