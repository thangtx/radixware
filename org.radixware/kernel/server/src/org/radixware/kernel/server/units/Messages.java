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

package org.radixware.kernel.server.units;

import java.util.ResourceBundle;
import org.radixware.kernel.common.exceptions.RadixError;

/**
 * ��������� ������������ ������� 
 *
 */
public final class Messages {
	private Messages(){};

    static {
        final ResourceBundle bundle = ResourceBundle.getBundle("org.radixware.kernel.server.units.mess.messages");

        TITLE_UNIT = bundle.getString("TITLE_UNIT");
        ERR_IN_DB_QRY = bundle.getString("ERR_IN_DB_QRY");
        ERR_CANT_READ_SERVICE_OPTIONS = bundle.getString("ERR_CANT_READ_SERVICE_OPTIONS");
        UNIT_IS_NOT_STARTED = bundle.getString("UNIT_IS_NOT_STARTED");
        ERR_ON_DB_CONNECTION_CLOSE = bundle.getString("ERR_ON_DB_CONNECTION_CLOSE");
        ERR_ON_TRACE_OPTION_READING = bundle.getString("ERR_ON_TRACE_OPTION_READING");
        ERR_CANT_READ_OPTIONS = bundle.getString("ERR_CANT_READ_OPTIONS");
        ERR_CANT_CREATE_SSLCONTEXT = bundle.getString("ERR_CANT_CREATE_SSLCONTEXT");
        UNIT_THREAD_INTERRUPTED = bundle.getString("UNIT_THREAD_INTERRUPTED");
        UNIT_THREAD_INTERRUPTED_BY_EXCEPTION = bundle.getString("UNIT_THREAD_INTERRUPTED_BY_EXCEPTION");
        ERR_ON_UNIT_STOP = bundle.getString("ERR_ON_UNIT_STOP");
        TRY_SHUTDOWN_UNIT = bundle.getString("TRY_SHUTDOWN_UNIT");
        ERR_ON_UNIT_SHUTDOWN = bundle.getString("ERR_ON_UNIT_SHUTDOWN");
        TRY_INTERRUPT_UNIT = bundle.getString("TRY_INTERRUPT_UNIT");
        TITLE_ERROR = bundle.getString("TITLE_ERROR");
        CONFIRM_UNIT_STOP = bundle.getString("CONFIRM_UNIT_STOP");
        CONFIRM_UNIT_RESTART = bundle.getString("CONFIRM_UNIT_RESTART");
        TITLE_CONFIRM = bundle.getString("TITLE_CONFIRM");
        UNIT_IS_NOT_STOPPED = bundle.getString("UNIT_IS_NOT_STOPPED");
        ST_UNIT_STOPPED = bundle.getString("ST_UNIT_STOPPED");
        ST_UNIT_STARTING = bundle.getString("ST_UNIT_STARTING");
        ST_UNIT_STARTED = bundle.getString("ST_UNIT_STARTED");
        ST_UNIT_START_POSTPONED = bundle.getString("ST_UNIT_START_POSTPONED");
        ST_UNIT_STOPPING = bundle.getString("ST_UNIT_STOPPING");
        //TRY_RESTORE_DB_CONNECTION = bundle.getString("TRY_RESTORE_DB_CONNECTION");
        DB_CONNECTION_RESTORED = bundle.getString("DB_CONNECTION_RESTORED");
        TRACE_PROFILE_CHANGED = bundle.getString("TRACE_PROFILE_CHANGED");
        FILE_TRACE_OPTIONS_CHANGED = bundle.getString("FILE_TRACE_OPTIONS_CHANGED");
        SCP_CHANGED = bundle.getString("SCP_CHANGED");
        START_OPTIONS = bundle.getString("START_OPTIONS");
        OPTIONS_CHANGED = bundle.getString("OPTIONS_CHANGED");
        UNIT_START_POSPONED = bundle.getString("UNIT_START_POSPONED");
        UNABLE_TO_START_UNIT = bundle.getString("UNABLE_TO_START_UNIT");
        UNIT_ID_IS_ALREADY_RUNNING = bundle.getString("UNIT_ID_IS_ALREADY_RUNNING");
        CONFIRM_UNIT_INTERRUPT = bundle.getString("CONFIRM_UNIT_INTERRUPT");
        COMMAND_FROM_GUI = bundle.getString("COMMAND_FROM_GUI");
        UNIT_THREAD_UNEXPECTED_STOP = bundle.getString("UNIT_THREAD_UNEXPECTED_STOP");
    }
    
    public static final String COMMAND_FROM_GUI;
    public static final String ERR_IN_DB_QRY;
    public static final String UNIT_IS_NOT_STARTED;
    public static final String UNABLE_TO_START_UNIT;
    public static final String UNIT_ID_IS_ALREADY_RUNNING;
    public static final String ERR_ON_DB_CONNECTION_CLOSE;
    public static final String ERR_CANT_READ_SERVICE_OPTIONS;
    public static final String ERR_CANT_READ_OPTIONS;
    public static final String ERR_CANT_CREATE_SSLCONTEXT;
    static final String ERR_ON_TRACE_OPTION_READING;
    static final String TITLE_UNIT;
    static final String UNIT_THREAD_INTERRUPTED;
    static final String UNIT_THREAD_UNEXPECTED_STOP;
    static final String UNIT_THREAD_INTERRUPTED_BY_EXCEPTION;
    static final String ERR_ON_UNIT_STOP;
    static final String TRY_SHUTDOWN_UNIT;
    static final String ERR_ON_UNIT_SHUTDOWN;
    static final String TRY_INTERRUPT_UNIT;
    static final String TITLE_ERROR;
    static final String CONFIRM_UNIT_STOP;
    static final String CONFIRM_UNIT_RESTART;
    static final String CONFIRM_UNIT_INTERRUPT;
    static final String TITLE_CONFIRM;
    static final String UNIT_IS_NOT_STOPPED;
    public static final String START_OPTIONS;
    public static final String OPTIONS_CHANGED;

    static String getStateMessage(final UnitState state) {
        switch (state){
            case STOPPED:
                return ST_UNIT_STOPPED;
            case STARTING:
                return ST_UNIT_STARTING;
            case STARTED:
                return ST_UNIT_STARTED;
            case STOPPING:
                return ST_UNIT_STOPPING;
            case START_POSTPONED:
                return ST_UNIT_START_POSTPONED;
            default:
                throw new RadixError("Message is not defined for new unit state: " + String.valueOf(state));
        }
    }
    static private final String ST_UNIT_STOPPED;
    static private final String ST_UNIT_STARTING;
    static private final String ST_UNIT_STARTED;
    static private final String ST_UNIT_START_POSTPONED;
    static private final String ST_UNIT_STOPPING;
    //static final String TRY_RESTORE_DB_CONNECTION;
    static final String DB_CONNECTION_RESTORED;
    static final String TRACE_PROFILE_CHANGED;
    static final String FILE_TRACE_OPTIONS_CHANGED;
    static final String SCP_CHANGED;
    static final String UNIT_START_POSPONED;

    static String getStateMessageMslId(final UnitState state) {
        switch (state){
            case STOPPED:
                return MLS_ID_ST_UNIT_STOPPED;
            case STARTING:
                return MLS_ID_ST_UNIT_STARTING;
            case STARTED:
                return MLS_ID_ST_UNIT_STARTED;
            case STOPPING:
                return MLS_ID_ST_UNIT_STOPPING;
            case START_POSTPONED:
                return MLS_ID_ST_UNIT_START_POSTPONED;
            default:
                throw new RadixError("Message is not defined for new unit state: " + String.valueOf(state));
        }
    }
    private static final String MLS_ID_ST_UNIT_STOPPED = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mls4OO2PNDCDBGCPGMJ43DHGLS6VQ";   // MLS "Unit \"%1\" stopped", Event, Unit 
    private static final String MLS_ID_ST_UNIT_STARTING = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsYTLBWYXLMVEWRFS3SYFVQ4RDWU"; // MLS "Starting unit \"%1\"", Event, Unit
    private static final String MLS_ID_ST_UNIT_STARTED = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsCCWQQSWIFRETDCSMHCGFHGNG2I";   // MLS "Unit \"%1\" started", Event, Unit
    private static final String MLS_ID_ST_UNIT_START_POSTPONED = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mls7UXWFGNW2JFITM24D2KF63LTHE"; // MLS "Unit \"%1\" start postponed", Event, Unit
    private static final String MLS_ID_ST_UNIT_STOPPING = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsWI2S6CL6YVCGTJEQ22EGMYRHZ4"; // MLS "Stopping unit \"%1\"", Event, Unit
    public static final String MLS_ID_START_OPTIONS = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsARHFDQHGHBG7PEJERQNDCLYMPU"; // MLS "Start options of unit \"%1\" is: %2", Event, Unit
    public static final String MLS_ID_OPTIONS_CHANGED = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsPA6SRTZXEZCHNI6KIBG4HFME3A"; // MLS "Unit \"%1\" options changed. New options is: %2", Event, Unit
    public static final String MLS_ID_ERR_IN_DB_QRY = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsOZ2JS6XCJBD2JBP7EVYB56F6NY"; // MLS "Error on executing or preparing service DB query of \"%1\" unit: %2", Error, Unit  
    public static final String MLS_ID_ERR_CANT_READ_OPTIONS = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsYKG23NWFMRHJ3A4AOJDBOKCEYI"; // MLS "Can' read \"%1\" unit options: %2", Error, Unit
    public static final String MLS_ID_ERR_ON_DB_CONNECTION_CLOSE = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsUOUFW4DBKBHE7BLXWNWP2SB6YY";// MLS "Error on \"%1\" unit DB connection close: %2", Error, Unit
    public static final String MLS_ID_ERR_CANT_CREATE_SSLCONTEXT = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsHVN76PDSVNGMBMTZNP6AHEJC4M";// MLS "Can't create SSLContext: %1", Error, Unit
    static final String MLS_ID_TRY_SHUTDOWN_UNIT = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsKCGZ7H57ABFCXDCDKGTXKFX2FQ";// MLS "Try to shutdown unit \"%1\"", Event, Unit
    static final String MLS_ID_ERR_ON_UNIT_SHUTDOWN = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsEYNQ7S4BG5EWPFSNUOJ6L3FCTU"; // MLS "Error on unit \"%1\" shutdown: %2", Error, Unit
    static final String MLS_ID_TRY_INTERRUPT_UNIT = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsIJQX76PMBVCCJPSZMHHU5POBQI";// MLS "Try to interrupt unit \"%1\" thread", Warning, Unit 
    static final String MLS_ID_DB_CONNECTION_RESTORED = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsHVJ5R2FB7ZF4PIBKNRKTWCU544";// MLS "DB connection restored by \"%1\" unit", Event, Unit
    static final String MLS_ID_TRACE_PROFILE_CHANGED = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsPBTEGYEGBBCMJA2UM6VT4OBX24";// MLS "\"%1\" unit trace profile changed. New profile is \"%2\"", Event, Unit
    static final String MLS_ID_UNIT_THREAD_INTERRUPTED = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsGOXBD42OZZETBGPFUHTMYPI7ZU";// MLS "\"%1\" unit thread is interrupted", Event, Unit
    static final String MLS_ID_UNIT_THREAD_INTERRUPTED_BY_EXCEPTION = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsSRLK77XXJVHU7ATMQ7ERO3AFCM";// MLS "\"%1\" unit thread is interrupted by error: %2", Error, Unit
    static final String MLS_ID_ERR_ON_UNIT_STOP = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsOCMGIYWMJZA6BI4FXVT4K2IBEU";// MLS "Error on \"%1\" unit stop: %2", Error, Unit
    static final String MLS_ID_ERR_ON_TRACE_OPTION_READING = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsME7WWMQCTNBT5B7USZWUVXRBJM"; // MLS "Can't read \"%1\" unit trace options: %2", Error, Unit
    static final String MLS_ID_UNIT_START_POSPONED = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsLG72CWK6SFGRTBQKZ3L56KCDOI"; // MLS "Unit \"%1\" start was postponed because unit of same type is already started", Event, Unit
    static final String MLS_ID_SCP_CHANGED = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsHSQZTLP2SBGI7C7OG2FX4YK75U";// MLS "\"%1\" unit SCP changed. New SCP is \"%2\"", Event, Unit
    static final String MLS_ID_FILE_TRACE_OPTIONS_CHANGED = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mls3X5WYA3VRRFWVKD77KBZ4OM23Y";// MLS "%1" unit file trace options where changed. New options are "%2"
    static final String MLS_ID_UNIT_START_POSPONED_WITH_REASON = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsKVQ4YU4QWFGYJJXGK4AIYTMUHE";// MLS Unit "%1" start postponed. Reason: %2, Event, Unit
}
