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


final class NumberWithPostfix extends ValueToken<Character> {

    private final ChoiceSymbol postfixSmb;

    public NumberWithPostfix(final char[] possiblePostfix, final boolean isMandatory) {
        super(isMandatory);
        final char postfix[] = new char[possiblePostfix.length];
        System.arraycopy(possiblePostfix, 0, postfix, 0, possiblePostfix.length);
        postfixSmb = new ChoiceSymbol(postfix, true);        
    }

    @Override
    @SuppressWarnings("unchecked")
    public ParsingResult<Character> parse(final Scaner scaner) {
        final ParsingResult<Integer> number = new IntegerNumber(isMandatory()).parse(scaner);
        if (number == ParsingResult.INVALID) {
            return ParsingResult.INVALID;
        }
        if (number == ParsingResult.INCOMPLETE) {
            return ParsingResult.INCOMPLETE;
        }
        if (number == ParsingResult.NO_TOKEN) {
            return ParsingResult.NO_TOKEN;
        }
        return postfixSmb.parse(scaner);
    }
}
