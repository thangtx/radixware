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

import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;
import javax.swing.SwingUtilities;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.ConnectProvider;
import org.netbeans.api.visual.action.ConnectorState;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.anchor.Anchor;
import org.netbeans.api.visual.graph.GraphScene;
import org.netbeans.api.visual.router.Router;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.EventProcessingType;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;

import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.RadixObjects.ContainerChangedEvent;
import org.radixware.kernel.common.defs.RadixObjects.EChangeType;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsDefinition;
import org.radixware.kernel.common.defs.dds.DdsDefinitions;
import org.radixware.kernel.common.defs.dds.DdsExtTableDef;
import org.radixware.kernel.common.defs.dds.DdsModelDef;
import org.radixware.kernel.common.defs.dds.DdsPrimaryKeyDef;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.dds.IPlacementSupport;
import org.radixware.kernel.common.defs.dds.providers.DdsVisitorProvider;
import org.radixware.kernel.common.defs.dds.utils.DbNameUtils;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.dds.editors.diagram.widgets.DdsDefinitionWidgetFactory;
import org.radixware.kernel.designer.dds.editors.diagram.widgets.DdsEditorsManager;
import org.radixware.kernel.designer.dds.editors.diagram.widgets.IDdsDefinitionWidget;

/**
 * DDS Model Diagram
 */
public class DdsModelDiagram extends GraphScene<DdsDefinition, DdsReferenceDef> implements RadixObjects.ContainerChangesListener {

    private final DdsModelDef model;
    //private static final Color MAGENTA = new Color(0, 128, 128);
    private final LayerWidget backLayer = new LayerWidget(this);
    private final LayerWidget mainLayer = new LayerWidget(this);
    private final LayerWidget connLayer = new LayerWidget(this);
    private final LayerWidget interLayer = new LayerWidget(this);
//    private final Router refRouter = RouterFactory.createOrthogonalSearchRouter(mainLayer, connLayer);
    private final Router refRouter = new ReferenceRouter();
    private final WidgetAction connectAction = ActionFactory.createExtendedConnectAction(null, interLayer, new DdsConnectProvider(), MouseEvent.ALT_MASK);
    //private final WidgetAction alignAction = ActionFactory.createAlignWithMoveAction(mainLayer, interLayer, null);

    private class NodeVisitor implements IVisitor {

        @Override
        public void accept(RadixObject obj) {
            DdsModelDiagram.this.addNode((DdsDefinition) obj);
        }
    }

    private static class DiagramNodesVisitorProvider extends DdsVisitorProvider {

        @Override
        public boolean isTarget(RadixObject object) {
            return (object instanceof IPlacementSupport);
        }
    }

    private Font defaultFont;

    public DdsModelDiagram(DdsModelDef model, Font defaultFont) {
        super();

        setFont(defaultFont);
        this.defaultFont = defaultFont;

        this.model = model;

        addChild(backLayer);
        addChild(mainLayer);
        addChild(connLayer);
        addChild(interLayer);

        NodeVisitor nodeVisitor = new NodeVisitor();
        model.visit(nodeVisitor, new DiagramNodesVisitorProvider());

        for (DdsReferenceDef ref : model.getReferences()) {
            this.addEdge(ref);
        }

        getActions().addAction(ActionFactory.createMouseCenteredZoomAction(1.2));
        getActions().addAction(ActionFactory.createWheelPanAction());
        getActions().addAction(ActionFactory.createRectangularSelectAction(this, backLayer));

        DdsModelDiagramPopupMenu popupMenu = new DdsModelDiagramPopupMenu(this, model);
        getActions().addAction(ActionFactory.createPopupMenuAction(popupMenu));

        for (DdsDefinitions ddsDefinitions : model.getDiagramContainers()) {
            ddsDefinitions.getContainerChangesSupport().addEventListener(this);
        }

        this.setKeyEventProcessingType(EventProcessingType.FOCUSED_WIDGET_AND_ITS_CHILDREN);
    }

    @Override
    public Font getDefaultFont() {
        return defaultFont;
    }


    public WidgetAction getConnectAction() {
        return connectAction;
    }

    public Router getReferencesRouter() {
        return refRouter;
    }

//    public WidgetAction getAlignAction() {
//        return alignAction;
//    }
    private class DdsConnectProvider implements ConnectProvider {

        private DdsReferenceDef newReference(DdsTableDef childTable, DdsTableDef parentTable) {
            final DdsReferenceDef reference = DdsReferenceDef.Factory.newInstance();

            reference.setChildTableId(childTable.getId());
            reference.setParentTableId(parentTable.getId());

            final DdsPrimaryKeyDef pk = parentTable.getPrimaryKey();
            reference.setParentUnuqueConstraintId(pk.getUniqueConstraint().getId());

            for (DdsPrimaryKeyDef.ColumnInfo columnInfo : pk.getColumnsInfo()) {
                final DdsColumnDef parentColumn = columnInfo.findColumn();
                if (parentColumn != null) {
                    reference.getColumnsInfo().add(null, parentColumn);
                }
            }
            if (pk.getColumnsInfo().size() == 1) {
                final DdsColumnDef parentColumn = pk.getColumnsInfo().get(0).findColumn();
                if (parentColumn != null && "id".equals(parentColumn.getName().toLowerCase())) {
                    final String childColumnLowerName = parentTable.getName().toLowerCase() + parentColumn.getName().toLowerCase();
                    for (DdsColumnDef childColumn : childTable.getColumns().get(EScope.ALL)) {
                        if (childColumn.getName().toLowerCase().equals(childColumnLowerName)) {
                            reference.getColumnsInfo().get(0).setChildColumnId(childColumn.getId());
                        }
                    }
                }
            }

            return reference;
        }

        @Override
        public void createConnection(Widget sourceWidget, Widget targetWidget) {
            Object child = DdsModelDiagram.this.findObject(sourceWidget);
            Object parent = DdsModelDiagram.this.findObject(targetWidget);

            DdsTableDef childTable = (child instanceof DdsTableDef ? (DdsTableDef) child : ((DdsExtTableDef) child).findTable());
            DdsTableDef parentTable = (parent instanceof DdsTableDef ? (DdsTableDef) parent : ((DdsExtTableDef) parent).findTable());

            if (childTable == null) {
                DialogUtils.messageError("Child table not found.");
                return;
            }

            if (parentTable == null) {
                DialogUtils.messageError("Parent table not found.");
                return;
            }

            final DdsReferenceDef reference = newReference(childTable, parentTable);

            if (child instanceof DdsExtTableDef) {
                reference.setExtChildTableId(((DdsExtTableDef) child).getId());
            }
            if (parent instanceof DdsExtTableDef) {
                reference.setExtParentTableId(((DdsExtTableDef) parent).getId());
            }

            DdsModelDiagram.this.model.getReferences().add(reference);
            DbNameUtils.updateAutoDbNames(reference);
            if (DdsEditorsManager.open(reference)) {
                update();
            } else {
                reference.delete();
            }
        }

        @Override
        public boolean hasCustomTargetWidgetResolver(Scene scene) {
            return false; // call isTargetWidget instead of resolveTargetWidget
        }

        @Override
        public boolean isSourceWidget(Widget sourceWidget) {
            if (DdsModelDiagram.this.model.isReadOnly()) {
                return false;
            }
            Object sourceObject = DdsModelDiagram.this.findObject(sourceWidget);
            return (sourceObject instanceof DdsTableDef || sourceObject instanceof DdsExtTableDef);
        }

        @Override
        public ConnectorState isTargetWidget(Widget sourceWidget, Widget targetWidget) {
            Object targetObject = DdsModelDiagram.this.findObject(targetWidget);
            if (targetObject instanceof DdsTableDef || targetObject instanceof DdsExtTableDef) {
                return ConnectorState.ACCEPT;
            } else {
                return ConnectorState.REJECT;
            }
        }

        @Override
        public Widget resolveTargetWidget(Scene scene, Point sceneLocation) {
            throw new IllegalStateException("resolveTargetWidget called");
        }
    }

    @Override
    protected Widget attachNodeWidget(DdsDefinition definition) {
        final Widget widget = DdsDefinitionWidgetFactory.newInstance(this, definition);
        mainLayer.addChild(widget);
        return widget;
    }

    public Anchor getAnchor(Widget widget) {
        for (Widget sibling : connLayer.getChildren()) {
            if (sibling instanceof ConnectionWidget) {
                ConnectionWidget connectionWidget = (ConnectionWidget) sibling;
                Anchor targetAnchor = connectionWidget.getTargetAnchor();
                if (targetAnchor != null && targetAnchor.getRelatedWidget() == widget) {
                    return targetAnchor;
                }
                Anchor sourceAnchor = connectionWidget.getSourceAnchor();
                if (sourceAnchor != null && sourceAnchor.getRelatedWidget() == widget) {
                    return sourceAnchor;
                }
            }
        }
        return new MultiAnchor(widget);
    }

    @Override
    protected Widget attachEdgeWidget(DdsReferenceDef ref) {
        Widget connectionWidget = DdsDefinitionWidgetFactory.newInstance(this, ref);
        connLayer.addChild(connectionWidget);
        return connectionWidget;
    }

    @Override
    protected void attachEdgeSourceAnchor(DdsReferenceDef edge, DdsDefinition oldSourceNode, DdsDefinition sourceNode) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void attachEdgeTargetAnchor(DdsReferenceDef edge, DdsDefinition oldTargetNode, DdsDefinition targetNode) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void update() {
        for (Widget widget : this.mainLayer.getChildren()) {
            IDdsDefinitionWidget ddsDefinitionWidget = (IDdsDefinitionWidget) widget;
            ddsDefinitionWidget.update();
        }
        for (Widget widget : this.connLayer.getChildren()) {
            IDdsDefinitionWidget ddsDefinitionWidget = (IDdsDefinitionWidget) widget;
            ddsDefinitionWidget.update();
        }
        this.validate(); // required before repaint, see http://graph.netbeans.org/faq.html
    }

    private void expandSelection(final RadixObject radixObject) {
        final Set<?> selectedObjects = getSelectedObjects();
        final Set<RadixObject> newSelectedObjects = new HashSet<RadixObject>();
        for (Object obj : selectedObjects) {
            newSelectedObjects.add((RadixObject) obj);
        }
        newSelectedObjects.add(radixObject);
        setSelectedObjects(newSelectedObjects);
    }

    private void processEventOnAwtThread(final ContainerChangedEvent event) {
        if (event.changeType == EChangeType.ENLARGE) {
            final RadixObject addedRadixObject = event.object;
            if (event.object instanceof DdsReferenceDef) {
                addEdge((DdsReferenceDef) addedRadixObject);
            } else if (event.object instanceof IPlacementSupport) {
                addNode((DdsDefinition) addedRadixObject);
            }
            this.validate(); // required before repaint, see http://graph.netbeans.org/faq.html
            expandSelection(addedRadixObject);
        } else if (event.changeType == RadixObjects.EChangeType.SHRINK) {
            final RadixObject removedRadixObject = event.object;
            final Widget widget = DdsModelDiagram.this.findWidget(removedRadixObject);
            if (widget != null) {
                if (event.object instanceof DdsReferenceDef) {
                    DdsModelDiagram.this.removeEdge((DdsReferenceDef) removedRadixObject);
                } else {
                    DdsModelDiagram.this.removeNode((DdsDefinition) removedRadixObject);
                }
            }
            this.validate(); // required before repaint, see http://graph.netbeans.org/faq.html
        }
    }

    @Override
    public void onEvent(final ContainerChangedEvent event) {
        if (SwingUtilities.isEventDispatchThread()) {
            processEventOnAwtThread(event);
        } else {
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    processEventOnAwtThread(event);
                }
            });
        }
    }

    public int scaleToPixels(int val) {
        return (int)(val * getScaleFactor());
    }

    /**
     * Scale factor depends on font-size.
     */
    public float getScaleFactor() {
        return getFont().getSize() / 12f;
    }
}
