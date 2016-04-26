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

import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.mask.validators.IInputValidator;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.client.editors.xmleditor.model.validators.ValueToken.ParsingResult;


class AbstractValidator implements IInputValidator{
    
    private final List<ValueToken> tokens = new LinkedList<>();
    
    public AbstractValidator(final ValueToken[] arrTokens){
        for (ValueToken token: arrTokens){
            tokens.add(token);
        }
        tokens.add(EndOfInput.INSTANCE);
    }

    @Override
    public final String fixup(String input) {
        return input;
    }

    @Override
    public final ValidationResult validate(IClientEnvironment environment, String input, int position) {
        final Scaner scaner = new Scaner(input);
        for (ValueToken token: tokens){
            final ParsingResult parsingResult = token.parse(scaner);
            if (parsingResult==ParsingResult.INVALID){
                return ValidationResult.INVALID;
            }
            if (parsingResult==ParsingResult.INCOMPLETE){
                return ValidationResult.INTERMEDIATE;
            }            
        }
        return ValidationResult.ACCEPTABLE;
    }     
}