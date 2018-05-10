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

package org.radixware.kernel.common.sqml.tags;

/**
 * Tag - count of values in parameter.
 * If parameter value is null then the result will be -1.
 * For array value the result will be number of items. In other cases the result will be 1.
 */
public final class ParamValCountTag extends ParameterAbstractTag{
    
    public static final class Factory {

        private Factory() {
        }

        public static ParamValCountTag newInstance() {
            return new ParamValCountTag();
        }
    }      
    
    protected ParamValCountTag(){
        super();
    }

}
