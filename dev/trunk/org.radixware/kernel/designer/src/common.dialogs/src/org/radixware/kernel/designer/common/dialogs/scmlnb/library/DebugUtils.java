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

package org.radixware.kernel.designer.common.dialogs.scmlnb.library;

import java.io.IOException;
import java.util.logging.LogManager;
import org.openide.util.Exceptions;


public class DebugUtils {

    public static void enableFocusLog() {
        try {
            System.setProperty("java.awt.focus.Component.level", "100");
            System.setProperty("sun.awt.X11.focus.XComponentPeer", "100");
            LogManager.getLogManager().readConfiguration();
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        } catch (SecurityException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    public static void disableFocusLog() {
        try {
            System.setProperty("java.awt.focus.Component.level", "900");
            System.setProperty("sun.awt.X11.focus.XComponentPeer", "900");
            LogManager.getLogManager().readConfiguration();
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        } catch (SecurityException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
}
