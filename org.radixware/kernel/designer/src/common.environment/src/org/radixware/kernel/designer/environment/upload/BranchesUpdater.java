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

package org.radixware.kernel.designer.environment.upload;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import org.openide.util.RequestProcessor;
import org.openide.util.Task;
import org.openide.windows.WindowManager;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.general.upload.PostUploadAction;

/**
 * Allows to lister changes in branches directories in backgroud thread and
 * update instances of Branches.
 *
 */
public class BranchesUpdater extends Task {

    private static BranchesUpdater INSTANCE = null;
    private static final int PERIOD_MS = 2000;
    private final Set<Branch> branchesToScan = new HashSet<>();
    private final Set<Branch> branchesToUpload = new HashSet<>();
    private static final Logger LOGGER = Logger.getLogger("org.radixware.kernel.designer.environment.upload.BranchesUpdater");

    private BranchesUpdater() {
        super();
    }

    public static BranchesUpdater getDefault() {
        synchronized (BranchesUpdater.class) {
            if (INSTANCE == null) {
                AbstractRadixObjectUploaderUI.INSTANCE = new DefaultRadixObjectUploaderUI();
                INSTANCE = new BranchesUpdater();
                RequestProcessor.getDefault().post(INSTANCE, 220); // 220 - from Netbeans sources, allows to start in another thread.
            }
            return INSTANCE;
        }
    }

    @Override
    public String toString() {
        return "RadixWare Designer Branches Scanner and Uploader";
    }

    public synchronized void put(Branch branch) {
        branchesToScan.add(branch);
        branchesToUpload.remove(branch); // remove, not add
    }

    public synchronized void remove(Branch branch) {
        branchesToScan.remove(branch);
        branchesToUpload.remove(branch);
    }

    private synchronized Branch getNextBranchToScan() {
        final Iterator<Branch> iterator = branchesToScan.iterator();
        if (iterator.hasNext()) {
            final Branch branch = iterator.next();
            iterator.remove();
            branchesToUpload.add(branch);
            return branch;
        } else {
            return null;
        }
    }

    private synchronized Set<Branch> getBranchesToUpload() {
        return new HashSet<>(branchesToUpload);
    }
    private volatile boolean netbeansActive = false;

    private boolean isNetbeansActive() {
        if (netbeansActive) {
            netbeansActive = false;
            return true;
        }

        SwingUtilities.invokeLater(new Runnable() { // unable to use invokeAndWait - deadlock with user action (unknown reason, but fact).
            @Override
            public void run() {
                netbeansActive = WindowManager.getDefault().getMainWindow().isActive();
            }
        });
        return false;
    }

    private void scan(Branch branch) {
        LOGGER.fine("Branch scanning started");
        long t = System.currentTimeMillis();
        final BranchScanner scanner = new BranchScanner(branch);
        scanner.scan();
        t = System.currentTimeMillis() - t;
        LOGGER.fine("Branch scanning completed in " + t + " msecs.");
    }

    private void upload(Branch branch) {
        LOGGER.fine("Branch uploading started");
        long t = System.currentTimeMillis();
        final BranchUploader branchUploader = new BranchUploader(branch);
        branchUploader.update();
        t = System.currentTimeMillis() - t;
        LOGGER.fine("Branch uploading completed in " + t + " msecs.");
    }

    @Override
    public void run() {
        try {
            AbstractRadixObjectUploaderUI.getDefault().resetIgnoreAll();
            while (true) {
                // scan and move to upload set
                for (Branch branch = getNextBranchToScan(); branch != null; branch = getNextBranchToScan()) {
                    scan(branch);
                }
                if (isNetbeansActive()) {
                    for (Branch branch : getBranchesToUpload()) {
                        upload(branch);
                    }
                }
                PostUploadAction.getInstance().process();
                TimeUnit.MILLISECONDS.sleep(PERIOD_MS);
            }
        } catch (InterruptedException ex) {            
        } catch (final Throwable ex) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    DialogUtils.messageError(ex);
                    throw ex;
                }
            });
            
        }
        
    }
}
