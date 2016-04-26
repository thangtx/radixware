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

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.swing.Icon;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.lookup.Lookups;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsDynamicPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsExpressionPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsFieldPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsParameterPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsProcedureClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsSqlClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsSqlClassDef.UsedTable;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsStatementClassDef;
import org.radixware.kernel.common.defs.ads.common.AdsUtils;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumUtils;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsDefinitionIcon;
import org.radixware.kernel.common.defs.dds.DdsIndexDef;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.EPropNature;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.sqml.tags.DbNameTag;
import org.radixware.kernel.common.sqml.tags.IfParamTag;
import org.radixware.kernel.common.sqml.tags.JoinTag;
import org.radixware.kernel.common.sqml.tags.PropSqlNameTag;
import org.radixware.kernel.common.sqml.tags.TableSqlNameTag;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.designer.ads.common.sql.AdsSqlClassVisitorProviderFactory;
import org.radixware.kernel.designer.ads.editors.clazz.creation.PropertyCreature;
import org.radixware.kernel.designer.ads.editors.clazz.sql.AdsSqlClassTree.Node;
import org.radixware.kernel.designer.ads.editors.clazz.sql.AdsSqlClassTree.NodeInfo;
import org.radixware.kernel.designer.ads.editors.clazz.sql.AdsSqlClassTree.PopupMenuAction;
import org.radixware.kernel.designer.ads.editors.clazz.sql.panels.AliasEditor;
import org.radixware.kernel.designer.ads.editors.clazz.sql.panels.AliasesEditorPanel;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinition;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;

import org.radixware.kernel.designer.common.dialogs.wizards.newobject.CreationWizard;
import org.radixware.kernel.designer.common.general.creation.ICreatureGroup;
import org.radixware.kernel.designer.common.general.creation.ICreatureGroup.ICreature;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.dialogs.utils.EditorOpenInfo;


public class AdsSqlClassTreeActions {

    public static final String MOVE_PARAMETER_UP = "move-parameter-up";
    public static final String MOVE_PARAMETER_DOWN = "move-parameter-down";
    public static final String ADD_USED_TABLE_TO_TREE = "add-used-table-to-tree";
    public static final String ADD_USED_PACKAGE_TO_TREE = "add-used-package-to-tree";
    public static final String CHANGE_TABLE_ALIAS = "change-table-alias";
    public static final String ADD_LITERAL_PARAMETER = "add-literal-parameter";
    public static final String ADD_CUSTOM_PARAMETER = "add-custom-parameter";
    public static final String EDIT_OBJECT_IN_TREE = "edit-object-in-tree";
    public static final String GO_TO_OBJECT = "go-to-object";
    public static final String ADD_TYPIFIED_PARAMETER = "add-typified-parameter";
    public static final String ADD_PK_PARAMETER = "add-pk-parameter";
    public static final String ADD_FIELD = "add-field";
    public static final String INSERT_TAG_TO_EDITOR = "insert-tag-to-editor";
    public static final String REMOVE_OBJECT_FROM_TREE = "remove-object-from-tree";
    public static final String REMOVE_SELECTED_OBJECTS = "remove-selected-objects";
    public static final String INSERT_IF_TAG_TO_EDITOR = "insert-if-tag-to-editor";
    public static final String INSERT_TYPIFIED_CALC_FIELD_TAG = "insert-typified-calc-field-tag";
    public static final String INSERT_TAG_AND_GENERATE_FIELD = "insert-tag-and-generate-field";
    public static final String INSERT_INDEX_EXPRESSION = "insert-index-expression";
    public static final String RENAME_OBJECT = "rename-parameter";
    public static final String ADD_DYNAMIC_PARAMETER = "add-dynamic-parameter";

    public static abstract class AbstractPopupMenuAction extends AdsSqlClassTree.PopupMenuAction {

        private boolean valuesFilled = false;

        public AbstractPopupMenuAction(final String name) {
            super(name);

        }

        @Override
        public Object[] getKeys() {
            if (!valuesFilled) {
                fillValues();
            }
            return super.getKeys();
        }

        @Override
        public Object getValue(final String key) {
            if (!valuesFilled) {
                fillValues();
            }
            return super.getValue(key);
        }

        @Override
        public void putValue(final String key, final Object newValue) {
            if (!valuesFilled) {
                fillValues();
            }
            super.putValue(key, newValue);
        }

        private void fillValues() {
            valuesFilled = true;
            putValue(POPUP_MENU_TEXT, getLocalizedTitle());
            putValue(SHORT_DESCRIPTION, getLocalizedToolTip());
            putValue(SMALL_ICON, getIcon());
        }

        protected String getLocalizedTitle() {
            final Object val = getValue(NAME);
            String title = "Title Not Found";
            if (val instanceof String) {
                String key = (String) val;
                key += "-title";
                title = NbBundle.getMessage(AdsSqlClassTreeActions.class, key);
            }
            return title;
        }

        protected String getLocalizedToolTip() {
            final Object val = getValue(NAME);
            String tooltip = null;
            if (val instanceof String) {
                String key = (String) val;
                key += "-tooltip";
                tooltip = NbBundle.getMessage(AdsSqlClassTreeActions.class, key);
            }
            return tooltip;
        }

        protected abstract Icon getIcon();
    }

    public static class RemoveObjectFromTree extends AbstractPopupMenuAction {

        public RemoveObjectFromTree() {
            super(REMOVE_OBJECT_FROM_TREE);
            putValue(ACTION_COMMAND_KEY, "DELETE");
        }

        @Override
        protected void actionPerformed(final AdsSqlClassTree tree) {
            final NodeInfo info = getSelectedNodeInfo(tree);
            final TreePath path = tree.getSelectionPath();
            final String confirmMessage = NbBundle.getMessage(AdsSqlClassTreeActions.class, "msg-confirm-remove-one");
            final int res = JOptionPane.showConfirmDialog(null, confirmMessage, "", JOptionPane.YES_NO_CANCEL_OPTION);
            if (res == JOptionPane.YES_OPTION && info.canRemove(tree)) {
                info.remove(tree);
                final MutableTreeNode parentNode = (MutableTreeNode) path.getParentPath().getLastPathComponent();
                parentNode.remove((MutableTreeNode) path.getLastPathComponent());
                final DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
                model.nodeStructureChanged(parentNode);
                tree.getEditor().update();
            }
        }

        @Override
        protected boolean isAvailable(final AdsSqlClassTree tree) {
            setEnabled(!tree.isReadOnly() && getSelectedNodeInfo(tree).canRemove(tree));
            return true;
        }

        @Override
        protected int getType() {
            return PopupMenuAction.BOTTOM;
        }

        @Override
        protected int getPriority() {
            return 500;
        }

        @Override
        protected Icon getIcon() {
            return RadixWareIcons.DELETE.DELETE.getIcon();
        }
    }

    public static class RemoveSelectedObjects extends AbstractPopupMenuAction {

        public RemoveSelectedObjects() {
            super(REMOVE_SELECTED_OBJECTS);
        }

        @Override
        protected void actionPerformed(AdsSqlClassTree tree) {
            TreePath[] selections = tree.getSelectionPaths();
            String confirmMessage;
            if (selections.length == 1) {
                confirmMessage = NbBundle.getMessage(AdsSqlClassTreeActions.class, "msg-confirm-remove-one");
            } else {
                confirmMessage = NbBundle.getMessage(AdsSqlClassTreeActions.class, "msg-confirm-remove-many");
            }
            final int res = JOptionPane.showConfirmDialog(null, confirmMessage, "", JOptionPane.YES_NO_CANCEL_OPTION);
            if (res == JOptionPane.YES_OPTION) {
                for (TreePath path : selections) {
                    if (path.getLastPathComponent() instanceof AdsSqlClassTree.Node) {
                        final NodeInfo info = ((AdsSqlClassTree.Node) path.getLastPathComponent()).getNodeInfo();
                        info.remove(tree);
                        final MutableTreeNode parentNode = (MutableTreeNode) path.getParentPath().getLastPathComponent();
                        parentNode.remove((MutableTreeNode) path.getLastPathComponent());
                        final DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
                        model.nodeStructureChanged(parentNode);
                    }
                }
                tree.getEditor().update();
            }
        }

        @Override
        protected boolean isAvailable(final AdsSqlClassTree tree) {
            if (tree.isReadOnly()) {
                return false;
            }
            final TreePath[] selections = tree.getSelectionPaths();
            if (selections == null || selections.length == 0) {
                return false;
            }
            for (TreePath path : selections) {
                if (path.getLastPathComponent() instanceof AdsSqlClassTree.Node) {
                    final NodeInfo info = ((AdsSqlClassTree.Node) path.getLastPathComponent()).getNodeInfo();
                    if (info == null || !info.canRemove(tree)) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
            return true;
        }

        @Override
        protected int getType() {
            return PopupMenuAction.BOTTOM;
        }

        @Override
        protected int getPriority() {
            return 1000;
        }

        @Override
        protected Icon getIcon() {
            return RadixWareIcons.DELETE.DELETE.getIcon();
        }
    }

    public static abstract class AddObjectToTree<T extends Object> extends AbstractPopupMenuAction {

        public AddObjectToTree(final String name) {
            super(name);
            final Icon icon = getSpecialIcon();//NOPMD
            if (icon != null) {
                putValue(SMALL_ICON, icon);
            }
        }

        @Override
        protected void actionPerformed(final AdsSqlClassTree tree) {
            final Node parentNode = getParentNode(tree);
            final Collection<T> objects = getCreatedObjects(tree);
            if (objects != null) {
                final DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
                Node lastAdded = null;
                for (T object : objects) {
                    final Node node = tree.getNodeFactory().create(object, tree);
                    parentNode.add(node);
                    lastAdded = node;
                }
                model.nodeStructureChanged(parentNode);

                tree.setSelectedNode(lastAdded);
                tree.rebuildTreeWithStatePersistance();
            }
        }

        protected abstract Collection<T> getCreatedObjects(AdsSqlClassTree tree);

        protected abstract Node getParentNode(AdsSqlClassTree tree);

        @Override
        protected boolean isAvailable(final AdsSqlClassTree tree) {
            return !tree.isReadOnly();
        }

        @Override
        public JMenuItem getPopupPresenter() {
            final JMenuItem presenter = super.getPopupPresenter();
            final Icon specialIcon = getSpecialIcon();
            if (specialIcon != null) {
                presenter.setIcon(specialIcon);
            }
            return presenter;
        }

        protected Icon getSpecialIcon() {
            return null;
        }

        @Override
        protected int getType() {
            return PopupMenuAction.ADD;
        }

        @Override
        protected Icon getIcon() {
            return RadixWareIcons.CREATE.ADD.getIcon();
        }
    }

    public static class AddUsedTableToTree extends AddObjectToTree<UsedTable> {

        public AddUsedTableToTree() {
            super(ADD_USED_TABLE_TO_TREE);
        }

        @Override
        protected Collection<UsedTable> getCreatedObjects(final AdsSqlClassTree tree) {
            final ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(
                    tree.getSqlClass(),
                    AdsSqlClassVisitorProviderFactory.newUsedTableProvider());
            final List<Definition> definitions = ChooseDefinition.chooseDefinitions(cfg);
            LinkedList<UsedTable> usedTables = null;
            if (definitions != null) {
                usedTables = new LinkedList<UsedTable>();
                final AdsSqlClassDef sqlClass = tree.getSqlClass();
                for (Definition definition : definitions) {
                    final DdsTableDef table = (DdsTableDef) definition;
                    usedTables.add(sqlClass.getUsedTables().add(table, null));
                }
            } else {
                return null;
            }
            if (usedTables.size() == 1) {
                final AliasEditor panel = new AliasEditor();
                String alias = usedTables.getFirst().getAlias();
                if (alias == null) {
                    alias = "";
                }
                panel.open(alias, new EditorOpenInfo(false, Lookup.EMPTY));
                if (panel.showModal()) {
                    usedTables.getFirst().setAlias(panel.getAlias());
                }
            } else if (usedTables.size() > 1) {
                final AliasesEditorPanel panel = new AliasesEditorPanel();
                panel.open(usedTables);
                panel.showModal();
            }
            final DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
            model.nodeStructureChanged(tree.findNodeByGroupName(AdsSqlClassTree.TABLES_NODE));
            return usedTables;
        }

        @Override
        protected Node getParentNode(final AdsSqlClassTree tree) {
            return tree.findNodeByGroupName(AdsSqlClassTree.TABLES_NODE);
        }

        @Override
        protected Icon getSpecialIcon() {
            return DdsDefinitionIcon.TABLE.getIcon();
        }

        @Override
        protected int getPriority() {
            return 100;
        }

        @Override
        protected Icon getIcon() {
            return null;
        }
    }

    public static abstract class MoveParameter extends AbstractPopupMenuAction {

        public MoveParameter(final String name) {
            super(name);
        }

        @Override
        protected void actionPerformed(final AdsSqlClassTree tree) {
            final AdsSqlClassDef sqlClass = tree.getSqlClass();
            final AdsParameterPropertyDef parameter = getParameter(tree);
            final int paramIndex = getParameterIndex(sqlClass, parameter);
            final int swapParamIndex = getSwapIndex(sqlClass, parameter);
            sqlClass.getProperties().getLocal().swap(paramIndex, swapParamIndex);
            final Node node = (Node) tree.getSelectionPath().getLastPathComponent();
            final Node parent = (Node) node.getParent();
            final int idx = parent.getIndex(node);
            final int selectedRow = tree.getSelectionRows()[0];
            parent.remove(idx);
            parent.insert(node, idx + getShift());
            final DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
            model.nodeStructureChanged(parent);
            tree.setSelectionRow(selectedRow + getShift());

        }

        protected AdsParameterPropertyDef getParameter(final AdsSqlClassTree tree) {
            if (getSelectedNode(tree) != null) {
                if (getSelectedNodeInfo(tree).getObject() instanceof AdsParameterPropertyDef) {
                    return (AdsParameterPropertyDef) getSelectedNodeInfo(tree).getObject();
                }
            }
            return null;
        }

        protected abstract int getSwapIndex(AdsSqlClassDef sqlClass, AdsParameterPropertyDef parameter);

        protected abstract int getShift();

        protected boolean canMove(final AdsSqlClassTree tree) {
            return getSwapIndex(tree.getSqlClass(), getParameter(tree)) != -1;
        }

        protected int getParameterIndex(final AdsSqlClassDef sqlClass, final AdsParameterPropertyDef parameter) {
            return sqlClass.getProperties().getLocal().indexOf(parameter);
        }

        @Override
        protected boolean isAvailable(final AdsSqlClassTree tree) {
            if (tree.isReadOnly()) {
                return false;
            }
            final NodeInfo info = getSelectedNodeInfo(tree);
            if (info == null) {
                return false;
            }
            if (info.getObject() instanceof AdsParameterPropertyDef) {
                return canMove(tree);
            }
            return false;
        }

        @Override
        protected int getType() {
            return PopupMenuAction.MIDDLE;
        }
    }

    public static class MoveParameterUp extends MoveParameter {

        public MoveParameterUp() {
            super(MOVE_PARAMETER_UP);
            putValue(ACTION_COMMAND_KEY, "control SUBTRACT");
        }

        @Override
        protected int getShift() {
            return -1;
        }

        @Override
        protected int getPriority() {
            return 100;
        }

        @Override
        protected Icon getIcon() {
            return RadixWareIcons.ARROW.MOVE_UP.getIcon();
        }

        @Override
        protected int getSwapIndex(final AdsSqlClassDef sqlClass, final AdsParameterPropertyDef parameter) {
            final int paramIndex = sqlClass.getProperties().getLocal().indexOf(parameter);
            for (int i = paramIndex - 1; i >= 0; i--) {
                if (sqlClass.getProperties().getLocal().get(i) instanceof AdsParameterPropertyDef) {
                    return i;
                }
            }
            return -1;
        }
    }

    public static class MoveParameterDown extends MoveParameter {

        public MoveParameterDown() {
            super(MOVE_PARAMETER_DOWN);
            putValue(ACTION_COMMAND_KEY, "control ADD");
        }

        @Override
        protected int getShift() {
            return 1;
        }

        @Override
        protected int getPriority() {
            return 200;
        }

        @Override
        protected Icon getIcon() {
            return RadixWareIcons.ARROW.MOVE_DOWN.getIcon();
        }

        @Override
        protected int getSwapIndex(final AdsSqlClassDef sqlClass, final AdsParameterPropertyDef parameter) {
            final int paramIndex = sqlClass.getProperties().getLocal().indexOf(parameter);
            for (int i = paramIndex + 1; i < sqlClass.getProperties().getLocal().size(); i++) {
                if (sqlClass.getProperties().getLocal().get(i) instanceof AdsParameterPropertyDef) {
                    return i;
                }
            }
            return -1;
        }
    }

    public static class ChangeTableAlias extends AbstractPopupMenuAction {

        private final UsedTable table;

        public ChangeTableAlias(final UsedTable table) {
            super(CHANGE_TABLE_ALIAS);
            putValue(ACTION_COMMAND_KEY, "F2");
            this.table = table;
        }

        @Override
        protected void actionPerformed(final AdsSqlClassTree tree) {
            final String newAlias = getNewAlias();
            if (newAlias != null && !newAlias.equals(table.getAlias())) {
                updateSqml((Sqml) tree.getEditor().getPane().getScml(), newAlias);
                tree.getEditor().update();
                table.setAlias(newAlias);
                final DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
                model.nodeStructureChanged(tree.findNodeByObject(table));
            }
        }

        private void updateSqml(final Sqml sqml, final String newAlias) {
            for (Sqml.Item item : sqml.getItems()) {
                if (item instanceof TableSqlNameTag) {
                    final TableSqlNameTag tag = (TableSqlNameTag) item;
                    if (tag.getTableId().equals(table.getTableId()) && Utils.aliasesAreEqual(table.getAlias(), tag.getTableAlias())) {
                        tag.setTableAlias(newAlias);
                    }
                }
                if (item instanceof PropSqlNameTag) {
                    final PropSqlNameTag tag = (PropSqlNameTag) item;
                    if (tag.getPropOwnerId().equals(table.getTableId()) && Utils.aliasesAreEqual(table.getAlias(), tag.getTableAlias())) {
                        tag.setTableAlias(newAlias);
                    }
                }
                if (item instanceof JoinTag) {
                    final JoinTag tag = (JoinTag) item;
                    final DdsReferenceDef referenceDef = tag.findReference();
                    if (referenceDef != null) {
                        if (referenceDef.getChildTableId().equals(table.getTableId()) && Utils.aliasesAreEqual(table.getAlias(), tag.getChildTableAlias())) {
                            tag.setChildTableAlias(newAlias);
                        }

                        if (referenceDef.getParentTableId().equals(table.getTableId()) && Utils.aliasesAreEqual(table.getAlias(), tag.getParentTableAlias())) {
                            tag.setParentTableAlias(newAlias);
                        }
                    }
                }
            }
        }

        private String getNewAlias() {
            final AliasEditor panel = new AliasEditor();
            panel.open(table.getAlias(), new EditorOpenInfo(false, Lookup.EMPTY));
            panel.showModal();
            return panel.getAlias();
        }

        @Override
        protected boolean isAvailable(final AdsSqlClassTree tree) {
            return !tree.isReadOnly() && table.findTable() != null;
        }

        @Override
        protected int getType() {
            return PopupMenuAction.MIDDLE;
        }

        @Override
        protected int getPriority() {
            return 100;
        }

        @Override
        protected Icon getIcon() {
            return RadixWareIcons.EDIT.EDIT.getIcon();
        }
    }

    public static class AddCustomParameter extends AddObjectToTree<AdsParameterPropertyDef> {

        public AddCustomParameter() {
            super(ADD_CUSTOM_PARAMETER);
        }

        @Override
        protected Collection<AdsParameterPropertyDef> getCreatedObjects(final AdsSqlClassTree tree) {
            final AdsSqlClassDef sqlClass = tree.getSqlClass();
            final List<ICreature> creatures = PropertyCreature.Factory.createInstances(sqlClass.getPropertyGroup(), Collections.singleton(EPropNature.SQL_CLASS_PARAMETER));
            final ICreatureGroup group = new ICreatureGroup() {

                @Override
                public List<ICreature> getCreatures() {
                    return creatures;
                }

                @Override
                public String getDisplayName() {
                    return "group";
                }
            };
            final ICreature result = CreationWizard.execute(new ICreatureGroup[]{group}, creatures.get(0));
            if (result != null) {
                AdsParameterPropertyDef prop = (AdsParameterPropertyDef) result.commit();
                if (!prop.canBeUsedInSqml()) {
                    DialogUtils.messageInformation("Parameter " + prop.getName() + " can not be used in sqml");
                }
                return Collections.singleton(prop);

            }
            return null;
        }

        @Override
        protected Node getParentNode(final AdsSqlClassTree tree) {
            return tree.findNodeByGroupName(AdsSqlClassTree.PARAMETERS_NODE);
        }

        @Override
        protected Icon getSpecialIcon() {
            return AdsDefinitionIcon.SQL_CLASS_CUSTOM_PARAMETER.getIcon();
        }

        @Override
        protected int getPriority() {
            return 200;
        }
    }

    public static class AddDynamicProperty extends AddObjectToTree<AdsDynamicPropertyDef> {

        public AddDynamicProperty() {
            super(ADD_DYNAMIC_PARAMETER);
        }

        @Override
        protected Collection<AdsDynamicPropertyDef> getCreatedObjects(final AdsSqlClassTree tree) {
            final AdsSqlClassDef sqlClass = tree.getSqlClass();
            final List<ICreature> creatures = PropertyCreature.Factory.createInstances(sqlClass.getPropertyGroup(), Collections.singleton(EPropNature.DYNAMIC));
            final ICreatureGroup group = new ICreatureGroup() {

                @Override
                public List<ICreature> getCreatures() {
                    return creatures;
                }

                @Override
                public String getDisplayName() {
                    return "group";
                }
            };
            final ICreature result = CreationWizard.execute(new ICreatureGroup[]{group}, creatures.get(0));
            if (result != null) {
                result.commit();
                tree.update();
            }
            return null;
        }

        @Override
        protected Icon getSpecialIcon() {
            return AdsDefinitionIcon.Property.PROPERTY_DYNAMIC.getIcon();
        }

        @Override
        protected Node getParentNode(final AdsSqlClassTree tree) {
            return tree.findNodeByGroupName(AdsSqlClassTree.DYNAMIC_PROPERTIES_NODE);
        }

        @Override
        protected int getPriority() {
            return 200;
        }
    }

    public static class EditObjectInTree extends AbstractPopupMenuAction {

        public EditObjectInTree() {
            super(EDIT_OBJECT_IN_TREE);
        }

        @Override
        protected void actionPerformed(final AdsSqlClassTree tree) {
            final NodeInfo info = getSelectedNodeInfo(tree);
            if (info.canEdit(tree)) {
                info.edit(tree);
            }
            final DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
            model.nodeChanged(getSelectedNode(tree));
            tree.getEditor().update();
        }

        @Override
        protected boolean isAvailable(final AdsSqlClassTree tree) {
            if (tree.isReadOnly()) {
                return false;
            }
            if (tree.getSelectionCount() != 1) {
                return false;
            }
            if (getSelectedNode(tree) != null) {
                return getSelectedNodeInfo(tree).canEdit(tree);
            }
            return false;
        }

        @Override
        protected int getType() {
            return PopupMenuAction.EDIT;
        }

        @Override
        protected int getPriority() {
            return 100;
        }

        @Override
        protected Icon getIcon() {
            return RadixWareIcons.EDIT.EDIT.getIcon();
        }
    }

    public static class GoToObject extends AbstractPopupMenuAction {

        public GoToObject() {
            super(GO_TO_OBJECT);
            putValue(ACTION_COMMAND_KEY, "control G");
        }

        @Override
        protected void actionPerformed(final AdsSqlClassTree tree) {
            final NodeInfo info = getSelectedNodeInfo(tree);
            info.performGoTo();
        }

        @Override
        protected boolean isAvailable(final AdsSqlClassTree tree) {
            return getSelectedNodeInfo(tree).canGoTo();
        }

        @Override
        protected int getType() {
            return PopupMenuAction.BOTTOM;
        }

        @Override
        protected int getPriority() {
            return 100;
        }

        @Override
        protected Icon getIcon() {
            return RadixWareIcons.ARROW.GO_TO_OBJECT.getIcon();
        }
    }

    public static class AddTypifiedParameter extends AddObjectToTree<AdsParameterPropertyDef> {

        public AddTypifiedParameter() {
            super(ADD_TYPIFIED_PARAMETER);
        }

        @Override
        protected boolean isAvailable(final AdsSqlClassTree tree) {
            final AdsTypeDeclaration typeDecl = extractType(getSelectedNode(tree));
            if (typeDecl == null || typeDecl.getTypeId() == null || typeDecl.getTypeId() == EValType.ANY) {
                return false;
            }
            return true;
        }

        @Override
        protected Collection<AdsParameterPropertyDef> getCreatedObjects(final AdsSqlClassTree tree) {
            final AdsParameterPropertyDef parameter = AdsParameterPropertyDef.Factory.newInstance();

            final TreePath path = tree.getSelectionPath();

            final Node node = (Node) path.getLastPathComponent();

            final AdsTypeDeclaration type = extractType(node);

            final String baseName = extractObjectName(node);
            parameter.setName("p" + baseName.substring(0, 1).toUpperCase() + baseName.substring(1));
            parameter.getValue().setType(type);

            tree.getSqlClass().getProperties().getLocal().add(parameter);
            return Collections.singleton(parameter);
        }

        @Override
        protected Node getParentNode(final AdsSqlClassTree tree) {
            return tree.findNodeByGroupName(AdsSqlClassTree.PARAMETERS_NODE);
        }

        @Override
        protected Icon getSpecialIcon() {
            return AdsDefinitionIcon.SQL_CLASS_TYPIFIED_PARAMETER.getIcon();
        }

        @Override
        protected int getPriority() {
            return 300;
        }
    }

    public static class AddPkParameter extends AddObjectToTree<AdsParameterPropertyDef> {

        public AddPkParameter() {
            super(ADD_PK_PARAMETER);
        }

        @Override
        protected Collection<AdsParameterPropertyDef> getCreatedObjects(final AdsSqlClassTree tree) {
            final UsedTable usedTable = (UsedTable) getSelectedNodeInfo(tree).getObject();
            final DdsTableDef table = usedTable.findTable();
            final AdsParameterPropertyDef parameter = AdsParameterPropertyDef.Factory.newInstance();
            final AdsEntityClassDef entity = AdsUtils.findEntityClass(table);

            if (entity == null) {
                DialogUtils.messageError("Entity class by table \"" + table.getName() + "\" [#" + table.getId() + "] not found");
                return null;
            }

            final AdsTypeDeclaration type = AdsTypeDeclaration.Factory.newParentRef(entity);
            //If there is no dependency on entity module, then parameter would be unresolved.
            tree.getSqlClass().getModule().getDependences().add(entity.getModule());

            String pName = usedTable.getAlias();
            if (pName == null) {
                pName = entity.getName();
            }
            parameter.setName("p" + pName);
            parameter.getValue().setType(type);

            tree.getSqlClass().getProperties().getLocal().add(parameter);
            return Collections.singleton(parameter);
        }

        @Override
        protected Node getParentNode(final AdsSqlClassTree tree) {
            return tree.findNodeByGroupName(AdsSqlClassTree.PARAMETERS_NODE);
        }

        @Override
        protected Icon getSpecialIcon() {
            return AdsDefinitionIcon.SQL_CLASS_PK_PARAMETER.getIcon();
        }

        @Override
        protected int getPriority() {
            return 300;
        }
    }

    public static class InsertTagToEditor extends AbstractPopupMenuAction {

        public InsertTagToEditor() {
            super(INSERT_TAG_TO_EDITOR);

        }

        @Override
        protected void actionPerformed(final AdsSqlClassTree tree) {
            getSelectedNodeInfo(tree).insertTag(tree);
            tree.getEditor().getPane().requestFocusInWindow();
        }

        @Override
        protected boolean isAvailable(final AdsSqlClassTree tree) {
            if (getSelectedNode(tree) == null) {
                return false;
            }
            if (tree.getEditor().getPane().isCurrentPositionEditable()) {
                return getSelectedNodeInfo(tree).canInsertTag(tree);
            }
            return false;
        }

        @Override
        protected int getType() {
            return PopupMenuAction.TOP;
        }

        @Override
        protected int getPriority() {
            return 100;
        }

        @Override
        protected Icon getIcon() {
            return RadixWareIcons.EDIT.INSERT_OBJECT.getIcon();
        }
    }

    public static class InsertTagAndGenerateField extends AbstractPopupMenuAction {

        public InsertTagAndGenerateField() {
            super(INSERT_TAG_AND_GENERATE_FIELD);
        }

        private String getFieldName(final String initName) {
            final AliasEditor editor = new AliasEditor("Field name:", "Field Name");
            editor.setAlias(initName);
            if (editor.showModal()) {
                return editor.getAlias().length() > 0 ? editor.getAlias() : null;
            } else {
                return null;
            }
        }

        @Override
        protected void actionPerformed(final AdsSqlClassTree tree) {
            final Node node = getSelectedNode(tree);
            final AdsSqlClassDef sqlClass = tree.getSqlClass();
            AdsFieldPropertyDef field;
            final String fieldName = getFieldName(extractObjectName(node));
            if (fieldName == null) {
                return;
            }
            field = AdsFieldPropertyDef.Factory.newInstance(fieldName);
            sqlClass.getProperties().getLocal().add(field);
            field.setConst(true);
            field.getAccessFlags().setPublic();

            AdsTypeDeclaration typeDecl = extractType(node);

            field.getValue().setType(typeDecl);

            final Scml.Tag columnTag = node.getNodeInfo().createTag(tree);
            assert columnTag instanceof PropSqlNameTag;

            tree.getEditor().getPane().insertTag(columnTag);
            tree.getEditor().getPane().insertString(" as ");

            final PropSqlNameTag propTag = PropSqlNameTag.Factory.newInstance();
            propTag.setOwnerType(PropSqlNameTag.EOwnerType.THIS);
            propTag.setPropId(field.getId());
            propTag.setPropOwnerId(field.getOwnerDefinition().getId());

            tree.update();
            tree.getEditor().getPane().insertTag(propTag);
            tree.getEditor().getPane().requestFocusInWindow();

        }

        @Override
        protected boolean isAvailable(final AdsSqlClassTree tree) {
            final AdsTypeDeclaration typeDecl = extractType(getSelectedNode(tree));
            if (typeDecl == null || typeDecl.getTypeId() == null || typeDecl.getTypeId() == EValType.ANY) {
                return false;
            }
            if (tree.getEditor().getPane().isCurrentPositionEditable()) {
                return getSelectedNodeInfo(tree).canInsertTag(tree);
            }
            return false;
        }

        @Override
        protected int getType() {
            return PopupMenuAction.TOP;
        }

        @Override
        protected int getPriority() {
            return 200;
        }

        @Override
        protected Icon getIcon() {
            return RadixWareIcons.EDIT.INSERT_OBJECT.getIcon();
        }
    }

    private static Node getSelectedNode(final AdsSqlClassTree tree) {
        if (tree.getSelectionPath() != null && tree.getSelectionPath().getLastPathComponent() != null && tree.getSelectionPath().getLastPathComponent() instanceof Node) {
            return (Node) tree.getSelectionPath().getLastPathComponent();
        }
        return null;
    }

    private static NodeInfo getSelectedNodeInfo(final AdsSqlClassTree tree) {
        if (getSelectedNode(tree) == null) {
            return null;
        } else {
            return getSelectedNode(tree).getNodeInfo();
        }
    }

    public static class InsertIndexExpression extends AbstractPopupMenuAction {

        private final DdsIndexDef index;

        public InsertIndexExpression(final DdsIndexDef index) {
            super(INSERT_INDEX_EXPRESSION);
            this.index = index;
        }

        @Override
        protected Icon getIcon() {
            return DdsDefinitionIcon.INDEX.getIcon();
        }

        @Override
        protected void actionPerformed(final AdsSqlClassTree tree) {
            final DbNameTag indexTag = DbNameTag.Factory.newInstance(index.getIdPath());
            final TableSqlNameTag tableTag = TableSqlNameTag.Factory.newInstance();
            tableTag.setTableId(index.getOwnerTable().getId());
            String alias = null;

            final Node indexNode = getSelectedNode(tree);
            if (indexNode != null && indexNode.getNodeInfo().getObject() == index) {
                final Object usedTableObj = ((Node) indexNode.getParent().getParent()).getNodeInfo().getObject();
                if (usedTableObj instanceof UsedTable) {
                    alias = ((UsedTable) usedTableObj).getAlias();
                }
            }

            tableTag.setTableAlias(alias);

            tree.getEditor().getPane().insertString("/*+ INDEX(");
            tree.getEditor().getPane().insertTag(tableTag);
            tree.getEditor().getPane().insertString(" ");
            tree.getEditor().getPane().insertTag(indexTag);
            tree.getEditor().getPane().insertString(") */");

            tree.getEditor().getPane().requestFocusInWindow();
        }

        @Override
        protected boolean isAvailable(final AdsSqlClassTree tree) {
            return tree.getEditor().getPane().isCurrentPositionEditable();
        }

        @Override
        protected int getType() {
            return PopupMenuAction.TOP;
        }

        @Override
        protected int getPriority() {
            return 300;
        }
    }

    public static class InsertIfTag extends AbstractPopupMenuAction {

        private final AdsParameterPropertyDef parameter;

        public InsertIfTag(final AdsParameterPropertyDef parameter) {
            super(INSERT_IF_TAG_TO_EDITOR);
            this.parameter = parameter;
        }

        @Override
        protected void actionPerformed(final AdsSqlClassTree tree) {
            final IfParamTag tag = IfParamTag.Factory.newInstance();
            tag.setParameterId(parameter.getId());
            if (tree.getEditor().getPane().editTag(tag, Lookups.fixed(parameter))) {
                tree.getEditor().getPane().insertTag(tag);
                tree.getEditor().getPane().requestFocusInWindow();
            }
        }

        @Override
        protected boolean isAvailable(final AdsSqlClassTree tree) {
            return AdsSqlClassVisitorProviderFactory.newPropertyForPreprocessorTag().isTarget(parameter) && tree.getEditor().getPane().isCurrentPositionEditable();
        }

        @Override
        protected int getType() {
            return PopupMenuAction.MIDDLE;
        }

        @Override
        protected int getPriority() {
            return 1000;
        }

        @Override
        protected Icon getIcon() {
            return RadixWareIcons.DEBUG.IF.getIcon();
        }
    }

    public static class RenameRadixObjectAction extends AbstractPopupMenuAction {

        private final RadixObject parameter;

        public RenameRadixObjectAction(final RadixObject parameter) {
            super(RENAME_OBJECT);
            putValue(ACTION_COMMAND_KEY, "F2");
            this.parameter = parameter;
        }

        @Override
        protected void actionPerformed(final AdsSqlClassTree tree) {
            final String newAlias = getNewAlias();
            if (newAlias != null && newAlias.length() > 0 && !newAlias.equals(parameter.getName())) {
                parameter.setName(newAlias);
                tree.getEditor().update();
                final DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
                model.nodeStructureChanged(tree.findNodeByObject(parameter));
            }
        }

        private String getNewAlias() {
            final AliasEditor panel = new AliasEditor("Name:", "Rename");
            panel.open(parameter.getName(), new EditorOpenInfo(false, Lookup.EMPTY));
            panel.showModal();
            return panel.getAlias();
        }

        @Override
        protected boolean isAvailable(final AdsSqlClassTree tree) {
            return !tree.isReadOnly();
        }

        @Override
        protected int getType() {
            return PopupMenuAction.EDIT;
        }

        @Override
        protected int getPriority() {
            return 100;
        }

        @Override
        protected Icon getIcon() {
            return RadixWareIcons.EDIT.EDIT.getIcon();
        }
    }

    public static class AddNewFieldAction extends AbstractPopupMenuAction {

        public AddNewFieldAction() {
            super(ADD_FIELD);
        }

        @Override
        protected Icon getIcon() {
            return AdsDefinitionIcon.Property.PROPERTY_INNATE.getIcon();
        }

        @Override
        protected void actionPerformed(AdsSqlClassTree tree) {
            final AdsSqlClassDef sqlClass = tree.getSqlClass();
            final List<ICreature> creatures = PropertyCreature.Factory.createInstances(sqlClass.getPropertyGroup(), Collections.singleton(EPropNature.FIELD));
            final ICreatureGroup group = new ICreatureGroup() {

                @Override
                public List<ICreature> getCreatures() {
                    return creatures;
                }

                @Override
                public String getDisplayName() {
                    return "group";
                }
            };
            final ICreature result = CreationWizard.execute(new ICreatureGroup[]{group}, creatures.get(0));
            if (result != null) {
                result.commit();
                tree.update();
            }
        }

        @Override
        protected boolean isAvailable(AdsSqlClassTree tree) {
            if (tree.getSqlClass() instanceof AdsStatementClassDef || tree.getSqlClass() instanceof AdsProcedureClassDef) {
                return false;
            }
            return !tree.isReadOnly();
        }

        @Override
        protected int getType() {
            return TOP;
        }

        @Override
        protected int getPriority() {
            return 100;
        }
    }

    private static AdsTypeDeclaration extractType(Node node) {
        if (node == null) {
            return null;
        }
        Object nodeObject = node.getNodeInfo().getObject();
        final AdsTypeDeclaration type;
        if (nodeObject instanceof DdsColumnDef) {
            final DdsColumnDef column = (DdsColumnDef) nodeObject;
            final AdsEnumDef enumDef = AdsEnumUtils.findColumnEnum(column);
            if (enumDef != null) {
                type = AdsTypeDeclaration.Factory.newInstance(enumDef);
            } else {
                type = AdsTypeDeclaration.Factory.newInstance(column.getValType());
            }
        } else if (nodeObject instanceof AdsExpressionPropertyDef) {
            return ((AdsExpressionPropertyDef) nodeObject).getValue().getType();
        } else {
            throw new IllegalArgumentException("Cant't extract type from " + nodeObject);
        }
        return type;
    }

    private static String extractObjectName(Node node) {
        Object nodeObject = node.getNodeInfo().getObject();
        if (nodeObject instanceof Definition) {
            return ((Definition) nodeObject).getName();
        }
        throw new IllegalArgumentException("Cant extract base name from " + nodeObject);
    }
}
