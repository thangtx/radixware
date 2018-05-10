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
package org.radixware.kernel.designer.ads.editors.clazz.forms;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.ConnectorState;
import org.netbeans.api.visual.graph.GraphScene;
import org.netbeans.api.visual.model.ObjectSceneEvent;
import org.netbeans.api.visual.model.ObjectSceneEventType;
import org.netbeans.api.visual.model.ObjectSceneListener;
import org.netbeans.api.visual.model.ObjectState;
import org.netbeans.api.visual.widget.EventProcessingType;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Widget;
import org.openide.windows.TopComponent;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProviderFactory;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil.IVisitorUI;
import org.radixware.kernel.common.defs.ads.ui.*;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.*;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.Item;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.Palette;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.CustomItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.widget.BaseWidget;
import org.radixware.kernel.designer.ads.editors.clazz.forms.widget.PaginalWidget;

public class GraphSceneImpl extends GraphScene<RadixObject, Object> implements ObjectSceneListener {

    private final class LocalAcceptProvider implements WidgetAcceptAction.WidgetAcceptProvider {

        private DragDropLocator locator = null;
        private BaseWidget dropViewWidget;

        @Override
        public ConnectorState isAcceptable(Widget scene, Point scenePoint, Transferable transferable) {

            final Item item = getItem(transferable);

            if (item == null) {
                Logger.getLogger(LocalAcceptProvider.class.getName()).log(Level.INFO, "Item for widget not found");
                return ConnectorState.REJECT_AND_STOP;
            }

            dropView(scenePoint, item);

            if (dropViewWidget == null) {
                Logger.getLogger(LocalAcceptProvider.class.getName()).log(Level.INFO, "Can't create widget");
                return ConnectorState.REJECT_AND_STOP;
            }

            final BaseWidget container = findSuitableContainer(scenePoint, dropViewWidget);
            if (container == null) {
                return ConnectorState.REJECT;
            }
            if (AdsUIUtil.isReadOnlyNode(uiDef, container.getNode())) {
                return ConnectorState.REJECT_AND_STOP;
            }

            if (!dropViewWidget.isSelfContained()) {
                if (locator == null) {
                    locator = new DragDropLocator(interLayer);
                    locator.show();
                }
                container.locate(locator, container.convertSceneToLocal(scenePoint));
            }

            return ConnectorState.ACCEPT;
        }

        @Override
        public void accept(Widget scene, Point scenePoint, Transferable transferable) {

            final Rectangle geometry = dropViewWidget.getGeometry();
            final RadixObject node = dropViewWidget.getNode();
            final BaseWidget owner = findSuitableContainer(scenePoint, dropViewWidget);

            clear();

            if (owner != null) {
                final Point localPoint = owner.convertSceneToLocal(scenePoint);
                final Point offset = owner.offsetPoint();
                localPoint.x += offset.x;
                localPoint.y += offset.y;

                final BaseWidget source = (BaseWidget) GraphSceneImpl.this.addNode(node);
                owner.add(source, owner.convertSceneToLocal(scenePoint));

                source.setGeometry(geometry);
                source.setPreferredLocation(getCenter(scenePoint, source.getGeometry()));
                source.saveGeometry();

                validate();
                revalidate();
                repaint();

//                setSelectedObjects(Collections.singleton(node));
                Component component = view.getParent();
                while (component != null) {
                    if (component instanceof TopComponent) {
                        ((TopComponent) component).requestActive();
                        break;
                    }
                    component = component.getParent();
                }
                setSelectedObjects(Collections.singleton(node));
            }
        }

        @Override
        public void cancel(Widget widget) {
            clear();
        }

        private Point getCenter(Point p, Rectangle rect) {
            p = toGrid(p);

            return new Point(p.x - rect.width / 2, p.y - rect.height / 2);
        }

        private boolean same(Item item, RadixObject node) {
            return item.getClazz().equals(AdsUIUtil.getUiClassName(node));
        }

        private Point toGrid(Point point) {
            Point p = new Point(point);
            if (isSnaped()) {
                p.x = Math.round(1f * point.x / SNAP_SIZE) * SNAP_SIZE;
                p.y = Math.round(1f * point.y / SNAP_SIZE) * SNAP_SIZE;
            }
            return p;
        }

        private Item getItem(Transferable transferable) {
            try {
                final Object transferData = transferable.getTransferData(Palette.MY_DATA_FLAVOR);
                if (transferData instanceof Item) {
                    return (Item) transferable.getTransferData(Palette.MY_DATA_FLAVOR);
                }
            } catch (UnsupportedFlavorException | IOException ex) {
                Logger.getLogger(GraphSceneImpl.class.getName()).log(Level.WARNING, null, ex);
            }
            Logger.getLogger(GraphSceneImpl.class.getName()).log(Level.WARNING, "Empty transfer data");
            return null;
        }

        private void dropView(Point point, Item item) {
            if (dropViewWidget == null || !same(item, dropViewWidget.getNode())) {

                final RadixObject dvwnode = item.createObjectUI(getUI());

                if (dvwnode instanceof AdsUIItemDef) {
                    ((AdsUIItemDef) dvwnode).setContainer(uiDef);
                }

                BaseWidget source = BaseWidget.Factory.newInstance(GraphSceneImpl.this, dvwnode);
                dropViewWidget = source;

                Rectangle geometry = getGeometry(point, dvwnode);
                dropViewWidget.setGeometry(geometry);
                if (dvwnode instanceof AdsUIItemDef) {
                    Rectangle rect = new Rectangle(geometry);
                    rect.y += rect.height / 2;
                    AdsUIUtil.setUiProperty(dvwnode, new AdsUIProperty.RectProperty("geometry", rect));
                }

                interLayer.addChild(dropViewWidget);
            } else {
                dropViewWidget.setPreferredLocation(getCenter(point, dropViewWidget.getGeometry()));
            }
            validate();
        }

        private Rectangle getGeometry(Point point, RadixObject node) {
            Point p = toGrid(point);

            Dimension sz = LayoutUtil.getHintSize(node);
            Rectangle r = new Rectangle(p.x, p.y, sz.width, sz.height);
            if (node instanceof AdsLayout) {
                r.width = 120;
                r.height = 80;
            }

            if (AdsUIUtil.isCustomWidget(node)) {
                AdsUIItemDef widget = CustomItem.getWidgetByRef((AdsUIItemDef) node);
                if (widget != null) {
                    AdsUIProperty.RectProperty geom = (AdsUIProperty.RectProperty) AdsUIUtil.getUiProperty(widget, "geometry");
                    r.width = Math.min(geom.width, 200);
                    r.height = Math.min(geom.height, 200);
                }
            }
            return r;
        }

        private void clear() {
            if (locator != null) {
                locator.hide();
                locator = null;
            }

            if (dropViewWidget != null) {
                dropViewWidget.stopContainerListening();
                dropViewWidget.removeFromParent();
                dropViewWidget = null;
            }
        }
    }

    private LayerWidget backLayer, mainLayer, interLayer;
    private AdsAbstractUIEditor view;
    private AdsAbstractUIDef uiDef;

    public GraphSceneImpl(AdsAbstractUIEditor view) {
        this.view = view;

        backLayer = new LayerWidget(this);
        addChild(backLayer);

        mainLayer = new LayerWidget(this);
        addChild(mainLayer);

        interLayer = new GlassLayer(this);
        addChild(interLayer);

        interLayer.bringToFront();

        getActions().addAction(ActionFactory.createWheelPanAction());
        getActions().addAction(SelectDecorator.createRectangularSelectAction(this, interLayer));

        getActions().addAction(new WidgetAcceptAction(new LocalAcceptProvider()));

        getActions().addAction(ActionFactory.createZoomAction());
        getActions().addAction(ActionFactory.createPanAction());

        setKeyEventProcessingType(EventProcessingType.FOCUSED_WIDGET_AND_ITS_PARENTS);
        getActions().addAction(ActionFactory.createCycleObjectSceneFocusAction());

        addObjectSceneListener(this, ObjectSceneEventType.OBJECT_SELECTION_CHANGED);
    }

    public LayerWidget getBackLayer() {
        return backLayer;
    }

    public LayerWidget getMainLayer() {
        return mainLayer;
    }

    public LayerWidget getInterLayer() {
        return interLayer;
    }
    final public static int SNAP_SIZE = 10;
    private boolean snaped = false;

    public void setSnaped(boolean snaped) {
        this.snaped = snaped;
    }

    public boolean isSnaped() {
        return snaped;
    }

    public void setUI(final AdsAbstractUIDef uiDef) {
        this.uiDef = uiDef;
        assert uiDef != null;

        // change string to multistring
        final AdsUIItemDef widget = uiDef.getWidget();
        widget.visit(new IVisitor() {

            @Override
            public void accept(RadixObject node) {
                UiProperties props = null;
                if (node instanceof AdsItemWidgetDef.Row) {
                    props = ((AdsItemWidgetDef.Row) node).getProperties();
                }
                if (node instanceof AdsItemWidgetDef.Column) {
                    props = ((AdsItemWidgetDef.Column) node).getProperties();
                }
                if (node instanceof AdsItemWidgetDef.WidgetItem) {
                    props = ((AdsItemWidgetDef.WidgetItem) node).getProperties();
                }

                if (props == null) {
                    return;
                }
                for (int i = 0; i < props.size(); i++) {
                    AdsUIProperty p = props.get(i);
                    if ("text".equals(p.getName()) && p instanceof AdsUIProperty.StringProperty) {
                        AdsUIProperty.LocalizedStringRefProperty sp = new AdsUIProperty.LocalizedStringRefProperty(p.getName());
                        String v = ((AdsUIProperty.StringProperty) p).value;
                        if (v != null) {
                            AdsMultilingualStringDef sDef = AdsMultilingualStringDef.Factory.newInstance();
                            uiDef.findLocalizingBundle().getStrings().getLocal().add(sDef);
                            sDef.setValue(EIsoLanguage.RUSSIAN, v);
                            sDef.setValue(EIsoLanguage.ENGLISH, v);
                            sp = new AdsUIProperty.LocalizedStringRefProperty(p.getName(), sDef);
                        }
                        props.remove(i);
                        props.add(i, sp);
                    }
                }
            }
        }, VisitorProviderFactory.createDefaultVisitorProvider());

        AdsUIUtil.visitUI(widget, new IVisitorUI() {

            @Override
            public void visit(RadixObject node, boolean active) {
                BaseWidget wg = (BaseWidget) addNode(node);
                wg.restoreGeometry();
                wg.setVisible(active);
            }
        }, true);

        revalidate();
        repaint();
    }

    public AdsAbstractUIDef getUI() {
        return uiDef;
    }

    public AdsAbstractUIEditor getFormView() {
        return view;
    }

    public void focus(final RadixObject o) {

        select(o);

        RadixObject object = o;
        while (object != null) {
            if (isNode(object)) {
                break;
            }
            object = object.getContainer();
        }

        if (object == null) {
            return;
        }

        setSelectedObjects(Collections.singleton(object));
        setFocusedObject(object);

        Widget wg = findWidget(object);
        assert wg != null;

        Rectangle bounds = wg.getBounds();
        if (bounds != null) {
            getView().scrollRectToVisible(bounds);
        }
    }

    private void select(RadixObject object) {
        if (object instanceof AdsUIItemDef) {
            final AdsUIItemDef item = (AdsUIItemDef) object;

            if (item.getOwnerDef() instanceof AdsUIItemDef) {

                final AdsUIItemDef owner = (AdsUIItemDef) item.getOwnerDef();
                final WidgetAdapter ownerAdapter = new WidgetAdapter(owner);

                switch (ownerAdapter.getClassName()) {
                    case AdsMetaInfo.STACKED_WIDGET_CLASS:
                    case AdsMetaInfo.TAB_WIDGET_CLASS:
                    case AdsMetaInfo.RWT_TAB_SET:
                        int index = 0;
                        for (final AdsUIItemDef i : ownerAdapter.getWidgets()) {
                            if (i == item) {
                                break;
                            }
                            ++index;
                        }
                        if (index < ownerAdapter.getWidgets().size()) {
                            final Widget widget = findWidget(ownerAdapter.getWidget());
                            if (widget instanceof PaginalWidget) {
                                ((PaginalWidget) widget).setCurrentIndex(index);
                            }
                        }
                        break;
                }

                select(owner);
            }
        }
    }

    @Override
    protected Widget attachNodeWidget(RadixObject node) {
        BaseWidget widget = BaseWidget.Factory.newInstance(this, node);
        assert widget != null : "widget can't be null";
        mainLayer.addChild(widget);
        return widget;
    }

    @Override
    protected void detachNodeWidget(RadixObject node, Widget widget) {
        super.detachNodeWidget(node, widget);
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
        AdsUIItemDef widget = uiDef.getWidget();
        if (widget == null) {
            return;
        }
        Set<Object> selectedObjects = new HashSet<>((Set<Object>) getSelectedObjects());
        if (selectedObjects.isEmpty()) {
            selectedObjects.add(widget);
        }
        selectionChanged(null, null, selectedObjects);
    }

    @Override
    public void selectionChanged(ObjectSceneEvent event, Set<Object> previousSelection, Set<Object> newSelection) {
        AdsUIItemDef widget = uiDef.getWidget();
        if (widget == null) {
            return;
        }
        final Set<RadixObject> selectedChildren = new HashSet<>();
        final List<RadixObject> selectedObjects = new ArrayList<>();
        for (final Object object : newSelection) {
            if (isNode(object)) {
                selectedObjects.add((RadixObject) object);
                if (!object.equals(widget)) {
                    AdsUIUtil.visitUI((RadixObject) object, new IVisitorUI() {

                        @Override
                        public void visit(RadixObject node, boolean active) {
                            if (!node.equals(object)) {
                                selectedChildren.add(node);
                            }
                        }
                    }, true);
                }
            }
        }

        boolean empty = selectedObjects.isEmpty();
        if (empty) {
            selectedObjects.add(widget);
        } else {
            for (RadixObject object : selectedChildren) {
                if (selectedObjects.contains(object)) {
                    selectedObjects.remove(object);
                }
            }
            if (selectedObjects.size() > 1) {
                if (selectedObjects.contains(widget)) {
                    selectedObjects.remove(widget);
                }
            }
        }

        view.notifySelectionChanged(selectedObjects);
        view.updateToolBar();
        getView().requestFocusInWindow();
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
    protected Widget attachEdgeWidget(Object edge) {
        return null;
    }

    @Override
    protected void attachEdgeSourceAnchor(Object edge, RadixObject oldSourceNode, RadixObject sourceNode) {
    }

    @Override
    protected void attachEdgeTargetAnchor(Object edge, RadixObject oldTargetNode, RadixObject targetNode) {
    }

    public BaseWidget getContainer(Point scenePoint, BaseWidget excludeWidget) {
        List<Widget> widgets = mainLayer.getChildren();
        for (int i = widgets.size() - 1; i >= 0; i--) {
            BaseWidget widget = (BaseWidget) widgets.get(i);
            if (widget.isVisible() && widget.isContainer() && widget.getGeometry().contains(scenePoint)) {
                if (excludeWidget == null) {
                    return widget;
                }
                RadixObject excludeNode = (RadixObject) findObject(excludeWidget);
                RadixObject node = (RadixObject) findObject(widget);
                while (!node.equals(excludeNode)) {
                    if (node.equals(uiDef)) {
                        return widget;
                    }
                    node = node.getContainer();
                }
            }
        }
        BaseWidget rootWidget = getContainerRoot();
        return rootWidget.equals(excludeWidget) ? null : rootWidget;
    }

    public BaseWidget getContainer(Point scenePoint) {
        return getContainer(scenePoint, null);
    }

    public BaseWidget getContainerRoot() {
        AdsUIItemDef widget = uiDef.getWidget();
        if (widget == null) {
            return null;
        }
        return (BaseWidget) findWidget(widget);
    }

    public BaseWidget getContainerSelected() {
        Iterator it = getSelectedObjects().iterator();
        if (it.hasNext()) {
            BaseWidget widget = (BaseWidget) findWidget(it.next());
            boolean isContainer = widget.isContainer() && !widget.isLayout();
            return it.hasNext() || !isContainer ? null : widget;
        }
        return null;
    }

    public BaseWidget findSuitableContainer(Point scenePoint, BaseWidget widget) {
        if (widget == null || scenePoint == null) {
            return null;
        }

        if (widget.isSelfContained()) {
            return getContainerRoot();
        }

        final String clazz = AdsUIUtil.getUiClassName(widget.getNode());
        final List<Widget> widgets = mainLayer.getChildren();
        for (int i = widgets.size() - 1; i >= 0; i--) {
            final BaseWidget wdg = (BaseWidget) widgets.get(i);
            final Point localPoint = wdg.convertSceneToLocal(scenePoint);
            if (wdg.isVisible() && wdg.isContainer(clazz, localPoint)) {
                if (widget == null) {
                    return wdg;
                }
                RadixObject excludeNode = (RadixObject) findObject(widget);
                RadixObject node = (RadixObject) findObject(wdg);
                // смотрим чтоб не вставить компонент в дочерний контейнер запрещенного контейнера
                while (node != null && !node.equals(excludeNode)) {
                    if (node.equals(uiDef)) {
                        return wdg;
                    }
                    node = node.getContainer();
                }
            }
        }
        final BaseWidget rootWidget = getContainerRoot();
        if (!rootWidget.equals(widget) && rootWidget.isContainer(clazz, rootWidget.convertSceneToLocal(scenePoint))) {
            return rootWidget;
        }
        return null;
    }
}
