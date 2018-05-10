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
import org.radixware.kernel.explorer.webdriver.WebDriverUserPromptManager;
import org.radixware.kernel.explorer.webdriver.WebDrvCapabilities;
import org.radixware.kernel.explorer.webdriver.WebDrvSession;
import org.radixware.kernel.explorer.webdriver.WebDrvUri;
import org.radixware.kernel.explorer.webdriver.exceptions.EWebDrvErrorCode;
import org.radixware.kernel.explorer.webdriver.exceptions.WebDrvException;

public class DismissAlertCmd extends GuiCmd{

    @Override
    public String getName() {
        return "dismiss";
    }

    @Override
    public String getGroupName() {
        return "alert";
    }

    @Override
    public EHttpMethod getHttpMethod() {
        return EHttpMethod.POST;
    }
    
    @Override
    public boolean isInputActionCommand() {
        return true;
    }    

    @Override
    public JSONObject execute(final WebDrvSession session, final WebDrvUri uri, final JSONObject parameter) throws WebDrvException {
        if (!WebDriverUserPromptManager.getInstance().handleUserPrompt(WebDrvCapabilities.EPromptAction.DISMISS)){
            throw new WebDrvException(EWebDrvErrorCode.NO_SUCH_ALERT);
        }
        return null;
    }
    
    

}
