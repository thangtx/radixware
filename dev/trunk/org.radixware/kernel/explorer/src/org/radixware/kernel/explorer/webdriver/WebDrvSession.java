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

import com.trolltech.qt.gui.QFileDialog;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.radixware.kernel.explorer.webdriver.elements.WebDrvUIElementsManager;
import org.radixware.kernel.explorer.webdriver.exceptions.WebDrvException;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.eas.AbstractEasSession;
import org.radixware.kernel.common.client.trace.ClientTracer;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.utils.Guid;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.utils.QtJambiExecutor;
import org.radixware.kernel.explorer.views.selector.ChooseGroupSetting;
import org.radixware.kernel.explorer.webdriver.commands.IWebDrvSessionCommand;
import org.radixware.kernel.explorer.webdriver.elements.finders.xmlwrapper.DocumentWrapper;
import org.radixware.kernel.explorer.webdriver.exceptions.EWebDrvErrorCode;
import org.radixware.kernel.explorer.webdriver.input.WebDrvInputSourceManager;

public final class WebDrvSession {

    private final static long WAIT_PAUSE_MILLIS = 50;

    private final static class ExecCommandTask implements Callable<JSONObject> {

        private final IWebDrvSessionCommand command;
        private final WebDrvSession session;
        private final WebDrvUri uri;
        private final JSONObject params;

        public ExecCommandTask(final WebDrvSession session,
                final WebDrvUri uri,
                final IWebDrvSessionCommand command,
                final JSONObject params) {
            this.session = session;
            this.uri = uri;
            this.command = command;
            this.params = params;

        }

        @Override
        public JSONObject call() throws Exception {
            return command.execute(session, uri, params);
        }

    }

    private final static class CheckForEasRequestTask implements Callable<Boolean> {

        private final ClientTracer tracer;
        private final AbstractEasSession easSession;

        public CheckForEasRequestTask(final IClientEnvironment environment) {
            this.easSession = (AbstractEasSession) environment.getEasSession();
            tracer = environment.getTracer();
        }

        @Override
        public Boolean call() throws Exception {
            final boolean isBusy = easSession.isBusy() && !easSession.inInteractiveCallBackRequestProcessing();
            if (isBusy) {
                tracer.put(EEventSeverity.DEBUG, "EAS session is busy", EEventSource.WEB_DRIVER);
            } else {
                tracer.put(EEventSeverity.DEBUG, "EAS session is in IDLE state", EEventSource.WEB_DRIVER);
            }
            return isBusy;
        }
    }

    private final UUID id;
    private final WebDrvCapabilities capabilities;
    private final QtJambiExecutor executor;
    private final WebDrvUIElementsManager elements;
    private final WebDrvInputSourceManager inputManager = new WebDrvInputSourceManager(this);
    private final IClientEnvironment environment;
    private final List<File> filesToDeleteOnExit = new ArrayList<>();
    private final CheckForEasRequestTask checkForEasRequestTask;
    private final long timeStart;
    private final Map<String, OpenAndSaveDialogHandler> dialogHandlers = new HashMap<>();

    WebDrvSession(final WebDrvCapabilities caps, final QtJambiExecutor executor, final IClientEnvironment environment, long timeStart) {
        id = UUID.randomUUID();
        capabilities = caps;
        capabilities.init();
        this.executor = executor;
        elements = new WebDrvUIElementsManager(capabilities, environment);
        this.environment = environment;
        checkForEasRequestTask = new CheckForEasRequestTask(environment);
        this.timeStart = timeStart;
    }

    public long getTimeStart() {
        return this.timeStart;
    }

    public UUID getId() {
        return id;
    }

    public WebDrvCapabilities getCapabilities() {
        return capabilities;
    }

    public WebDrvUIElementsManager getUIElements() {
        return elements;
    }

    public WebDrvInputSourceManager getInputSourceManager() {
        return inputManager;
    }

    private boolean isEasRequestExecuting() throws InterruptedException, ExecutionException {
        environment.getTracer().put(EEventSeverity.DEBUG, "Checking EAS session state", EEventSource.WEB_DRIVER);
        for (int i = 1; i < 5; i++) {
            //It is useless to check EAS session state directly here
            //It may be that WebDriver command posted some events that cause EAS request execution when processing
            //So EAS session state check must be postponed in separate event
            if (executor.invoke(checkForEasRequestTask)) {
                return true;
            }
        }
        return false;
    }

    private void waitForFinishEasRequest() throws WebDrvException {
        final long pageLoadTimeout = capabilities.getTimeouts().getPageLoad();
        if (pageLoadTimeout > 0) {
            long startTime = 0;
            while (true) {
                if (startTime > 0) {
                    try {
                        Thread.sleep(WAIT_PAUSE_MILLIS);
                    } catch (InterruptedException exception) {
                        throw new WebDrvException(exception.getMessage(), exception);
                    }
                } else {
                    startTime = System.currentTimeMillis();
                }
                try {
                    if (!isEasRequestExecuting()) {
                        return;
                    }
                } catch (InterruptedException exception) {
                    //ignore;
                } catch (ExecutionException exception) {
                    final Throwable cause = exception.getCause();
                    throw new WebDrvException(cause.getMessage(), cause);
                }
                // can wait indefinite if pageLoadTimeout is negative.
                if (pageLoadTimeout >= 0) {
                    if ((System.currentTimeMillis() - startTime) > pageLoadTimeout) {
                        //web client will resend request automatically in case when 408 code received
                        //so do not throw EWebDrvException.TIMEOUT here
                        throw new WebDrvException(EWebDrvErrorCode.EAS_REQUEST_TIMEOUT);
                    }
                }
            }
        }
    }

    private JSONObject execTask(final ExecCommandTask task) throws WebDrvException {
        final long implicitTimeout = getCapabilities().getTimeouts().getImplicit();
        final long startTime = System.currentTimeMillis();
        while (true) {
            try {
                return executor.invoke(task);
            } catch (InterruptedException exception) {
                throw new WebDrvException(exception.getMessage(), exception);
            } catch (ExecutionException exception) {
                WebDrvException webDrvException = null;
                for (Throwable cause = exception.getCause(); cause != null; cause = cause.getCause()) {
                    if (cause instanceof WebDrvException) {
                        webDrvException = (WebDrvException) cause;
                        break;
                    }
                }
                if (webDrvException != null) {
                    if (webDrvException.isRecoverable()
                            && implicitTimeout > 0
                            && (System.currentTimeMillis() - startTime) < implicitTimeout) {
                        try {
                            Thread.sleep(WAIT_PAUSE_MILLIS);
                        } catch (InterruptedException ex) {
                            throw new WebDrvException(ex.getMessage(), ex);
                        }
                        continue;
                    } else {
                        throw webDrvException;
                    }
                }
                final Throwable cause = exception.getCause();
                throw new WebDrvException(cause.getMessage(), cause);
            }
        }
    }

    /**
     * Р’С‹Р·С‹РІР°С‚СЊ РЅРµ РёР· РїРѕС‚РѕРєР° GUI. РћР¶РёРґР°РЅРёРµ, РєРѕРіРґР° РїРѕСЃР»РµРґРЅРёР№ СЂР°СЃРєСЂС‹С‚С‹Р№ popup
     * РјРѕР¶РЅРѕ Р±СѓРґРµС‚ Р·Р°РєСЂС‹РІР°С‚СЊ.
     */
    private static void waitForChooseGroupSetting() throws InterruptedException {
        while (true) {
            long ms = System.currentTimeMillis() - ChooseGroupSetting.getLastPopupShownTimestamp();
            if (ms < ChooseGroupSetting.dontHidePopupDelay) {
                Thread.sleep(ChooseGroupSetting.dontHidePopupDelay - ms);
            } else {
                break;
            }
        }
    }

    public JSONObject execCommand(final IWebDrvSessionCommand command, final WebDrvUri uri, final JSONObject params) throws WebDrvException {
        final JSONObject commandResult;
        if (command.isGuiCommand()) {
            commandResult = execTask(new ExecCommandTask(this, uri, command, params));
            if (command.isInputActionCommand()) {
                waitForFinishEasRequest();
            }
            try {
                waitForChooseGroupSetting();
            } catch (Exception ex) {
                throw new WebDrvException(ex.getMessage(), ex);
            }
        } else {
            commandResult = command.execute(this, uri, params);
        }
        if (commandResult == null) {
            return null;
        } else if (commandResult instanceof WebDrvSessionCommandResult) {
            return commandResult;
        } else {
            return new WebDrvSessionCommandResult(commandResult);
        }
    }

    public String getPageSource() throws WebDrvException {
        try {
            final Transformer transformer = TransformerFactory.newInstance().newTransformer();
            final Writer writer = new StringWriter();
            final Result output = new StreamResult(writer);
            final Source input = new DOMSource(new DocumentWrapper());
            transformer.transform(input, output);
            return writer.toString();
        } catch (TransformerException | TransformerFactoryConfigurationError error) {
            throw new WebDrvException("", error);
        }
    }

    @SuppressWarnings("unchecked")
    public void writeToJson(final JSONObject writeTo) {
        writeTo.put("sessionId", id.toString());
        capabilities.writeToJson(writeTo);
    }

    public QtJambiExecutor close() {
        this.deleteFilesToDeleteOnExit();
        removeAllDialogHandler();
        return executor;
    }

    public void installDialogHandler(final OpenAndSaveDialogHandler dialogHandler) throws IllegalArgumentException {
        if (dialogHandler == null) {
            throw new IllegalArgumentException("Argument dialogHandler is null");
        }
        QFileDialog.installDialogHandler(dialogHandler);
        dialogHandlers.put(dialogHandler.getGuid(), dialogHandler);
    }

    public void removeDialogHandler(final OpenAndSaveDialogHandler dialogHandler) throws IllegalArgumentException {
        if (dialogHandler == null) {
            throw new IllegalArgumentException("Argument dialogHandler is null");
        }
        if (dialogHandlers.remove(dialogHandler.getGuid()) != null) {
            QFileDialog.removeDialogHandler(dialogHandler);
        }
    }

    public void removeDialogHandler(final String guid) throws IllegalArgumentException {
        if (guid == null || guid.isEmpty()) {
            throw new IllegalArgumentException("Argument guid is null or empty");
        }

        final OpenAndSaveDialogHandler handler = dialogHandlers.get(guid);

        if (handler == null) {
            throw new IllegalArgumentException("Non-existent guid");
        }

        QFileDialog.removeDialogHandler(handler);
        dialogHandlers.remove(guid);
    }

    public void removeAllDialogHandler() {
        for (Entry<String, OpenAndSaveDialogHandler> handler : dialogHandlers.entrySet()) {
            QFileDialog.removeDialogHandler(handler.getValue());
        }
        dialogHandlers.clear();
    }
    
    public final static class OpenAndSaveDialogHandler implements QFileDialog.DialogHandler {

        final private List<String> files;
        final private List<String> filters;
        final private QFileDialog.AcceptMode mode;
        final private QFileDialog.Options options;
        final private String guid;
        final private WebDrvSession session;

        @SuppressWarnings("unchecked")
        public OpenAndSaveDialogHandler(final JSONArray files, final String mode, final JSONArray filters, final JSONArray options, final WebDrvSession session) throws IllegalArgumentException, FileNotFoundException {
            guid = Guid.generateGuid();
            this.mode = getMode(mode);
            this.files = new ArrayList<>(files);
            this.session = session;

            if (options == null || options.size() == 0) {
                this.options = null;
            } else {
                this.options = new QFileDialog.Options(0);
                
                for (Object option : options) {
                    this.options.set(getOption((String) option));
                }
            }
            
            this.filters = filters == null || filters.size() == 0 ? Arrays.asList("*") : filters;

            if (this.mode != null && this.mode == QFileDialog.AcceptMode.AcceptOpen) {
                for (String path : this.files) {
                    if (!new File(path).exists()) {
                        throw new FileNotFoundException(new StringBuilder("File [").append(path).append("] not found").toString());
                    }
                }
            }
        }

        public String getGuid() {
            return guid;
        }

        @Override
        public List<String> beforeOpen(QFileDialog.AcceptMode mode, String caption, String dir, String filters, QFileDialog.Options optns) {
            boolean isSuitable = (options == null || options.value() == optns.value()) && (mode == null || mode.value() == mode.value());

            if (isSuitable) {
                if (!(isSuitable = this.filters.contains("*") || filters.contains("*"))) {
                    for (String filter : this.filters) {
                        if (filters.contains(filter)) {
                            isSuitable = true;
                            break;
                        }
                    }
                }
            }

            if (isSuitable) {
                session.removeDialogHandler(this);
                return files;
            }
            return null;
        }

        private QFileDialog.AcceptMode getMode(final String mode) throws IllegalArgumentException {
            if (mode == null) {
                return null;
            }

            switch (mode) {
                case "ACCEPT_OPEN":
                    return QFileDialog.AcceptMode.AcceptOpen;
                case "ACCEPT_SAVE":
                    return QFileDialog.AcceptMode.AcceptSave;
                default:
                    throw new IllegalArgumentException(new StringBuilder("Not supported value: [").append(mode).append("]").toString());
            }
        }

        private QFileDialog.Option getOption(final String option) throws IllegalArgumentException {
            switch (option) {
                case "SHOW_DIRS_ONLY":
                    return QFileDialog.Option.ShowDirsOnly;
                case "DONT_RESOLVE_SYMLINKS":
                    return QFileDialog.Option.DontResolveSymlinks;
                case "DONT_CONFIRM_OVERWRITE":
                    return QFileDialog.Option.DontConfirmOverwrite;
                case "DONT_USE_NATIVE_DIALOG":
                    return QFileDialog.Option.DontUseNativeDialog;
                case "READ_ONLY":
                    return QFileDialog.Option.ReadOnly;
                case "HIDE_NAME_FILTER_DETAILS":
                    return QFileDialog.Option.HideNameFilterDetails;
                case "DONT_USE_SHEET":
                    return QFileDialog.Option.DontUseSheet;
                case "DONT_USE_CUSTOM_DIRECTORY_ICONS":
                    return QFileDialog.Option.DontUseCustomDirectoryIcons;
                default:
                    throw new IllegalArgumentException(new StringBuilder("Not supported value: [").append(option).append("]").toString());
            }
        }

        @Override
        public List<String> afterOpen(QFileDialog.AcceptMode am, String string, String string1, String string2, QFileDialog.Options optns, List<String> list) {
            return list;
        }
    }

    public IClientEnvironment getEnvironment() {
        return environment;
    }

    public void registerFileToDeleteOnExit(File file) {
        filesToDeleteOnExit.add(file);
    }

    private void deleteFilesToDeleteOnExit() {
        for (File f : filesToDeleteOnExit) {
            try {
                if (f.exists()) {
                    f.delete();
                }
            } catch (Exception ex) {
                String msg = Application.getInstance().getEnvironment()
                        .getMessageProvider()
                        .translate("WebDriver", "Failed to delete temp file '%1$s' created in session '%2$s'");
                WebDrvServer.traceEvent(msg, f.getAbsolutePath(), this.getId().toString()
                );
            }
        }
        filesToDeleteOnExit.clear();
    }
}
