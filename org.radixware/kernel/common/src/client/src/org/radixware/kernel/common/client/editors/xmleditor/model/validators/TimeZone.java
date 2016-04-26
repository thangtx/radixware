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

import java.util.HashMap;
import java.util.Map;


final class TimeZone extends ValueToken<Map<ValueToken, Object>> {

    private static final Symbol Z_TOKEN = new Symbol('Z', false);
    private static final ChoiceSymbol SIGN_TOKEN = new ChoiceSymbol(new char[]{'+', '-'}, false);
    private static final Hours HOURS_TOKEN = new Hours(14, true);
    private static final Symbol COLONE_TOKEN = new Symbol(':', true);
    private static final Minutes MINUTES_TOKEN = new Minutes(true);

    public TimeZone(final boolean isMandatory) {
        super(isMandatory);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ParsingResult<Map<ValueToken, Object>> parse(final Scaner scaner) {
        final Map<ValueToken, Object> valuesByToken = new HashMap<>();
        final ParsingResult zTokenResult = Z_TOKEN.parse(scaner);
        if (zTokenResult.getValue() != null) {
            valuesByToken.put(Z_TOKEN, zTokenResult.getValue());
            return new ParsingResult<>(valuesByToken);
        }
        final ParsingResult sign = SIGN_TOKEN.parse(scaner);
        if (sign.getValue() != null) {
            valuesByToken.put(SIGN_TOKEN, sign.getValue());
            final ParsingResult<Integer> hours = HOURS_TOKEN.parse(scaner);
            if (hours == ParsingResult.INVALID) {
                return ParsingResult.INVALID;
            }
            if (hours == ParsingResult.INCOMPLETE) {
                return ParsingResult.INCOMPLETE;
            }
            if (hours.getValue() > 14) {
                return ParsingResult.INVALID;
            }
            final ParsingResult colone = COLONE_TOKEN.parse(scaner);
            if (colone.getValue() == null) {
                return colone;
            }
            final ParsingResult<Integer> minutes = MINUTES_TOKEN.parse(scaner);
            if (minutes == ParsingResult.INVALID) {
                return ParsingResult.INVALID;
            }
            if (minutes == ParsingResult.INCOMPLETE) {
                return ParsingResult.INCOMPLETE;
            }
            if (hours.getValue() == 14 && minutes.getValue() > 0) {
                return ParsingResult.INVALID;
            }
            valuesByToken.put(HOURS_TOKEN, hours.getValue());
            valuesByToken.put(COLONE_TOKEN, colone.getValue());
            valuesByToken.put(MINUTES_TOKEN, minutes.getValue());
            return new ParsingResult<>(valuesByToken);
        }
        if (isMandatory()) {
            return tokenMissing(scaner);
        } else {
            return ParsingResult.NO_TOKEN;
        }
    }
}