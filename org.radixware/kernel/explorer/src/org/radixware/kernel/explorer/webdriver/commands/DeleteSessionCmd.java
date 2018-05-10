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
import org.radixware.kernel.explorer.webdriver.WebDrvServer;
import org.radixware.kernel.explorer.webdriver.exceptions.WebDrvException;
import org.radixware.kernel.explorer.webdriver.WebDrvSession;
import org.radixware.kernel.explorer.webdriver.WebDrvSessionsManager;
import org.radixware.kernel.explorer.webdriver.WebDrvUri;

final class DeleteSessionCmd implements IWebDrvSessionCommand{
    
    public DeleteSessionCmd(){        
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
        return EHttpMethod.DELETE;
    }

    @Override
    public boolean isGuiCommand() {
        return false;
    }        

    @Override
    public boolean isInputActionCommand() {
        return false;
    }        

    @Override
    public JSONObject execute(final WebDrvSession session, final WebDrvUri uri, final JSONObject parameter) throws WebDrvException {
        return executeWithOptionalContext(session, uri, parameter, null);
    }
    
    @SuppressWarnings("unchecked")
    public JSONObject executeWithOptionalContext(final WebDrvSession session, final WebDrvUri uri, final JSONObject parameter, WebDrvCommandExecContext context) throws WebDrvException {
        WebDrvSessionsManager.getInstance().deleteSession(session);
        if(context != null) {
            WebDrvServer.traceEvent(
                    QApplication.translate("WebDriver", "WebDriver session '%1$s' was terminated for client at %2$s host"),
                     session.getId().toString(),
                     context.getRemoteHostString());
        }
        return null;
    }
}
