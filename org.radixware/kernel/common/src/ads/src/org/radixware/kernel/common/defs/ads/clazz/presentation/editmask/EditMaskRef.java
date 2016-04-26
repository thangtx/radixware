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

package org.radixware.kernel.common.defs.ads.clazz.presentation.editmask;

import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.enums.EEditMaskType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.types.MapStrStr;


public class EditMaskRef extends EditMask {

    private Id selectorPresentationId;
    private List<Id> editorPresentationIds;
    private org.radixware.schemas.xscml.Sqml condition;
    private Id defaultFilterId;
    private boolean defaultFilterDefined;
    private Map<Id, Id> defaultSortingIdByFilterId;

    EditMaskRef(RadixObject context, boolean virtual) {
        super(context, virtual);
    }

    EditMaskRef(RadixObject context, org.radixware.schemas.editmask.EditMaskRef xDef, boolean virtual) {
        super(context, virtual);
    }

    @Override
    public void appendTo(org.radixware.schemas.editmask.EditMask xDef) {
        final org.radixware.schemas.editmask.EditMaskRef editMaskRef = xDef.addNewReference();
        if (condition != null) {
            editMaskRef.setCondition(condition);
        }
        if (defaultFilterId != null) {
            editMaskRef.setDefaultFilterId(defaultFilterId);
        }
        if (defaultSortingIdByFilterId != null) {
            MapStrStr sortingOnFilter = editMaskRef.addNewDefaultSortingIdByFilterId();
            for (Map.Entry<Id, Id> entry : defaultSortingIdByFilterId.entrySet()) {
                MapStrStr.Entry e = sortingOnFilter.addNewEntry();
                e.setKey(entry.getKey().toString());
                e.setValue(entry.getValue().toString());
            }
            editMaskRef.setDefaultSortingIdByFilterId(sortingOnFilter);
        }
        if (editorPresentationIds != null) {
            editMaskRef.setEditorPresentationIds(editorPresentationIds);
        }
        if (selectorPresentationId != null) {
            editMaskRef.setSelectorPresentationId(selectorPresentationId);
        }
        editMaskRef.setDefaultFilterDefined(defaultFilterDefined);
    }

    @Override
    public boolean isCompatible(EValType valType) {
        return valType == EValType.ARR_REF || valType == EValType.PARENT_REF;
    }

    @Override
    public EEditMaskType getType() {
        return EEditMaskType.OBJECT_REFERENCE;
    }

    @Override
    public void applyDbRestrictions() {
        //
    }
}
