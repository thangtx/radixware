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

final class TimeoutValidator implements IJsonValueValidator{
    
    private final static TimeoutValidator INSTANCE = new TimeoutValidator();
    
    private TimeoutValidator(){        
    }
    
    public static TimeoutValidator getInstance(){
        return INSTANCE;
    }        

    @Override
    public boolean isValid(final Object value) {
        if (value!=null && value instanceof Long){
            final long timeout = (Long)value;
            return timeout>=0;
        }else{
            return false;
        }
    }

}
