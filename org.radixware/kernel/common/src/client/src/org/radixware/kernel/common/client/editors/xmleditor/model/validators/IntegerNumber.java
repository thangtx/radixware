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


class IntegerNumber extends  ValueToken<Integer>{
    
    public IntegerNumber(final boolean isMandatory){
        super(isMandatory);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ParsingResult<Integer> parse(final Scaner scaner) {
        final String number = scaner.buildIntegerNumber();
        if (number.isEmpty()) {
            if (isMandatory()){
                return tokenMissing(scaner);
            }
            return ParsingResult.NO_TOKEN;
        }
        return new ParsingResult<>(Integer.parseInt(number));        
    }    
}
