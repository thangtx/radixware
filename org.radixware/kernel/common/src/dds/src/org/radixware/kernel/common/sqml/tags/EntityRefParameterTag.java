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

package org.radixware.kernel.common.sqml.tags;

import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.EPidTranslationMode;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;

/**
 * Under construction.
 */
public class EntityRefParameterTag extends ParameterAbstractTag implements IEntityRefTag {

    private EPidTranslationMode pidTranslationMode = EPidTranslationMode.PRIMARY_KEY_PROPS;

    public EPidTranslationMode getPidTranslationMode() {
        return pidTranslationMode;
    }

    public void setPidTranslationMode(final EPidTranslationMode mode) {
        if (!Utils.equals(this.pidTranslationMode, mode)) {
            this.pidTranslationMode = mode;
            setEditState(EEditState.MODIFIED);
        }
    }
    private Id pidTranslationSkId;

    public Id getPidTranslationSecondaryKeyId() {
        return pidTranslationSkId;
    }

    public void setPidTranslationSecondaryKeyId(final Id skId) {
        if (!Utils.equals(this.pidTranslationSkId, skId)) {
            this.pidTranslationSkId = skId;
            setEditState(EEditState.MODIFIED);
        }
    }
    private Id referencedTableId;

    public Id getReferencedTableId() {
        return referencedTableId;
    }

    public void setReferencedTableId(final Id tabId) {
        if (!Utils.equals(this.referencedTableId, tabId)) {
            this.referencedTableId = tabId;
            setEditState(EEditState.MODIFIED);
        }
    }

    protected EntityRefParameterTag() {
        super();
    }

    public static final class Factory {

        private Factory() {
        }

        public static EntityRefParameterTag newInstance() {
            return new EntityRefParameterTag();
        }
    }
    public static final String ENTITY_REF_PARAMETER_TAG_TYPE_TITLE = "Entity Reference Parameter Tag";
    public static final String ENTITY_REF_PARAMETER_TAG_TYPES_TITLE = "Entity Reference Parameter Tags";

    @Override
    public String getTypeTitle() {
        return ENTITY_REF_PARAMETER_TAG_TYPE_TITLE;
    }

    @Override
    public String getTypesTitle() {
        return ENTITY_REF_PARAMETER_TAG_TYPES_TITLE;
    }

    @Override
    protected void appendAdditionalToolTip(StringBuilder sb) {
        super.appendAdditionalToolTip(sb);
        sb.append("<br>Table Id: " + String.valueOf(getReferencedTableId()));
    }

    @Override
    public void collectDependences(final List<Definition> list) {
        super.collectDependences(list);
        final Sqml sqml = getOwnerSqml();
        final DdsTableDef referencedTable = sqml.getEnvironment().findTableById(getReferencedTableId());
        if (referencedTable != null) {
            list.add(referencedTable);
        }
//        final IParameterDef parameter = findParameter();
//        if (parameter != null) {
//            final Definition definition = parameter.getDefinition();
//            if (definition != null) {
//                list.add(definition);
//            }
//        }
    }
}
