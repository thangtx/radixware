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

package org.radixware.kernel.explorer.webdriver.commands;

import org.json.simple.JSONObject;
import org.radixware.kernel.explorer.webdriver.EHttpMethod;
import org.radixware.kernel.explorer.webdriver.WebDrvSessionsManager;

final class StatusCmd implements IWebDrvServiceCommand{

    @Override
    public String getName() {
        return "status";
    }
    
    @Override
    public String getGroupName() {
        return null;
    }    

    @Override
    public EHttpMethod getHttpMethod() {
        return EHttpMethod.GET;
    }

    @Override
    @SuppressWarnings("unchecked")
    public JSONObject execute(final JSONObject parameter) {
        final JSONObject result = new JSONObject();
        final int sessionCount = WebDrvSessionsManager.getInstance().getSessionCount();
        final int sessionLimit = WebDrvSessionsManager.getInstance().getSessionLimit();
        final boolean canCreate = sessionCount<sessionLimit;
        result.put("ready", canCreate);
        if (canCreate){
            result.put("message", "Explorer WebDriver is ready to open new session");
        }else{
            result.put("message", String.format("Maximum number of sessions (%1$s) exceeded", String.valueOf(sessionLimit)));
        }
        return result;
    }
}
