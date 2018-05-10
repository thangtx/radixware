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

package org.radixware.kernel.explorer.webdriver.exceptions;

import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONObject;

public final class WebDrvUnexpectedAlertOpenException extends WebDrvException{
    
    private static final long serialVersionUID = -7970614215769819418L;
    private final String title;
    private final String message;

    public WebDrvUnexpectedAlertOpenException(final String title, final String message){
        super(EWebDrvErrorCode.UNEXPECTED_ALERT_OPEN);
        this.title = title;
        this.message = message;
    }

    @Override
    public String getHttpBody() {
        final Map<String,Object> body = new HashMap<>();
        body.put("error", EWebDrvErrorCode.UNEXPECTED_ALERT_OPEN.getJsonError());
        body.put("message", EWebDrvErrorCode.UNEXPECTED_ALERT_OPEN.getDescription());
        final Map<String,String> data = new HashMap<>();
        data.put("title", title);
        data.put("text", message);
        body.put("data", data);
        return JSONObject.toJSONString(body);
    }  
}
