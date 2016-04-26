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

import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.utils.Utils;

/**
 * Пользователь указывает цепочку исходящих связей от текущей таблицы.
 * Тэг оттранслируется в ссылку на указанную колонку + в несколько join'ов.
 * В DAC Designer'е тэг не вставляется (теоретически должен), пока что только в проводнике.
 */
public class ParentRefPropSqlNameTag extends Sqml.Tag {

    protected ParentRefPropSqlNameTag() {
        super();
    }
    private Id propId = null;

    public Id getPropId() {
        return propId;
    }

    public void setPropId(Id propId) {
        if (!Utils.equals(this.propId, propId)) {
            this.propId = propId;
            setEditState(EEditState.MODIFIED);
        }
    }
    private final List<Id> referenceIds = new ArrayList<Id>();

    public List<Id> getReferenceIds() {
        return referenceIds;
    }

    public static final class Factory {

        private Factory() {
        }

        public static ParentRefPropSqlNameTag newInstance() {
            return new ParentRefPropSqlNameTag();
        }
    }

    public static final String PARENT_REF_PROP_TAG_TYPE_TITLE = "Parent Reference Property Tag";
    public static final String PARENT_REF_PROP_TAG_TYPES_TITLE = "Parent Reference Property Tags";

    @Override
    public String getTypeTitle() {
        return PARENT_REF_PROP_TAG_TYPE_TITLE;
    }

    @Override
    public String getTypesTitle() {
        return PARENT_REF_PROP_TAG_TYPES_TITLE;
    }
}   
