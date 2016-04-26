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

import com.trolltech.qt.gui.QWidget;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.explorer.env.trace.ExplorerTraceItem;
import org.radixware.kernel.explorer.tester.tests.TestResultEvent;
import org.radixware.kernel.explorer.tester.tests.TestResult;


class TestResultItem {

    final List<TestResultEvent> errorsLog;
    final List<ExplorerTraceItem> traceLog;
    private final QWidget parent;
    private final IClientEnvironment environment;

    public TestResultItem(final TestResult result, final  QWidget parent) {
        this.parent = parent;
        this.environment = result.getenvironment();
        this.errorsLog = result.getEventsLog();
        this.traceLog = result.getTraceEventsLog();
    }

    public TestResultItem(final IClientEnvironment environment, final List<TestResultEvent> eventList, final  List<ExplorerTraceItem> traceList, final  QWidget parent) {
        this.parent = parent;
        this.errorsLog = eventList;
        this.traceLog = traceList;
        this.environment = environment;
    }

    public void callResultDialog() {
        ErrorsLogDialog e = new ErrorsLogDialog(environment, parent, errorsLog, traceLog);
        e.exec();
    }
}
