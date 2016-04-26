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

package org.radixware.kernel.server.sap;

import java.util.ResourceBundle;

/**
 * ��������� SAP
 *
 *
 */
final class Messages {

    private Messages() {
    }

    static {
        final ResourceBundle bundle = ResourceBundle.getBundle("org.radixware.kernel.server.sap.mess.messages");

        SERVICE_STARTED = bundle.getString("SERVICE_STARTED");
        SERVICE_STOPPED = bundle.getString("SERVICE_STOPPED");

        ERR_CANT_READ_SERVICE_OPTIONS = bundle.getString("ERR_CANT_READ_SERVICE_OPTIONS");
        ERR_IN_DB_QRY = bundle.getString("ERR_IN_DB_QRY");
        ERR_ON_DB_CONNECTION_CLOSE = bundle.getString("ERR_ON_DB_CONNECTION_CLOSE");
        ERR_ON_SERVICE_START = bundle.getString("ERR_ON_SERVICE_START");
        ERR_ON_CREATE_SSL_CONTEXT = bundle.getString("ERR_ON_CREATE_SSL_CONTEXT");

        SAP = bundle.getString("SAP");
        ADDRESS = bundle.getString("ADDRESS");
        SERVICE_URI = bundle.getString("SERVICE_URI");

        SECURITY_PROTOCOL = bundle.getString("SECURITY_PROTOCOL");
        SERVER_KEY_ALIASES = bundle.getString("SERVER_KEY_ALIASES");
        CLIENT_CERT_ALIASES = bundle.getString("CLIENT_CERT_ALIASES");
        CIPHER_SUITES = bundle.getString("CIPHER_SUITES");
        CHECK_CLIENT_CERT = bundle.getString("CHECK_CLIENT_CERT");
        EAS_KRB_AUTH = bundle.getString("EAS_KRB_AUTH");
        SERVER_KEYSTORE_MODIFICATION_TIME = bundle.getString("SERVER_KEYSTORE_MODIFICATION_TIME");
        
        SOAP_SERVICE_OPTIONS = bundle.getString("SOAP_SERVICE_OPTIONS");
        ADDITIONAL_ATTRIBUTES = bundle.getString("ADDITIONAL_ATTRIBUTES");

        ANY = bundle.getString("ANY");
        ANY_STRONG = bundle.getString("ANY_STRONG");
        ERR_UNHANDLED_IN_SERVICE = bundle.getString("ERR_UNHANDLED_IN_SERVICE");
    }
    static final String SERVICE_STARTED;
    static final String SERVICE_STOPPED;
    static final String ERR_CANT_READ_SERVICE_OPTIONS;
    static final String ERR_IN_DB_QRY;
    static final String ERR_ON_DB_CONNECTION_CLOSE;
    static final String ERR_ON_SERVICE_START;
    static final String ERR_ON_CREATE_SSL_CONTEXT;
    static final String SAP;
    static final String ADDRESS;
    static final String SERVICE_URI;
    static final String SECURITY_PROTOCOL;
    static final String SOAP_SERVICE_OPTIONS;
    static final String SERVER_KEY_ALIASES;
    static final String CIPHER_SUITES;
    static final String CLIENT_CERT_ALIASES;
    static final String CHECK_CLIENT_CERT;
    static final String EAS_KRB_AUTH;
    static final String SERVER_KEYSTORE_MODIFICATION_TIME;
    static final String ANY;
    static final String ANY_STRONG;
    static final String ADDITIONAL_ATTRIBUTES;
    static final String ERR_UNHANDLED_IN_SERVICE;
    static final String MLS_ID_ERR_IN_DB_QRY = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsJT5AKKAM4REIXF6RYW5I7PV6D4"; // MLS "Database query error: %1", ERROR, EEventSource.Instance
    static final String MLS_ID_ERR_ON_DB_CONNECTION_CLOSE = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsAKUGIU64BZEWBE2VM3PEEW5BKI"; // MLS "Database connection close error: %1", ERROR, EEventSource.Instance
    static final String MLS_ID_SERVICE_STARTED = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsAXE2ULLROJBILJW7IYVHPBJ26U";// MLS "Service started: %1", EVENT, EEventSource.Instance
    static final String MLS_ID_SERVICE_STOPPED = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsQMLF6QYN35HV3NXJFEH6MDPZCA";// MLS "Service stopped: %1", EVENT, EEventSource.Instance
    static final String MLS_ID_ERR_ON_SERVICE_START = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsY4YJWZMJ3BAUXCOICEEX7DMLHE";// MLS "Can't start service %1: %2", ERROR, EEventSource.Instance
}
