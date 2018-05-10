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

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.json.simple.JSONObject;
import org.radixware.kernel.explorer.webdriver.EHttpMethod;
import org.radixware.kernel.explorer.webdriver.exceptions.EWebDrvErrorCode;
import org.radixware.kernel.explorer.webdriver.exceptions.WebDrvException;
import org.radixware.kernel.explorer.webdriver.WebDrvSession;
import org.radixware.kernel.explorer.webdriver.WebDrvSessionsManager;
import org.radixware.kernel.explorer.webdriver.WebDrvUri;

public final class EndPoint {

    private final static Collection<IWebDrvCommand> COMMANDS
            = Arrays.<IWebDrvCommand>asList(new StatusCmd(),
                    new NewSessionCmd(),
                    new DeleteSessionCmd(),
                    new GetTimeoutsCmd(),
                    new SetTimeoutsCmd(),
                    new GoCmd(),
                    new GetCurrentUrlCmd(),
                    new BackCmd(),
                    new ForwardCmd(),
                    new RefreshCmd(),
                    new GetTitleCmd(),
                    new GetWindowHandleCmd(),
                    new CloseWindowCmd(),
                    new SwitchWindowCmd(),
                    new GetWindowHandlesCmd(),
                    new SwitchToFrameCmd(),
                    new SwitchToParentFrameCmd(),
                    new GetWindowRectCmd(),
                    new SetWindowRectCmd(),
                    new MaximizeWindowCmd(),
                    new MinimizeWindowCmd(),
                    new FullscreenWindowCmd(),
                    new FindElementCmd(),
                    new FindElementsCmd(),
                    new FindElementFromElementCmd(),
                    new FindElementsFromElementCmd(),
                    new IsElementSelectedCmd(),
                    new GetElementPropertyCmd(),
                    new GetElementAttributeCmd(),
                    new GetElementCssValueCmd(),
                    new GetElementTextCmd(),
                    new GetElementTagNameCmd(),
                    new GetElementRectCmd(),
                    new IsElementEnabledCmd(),
                    new ElementClickCmd(),
                    new ElementClearCmd(),
                    new SendKeysCmd(),
                    new ExecuteScriptCmd(),
                    new GetPageSourceCmd(),
                    new GetAllCookies(),
                    new GetNamedCookie(),
                    new AddCookieCmd(),
                    new DeleteCookieCmd(),
                    new DeleteAllCookiesCmd(),
                    new PerformActionsCmd(),
                    new ReleaseActionsCmd(),
                    new DismissAlertCmd(),
                    new AcceptAlertCmd(),
                    new GetAlertTextCmd(),
                    new SendAlertTextCmd(),
                    new TakeScreenshotCmd(),
                    new ElementScreenshotCmd(),
                    new GetActiveElementCmd(),
                    new SendKeysToActiveElementCmd(),
                    new UploadFileCmd(),
                    new GetAvailableLogTypesCmd(),
                    new GetLogCmd()
            );

    private final IWebDrvCommand command;
    private final WebDrvSession session;
    private final WebDrvUri uri;

    private EndPoint(final IWebDrvCommand cmd, final WebDrvSession session, final WebDrvUri uri) {
        command = cmd;
        this.session = session;
        this.uri = uri;
    }

    public static EndPoint route(final String uriPath, final String methodName) throws WebDrvException {
        final WebDrvUri uri = WebDrvUri.parse(uriPath);
        final IWebDrvCommand command = getCommand(uri, methodName);
        final UUID sessionId = uri.getSessionId();
        if (sessionId == null) {
            if (command instanceof IWebDrvServiceCommand == false) {
                throw new WebDrvException(EWebDrvErrorCode.UNKNOWN_COMMAND);
            }
            return new EndPoint(command, null, uri);
        } else {
            final WebDrvSession session = WebDrvSessionsManager.getInstance().getSessionById(sessionId);
            if (command instanceof IWebDrvSessionCommand == false) {
                throw new WebDrvException(EWebDrvErrorCode.UNKNOWN_COMMAND);
            }
            return new EndPoint(command, session, uri);
        }
    }

    private static IWebDrvCommand getCommand(final WebDrvUri uri, final String methodName) throws WebDrvException {
        final List<IWebDrvCommand> matchedByName = new LinkedList<>();
        for (IWebDrvCommand command : COMMANDS) {
            if (uri.getCommandName().equals(command.getName())
                    && Objects.equals(uri.getCommandGroupName(), command.getGroupName())) {
                matchedByName.add(command);
            }
        }
        if (matchedByName.isEmpty()) {
            throw new WebDrvException(EWebDrvErrorCode.UNKNOWN_COMMAND);
        }
        final EHttpMethod httpMethod = getHttpMethod(methodName);
        for (IWebDrvCommand command : matchedByName) {
            if (command.getHttpMethod() == httpMethod) {
                return command;
            }
        }
        throw new WebDrvException(EWebDrvErrorCode.UNKNOWN_METHOD);
    }

    public JSONObject processRequest(final JSONObject params, WebDrvCommandExecContext context) throws WebDrvException {
        if (command instanceof NewSessionCmd) {
            return ((NewSessionCmd) command).executeWithOptionalContext(params, context);
        }

        if (command instanceof DeleteSessionCmd) {
            return ((DeleteSessionCmd) command).executeWithOptionalContext(session, uri, params, context);
        }

        if (command instanceof IWebDrvServiceCommand) {
            return ((IWebDrvServiceCommand) command).execute(params);
        }

        if (command instanceof IWebDrvSessionCommand) {
            return session.execCommand((IWebDrvSessionCommand) command, uri, params);
        }

        throw new WebDrvException(EWebDrvErrorCode.UNKNOWN_COMMAND, "Unknown command \'" + command.getName() + "\'");
    }

    private static EHttpMethod getHttpMethod(final String methodName) throws WebDrvException {
        try {
            return EHttpMethod.valueOf(methodName);
        } catch (IllegalArgumentException exception) {
            throw new WebDrvException(EWebDrvErrorCode.UNKNOWN_METHOD);
        }
    }
}
