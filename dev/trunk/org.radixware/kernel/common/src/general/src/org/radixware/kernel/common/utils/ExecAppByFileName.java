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

package org.radixware.kernel.common.utils;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.exceptions.DesktopNotSupported;


public class ExecAppByFileName {

    /**
     * probably don't work at Linux
     */
    public static void open(File file) throws IOException, DesktopNotSupported {
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().open(file);
        } else {
            throw new DesktopNotSupported();
        }
    }

//    @Deprecated
    public static void exec(String fileName) {
        Runtime rt = Runtime.getRuntime();
        Process p = null;
        try {
            OutputStream stream = null;
            if (SystemTools.isWindows) {
                p = rt.exec("cmd");
                stream = p.getOutputStream();
                stream.write(("start " + fileName + "\n").getBytes());
                stream.flush();
                stream.write("exit\n".getBytes());
                stream.flush();
                p.waitFor();
            } else if (SystemTools.isLinux || SystemTools.isUnix()) {
                p = rt.exec("xdg-open " + fileName);
                p.waitFor();
            } else {
                System.out.println("OS not supported ");
            }
        } catch (Exception e) {
            System.out.println("Runtime error with message: " + e);
        } finally {
            if (p != null) {
                try {
                    p.getErrorStream().close();
                } catch (IOException e) {
                    Logger.getLogger(ExecAppByFileName.class.getName()).log(Level.SEVERE, null, e);
                }
                try {
                    p.getOutputStream().close();
                } catch (IOException e) {
                    Logger.getLogger(ExecAppByFileName.class.getName()).log(Level.SEVERE, null, e);
                }
                try {
                    p.getInputStream().close();
                } catch (IOException e) {
                    Logger.getLogger(ExecAppByFileName.class.getName()).log(Level.SEVERE, null, e);
                }
                p.destroy();

            }
        }
    }
}
