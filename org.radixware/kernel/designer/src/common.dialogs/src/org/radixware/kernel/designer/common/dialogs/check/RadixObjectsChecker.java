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

package org.radixware.kernel.designer.common.dialogs.check;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.SwingUtilities;
import org.openide.awt.StatusDisplayer;
import org.openide.util.Exceptions;
import org.radixware.kernel.common.builder.check.common.CheckOptions;
import org.radixware.kernel.common.builder.check.common.Checker;
import org.radixware.kernel.common.check.RadixProblemRegistry;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProviderFactory;
import org.radixware.kernel.common.defs.ads.platform.PlatformLib;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.dialogs.results.RadixObjectsLookuper;
import org.radixware.kernel.designer.common.general.utils.RadixMutex;

/**
 * Allows to check Radix objects, visual representation is abstract.
 *
 */
class RadixObjectsChecker extends RadixObjectsLookuper {

    private String processName;
    private long startTimeMillis;
    private final Checker checker;

    protected RadixObjectsChecker(CheckOptions options) {
        super();
        this.checker = new Checker(options);
    }

    @Override
    protected String getPreparingProcessName() {
        return "Preparing to check...";
    }

    @Override
    protected String getProcessName() {
        synchronized (this) {
            return processName;
        }
    }

    private void prepare(Collection<? extends RadixObject> contexts) {
        startTimeMillis = System.currentTimeMillis();

        if (contexts != null && contexts.size() > 0) {
            if (contexts.size() > 1) {
                processName = MessageFormat.format("Checking {0} objects", contexts.size());
            } else {
                processName = MessageFormat.format("Checking {0}", contexts.iterator().next().getQualifiedName());
            }
        } else {
            processName = "Nothing to check";
        }

        RadixProblemRegistry.getDefault().clear(contexts);
    }

    private void complete() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                final CheckResultsTopComponent checkResults = CheckResultsTopComponent.findInstance();
                final String status;
                if (!checkResults.isEmpty()) {
                    checkResults.open();
                    checkResults.requestVisible();
                    checkResults.requestActive();
                    status = "Check complete for " + DialogUtils.millisToString(System.currentTimeMillis() - startTimeMillis);
                } else {
                    //status = "Check complete for " + (System.currentTimeMillis() - startTimeMillis) + " msec.";
                    status = "No problems found during check";
                }
                StatusDisplayer.getDefault().setStatusText(status);
            }
        });
    }

    public void check(Collection<? extends RadixObject> contexts) {
        if (RadixMutex.getLongProcessLock().tryLock()) {

            try {
                RadixObject.disableChangeTracking();
                PlatformLib.enableClassCaching(true);
                prepare(contexts);
                super.lookup(contexts, VisitorProviderFactory.createCheckVisitorProvider(), false);
                complete();
            } finally {
                RadixObject.enableChangeTracking();
                PlatformLib.enableClassCaching(false);
                RadixMutex.getLongProcessLock().unlock();
            }

        } else {
            DialogUtils.messageError("Check or build is already running.");
        }
    }

    @Override
    protected final void process(final RadixObject radixObject) {
        RadixMutex.readAccess(new Runnable() {
            @Override
            public void run() {
                if (radixObject.isInBranch()) {
                    checker.checkRadixObject(radixObject);
                }

            }
        });
    }
}
