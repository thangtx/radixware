/*
 * Copyright (c) 2008-2018, Compass Plus Limited. All rights reserved.
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
import java.io.InputStream;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import org.apache.commons.lang.StringUtils;
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
    
    private static List<NetworkInterface> getAllNetworkInterfaces() {
        try {
            return Collections.list(NetworkInterface.getNetworkInterfaces());
        } catch (SocketException ex) {
            return Collections.emptyList();
        }
    }
    
    private static List<NetworkInterface> getNetworkInterfaces() {
        final List<NetworkInterface> all = getAllNetworkInterfaces();
        final List<NetworkInterface> result = new ArrayList<>();
        for (NetworkInterface nif: all) {
            try {
                if (nif.isUp() && !nif.isLoopback()) {
                    result.add(nif);
                }
            } catch (SocketException ex) {
            }
        }
        return result;
    }
    
    private static List<InetAddress> getInetAddresses(boolean ipv4, boolean ipv6) {
        final List<NetworkInterface> nifList = getNetworkInterfaces();
        final List<InetAddress> addresses = new ArrayList<>();
        for (NetworkInterface nif : nifList) {
            final List<InetAddress> nifAddresses = Collections.list(nif.getInetAddresses());
            for (InetAddress addr: nifAddresses) {
                if (!addr.isLinkLocalAddress() && !addr.isLoopbackAddress() && !addr.isMulticastAddress()
                        && (ipv4 && addr instanceof Inet4Address || ipv6 && addr instanceof Inet6Address)) {
                    addresses.add(addr);
                }
            }
        }
        return addresses;
    }
    
    private static List<InetAddress> getInet4Addresses() {
        return getInetAddresses(true, false);
    }
    
    public static List<String> getIp4Addresses() {
        final List<InetAddress> addresses = getInet4Addresses();
        final List<String> ipList = new ArrayList<>(addresses.size());
        for (InetAddress addr: addresses) {
            ipList.add(addr.getHostAddress());
        }
        return ipList;
    }
    
    private static String getJavaNetHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException ex) {
        }
        return null;
    }
    
    private static Process getProcessForOsComamnd(String command) {
        try {
            return Runtime.getRuntime().exec(command);
        } catch (Throwable ex) {
        }
        return null;
    }
    
    private static void closeOsProcess(Process p) {
        if (p == null) {
            return;
        }
        
        try {
            p.getErrorStream().close();
        } catch (Throwable e) {
        }
        try {
            p.getOutputStream().close();
        } catch (Throwable e) {
        }
        try {
            p.getInputStream().close();
        } catch (Throwable e) {
        }
        
        try {
            p.destroy();
        } catch (Throwable e) {
        }
    }
        
    private static String execOsCommand(String command) {
        String result = null;
        final Process p = getProcessForOsComamnd(command);
        try (final InputStream is = p == null ? null : p.getInputStream()) {
            try (final Scanner sc = is == null ? null : new Scanner(is)) {
                result = sc != null && sc.hasNext() ? sc.nextLine() : null;
            }
        } catch (Throwable ex) {
        } finally {
            closeOsProcess(p);
        }
        return result;
    }
    
    public static String getHostName() {
        
        final String[] probableHostNames = new String[] {
            execOsCommand("hostname"),
            execOsCommand("cat /etc/hostname"),
            System.getenv("COMPUTERNAME"),
            System.getenv("HOSTNAME"),
            getJavaNetHostName()
        };
        
        for (String hostName: probableHostNames) {
            if (StringUtils.isNotBlank(hostName) && !hostName.equalsIgnoreCase("localhost")) {
                return hostName;
            }
        }
        
        for (String hostName : probableHostNames) {
            if (StringUtils.isNotBlank(hostName) && hostName.equalsIgnoreCase("localhost")) {
                return "localhost";
            }
        }
        
        return null;
    }
}
