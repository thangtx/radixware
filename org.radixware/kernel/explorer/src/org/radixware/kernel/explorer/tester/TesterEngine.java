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

package org.radixware.kernel.explorer.tester;

import org.radixware.kernel.explorer.tester.providers.TestsProvider;
import org.radixware.kernel.explorer.tester.tests.ITest;


import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QMessageBox;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.errors.CantOpenSelectorError;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.meta.mask.EditMaskTimeInterval;
import org.radixware.kernel.common.client.trace.ClientTraceItem;
import org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMaskTimeInterval.Scale;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.trace.TraceItem;
import org.radixware.kernel.explorer.env.MessageFilter;
import org.radixware.kernel.explorer.env.trace.ExplorerTraceItem;

import org.radixware.kernel.explorer.tester.tests.TestResultEvent;
import org.radixware.kernel.explorer.tester.tests.TestResult;


public final class TesterEngine extends QObject {

    private ITest currentTest;
    private TestsProvider currentProvider;
    private TestsOptions options;
    private final Collection<ITestResultsWriter> resultWriters = new ArrayList<>();
    private final TesterEnvironment testerEnvironment;
    public Signal0 finished = new Signal0();

    public TesterEngine(IClientEnvironment environment) {
        testerEnvironment = new TesterEnvironment(environment);
    }

    private static final class StartCurrentTestEvent extends QEvent {

        public StartCurrentTestEvent() {
            super(QEvent.Type.User);
        }
    }

    private static final class GoToNextTestEvent extends QEvent {

        public GoToNextTestEvent() {
            super(QEvent.Type.User);
        }
    }

    public static final class NoMoreTestsEvent extends QEvent {

        public NoMoreTestsEvent() {
            super(QEvent.Type.User);
        }
    }

    private boolean processTimeLimitCheck(TestResult result, Long time) {
        if (result.operation != null && !result.operation.isEmpty()) {
            if (result.operation.equals(TesterConstants.TEST_OPENING.getTitle())) {
                return time <= options.openingTimeBoundary;
            } else if (result.operation.equals(TesterConstants.TEST_INSERTIONS.getTitle())) {
                return time <= options.insertsTimeBoundary;
            } else if (result.operation.equals(TesterConstants.TEST_FILTERS.getTitle())) {
                return time <= options.filtersTimeBoundary;
            } else if (result.operation.equals(TesterConstants.TEST_PAGE.getTitle())) {
                return time <= options.pagesTimeBoundary;
            } else if (result.operation.equals(TesterConstants.TEST_CLOSING.getTitle())) {
                return time <= options.closingTimeBoundary;
            }
        }
        return true;
    }

    @Override
    protected void customEvent(QEvent event) {
        if (event instanceof StartCurrentTestEvent) {
            if (currentTest != null) {
                testerEnvironment.getTraceBuffer().clear();

                final long timeBefore = System.currentTimeMillis();
                currentTest.execute(options, testerEnvironment);
                final long timeAfter = System.currentTimeMillis();
                EditMaskTimeInterval timeInterval =
                        new EditMaskTimeInterval(Scale.MILLIS.longValue(), "hh:mm:ss:zzzz", null, null);

                if (currentTest != null && currentProvider!=null) {
                    String intervalStr = timeInterval.toStr(currentProvider.getEnvironment(), Long.valueOf(timeAfter - timeBefore));
                    try {
                        TestResult result = currentTest.getTestResult();

                        processMessageFilterCheck(result);
                        result.time = intervalStr;
                        final boolean validTime = processTimeLimitCheck(result, Long.valueOf(timeAfter - timeBefore));
                        if (!validTime) {
                            result.result = TesterConstants.RESULT_FAIL_TIME_LIMIT.getTitle();
                        }

                        processTraceCheck(result, result.hasErrors(), validTime);

                        for (ITestResultsWriter writer : resultWriters) {
                            writer.writeResult(result);
                        }
                        QApplication.postEvent(this, new GoToNextTestEvent());
                    } catch (Throwable ex) {
                        processException(ex);
                    }
                }
            }
            if (currentTest == null) {
                QApplication.postEvent(this, new NoMoreTestsEvent());
            }
        } else if (event instanceof GoToNextTestEvent) {
            try {
                if (currentTest != null) {
                    final TestsProvider childProvider = currentTest.createChildTestsProvider(currentProvider, options);
                    if (childProvider != null) {
                        currentProvider = childProvider;
                        for (ITestResultsWriter writer : resultWriters) {
                            writer.enterTestGoup(currentTest.getTestResult().type);
                        }
                    }
                }
                currentTest = null;
                while (currentTest == null && currentProvider != null) {
                    currentTest = currentProvider.createNextTest(options);
                    if (currentTest == null) {
                        currentProvider = currentProvider.getParentProvider();
                        for (ITestResultsWriter writer : resultWriters) {
                            writer.exitTestGoup();
                        }
                    }
                }
                if (currentTest != null) {
                    QApplication.postEvent(this, new StartCurrentTestEvent());
                } else {
                    QApplication.postEvent(this, new NoMoreTestsEvent());
                }
            } catch (Throwable ex) {
                processException(ex);
            }
        } else if (event instanceof NoMoreTestsEvent) {
            try {                
                testerEnvironment.restore();
                finished.emit();
            } catch (Throwable ex) {
                processException(ex);
            }
            finally{
                QApplication.removePostedEvents(this, QEvent.Type.User.value());
            }
        } else {
            super.customEvent(event);
        }
    }

    private void processException(Throwable ex) {
        testerEnvironment.getEnvironment().processException(ex);
    }

    public void startTesting(TestsProvider rootProvider) {
        if (options == null) {
            throw new IllegalUsageError(TesterConstants.WARNING_NO_OPTIONS.getTitle());
        }
        currentProvider = rootProvider;
        testerEnvironment.init();
        QApplication.postEvent(this, new GoToNextTestEvent());
    }

    public void finishTesting() {
        if (currentTest != null) {
            currentTest.interrupt();
        }
        currentTest = null;
        currentProvider = null;
    }

    public void setTestsOptions(TestsOptions newOptions) {
        options = newOptions;
    }

    public TestsOptions getTestsOptions() {
        return options;
    }

    public void addResultsWriter(ITestResultsWriter writer) {
        resultWriters.add(writer);
    }

    public void clearResultWriters() {
        resultWriters.clear();
    }

    public void processTraceCheck(TestResult testResult, boolean withErrors, boolean validTime) {
        if (testerEnvironment.getTraceBuffer().asList().size() > 0) {
            List<ExplorerTraceItem> items = testerEnvironment.getTraceBuffer().asList();
            if (withErrors) {
                writeAllEventsToResult(testResult, items);
            } else {
                boolean stop = false;
                boolean hasWarning = false;
                boolean hasErrors = false;
                int i = 0;
                while (i < items.size() && !stop) {
                    ExplorerTraceItem t = items.get(i);

                    if (!hasWarning && t.getSeverity()==EEventSeverity.WARNING) {
                        hasWarning = true;
                    }
                    if (!hasErrors && (t.getSeverity()==EEventSeverity.ERROR
                            || t.getSeverity()==EEventSeverity.ALARM)) {
                        hasErrors = true;
                    }

                    if (hasErrors || hasWarning) {
                        stop = true;
                    } else {
                        i++;
                    }
                }

                if (hasErrors) {
                    testResult.setHasErrors();
                } else if (hasWarning) {
                    testResult.setHasWarnings();
                }
                if (stop || !validTime) {
                    writeAllEventsToResult(testResult, items);
                }
            }
        }
    }

    private void writeAllEventsToResult(TestResult testResult, List<ExplorerTraceItem> items) {
        for (ExplorerTraceItem t : items) {
            testResult.writeTraceEventToLog(t);
        }
    }

    public void processMessageFilterCheck(TestResult testResult) {
        TesterMessageFilter filter = ((TesterMessageFilter) testerEnvironment.getMessageFilter());
        List<TestResultEvent> log = filter.getLog();
        if (log.size() > 0) {
            for (TestResultEvent err : log) {
                testResult.writeEventToLog(err);
            }
            filter.clearLog();
            testResult.setHasInfoMessages();
        }
    }

    public static void processThrowable(IClientEnvironment environment, Throwable ex, TestResult testResult) {
        final boolean isInterruption = ex instanceof InterruptedException
                || (ex instanceof CantOpenSelectorError && ex.getCause() instanceof InterruptedException);

        TestResultEvent testError = new TestResultEvent(ClientException.getExceptionReason(environment.getMessageProvider(), ex), ex.getClass().getSimpleName(), ClientException.exceptionStackToString(ex));
        if (isInterruption) {
            testResult.result = TesterConstants.RESULT_FAIL_INTERRUPTED.getTitle();
        } else {
            testResult.result = TesterConstants.RESULT_FAIL_EXCEPTION.getTitle();
        }
        testResult.writeEventToLog(testError);
        testResult.setHasErrors();
    }

    public static class TesterMessageFilter extends MessageFilter {

        private List<TestResultEvent> log = new ArrayList<TestResultEvent>();
        private final IClientEnvironment environment;

        public TesterMessageFilter(IClientEnvironment environment) {
            this.environment = environment;
        }

        public List<TestResultEvent> getLog() {
            return this.log;
        }

        public void clearLog() {
            this.log.clear();
        }

        @Override
        public QMessageBox.StandardButton beforeMessageBox(String title, String message, QMessageBox.Icon icon, QMessageBox.StandardButtons buttons) {
            String type = icon.name();
            log.add(new TestResultEvent(message, type, ""));
            if (buttons.isSet(QMessageBox.StandardButton.Yes)) {
                return QMessageBox.StandardButton.Yes;
            }
            return QMessageBox.StandardButton.Ok;
        }

        @Override
        public boolean beforeMessageException(String title, String message, String detail, Throwable exception) {
            log.add(new TestResultEvent(ClientException.getExceptionReason(environment.getMessageProvider(), exception), exception.getClass().getSimpleName(), ClientException.exceptionStackToString(exception)));
            return false;
        }
    }
}
