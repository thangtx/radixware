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


abstract class TwoDigitNumber extends ValueToken<Integer> {

    private final int minValue;
    private final int maxValue;

    public TwoDigitNumber(final int min, final int max, final boolean isMandatory) {
        super(isMandatory);
        minValue = min;
        maxValue = max;
    }

    @Override
    @SuppressWarnings("unchecked")
    public final ParsingResult<Integer> parse(Scaner scaner) {
        final String numAsStr = scaner.buildIntegerNumber();
        if (numAsStr.isEmpty()) {
            if (isMandatory()) {
                return tokenMissing(scaner);
            }
            return ParsingResult.NO_TOKEN;
        }
        final int digitsCount = numAsStr.length();
        final int value = Integer.parseInt(numAsStr);
        if (value < minValue || value > maxValue || digitsCount < 2) {
            if (digitsCount < 2 && value <=  (maxValue / 10)) {
                return tokenMissing(scaner);
            }
            return ParsingResult.INVALID;
        } else if (digitsCount > 2) {
            return ParsingResult.INVALID;
        }
        return new ParsingResult<>(value);
    }
}