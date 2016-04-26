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

package org.radixware.kernel.common.client.meta.mask;

import java.util.EnumSet;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.client.utils.ValueConverter;
import org.radixware.kernel.common.enums.EEditMaskType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Arr;
import org.radixware.kernel.common.types.Bin;

/**
 * Маска редактирования "как есть".
 * Создана для универсальности.
 * Используется для типов Bin(ArrBin),Blob(ArrBlob),Ref(ArrRef),Bool(ArrBool),Object,Xml
 */
public final class EditMaskNone extends org.radixware.kernel.common.client.meta.mask.EditMask {

    private static final EnumSet<EValType> SUPPORTED_VALTYPES =
            EnumSet.of(EValType.BIN, EValType.BLOB, EValType.BOOL, EValType.OBJECT, EValType.PARENT_REF,
            EValType.XML, EValType.ARR_BIN, EValType.ARR_BLOB, EValType.ARR_BOOL, EValType.ARR_REF);


    /* (non-Javadoc)
     * @see org.radixware.kernel.explorer.meta.mask.EditMask#toStr(java.lang.Object)
     */
    @Override
    public String toStr(IClientEnvironment environment, Object o) {
        //if (wasInherited()) return INHERITED_VALUE;
        if (o == null) {
            return getNoValueStr(environment.getMessageProvider());
        } else if (o instanceof Arr) {
            return arrToStr(environment,(Arr) o);
        } else if (o instanceof Bin) {
            return ValueConverter.arrByte2Str(((Bin) o).get(), " ");
        }
        return o.toString();
    }

    @Override
    public ValidationResult validate(final IClientEnvironment environment, final Object value) {
        return ValidationResult.ACCEPTABLE;
    }

    @Override
    public void writeToXml(org.radixware.schemas.editmask.EditMask editMask) {
        throw new AbstractMethodError("EditMaskNone cannot be stored into xml");
    }

    @Override
    public EEditMaskType getType() {
        return null;
    }

    @Override
    public EnumSet<EValType> getSupportedValueTypes() {
        return SUPPORTED_VALTYPES;
    }
}
