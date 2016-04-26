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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Point;
import java.util.Collections;
import java.util.List;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Scene;


public final class DragDropLocator {

    private static final BasicStroke STROKE = new BasicStroke(2.0F, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL);
    private LayerWidget interractionLayer = null;
    private ConnectionWidget lineWidget = null;

    public DragDropLocator(LayerWidget interractionLayer) {
        this.interractionLayer = interractionLayer;
    }

    public void show() {
        if (interractionLayer != null) {
            if (lineWidget == null) {
                lineWidget = createLineWidget(interractionLayer.getScene());
            }
            interractionLayer.addChild(lineWidget);
            lineWidget.setControlPoints(Collections.<Point>emptySet(), true);
        }
    }

    public void hide() {
        if (interractionLayer != null && lineWidget != null) {
            interractionLayer.removeChild(lineWidget);
        }
    }

    public void locate(List<Point> points) {
        if (lineWidget != null) {
            lineWidget.setControlPoints(points, true);
        }
    }

    public void reset() {
        if (lineWidget != null) {
            lineWidget.setControlPoints(Collections.<Point>emptySet(), true);
        }
    }

    private ConnectionWidget createLineWidget(Scene scene) {
        ConnectionWidget widget = new TestConnectionWidget(scene);
        widget.setStroke(STROKE);
        widget.setForeground(Color.BLUE);
        return widget;
    }
}
