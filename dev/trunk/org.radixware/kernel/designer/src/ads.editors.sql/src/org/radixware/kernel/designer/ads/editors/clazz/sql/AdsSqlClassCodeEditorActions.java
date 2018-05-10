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

import java.util.Collections;
import java.util.List;
import javax.swing.Icon;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsProcedureClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsSqlClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsStatementClassDef;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.scml.Scml.Tag;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.sqml.providers.SqmlVisitorProviderFactory;
import org.radixware.kernel.common.sqml.tags.ElseIfTag;
import org.radixware.kernel.common.sqml.tags.EndIfTag;
import org.radixware.kernel.common.sqml.tags.PropSqlNameTag;
import org.radixware.kernel.common.sqml.tags.PropSqlNameTag.EOwnerType;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinition;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlEditor;
import org.radixware.kernel.designer.common.dialogs.scmlnb.tags.ScmlInsertTagAction;
import org.radixware.kernel.designer.common.general.utils.DefinitionsUtils;
import org.radixware.kernel.common.resources.RadixWareIcons;


public class AdsSqlClassCodeEditorActions {

    public static final String INSERT_CALC_FIELD_TAG_ACTION = "insert-calc-field-tag-action";
    
    public static final String INSERT_CURSOR_FIELD_TAG_TO_EDITOR = "insert-cursor-field-to-editor";

    public static class InsertCursorFieldTagToEditorAction extends ScmlInsertTagAction {

        public InsertCursorFieldTagToEditorAction(final ScmlEditor editor) {
            super(INSERT_CURSOR_FIELD_TAG_TO_EDITOR, editor);
        }

        @Override
        protected List<Tag> createTags() {
            final List<Definition> thisFields = DefinitionsUtils.collectAllInside(getEditor().getSource().getDefinition(),
                    SqmlVisitorProviderFactory.newPropSqlNameTagProvider((Sqml) (getEditor().getSource()), EOwnerType.THIS));
            final ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(thisFields);
            final Definition def = ChooseDefinition.chooseDefinition(cfg);
            if (def != null) {
                final PropSqlNameTag tag = PropSqlNameTag.Factory.newInstance();
                tag.setOwnerType(EOwnerType.THIS);
                tag.setPropId(def.getId());
                tag.setPropOwnerId(def.getOwnerDefinition().getId());
                return Collections.singletonList((Tag) tag);
            }
            return null;
        }

        @Override
        public Icon getIcon() {
            return AdsDefinitionIcon.Property.PROPERTY_INNATE.getIcon();
        }

        @Override
        public boolean isAvailable(final Scml scml) {
            Definition def = scml.getDefinition();
            return def != null && def instanceof AdsSqlClassDef && !(def instanceof AdsStatementClassDef || def instanceof AdsProcedureClassDef);
        }

        @Override
        public int getGroupType() {
            return 1000;
        }

        @Override
        public int getPosition() {
            return 100;
        }

        @Override
        public String getAcceleratorKey() {
            return "ctrl alt T";
        }

        @Override
        protected Class getClassForBundle() {
            return AdsSqlClassCodeEditorActions.class;
        }
    }


}
