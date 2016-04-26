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

package org.radixware.kernel.designer.common.dialogs.scmlnb.tags;

import java.util.List;
import javax.swing.Icon;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsDefinitionIcon;
import org.radixware.kernel.common.defs.dds.DdsFunctionDef;
import org.radixware.kernel.common.defs.dds.DdsIndexDef;
import org.radixware.kernel.common.defs.dds.DdsPackageDef;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsSequenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.dds.DdsTriggerDef;
import org.radixware.kernel.common.defs.dds.DdsTypeDef;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.common.jml.JmlTagDbName;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.scml.Scml.Tag;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.sqml.tags.DbNameTag;
import org.radixware.kernel.common.sqml.tags.SequenceDbNameTag;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinition;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlEditor;


public abstract class InsertDbNameTagActions {

    public static final int TABLE_GROUP = 1000;
    public static final String INSERT_TABLE_DB_NAME_TAG_ACTION = "insert-table-db-name-tag-action";
    public static final String INSERT_COLUMN_DB_NAME_TAG_ACTION = "insert-column-db-name-tag-action";
    public static final int INDEX_GROUP = 2000;
    public static final String INSERT_INDEX_DB_NAME_TAG_ACTION = "insert-index-db-name-tag-action";
    public static final String INSERT_TRIGGER_DB_NAME_TAG_ACTION = "insert-trigger-db-name-tag-action";
    public static final int REFERENCE_GROUP = 3000;
    public static final String INSERT_REFERENCE_DB_NAME_TAG_ACTION = "insert-reference-db-name-tag-action";
    public static final String INSERT_SQML_SEQUENCE_DB_NAME_TAG_ACTION = "insert-sqml-sequence-db-name-tag-action";
    public static final String INSERT_JML_SEQUENCE_DB_NAME_TAG_ACTION = "insert-jml-sequence-db-name-tag-action";
    public static final int PACKAGE_GROUP = 4000;
    public static final String INSERT_PACKAGE_DB_NAME_TAG_ACTION = "insert-package-db-name-tag-action";
    public static final String INSERT_TYPE_DB_NAME_TAG_ACTION = "insert-type-db-name-tag-action";
    public static final String INSERT_FUNCTION_DB_NAME_TAG_ACTION = "insert-function-db-name-tag-action";

    public static abstract class InsertDbNameTagAction extends InsertTagByDefinitionAction {

        public InsertDbNameTagAction(String name, ScmlEditor editor) {
            super(name, editor);
        }

        @Override
        protected Tag createTag(Definition definition) {
            assert definition != null : "Can not create tag for null definition";
            Scml.Tag tag;
            if (getPane().getScml() instanceof Sqml) {
                tag = DbNameTag.Factory.newInstance(definition.getIdPath());
            } else if (getPane().getScml() instanceof Jml) {
                if (definition instanceof DdsTableDef) {
                    tag = JmlTagDbName.Factory.newInstance((DdsTableDef) definition);
                } else if (definition instanceof DdsColumnDef) {
                    tag = JmlTagDbName.Factory.newInstance((DdsColumnDef) definition);
                } else if (definition instanceof DdsSequenceDef) {
                    tag = JmlTagDbName.Factory.newInstance((DdsSequenceDef) definition);
                } else if (definition instanceof DdsIndexDef) {
                    tag = JmlTagDbName.Factory.newInstance((DdsIndexDef) definition);
                } else if (definition instanceof DdsReferenceDef) {
                    tag = JmlTagDbName.Factory.newInstance((DdsReferenceDef) definition);
                } else if (definition instanceof DdsFunctionDef) {
                    tag = JmlTagDbName.Factory.newInstance((DdsFunctionDef) definition);
                } else if (definition instanceof DdsTypeDef) {
                    tag = JmlTagDbName.Factory.newInstance((DdsTypeDef) definition);
                } else {
                    throw new IllegalArgumentException("Can not create JmlTagDbName by " + definition);
                }
            } else {
                throw new IllegalStateException("Unsupported Scml type: " + getEditor().getPane().getScml());
            }
            return tag;
        }
    }

    protected static abstract class InsertDbNameTagActionImpl extends InsertDbNameTagAction {

        private final Icon icon;
        private final int position;
        private final int groupType;
        private final int stepCount;
        private final Class<? extends Definition> definitionClass;

        public InsertDbNameTagActionImpl(
                String name,
                ScmlEditor editor,
                Class<? extends Definition> definitionClass,
                RadixIcon radixIcon,
                int stepCount,
                int groupType,
                int position) {
            super(name, editor);
            this.definitionClass = definitionClass;
            this.icon = radixIcon.getIcon();
            this.stepCount = stepCount;
            this.groupType = groupType;
            this.position = position;
        }

        @Override
        protected List<Definition> chooseDefinitions() {
            return ChooseDefinition.chooseDefinitions(createCfg());
        }

        protected ChooseDefinitionCfg createCfg() {
            return ChooseDefinitionCfgFactory.createCfgForDbNameTag(getPane().getScml(), definitionClass, stepCount);
        }

        @Override
        public int getGroupType() {
            return groupType;
        }

        @Override
        public int getPosition() {
            return position;
        }

        @Override
        public Icon getIcon() {
            return icon;
        }

        @Override
        public String getAcceleratorKey() {
            return null;
        }

        @Override
        protected Class getClassForBundle() {
            return InsertDbNameTagActions.class;
        }

        @Override
        public boolean isAvailable(Scml scml) {
            return scml != null;
        }
    }

    public static class InsertIndexDbNameTagAction extends InsertDbNameTagActionImpl {

        public InsertIndexDbNameTagAction(ScmlEditor editor) {
            super(INSERT_INDEX_DB_NAME_TAG_ACTION, editor, DdsIndexDef.class, DdsDefinitionIcon.INDEX, 2, INDEX_GROUP, 100);
        }
    }

    public static class InsertTriggerDbNameTagAction extends InsertDbNameTagActionImpl {

        public InsertTriggerDbNameTagAction(ScmlEditor editor) {
            super(INSERT_TRIGGER_DB_NAME_TAG_ACTION, editor, DdsTriggerDef.class, DdsDefinitionIcon.TRIGGER, 2, INDEX_GROUP, 200);
        }
    }

    public static class InsertReferenceDbNameTagAction extends InsertDbNameTagActionImpl {

        public InsertReferenceDbNameTagAction(ScmlEditor editor) {
            super(INSERT_REFERENCE_DB_NAME_TAG_ACTION, editor, DdsReferenceDef.class, DdsDefinitionIcon.REFERENCE, 1, REFERENCE_GROUP, 100);
        }
    }

    public static class InsertPackageDbNameTagAction extends InsertDbNameTagActionImpl {

        public InsertPackageDbNameTagAction(ScmlEditor editor) {
            super(INSERT_PACKAGE_DB_NAME_TAG_ACTION, editor, DdsPackageDef.class, DdsDefinitionIcon.PACKAGE, 1, PACKAGE_GROUP, 100);
        }
    }

    public static class InsertTypeDbNameTagAction extends InsertDbNameTagActionImpl {

        public InsertTypeDbNameTagAction(ScmlEditor editor) {
            super(INSERT_TYPE_DB_NAME_TAG_ACTION, editor, DdsTypeDef.class, DdsDefinitionIcon.TYPE, 1, PACKAGE_GROUP, 200);
        }
    }

    public static class InsertFunctionDbNameTagAction extends InsertDbNameTagActionImpl {

        public InsertFunctionDbNameTagAction(ScmlEditor editor) {
            super(INSERT_FUNCTION_DB_NAME_TAG_ACTION, editor, DdsFunctionDef.class, DdsDefinitionIcon.FUNCTION, 2, PACKAGE_GROUP, 300);
        }
    }

    public static class InsertTableDbNameTagAction extends InsertDbNameTagActionImpl {

        public InsertTableDbNameTagAction(ScmlEditor editor) {
            super(INSERT_TABLE_DB_NAME_TAG_ACTION, editor, DdsTableDef.class, DdsDefinitionIcon.TABLE, 1, TABLE_GROUP, 100);
        }
    }

    public static class InsertColumnDbNameTagAction extends InsertDbNameTagActionImpl {

        public InsertColumnDbNameTagAction(ScmlEditor editor) {
            super(INSERT_COLUMN_DB_NAME_TAG_ACTION, editor, DdsColumnDef.class, DdsDefinitionIcon.COLUMN, 2, TABLE_GROUP, 200);
        }
    }

    public static class InsertJmlSequenceDbNameTagAction extends InsertDbNameTagActionImpl {

        public InsertJmlSequenceDbNameTagAction(ScmlEditor editor) {
            super(INSERT_JML_SEQUENCE_DB_NAME_TAG_ACTION, editor, DdsSequenceDef.class, DdsDefinitionIcon.SEQUENCE, 1, REFERENCE_GROUP, 200);
        }
    }

    public static class InsertSqmlSequenceDbNameTagAction extends InsertDbNameTagActionImpl {

        public InsertSqmlSequenceDbNameTagAction(ScmlEditor editor) {
            super(INSERT_SQML_SEQUENCE_DB_NAME_TAG_ACTION, editor, DdsSequenceDef.class, DdsDefinitionIcon.SEQUENCE, 1, REFERENCE_GROUP, 200);
        }

        @Override
        protected Tag createTag(Definition definition) {
            SequenceDbNameTag tag = SequenceDbNameTag.Factory.newInstance();
            tag.setSequenceId(definition.getId());
            return tag;
        }
    }
}
