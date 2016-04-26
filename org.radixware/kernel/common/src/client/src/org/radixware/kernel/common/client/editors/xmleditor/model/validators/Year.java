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


final class Year extends ValueToken<Integer> {

    public Year(final boolean isMandatory) {
        super(isMandatory);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ParsingResult<Integer> parse(Scaner scaner) {
        final String yearAsStr = scaner.buildIntegerNumber();
        if (yearAsStr.isEmpty()) {
            if (isMandatory()) {
                return tokenMissing(scaner);
            }
            return ParsingResult.NO_TOKEN;
        }
        final int countNumYears = yearAsStr.length();
        if (countNumYears < 4) {
            return tokenMissing(scaner);
        }
        if ((countNumYears > 4 && yearAsStr.charAt(0) == '0')
                || "0000".equals(yearAsStr)) {
            return ParsingResult.INVALID;
        }
        final int years = Integer.parseInt(yearAsStr);
        return new ParsingResult<>(years);
    }
}
