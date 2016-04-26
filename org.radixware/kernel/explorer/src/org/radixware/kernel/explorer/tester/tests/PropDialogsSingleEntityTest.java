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

import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QWidget;
import java.util.Collection;
import org.radixware.kernel.common.client.meta.mask.EditMaskTimeInterval;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMaskTimeInterval.Scale;

import org.radixware.kernel.explorer.utils.DialogWatcher;

import org.radixware.kernel.explorer.tester.TesterConstants;
import org.radixware.kernel.explorer.tester.TesterEngine;
import org.radixware.kernel.explorer.tester.TesterEnvironment;
import org.radixware.kernel.explorer.tester.TestsOptions;
import org.radixware.kernel.explorer.tester.providers.TestsProvider;


public class PropDialogsSingleEntityTest implements ITest {

    private boolean interrupted = false;
    private PropDialogsSingleEntityTestResult testResult;
    private EntityModel model;

    public PropDialogsSingleEntityTest(EntityModel model) {
        this.model = model;
        this.testResult = new PropDialogsSingleEntityTestResult(model);
        this.testResult.operation = TesterConstants.TEST_SINGLE_ENTITY_PROPS.getTitle();
    }

    @Override
    public TestsProvider createChildTestsProvider(TestsProvider parentProvider, TestsOptions options) {
        return null;
    }

    private static class DialogHandler implements DialogWatcher.IDialogHandler {

        private boolean doneRejection = false;
        private EntityModel model;

        public DialogHandler(EntityModel model) {
            this.model = model;
        }

        @Override
        public boolean dialogAccepted(final QWidget dialog) {
            if (!model.getEnvironment().getEasSession().isBusy()) {
                return (dialog instanceof org.radixware.kernel.explorer.views.Dialog)
                        || (dialog instanceof org.radixware.kernel.explorer.dialogs.ExplorerDialog);
            }
            return false;
        }

        @Override
        public void doAction(final QWidget dialog) {
            if (dialog instanceof QDialog) {
                ((QDialog) dialog).reject();
                doneRejection = true;
            }
        }

        public boolean hasDoneRejection() {
            return this.doneRejection;
        }
    }

    @Override
    public void execute(TestsOptions options, TesterEnvironment env) {
        try {
            Collection<Property> props = model.getActiveProperties();

            final long timeBoundary = options.propDialogTimeBoundary;

            for (Property p : props) {
                if (p.isVisible()
                        && p.hasSubscriber()
                        && p.getDefinition().customDialog()) {
                    if (interrupted) {
                        return;
                    }
                    DialogWatcher watcher = DialogWatcher.getInstance();
                    DialogHandler handler = new DialogHandler(model);
                    watcher.invokeLater(handler);

                    long timeBefore = System.currentTimeMillis();
                    try{
                        p.execPropEditorDialog();
                    }finally{
                        watcher.stopWatcher();
                    }
                    long timeAfter = System.currentTimeMillis();
                    EditMaskTimeInterval timeInterval = new EditMaskTimeInterval(Scale.MILLIS.longValue(), "hh:mm:ss:zzzz", null, null);
                    Long interval = Long.valueOf(timeAfter - timeBefore);

                    final String timeResult = timeInterval.toStr(env.getEnvironment(), interval);

                    boolean goodTime = interval <= timeBoundary;

                    final String result;
                    if (handler.hasDoneRejection()) {
                        if (goodTime) {
                            result = TesterConstants.RESULT_PROPDIALOG_SCS.getTitle();
                        } else {
                            result = TesterConstants.RESULT_FAIL_TIME_LIMIT.getTitle();
                        }
                    } else {
                        result = TesterConstants.RESULT_PROPDIALOG_FAIL.getTitle();
                    }

                    this.testResult.addTestResult(p, result, timeResult);
                }
            }
            if (this.testResult.getTestedPropertiesCount() == 0) {
                this.testResult.result = TesterConstants.RESULT_PROPDIALOG_NOPROPS.getTitle();
            }

        } catch (Throwable ex) {
            TesterEngine.processThrowable(model.getEnvironment(), ex, testResult);
        }
    }

    @Override
    public TestResult getTestResult() {
        return this.testResult;
    }

    @Override
    public void interrupt() {
        this.interrupted = true;
        model.getEnvironment().getEasSession().breakRequest();
    }
}
