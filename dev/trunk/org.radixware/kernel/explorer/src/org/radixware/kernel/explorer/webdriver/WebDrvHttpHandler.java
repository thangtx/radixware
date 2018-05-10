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
package org.radixware.kernel.explorer.webdriver;

import org.radixware.kernel.explorer.webdriver.exceptions.WebDrvException;
import org.radixware.kernel.explorer.webdriver.exceptions.EWebDrvErrorCode;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.webdriver.commands.EndPoint;
import org.radixware.kernel.explorer.webdriver.commands.WebDrvCommandExecContext;

final class WebDrvHttpHandler implements HttpHandler {

    private final static WebDrvHttpHandler INSTANCE = new WebDrvHttpHandler();

    private WebDrvHttpHandler() {
        this.whiteListClients.add("localhost");
        this.whiteListClients.add("127.0.0.1");
        this.whiteListClients.add("::1");
    }

    public static WebDrvHttpHandler getInstance() {
        return INSTANCE;
    }

    /**
     * Белый список адресов, с которых разрешено подключение к webdriver. По
     * умолчнию только localhost и 127.0.0.1.
     */
    private final HashSet<String> whiteListClients = new HashSet<String>();

    /**
     * whiteListClients перечисленные через запятую адреса, с которых разрешать
     * подключение.
     */
    public void appendWhiteListClients(String whiteListClients) {
        if (whiteListClients == null) {
            return;
        }
        for (String address : whiteListClients.split(",")) {
            if (address.isEmpty()) {
                continue;
            }
            this.whiteListClients.add(address);
        }
    }

    @Override
    public void handle(final HttpExchange httpExchange) throws IOException {
        try {
            long timeReceive = System.currentTimeMillis();
            if (!whiteListClients.contains(httpExchange.getRemoteAddress().getHostString())) {
                String msg = Application.getInstance().getEnvironment()
                        .getMessageProvider()
                        .translate("WebDriver", "Host '%1$s' is not allowed to connect to this WebDriver server");
                WebDrvServer.traceEvent(msg, httpExchange.getRemoteAddress().getHostString());
                sendError(httpExchange,
                        new WebDrvException(EWebDrvErrorCode.IP_REJECTED
                                , String.format("Host '%1$s' is not whitelisted"
                                        , httpExchange.getRemoteAddress().getHostString())));
                return;
            }

            final String urlPath = httpExchange.getRequestURI().getPath();
            final String rootPath = httpExchange.getHttpContext().getPath();
            final String commandName = urlPath.substring(rootPath.length());
            final JSONObject response;
            try {
                final EndPoint endPoint = EndPoint.route(commandName, httpExchange.getRequestMethod());
                final String requestString;
                try {
                    requestString = readString(httpExchange.getRequestBody());
                } catch (IOException exception) {
                    throw new WebDrvException("Failed to read request body", exception);
                }
                final JSONObject requestParams;
                if (requestString == null || requestString.isEmpty()) {
                    requestParams = null;
                } else {
                    final Object requestObj;
                    try {
                        requestObj = new JSONParser().parse(requestString);
                    } catch (org.json.simple.parser.ParseException exception) {
                        throw new WebDrvException("Failed to parse request body", exception);
                    }
                    if (requestObj == null) {
                        requestParams = null;
                    } else if (requestObj instanceof JSONObject) {
                        requestParams = (JSONObject) requestObj;
                    } else {
                        final String message = "Unexpected request body type " + requestObj.getClass().getName();
                        throw new WebDrvException(EWebDrvErrorCode.UNKNOWN_ERROR, message);
                    }
                }
                
                WebDrvServer.traceDebug("REQUEST (%1$s) received from %2$s:\\nURI:%3$s\\nBody:\\n%4$s"
                        , httpExchange.getRequestMethod()
                        , httpExchange.getRemoteAddress().getHostString()
                        , httpExchange.getRequestURI().toASCIIString()
                        , requestParams == null ? "" : requestParams.toJSONString()
                );

                WebDrvCommandExecContext ctx = new WebDrvCommandExecContext(httpExchange, timeReceive);

                response = endPoint.processRequest(requestParams, ctx);
            } catch (WebDrvException exception) {
                sendError(httpExchange, exception);
                return;
            } catch (RuntimeException exception) {
                final String message;
                if (exception.getMessage() != null && !exception.getMessage().isEmpty()) {
                    message = exception.getMessage();
                } else {
                    message = exception.getClass().getName();
                }
                sendError(httpExchange, new WebDrvException(message, exception));
                return;
            }
            sendAnswer(httpExchange, response);
        } catch (IOException ioex) {
            WebDrvServer.traceError("IOException - %1$s - %2$s"
                    , ioex.getMessage()
                    , ClientException.exceptionStackToString(ioex));
        }
    }

    private static String readString(final InputStream stream) throws IOException {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(stream, "UTF-8");
        for (;;) {
            int rsz = in.read(buffer, 0, buffer.length);
            if (rsz < 0) {
                break;
            }
            out.append(buffer, 0, rsz);
        }
        return out.toString();
    }

    private void sendError(final HttpExchange httpExchange, final WebDrvException exception) throws IOException {
        sendResponse(httpExchange, exception.getHttpStatus(), exception.getHttpBody());
    }

    private void sendAnswer(final HttpExchange httpExchange, final JSONObject answer) throws IOException {
        sendResponse(httpExchange, 200, answer == null ? "" : answer.toJSONString());
    }

    private void sendResponse(final HttpExchange httpExchange, final int responseCode, final String responseBody) throws IOException {
        if (responseBody == null || responseBody.isEmpty()) {
            WebDrvServer.traceDebug("RESPONSE (HTTP code %1$s) send to %2$s:\nBody length: %3$s"
                    , Integer.toString(responseCode)
                    , httpExchange.getRemoteAddress().getHostString()
                    , "0");
            httpExchange.sendResponseHeaders(responseCode, 0);
            httpExchange.getResponseBody().close();
        } else {
            if (responseBody.length() > 100000) {
                WebDrvServer.traceDebug("RESPONSE (HTTP code %1$s) send to %2$s:\nBody length: %3$s"
                    , Integer.toString(responseCode)
                    , httpExchange.getRemoteAddress().getHostString()
                    , Integer.toString(responseBody.length()));
            } else {
                WebDrvServer.traceDebug("RESPONSE (HTTP code %1$s) send to %2$s:\n%3$s"
                    , Integer.toString(responseCode)
                    , httpExchange.getRemoteAddress().getHostString()
                    , responseBody);
            }
            final byte[] data = responseBody.getBytes(StandardCharsets.UTF_8);

            httpExchange.sendResponseHeaders(responseCode, data.length);
            try (final OutputStream os = httpExchange.getResponseBody()) {
                os.write(data);
            }
        }
    }

}
