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

package org.radixware.kernel.server.units.job.executor;

import java.util.ResourceBundle;


final class JobExecutorMessages {

    private JobExecutorMessages() {
    }
    static final String JOB_NOT_EXECUTING_ON_RESPONSE;
    static final String JOB_EXECUTOR_UNIT_TYPE_TITLE;
    static final String PARALLEL_COUNT;
    static final String ABOVE_NORMAL_DELTA;
    static final String HIGH_DELTA;
    static final String VERY_HIGH_DELTA;
    static final String CRITICAL_DELTA;
    static final String EXEC_PERIOD;
    static final String PRIORITY_MAP;
    static final String JOB;
    static final String HUNG_JOB_UNLOCKED;
    static final String HUNG_JOB_REMOVED;
    //static final String CLASS_NAME;
    //static final String METHOD_NAME;
    //static final String PARAMETERS;
    //static final String DAC_MODE;
    //static final String DAC_BRANCH_ID;
    static final String CLIENT_LANG;
    static final String ERR_CANT_LOAD_JOB;
    static final String ERR_UNIT_IS_OVERLOADED;
    static final String _TERMINATED_BY_ERR;
    static final String _STARTED;
    static final String _IS_DONE;
    static final String TAB_TRACE;
    static final String TAB_STATISTIC;
    static final String LBL_TASKS_QUANTITY;
    static final String MLS_ID_JOB_STARTED = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsIIDJM4EOLZCI7BPX7PLQZOPJYM"; // MLS "Job "#%1-%2" started", Event, JobExecutor
    static final String MLS_ID_JOB_TERMINATED_BY_ERR = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsLFSZCKDHNRFIHKUTR6QB34YWMQ";  // MLS "Job "#%1-%2" terminated by exception: %3", Error, JobExecutor
    static final String MLS_ID_JOB_IS_DONE = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mls74AJIIZJABGA7IEFV2LPWTHZME";  // MLS "Job "#%1-%2" is done", Event, JobExecutor
    static final String MLS_ID_ERR_UNIT_IS_OVERLOADED = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsYLRKOEWS5FFBDDCUY5M6CAMGBA";// "Job executor \"%1\" is overloaded by jobs", Warning, JobExecutor  
    static final String MLS_ID_ERR_CANT_LOAD_JOB = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsPO6MAA6YEBDJHKFXOYCG7AZNNY";// "Job executor \"%1\" can't load job: %2", Error, JobExecutor 
    static final String MLS_JOB_NOT_EXECUTING_ON_RESPONSE = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsUEKW4QSKKVEVTMLYWL334U4RB4";// eventCode["Job is not marked as executing upon response receiving: %1"], Warning, JobExecutor
    static final String MLS_HUNG_JOB_UNLOCKED = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsQZHILHKS65EMNP4TD3N4DBMFLI";// eventCode["Job '%1' executing on '%2' was unlocked because of no activity checks in %3 seconds"], Warning, JobExecutor
    static final String MLS_HUNG_JOB_REMOVED = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsGR2FCN2QGVEIRO5CJGYBNPAWUQ";//eventCode["Job '%1' executing on '%2' was removed because of no activity checks in %3 seconds"], Error, JobExecutor
    
    

    static {
        final ResourceBundle bundle = ResourceBundle.getBundle("org.radixware.kernel.server.units.job.executor.mess.messages");
        
        JOB_NOT_EXECUTING_ON_RESPONSE = bundle.getString("JOB_NOT_EXECUTING_ON_RESPONSE");

        JOB_EXECUTOR_UNIT_TYPE_TITLE = bundle.getString("JOB_EXECUTOR_UNIT_TYPE_TITLE");
        PARALLEL_COUNT = bundle.getString("PARALLEL_COUNT");
        ABOVE_NORMAL_DELTA = bundle.getString("ABOVE_NORMAL_DELTA");
        HIGH_DELTA = bundle.getString("HIGH_DELTA");
        VERY_HIGH_DELTA = bundle.getString("VERY_HIGH_DELTA");
        CRITICAL_DELTA = bundle.getString("CRITICAL_DELTA");
        EXEC_PERIOD = bundle.getString("EXEC_PERIOD");
        PRIORITY_MAP = bundle.getString("PRIORITY_MAP");

        JOB = bundle.getString("JOB");
        //CLASS_NAME=bundle.getString("CLASS_NAME");
        //METHOD_NAME=bundle.getString("METHOD_NAME");
        //PARAMETERS=bundle.getString("PARAMETERS");
        //DAC_MODE= bundle.getString("DAC_MODE");
        //DAC_BRANCH_ID=bundle.getString("DAC_BRANCH_ID");
        CLIENT_LANG = bundle.getString("CLIENT_LANG");

        ERR_CANT_LOAD_JOB = bundle.getString("ERR_CANT_LOAD_JOB");
        ERR_UNIT_IS_OVERLOADED = bundle.getString("ERR_UNIT_IS_OVERLOADED");

        _TERMINATED_BY_ERR = bundle.getString("_TERMINATED_BY_ERR");
        _STARTED = bundle.getString("_STARTED");
        _IS_DONE = bundle.getString("_IS_DONE");

        TAB_TRACE = bundle.getString("TAB_TRACE");
        TAB_STATISTIC = bundle.getString("TAB_STATISTIC");

        LBL_TASKS_QUANTITY = bundle.getString("LBL_TASKS_QUANTITY");
        
        HUNG_JOB_UNLOCKED = bundle.getString("HUNG_JOB_UNLOCKED");
        HUNG_JOB_REMOVED = bundle.getString("HUNG_JOB_REMOVED");
        
    }
}
