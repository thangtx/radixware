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

package org.radixware.kernel.explorer.webdriver;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.radixware.kernel.explorer.webdriver.exceptions.EWebDrvErrorCode;
import org.radixware.kernel.explorer.webdriver.exceptions.InvalidPropertyValueTypeException;
import org.radixware.kernel.explorer.webdriver.exceptions.NullPropertyValueException;
import org.radixware.kernel.explorer.webdriver.exceptions.WebDrvException;

public class WebDrvJsonUtils {
    
    private WebDrvJsonUtils(){        
    }
    
    public static String getStringProperty(final JSONObject json, final String propName) throws WebDrvException{
        final Object value = json.get(propName);
        if (value==null){
            throw new NullPropertyValueException(propName);
        }
        if (value instanceof String==false){
            throw new InvalidPropertyValueTypeException(propName, value);
        }
        return (String)value;
    }    
    
    public static Integer getNonNegativeInteger(final JSONObject json, final String propName, final boolean mandatory) throws WebDrvException{
        final Object value = json.get(propName);
        if (value==null){
            if (mandatory){
                throw new NullPropertyValueException(propName);
            }else{
                return null;
            }
        }
        if (value instanceof Long==false){
            throw new InvalidPropertyValueTypeException(propName, value);
        }
        final long number = (Long)value;
        if (number<0 || number>Integer.MAX_VALUE){
            throw new WebDrvException(EWebDrvErrorCode.INVALID_ARGUMENT, "Value of "+propName+" is out of bounds");
        }
        return (int)number;
    }    
    
    public static JSONArray getArray(final JSONObject json, final String propName) throws WebDrvException{
        final Object value = json.get(propName);
        if (value==null){
            throw new NullPropertyValueException(propName);
        }
        if (value instanceof JSONArray==false){
            throw new InvalidPropertyValueTypeException(propName, value);
        }
        return (JSONArray)value;
        
    }

}
