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
import org.radixware.kernel.explorer.webdriver.WebDrvJsonUtils;
import org.radixware.kernel.explorer.webdriver.exceptions.EWebDrvErrorCode;
import org.radixware.kernel.explorer.webdriver.exceptions.WebDrvException;

final class KeyInputSource extends InputSource{
    
    private final KeyInputSourceState state = new KeyInputSourceState();

    public KeyInputSource(final InputSourceId id){
        super(id);
    }

    @Override
    public InputAction parseAction(JSONObject actionItem) throws WebDrvException {
        final String type = WebDrvJsonUtils.getStringProperty(actionItem, "type");
        switch(type){
            case "pause":{
                return new NullAction<>(this, WebDrvJsonUtils.getNonNegativeInteger(actionItem, "duration", false));
            }
            case "keyUp":{
                final String value = WebDrvJsonUtils.getStringProperty(actionItem, "value");
                if (value.length()!=1){
                    throw new WebDrvException(EWebDrvErrorCode.INVALID_ARGUMENT, "The value property of keyUp action expected to be one character string");
                }
                return new KeyUpAction(this, value.charAt(0));
            }
            case "keyDown":{
                final String value = WebDrvJsonUtils.getStringProperty(actionItem, "value");
                if (value.length()!=1){
                    throw new WebDrvException(EWebDrvErrorCode.INVALID_ARGUMENT, "The value property of keyUp action expected to be one character string");
                }
                return new KeyDownAction(this, value.charAt(0));
            }default:{
                throw new WebDrvException(EWebDrvErrorCode.INVALID_ARGUMENT, "Unknown action type \'"+type+"\' for key input source");
            }         
        }

    }        

    @Override
    public KeyInputSourceState getState() {
        return state;
    }
}
