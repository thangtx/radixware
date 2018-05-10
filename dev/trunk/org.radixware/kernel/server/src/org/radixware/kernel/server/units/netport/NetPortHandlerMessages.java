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

package org.radixware.kernel.server.units.netport;

import org.radixware.kernel.server.utils.MessagesHelper;


final class NetPortHandlerMessages {

    private NetPortHandlerMessages() {
    }

    static {
        MessagesHelper.initialize(NetPortHandlerMessages.class, NetPortHandlerMessages.class.getPackage().getName() + ".mess.messages");
    }
    
    static volatile String MAX_AAS_SEANCES;
    static volatile String NPH_UNIT_TYPE_TYTLE;
    static volatile String TAB_TRACE;
    static volatile String TAB_NET_CHANNELS;
    static volatile String CONNECTION_WITH;
    static volatile String ADDRESS;
    static volatile String REMOTE_ADDRESS;
    static volatile String LINK_LEVEL_PROTOCOL;
    static volatile String IN_FRAME;
    static volatile String OUT_FRAME;
    static volatile String MAX_SESSION_COUNT;
    static volatile String CHANNEL_STARTED;
    static volatile String CHANNEL_STOPPED;
    static volatile String TRACE_PROFILE;
    static volatile String RECV_TIMEOUT_SEC;
    static volatile String SEND_TIMEOUT_SEC;
    static volatile String KEEP_CONNECT_TIMEOUT_SEC;
    static volatile String RECV_TIMEOUT;
    static volatile String ERR_CANT_CREATE_SSLCONTEXT;
    static volatile String ERR_ON_SOCKET_START;
    static volatile String ERR_ON_SOCKET_IO;
    static volatile String ERR_ON_NET_PORT_RQ_PROC;
    static volatile String ERR_ON_CHANNEL_START;
    static volatile String ERR_ON_CHANNEL_STOP;
    static volatile String ERR_ON_CHANNELS_REREAD;
    static volatile String ERR_MAX_SESSION_COUNT_EXCEEDED;
    static volatile String IS_LISTENER;
    static volatile String IS_CONNECT_READY_NTF_ON;
    static volatile String IS_DISCONNECT_NTF_ON;
    static volatile String WRITE_CONNECTED_STATE_TO_DB_ON;
    static volatile String ON;
    static volatile String OFF;
    static volatile String SECURITY_PROTOCOL;
    static volatile String SERVER_KEY_ALIASES;
    static volatile String CLIENT_CERT_ALIASES;
    static volatile String CHECK_CLIENT_CERT;
    static volatile String SERVER_KEYSTORE_MODIFICATION_TIME;
    static volatile String ANY;
    static volatile String ERR_ON_WRITE_CHANNEL_CONNECTED_STATE_TO_DB;
    static volatile String SYNC_MODE;

    static final String MLS_ID_ERR_ON_SOCKET_START = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsKJ3C3QBTFJFJPLIO7YW46FN6XU"; //MLS "Can't start server socket of \"%1\": %2", Error, EEventSource.NET_PORT_HANDLER
    static final String MLS_ID_ERR_ON_SOCKET_IO = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsIEKI34XTFVBW5DWH32PFJM7WF4";//MLS "%1: Socket IO Error of "%2": %3", Error, EEventSource.UNIT_PORT
    static final String MLS_ID_ERR_ON_NET_PORT_RQ_PROC = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mls6YGZELQJWFDX5DRE5DGTJI2KIE";//MLS "%1: Error on net port request procesing in "%2": %3", Error, EEventSource.UNIT_PORT
    static final String MLS_ID_CHANNEL_STARTED = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mls6G42ZQLHGBC5VNUJS4C7OQM4KY";//MLS "Net listener \"%1\" started: %2", Event, EEventSource.NET_PORT_HANDLER;
    static final String MLS_ID_CHANNEL_STOPPED = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsNHAW2AJTF5BHJFYPJ433MYCLDE";//MLS "Net listener \"%1\" stopped", Event, EEventSource.NET_PORT_HANDLER;
    static final String MLS_ID_ERR_ON_CHANNEL_START = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsEMCCA6QT5ZFILONNPTAX5SRXKE";//MLS "Can't start net listener \"%1\": %2", Error, EEventSource.NET_PORT_HANDLER
    static final String MLS_ID_ERR_ON_CHANNEL_STOP = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsXF3GPZ4MAJH7HGATEEO42ENNFI";//MLS "Can't stop net listener \"%1\": %2", Error, EEventSource.NET_PORT_HANDLER
    static final String MLS_ID_ERR_ON_CHANNELS_REREAD = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsV42TUPQFOREYJPX3FMU77EFUNI";//MLS "Net channels reread error: %1", Event, EEventSource.NET_PORT_HANDLER;
    static final String MLS_ID_RECV_TIMEOUT = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsAVA3K6DA5BB2XKIA4BMOZ7HVAQ";//MLS "Receive timeout exceeded", Error, EEventSource.NET_PORT_HANDLER
    static final String MLS_ID_ERR_MAX_SESSION_COUNT_EXCEEDED = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsQ3R37P5RVBFNPKX47FQ6X26S6E";//MLS "Maximal active session count exceeded: %1", Error, EEventSource.NET_PORT_HANDLER
    static final String MLS_ID_ERR_CANT_READ_SERVICE_OPTIONS = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsQNCITQ752RBBRCQ5OBTBX7YCOQ";//MLS "Can't read read service options of \"%1\": %2", Error, EEventSource.NET_PORT_HANDLER
    static final String MLS_ID_ERR_UNABLE_TO_WRITE_CHANNEL_CONNECTED_STATE_TO_DB = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsEOLELJ3KHFHKXFSEX5KXUXBNOY";//MLS "Unable to write channel connected state to database: %1\", Error, EEventSource.NET_PORT_HANDLER
}