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
 * Tag for inserting This table key into SQL. Can be translated in several different
 * way according to the PidTranslationMode attribute.
 */
public class ThisTableRefTag extends Sqml.Tag implements IEntityRefTag {

    protected ThisTableRefTag() {
        super();
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
    
    public static final class Factory {

        private Factory() {
        }

        public static ThisTableRefTag newInstance() {
            return new ThisTableRefTag();
        }
    }
    public static final String THIS_TABLE_REF_VALUE_TAG_TYPE_TITLE = "This Table Reference Value Tag";
    public static final String THIS_TABLE_REF_VALUE_TAG_TYPES_TITLE = "This Table Reference Value Tags";

    @Override
    public String getTypeTitle() {
        return THIS_TABLE_REF_VALUE_TAG_TYPE_TITLE;
    }

    @Override
    public String getTypesTitle() {
        return THIS_TABLE_REF_VALUE_TAG_TYPES_TITLE;
    }

}
