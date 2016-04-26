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

package org.radixware.kernel.explorer.tester.tests;

import org.radixware.kernel.common.client.models.FilterModel;
import org.radixware.kernel.explorer.env.Application;

import org.radixware.kernel.explorer.tester.TesterConstants;
import org.radixware.kernel.explorer.tester.TesterEngine;
import org.radixware.kernel.explorer.tester.TesterEnvironment;
import org.radixware.kernel.explorer.tester.TestsOptions;
import org.radixware.kernel.explorer.tester.providers.TestsProvider;
import org.radixware.kernel.explorer.views.selector.Selector;


public class FilterSingleTest implements ITest {

    private boolean interrupted = false;
    private FilterModel filter;
    private Selector selector;
    private FilterSingleTestResult testResult;

    public FilterSingleTest(FilterModel filter, Selector selector) {
        this.filter = filter;
        this.testResult = new FilterSingleTestResult(filter);
        this.testResult.operation = "";
        this.selector = selector;
    }

    @Override
    public TestsProvider createChildTestsProvider(TestsProvider parentProvider, TestsOptions options) {
        return null;
    }

    @Override
    public void execute(TestsOptions options, TesterEnvironment env) {
        try {
            if (interrupted) {
                return;
            }
            selector.getGroupModel().setFilter(filter);
            testResult.result = TesterConstants.RESULT_FILTERS_SCS.getTitle();
        } catch (Throwable ex) {
            TesterEngine.processThrowable(filter.getEnvironment(), ex, testResult);
        }
    }

    @Override
    public void interrupt() {
        interrupted = true;
        filter.getEnvironment().getEasSession().breakRequest();
    }

    @Override
    public TestResult getTestResult() {
        return testResult;
    }
}
