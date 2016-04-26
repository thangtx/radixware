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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.HashMap;

import org.radixware.kernel.server.arte.services.Service;
import org.radixware.kernel.server.arte.services.aas.ArteAccessService;
import org.radixware.kernel.server.arte.services.eas.ExplorerAccessService;
import org.radixware.kernel.server.types.Cursor;
import org.radixware.kernel.server.types.Entity;
import org.radixware.kernel.server.types.EntityGroup;
import org.radixware.kernel.server.types.EntityPropVals;
import org.radixware.kernel.server.types.Pid;
import org.radixware.kernel.server.types.PresentationEntityAdapter;
import org.radixware.kernel.server.utils.TransactionLock;
import java.sql.Connection;
import java.sql.Types;
import java.util.*;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.auth.AuthUtils;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.utils.MlsProcessor;
import org.radixware.kernel.common.enums.*;
import org.radixware.kernel.common.environment.IRadixEnvironment;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.exceptions.WrongFormatError;
import org.radixware.kernel.common.lang.ReflectiveCallable;
import org.radixware.kernel.common.trace.LocalTracer;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.SrvRunParams;
import org.radixware.kernel.server.arte.services.sb.SbService;
import org.radixware.kernel.server.exceptions.ArteInitializationError;
import org.radixware.kernel.server.exceptions.DatabaseError;
import org.radixware.kernel.server.exceptions.ServiceProcessBreakException;
import org.radixware.kernel.common.repository.DbConfiguration;
import org.radixware.kernel.server.instance.Instance;
import org.radixware.kernel.server.instance.ObjectCache;
import org.radixware.kernel.server.instance.arte.IArteProvider;
import org.radixware.kernel.server.instance.arte.IArteRequest;
import org.radixware.kernel.server.trace.TraceProfiles;
import org.radixware.kernel.server.types.SqlClass.PreparedStatementsCache;
import org.radixware.kernel.server.utils.KernelLayers;
import org.radixware.kernel.server.utils.PriorityResourceManager;
import org.radixware.kernel.starter.config.ConfigEntry;
import org.radixware.kernel.starter.config.ConfigFileAccessor;
import org.radixware.kernel.starter.config.ConfigFileParseException;
import org.radixware.kernel.starter.radixloader.RadixLoader;
import org.radixware.kernel.starter.radixloader.RadixLoaderException;

public class Arte implements IRadixEnvironment {

    private static enum ECommitMode {

        FINAL,
        INTERMEDIATE;
    }
    private static final int AAS_RECV_TIMEOUT_SEC = 5 * 60;//seconds = 5 min
    private static final int EAS_RECV_TIMEOUT_SEC = 5 * 60;//seconds = 5 min
    private static final String ERR_CANT_INVOKE_METHOD = "Can\'t invoke method: ";
    private static final String APP_PARAMS_SECTION = "App";
    private static final String INACTIVE_MARKER = " [incactive]";
    private static final int MAX_AFTER_COMMIT_ITERATIONS = 10;
    //
    private static volatile IArteProvider FALLBACK_ARTE_PROVIDER;//for tests
    //
    private final ArteProfiler profiler;
    private List<EIsoLanguage> languages = null;
    private final SessionLock sessionLock;
    private DbConfiguration dbConfiguration;
    private ArteSocket arteSocket = null;
    private final Map<String, Service> services = new HashMap<>(7); //{service - presenter}
    private final Vector<Savepoint> savepoints = new Vector<>();
    private Object traceTargetHandler = null;    //Server params
    private EIsoLanguage serverLanguage = EIsoLanguage.ENGLISH;
    private EIsoCountry serverCountry = EIsoCountry.UNITED_STATES;
    private TraceProfiles arteTraceProfiles = TraceProfiles.DEFAULT;
    private long arteInstSeq = 0;
    private final TransactionLock tranLock;
    private final Instance instance;
    private final DbConnection dbConnection;
    private final Cache cache;
    private final ObjectCache objectCache = new ObjectCache();
    private boolean inTransaction = false;
    private PreparedStatement qryRegister = null;
    private PreparedStatement qryUnregister = null;
    private PreparedStatement stmtReadParams = null;
    private final Rights rights;
    private final DefManager defManager;
    private final JobQueue jobQueue;
    private boolean inServiceRequest;
    private boolean serviceRequestBroken;
    private ArteTransactionParams transactionParams;
    private EIsoCountry clientCountry;
    private EIsoLanguage clientLanguage = EIsoLanguage.ENGLISH;    
    private Locale clientLocale;
    private Locale customClientLocale;
    private long lastReadParamsTime = 0;
    private boolean inited = false;
    private long tranSeqNum = 0;
    boolean readonlySeance = false;
    private long dbTranSeqNum = 1; //see RADIX-4942
    private String systemName = null;
    private final Thread processorThread;
    private long rqProcessingStartMillis = -1;
    private IArteRequest request;
    private boolean useActiveArteLimits = false;
    private volatile boolean maintenanceReuested;
    private long dbSid;
    private long dbSerial;

    public Arte(final Instance instance) {
        this.instance = instance;
        cache = new Cache(this);
        dbConnection = new DbConnection(this);
        defManager = new DefManager(this);
        rights = new Rights(this);
        trace = Trace.Factory.newInstance(this);
        jobQueue = new JobQueue(this);
        sessionLock = new SessionLock(this);
        tranLock = new TransactionLock();
        profiler = new ArteProfiler(this);
        dbConfiguration = instance.getDbConfiguration();
        processorThread = Thread.currentThread();
    }

    public Thread getProcessorThread() {
        return processorThread;
    }

    public ObjectCache getObjectCache() {
        return objectCache;
    }

    public SessionLock getSessionLock() {
        return sessionLock;
    }

    public DbConfiguration getDbConfiguration() {
        return dbConfiguration;
    }

    public ArteProfiler getProfiler() {
        return profiler;
    }

    public long getExplicitRequestVersion() {
        return request.getVersion();
    }

    public IArteRequest getRequest() {
        return request;
    }

    public long getEffectiveRequestVersion() {
        if (getExplicitRequestVersion() > 0) {
            return getExplicitRequestVersion();
        }
        return getDefManager().getCachedVersions().get(0);
    }

    public long getLatestCachedVersion() {
        if (getDefManager() == null || getDefManager().getCachedVersions() == null || getDefManager().getCachedVersions().isEmpty()) {
            return -1;
        }
        return getDefManager().getCachedVersions().get(0);
    }

    public TransactionLock getTransactionLock() {
        return tranLock;
    }

    public JobQueue getJobQueue() {
        return jobQueue;
    }
    private final Trace trace;

    @Override
    public Trace getTrace() {
        return trace;
    }

    public LocalTracer createLocalTracer(final String eventSource) {
        return getTrace().newTracer(eventSource);
    }

    public Rights getRights() {
        return rights;
    }

    @Override
    public DefManager getDefManager() {
        return defManager;
    }

    public DbConnection getDbConnection() {
        return dbConnection;
    }

    public long getSeqNumber() {
        return arteInstSeq;
    }

    public final void init(final Connection dbConnection, final ArteSocket socket, final long instSeq) {
        if (inited) {
            return;
        }
        arteInstSeq = instSeq;
        getDbConnection().init(dbConnection);
        try {
            final Statement st = getDbConnection().get().createStatement();
            try {
                final ResultSet rs;
                getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
                try {
                    rs = st.executeQuery("select NAME, SYS_CONTEXT('USERENV', 'CURRENT_SCHEMA') SCHEMA,"
                            + " arteDbTraceProfile, arteFileTraceProfile, arteGuiTraceProfile,"
                            + " (SELECT sid FROM v$session WHERE audsid = userenv('sessionid')) dbSid, (SELECT serial# FROM v$session WHERE audsid = userenv('sessionid')) dbSerial"
                            + " from rdx_system where id=1");
                } finally {
                    getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
                }
                try {
                    if (rs.next()) {
                        systemName = rs.getString("NAME");
                        databaseScheme = rs.getString("SCHEMA").toUpperCase();
                        arteTraceProfiles
                                = new TraceProfiles(
                                        rs.getString("arteDbTraceProfile"),
                                        rs.getString("arteFileTraceProfile"),
                                        rs.getString("arteGuiTraceProfile"));
                        dbSid = rs.getLong("dbSid");
                        dbSerial = rs.getLong("dbSerial");
                        getTrace().put(EEventSeverity.EVENT, Messages.MLS_ID_TRACE_PROFILE_CHANGED, new ArrStr(arteTraceProfiles.toString()), EEventSource.ARTE.getValue());
                        traceTargetHandler = getTrace().addTargetLog(arteTraceProfiles.getDbTraceProfile());
                    } else {
                        throw new ArteInitializationError("Can't determinate product name etc.\nTable DBP_SYSTEM is invalid.", null);
                    }
                } finally {
                    rs.close();
                }
            } finally {
                st.close();
            }

            registerService(ExplorerAccessService.SERVICE_WSDL, new ExplorerAccessService(this, EAS_RECV_TIMEOUT_SEC));
            registerService(ArteAccessService.SERVICE_WSDL, new ArteAccessService(this, AAS_RECV_TIMEOUT_SEC));

            //other layers initializing
            for (Class<?> c : KernelLayers.classListFromBottomToTopLayer("server.app.Initializer")) {
                try {
                    ((AppInitializer) c.newInstance()).init(this);
                } catch (Throwable e) {
                    throw new ArteInitializationError("Can't execute the application initializer: " + getTrace().exceptionStackToString(e), e);
                }
            }

            arteSocket = socket;

            //RADIX-1893: load dds and enums for current version
            getDefManager().setVersion(getActualVersion());
            inited = true;
        } catch (SQLException e) {
            getTrace().put(EEventSeverity.ALARM, "Can't init ARTE: " + getTrace().exceptionStackToString(e), EEventSource.ARTE);
            getDbConnection().close();
            throw new DatabaseError(e);
        } catch (ArteInitializationError e) {
            throw e;
        } catch (Throwable e) {
            getTrace().put(EEventSeverity.ALARM, "Can't init ARTE: " + getTrace().exceptionStackToString(e), EEventSource.ARTE);
            getDbConnection().close();
            throw new ArteInitializationError("Can' init ARTE", e);
        }

    }

    public void afterUpdate(final Entity entity) {
        getCache().afterUpdate(entity);
    }

    public void afterDelete(final Entity entity) {
        getCache().afterDelete(entity);
    }

    public void afterCreate(final Entity entity) {
        getCache().afterCreate(entity);
    }

    public void requestMaintenance() {
        maintenanceReuested = true;
    }

    public boolean needMaintenance() {
        return maintenanceReuested;
    }

    public void maintenance() {
        maintenanceReuested = false;
        getDefManager().maintenanceAllUserCaches();
        pingDb();
    }

    private void pingDb() {
        actualizeServerParams();
        getDefManager().setVersion(getActualVersion());
    }

    public void registerService(final String serviceUri, final Service p) {
        services.put(serviceUri, p);
    }

    public Service getService(final String serviceUri) {
        Service p = services.get(serviceUri);
        if (p == null) {
            p = new SbService(this, serviceUri, AAS_RECV_TIMEOUT_SEC); //RADIX-6213
            //throw new IllegalUsageError("Invalid service " + serviceUri);
        }
        return p;
    }

    public Map<String, String> getAppParams() {
        final String configFile = SrvRunParams.getConfigFile();
        if (configFile != null && !configFile.isEmpty()) {
            try {
                final ConfigFileAccessor configAccessor = ConfigFileAccessor.get(configFile, APP_PARAMS_SECTION);
                if (configAccessor == null) {
                    return Collections.emptyMap();
                }
                final Map<String, String> result = new HashMap<String, String>();
                for (ConfigEntry entry : configAccessor.getEntries()) {
                    result.put(entry.getKey(), entry.getValue());
                }
                return result;
            } catch (ConfigFileParseException ex) {
                return Collections.emptyMap();
            }
        } else {
            return Collections.emptyMap();
        }
    }

    public void processServiceRequest(final Service service) throws InterruptedException {
        try {
            inServiceRequest = true;
            serviceRequestBroken = false;
            useActiveArteLimits = request != null && request.getActiveTicket() != null;
            service.process(arteSocket);
        } catch (RuntimeException e) {
            getTrace().put(EEventSeverity.ERROR, "Unhandled " + getTrace().exceptionStackToString(e), EEventSource.ARTE);
            throw e;
        } finally {
            inServiceRequest = false;
        }
    }

    public long getDbSid() {
        return dbSid;
    }

    public long getDbSerial() {
        return dbSerial;
    }

    public boolean needBreak() {
        if (!inServiceRequest) {
            return false;
        }
        if (serviceRequestBroken || arteSocket.breakSignaled()) {
            serviceRequestBroken = true;
        }
        return serviceRequestBroken;
    }

    public final void checkBreak() throws ServiceProcessBreakException {
        if (needBreak()) {
            throw new ServiceProcessBreakException("ARTE service request aborted by client");
        }
    }

    public long calcSpNesting(final String spId) throws WrongFormatError {
        if (spId == null) {
            return 0;
        }
        try {
            return Integer.parseInt(spId.substring(3));
        } catch (NumberFormatException e) {
            throw new WrongFormatError("Wrong format of savepoint id: " + spId, e);
        }
    }

    public Cache getCache() {
        return cache;
    }

    public Entity getEntityObject(final Pid pid) {
        return getEntityObject(pid, null, false);
    }

    public Entity getEntityObject(final Pid pid, final EntityPropVals loadedProps, final boolean checkExistence) {
        return getCache().getEntityObject(pid, loadedProps, checkExistence);
    }

    public Object newObject(final Id classId) {
        return getCache().newObject(classId);
    }

    public void registerExistingEntityObject(final Entity obj) {
        getCache().registerExistingEntityObject(obj);
    }

    public void unregisterExistingEntityObject(final Entity obj) {
        getCache().unregisterExistingEntityObject(obj);
    }

    public void registerNewEntityObject(final Entity obj) {
        getCache().registerNewEntityObject(obj);
    }

    public void unregisterNewEntityObject(final Entity obj) {
        getCache().unregisterNewEntityObject(obj);
    }

    public Entity findParentInNewEntityObjects(final DdsReferenceDef ref, final Entity child) {
        return getCache().findParentInNewEntityObjects(ref, child);
    }

    public Entity findNewEntityObjectByPid(final Pid pid) {
        return getCache().findNewEntityObjectByPid(pid);
    }

    public EntityGroup getGroupHander(final Id entityId, final boolean isContextWrapper) {
        return getCache().getGroupHander(entityId, isContextWrapper);
    }

    public EntityGroup getGroupHander(final Id entityId) {
        return getGroupHander(entityId, false);
    }

    public <T extends Entity> PresentationEntityAdapter<T> getPresentationAdapter(final T entity) {
        return getCache().getPresentationAdapter(entity);
    }

    public void registerCursor(final Cursor obj) {
        getCache().registerCursor(obj);
    }

    public void unregisterCursor(final Cursor obj) {
        getCache().unregisterCursor(obj);
    }

    public void registerStatementsCache(final PreparedStatementsCache statementsCache) {
        getDefManager().getReleaseCache().registerStatementsCache(statementsCache);
    }

    public ArteSocket getArteSocket() {
        return arteSocket;
    }

    public final Long getVersion() {
        return transactionParams==null ? null : transactionParams.getVersion();
    }
    
    public final Long getEasSessionId() {
        return transactionParams==null ? null : transactionParams.getEasSessionId();
    }    

    public final String getUserName() {
        return transactionParams==null ? null : transactionParams.getUserName();
    }    

    public final String getStationName() {
        return transactionParams==null ? null : transactionParams.getStationName();
    }        

    @Override
    public final EIsoLanguage getClientLanguage() {
        return clientLanguage;
    }

    public final void setClientLanguage(final EIsoLanguage clientLanguage) {
        if (this.clientLanguage != clientLanguage) {
            this.clientLanguage = clientLanguage;
            updateClientLocale();
        }
    }

    public final EIsoCountry getClientCountry() {
        return clientCountry;
    }

    public final void setClientCountry(final EIsoCountry clientCountry) {
        if (this.clientCountry != clientCountry) {
            this.clientCountry = clientCountry;
            updateClientLocale();
        }
    }

    public final Locale getClientLocale() {
        return customClientLocale == null ? clientLocale : customClientLocale;
    }

    public final void setClientLocale(final Locale newLocale) {
        customClientLocale = newLocale;
    }

    private void updateClientLocale() {
        if (clientCountry == null) {
            clientLocale = new Locale(clientLanguage.getValue());
        } else {
            clientLocale = new Locale(clientLanguage.getValue(), clientCountry.getValue());
        }
    }    

    public final ERuntimeEnvironmentType getClientEnvironment() {
        return transactionParams==null ? null : transactionParams.getClientEnvironment();
    }
    
    public final boolean isEasSessionKeyAccessible(){
        return transactionParams!=null && transactionParams.isSessionKeyAccessible();
    }
    
    public final byte[] encryptByEasSessionKey(final byte[] data){
        if (transactionParams==null || !transactionParams.isSessionKeyAccessible()){
            throw new IllegalStateException("EAS session key does not accessible");
        }
        final byte[] sessionKey = transactionParams.getSessionKey();
        try{
            return AuthUtils.encrypt(data, sessionKey);
        }finally{
            Arrays.fill(sessionKey, (byte)0);
        }        
    }
    
    public final byte[] decryptByEasSessionKey(final byte[] encryptedData){
        if (transactionParams==null || !transactionParams.isSessionKeyAccessible()){
            throw new IllegalStateException("EAS session key does not accessible");
        }
        final byte[] sessionKey = transactionParams.getSessionKey();
        try{
            return AuthUtils.decrypt(encryptedData, sessionKey);
        }finally{
            Arrays.fill(sessionKey, (byte)0);
        }
    }

    public EIsoLanguage getServerLanguage() {
        return serverLanguage;
    }

    public final String getSystemName() {
        return systemName;
    }
    private String databaseScheme = null;

    public final String getDatabaseScheme() {
        return databaseScheme;
    }        

    //write down all changes to database. 
    public final void updateDatabase(final EAutoUpdateReason reason) {
        if (isReadonlyTransaction()) {
            return;
        }
        getCache().updateDatabase(reason);
        getDefManager().sendBatches(true); //send all batches
    }

    //set savepoint.
    public final String setSavepoint() {
        updateDatabase(EAutoUpdateReason.PREPARE_SAVEPOINT);
        final String id = "spt" + String.valueOf(savepoints.size() + 1);
        try {
            savepoints.add(getDbConnection().get().setSavepoint(id));
        } catch (SQLException e) {
            throw new DatabaseError("Can't set savepoint: " + ExceptionTextFormatter.getExceptionMess(e), e);
        }
        getCache().onSetSavepoint(id);
        return id;
    }

    public void beforeRequestProcessing(IArteRequest request) {
        rqProcessingStartMillis = System.currentTimeMillis();
        this.request = request;
    }

    public void markInactive() {
        if (!useActiveArteLimits) {
            return;
        }
        if (request != null && request.getActiveTicket() != null && !request.getActiveTicket().isReleased()) {
            instance.getActiveArteResourceManager().releaseTicket(request.getActiveTicket());
            Thread.currentThread().setName(Thread.currentThread().getName() + INACTIVE_MARKER);
            trace.put(EEventSeverity.DEBUG, "ARTE was marked as inactive", EEventSource.ARTE);
        }
    }

    public void markActive() {
        if (!useActiveArteLimits) {
            return;
        }
        if (request != null && request.getActiveTicket() != null && request.getActiveTicket().isReleased()) {
            try {
                getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_WAIT_ACTIVE);
                try {
                    request.setActiveTicket(instance.getActiveArteResourceManager().requestTicket(request.getRadixPriority(), -1, PriorityResourceManager.EQueuePriority.FIRST));
                    Thread.currentThread().setName(Thread.currentThread().getName().replace(INACTIVE_MARKER, ""));
                } finally {
                    getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_WAIT_ACTIVE);
                }
                trace.put(EEventSeverity.DEBUG, "ARTE was marked as active", EEventSource.ARTE);
            } catch (InterruptedException ex) {
                getTrace().put(EEventSeverity.WARNING, "Interrupted while acquiring activity ticket", EEventSource.ARTE);
            }
        }
    }

    public long getRqProcessingStartMillis() {
        return rqProcessingStartMillis;
    }

    //rollback to savepoint.
    public final void rollbackToSavepoint(final String id) {
        getCache().beforeRollbackToSavepoint(id);
        getDefManager().sendBatches(false);//rollback to savepoint does not clear batches let's send them
        int idx;
        try {
            idx = Integer.parseInt(id.substring(3)) - 1;
        } catch (NumberFormatException e) {
            throw new WrongFormatError("Wrong format of savepoint id: " + id, e);
        }
        final Savepoint sp = savepoints.get(idx);
        //deleting this and later savepoints
        savepoints.setSize(idx);  //savepoints.remove(id);
        getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_ROLLBACK);
        try {
            getDbConnection().get().rollback(sp);
        } catch (SQLException e) {
            throw new DatabaseError("Can't rollback to savepoint: " + ExceptionTextFormatter.getExceptionMess(e), e);
        } finally {
            getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_ROLLBACK);
        }
        getCache().afterRollbackToSavepoint(id);
    }

    public void startTransaction(final ArteTransactionParams transactionParams) {
        if (isInTransaction()) {
            throw new IllegalUsageError("Double start transaction");
        }
        tranSeqNum++;
        this.transactionParams = transactionParams;
        clientLanguage = transactionParams.getClientLanguage() == null ? serverLanguage : transactionParams.getClientLanguage();
        clientCountry = transactionParams.getClientCountry() == null ? serverCountry : transactionParams.getClientCountry();
        updateClientLocale();
        readonlySeance = false;

        savepoints.clear();
        getDefManager().setVersion(transactionParams.getVersion());
        inTransaction = true;
        getCache().prepareForTransaction();
        final StringBuilder traceMessage = new StringBuilder();
        traceMessage.append("ARTE session started at instance \'");
        traceMessage.append(getInstance().getFullTitle());
        traceMessage.append("\', ARTE instance number=");
        traceMessage.append(String.valueOf(arteInstSeq));
        traceMessage.append(", version=");
        traceMessage.append(String.valueOf(transactionParams.getVersion()));
        getTrace().put(EEventSeverity.DEBUG, traceMessage.toString(), EEventSource.ARTE);
        getProfiler().onBeginTransaction();
        actualizeServerParams();//performs db requests, should be called after profiling start
    }

    public long getActualVersion() {
        try {
            return RadixLoader.getInstance().getCurrentRevision();
        } catch (RadixLoaderException e) {
            throw new ArteInitializationError(e.getMessage(), e);
        }
    }

    private void actualizeServerParams() {
        dbConfiguration = instance.getDbConfiguration();

        final long currTime = System.currentTimeMillis();

        if ((lastReadParamsTime == 0 || currTime - lastReadParamsTime >= 60000) && getDbConnection().get() != null) {
            lastReadParamsTime = currTime;
            if (stmtReadParams == null) {
                getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
                try {
                    stmtReadParams = getDbConnection().get().prepareStatement("select arteDbTraceProfile, arteFileTraceProfile, arteGuiTraceProfile, ARTELANGUAGE, arteCountry, profileMode, profiledPrefixes from RDX_SYSTEM where ID = 1");
                } catch (SQLException e) {
                    throw new ArteInitializationError("Can't prepare server parameters read: " + ExceptionTextFormatter.getExceptionMess(e), e);
                } finally {
                    getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
                }
            }
            ResultSet rs = null;
            try {
                getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
                try {
                    rs = stmtReadParams.executeQuery();
                } finally {
                    getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
                }
                if (rs.next()) {
                    final TraceProfiles traceProfiles
                            = new TraceProfiles(
                                    rs.getString("arteDbTraceProfile"),
                                    rs.getString("arteFileTraceProfile"),
                                    rs.getString("arteGuiTraceProfile"));
                    profiler.setOptions(new ArteProfiler.Options(
                            EProfileMode.getForValue(rs.getInt("profileMode")),
                            rs.getString("profiledPrefixes"),
                            instance.getInstanceMonitor().getMonitoredSections(),
                            instance.getInstanceMonitor().isCountProfileStatistic()));

                    if (!arteTraceProfiles.equals(traceProfiles)) {
                        getTrace().changeTargetProfile(traceTargetHandler, traceProfiles.getDbTraceProfile());
                        arteTraceProfiles = traceProfiles;
                        getTrace().put(EEventSeverity.EVENT, Messages.MLS_ID_TRACE_PROFILE_CHANGED, new ArrStr(traceProfiles.toString()), EEventSource.ARTE.getValue());
                    }
                    if (serverLanguage == null || !serverLanguage.toString().equals(rs.getString("ARTELANGUAGE"))) {
                        serverLanguage = EIsoLanguage.getForValue(rs.getString("ARTELANGUAGE"));
                    }
                    if (serverCountry == null || !serverCountry.getValue().equals(rs.getString("arteCountry"))) {
                        try {
                            serverCountry = EIsoCountry.getForValue(rs.getString("arteCountry"));
                        } catch (NoConstItemWithSuchValueError error) {
                            serverCountry = EIsoCountry.UNITED_STATES;
                        }
                    }
                } else {
                    throw new ArteInitializationError("Can't read server paramters: RDX_SYSTEM with ID = 1 (This System) is not found", null);
                }
            } catch (SQLException e) {
                throw new ArteInitializationError("Can't read server paramters: " + ExceptionTextFormatter.getExceptionMess(e), e);
            } finally {
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (SQLException e) {
                        getTrace().put(EEventSeverity.WARNING, "Can't close ResultSet: " + getTrace().exceptionStackToString(e), EEventSource.ARTE_DB);
                    }
                }
            }
        }
    }

    private final void incDbTranSeq() {
        dbTranSeqNum++;
    }

    public long getDbTransactionSeqNumber() {
        return dbTranSeqNum;
    }

    public long getSavePointsNesting() {
        return savepoints.size();
    }

    public void endTransaction(final boolean commit) throws Throwable {
        Throwable error = null;
        if (!getDbConnection().get().isClosed()) {
            try {
                if (commit) {
                    commit(ECommitMode.FINAL);
                } else {
                    rollback();
                }
            } catch (Throwable e) {
                error = e;
                Throwable rollbackException = commit ? null : e;
                if (commit) {
                    getTrace().put(EEventSeverity.ERROR, "Can't do commit at the end of transaction: " + getTrace().exceptionStackToString(e) + "\nTrying to do rollback.", EEventSource.ARTE);
                    try {
                        rollback();
                    } catch (Throwable rlbckEx) {
                        rollbackException = rlbckEx;
                    }
                }
                if (rollbackException != null) {
                    getTrace().put(EEventSeverity.ERROR, "Can't do full ARTE rollback at the end of transaction: " + getTrace().exceptionStackToString(rollbackException) + "\nTrying to do database transaction rollback.", EEventSource.ARTE);
                    try {
                        getDbConnection().get().rollback();
                    } catch (SQLException sqlEx) {
                        throw new DatabaseError("Can't do rollback: " + ExceptionTextFormatter.getExceptionMess(e), sqlEx);
                    }
                    incDbTranSeq();
                }
            }
            if (getCache().getEntityModificationsCount() > 0) {
                getTrace().put(EEventSeverity.WARNING, "There are pending modifications after transaction has been ended, count = " + getCache().getEntityModificationsCount(), EEventSource.ARTE);
            }
            getCache().clear(null);
            getCache().setDefaultMode();
            if (isInTransaction() && isReadonlyTransaction()) {
                //remove all modified keptInCache Entities
                //they are corrupted because of modifications done beetwen startTran and switchToReadonlyTran
                //EAS sometimes temporary modifies objects
                //to let its methods "see" modification that is done in editor but not saved in DB yet
                //Than EAS switches to readonly transaction to prevent saving these changes
                getCache().visitAllUsedExistingEntities(null, new Cache.EntityVisitor() {
                    @Override
                    public void visit(final Entity ent) {
                        if (ent.isModified()) {
                            ent.discard();
                        }
                    }
                });
            }
            readonlySeance = false;
            getSessionLock().releaseAllLocks();
            getDbConnection().traceSqlWarnings();
        } else {
            error = new DatabaseError("Unable to end transaction: database connection is closed", null);
        }

        if (isInTransaction()) {
            inTransaction = false;
            getDefManager().clear();
            getTrace().put(EEventSeverity.DEBUG, "ARTE seance finished, version=" + transactionParams.getVersion() + ", duration=" + (System.currentTimeMillis() - rqProcessingStartMillis) + "ms", EEventSource.ARTE);
        }
        
        transactionParams = null; //RADIX-2490: clear seance info not to be used during future tracing

        getProfiler().onEndTransaction();
        getProfiler().flush();
        getObjectCache().maintenance();
        getDefManager().getReleaseCache().getUserCache().maintenance();

        if (error != null) {
            throw error;
        }
    }

    public void commit() {
        commit(ECommitMode.INTERMEDIATE);
    }

    private void commit(final ECommitMode commitMode) {
        getTrace().put(EEventSeverity.DEBUG, "Commit requested [" + commitMode.name() + "]", EEventSource.ARTE);
        int afterCommitsCount = 0;
        if (isInTransaction()) //in ARTE transaction (seance)
        {
            updateDatabase(EAutoUpdateReason.PREPARE_COMMIT);
        }
        while (true) {
            getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_COMMIT);
            try {
                getDbConnection().get().commit();
            } catch (SQLException e) {
                throw new DatabaseError("Can't do commit: " + ExceptionTextFormatter.getExceptionMess(e), e);
            } finally {
                getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_COMMIT);
            }
            getTransactionLock().unregisterAllLocks();
            incDbTranSeq();
            getTrace().put(EEventSeverity.DEBUG, "Commit complete", EEventSource.ARTE);

            if (afterCommitsCount == MAX_AFTER_COMMIT_ITERATIONS) {
                getTrace().put(EEventSeverity.ERROR, "afterCommit iterations limit reached (" + MAX_AFTER_COMMIT_ITERATIONS + "), skipping afterCommit phase", EEventSource.ARTE);
                break;
            }

            getCache().afterCommit();
            afterCommitsCount++;

            if (commitMode == ECommitMode.INTERMEDIATE) {
                break;
            } else {
                if (isInTransaction()) {
                    updateDatabase(EAutoUpdateReason.PREPARE_COMMIT);
                }
                if (getDbConnection().getRadixConnection().wereWriteOperations()) {
                    getTrace().put(EEventSeverity.DEBUG, "Additional commit requested due to changes in afterCommit #" + afterCommitsCount, EEventSource.ARTE);
                } else {
                    break;
                }
            }
        }
    }

    public void rollback() {
        getTrace().put(EEventSeverity.DEBUG, "Rollback requested", EEventSource.ARTE);
        if (isInTransaction()) {
            getDefManager().sendBatches(false);//rollback does not clear batches let's send them
        }
        getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_ROLLBACK);
        try {
            getDbConnection().get().rollback();
        } catch (SQLException e) {
            throw new DatabaseError("Can't do rollback: " + ExceptionTextFormatter.getExceptionMess(e), e);
        } finally {
            getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_ROLLBACK);
        }
        try {
            getTransactionLock().unregisterAllLocks();
            getCache().afterRollback();
        } finally {
            incDbTranSeq();
        }
        getTrace().put(EEventSeverity.DEBUG, "Rollback complete", EEventSource.ARTE);
    }

    public final void discardAllUsedEntities(final boolean bIncludeKeptInCache) {
        getCache().visitAllUsedExistingEntities(null, new Cache.EntityVisitor() {
            @Override
            public void visit(final Entity ent) {
                if (bIncludeKeptInCache || ent.getCanBeRemovedFromCache()) {
                    ent.discard();
                }
            }
        });
    }

    public final boolean isInTransaction() {
        return inTransaction;
    }

    /**
     * ????? ???????????? ??????.
     */
    public Object invokeByClassId(final Id classId, final String methodName, final Class[] paramTypes, final Object[] paramVals) throws Exception {
        if (!isInTransaction()) {
            throw new IllegalUsageError("Transaction not started");
        }
        try {
            final Class<?> clazz = getDefManager().getClass(classId);
            final Method method = getMethod(clazz, methodName, paramTypes);
            if (!Modifier.isStatic(method.getModifiers())) {
                throw new IllegalUsageError("Method " + method + " is not static");
            }
            return method.invoke(null, paramVals);
        } catch (SecurityException e) {
            throw new RadixError(ERR_CANT_INVOKE_METHOD + ExceptionTextFormatter.getExceptionMess(e), e);
        } catch (IllegalArgumentException e) {
            throw new RadixError(ERR_CANT_INVOKE_METHOD + ExceptionTextFormatter.getExceptionMess(e), e);
        } catch (IllegalAccessException e) {
            throw new RadixError(ERR_CANT_INVOKE_METHOD + ExceptionTextFormatter.getExceptionMess(e), e);
        } catch (InvocationTargetException e) {
            final Throwable cause = e.getCause();
            if (cause != null) {
                if (cause instanceof Exception) {
                    throw (Exception) cause;
                }
                throw new RadixError(ERR_CANT_INVOKE_METHOD + ExceptionTextFormatter.getExceptionMess(cause), cause);
            }
            throw new RadixError(ERR_CANT_INVOKE_METHOD + ExceptionTextFormatter.getExceptionMess(e), e);
        }
    }

    /**
     * ????? ???????????? ??????.
     *
     * @throws ClassNotFoundException
     */
    public Object invokeByClassName(final String className, final String methodName, final Class[] paramTypes, final Object[] paramVals) throws ClassNotFoundException, Exception {
        if (!isInTransaction()) {
            throw new IllegalUsageError("Transaction not started");
        }
        try {
            final Method method = getMethod(getDefManager().getClassLoader().loadClass(className), methodName, paramTypes);
            if (!Modifier.isStatic(method.getModifiers())) {
                throw new IllegalUsageError("Method " + method + " is not static");
            }
            return method.invoke(null, paramVals);
        } catch (SecurityException e) {
            throw new RadixError(ERR_CANT_INVOKE_METHOD + ExceptionTextFormatter.getExceptionMess(e), e);
        } catch (IllegalArgumentException e) {
            throw new RadixError(ERR_CANT_INVOKE_METHOD + ExceptionTextFormatter.getExceptionMess(e), e);
        } catch (IllegalAccessException e) {
            throw new RadixError(ERR_CANT_INVOKE_METHOD + ExceptionTextFormatter.getExceptionMess(e), e);
        } catch (InvocationTargetException e) {
            final Throwable cause = e.getCause();
            if (cause != null) {
                if (cause instanceof Exception) {
                    throw (Exception) cause;
                }
                throw new RadixError(ERR_CANT_INVOKE_METHOD + ExceptionTextFormatter.getExceptionMess(cause), cause);
            }
            throw new RadixError(ERR_CANT_INVOKE_METHOD + ExceptionTextFormatter.getExceptionMess(e), e);
        }
    }

    private final Method getMethod(final Class<?> c, final String name, final Class[] paramTypes) throws SecurityException, NoSuchMethodException {
        try {
            final Method method = c.getMethod(name, paramTypes);
            if (method.getAnnotation(ReflectiveCallable.class) == null) {
                getTrace().put(EEventSeverity.WARNING, method.toGenericString() + " doesn't support reflective call", EEventSource.ARTE);
            }
            return method;
        } catch (NoSuchMethodException e) {//
            if (paramTypes != null && paramTypes.length > 0) {
                for (Method m : c.getMethods()) {
                    if (m.getName().equals(name) && paramTypes.length == m.getParameterTypes().length) {
                        boolean ok = true;
                        for (int i = 0; ok && i < paramTypes.length; i++) {
                            if (m.getParameterTypes()[i].isPrimitive() || paramTypes[i] != null && !m.getParameterTypes()[i].isAssignableFrom(paramTypes[i])) {
                                ok = false;
                            }
                        }
                        if (ok) {
                            return m;
                        }
                    }
                }
            }
            //RADIX-4589 
            throw e;// throw new RadixError(ERR_CANT_INVOKE_METHOD + ExceptionTextFormatter.getExceptionMess(e), e);
        }
    }

    /**
     * ????? ?????? Entity ??????
     */
    public Object invoke(final Pid pid, final String methodName, final Class[] paramTypes, final Object[] paramVals) throws Exception {
        if (!isInTransaction()) {
            throw new IllegalUsageError("Transaction not started");
        }
        try {
            final Entity entity = getEntityObject(pid);
            final Method method = getMethod(entity.getClass(), methodName, paramTypes);
            return method.invoke(entity, paramVals);
        } catch (SecurityException e) {
            throw new RadixError(ERR_CANT_INVOKE_METHOD + ExceptionTextFormatter.getExceptionMess(e), e);
        } catch (IllegalArgumentException e) {
            throw new RadixError(ERR_CANT_INVOKE_METHOD + ExceptionTextFormatter.getExceptionMess(e), e);
        } catch (IllegalAccessException e) {
            throw new RadixError(ERR_CANT_INVOKE_METHOD + ExceptionTextFormatter.getExceptionMess(e), e);
        } catch (InvocationTargetException e) {
            final Throwable cause = e.getCause();
            if (cause != null) {
                if (cause instanceof Exception) {
                    throw (Exception) cause;
                }
                throw new RadixError(ERR_CANT_INVOKE_METHOD + ExceptionTextFormatter.getExceptionMess(cause), cause);
            }
            throw new RadixError(ERR_CANT_INVOKE_METHOD + ExceptionTextFormatter.getExceptionMess(e), e);
        }
    }

    public void logCleanupError(final Throwable throwable, final String where) {
        try {
            getTrace().put(EEventSeverity.WARNING, "Error during cleanup in '" + Thread.currentThread().getName() + "' (" + where + "): " + ExceptionTextFormatter.exceptionStackToString(throwable), EEventSource.ARTE);
        } catch (Exception ex) {
            //skip
        }
    }

    public void close() {
        try {
            getTrace().flush();
        } catch (Throwable t) {
            //ignore
        }
        try {
            getDefManager().close();
        } catch (Throwable t) {
            logCleanupError(t, "DefManager");
        }
        for (Service s : services.values()) {
            try {
                s.close();
            } catch (Throwable t) {
                logCleanupError(t, "Service in ARTE");
            }

        }
        if (qryRegister != null) {
            try {
                qryRegister.close();
            } catch (SQLException e) {
                //do nothing
            }
            qryRegister = null;
        }
        if (stmtReadParams != null) {
            try {
                stmtReadParams.close();
            } catch (SQLException e) {
                //do nothing
            }
            stmtReadParams = null;
        }
        try {
            getJobQueue().close();
        } catch (Throwable t) {
            logCleanupError(t, "Job Queue");
        }
        try {
            getSessionLock().closeAllDbQueries();
        } catch (Throwable t) {
            logCleanupError(t, "SessionLock DbQueries");
        }
        try {
            getProfiler().close();
        } catch (Throwable t) {
            logCleanupError(t, "Profiler");
        }
        try {
            getRights().close();
        } catch (Throwable t) {
            logCleanupError(t, "Rights");
        }
        try {
            objectCache.clear();
        } catch (Throwable t) {
            logCleanupError(t, "ObjectCache");
        }
        try {
            getDbConnection().close();
        } catch (Throwable t) {
            logCleanupError(t, "DbConnection");
        }
        try {
            getTrace().flush();
        } catch (Throwable t) {
            //ignore
        }
    }

    public TraceProfiles getArteTraceProfiles() {
        actualizeServerParams();
        return arteTraceProfiles;
    }
//Debugger

    public final void onStartRequestExecution(final String erId) {
        if (executorRequesterId != null) {
            throw new IllegalUsageError("Request executor is already registered");
        }
        executorRequesterId = erId;
        if (isInTransaction()) {
            registerSession();
        }
    }

    public final void onFinishRequestExecution() {
        executorRequesterId = null;
        if (isInTransaction()) {
            unregisterSession();
        }
    }

    public static final String genUserRequestExecutorId(final String userName) {
        return EExecutionRequesterType.USER_SESSION.getValue() + "-" + userName;
    }
    private String executorRequesterId;

    public final String getExecutorRequesterId() {
        return executorRequesterId;
    }//Service        
    private XmlObject curProcessedServiceRq = null;

    public final void serviceRqProcessingStarted(final XmlObject rq) {
        if (curProcessedServiceRq != null) {
            throw new IllegalUsageError("Service request is already registered");
        }
        curProcessedServiceRq = rq;
    }

    public final void serviceRqProcessingStopped() {
        curProcessedServiceRq = null;
    }

    public final XmlObject getCurrentProcessedServiceRq() {
        return curProcessedServiceRq;
    }

    /**
     * Modifications are not allowed all modifications done before switching to
     * readonly transaction will be ignored
     */
    public boolean isReadonlyTransaction() {
        return readonlySeance;
    }

    /**
     * Ignore all unsaved modification and do not allow do new ones
     */
    public void switchToReadonlyTransaction() {
        readonlySeance = true;
    }

    long getTransactionSeqNumber() {
        return tranSeqNum;
    }

    private final void unregisterSession() {
        try {
            if (qryUnregister == null) {
                getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
                try {
                    qryUnregister = getDbConnection().get().prepareStatement(
                            "begin RDX_ARTE.unregisterSession(?); end;");
                } finally {
                    getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
                }
            }
            getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
            try {
                if (getEasSessionId()!= null) {
                    qryUnregister.setLong(1, getEasSessionId().longValue());
                } else {
                    qryUnregister.setNull(1, Types.INTEGER);
                }
                qryUnregister.execute();
            } finally {
                getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
            }
        } catch (SQLException e) {
            throw new DatabaseError("Can't unregister the session: " + ExceptionTextFormatter.getExceptionMess(e), e);
        }
    }

    private final void registerSession() {
        try {
            if (qryRegister == null) {
                getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
                try {
                    qryRegister = getDbConnection().get().prepareStatement("begin RDX_Arte.registerSession(?,?,?,?,?,?,?,?,?); end;");
                } finally {
                    getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
                }
            }
            getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
            try {
                qryRegister.setString(1, transactionParams.getUserName());
                qryRegister.setString(2, transactionParams.getStationName());
                qryRegister.setString(3, clientLanguage.getValue());
                qryRegister.setString(4, clientCountry.getValue());
                if (transactionParams.getEasSessionId() != null) {
                    qryRegister.setLong(5, transactionParams.getEasSessionId().longValue());
                } else {
                    qryRegister.setNull(5, java.sql.Types.INTEGER);
                }
                qryRegister.setLong(6, transactionParams.getVersion().longValue());
                qryRegister.setLong(7, arteSocket.getSapId());
                qryRegister.setString(8, String.valueOf(arteSocket.getRemoteAddress()));
                qryRegister.setLong(9, arteSocket.getUnit() == null ? -1 : arteSocket.getUnit().getId());
                qryRegister.execute();
            } finally {
                getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
            }
        } catch (SQLException e) {
            throw new DatabaseError("Can't register the session: " + ExceptionTextFormatter.getExceptionMess(e), e);
        }
    }

    public MlsProcessor getMlsProcessor() {
        return new MlsProcessor() {
            @Override
            public EIsoLanguage getDefLanguage() {
                return getClientLanguage();
            }

            @Override
            public EEventSeverity getEventSeverityByCode(final String code) {
                return getDefManager().getEventSeverityByCode(code);
            }

            @Override
            public String getEventSourceByCode(final String code) {
                return getDefManager().getEventSourceByCode(code);
            }

            @Override
            public String getEventTitleByCode(final String code, final EIsoLanguage lang) {
                return getDefManager().getEventTitleByCode(code, lang);
            }
        };
    }

    public Instance getInstance() {
        return instance;
    }

    @Override
    public List<EIsoLanguage> getLanguages() {
        if (languages == null) {
            try {
                List<String> langsAsStr = defManager.releaseCache.getRelease().getRepository().getRevisionMeta().getLanguages();
                languages = new ArrayList<>(langsAsStr.size());
                for (String lang : langsAsStr) {
                    try {
                        languages.add(EIsoLanguage.getForValue(lang));
                    } catch (NoConstItemWithSuchValueError e) {//NOPMD
                    }
                }
            } catch (Throwable e) {
                languages = Collections.emptyList();
            }
        }
        return languages;
    }

    public static void setFallbackArteProvider(final IArteProvider provider) {
        FALLBACK_ARTE_PROVIDER = provider;
    }

    public static Arte get() {
        if (Thread.currentThread() instanceof IArteProvider) {
            return ((IArteProvider) Thread.currentThread()).getArte();
        }
        final IArteProvider fallbackSnapshot = FALLBACK_ARTE_PROVIDER;
        if (fallbackSnapshot != null) {
            return fallbackSnapshot.getArte();
        }
        return null;
    }
}
