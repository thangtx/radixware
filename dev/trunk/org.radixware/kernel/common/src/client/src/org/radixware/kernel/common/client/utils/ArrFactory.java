/*
* Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
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

import org.radixware.kernel.common.client.types.ArrRef;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.types.*;


public interface ArrFactory {
    
    public final static ArrFactory DEFAULT = new DefaultFactory();
    
    final static class DefaultFactory implements ArrFactory{
        @Override
        public Arr createArray(final EValType type) {
            switch (type) {
                case ARR_BIN:
                case BIN:
                case ARR_BLOB:
                case BLOB:
                    return new ArrBin();
                case ARR_BOOL:
                case BOOL:
                    return new ArrBool();
                case ARR_CHAR:
                case CHAR:
                    return new ArrChar();
                case ARR_DATE_TIME:
                case DATE_TIME:
                    return new ArrDateTime();
                case ARR_INT:
                case INT:
                    return new ArrInt();
                case ARR_NUM:
                case NUM:
                    return new ArrNum();
                case ARR_REF:
                case PARENT_REF:
                    return new ArrRef();
                case ARR_STR:
                case STR:
                case ARR_CLOB:
                case CLOB:
                    return new ArrStr();
                default:
                    throw new IllegalUsageError("Can't create array of \'" + type.getName() + "\' type");            
            }
        }
        
    }

    Arr createArray(final EValType type);
    
}
