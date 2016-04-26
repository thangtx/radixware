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

import java.io.File;
import org.radixware.kernel.common.enums.ERadixApplication;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;


public class SystemTools {

    public final static boolean isWindows;
    public final static boolean isOS2;
    public final static boolean isOSX;
    public final static boolean isBSD;
    public final static boolean isOpenVMS;
    public final static boolean isSunOS;
    public final static boolean isLinux;

    static {
        final String osName = System.getProperty("os.name");
        boolean windows = osName != null && osName.toLowerCase().indexOf("windows") >= 0;
        if (!windows && osName != null) {
            windows = osName.toLowerCase().indexOf("os/2") >= 0;
            isOS2 = windows;
        } else {
            isOS2 = false;
        }
        isWindows = windows;
        isOSX = !isWindows && osName != null && osName.toLowerCase().indexOf("mac os x") >= 0;
        isLinux = !isWindows && osName != null && osName.toLowerCase().indexOf("linux") >= 0;
        isBSD = !isWindows && !isLinux && osName != null && osName.toLowerCase().indexOf("bsd") >= 0;
        isOpenVMS = osName != null && !isWindows && !isOSX && !isBSD && osName.toLowerCase().indexOf("openvms") >= 0;
        isSunOS = osName != null && !isWindows && !isOSX && !isBSD && !isOpenVMS && osName.toLowerCase().indexOf("sunos") >= 0;
    }

    public static boolean isUnix() {
        return isOSX || isBSD || isOpenVMS || isLinux || isSunOS;
    }

    public static File getApplicationDataPath(final String name) {
        if (isWindows) {
            String app_data = System.getenv("APPDATA");
            File data_path;
            if (app_data == null) {
                data_path = new File(new File(System.getProperty("user.home")), "Application Data");
            } else {
                data_path = new File(app_data);
            }
            return new File(data_path, name);
        } else if (isOpenVMS) {
            return new File("/sys$login", "." + name).getAbsoluteFile();
        }
        return new File(System.getProperty("user.home"), "." + name);
    }
    //yremizov
    //private static final String RDX_ORG = "radixware.org";

    public static File getRadixApplicationDataPath(final ERadixApplication application) {
        return new File(getApplicationDataPath("radixware.org"), application.getValue());
    }

    public static File getRadixApplicationDataPath(final ERuntimeEnvironmentType runtimeType) {
        return new File(getApplicationDataPath("radixware.org"), runtimeType.getValue());
    }
}
