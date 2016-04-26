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

package org.radixware.kernel.designer.dds.editors.diagram;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import org.netbeans.api.visual.anchor.Anchor;
import org.netbeans.api.visual.anchor.Anchor.Direction;
import org.netbeans.api.visual.anchor.Anchor.Entry;
import org.netbeans.api.visual.anchor.Anchor.Result;
import org.netbeans.api.visual.router.Router;
import org.netbeans.api.visual.widget.ConnectionWidget;


public class ReferenceRouter implements Router {

    static final int OFFSET_EDGE = 16;

    public ReferenceRouter () {
    }

    /**
     * Is entry cycled.
     * @param entry the entry.
     * @return true if entry is cycled, false otherwise.
     */
    private static boolean isCycled(Entry entry) {
        Anchor attached = entry.getAttachedAnchor();
        Anchor opposite = entry.getOppositeAnchor();
        return attached == opposite;
    }

    public java.util.List<Point> routeConnection(ConnectionWidget widget) {
        Anchor sourceAnchor = widget.getSourceAnchor();
        Anchor targetAnchor = widget.getTargetAnchor();
        if (sourceAnchor == null || targetAnchor == null) {
            return Collections.emptyList();
        }

        Point sourcePoint = sourceAnchor.getRelatedSceneLocation();
        Point targetPoint = targetAnchor.getRelatedSceneLocation();

        //set the default test direction to be ANY in order to test all directions
        //to find the best path.
        EnumSet<Direction> sourceDirections = Anchor.DIRECTION_ANY;
        EnumSet<Direction> targetDirections = Anchor.DIRECTION_ANY;

        //if the anchor does not allow arbitrary connection points, then ask the
        //anchor for the attach point.
        if (!sourceAnchor.allowsArbitraryConnectionPlacement()) {
            Result sourceResult = sourceAnchor.compute(widget.getSourceAnchorEntry());
            sourceDirections = sourceResult.getDirections();
            sourcePoint = sourceResult.getAnchorSceneLocation();
        }
        if (!targetAnchor.allowsArbitraryConnectionPlacement()) {
            Result targetResult = targetAnchor.compute(widget.getTargetAnchorEntry());
            targetDirections = targetResult.getDirections();
            targetPoint = targetResult.getAnchorSceneLocation();
        }

        List<Point> points = new ArrayList<Point>();
        points.add(sourcePoint);

        int cycledSize = OFFSET_EDGE;
        if (sourceAnchor == targetAnchor) {
            List<ConnectionWidget> conns = new ArrayList<ConnectionWidget>();
            for (Entry sibling : sourceAnchor.getEntries()) {
                if (isCycled(sibling)) {
                    ConnectionWidget conn = sibling.getAttachedConnectionWidget();
                    if (conn == widget) {
                        break;
                    }
                    if (!conns.contains(conn)) {
                        cycledSize += OFFSET_EDGE;
                        conns.add(conn);
                    }
                }
            }
        }

        Point p1 = (Point)sourcePoint.clone();
        if (sourceDirections.contains(Direction.TOP)) {
            p1.translate(0, -cycledSize);
        } else if (sourceDirections.contains(Direction.LEFT)) {
            p1.translate(-cycledSize, 0);
        } else if (sourceDirections.contains(Direction.RIGHT)) {
            p1.translate(cycledSize, 0);
        } else if (sourceDirections.contains(Direction.BOTTOM)) {
            p1.translate(0, cycledSize);
        }
        
        Point p2 = (Point)targetPoint.clone();
        if (targetDirections.contains(Direction.TOP)) {
            p2.translate(0, -cycledSize);
        } else if (targetDirections.contains(Direction.LEFT)) {
            p2.translate(-cycledSize, 0);
        } else if (targetDirections.contains(Direction.RIGHT)) {
            p2.translate(cycledSize, 0);
        } else if (targetDirections.contains(Direction.BOTTOM)) {
            p2.translate(0, cycledSize);
        }
        
        points.add(p1);
        if (sourceAnchor == targetAnchor) {
            Point mid = new Point(p1.x, p2.y);
            points.add(mid);
        }
        points.add(p2);

        points.add(targetPoint);
        return points;
    }

}
