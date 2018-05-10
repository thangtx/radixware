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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.radixware.kernel.explorer.webdriver.EHttpMethod;
import org.radixware.kernel.explorer.webdriver.WebDrvSession;
import org.radixware.kernel.explorer.webdriver.WebDrvSession.OpenAndSaveDialogHandler;
import org.radixware.kernel.explorer.webdriver.WebDrvSessionCommandResult;
import org.radixware.kernel.explorer.webdriver.WebDrvUri;
import org.radixware.kernel.explorer.webdriver.elements.UIElementId;
import org.radixware.kernel.explorer.webdriver.elements.UIElementReference;
import org.radixware.kernel.explorer.webdriver.exceptions.EWebDrvErrorCode;
import org.radixware.kernel.explorer.webdriver.exceptions.WebDrvException;

final class ExecuteScriptCmd extends GuiCmd {

    // adamantine kludge
    private final static String GET_PAGE_SOURCE_SCRIPT = "var source = document.documentElement.outerHTML; \nif (!source) { source = new XMLSerializer().serializeToString(document); }\nreturn source;";
    private final static String GET_LOCAL_STORAGE_KEYS_SCRIPT = "return Object.keys(localStorage)";
    private final static String SET_LOCAL_STORAGE_ITEM_SCRIPT = "localStorage.setItem('arguments[0]', 'arguments[1]')";
    private final static String GET_LOCAL_STORAGE_ITEM_SCRIPT = "return localStorage.getItem('arguments[0]')";
    private final static String CLEAR_LOCAL_STORAGE_SCRIPT = "localStorage.clear()";
    private final static String REMOVE_LOCAL_STORAGE_ITEM_SCRIPT = "localStorage.removeItem('arguments[0]')";
    private final static String GET_LOCAL_STORAGE_SIZE_SCRIPT = "return localStorage.length";
    private final static String GET_ELEMENT_LOCATION_ONCE_SCROLLED_INTO_VIEW_SCRIPT = "var e = arguments[0]; e.scrollIntoView({behavior: 'instant', block: 'end', inline: 'nearest'}); var rect = e.getBoundingClientRect(); return {'x': rect.left, 'y': rect.top};";
    private final static String REMOVE_ALL_DIALOG_HANDLER_CMD = "QFileDialog.removeAllDialogHandlers";
    private final static String REMOVE_DIALOG_HANDLER_BY_GUID_CMD = "QFileDialog.removeDialogHandler";
    private final static String INSTALL_DIALOG_HANDLER_CMD = "QFileDialog.installDialogHandler";
    private final static String SUCCESSFUL_RESULT = "SUCCESSFUL";

    /**
     * IS_ELEMENT_DISPLAYED_SCRIPT_HASH и IS_ELEMENT_DISPLAYED_SCRIPT_LENGTH
     * получены так: запуск в отладчике эксплорера, посылание эксплореру из
     * selenium клиента команды методом RemoteWebElement.isDisplayed, потом в
     * отладчике смотрим хеш и длину пришедшей строки. Версия клиента была 3.9.1
     * (2018.03) selenium.jar\org\openqa\selenium\remote\isDisplayed.js
     */
    private static int IS_ELEMENT_DISPLAYED_SCRIPT_HASH = -2087367150;
    private static int IS_ELEMENT_DISPLAYED_SCRIPT_LENGTH = 44046;

    // selenium.jar\org\openqa\selenium\remote\getAttribute.js
    private static int IS_GET_ELEMENT_ATTRIBUTE_SCRIPT_HASH = 819060784;
    private static int IS_GET_ELEMENT_ATTRIBUTE_SCRIPT_LENGTH = 6314;

    @Override
    public String getName() {
        return "sync";
    }

    @Override
    public String getGroupName() {
        return "execute";
    }

    @Override
    public EHttpMethod getHttpMethod() {
        return EHttpMethod.POST;
    }

    @Override
    public boolean isInputActionCommand() {
        return true;//in case of GET_ELEMENT_LOCATION_ONCE_SCROLLED_INTO_VIEW_SCRIPT
    }

    @Override
    public JSONObject execute(final WebDrvSession session, final WebDrvUri uri, final JSONObject parameter) throws WebDrvException {
        super.execute(session, uri, parameter);
        String script = (String) parameter.get("script");
        if (script.equals(GET_PAGE_SOURCE_SCRIPT)) {
            return new WebDrvSessionCommandResult(session.getPageSource());
        }

        JSONArray args = (JSONArray) parameter.get("args");
        if (script.equals(GET_LOCAL_STORAGE_KEYS_SCRIPT)) {
            return new WebDrvSessionCommandResult(session.getEnvironment().getConfigStore().allKeys());
        }

        if (script.equals(GET_LOCAL_STORAGE_ITEM_SCRIPT)) {
            String key = (String) args.get(0);
            return new WebDrvSessionCommandResult(session.getEnvironment().getConfigStore().readString(key));
        }

        if (script.equals(CLEAR_LOCAL_STORAGE_SCRIPT)) {
            session.getEnvironment().getConfigStore().remove("");
            return null;
        }
        if (script.equals(REMOVE_LOCAL_STORAGE_ITEM_SCRIPT)) {
            String key = (String) args.get(0);
            session.getEnvironment().getConfigStore().remove(key);
            return null;
        }

        if (script.equals(SET_LOCAL_STORAGE_ITEM_SCRIPT)) {
            String key = (String) args.get(0);
            String val = (String) args.get(1);
            session.getEnvironment().getConfigStore().writeString(key, val);
            return null;
        }

        if (script.equals(GET_LOCAL_STORAGE_SIZE_SCRIPT)) {
            return new WebDrvSessionCommandResult(session.getEnvironment().getConfigStore().allKeys().size());
        }

        if (script.equals(GET_ELEMENT_LOCATION_ONCE_SCROLLED_INTO_VIEW_SCRIPT)) {
            JSONObject jo = (JSONObject) args.get(0);
            String id = (String) jo.get(UIElementId.JSON_KEY);
            UIElementId eid = UIElementId.fromString(id);
            final UIElementReference ref = session.getUIElements().getUIElementReference(eid);
            ref.scrollToView();
            return qRectToJSON(ref.getElementRect());
        }

        if (script.length() == IS_ELEMENT_DISPLAYED_SCRIPT_LENGTH
                && script.hashCode() == IS_ELEMENT_DISPLAYED_SCRIPT_HASH) {
            JSONObject jo = (JSONObject) args.get(0);
            String id = (String) jo.get(UIElementId.JSON_KEY);
            UIElementId eid = UIElementId.fromString(id);
            final UIElementReference ref = session.getUIElements().getUIElementReference(eid);
            return new WebDrvSessionCommandResult(ref.isDisplayed());
        }

        if (script.length() == IS_GET_ELEMENT_ATTRIBUTE_SCRIPT_LENGTH
                && script.hashCode() == IS_GET_ELEMENT_ATTRIBUTE_SCRIPT_HASH) {
            final Object firstArg = args.get(0);
            if (firstArg instanceof JSONObject == false) {
                throw new WebDrvException(EWebDrvErrorCode.INVALID_ARGUMENT, "Element id expected in first argument");
            }
            final Object idAsObj = ((JSONObject) firstArg).get(UIElementId.JSON_KEY);
            if (idAsObj instanceof String == false) {
                throw new WebDrvException(EWebDrvErrorCode.INVALID_ARGUMENT, "Element id expected in first argument");
            }
            final UIElementId elementId = UIElementId.fromString((String) idAsObj);
            final UIElementReference elementRef = session.getUIElements().getUIElementReference(elementId);
            final Object secondArg = args.get(1);
            if (secondArg instanceof String == false) {
                throw new WebDrvException(EWebDrvErrorCode.INVALID_ARGUMENT, "Attribute name expected in second argument");
            }
            return new WebDrvSessionCommandResult(elementRef.getPropertyValue((String) secondArg));
        }

        if (script.equals(INSTALL_DIALOG_HANDLER_CMD)) {
            if (args.size() == 0 || args.get(0) == null) {
                throw new WebDrvException(EWebDrvErrorCode.INVALID_ARGUMENT, "Arguments not found or the first argument is null");
            }

            final JSONArray files;
            final JSONArray filter;
            final String mode;
            final JSONArray options;

            try {
                files = (JSONArray) new JSONParser().parse((String) args.get(0));

                if (args.size() > 1 && args.get(1) != null) {
                    filter = (JSONArray) new JSONParser().parse((String) args.get(1));
                } else {
                    filter = null;
                }

                mode = args.size() > 2 ? (String) args.get(2) : null;

                if (args.size() > 3 && args.get(3) != null) {
                    options = (JSONArray) new JSONParser().parse((String) args.get(3));
                } else {
                    options = null;
                }
            } catch (ParseException ex) {
                throw new WebDrvException(EWebDrvErrorCode.INVALID_ARGUMENT, "Error parsing json");
            }

            try {
                OpenAndSaveDialogHandler handler = new OpenAndSaveDialogHandler(files, mode, filter, options, session);
                session.installDialogHandler(handler);
                return new WebDrvSessionCommandResult(handler.getGuid());
            } catch (IllegalArgumentException | FileNotFoundException ex) {
                throw new WebDrvException(EWebDrvErrorCode.INVALID_ARGUMENT, ex.getMessage());
            } 
        }

        if (script.equals(REMOVE_DIALOG_HANDLER_BY_GUID_CMD)) {
            if (args.size() == 0 || args.get(0) == null) {
                throw new WebDrvException(EWebDrvErrorCode.INVALID_ARGUMENT, "Arguments not found or the first argument is null");
            }
            try {
                session.removeDialogHandler((String) args.get(0));
                return new WebDrvSessionCommandResult(SUCCESSFUL_RESULT);
            } catch (IllegalArgumentException ex) {
                throw new WebDrvException(EWebDrvErrorCode.INVALID_ARGUMENT, ex.getMessage());
            }
        }

        if (script.equals(REMOVE_ALL_DIALOG_HANDLER_CMD)) {
            session.removeAllDialogHandler();
            return new WebDrvSessionCommandResult(SUCCESSFUL_RESULT);
        }

        throw new WebDrvException(EWebDrvErrorCode.UNSUPPORTED_OPERATION);
    }

    private static String readUTF8StreamToString(InputStream input) throws IOException {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(input, "UTF-8");
        for (;;) {
            int rsz = in.read(buffer, 0, buffer.length);
            if (rsz < 0) {
                break;
            }
            out.append(buffer, 0, rsz);
        }
        return out.toString();
    }
}
