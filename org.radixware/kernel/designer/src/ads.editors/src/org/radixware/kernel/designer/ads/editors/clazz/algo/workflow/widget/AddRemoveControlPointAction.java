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

package org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.widget;

import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.Widget;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.util.ArrayList;


public class AddRemoveControlPointAction extends WidgetAction.Adapter {

    private double createSensitivity;
    private double deleteSensitivity;
    private ConnectionWidget.RoutingPolicy routingPolicy;

    public AddRemoveControlPointAction (double createSensitivity, double deleteSensitivity, ConnectionWidget.RoutingPolicy routingPolicy) {
        this.createSensitivity = createSensitivity;
        this.deleteSensitivity = deleteSensitivity;
        this.routingPolicy = routingPolicy;
    }

    public State mouseClicked(Widget widget, WidgetMouseEvent event) {
        if(event.getButton()==MouseEvent.BUTTON1 && event.getClickCount()==2  &&  widget instanceof ConnectionWidget) {
            addRemoveControlPoint ((ConnectionWidget) widget, event.getPoint ());
            return State.CONSUMED;
        }
        return State.REJECTED;
    }
    
    /**
     * Adds or removes a control point on a specified location
     * @param widget the connection widget
     * @param localLocation the local location
     */
    private void addRemoveControlPoint (ConnectionWidget widget, Point localLocation) {
        ArrayList<Point> list = new ArrayList<Point> (widget.getControlPoints ());
        if (!removeControlPoint (localLocation, list, deleteSensitivity)) {
            Point exPoint = null;
            int index = 0;
            for (Point elem : list) {
                if (exPoint != null) {
                    Line2D l2d = new Line2D.Double (exPoint, elem);
                    if (l2d.ptLineDist (localLocation) < createSensitivity) {
                        list.add (index, localLocation);
                        break;
                    }
                }
                exPoint = elem;
                index++;
            }
        }
        if (routingPolicy != null)
            widget.setRoutingPolicy (routingPolicy);
        widget.setControlPoints (list, false);
    }
    
    private boolean removeControlPoint(Point point, ArrayList<Point> list, double deleteSensitivity){
        for (Point elem : list) {
            if (elem.distance (point) < deleteSensitivity) {
                list.remove (elem);
                return true;
            }
        }
        return false;
    }

}
