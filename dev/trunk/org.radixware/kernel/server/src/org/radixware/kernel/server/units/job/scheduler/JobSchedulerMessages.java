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

package org.radixware.kernel.server.units.job.scheduler;

import java.util.ResourceBundle;

final class JobSchedulerMessages {

    private JobSchedulerMessages() {
    }
    static final String JOB_SCHD_UNIT_TYPE_TITLE;
    static final String SCHD_JOB;
    static final String _SCHEDULED_TO_BE_EXEC_AT_;
    public static final String WARN_TASK_IS_ALREADY_SCHEDULED;
    public static final String WARN_SCHEDULE_IS_CALLED_TO_EARLY;
    public static final String WARN_TASK_IS_RUNNING_FOR_TOO_LONG;
    public static final String WARN_TASK_HAS_BEEN_ACTUALIZED;

    static {
        final ResourceBundle bundle = ResourceBundle.getBundle("org.radixware.kernel.server.units.job.scheduler.mess.messages");

        JOB_SCHD_UNIT_TYPE_TITLE = bundle.getString("JOB_SCHD_UNIT_TYPE_TITLE");
        SCHD_JOB = bundle.getString("SCHD_JOB");
        _SCHEDULED_TO_BE_EXEC_AT_ = bundle.getString("_SCHEDULED_TO_BE_EXEC_AT_");
        WARN_TASK_IS_ALREADY_SCHEDULED = bundle.getString("WARN_TASK_IS_ALREADY_SCHEDULED");
        WARN_SCHEDULE_IS_CALLED_TO_EARLY = bundle.getString("WARN_SCHEDULE_IS_CALLED_TO_EARLY");
        WARN_TASK_IS_RUNNING_FOR_TOO_LONG = bundle.getString("WARN_TASK_IS_RUNNING_FOR_TOO_LONG");
        WARN_TASK_HAS_BEEN_ACTUALIZED = bundle.getString("WARN_TASK_HAS_BEEN_ACTUALIZED");
    }
    static final String MLS_ID_SCHD_JOB_SCHEDULED = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsX67TPBPUWBBPVKM5QZAJGXABKU"; //MLS "Periodical job #%1 scheduled to be executed at %2", Event, JobScheduler
    public static final String MLS_ID_TASK_IS_ALREADY_SCHEDULED = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mls54RCXSTOBRELRBT3FXOVEMUEVU";//Task #%1 '%2' was not scheduled because it is already scheduled or executing, Warning, id, title
    public static final String MLS_ID_TASK_IS_RUNNING_FOR_TO_LONG = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsDSHLC2BIINDDRIDCXUHUK2EW7E";//Task '%1' has not been completed in expected amount of time (%2 s)", Warning, JobScheduler
    public static final String MLS_ID_TASK_HAS_BEEN_ACTUALIZED = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsRQJLSSN57FALZNLF6H4YFLGWFY";//Task %1 had outdated status and was actualized", Warning, JobScheduler
}
