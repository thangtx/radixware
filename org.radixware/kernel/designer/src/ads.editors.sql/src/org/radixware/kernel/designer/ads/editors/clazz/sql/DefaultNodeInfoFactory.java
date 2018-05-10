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

import java.util.Arrays;
import javax.swing.Icon;
import javax.swing.tree.TreeNode;
import org.openide.util.NbBundle;
import org.openide.util.lookup.Lookups;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IEnumDef;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsDynamicPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsExpressionPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsFieldPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsParameterPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsSqlClassDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumItemDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumUtils;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsDefinitionIcon;
import org.radixware.kernel.common.defs.dds.DdsFunctionDef;
import org.radixware.kernel.common.defs.dds.DdsIndexDef;
import org.radixware.kernel.common.defs.dds.DdsPlSqlObjectDef;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.dds.utils.ISqlDef;
import org.radixware.kernel.common.defs.dds.utils.ISqlDef.IUsedTable;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.scml.Scml.Tag;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.sqml.tags.ConstValueTag;
import org.radixware.kernel.common.sqml.tags.DbFuncCallTag;
import org.radixware.kernel.common.sqml.tags.DbNameTag;
import org.radixware.kernel.common.sqml.tags.IndexDbNameTag;
import org.radixware.kernel.common.sqml.tags.JoinTag;
import org.radixware.kernel.common.sqml.tags.ParameterTag;
import org.radixware.kernel.common.sqml.tags.PropSqlNameTag;
import org.radixware.kernel.common.sqml.tags.TableSqlNameTag;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.designer.ads.editors.clazz.sql.AdsSqlClassTree.Group;
import org.radixware.kernel.designer.ads.editors.clazz.sql.AdsSqlClassTree.Node;
import org.radixware.kernel.designer.ads.editors.clazz.sql.AdsSqlClassTree.NodeInfo;
import org.radixware.kernel.designer.ads.editors.clazz.sql.AdsSqlClassTree.PopupMenuAction;
import org.radixware.kernel.designer.common.editors.sqml.editors.JoinTagEditor;
import org.radixware.kernel.designer.common.general.editors.EditorsManager;

/**
 * Default implementation of NodeInfoFactory
 */
public class DefaultNodeInfoFactory implements AdsSqlClassTree.NodeInfoFactory {

    @Override
    public NodeInfo create(final Object object) {
        if (object instanceof AdsSqlClassDef) {
            return new RootInfo((AdsSqlClassDef) object);
        } else if (object instanceof Group) {
            return new GroupInfo((Group) object);
        } else if (object instanceof AdsParameterPropertyDef) {
            return new ParameterInfo((AdsParameterPropertyDef) object);
        } else if (object instanceof AdsDynamicPropertyDef) {
            return new DynamicPropertyInfo((AdsDynamicPropertyDef) object);
        } else if (object instanceof AdsFieldPropertyDef) {
            return new FieldInfo((AdsFieldPropertyDef) object);
        } else if (object instanceof DdsColumnDef) {
            return new ColumnInfo((DdsColumnDef) object);
        } else if (object instanceof IUsedTable) {
            return new TableInfo((IUsedTable) object);
        } else if (object instanceof DdsIndexDef) {
            return new IndexInfo((DdsIndexDef) object);
        } else if (object instanceof DdsPlSqlObjectDef) {
            return new PackageInfo((DdsPlSqlObjectDef) object);
        } else if (object instanceof DdsFunctionDef) {
            return new FunctionInfo((DdsFunctionDef) object);
        } else if (object instanceof DdsReferenceDef) {
            return new ReferenceInfo((DdsReferenceDef) object);
        } else if (object instanceof IEnumDef) {
            return new IEnumDefInfo((IEnumDef) object);
        } else if (object instanceof IEnumDef.IItem) {
            return new IEnumDefItemInfo((IEnumDef.IItem) object);
        } else if (object instanceof AdsExpressionPropertyDef) {
            return new ExpressionPropertyInfo((AdsExpressionPropertyDef) object);
        } else if (object instanceof Definition) {
            return new DefinitionInfo((Definition) object);
        } else {
            throw new IllegalArgumentException("Cannot create NodeInfo for " + object.getClass().getName());
        }
    }

    private static class DefinitionInfo<T extends Definition> extends NodeInfo<T> {

        public DefinitionInfo(final T definition) {
            super(definition);
        }

        @Override
        public Icon getIcon() {
            return getObject().getIcon().getIcon();
        }

        @Override
        public String getText() {
            return getObject().getName();
        }

        @Override
        public String getToolTip() {
            return getObject().getToolTip();
        }
    }

    private static class GroupInfo<T extends Group> extends NodeInfo<T> {

        private final Group group;

        public GroupInfo(final T group) {
            super(group, group.getIcon(), group.getTitle(), group.getToolTip());
            this.group = group;
        }

        @Override
        public PopupMenuAction[] getActions() {
            return group.getActions();
        }
    }

    private static class IEnumDefInfo<T extends IEnumDef> extends NodeInfo<T> {

        public IEnumDefInfo(final T object) {
            super(object, RadixObjectIcon.ENUM.getIcon(), object.getName(), null);
        }
    }

    private static class IEnumDefItemInfo<T extends IEnumDef.IItem> extends NodeInfo<T> {

        @Override
        public boolean canGoTo() {
            return getObject() instanceof AdsEnumItemDef;
        }

        @Override
        public void performGoTo() {
            if (getObject() instanceof AdsEnumItemDef) {
                EditorsManager.getDefault().open((AdsEnumItemDef) getObject());
            }
        }

        public IEnumDefItemInfo(final T object) {
            super(object, RadixObjectIcon.ENUM_ITEM.getIcon(), object.getName(), null);
        }

        @Override
        public PopupMenuAction[] getActions() {
            return new PopupMenuAction[]{
                        new AdsSqlClassTreeActions.InsertTagToEditor(),
                        new AdsSqlClassTreeActions.GoToObject()};
        }

        @Override
        public boolean canInsertTag(final AdsSqlClassTree tree) {
            return true;
        }

        @Override
        public PopupMenuAction getDefultAction() {
            return new AdsSqlClassTreeActions.InsertTagToEditor();
        }

        @Override
        public String getToolTip() {
            if (getObject() instanceof AdsEnumItemDef) {
                return ((AdsEnumItemDef) getObject()).getToolTip();
            }
            return null;
        }

        @Override
        public Scml.Tag createTag(final AdsSqlClassTree tree) {
            final ConstValueTag tag = ConstValueTag.Factory.newInstance();

            tag.setItemId(getObject().getId());

            final Node node = (Node) tree.findNodeByObject(getObject()).getParent();
            final DdsColumnDef columnDef = (DdsColumnDef) node.getNodeInfo().getObject();
            final AdsEnumDef enumDef = AdsEnumUtils.findColumnEnum(columnDef);

             Definition owner = tree.getSqlDef().getDefinition();
            if (owner != null){
                Module module = owner.getModule();
                if (module != null){
                    module.getDependences().add(enumDef.getModule());
                }
            }
            tag.setEnumId(enumDef.getId());

            return tag;
        }
    }

    private static class RootInfo<T extends AdsSqlClassDef> extends DefinitionInfo<T> {

        public RootInfo(final T sqlClass) {
            super(sqlClass);
        }
    }

    private static class PackageInfo<T extends DdsPlSqlObjectDef> extends DefinitionInfo<T> {

        public PackageInfo(final T definition) {
            super(definition);
        }

        @Override
        public PopupMenuAction[] getActions() {
            return new PopupMenuAction[]{
                        new AdsSqlClassTreeActions.InsertTagToEditor(),
                        new AdsSqlClassTreeActions.RemoveObjectFromTree(),};
        }

        @Override
        public boolean canInsertTag(final AdsSqlClassTree tree) {
            return true;
        }

        @Override
        protected Tag createTag(final AdsSqlClassTree tree) {
            return DbNameTag.Factory.newInstance(getObject().getIdPath());
        }

        @Override
        public boolean canRemove(final AdsSqlClassTree tree) {
            return true;
        }

        @Override
        public void remove(final AdsSqlClassTree tree) {
            //AdsSqlClass doesn't store list of used packages. so it's nothing to do here
        }
    }

    private static class ReferenceInfo<T extends DdsReferenceDef> extends DefinitionInfo<T> {

        public ReferenceInfo(final T definition) {
            super(definition);
        }

        @Override
        public boolean canGoTo() {
            return true;
        }

        @Override
        public PopupMenuAction[] getActions() {
            final PopupMenuAction[] actions = new PopupMenuAction[]{
                new AdsSqlClassTreeActions.GoToObject(),
                new AdsSqlClassTreeActions.InsertTagToEditor()};
            return mergeArrays(actions, super.getActions());
        }

        @Override
        public PopupMenuAction getDefultAction() {
            return new AdsSqlClassTreeActions.InsertTagToEditor();
        }

        @Override
        public boolean canInsertTag(final AdsSqlClassTree tree) {
            return true;
        }

        @Override
        public Scml.Tag createTag(final AdsSqlClassTree tree) {
            final JoinTag tag = JoinTag.Factory.newInstance();
            tag.setReferenceId(getObject().getId());
            final Node usedTableNode = AdsSqlClassTreeUtilities.getParentUsedTableNode((TreeNode) tree.getSelectionPath().getLastPathComponent());
            final IUsedTable usedTable = (IUsedTable) usedTableNode.getNodeInfo().getObject();
            final JoinTagEditor.AliasCookie aliasCookie = new JoinTagEditor.AliasCookie(usedTable.getAlias(), null);
            if (tree.getEditor().getPane().editTag(tag, Lookups.fixed(tree.getSqlDef(), getObject(), aliasCookie))) {
                return tag;
            }
            return null;
        }

        @Override
        public void performGoTo() {
            EditorsManager.getDefault().open(getObject());
        }
    }

    private static class FieldInfo<T extends AdsFieldPropertyDef> extends DefinitionInfo<T> {

        public FieldInfo(T definition) {
            super(definition);
        }

        @Override
        public boolean canInsertTag(AdsSqlClassTree tree) {
            return true;
        }

        @Override
        public boolean canRemove(AdsSqlClassTree tree) {
            return true;
        }

        @Override
        public PopupMenuAction[] getActions() {
            PopupMenuAction[] actions = new PopupMenuAction[]{
                new AdsSqlClassTreeActions.InsertTagToEditor(),
                new AdsSqlClassTreeActions.RemoveObjectFromTree(),
                new AdsSqlClassTreeActions.EditObjectInTree(),
                new AdsSqlClassTreeActions.GoToObject(),
                new AdsSqlClassTreeActions.RenameRadixObjectAction(getObject())
            };

            return mergeArrays(actions, super.getActions());
        }

        @Override
        public PopupMenuAction getDefultAction() {
            return new AdsSqlClassTreeActions.InsertTagToEditor();
        }

        @Override
        public boolean canGoTo() {
            return true;
        }

        @Override
        public void performGoTo() {
            EditorsManager.getDefault().open(getObject());
        }

        @Override
        public boolean canEdit(AdsSqlClassTree tree) {
            return true;
        }

        @Override
        public boolean edit(AdsSqlClassTree tree) {
            EditorsManager.getDefault().open(getObject());
            return true;
        }

        @Override
        protected Tag createTag(AdsSqlClassTree tree) {
            final PropSqlNameTag propTag = PropSqlNameTag.Factory.newInstance();
            propTag.setOwnerType(PropSqlNameTag.EOwnerType.THIS);
            propTag.setPropId(getObject().getId());
            propTag.setPropOwnerId(getObject().getOwnerDefinition().getId());
            return propTag;
        }

        @Override
        public void remove(AdsSqlClassTree tree) {
            getObject().delete();
        }
    }

    private static class DynamicPropertyInfo<T extends AdsDynamicPropertyDef> extends DefinitionInfo<T> {

        public DynamicPropertyInfo(T definition) {
            super(definition);
        }

        @Override
        public boolean canInsertTag(AdsSqlClassTree tree) {
            return true;
        }

        @Override
        public boolean canRemove(AdsSqlClassTree tree) {
            return true;
        }

        @Override
        public PopupMenuAction[] getActions() {
            PopupMenuAction[] actions = new PopupMenuAction[]{
                new AdsSqlClassTreeActions.InsertTagToEditor(),
                new AdsSqlClassTreeActions.RemoveObjectFromTree(),
                new AdsSqlClassTreeActions.EditObjectInTree(),
                new AdsSqlClassTreeActions.GoToObject(),
                new AdsSqlClassTreeActions.RenameRadixObjectAction(getObject())
            };

            return mergeArrays(actions, super.getActions());
        }

        @Override
        public PopupMenuAction getDefultAction() {
            return new AdsSqlClassTreeActions.InsertTagToEditor();
        }

        @Override
        public boolean canGoTo() {
            return true;
        }

        @Override
        public void performGoTo() {
            EditorsManager.getDefault().open(getObject());
        }

        @Override
        public boolean canEdit(AdsSqlClassTree tree) {
            return true;
        }

        @Override
        public boolean edit(AdsSqlClassTree tree) {
            EditorsManager.getDefault().open(getObject());
            return true;
        }

        @Override
        protected Tag createTag(AdsSqlClassTree tree) {
            final ParameterTag tag = ParameterTag.Factory.newInstance();
            tag.setParameterId(getObject().getId());
            return tag;
        }

        @Override
        public void remove(AdsSqlClassTree tree) {
            getObject().delete();
        }
    }

    private static class ParameterInfo<T extends AdsParameterPropertyDef> extends DefinitionInfo<T> {

        public ParameterInfo(final T parameter) {
            super(parameter);
        }

        @Override
        public PopupMenuAction[] getActions() {
            final PopupMenuAction[] actions = new PopupMenuAction[]{
                new AdsSqlClassTreeActions.MoveParameterUp(),
                new AdsSqlClassTreeActions.MoveParameterDown(),
                new AdsSqlClassTreeActions.InsertTagToEditor(),
                new AdsSqlClassTreeActions.EditObjectInTree(),
                new AdsSqlClassTreeActions.RemoveObjectFromTree(),
                new AdsSqlClassTreeActions.RenameRadixObjectAction(getObject())};
            return mergeArrays(actions, super.getActions());
        }

        @Override
        public PopupMenuAction getDefultAction() {
            return new AdsSqlClassTreeActions.InsertTagToEditor();
        }

        @Override
        public String getText() {
            return getObject().getName();
        }

        @Override
        public boolean canInsertTag(final AdsSqlClassTree tree) {
            return true;
        }

        @Override
        public boolean canRemove(final AdsSqlClassTree tree) {
            return true;
        }

        @Override
        public void remove(final AdsSqlClassTree tree) {
            getObject().delete();
        }

        @Override
        public Scml.Tag createTag(final AdsSqlClassTree tree) {
            final ParameterTag tag = ParameterTag.Factory.newInstance();
            tag.setParameterId(getObject().getId());
            boolean needToOpenEditor = false;
            if (AdsParameterPropertyDef.canBeOutput(getObject())) {
                needToOpenEditor = true;
            }
            if (getObject().getValue().getType().getTypeId() == EValType.PARENT_REF || getObject().getValue().getType().getTypeId().isArrayType()) {
                needToOpenEditor = true;
            }
            if (needToOpenEditor) {
                if (tree.getEditor().getPane().editTag(tag, Lookups.fixed(getObject()))) {
                    return tag;
                } else {
                    return null;
                }
            }
            return tag;
        }

        @Override
        public boolean canEdit(final AdsSqlClassTree tree) {
            return true;
        }

        @Override
        public boolean edit(final AdsSqlClassTree tree) {
            EditorsManager.getDefault().open(getObject());
            return true;
        }

        @Override
        public Icon getIcon() {
            return RadixObjectIcon.getForValType(getObject().getValue().getType().getTypeId()).getIcon();
        }
    }

    private static class ColumnInfo<T extends DdsColumnDef> extends DefinitionInfo<T> {

        public ColumnInfo(final T column) {
            super(column);
        }

        @Override
        public PopupMenuAction[] getActions() {
            final PopupMenuAction[] actions = new PopupMenuAction[]{
                new AdsSqlClassTreeActions.InsertTagToEditor(),
                new AdsSqlClassTreeActions.GoToObject(),
                new AdsSqlClassTreeActions.AddTypifiedParameter(),
                new AdsSqlClassTreeActions.InsertTagAndGenerateField()};
            return mergeArrays(actions, super.getActions());
        }

        @Override
        public PopupMenuAction getDefultAction() {
            return new AdsSqlClassTreeActions.InsertTagToEditor();
        }

        @Override
        public boolean canGoTo() {
            return true;
        }

        @Override
        public void performGoTo() {
            EditorsManager.getDefault().open(getObject());
        }

        @Override
        public boolean canInsertTag(final AdsSqlClassTree tree) {
            return true;
        }

        @Override
        public Scml.Tag createTag(final AdsSqlClassTree tree) {
            final Node usedTableNode = AdsSqlClassTreeUtilities.getParentUsedTableNode((TreeNode) tree.getSelectionPath().getLastPathComponent());
            final NodeInfo info = usedTableNode.getNodeInfo();
            final IUsedTable usedTable = (IUsedTable) info.getObject();
            final PropSqlNameTag tag = PropSqlNameTag.Factory.newInstance();
            tag.setPropId(getObject().getId());
            tag.setPropOwnerId(getObject().getOwnerDefinition().getId());
            tag.setOwnerType(PropSqlNameTag.EOwnerType.TABLE);
            tag.setTableAlias(usedTable.getAlias());
            return tag;
        }
    }

    private static class ExpressionPropertyInfo<T extends AdsExpressionPropertyDef> extends DefinitionInfo<T> {

        public ExpressionPropertyInfo(final T property) {
            super(property);
        }

        @Override
        public Icon getIcon() {
            final EValType valType = getObject().getValue().getType().getTypeId();
            return RadixObjectIcon.getForValType(valType).getIcon();
        }

        @Override
        public PopupMenuAction[] getActions() {
            final PopupMenuAction[] actions = new PopupMenuAction[]{
                new AdsSqlClassTreeActions.InsertTagToEditor(),
                new AdsSqlClassTreeActions.GoToObject(),
                new AdsSqlClassTreeActions.AddTypifiedParameter(),
                new AdsSqlClassTreeActions.InsertTagAndGenerateField()};
            return mergeArrays(actions, super.getActions());
        }

        @Override
        public PopupMenuAction getDefultAction() {
            return new AdsSqlClassTreeActions.InsertTagToEditor();
        }

        @Override
        public boolean canGoTo() {
            return true;
        }

        @Override
        public void performGoTo() {
            EditorsManager.getDefault().open(getObject());
        }

        @Override
        public boolean canInsertTag(final AdsSqlClassTree tree) {
            return true;
        }

        @Override
        public Scml.Tag createTag(final AdsSqlClassTree tree) {
            final Node usedTableNode = AdsSqlClassTreeUtilities.getParentUsedTableNode((TreeNode) tree.getSelectionPath().getLastPathComponent());
            final NodeInfo info = usedTableNode.getNodeInfo();
            final IUsedTable usedTable = (IUsedTable) info.getObject();
            final PropSqlNameTag tag = PropSqlNameTag.Factory.newInstance();
            tag.setPropId(getObject().getId());
            tag.setPropOwnerId(getObject().getOwnerDefinition().getId());
            tag.setOwnerType(PropSqlNameTag.EOwnerType.TABLE);
            tag.setTableAlias(usedTable.getAlias());
            return tag;
        }
    }

    private static class TableInfo<T extends IUsedTable> extends NodeInfo<IUsedTable> {

        private final String notFoundMessage = NbBundle.getMessage(DefaultNodeInfoFactory.class, "table-not-found");

        public TableInfo(final IUsedTable table) {
            super(table);
            setIcon(DdsDefinitionIcon.TABLE.getIcon());

        }

        @Override
        public PopupMenuAction[] getActions() {
            return new PopupMenuAction[]{
                        new AdsSqlClassTreeActions.InsertTagToEditor(),
                        new AdsSqlClassTreeActions.RemoveObjectFromTree(),
                        new AdsSqlClassTreeActions.ChangeTableAlias(getObject()),
                        new AdsSqlClassTreeActions.GoToObject(),
                        new AdsSqlClassTreeActions.AddPkParameter()};
        }

        @Override
        public boolean canGoTo() {
            return getObject().findTable() != null;
        }

        @Override
        public void performGoTo() {
            EditorsManager.getDefault().open(getObject().getTable());
        }

        @Override
        public PopupMenuAction getDefultAction() {
            return new AdsSqlClassTreeActions.InsertTagToEditor();
        }

        @Override
        public boolean canRemove(final AdsSqlClassTree tree) {
            final Id tableId = getObject().getTableId();
            final Sqml sqml = tree.getEditor().getSource();
            for (Sqml.Item item : sqml.getItems()) {
                if (item instanceof PropSqlNameTag) {
                    final PropSqlNameTag tag = (PropSqlNameTag) item;
                    if (tag.getOwnerDefinition().getId().equals(tableId) && Utils.aliasesAreEqual(tag.getTableAlias(), getObject().getAlias())) {
                        return false;
                    }
                }
                if (item instanceof TableSqlNameTag) {
                    final TableSqlNameTag tag = (TableSqlNameTag) item;
                    if (tag.getTableId().equals(tableId) && Utils.aliasesAreEqual(tag.getTableAlias(), getObject().getAlias())) {
                        return false;
                    }
                }
            }
            return true;

        }

        @Override
        public void remove(final AdsSqlClassTree tree) {
            final ISqlDef sqlClass = tree.getSqlDef();
            sqlClass.getUsedTables().remove((IUsedTable)getObject());
        }

        @Override
        public boolean canInsertTag(final AdsSqlClassTree tree) {
            return true;
        }

        @Override
        public Scml.Tag createTag(final AdsSqlClassTree tree) {
            final TableSqlNameTag tag = TableSqlNameTag.Factory.newInstance();
            final DdsTableDef table = getObject().findTable();
            tag.setTableId(table.getId());
            tag.setTableAlias(getObject().getAlias());
            return tag;
        }

        @Override
        public String getText() {
            final DdsTableDef tableDef = getObject().findTable();
            if (tableDef != null) {
                if (getObject().getAlias() != null && getObject().getAlias().length() > 0) {
                    return tableDef.getName() + " [" + getObject().getAlias() + "]";
                }
                return tableDef.getName();
            } else {
                return "<html><font color = \"ff0000\">" + getObject().getTableId().toString() + "</font></html>";
            }
        }

        @Override
        public String getToolTip() {
            final DdsTableDef tableDef = getObject().findTable();
            if (tableDef != null) {
                return tableDef.getToolTip();
            } else {
                return notFoundMessage;
            }
        }
    }

    private static class IndexInfo<T extends DdsIndexDef> extends DefinitionInfo<T> {

        public IndexInfo(final T index) {
            super(index);
        }

        @Override
        public PopupMenuAction[] getActions() {
            final PopupMenuAction[] actions = new PopupMenuAction[]{
                new AdsSqlClassTreeActions.InsertTagToEditor(),
                new AdsSqlClassTreeActions.InsertIndexExpression(getObject()),
                new AdsSqlClassTreeActions.GoToObject()};
            return mergeArrays(actions, super.getActions());
        }

        @Override
        public PopupMenuAction getDefultAction() {
            return new AdsSqlClassTreeActions.InsertTagToEditor();
        }

        @Override
        public boolean canGoTo() {
            return true;
        }

        @Override
        public void performGoTo() {
            EditorsManager.getDefault().open(getObject());
        }

        @Override
        public boolean canInsertTag(final AdsSqlClassTree tree) {
            return true;
        }

        @Override
        public Scml.Tag createTag(final AdsSqlClassTree tree) {
            final IndexDbNameTag tag = IndexDbNameTag.Factory.newInstance();
            tag.setIndexId(getObject().getId());
            tag.setTableId(getObject().getOwnerTable().getId());
            return tag;
        }
    }

    private static class FunctionInfo<T extends DdsFunctionDef> extends DefinitionInfo<T> {

        public FunctionInfo(final T definition) {
            super(definition);
        }

        @Override
        public PopupMenuAction[] getActions() {
            final PopupMenuAction[] actions = new PopupMenuAction[]{
                new AdsSqlClassTreeActions.InsertTagToEditor()
            };
            return mergeArrays(actions, super.getActions());
        }

        @Override
        public PopupMenuAction getDefultAction() {
            return new AdsSqlClassTreeActions.InsertTagToEditor();
        }

        @Override
        public boolean canInsertTag(final AdsSqlClassTree tree) {
            return true;
        }

        @Override
        public Scml.Tag createTag(final AdsSqlClassTree tree) {
            final DbFuncCallTag tag = DbFuncCallTag.Factory.newInstance();
            tag.setFunctionId(getObject().getId());
            return tag;
        }
    }

    private static <T> T[] mergeArrays(final T[] a, final T[] b) {
        if (a == null) {
            return b;
        }
        if (b == null) {
            return a;
        }
        T[] r = Arrays.copyOf(a, a.length + b.length);
        for (int i = a.length; i < a.length + b.length; i++) {
            r[i] = b[i - a.length];
        }
        return r;
    }
}
