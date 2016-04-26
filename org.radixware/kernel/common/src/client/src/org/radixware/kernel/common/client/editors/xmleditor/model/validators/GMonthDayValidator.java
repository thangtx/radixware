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


public class GMonthDayValidator extends AbstractValidator {

    final static class GMonthDay extends ValueToken<Map<ValueToken, Object>> {

        private static final Symbols DOUBLE_MINUS_TOKEN = new Symbols(String.valueOf("--"), true);
        private static final Month MONTH_TOKEN = new Month(true);
        private static final Symbol MINUS_TOKEN = new Symbol('-', true);

        public GMonthDay(final boolean isMandatory) {
            super(isMandatory);
        }

        @Override
        @SuppressWarnings("unchecked")
        public ParsingResult<Map<ValueToken, Object>> parse(Scaner scaner) {
            final ParsingResult doubleMinysTokenResult = DOUBLE_MINUS_TOKEN.parse(scaner);
            while (doubleMinysTokenResult.getValue() == null) {
                if (doubleMinysTokenResult == ParsingResult.INVALID) {
                    return ParsingResult.INVALID;
                } else {
                    return ParsingResult.INCOMPLETE;
                }
            }
            final Map<ValueToken, Object> valuesByToken = new HashMap<>();
            valuesByToken.put(DOUBLE_MINUS_TOKEN, doubleMinysTokenResult.getValue());
            final ParsingResult<Integer> month = MONTH_TOKEN.parse(scaner);
            if (month == ParsingResult.INCOMPLETE) {
                return ParsingResult.INCOMPLETE;
            }
            if (month == ParsingResult.INVALID) {
                return ParsingResult.INVALID;
            }
            final ParsingResult minus = MINUS_TOKEN.parse(scaner);
            if (minus == ParsingResult.INVALID) {
                return ParsingResult.INVALID;
            }
            if (minus == ParsingResult.INCOMPLETE) {
                return ParsingResult.INCOMPLETE;
            }
            Day DAY_TOKEN;
            if (month.getValue() == 2) {
                DAY_TOKEN = new Day(28, true);
            } else if (month.getValue() == 4 || month.getValue() == 6 || month.getValue() == 9 || month.getValue() == 11) {
                DAY_TOKEN = new Day(30, true);
            } else {
                DAY_TOKEN = new Day(31, true);
            }
            final ParsingResult<Integer> day = DAY_TOKEN.parse(scaner);
            if (day == ParsingResult.INCOMPLETE) {
                return ParsingResult.INCOMPLETE;
            }
            if (day == ParsingResult.INVALID) {
                return ParsingResult.INVALID;
            }
            valuesByToken.put(MONTH_TOKEN, month.getValue());
            valuesByToken.put(MINUS_TOKEN, minus.getValue());
            valuesByToken.put(DAY_TOKEN, day.getValue());
            return new ParsingResult<>(valuesByToken);
        }
    }

    /* gMonthDay:
     --MM-DD
     --MM-DDZ
     --MM-DD+hh:mm
     --MM-DD-hh:mm 
     */
    public GMonthDayValidator() {
        super(new ValueToken[]{new GMonthDay(true), new TimeZone(false)});
    }
}