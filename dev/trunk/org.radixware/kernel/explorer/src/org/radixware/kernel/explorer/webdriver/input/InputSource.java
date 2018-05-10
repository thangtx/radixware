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

package org.radixware.kernel.explorer.webdriver.input;

import org.json.simple.JSONObject;
import org.radixware.kernel.explorer.webdriver.exceptions.EWebDrvErrorCode;
import org.radixware.kernel.explorer.webdriver.exceptions.WebDrvException;

abstract class InputSource {

    public static enum Type {
        NONE,KEY,POINTER;
        public static Type fromString(final String type) throws WebDrvException{
            switch(type){
                case "none":
                    return NONE;
                case "key":
                    return KEY;
                case "pointer":
                    return POINTER;
                default:
                    throw new WebDrvException(EWebDrvErrorCode.INVALID_ARGUMENT,"Unknown input device type \'"+type+"\'");
            }
        }
    }
    
    private final InputSourceId id;
    
    protected InputSource(final InputSourceId id){
        this.id = id;
    }
    
    public abstract InputAction parseAction(final JSONObject json) throws WebDrvException;
    
    public abstract InputSourceState getState();
}
