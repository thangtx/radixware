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
package org.radixware.kernel.license;

import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

public class LicenseManager {

    public static final long INIT_TIME_NANOS = System.nanoTime();

    private static class LicenseClientHolder {

        public static final ILicenseClient CLIENT;

        static {
            final ServiceLoader<ILicenseClient> loader = ServiceLoader.load(ILicenseClient.class, LicenseManager.class.getClassLoader());
            final Iterator<ILicenseClient> iterator = loader.iterator();
            if (iterator.hasNext()) {
                CLIENT = loader.iterator().next();
            } else {
                CLIENT = null;
            }
        }
    }

    private LicenseManager() {
    }

    public static void touch() {
        //just set init_time
    }

    public static void start(final ILicenseEnvironment licenseEnvironment) {
        if (LicenseClientHolder.CLIENT != null) {
            LicenseClientHolder.CLIENT.scheduleStart(licenseEnvironment);
        }
    }

    public static CountDownLatch stop() {
        if (LicenseClientHolder.CLIENT != null) {
            return LicenseClientHolder.CLIENT.scheduleStop();
        } else {
            return new CountDownLatch(0);
        }
    }

    public static CountDownLatch destroy() {
        if (LicenseClientHolder.CLIENT != null) {
            return LicenseClientHolder.CLIENT.scheduleDestroy();
        } else {
            return new CountDownLatch(0);
        }
    }

    public static byte[] processRequest(final byte[] encryptedRequest) {
        if (LicenseClientHolder.CLIENT != null) {
            return LicenseClientHolder.CLIENT.processRequest(encryptedRequest);
        } else {
            return new byte[]{};
        }
    }

    public static List<ILicenseInfo> getDefinedLicenses(final String licenseServerAddress) throws IOException {
        if (LicenseClientHolder.CLIENT != null) {
            return LicenseClientHolder.CLIENT.getDefinedLicenses(licenseServerAddress);
        } else {
            return Collections.emptyList();
        }
    }

    public static void setDelegateLogger(final Logger logger) {
        LicenseManagerLog.setDelegate(logger);
    }

    public static void reportError(final String message) {
        LicenseManagerLog.error(message);
    }
  
    public static ILicenseReport newReport() {
        if (LicenseClientHolder.CLIENT != null) {
            return LicenseClientHolder.CLIENT.newReport();
        }
        return null;
    }

    public static void sendReport(final Date reportDate, final Reader data, final Connection dbConnection) throws IOException {
        if (LicenseClientHolder.CLIENT != null) {
            LicenseClientHolder.CLIENT.sendReport(reportDate, data, dbConnection);
        }
    }
}
