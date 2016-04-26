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
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.mask.validators.EValidatorState;
import org.radixware.kernel.common.client.meta.mask.validators.IInputValidator;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;


public class DurationValidator implements IInputValidator {

    /* duration:
     PnYnMnDTnHnMnS
     */
    private final static Symbol MINUS = new Symbol('-', false);
    private final static char[] TIME_CHARS = {'H', 'M', 'S'};
    private final static char[] DATE_CHARS = {'Y', 'M', 'D'};
    private final static Symbol PREFIX = new Symbol('P', true);
    private final static NumberWithPostfix DATE_TOKEN = new NumberWithPostfix(DATE_CHARS, false);
    private final static Symbol TIME_PREFIX = new Symbol('T', false);
    private final static DecimalNumber TIME_VALUE = new DecimalNumber(false);
    private final static ChoiceSymbol TIME_UNIT = new ChoiceSymbol(TIME_CHARS, true);
    private final static Symbol SECONDS_UNIT = new Symbol('S', true);

    @Override
    public String fixup(final String input) {
        return input;
    }

    @Override
    public ValidationResult validate(final IClientEnvironment environment, final String input, final int position) {
        final Scaner scaner = new Scaner(input);
        final ValueToken.ParsingResult<Character> minus = MINUS.parse(scaner);
        if (minus == ValueToken.ParsingResult.INVALID) {
            return ValidationResult.INVALID;
        }
        final ValueToken.ParsingResult<Character> prefix = PREFIX.parse(scaner);
        if (prefix.getValidatorState() == EValidatorState.ACCEPTABLE) {
            int dateTokenIndex = 0;
            while (dateTokenIndex < DATE_CHARS.length) {
                ValueToken.ParsingResult<Character> dateToken = DATE_TOKEN.parse(scaner);
                if (dateToken == ValueToken.ParsingResult.NO_TOKEN) {
                    break;
                } else if (dateToken.getValue() == null) {
                    return getValidationResult(dateToken);
                } else {
                    int tokenIndex = findArrayItemIndex(DATE_CHARS, dateToken.getValue().charValue());
                    if (dateTokenIndex > tokenIndex) {
                        return ValidationResult.INVALID;
                    }
                    dateTokenIndex = tokenIndex + 1;
                }
            }
            final ValueToken.ParsingResult<Character> timePrefix = TIME_PREFIX.parse(scaner);
            if (timePrefix.getValidatorState() == EValidatorState.ACCEPTABLE) {
                return validateTimePart(scaner);
            } else {
                if (timePrefix == ValueToken.ParsingResult.INVALID) {
                    return ValidationResult.INVALID;
                } else if (dateTokenIndex > 0) {
                    return getValidationResult(EndOfInput.INSTANCE.parse(scaner));
                } else {
                    return scaner.atEnd() ? ValidationResult.INTERMEDIATE : ValidationResult.INVALID;
                }
            }
        } else {
            return getValidationResult(prefix);
        }
    }

    private static ValidationResult validateTimePart(final Scaner scaner) {
        int timeTokenIndex = 0;
        while (timeTokenIndex < 3) {
            final ValueToken.ParsingResult<BigDecimal> timeValue = TIME_VALUE.parse(scaner);
            if (timeValue == ValueToken.ParsingResult.NO_TOKEN) {
                if (timeTokenIndex == 0) {
                    return ValidationResult.INTERMEDIATE;
                } else {
                    return getValidationResult(EndOfInput.INSTANCE.parse(scaner));
                }
            } else if (timeValue.getValue() == null) {
                return getValidationResult(timeValue);
            } else {
                if (timeValue.getValue().scale() > 0) {
                    final ValueToken.ParsingResult sSymbol = SECONDS_UNIT.parse(scaner);
                    if (sSymbol.getValue() == null) {
                        return getValidationResult(sSymbol);
                    }
                    break;
                } else {
                    ValueToken.ParsingResult<Character> timeUnit = TIME_UNIT.parse(scaner);
                    if (timeUnit.getValue() == null) {
                        return getValidationResult(timeUnit);
                    } else {
                        final int unitIndex = findArrayItemIndex(TIME_CHARS, timeUnit.getValue().charValue());
                        if (timeTokenIndex > unitIndex) {
                            return ValidationResult.INVALID;
                        }
                        if (scaner.atEnd()){
                            break;
                        }
                        timeTokenIndex = unitIndex + 1;
                    }
                }
            }
        }//while (timeTokenIndex<3)
        return getValidationResult(EndOfInput.INSTANCE.parse(scaner));
    }

    private static ValidationResult getValidationResult(ValueToken.ParsingResult parsingResult) {
        if (parsingResult == ValueToken.ParsingResult.INVALID) {
            return ValidationResult.INVALID;
        } else if (parsingResult == ValueToken.ParsingResult.INCOMPLETE) {
            return ValidationResult.INTERMEDIATE;
        } else {
            return ValidationResult.ACCEPTABLE;
        }
    }

    private static int findArrayItemIndex(final char[] array, final char arrayItem) {
        for (int i = array.length - 1; i >= 0; i--) {
            if (array[i] == arrayItem) {
                return i;
            }
        }
        return -1;
    }
}