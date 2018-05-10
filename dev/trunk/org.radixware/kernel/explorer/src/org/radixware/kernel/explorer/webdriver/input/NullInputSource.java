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

final class NullInputSource extends InputSource{        
    
    private final static InputSourceState STATE = new InputSourceState(){};

    public NullInputSource(final InputSourceId id){
        super(id);
    }

    @Override
    public InputAction parseAction(final JSONObject json) throws WebDrvException {
        final String type = WebDrvJsonUtils.getStringProperty(json, "type");
        if ("pause".equals(type)){
            return new NullAction<>(this,WebDrvJsonUtils.getNonNegativeInteger(json, "duration", false));
        }else{
            throw new WebDrvException(EWebDrvErrorCode.INVALID_ARGUMENT, "Unknown action type \'"+type+"\'for null input source");
        }
    }

    @Override
    public InputSourceState getState() {
        return STATE;
    }        
}
