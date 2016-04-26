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
import org.radixware.kernel.designer.ads.editors.clazz.forms.GraphSceneImpl;
import org.radixware.kernel.designer.ads.editors.clazz.forms.widget.BaseWidget;


public class VerticalLayout implements Layout {

    public VerticalLayout() {
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
                layout = ((AdsWidgetDef) AdsUIUtil.currentWidget(((AdsWidgetDef) node))).getLayout();
            } else if (node instanceof AdsLayout) {
                layout = (AdsLayout) node;
            }

            if (layout == null) {
                System.err.println("error: vertical layout can not be null");
                return;
            }

            assert layout != null : "layout cann't be null";

            final BaseWidget wg = (BaseWidget) widget;

            final Rectangle r = wg.getGeometry();
            final Rectangle lr = wg.getLayoutGeometry();
            Rectangle[] ir = LayoutUtil.justifyVerticalLayout(layout, lr);
            if (ir == null) {
                return;
            }

            AdsLayout.Items items = layout.getItems();
            for (int idx = 0; idx < ir.length; idx++) {
                RadixObject object = AdsUIUtil.getItemNode(items.get(idx));
                assert object != null;

                BaseWidget w = (BaseWidget) scene.findWidget(object);
//            if (widget.getState().isSelected() || w.getState().isSelected()) {
//                ir[idx] = LayoutUtil.adjustSelection(ir[idx], r);
//            }

                if (w != null) {
                    w.setGeometry(ir[idx]);
                }
//            w.saveGeometry();
            }

            scene.repaint();
        }
    }
}
