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

package org.radixware.kernel.common.client.meta.mask.validators;

import java.math.BigDecimal;
import java.text.NumberFormat;


public class ValidatorsFactory {

    public static IInputValidator createBigDecimalValidator(final BigDecimal min, final BigDecimal max, final int precision) {
        return new BigDecimalValidator(min, max, precision);
    }
    
    public static IInputValidator createBigDecimalValidator(final BigDecimal min, final BigDecimal max, final long scale, final NumberFormat numberFormat, final int precision) {
        return new BigDecimalValidator(min, max, scale, numberFormat, precision);
    }
    
    public static IInputValidator createRegExpValidator(final String regex, final boolean caseSencitive) {
        return new RegExpValidator(regex, caseSencitive);
    }

    public static IInputValidator createLongValidator(final Long min, final Long max, final Byte radix) {
        return new LongValidator(min, max, radix);
    }

    public static IInputValidator createLongValidator(final Long min, final Long max) {
        return createLongValidator(min, max, null);
    }

    public static IInputValidator loadFromXml(final org.radixware.schemas.editmask.EditMaskStr editMaskStr) {
        final int validatorTypeValue = editMaskStr.getValidatorType();
        switch (validatorTypeValue) {
            case 1:
                return LongValidator.loadFromXml(editMaskStr);
            case 2:
                return BigDecimalValidator.loadFromXml(editMaskStr);
            case 3:
                return RegExpValidator.loadFromXml(editMaskStr);
            default:
                return null;
        }
    }

    public static boolean appendToXml(final IInputValidator validator, final org.radixware.schemas.editmask.EditMaskStr editMaskStr) {
        if (validator instanceof LongValidator) {
            editMaskStr.setValidatorType(1);
            ((LongValidator) validator).appendToXml(editMaskStr);
            return true;
        } else if (validator instanceof BigDecimalValidator) {
            editMaskStr.setValidatorType(2);
            ((BigDecimalValidator) validator).appendToXml(editMaskStr);
            return true;
        } else if (validator instanceof RegExpValidator) {
            editMaskStr.setValidatorType(3);
            ((RegExpValidator) validator).appendToXml(editMaskStr);
            return true;
        }
        return false;
    }
}
