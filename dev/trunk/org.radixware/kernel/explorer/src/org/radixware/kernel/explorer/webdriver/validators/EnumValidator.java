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

package org.radixware.kernel.explorer.webdriver.validators;

import java.util.Arrays;
import java.util.List;

public class EnumValidator implements IJsonValueValidator{
    
    private final List<String> values;
    private final boolean lowerCase;
    
    private EnumValidator(final String[] vals, final boolean lowerCase){
        values = Arrays.asList(vals);
        this.lowerCase = lowerCase;
    }
    
    public static EnumValidator getInstance(final String[] vals, final boolean lowerCase){
        return new EnumValidator(vals,lowerCase);
    }        

    @Override
    public boolean isValid(final Object value) {
        return value!=null && value instanceof String && values.contains(lowerCase ? ((String)value).toLowerCase() : value);
    }
}