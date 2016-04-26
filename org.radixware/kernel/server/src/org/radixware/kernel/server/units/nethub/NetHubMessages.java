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

package org.radixware.kernel.server.units.nethub;

import java.util.ResourceBundle;


final class NetHubMessages {

    static {
        final ResourceBundle bundle = ResourceBundle.getBundle("org.radixware.kernel.server.units.nethub.mess.messages");

        REMOTE_ADDRESS = bundle.getString("REMOTE_ADDRESS");
        SAP = bundle.getString("SAP");
        TRACE_PROFILE = bundle.getString("TRACE_PROFILE");
        EXTPORT_RECV_TIMEOUT = bundle.getString("EXTPORT_RECV_TIMEOUT");
        SAP_RECV_TIMEOUT = bundle.getString("SAP_RECV_TIMEOUT");
        RECV_TIMEOUT_SEC = bundle.getString("RECV_TIMEOUT_SEC");
        SEND_TIMEOUT_SEC = bundle.getString("SEND_TIMEOUT_SEC");
        ERR_ON_SOCKET_START = bundle.getString("ERR_ON_SOCKET_START");
        ERR_ON_SOCKET_IO = bundle.getString("ERR_ON_SOCKET_IO");
        ERR_ON_EXT_RQ_PROC = bundle.getString("ERR_ON_EXT_RQ_PROC");
        ERR_DUPLICATE_RQ = bundle.getString("ERR_DUPLICATE_RQ");
        ERR_LATE_RS = bundle.getString("ERR_LATE_RS");
        ERR_EXT_HOST_REJECT = bundle.getString("ERR_EXT_HOST_REJECT");
        ERR_ENCRYPT_DECRYPT_MESSAGE = bundle.getString("ERR_ENCRYPT_DECRYPT_MESSAGE");

        EXT_PORT_OPTIONS = bundle.getString("EXT_PORT_OPTIONS");
        ECHO_TEST_PERIOD_SEC = bundle.getString("ECHO_TEST_PERIOD_SEC");
        RECONNECT_NO_ECHO_COUNT = bundle.getString("RECONNECT_NO_ECHO_COUNT");
        OUT_REQ_COUNT = bundle.getString("OUT_REQ_COUNT");
        IN_REQ_COUNT = bundle.getString("IN_REQ_COUNT");

        FRAME = bundle.getString("FRAME");
        CLIENT_SOCKET = bundle.getString("CLIENT_SOCKET");
        SRV_SOCKET = bundle.getString("SRV_SOCKET");
        ERR_IN_RQ_LIMIT_EXEEDED = bundle.getString("ERR_IN_RQ_LIMIT_EXEEDED");
        ERR_OUT_RQ_LIMIT_EXEEDED = bundle.getString("ERR_OUT_RQ_LIMIT_EXEEDED");
        EV_EXT_HOST_CONNECTED = bundle.getString("EV_EXT_HOST_CONNECTED");
        EV_EXT_HOST_DISCONNECTED = bundle.getString("EV_EXT_HOST_DISCONNECTED");
        UNIT_TYPE_TITLE = bundle.getString("UNIT_TYPE_TITLE");
    }
    static final String UNIT_TYPE_TITLE;
    static final String ERR_EXT_HOST_REJECT;
    static final String EXT_PORT_OPTIONS;
    static final String ECHO_TEST_PERIOD_SEC;
    static final String RECONNECT_NO_ECHO_COUNT;
    static final String OUT_REQ_COUNT;
    static final String IN_REQ_COUNT;
    static final String REMOTE_ADDRESS;
    static final String SAP;
    static final String TRACE_PROFILE;
    static final String RECV_TIMEOUT_SEC;
    static final String SEND_TIMEOUT_SEC;
    static final String EXTPORT_RECV_TIMEOUT;
    static final String SAP_RECV_TIMEOUT;
    static final String ERR_IN_RQ_LIMIT_EXEEDED;
    static final String ERR_OUT_RQ_LIMIT_EXEEDED;
    static final String ERR_ON_SOCKET_START;
    static final String ERR_ON_SOCKET_IO;
    static final String ERR_ON_EXT_RQ_PROC;
    static final String ERR_DUPLICATE_RQ;
    static final String ERR_LATE_RS;
    static final String ERR_ENCRYPT_DECRYPT_MESSAGE;
    static final String FRAME;
    static final String CLIENT_SOCKET;
    static final String SRV_SOCKET;
    static final String EV_EXT_HOST_CONNECTED;
    static final String EV_EXT_HOST_DISCONNECTED;

    static final String MLS_ID_ERR_ON_SOCKET_START = "mlbaclQPCA3XD2BZAZ3ADC4BQZU2MDLM-mlsQKCN47IA4FHPTEHAWZF6KPR7WQ";			//MLS "Can't start external socket of %1: %2", Error, CDbpEventSource.NetHub
    static final String MLS_ID_ERR_ON_SOCKET_IO = "mlbaclQPCA3XD2BZAZ3ADC4BQZU2MDLM-mlsE6EYWA5GTREFZA4E3WWJSVRVOE";			//MLS "Socket IO Error in \"%1\" : %2", Error, CDbpEventSource.NetHub
    static final String MLS_ID_ERR_ON_EXT_RQ_PROC = "mlbaclQPCA3XD2BZAZ3ADC4BQZU2MDLM-mlsV4NXBYIMNJFOJC4OXBEJQOMDDQ";			//MLS "Error on external request procesing in \"%1\": %2", Error, CDbpEventSource.NetHub
    static final String MLS_ID_SAP_RECV_TIMEOUT = "mlbaclQPCA3XD2BZAZ3ADC4BQZU2MDLM-mlsVE2URHXMUFBQLDVKSVE5DFNBEA";			//MLS "Response from external system timeout was exceeded (STAN=%1)", Error, CDbpEventSource.NetHub
    static final String MLS_ID_EXTPORT_RECV_TIMEOUT = "mlbaclQPCA3XD2BZAZ3ADC4BQZU2MDLM-mlsBZWLEFFJKBA5LFPYM6NKY7OXYY";			//MLS "Receive timeout exceeded (STAN=%1)", Error, CDbpEventSource.NetHub
    static final String MLS_ID_ERR_CANT_READ_SERVICE_OPTIONS = "mlbaclQPCA3XD2BZAZ3ADC4BQZU2MDLM-mlsLX6NEI4I6BDXHLE7WHWNLQ752Y";	//MLS "Can't read service options of \"%1\": %2", Error, CDbpEventSource.NetHub
    static final String MLS_ID_ERR_DUPLICATE_RQ = "mlbaclQPCA3XD2BZAZ3ADC4BQZU2MDLM-mlsOLQPB4C5WZGT5DYPDN4K33PC4E";			//MLS "Duplicate request received from external port of \"%1\" : STAN=%2"
    static final String MLS_ID_ERR_LATE_RS = "mlbaclQPCA3XD2BZAZ3ADC4BQZU2MDLM-mlsLYXXGYB4EZHUHI5MDIPW7A2FWY";				//MLS "Late response detected in \"%1\""
    static final String MLS_ID_ERR_IN_RQ_LIMIT_EXEEDED = "mlbaclQPCA3XD2BZAZ3ADC4BQZU2MDLM-mlsS2A4H4U425AQRBIKU67LNQYQOM";		//MLS "External system request count limit(%1) exeeded in \"%2\""
    static final String MLS_ID_ERR_OUT_RQ_LIMIT_EXEEDED = "mlbaclQPCA3XD2BZAZ3ADC4BQZU2MDLM-mlsOO7AELILWNELLAF2DNSACWVMEQ";		//MLS "Request to external system count limit(%1) exeeded in \"%2\""
    static final String MLS_ID_ERR_EXT_HOST_REJECT = "mlbaclQPCA3XD2BZAZ3ADC4BQZU2MDLM-mls4TKBOUN5WBCX3FECXM37LKB3PY";			//MLS "External host rejects message of \"%1\" : STAN=%2"
    static final String MLS_ID_EV_EXT_HOST_CONNECTED = "mlbaclQPCA3XD2BZAZ3ADC4BQZU2MDLM-mlsZNX3KPRR4FBRVANPNYT4SGSXTY";		//MLS "Connection with the external system was established: %1"
    static final String MLS_ID_EV_EXT_HOST_DISCONNECTED = "mlbaclQPCA3XD2BZAZ3ADC4BQZU2MDLM-mlsAKLMU6TAHBFELE347TYELA3EB4";		//MLS "The external system disconnected"
}
