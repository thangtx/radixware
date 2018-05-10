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

import java.util.ArrayList;
import java.util.logging.Level;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.explorer.env.trace.ExplorerTraceItem;
import org.radixware.kernel.explorer.webdriver.EHttpMethod;
import org.radixware.kernel.explorer.webdriver.WebDrvSession;
import org.radixware.kernel.explorer.webdriver.WebDrvSessionCommandResult;
import org.radixware.kernel.explorer.webdriver.WebDrvUri;
import org.radixware.kernel.explorer.webdriver.exceptions.EWebDrvErrorCode;
import org.radixware.kernel.explorer.webdriver.exceptions.WebDrvException;


public class GetLogCmd extends GuiCmd {

    public static final String NAME = "getLogCmd";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getGroupName() {
        return null;
    }

    @Override
    public EHttpMethod getHttpMethod() {
        //return EHttpMethod.GET;
        return EHttpMethod.POST; // 20180213 1347 Котрачев. Почемуто клиент селениума шлёт POST.
    }

    @Override
    @SuppressWarnings("unchecked")
    public JSONObject execute(final WebDrvSession session, final WebDrvUri uri, final JSONObject parameter) throws WebDrvException {
        if (parameter.get("type").equals(GetAvailableLogTypesCmd.LOG_TYPE)) {
            ArrayList lst = (ArrayList) session.getEnvironment().getTracer().getBuffer().asList();

            JSONArray ja = new JSONArray();
            
            for (Object itemObj : lst) {
                ExplorerTraceItem item = (ExplorerTraceItem) itemObj;
                if(item.getTime() >= session.getTimeStart()) {
                    JSONObject ji = new JSONObject();
                    ji.put("message", item.getMessageText());
                    ji.put("level", sevirityToString(item.getSeverity()));
                    ji.put("timestamp", (Long) item.getTime());
                    ja.add(ji);
                }
            }

            return new WebDrvSessionCommandResult(ja);
        }
        throw new WebDrvException(EWebDrvErrorCode.INVALID_ARGUMENT, String.format("parameter 'type' must be '%s'", GetAvailableLogTypesCmd.LOG_TYPE));

    }

    private static String sevirityToString(EEventSeverity s) {
        if (s == EEventSeverity.ALARM) {
            return Level.SEVERE.getName();
        }
        if (s == EEventSeverity.DEBUG) {
            return Level.FINE.getName();
        }
        if (s == EEventSeverity.EVENT) {
            return Level.INFO.getName();
        }
        if (s == EEventSeverity.ERROR) {
            return Level.SEVERE.getName();
        }
        if (s == EEventSeverity.WARNING) {
            return Level.WARNING.getName();
        }
        if (s == EEventSeverity.NONE) {
            return Level.ALL.getName();
        }
        return Level.OFF.getName();
    }

}
