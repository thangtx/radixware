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

import org.openide.modules.ModuleInstall;
import org.openide.util.Exceptions;


public class Installer extends ModuleInstall {

    @Override
    protected boolean clearSharedData() {
        return true;
    }

    // @Override
    // public void restored() {
    //     super.restored();
    // }
    @Override
    public void close() {
        try {
            super.close();

            UserExtensionManager.getInstance().cleanup();
        } finally {
            doClose();
        }
    }

    @Override
    public boolean closing() {
        return UserExtensionManager.getInstance().canClose();
    }

    private void doClose() {
        final Object holder = Config.getValue("org.radixware.devtool.holder");
        final int timeout;
        final Object timeoutObj = Config.getValue("timeout");
        if (timeoutObj instanceof Integer) {
            timeout = (Integer) timeoutObj;
        } else {
            timeout = 3000;
        }
        if (holder instanceof Runnable) {
            if (timeout <= 0) {
                ((Runnable) holder).run();
            } else {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //try {
                        try {
                            Thread.sleep(timeout);
                        } catch (InterruptedException ex) {
                            Exceptions.printStackTrace(ex);
                        }
                        ((Runnable) holder).run();
                        //} finally {
                        //}
                    }
                });
                t.setDaemon(true);
                t.setName("Killer thread");
                t.start();
            }

        }
    }
}
