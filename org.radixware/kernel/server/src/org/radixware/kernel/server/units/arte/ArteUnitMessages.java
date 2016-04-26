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

package org.radixware.kernel.server.units.arte;

import java.util.ResourceBundle;


public final class ArteUnitMessages {

    private ArteUnitMessages() {
    }

    static {
        final ResourceBundle bundle = ResourceBundle.getBundle("org.radixware.kernel.server.units.arte.mess.messages");

        ARTE_UNIT_TYPE_TITLE = bundle.getString("ARTE_UNIT_TYPE_TITLE");
        HIGH_ARTE_INST_COUNT = bundle.getString("HIGH_ARTE_INST_COUNT");
        LOW_ARTE_INST_COUNT = bundle.getString("LOW_ARTE_INST_COUNT");
        ADDRESS = bundle.getString("ADDRESS");
        SERVICE_URI = bundle.getString("SERVICE_URI");
        SAP = bundle.getString("SAP");
        SECURITY_PROTOCOL = bundle.getString("SECURITY_PROTOCOL");
        SERVER_KEY_ALIASES = bundle.getString("SERVER_KEY_ALIASES");
        CLIENT_CERT_ALIASES = bundle.getString("CLIENT_CERT_ALIASES");
        CHECK_CLIENT_CERT = bundle.getString("CHECK_CLIENT_CERT");
        SSL_CONTEXT_CREATION_ERROR = bundle.getString("SSL_CONTEXT_CREATION_ERROR");
        SERVER_KEYSTORE_WAS_REREAD = bundle.getString("SERVER_KEYSTORE_WAS_REREAD");
        SERVER_KEYSTORE_MODIFICATION_TIME = bundle.getString("SERVER_KEYSTORE_MODIFICATION_TIME");
        SOCKET_LISTENS = bundle.getString("SOCKET_LISTENS");
        //ERR_CANT_REGISTER_FREE_SOCKET = bundle.getString("ERR_CANT_REGISTER_FREE_SOCKET");
        ERR_ON_ACCEPTED_SOCKET_CLOSE = bundle.getString("ERR_ON_ACCEPTED_SOCKET_CLOSE");
        ERR_ON_SOCKET_START = bundle.getString("ERR_ON_SOCKET_START");
        ERR_ON_SOCKET_CLOSE = bundle.getString("ERR_ON_SOCKET_CLOSE");
        ERR_ON_SOCKET_IO = bundle.getString("ERR_ON_SOCKET_IO");
        //RESTARTING_SERVER_SOCKET=bundle.getString("RESTARTING_SERVER_SOCKET");
        //ERR_CANT_GET_ARTE_VIEW=bundle.getString("ERR_CANT_GET_ARTE_VIEW");
        //TITLE_ARTE_INST=bundle.getString("TITLE_ARTE_INST");
        _STARTED = bundle.getString("_STARTED");
        _STOPPED = bundle.getString("_STOPPED");

        ERR_ON_SERVICE_RQ_PROC = bundle.getString("ERR_ON_SERVICE_RQ_PROC");
        ERR_ON_ARTE_INST_STOP = bundle.getString("ERR_ON_ARTE_INST_STOP");
        ERR_ON_ARTE_INST_INIT = bundle.getString("ERR_ON_ARTE_INST_INIT");
        INSUF_ARTE_COUNT = bundle.getString("INSUF_ARTE_COUNT");

        _USAGE_ = bundle.getString("_USAGE_");

        TAB_TRACE = bundle.getString("TAB_TRACE");
        TAB_STATISTIC = bundle.getString("TAB_STATISTIC");
        TAB_ARTE_INSTS = bundle.getString("TAB_ARTE_INSTS");
        //ERR_CANT_REUSE_FREE_SOCKET = bundle.getString("ERR_CANT_REUSE_FREE_SOCKET");
        //ERR_CANT_FLUSH_EVENT_LOG = bundle.getString("ERR_CANT_FLUSH_EVENT_LOG");
        CANT_INIT_ARTE = bundle.getString("CANT_INIT_ARTE");

        LBL_USAGE = bundle.getString("LBL_USAGE");

        TITLE_ERROR = bundle.getString("TITLE_ERROR");
        ARTE_IS_NOT_STOPPED = bundle.getString("ARTE_IS_NOT_STOPPED");

        ANY = bundle.getString("ANY");
        SUITE_ANY = bundle.getString("SUITE_ANY"); 
        SUITE_ANY_STRONG = bundle.getString("SUITE_ANY_STRONG");
        CIPHER_SUITES = bundle.getString("CIPHER_SUITES");
        KEPT_CONNECTION_FOR_READ_NOT_FOUND = bundle.getString("KEPT_CONNECTION_FOR_READ_NOT_FOUND");
        ARTE_INST_USAGE_LIMIT_EXCEEDED = bundle.getString("ARTE_INST_USAGE_LIMIT_EXCEEDED");
        THREAD_PRIORITY = bundle.getString("THREAD_PRIORITY");
    }
    static final String ARTE_INST_USAGE_LIMIT_EXCEEDED;
    static final String KEPT_CONNECTION_FOR_READ_NOT_FOUND;
    static final String TITLE_ERROR;
    static final String ARTE_IS_NOT_STOPPED;
    static final String THREAD_PRIORITY;
    static final String ARTE_UNIT_TYPE_TITLE;
    static final String HIGH_ARTE_INST_COUNT;
    static final String LOW_ARTE_INST_COUNT;
    static final String ADDRESS;
    static final String SERVICE_URI;
    static final String SAP;
    static final String ANY;
    static final String SUITE_ANY;
    static final String SUITE_ANY_STRONG;
    static final String CIPHER_SUITES;
    static final String SECURITY_PROTOCOL;
    static final String SERVER_KEY_ALIASES;
    static final String CLIENT_CERT_ALIASES;
    static final String CHECK_CLIENT_CERT;
    static final String SSL_CONTEXT_CREATION_ERROR;
    static final String SERVER_KEYSTORE_WAS_REREAD;
    static final String SERVER_KEYSTORE_MODIFICATION_TIME;
    static final String SOCKET_LISTENS;
    static final String ERR_ON_ACCEPTED_SOCKET_CLOSE;
    final static String ERR_ON_SOCKET_START;
    final static String ERR_ON_SOCKET_CLOSE;
    final static String ERR_ON_SOCKET_IO;
    //static final String RESTARTING_SERVER_SOCKET;
    //static final String ERR_CANT_GET_ARTE_VIEW;
    //static final String ERR_CANT_FLUSH_EVENT_LOG;
    //static final String TITLE_ARTE_INST;
    static final String _STOPPED;
    static final String _STARTED;
    public static final String CANT_INIT_ARTE;
    static final String ERR_ON_SERVICE_RQ_PROC;
    static final String ERR_ON_ARTE_INST_STOP;
    static final String ERR_ON_ARTE_INST_INIT;
    static final String INSUF_ARTE_COUNT;
    static final String _USAGE_;
    static final String TAB_TRACE;
    static final String TAB_STATISTIC;
    static final String TAB_ARTE_INSTS;
    //static final String ERR_CANT_REGISTER_FREE_SOCKET;
    //static final String ERR_CANT_REUSE_FREE_SOCKET;
    static final String LBL_USAGE;
    static final String MLS_ID_CLIENT_CONNECTED = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsD4OJQF5MNVAWLNHPV7FWT2NX6E";//MLS "Client \"%2\" connected to \"%1\"", EEventSeverity.EVENT, EEventSource.ARTE_COMMUNICATOR
    static final String MLS_ID_CLIENT_DISCONNECTED = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsSJZ5AOEF3NEYLA5PJZKVYZKN6I";// MLS "Client \"%2\" disconnected from \"%1\"", EEventSeverity.EVENT, EEventSource.ARTE_COMMUNICATOR
    static final String MLS_ID_ERR_CANT_FLUSH_EVENT_LOG = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mls5VEPBY2DPNCUTGVFMNLND7HVSM"; //MLS "Can't write events of  \"%1\" ARTE instance to database: %2", Error, ARTE_UNIT
    static final String MLS_ID_ERR_CANT_GET_ARTE_VIEW = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mls3ZUOTJAJJFE73FNE7YYTH32Z7U"; // MLS "Can't get view of \"%1\" ARTE instance: %2", Error, ARTE_UNIT
    static final String MLS_ID_ERR_ON_SOCKET_CLOSE = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsQN7NJUGYTVDOZDBOVZZEYV6WFU";// MLS "Can't close server socket of \"%1\" unit: %2", Error, ARTE_UNIT
    static final String MLS_ID_SOCKET_LISTENS = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsX3JB5G35VBBWDEQJ4QMXTSDTCM";// MLS "\"%1\" ARTE unit listens %2", Error, ARTE_UNIT
    static final String MLS_ID_ERR_ON_SOCKET_START = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mls7D4OVIGAIFB6LDWS53JEQ4P27Q"; //MLS "Can't start server socket of \"%1\": %2", Error, EEventSource.ARTE_COMMUNICATOR
    static final String MLS_ID_XXX_STARTED = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsAALJQDTWUFFUTBK5H6WLI35A6U"; // MLS "\"%1\" started", Event, ARTE_UNIT
    static final String MLS_ID_XXX_STOPPED = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsCXKQ7ZDH45GVNJLE4UDPTEOBY4"; // MLS "\"%1\" stopped", Event, ARTE_UNIT
    static final String MLS_ID_ERR_ON_SOCKET_IO = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsA3VMJBFDB5G2PJ5HBKEZRHQYEM"; // MLS "\"%1\" server socket IO error: %2", EEventSeverity.ERROR, EEventSource.ARTE_COMMUNICATOR
    static final String MLS_ID_ERR_ON_SERVICE_RQ_PROC = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsBBZPMQ7LTNCINN6XWELWUEXJMA";// MLS "\"%1\" ARTE service error: %2", EEventSeverity.ERROR, EEventSource.ARTE;
    static final String MLS_ID_ERR_ON_ACCEPTED_SOCKET_CLOSE = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mls77UT2J2UINHNZMH6ES6LQ3OK7E";// MLS "\"%1\" can't close accepted socket: %2", EEventSeverity.WARNING, EEventSource.ARTE_COMMUNICATOR
    static final String MLS_ID_INSUF_ARTE_COUNT = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsGVK53K2MINGYBEJPIE4NV7LLEY";// MLS "Unit \"%1\" has insufficient ARTE instances count", Warning, ARTE_UNIT
    static final String MLS_ID_ERR_ON_ARTE_INST_INIT = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsN5BQ7VKUIZFSRND2L56RBBU6WM";// MLS "Unit \"%1\" can't init new ARTE instance: %2", Error, ARTE_UNIT
    static final String MLS_ID_ERR_ON_ARTE_INST_STOP = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mls4FR67PQAU5A2XNUKNCZ3XSNVHY";// MLS "Error on ARTE instance \"%1\" stopping: %2", Error, ARTE_UNIT
    public static final String MLS_ID_CANT_INIT_ARTE = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsSLRTXPRVABEXXH3TYLRLJSYEZ4"; //MLS "Can't initialize ARTE \"%1\": %2", Error, EEventSource.ARTE_UNIT
    public static final String MLS_ID_KEPT_CONNECTION_NOT_FOUND = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsW453NQ7VZFHSXLV362ORIHVR5U";//MLS "Kept connection not found for received data", Error, ARTE_UNIT
    public static final String MLS_ID_ARTE_INST_USAGE_LIMIT_EXCEEDED = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mls45R77NRZ2VCTPOKZE3GNR4H24U";//MLS: "ARTE instance usage limit exceeded", Warning, ARTE_UNIT
}
