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

import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.util.List;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.netbeans.api.visual.anchor.AnchorShape;
import org.netbeans.api.visual.anchor.PointShape;
import org.netbeans.api.visual.router.RouterFactory;

import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.EditProvider;

import org.netbeans.api.visual.widget.*;
import org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.*;

import org.netbeans.api.visual.action.MoveControlPointProvider;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.openide.actions.CopyAction;
import org.openide.actions.CutAction;
import org.openide.actions.DeleteAction;
import org.openide.util.NbBundle;
import org.openide.util.actions.SystemAction;
import org.radixware.kernel.common.defs.RadixObject.EEditState;
import org.radixware.kernel.common.defs.ads.clazz.algo.generation.AppUtils;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.*;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.dialog.EdgePanel;
import org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.dialog.EditorDialog;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.ads.common.dialogs.ProfilePanel;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;


public class EdgeWidget extends ConnectionWidget {

    public EdgeWidget(GraphSceneImpl scene, AdsEdge edge) {
        super(scene);
        setTargetAnchorShape(AnchorShape.TRIANGLE_FILLED);
        setEndPointShape(PointShape.SQUARE_FILLED_BIG);
        setRouter(RouterFactory.createFreeRouter());
        setPaintControlPoints(true);
        setControlPointShape(PointShape.SQUARE_FILLED_BIG);
        getActions().addAction(scene.createObjectHoverAction());
        getActions().addAction(scene.createSelectAction());
        getActions().addAction(scene.getReconnectAction());
        
        getActions().addAction(new AddRemoveControlPointAction(3.0, 5.0, null) {
            @Override
            public State mouseClicked(Widget widget, WidgetMouseEvent event) {
                if (event.isControlDown())
                    return super.mouseClicked(widget, event);
                return State.REJECTED;
            }
        });

        getActions().addAction(ActionFactory.createMoveControlPointAction(new MoveControlPointProvider() {
            @Override
            public List<Point> locationSuggested(ConnectionWidget connectionWidget, int index, Point suggestedLocation) {
                AdsEdge edge = getEdge();
                if (edge.isReadOnly())
                    return connectionWidget.getControlPoints();
                List<Point> controlPoints = connectionWidget.getControlPoints();
                int cpSize = controlPoints.size() - 1;
                ArrayList<Point> list = new ArrayList<Point>(controlPoints);
                if (index <= 0 || index >= cpSize)
                    return null;
                if (index == 1)
                    list.set(0,connectionWidget.getSourceAnchor().compute(connectionWidget.getSourceAnchorEntry()).getAnchorSceneLocation());
                if (index == cpSize - 1)
                    list.set(cpSize,connectionWidget.getTargetAnchor().compute(connectionWidget.getTargetAnchorEntry()).getAnchorSceneLocation());
                list.set(index, suggestedLocation);
                connectionWidget.setControlPoints(list, false);
                notifyModified();
                return list;
            }
        }, null));
        
        //edit notification
        getActions().addAction(ActionFactory.createEditAction (new EditProvider() {
            @Override
            public void edit (Widget widget) {
                notifyEdited();
            }
        }));

        getActions().addAction(ActionFactory.createPopupMenuAction (new WidgetPopupMenu()));
    }
    
    public GraphSceneImpl getSceneImpl() {
        return (GraphSceneImpl)getScene();
    }
    
    public AdsEdge getEdge() {
        AdsEdge edge = (AdsEdge)getSceneImpl().findObject(this);
        assert edge != null;
        return edge;
    }
    
    public AdsPage getOwner() {
        return getSceneImpl().getPage();
    }

    public void notifyModified() {
        AdsEdge edge = getEdge();
        edge.setPoints(getControlPoints());
        edge.setEditState(EEditState.MODIFIED);
    }
    
    /**
     * custom widget editor
     */
    protected boolean notifyEdited() {
        return EditorDialog.execute(new EdgePanel(getEdge()));
    }
    
    /**
     * used to do actions on destroy
     */
    @Override
    protected void notifyRemoved () {
    }

    private static final double HIT_DISTANCE_SQUARE = 4.0;

    @Override
    public boolean isHitAt (Point localLocation) {
        if (getState().isSelected())
            return super.isHitAt(localLocation);

        List<Point> controlPoints = getControlPoints();
        for (int i = 0; i < controlPoints.size () - 1; i++) {
            Point point1 = controlPoints.get(i);
            Point point2 = controlPoints.get(i + 1);
            double dist = Line2D.ptSegDistSq (point1.x, point1.y, point2.x, point2.y, localLocation.x, localLocation.y);
            if (dist < HIT_DISTANCE_SQUARE)
                return true;
        }

        return false;
    }

    @Override
    protected void paintWidget() {
        super.paintWidget();

        if (!AppUtils.isTraceable(getEdge()))
            return;

        Graphics2D gr = getGraphics();        
        RadixIcon image = RadixWareIcons.EVENT_LOG.getForSeverity(getEdge().getTraceSeverity());
        if (image == null)
            return;

        int x, y;
        List<Point> points = getControlPoints();
        if (points.size() < 2)
            return;

        if (points.size() % 2 == 0) {
            x = (points.get(points.size()/2-1).x + points.get(points.size()/2).x) / 2;
            y = (points.get(points.size()/2-1).y + points.get(points.size()/2).y) / 2;
        } else {
            x = points.get(points.size()/2).x;
            y = points.get(points.size()/2).y;
        }
        
        AffineTransform previousTransform = gr.getTransform();
        gr.translate(x, y);
        gr.drawImage(image.getImage(16), -8, -8, null);
        gr.setTransform(previousTransform);
    }

    protected class EditAction extends AbstractAction {

        public EditAction() {
            AdsEdge edge = getEdge();
            putValue(Action.NAME, NbBundle.getMessage(GraphSceneImpl.class, edge.isReadOnly() ? "View" : "Edit"));
            putValue(Action.SMALL_ICON, RadixWareIcons.EDIT.EDIT.getIcon());
            putValue(Action.SHORT_DESCRIPTION, NbBundle.getMessage(GraphSceneImpl.class, "Edit"));
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            notifyEdited();
            repaint();
        }
    }

    protected class ProfileAction extends AbstractAction {

        public ProfileAction() {
            putValue(Action.NAME, NbBundle.getMessage(GraphSceneImpl.class, "ProfilingSettings"));
            putValue(Action.SHORT_DESCRIPTION, NbBundle.getMessage(GraphSceneImpl.class, "ProfilingSettings"));
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            AdsEdge node = getEdge();
            ProfilePanel panel = new ProfilePanel();
            panel.open(node);
            ModalDisplayer displayer = new ModalDisplayer(panel, "Profiling settings for " + node.getAdsDefinition().getQualifiedName());
            displayer.getDialogDescriptor().setValid(panel.isOk());
            if (displayer.showModal()) {
                panel.apply();
            }
        }
    }

    private class WidgetPopupMenu implements PopupMenuProvider, ActionListener {

        @Override
        public JPopupMenu getPopupMenu (Widget widget, Point localLocation) {
            AdsEdge edge = getEdge();
            JPopupMenu popupMenu = new JPopupMenu();

            addMenuItem(popupMenu, new EditAction());
            popupMenu.addSeparator();

            addMenuItem(popupMenu, new ProfileAction());
            popupMenu.addSeparator();

            Action cutAction = (Action)SystemAction.get(CutAction.class);
            addMenuItem(popupMenu, cutAction);

            Action copyAction = (Action)SystemAction.get(CopyAction.class);
            addMenuItem(popupMenu, copyAction);

            popupMenu.addSeparator();

            Action deleteAction = (Action)SystemAction.get(DeleteAction.class);
            addMenuItem(popupMenu, deleteAction);

            return popupMenu;
        }

        private JMenuItem addMenuItem(JPopupMenu menu, Action action) {
            JMenuItem item = DialogUtils.createMenuItem(action);
            menu.add(item);
            return item;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
        }
    }

}
