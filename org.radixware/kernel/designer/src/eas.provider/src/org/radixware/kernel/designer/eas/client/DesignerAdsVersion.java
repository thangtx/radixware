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

package org.radixware.kernel.designer.eas.client;

import java.util.Collection;
import org.netbeans.api.progress.ProgressUtils;
import org.openide.awt.StatusLineElementProvider;
import org.openide.util.Lookup;
import org.radixware.kernel.common.client.IClientApplication;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.AdsVersion;
import org.radixware.kernel.common.client.views.IProgressHandle;


class DesignerAdsVersion extends AdsVersion {

    public DesignerAdsVersion(IClientApplication env) {
        super(env);
    }

    @Override
    protected void restartApplication() {
    }

    @Override
    protected void versionNumberUpdated() {
    }

    @Override
    protected IProgressHandle startChangeAnalyse(IClientEnvironment ice) {
        ProgressHandleImpl impl = new ProgressHandleImpl();
        impl.startProgress("Looking for changes...", false);
        return impl;
    }

    @Override
    protected void finishChangeAnalyse(IProgressHandle iph) {
        iph.finishProgress();
    }

    @Override
    public void setTargetVersion(long version) {
        super.setTargetVersion(version);
        updateOldVersionButton(new UpdateVersionButton.Status() {

            @Override
            public boolean isVisible() {
                return true;
            }

            @Override
            public void apply() {
                Thread updaterThread = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        ProgressUtils.showProgressDialogAndRun(new Runnable() {

                            @Override
                            public void run() {
                                switchToTargetVersion();
                            }
                        }, "Update to New Version...");

                    }
                });
                updaterThread.setName("Update ADS version");
                updaterThread.setDaemon(true);
                updaterThread.start();

            }
        });
    }

    @Override
    protected void afterSwitchVersion(final Integer oldClassLoaderId) {
        super.afterSwitchVersion(oldClassLoaderId);
        updateOldVersionButton(new UpdateVersionButton.Status() {

            @Override
            public boolean isVisible() {
                return false;
            }

            @Override
            public void apply() {
            }
        });        
    }        

    private void updateOldVersionButton(UpdateVersionButton.Status status) {
        Collection<? extends StatusLineElementProvider> all =
                Lookup.getDefault().lookupAll(StatusLineElementProvider.class);
        for (StatusLineElementProvider a : all) {
            if (a instanceof UpdateVersionButton) {
                ((UpdateVersionButton) a).refresh(status);
            }
        }
    }
}
