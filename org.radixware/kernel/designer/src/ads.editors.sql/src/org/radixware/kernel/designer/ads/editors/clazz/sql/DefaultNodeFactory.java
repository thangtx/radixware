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

package org.radixware.kernel.designer.ads.editors.clazz.sql;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.swing.Icon;
import javax.swing.tree.DefaultMutableTreeNode;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IEnumDef;
import org.radixware.kernel.common.defs.IFilter;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsDynamicPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsExpressionPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsFieldPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsFieldRefPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsParameterPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsProcedureClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsSqlClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsStatementClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.ads.common.AdsUtils;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumItemDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumUtils;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsDefinitionIcon;
import org.radixware.kernel.common.defs.dds.DdsFunctionDef;
import org.radixware.kernel.common.defs.dds.DdsIndexDef;
import org.radixware.kernel.common.defs.dds.DdsPlSqlObjectDef;
import org.radixware.kernel.common.defs.dds.DdsPlSqlObjectItemDef;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.dds.utils.ISqlDef;
import org.radixware.kernel.common.defs.dds.utils.ISqlDef.IUsedTable;
import org.radixware.kernel.common.defs.dds.utils.ISqlDef.IUsedTables;
import org.radixware.kernel.common.sqml.providers.SqmlVisitorProviderFactory;
import org.radixware.kernel.designer.ads.common.sql.AdsSqlClassVisitorProviderFactory;
import org.radixware.kernel.designer.ads.editors.clazz.sql.AdsSqlClassTree.Group;
import org.radixware.kernel.designer.ads.editors.clazz.sql.AdsSqlClassTree.Node;
import org.radixware.kernel.designer.ads.editors.clazz.sql.AdsSqlClassTree.NodeInfo;

/**
 * Default implementation of NodeFactory
 */
public class DefaultNodeFactory implements AdsSqlClassTree.NodeFactory {

    private AdsSqlClassTree.NodeInfoFactory infoFactory;

    public DefaultNodeFactory(AdsSqlClassTree.NodeInfoFactory infoFactory) {
        this.infoFactory = infoFactory;
    }

    public DefaultNodeFactory() {
        infoFactory = new DefaultNodeInfoFactory();
    }

    @Override
    public Node create(Object object, AdsSqlClassTree tree) {
        if (object instanceof ISqlDef) {
            return createMainNode((ISqlDef) object);
        } else if (object instanceof AdsParameterPropertyDef) {
            return createParameterNode((AdsParameterPropertyDef) object);
        } else if (object instanceof DdsColumnDef) {
            return createColumnNode((DdsColumnDef) object);
        } else if (object instanceof DdsIndexDef) {
            return createIndexNode((DdsIndexDef) object);
        } else if (object instanceof IUsedTable) {
            return createTableNode((IUsedTable) object, tree.getSqlDef());
        } else if (object instanceof DdsReferenceDef) {
            return createReferenceNode((DdsReferenceDef) object);
        } else if (object instanceof DdsPlSqlObjectDef) {
            return createPackageNode((DdsPlSqlObjectDef) object);
        } else {
            throw new IllegalArgumentException("Cannot create Node for " + object.getClass().getName());
        }

    }

    private Node createMainNode(ISqlDef sqlDef) {
        NodeInfo objectInfo = infoFactory.create(sqlDef);
        Node node = new Node(objectInfo);

        if (sqlDef instanceof AdsSqlClassDef){
            AdsSqlClassDef sqlClassDef = (AdsSqlClassDef) sqlDef;
            node.add(createParametersNode(sqlClassDef));

            if (!(sqlDef instanceof AdsStatementClassDef || sqlDef instanceof AdsProcedureClassDef)) {
                node.add(createFieldsNode(sqlClassDef));
            }

            if (sqlDef instanceof AdsReportClassDef) {
                node.add(createDynamicPropertiesNode(sqlClassDef));
            }
        }
        node.add(createTablesNode(sqlDef));

        return node;
    }

    private Node createDynamicPropertiesNode(AdsSqlClassDef sqlClass) {
        String text = NbBundle.getMessage(AdsSqlClassTree.class, "dynamic-properties-node-text");
        String toolTip = NbBundle.getMessage(AdsSqlClassTree.class, "dynamic-properties-node-tooltip");
        Icon icon = AdsDefinitionIcon.PROPERTY_GROUP.getIcon();
        AdsSqlClassTree.PopupMenuAction[] actions = new AdsSqlClassTree.PopupMenuAction[]{new AdsSqlClassTreeActions.AddDynamicProperty()};
        Group group = new AdsSqlClassTree.Group(AdsSqlClassTree.DYNAMIC_PROPERTIES_NODE, text, icon, toolTip, actions);
        NodeInfo info = infoFactory.create(group);
        Node node = new Node(info);

        for (AdsPropertyDef property : sqlClass.getProperties().getLocal()) {
            if (property instanceof AdsDynamicPropertyDef && !(property instanceof AdsParameterPropertyDef)) {
                node.add(createDynamicPropertyNode((AdsDynamicPropertyDef) property));
            }
        }

        return node;
    }

    private Node createFieldsNode(AdsSqlClassDef sqlClass) {
        String text = NbBundle.getMessage(AdsSqlClassTree.class, "fields-node-text");
        String toolTip = NbBundle.getMessage(AdsSqlClassTree.class, "fields-node-tooltip");
        Icon icon = AdsDefinitionIcon.PROPERTY_GROUP.getIcon();
        AdsSqlClassTree.PopupMenuAction[] actions = new AdsSqlClassTree.PopupMenuAction[]{new AdsSqlClassTreeActions.AddNewFieldAction()};
        Group group = new AdsSqlClassTree.Group(AdsSqlClassTree.FIELDS_NODE, text, icon, toolTip, actions);
        NodeInfo info = infoFactory.create(group);
        Node node = new Node(info);

        for (AdsPropertyDef property : sqlClass.getProperties().getLocal()) {
            if (property instanceof AdsFieldPropertyDef && !(property instanceof AdsFieldRefPropertyDef)) {
                node.add(createFieldNode((AdsFieldPropertyDef) property));
            }
        }

        return node;
    }

    private Node createDynamicPropertyNode(AdsDynamicPropertyDef property) {
        NodeInfo info = infoFactory.create(property);
        Node node = new Node(info);
        return node;
    }

    private Node createFieldNode(AdsFieldPropertyDef field) {
        NodeInfo info = infoFactory.create(field);
        Node node = new Node(info);
        return node;
    }

    private Node createParametersNode(AdsSqlClassDef sqlClass) {
        String text = NbBundle.getMessage(AdsSqlClassTree.class, "parameters-node-text");
        String toolTip = NbBundle.getMessage(AdsSqlClassTree.class, "parameters-node-tooltip");
        Icon icon = AdsDefinitionIcon.SQL_CLASS_PARAMETERS.getIcon();
        AdsSqlClassTree.PopupMenuAction[] actions = new AdsSqlClassTree.PopupMenuAction[]{
            new AdsSqlClassTreeActions.AddCustomParameter(),};
        Group group = new AdsSqlClassTree.Group(AdsSqlClassTree.PARAMETERS_NODE, text, icon, toolTip, actions);
        NodeInfo info = infoFactory.create(group);
        Node node = new Node(info);

        for (AdsPropertyDef property : sqlClass.getProperties().getLocal()) {
            if (property instanceof AdsParameterPropertyDef) {
                AdsParameterPropertyDef param = (AdsParameterPropertyDef) property;
                if (param.canBeUsedInSqml()) {
                    node.add(createParameterNode(param));
                }
            }
        }

        return node;
    }

    private Node createParameterNode(AdsParameterPropertyDef parameter) {
        NodeInfo info = infoFactory.create(parameter);
        Node node = new Node(info);
        return node;
    }

    private DefaultMutableTreeNode createTablesNode(ISqlDef sqlDef) {
        String text = NbBundle.getMessage(AdsSqlClassTree.class, "tables-node-text");
        String toolTip = NbBundle.getMessage(AdsSqlClassTree.class, "tables-node-tooltip");
        Icon icon = DdsDefinitionIcon.TABLE.getIcon();
        AdsSqlClassTree.PopupMenuAction[] actions = new AdsSqlClassTree.PopupMenuAction[]{
            new AdsSqlClassTreeActions.AddUsedTableToTree(),};
        Group group = new AdsSqlClassTree.Group(AdsSqlClassTree.TABLES_NODE, text, icon, toolTip, actions);

        NodeInfo info = infoFactory.create(group);
        Node node = new Node(info);

        for (IUsedTable usedTable : sqlDef.getUsedTables().list()) {
            node.add(createTableNode(usedTable, sqlDef));
        }

        return node;
    }

    private Node createTableNode(IUsedTable table, ISqlDef sqlDef) {
        NodeInfo info = infoFactory.create(table);
        Node node = new Node(info);
        DdsTableDef tableDef = table.findTable();

        if (tableDef != null) {
            node.add(createReferencesNode(tableDef));
            node.add(createColumnsNode(tableDef, sqlDef));
            node.add(createIndicesNode(tableDef));
        }
        return node;
    }

    private DefaultMutableTreeNode createReferencesNode(DdsTableDef table) {
        String text = NbBundle.getMessage(AdsSqlClassTree.class, "references-node-text");
        String toolTip = NbBundle.getMessage(AdsSqlClassTree.class, "references-node-tooltip");
        Icon icon = DdsDefinitionIcon.REFERENCE.getIcon();
        Group group = new AdsSqlClassTree.Group(AdsSqlClassTree.TABLE_REFERENCES_NODE, text, icon, toolTip, null);

        NodeInfo info = infoFactory.create(group);
        Node node = new Node(info);

        for (DdsReferenceDef reference : getReferences(table)) {
            node.add(createReferenceNode(reference));
        }

        return node;
    }

    private DefaultMutableTreeNode createColumnsNode(DdsTableDef table, ISqlDef sqlDef) {
        String text = NbBundle.getMessage(AdsSqlClassTree.class, "properties-node-text");
        String toolTip = NbBundle.getMessage(AdsSqlClassTree.class, "properties-node-tooltip");
        Icon icon = DdsDefinitionIcon.COLUMN.getIcon();
        Group group = new AdsSqlClassTree.Group(AdsSqlClassTree.TABLE_PROPERTIES_NODE, text, icon, toolTip, null);

        NodeInfo info = infoFactory.create(group);
        Node node = new Node(info);

        for (DdsColumnDef column : getColumns(table)) {
            if (sqlDef instanceof AdsSqlClassDef || column.isGeneratedInDb()){
                node.add(createColumnNode(column));
            }
        }
        
        if (sqlDef instanceof AdsSqlClassDef){
            for (AdsExpressionPropertyDef propertyDef : getExpressionProperties(table)) {
                node.add(createExpressionPropertyNode(propertyDef));
            }
        }
        
        return node;
    }

    private Node createDefaultNode(Object object) {
        NodeInfo info = infoFactory.create(object);

        Node node = new Node(info);

        return node;
    }

    private DefaultMutableTreeNode createIndicesNode(DdsTableDef table) {
        String text = NbBundle.getMessage(AdsSqlClassTree.class, "indicies-node-text");
        String toolTip = NbBundle.getMessage(AdsSqlClassTree.class, "indicies-node-tooltip");
        Icon icon = DdsDefinitionIcon.INDEX.getIcon();
        Group group = new AdsSqlClassTree.Group(AdsSqlClassTree.TABLE_INDICIES_NODE, text, icon, toolTip, null);

        NodeInfo info = infoFactory.create(group);
        Node node = new Node(info);

        node.add(createIndexNode(table.getPrimaryKey()));

        for (DdsIndexDef index : getIndices(table)) {
            node.add(createIndexNode(index));
        }

        return node;
    }

    private Node createReferenceNode(DdsReferenceDef reference) {
        return createDefaultNode(reference);
    }

    private Node createExpressionPropertyNode(AdsExpressionPropertyDef expressionPropertyDef) {
        return createDefaultNode(expressionPropertyDef);
    }

    private Node createColumnNode(DdsColumnDef column) {
        NodeInfo info = infoFactory.create(column);

        Node node = new Node(info);

        AdsEnumDef enumDef = AdsEnumUtils.findColumnEnum(column);
        if (enumDef != null) {
            for (AdsEnumItemDef item : enumDef.getItems().list(EScope.ALL)) {
                node.add(createEnumItemNode(item));
            }
        }
        return node;
    }

    private Node createEnumItemNode(IEnumDef.IItem item) {
        return createDefaultNode(item);
    }

    private Node createIndexNode(DdsIndexDef index) {
        return createDefaultNode(index);
    }

    private Node createPackageNode(DdsPlSqlObjectDef plSqlObject) {
        NodeInfo info = infoFactory.create(plSqlObject);
        Node node = new Node(info);
        VisitorProvider provider = SqmlVisitorProviderFactory.newDbFuncCallTagProvider(null);
        for (DdsPlSqlObjectItemDef item : plSqlObject.getBody().getItems()) {
            if (provider.isTarget(item)) {
                node.add(createFunctionNode((DdsFunctionDef) item));
            }
        }
        return node;
    }

    private Node createFunctionNode(DdsFunctionDef item) {
        return createDefaultNode(item);
    }

    private Collection<DdsReferenceDef> getReferences(DdsTableDef table) {
        final IFilter<DdsReferenceDef> filter = SqmlVisitorProviderFactory.newJoinTagFilter(table);
        final Set<DdsReferenceDef> refs = table.collectOutgoingReferences(filter);
        return refs;
    }

    private Collection<DdsIndexDef> getIndices(DdsTableDef table) {
        final IFilter<DdsIndexDef> filter = AdsSqlClassVisitorProviderFactory.newIndicesFilters();
        final Collection<DdsIndexDef> indicies = table.getIndices().get(EScope.ALL, filter);
        return indicies;
    }

    private Collection<DdsColumnDef> getColumns(DdsTableDef table) {
        final IFilter<DdsColumnDef> filter = AdsSqlClassVisitorProviderFactory.newColumnsFilter();
        final Collection<DdsColumnDef> columns = table.getColumns().get(EScope.ALL, filter);
        return columns;
    }

    private Collection<AdsExpressionPropertyDef> getExpressionProperties(final DdsTableDef table) {
        AdsEntityClassDef entity = AdsUtils.findEntityClass(table);
        if (entity == null) {
            return Collections.emptyList();
        }
        List<AdsExpressionPropertyDef> expressionProperties = new ArrayList<AdsExpressionPropertyDef>();
        for (AdsPropertyDef property : entity.getProperties().get(EScope.LOCAL)) {
            if (property instanceof AdsExpressionPropertyDef) {
                expressionProperties.add((AdsExpressionPropertyDef) property);
            }
        }
        return expressionProperties;
    }
}
