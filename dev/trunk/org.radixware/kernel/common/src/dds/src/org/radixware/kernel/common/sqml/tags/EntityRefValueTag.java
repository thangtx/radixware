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

import org.radixware.kernel.common.enums.EPidTranslationMode;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;

/**
 * Tag for inserting Entity key in SQL. Can be translated in several different
 * way according to the PidTranslationMode attribute.
 */
public class EntityRefValueTag extends Sqml.Tag {

    protected EntityRefValueTag() {
        super();
    }

    private String displayValue = "";

    /**
     * Get value displayed in editor.
     */
    public String getDisplayValue() {
        return displayValue;
    }

    public void setDisplayValue(String displayValue) {
        if (!Utils.equals(this.displayValue, displayValue)) {
            this.displayValue = displayValue;
            setEditState(EEditState.MODIFIED);
        }
    }

    private Id referencedTableId;

    /**
     * Referenced table ID.
     * @return
     */
    public Id getReferencedTableId() {
        return referencedTableId;
    }

    public void setReferencedTableId(final Id tableId) {
        if (!Utils.equals(this.referencedTableId, tableId)) {
            this.referencedTableId = tableId;
            setEditState(EEditState.MODIFIED);
        }
    }

    private String referencedPidAsStr;
    /**
     * Referenced object's PK as PID string (that does not include tableId)
     * @return
     */
    public String getReferencedPidAsStr() {
        return referencedPidAsStr;
    }
    public void setReferencedPidAsStr(final String pidAsStr) {
        if (!Utils.equals(this.referencedPidAsStr, pidAsStr)) {
            this.referencedPidAsStr = pidAsStr;
            setEditState(EEditState.MODIFIED);
        }
    }


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

    private boolean isLiteral = false;
    public void setLiteral(final boolean b) {
        isLiteral = b;
    }

    public boolean isLiteral() {
        return isLiteral;
    }
    
    public static final class Factory {

        private Factory() {
        }

        public static EntityRefValueTag newInstance() {
            return new EntityRefValueTag();
        }
    }
    public static final String ENTITY_REF_VALUE_TAG_TYPE_TITLE = "Entity Reference Value Tag";
    public static final String ENTITY_REF_VALUE_TAG_TYPES_TITLE = "Entity Reference Value Tags";

    @Override
    public String getTypeTitle() {
        return ENTITY_REF_VALUE_TAG_TYPE_TITLE;
    }

    @Override
    public String getTypesTitle() {
        return ENTITY_REF_VALUE_TAG_TYPES_TITLE;
    }

    @Override
    protected void appendAdditionalToolTip(StringBuilder sb) {
        super.appendAdditionalToolTip(sb);
        sb.append("<br>Table Id: " + String.valueOf(getReferencedTableId()));
    }
}
