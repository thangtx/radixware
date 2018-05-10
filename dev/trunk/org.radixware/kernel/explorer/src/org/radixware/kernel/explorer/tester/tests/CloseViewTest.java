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
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.explorer.tester.TesterConstants;
import org.radixware.kernel.explorer.tester.TesterEngine;
import org.radixware.kernel.explorer.tester.TesterEnvironment;
import org.radixware.kernel.explorer.tester.TestsOptions;
import org.radixware.kernel.explorer.tester.providers.TestsProvider;
import org.radixware.kernel.explorer.views.IExplorerView;


public class CloseViewTest implements ITest {

    IExplorerView view = null;
    EntityModel model = null;
    final IClientEnvironment environment;
    private CloseTestResult testResult;
    private boolean interrupted = false;

    public CloseViewTest(IClientEnvironment environment, final IExplorerView view) {
        this.view = view;
        this.environment = environment;
        initTestResult(environment, view);
    }

    public CloseViewTest(final EntityModel model) {
        this.model = model;
        environment = model.getEnvironment();
        initTestResult(environment, model);
    }

    private void initTestResult(IClientEnvironment environment, Object testObject) {
        testResult = new CloseTestResult(environment, testObject);
        testResult.operation = TesterConstants.TEST_CLOSING.getTitle();
    }

    @Override
    public void execute(TestsOptions options, TesterEnvironment env) {
        try {
            if (interrupted) {
                return;
            }

            if (view == null && model != null) {
                view = (IExplorerView) model.getEntityView();
                initTestResult(model.getEnvironment(), view);
            }
            
            boolean res;
            if (model!=null && model instanceof EntityModel==false){
                model.clean();//clean model data and close model view                
                res = true;
            }else{
                if (view!=null){
                    res = view.close(false);
                    if (!res) {
                        res = view.close(true);
                    }
                }else{
                    res = false;
                }
            }

            if (res) {
                testResult.result = TesterConstants.RESULT_CLOSING_SCS.getTitle();
            } else {
                testResult.result = TesterConstants.RESULT_CLOSING_FAIL.getTitle();
            }

        } catch (Throwable ex) {
            TesterEngine.processThrowable(environment, ex, testResult);
        }
    }

    @Override
    public void interrupt() {
        interrupted = true;
        environment.getEasSession().breakRequest();
    }

    @Override
    public TestResult getTestResult() {
        return testResult;
    }

    @Override
    public TestsProvider createChildTestsProvider(TestsProvider parentProvider, TestsOptions options) {
        return null;
    }
}
