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

import java.awt.Color;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.Rectangle;
import org.netbeans.api.visual.action.ResizeProvider.ControlPoint;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.border.BorderFactory;

import org.netbeans.api.visual.action.EditProvider;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.PopupMenuProvider;

import org.netbeans.api.visual.laf.LookFeel;
import org.netbeans.api.visual.model.ObjectState;

import org.netbeans.api.visual.widget.*;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.util.Map;
import java.util.List;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;
import org.openide.util.NbBundle;
import org.openide.actions.CopyAction;
import org.openide.util.actions.SystemAction;
import org.netbeans.api.visual.action.MoveProvider;
import org.netbeans.api.visual.action.MoveStrategy;
import org.netbeans.api.visual.action.ResizeProvider;
import org.netbeans.api.visual.action.ResizeStrategy;
import org.openide.actions.CutAction;
import org.openide.actions.DeleteAction;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObject.EEditState;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.*;
import org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.*;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.ads.common.dialogs.ProfilePanel;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;


public abstract class BaseWidget<N extends AdsBaseObject> extends Widget {
    
    static final Color BORDER_COLOR = Color.BLACK;
    static final Color BODY_COLOR = new Color(250, 250, 250);    
    static final ObjectState STATE_SELECTED = ObjectState.createNormal().deriveSelected(true);
    
    protected WidgetAction connAction;    
    protected Widget entryWidget, leaveWidget, mainWidget;
    private boolean composed;
                
    public BaseWidget(GraphSceneImpl scene, N node, boolean composed) {
        super(scene);
        this.composed = composed;

        connAction = scene.getConnectAction();
        
        setState(STATE_SELECTED);
        setLayout(LayoutFactory.createOverlayLayout());
        
        setPreferredSize(new Dimension(100, 87));
        setMinimumSize(new Dimension(100, 87));
        setCheckClipping(true);
        
        Widget body = new Widget(scene);
        body.setLayout(LayoutFactory.createOverlayLayout());
        body.setBorder(BorderFactory.createEmptyBorder(3));
        addChild(body);
        
        Widget layer, dummy;
                        
// layer 1
        layer = new Widget(scene);
        layer.setLayout(LayoutFactory.createVerticalFlowLayout(LayoutFactory.SerialAlignment.JUSTIFY, 0));
        body.addChild(layer);
                
        if (composed) {
            dummy = new Widget(scene);
            dummy.setPreferredSize(new Dimension(1, 8));
            layer.addChild(dummy);
        }
                
        mainWidget = new Widget(scene);
        mainWidget.setBorder(BorderFactory.createEmptyBorder());
        mainWidget.setLayout(LayoutFactory.createOverlayLayout());
        mainWidget.setCheckClipping(true);
        layer.addChild(mainWidget, 100);
                
        if (composed) {
            dummy = new Widget(scene);
            dummy.setPreferredSize(new Dimension(1, 8));
            layer.addChild(dummy);
        }
        
// layer 2
        if (composed) {
            layer = new Widget(scene);        
            layer.setLayout(LayoutFactory.createVerticalFlowLayout(LayoutFactory.SerialAlignment.JUSTIFY, 0));
            body.addChild(layer);

            Widget top = new Widget(scene);
            top.setLayout(LayoutFactory.createHorizontalFlowLayout(LayoutFactory.SerialAlignment.CENTER, 0));
            layer.addChild(top);
        
            dummy = new Widget(scene);
            dummy.setPreferredSize(new Dimension(10, 1));
            top.addChild(dummy);
        
            entryWidget = new Widget(scene);
            entryWidget.setLayout(LayoutFactory.createHorizontalFlowLayout(LayoutFactory.SerialAlignment.LEFT_TOP, 1));
            top.addChild(entryWidget, 100);

            dummy = new Widget(scene);
            dummy.setPreferredSize(new Dimension(10, 1));
            top.addChild(dummy);

            dummy = new Widget(scene);
            layer.addChild(dummy, 100);
        
            Widget bottom = new Widget(scene);
            bottom.setLayout(LayoutFactory.createHorizontalFlowLayout(LayoutFactory.SerialAlignment.CENTER, 0));
            layer.addChild(bottom);
        
            dummy = new Widget(scene);
            dummy.setPreferredSize(new Dimension(10, 1));
            bottom.addChild(dummy);
        
            leaveWidget = new Widget(scene);
            leaveWidget.setLayout(LayoutFactory.createHorizontalFlowLayout(LayoutFactory.SerialAlignment.RIGHT_BOTTOM, 1));
            bottom.addChild(leaveWidget, 100);

            dummy = new Widget(scene);
            dummy.setPreferredSize(new Dimension(10, 1));
            bottom.addChild(dummy);
        }
        
        //resize widget        
        getActions().addAction(ActionFactory.createResizeAction(new ResizeStrategy() {
            
            @Override
            public Rectangle boundsSuggested(Widget widget, Rectangle originalBounds, Rectangle suggestedBounds, ControlPoint controlPoint) {
                AdsBaseObject node = getNode();
                return node.isReadOnly() ? originalBounds : suggestedBounds;
            }}, new ResizeProvider() {

            @Override
            public void resizingStarted(Widget widget) {
            }

            @Override
            public void resizingFinished(Widget widget) {
                AdsBaseObject node = getNode();
                if (!node.isReadOnly())
                    notifyModified();
            }
        }));        
        //getActions().addAction(ActionFactory.createAlignWithResizeAction(scene.getMainLayer(), scene.getInterLayer(), null, false));
        
        //single-click, the event is not consumed:
        getActions().addAction(scene.createSelectAction());

        //mouse-over, the event is consumed while the mouse is over the widget:
        getActions().addAction(scene.createObjectHoverAction());
        
        //mouse-dragged, the event is consumed while mouse is dragged:
        getActions().addAction(ActionFactory.createMoveAction(
                new MoveStrategy () {
                    @Override
                    public Point locationSuggested (Widget widget, Point originalLocation, Point suggestedLocation) {
                        AdsBaseObject node = getNode();
                        return node.isReadOnly() ? originalLocation : suggestedLocation;
                    }},

                new MoveProvider() {
                    private GraphSceneImpl scene = (GraphSceneImpl)getScene();
                    private HashMap<BaseWidget, Point> originalNodes = new HashMap<BaseWidget, Point>();
                    private HashMap<EdgeWidget, List<Point>> originalEdges = new HashMap<EdgeWidget, List<Point>>();
                    private Point original;

                    @Override
                    public void movementStarted(Widget widget) {
                        bringToFront();
                        for (Object o: scene.getSelectedObjects()) {
                            Widget w = scene.findWidget(o);
                            if (w instanceof BaseWidget)
                                originalNodes.put((BaseWidget)w, w.getPreferredLocation());
                            if (w instanceof EdgeWidget) {
                                List<Point> points = new ArrayList<Point>();
                                for (Point point : ((EdgeWidget)w).getControlPoints())
                                    points.add(point.getLocation());
                                originalEdges.put((EdgeWidget)w, points);
                            }
                        }
                        original = widget.getPreferredLocation();
                    }

                    @Override
                    public void movementFinished(Widget widget) {
                        if (!original.equals(widget.getPreferredLocation())) {
                            for (BaseWidget w: originalNodes.keySet())
                                w.notifyModified();
                            for (EdgeWidget w: originalEdges.keySet())
                                w.notifyModified();
                        }
                        originalNodes.clear();
                        originalEdges.clear();
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
                        for (Map.Entry<BaseWidget, Point> entry: originalNodes.entrySet()) {
                            BaseWidget w = entry.getKey();
                            Point point = entry.getValue();
                            w.setPreferredLocation(new Point(point.x + dx, point.y + dy));
                        }
                        for (Map.Entry<EdgeWidget, List<Point>> entry: originalEdges.entrySet()) {
                            EdgeWidget w = entry.getKey();
                            List<Point> points = new ArrayList<Point>();
                            for (Point point: entry.getValue())
                                points.add(new Point(point.x + dx, point.y + dy));
                            w.setControlPoints(points, false);
                            w.reroute();
                        }
                    }
                }));

        //edit notification
        getActions().addAction(ActionFactory.createEditAction (new EditProvider() {
            @Override
            public void edit (Widget widget) {
                if (notifyEdited()) {
                    if (sync())
                        notifyModified();
                }
            }
        }));

        getActions().addAction(ActionFactory.createPopupMenuAction(new WidgetPopupMenu()));
    }
    
    public GraphSceneImpl getSceneImpl() {
        return (GraphSceneImpl)getScene();
    }
    
    public AdsPage getOwner() {
        return getSceneImpl().getPage();
    }
    
    public N getNode() {
        @SuppressWarnings("unchecked")
        N node = (N)getSceneImpl().findObject(this);
        assert node != null;
        return node;
    }

    @Override
    public void notifyStateChanged (ObjectState previousState, ObjectState state) {
        LookFeel lookFeel = getScene().getLookFeel();
        setBorder(lookFeel.getBorder(state));
    }
    
    public Widget attachPin(AdsPin pin) {
        if (!composed)
            return null; // for non-composed should be overlayed
        Widget pinWidget;
        N node = getNode();
        GraphSceneImpl scene = getSceneImpl();
        if (node.isSourcePin(pin)) {
            pinWidget = new LeavePinWidget(scene, pin);
            leaveWidget.addChild(pinWidget, 100);            
        }
        else {
            pinWidget = new EntryPinWidget(scene, pin);
            entryWidget.addChild(pinWidget, 100);
        }
        return pinWidget;
    }

    public void notifyModified() {
        N node = getNode();
        Rectangle bounds = getBounds();
        Point location = getPreferredLocation();
        Dimension size = getPreferredSize();
        node.setBounds(new Rectangle(location.x, location.y, bounds == null ? size.width : bounds.width, bounds == null ? size.height : bounds.height));
        //if (structModified)
        //    node.setStructModified();
        //else
        node.setEditState(EEditState.MODIFIED);
    }

    /**
     * custom widget editor
     */
    protected boolean notifyEdited() {
        return false;
    }
    
    /*
    * return ordered pins
    */
    public List<AdsPin> getPinsOrdered() {
        List<AdsPin> pins = new ArrayList<AdsPin>();
        GraphSceneImpl scene = getSceneImpl();
        if (composed) {
            for (Widget w: entryWidget.getChildren()) {
                Object pin = scene.findObject(w);
                if (scene.isPin(pin))
                    pins.add((AdsPin)pin);
            }
            for (Widget w: leaveWidget.getChildren()) {
                Object pin = scene.findObject(w);
                if (scene.isPin(pin))
                    pins.add((AdsPin)pin);
            }
        } else
            for (Widget w: mainWidget.getChildren()) {
                Object pin = scene.findObject(w);
                if (scene.isPin(pin))
                    pins.add((AdsPin)pin);
            }
        return pins;
    }

    /*
     * sync node data with widget
     */
    public boolean sync() {
        GraphSceneImpl scene = getSceneImpl();

        N node = getNode();
        node.sync(); // first of all sync node

        List<AdsPin> pins = getPinsOrdered();
        List<AdsPin> pinsNew = node.getPins();

        if (pins.isEmpty()) {   // widget creation
            for (AdsPin pin : pinsNew)
                scene.addPin(node, pin);
            repaint();
            return pinsNew.size() > 0;
        }

        if (pins.equals(pinsNew)) {
            for (AdsPin pin: pins) {
                Widget w = scene.findWidget(pin);
                if (w instanceof EntryPinWidget)
                    ((EntryPinWidget)w).sync();
                if (w instanceof LeavePinWidget)
                    ((LeavePinWidget)w).sync();
            }
            repaint();
            return false;
        }

        HashMap<AdsPin, List<AdsEdge>> pinEdges = new HashMap<AdsPin, List<AdsEdge>>();
        for (AdsPin pin: pins) {
            if (pinsNew.contains(pin)) {
                List<AdsEdge> edges = new ArrayList<AdsEdge>();
                for (AdsEdge edge: scene.findPinEdges(pin, true, true)) {
                    if (pin.equals(edge.getSource()))
                        edge.setSource(null);
                    if (pin.equals(edge.getTarget()))
                        edge.setTarget(null);
                    edges.add(edge);
                }
                pinEdges.put(pin, edges);
            }
            else {
                for (AdsEdge edge: scene.findPinEdges(pin, true, true))
                    edge.delete();
            }
            scene.removePin(pin);
        }

        for (AdsPin pin : pinsNew)
            scene.addPin(node, pin);

        for (AdsPin pin: pinEdges.keySet()) {
            List<AdsEdge> edges = pinEdges.get(pin);
            for (AdsEdge edge: edges) {
                if (edge.getSource() == null) {
                    edge.setSource(pin);
                    scene.setEdgeSource(edge, pin);
                }
                if (edge.getTarget() == null) {
                    edge.setTarget(pin);
                    scene.setEdgeTarget(edge, pin);
                }
            }
        }

        repaint();
        return true;
    }

    protected List<Action> getActionList() {
        Action cutAction = (Action)SystemAction.get(CutAction.class);
        Action copyAction = (Action)SystemAction.get(CopyAction.class);
        Action deleteAction = (Action)SystemAction.get(DeleteAction.class);
        return Arrays.asList(
                new EditAction(),
                null,
                cutAction,
                copyAction,
                null,
                deleteAction
                );
    }

    protected class EditAction extends AbstractAction {

        public EditAction() {
            AdsBaseObject node = getNode();
            putValue(Action.NAME, NbBundle.getMessage(GraphSceneImpl.class, node.isReadOnly() ? "View" : "Edit"));
            putValue(Action.SMALL_ICON, RadixWareIcons.EDIT.EDIT.getIcon());
            putValue(Action.SHORT_DESCRIPTION, NbBundle.getMessage(GraphSceneImpl.class, "Edit"));
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            if (notifyEdited()) {
                if (sync())
                    notifyModified();
            }
        }
    }

    protected class SelectInExplorerAction extends AbstractAction {

        public SelectInExplorerAction() {
            putValue(Action.NAME, NbBundle.getMessage(GraphSceneImpl.class, "SelectInExplorer"));
            putValue(Action.SMALL_ICON, RadixWareIcons.TREE.SELECT_IN_TREE.getIcon());
            putValue(Action.SHORT_DESCRIPTION, NbBundle.getMessage(GraphSceneImpl.class, "SelectInExplorer"));
        }

        protected RadixObject getObject() {
            return getNode();
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            DialogUtils.goToObject(getObject());
        }
    }


    protected class ProfileAction extends AbstractAction {

        public ProfileAction() {
            putValue(Action.NAME, NbBundle.getMessage(GraphSceneImpl.class, "ProfilingSettings"));
            putValue(Action.SHORT_DESCRIPTION, NbBundle.getMessage(GraphSceneImpl.class, "ProfilingSettings"));
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            AdsBaseObject node = getNode();
            ProfilePanel panel = new ProfilePanel();
            panel.open(node);
            ModalDisplayer displayer = new ModalDisplayer(panel, "Profiling settings for " + node.getAdsDefinition().getQualifiedName());
            displayer.getDialogDescriptor().setValid(panel.isOk());
            if (displayer.showModal()) {
                panel.apply();
            }
        }
    }

    protected class WidgetPopupMenu implements PopupMenuProvider, ActionListener {
    
        @Override
        public JPopupMenu getPopupMenu (Widget widget, Point localLocation) {
            JPopupMenu popupMenu = null;
            List<Action> actionList = getActionList();
            if (actionList != null && actionList.size() > 0) {
                popupMenu = new JPopupMenu();
                for (Action action : actionList) {
                    if (action == null)
                        popupMenu.addSeparator();
                    else
                        addMenuItem(popupMenu, action);
                }
            }
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
