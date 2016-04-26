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

package org.radixware.kernel.designer.ads.editors.clazz.creation;

import java.awt.BorderLayout;
import java.awt.Image;
import java.beans.PropertyVetoException;
import java.text.Collator;
import java.util.*;
import javax.swing.Action;
import javax.swing.ActionMap;
import org.netbeans.swing.outline.DefaultOutlineModel;
import org.netbeans.swing.outline.Outline;
import org.netbeans.swing.outline.OutlineModel;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.view.CheckableNode;
import org.openide.explorer.view.OutlineView;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.Definitions;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IFilter;
import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsInnateColumnPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsInnateRefPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.common.AdsUtils;
import org.radixware.kernel.common.defs.dds.*;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.ads.editors.clazz.creation.ColumnBasedPropertiesLib.PropCellEditor;
import org.radixware.kernel.designer.ads.editors.clazz.creation.ColumnBasedPropertiesLib.PropRowModel;
import org.radixware.kernel.designer.ads.editors.clazz.creation.ColumnBasedPropertiesLib.PropTreeModel;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;


public class ColumnBasedPropertiesPanel extends javax.swing.JPanel implements Lookup.Provider, ExplorerManager.Provider {

    private static final String COLUMNS_NODE = "Columns";
    private static final String REFS_NODE = "Outgoing references";
    private static Comparator<PropInfo> PROP_COMPARATOR = new Comparator<PropInfo>() {
        @Override
        public int compare(PropInfo o1, PropInfo o2) {
            int result = Collator.getInstance().compare(o1.ddsdef.getName(), o2.ddsdef.getName());
            if (result == 0) {
                result = o1.ddsdef.getId().toString().compareTo(o2.ddsdef.getId().toString());
            }
            return result;
        }
    };

    static class PropInfo {

        DdsDefinition ddsdef;
        AdsPropertyDef adsproperty;
        boolean isSelected = false;
        boolean isNewTemporaryProperty = false;

        PropInfo(DdsDefinition ddsdef, AdsPropertyDef property) {
            this.ddsdef = ddsdef;
            this.adsproperty = property;
            this.isSelected = property != null;
        }
    }

    static class PropNode extends AbstractNode implements CheckableNode {

        PropInfo info;
        private AdsEntityObjectClassDef context;
        private Outline viewTable;
        private ExplorerManager ownerManager;

        PropNode(ExplorerManager manager, AdsEntityObjectClassDef context, PropInfo info, Children children, Outline viewTable) {
            super(children);
            this.info = info;
            this.context = context;
            this.viewTable = viewTable;
            this.ownerManager = manager;
        }

        @Override
        public void setSelected(Boolean selected) {
            Node[] selection = ownerManager.getSelectedNodes();

            info.isSelected = setNodeSelected(this, selected);

            if (selection.length > 1) {
                for (final Node node : selection) {
                    if (node != this && node instanceof PropNode) {
                        final PropNode propNode = ((PropNode) node);
                        propNode.info.isSelected = setNodeSelected(propNode, selected);
                    }
                }
            }

            viewTable.repaint();
        }

        @Override
        public Boolean isSelected() {
            return info.isSelected;
        }

        @Override
        public boolean isCheckEnabled() {
            if (info.adsproperty != null) {
                return info.adsproperty.getOwnerClass().equals(context);
            }
            return true;
        }

        @Override
        public Action[] getActions(boolean context) {
            return new Action[0];
        }

        @Override
        public boolean isCheckable() {
            return true;
        }

        @Override
        public Image getIcon(int type) {
            return info.ddsdef.getIcon().getImage(16, 16);
        }

        @Override
        public Image getOpenedIcon(int type) {
            return this.getIcon(type);
        }

        @Override
        public String getDisplayName() {
            if (info.adsproperty != null
                    && !info.adsproperty.getOwnerClass().equals(context)) {
                return "<html><body><font color=\"gray\">" + info.ddsdef.getName() + "</font></body></html>";
            }
            return info.ddsdef.getName();
        }
    }

    private static boolean setNodeSelected(PropNode node, boolean selected) {
        final PropInfo info = node.info;

        if (selected) {
            if (info.adsproperty == null) {
                if (info.ddsdef instanceof DdsColumnDef) {
                    info.adsproperty = AdsInnateColumnPropertyDef.Factory.newTemporaryInstance(node.context.getPropertyGroup());
                    ((AdsInnateColumnPropertyDef) info.adsproperty).getColumnInfo().setColumnId(info.ddsdef.getId());
                    info.adsproperty.setName(info.ddsdef.getName());
                } else if (info.ddsdef instanceof DdsReferenceDef) {
                    final DdsTableDef parentTable = ((DdsReferenceDef) info.ddsdef).getParentTable(node.context);

                    final AdsEntityClassDef entityClass = AdsUtils.findEntityClass(parentTable);
                    if (entityClass == null) {
                        if (!DialogUtils.messageConfirmation("Entity class for selected relationship not found, create a property?")) {
                            return false;
                        }
                    } else {
                        node.context.getModule().getDependences().add(entityClass.getModule());
                    }

                    info.adsproperty = AdsInnateRefPropertyDef.Factory.newTemporaryInstance(node.context.getPropertyGroup());
                    ((AdsInnateRefPropertyDef) info.adsproperty).associateWith((DdsReferenceDef) info.ddsdef);

                    if (parentTable != null) {
                        String first = parentTable.getName().substring(0, 1);
                        first = first.toLowerCase();
                        info.adsproperty.setName(first + parentTable.getName().substring(1));
                    } else {
                        info.adsproperty.setName("newReferenceProperty");
                    }
                }
                toCreate.add(info.adsproperty);
                info.isNewTemporaryProperty = true;
            } else {
                if (!info.isNewTemporaryProperty) {
                    toRemove.remove(info.adsproperty);
                }
            }
        } else if (info.adsproperty != null) {
            if (info.isNewTemporaryProperty) {
                toCreate.remove(info.adsproperty);
                info.adsproperty.delete();
                info.adsproperty = null;
                info.isNewTemporaryProperty = false;
            } else {
                toRemove.add(info.adsproperty);
            }
        }

        return selected;
    }

    static class PropNodeChildren extends Children.Array {

        private static Collection<Node> createNodes(ExplorerManager ownerManager, AdsEntityObjectClassDef context, List<PropInfo> infos, Outline viewTable) {
            ArrayList<Node> result = new ArrayList<>();
            Collections.sort(infos, PROP_COMPARATOR);
            for (PropInfo i : infos) {
                result.add(new PropNode(ownerManager, context, i, Children.LEAF, viewTable));
            }
            return result;
        }

        PropNodeChildren(ExplorerManager ownerManager, AdsEntityObjectClassDef context, List<PropInfo> infos, Outline viewTable) {
            super(createNodes(ownerManager, context, infos, viewTable));
        }
    }

    static class RootNodeChildren extends Children.Array {

        private static Collection<Node> createNodes(ExplorerManager ownerManager, AdsEntityObjectClassDef context, Outline viewTable) {
            LinkedList<Node> nodes = new LinkedList<>();
            nodes.add(new InformerNode(ownerManager, context, COLUMNS_NODE, viewTable));
            nodes.add(new InformerNode(ownerManager, context, REFS_NODE, viewTable));
            return nodes;
        }

        RootNodeChildren(ExplorerManager ownerManager, AdsEntityObjectClassDef context, Outline viewTable) {
            super(createNodes(ownerManager, context, viewTable));
        }
    }

    static class InformerNode extends AbstractNode {

        private static List<PropInfo> createNodeList(AdsEntityObjectClassDef context, String label) {
            ArrayList<PropInfo> infos = new ArrayList<>();
            switch (label) {
                case COLUMNS_NODE:
                    ChooseInnateColumnCfg colCfg = new ChooseInnateColumnCfg(context.findTable(context), context);
                    Collection<? extends Definition> availableColumns = colCfg.collectAllowedDefinitions();
                    List<AdsPropertyDef> exColumnProps = context.getProperties().get(EScope.ALL, new InnateColumnPropertyFilter());
                    Set<Definition> addedCols = new HashSet<>();
                    for (AdsPropertyDef excol : exColumnProps) {
                        DdsColumnDef column = ((AdsInnateColumnPropertyDef) excol).getColumnInfo().findColumn();
                        if (column != null) {
                            PropInfo info = new PropInfo(column, excol);
                            infos.add(info);
                            addedCols.add(column);
                        }
                    }
                    for (Definition col : availableColumns) {
                        if (addedCols.contains(col)) {
                            continue;
                        }
                        infos.add(new PropInfo((DdsDefinition) col, null));
                    }
                    break;
                case REFS_NODE:
                    ChooseParentRefCfg refCfg = new ChooseParentRefCfg(context.findTable(context), context);
                    Collection<? extends Definition> availableRefs = refCfg.collectAllowedDefinitions();
                    List<AdsPropertyDef> exRefProps = context.getProperties().get(EScope.ALL, new InnateRefPropertyFilter());
                    for (AdsPropertyDef refcol : exRefProps) {
                        DdsReferenceDef reference = ((AdsInnateRefPropertyDef) refcol).getParentReferenceInfo().findParentReference();
                        PropInfo info = new PropInfo(reference, refcol);
                        infos.add(info);
                    }
                    for (AdsPropertyDef p : exRefProps) {
                        DdsReferenceDef ref = ((AdsInnateRefPropertyDef) p).getParentReferenceInfo().findParentReference();
                        if (ref != null) {
                            if (availableRefs.contains(ref)) {
                                availableRefs.remove(ref);
                            }
                        }
                    }
                    for (Definition ref : availableRefs) {
                        infos.add(new PropInfo((DdsDefinition) ref, null));
                    }
                    break;
            }
            return infos;
        }
        private String label;

        InformerNode(ExplorerManager ownerManager, AdsEntityObjectClassDef context, String label, Outline viewTable) {
            super(new PropNodeChildren(ownerManager, context, createNodeList(context, label), viewTable));
            this.label = label;
        }

        @Override
        public Action[] getActions(boolean context) {
            return new Action[0];
        }

        @Override
        public String getDisplayName() {
            switch (label) {
                case COLUMNS_NODE:
                    return "<html><body><b>" + COLUMNS_NODE + "</b></body></html>";
                case REFS_NODE:
                    return "<html><body><b>" + REFS_NODE + "</b></body></html>";
            }
            return "<Not Defined>";
        }

        @Override
        public Image getIcon(int type) {
            switch (label) {
                case COLUMNS_NODE:
                    return DdsDefinitionIcon.COLUMN.getImage(16, 16);
                case REFS_NODE:
                    return DdsDefinitionIcon.REFERENCE.getImage(16, 16);
            }
            return RadixObjectIcon.UNKNOWN.getImage(16, 16);
        }

        @Override
        public Image getOpenedIcon(int type) {
            return getIcon(type);
        }
    }

    class RootNode extends AbstractNode {

        private DdsTableDef table;
        private Id tableId;

        public RootNode(ExplorerManager ownerManager, AdsEntityObjectClassDef context, Outline viewTable) {
            super(new RootNodeChildren(ownerManager, context, viewTable));
            this.table = context.findTable(context);
            this.tableId = context.getEntityId();
        }

        @Override
        public String getDisplayName() {
            return table != null ? table.getName() : "Dds table not found: " + tableId.toString();
        }

        @Override
        public Action[] getActions(boolean context) {
            return new Action[0];
        }

        @Override
        public Image getIcon(int type) {
            return table != null ? table.getIcon().getImage(16, 16) : RadixObjectIcon.UNKNOWN.getImage(16, 16);
        }

        @Override
        public Image getOpenedIcon(int type) {
            return getIcon(type);
        }
    }
    private ExplorerManager manager = new ExplorerManager();
    private Lookup lookup;
    private OutlineView view;
    private AdsEntityObjectClassDef owner;
    private static List<AdsPropertyDef> toRemove = new ArrayList<>();
    private static List<AdsPropertyDef> toCreate = new ArrayList<>();

    public void apply() {
        Definitions<AdsPropertyDef> props = owner.getProperties().getLocal();
        for (AdsPropertyDef p : toCreate) {
            AdsPropertyDef newProperty = p.getClipboardSupport().copy();
            props.add(newProperty);
        }
        for (AdsPropertyDef p : toRemove) {
            p.delete();
        }

        owner.getModule().getDependences().actualize();
    }
    static private ColumnBasedPropertiesLib.HeaderRenderer headerRenderer = new ColumnBasedPropertiesLib.HeaderRenderer();

    private static class MyView extends OutlineView {

        public MyView() {
            setColumnHeader(null);
            setPropertyColumns("DDS Definition", "ADS Property");
        }
    }

    public ColumnBasedPropertiesPanel() {
        view = new MyView();
        Outline outline = view.getOutline();
        outline.setRootVisible(true);
        outline.setRowSelectionAllowed(true);
        outline.setColumnHidingAllowed(false);
        outline.setModel(DefaultOutlineModel.createOutlineModel(new PropTreeModel(null), new PropRowModel(), false, "DDS Definition"));

        outline.getColumnModel().getColumn(1).setCellEditor(new PropCellEditor(new javax.swing.JTextField()));
        outline.getColumnModel().getColumn(1).setHeaderRenderer(headerRenderer);
        outline.getColumnModel().getColumn(1).setCellRenderer(new ColumnBasedPropertiesLib.PropColumnRenderer(owner));
        setLayout(new BorderLayout());
        add(view, BorderLayout.CENTER);

        lookup = ExplorerUtils.createLookup(manager, new ActionMap());
    }

    @Override
    public Lookup getLookup() {
        return lookup;
    }

    public void open(AdsEntityObjectClassDef classContext) {
        this.owner = classContext;
        toCreate.clear();
        toRemove.clear();

        Node root = new RootNode(manager, classContext, view.getOutline());
        OutlineModel model = DefaultOutlineModel.createOutlineModel(new PropTreeModel(root), new PropRowModel(), false, "DDS Definition");

        view.getOutline().setModel(model);
        view.getOutline().getColumnModel().getColumn(1).setCellEditor(new PropCellEditor(new javax.swing.JTextField()));
        view.getOutline().getColumnModel().getColumn(1).setHeaderRenderer(headerRenderer);
        view.getOutline().getColumnModel().getColumn(1).setCellRenderer(new ColumnBasedPropertiesLib.PropColumnRenderer(classContext));
        manager.setRootContext(root);

        Node[] nodes = root.getChildren().getNodes();
        for (Node n : nodes) {
            view.expandNode(n);
        }
        try {
            manager.setSelectedNodes(new Node[]{root});
        } catch (PropertyVetoException ex) {
            //do nothing
        }
    }

    @Override
    public ExplorerManager getExplorerManager() {
        return manager;
    }

    //-----------------------------------------------------------------------------------
    private static class ChooseInnateColumnCfg extends ChooseDefinitionCfg {

        public ChooseInnateColumnCfg(final DdsTableDef table, final AdsEntityObjectClassDef context) {
            super(table.getColumns().get(EScope.ALL, new IFilter<DdsColumnDef>() {
                @Override
                public boolean isTarget(DdsColumnDef object) {
                    return context.getProperties().findById(object.getId(), EScope.ALL).isEmpty();
                }
            }));
        }
    }

    private static class ChooseParentRefCfg extends ChooseDefinitionCfg {

        public ChooseParentRefCfg(final DdsTableDef table, final AdsEntityObjectClassDef context) {
            super(table.collectOutgoingReferences(new IFilter<DdsReferenceDef>() {
                @Override
                public boolean isTarget(DdsReferenceDef object) {
                    return object.getType() != DdsReferenceDef.EType.MASTER_DETAIL && isVisibleReference(object, context);
                }
            }));
        }
    }

    private static boolean isVisibleReference(DdsReferenceDef ref, final AdsEntityObjectClassDef context) {
        Layer thisLayer = context.getModule().getSegment().getLayer();
        Layer refLayer = ref.getModule().getSegment().getLayer();

        return thisLayer == refLayer || thisLayer.isHigherThan(refLayer);
    }

    private static class InnateColumnPropertyFilter implements IFilter<AdsPropertyDef> {

        @Override
        public boolean isTarget(AdsPropertyDef radixObject) {
            return radixObject instanceof AdsInnateColumnPropertyDef;
        }
    }

    private static class InnateRefPropertyFilter implements IFilter<AdsPropertyDef> {

        @Override
        public boolean isTarget(AdsPropertyDef radixObject) {
            return radixObject instanceof AdsInnateRefPropertyDef;
        }
    }
}
