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

package org.radixware.kernel.designer.dds.editors.diagram.widgets;

import java.awt.Color;
import java.awt.Point;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.MoveProvider;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.graph.GraphScene;
import org.netbeans.api.visual.model.ObjectState;
import org.netbeans.api.visual.widget.Widget;
import org.radixware.kernel.common.defs.dds.DdsDefinition;
import org.radixware.kernel.common.defs.dds.IPlacementSupport;
import org.radixware.kernel.common.defs.dds.DdsDefinitionPlacement;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.designer.dds.editors.diagram.DdsModelDiagram;

/**
 * {@linkplain DdsDefinition} node widget.
 *
 */
abstract class DdsDefinitionNodeWidget extends GradientWidget implements IDdsDefinitionWidget {

    private static final Border border = BorderFactory.createLineBorder(Color.BLACK);
    private static final Border selectedBorder = BorderFactory.createLineBorder(Color.RED, 2);
    private static final WidgetAction moveAction = ActionFactory.createMoveAction(null, new MultiMoveProvider());
    private final DdsDefinition ddsDefinition;

    protected DdsDefinitionNodeWidget(DdsModelDiagram diagram, DdsDefinition ddsDefinition) {
        super(diagram);
        this.ddsDefinition = ddsDefinition;

        setOpaque(true);
        setCheckClipping(true);
        setSelected(false);

        final DdsDefinitionPlacement placement = getScalePlacement();
        this.setPreferredLocation(new Point(placement.getPosX(), placement.getPosY()));

        getActions().addAction(diagram.getConnectAction());
        getActions().addAction(diagram.createSelectAction());
        //getActions().addAction(diagram.getAlignAction());
        getActions().addAction(moveAction);

        final DdsWidgetPopupMenu popupMenu = new DdsWidgetPopupMenu(diagram, ddsDefinition);
        getActions().addAction(ActionFactory.createPopupMenuAction(popupMenu));
        getActions().addAction(ActionFactory.createEditAction(DdsDefinitionEditProvider.Factory.getDefault()));
    }

    protected boolean canMove() {
        return !ddsDefinition.isReadOnly();
    }

    protected final DdsDefinitionPlacement getPlacement() {
        return ((IPlacementSupport) ddsDefinition).getPlacement();
    }

    public void setSelected(boolean isSelected) {
        if (isSelected) {
            this.setBorder(selectedBorder);
        } else {
            this.setBorder(border);
        }
    }

    @Override
    protected void notifyStateChanged(ObjectState previousState, ObjectState state) {
        super.notifyStateChanged(previousState, state);
        if (previousState.isSelected() != state.isSelected()) {
            setSelected(state.isSelected());
        }
    }

    private static class MultiMoveProvider implements MoveProvider {

        private HashMap<Widget, Point> originals = new HashMap<Widget, Point>();
        private Point original;

        @Override
        public void movementStarted(Widget widget) {
            GraphScene scene = (GraphScene) widget.getScene();

            Object object = scene.findObject(widget);
            if (scene.isNode(object)) {
                for (Object o : scene.getSelectedObjects()) {
                    if (scene.isNode(o)) {
                        Widget w = scene.findWidget(o);
                        if (w != null) {
                            originals.put(w, w.getPreferredLocation());
                        }
                    }
                }
            } else {
                originals.put(widget, widget.getPreferredLocation());
            }
        }

        @Override
        public void movementFinished(Widget widget) {
            originals.clear();
            original = null;
        }

        @Override
        public Point getOriginalLocation(Widget widget) {
            original = widget.getPreferredLocation();
            return original;
        }

        @Override
        public void setNewLocation(Widget widget, Point location) {
            int dx = location.x - original.x;
            int dy = location.y - original.y;
            for (Map.Entry<Widget, Point> entry : originals.entrySet()) {
                DdsDefinitionNodeWidget ddsDefinitionNodeWidget = (DdsDefinitionNodeWidget) entry.getKey();
                if (ddsDefinitionNodeWidget.canMove()) {
                    Point point = entry.getValue();
                    Point newPoint = new Point(point.x + dx, point.y + dy);
                    entry.getKey().setPreferredLocation(newPoint);

                    ddsDefinitionNodeWidget.setPlacement(newPoint.x, newPoint.y);
//                    ddsDefinitionNodeWidget.getPlacement().setPosX((int)(newPoint.x / ddsDefinitionNodeWidget.getScale()));
//                    ddsDefinitionNodeWidget.getPlacement().setPosY((int)(newPoint.y / ddsDefinitionNodeWidget.getScale()));
                }
            }

            // update refs
            final DdsModelDiagram scene = (DdsModelDiagram) widget.getScene();
            for (DdsReferenceDef ref : scene.getEdges()) {
                final DdsReferenceWidget refWidget = (DdsReferenceWidget) scene.findWidget(ref);
                if (refWidget != null) {
                    refWidget.update();
                }
            }
        }
    }

    @Override
    public String toString() {
        return super.toString() + "; " + ddsDefinition.toString();
    }

    protected float getScaleFactor() {
        return ((DdsModelDiagram)getScene()).getScaleFactor();
    }

    protected int scaleToPixels(int val) {
        return ((DdsModelDiagram)getScene()).scaleToPixels(val);
    }

    /**
     * Returns location of widget in pixels scaled by font-size.
     */
    protected final DdsDefinitionPlacement getScalePlacement() {
        final DdsDefinitionPlacement source = ((IPlacementSupport) ddsDefinition).getPlacement();
        final DdsDefinitionPlacement placement = DdsDefinitionPlacement.Factory.newInstance(
                source.getDefinition(), scaleToPixels(source.getPosX()), scaleToPixels(source.getPosY()));

        return placement;
    }

    /**
     * Sets location of widget in pixels.
     * Location of dds definition will be scaled by font-size
     */
    public final void setPlacement(int x, int y) {
        getPlacement().setPosX((int)(x / getScaleFactor()));
        getPlacement().setPosY((int)(y / getScaleFactor()));
    }
}
