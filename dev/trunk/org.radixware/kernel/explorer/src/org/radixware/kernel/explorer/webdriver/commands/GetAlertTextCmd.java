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

import com.trolltech.qt.gui.QDialog;
import org.json.simple.JSONObject;
import org.radixware.kernel.explorer.webdriver.EHttpMethod;
import org.radixware.kernel.explorer.webdriver.WebDriverUserPromptManager;
import org.radixware.kernel.explorer.webdriver.WebDrvSession;
import org.radixware.kernel.explorer.webdriver.WebDrvSessionCommandResult;
import org.radixware.kernel.explorer.webdriver.WebDrvUri;
import org.radixware.kernel.explorer.webdriver.exceptions.EWebDrvErrorCode;
import org.radixware.kernel.explorer.webdriver.exceptions.WebDrvException;

public class GetAlertTextCmd extends GuiCmd{

    @Override
    public String getName() {
        return "text";
    }

    @Override
    public String getGroupName() {
        return "alert";
    }

    @Override
    public EHttpMethod getHttpMethod() {
        return EHttpMethod.GET;
    }

    @Override
    @SuppressWarnings("unchecked")
    public JSONObject execute(final WebDrvSession session, final WebDrvUri uri, final JSONObject parameter) throws WebDrvException {
        final QDialog dialog = 
            WebDriverUserPromptManager.getInstance().findPromptDialog();        
        if (dialog==null){
            throw new WebDrvException(EWebDrvErrorCode.NO_SUCH_ALERT);
        }
        final String message = 
            WebDriverUserPromptManager.getInstance().getMessageText(dialog);
        final JSONObject result = new WebDrvSessionCommandResult(message);
        result.put("message", message);
        return result;
    }
}
