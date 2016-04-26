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

package org.radixware.kernel.designer.common.dialogs.scmlnb.finder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.swing.SwingUtilities;
import org.openide.awt.StatusDisplayer;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProviderFactory;
import org.radixware.kernel.designer.common.dialogs.results.RadixObjectsLookuper;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;


public class FindInSources extends RadixObjectsLookuper {

    private final IFindInSourcesCfg cfg;
    private int occurrencesCount = 0;

    public FindInSources(final IFindInSourcesCfg cfg) {
        this.cfg = cfg;
    }

    @Override
    protected String getProcessName() {
        return "Searching for occurrences...";
    }

    @Override
    protected String getPreparingProcessName() {
        return "Prepearing for searching...";
    }

    @Override
    protected void process(final RadixObject radixObject) {
        if (cfg.getAcceptor().accept(radixObject)) {
            List<IOccurrence> occurrences = new ArrayList<IOccurrence>();
            for (IOccurrence occurrence : cfg.getFinder(radixObject).list(radixObject)) {
                occurrences.add(occurrence);
            }
            if (!occurrences.isEmpty()) {
                accept(occurrences);
            }
        }

    }

    private void accept(final Collection<IOccurrence> occurrences) {
        occurrencesCount += occurrences.size();
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                final FindInSourcesResults resultsWindow = FindInSourcesResults.findInstance();
                for (IOccurrence occurance : occurrences) {
                    resultsWindow.add(occurance);
                }
            }
        });
    }
    private static final Lock LOCK = new ReentrantLock();

    private void prepare() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                FindInSourcesResults resultsWindow = FindInSourcesResults.findInstance();
                resultsWindow.clear();
                resultsWindow.open();
                resultsWindow.requestVisible();
                resultsWindow.requestActive();
            }
        });
    }

    private void complete() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                if (occurrencesCount > 0) {
                    StatusDisplayer.getDefault().setStatusText(occurrencesCount + " occurrences found.");
                }
                final FindInSourcesResults resultsWindow = FindInSourcesResults.findInstance();
                resultsWindow.requestActive();
                if (resultsWindow.isEmpty()) {
                    DialogUtils.messageInformation("No occurrences found.");
                }
            }
        });
    }

    private void find() {
        if (LOCK.tryLock()) {
            try {
                prepare();
                doFind();
                complete();
            } finally {
                LOCK.unlock();
            }
        } else {
            DialogUtils.messageError("Search is already running.");
        }
    }

    private void doFind() {
        lookup(cfg.getRoots(), VisitorProviderFactory.createDefaultVisitorProvider());
    }

    public static void find(final IFindInSourcesCfg cfg) {
        new FindInSources(cfg).find();
    }
}
