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

package org.radixware.kernel.designer.ads.editors.clazz.algo.workflow;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

import java.io.IOException;

import org.netbeans.api.visual.graph.GraphPinScene;
import org.netbeans.api.visual.model.ObjectSceneEvent;
import org.netbeans.api.visual.model.ObjectState;
import java.awt.geom.AffineTransform;

import org.netbeans.api.visual.border.Border;
import org.netbeans.api.visual.border.BorderFactory;

import org.netbeans.api.visual.action.AcceptProvider;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.ConnectProvider;
import org.netbeans.api.visual.action.ReconnectProvider;
import org.netbeans.api.visual.action.ConnectorState;

import org.netbeans.api.visual.action.PopupMenuProvider;

import org.netbeans.api.visual.anchor.AnchorFactory;
import java.awt.image.BufferedImage;

import java.util.*;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.netbeans.api.visual.model.ObjectSceneEventType;
import org.netbeans.api.visual.model.ObjectSceneListener;
import org.netbeans.api.visual.widget.*;
import org.openide.actions.CopyAction;
import org.openide.actions.CutAction;
import org.openide.actions.PasteAction;
import org.openide.actions.DeleteAction;
import org.openide.nodes.Node;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle;
import org.openide.util.actions.SystemAction;
import org.radixware.kernel.common.builder.BuildActionExecutor.EBuildActionType;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObject.EEditState;
import org.radixware.kernel.common.defs.RadixObjects.ContainerChangedEvent;
import org.radixware.kernel.common.defs.RadixObjects.ContainerChangesListener;
import org.radixware.kernel.common.defs.RadixObjects.EChangeType;
import org.radixware.kernel.common.defs.ads.clazz.algo.AdsAlgoClassDef;
import org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.widget.*;
import org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.palette.*;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.*;
import org.radixware.kernel.designer.ads.build.actions.AbstractBuildAction;
import org.radixware.kernel.designer.ads.build.actions.CompileDefinitionAction;
import org.radixware.kernel.designer.ads.editors.clazz.algo.AdsPageEditor;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.general.nodes.NodesManager;


class MyLookFeel extends DefaultLookFeel {

    private static final Color MY_COLOR_SELECTED = new Color(0x447BCD);
    private static final Border MY_BORDER_SELECTED = BorderFactory.createResizeBorder(6, Color.BLACK, false);
    private static final Border MY_BORDER_HOVERED = BorderFactory.createResizeBorder(6, MY_COLOR_SELECTED, false);
    private static final Border MY_BORDER_NORMAL = BorderFactory.createEmptyBorder(6, 6);

    @Override
    public Border getBorder(ObjectState state) {
        if (state.isHovered()) {
            return MY_BORDER_HOVERED;
        }
        if (state.isSelected()) {
            return MY_BORDER_SELECTED;
        }
        if (state.isFocused()) {
            return MY_BORDER_SELECTED;
        }
        return MY_BORDER_NORMAL;
    }
}

public class GraphSceneImpl extends GraphPinScene<AdsBaseObject, AdsEdge, AdsPin> implements ObjectSceneListener, ContainerChangesListener {

    private AdsPage page = null;
    private LayerWidget backLayer, mainLayer, connLayer, interLayer;
    private WidgetAction connAction, reconnAction;
    private AdsPageEditor view;
    private Font defaultFont;

    public GraphSceneImpl(AdsPageEditor view) {
        this.view = view;
        setLookFeel(new MyLookFeel());

        backLayer = new LayerWidget(this);
        addChild(backLayer);

        mainLayer = new LayerWidget(this);
        addChild(mainLayer);

        connLayer = new LayerWidget(this);
        addChild(connLayer);

        interLayer = new LayerWidget(this);
        addChild(interLayer);

        connAction = ActionFactory.createExtendedConnectAction(interLayer, new GraphConnectProvider());
        reconnAction = ActionFactory.createReconnectAction(new GraphReconnectProvider());

        getActions().addAction(ActionFactory.createWheelPanAction());
        getActions().addAction(ActionFactory.createRectangularSelectAction(this, backLayer));
        getActions().addAction(ActionFactory.createAcceptAction(new AcceptProvider() {
            @Override
            public ConnectorState isAcceptable(Widget widget, Point point, Transferable transferable) {
                Item item = null;
                try {
                    item = (Item) transferable.getTransferData(Palette.MY_DATA_FLAVOR);
                } catch (UnsupportedFlavorException | IOException ex) {
                    Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                }
                if (item == null) {
                    return ConnectorState.REJECT_AND_STOP;
                }

                Image dragImage = item.getBigImage();
                JComponent view = getView();
                Graphics2D g2 = (Graphics2D) view.getGraphics();
                Rectangle visRect = view.getVisibleRect();
                view.paintImmediately(visRect.x, visRect.y, visRect.width, visRect.height);

                Point p = convertSceneToView(point);
                g2.drawImage(dragImage,
                        AffineTransform.getTranslateInstance(p.getX() - dragImage.getWidth(null) / 2, p.getY() - dragImage.getHeight(null) / 2),
                        null);

                return ConnectorState.ACCEPT;
            }

            @Override
            public void accept(Widget widget, Point point, Transferable transferable) {
                Item item = null;
                try {
                    item = (Item) transferable.getTransferData(Palette.MY_DATA_FLAVOR);
                } catch (UnsupportedFlavorException | IOException ex) {
                    Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                }
                AdsBaseObject node = ObjectFactory.createNode(item.getKind(), item.getClazz(), convertLocalToScene(point));
                getPage().add(node);
                if (item instanceof AppItem && node instanceof AdsAppObject) {
                    ((AppItem) item).init((AdsAppObject) node);
                    ((BaseWidget) findWidget(node)).sync();
                }
                setSelectedObjects(Collections.singleton(node));
                if (getPage().getModule() != null) {
                    getPage().getModule().getDependences().actualize();
                }
            }
        }));

        getActions().addAction(ActionFactory.createZoomAction());
        getActions().addAction(ActionFactory.createPanAction());

        getActions().addAction(ActionFactory.createPopupMenuAction(new GraphPopupMenu()));
        addObjectSceneListener(this, ObjectSceneEventType.OBJECT_SELECTION_CHANGED);

        defaultFont = view.getFont();
        setFont(defaultFont);

        setZoomFactor(getFont().getSize() / 12.0);
    }

    @Override
    public Font getDefaultFont() {
        return defaultFont;
    }

    public void setPage(AdsPage page) {
        this.page = page;
        assert page != null;

        AdsAlgoClassDef algo = page.getOwnerClass();

        for (AdsBaseObject node : page.getNodes()) {
            WidgetFactory.createWidgetFull(this, node);
        }
        for (AdsEdge edge : page.getEdges()) {
            WidgetFactory.createEdgeFull(this, edge);
        }

        for (AdsBaseObject node : page.getNodes()) {
            BaseWidget widget = (BaseWidget) findWidget(node);
            if (node instanceof AdsAppObject) {
                AppItem item = (AppItem) Item.getItem(node);
                if (item != null) {
                    EEditState oldState = algo.getEditState();
                    item.syncProperties((AdsAppObject) node);
                    EEditState newState = algo.getEditState();
                    if (newState.equals(EEditState.MODIFIED) && !newState.equals(oldState)) {
                        algo.setEditState(oldState);
                    }
                }
            }
            widget.sync();
        }

        page.addEventListener(this);
        validate();
    }

    public void clear() {
        if (page != null) {
            for (AdsEdge edge : page.getEdges()) {
                removeEdge(edge);
            }
            for (AdsBaseObject node : page.getNodes()) {
                removeNode(node);
            }
        }
    }

    public AdsPage getPage() {
        assert page != null;
        return page;
    }

    public LayerWidget getBackLayer() {
        return backLayer;
    }

    public LayerWidget getMainLayer() {
        return mainLayer;
    }

    public LayerWidget getConnLayer() {
        return connLayer;
    }

    public LayerWidget getInterLayer() {
        return interLayer;
    }

    public WidgetAction getConnectAction() {
        return connAction;
    }

    public WidgetAction getReconnectAction() {
        return reconnAction;
    }

    public void Focus(RadixObject o) {
        Focus(o, null);
    }

    public void Focus(RadixObject o, RadixObject context) {
        if (!isNode(o) && !isEdge(o)) {
            return;
        }

        setSelectedObjects(Collections.singleton(o));

        Rectangle bounds = null;
        if (o instanceof AdsBaseObject) {
            bounds = ((AdsBaseObject) o).getBounds();
        }
        if (o instanceof AdsEdge) {
            bounds = ((AdsEdge) o).getBounds();
        }

        if (bounds != null) {
            getView().scrollRectToVisible(bounds);
        }
    }

    @Override
    protected Widget attachNodeWidget(AdsBaseObject node) {
        BaseWidget widget = WidgetFactory.createWidget(this, node);
        mainLayer.addChild(widget);
        return widget;
    }

    @Override
    protected void detachNodeWidget(AdsBaseObject node, Widget widget) {
        super.detachNodeWidget(node, widget);
    }

    @Override
    protected Widget attachEdgeWidget(AdsEdge edge) {
        EdgeWidget widget = WidgetFactory.createEdge(this, edge);
        connLayer.addChild(widget);
        return widget;
    }

    @Override
    protected void detachEdgeWidget(AdsEdge edge, Widget widget) {
        super.detachEdgeWidget(edge, widget);
    }

    @Override
    protected Widget attachPinWidget(AdsBaseObject node, AdsPin pin) {
        BaseWidget widget = (BaseWidget) findWidget(node);
        return widget.attachPin(pin);
    }

    @Override
    protected void attachEdgeSourceAnchor(AdsEdge edge, AdsPin oldSourcePin, AdsPin sourcePin) {
        ConnectionWidget edgeWidget = (ConnectionWidget) findWidget(edge);
        if (sourcePin == null) {
            edgeWidget.setSourceAnchor(null);
            return;
        }
        Widget w = sourcePin != null ? findWidget(sourcePin) : null;
        AdsBaseObject node = getPinNode(sourcePin);
        switch (node.getKind()) {
            case START:
            case FINISH:
            case RETURN:
            case TERMINATE:
            case THROW:
                edgeWidget.setSourceAnchor(AnchorFactory.createCircularAnchor(w, 22));
                break;
            default:
                if (w instanceof LeavePinWidget) {
                    w = ((LeavePinWidget) w).getPinWidget();
                }
                edgeWidget.setSourceAnchor(AnchorFactory.createFreeRectangularAnchor(w, true));
                break;
        }
    }

    @Override
    protected void attachEdgeTargetAnchor(AdsEdge edge, AdsPin oldTargetPin, AdsPin targetPin) {
        ConnectionWidget edgeWidget = (ConnectionWidget) findWidget(edge);
        if (targetPin == null) {
            edgeWidget.setTargetAnchor(null);
            return;
        }
        Widget w = findWidget(targetPin);
        AdsBaseObject node = getPinNode(targetPin);
        switch (node.getKind()) {
            case START:
            case FINISH:
            case RETURN:
            case TERMINATE:
            case THROW:
                edgeWidget.setTargetAnchor(AnchorFactory.createCircularAnchor(w, 22));
                break;
            default:
                if (w instanceof EntryPinWidget) {
                    w = ((EntryPinWidget) w).getPinWidget();
                }
                edgeWidget.setTargetAnchor(AnchorFactory.createFreeRectangularAnchor(w, true));
                break;
        }
    }
    final static private TexturePaint BACKGROUND = createTexture("org/radixware/kernel/designer/ads/editors/clazz/algo/workflow/grid.png");
    final static private TexturePaint BACKGROUND_GRAY = createTexture("org/radixware/kernel/designer/ads/editors/clazz/algo/workflow/gridGray.png");

    ;

    private static TexturePaint createTexture(String resourceId) {
        Image sourceImage = ImageUtilities.loadImage(resourceId);
        int width = sourceImage.getWidth(null);
        int height = sourceImage.getHeight(null);
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        graphics.drawImage(sourceImage, 0, 0, null);
        graphics.dispose();
        return new TexturePaint(image, new Rectangle(0, 0, width, height));
    }

    @Override
    protected void paintBackground() {
        Graphics2D gr = getScene().getGraphics();
        Rectangle bounds = getBounds();
        gr.setPaint(page.isReadOnly() ? BACKGROUND_GRAY : BACKGROUND);
        gr.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    public boolean isSourcePin(AdsPin pin) {
        if (pin == null) {
            return false;
        }
        AdsBaseObject node = getPinNode(pin);
        switch (node.getKind()) {
            case START:
            case EMPTY:
            case MERGE:
            case INCLUDE:
            case PROGRAM:
            case SCOPE:
            case APP:
                if (!node.isSourcePin(pin)) {
                    return false;
                }
                Collection<AdsEdge> eges = findPinEdges(pin, true, false);
                return eges.size() == 0;
        }
        return node.isSourcePin(pin);
    }

    public boolean isTargetPin(AdsPin pin) {
        if (pin == null) {
            return false;
        }
        AdsBaseObject node = getPinNode(pin);
        return node.isTargetPin(pin);
    }

    @Override
    public void objectAdded(ObjectSceneEvent event, Object addedObject) {
    }

    @Override
    public void objectRemoved(ObjectSceneEvent event, Object removedObject) {
    }

    @Override
    public void objectStateChanged(ObjectSceneEvent event, Object changedObject, ObjectState previousState, ObjectState newState) {
    }

    @SuppressWarnings("unchecked")
    public void updateSelection() {
        Set<Object> selectedObjects = new HashSet<Object>((Set<Object>) getSelectedObjects());
        if (selectedObjects.isEmpty()) {
            selectedObjects.add(page);
        }
        selectionChanged(null, null, selectedObjects);
    }

    @Override
    public void selectionChanged(ObjectSceneEvent event, Set<Object> previousSelection, Set<Object> newSelection) {
        List<RadixObject> selectedObjects = new ArrayList<RadixObject>();
        for (Object o : newSelection) {
            if (isEdge(o)) {
                selectedObjects.add((RadixObject) o);
            }
        }
        for (Object o : newSelection) {
            if (isNode(o) && !(o instanceof AdsStartObject)) {
                selectedObjects.add((RadixObject) o);
            }
        }
        if (selectedObjects.isEmpty()) {
            selectedObjects.add(page);
        }

        view.notifySelectionChanged(selectedObjects);
        //TopComponent paletteComponent = WindowManager.getDefault().findTopComponent("CommonPalette");
        //if (paletteComponent != null)
        //    paletteComponent.open();
    }

    @Override
    public void highlightingChanged(ObjectSceneEvent event, Set<Object> previousHighlighting, Set<Object> newHighlighting) {
    }

    @Override
    public void hoverChanged(ObjectSceneEvent event, Object previousHoveredObject, Object newHoveredObject) {
    }

    @Override
    public void focusChanged(ObjectSceneEvent event, Object previousFocusedObject, Object newFocusedObject) {
    }

    @Override
    public void onEvent(ContainerChangedEvent e) {
        if (e.changeType.equals(EChangeType.ENLARGE)) {
            if (e.object instanceof AdsBaseObject) {
                WidgetFactory.createWidgetFull(this, (AdsBaseObject) e.object);
            }
            if (e.object instanceof AdsEdge) {
                WidgetFactory.createEdgeFull(this, (AdsEdge) e.object);
            }
        }
        if (e.changeType.equals(EChangeType.SHRINK)) {
            if (e.object instanceof AdsEdge) {
                removeEdge((AdsEdge) e.object);
            }
            if (e.object instanceof AdsBaseObject) {
                removeNode((AdsBaseObject) e.object);
            }
        }

        if (e.object instanceof AdsReturnObject) {
            ((AdsReturnObject) e.object).syncOwnerPins(getPage());
        }

        validate();
        repaint();
    }

    private class GraphConnectProvider implements ConnectProvider {

        private AdsPin source = null;
        private AdsPin target = null;

        @Override
        public boolean isSourceWidget(Widget sourceWidget) {
            if (page.isReadOnly()) {
                return false;
            }
            Object object = findObject(sourceWidget);
            source = isPin(object) ? (AdsPin) object : null;
            return isSourcePin(source);
        }

        @Override
        public ConnectorState isTargetWidget(Widget sourceWidget, Widget targetWidget) {
            if (page.isReadOnly()) {
                return ConnectorState.REJECT;
            }
            Object object = findObject(targetWidget);
            target = isPin(object) ? (AdsPin) object : null;
            if (isTargetPin(target)) {
                return !source.equals(target) ? ConnectorState.ACCEPT : ConnectorState.REJECT_AND_STOP;
            }
            return object != null ? ConnectorState.REJECT_AND_STOP : ConnectorState.REJECT;
        }

        @Override
        public boolean hasCustomTargetWidgetResolver(Scene scene) {
            return false;
        }

        @Override
        public Widget resolveTargetWidget(Scene scene, Point sceneLocation) {
            return null;
        }

        @Override
        public void createConnection(Widget sourceWidget, Widget targetWidget) {
            AdsEdge edge = ObjectFactory.createEdge(source, target);
            getPage().add(edge);
        }
    };

    private class GraphReconnectProvider implements ReconnectProvider {

        AdsEdge edge;
        AdsPin oldPin;
        AdsPin newPin;

        @Override
        public void reconnectingStarted(ConnectionWidget connWidget, boolean reconnectingSource) {
        }

        @Override
        public void reconnectingFinished(ConnectionWidget connWidget, boolean reconnectingSource) {
        }

        @Override
        public boolean isSourceReconnectable(ConnectionWidget connWidget) {
            if (page.isReadOnly()) {
                return false;
            }
            Object object = findObject(connWidget);
            edge = isEdge(object) ? (AdsEdge) object : null;
            oldPin = edge != null ? getEdgeSource(edge) : null;
            return oldPin != null;
        }

        @Override
        public boolean isTargetReconnectable(ConnectionWidget connWidget) {
            if (page.isReadOnly()) {
                return false;
            }
            Object object = findObject(connWidget);
            edge = isEdge(object) ? (AdsEdge) object : null;
            oldPin = edge != null ? getEdgeTarget(edge) : null;
            return oldPin != null;
        }

        @Override
        public boolean hasCustomReplacementWidgetResolver(Scene scene) {
            return false;
        }

        @Override
        public Widget resolveReplacementWidget(Scene scene, Point sceneLocation) {
            return null;
        }

        @Override
        public void reconnect(ConnectionWidget connWidget, Widget replacementWidget, boolean reconnectingSource) {
            if (replacementWidget == null) {
                getPage().remove(edge);
            } else if (reconnectingSource) {
                edge.setSource(newPin);
                setEdgeSource(edge, newPin);
            } else {
                edge.setTarget(newPin);
                setEdgeTarget(edge, newPin);
            }
        }

        @Override
        public ConnectorState isReplacementWidget(ConnectionWidget connWidget, Widget replacementWidget, boolean reconnectingSource) {
            Object object = findObject(replacementWidget);
            newPin = isPin(object) ? (AdsPin) object : null;
            if (reconnectingSource ? isSourcePin(newPin) : isTargetPin(newPin)) {
                return ConnectorState.ACCEPT;
            }
            return object != null ? ConnectorState.REJECT_AND_STOP : ConnectorState.REJECT;
        }
    }

    private static class CompileAction extends AbstractBuildAction {

        public CompileAction() {
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("F9"));
        }
        private AdsAlgoClassDef algo = null;

        public void activate(AdsAlgoClassDef algo) {
            this.algo = algo;
        }

        ;

        @Override
        public String getName() {
            return NbBundle.getMessage(CompileDefinitionAction.class, "CTL_BuildDefinition");
        }

        @Override
        protected String iconResource() {
            return "org/netbeans/modules/project/ui/resources/compileSingle.png";
        }

        @Override
        protected Class<?>[] cookieClasses() {
            return new Class[]{CompileCookie.class};
        }

        @Override
        protected EBuildActionType getBuildActionType() {
            return EBuildActionType.COMPILE_SINGLE;
        }

        @Override
        protected boolean enable(final Node[] activatedNodes) {
            final Node node = NodesManager.findNode(algo);
            if (node != null) {
                final BuildCookie c = node.getCookie(BuildCookie.class);
                if (c == null || c.getClass() != cookieClasses()[0] || algo.isReadOnly()) {
                    return false;
                }
                return true;
            } else {
                return false;
            }
        }

        @Override
        protected void performAction(final Node[] activatedNodes) {
            final Node node = NodesManager.findNode(algo);
            if (node != null) {
                performAction(node);
                complete();
            }
        }
    }
    final static CompileAction compileAction = new CompileAction();

    private class GraphPopupMenu implements PopupMenuProvider, ActionListener {

        @Override
        public JPopupMenu getPopupMenu(Widget widget, Point localLocation) {
            JPopupMenu popupMenu = new JPopupMenu();

            addMenuItem(popupMenu, compileAction);
            popupMenu.addSeparator();

            Action cutAction = (Action) SystemAction.get(CutAction.class);
            addMenuItem(popupMenu, cutAction);

            Action copyAction = (Action) SystemAction.get(CopyAction.class);
            addMenuItem(popupMenu, copyAction);

            Action pasteAction = (Action) SystemAction.get(PasteAction.class);
            addMenuItem(popupMenu, pasteAction);

            popupMenu.addSeparator();

            Action deleteAction = (Action) SystemAction.get(DeleteAction.class);
            addMenuItem(popupMenu, deleteAction);

            return popupMenu;
        }

        private JMenuItem addMenuItem(JPopupMenu menu, Action action) {
            if (action instanceof CompileAction) {
                ((CompileAction) action).activate(page.getOwnerClass());
            }
            JMenuItem item = DialogUtils.createMenuItem(action);
            menu.add(item);
            return item;
        }

        @Override
        public void actionPerformed(ActionEvent ev) {
        }
    }
}