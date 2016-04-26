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

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.Collections;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Widget;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.designer.ads.editors.clazz.forms.GraphSceneImpl;
import org.radixware.kernel.designer.ads.editors.clazz.forms.widget.BaseWidget;


public final class MoveAction extends WidgetAction.LockedAdapter {

    final static private int RADIUS = 3;
    private BaseWidget movingWidget = null;
    private Point dragSceneLocation = null;
    private Point originalSceneLocation = null;
    private boolean dragging = false;
    final private DragDropLocator locator;

    public MoveAction(LayerWidget interractionLayer) {
        locator = new DragDropLocator(interractionLayer);
    }

    @Override
    protected boolean isLocked() {
        return movingWidget != null;
    }

    @Override
    public State mousePressed(Widget widget, WidgetMouseEvent event) {
        if (isLocked()) {
            return State.createLocked(widget, this);
        }

        if (event.getButton() == MouseEvent.BUTTON1 && event.getClickCount() == 1) {

            movingWidget = (BaseWidget) widget;
            movingWidget.toFront();

            dragging = false;

            originalSceneLocation = widget.getPreferredLocation();
            dragSceneLocation = widget.convertLocalToScene(event.getPoint());

            return State.createLocked(widget, this);
        }
        return State.REJECTED;
    }

    @Override
    public State mouseDragged(Widget widget, WidgetMouseEvent event) {
        if (movingWidget == null) {
            return State.REJECTED;
        }
        return drag(movingWidget, movingWidget.convertLocalToScene(event.getPoint())) ? State.createLocked(widget, this) : State.REJECTED;
    }

    @Override
    public State mouseReleased(Widget widget, WidgetMouseEvent event) {

        boolean result = dragging ? drop(movingWidget, event.getPoint()) : true;

        if (!dragging && movingWidget != null) {
            movingWidget.notifyClicked(event.getPoint());
        }

        dragSceneLocation = null;
        originalSceneLocation = null;
        movingWidget = null;
        dragging = false;

        return result ? State.CONSUMED : State.REJECTED;
    }

    private boolean drop(BaseWidget widget, Point location) {

        final Point newLocation = widget.convertLocalToScene(location);
        if (!move(widget, newLocation, true)) {
            return false;
        }

        locator.hide();

        final GraphSceneImpl scene = widget.getSceneImpl();

        BaseWidget container = null;
        if (widget != scene.getContainerRoot()) {
            container = scene.findSuitableContainer(newLocation, widget);
            if (container == null) {
                container = scene.getContainerRoot();
            }
        }

        if (container != null) {
            container.add(widget, container.convertSceneToLocal(newLocation));
            widget.saveGeometry();
        }

        final RadixObject node = widget.getNode();
        AdsUIUtil.fire(node, AdsUIUtil.getUiProperty(node, "geometry"));

        widget.restoreGeometry();
        scene.revalidate();

        scene.setSelectedObjects(Collections.singleton(node));
        scene.updateSelection();

        return true;
    }

    private boolean move(BaseWidget widget, final Point sceneLocation, final boolean hasOffset) {

        final GraphSceneImpl scene = widget.getSceneImpl();
        final Point location = new Point(originalSceneLocation.x + sceneLocation.x - dragSceneLocation.x, originalSceneLocation.y + sceneLocation.y - dragSceneLocation.y);
        final BaseWidget container = scene.findSuitableContainer(sceneLocation, widget);

        if (container != null && hasOffset) {
            final Point offset = container.offsetPoint();
            location.x += offset.x;
            location.y += offset.y;
        }

        if (scene.isSnaped()) {
            location.x = Math.round(1f * location.x / GraphSceneImpl.SNAP_SIZE) * GraphSceneImpl.SNAP_SIZE;
            location.y = Math.round(1f * location.y / GraphSceneImpl.SNAP_SIZE) * GraphSceneImpl.SNAP_SIZE;
        }

        widget.setPreferredLocation(location);

        if (!widget.isSelfContained() && container != null) {
            container.locate(locator, container.convertSceneToLocal(sceneLocation));
        } else {
            locator.reset();
        }
        return true;
    }

    private boolean drag(BaseWidget widget, final Point sceneLocation) {

        final GraphSceneImpl scene = widget.getSceneImpl();
        final RadixObject node = widget.getNode();

        final Point newSceneLocation = widget.convertLocalToScene(sceneLocation);
        final int dx = newSceneLocation.x - dragSceneLocation.x;
        final int dy = newSceneLocation.y - dragSceneLocation.y;

        if (!dragging) {
            if (Math.sqrt(dx * dx + dy * dy) < RADIUS || AdsUIUtil.isReadOnlyNode(widget.getSceneImpl().getUI(), node)) {
                return true;
            }
            widget.setModified();
            final BaseWidget ownerWidget = widget.getContainerWidget();
            if (ownerWidget != null) {
                ownerWidget.remove(widget);
            }
            dragging = true;

            if (!widget.isSelfContained()) {
                locator.show();
            }

            scene.setSelectedObjects(Collections.EMPTY_SET);
            scene.updateSelection();
        }

        move(widget, sceneLocation, false);
        return true;
    }
}
