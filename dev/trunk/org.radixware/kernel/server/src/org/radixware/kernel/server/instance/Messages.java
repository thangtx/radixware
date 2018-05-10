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
package org.radixware.kernel.server.instance;

import java.util.ResourceBundle;

public final class Messages {

    static {
        final ResourceBundle bundle = ResourceBundle.getBundle("org.radixware.kernel.server.instance.mess.messages");

        MSG_ATTEMPT_TO_ENABLE_TRACE_LONGER_THAN_8_H = bundle.getString("MSG_ATTEMPT_TO_ENABLE_TRACE_LONGER_THAN_8_H");
        FILE_TRACE_OPTIONS_CHANGED = bundle.getString("FILE_TRACE_OPTIONS_CHANGED");
        TITLE_INSTANCE = bundle.getString("TITLE_INSTANCE");
        ERR_CANT_SAVE_CFG = bundle.getString("ERR_CANT_SAVE_CFG");
        ERR_IN_DB_QRY = bundle.getString("ERR_IN_DB_QRY");
        ERR_ON_DB_CONNECTION_CLOSE = bundle.getString("ERR_ON_DB_CONNECTION_CLOSE");
        //LOADING_UNIT =  bundle.getString("LOADING_UNIT");
        UNIT_LOADED = bundle.getString("UNIT_LOADED");
        //STARTING_UNIT =  bundle.getString("STARTING_UNIT");
        UNIT_STARTED = bundle.getString("UNIT_STARTED");
        UNIT_START_POSTPONED = bundle.getString("UNIT_START_POSTPONED");
        //RESTARTING_UNIT =  bundle.getString("RESTARTING_UNIT");
        //UNIT_RESTARTED = bundle.getString("UNIT_RESTARTED");
        UNIT_LOAD_ERROR = bundle.getString("UNIT_LOAD_ERROR");
        ERR_ON_UNIT_START = bundle.getString("ERR_ON_UNIT_START");
        ERR_ON_UNIT_RESTART = bundle.getString("ERR_ON_UNIT_RESTART");
        ERR_ON_UNIT_STOP = bundle.getString("ERR_ON_UNIT_STOP");
        ERR_ON_UNITS_START = bundle.getString("ERR_ON_UNITS_START");
        ERR_ON_UNITS_RESTART = bundle.getString("ERR_ON_UNITS_RESTART");
        ERR_ON_UNITS_STOP = bundle.getString("ERR_ON_UNITS_STOP");
        //UNLOADING_UNIT =  bundle.getString("UNLOADING_UNIT");
        UNIT_UNLOADED = bundle.getString("UNIT_UNLOADED");
        ERR_ON_UNIT_UNLOAD = bundle.getString("ERR_ON_UNIT_UNLOAD");
        ERR_ON_TRACE_OPTION_READING = bundle.getString("ERR_ON_TRACE_OPTION_READING");
        INSTANCE_THREAD_INTERRUPTED = bundle.getString("INSTANCE_THREAD_INTERRUPTED");
        INSTANCE_THREAD_INTERRUPTED_BY_EXCEPTION = bundle.getString("INSTANCE_THREAD_INTERRUPTED_BY_EXCEPTION");
        ERR_ON_INSTANCE_STOP = bundle.getString("ERR_ON_INSTANCE_STOP");
        ERR_CANT_READ_SERVICE_OPTIONS = bundle.getString("ERR_CANT_READ_SERVICE_OPTIONS");
        ERR_CANT_READ_KERBEROS_OPTIONS = bundle.getString("ERR_CANT_READ_KERBEROS_OPTIONS");
        ERR_KEYTAB_DOES_NOT_EXIST = bundle.getString("ERR_KEYTAB_DOES_NOT_EXIST");
        ERR_WRONG_SPN_FORMAT = bundle.getString("ERR_WRONG_SPN_FORMAT");
        ERR_ON_KDC_LOGIN = bundle.getString("ERR_ON_KDC_LOGIN");
        ERR_CANT_GET_GSS_CREDENTIALS = bundle.getString("ERR_CANT_GET_GSS_CREDENTIALS");
        WARN_NO_COMPATIBLE_KEY_IN_KEYTAB = bundle.getString("WARN_NO_COMPATIBLE_KEY_IN_KEYTAB");
        TRY_SHUTDOWN_INSTANCE = bundle.getString("TRY_SHUTDOWN_INSTANCE");
        ERR_ON_INSTANCE_SHUTDOWN = bundle.getString("ERR_ON_INSTANCE_SHUTDOWN");
        TRY_INTERRUPT_INSTANCE = bundle.getString("TRY_INTERRUPT_INSTANCE");
        TITLE_ERROR = bundle.getString("TITLE_ERROR");
        CONFIRM_SRV_STOP = bundle.getString("CONFIRM_SRV_STOP");
        TITLE_CONFIRM = bundle.getString("TITLE_CONFIRM");
        ST_INST_STOPPED = bundle.getString("ST_INST_STOPPED");
        ST_INST_STARTING = bundle.getString("ST_INST_STARTING");
        ST_INST_STARTED = bundle.getString("ST_INST_STARTED");
        ST_INST_STOPPING = bundle.getString("ST_INST_STOPPING");
        INSTANCE_IS_NOT_STARTED = bundle.getString("INSTANCE_IS_NOT_STARTED");
        INSTANCE_IS_NOT_STOPPED = bundle.getString("INSTANCE_IS_NOT_STOPPED");
        ERR_OF_SERVICE = bundle.getString("ERR_OF_SERVICE");
        UNIT = bundle.getString("UNIT");
        _NOT_FOUND = bundle.getString("_NOT_FOUND");
        TRY_RESTORE_DB_CONNECTION = bundle.getString("TRY_RESTORE_DB_CONNECTION");
        DB_CONNECTION_RESTORED = bundle.getString("DB_CONNECTION_RESTORED");
        TRACE_PROFILE_CHANGED = bundle.getString("TRACE_PROFILE_CHANGED");
        //STOPPING_UNIT = bundle.getString("STOPPING_UNIT");
        UNIT_STOPPED = bundle.getString("UNIT_STOPPED");
        TAB_TRACE = bundle.getString("TAB_TRACE");
        TAB_UNITS = bundle.getString("TAB_UNITS");
        ERR_UNHANDLED_IN_SERVICE = bundle.getString("ERR_UNHANDLED_IN_SERVICE");
        ERR_WHILE_CHECKING_KEYSTORE = bundle.getString("ERR_WHILE_CHECKING_KEYSTORE");
        INCORRECT_KEYSTORE_PASSWORD = bundle.getString("INCORRECT_KEYSTORE_PASSWORD");

        MNU_MENU = bundle.getString("MNU_MENU");
        MNU_ABOUT = bundle.getString("MNU_ABOUT");
        MNU_START = bundle.getString("MNU_START_SRV");
        MNU_STOP = bundle.getString("MNU_STOP_SRV");
        MNU_SRV_SQL_TRACE_CFG = bundle.getString("MNU_SRV_SQL_TRACE_CFG");
        MNU_RECOVERY_INSTANCE = bundle.getString("MNU_RECOVERY_INSTANCE");
        MNU_ACTUALIZE_VER = bundle.getString("MNU_ACTUALIZE_VER");
        MNU_CREATE_CONF_FILE = bundle.getString("MNU_CREATE_CONF_FILE");
        TITLE_RELEASE_NUMBER = bundle.getString("TITLE_RELEASE_NUMBER");
        LAYER_URI = bundle.getString("LAYER_URI");
        RELEASE_NUMBER = bundle.getString("RELEASE_NUMBER");

        ERR_ON_LOADER_ACTUALIZE = bundle.getString("ERR_ON_LOADER_ACTUALIZE");
        ERR_KERNEL_VER = bundle.getString("ERR_KERNEL_VER");
        ERR_DDS_VER = bundle.getString("ERR_DDS_VER");
        VER_SWITCHED_ = bundle.getString("VER_SWITCHED_");

        ERR_ON_SINGL_UNIT_LOCK_RELEASE_ = bundle.getString("ERR_ON_SINGL_UNIT_LOCK_RELEASE_");

        ERR_APP_DB_MISTIMING = bundle.getString("ERR_APP_DB_MISTIMING");
        WARN_APP_DB_LOCAL_MISTIMING = bundle.getString("WARN_APP_DB_LOCAL_MISTIMING");

        DB_IS_ACTUAL = bundle.getString("DB_IS_ACTUAL");
        WARNING_DDS_UPGRADE = bundle.getString("WARNING_DDS_UPGRADE");
        ERR_UNCOMPATIBLE_DDS_VER = bundle.getString("ERR_UNCOMPATIBLE_DDS_VER");
        ERR_DDS_UNCOMPATIBLE_UPGRADE = bundle.getString("ERR_DDS_UNCOMPATIBLE_UPGRADE");
        ERR_DDS_VER_ON_START = bundle.getString("ERR_DDS_VER_ON_START");
        COL_LAYER = bundle.getString("COL_LAYER");
        COL_META_VER = bundle.getString("COL_META_VER");
        COL_DB_COMPATIBLE_VER = bundle.getString("COL_DB_COMPATIBLE_VER");
        START_ANYWAY = bundle.getString("START_ANYWAY");
        MNU_ENABLE_SENSITIVE_TRACE = bundle.getString("MNU_ENABLE_SENSITIVE_TRACE");
        GLOBAL_SENS_TRC_ON = bundle.getString("GLOBAL_SENS_TRC_ON");
        GLOBAL_SENS_TRC_OFF = bundle.getString("GLOBAL_SENS_TRC_OFF");

        AUTO_ACTUALIZE_ON = bundle.getString("AUTO_ACTUALIZE_ON");
        AUTO_ACTUALIZE_OFF = bundle.getString("AUTO_ACTUALIZE_OFF");

        ARTE_INST_LIVE_TIME_MIN = bundle.getString("ARTE_INST_LIVE_TIME_MIN");

        HINT_YOU_HAVE_TO_ENABLE_SENSITIVE_TRC_LOCALY = bundle.getString("HINT_YOU_HAVE_TO_ENABLE_SENSITIVE_TRC_LOCALY");
        HINT_YOU_HAVE_TO_ENABLE_SENSITIVE_TRC_GLOBALY = bundle.getString("HINT_YOU_HAVE_TO_ENABLE_SENSITIVE_TRC_GLOBALY");
        LBL_GLB_SENSITIVE_TRC_PERMIS = bundle.getString("LBL_GLB_SENSITIVE_TRC_PERMIS");
        LBL_IS_OFF = bundle.getString("LBL_IS_OFF");
        LBL_IS_ON = bundle.getString("LBL_IS_ON");
        LBL_LOCAL_SENSITIVE_TRC_FINISH_TIME = bundle.getString("LBL_LOCAL_SENSITIVE_TRC_FINISH_TIME");
        LBL_SENSITIVE_TRC_IS_ON_TILL_ = bundle.getString("LBL_SENSITIVE_TRC_IS_ON_TILL_");
        MSG_SENSITIVE_TRC_IS_OFF_DUE_TO_GLBL_SET = bundle.getString("MSG_SENSITIVE_TRC_IS_OFF_DUE_TO_GLBL_SET");
        MSG_SENSITIVE_TRC_IS_OFF = bundle.getString("MSG_SENSITIVE_TRC_IS_OFF");
        TITLE_SENSITIVE_DATA_TRACING_OPTIONS = bundle.getString("TITLE_SENSITIVE_DATA_TRACING_OPTIONS");
        TITLE_INFO = bundle.getString("TITLE_INFO");

        SERVER_KEYSTORE_WAS_REREAD = bundle.getString("SERVER_KEYSTORE_WAS_REREAD");
        CHECK_FOR_UPDATES = bundle.getString("CHECK_FOR_UPDATES");

        MEMORY_CONSUMPTION_DECREASED_TO = bundle.getString("MEMORY_CONSUMPTION_DECREASED_TO");
        MEMORY_CONSUMPTION_INCREASED_TO = bundle.getString("MEMORY_CONSUMPTION_INCREASED_TO");

        PROFILE_PERIOD_SEC = bundle.getString("PROFILE_PERIOD_SEC");
        MEMORY_CHECK_PERIOD_SEC = bundle.getString("MEMORY_CHECK_PERIOD_SEC");
        HTTP_PROXY = bundle.getString("HTTP_PROXY");
        HTTPS_PROXY = bundle.getString("HTTPS_PROXY");

        KRB_OPTIONS = bundle.getString("KRB_OPTIONS");
        KEYTAB_PATH = bundle.getString("KEYTAB_PATH");
        KRB_PRINCIPAL = bundle.getString("KRB_PRINCIPAL");
        KRB_DEFAULT = bundle.getString("KRB_DEFAULT");

        CHECK_IS_OFF = bundle.getString("CHECK_IS_OFF");

        START_OPTIONS = bundle.getString("START_OPTIONS");
        OPTIONS_CHANGED = bundle.getString("OPTIONS_CHANGED");

        TAB_ARTE_POOL = bundle.getString("TAB_ARTE_POOL");
        SCP_NAME = bundle.getString("SCP_NAME");

        STARTER_TEMP_DIR = bundle.getString("STARTER_TEMP_DIR");
        USING_ = bundle.getString("USING_");
        WARN_RUNNING_PIDS_DISCOVERY_BROKEN = bundle.getString("WARN_RUNNING_PIDS_DISCOVERY_BROKEN");

        MNU_REFRESH_UNITS = bundle.getString("MNU_REFRESH_UNITS");
        MNU_SCHEDULE_MAINTENANCE_TASKS = bundle.getString("MNU_SCHEDULE_MAINTENANCE_TASKS");

        ERR_ON_FILE_LOG_MAINTENANCE = bundle.getString("ERR_ON_FILE_LOG_MAINTENANCE");
        DB_CONFIG_CHANGED = bundle.getString("DB_CONFIG_CHANGED");

        UNLOADING_STOPPED_UNIT = bundle.getString("UNLOADING_STOPPED_UNIT");
        INSTANCE_START = bundle.getString("INSTANCE_START");

        INSTANCE_STOP = bundle.getString("INSTANCE_STOP");
        AUTOSTART_POSTPONED_UNITS = bundle.getString("AUTOSTART_POSTPONED_UNITS");
        INSTANCE_RC_SAP_COMMAND = bundle.getString("INSTANCE_RC_SAP_COMMAND");

        ARTE_POOL_RELOAD_REQUESTED = bundle.getString("ARTE_POOL_RELOAD_REQUESTED");
        INSTANCE_MAINTENANCE_REQUESTED = bundle.getString("INSTANCE_MAINTENANCE_REQUESTED");
        LOCAL_JOB_EXECUTOR_IS_USED = bundle.getString("LOCAL_JOB_EXECUTOR_IS_USED");

        PRIORITY_MAP_STR = bundle.getString("PRIORITY_MAP_STR");

        MIN_ARTE_INST_COUNT = bundle.getString("MIN_ARTE_INST_COUNT");
        NORMAL_ARTE_INST_COUNT = bundle.getString("NORMAL_ARTE_INST_COUNT");
        ABOVE_NORMAL_ARTE_INST_COUNT = bundle.getString("ABOVE_NORMAL_ARTE_INST_COUNT");
        HIGH_ARTE_INST_COUNT = bundle.getString("HIGH_ARTE_INST_COUNT");
        VERY_HIGH_ARTE_INST_COUNT = bundle.getString("VERY_HIGH_ARTE_INST_COUNT");
        CRITICAL_ARTE_INST_COUNT = bundle.getString("CRITICAL_ARTE_INST_COUNT");
        MAX_TOTAL_ARTE_INST_COUNT = bundle.getString("MAX_TOTAL_ARTE_INST_COUNT");

        USE_ORA_IMPL_STMT_CACHE = bundle.getString("USE_ORA_IMPL_STMT_CACHE");
        ORA_IMPL_STMT_CACHE_SIZE = bundle.getString("ORA_IMPL_STMT_CACHE_SIZE");
        MNU_RELOAD_ARTE_POOL = bundle.getString("MNU_RELOAD_ARTE_POOL");

        INVALID_LOCAL_STARTER_VERSION = bundle.getString("INVALID_LOCAL_STARTER_VERSION");
        USE_ACTIVE_ARTE_CONSTRAINTS = bundle.getString("USE_ACTIVE_ARTE_CONSTRAINTS");
        SENSITIVE_TRACE_TIME_IS_SET_TO = bundle.getString("SENSITIVE_TRACE_TIME_IS_SET_TO");
        USING_CONFIG_FILE = bundle.getString("USING_CONFIG_FILE");

        EXPIRE_SOON = bundle.getString("EXPIRE_SOON");
        LESS_THEN = bundle.getString("LESS_THEN");
        DAYS = bundle.getString("DAYS");
        ONE_DAY = bundle.getString("ONE_DAY");
        ONE_HOUR = bundle.getString("ONE_HOUR");
        TEN_MINUTES = bundle.getString("TEN_MINUTES");
        ONE_MINUTE = bundle.getString("ONE_MINUTE");
        LICENSES_UPDATED = bundle.getString("LICENSES_UPDATED");
        EXPIRED = bundle.getString("EXPIRED");
        LICENSE_SET_IN_USE = bundle.getString("LICENSE_SET_IN_USE");
        LOADING_ARTE_POOL = bundle.getString("LOADING_ARTE_POOL");
        AADC_MEMBER = bundle.getString("AADC_MEMBER");
        AADC_DG_ADDRESS = bundle.getString("AADC_DG_ADDRESS");
        AUTO_RESTART_DELAY = bundle.getString("AUTO_RESTART_DELAY");
        
        KEYSTORE_LOADER_FROM = bundle.getString("KEYSTORE_LOADER_FROM");
        ACCEPTABLE_VERSIONS_CHANGED = bundle.getString("ACCEPTABLE_VERSIONS_CHANGED");
        JOBS_MOVED_TO_OTHER_AADC_MEMBER = bundle.getString("JOBS_MOVED_TO_OTHER_AADC_MEMBER");
    }
    static final String SENSITIVE_TRACE_TIME_IS_SET_TO;
    static final String MNU_RELOAD_ARTE_POOL;
    static final String LOCAL_JOB_EXECUTOR_IS_USED;
    static final String ARTE_POOL_RELOAD_REQUESTED;
    static final String INSTANCE_RC_SAP_COMMAND;
    static final String INSTANCE_MAINTENANCE_REQUESTED;
    static final String AUTOSTART_POSTPONED_UNITS;
    static final String INSTANCE_STOP;
    static final String INSTANCE_START;
    static final String UNLOADING_STOPPED_UNIT;
    static final String DB_CONFIG_CHANGED;
    static final String ERR_ON_FILE_LOG_MAINTENANCE;
    static final String SCP_NAME;
    static final String CHECK_FOR_UPDATES;
    static final String ERR_UNCOMPATIBLE_DDS_VER;
    static final String ERR_DDS_UNCOMPATIBLE_UPGRADE;
    static final String ERR_CANT_SAVE_CFG;
    static final String ERR_IN_DB_QRY;
    static final String ERR_ON_DB_CONNECTION_CLOSE;
    static final String UNIT_LOADED;
    static final String UNIT_STARTED;
    static final String UNIT_START_POSTPONED;
    static final String UNIT_LOAD_ERROR;
    static final String ERR_ON_UNIT_START;
    static final String ERR_ON_UNIT_RESTART;
    static final String ERR_ON_UNIT_STOP;
    static final String ERR_ON_UNIT_UNLOAD;
    static final String ERR_ON_UNITS_START;
    static final String ERR_ON_UNITS_RESTART;
    static final String ERR_ON_UNITS_STOP;
    static final String UNIT_UNLOADED;
    static final String ERR_ON_TRACE_OPTION_READING;
    static final String INSTANCE_THREAD_INTERRUPTED;
    static final String INSTANCE_THREAD_INTERRUPTED_BY_EXCEPTION;
    static final String ERR_ON_INSTANCE_STOP;
    static final String TRY_SHUTDOWN_INSTANCE;
    static final String ERR_ON_INSTANCE_SHUTDOWN;
    static final String TRY_INTERRUPT_INSTANCE;
    static final String TITLE_ERROR;
    static final String CONFIRM_SRV_STOP;
    static final String TITLE_CONFIRM;
    static final String ERR_ON_LOADER_ACTUALIZE;
    static final String ERR_KERNEL_VER;
    static final String ERR_DDS_VER;
    static final String WARNING_DDS_UPGRADE;
    static final String VER_SWITCHED_;
    static final String DB_IS_ACTUAL;
    public static final String ERR_DDS_VER_ON_START;
    static final String COL_LAYER;
    static final String COL_META_VER;
    static final String COL_DB_COMPATIBLE_VER;
    static final String START_ANYWAY;
    static final String MNU_ENABLE_SENSITIVE_TRACE;
    static final String GLOBAL_SENS_TRC_ON;
    static final String GLOBAL_SENS_TRC_OFF;
    static final String ERR_APP_DB_MISTIMING;
    static final String WARN_APP_DB_LOCAL_MISTIMING;
    static final String AUTO_ACTUALIZE_ON;
    static final String AUTO_ACTUALIZE_OFF;
    static final String MNU_ACTUALIZE_VER;
    static final String MNU_REFRESH_UNITS;
    static final String MNU_SCHEDULE_MAINTENANCE_TASKS;
    static final String PROFILE_PERIOD_SEC;
    static final String MEMORY_CHECK_PERIOD_SEC;
    static final String HTTP_PROXY;
    static final String HTTPS_PROXY;
    static final String PRIORITY_MAP_STR;
    static final String KRB_OPTIONS;
    static final String KEYTAB_PATH;
    static final String KRB_PRINCIPAL;
    static final String KRB_DEFAULT;
    static final String CHECK_IS_OFF;
    static final String START_OPTIONS;
    static final String OPTIONS_CHANGED;
    static final String ARTE_INST_LIVE_TIME_MIN;
    static final String MNU_CREATE_CONF_FILE;
    static final String MSG_ATTEMPT_TO_ENABLE_TRACE_LONGER_THAN_8_H;
    static final String WARN_RUNNING_PIDS_DISCOVERY_BROKEN;
    static final String MIN_ARTE_INST_COUNT;
    static final String NORMAL_ARTE_INST_COUNT;
    static final String ABOVE_NORMAL_ARTE_INST_COUNT;
    static final String HIGH_ARTE_INST_COUNT;
    static final String VERY_HIGH_ARTE_INST_COUNT;
    static final String CRITICAL_ARTE_INST_COUNT;
    static final String MAX_TOTAL_ARTE_INST_COUNT;
    static final String USE_ORA_IMPL_STMT_CACHE;
    static final String ORA_IMPL_STMT_CACHE_SIZE;
    static final String INVALID_LOCAL_STARTER_VERSION;
    static final String USE_ACTIVE_ARTE_CONSTRAINTS;
    static final String AUTO_RESTART_DELAY;
    static final String JOBS_MOVED_TO_OTHER_AADC_MEMBER;
    public static final String ACCEPTABLE_VERSIONS_CHANGED;

    static String getStateMessage(final InstanceState state) {
        if (state == InstanceState.STOPPED) {
            return ST_INST_STOPPED;
        }
        if (state == InstanceState.STARTING) {
            return ST_INST_STARTING;
        }
        if (state == InstanceState.STARTED) {
            return ST_INST_STARTED;
        }
        if (state == InstanceState.STOPPING) {
            return ST_INST_STOPPING;
        }
        throw new IllegalStateException("Message is not defined for instance state: " + String.valueOf(state));
    }
    private static final String ST_INST_STOPPED;
    private static final String ST_INST_STARTING;
    private static final String ST_INST_STARTED;
    private static final String ST_INST_STOPPING;
    static final String MNU_MENU;
    static final String MNU_ABOUT;
    static final String MNU_START;
    static final String MNU_STOP;
    static final String MNU_SRV_SQL_TRACE_CFG;
    static final String MNU_RECOVERY_INSTANCE;
    static final String TITLE_RELEASE_NUMBER;
    static final String LAYER_URI;
    static final String RELEASE_NUMBER;
    static final String INSTANCE_IS_NOT_STARTED;
    static final String INSTANCE_IS_NOT_STOPPED;
    static final String ERR_OF_SERVICE;
    static final String UNIT;
    static final String _NOT_FOUND;
    static final String TITLE_INSTANCE;
    static final String TRY_RESTORE_DB_CONNECTION;
    static final String DB_CONNECTION_RESTORED;
    static final String MEMORY_CONSUMPTION_DECREASED_TO;
    static final String MEMORY_CONSUMPTION_INCREASED_TO;
    static final String TRACE_PROFILE_CHANGED;
    static final String FILE_TRACE_OPTIONS_CHANGED;
    //static final String STOPPING_UNIT;
    static final String UNIT_STOPPED;
    static final String TAB_TRACE;
    static final String TAB_UNITS;
    static final String TAB_ARTE_POOL;
    static final String ERR_CANT_READ_SERVICE_OPTIONS;
    static final String ERR_CANT_READ_KERBEROS_OPTIONS;
    static final String ERR_KEYTAB_DOES_NOT_EXIST;
    static final String ERR_WRONG_SPN_FORMAT;
    static final String ERR_ON_KDC_LOGIN;
    static final String ERR_CANT_GET_GSS_CREDENTIALS;
    static final String WARN_NO_COMPATIBLE_KEY_IN_KEYTAB;
    static final String ERR_UNHANDLED_IN_SERVICE;
    static final String ERR_ON_SINGL_UNIT_LOCK_RELEASE_;
    static final String ERR_WHILE_CHECKING_KEYSTORE;
    static final String KEYSTORE_LOADER_FROM;
    static final String INCORRECT_KEYSTORE_PASSWORD;
    static final String HINT_YOU_HAVE_TO_ENABLE_SENSITIVE_TRC_LOCALY;
    static final String HINT_YOU_HAVE_TO_ENABLE_SENSITIVE_TRC_GLOBALY;
    static final String LBL_GLB_SENSITIVE_TRC_PERMIS;
    static final String LBL_IS_OFF;
    static final String LBL_IS_ON;
    static final String LBL_LOCAL_SENSITIVE_TRC_FINISH_TIME;
    static final String LBL_SENSITIVE_TRC_IS_ON_TILL_;
    static final String MSG_SENSITIVE_TRC_IS_OFF_DUE_TO_GLBL_SET;
    static final String MSG_SENSITIVE_TRC_IS_OFF;
    static final String TITLE_SENSITIVE_DATA_TRACING_OPTIONS;
    static final String TITLE_INFO;
    static final String SERVER_KEYSTORE_WAS_REREAD;
    static final String STARTER_TEMP_DIR;
    static final String USING_;
    static final String USING_CONFIG_FILE;
    static final String EXPIRE_SOON;
    static final String LESS_THEN;
    static final String DAYS;
    static final String ONE_DAY;
    static final String ONE_HOUR;
    static final String TEN_MINUTES;
    static final String ONE_MINUTE;
    static final String LICENSES_UPDATED;
    static final String EXPIRED;
    static final String LICENSE_SET_IN_USE;
    static final String LOADING_ARTE_POOL;
    static final String AADC_MEMBER;
    static final String AADC_DG_ADDRESS;

    static String getStateMessageMslId(final InstanceState state) {
        if (state == InstanceState.STOPPED) {
            return MLS_ID_ST_INST_STOPPED;
        }
        if (state == InstanceState.STARTING) {
            return MLS_ID_ST_INST_STARTING;
        }
        if (state == InstanceState.STARTED) {
            return MLS_ID_ST_INST_STARTED;
        }
        if (state == InstanceState.STOPPING) {
            return MLS_ID_ST_INST_STOPPING;
        }
        throw new IllegalStateException("Message is not defined for instance state: " + String.valueOf(state));
    }
    private static final String MLS_ID_ST_INST_STOPPED = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mls4A77OUCORVDW7N5YXRJQLIK6PA";   // MLS "Instance \"%1\" stopped on host %2", Event, Instance
    private static final String MLS_ID_ST_INST_STARTING = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsFFZFHPEYX5EGPP4WYQYB5UJSRE"; // MLS "Starting instance \"%1\" on host:%2", Event, Instance
    private static final String MLS_ID_ST_INST_STARTED = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mls2NABA7556FELVNGSHKQO6JTVYE";   // MLS "Instance \"%1\" started on host %2", Event, Instance
    private static final String MLS_ID_ST_INST_STOPPING = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsSGQFF7X7JJBVLPDWB2FAH7TPAA"; // MLS "Stopping instance \"%1\" on host:%2", Event, Instance
    static final String MLS_ID_TRY_SHUTDOWN_INSTANCE = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsI4RIDXYFZRDBHAJHZXSLIJ4PU4";// MLS "Try to shutdown instance \"%1\"", Event, Instance
    static final String MLS_ID_TRY_INTERRUPT_INSTANCE = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsUJF25HEJWBHN7CW5YZHQSBPSEA";// MLS "Try to interrupt thread of instance \"%1\"", Warning, Instance
    static final String MLS_ID_ERR_ON_INSTANCE_SHUTDOWN = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsN223BRGAHVDUXJMYY2PKBQ75XQ";// MLS "Error on instance \"%1\" shutdown: %2", Error, Instance
    static final String MLS_ID_ERR_OF_SERVICE = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsG5XOI5JYI5H4TO3MBVEYIA425I";// MLS "Error of instance \"%1\" remote control service: %2", Error, Instance
    static final String MLS_ID_ERR_IN_DB_QRY = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mls7P3P4TKFOJGP7BZG3V5TMYGK2Q";// MLS "Error on creating or executing service db query: %1", Error, Instance
    static final String MLS_ID_DB_CONNECTION_RESTORED = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsMG5IJG5SWZHHBEHQ3NYU36Y7XA"; // MLS "DB connection restored by instance \"%1\"", Event, Instance
    static final String MLS_ID_TRACE_PROFILE_CHANGED = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsYKJI7FIPRREBHJZ5AJVCSQHJJU";// MLS "Trace profile of instance \"%1\" changed. New profile is \"%2\"", Event, Instance
    static final String MLS_ID_ERR_ON_TRACE_OPTION_READING = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsSYA5QNCLCVCTDK2YNKVLW2TQGQ"; // MLS "Can't read trace options of instance \"%1\": %2", Error, Instance
    static final String MLS_ID_ERR_CANT_READ_SERVICE_OPTIONS = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mls34IME7VSPFE5ZJZV7CLQZZTLFU"; // MLS "Can't read instance \"%1\" service options: %2", Error, Instance
    static final String MLS_ID_ERR_CANT_READ_KERBEROS_OPTIONS = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsPIPFE42J3VGCPA4UAXRCPEMZKU"; // MLS "Exception on kerberos configuration options reading for instance \"%1\": %2", Error, Instance
    static final String MLS_ID_ERR_KEYTAB_DOES_NOT_EXIST = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsWSRBXTLM6BBS7C7GEULQILXNHU"; // MLS "Keytab file \"%1\" does not exist.\nKerberos authentication will be disabled", Error, Instance  
    static final String MLS_ID_ERR_WRONG_SPN_FORMAT = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsOFU5FWVWRRAS5HCJ634JNTJYB4"; // MLS "Wrong format of service principal name \"%1\".\nKerberos authentication will be disabled", Error, Instance
    static final String MLS_ID_ERR_WRITE_KRB_LOGIN_CONF_FILE = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsCR3BUPRKYBDVBAZPBBEWDFUUSE";//MLS "Instance \"%1\". Can't create kerberos login configuration file \"%2\":\n%3\nKerberos authentication will be disabled", Error, Instance
    static final String MLS_ID_ERR_ON_KDC_LOGIN = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsMIMKFUP2HRGK3LGKVDX447TWKM";// MLS "Instance \"%1\". Error on login to key distribution center as service \"%2\":\n%3\nKerberos authentication will be disabled", Error, Instance
    static final String MLS_ID_ERR_CANT_GET_GSS_CREDENTIALS = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsDCLXND5SOZFLVPQZCN32ISXPZY";// MLS Instance \"%1\". Can't get credentials for service "%2":\n%3\nKerberos authentication will be disabled
    static final String MLS_ID_WARN_NO_COMPATIBLE_KEY_IN_KEYTAB = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsFD36C3AO5VHYNGCM2EUKWS232A"; // MLS "Keytab file \"%1\" does not contain any compatible key for \"%2\" service", Error, Instance
    static final String MLS_ID_INSTANCE_THREAD_INTERRUPTED = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsBRWPXHNWXZHNVBIO6FU67QJLSU"; // MLS "Instance \"%1\" thread is interrupted", Event, Instance
    static final String MLS_ID_INSTANCE_THREAD_INTERRUPTED_BY_EXCEPTION = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsOMMGUUYTBBASXK2LTDSPWZZHFA"; // MLS "Instance \"%1\" thread interrupted by error: %2", Error, Instance
    static final String MLS_ID_ERR_ON_INSTANCE_STOP = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsERGRSWPSZNEKNOT2FQJ627MLVU";// MLS "Error on instance \"%1\" stop: %2", Error, Instance
    static final String MLS_ID_UNIT_LOADED = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsOP2KJSZ76BBORJLKBCCPVYDK3I";// MLS "Unit %1 loaded by instance \"%2\"", Event, Instance
    static final String MLS_ID_UNIT_STARTED = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsNSE6L4QJL5ADDB3X5KY3BGLRPM";// MLS "Unit %1 started", Event, Instance
    static final String MLS_ID_UNIT_START_POSTPONED = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mls6O3RWJQNBZFPNJWZ72U42HGT7M";// MLS "Unit %1 start postponed because another unit of such type is already started.", Event, Instance
    static final String MLS_ID_UNIT_RESTARTED = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mls6VDY4TJQMNHMNB5XMHGKO4HEZQ";// MLS "Unit %1 restarted", Event, Instance
    static final String MLS_ID_UNIT_STOPPED = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsQ45MXNOUSRHDTPFTBF67USUHC4";// MLS "Unit %1 stopped", Event, Instance
    static final String MLS_ID_UNIT_UNLOADED = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mls3QGLE762JRHT5E7GZT3J5JDZRY";// MLS "Unit %1 unloaded", Event, Instance
    static final String MLS_ID_UNIT_LOAD_ERROR = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsUXQIGE4OTJAI7ASER7BETFPPV4";// MLS "Error on loading unit %1: %2", Error, Instance
    static final String MLS_ID_ERR_ON_UNIT_UNLOAD = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsKKJEF3ORHJAARGSDWXF3ZFJQVU";// MLS "Error on unloading unit %1: %2", Error, Instance
    static final String MLS_ID_ERR_ON_UNIT_STOP = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsAYDHSWDIWZD4HPX2IJB5QTJCVU";// MLS "Error on stopping unit %1: %2", Error, Instance
    static final String MLS_ID_ERR_ON_UNIT_START = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsS2S4NSIDJZBY3BXQOMWRAA3GZM";// MLS "Error on starting unit %1: %2", Error, Instance
    static final String MLS_ID_ERR_ON_UNIT_RESTART = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsNQJP3X2TYJDQJCW2DAUL4XNJT4";// MLS "Error on restarting unit %1: %2", Error, Instance
    static final String MLS_ID_ERR_ON_UNITS_START = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsIFCI4SFUN5FEJMEHSDHX6PABLU";// MLS "Instance \"%1\" remote control service cann't start units: %2", Error, Instance
    static final String MLS_ID_ERR_ON_UNITS_RESTART = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsNB5IE3XBMRADVLQU6DGKKQIWW4";// MLS "Instance \"%1\" remote control service cann't restart units: %2", Error, Instance
    static final String MLS_ID_ERR_ON_UNITS_STOP = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsTRRBKGWWIJFHFLZEUUUVYYMJLM";// MLS "Instance \"%1\" remote control service cann't stop units: %2", Error, Instance
    static final String MLS_ID_ERR_UNHANDLED_IN_SERVICE = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsHONB5ZBOYNFT5J7JFFZCAANDY4"; // MLS "Unhandled exception in serive \"%1\" request processing: %2", Warning, Instance
    static final String MLS_ID_ERR_ON_LOADER_ACTUALIZE = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsPT35CMP7B5C5ZGITKRWCTYXLBA"; // MLS "Instance \"%1\" can't load definitions: %2", Error, Instance
    static final String MLS_ID_ERR_KERNEL_VER = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mls4XGHTZQC4FDWJN4GVMPKQ5RTL4"; // MLS "Instance \"%1\" runtime is not actual. Loaded version: %2, actual: %3.", Error, Instance
    static final String MLS_ID_ERR_DDS_VER = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mls7QAQ2GHEKBAUVF2LFGPCV4TAA4"; // MLS "Instance \"%1\" data structure metainformation is not correspoding the database. Metainformation version: %2, database version: %3.", Warning, Instance
    static final String MLS_ID_VER_SWITCHED = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mls7LT25XFGZVGERAXBCX3S275WBY"; // MLS "Instance \"%1\" switched to new definition version: %2.", Event, Instance
    static final String MLS_ID_ERR_ON_SINGL_UNIT_LOCK_RELEASE = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mls3SMGEIZ6KVH6RPVLZCVW7HLBQU"; // Instance "%1" can't release singleton unit type session lock: %2, Error, Instance
    static final String MLS_ID_STARTER_TEMP_DIR = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsCEHPOS66QFB3RDCF5FOSIDA5AY";// MLS Directory for starter temporary files: %1, Event, Instance
    static final String MLS_ID_FILE_TRACE_OPTIONS_CHANGED = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsHDEXIWGNXNB2VNATN2NMKAHYKA";// MLS File trace options of instance "%1" were changed. New options are:  "%2"
    static final String MLS_ID_DB_IS_ACTUAL = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsZE4OHXHTTJG5RJUG6Z4TT6TBCQ";// MLS Instance "%1" data structure metainformation corresponds to database., Event, Instance
    static final String MLS_ID_WARNING_DDS_UPGRADE = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsFEFP3NGZMZCPLFHU5GCSWVFEDA"; //MLS Instance "%1" database structure of layer "%2" is upgrading to version %3 since %4., Warning, Instance
    static final String MLS_ID_ERR_DDS_UNCOMPATIBLE_UPGRADE = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsBP5Z5T7QLFEXRBW3YJDBMSNEBM";//MLS  Instance "%1"'s database structure of layer "%2" is upgrading to version %3 since %4. The modifications are not compatible with used database structure metainformation., Error, Instance
    static final String MLS_ID_ERR_UNCOMPATIBLE_DDS_VER = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsBWHORUWMCFBZFNGVWLZ4RX7JBQ";//MLS Instance "%1"'s data structure metainformation does not compatible with the database. Layer: "%2", metainformation version: %3 database compatible with version %4 and newer.,Error, Instance
    static final String MLS_ID_GLOBAL_SENS_TRC_ON = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mls7DY7OYBD5NBZTPXUXCRIPNJSU4"; // MLS Global sensitive data tracing is on, Event, Instance
    static final String MLS_ID_GLOBAL_SENS_TRC_OFF = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsBFAWLENH6RAS7I5P4EICVYAD6U"; // MLS Global sensitive data tracing is off, Event, Instance
    static final String MLS_ID_ERR_APP_DB_MISTIMING = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsTKAZGDU4GRH27BGWNOWFLMHQQ4"; //MLS Instance "%1"'s time (%2) is not corresponding to database time (%3)., Instance, Error
    static final String MLS_ID_WARNING_APP_DB_LOCAL_MISTIMING = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsQQLLWU4BAZD7VA5BGO2K3MRKMU"; //MLS Local instance "%1"'s time from database point of view (%2) is not corresponding to database time (%3)., Instance, Warning
    static final String MLS_ID_AUTO_ACTUALIZE_ON = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsJY5OMJJ7T5CMXMP5QM65Y3DU4U"; //Autoupdate of runtime components is on for instance "%1", Instance, Event
    static final String MLS_ID_AUTO_ACTUALIZE_OFF = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsADJEGNL32FBWVDZI2GNCK2ZI6M"; //Autoupdate of runtime components is off for instance "%1", Instance, Event
    static final String MLS_ID_CHECK_FOR_UPDATES = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsYZ6746F46FA7ZDULDDN2BUGFGI";//Instance "%1" checks for updates of runtime components, Instance, Event
    static final String MLS_ID_MEMORY_CONSUMPTION_DECREASED_TO = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mls7CPM5LJFA5ANXIJXUPK5ZDDRBM";//"%1" memory consumption decreased to %2.
    static final String MLS_ID_MEMORY_CONSUMPTION_INCREASED_TO_EVENT = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsZCGTTCUJTRE4RKUCRVEALLKVKI";//"%1" memory consumption increased to %2.
    static final String MLS_ID_MEMORY_CONSUMPTION_INCREASED_TO_WARNING = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsFGZBIAJJIZE7JBOR4OUZP7ESOQ";//"%1" memory consumption increased to %2.
    static final String MLS_ID_MEMORY_CONSUMPTION_INCREASED_TO_ERROR = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsTGTDPTEKUVEINEWEZBC3L45RRQ";//"%1" memory consumption increased to %2.
    static final String MLS_ID_MEMORY_CONSUMPTION_INCREASED_TO_ALARM = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mls3SX5IR7UUJB2VDH2AYCRJWRR3A";//"%1" memory consumption increased to %2.
    static final String MLS_ID_START_OPTIONS = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mls6NS2EQ422JEYDOSKB2CCS2E6TA";//Start options of instance "%1" are: %2, Instance, Event
    static final String MLS_ID_OPTIONS_CHANGED = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsJXKSABEPMJBI3NZWJXJ2QL2SNA";//Instance "%1" options changed. New options are: %2, Instance, Event
    static final String MLS_ID_WARN_RUNNING_PIDS_DISCOVERY_BROKEN = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsHD3IGZCB6RB7FHJT3MPCJLCUV4";//Unable to determine currently running process pids, cleanup of outdated starter temporary files has been disabled, Instance, Warning
    static final String MLS_ID_USING_ = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsRXPPB34LDZFZFBLCSISOYPL7WA";//Using %1, Event, Server.Instance
    static final String MLS_ID_ERR_ON_FILE_LOG_MAINTENANCE = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsYXT2HG7R65DBLHFTPMXXZEGL7I";//Error on file log '%1' maintenance: %2, Server.Instance, Error
    static final String MLS_ID_DB_CONFIG_CHANGED = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsN7I75NIGKBGXXINDRLM5L62IFU";//eventCode["New database configuration has been loaded: %1"], Server.Instance, Event
    static final String MLS_ID_RELOAD_ARTE_POOL_REQUESTED = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mls23QB7AHI7NG63FUZ2PALON3KZQ";//eventCode["ARTE pool reload requested. Reason: '%1'"] Server.Instance, Event
    static final String MLS_ID_INSTANCE_MAINTENANCE_REQUESTED = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsCVMUWFXSSBEXHA7CXF6FXKWQ6A";//eventCode["Instance maintenance requested. Reason: '%1'"] Server.Instance, Event
    static final String MLS_ID_INVALID_LOCAL_STARTER_VERSION = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsERLAGQ7ZLNER3HU4OJGZJWG6IY";//eventCode["Local starter version (%1) is not actual (%2)"], Warning
    static final String MLS_ID_USING_CONFIG_FILE = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsH4HA4C4OLZHWHAGNHUTRNVTXZA";//eventCode["Server is using configuration file '%1'"], Event
    static final String MLS_ID_LOADING_ARTE_POOL = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsOQBR4YZAPRDINBZZDZOPJOZRWI";//eventCode["eventCode["Loading ARTE Pool..."]"], Event
    static final String MLS_ID_KEYSTORE_LOADED_FROM = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsACF3ZADBQNFFRNEUVD37RY5J44";//eventCode["Keystore has been loaded from '%1'"], Event, Instance
    public static final String MLS_ID_ACCEPTED_VERSIONS_CHANGED = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsHBFRLJIZ2VEZTKGHXR6WNLEKOA";//eventCode["Instance '%1' serves versions [%2..%3]"], Event, Instance

}
