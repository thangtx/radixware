/*
* Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.kernel.common.client.utils;

import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EValType;


public interface IValAsStrConverter {        

    public final static IValAsStrConverter DEFAULT =  new IValAsStrConverter() {
        
        @Override
        public ValAsStr obj2ValAsStr(final Object obj, final EValType type) {
            return ValueConverter.obj2ValAsStr(obj, type);
        }

        @Override
        public Object valAsStr2Obj(final ValAsStr val, final EValType type) {
            return ValueConverter.valAsStr2Obj(val, type);
        }
    };
    
    ValAsStr obj2ValAsStr(final Object obj, final EValType type);
    
    Object valAsStr2Obj(final ValAsStr val, final EValType type);
    
}
