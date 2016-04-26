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
package org.radixware.kernel.server.units.persocomm;

import org.radixware.kernel.server.units.Messages;
import org.radixware.kernel.server.units.persocomm.tools.MultiLangStringWrapper;
import org.radixware.kernel.server.utils.MessagesHelper;

class PCMessages {

    private PCMessages() {
    }

    static {
        MessagesHelper.initialize(PCMessages.class, PCMessages.class.getPackage().getName()+".mess.messages");
    }
    static volatile String APNS_MAX_PARALLEL_SEND_COUNT;
    static volatile String APNS_TIME_SUCCESSFUL_MILLIS;
    static volatile String APP_NAME_MASK;
    static volatile String APNS_FEEDBACK_ADDRESS;
    static volatile String APNS_PUSH_ADDRESS;
    static volatile String APNS_KEY_ALIAS;
    static volatile String APNS_UNIT_TYPE_TITLE;
    static volatile String GCM_API_KEY;
    static volatile String GCM_UNIT_TYPE_TITLE;
    static volatile String FILE_UNIT_TYPE_TITLE;
    static volatile String GSM_UNIT_TYPE_TITLE;
    static volatile String MAIL_UNIT_TYPE_TITLE;
    static volatile String SMS_MAIL_UNIT_TYPE_TITLE;
    static volatile String SMPP_UNIT_TYPE_TITLE;
    static volatile String TWITTER_UNIT_TYPE_TITLE;
    static volatile String TWITTER_CONSUMER_TOKEN;
    static volatile String TWITTER_CONSUMER_SECRET;
    static volatile String TWITTER_ACCESS_TOKEN;
    static volatile String TWITTER_ACCESS_SECRET;
    static volatile String SERVICEBUS_UNIT_TYPE_TITLE;
    static volatile String SEND_PERIOD;
    static volatile String RECV_PERIOD;
    static volatile String SEND_ADDRESS;
    static volatile String RECV_ADDRESS;
    static volatile String SMPP_SYSTEM_ID;
    static volatile String SMPP_SYSTEM_TYPE;
    static volatile String SMPP_PASSWORD;
    static volatile String SMPP_SOURCE_ADDRESS;
    static volatile String SMPP_SOURCE_ADDRESS_TON;
    static volatile String SMPP_SOURCE_ADDRESS_NPI;
    static volatile String SMPP_DESTINATION_TON;
    static volatile String SMPP_DESTINATION_NPI;
    static volatile String SMPP_INTERFACE;
    static volatile String SMPP_MAX_LEN;
    static volatile String POP3ADDRESS;
    static volatile String EMAIL_LOGIN;
    static volatile String EMAIL_PASSWORD;
    static volatile String EMAIL_SECURE;
    static volatile String ADDRESS_TEMPLATE;
    static volatile String SUBJECT_TEMPLATE;
    static volatile String ENCODING;
    static volatile String DEBUG_MESSAGE;
    static volatile String FILE_FORMAT;
    static volatile String AAS_CLIENT_STOPPED;
    static volatile String SMSC_BIND_FAILED;
    static volatile String SMSC_UNBIND_ERROR;
    static volatile String WRONG_POP3_ADDRESS;
    static volatile String READ_ATTACHMENT_ERROR;
    static volatile String WRITE_ATTACHMENT_ERROR;
    static volatile String SENT_MESS_ERROR;
    static volatile String RECV_MESS_ERROR;
    static volatile String FILE_WRITE_ERROR;
    static volatile String SEND_ADDRESS_MISSING;
    static volatile String RECEIVE_ADDRESS_MISSING;
    static volatile String FILE_FORMAT_MISSING;
    static volatile String WRONG_DIRECTORY_NAME;
    static volatile String WRONG_DIRECTORY_ACCESS;
    static volatile String WRONG_FILE_FORMAT;
    static volatile String WRONG_MIME_FORMAT;
    static volatile String WRONG_SEND_ADDRESS_FORMAT;
    static volatile String WRONG_RECEIVER_ADDRESS_FORMAT;
    static volatile String FILE_PARSE_ERROR;
    static volatile String FILE_READ_ERROR;

    static final String MLS_ID_LOADED_FROM_SENT_QUEUE = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsLYY4F7SEBFGWHLGEGOQT2AHMBQ"; //Message loaded from sent queue (id = %1)
    static final String MLS_ID_LOAD_ERROR = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsJL5ESYYI3JDGDEOZULKNLPESUQ"; //Can't read message(s) from queue
    static final String MLS_ID_SENT = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsD5XKZJSRDFD37G3VLMFWUTMGJA"; //Message (%1) sent
    static final String MLS_ID_SENT_ERROR = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsEXPEJ5CPXZD4VDAPSHE3UDFUSY"; //Sent error (error message: '%1')
    static final String MLS_ID_MOVE_TO_ARHIVE_ERROR = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsNAKTN65SIFHQVGTPRFFMJCAONI"; //Save message(s) to archive error: %1
    static final String MLS_ID_MESS_RECEIVED = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsQ6SLRMSV3FGOJOAPCVZ2ZQVLZA"; //Message received
    static final String MLS_ID_RECV_ERROR = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsMWY3FB2YE5G7DCLZN263DNMU6Q"; //Receive message error: %1
    static final String MLS_ID_SAVED_TO_ARCHIVE = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsZ63SPQBMANH55LBFT756Y73K5U"; //Message (%1) moved to archive

    static final String MLS_ID_START_SEND = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsWH4R2IDXMVFHHAVJEEL6LFLBKE"; //Start processing send queue
    static final String MLS_ID_STOP_SEND = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mls6EORKT67DFEHFG3PTXROCBDWC4"; //Send queue processing finished, (%1) items were processed

    static final String MLS_ID_START_RECV = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsRB4GBZMC7NFFJDKKC27K2BJTRI"; //Start receiving messages
    static final String MLS_ID_STOP_RECV = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsMWJZZ5EOPNAE7IENDTWCJTQYHM"; //Receiving messages finished, (%1) items were processed   

    static final MultiLangStringWrapper W_MLS_ID_LOADED_FROM_SENT_QUEUE = new MultiLangStringWrapper("mlbadcXCB5KK6HMJH7NP6E642OHPOMXY","mlsLYY4F7SEBFGWHLGEGOQT2AHMBQ"); //Message loaded from sent queue (id = %1)
    static final MultiLangStringWrapper W_MLS_ID_LOAD_ERROR = new MultiLangStringWrapper("mlbadcXCB5KK6HMJH7NP6E642OHPOMXY","mlsJL5ESYYI3JDGDEOZULKNLPESUQ"); //Can't read message(s) from queue
    static final MultiLangStringWrapper W_MLS_ID_SENT = new MultiLangStringWrapper("mlbadcXCB5KK6HMJH7NP6E642OHPOMXY","mlsD5XKZJSRDFD37G3VLMFWUTMGJA"); // Message (%1) sent
    static final MultiLangStringWrapper W_MLS_ID_SENT_ERROR = new MultiLangStringWrapper("mlbadcXCB5KK6HMJH7NP6E642OHPOMXY","mlsEXPEJ5CPXZD4VDAPSHE3UDFUSY"); //Sent error (error message: '%1')
    static final MultiLangStringWrapper W_MLS_ID_MOVE_TO_ARHIVE_ERROR = new MultiLangStringWrapper("mlbadcXCB5KK6HMJH7NP6E642OHPOMXY","mlsNAKTN65SIFHQVGTPRFFMJCAONI"); //Save message(s) to archive error: %1
    static final MultiLangStringWrapper W_MLS_ID_MESS_RECEVIED_AND_STORED = new MultiLangStringWrapper("mlbadcXCB5KK6HMJH7NP6E642OHPOMXY","mlsLS6VSEY3V5AB7L24T4AMHWEATE"); //Message received and stored (id=%1)
    
    static final MultiLangStringWrapper W_MLS_ID_RECV_ERROR = new MultiLangStringWrapper("mlbadcXCB5KK6HMJH7NP6E642OHPOMXY","mlsMWY3FB2YE5G7DCLZN263DNMU6Q"); //Receive message error: %1
    static final MultiLangStringWrapper W_MLS_ID_SAVED_TO_ARCHIVE = new MultiLangStringWrapper("mlbadcXCB5KK6HMJH7NP6E642OHPOMXY","mlsZ63SPQBMANH55LBFT756Y73K5U"); //Message (%1) moved to archive
                                                                                                                                   
    static final MultiLangStringWrapper W_MLS_ID_START_SEND = new MultiLangStringWrapper("mlbadcXCB5KK6HMJH7NP6E642OHPOMXY","mlsWH4R2IDXMVFHHAVJEEL6LFLBKE"); //Start processing send queue
    static final MultiLangStringWrapper W_MLS_ID_STOP_SEND = new MultiLangStringWrapper("mlbadcXCB5KK6HMJH7NP6E642OHPOMXY","mls6EORKT67DFEHFG3PTXROCBDWC4"); //Send queue processing complete, (%1) items were processed
    
    static final MultiLangStringWrapper W_MLS_ID_START_RECV = new MultiLangStringWrapper("mlbadcXCB5KK6HMJH7NP6E642OHPOMXY","mlsRB4GBZMC7NFFJDKKC27K2BJTRI"); //Start receiving messages
    static final MultiLangStringWrapper W_MLS_ID_STOP_RECV = new MultiLangStringWrapper("mlbadcXCB5KK6HMJH7NP6E642OHPOMXY","mlsMWJZZ5EOPNAE7IENDTWCJTQYHM"); //Message receiving complete, (%1) items were processed
    
    static final MultiLangStringWrapper W_MLS_STAT_SAVED_TO_ARCHIVE = new MultiLangStringWrapper("mlbadcXCB5KK6HMJH7NP6E642OHPOMXY","mlsBDXUTUBQANG6XJKZN5GYABHJTA"); //Statistics for message (%1) was saved successfully
    static final MultiLangStringWrapper W_MLS_STAT_SAVE_TO_ARHIVE_ERROR = new MultiLangStringWrapper("mlbadcXCB5KK6HMJH7NP6E642OHPOMXY","mlsYN4LRF74UVEMPJZHK7CRJQWLTM"); //Error saving statistics for message (%1) : %2

    
    static final MultiLangStringWrapper W_MLS_ID_ERR_IN_DB_QRY = new MultiLangStringWrapper(Messages.MLS_ID_ERR_IN_DB_QRY);
    static final MultiLangStringWrapper W_MLS_ID_START_OPTIONS = new MultiLangStringWrapper(Messages.MLS_ID_START_OPTIONS);
}
