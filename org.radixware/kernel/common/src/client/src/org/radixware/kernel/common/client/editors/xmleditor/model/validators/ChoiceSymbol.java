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

import java.util.HashSet;
import java.util.Set;


final class ChoiceSymbol extends ValueToken<Character>{
    
    private final Set<Character> symbols = new HashSet<>();
    
    public ChoiceSymbol(final char[] smbSet, final boolean isMandatory){
        super(isMandatory);
        for (char symbol: smbSet){
            symbols.add(Character.valueOf(symbol));
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public ParsingResult<Character> parse (final Scaner scaner) {
        for (Character ch: symbols){
            if (scaner.isSymbol(ch)){
                scaner.moveCurrentPosition(1);
                return new ParsingResult<>(ch);
            }
        }
        if (isMandatory()){
            return tokenMissing(scaner);
        }else{
            return ParsingResult.NO_TOKEN;
        }
    }
}
