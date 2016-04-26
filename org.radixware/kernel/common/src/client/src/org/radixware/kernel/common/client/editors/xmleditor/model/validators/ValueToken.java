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

import org.radixware.kernel.common.client.meta.mask.validators.EValidatorState;


abstract class ValueToken<T> {

    public static class ParsingResult<T> {

        public static final ParsingResult INCOMPLETE = new ParsingResult(EValidatorState.INTERMEDIATE);
        public static final ParsingResult INVALID = new ParsingResult(EValidatorState.INVALID);
        public static final ParsingResult NO_TOKEN = new ParsingResult((EValidatorState) null);
        private final T value;
        private final EValidatorState validatorState;

        public ParsingResult(final T val) {
            value = val;
            validatorState = EValidatorState.ACCEPTABLE;
        }

        private ParsingResult(final EValidatorState state) {
            value = null;
            validatorState = state;
        }

        public T getValue() {
            return value;
        }

        public EValidatorState getValidatorState() {
            return validatorState;
        }
    }
    private final boolean isMandatory;

    public ValueToken(final boolean mandatory) {
        isMandatory = mandatory;
    }

    public boolean isMandatory() {
        return isMandatory;
    }

    public abstract ParsingResult<T> parse(final Scaner scaner);

    @SuppressWarnings("unchecked")
    protected ParsingResult<T> tokenMissing(final Scaner scaner) {
        return scaner.atEnd() ? ParsingResult.INCOMPLETE : ParsingResult.INVALID;
    }
}