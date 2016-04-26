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

package org.radixware.kernel.reporteditor.common;

import java.io.IOException;
import java.util.concurrent.Executors;
import org.netbeans.core.startup.RunLevel;
import org.openide.LifecycleManager;
import org.openide.util.Exceptions;
import org.openide.util.NbPreferences;
import org.openide.util.lookup.ServiceProvider;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;


@ServiceProvider(service = RunLevel.class)
public class UserExtRunLevel implements RunLevel {

    private static class Killer implements Runnable {

        @Override
        public void run() {
            Executors.newFixedThreadPool(1).submit(new Runnable() {

                @Override
                public void run() {
                    LifecycleManager.getDefault().exit();
                }
            });
        }
    }

   // public UserExtRunLevel() {
    //}
    public static final String WWWB  = "WWWBrowser"; // NOI18N
      public static final  String WWWEB  = "ExternalWWWBrowser"; // NOI18N

    @Override
    public void run() {
        try {                       
            Config.putValue("org.radixware.devtool.killer", new Killer());           
            if (!UserExtensionManager.getInstance().openReports()) {
                LifecycleManager.getDefault().exit();
            }            
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
            DialogUtils.messageError(ex);
            LifecycleManager.getDefault().exit();
        }
    }
}
