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

package org.radixware.kernel.common.client.meta.sqml;

import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.xscml.Sqml;


public interface ISqmlModifiableParameter extends ISqmlParameter {

    void setBaseProperty(ISqmlColumnDef column);

    void setValType(EValType valType, EditMask editMask, boolean isMandatory, String nullTitle);

    void setTitle(String title);

    void setEditMask(EditMask mask);

    void setNullString(String nullString);

    void setMandatory(boolean isMandatory);

    void setParentSelectorPresentation(final Id ownerClassId, final Id presentationId);
    
    void setParentSelectorAdditionalCondition(final Sqml condition);
    
    void setUseDropDownList(Boolean useDropDownList);
    
    Boolean getUseDropDownList();
    
    void setMinArrayItemsCount(final int count);
    
    void setMaxArrayItemsCount(final int count);

    void setInitialValue(ValAsStr value);

    void saveState();

    void restoreState();
}
