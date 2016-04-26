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

import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.models.ITestableModel;
import org.radixware.kernel.explorer.tester.TesterConstants;
import org.radixware.kernel.explorer.tester.TesterEngine;
import org.radixware.kernel.explorer.tester.TesterEnvironment;
import org.radixware.kernel.explorer.tester.TestsOptions;
import org.radixware.kernel.explorer.tester.providers.TestsProvider;


public class CustomTest implements ITest {

    private boolean interrupted = false;
    private CustomTestResult testResult;
    private ITestableModel model;
    private IClientEnvironment environment;

    public CustomTest(IClientEnvironment environment, ITestableModel model) {
        this.model = model;
        this.environment = environment;
        testResult = new CustomTestResult(environment, model);
        testResult.operation = TesterConstants.TEST_CUSTOM.getTitle();
    }

    @Override
    public TestsProvider createChildTestsProvider(TestsProvider parentProvider, TestsOptions options) {
        return null;
    }

    @Override
    public void execute(TestsOptions options, TesterEnvironment env) {
        try {
            if (model != null) {
                if (interrupted) {
                    return;
                }
                model.runTest();

                testResult.result = TesterConstants.RESULT_CUSTOM_SCS.getTitle();
            }
        } catch (Throwable ex) {
            TesterEngine.processThrowable(environment, ex, testResult);
        }
    }

    @Override
    public TestResult getTestResult() {
        return testResult;
    }

    @Override
    public void interrupt() {
        interrupted = true;
        environment.getEasSession().breakRequest();
    }
}
