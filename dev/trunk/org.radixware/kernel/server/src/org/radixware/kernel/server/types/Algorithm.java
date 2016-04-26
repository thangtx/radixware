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

package org.radixware.kernel.server.types;

import java.util.Set;
import java.sql.Clob;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashSet;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.enums.EDwfProcessState;
import org.radixware.kernel.common.enums.EEntityInitializationPhase;
import org.radixware.kernel.common.enums.EEntityLockMode;
import org.radixware.kernel.common.enums.EEventContextType;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.AppException;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.lang.ReflectiveCallable;
import org.radixware.kernel.common.types.IKernelEnum;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.algo.AlgorithmExecutor;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.arte.AdsClassLoader;
import org.radixware.kernel.server.arte.JobQueue;
import org.radixware.kernel.server.exceptions.AlgorithmDefinitionError;

public abstract class Algorithm {

    static final public Id ALGORITHM_ID = Id.Factory.loadFrom("pdcAlgorithm_________________");
    static final public Id DWF_PROCESS_ENTITY_ID = Id.Factory.loadFrom("tblRDXQVFY6PLNRDANMABIFNQAABA");
    static final public Id DWF_PROCESS_CLASS_ID = Id.Factory.loadFrom("aecRDXQVFY6PLNRDANMABIFNQAABA");
    static final public Id DWF_PROCESS_PROP_ID_ID = Id.Factory.loadFrom("colYBCGVMA6PLNRDANMABIFNQAABA");
    static final public Id DWF_PROCESS_PROP_TITLE_ID = Id.Factory.loadFrom("colSYRTXEA7PLNRDANMABIFNQAABA");
    static final public Id DWF_PROCESS_PROP_CREATOR_ID = Id.Factory.loadFrom("colKKF6BJQ7PLNRDANMABIFNQAABA");
    static final public Id DWF_PROCESS_PROP_OWNER_ID = Id.Factory.loadFrom("colXLGX3QEDXLNRDF5JABIFNQAABA");
    static final public Id DWF_PROCESS_PROP_LANGUAGE_ID = Id.Factory.loadFrom("colFPULBNGVQLOBDBFRAALOYD3UKE");
    static final public Id DWF_PROCESS_PROP_ALGO_ID = Id.Factory.loadFrom("colHDZJU3I7PLNRDANMABIFNQAABA");
    static final public Id DWF_PROCESS_PROP_STATE_ID = Id.Factory.loadFrom("colQTG3VSI7PLNRDANMABIFNQAABA");
    static final public Id DWF_PROCESS_PROP_START_TIME_ID = Id.Factory.loadFrom("colPIZGX2I7PLNRDANMABIFNQAABA");
    static final public Id DWF_PROCESS_PROP_FINISH_TIME_ID = Id.Factory.loadFrom("colDKM24AZAPLNRDANMABIFNQAABA");
    static final public Id DWF_PROCESS_PROP_EXCEPTION_ID = Id.Factory.loadFrom("colICQDIGZAPLNRDANMABIFNQAABA");
    static final public Id DWF_PROCESS_PROP_CONTEXT_ID = Id.Factory.loadFrom("colKD4KAOZAPLNRDANMABIFNQAABA");
    static final public Id DWF_PROCESS_PROP_TRACE_PROF_ID = Id.Factory.loadFrom("colYRAZOVBAPLNRDANMABIFNQAABA");
    static final public Id DWF_PROCESS_PROP_SIMULATED_TIME_ID = Id.Factory.loadFrom("colUTVP7ABAPLNRDANMABIFNQAABA");
    static final public Id DWF_PROCESS_PROP_TYPE_ID = Id.Factory.loadFrom("colMCUP6FRT5FBYXDBZUYABU5RFII");
    static final public Id DWF_PROCESS_TYPE_ENTITY_ID = Id.Factory.loadFrom("tblXI6CAOIGDBAGJEE6JQEGJME43Q");
    static final public Id DWF_PROCESS_TYPE_CLASS_ID = Id.Factory.loadFrom("aecXI6CAOIGDBAGJEE6JQEGJME43Q");
    static final public Id DWF_PROCESS_TYPE_PROP_ID_ID = Id.Factory.loadFrom("colAEAAPJXBX5BIXHBNJOYP3FBIR4");
    static final public Id DWF_PROCESS_TYPE_PROP_ALGO_ID = Id.Factory.loadFrom("colE62KHOUF45E6JDWSYCA7TICXAI");
    static final public Id DWF_PROCESS_TYPE_PROP_ADMIN_ROLE_IDS_ID = Id.Factory.loadFrom("col6KIGNQEL7NEWFDQMOEIO4POSTQ");
    static final public Id DWF_PROCESS_TYPE_PROP_CLERK_ROLE_IDS_ID = Id.Factory.loadFrom("col6SKMJNSZZNFVDNGCP26PG6QPEM");
    static final public Id DWF_PROCESS_TYPE_PROP_NOTES_ID = Id.Factory.loadFrom("colXPT3ITROQNCC5OQ67AAKUEY2IQ");
    static final public Id DWF_PROCESS_TYPE_PROP_STORE_DAYS_ID = Id.Factory.loadFrom("colNWJ6DLDISBBWZKPVZUMMGLM5LI");
    static final public Id DWF_PROCESS_TYPE_PROP_TITLE_ID = Id.Factory.loadFrom("colNWJ6DLDISBBWZKPVZUMMGLM5LI");
    static final public Id DWF_PROCESS_TYPE_PROP_TRACE_PROF_ID = Id.Factory.loadFrom("colCGY7I2E76ZF7LNL4OVRXERFG7I");
    protected AlgorithmExecutor executor = null;

    protected Algorithm() {
    }
    private Arte arte = null;

    public final Arte getArte() {
        if (arte == null) {
            arte = ((AdsClassLoader) getClass().getClassLoader()).getArte();
        }
        return arte;
    }

    public void setExecutor(AlgorithmExecutor executor) {
        this.executor = executor;
    }

    public AlgorithmExecutor getExecutor() {
        return executor;
    }

    public AlgorithmExecutor.Thread getCurrentThread() {
        return executor.getCurrentThread();
    }

    public final Entity scheduleTimeoutJob(double timeout, Id varForWaitId) throws AppException {
        return scheduleTimeoutJob(timeout, varForWaitId, true);
    }

    public final Entity scheduleTimeoutJob(double timeout, Id varForWaitId, boolean clear) throws AppException {
        final Entity process = executor.getCurrentAlgo().getProcess();
        final Object processId = process.getProp(DWF_PROCESS_PROP_ID_ID);
        JobQueue.Param[] params = {
            new JobQueue.ArteParam(),
            new JobQueue.Param("processId", EValType.INT, processId),
            new JobQueue.JobIdParam()
        };
        final Object processTitle = process.getProp(DWF_PROCESS_PROP_TITLE_ID);
        final Entity job = executor.getArte().getJobQueue().schedule("Timeout job for process #" + processId + "-" + processTitle, process.getPid(), timeout, executor.getArte().getDefManager().getAdsExecClassNameById(ALGORITHM_ID), "onTimeout", params, null, null, false);
        final Long waitId = (Long) job.getPid().getPkVals().get(0);
        executor.getCurrentThread().registerWait(waitId, null, executor.getCurrentTime(Math.round(timeout)), clear);
        if (varForWaitId != null) {
            executor.setData(varForWaitId, waitId);
        }
        return job;
    }

    public final Entity scheduleEventJob(String clientData, Id varForWaitId) throws AppException {
        final Entity process = executor.getCurrentAlgo().getProcess();
        final Object processId = process.getProp(DWF_PROCESS_PROP_ID_ID);
        JobQueue.Param[] params = {
            new JobQueue.ArteParam(),
            new JobQueue.Param("processId", EValType.INT, processId),
            new JobQueue.JobIdParam(),
            new JobQueue.Param("clientData", EValType.STR, clientData)
        };
        final Object processTitle = process.getProp(DWF_PROCESS_PROP_TITLE_ID);
        final Entity job = executor.getArte().getJobQueue().post("Event job for process #" + processId + "-" + processTitle, process.getPid(), executor.getArte().getDefManager().getAdsExecClassNameById(ALGORITHM_ID), "onEvent", params, null, null, false);
        final Long waitId = (Long) job.getPid().getPkVals().get(0);
        executor.getCurrentThread().registerWait(waitId, null, null, true);
        if (varForWaitId != null) {
            executor.setData(varForWaitId, waitId);
        }
        return job;
    }

    public final Long scheduleEvent(Id varForWaitId) {
        final Long waitId = Long.valueOf(--executor.lastWaitId);
        executor.getCurrentThread().registerWait(waitId, null, null, true);
        if (varForWaitId != null) {
            executor.setData(varForWaitId, waitId);
        }
        return waitId;
    }

    public final void scheduleStrob(Id strobId) {
        executor.getCurrentThread().registerWait(null, strobId, null, true);
    }

    // timeout event
    @ReflectiveCallable
    public static void onTimeout(Arte arte, Long processId, Long waitId) {
        Pid pid = new Pid(arte, DWF_PROCESS_ENTITY_ID, DWF_PROCESS_PROP_ID_ID, processId);
        if (!pid.isExistsInDb()) {
            return;
        }
        Entity process = arte.getEntityObject(pid);
        resume(arte, process, waitId, null, null);
    }

    @ReflectiveCallable
    public static void onEvent(Arte arte, Long processId, Long waitId, String clientData) {
        //TODO может этот метод не нужен, ява и рефлекшн сами сообразят что вызвать???
        onEvent(arte, processId, waitId, (Object) clientData);
    }

    @ReflectiveCallable
    public static void onEvent(Arte arte, Long processId, Long waitId, Object clientData) {
        Pid pid = new Pid(arte, DWF_PROCESS_ENTITY_ID, DWF_PROCESS_PROP_ID_ID, processId);
        if (!pid.isExistsInDb()) {
            return;
        }
        Entity process = arte.getEntityObject(pid);
        resume(arte, process, waitId, null, clientData);
    }

    @ReflectiveCallable
    public static void onStrob(Arte arte, Long processId, Id strobId) {
        Pid pid = new Pid(arte, DWF_PROCESS_ENTITY_ID, DWF_PROCESS_PROP_ID_ID, processId);
        if (!pid.isExistsInDb()) {
            return;
        }
        Entity process = arte.getEntityObject(pid);
        resume(arte, process, null, strobId, null);
    }

    public final void strob(Id strobId) {
        executor.resume(null, strobId, null);
    }

    public final void resume(Long waitId, Object clientData) {
        executor.resume(waitId, null,
                (clientData instanceof IKernelEnum) ? ((IKernelEnum) clientData).getValue() : clientData);
    }

    private static Id getWorkingClassId(Arte arte, Id algoId) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Algorithm algo = (Algorithm) arte.newObject(algoId);
        if (algo != null) {
            Class<?> clazz = algo.getClass();
            java.lang.reflect.Method m = clazz.getMethod("getWorkingClassId", new Class<?>[]{});
            return (Id) m.invoke(null, new Object[]{});
        }
        return algoId;
    }

    protected static void resume(Arte arte, Entity process, Long waitId, Id strobId, Object clientData) {
        process.lock(EEntityLockMode.TRANSACTION, null);
        process.reread(EEntityLockMode.NONE, null);
        Long processId = (Long) process.getProp(DWF_PROCESS_PROP_ID_ID);
        EDwfProcessState state = (EDwfProcessState) process.getProp(DWF_PROCESS_PROP_STATE_ID);
        if (!state.equals(EDwfProcessState.ACTIVE)) {
            return; // процесс уже закончен или прерван
        }
        AlgorithmExecutor executor = new AlgorithmExecutor(arte, process, (Timestamp) process.getProp(DWF_PROCESS_PROP_SIMULATED_TIME_ID));
        String spid = arte.setSavepoint(); // FIXME не используется !
        Object traceTargetHandler = arte.getTrace().addTargetLog((String) process.getProp(DWF_PROCESS_PROP_TRACE_PROF_ID));
        arte.getTrace().enterContext(EEventContextType.WF_PROCESS, processId);
        arte.setClientLanguage(EIsoLanguage.getForValue((String) process.getProp(DWF_PROCESS_PROP_LANGUAGE_ID)));
        executor.trace(EEventSeverity.DEBUG, "WF process #" + processId + " resumed at " + (strobId != null ? "strobId " + strobId : "waitId " + waitId));
        try {
            // register data before continue execution
            final Id algoId = Id.Factory.loadFrom((String) process.getProp(DWF_PROCESS_PROP_ALGO_ID));
            executor.registerAlgo("", algoId, getWorkingClassId(arte, algoId));

            // merge context with registered
            executor.setContext((Clob) process.getProp(DWF_PROCESS_PROP_CONTEXT_ID));

            Algorithm algo = executor.getAlgo(algoId);
            executor.check();
            while (true) {
                if (!executor.resume(waitId, strobId, clientData)) {
                    break; // некого будить
                }
                algo.beforeResume();
                if (executor.execute()) {
                    algo.beforeFinish();
                    process.setProp(DWF_PROCESS_PROP_STATE_ID, EDwfProcessState.FINISHED);
                    process.setProp(DWF_PROCESS_PROP_FINISH_TIME_ID, executor.getCurrentTime());
                    process.update();
                    return;
                }
                algo.beforeSuspend();
                if (!executor.isSimulated()) {
                    break;
                }
            }
        } catch (Throwable e) {
            try {
                arte.rollbackToSavepoint(spid);
            } catch (Throwable ex) {
                // do nothing
                Logger.getLogger(Algorithm.class.getName()).log(Level.FINE, ex.getMessage(), ex);
            }
            final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
            executor.trace(EEventSeverity.ERROR, "WF process #" + processId + " execution error: " + e.getClass().getName() + (e.getMessage() != null ? ": " + e.getMessage() : "") + "\n" + exStack);
            process.setProp(DWF_PROCESS_PROP_STATE_ID, EDwfProcessState.EXCEPTED);
            process.setProp(DWF_PROCESS_PROP_FINISH_TIME_ID, executor.getCurrentTime());
            process.setProp(DWF_PROCESS_PROP_EXCEPTION_ID, e.toString());
            process.update();
            return;
        } finally {
            arte.getTrace().delTarget(traceTargetHandler);
            arte.getTrace().leaveContext(EEventContextType.WF_PROCESS, processId);
        }

        process.setProp(DWF_PROCESS_PROP_CONTEXT_ID, executor.getContext());
        process.update();
    }

    protected static boolean execute(AlgorithmExecutor executor) throws AppException {
        final Arte arte = executor.getArte();
        String spid = arte.setSavepoint();
        Object traceTargetHandler = null;
        if (executor.process != null) {
            traceTargetHandler = arte.getTrace().addTargetLog((String) executor.process.getProp(DWF_PROCESS_PROP_TRACE_PROF_ID));
            arte.getTrace().enterContext(EEventContextType.WF_PROCESS, (Long) executor.process.getProp(DWF_PROCESS_PROP_ID_ID));
            arte.setClientLanguage(EIsoLanguage.getForValue((String) executor.process.getProp(DWF_PROCESS_PROP_LANGUAGE_ID)));
        }
        Algorithm algo = executor.getAlgo(executor.getMainAlgoId());
        try {
            algo.beforeStart();
            if (executor.execute()) {
                algo.beforeFinish();
                if (executor.process != null) {
                    executor.process.setProp(DWF_PROCESS_PROP_STATE_ID, EDwfProcessState.FINISHED);
                    executor.process.setProp(DWF_PROCESS_PROP_FINISH_TIME_ID, executor.getCurrentTime());
                    executor.process.update();
                }
                return true;  // алгоритм исполнен синхронно
            }
            algo.beforeSuspend();
        } catch (Throwable e) {
            try {
                arte.rollbackToSavepoint(spid);
            } catch (Throwable ex) {
                // do nothing    
                Logger.getLogger(Algorithm.class.getName()).log(Level.FINE, ex.getMessage(), ex);
            }
            final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
            executor.trace(EEventSeverity.ERROR, "Process execution error: " + e.getClass().getName() + (e.getMessage() != null ? ": " + e.getMessage() : "") + "\n" + exStack);
            if (executor.process != null && executor.process.getPid().isExistsInDb()) {
                executor.process.setProp(DWF_PROCESS_PROP_STATE_ID, EDwfProcessState.EXCEPTED);
                executor.process.setProp(DWF_PROCESS_PROP_FINISH_TIME_ID, executor.getCurrentTime());
                executor.process.setProp(DWF_PROCESS_PROP_EXCEPTION_ID, e.toString());
                executor.process.update();
                return true;
            } else {
                throw new AppException("Algorithm exception - " + e.getMessage(), e);
            }
        } finally {
            if (traceTargetHandler != null) {
                arte.getTrace().delTarget(traceTargetHandler);
                arte.getTrace().leaveContext(EEventContextType.WF_PROCESS, (Long) executor.process.getProp(DWF_PROCESS_PROP_ID_ID));
            }
        }
        executor.process.setProp(DWF_PROCESS_PROP_STATE_ID, EDwfProcessState.ACTIVE);
        executor.process.setProp(DWF_PROCESS_PROP_CONTEXT_ID, executor.getContext());
        executor.process.update();
        return false;
    }

    public final Entity getProcess() /*throws AppException*/ { //доступ к предопределенной переменной process
        if (executor == null) {
            return null;
        }
        if (executor.process == null) {
            executor.process = createProcess();
        }
        return executor.process;
    }

    private Entity createProcess(/*AlgorithmExecutor executor, Id processClassId*/) /*throws AppException*/ {
        final Arte arte = executor.getArte();
        final Entity type = executor.getProcessType();
        if (type == null || !type.getPid().isExistsInDb()) {
            throw new IllegalUsageError("Workflow process type is not defined");
        }

        final Id processClassId = getProcessClassId();
        final Entity process = (Entity) arte.newObject(processClassId == null ? DWF_PROCESS_CLASS_ID : processClassId);
        process.init(null, null, EEntityInitializationPhase.PROGRAMMED_CREATION);

        //process.setProp(DWF_PROCESS_PROP_TITLE_ID, "Automatically generated process");
        process.setProp(DWF_PROCESS_PROP_CREATOR_ID, arte.getUserName());
        process.setProp(DWF_PROCESS_PROP_OWNER_ID, arte.getUserName());
        process.setProp(DWF_PROCESS_PROP_LANGUAGE_ID, arte.getClientLanguage().getValue());
        process.setProp(DWF_PROCESS_PROP_ALGO_ID, String.valueOf(executor.getMainAlgoId()));
        process.setProp(DWF_PROCESS_PROP_STATE_ID, EDwfProcessState.NEW);
        process.setProp(DWF_PROCESS_PROP_START_TIME_ID, executor.getCurrentTime());
        process.setProp(DWF_PROCESS_PROP_FINISH_TIME_ID, null);
        process.setProp(DWF_PROCESS_PROP_EXCEPTION_ID, null);
        process.setProp(DWF_PROCESS_PROP_CONTEXT_ID, null);
        process.setProp(DWF_PROCESS_PROP_TYPE_ID, type.getProp(DWF_PROCESS_TYPE_PROP_ID_ID));
        process.setProp(DWF_PROCESS_PROP_TRACE_PROF_ID, type.getProp(DWF_PROCESS_TYPE_PROP_TRACE_PROF_ID));
        process.setProp(DWF_PROCESS_PROP_SIMULATED_TIME_ID, null);

        process.create(null);
        return process;
    }

    public void beforeStart() throws AppException {
    }

    public void beforeFinish() throws AppException {
    }

    public void beforeSuspend() throws AppException {
    }

    public void beforeResume() throws AppException {
    }

// Время симулятора
    public static void setSimulatedTime(Arte arte, Entity process, Timestamp time) {
        process.setProp(DWF_PROCESS_PROP_SIMULATED_TIME_ID, time);
        process.update();
        resume(arte, process, null, null, null);
    }

// Вспомогательные методы для алгоритмов
    public final Object getData(Id id) {
        return executor.getData(id);
    }

    public final void setData(Id id, Object val) {
        executor.setData(id, val);
    }

    public final Object getProperty(String name) {
        return executor.getProperty(name);
    }

    public final void setProperty(String name, Object val) {
        executor.setProperty(name, val);
    }

    protected final Throwable getexception() { //доступ к предопределенной переменной exception
        return executor.getException();
    }

    protected final Timestamp gettime() { //доступ к предопределенной переменной time
        return executor.getCurrentTime();
    }

    protected final void trace(EEventSeverity severity, String mess) { //трасса алгоритма
        getArte().getTrace().put(severity, mess, EEventSource.WORKFLOW);
    }

    public Id getProcessClassId() {
        return DWF_PROCESS_CLASS_ID;
    }

// Aбстрактные методы, реализуются в конкретных алгоритмах
    public abstract Set<Id> getBlockIds();

    public abstract void registerInExecutor(AlgorithmExecutor exec, String path);

    public abstract int getBlockType(Id block);

    public abstract Id getNextBlock(Id block, int output);

    public abstract int getNextBlockInput(Id block, int output);

    public abstract Id getBlockScope(Id block);

    public abstract Id getFirstScopeBlock(Id scope);

    public abstract int getFirstScopeBlockInput(Id scope);

    public abstract Id[] getForkNextBlocks(Id block);

    public abstract int[] getForkNextBlocksInput(Id block);

    public abstract Id getMergeBlock(Id block);

    public abstract int getMergeBlockN(Id block);

    public abstract int getMergeBlockM(Id block);

    public abstract Id getIncludeBlockAlgorithm(Id block);

    public abstract void writeIncludeBlockParameters(Id block);

    public abstract void readIncludeBlockParameters(Id block);

    public abstract Id getBlockPropId(Id block, String propName);
// Исполнить указанный блок. return номер выхода, 0 - переход к ожиданию

    public abstract int executeBlock(Id block) throws Throwable;
// Поиск обработчика исключения указанного scope. return номер следующего блока

    public abstract Id findCatch(Id scope, Throwable e) throws Throwable;
// Возвращает номер выхода

    public abstract int getReturn(Id block);
// Возвращает название объекта по Id
    //public abstract String 	getTitleById(Id id);

// Интерфейс для блоков Trace
    protected static interface TraceProducer {

        public String getMessage() throws Throwable;
    }
}

// Пример конкретного алгоритма:
// Auto-generated class 
//public 
final class TaacXXX extends Algorithm {
// Приватные методы

    private TaacXXX() {
        super();
    }

    public static Id getWorkingClassId() {
        return Id.Factory.loadFrom("aacXXX");
        // если определен replacement TaacXXX2:
        //	return TaacXXX2.getWorkingClass();   	
    }

//	Публичные статические методы	
    //Синхронное исполнение
    public static void execute(Object clientData,
            Long par1, String par2, String[] out1) throws Throwable {
        AlgorithmExecutor executor = new AlgorithmExecutor(null, null, null, true, Id.Factory.loadFrom("aacXXX"), getWorkingClassId(), clientData);
        executor.setData(Id.Factory.loadFrom("parGUID1"), par1);
        executor.setData(Id.Factory.loadFrom("parGUID2"), par2);
        executor.setData(Id.Factory.loadFrom("parGUID3"), out1[0]);
        executor.execute();
        out1[0] = (String) executor.getData(Id.Factory.loadFrom("parGUID3"));
    }

    //Запуск асинхронного исполнения. 
    //return true - алгоритм исполнен синхронно, false - перешел в ожидание
    public static Entity start(Entity process, Object clientData,
            Long par1, String par2, String out1) {
        AlgorithmExecutor executor = new AlgorithmExecutor(null, process, null, false, Id.Factory.loadFrom("aacXXX"), getWorkingClassId(), clientData);
        executor.setData(Id.Factory.loadFrom("parGUID1"), par1);
        executor.setData(Id.Factory.loadFrom("parGUID2"), par2);
        executor.setData(Id.Factory.loadFrom("parGUID3"), out1);
        try {
            execute(executor);
        } catch (Throwable ex) {
            // do nothing
            // сюда прийти мы не должны
            Logger.getLogger(Algorithm.class.getName()).log(Level.FINE, ex.getMessage(), ex);
        }
        return executor.process;
    }

    //Запуск синхронного или асинхронного исполнения. Процесс создастся при необходимости
    //return null - алгоритм исполнен синхронно, not null - перешел в ожидание
/*
     public static Entity start(Entity type, Object clientData,
     Long par1, String par2, String out1) throws Throwable {		
     AlgorithmExecutor executor = new AlgorithmExecutor(null, null, type, false, Id.Factory.loadFrom("aacXXX"), getWorkingClassId(), clientData);
     executor.setData(Id.Factory.loadFrom("parGUID1"), par1);
     executor.setData(Id.Factory.loadFrom("parGUID2"), par2);
     executor.setData(Id.Factory.loadFrom("parGUID3"), out1);
     executor.execute();
     return executor.process;
     }
     */
//Доступ к параметрам и переменным для програмных блоков	
    public Long getaprXXX() {
        return (Long) getData(Id.Factory.loadFrom("aprXXX"));
    }

    public void setaprXXX(Long x) {
        setData(Id.Factory.loadFrom("aprXXX"), x);
    }
    //...

    //Регистрация параметров и переменных
    @Override
    public void registerInExecutor(AlgorithmExecutor exec, String path) {
        //Регистрация параметров
/*
         exec.registerData(path, "param1", null); 
         exec.registerData(path, "param2", null);
         exec.registerData(path, "param3", null);
         //Регистрация и присвоение начальных значений переменных всех scope
         exec.registerData(path, "avrXXX", null);
         exec.registerData(path, "avrYYY", new Long(1));
         //Регистрация и присвоение начальных значений свойств всех блоков 
         exec.registerData(path, "abpXXXT", new Double(22.2));
         exec.registerData(path, "abpXXXTO", new Double(12));
         exec.registerData(path, "abpXXXW1", null);
         exec.registerData(path, "abpXXXW2", null);
         exec.registerData(path, "abpXXXO",  null);
         */
        //Регистрация всех вложенных алгоритмов
        exec.registerAlgo(path + "-" + ndeXXX2, Id.Factory.loadFrom("aacXXX"), TaacXXX.getWorkingClassId());
//		exec.registerAlgoClass(TaacXXX.class.getName(), TaacXXX.getWorkingClassName());
//		((Algorithm) Arte.newObject(TaacXXX.getWorkingClassName())).registerInExecutor(exec, path+"_88");
        //...
    }
    private static final Id ndeXXX1 = Id.Factory.loadFrom("ndeXXX1");
    private static final Id ndeXXX2 = Id.Factory.loadFrom("ndeXXX2");
    private static final Id edgXXX1 = Id.Factory.loadFrom("edgXXX1");

    @Override
    public Set<Id> getBlockIds() {
        return new HashSet<Id>(Arrays.asList(new Id[]{ndeXXX1, ndeXXX2, edgXXX1}));
    }

    @Override
    public int getBlockType(Id block) {
        if (block.equals(ndeXXX1)) {
            return AlgorithmExecutor.PROGRAM_BLOCK_TYPE;
        }
        if (block.equals(ndeXXX2)) {
            return AlgorithmExecutor.APP_BLOCK_TYPE;
        }
        if (block.equals(edgXXX1)) {
            return AlgorithmExecutor.TRACE_BLOCK_TYPE;
        }
        throw new AlgorithmDefinitionError("Invalid algorithm, unknown block #" + block);
    }

    @Override
    public Id getBlockScope(Id block) {
        if (block.equals(ndeXXX1)) {
            return ndeXXX2;
        }
        if (block.equals(ndeXXX2)) {
            return null;
        }
        throw new AlgorithmDefinitionError("Invalid algorithm, unknown block #" + block);
    }

    @Override
    public Id getFirstScopeBlock(Id scope) {
        if (scope.equals(ndeXXX1)) {
            return ndeXXX2;
        }
        if (scope.equals(ndeXXX2)) {
            return null;
        }
        throw new AlgorithmDefinitionError("Invalid algorithm, unknown scope #" + scope);
    }

    @Override
    public int getFirstScopeBlockInput(Id scope) {
        if (scope.equals(ndeXXX1)) {
            return 0;
        }
        if (scope.equals(ndeXXX2)) {
            return 1;
        }
        throw new AlgorithmDefinitionError("Invalid algorithm, unknown scope #" + scope);
    }

    @Override
    public Id[] getForkNextBlocks(Id block) {
        if (block.equals(ndeXXX1)) {
            return new Id[]{ndeXXX1, ndeXXX2};
        }
        throw new AlgorithmDefinitionError("Invalid algorithm, unknown block #" + block);
    }

    @Override
    public int[] getForkNextBlocksInput(Id block) {
        if (block.equals(ndeXXX1)) {
            return new int[]{0, 0};
        }
        throw new AlgorithmDefinitionError("Invalid algorithm, unknown block #" + block);
    }

    @Override
    public int getMergeBlockM(Id block) {
        if (block.equals(ndeXXX1)) {
            return 3;
        }
        throw new AlgorithmDefinitionError("Invalid algorithm, unknown merge block #" + block);
    }

    @Override
    public int getMergeBlockN(Id block) {
        if (block.equals(ndeXXX1)) {
            return 4;
        }
        throw new AlgorithmDefinitionError("Invalid algorithm, unknown merge block #" + block);
    }

    @Override
    public Id getIncludeBlockAlgorithm(Id block) {
        if (block.equals(ndeXXX2)) {
            return Id.Factory.loadFrom("aacYYYY");
        }
        throw new AlgorithmDefinitionError("Invalid algorithm, unknown include block #" + block);
    }

    @Override
    public Id getBlockPropId(Id block, String propName) {
        if (block.equals(ndeXXX1)) {
            if (propName.equals("Timeout")) {
                return Id.Factory.loadFrom("abpXXXT");
            }
            throw new AlgorithmDefinitionError("Invalid algorithm, unknown property #" + propName + " of block #" + block);
        }
        if (block.equals(ndeXXX2)) {
            if (propName.equals("TO")) {
                return Id.Factory.loadFrom("abpXXXTO");
            } else if (propName.equals("W1")) {
                return Id.Factory.loadFrom("abpXXXW1");
            } else if (propName.equals("W2")) {
                return Id.Factory.loadFrom("abpXXXW2");
            } else if (propName.equals("O")) {
                return Id.Factory.loadFrom("abpXXXO");
            } else {
                throw new AlgorithmDefinitionError("Invalid algorithm, unknown property " + propName + " of block #" + block);
            }
        }
        throw new AlgorithmDefinitionError("Invalid algorithm, unknown block #" + block);
    }

    //связи между блоками
    @Override
    public Id getNextBlock(Id block, int output) {
        if (block.equals(ndeXXX1)) {
            switch (output) {
                case 0:
                    return ndeXXX2;	//next block в пределах scope
                case 1:
                    return edgXXX1;	//вход в scope
                case 2:
                    return null; 	//выход из scope
                default:
                    throw new AlgorithmDefinitionError("Invalid algorithm, unknown output of block #" + block);
            }
        }
        if (block.equals(ndeXXX2)) {
            switch (output) {
                case 0:
                    return ndeXXX1;	//next block в пределах scope
                case 1:
                    return null;	//выход из scope
                default:
                    throw new AlgorithmDefinitionError("Invalid algorithm, unknown output of block #" + block);
            }
        }
        throw new AlgorithmDefinitionError("Invalid algorithm, unknown block #" + block);
    }

    @Override
    public int getNextBlockInput(Id block, int output) {
        if (block.equals(ndeXXX1)) {
            switch (output) {
                case 1:
                    return 2;	//next block в пределах scope
                case 2:
                    return 1;	//вход в scope
                case 3:
                    return -1; 	//выход из scope 
                default:
                    throw new AlgorithmDefinitionError("Invalid algorithm, unknown output of block #" + block);
            }
        }
        if (block.equals(ndeXXX1)) {
            switch (output) {
                case 1:
                    return 2;	//next block в пределах scope
                case 2:
                    return -1;	//выход из scope 
                default:
                    throw new AlgorithmDefinitionError("Invalid algorithm, unknown output of block #" + block);
            }
        }
        throw new AlgorithmDefinitionError("Invalid algorithm, unknown block #" + block);
    }

    @Override
    public int getReturn(Id block) {
        if (block.equals(ndeXXX1)) {
            return 2;
        }
        throw new AlgorithmDefinitionError("Invalid algorithm, unknown return block #" + block);
    }

    @Override
    public Id getMergeBlock(Id block) {
        if (block.equals(ndeXXX1)) {
            return ndeXXX1;
        }
        throw new AlgorithmDefinitionError("Invalid algorithm, unknown merge block #" + block);
    }

    @Override
    public void writeIncludeBlockParameters(Id block) {
        if (block.equals(ndeXXX1)) {
//			executor.setData("_"+block+"param1", executor.getData("aprXXX"));
        }
        throw new AlgorithmDefinitionError("Invalid algorithm, unknown include block #" + block);
    }

    @Override
    public void readIncludeBlockParameters(Id block) {
        if (block.equals(ndeXXX1)) {
//			executor.setData("aprXXX", executor.getData("_"+block+"param1"));
        }
        throw new AlgorithmDefinitionError("Invalid algorithm, unknown include block #" + block);
    }

    @Override
    public int executeBlock(Id block) throws Throwable {
        if (block.equals(ndeXXX1)) {
            return executeBlock1();
        }
        if (block.equals(ndeXXX2)) {
            return executeBlock11();
        }
        throw new AlgorithmDefinitionError("Invalid algorithm, unknown block #" + block);
    }

    //диспетчеризация исключения
    @Override
    public Id findCatch(Id scope, Throwable e0) throws Throwable {
        if (scope.equals(ndeXXX1)) {
            try {
                throw e0;
            } catch (Throwable e) {  //генерируется для каждого блока Catch
                return ndeXXX2; 		//номер следующего после Catch блока
            }
//			catch (ExceptionXXX e) {
//			...
//			}
//		case 11:
        }
        throw new AlgorithmDefinitionError("Invalid algorithm, unknown scope #" + scope);
    }

//Тела для каждого блока	
    private int executeBlock1() throws Throwable { //return номер выхода или нет return
        //Для блока Program, Empty:
        //	java код ...; 

        //Для блока Switch:
        //	if (...) return 1; if (...) return 2; ...

        //Для блока Throw 
        //	throw new <ExceptionXXX>(<parameters>);

        //Для блока Trace:
        trace(EEventSeverity.DEBUG,
                new TraceProducer() {
            @Override
            public String getMessage() throws Throwable {
                //	start user java код 
                String mess = "trace message";
                return mess;
                // end user java код
            }
        }.getMessage());
        //	java код ...; 

        //Для блока на базе App
        //		/*
        int exit = -1;
        if (getCurrentThread().state == AlgorithmExecutor.Thread.ACTIVE) {
            exit = AppBlockXXXExecutor.invoke(this);
            if (exit == 0) {  //wait
                getCurrentThread().state = AlgorithmExecutor.Thread.WAITING;
                return -1;
            }
        } else if (getCurrentThread().state == AlgorithmExecutor.Thread.RESUME) {
            exit = AppBlockXXXExecutor.resume(this);
        }
        return exit;
        //		*/
        //	return 1; //returns output number
    }

    private int executeBlock11() throws Throwable { //код 11-го блока
        //...
        return 1;
    }
//        @Override
//        public String getTitleById(Id id) {
//            return null;
//        }
}

//пример прикладного блока org.radixware.algo
//public
class AppBlockXXXExecutor {
    //return 0 - wait

    static public int invoke(Algorithm algo) throws Throwable {
        AlgorithmExecutor executor = algo.getExecutor();
        AlgorithmExecutor.Thread thread = algo.getCurrentThread();
        Id block = thread.blockId;
        double timeout = ((Double) executor.getData(algo.getBlockPropId(block, "TO"))).doubleValue();
        if (executor.syncExecution || timeout <= AlgorithmExecutor.WAIT_TIMEOUT_BOUNDARY) {

            //исполнить с синхронным ожиданием события

            executor.setData(algo.getBlockPropId(block, "O"), "-"); //выходной параметр 
            return 1;
        } else {
//			Entity resumeJob=algo.scheduleEventJob(null, algo.getBlockPropId(block,"W1")); //подписка на асинхронный onEvent
//			Entity timeoutJob=algo.scheduleTimeoutJob(timeout, algo.getBlockPropId(block,"W2")); //подписка на onTimeout
//			JobQueue.linkOrList(resumeJob,timeoutJob); //связывание заданий в OR-list 
            //запустить обработку

            return 0;
        }
    }

    public static int resume(Algorithm algo) {
        AlgorithmExecutor executor = algo.getExecutor();
        AlgorithmExecutor.Thread thread = executor.getCurrentThread();
        Id block = thread.blockId;
        Long waitId = thread.resumedWaitId;
        String payload = (String) thread.clientData;
        executor.setProperty("O", payload); //выходной параметр
        if (waitId.equals(executor.getData(algo.getBlockPropId(block, "W1")))) {
            return 1;
        } else if (waitId.equals(executor.getData(algo.getBlockPropId(block, "W2")))) {
            return 2;
        } else {
            throw new RadixError("Unknown waitId " + waitId);
        }
    }
}