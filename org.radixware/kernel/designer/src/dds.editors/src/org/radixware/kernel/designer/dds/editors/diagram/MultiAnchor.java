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
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.api.visual.anchor.Anchor;

/**
 * GraphModel anchor witch supports several connection widgets between two
 * nodes. Also supports cycled connection widgets. Table or external table has
 * anchor, if it has one or more connected references. Each anchor has entry for
 * each connected reference. If reference has no source/target widget it has
 * source/target anchor with null source/target widget, wich displayed in
 * Point(0,0).
 *
 */
class MultiAnchor extends Anchor {

    private final int CYCLED_SIZE = 10;

    public MultiAnchor(Widget widget) {
        super(widget);
    }

    /**
     * Is entry cycled.
     *
     * @param entry the entry.
     * @return true if entry is cycled, false otherwise.
     */
    private static boolean isCycled(Entry entry) {
        Anchor attached = entry.getAttachedAnchor();
        Anchor opposite = entry.getOppositeAnchor();
        return attached == opposite;
    }

    private static Rectangle getWidgetBounds(Widget widget) {
        if (widget != null) {
            Rectangle bounds = widget.getBounds();
            bounds = widget.convertLocalToScene(bounds);
            return bounds;
        } else {
            return new Rectangle(-50, -50, 100, 100);
        }
    }

    /**
     * Calculate anchor direction for the entry.
     *
     * @param entry the entry.
     * @return anchor direction for the entry of null if Anchor.DIRECTION_ANY.
     */
    private static Anchor.Direction calcDirection(Entry entry) {
        Anchor attachedAnchor = entry.getAttachedAnchor();
        Anchor oppositeAnchor = entry.getOppositeAnchor();

        Widget attachedWidget = attachedAnchor.getRelatedWidget();
        Widget oppositeWidget = oppositeAnchor.getRelatedWidget();

        Rectangle attachedBounds = getWidgetBounds(attachedWidget);
        Rectangle oppositeBounds = getWidgetBounds(oppositeWidget);

        if (attachedBounds.isEmpty() || oppositeBounds.isEmpty()) {
            return null;
        }

        Point atachedCenter = attachedAnchor.getRelatedSceneLocation();
        Point oppositeCenter = oppositeAnchor.getRelatedSceneLocation();

        if (isCycled(entry)) {
            if (entry.isAttachedToConnectionSource()) {
                return Anchor.Direction.RIGHT;
            } else {
                return Anchor.Direction.TOP;
            }
        }

        if (atachedCenter.equals(oppositeCenter)) {
            return null; // source and targer has one center.
        }

        if (oppositeBounds.y + oppositeBounds.height < attachedBounds.y) {
            if (oppositeBounds.x > attachedBounds.x + attachedBounds.width) {
                // TopRight
                if (oppositeBounds.x - (attachedBounds.x + attachedBounds.width) > attachedBounds.y - (oppositeBounds.y + oppositeBounds.height)) {
                    return Anchor.Direction.RIGHT;
                } else {
                    return Anchor.Direction.TOP;
                }
            } else if (oppositeBounds.x + oppositeBounds.width < attachedBounds.x) {
                // TopLeft
                if (attachedBounds.x - (oppositeBounds.x + oppositeBounds.width) > attachedBounds.y - (oppositeBounds.y + oppositeBounds.height)) {
                    return Anchor.Direction.LEFT;
                } else {
                    return Anchor.Direction.TOP;
                }
            } else {
                return Anchor.Direction.TOP;
            }
        }

        if (oppositeBounds.y > attachedBounds.y + attachedBounds.height) {
            if (oppositeBounds.x > attachedBounds.x + attachedBounds.width) {
                // BottomRight
                if (oppositeBounds.x - (attachedBounds.x + attachedBounds.width) > oppositeBounds.y - (attachedBounds.y + attachedBounds.height)) {
                    return Anchor.Direction.RIGHT;
                } else {
                    return Anchor.Direction.BOTTOM;
                }
            } else if (oppositeBounds.x + oppositeBounds.width < attachedBounds.x) {
                // BottomLeft
                if (attachedBounds.x - (oppositeBounds.x + oppositeBounds.width) > oppositeBounds.y - (attachedBounds.y + attachedBounds.height)) {
                    return Anchor.Direction.LEFT;
                } else {
                    return Anchor.Direction.BOTTOM;
                }
            } else {
                return Anchor.Direction.BOTTOM;
            }
        }

        if (oppositeCenter.x > atachedCenter.x) {
            return Direction.RIGHT;
        } else {
            return Direction.LEFT;
        }
    }

    /**
     * Calculate entry position and direction.
     */
    @Override
    public Result compute(Entry entry) {
        // center of this widget
        Point relatedLocation = getRelatedSceneLocation();

        // calculate direction of this entry
        Anchor.Direction direction = calcDirection(entry);
        if (direction == null) {
            return new Anchor.Result(relatedLocation, Anchor.DIRECTION_ANY);
        }

        Widget widget = getRelatedWidget();
        Rectangle bounds = getWidgetBounds(widget);

        int cycledSize = 0;
        List<Entry> nonCycledEntriesWithSameDirection = new ArrayList<Entry>();
        for (Entry sibling : this.getEntries()) {
            Anchor.Direction eDirection = calcDirection(sibling);
            if (isCycled(sibling)) {
                cycledSize += CYCLED_SIZE;
                if (entry == sibling) {
                    Point p;
                    if (entry.isAttachedToConnectionSource()) {
                        p = new Point(bounds.x + bounds.width, bounds.y + cycledSize);
                    } else {
                        p = new Point(bounds.x + bounds.width - cycledSize, bounds.y);
                    }
                    return new Anchor.Result(p, direction);
                }
            } else if (eDirection == direction) {
                nonCycledEntriesWithSameDirection.add(sibling);
            }
        }

        // sort entries with the same direction in order of center of opposit entries
        Collections.sort(nonCycledEntriesWithSameDirection, new CentreComparator(direction, relatedLocation));
        int idx = nonCycledEntriesWithSameDirection.indexOf(entry) + 1;
        int spaceCount = nonCycledEntriesWithSameDirection.size() + 1;
        Point p;
        switch (direction) {
            case BOTTOM:
                p = new Point(bounds.x + bounds.width * idx / spaceCount, bounds.y + bounds.height);
                break;
            case TOP:
                p = new Point(bounds.x + (bounds.width - cycledSize) * idx / spaceCount, bounds.y);
                break;
            case LEFT:
                p = new Point(bounds.x, bounds.y + bounds.height * idx / spaceCount);
                break;
            case RIGHT:
                p = new Point(bounds.x + bounds.width, bounds.y + (bounds.height - cycledSize) * idx / spaceCount + cycledSize);
                break;
            default:
                p = relatedLocation;
                break;
        }
        return new Anchor.Result(p, direction);
    }

    /**
     * Allows to sort definitions in order of their centers according to
     * specified direction.
     */
    private static class CentreComparator implements Comparator<Entry> {

        private final Anchor.Direction direction;
        private final Point relatedLocation;

        public CentreComparator(Anchor.Direction direction, Point relatedLocation) {
            this.direction = direction;
            this.relatedLocation = relatedLocation;
        }

        @Override
        public int compare(Entry entry1, Entry entry2) {
            Point oppositeLocation1 = entry1.getAttachedAnchor().getOppositeSceneLocation(entry1);
            Point oppositeLocation2 = entry2.getAttachedAnchor().getOppositeSceneLocation(entry2);
            Point p1 = new Point(oppositeLocation1.x - relatedLocation.x, oppositeLocation1.y - relatedLocation.y);
            Point p2 = new Point(oppositeLocation2.x - relatedLocation.x, oppositeLocation2.y - relatedLocation.y);
            int vp = p2.x * p1.y - p2.y * p1.x;
            if (direction == Anchor.Direction.TOP || direction == Anchor.Direction.RIGHT) {
                return vp;
            } else {
                return -vp;
            }
        }
    }

//    private static class CentreComparator implements Comparator<Entry> {
//
//        private final Anchor.Direction direction;
//
//        public CentreComparator(Anchor.Direction direction) {
//            this.direction = direction;
//        }
//
//        @Override
//        public int compare(Entry entry1, Entry entry2) {
//            Point oppositeLocation1 = entry1.getAttachedAnchor().getOppositeSceneLocation(entry1);
//            Point oppositeLocation2 = entry2.getAttachedAnchor().getOppositeSceneLocation(entry2);
//            if (direction == Anchor.Direction.LEFT || direction == Anchor.Direction.RIGHT) {
//                return oppositeLocation1.y - oppositeLocation2.y;
//            } else {
//                return oppositeLocation1.x - oppositeLocation2.x;
//            }
//        }
//    }
    @Override
    public Point getRelatedSceneLocation() {
        Widget relatedWidget = getRelatedWidget();
        if (relatedWidget != null) {
            return super.getRelatedSceneLocation();
        }
        return new Point(0, 0);
    }
}
