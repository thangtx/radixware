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

package org.radixware.kernel.designer.common.editors.sqml.actions;

import java.util.LinkedList;
import java.util.List;
import javax.swing.Icon;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsDefinitionIcon;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.scml.Scml.Tag;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.sqml.providers.SqmlVisitorProviderFactory;
import org.radixware.kernel.common.sqml.tags.PropSqlNameTag;
import org.radixware.kernel.common.sqml.tags.PropSqlNameTag.EOwnerType;
import org.radixware.kernel.common.sqml.tags.TableSqlNameTag;
import org.radixware.kernel.common.sqml.tags.ThisTableIdTag;
import org.radixware.kernel.common.sqml.tags.ThisTableSqlNameTag;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinition;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlEditor;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlEditorPane;
import org.radixware.kernel.designer.common.dialogs.scmlnb.tags.ScmlInsertTagAction;


public class SqmlPopupActions {

    // This table ID 
    public static final String INSERT_THIS_TABLE_ID_TAG = "insert-this-table-id-tag";
    // Property SQL Name
    public static final String INSERT_THIS_PROPERTY_SQL_NAME_TAG_ACTION = "insert-this-property-sql-name-tag-action";
    public static final String INSERT_TABLE_PROPERTY_SQL_NAME_TAG_ACTION = "insert-table-property-sql-name-tag-action";
    public static final String INSERT_CHILD_PROPERTY_SQL_NAME_ACTION = "insert-child-property-sql-name-tag-action";
    public static final String INSERT_PARENT_PROPERTY_SQL_NAME_TAG_ACTION = "insert-parent-property-sql-name-tag-action";
    public static final String INSERT_PROPERTY_SQL_NAME_TAG_ACTION = "insert-property-sql-name-tag-action";
    // Table SQL Name
    public static final String INSERT_TABLE_SQL_NAME_TAG = "insert-table-sql-name-tag";
    public static final String INSERT_THIS_TABLE_SQL_NAME_TAG = "insert-this-table-sql-name-tag";

    public static abstract class SqmlPopupAction extends ScmlInsertTagAction {

        public SqmlPopupAction(String name, ScmlEditor editor) {
            super(name, editor);
        }

        @Override
        protected Class getClassForBundle() {
            return SqmlPopupActions.class;
        }

        @Override
        public void updateState() {
            super.updateState();
            if (isEnabled() && getEditor().getSource() instanceof Sqml) {
                updateStateForSqml((Sqml) getEditor().getSource());
            }
        }

        @Override
        public String getAcceleratorKey() {
            return null;
        }

        @Override
        public boolean isAvailable(Scml scml) {
            return isSqml(scml);
        }

        protected abstract void updateStateForSqml(Sqml sqml);
    }

    // Abstract SQL Name Classes
    public static abstract class InsertPropSqlNameTag extends SqmlPopupAction {

        public InsertPropSqlNameTag(String name, ScmlEditor editor) {
            super(name, editor);
        }

        @Override
        public int getGroupType() {
            return 0;
        }

        @Override
        public int getPosition() {
            return 0;
        }

        @Override
        public List<Tag> createTags() {
            ScmlEditorPane editor = getPane();
            List<Tag> tags = new LinkedList<Tag>();
            Sqml sqml = (Sqml) editor.getScml();
            if (sqml != null) {
                ChooseDefinitionCfg cfg = getCfg(sqml);
                List<Definition> definitions = ChooseDefinition.chooseDefinitions(cfg);
                if (definitions != null) {
                    for (Definition definition : definitions) {
                        PropSqlNameTag tag = PropSqlNameTag.Factory.newInstance();
                        tag.setOwnerType(getOwnerType());
                        tag.setPropId(definition.getDefinition().getId());
                        tag.setPropOwnerId(definition.getOwnerDefinition().getId());
                        tags.add(tag);
                    }
                }
            }
            return tags;
        }

        //protected abstract Scml.Tag createTag(Definition definition);
        protected abstract EOwnerType getOwnerType();

        protected abstract ChooseDefinitionCfg getCfg(Sqml sqml);
    }

    public static abstract class AbstractInsertPropertySqlNameTag extends InsertPropSqlNameTag {

        public AbstractInsertPropertySqlNameTag(String name, ScmlEditor editor) {
            super(name, editor);
        }

        @Override
        protected ChooseDefinitionCfg getCfg(Sqml sqml) {
            return ChooseDefinitionCfg.Factory.newInstance(sqml, SqmlVisitorProviderFactory.newPropSqlNameTagProvider(sqml, getOwnerType()));
        }
    }

    // Property SQL Name Classes
    public static class InsertThisPropertySqlNameTagAction extends AbstractInsertPropertySqlNameTag {

        public InsertThisPropertySqlNameTagAction(ScmlEditor pane) {
            super(INSERT_THIS_PROPERTY_SQL_NAME_TAG_ACTION, pane);
        }

        @Override
        protected void updateStateForSqml(Sqml sqml) {
            if (sqml.getEnvironment().findThisPropertiesOwner() == null) {
                setEnabled(false);
            } else {
                setEnabled(true);
            }
        }

        @Override
        public Icon getIcon() {
            return DdsDefinitionIcon.COLUMN.getIcon(16);
        }

        @Override
        protected EOwnerType getOwnerType() {
            return EOwnerType.THIS;
        }
    }

    public static class InsertTablePropertySqlNameTagAction extends AbstractInsertPropertySqlNameTag {

        public InsertTablePropertySqlNameTagAction(ScmlEditor pane) {
            super(INSERT_TABLE_PROPERTY_SQL_NAME_TAG_ACTION, pane);
        }

        @Override
        protected void updateStateForSqml(Sqml sqml) {
        }

        @Override
        public Icon getIcon() {
            return DdsDefinitionIcon.COLUMN.getIcon(16);
        }

        @Override
        protected EOwnerType getOwnerType() {
            return EOwnerType.TABLE;
        }

        @Override
        public boolean isAvailable(Scml scml) {
            //Oracle restriction (you can only use columns of the curent table in column sql expression)
            if (scml.getDefinition() instanceof DdsColumnDef) {
                return false;
            }
            return true;
        }

        @Override
        protected ChooseDefinitionCfg getCfg(Sqml sqml) {
            ChooseDefinitionCfg cfg = super.getCfg(sqml);
            cfg.setStepCount(2);
            return cfg;
        }
    }

    public static class InsertChildPropertySqlNameTagAction extends AbstractInsertPropertySqlNameTag {

        public InsertChildPropertySqlNameTagAction(ScmlEditor pane) {
            super(INSERT_CHILD_PROPERTY_SQL_NAME_ACTION, pane);
        }

        @Override
        public Icon getIcon() {
            return DdsDefinitionIcon.COLUMN.getIcon(16);
        }

        @Override
        protected void updateStateForSqml(Sqml sqml) {
            if (sqml.getEnvironment().findChildPropertiesOwner() == null) {
                setEnabled(false);
            } else {
                setEnabled(true);
            }
        }

        @Override
        protected EOwnerType getOwnerType() {
            return EOwnerType.CHILD;
        }
    }

    public static class InsertParentPropertySqlNameTagAction extends AbstractInsertPropertySqlNameTag {

        public InsertParentPropertySqlNameTagAction(ScmlEditor pane) {
            super(INSERT_PARENT_PROPERTY_SQL_NAME_TAG_ACTION, pane);
        }

        @Override
        protected void updateStateForSqml(Sqml sqml) {
            if (sqml.getEnvironment().findParentPropertiesOwner() == null) {
                setEnabled(false);
            } else {
                setEnabled(true);
            }
        }

        @Override
        public Icon getIcon() {
            return DdsDefinitionIcon.COLUMN.getIcon(16);
        }

        @Override
        protected EOwnerType getOwnerType() {
            return EOwnerType.PARENT;
        }
    }

    public static class InsertPropertySqlNameTagAction extends AbstractInsertPropertySqlNameTag {

        public InsertPropertySqlNameTagAction(ScmlEditor pane) {
            super(INSERT_PROPERTY_SQL_NAME_TAG_ACTION, pane);
        }

        @Override
        protected void updateStateForSqml(final Sqml sqml) {
        }

        @Override
        public Icon getIcon() {
            return DdsDefinitionIcon.COLUMN.getIcon(16);
        }

        @Override
        public boolean isAvailable(Scml scml) {
            //Oracle restriction (you can only use columns of the curent table in column sql expression)
            if (scml.getDefinition() instanceof DdsColumnDef) {
                return false;
            }
            return true;
        }

        @Override
        protected EOwnerType getOwnerType() {
            return EOwnerType.NONE;
        }

        @Override
        protected ChooseDefinitionCfg getCfg(Sqml sqml) {
            ChooseDefinitionCfg cfg = super.getCfg(sqml);
            cfg.setStepCount(2);
            return cfg;
        }
    }

    // Table SQL Name Classes
    public static class InsertTableSqlNameTagAction extends SqmlPopupAction {

        public InsertTableSqlNameTagAction(ScmlEditor pane) {
            super(INSERT_TABLE_SQL_NAME_TAG, pane);
        }

        @Override
        public List<Tag> createTags() {
            ScmlEditorPane editor = getPane();
            ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(
                    editor.getScml(),
                    SqmlVisitorProviderFactory.newTableSqlNameTagProvider());
            List<Definition> definitions = ChooseDefinition.chooseDefinitions(cfg);
            List<Tag> tags = new LinkedList<Tag>();
            if (definitions != null) {
                boolean first = true;
                for (Definition definition : definitions) {
                    TableSqlNameTag tag = TableSqlNameTag.Factory.newInstance();
                    tag.setTableId(definition.getId());
                    tags.add(tag);
                }
            }
            return tags;
        }

        @Override
        public Icon getIcon() {
            return DdsDefinitionIcon.TABLE.getIcon(16);
        }

        @Override
        protected void updateStateForSqml(final Sqml sqml) {
        }

        @Override
        public boolean isAvailable(Scml scml) {
            //Oracle restriction (you can only use columns of the curent table in column sql expression)
            if (scml.getDefinition() instanceof DdsColumnDef) {
                return false;
            }
            return true;
        }

        @Override
        public int getGroupType() {
            return 0;
        }

        @Override
        public int getPosition() {
            return 0;
        }
    }

    public static class InsertThisTableSqlNameTagAction extends SqmlPopupAction {

        public InsertThisTableSqlNameTagAction(ScmlEditor pane) {
            super(INSERT_THIS_TABLE_SQL_NAME_TAG, pane);
        }

        @Override
        public List<Tag> createTags() {
            ScmlEditorPane editor = getPane();
            Sqml sqml = (Sqml) editor.getScml();
            final DdsTableDef thisTable = sqml.getEnvironment().findThisTable();
            List<Tag> tags = new LinkedList<Tag>();
            if (sqml != null && thisTable != null) {
                ThisTableSqlNameTag tag = ThisTableSqlNameTag.Factory.newInstance();
                tags.add(tag);
            }
            return tags;
        }

        @Override
        protected void updateStateForSqml(Sqml sqml) {
            if (sqml.getEnvironment().findThisTable() == null) {
                setEnabled(false);
            } else {
                setEnabled(true);
            }
        }

        @Override
        public Icon getIcon() {
            return DdsDefinitionIcon.TABLE.getIcon(16);
        }

        @Override
        public int getGroupType() {
            return 0;
        }

        @Override
        public int getPosition() {
            return 0;
        }
    }

    public static class InsertThisTableIdTagAction extends SqmlPopupAction {

        public InsertThisTableIdTagAction(ScmlEditor pane) {
            super(INSERT_THIS_TABLE_ID_TAG, pane);
        }

        @Override
        public List<Tag> createTags() {
            ScmlEditorPane editor = getPane();
            Sqml sqml = (Sqml) editor.getScml();
            final DdsTableDef thisTable = sqml.getEnvironment().findThisTable();
            List<Tag> tags = new LinkedList<Tag>();
            if (sqml != null && thisTable != null) {
                ThisTableIdTag tag = ThisTableIdTag.Factory.newInstance();
                tags.add(tag);
            }
            return tags;
        }

        @Override
        protected void updateStateForSqml(Sqml sqml) {
            if (sqml.getEnvironment().findThisPropertiesOwner() instanceof DdsTableDef) {
                setEnabled(true);
            } else {
                setEnabled(false);
            }
        }

        @Override
        public Icon getIcon() {
            return DdsDefinitionIcon.TABLE.getIcon(16);
        }

        @Override
        public int getGroupType() {
            return 0;
        }

        @Override
        public int getPosition() {
            return 0;
        }
    }    
}
