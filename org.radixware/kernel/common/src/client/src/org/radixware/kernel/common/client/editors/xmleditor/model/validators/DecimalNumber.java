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

package org.radixware.kernel.common.client.editors.xmleditor.model.validators;

import java.math.BigDecimal;


class DecimalNumber extends ValueToken<BigDecimal> {

    public DecimalNumber(final boolean isMandatory) {
        super(isMandatory);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ParsingResult<BigDecimal> parse(Scaner scaner) {
        final String number = scaner.buildDecimalNumber();
        if (number.isEmpty()) {
            return tokenMissing(scaner);
        }
        if (number.charAt(0) == '.') {
            return ParsingResult.INVALID;
        }
        if (number.charAt(number.length() - 1) == '.') {
            return ParsingResult.INCOMPLETE;
        }
        return new ParsingResult<>(new BigDecimal(number));
    }
}