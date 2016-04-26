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

package org.radixware.kernel.server.units.jms;

import java.util.ResourceBundle;


final class JmsHandlerMessages {

    static {
        final ResourceBundle bundle = ResourceBundle.getBundle("org.radixware.kernel.server.units.jms.mess.messages");
        
        ERR_ON_JMS_START = bundle.getString("ERR_ON_JMS_START");
        ERR_ON_JMS_IO = bundle.getString("ERR_ON_JMS_IO");
        UNIT_TYPE_TITLE = bundle.getString("UNIT_TYPE_TITLE");
        SAP_RECV_TIMEOUT = bundle.getString("SAP_RECV_TIMEOUT");
        UNCORRELATED_RECV = bundle.getString("UNCORRELATED_RECV");
        
        JMS_MESS_FORMAT = bundle.getString("JMS_MESS_FORMAT");
        JMS_CONNECT_PROPS = bundle.getString("JMS_CONNECT_PROPS");
        JMS_MESS_PROPS = bundle.getString("JMS_MESS_PROPS");
        JMS_LOGIN = bundle.getString("JMS_LOGIN");
        JMS_PASSWORD = bundle.getString("JMS_PASSWORD");
        MS_RQ_QUEUE_NAME = bundle.getString("MS_RQ_QUEUE_NAME");
        MS_RS_QUEUE_NAME = bundle.getString("MS_RS_QUEUE_NAME");
        IN_SEANCE_CNT = bundle.getString("IN_SEANCE_CNT");
        OUT_SEANCE_CNT = bundle.getString("OUT_SEANCE_CNT");
        RS_TIMEOUT = bundle.getString("RS_TIMEOUT");

        ERR_IN_RQ_LIMIT_EXEEDED = bundle.getString("ERR_IN_RQ_LIMIT_EXEEDED");
        ERR_OUT_RQ_LIMIT_EXEEDED = bundle.getString("ERR_OUT_RQ_LIMIT_EXEEDED");
        ERR_ON_JMS_PROPS_PARSE = bundle.getString("ERR_ON_JMS_PROPS_PARSE");
    }
    
    static final String UNIT_TYPE_TITLE;
    static final String SAP_RECV_TIMEOUT;
    static final String UNCORRELATED_RECV;
    static final String JMS_MESS_FORMAT;
    static final String JMS_CONNECT_PROPS;
    static final String JMS_MESS_PROPS;
    static final String JMS_LOGIN;
    static final String JMS_PASSWORD;
    static final String MS_RQ_QUEUE_NAME;
    static final String MS_RS_QUEUE_NAME;
    static final String IN_SEANCE_CNT;
    static final String OUT_SEANCE_CNT;
    static final String RS_TIMEOUT;    

    static final String ERR_IN_RQ_LIMIT_EXEEDED;
    static final String ERR_OUT_RQ_LIMIT_EXEEDED;    
    static final String ERR_ON_JMS_START;
    static final String ERR_ON_JMS_IO;
    static final String ERR_ON_JMS_PROPS_PARSE;
    
    static final String MLS_ID_ERR_ON_JMS_START = "mlbaclMU4BLUT6FJCZLDQ2WL6SCH43NE-mlsWGRHNJL575FRRKLIK2VCUN3QDM";			//MLS "Can't start jms connection of %1: %2", Error, CDbpEventSource.JMSHandler
    static final String MLS_ID_ERR_ON_JMS_IO = "mlbaclMU4BLUT6FJCZLDQ2WL6SCH43NE-mlsTK4JYYAUQBBPZLAKYYIKZN4EFU";			//MLS "Jms send-receive error in \"%1\" : %2", Error, CDbpEventSource.JMSHandler
    static final String MLS_ID_ERR_IN_RQ_LIMIT_EXEEDED = "mlbaclMU4BLUT6FJCZLDQ2WL6SCH43NE-mlsFVGZTPQLB5CPBPABI5SYPSICH4";		//MLS "External system request count limit(%1) exeeded in \"%2\""
    static final String MLS_ID_ERR_OUT_RQ_LIMIT_EXEEDED = "mlbaclMU4BLUT6FJCZLDQ2WL6SCH43NE-mls3POFK2JNWFCXPPCDMU2CLIDXVY";		//MLS "Request to external system count limit(%1) exeeded in \"%2\""
    static final String MLS_ID_ERR_CANT_READ_SERVICE_OPTIONS = "mlbaclMU4BLUT6FJCZLDQ2WL6SCH43NE-mlsKYLUGGXB7BF7JLGCTXDMDTPEIA";	//MLS "Can't read service options of \"%1\": %2", Error, CDbpEventSource.JMSHandler
    static final String MLS_ID_SAP_RECV_TIMEOUT = "mlbaclMU4BLUT6FJCZLDQ2WL6SCH43NE-mlsQWV54QYK3ZAEFF7YUBM5EQIXSY";			//MLS "Response from external system timeout was exceeded (id=%1)", Error, CDbpEventSource.JMSHandler
    static final String MLS_ID_ERR_UNCORRELATED_RECV = "mlbaclMU4BLUT6FJCZLDQ2WL6SCH43NE-mlsP6IS4CUI5VEXRJLB3UUZ55CATU";		//MLS "Uncorrelated response received (id=%1)", Error, CDbpEventSource.JMSHandler
    static final String MLS_ID_ERR_PROPS_PARSE = "mlbaclMU4BLUT6FJCZLDQ2WL6SCH43NE-mls5WUQPFRVUVG3THPEAL3OE5TVA4";		        //MLS "Jms properties parsing error: %1", Error, CDbpEventSource.JMSHandler    
}