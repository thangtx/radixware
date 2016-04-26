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

import com.trolltech.qt.core.Qt;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.tree.nodes.IExplorerTreeNode;
import org.radixware.kernel.explorer.tester.TesterConstants;
import org.radixware.kernel.explorer.tester.TesterEngine;
import org.radixware.kernel.explorer.tester.TesterEnvironment;
import org.radixware.kernel.explorer.tester.TestsOptions;
import org.radixware.kernel.explorer.tester.providers.EditorTabTests;
import org.radixware.kernel.explorer.tester.providers.SelectorTests;
import org.radixware.kernel.explorer.tester.providers.TestsProvider;

import org.radixware.kernel.explorer.views.Editor;
import org.radixware.kernel.explorer.views.IExplorerView;
import org.radixware.kernel.explorer.views.selector.Selector;


public class OpenViewTest implements ITest {

    private IExplorerTreeNode node;
    private IExplorerView view;
    private Model model;
    private boolean interrupted = false;
    private boolean failed = false;
    private TestResult testResult;

    public OpenViewTest(IExplorerTreeNode treeNode) {
        node = treeNode;
        initTestResult(treeNode.getExplorerTree().getEnvironment(), treeNode);
    }

    public OpenViewTest(EntityModel model) {
        this.model = model;
        initTestResult(model.getEnvironment(), model);
    }

    public OpenViewTest(IClientEnvironment environment, IExplorerView view) {
        this.view = view;
        initTestResult(environment, view);
    }

    private void initTestResult(IClientEnvironment environment, Object testObject) {
        testResult = new TestResult(environment, testObject);
        testResult.operation = TesterConstants.TEST_OPENING.getTitle();
    }

    @Override
    public void execute(TestsOptions options, TesterEnvironment environment) {
        try {
            if (view != null) {
                if (interrupted) {
                    return;
                }
                view.asQWidget().setAttribute(Qt.WidgetAttribute.WA_DeleteOnClose, true);
                if (view.getModel().getView() == null) {
                    view.open(view.getModel());
                    testResult.result = TesterConstants.RESULT_OPENING_SCS.getTitle();
                } else {
                    testResult.result = TesterConstants.RESULT_FAIL_ALREADY_OPENED.getTitle();
                }

            } else if (node != null || model != null) {
                model = node != null ? node.getView().getModel() : model;

                if (model instanceof EntityModel) {
                    //reread data for entity model
                    final EntityModel entity = (EntityModel) model;
                    if (!entity.isNew()) {
                        entity.clean();
                        entity.read();
                    }
                }
                if (interrupted) {
                    return;
                }

                view = (IExplorerView) model.createView();
                if (view != null) {
                    view.asQWidget().setAttribute(Qt.WidgetAttribute.WA_DeleteOnClose, true);
                    view.open(model);

                    testResult.result = TesterConstants.RESULT_OPENING_SCS.getTitle();
                }
            }
        } catch (Throwable ex) {
            TesterEngine.processThrowable(testResult.getenvironment(), ex, testResult);
            failed = true;
        }
    }

    @Override
    public TestResult getTestResult() {
        return testResult;
    }

    @Override
    public TestsProvider createChildTestsProvider(TestsProvider parentProvider, TestsOptions options) {
        if (!failed) {
            if (view instanceof Selector) {
                return new SelectorTests(parentProvider.getEnvironment(), parentProvider, (Selector) view);
            } else if (view instanceof Editor && options.testPages) {
                return new EditorTabTests(parentProvider.getEnvironment(), parentProvider, (Editor) view);
            }
        }
        return null;
    }

    @Override
    public void interrupt() {
        interrupted = true;
        testResult.getenvironment().getEasSession().breakRequest();
    }
}
