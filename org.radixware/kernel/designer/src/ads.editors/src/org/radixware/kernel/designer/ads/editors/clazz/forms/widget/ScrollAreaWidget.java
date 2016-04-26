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

package org.radixware.kernel.designer.ads.editors.clazz.forms.widget;

import java.awt.Point;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import org.radixware.kernel.common.defs.ads.ui.AdsUIItemDef;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.defs.ads.ui.AdsWidgetDef;
import org.radixware.kernel.designer.ads.editors.clazz.forms.GraphSceneImpl;


public class ScrollAreaWidget extends FrameWidget {

    public ScrollAreaWidget(GraphSceneImpl scene, AdsWidgetDef node) {
        super(scene, node);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        AdsUIProperty prop = (AdsUIProperty) evt.getNewValue();
        if ("widgetResizable".equals(prop.getName()) && prop instanceof AdsUIProperty.BooleanProperty) {
            AdsUIProperty.BooleanProperty resizable = (AdsUIProperty.BooleanProperty) prop;

            if (resizable.value) {
                setContentGeometry();
            }
            revalidate();

        }
//        else if ("geometry".equals(prop.getName()) && prop instanceof AdsUIProperty.RectProperty) {
//            AdsWidgetDef node = (AdsWidgetDef) getNode();
//            AdsUIProperty.BooleanProperty widgetResizable = (AdsUIProperty.BooleanProperty) AdsUIUtil.getUiProperty(node, "widgetResizable");
//
//            if (widgetResizable != null) {
//                if (widgetResizable.value) {
//                    AdsUIItemDef currentWidget = AdsUIUtil.currentWidget(node);
//                    AdsUIProperty.RectProperty widgetRect = (AdsUIProperty.RectProperty) AdsUIUtil.getUiProperty(currentWidget, "geometry");
//
//                    Rectangle containerGeometry = getContentGeometry();
//                    if (widgetRect != null) {
//                        widgetRect.x = 0;
//                        widgetRect.y = 0;
//                        widgetRect.width = containerGeometry.width;
//                        widgetRect.height = containerGeometry.height;
//                    }
//                }
//            }
//        }

        super.propertyChange(evt);
    }

    @Override
    public boolean isContainer(String clazz, Point localLocation) {
        return super.isContainer(clazz, localLocation) && getContentGeometry().contains(convertLocalToScene(localLocation));
    }

    @Override
    public void setGeometry(int x, int y, int width, int height) {
        super.setGeometry(x, y, width, height);
        setContentGeometry();
    }

    private void setContentGeometry() {
        AdsWidgetDef node = (AdsWidgetDef) getNode();
        AdsUIProperty.BooleanProperty widgetResizable = (AdsUIProperty.BooleanProperty) AdsUIUtil.getUiProperty(node, "widgetResizable");

        if (widgetResizable != null && widgetResizable.value) {
            AdsUIItemDef currentWidget = AdsUIUtil.currentWidget(node);
            AdsUIProperty.RectProperty widgetRect = (AdsUIProperty.RectProperty) AdsUIUtil.getUiProperty(currentWidget, "geometry");

            Rectangle containerGeometry = getContentGeometry();
            if (widgetRect != null) {
                widgetRect.x = 0;
                widgetRect.y = 0;
                widgetRect.width = containerGeometry.width;
                widgetRect.height = containerGeometry.height;
            }
        }
    }
}
