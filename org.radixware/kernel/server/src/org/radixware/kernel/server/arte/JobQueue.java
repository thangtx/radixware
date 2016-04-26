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
package org.radixware.kernel.server.arte;

import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.enums.*;

import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.lang.ReflectiveCallable;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.exceptions.DatabaseError;
import org.radixware.kernel.server.types.Entity;
import org.radixware.kernel.server.types.Pid;
import org.radixware.kernel.server.utils.SrvValAsStr;

public final class JobQueue {

    private final Arte arte;
    /**
     * Запрос для добавления параметра
     */
    private PreparedStatement qryAddParam = null;
    /**
     * Запрос для изменения значения параметра
     */
    private PreparedStatement qryUpdateParam = null;
    private CallableStatement qryScheduleRelative = null;
    private PreparedStatement qryJob = null;
    private PreparedStatement qryGetParams = null;
    private PreparedStatement delJob = null;
    private CallableStatement qrySchedule = null;
    private CallableStatement qryOnStart = null;
    private Long currentJobId = null;
    private Long relatedTaskId = null;
    private boolean bJobDeleted = false;
    private final Utils utils;

    JobQueue(final Arte arte) {
        this.arte = arte;
        utils = new Utils(arte);
    }

    public static class Param {

        protected String name;
        protected EValType type;
        protected Object val;

        public Param(final String name, final EValType type, final Object val) {
            this.name = name;
            this.type = type;
            this.val = val;
        }
        //TODO refactoring mth valToStmt (stmt, strValIdx, clobValIdx, blobValIdx, objValIdx);	use in schedule and awake 
    }

    public static final class JobIdParam extends Param {//для спец. параметра - job.id 

        public static final String NAME = "JOB.ID";

        public JobIdParam() {
            super(NAME, null, null);
        }
    }

    public static final class ArteParam extends Param {//для спец. параметра - arte

        public static final String NAME = "ARTE";

        public ArteParam() {
            super(NAME, null, null);
        }
    }
    static final Id JOB_ENTITY_ID = Id.Factory.loadFrom("tblHRWUWEEMIHNRDJIEACQMTAIZT4"); /*
     * DWF/JobQueue
     */

    static final Id JOB_ENTITY_CLASS_ID = Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"); /*
     * DWF/JobQueue
     */

    static final Id JOB_PROP_ID_ID = Id.Factory.loadFrom("colAAQIIHUMIHNRDJIEACQMTAIZT4"); /*
     * DWF.JobQueue.ID
     */

    static final Id JOB_PROP_DUETIME_ID = Id.Factory.loadFrom("colPSF7WRUMIHNRDJIEACQMTAIZT4"); /*
     * DWF.JobQueue.DueTime
     */

    static final Id JOB_PROP_TITLE_ID = Id.Factory.loadFrom("colGYABEOPPRBD53G55ZT4ZW24ZIQ");
    static final Id JOB_PROP_CLASS_NAME_ID = Id.Factory.loadFrom("colHCKW54UMIHNRDJIEACQMTAIZT4"); /*
     * DWF.JobQueue.ClassName
     */

    static final Id JOB_PROP_TASK_ID_ID = Id.Factory.loadFrom("colBMKNBRS6MFDOHOCOER7UVUAKY4");
    static final Id JOB_PROP_METHOD_NAME_ID = Id.Factory.loadFrom("colUA2AEBUNIHNRDJIEACQMTAIZT4"); /*
     * DWF.JobQueue.MethodName
     */

    static final Id JOB_PROP_PRIORITY_ID = Id.Factory.loadFrom("colOB766EUNIHNRDJIEACQMTAIZT4"); /*
     * DWF.JobQueue.Priority
     */

    static final Id JOB_PROP_BOOSTING_SPEED_ID = Id.Factory.loadFrom("colOJ766EUNIHNRDJIEACQMTAIZT4"); /*
     * DWF.JobQueue.PriorityBoostingSpeed
     */

    static final Id JOB_PROP_CUR_PRIORITY_ID = Id.Factory.loadFrom("colUC47CLMNIHNRDJIEACQMTAIZT4"); /*
     * DWF.JobQueue.CurPriority
     */

    static final Id JOB_PROP_CREATOR_ENTITY_GUID = Id.Factory.loadFrom("colKO3GLUKLZVBJ5LB74GGO6QEBKE");/*
     * DWF.JobQueue.creatorEntityGuid
     */

    static final Id JOB_PROP_CREATOR_PID = Id.Factory.loadFrom("col3QEST2453NB6VO2QYXLW6IJTMM");/*
     * DWF.JobQueue.creatorPid
     */

    static final Id JOB_PROP_EXEC_RQ_ID = Id.Factory.loadFrom("colBOGCPSGVIBAB7JKMSBOY55RGH4");/*
     * Platform.DWF.JobQueue.execRequesterId
     */


    /**
     * Подготовка задания без назначения времени исполнения.
     */
    public static Entity post(final Arte arte, final String title, final Pid creator, final String className, final String methodName, final Param[] params, final Long priority, final Long boosting, final boolean allowRerun) {
        return arte.getJobQueue().post(title, creator, className, methodName, params, priority, boosting, allowRerun);
    }

    /**
     * Подготовка задания без назначения времени исполнения.
     *
     */
    public Entity post(final String title, final Pid creator, final String className, final String methodName, final Param[] params, final Long priority, final Long boosting, final boolean allowRerun) {
        return schedule(title, creator, null, className, methodName, params, priority, boosting, allowRerun);
    }

    /**
     * Планирование готового к исполнению задания.
     */
    public static Entity schedule(final Arte arte, final String title, final Pid creator, final String className, final String methodName, final Param[] params, final Long priority, final Long boosting, final boolean allowRerun) {
        return arte.getJobQueue().schedule(title, creator, className, methodName, params, priority, boosting, allowRerun);
    }

    /**
     * Планирование готового к исполнению задания.
     */
    public Entity schedule(final String title, final Pid creator, final String className, final String methodName, final Param[] params, final Long priority, final Long boosting, final boolean allowRerun) {
        return scheduleRelative(title, creator, 0, className, methodName, params, priority, boosting, allowRerun);
    }

    /**
     * Планирование задания с задержкой.
     *
     */
    public static Entity schedule(final Arte arte, final String title, final Pid creator, final double delay, final String className, final String methodName, final Param[] params, final Long priority, final Long boosting, final boolean allowRerun) {
        return arte.getJobQueue().schedule(title, creator, delay, className, methodName, params, priority, boosting, allowRerun);
    }

    /**
     * Планирование задания с задержкой.
     *
     */
    public Entity schedule(final String title, final Pid creator, final double delay, final String className, final String methodName, final Param[] params, final Long priority, final Long boosting, final boolean allowRerun) {
        return scheduleRelative(title, creator, Math.round(delay * 1000), className, methodName, params, priority, boosting, allowRerun);
    }

    public static void unsheduleJob(final Arte arte, final Long id) {
        arte.getJobQueue().unsheduleJob(id);
    }

    public final void unsheduleJob(final Long id) {
        deleteJob(id, null);
    }

    public static void unsheduleJob(final Entity job) {
        job.getArte().getJobQueue().unsheduleJob((Long) job.getProp(JOB_PROP_ID_ID));
    }

    /**
     * Планирование задания на заданное время.
     *
     */
    public static Entity schedule(final Arte arte, final String title, final Pid creator, final Timestamp timestamp, final String className, final String methodName, final Param[] params, final Long priority, final Long boosting, final boolean allowRerun) {
        return arte.getJobQueue().schedule(title, creator, timestamp, className, methodName, params, priority, boosting, allowRerun);
    }

    /**
     * Планирование задания на заданное время.
     */
    public Entity schedule(final String title, final Pid creator, final Timestamp timestamp, final String className, final String methodName, final Param[] params, final Long priority, final Long boosting, final boolean allowRerun) {
        return schedule(title, creator, timestamp, className, methodName, params, priority, boosting, null, allowRerun);
    }

    /**
     * Планирование задания на заданное время.
     */
    public Entity schedule(final String title, final Pid creator, final Timestamp timestamp, final String className, final String methodName, final Param[] params, final Long priority, final Long boosting, final Long taskId, final boolean allowRerun) {
        return schedule(title, creator, timestamp, className, methodName, params, null, priority, boosting, taskId, allowRerun);
    }

    /**
     * Планирование задания на заданное время.
     */
    public Entity schedule(final String title, final Pid creator, final Timestamp timestamp, final String className, final String methodName, final Param[] params, final String scpName, final Long priority, final Long boosting, final Long taskId, final boolean allowRerun) {
        try {
            if (qrySchedule == null) {
                arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
                try {
                    qrySchedule = arte.getDbConnection().get().prepareCall(
                            "begin ?:= RDX_JS_JOB.schedule(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?); end;");
                } finally {
                    arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
                }
            }

            qrySchedule.registerOutParameter(1, Types.INTEGER);
            qrySchedule.setInt(2, allowRerun ? 1 : 0);
            if (timestamp != null) {
                qrySchedule.setTimestamp(3, timestamp);
            } else {
                qrySchedule.setNull(3, Types.TIMESTAMP);
            }
            qrySchedule.setString(4, className);
            qrySchedule.setString(5, methodName);
            qrySchedule.setLong(6, (priority == null ? 5 : priority));
            qrySchedule.setLong(7, boosting == null ? 0 : boosting);
            if (arte.getExecutorRequesterId() != null) {
                qrySchedule.setString(8, arte.getExecutorRequesterId());
            } else {
                qrySchedule.setNull(8, Types.VARCHAR);
            }

            if (creator != null) {
                qrySchedule.setString(9, creator.getEntityId().toString());
                qrySchedule.setString(10, creator.toString());
            } else {
                qrySchedule.setNull(9, Types.VARCHAR);
                qrySchedule.setNull(10, Types.VARCHAR);
            }
            if (title != null) {
                qrySchedule.setString(11, title);
            } else {
                qrySchedule.setNull(11, Types.VARCHAR);
            }

            if (scpName == null) {
                qrySchedule.setNull(12, Types.VARCHAR);
            } else {
                qrySchedule.setString(12, scpName);
            }

            if (taskId == null) {
                qrySchedule.setNull(13, Types.INTEGER);
            } else {
                qrySchedule.setLong(13, taskId);
            }

            qrySchedule.execute();
            final Long jobId = qrySchedule.getLong(1);

            writeJobParams(jobId, params);

            return arte.getEntityObject(new Pid(arte, JOB_ENTITY_ID, jobId.toString()));
        } catch (SQLException ex) {
            throw createErrorOnShedule(ex);
        }
    }

    public Entity scheduleRelative(final String title, final Pid creator, final long delayMillis, final String className, final String methodName, final Param[] params, final Long priority, final Long boosting, final boolean allowRerun) {
        return scheduleRelative(title, creator, delayMillis, className, methodName, params, null, priority, boosting, allowRerun);
    }

    public Entity scheduleRelative(final String title, final Pid creator, final long delayMillis, final String className, final String methodName, final Param[] params, final String scpName, final Long priority, final Long boosting, final boolean allowRerun) {
        return scheduleRelative(title, creator, delayMillis, className, methodName, params, scpName, priority, boosting, null, allowRerun);
    }

    public Entity scheduleRelative(final String title, final Pid creator, final long delayMillis, final String className, final String methodName, final Param[] params, final String scpName, final Long priority, final Long boosting, final Long taskId, final boolean allowRerun) {
        try {
            if (qryScheduleRelative == null) {
                arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
                try {
                    qryScheduleRelative = arte.getDbConnection().get().prepareCall(
                            "begin ?:= RDX_JS_JOB.scheduleRelative(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?); end;");
                } finally {
                    arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
                }
            }
            qryScheduleRelative.registerOutParameter(1, Types.INTEGER);
            qryScheduleRelative.setInt(2, allowRerun ? 1 : 0);
            qryScheduleRelative.setLong(3, delayMillis);
            qryScheduleRelative.setString(4, className);
            qryScheduleRelative.setString(5, methodName);
            qryScheduleRelative.setLong(6, (priority == null ? 5 : priority));
            qryScheduleRelative.setLong(7, boosting == null ? 0 : boosting);
            if (arte.getExecutorRequesterId() != null) {
                qryScheduleRelative.setString(8, arte.getExecutorRequesterId());
            } else {
                qryScheduleRelative.setNull(8, Types.VARCHAR);
            }

            if (creator != null) {
                qryScheduleRelative.setString(9, creator.getEntityId().toString());
                qryScheduleRelative.setString(10, creator.toString());
            } else {
                qryScheduleRelative.setNull(9, Types.VARCHAR);
                qryScheduleRelative.setNull(10, Types.VARCHAR);
            }
            if (title != null) {
                qryScheduleRelative.setString(11, title);
            } else {
                qryScheduleRelative.setNull(11, Types.VARCHAR);
            }

            if (scpName == null) {
                qryScheduleRelative.setNull(12, Types.VARCHAR);
            } else {
                qryScheduleRelative.setString(12, scpName);
            }

            if (taskId == null) {
                qryScheduleRelative.setNull(13, Types.INTEGER);
            } else {
                qryScheduleRelative.setLong(13, taskId);
            }

            qryScheduleRelative.execute();
            final Long jobId = qryScheduleRelative.getLong(1);
            writeJobParams(jobId, params);

            return arte.getEntityObject(new Pid(arte, JOB_ENTITY_ID, jobId.toString()));

        } catch (SQLException e) {
            throw createErrorOnShedule(e);
        }
    }

    private DatabaseError createErrorOnShedule(final SQLException ex) {
        throw new DatabaseError("Can't schedule job: " + ExceptionTextFormatter.getExceptionMess(ex), ex);
    }

    private void writeJobParams(final long jobId, final Param[] params) {
        if (params == null) {
            return;
        }
        try {
            if (qryAddParam == null) {
                arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
                try {
                    qryAddParam = arte.getDbConnection().get().prepareStatement(
                            "Insert into RDX_JS_JOBPARAM (JOBID, NAME, SEQ, VALTYPE, VAL, CLOBVAL, BLOBVAL) values (?,?,?,?,?,?,?)");
                } finally {
                    arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
                }
            }

            // записать параметры
            arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
            try {
                for (int i = 0; i < params.length; i++) {
                    qryAddParam.setLong(1, jobId);
                    qryAddParam.setString(2, params[i].name);
                    qryAddParam.setLong(3, i);

                    if (params[i].type != null) {
                        qryAddParam.setLong(4, params[i].type.getValue().longValue());
                        if (params[i].type == EValType.CLOB) {
                            qryAddParam.setString(5, null);
                            qryAddParam.setClob(6, (Clob) params[i].val);
                            qryAddParam.setBlob(7, (Blob) null);
                        } else if (params[i].type == EValType.BLOB) {
                            qryAddParam.setString(5, null);
                            qryAddParam.setClob(6, (Clob) null);
                            qryAddParam.setBlob(7, (Blob) params[i].val);
                        } else {
                            qryAddParam.setString(5, SrvValAsStr.toStr(arte, params[i].val, params[i].type));
                            qryAddParam.setClob(6, (Clob) null);
                            qryAddParam.setBlob(7, (Blob) null);
                        }
                    } else if (params[i] instanceof JobIdParam) {
                        qryAddParam.setObject(4, null);
                        qryAddParam.setString(5, Long.toString(jobId));
                        qryAddParam.setClob(6, (Clob) null);
                        qryAddParam.setBlob(7, (Blob) null);
                    } else if (params[i] instanceof ArteParam) {
                        qryAddParam.setObject(4, null);
                        qryAddParam.setString(5, params[i].name);
                        qryAddParam.setClob(6, (Clob) null);
                        qryAddParam.setBlob(7, (Blob) null);
                    } else {
                        throw new IllegalUsageError("Job parameter type is not defined");
                    }
                    qryAddParam.executeQuery();
                }
            } finally {
                arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
            }
        } catch (SQLException e) {
            throw new DatabaseError("Can't schedule job: " + ExceptionTextFormatter.getExceptionMess(e), e);
        }
    }

    public void close() {

        if (qryAddParam != null) {
            try {
                qryAddParam.close();
                qryAddParam = null;
            } catch (SQLException ex) {
                //do nothing
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
        }

        if (qryUpdateParam != null) {
            try {
                qryUpdateParam.close();
            } catch (SQLException ex) {
                //do nothing
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
            qryUpdateParam = null;
        }
        if (qryScheduleRelative != null) {
            try {
                qryScheduleRelative.close();
            } catch (SQLException ex) {
                //do nothing
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
            qryScheduleRelative = null;
        }
        if (qryJob != null) {
            try {
                qryJob.close();
            } catch (SQLException ex) {
                //do nothing
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
            qryJob = null;
        }
        if (qryGetParams != null) {
            try {
                qryGetParams.close();
            } catch (SQLException ex) {
                //do nothing
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
            qryGetParams = null;
        }

        if (delJob != null) {
            try {
                delJob.close();
            } catch (SQLException ex) {
                //do nothing
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
            delJob = null;
        }

        if (qrySchedule != null) {
            try {
                qrySchedule.close();
            } catch (SQLException ex) {
                //do nothing
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
            qrySchedule = null;
        }

        if (qryOnStart != null) {
            try {
                qryOnStart.close();
            } catch (SQLException ex) {
                //do nothing
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
            qryOnStart = null;
        }

        utils.setDbConnection(null);
    }

    /**
     * Сигнал о необходимости немедленно исполнить задание с передачей
     * параметров
     *
     * @param job
     * @param params
     */
    public static void awake(final Entity job, final Param[] params) {
        job.getArte().getJobQueue().awakeImpl(job, params);
    }

    private void awakeImpl(final Entity job, final Param[] params) {
        final Long jobId = (Long) job.getProp(JOB_PROP_ID_ID);

        try {
            if (qryUpdateParam == null) {
                arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
                try {
                    qryUpdateParam = job.getArte().getDbConnection().get().prepareStatement(
                            "Update RDX_JS_JOBPARAM set VAL=?, CLOBVAL=?, BLOBVAL=?  where JOBID=? and NAME=?");
                } finally {
                    arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
                }
            }

            // записать параметры
            arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
            try {
                for (int i = 0; i < params.length; i++) {
                    if (params[i].type != null) {
                        if (params[i].type == EValType.CLOB) {
                            qryUpdateParam.setString(1, null);
                            qryUpdateParam.setClob(2, (Clob) params[i].val);
                            qryUpdateParam.setBlob(3, (Blob) null);
                        } else if (params[i].type == EValType.BLOB) {
                            qryUpdateParam.setString(1, null);
                            qryUpdateParam.setClob(2, (Clob) null);
                            qryUpdateParam.setBlob(3, (Blob) params[i].val);
                        } else {
                            qryUpdateParam.setString(1, SrvValAsStr.toStr(arte, params[i].val, params[i].type));
                            qryUpdateParam.setClob(2, (Clob) null);
                            qryUpdateParam.setBlob(3, (Blob) null);
                        }
                    } else if (params[i] instanceof JobIdParam) {
                        qryUpdateParam.setString(1, String.valueOf(jobId));
                        qryUpdateParam.setClob(2, (Clob) null);
                        qryUpdateParam.setBlob(3, (Blob) null);
                    } else if (params[i] instanceof ArteParam) {
                        qryUpdateParam.setString(1, params[i].name);
                        qryUpdateParam.setClob(2, (Clob) null);
                        qryUpdateParam.setBlob(3, (Blob) null);
                    } else {
                        throw new IllegalUsageError("Job parameter type is not defined");
                    }
                    qryUpdateParam.setLong(4, jobId.longValue());
                    qryUpdateParam.setString(5, params[i].name);
                    qryUpdateParam.executeQuery();
                }
            } finally {
                arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
            }
        } catch (SQLException e) {
            throw new DatabaseError("Can't awake job: " + ExceptionTextFormatter.getExceptionMess(e), e);
        }

        // задать время
        job.setProp(JOB_PROP_DUETIME_ID, new Timestamp(System.currentTimeMillis()));
        job.update();
    }

    public static void awake(final Arte arte, final Long jobId, final Param[] params) {
        arte.getJobQueue().awake(jobId, params);
    }

    public void awake(Long jobId, Param[] params) {
        awake(arte.getEntityObject(new Pid(arte, JOB_ENTITY_ID, JOB_PROP_ID_ID, jobId)), params);
    }

    private void debug(final String mess, final Long id) {
        arte.getTrace().put(EEventSeverity.DEBUG, mess + (id != null ? (", #" + id.toString()) : ""), EEventSource.JOB_QUEUE);
    }

    /**
     * Job execution called by JobExecutor via AAS. Deletes the job in any case.
     * The deletion is commited via arte.commit() due to perfomance reasons.
     * This call should be the only call in ARTE's transcation because it calls
     * arte.commit() and can call arte.rollback().
     *
     * @param id
     * @param traceProfile
     * @return
     */
    @ReflectiveCallable
    public static String execute(final Arte arte, final Long id) {
        return arte.getJobQueue().execute(id);
    }

    /**
     * Job execution called by JobExecutor via AAS. Deletes the job in any case.
     * The deletion is commited via arte.commit() due to perfomance reasons.
     * This call should be the only call in ARTE's transcation because it calls
     * arte.commit() and can call arte.rollback().
     *
     * @param id
     * @return
     */
    public String execute(final Long id) {
        boolean bJobStarted = false;
        bJobDeleted = false;
        String dueTimeBeforeStartDump = null;
        currentJobId = id;
        try {
            debug("Job execution started", id);
            final String execRequesterId;
            final ArrayList<Class> paramClassesVector = new ArrayList<Class>();
            final ArrayList<Object> paramValsVector = new ArrayList<Object>();
            final String jobClassName;
            final String jobMethodName;
            StringBuffer params = null;
            try {
                if (qryJob == null) {
                    arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
                    try {
                        qryJob = arte.getDbConnection().get().prepareStatement("select CLASSNAME, METHODNAME, EXECREQUESTERID, dump(DUETIME) dueTimeDump, TASKID from RDX_JS_JOBQUEUE where ID = ?");
                    } finally {
                        arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
                    }
                }
                final ResultSet rs;
                arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
                try {
                    qryJob.setLong(1, id.longValue());
                    rs = qryJob.executeQuery();
                } finally {
                    arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
                }
                try {
                    if (!rs.next()) {
                        throw new IllegalUsageError("Job #" + String.valueOf(id) + " not found");
                    }
                    jobClassName = rs.getString("CLASSNAME");
                    jobMethodName = rs.getString("METHODNAME");
                    execRequesterId = rs.getString("EXECREQUESTERID");
                    dueTimeBeforeStartDump = rs.getString("dueTimeDump");
                    relatedTaskId = rs.getLong("taskId");
                    if (rs.wasNull()) {
                        relatedTaskId = null;
                    }
                } finally {
                    rs.close();
                }
                // получение списка параметров
                if (qryGetParams == null) {
                    arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
                    try {
                        qryGetParams = arte.getDbConnection().get().prepareStatement(
                                "select NAME, VALTYPE, VAL, CLOBVAL, BLOBVAL from RDX_JS_JOBPARAM where JOBID=? order by SEQ");
                    } finally {
                        arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
                    }
                }

                {
                    final ResultSet paramsResultSet;
                    arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
                    try {
                        qryGetParams.setLong(1, id.longValue());
                        paramsResultSet = qryGetParams.executeQuery();
                    } finally {
                        arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
                    }
                    try {
                        while (paramsResultSet.next()) {
                            final String paramName = paramsResultSet.getString(1);
                            final Long paramValType = (paramsResultSet.getObject(2) != null ? Long.valueOf(paramsResultSet.getLong(2)) : null);

                            final Class paramClass;
                            final Object paramObj;
                            final String paramType;

                            if (paramValType != null) {
                                final EValType paramDbpValType = EValType.getForValue(paramValType);
                                //paramClass = Dbp2JavaTypeMap.getJavaClass(paramDbpValType);
                                if (paramDbpValType == EValType.CLOB) {
                                    paramObj = paramsResultSet.getClob(4);
                                    paramClass = java.sql.Clob.class;
                                } else if (paramDbpValType == EValType.BLOB) {
                                    paramObj = paramsResultSet.getBlob(5);
                                    paramClass = java.sql.Blob.class;
                                } else {
                                    final String paramVal = paramsResultSet.getString(3);
                                    paramObj = SrvValAsStr.fromStr(arte, paramVal, paramDbpValType);
                                    paramClass = paramObj == null ? null : paramObj.getClass();
                                }
                                paramType = paramDbpValType.getName();

                            } else {
                                // Если ValType==null, то передается JobID
                                if (JobIdParam.NAME.equals(paramName)) {
                                    paramClass = Long.class;
                                    paramObj = id;
                                    paramType = "JobId";
                                } else if (ArteParam.NAME.equals(paramName)) {
                                    paramClass = Arte.class;
                                    paramObj = arte;
                                    paramType = "Arte";
                                } else {
                                    throw new IllegalUsageError("Unsupported special parameter: " + paramName);
                                }
                            }

                            paramClassesVector.add(paramClass);
                            paramValsVector.add(paramObj);

                            if (params == null) {
                                params = new StringBuffer();
                            } else {
                                params.append(", ");
                            }
                            params.append(paramType);
                            params.append(' ');
                            params.append(paramName);
                        }
                    } finally {
                        paramsResultSet.close();
                    }
                }
            } catch (SQLException e) {
                throw new DatabaseError("Can't execute job: " + arte.getTrace().exceptionStackToString(e), e);
            }

            final Class[] paramTypes = new Class[paramClassesVector.size()];
            paramClassesVector.toArray(paramTypes);
            final Object[] paramVals = paramValsVector.toArray();

            try {
                arte.onStartRequestExecution(execRequesterId);
                if (qryOnStart == null) {
                    qryOnStart = arte.getDbConnection().get().prepareCall("begin RDX_JS_JOB.onStartJobExecution(?); end;");
                }
                qryOnStart.setLong(1, id);
                qryOnStart.executeQuery();
                //commit is done in RDX_JS_JOB.onStartJobExecution(...)"

                bJobStarted = true;

                arte.getInstance().getJobCheckTimeUpdater().register(arte, id, relatedTaskId);

                debug("Job method invoke started", id);
                arte.invokeByClassName(jobClassName, jobMethodName, paramTypes, paramVals);

                if (!bJobDeleted) {
                    deleteJob(id, dueTimeBeforeStartDump);
                }
                arte.commit(); //see RADIX-5101
                bJobDeleted = true;

                debug("Job execution completed", id);
            } catch (Throwable e) {
                try {
                    arte.rollback();
                } catch (Throwable ex) {
                    //do nothing
                    Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                }
                final String excErr = arte.getTrace().exceptionStackToString(e);
                final String sJobID = Long.toString(id.longValue());
                final ArrStr excWords = new ArrStr(sJobID, jobClassName, jobMethodName, String.valueOf(params), excErr);
                //FIXME use event code!
                arte.getTrace().put(EEventSeverity.ERROR, "Job execution error: " + excWords.toString(), EEventSource.JOB_QUEUE);
            } finally {
                arte.onFinishRequestExecution();
            }
        } finally {
            currentJobId = null;
            relatedTaskId = null;
            arte.getInstance().getJobCheckTimeUpdater().unregister(arte);
            if (bJobStarted && !bJobDeleted) {
                deleteJob(id, dueTimeBeforeStartDump);
                arte.commit();//autonomous transaction is not used due to perfomance reasons
            }
        }
        return null;
    }

    public Long getRelatedTaskId() {
        return relatedTaskId;
    }

    public Long getCurrentJobId() {
        return currentJobId;
    }

    /**
     * Delete current job from job queue without commit.
     * <br/>Use case:<br/> {@code JobQueue.deleteCurrentJob();}<br/>
     * {@code Arte.commit();}<br/>
     * {@code JobQueue.markCurrentJobDeleted();}<br/>
     */
    public void deleteCurrentJob() {
        if (getCurrentJobId() != null) {
            deleteJob(getCurrentJobId(), null);
        }
    }

    /**
     * Notifies job queue that there is no need to delete current job
     * automatically.
     * <br/>Use case:<br/> {@code JobQueue.deleteCurrentJob();}<br/>
     * {@code Arte.commit();}<br/>
     * {@code JobQueue.markCurrentJobDeleted();}<br/>
     */
    public void markCurrentJobDeleted() {
        bJobDeleted = true;
    }

    public void unlockJob(final Long id) {
        utils.unlockJob(id);
    }

    private void deleteJob(final Long id, final String dueTimeBeforeStartDump) {
        try {
            if (delJob == null) {
                arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
                try {
                    delJob = arte.getDbConnection().get().prepareStatement("Delete from RDX_JS_JOBQUEUE where id=? and dump(duetime)=nvl(?, dump(duetime))");
                } finally {
                    arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
                }
            }

            boolean deleted = false;
            arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
            try {
                delJob.setLong(1, id);
                if (dueTimeBeforeStartDump != null) {
                    delJob.setString(2, dueTimeBeforeStartDump);
                } else {
                    delJob.setNull(2, Types.VARCHAR);
                }
                deleted = delJob.executeUpdate() > 0;
            } finally {
                arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
            }
            if (!deleted) {
                if (utils.unlockJob(id)) {
                    arte.getTrace().put(EEventSeverity.DEBUG, "Job #" + id + " was rescheduled", EEventSource.JOB_QUEUE);
                } else {
                    arte.getTrace().put(EEventSeverity.WARNING, "Job #" + id + " was not found for unlocking", EEventSource.JOB_QUEUE);
                }
            } else if (currentJobId != null && currentJobId.longValue() == id) {
                markCurrentJobDeleted();
            }
        } catch (Throwable e) {
            throw new DatabaseError("Can't delete job: " + arte.getTrace().exceptionStackToString(e), e);
        }
    }

    public static class Utils {

        private PreparedStatement qryUnlockJob = null;
        private Connection dbConnection;
        private final Arte arte;

        public Utils(Arte arte) {
            this(arte, null);//connection will be initialized later
        }

        public Utils(Connection connection) {
            this(null, connection);
        }

        private Utils(Arte arte, Connection dbConnection) {
            this.arte = arte;
            this.dbConnection = dbConnection;
        }

        public void setDbConnection(Connection connection) {
            if (qryUnlockJob != null) {
                try {
                    qryUnlockJob.close();
                } catch (SQLException ex) {
                    //ignore
                    Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                } finally {
                    qryUnlockJob = null;
                }
            }
            dbConnection = connection;
        }

        public boolean unlockJob(final Long id) {
            if (dbConnection == null) {
                if (arte != null && arte.getDbConnection().get() != null) {
                    dbConnection = arte.getDbConnection().get();
                } else {
                    throw new IllegalStateException("DB connection is null");
                }
            }
            try {
                if (qryUnlockJob == null) {
                    if (arte != null) {
                        arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
                    }
                    try {
                        qryUnlockJob = dbConnection.prepareStatement("update RDX_JS_JOBQUEUE set processorTitle=null, unlockCount=unlockCount+1, selfCheckTime=null where id=? and processorTitle is not null");
                    } finally {
                        if (arte != null) {
                            arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
                        }
                    }
                }
                if (arte != null) {
                    arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
                }
                try {
                    qryUnlockJob.setLong(1, id);
                    final int updatedCount = qryUnlockJob.executeUpdate();
                    return updatedCount == 1;
                } finally {
                    if (arte != null) {
                        arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
                    }
                }
            } catch (SQLException e) {
                throw new DatabaseError("Unable to unlock job: " + e.getMessage(), e);
            }
        }
    }
}
