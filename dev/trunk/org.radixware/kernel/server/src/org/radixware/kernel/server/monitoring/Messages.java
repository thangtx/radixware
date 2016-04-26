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

package org.radixware.kernel.server.monitoring;

import java.util.ResourceBundle;


public final class Messages {

    private Messages() {
    }

    static {
        final ResourceBundle bundle = ResourceBundle.getBundle("org.radixware.kernel.server.monitoring.mess.messages");

        ERR_IN_DB_QRY = bundle.getString("ERR_IN_DB_QRY");
        ERR_ON_DB_CONNECTION_CLOSE = bundle.getString("ERR_ON_DB_CONNECTION_CLOSE");
        ERR_UNSUPPORTED_METRIC_KIND = bundle.getString("ERR_UNSUPPORTED_METRIC_KIND");
        ERR_ON_GET_MONITORED_SECTIONS = bundle.getString("ERR_ON_GET_MONITORED_SECTIONS");
        ERR_ON_DB_QRY_CLOSE = bundle.getString("ERR_ON_DB_QRY_CLOSE");
    }
    
    public static final String ERR_ON_DB_QRY_CLOSE;
    public static final String ERR_IN_DB_QRY;
    public static final String ERR_ON_DB_CONNECTION_CLOSE;
    public static final String ERR_UNSUPPORTED_METRIC_KIND;
    public static final String ERR_ON_GET_MONITORED_SECTIONS;
}
