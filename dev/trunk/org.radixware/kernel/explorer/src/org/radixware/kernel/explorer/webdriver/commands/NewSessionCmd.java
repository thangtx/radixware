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

import com.trolltech.qt.gui.QApplication;
import org.json.simple.JSONObject;
import org.radixware.kernel.explorer.webdriver.EHttpMethod;
import org.radixware.kernel.explorer.webdriver.exceptions.EWebDrvErrorCode;
import org.radixware.kernel.explorer.webdriver.WebDrvCapabilities;
import org.radixware.kernel.explorer.webdriver.WebDrvServer;
import org.radixware.kernel.explorer.webdriver.exceptions.WebDrvException;
import org.radixware.kernel.explorer.webdriver.WebDrvSession;
import org.radixware.kernel.explorer.webdriver.WebDrvSessionsManager;

final class NewSessionCmd implements IWebDrvServiceCommand {

    public NewSessionCmd() {
    }

    @Override
    public String getName() {
        return "session";
    }

    @Override
    public String getGroupName() {
        return null;
    }

    @Override
    public EHttpMethod getHttpMethod() {
        return EHttpMethod.POST;
    }

    @Override
    public JSONObject execute(final JSONObject parameter) throws WebDrvException {
        return this.executeWithOptionalContext(parameter, null);
    }

    @SuppressWarnings("unchecked")
    public JSONObject executeWithOptionalContext(final JSONObject parameter, WebDrvCommandExecContext context) throws WebDrvException {
        final WebDrvCapabilities capabilities = WebDrvCapabilities.parse(parameter);
        JSONObject jo = new JSONObject();
        capabilities.writeToJson(jo);
        if (capabilities == null) {
            throw new WebDrvException(EWebDrvErrorCode.SESSION_NOT_CREATED, "capabilities does not match");
        }
        final WebDrvSession session = WebDrvSessionsManager.getInstance().createSession(capabilities, context.getTimeReceive());

        final JSONObject value = new JSONObject();
        session.writeToJson(value);
        final JSONObject result = new JSONObject();
        result.put("value", value);
        if (context != null) {
            WebDrvServer.traceEvent(
                    QApplication.translate("WebDriver", "WebDriver session '%1$s' was started for client at '%2$s' host"),
                     session.getId().toString(),
                     context.getRemoteHostString());
        }
        return result;
    }

}
