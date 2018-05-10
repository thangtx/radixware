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

public enum EWebDrvErrorCode {
    
    ELEMENT_CLICK_INTERCEPTED(400, "element click intercepted", "The Element Click command could not be completed because the element receiving the events is obscuring the element that was requested clicked.", true),
    ELEMENT_NOT_SELECTABLE(400, "element not selectable", "An attempt was made to select an element that cannot be selected."),
    ELEMENT_NOT_INTERACTABLE(400, "element not interactable", "A command could not be completed because the element is not pointer- or keyboard interactable.", true),
    INSECURE_CERTIFICATE(400, "insecure certificate", "Navigation caused the user agent to hit a certificate warning, which is usually the result of an expired or invalid TLS certificate."),
    INVALID_ARGUMENT(400, "invalid argument", "The arguments passed to a command are either invalid or malformed."),
    INVALID_COOKIE_DOMAIN(400, "invalid cookie domain", "An illegal attempt was made to set a cookie under a different domain than the current page."),
    INVALID_COORDINATES(400, "invalid coordinates", "The coordinates provided to an interactions operation are invalid."),
    INVALID_ELEMENT_STATE(400, "invalid element state", "A command could not be completed because the element is in an invalid state, e.g. attempting to click an element that is no longer attached to the document.",true),
    INVALID_SELECTOR(400, "invalid selector", "Argument was an invalid selector."),
    INVALID_SESSION_ID(404, "invalid session id", "The session either does not exist or that it’s not active."),
    JAVASCRIPT_ERROR(500, "javascript error", "An error occurred while executing JavaScript supplied by the user."),
    MOVE_TARGET_OUT_OF_BOUNDS(500, "move target out of bounds", "The target for mouse interaction is not in the browser’s viewport and cannot be brought into that viewport."),
    NO_SUCH_ALERT(400, "no such alert", "An attempt was made to operate on a modal dialog when one was not open."),
    NO_SUCH_COOKIE(404, "no such cookie", "No cookie matching the given path name was found amongst the associated cookies of the current browsing context’s active document."),
    NO_SUCH_ELEMENT(404, "no such element", "An element could not be located in application using the given search parameters.", true),
    NO_SUCH_FRAME(400, "no such frame", "A command to switch to a frame could not be satisfied because the frame could not be found.", true),
    NO_SUCH_WINDOW(400, "no such window", "A command to switch to a window could not be satisfied because the window could not be found.", true),
    SCRIPT_TIMEOUT(408, "script timeout", "A script did not complete before its timeout expired."),
    SESSION_NOT_CREATED(500, "session not created", "A new session could not be created."),
    STALE_ELEMENT_REFERENCE(400, "stale element reference", "A command failed because the referenced element is no longer attached to the DOM."),
    TIMEOUT(408, "timeout", "An operation did not complete before its timeout expired."),
    EAS_REQUEST_TIMEOUT(400, "timeout", "Eas request processing takes too much time."),
    UNABLE_TO_SET_COOKIE(500, "unable to set cookie", "A command to set a cookie’s value could not be satisfied."),
    UNABLE_TO_CAPTURE_SCREEN(500, "unable to capture screen", "A screen capture was made impossible."),
    UNEXPECTED_ALERT_OPEN(500, "unexpected alert open", "A modal message dialog was open, blocking this operation."),
    UNKNOWN_COMMAND(404, "unknown command", "A command could not be executed because the remote end is not aware of it."),
    UNKNOWN_ERROR(500, "unknown error", "An unknown error occurred in the remote end while processing the command."),
    UNKNOWN_METHOD(405, "unknown method", "The requested command matched a known URL but did not match an method for that URL."),
    UNSUPPORTED_OPERATION(500, "unsupported operation", "This operation is not supported in current WebDriver implementation"),
	IP_REJECTED(403, "403.6 - IPaddress rejected", "Host '%1$s' is not whitelisted.");

    private final int httpStatus;
    private final String jsonError;
    private final String jsonMessage;
    private final boolean recoverable;
    
    private EWebDrvErrorCode(final int httpCode, 
                                              final String jsonError, 
                                              final String jsonMessage){
        this(httpCode, jsonError, jsonMessage, false);
    }    
    
    private EWebDrvErrorCode(final int httpCode, 
                                              final String jsonError, 
                                              final String jsonMessage,
                                              final boolean recoverable){
        this.httpStatus = httpCode;
        this.jsonError = jsonError;
        this.jsonMessage = jsonMessage;
        this.recoverable = recoverable;
    }
    
    public int getHttpStatus(){
        return httpStatus;
    }
    
    public String getJsonError(){
        return jsonError;
    }
    
    public String getDescription(){
        return jsonMessage;
    }
    
    public String getHttpBody(final String message, final String stackTrace){
        final Map<String,String> body = new HashMap<>();
        body.put("error", jsonError);
        body.put("message", message==null || message.isEmpty() ? jsonMessage : message);
        body.put("stacktrace", stackTrace==null ? "" : stackTrace);
        return JSONObject.toJSONString(body);
    }
    
    public boolean isRecoverable(){
        return recoverable;
    }
}
