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

package org.radixware.kernel.starter.utils;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import org.radixware.kernel.starter.Starter;


public class SystemTools {

    public final static boolean isWindows;
    public final static boolean isOS2;
    public final static boolean isOSX;
    public final static boolean isBSD;
    public final static boolean isOpenVMS;
    public final static boolean isSunOS;
    public final static boolean isLinux;
    public final static boolean isAix;
    private static final String nativeLibDirName;

    static {
        String osName = System.getProperty("os.name");
        boolean windows = osName != null && osName.toLowerCase().indexOf("windows") >= 0;
        if (!windows && osName != null) {
            windows = osName.toLowerCase().indexOf("os/2") >= 0;
            isOS2 = windows;
        } else {
            isOS2 = false;
        }
        isWindows = windows;
        isOSX = !isWindows && osName != null && osName.toLowerCase().indexOf("mac") >= 0;
        isLinux = !isWindows && osName != null && osName.toLowerCase().indexOf("linux") >= 0;
        isBSD = !isWindows && !isLinux && osName != null && osName.toLowerCase().indexOf("bsd") >= 0;
        isOpenVMS = osName != null && !isWindows && !isOSX && !isBSD && osName.toLowerCase().indexOf("openvms") >= 0;
        isSunOS = osName != null && !isWindows && !isOSX && !isBSD && !isOpenVMS && osName.toLowerCase().indexOf("sunos") >= 0;
        isAix = osName != null && !isWindows && !isOSX && !isBSD && !isOpenVMS && !isSunOS && osName.toLowerCase().indexOf("aix") >= 0;

        if (isWindows) {
            nativeLibDirName = System.getProperty("os.arch").equalsIgnoreCase("amd64") ? "win-i-64" : "win-i-32";
        } else if (isLinux) {
            if (System.getProperty("os.arch").equalsIgnoreCase("i386")) {
                nativeLibDirName = "linux-i-32";
            } else if (System.getProperty("os.arch").equalsIgnoreCase("amd64")) {
                nativeLibDirName = "linux-i-64";
            } else if (System.getProperty("os.arch").equalsIgnoreCase("ppc64")) {
                nativeLibDirName = "linux-ppc-64";
            } else {
                nativeLibDirName = null;
            }
        } else if (isOSX) {
            nativeLibDirName = "macosx-i-64";
        } else if (isBSD) {
            nativeLibDirName = "bsd-i"; // TODO ? 32 and 64
        } else if (isOpenVMS) {
            nativeLibDirName = "openvms-i";// TODO ? 32 and 64
        } else if (isSunOS) {
            //TODO: for SPARC return sunos-s-32, sunos-s-64
            nativeLibDirName = "sunos-i"; // TODO ? 32 and 64
        } else if (isAix) {
            nativeLibDirName = "aix-ppc-64";
        } else {
            nativeLibDirName = null;
        }
    }

    public static File getTmpDir() {
        final File tmp = new File(getApplicationDataPath(Starter.STARTER_APP_DATA_ROOT) + "/tmp");
        if (!tmp.exists()) {
            tmp.mkdirs();
        }
        return tmp;
    }

    public static File getApplicationDataPath(String name) {
        if (isWindows) {
            String app_data = System.getenv("APPDATA");
            File data_path;
            if (app_data == null) {
                data_path = new File(new File(System.getProperty("user.home")), "Application Data");
            } else {
                data_path = new File(app_data);
            }
            return new File(data_path, name);
        }
        final String linuxFolderName = "." + name;
        if (isOpenVMS) {
            return new File("/sys$login", linuxFolderName).getAbsoluteFile();
        }
        return new File(System.getProperty("user.home"), linuxFolderName);
    }

    public static String getNativeLibDirShortName() {
        if (nativeLibDirName == null) {
            throw new UnsupportedOperationException("OS " + System.getProperty("os.name") + " on " + System.getProperty("os.arch") + " is not supported");
        }
        return nativeLibDirName;
    }

    public static String getCommonNativeLibLayerPath(final String libName) {
        return getNativeLibLayerPath("common", libName);
    }

    public static String getExplorerNativeLibLayerPath(final String libName) {
        return getNativeLibLayerPath("explorer", libName);
    }

    public static String getServerNativeLibLayerPath(final String libName) {
        return getNativeLibLayerPath("server", libName);
    }

    public static String getNativeLibLayerPath(final String kernelModuleName, final String libName) {
        return "kernel/" + kernelModuleName + "/lib/native/" + getNativeLibDirShortName() + "/" + System.mapLibraryName(libName);
    }

    public static String getSystemIndependentNativeLibName(final String fullLibName) {
        if (isWindows) {
            if (fullLibName.endsWith(".dll")) {
                return fullLibName.substring(0, fullLibName.length() - 4);
            } else {
                throw new IllegalArgumentException("Invalid native library name: '" + fullLibName + "'");
            }
        } else {
            if (fullLibName.startsWith("lib") && fullLibName.endsWith(".so")) {
                return fullLibName.substring(3, fullLibName.length() - 3);
            } else {
                throw new IllegalArgumentException("Invalid native library name: '" + fullLibName + "'");
            }
        }
    }

    public static int getCurrentProcessPid() {
        final String jvmName = ManagementFactory.getRuntimeMXBean().getName();
        final int index = jvmName.indexOf('@');

        if (index < 1) {
            throw new IllegalStateException("Unable to determine current process pid from jvm name: '" + jvmName + "'");
        }

        return Integer.parseInt(jvmName.substring(0, index));
    }

    public static Set<Integer> getRunningProcessPids() {
        final String command = isWindows ? "tasklist.exe /FO CSV /NH" : "ps -e";
        final Set<Integer> pids = new HashSet<>();
        try {
            final Process p = Runtime.getRuntime().exec(command);
            final Scanner sc = new Scanner(p.getInputStream());
            while (sc.hasNextLine()) {
                final String line = sc.nextLine().trim();
                if (!line.isEmpty()) {
                    try {
                        if (isWindows) {
                            pids.add(Integer.valueOf(line.split(",")[1].replaceAll("\"", "")));
                        } else {
                            pids.add(Integer.valueOf(line.split(" ")[0]));
                        }
                    } catch (Exception ex) {
                        //header line - skip
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
            return Collections.emptySet();
        }
        return Collections.unmodifiableSet(pids);
    }
}
