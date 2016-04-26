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

package org.radixware.kernel.server.instance.arte;

import java.util.ResourceBundle;
import org.radixware.kernel.server.utils.MessagesHelper;


public final class ArteMessages {

    private ArteMessages() {
    }

    static {
        MessagesHelper.initialize(ArteMessages.class, "org.radixware.kernel.server.instance.arte.mess.messages");
    }
    public static volatile String TITLE_ERROR;
    public static volatile String ARTE_IS_NOT_STOPPED;
    public static volatile String SAP;
    public static volatile String ANY;
    public static volatile String CANT_INIT_ARTE;
    public static volatile String ERR_ON_SERVICE_RQ_PROC;
    public static volatile String ERR_ON_ARTE_INST_STOP;
    public static volatile String ERR_ON_ARTE_INST_INIT;
    public static volatile String INSUF_ARTE_COUNT;
    public static volatile String ERR_ON_SESS_END_NOTIFICATION;
    public static volatile String ERR_ON_SESS_START_NOTIFICATION;
    public static volatile String _LOADED_NEW_VERSION;
    public static volatile String ALL_ARTE_LOADED_VERSION;
    static final String MLS_ID_CLIENT_CONNECTED = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsD4OJQF5MNVAWLNHPV7FWT2NX6E";//MLS "Client \"%2\" connected to \"%1\"", EEventSeverity.EVENT, EEventSource.ARTE_COMMUNICATOR
    static final String MLS_ID_CLIENT_DISCONNECTED = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsSJZ5AOEF3NEYLA5PJZKVYZKN6I";// MLS "Client \"%2\" disconnected from \"%1\"", EEventSeverity.EVENT, EEventSource.ARTE_COMMUNICATOR
    static final String MLS_ID_ERR_ON_SOCKET_START = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mls7D4OVIGAIFB6LDWS53JEQ4P27Q"; //MLS "Can't start server socket of \"%1\": %2", Error, EEventSource.ARTE_COMMUNICATOR
    static final String MLS_ID_XXX_STARTED = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsAALJQDTWUFFUTBK5H6WLI35A6U"; // MLS "\"%1\" started", Event, ARTE
    static final String MLS_ID_XXX_STOPPED = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsCXKQ7ZDH45GVNJLE4UDPTEOBY4"; // MLS "\"%1\" stopped", Event, ARTE
    static final String MLS_ID_ERR_ON_SOCKET_IO = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsA3VMJBFDB5G2PJ5HBKEZRHQYEM"; // MLS "\"%1\" server socket IO error: %2", EEventSeverity.ERROR, EEventSource.ARTE_COMMUNICATOR
    static final String MLS_ID_ERR_ON_SERVICE_RQ_PROC = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsBBZPMQ7LTNCINN6XWELWUEXJMA";// MLS "\"%1\" ARTE service error: %2", EEventSeverity.ERROR, EEventSource.ARTE;
    static final String MLS_ID_ERR_ON_ACCEPTED_SOCKET_CLOSE = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mls77UT2J2UINHNZMH6ES6LQ3OK7E";// MLS "\"%1\" can't close accepted socket: %2", EEventSeverity.WARNING, EEventSource.ARTE_COMMUNICATOR
    static final String MLS_ID_INSUF_ARTE_COUNT = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsGVK53K2MINGYBEJPIE4NV7LLEY";// MLS "Instance \"%1\" has insufficient ARTE instances count", Warning, ARTE_POOL
    static final String MLS_ID_ERR_ON_ARTE_INST_INIT = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsN5BQ7VKUIZFSRND2L56RBBU6WM";// MLS "Instance \"%1\" can't init new ARTE instance: %2", Error, ARTE_POOL
    static final String MLS_ID_ERR_ON_ARTE_INST_STOP = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mls4FR67PQAU5A2XNUKNCZ3XSNVHY";// MLS "Error on ARTE instance \"%1\" stopping: %2", Error, ARTE_POOL
    public static final String MLS_ID_CANT_INIT_ARTE = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsSLRTXPRVABEXXH3TYLRLJSYEZ4"; //MLS "Can't initialize ARTE \"%1\": %2", Error, EEventSource.ARTE
    static final String MLS_ID_LOADED_VERSION = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsEBQMGBFCN5BBRM73JUFSZFSPN4";// "%1 loaded version %2", Event, Arte
}
