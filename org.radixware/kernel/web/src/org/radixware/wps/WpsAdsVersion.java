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

package org.radixware.wps;

import java.util.Set;
import org.radixware.kernel.common.client.env.AdsVersion;
import org.radixware.kernel.common.client.IClientApplication;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.views.IProgressHandle;
import org.radixware.kernel.starter.meta.RevisionMeta;
import org.radixware.kernel.starter.radixloader.EActualizeAction;
import org.radixware.kernel.starter.radixloader.IActualizeController;
import org.radixware.kernel.starter.radixloader.RadixLoader;
import org.radixware.kernel.starter.radixloader.RadixLoaderException;
import org.radixware.schemas.product.Directory.FileGroups.FileGroup.GroupType;


class WpsAdsVersion extends AdsVersion {

    private final static Object SEMAPHORE = new Object();
    private static volatile boolean IS_KERNEL_CHANGED = false;
    private static volatile long FIXED_LATEST_VERSION = -1;

    final private static class ActualizeController implements IActualizeController {

        private final boolean allowKernelChanges;
        private boolean kernelChanged = false;
        private long newVersion = -1;

        public ActualizeController(final boolean allowKernelChanges) {
            this.allowKernelChanges = allowKernelChanges;
        }

        public long getNewVersion() {
            return newVersion;
        }

        public boolean kernelWasChanged() {
            return kernelChanged;
        }

        @Override
        public EActualizeAction canUpdateTo(RevisionMeta revisionMeta, Set<String> modifiedFiles, Set<String> removedFiles, Set<String> changedGroups) {
            newVersion = revisionMeta.getNum();
            if (!allowKernelChanges && changedGroups != null && isKernelChanged(changedGroups)) {
                kernelChanged = true;
                return EActualizeAction.POSTPONE;
            }
            return EActualizeAction.PERFORM_ACTUALIZATION;
        }

        private static boolean isKernelChanged(final Set<String> changedGroups) {
            return changedGroups.contains(GroupType.KERNEL_WEB.toString())
                    || changedGroups.contains(GroupType.KERNEL_COMMON.toString());
        }
    }
    
    public WpsAdsVersion(final IClientApplication env, final long targetVersion){
        super(env, targetVersion);
    }

    @Override
    protected void restartApplication() {
    }

    @Override
    protected void versionNumberUpdated() {
    }

    @Override
    public void setTargetVersion(final long version) {
        super.setTargetVersion(version);
        if (isKernelWasChanged()) {
            synchronized (SEMAPHORE) {
                if (!IS_KERNEL_CHANGED) {
                    IS_KERNEL_CHANGED = true;
                    FIXED_LATEST_VERSION = getNumber();
                }
            }
        }
    }

    @Override
    protected IProgressHandle startChangeAnalyse(IClientEnvironment context) {
        return context.getProgressHandleManager().newProgressHandle();
    }

    @Override
    protected void finishChangeAnalyse(IProgressHandle ph) {
        ph.finishProgress();
    }

    public static boolean isKernelModified() {
        synchronized (SEMAPHORE) {
            return IS_KERNEL_CHANGED;
        }
    }
    private static volatile boolean isActualizing = false;

    public static boolean isActualizing() {
        return isActualizing;
    }

    public static long actualize(final boolean allowKernelChanges, final long versionNumber) throws RadixLoaderException {
        final ActualizeController acontroller = new ActualizeController(allowKernelChanges);
        RadixLoader.getInstance().setActualizeController(acontroller);
        if (versionNumber>-1){
            RadixLoader.getInstance().actualize(versionNumber, null, null, null, null);
        }else{
            RadixLoader.getInstance().actualize();
        }
        if (acontroller.kernelWasChanged()) {
            synchronized (SEMAPHORE) {
                IS_KERNEL_CHANGED = true;
                FIXED_LATEST_VERSION = RadixLoader.getInstance().getCurrentRevision();
                return FIXED_LATEST_VERSION;
            }
        }
        if (acontroller.getNewVersion() == -1) {//version was not changed
            return RadixLoader.getInstance().getCurrentRevision();
        }
        return acontroller.getNewVersion();
    }

    public static long getLatestVersionNumber() throws RadixLoaderException {
        synchronized (SEMAPHORE) {
            if (IS_KERNEL_CHANGED) {
                return FIXED_LATEST_VERSION;
            }
        }
        return actualize(false, -1);
    }

    @Override
    protected boolean confirmToRestart() {
        WpsEnvironment environment = WpsEnvironment.getEnvironmentStatic();
        if (environment != null) {
            environment.messageError(environment.getMessageProvider().translate("AdsVersionMessages", "Version update is not available until restart of Web Presentation server"));
        }
        return false;
    }
}
