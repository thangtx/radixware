
/* Radix::Arte::Arte - Server Executable*/

/*Radix::Arte::Arte-Server Dynamic Class*/

package org.radixware.ads.Arte.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte")
public published class Arte  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {

	private static final org.radixware.kernel.server.arte.Arte ARTE_INSTANCE_INTERNAL;

	static {
	    ARTE_INSTANCE_INTERNAL = (pdcArte______________________.class.getClassLoader() instanceof org.radixware.kernel.common.environment.IRadixClassLoader) ? (org.radixware.kernel.server.arte.Arte) ((org.radixware.kernel.common.environment.IRadixClassLoader) pdcArte______________________.class.getClassLoader()).getEnvironment() : null;
	}

	public static org.radixware.kernel.server.arte.Arte __getArteInstanceInternal() {
	    return ARTE_INSTANCE_INTERNAL;
	}

	//invokeInternalService
	@Deprecated
	public static Xml mthAEWBPU7ZXTNRDISQAAAAAAAAAA(
	        Xml rq,
	        java.lang.Class<?> resultClass,
	        Str service,
	        int keepConnectTime,
	        int timeout)
	        throws Exceptions::InterruptedException, Exceptions::ServiceCallException,
	        Exceptions::ServiceCallFault, Exceptions::ServiceCallTimeout {
	    return invokeInternalService(rq, resultClass, service, keepConnectTime, timeout, AadcMember:ANY);
	}

	@Deprecated
	public static Xml mth7PCOMY6NBJEHVJE2WHBZVJJT3Q(
	        Xml rq,
	        java.lang.Class<?> resultClass,
	        Str service,
	        int keepConnectTime,
	        int receiveTimeout,
	        int connectTimeout)
	        throws Exceptions::InterruptedException, Exceptions::ServiceCallException,
	        Exceptions::ServiceCallFault, Exceptions::ServiceCallTimeout {
	    return invokeInternalService(rq, resultClass, service, keepConnectTime, receiveTimeout, connectTimeout, AadcMember:ANY);
	}

	//invokeService
	@Deprecated
	public static Xml mthWJE7EQFPP5AU7I2X5QTHQLGU5E(Xml rq,
	        java.lang.Class<?> resultClass,
	        Int systemId,
	        Str service,
	        Str scpName,
	        int keepConnectTime,
	        int timeout)
	        throws Exceptions::InterruptedException, Exceptions::ServiceCallException,
	        Exceptions::ServiceCallFault, Exceptions::ServiceCallTimeout {
	    return invokeService(rq, resultClass, systemId, service, scpName, keepConnectTime, timeout, AadcMember:ANY);
	}

	@Deprecated
	public static Xml mthCUDB2SCLUNEAFFUK77ANDMJFWI(
	        Xml rq,
	        java.util.Map<Str, Str> soapRequestParams,
	        java.lang.Class<?> resultClass,
	        Int systemId,
	        Str service,
	        Str scpName,
	        int keepConnectTime,
	        int timeout)
	        throws Exceptions::InterruptedException, Exceptions::ServiceCallException,
	        Exceptions::ServiceCallFault, Exceptions::ServiceCallTimeout {
	    return invokeService(rq, soapRequestParams, resultClass, systemId, service, scpName, keepConnectTime, timeout, AadcMember:ANY);
	}

	@Deprecated
	public static Xml mthB42AEKKYRFBSJG3IIG3D4CVTWE(
	        Xml rq,
	        java.util.Map<Str, Str> soapRequestParams,
	        java.lang.Class<?> resultClass,
	        Int systemId,
	        Str service,
	        Str scpName,
	        java.util.List<org.radixware.kernel.common.sc.SapClientOptions> additionalSaps,
	        int keepConnectTime,
	        int timeout)
	        throws Exceptions::InterruptedException, Exceptions::ServiceCallException,
	        Exceptions::ServiceCallFault, Exceptions::ServiceCallTimeout {
	    return invokeService(rq, soapRequestParams, resultClass, systemId, service, scpName, additionalSaps, keepConnectTime, timeout, AadcMember:ANY);
	}

	@Deprecated
	public static Xml mthAC6QWAIA6JGSHOA4U4FCPNXFME(
	        Xml rq,
	        java.util.Map<Str, Str> soapRequestParams,
	        java.lang.Class<?> resultClass,
	        Int systemId,
	        Str service,
	        Str scpName,
	        java.util.List<org.radixware.kernel.common.sc.SapClientOptions> additionalSaps,
	        int keepConnectTime,
	        int receiveTimeout,
	        int connectTimeout)
	        throws Exceptions::InterruptedException, Exceptions::ServiceCallException,
	        Exceptions::ServiceCallFault, Exceptions::ServiceCallTimeout {
	    return invokeService(rq, soapRequestParams, resultClass, systemId, service, scpName, additionalSaps, keepConnectTime, receiveTimeout, connectTimeout, AadcMember:ANY);
	}

	@Deprecated
	public static Xml mth6B35BO32E5AAFEAAT4PKR7AODM(
	        Xml rq,
	        java.lang.Class<?> resultClass,
	        Int systemId,
	        Str service,
	        Str scpName,
	        int keepConnectTime,
	        int receiveTimeout,
	        int connectTimeout)
	        throws Exceptions::InterruptedException, Exceptions::ServiceCallException,
	        Exceptions::ServiceCallFault, Exceptions::ServiceCallTimeout {
	    return invokeService(rq, resultClass, systemId, service, scpName, keepConnectTime, receiveTimeout, connectTimeout, AadcMember:ANY);
	}

	@Deprecated
	public static Xml mth6JCSLQLZDJDFJFWHWO2WEQLWJ4(
	        System::RadixSoapMessage soapMess,
	        Str scpName)
	        throws Exceptions::InterruptedException, Exceptions::ServiceCallException,
	        Exceptions::ServiceCallFault, Exceptions::ServiceCallTimeout {
	    return invokeService(soapMess, scpName, AadcMember:ANY);
	}
	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return Arte_mi.rdxMeta;}

	/*Radix::Arte::Arte:Nested classes-Nested Classes*/

	/*Radix::Arte::Arte:Properties-Properties*/

	/*Radix::Arte::Arte:stSysdate-Dynamic Property*/



	protected static org.radixware.ads.Types.server.SqlStatement stSysdate=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:stSysdate")
	private static final  org.radixware.ads.Types.server.SqlStatement getStSysdate() {

		if(internal[stSysdate]==null)
		  internal[stSysdate] = Types::SqlStatement.prepare("Select sysdate from Dual", false);
		return internal[stSysdate];
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:stSysdate")
	private static final   void setStSysdate(org.radixware.ads.Types.server.SqlStatement val) {
		stSysdate = val;
	}

	/*Radix::Arte::Arte:timeZone-Dynamic Property*/



	protected static java.util.TimeZone timeZone=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:timeZone")
	private static final  java.util.TimeZone getTimeZone() {
		return timeZone;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:timeZone")
	private static final   void setTimeZone(java.util.TimeZone val) {
		timeZone = val;
	}

	/*Radix::Arte::Arte:stSystimestamp-Dynamic Property*/



	protected static org.radixware.ads.Types.server.SqlStatement stSystimestamp=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:stSystimestamp")
	private static final  org.radixware.ads.Types.server.SqlStatement getStSystimestamp() {

		if(internal[stSystimestamp]==null)
		  internal[stSystimestamp] = Types::SqlStatement.prepare("Select systimestamp from Dual", false);
		return internal[stSystimestamp];
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:stSystimestamp")
	private static final   void setStSystimestamp(org.radixware.ads.Types.server.SqlStatement val) {
		stSystimestamp = val;
	}

	/*Radix::Arte::Arte:userCache-Dynamic Property*/



	protected static org.radixware.ads.Arte.server.UserCache userCache=new UserCache();











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:userCache")
	private static final  org.radixware.ads.Arte.server.UserCache getUserCache() {
		return userCache;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:userCache")
	private static final   void setUserCache(org.radixware.ads.Arte.server.UserCache val) {
		userCache = val;
	}

	/*Radix::Arte::Arte:INITIALIZED-Dynamic Property*/



	protected static boolean INITIALIZED=false;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:INITIALIZED")
	private static final  boolean getINITIALIZED() {
		return INITIALIZED;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:INITIALIZED")
	private static final   void setINITIALIZED(boolean val) {
		INITIALIZED = val;
	}



























































	/*Radix::Arte::Arte:Methods-Methods*/

	/*Radix::Arte::Arte:getVersion-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:getVersion")
	public static published  Int getVersion () {
		return getInstance().getVersion();

	}

	/*Radix::Arte::Arte:isEasRequestProcessing-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:isEasRequestProcessing")
	public static published  boolean isEasRequestProcessing () {
		Xml rq=getInstance().getCurrentProcessedServiceRq();
		if (rq == null || rq.schemaType() == null || rq.schemaType().getName() == null)
		   return false;
		return rq.schemaType().getName().getNamespaceURI()==EasXsd:Request.type.getName().getNamespaceURI();




	}

	/*Radix::Arte::Arte:invokeByClassId-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:invokeByClassId")
	public static published  java.lang.Object invokeByClassId (org.radixware.kernel.common.types.Id classId, Str methodName, java.lang.Class<?>[] paramTypes, java.lang.Object[] paramVals) throws java.lang.Exception {
		return getInstance().invokeByClassId( classId, methodName, paramTypes, paramVals );
	}

	/*Radix::Arte::Arte:getCurrentGreenwichTime-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:getCurrentGreenwichTime")
	@Deprecated
	public static published  java.sql.Timestamp getCurrentGreenwichTime () {
		long curr=System.currentTimeMillis();
		return new DateTime(curr - Utils::Timing.getTimeOffset(curr));

	}

	/*Radix::Arte::Arte:getCurrentMillis-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:getCurrentMillis")
	public static published  long getCurrentMillis () {
		return System.currentTimeMillis();
	}

	/*Radix::Arte::Arte:getClassById-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:getClassById")
	public static published  java.lang.Class<?> getClassById (org.radixware.kernel.common.types.Id id) {
		return getInstance().getDefManager().getClass(id);
	}

	/*Radix::Arte::Arte:enterCachingSession-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:enterCachingSession")
	public static published  Int enterCachingSession () {
		return getInstance().getCache().enterSection();
	}

	/*Radix::Arte::Arte:checkBreak-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:checkBreak")
	public static published  void checkBreak () throws org.radixware.kernel.server.exceptions.ServiceProcessBreakException {
		getInstance().checkBreak();
	}

	/*Radix::Arte::Arte:getCurrentLocalDate-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:getCurrentLocalDate")
	public static published  java.sql.Timestamp getCurrentLocalDate () {
		return Utils::Timing.truncTimeToDay(new DateTime(System.currentTimeMillis()));

	}

	/*Radix::Arte::Arte:getTimeOffset-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:getTimeOffset")
	@Deprecated
	public static published  int getTimeOffset (long localMillis) {
		if (timeZone==null)
		   timeZone=java.util.TimeZone.getDefault();
		return timeZone.getOffset(localMillis);

	}

	/*Radix::Arte::Arte:getCurrentTime-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:getCurrentTime")
	public static published  java.sql.Timestamp getCurrentTime () {
		return new DateTime( System.currentTimeMillis() );
	}

	/*Radix::Arte::Arte:getUserName-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:getUserName")
	public static published  Str getUserName () {
		return getInstance().getUserName();
	}

	/*Radix::Arte::Arte:changeClientLanguage-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:changeClientLanguage")
	public static published  org.radixware.kernel.common.enums.EIsoLanguage changeClientLanguage (org.radixware.kernel.common.enums.EIsoLanguage newLang) {
		Common::Language prevLang = getInstance().getClientLanguage();
		getInstance().setClientLanguage(newLang);
		return prevLang;
	}

	/*Radix::Arte::Arte:newObject-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:newObject")
	public static published  java.lang.Object newObject (org.radixware.kernel.common.types.Id classId) {
		return getInstance().newObject( classId );
	}

	/*Radix::Arte::Arte:sleep-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:sleep")
	public static published  void sleep (long millis) {
		try
		{
		   java.lang.Thread.sleep( millis );
		}
		catch( Throwable e )   
		{
		   throw new AppError( "Thread sleep interrupted:\n" + e.getMessage() );
		}
	}

	/*Radix::Arte::Arte:radixEncoding2Msdl-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:radixEncoding2Msdl")
	public static published  Str radixEncoding2Msdl (org.radixware.ads.Common.common.Encoding encoding) {
		//return null if appropriate encoding not found

		if(encoding==null)
		   return null;
		switch(encoding) {
		   case Common::Encoding:Cp866 : return  Meta::MsdlXsd:EncodingDef.CP_866.toString();  
		   case Common::Encoding:Cp1251: return  Meta::MsdlXsd:EncodingDef.CP_1251.toString(); 
		   case Common::Encoding:UTF8 :  return  Meta::MsdlXsd:EncodingDef.UTF_8.toString();  
		   case Common::Encoding:Ascii:  return  Meta::MsdlXsd:EncodingDef.ASCII.toString();   
		   case Common::Encoding:Ebcdic_Cp1047:  return  Meta::MsdlXsd:EncodingDef.EBCDIC_CP_1047.toString();
		   case Common::Encoding:Ebcdic_IBM500:  return  Meta::MsdlXsd:EncodingDef.EBCDIC.toString();
		   case Common::Encoding:DefaultCp: return  Meta::MsdlXsd:EncodingDef.DEFAULT_CP.toString();   
		   default: return null;
		   }   

	}

	/*Radix::Arte::Arte:getCurrentGreenwichTimeMillis-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:getCurrentGreenwichTimeMillis")
	@Deprecated
	public static published  long getCurrentGreenwichTimeMillis () {
		long curr=System.currentTimeMillis();
		return curr - Utils::Timing.getTimeOffset(curr);

	}

	/*Radix::Arte::Arte:getStationName-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:getStationName")
	public static published  Str getStationName () {
		return getInstance().getStationName();
	}

	/*Radix::Arte::Arte:needBreak-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:needBreak")
	public static published  boolean needBreak () {
		return getInstance().needBreak();
	}

	/*Radix::Arte::Arte:getInstance-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:getInstance")
	public static published  org.radixware.kernel.server.arte.Arte getInstance () {
		return __getArteInstanceInternal();
	}

	/*Radix::Arte::Arte:getClientLanguage-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:getClientLanguage")
	public static published  org.radixware.kernel.common.enums.EIsoLanguage getClientLanguage () {
		return getInstance().getClientLanguage();
	}

	/*Radix::Arte::Arte:leaveCachingSession-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:leaveCachingSession")
	public static published  void leaveCachingSession (Int sessionId) {
		getInstance().getCache().leaveSection(sessionId);
	}

	/*Radix::Arte::Arte:generateGuid-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:generateGuid")
	public static published  Str generateGuid () {
		return org.radixware.kernel.common.utils.Guid.generateGuid();

	}

	/*Radix::Arte::Arte:getEntityObject-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:getEntityObject")
	public static published  org.radixware.ads.Types.server.Entity getEntityObject (org.radixware.kernel.server.types.Pid pid) {
		return ( Types::Entity )getInstance().getEntityObject( pid );
	}

	/*Radix::Arte::Arte:getDefManager-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:getDefManager")
	public static published  org.radixware.kernel.server.arte.DefManager getDefManager () {
		return getInstance().DefManager;

	}

	/*Radix::Arte::Arte:valAsStr2ObjVal-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:valAsStr2ObjVal")
	public static published  java.lang.Object valAsStr2ObjVal (Str valAsStr, org.radixware.kernel.common.enums.EValType valType) {
		return org.radixware.kernel.server.utils.SrvValAsStr.fromStr(Arte.getInstance(), valAsStr, valType);

	}

	/*Radix::Arte::Arte:objVal2valAsStr-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:objVal2valAsStr")
	public static published  Str objVal2valAsStr (java.lang.Object valAsObject, org.radixware.kernel.common.enums.EValType valType) {
		return org.radixware.kernel.server.utils.SrvValAsStr.toStr(Arte.getInstance(),valAsObject, valType);

	}

	/*Radix::Arte::Arte:translateSqml-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:translateSqml")
	public static published  org.radixware.kernel.server.types.SqmlTranslateResult translateSqml (Str sqml, org.radixware.kernel.common.types.Id tableId, org.radixware.kernel.common.types.Id entityClassId, Str tableAlias) {
		return org.radixware.kernel.server.dbq.SqmlTranslator.translate(getInstance(), sqml, tableId, entityClassId, tableAlias);
	}

	/*Radix::Arte::Arte:translateSqml-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:translateSqml")
	public static published  org.radixware.kernel.server.types.SqmlTranslateResult translateSqml (Str sqml, org.radixware.kernel.common.defs.dds.DdsTableDef table, org.radixware.kernel.common.types.Id entityClassId, Str tableAlias) {
		return org.radixware.kernel.server.dbq.SqmlTranslator.translate(getInstance(), sqml, table, entityClassId, tableAlias);
	}

	/*Radix::Arte::Arte:translateSqml-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:translateSqml")
	public static published  org.radixware.kernel.server.types.SqmlTranslateResult translateSqml (org.radixware.schemas.xscml.Sqml sqml, org.radixware.kernel.common.types.Id tableId, org.radixware.kernel.common.types.Id entityClassId, Str tableAlias) {
		return org.radixware.kernel.server.dbq.SqmlTranslator.translate(getInstance(), sqml, tableId, entityClassId, tableAlias);
	}

	/*Radix::Arte::Arte:translateSqml-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:translateSqml")
	public static published  org.radixware.kernel.server.types.SqmlTranslateResult translateSqml (org.radixware.schemas.xscml.Sqml sqml, org.radixware.kernel.common.defs.dds.DdsTableDef table, org.radixware.kernel.common.types.Id entityClassId, Str tableAlias) {
		return org.radixware.kernel.server.dbq.SqmlTranslator.translate(getInstance(), sqml, table, entityClassId, tableAlias);
	}

	/*Radix::Arte::Arte:rollback-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:rollback")
	public static published  void rollback () {
		getInstance().rollback();
	}

	/*Radix::Arte::Arte:rollbackToSavepoint-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:rollbackToSavepoint")
	public static published  void rollbackToSavepoint (Str id) {
		getInstance().rollbackToSavepoint(id);
	}

	/*Radix::Arte::Arte:sendBatches-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:sendBatches")
	public static published  void sendBatches () {
		getInstance().getDefManager().getDbQueryBuilder().sendBatches(true);

	}

	/*Radix::Arte::Arte:setCreatesBatchSize-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:setCreatesBatchSize")
	public static published  void setCreatesBatchSize (org.radixware.kernel.common.types.Id tableId, int batchSize) {
		getInstance().getDefManager().getDbQueryBuilder().setCreatesBatchSize(tableId, batchSize);

	}

	/*Radix::Arte::Arte:setSavepoint-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:setSavepoint")
	public static published  Str setSavepoint () {
		return getInstance().setSavepoint();
	}

	/*Radix::Arte::Arte:setUpdatesBatchSize-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:setUpdatesBatchSize")
	public static published  void setUpdatesBatchSize (org.radixware.kernel.common.types.Id tableId, int batchSize) {
		getInstance().getDefManager().getDbQueryBuilder().setUpdatesBatchSize(tableId, batchSize);

	}

	/*Radix::Arte::Arte:updateDatabase-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:updateDatabase")
	public static published  void updateDatabase () {
		getInstance().updateDatabase(null);
	}

	/*Radix::Arte::Arte:commit-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:commit")
	public static published  void commit () {
		getInstance().commit();
	}

	/*Radix::Arte::Arte:createTemporaryBlob-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:createTemporaryBlob")
	public static published  java.sql.Blob createTemporaryBlob () {
		try {
		   return getInstance().getDbConnection().createTemporaryBlob();  
		   }
		catch(java.sql.SQLException e) {
		   throw new DatabaseError("Can't create temporary Blob: "+e.getMessage(),e);
		   }   
	}

	/*Radix::Arte::Arte:createTemporaryClob-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:createTemporaryClob")
	public static published  java.sql.Clob createTemporaryClob () {
		try {
		   return getInstance().getDbConnection().createTemporaryClob();
		   }
		catch(java.sql.SQLException e) {
		   throw new DatabaseError("Can't create temporary Clob: "+e.getMessage(),e);
		   }   
	}

	/*Radix::Arte::Arte:getDbConnection-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:getDbConnection")
	public static published  java.sql.Connection getDbConnection () {
		return getInstance().getDbConnection().get();
	}

	/*Radix::Arte::Arte:invokeByClassName-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:invokeByClassName")
	public static published  java.lang.Object invokeByClassName (Str className, Str methodName, java.lang.Class<?>[] paramTypes, java.lang.Object[] paramVals) throws java.lang.Exception {
		return getInstance().invokeByClassName( className, methodName, paramTypes, paramVals );
	}

	/*Radix::Arte::Arte:enterTimingSection-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:enterTimingSection")
	public static published  void enterTimingSection (org.radixware.ads.Profiler.common.TimingSection timingSectionId) {
		getInstance().Profiler.enterTimingSection(timingSectionId.Value);
	}

	/*Radix::Arte::Arte:leaveTimingSection-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:leaveTimingSection")
	public static published  void leaveTimingSection (org.radixware.ads.Profiler.common.TimingSection timingSectionId) {
		getInstance().Profiler.leaveTimingSection(timingSectionId.Value);
	}

	/*Radix::Arte::Arte:enterTimingSection-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:enterTimingSection")
	public static published  void enterTimingSection (org.radixware.ads.Profiler.common.TimingSection timingSectionId, Str path) {
		getInstance().Profiler.enterTimingSection(timingSectionId.Value+":"+path);
	}

	/*Radix::Arte::Arte:leaveTimingSection-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:leaveTimingSection")
	public static published  void leaveTimingSection (org.radixware.ads.Profiler.common.TimingSection timingSectionId, Str path) {
		getInstance().Profiler.leaveTimingSection(timingSectionId.Value+":"+path);
	}

	/*Radix::Arte::Arte:getDbDataSource-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:getDbDataSource")
	public  javax.sql.DataSource getDbDataSource () {
		return getInstance().getInstance().getDataSource();
	}

	/*Radix::Arte::Arte:getAppParams-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:getAppParams")
	public static published  java.util.Map<Str,Str> getAppParams () {
		return getInstance().AppParams;
	}

	/*Radix::Arte::Arte:getClientEnvironment-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:getClientEnvironment")
	public static published  org.radixware.kernel.common.enums.ERuntimeEnvironmentType getClientEnvironment () {
		return getInstance().getClientEnvironment();
	}

	/*Radix::Arte::Arte:getClientCountry-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:getClientCountry")
	public static published  org.radixware.kernel.common.enums.EIsoCountry getClientCountry () {
		return getInstance().getClientCountry();
	}

	/*Radix::Arte::Arte:changeClientCountry-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:changeClientCountry")
	public static published  org.radixware.kernel.common.enums.EIsoCountry changeClientCountry (org.radixware.kernel.common.enums.EIsoCountry newCountry) {
		final Common::Country prevCountry = getInstance().getClientCountry();
		getInstance().setClientCountry(newCountry);
		return prevCountry;

	}

	/*Radix::Arte::Arte:getClientLocale-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:getClientLocale")
	public static published  java.util.Locale getClientLocale () {
		return getInstance().getClientLocale();
	}

	/*Radix::Arte::Arte:changeClientLocale-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:changeClientLocale")
	public static published  java.util.Locale changeClientLocale (java.util.Locale newLocale) {
		final java.util.Locale prevLocale = getInstance().getClientLocale();
		getInstance().setClientLocale(newLocale);
		return prevLocale;
	}

	/*Radix::Arte::Arte:createLocalTracer-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:createLocalTracer")
	public static published  org.radixware.kernel.common.trace.LocalTracer createLocalTracer (org.radixware.ads.Arte.common.EventSource eventSource) {
		return getInstance().createLocalTracer(eventSource.Value);
	}

	/*Radix::Arte::Arte:getAllLayerVersionsAsString-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:getAllLayerVersionsAsString")
	public static published  Str getAllLayerVersionsAsString () {
		return org.radixware.kernel.starter.radixloader.RadixClassLoader.getRevisionMeta(getInstance()).LayerVersionsString;
	}

	/*Radix::Arte::Arte:getUserCache-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:getUserCache")
	public static published  org.radixware.ads.Arte.server.UserCache getUserCache () {
		return userCache;
	}

	/*Radix::Arte::Arte:markActive-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:markActive")
	public published  void markActive () {
		getInstance().markActive();
	}

	/*Radix::Arte::Arte:markInactive-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:markInactive")
	public published  void markInactive () {
		getInstance().markInactive();
	}

	/*Radix::Arte::Arte:getScpName-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:getScpName")
	public published  Str getScpName () {
		return getInstance().getInstance().getScpName();
	}

	/*Radix::Arte::Arte:onStart-User Method*/

	@org.radixware.kernel.common.lang.ReflectiveCallable
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:onStart")
	public static  void onStart () {
		if (INITIALIZED) {
		    return;
		}

		int classesLoaded = 0;
		int classesFailed = 0;
		try {
		    final System::Instance instance = System::Instance.loadByPK(Arte.getInstance().Instance.Id, true);
		    if (instance.classLoadingProfile != null) {
		        final GetClassLoadingProfileItemsCur cur = GetClassLoadingProfileItemsCur.open(instance.classLoadingProfileId);
		        try {
		            while (cur.next()) {
		                try {
		                    java.lang.Class.forName(cur.className, true, Arte.getDefManager().getReleaseCache().getPrivateClassloader());
		                    classesLoaded++;
		                } catch (Exception ex) {
		                    classesFailed++;
		                    Trace.debug("Error while loading class '" + cur.className + "'from class loading profile: " + Utils::ExceptionTextFormatter.exceptionStackToString(ex), EventSource:Arte);
		                }
		            }
		        } finally {
		            cur.close();
		        }
		        Trace.debug("Class loading profile '" + instance.classLoadingProfile.calcTitle() +  "' has been applied, classes loaded: " + classesLoaded + ", errors: " + classesFailed, EventSource:Arte);
		        if (classesFailed > 0) {
		            Trace.warning(String.format("During applying class loading profile '%s' %s errors occurred", instance.classLoadingProfile.calcTitle(), classesFailed), EventSource:Arte);
		        }
		    }
		} catch (Throwable ex) {
		    Trace.warning("Error applying the class loading profile: " + Utils::ExceptionTextFormatter.exceptionStackToString(ex), EventSource:Arte);
		}


		try {
		    System::System system = System::System.loadByPK(1l, true);
		    if (system.onStartArteUserFunc != null) {
		        system.onStartArteUserFunc.onStartArte();
		    }
		} catch (Throwable ex) {
		    Trace.warning("Exception in ARTE initialization UDF: " + Utils::ExceptionTextFormatter.exceptionStackToString(ex), EventSource:Arte);
		}

		INITIALIZED = true;
	}

	/*Radix::Arte::Arte:invokeService-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:invokeService")
	public static published  org.apache.xmlbeans.XmlObject invokeService (org.apache.xmlbeans.XmlObject rq, java.lang.Class<?> resultClass, Int systemId, Str service, Str scpName, int keepConnectTime, int timeout, org.radixware.kernel.common.enums.EAadcMember aadcMember) throws java.lang.InterruptedException,org.radixware.kernel.common.exceptions.ServiceCallException,org.radixware.kernel.common.exceptions.ServiceCallFault,org.radixware.kernel.common.exceptions.ServiceCallTimeout {
		return getInstance().getArteSocket().invokeService( rq, resultClass, Utils::Nvl.get(systemId,1), service, scpName, keepConnectTime, timeout, aadcMember);

	}

	/*Radix::Arte::Arte:invokeService-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:invokeService")
	public static published  org.apache.xmlbeans.XmlObject invokeService (org.apache.xmlbeans.XmlObject rq, java.util.Map<Str,Str> soapRequestParams, java.lang.Class<?> resultClass, Int systemId, Str service, Str scpName, int keepConnectTime, int timeout, org.radixware.kernel.common.enums.EAadcMember aadcMember) throws java.lang.InterruptedException,org.radixware.kernel.common.exceptions.ServiceCallException,org.radixware.kernel.common.exceptions.ServiceCallFault,org.radixware.kernel.common.exceptions.ServiceCallTimeout {
		return getInstance().getArteSocket().invokeService(rq, soapRequestParams, resultClass, Utils::Nvl.get(systemId,1), service, scpName, keepConnectTime, timeout, aadcMember);
	}

	/*Radix::Arte::Arte:invokeService-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:invokeService")
	public static published  org.apache.xmlbeans.XmlObject invokeService (org.apache.xmlbeans.XmlObject rq, java.util.Map<Str,Str> soapRequestParams, java.lang.Class<?> resultClass, Int systemId, Str service, Str scpName, java.util.List<org.radixware.kernel.common.sc.SapClientOptions> additionalSaps, int keepConnectTime, int timeout, org.radixware.kernel.common.enums.EAadcMember aadcMember) throws java.lang.InterruptedException,org.radixware.kernel.common.exceptions.ServiceCallException,org.radixware.kernel.common.exceptions.ServiceCallFault,org.radixware.kernel.common.exceptions.ServiceCallTimeout {
		return getInstance().getArteSocket().invokeService(rq, soapRequestParams, resultClass, Utils::Nvl.get(systemId,1), service, scpName, additionalSaps, keepConnectTime, timeout, aadcMember);
	}

	/*Radix::Arte::Arte:invokeService-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:invokeService")
	public static published  org.apache.xmlbeans.XmlObject invokeService (org.apache.xmlbeans.XmlObject rq, java.util.Map<Str,Str> soapRequestParams, java.lang.Class<?> resultClass, Int systemId, Str service, Str scpName, java.util.List<org.radixware.kernel.common.sc.SapClientOptions> additionalSaps, int keepConnectTime, int receiveTimeout, int connectTimeout, org.radixware.kernel.common.enums.EAadcMember aadcMember) throws java.lang.InterruptedException,org.radixware.kernel.common.exceptions.ServiceCallException,org.radixware.kernel.common.exceptions.ServiceCallFault,org.radixware.kernel.common.exceptions.ServiceCallTimeout {
		return getInstance().getArteSocket().invokeService(rq, soapRequestParams, resultClass, Utils::Nvl.get(systemId,1), service, scpName, additionalSaps, keepConnectTime, receiveTimeout, connectTimeout, aadcMember);
	}

	/*Radix::Arte::Arte:invokeService-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:invokeService")
	public static published  org.apache.xmlbeans.XmlObject invokeService (org.apache.xmlbeans.XmlObject rq, java.lang.Class<?> resultClass, Int systemId, Str service, Str scpName, int keepConnectTime, int receiveTimeout, int connectTimeout, org.radixware.kernel.common.enums.EAadcMember aadcMember) throws java.lang.InterruptedException,org.radixware.kernel.common.exceptions.ServiceCallException,org.radixware.kernel.common.exceptions.ServiceCallFault,org.radixware.kernel.common.exceptions.ServiceCallTimeout {
		return getInstance().getArteSocket().invokeService(rq, null, resultClass, Utils::Nvl.get(systemId,1), service, scpName, null, keepConnectTime, receiveTimeout, connectTimeout, aadcMember);

	}

	/*Radix::Arte::Arte:invokeInternalService-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:invokeInternalService")
	public static published  org.apache.xmlbeans.XmlObject invokeInternalService (org.apache.xmlbeans.XmlObject rq, java.lang.Class<?> resultClass, Str service, int keepConnectTime, int timeout, org.radixware.kernel.common.enums.EAadcMember aadcMember) throws java.lang.InterruptedException,org.radixware.kernel.common.exceptions.ServiceCallException,org.radixware.kernel.common.exceptions.ServiceCallFault,org.radixware.kernel.common.exceptions.ServiceCallTimeout {
		return getInstance().getArteSocket().invokeInternalService(rq, resultClass, service, keepConnectTime, timeout, aadcMember);

	}

	/*Radix::Arte::Arte:invokeInternalService-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:invokeInternalService")
	public static published  org.apache.xmlbeans.XmlObject invokeInternalService (org.apache.xmlbeans.XmlObject rq, java.lang.Class<?> resultClass, Str service, int keepConnectTime, int receiveTimeout, int connectTimeout, org.radixware.kernel.common.enums.EAadcMember aadcMember) throws java.lang.InterruptedException,org.radixware.kernel.common.exceptions.ServiceCallException,org.radixware.kernel.common.exceptions.ServiceCallFault,org.radixware.kernel.common.exceptions.ServiceCallTimeout {
		return getInstance().getArteSocket().invokeInternalService(rq, resultClass, service, keepConnectTime, receiveTimeout, connectTimeout, aadcMember);

	}

	/*Radix::Arte::Arte:unlock-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:unlock")
	public static published  Int unlock (Str lockName) {
		ReleaseSessionLockProc proc = ReleaseSessionLockProc.execute(lockName, "");
		return proc.oResult;
	}

	/*Radix::Arte::Arte:getSessionLockOwnerInfo-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:getSessionLockOwnerInfo")
	public static published  org.radixware.ads.Arte.server.DbLockOwnerInfo getSessionLockOwnerInfo (Str entityId, Str pid) {
		if (entityId == null || pid == null) {
		    return null;
		}

		final GetSessionLockOwnerProc proc = GetSessionLockOwnerProc.execute(entityId, pid);
		if (proc.oSid != null) {
		    return new DbLockOwnerInfo(proc.oSid, proc.oModule, proc.oAction);
		} else {
		    return null;
		}
	}

	/*Radix::Arte::Arte:lock-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:lock")
	public static published  Int lock (Str lockName) {
		RequestSessionLockProc proc = RequestSessionLockProc.execute(lockName, "", null);
		return proc.oResult;
	}

	/*Radix::Arte::Arte:getServiceSaps-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:getServiceSaps")
	public static published  java.util.List<org.radixware.kernel.common.sc.SapClientOptions> getServiceSaps (Int systemId, Str serviceUri, Str scpName, org.radixware.kernel.common.enums.EAadcMember targetAadcMember) throws org.radixware.kernel.common.exceptions.ServiceCallException {
		final java.util.List<org.radixware.kernel.common.sc.SapClientOptions> list = getInstance().getArteSocket().getServiceSaps(systemId, serviceUri, scpName);
		if (targetAadcMember == null || targetAadcMember == AadcMember:ANY) {
		    return list;
		}

		final java.util.List<org.radixware.kernel.common.sc.SapClientOptions> result = new java.util.ArrayList<>();
		for (org.radixware.kernel.common.sc.SapClientOptions sco : list) {
		    if (java.util.Objects.equals(sco.getAadcMemberId(), targetAadcMember.Value.intValue())) {
		        result.add(sco);
		    }
		}

		return result;

	}

	/*Radix::Arte::Arte:getDatabaseSysDate-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:getDatabaseSysDate")
	public static published  java.sql.Timestamp getDatabaseSysDate () {
		Types::SqlResultSet rs = stSysdate.executeQuery();
		try
		{  rs.next();  return rs.getDateTime(1); }
		finally
		{  rs.close(); }

	}

	/*Radix::Arte::Arte:getDatabaseSysTimestamp-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:getDatabaseSysTimestamp")
	public static published  java.sql.Timestamp getDatabaseSysTimestamp () {
		Types::SqlResultSet rs = stSystimestamp.executeQuery();
		try {
		    rs.next();
		    return rs.getDateTime(1);
		} finally {
		    rs.close();
		}

	}

	/*Radix::Arte::Arte:canHandleAasRequestsForScp-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:canHandleAasRequestsForScp")
	public static published  boolean canHandleAasRequestsForScp (Str scpName) {
		return canHandleArteServiceRequestsForScp(ArteXmlSchemes:AasWsdl.Value, scpName);
	}

	/*Radix::Arte::Arte:canHandleArteServiceRequestsForScp-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:canHandleArteServiceRequestsForScp")
	public static published  boolean canHandleArteServiceRequestsForScp (Str serviceUri, Str scpName) {
		if (serviceUri == null) {
		    return false;
		}
		if (scpName == null) {
		    return true;
		}
		final System::GetScpsForArteServiceInInstanceCursor cur = System::GetScpsForArteServiceInInstanceCursor.open(serviceUri, Arte.getInstance().Instance.getId());
		try {
		    while (cur.next()) {
		        if (java.util.Objects.equals(scpName, cur.scpName)) {
		            return true;
		        }
		    }
		    return false;
		} finally {
		    cur.close();
		}
	}

	/*Radix::Arte::Arte:getDbSid-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:getDbSid")
	public published  Int getDbSid () {
		return getInstance().getDbSid();
	}

	/*Radix::Arte::Arte:getDbSerial-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:getDbSerial")
	public  Int getDbSerial () {
		return getInstance().getDbSerial();
	}

	/*Radix::Arte::Arte:freeTemporaryBlob-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:freeTemporaryBlob")
	public static published  void freeTemporaryBlob (java.sql.Blob blob) {
		getInstance().DbConnection.freeTemporaryBlob(blob);
	}

	/*Radix::Arte::Arte:freeTemporaryClob-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:freeTemporaryClob")
	public static published  void freeTemporaryClob (java.sql.Clob clob) {
		getInstance().DbConnection.freeTemporaryClob(clob);
	}

	/*Radix::Arte::Arte:freeTemporaryBlobs-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:freeTemporaryBlobs")
	public static published  void freeTemporaryBlobs (java.util.Collection<java.sql.Blob> blobs) {
		getInstance().DbConnection.freeTemporaryBlobs(blobs);
	}

	/*Radix::Arte::Arte:freeTemporaryClobs-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:freeTemporaryClobs")
	public static published  void freeTemporaryClobs (java.util.Collection<java.sql.Clob> clobs) {
		getInstance().DbConnection.freeTemporaryClobs(clobs);
	}

	/*Radix::Arte::Arte:isEasSessionKeyAccessible-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:isEasSessionKeyAccessible")
	public static published  boolean isEasSessionKeyAccessible () {
		return getInstance().isEasSessionKeyAccessible();
	}

	/*Radix::Arte::Arte:encryptByEasSessionKey-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:encryptByEasSessionKey")
	public static published  byte[] encryptByEasSessionKey (byte[] data) {
		return getInstance().encryptByEasSessionKey(data);
	}

	/*Radix::Arte::Arte:decryptByEasSessionKey-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:decryptByEasSessionKey")
	public static published  byte[] decryptByEasSessionKey (byte[] encryptedData) {
		return getInstance().decryptByEasSessionKey(encryptedData);
	}

	/*Radix::Arte::Arte:invokeService-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:invokeService")
	public static published  org.apache.xmlbeans.XmlObject invokeService (org.radixware.kernel.common.soap.RadixSoapMessage soapMess, Str scpName, org.radixware.kernel.common.enums.EAadcMember aadcMember) throws java.lang.InterruptedException,org.radixware.kernel.common.exceptions.ServiceCallException,org.radixware.kernel.common.exceptions.ServiceCallFault,org.radixware.kernel.common.exceptions.ServiceCallTimeout {
		return getInstance().getArteSocket().invokeService(soapMess, scpName, aadcMember);
	}

	/*Radix::Arte::Arte:getSavepointId-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:getSavepointId")
	public static published  Str getSavepointId () {
		return getInstance().getSavePointId();
	}

	/*Radix::Arte::Arte:getSavepointNesting-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:getSavepointNesting")
	public static published  long getSavepointNesting () {
		return getInstance().getSavePointsNesting();

	}

	/*Radix::Arte::Arte:getSavepointNesting-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:getSavepointNesting")
	public static published  long getSavepointNesting (Str id) {
		return getInstance().getSavePointsNesting(id);

	}

	/*Radix::Arte::Arte:registerArteEventListener-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:registerArteEventListener")
	public static published  void registerArteEventListener (org.radixware.kernel.server.arte.IArteEventListener listener) {
		getInstance().getDefManager().getReleaseCache().registerArteEventListener(listener);
	}

	/*Radix::Arte::Arte:unregisterArteEventListener-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:unregisterArteEventListener")
	public static published  void unregisterArteEventListener (org.radixware.kernel.server.arte.IArteEventListener listener) {
		getInstance().getDefManager().getReleaseCache().unregisterArteEventListener(listener);
	}

	/*Radix::Arte::Arte:parseLayerVersions-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:parseLayerVersions")
	public static published  java.util.Map<Str,org.radixware.kernel.common.utils.VersionNumber> parseLayerVersions (Str versionsString) {
		return org.radixware.kernel.common.utils.Utils.parseVersionsByKey(versionsString);
	}

	/*Radix::Arte::Arte:getAllLayerVersions-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:getAllLayerVersions")
	public static published  java.util.Map<Str,org.radixware.kernel.common.utils.VersionNumber> getAllLayerVersions () {
		return parseLayerVersions(getAllLayerVersionsAsString());
	}

	/*Radix::Arte::Arte:getEntityObject-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:getEntityObject")
	public static published  org.radixware.ads.Types.server.Entity getEntityObject (org.radixware.kernel.server.types.Pid pid, Str classGuid) {
		return (Types::Entity) getInstance().getEntityObject(pid, classGuid);
	}

	/*Radix::Arte::Arte:setExpectedCacheSize-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:setExpectedCacheSize")
	public static published  void setExpectedCacheSize (int size) {
		Arte.getInstance().Cache.setExpectedCacheSize(size);
	}

	/*Radix::Arte::Arte:createTemporaryBlob-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:createTemporaryBlob")
	public static published  java.sql.Blob createTemporaryBlob (byte[] data) {
		Blob blob = null;

		try {
		   blob = getInstance().getDbConnection().createTemporaryBlob();  
		} catch(Exceptions::SQLException e) {
		   throw new DatabaseError("Can't create temporary Blob: " + e.getMessage(), e);
		}

		try {
		   blob.setBytes(1, data);
		} catch(Exceptions::SQLException e) {
		   throw new DatabaseError("Can't set initial data to temporary Blob: " + e.getMessage(), e);
		}

		return blob;
	}

	/*Radix::Arte::Arte:createTemporaryClob-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::Arte:createTemporaryClob")
	public static published  java.sql.Clob createTemporaryClob (Str data) {
		Clob clob = null;

		try {
		   clob = getInstance().getDbConnection().createTemporaryClob();
		} catch(Exceptions::SQLException e) {
		   throw new DatabaseError("Can't create temporary Clob: " + e.getMessage(), e);
		}

		try {
		   clob.setString(1, data);
		} catch(Exceptions::SQLException e) {
		   throw new DatabaseError("Can't set initial data to temporary Clob: " + e.getMessage(), e);
		}

		return clob;
	}


}

/* Radix::Arte::Arte - Server Meta*/

/*Radix::Arte::Arte-Server Dynamic Class*/

package org.radixware.ads.Arte.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Arte_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("pdcArte______________________"),"Arte",null,

						org.radixware.kernel.common.enums.EClassType.DYNAMIC,
						null,
						null,
						null,

						/*Radix::Arte::Arte:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::Arte::Arte:stSysdate-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdELZG7NQQRZAVXNZ2UKELJUMY5Q"),"stSysdate",null,org.radixware.kernel.common.enums.EValType.USER_CLASS,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Arte::Arte:timeZone-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdPGYYQTFE6PNRDA2JAAMPGXSZKU"),"timeZone",null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Arte::Arte:stSystimestamp-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdKSRSYOI72RA3THS6KJBOQ2GOI4"),"stSystimestamp",null,org.radixware.kernel.common.enums.EValType.USER_CLASS,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Arte::Arte:userCache-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdEHTVFCFEWRGQFC25CPB27UVT7I"),"userCache",null,org.radixware.kernel.common.enums.EValType.USER_CLASS,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Arte::Arte:INITIALIZED-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFRUH4AKG5VFMBMRCG5XK5JJV7E"),"INITIALIZED",null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::Arte::Arte:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth2RRXTVLCXLNRDISQAAAAAAAAAA"),"getVersion",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.INT),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth3KOPVW4FS7PBDEWRABIFNQAABA"),"isEasRequestProcessing",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth3L3ZXJE233NRDCK7ABIFNQAABA"),"invokeByClassId",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("classId",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprSPQOZNLHPZCXDO6FSFK5JIJCEQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("methodName",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprJQV6S3A65JBTDIQX3AIIJBOXXA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("paramTypes",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprAV6SUC36LJGGVPV4OZZPAX2CYY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("paramVals",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprZUOGLRRZANHY5JZSNF3IZBGSRE"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth3L7WEOQM4HNRDOLZABIFNQAABA"),"getCurrentGreenwichTime",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.DATE_TIME),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth4G5FL3ND6PNRDA2JAAMPGXSZKU"),"getCurrentMillis",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth5ASSCJP3DXPBDFHQABIFNQAABA"),"getClassById",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("id",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprBOAUYQ3Z2VGZXOUQVYMXLHCLWU"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth5JI4ZZQGR3OBDCWOAAMPGXSZKU"),"enterCachingSession",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.INT),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthFK5KUXGNY7NRDISQAAAAAAAAAA"),"checkBreak",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthFXBG6AQXQ3ORDMNEABIFNQAABA"),"getCurrentLocalDate",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.DATE_TIME),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthGW7UM5FE6PNRDA2JAAMPGXSZKU"),"getTimeOffset",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("localMillis",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprYUZSUQYML5G5FDMTDPLJAMRTIU"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthH5UTIPGXTPNRDISQABIFNQAABA"),"getCurrentTime",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.DATE_TIME),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthJQUPUAH4RPNRDCISABIFNQAABA"),"getUserName",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthMSK2MTNVO3PBDICMABIFNQAABA"),"changeClientLanguage",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newLang",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprIGPWJGSHDJEVBPVCEOELQB6W2A"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthO5PKKKU533NRDCK7ABIFNQAABA"),"newObject",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("classId",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5VXKUHHDFBF7THMQGPI5XAD5IE"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthONIEWR6P33OBDCSKABIFNQAABA"),"sleep",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("millis",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRS5C6L7QPNHCVA2KXH3V2SSGLE"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthQWUOD223LTOBDB2CAAN7YHKUNI"),"radixEncoding2Msdl",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("encoding",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprU4I5JYG5V5BODILQU4W4Q7DXCY"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthRH7ZCDFE6PNRDA2JAAMPGXSZKU"),"getCurrentGreenwichTimeMillis",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthSBMW6GP4RPNRDCISABIFNQAABA"),"getStationName",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthW2YVQDWLWHNRDOLCABIFNQAABA"),"needBreak",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthX22GMFYBKNC6RGIHJSGRQQ7K2A"),"getInstance",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthXFUZQYL2ATOBDA26AAMPGXSZKU"),"getClientLanguage",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthYFY5PGIHR3OBDCWOAAMPGXSZKU"),"leaveCachingSession",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("sessionId",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprAAM46JZDGFDHPGDP4TKOLA3LKE"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthYMUE52FPAPOBDA26AAMPGXSZKU"),"generateGuid",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthYY6FOOADRTNRDCISABIFNQAABA"),"getEntityObject",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pid",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprXAQFMW6H7VEVFIYIA3KNTM3LB4"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthJ22YS76JKFBLHMS32FAYSH3QNI"),"getDefManager",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthE5V4QN6ITBBSLD6DL3M3OXO4BY"),"valAsStr2ObjVal",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("valAsStr",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr73HDDBGB2RG4XJJV3HDCNNZFQU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("valType",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprVF476GS4ANBINDBPT3DJV6WMDQ"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthGQ7ZCUZLTTPBDBKLABIFNQAABA"),"objVal2valAsStr",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("valAsObject",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprTBGFOS6CZNHKPOV2FTETNMZTLU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("valType",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprVJR4JWP5AFHFFAWXDFPWFB7QIU"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthJMKRQCVOSND2XCKKOLLHD2U67M"),"translateSqml",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("sqml",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQ2JOAYQOGVCEFOYWFCXQ7Q2W2E")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("tableId",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQLVFBIPL35GEJB7FUJJXT5DQFQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("entityClassId",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprVWP3ETWAKFFTPKGCP4FDUIFDGU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("tableAlias",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprNSHCP3PZSRC4BABF54JBRKSQSQ"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthX7SDARD7DJHXTKSDE3SQCZOE3Q"),"translateSqml",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("sqml",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprXTFCK3CDMZFZDOJA24NTNFFZ2Q")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("table",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprFUTDBUONKRDAPEZPB7GQIBOYRI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("entityClassId",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRKVJ4V7GSREJPKI7XQY4USVIQ4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("tableAlias",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr66NPKXDN2ZCDRAEPFPQEO4QOVI"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth7WWFVLOVXJCPJM7VLTOAL3USIQ"),"translateSqml",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("sqml",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprG2II7CTRCRGQJMDHAVBN5OU7Y4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("tableId",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLF44ZT4ADRG6ZLPPZVVXVYLOQY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("entityClassId",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLIWTINOCWZFNHEOCHB5VXSFAOA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("tableAlias",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRJNNYMWXOBBNFMOB2SHJLQGMOA"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthBY2ZMGTXXBAS7LYLSOB5DQ2I4E"),"translateSqml",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("sqml",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprKSHVH32CWZCVZMVA4SAUSAUP6Q")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("table",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprFZ2M6VAJWRDY3F3XMSTGXXFHCI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("entityClassId",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprEWMDOYI5WZD4BDAVKVWZ5D2DJM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("tableAlias",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprF4QSEFDWIFBBPMEP2XPBKSWB3M"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthVR3O4RAGRTNRDCISABIFNQAABA"),"rollback",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthVPNKSAORTPNRDISQABIFNQAABA"),"rollbackToSavepoint",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("id",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprU3VOJIV5B5AVZH7BXK26WVVZ2M"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthDV3GVFXTA3PBDLT5AAN7YHKUNI"),"sendBatches",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthEYROCYIR7HORDHO4ABIFNQAABA"),"setCreatesBatchSize",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("tableId",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr6Z5EHLQPPVCPVLMMEHTRXAE7CI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("batchSize",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprXRNGFGREVFCBJL7TRCAE2IY5W4"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthKVZWVQOQTPNRDISQABIFNQAABA"),"setSavepoint",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthEVPFPGQR7HORDHO4ABIFNQAABA"),"setUpdatesBatchSize",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("tableId",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprOA3WJFBHABEAVEVAK7YIMMDUEE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("batchSize",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprBSP75NUGSFFNZIMVURCAABQJFE"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthKYS3RPTKXLNRDISQAAAAAAAAAA"),"updateDatabase",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthH2FKAOYGRTNRDCISABIFNQAABA"),"commit",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth35TKQI3R7DNRDOMIABIFNQAABA"),"createTemporaryBlob",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthFAPPRHDR7DNRDOMIABIFNQAABA"),"createTemporaryClob",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPDUPVLPYXTNRDISQAAAAAAAAAA"),"getDbConnection",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth7ISAWVM4QBE2VIC5WNQD3OZKBY"),"invokeByClassName",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("className",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprAPJRMGAG75D5BBFP4MH4Q5MNQE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("methodName",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprXW2SN5USBZCJLMYA3EPLDMACKU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("paramTypes",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprHQP5WIIORBGMTA5DQTZJSBHCOQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("paramVals",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprNGRT3OU2EJAKHNW3NNMZFJEIYM"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthSHFTB32QK5CRZCRIHVSAGNAJ64"),"enterTimingSection",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("timingSectionId",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5F6NWELUSJCSNBQDQTELKYDL6Y"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthU4PRIYWVBNDBLLVBK6AQ6YH5XY"),"leaveTimingSection",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("timingSectionId",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr6CPFHVKCKBDM7PLFFDWCLGAI4E"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthXILF3LTJVJFZ5JFJ6AWRON3NOY"),"enterTimingSection",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("timingSectionId",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprDRQNBHBONVDMTHHNKCM7T5RIQU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("path",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr7ULWVJEHGZAGBHHJLIOV7WDLFY"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth3CHE3V6EGVEETJS36BT5RTMLZY"),"leaveTimingSection",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("timingSectionId",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr637P7ZJPYFEYPM4JNUVBWZAEDA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("path",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprJACLEYYXIVEE7FS7EJBNB6QUUA"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth4NEMAH6JSRFVLIOZQKNPABN6FA"),"getDbDataSource",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthVBYT7JGPANGVVCVLOVN434UO44"),"getAppParams",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth2QT4RSOMLNCW3JOQ2SA57EEYJE"),"getClientEnvironment",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthMRSCGDAXNBHPFGFTCNBKUW6FP4"),"getClientCountry",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthZWJYABENAVEWHEOIDMMFECBL4E"),"changeClientCountry",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newCountry",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprACEA3356VZAAJON3OUHIXBSO2U"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth5NZQM372ZNACXGR66DJCYM66OE"),"getClientLocale",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthEVT5IREU7NEA5JW2MSIIJIFHSU"),"changeClientLocale",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newLocale",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr6DM6YKWHY5EBRAXG3FHPIHJWWU"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthRCSWGPQALFCKFHNAQBDPJ23WWA"),"createLocalTracer",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("eventSource",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr6TTQJLHAOJBWDOIR2A67RT7TIE"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth6U2XAAFFIFEB5AFSJDIWGPUT6Y"),"getAllLayerVersionsAsString",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthLE3IKXC2DBCVJCBDMCMLDUJAMQ"),"getUserCache",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthRUFBDP2JWZB65BRLAXJ67RJ4X4"),"markActive",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthRMAJBM7RKRENNMYLGX2F3PTBGI"),"markInactive",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthSHFVGI3P5RANZGTEZYTZMBQWQE"),"getScpName",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthT25AIBSXLJBCDLJEBVCGDAEV5A"),"onStart",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthWJE7EQFPP5AU7I2X5QTHQLGU5E"),"invokeService",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("rq",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprT2VRMA42WRCJ3KEYVYGRMA53IU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("resultClass",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprFRWN7PM3FJEWVHMG3I5H36H7KA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("systemId",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCJ23ESFRPVA5JIYS3E4I44BRMA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("service",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQII4ENFGYZEFFCXFHOGCQGKLW4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("scpName",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprHYNK3RTB7NCSPFDSPSSLO75SBY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("keepConnectTime",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPVKJMUNFIVBFVLM2XZRNFWP4XU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("timeout",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5JW5APUAKVHWFOPUY3A4IWSLM4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("aadcMember",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprIULTA5LAN5DJFNQ4PXP2C2XOZ4"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthCUDB2SCLUNEAFFUK77ANDMJFWI"),"invokeService",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("rq",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprHYD734HRFZDLFPNWP6S4DJQCJE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("soapRequestParams",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5HCPHNZIZZA7PJ7GSQ26JZ2ITY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("resultClass",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprWSL2J3GZRBCHZOZSSQWJUZCBHU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("systemId",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQ2PDOIKTLBEM3PK2GCIDHL7VLQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("service",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRXKU3E6BRVFA3OSJPNC4L7ZSII")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("scpName",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprFVYJ3NAGWJBHRJDSKEXR244LHI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("keepConnectTime",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5LWDEHZEOVCODH3B4UN4LVID7Q")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("timeout",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprHMX6VQBVQNEV5JBD7RZD4ID2EY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("aadcMember",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprMPSTALTSUFCR5MZ6BYTQRTRJWY"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthB42AEKKYRFBSJG3IIG3D4CVTWE"),"invokeService",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("rq",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprUFD3SDUPYBCR7EKSEY3JVEPVYQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("soapRequestParams",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQ6TQYFC56ZCRZDRRUIMVCZUBTQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("resultClass",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2FCMPZQD2BBRHOSQTC3HB6SS3A")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("systemId",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr4SCRIH2VSBABTOMSVKWPXNZ6VE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("service",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCWFGZWANEJD2TLCXG2PJ2VFOQU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("scpName",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprVHA6M45QS5BO3LA6J45O4DNUZY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("additionalSaps",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr7KBBKMILINC5LB6W45AWEVRSTA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("keepConnectTime",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5QAT46PWBFE7DG6WW7WHWEEFT4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("timeout",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprW52KESKPSBDXLIANQSZ7FLGXQA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("aadcMember",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprSXDT2FKFWVAQXP6GRKCR5MJIBY"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthAC6QWAIA6JGSHOA4U4FCPNXFME"),"invokeService",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("rq",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprUFD3SDUPYBCR7EKSEY3JVEPVYQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("soapRequestParams",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQ6TQYFC56ZCRZDRRUIMVCZUBTQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("resultClass",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2FCMPZQD2BBRHOSQTC3HB6SS3A")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("systemId",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr4SCRIH2VSBABTOMSVKWPXNZ6VE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("service",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCWFGZWANEJD2TLCXG2PJ2VFOQU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("scpName",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprVHA6M45QS5BO3LA6J45O4DNUZY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("additionalSaps",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr7KBBKMILINC5LB6W45AWEVRSTA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("keepConnectTime",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5QAT46PWBFE7DG6WW7WHWEEFT4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("receiveTimeout",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprW52KESKPSBDXLIANQSZ7FLGXQA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("connectTimeout",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprATZCZ3M6J5CMPFO4IQRQ6NOBH4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("aadcMember",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQKRSVGGIBNGZ7KJ4O4OQVKODXE"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth6B35BO32E5AAFEAAT4PKR7AODM"),"invokeService",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("rq",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprT2VRMA42WRCJ3KEYVYGRMA53IU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("resultClass",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprFRWN7PM3FJEWVHMG3I5H36H7KA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("systemId",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCJ23ESFRPVA5JIYS3E4I44BRMA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("service",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQII4ENFGYZEFFCXFHOGCQGKLW4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("scpName",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprHYNK3RTB7NCSPFDSPSSLO75SBY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("keepConnectTime",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPVKJMUNFIVBFVLM2XZRNFWP4XU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("receiveTimeout",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5JW5APUAKVHWFOPUY3A4IWSLM4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("connectTimeout",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprZDJWMGNNSREQRGZW7LDDT2KSCE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("aadcMember",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprNDMHUHTJ45FZ7D7IOX2ANHG7IM"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthAEWBPU7ZXTNRDISQAAAAAAAAAA"),"invokeInternalService",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("rq",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr4RMG5457DZDQJLE6Q4EPPU54WY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("resultClass",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprAW33BDLQJZCHFEMX35UFLJEDL4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("service",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2WJOZH2K4ZEQLBQNGIFGTEFIDI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("keepConnectTime",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2S62DEKM4BCKZFTKEPR3XBANLI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("timeout",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprADM36G556NCW5E6M53V2P4LSEY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("aadcMember",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprHHK2KDVUERCTBMMFFJMPLUPU6U"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth7PCOMY6NBJEHVJE2WHBZVJJT3Q"),"invokeInternalService",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("rq",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr4RMG5457DZDQJLE6Q4EPPU54WY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("resultClass",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprAW33BDLQJZCHFEMX35UFLJEDL4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("service",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2WJOZH2K4ZEQLBQNGIFGTEFIDI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("keepConnectTime",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2S62DEKM4BCKZFTKEPR3XBANLI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("receiveTimeout",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprADM36G556NCW5E6M53V2P4LSEY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("connectTimeout",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprWHAJCI7NIJDGVKN7N4O5AISHRQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("aadcMember",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQF22SBW5WFA6BP7YLYR3DR2RCI"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthCMZ3E67XBRBQLHZXVQEHGG2SMI"),"unlock",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("lockName",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRS5C6L7QPNHCVA2KXH3V2SSGLE"))
								},org.radixware.kernel.common.enums.EValType.INT),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthAKYZJ7KCMRGZPBKVVH4TBA4FKE"),"getSessionLockOwnerInfo",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("entityId",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprVKPQLCJA5FCXNGQBYB77MGIEG4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprVDZ3EWXGSBCCHL6LZZF6KPVITQ"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthJYYZZHYNINDKBPQEKUOMRM4ZSY"),"lock",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("lockName",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRS5C6L7QPNHCVA2KXH3V2SSGLE"))
								},org.radixware.kernel.common.enums.EValType.INT),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthWOPPFDISTFE2LGWN5EF7SZCPU4"),"getServiceSaps",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("systemId",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprKPPAQO6S3FA75HNRPTR2MDPBPM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("serviceUri",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQJPAUMYG5NDX3F7VGO3NB5B6T4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("scpName",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprC4S4GV4RD5FY7BPHPTAZQ3R5K4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("targetAadcMember",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr6B5NU6AIE5E77IQPVJDC2BLLHI"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthO6E3U4KDA5A5FFVCN7JGELDMAU"),"getDatabaseSysDate",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.DATE_TIME),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthOZ6IRS25V5BVFHBC7ZSKNZNTP4"),"getDatabaseSysTimestamp",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.DATE_TIME),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthQCH5JDACGBEWDC2I26QLFZM7DM"),"canHandleAasRequestsForScp",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("scpName",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprS3RWSLWIWZDMFCWLEV3Q73BAVQ"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth54DKS3WG3ZEYRC5AL4Y222OA4U"),"canHandleArteServiceRequestsForScp",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("serviceUri",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprTNG4TMSO4JEG7CCQRE5XHWI6KI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("scpName",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprOCPTMEZDVRBCPA2TYCX7DL3XZI"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth3FHH7DLMOBAQ7COVKYGC2PSMZE"),"getDbSid",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.INT),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthHN3PL5HSKRASXAANJU5WMXT6VQ"),"getDbSerial",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.INT),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthGKMEGEYP5BH3HOEQTMOHA72HFI"),"freeTemporaryBlob",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("blob",org.radixware.kernel.common.enums.EValType.BLOB,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprGBDAZJCVHZDZBCQFTLN5GQ6PFQ"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthXTVU25DCIZEUTJPIDXHYN5RT5E"),"freeTemporaryClob",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("clob",org.radixware.kernel.common.enums.EValType.CLOB,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRKVBXXTTG5BLJMYHOQMEPWONGQ"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthRISRVXXNTVBC5PIQYMK6EBFY7A"),"freeTemporaryBlobs",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("blobs",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprH6RKSUZVJJEBZH242W7FFTNMRY"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthYCJD3DO3HNEZ5GOREDH37CMTVQ"),"freeTemporaryClobs",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("clobs",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprH6RKSUZVJJEBZH242W7FFTNMRY"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthFM6HJCMTEBDD5LIWGXT66HOGYA"),"isEasSessionKeyAccessible",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthJZVQGO24W5HSFHNQF4KSBSKPIQ"),"encryptByEasSessionKey",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("data",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQN3QL6SOOBGK3JMLZA4VIOL2MA"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth7VQAVPW32ZHHFNSV4WSSLFEARU"),"decryptByEasSessionKey",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("encryptedData",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr6BQOEAXBNFFWBIGKF3XVDLQL2Y"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth6JCSLQLZDJDFJFWHWO2WEQLWJ4"),"invokeService",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("soapMess",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprXMBV6V2PCJFUBNBGW22D2PHPIU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("scpName",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprVMLH4HFLAFETXCABC6PSLMQE2A")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("aadcMember",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprTS57DG4WRZDILJTIEX7UZQ2YWM"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthTBGVQKHV2VEANECHZGETGDNW4A"),"getSavepointId",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthZCQRLCQJQVH7FNOIUJAUSGRHSQ"),"getSavepointNesting",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthAATBFBJRUZBKVNHULLG2K5KFLQ"),"getSavepointNesting",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("id",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprBZ2MSAIPIZC5HNZPV72AGBJVEY"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthJXSGG3WO5VB5PKC3INHXAQG7Y4"),"registerArteEventListener",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("listener",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprN62LJDKBYNGQ7DYK34BGAINPD4"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthYIBCJCRNINC7VHBK5FUN7D4YSQ"),"unregisterArteEventListener",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("listener",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprN62LJDKBYNGQ7DYK34BGAINPD4"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthMU3WCKQT5JH35H3ZMQHIS5XVWY"),"parseLayerVersions",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("versionsString",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQTTTVO5DGBAVPBRHRIMI5C2IYU"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthXB6ZFMBJKFFFNJR35RVIZP3J3U"),"getAllLayerVersions",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth2CDAL44USFHLZLIH6BR3EJTDNA"),"getEntityObject",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pid",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprXAQFMW6H7VEVFIYIA3KNTM3LB4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("classGuid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprDH7EUQNTY5CJVJVJWUAYNF2ZVA"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthZ7YTDB5IMVGBXCUJUJHCOTRXRY"),"setExpectedCacheSize",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("size",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprY3PEPWIKFBCTJJWAJ4ZXJAEYYI"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthVNKLXLL67RAKXM6MWDHL4RFMPA"),"createTemporaryBlob",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("data",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLDBSSSXGVREO3LVNNVOCLQJGUY"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthM5XHZKTTWBFS3EZVWKHOMYFVLQ"),"createTemporaryClob",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("data",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprEX3DQCFXCZBARGNGDGZGGOK5ZI"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS)
						},
						null,
						null,null,null,false);
}

/* Radix::Arte::Arte - Localizing Bundle */
package org.radixware.ads.Arte.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Arte - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
		loadStrings2();
		loadStrings3();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Список значений параметров метода (в порядке объявления методом)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls242DVB22RNDWLEXV7NFP4IOSNQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ARTE.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"ARTE.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls24NOGVIHURDVDHDEVQLMTVCNG4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Объект, содержащий результат преобразования");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2B3PT7IRKJHFFJ5LUX7UQMBSK4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Расшифровывает данные, зашифрованные сессионным ключом EAS");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2K2TLPJHFVEYTBXTHRNZPD6GHU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Список типов параметров метода (в порядке объявления методом)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2NAUPER6WBG3TP3XOCV6QOAFA4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Result returned by the service");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Результат, возвращенный сервисом");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2PCTKNKTSZBD7FDBK3KZ22DB5M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Целевой AADC узел");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2RTYKL7C65AONMASTBEWVXCMZA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Response timeout");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Таймаут получения ответа");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2T34EE6U3JCKXAP6JPZYHW7IMA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Sets the batch size for the respective table for UPDATE-like operations.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Устанавливает размер пакета соответствующей таблице для UPDATE-подобных операций.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls35PMITMDMBGFRPK7NFGAIYLFDU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ID of current savepoint.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид. текущей контрольной точки.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls36S6MRF5WZBPVBKIQMBOJJAUGM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Check result");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Результат проверки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3DLLG6P2VRBMJMJ3XMWRXTDBOU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод получения текущей системной даты (компонента времени сбрасывается в 00:00:00)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3F4DR62TXRAJ3DQVN7RFMXVTSM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Тип сериализуемого значения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3HJK6UOQEVD6BDVKS5IQ4E4UVE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Result returned by the service");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Результат, возвращенный сервисом");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3IFXK3O3YNHG5CNHWSWIYOY72A"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Invokes the system service");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод вызова сервиса системы");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3SMZJZ6HKNCF7LXINXSOO32IQU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Вызывает статический метод указанного класса с указанными параметрами");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3THBMKZUDRACBMSEGP2SHI63X4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Освобождает временное хранилище для CLOB");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3UTLABRCDRHCLFYZHIFADW6UQM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Целевой AADC узел");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3WXBSQEBHNA5LID2QYSUS65OI4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Список дополнительных точек доступа к сервису, используемых для соединения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3XTBZJHYJJETTOB7MBSJSRHSW4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод начала новой секции профилирования кода. Должен использоваться в паре с leaveTimingSection.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4245A64KBBALBL5CQTTCE57H3M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Gets the Java class by ID.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Позволяет получить Java-класс по его идентификатору.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls47HO6GGHBNFK3LLMH4MFDC72BI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Тип среды выполнения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4ICETD3S25FETB5XRTATJ36X2Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод получения текущего системного времени по Гринвичу (UTC) в виде количества миллисекунд с начала эпохи (Epoch)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4MVM74VF4VH2PMBHE4JWYCVYRQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Gets the name of the station of the current user");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод получения имени станции текущего пользователя");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4NYZK2A7ANECRGBGKDVYA7HFOA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Целевой AADC узел");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4OVV4Z5UJBGCDILHH4XOXVGQ3A"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Целевой AADC узел");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls53JMAXRILNDRLAPJH5FSFDHWQ4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Время поддержания соединения в активном состоянии (keep-alive)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls53RQZ3CGFZADBG3FBBMRM6QMWQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Конвертируемая кодировка");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls567BHQYNYFD3ZPYTHT2DPQ2MJM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Результат, возвращенный вызванным методом");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5CAEQRIHMZFB7MCNEJDWM3SL2E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Имя вызывающего SCP");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5CTFMMRHCNEPNM7HNPH5FUBOUE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ID of the new caching section. It must be used as a parameter of the leaveCachingSection method.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Идентификатор новой секции кэширования. Должен использоваться в качестве параметра метода leaveCachingSection");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5EYLYUWIHVBBRBE72XUSKY3PQE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Дата (в виде количества миллисекунд с начала эпохи) на которую запрашивается разница");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5GR66JHOHNEOPHOTKNV4KFDWR4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Текущее системное время в нулевом часовом поясе (UTC)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5H4EZPLMTJGBZBQBIQCR4SQNJU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ID of the class for the object being created");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Идентификатор класса создаваемого объекта");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5WWZ72IHFJFF3FL7VLTJVDQN5Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"SQL data source.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Источник SQL-данных.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5XK4SPEX6ZHR7LWBEWYSKPLV4E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Class of the result being returned");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Класс возвращаемого результата");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5YA22ZRFYVFXRMXLURYSBCA5DU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод изменения страны текущего клиента");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls65TARFJZ7JAF3K6UGBZ4XTEZVE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод покидания секции кэширования. Вызывает запись объектов, созданных в рамках данной секции, в БД");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6BDSXLVPRJGKZL2UKPQAYNP6MQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Registers listener for DB commits/rollbacks and version unloading. It is recommended to subclass from AbstractArteEventListener instead of implementing IArteEventListener directly");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6DYNQPPKEBARZA3ATATKGRCHEA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Alias of the main table");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Псевдоним главной таблицы");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6J3X6O634JH5PIDTTFDDU6ICLE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Имя освобождаемой блокировки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6JP7Z2MFDZBY3ONI6DWLDZUASU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Время поддержания соединения в активном состоянии (keep-alive)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6KA4NDJCNZCMPBGVD7BHPAZUN4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Result returned by the service");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Результат, возвращенный сервисом");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6KKC7MCDEJEYTCI3UXP53XE7GQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ID of created savepoint.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид. созданной контрольной точки.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6LDXA7ADWJESPK3UHQXSQBKMFI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Returns the SQL data source. For details, see javax.sql.DataSource interface.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Возвращает источник SQL-данных. Подробнее смотри интерфейс \"javax.sql.DataSource\".");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6QNF2BHSRZFDHED3LBO5PI5CAM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Entity instance");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Экземпляр сущности");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6QPCROAJIBA5PFMFPTSXASNT4E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Table ID.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид. таблицы.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6XN7KHBQHBAIDNB6PHOFQK5E5Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Освобождает временные хранилища для BLOB");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls724Q4LU6ZRAALK7TUOTSMNYD34"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Имя устанавливаемой блокировки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls75POQJT5MZF3PG77FJIFEHZAWA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Application configuration parameters");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Параметры конфигурации приложения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls76K3L4X4QZAYNHGIZL6LF77WYU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Количество миллисекунд с начала эпохи");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7CKGVZOAZFASHJHT6LYTANS72E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"SCP name");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Имя SCP");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7DT4PNPAWNBUDAGUIAZIQMLOOY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ID of the system to which the service being called belongs");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Идентификатор системы, которой принадлежит вызываемый сервис");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7DTY4D45SREA3KN2GTGZUPOTSM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Deserialized value");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Десериализованное значение");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7LD4AGI5VBERLJ5Q7C6WO4AOJE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"DB session ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Идентификатор сессии в БД");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7LDDEC557NGBPHDCF5FGTIVLDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Преобразует SQML в SQL");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7NUXYTOL7JBENGRVS7V2IULDYA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Вид секции профилирования");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7WOSA55IHZCTLCTIIV622M2LYA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Service URI");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"URI сервиса");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsA3KYXXLRJZB47AN2O25HAKZN5U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Gets the name of the current user");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод получения имени текущего пользователя");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsA3PNX6655BBZHDBXCHGIHCDASI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Class of the result being returned");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Класс возвращаемого результата");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAC2KL4KXFBEKLJM7F5E3XYG2JU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Request parameters");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Параметры запроса");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAD3YLHICERCTNBGX2JQ32WSHBE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Время поддержания соединения в активном состоянии (keep-alive)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsADDRNZA6CVHNXNGHLYVTMTQ2FY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Serialized value as a string");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сериализованное значение в виде строки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAKCQO3KJYRCORJ4AMVHELD2MQU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Class ID.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид. класса.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsALXXAVXY7JAEJNHYJSUNWVYBTU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ID of the main table");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Идентификатор главной таблицы");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAPNBJ4DXJVDPNJW2IO3ACDVGKA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Returns ARTE.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Возвращает ARTE.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsASX43RB6EVC37AF5HYDQQQKDYA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Returns the ID of the current savepoint.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Возвращает ид. текущей контрольной точки.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsB2447JU3D5F5HD4JWJH6GP7SNI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Name of the service being invoked");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Имя вызываемого сервиса");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBGAB2NX5ZJE2PDN77HDH4QMWEY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Язык клиента");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBGCBKJCYBZDVVB6AKPB7JSAXMA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Current system time");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Текущее системное время");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBGRIHUY76NDWTEZD4AZ5IWFJVA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Encrypted data");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Шифруемые данные");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBJDTBIB5EFHCRKWITYNPVT6SVQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ID of the system to which the service being called belongs");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Идентификатор системы, которой принадлежит вызываемый сервис");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBMGEREHRGRFKBJQDD7L4TK4YX4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Тип десериализуемого значения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBOKE5SOY7NE3BIV4QZOFJC2CAA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Id of the application class ");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Идентификатор прикладного класса на базе сущности");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBRRUETTT7BHHJMPC2ZL55IIP2M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод генерации нового глобально-уникального идентификатора");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBWHZ7CHXUVCKFM53IZMKOTG6EQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод получения сущности по её PID. Данный метод не гарантирует существования сущности в БД - может быть создана сущность-заглушка, подробнее см. в описании класса Entity");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsC3ZONGRLIJCNDCGHZZZBIOAISA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Имя вызывающего SCP");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsC6IKHN6OZZCRLO3M6E7UXQIVZM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Имя класса, метод которого вызывается");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCXBLZKACJNCSRN7P2GYUJCGBKI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Преобразуемый SQML");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsD2DUKEGIAFBMFLQFFVS3EQNUWI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Объект, содержащий результат преобразования");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsD3AAJOAW5FGIRC37DATPMCIX3E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Имя проверяемого SCP");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDEQKGSHMR5CULEXIAIJD22MURY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Gets the current system time");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод получения текущего системного времени");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDFDI7G7IVVHTVJW7JNSGEPH7WE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Class of the result being returned");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Класс возвращаемого результата");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDG6BHCLIOBHPHFWCUGYAEWQN2Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Gets the name of the current SCP");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод получения имени текущего SCP");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDI2UYILFAFFS3LZNWROOYRFISI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Invokes the system service");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод вызова сервиса системы");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDLUVTQEFBRD7RC4AOREYDWVTVI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Время поддержания соединения в активном состоянии (keep-alive)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDQXR7QQOMZARHPB5JIF4NJQK3M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Entity instance");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Экземпляр сущности");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDU2YYIFRPJFGXF5JACOSXH7R3I"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Decrypted data");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Расшифрованные данные");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsE3VYMFJQ2NENRMGW6J7B5BVPMU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод проверки была ли обработка запроса прервана со стороны клиента");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEC2ETO57INEDJMR7OU2XD33WQM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод получения языка текущего клиента");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEEMPFHTF5FHZ3MCBPWICP5DTQU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Alias of the main table");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Псевдоним главной таблицы");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEEZQY4ZI55HCDDOAJG4PUNB4XY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Current system date");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Текущая системная дата");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEJQMMEQHJ5G7PLHVGNFEM54IZI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Целевой AADC узел");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEJYNTBXKBRGOFFRLO42DM6XVM4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Целевой AADC узел");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEKML4UPISFFY5IFEITVXY2L7BE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Localized tracer.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Локализированный трассировщик.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEM5FS7WSQVGP7K2RGXDRGX5YPE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Marks ARTE as inactive");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Помечает ARTE как неактивную");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEWS7U6HIE5EPJOYV6ZYEZYNPUA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод получения разницы между UTC и местным временем на указанную дату");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEYQDMJQ2D5D5PO3MZGK3BJ2QNM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Alias of the main table");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Псевдоним главной таблицы");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEZEUWKYM2JGEHOYFMZOYSM52DA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Class of the result being returned");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Класс возвращаемого результата");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsF2FKBI7AL5F2HD2BNNHRPAPJFM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Имя вызывающего SCP");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFBTWBFRKQREALNFPOMDHKOYPYQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Устанавливаемая страна клиента");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFC4JL7TZKBAT5BEFO3F7MYUP2M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
	}

	@SuppressWarnings("unused")
	private static void loadStrings2(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"XML object containing the data of the request to the service");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"XML-объект, содержащий данные запроса к сервису");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFCDII7VZ7VDOBAUQHMHCQVWXCU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Response timeout");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Таймаут получения ответа");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFD276QL7EFEWZKY6AOQQPT44NQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Code version");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Версия кода");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFFEF45335BGSBP4W226VOUHTHQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Целевой AADC узел");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFFU5NWS7GBDJNGE2FVRLATRHTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Gets the current code version");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод получения текущей версии кода");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFJVCR677SBBWDEFK3GNMUI2SRU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод, проверяющий, может ли текущий процесс обработать AAS запрос для указанного SCP.\nЭквивалентен вызову Arte.canHandleArteServiceRequestForScp(ArteXmlSchemes::AasWsdl.Value, scpName);");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFNSEUBUEXRARVBHDSGT5VVWZLE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Имя вызывающего SCP");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFUFOQTD5PVFE5ICNMJ4WYW25ME"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод получения параметров приложения, определенных в конфигурационном файле");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFZA5G3KBXNDLXHNUZEEJIETICQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"PID запрашиваемой сущности");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGB73RXMO5RHJTLJ4IPCJ3UBIQU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Преобразуемый SQML");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGHY7FCR7PZB5DILENUTEGPPDHQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"PID (в строковом виде) сущности по которой запрашивается информация о блокировке");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGINS3FSMDRCTLI73QXRVTXOLDY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Creates the temporary storage for BLOB.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Создает временное хранилище для BLOB.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGN5W7DVDGRC5VIWLP2U7TADW2A"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод получения информации о точках доступа к сервису для указанных системы, сервиса и SCP");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGQZPN4F2INHUXBF6F6JNZB5XGM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод освобождения сессионной блокировки с указанным именем");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsH4ZHK7US5NDIHCBA2OGN3PNIJQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Result returned by the service");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Результат, возвращенный сервисом");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsH6EVHHA4HRHKVGZXCLALGYAPO4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"XML object containing the data of the request to the service");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"XML-объект, содержащий данные запроса к сервису");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsH6MSSNAMNNGJBG3NDZLWMFIUCI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Database connection.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Соединение с БД.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHDIJCKI6ZVCGXBCW66XR767WCA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод вызова внутреннего сервиса системы. Внутренний сервис - это сервис, принадлежащий системе #1.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHEPCNBAP3JHKJGKIW6AAOFPDQM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод изменения локали текущего клиента");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHJ6VU5CSO5D7DMR6DZRT7QQZ5M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Initial data to be stored in the creating BLOB");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Начальные данные для хранения в создаваемом BLOB\'е");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHRKZTOUOFFGUNGQLMMZO4I5DBE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Преобразует SQML в SQL");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHRVHRZ7UHJE67KURIOXYNBELZ4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Creates tracer with predefined EventSource");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Создает трассировщик с предустановленным источником событий");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHUQPSN65Q5ENTB5LIHMYXXIA3E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ID of the system to which the service being called belongs");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Идентификатор системы, которой принадлежит вызываемый сервис");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsI2AB6NMU2FGIHCSNT26UR3JEUQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"XML object containing the data of the request to the service");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"XML-объект, содержащий данные запроса к сервису");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsI366NUPSPVBOJP3WLSGRLMXA2Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Зашифрованные данные");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsI4EI4CFVAVA23IV53VYCDFNEGY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Класс, возвращаемого результата");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIHABT6QM2ZAS3G2436G4MNYQJA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Преобразует SQML в SQL");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIIDHF6XZ5VGU7HZALUOSJVOX54"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Returns the nesting level for the current savepoint.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Возвращает уровень вложенности текущей контрольной точки.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIJ7U2RLUGZHLBIK33YTU5MZLHA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Registers listener for DB commits/rollbacks and version unloading. It is recommended to subclass from AbstractArteEventListener instead of implementing IArteEventListener directly");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIJE75VIP3NBEFHHP3ZLBQRFALU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Шифрует данные сессионным ключом EAS");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIMIDW44KT5BLXEOSFL7AQH4PJA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Вид секции профилирования");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIN6BQRDLTNFXRGEIONBEXRI3YY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"String representation of versions.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Строковое представление версий.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIQMQ4SOHHZAPHHMJQLAWVCG3DY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Result returned by the service");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Результат, возвращенный сервисом");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsISXH5URTFNBOVLH7TUFINEZQDE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Освобождаемый CLOB");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsITKNC6XPVJBRZMD3K4RB5IO4EQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Marks ARTE as active");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Помечает ARTE как активную.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsITSCKFEN2RFEVKLKBPE7I5MFHU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Gets the information on the layer versions.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Позволяет получить информацию о версиях слоя.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJBIQGLA2X5B2VI7UMOFG6JDBSE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод проверки доступности сессионного ключа EAS");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJCWZ25IVYRAHTMSAMYG4QEILGY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ID of the main entity");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Идентификатор главной сущности");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJDSV6IV3PFADDEBF26BJ6SAKUY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Список точек доступа к сервису");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJIMT7LWM7VCHDDUKOMGUYRV4RU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Кодировка для использования в MSDL. В случае невозможности конвертации (в том числе если encoding==null) возвращает null.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJJ4EWUNRCZC5PMGMHV3YWNKOTM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Name of the service being invoked");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Имя вызываемого сервиса");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJMA5VSLS4ZAOBP6QB2EATW7DLU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод сериализации значения объекта в строку");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJMZOG27JYRH3RKK5ZWUZGEKJRM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Identifier of the savepoint to which the transaction is rolled back");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Идентификатор точки отката на которую производится откат");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJOIUBRVCXBE7XCEZHFBN3OQYNM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ID of the main table");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Идентификатор главной таблицы");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJQQFXNI6SBB6VNBB4EAARA7H3M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Менеджер дефиниций");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJVVYBR6F7BHH7D4NPKWV6NFWHM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод получения текущего системного времени по Гринвичу (UTC)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsK2YTKGVHMJAYDMSZ3ESZJGMFCE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ID of the main entity");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Идентификатор главной сущности");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsK3BIRLDHBBBKNIRDXBKGIUOREU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Request parameters");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Параметры запроса");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKBKLVN37ZFBJJCXCB7U6WC4RPQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Целевой AADC узел, null - любой");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKBV66C2ON5EDTOAFQA4TBKQHCM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Returns the opened DB connection.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Возвращает открытое соединение с БД.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKFAZC6LRBZF7RHNP7PVVESBYRE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Результат операции. 0 - блокировка успешно снята, иначе - код ошибки, см. функцию Oracle DBMS_LOCK.Release");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKHBY6SYHXFBS5GGE5E3OINLARY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"SQL Character Large Object.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"SQL Character Large Object.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKL3XBY35DJCFFDQQ6GD7E75UFY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Проверяет, была ли обработка запроса прервана со стороны клиента.\nВ случае прерывания обработки - генерирует исключение");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKO3Z4HIYABDAHMBAY4K23YUCYQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Откатывает изменения в БД, внесенные в рамках текущей транзакции. Метод сначала выполняет подготовленные запросы (см. sendBatches), затем откатывает транзакцию БД, затем обеспечивает консистентность кэша и базы данных:\n<ol>\n<li>Сначала вызывается обработчик Entity::onAutonomousStoreAfterRollback, позволяющий сохранить данную сущность в рамках автономной транзакции</li>\n<li>В случае, если метод onAutonomousStoreAfterRollback вернул false, то сущности, созданные в рамках данной транзакции, сбрасываются из кэша (см. Entity::discard), для прочих затронутых транзакцией сущностей вызывается метод Entity::purgePropCacheOnRollback, таким образом, последующие обращения к свойствам таких сущностей приведут к чтению из БД.</li>\n</ol>\nЗатем вызываются обработчики события afterRollbackToSavepoint(null)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKOJDV4UCBVCH7OU54BC5XZSNCM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Устанавливаемый язык клиента");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKRAXREG3M5BQVFNNVNC6MFWXZ4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ID of the class whose method is called");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Идентификатор класса, метод которого вызывается");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKUZD4JSMJ5CKRC3PRDISMT46NU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Результат выполнения операции. 0 - блокировка успешна, иначе - код ошибки, см. функцию Oracle DBMS_LOCK.Request");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKYUN7UVIOVFQXBYU6RD7EAHIZI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Connection timeout");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Таймаут соединения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsL2JQYKW4DFFMDKVLQBZ6BW7ERU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сериализуемое значение");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsL3OZ5VHS6VEFXBHZ2TDIV5YJNE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод завершения секции профилирования кода. Должен использоваться в паре с enterTimingSection.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLGCS6YAKMNEFFOIVJMBPRJ5W6U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Stops the ARTE thread.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Приостановка потока ARTE.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLINXK3AN2RD3NOP2DUSGXLPBVI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод установки сессионной блокировки с указанным именем");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLKQ5RC3QIVDC3NZGVVPW2C56HQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод изменения языка текущего клиента");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLSKLHQ3ZSNDPPP2TNEJQJCASTY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Дополнительный ключ для идентификации профилируемого кода");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLYB5QRKROVH2JG5UJ5B5KJUVG4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Batch size in simple queries/statements.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Размер пакета, выраженный в простых запросах/операторах.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLYI7HRF5CRGQLD4AQOLWTNI6BQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Разница между UTC и местным временем в миллисекундах");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLZ2NHM3KQJETRCIERFCQG7N2R4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ID of the main entity");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Идентификатор главной сущности");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLZOGA3YDXZBAPGACDGIRFGL2GI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод начала новой секции профилирования кода. Должен использоваться в паре с leaveTimingSection.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsM43RKPIIU5HBXM7DFVFY2BJIAM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Returns the database system timestamp.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Возвращает системную временную отметку.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMC4AZIAZXVBJ7IQODDP5HVUA7U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Конвертирует кодировку для использования в MSDL.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMES2SXVS7FC2PN2GT3IVLC2WKM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Invokes the system service");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод вызова сервиса системы");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMFUGP7ROAVDAHAV73IHS54HHJQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"PID запрашиваемой сущности");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMIZUUFHWJRHDDKNIWJOLJMOQRI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Имя проверяемого SCP");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMJY56BHG4JDYJC6ILKNV6LEDHU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод получения информации о владельце сессионной блокировки указанной сущности");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsML56IONWQNCGDI2VLY5ELGY2ZE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Connection timeout");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Таймаут соединения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMLSZJ6PGEJCUPGDC5FTHWZA7NE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Class of the result being returned");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Класс возвращаемого результата");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMNAF7ZMUG5CMLADAQV33NNGRVE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Форсирует выполнение всех подготовленных запросов создания (обновления) объектов");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMRUUG3J3BZDW3LHP4L6SYC2Y6A"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Creates the temporary storage for CLOB.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Создает временное хранилище для CLOB.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMSY2ESXG25DGZE2YIXO5J7YU44"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"SOAP-сообщение, содержащее данные вызываемого сервиса и его параметры");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNAXW5B2HFZBTLNXTPL45C6AICU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"XML object containing the data of the request to the service");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"XML-объект, содержащий данные запроса к сервису");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNFDZ4HRSOJCZVOIAT6MOSJYLNA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"URI проверяемого сервиса. См. перечисление ArteXmlSchemes");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNL67OIEJVFFMPBX2AI5L72KBPU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Response timeout");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Таймаут ожидания ответа");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsO3Y7F3QU5BCWBIBXCPPUXNGI3Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"System date of DB.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Системная дата БД.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsO5TFGKZFTJDHHAVZY2ZNW7AHCE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Batch size in simple queries/statements.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Размер пакета, выраженный в простых запросах/операторах.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOKB7V6C4VJHU7OUJDJCHXQZIUM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Список освобождаемых CLOB");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOLOF25K62BGAXJWHPXSVWYNFCY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Преобразуемый SQML");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOOLIV27DDZG7VCHC4AC3OVQ6T4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ID of the system to which the service being called belongs");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Идентификатор системы, которой принадлежит вызываемый сервис");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOQKWPWVTKNHWBJL2C5HOOXATMI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод , проверяющий, может ли текущий процесс обрабатывать запросы к указанному сервису для указанного SCP.\nПроверяет наличие в текущей инстанции запущенных ARTE-модулей, предоставляющих сервис serviceUri и поддерживающих указанный SCP.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsORSOT6GFDJG7FPYSZXSAEFWNGQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Connection timeout");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Таймаут ожидания соединения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsP22ICFSOE5DENCMUB2IMJLBZGI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Sets the batch size for the respective table for CREATE-like operations.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Устанавливает размер пакета соответствующей таблице для CREATE-подобных операций.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsP4XUD3EPU5EWXJEDYN6GRRLFLU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Event source.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Источник события.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsP5W2PZPHVZGKTGC2NB5ZIJI22E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Alias of the main table");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Псевдоним главной таблицы");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsP6LZWZTIP5GTFEQLVEZID3L46Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Вызывает статический метод указанного класса с указанными параметрами");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsP6WXQOIC3BAMFJ7BGAQCSO4RK4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод получения менеджера дефиниций системы");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPHWE7EPJ7FCULA5UMZWYXVQV54"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Освобождает временное хранилище для BLOB");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPKIAUTCMPBEMRMMJCGEY26AXZA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод получения локали текущего клиента");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPKXLQ3LCC5G2ZKM4FVPA6SFUAE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"SQL Binary Large Object.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"SQL Binary Large Object.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPNYFYMYNVVCBNNYJCRXWXJ7HCM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Request parameters");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Параметры запроса");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPPY6BMTIQREWFJZGORR5J4HEGU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Generated ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сгенерированный идентификатор");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPX3CGMWDZZDL5OAGXU4R6FSQUY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод завершения секции профилирования кода. Должен использоваться в паре с enterTimingSection.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQ3DRRAXAFBERPCAY4JJPZLNZMM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Имя текущего SCP");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQ4WEOTGQ5VHAVIHLAL3TK2UFGM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
	}

	@SuppressWarnings("unused")
	private static void loadStrings3(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Дефиниция главной таблицы");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQ5Q67SDNFBCXDMPBHMNXWNUPEM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Класс, представляющий API для работы с окружением исполняемого приложения (Application runtime environment - ARTE)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQ6JTWBUYR5DL3APNGBUXQGHYZA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Объект, содержащий результат преобразования");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQ7YCGY4GLRG4BFKCUK7VEFMOKQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ID of the main entity");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Идентификатор главной сущности");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQCFHIORGX5HR5C4VATJQDF73DE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Локаль клиента");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQFKIIFTZ5BGXHAVUQSGYM2RL54"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод получения типа среды выполнения (проводник, веб и т.д.) текущего клиента");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQMGVEJX4WJE2LKSMLG56VJIYDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Savepoint ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид. контрольной точки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQMJEPHQFM5EQVE5WTBDLQKDPPU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Class of the result being returned");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Класс возвращаемого результата");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQNKJBWTUDRDJ7PMDCPB6GZ2OVA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Result returned by the service");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Результат, возвращенный сервисом");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQSIMOH3YMNANLKXC57NHIRONBY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Результат проверки: true - обработка прервана, false - не прервана");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQVXKNOPUZBDRXCALRS7YBY2VTI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Имя вызывающего SCP");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQXWWCV35JJHFRBVOZUY4BCB5Y4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Идентификатор сущности по которой запрашивается информацию о блокировке");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsR3GP6JWBRRFIJEEYLWEC5JHZOI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Invokes the system service");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод вызова сервиса системы");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsR453RWT7RVBZRJPJQ3JRGD6C54"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Преобразует SQML в SQL");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsR5LU7MCFVNBZHGPHXI5JDDCMPI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Исключение, генерируемое в случае обнаружения прерывания запроса");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsR5SZNBC7ORCKTDV5U43V2UTUPA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Response timeout");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Таймаут получения ответа");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRDYNDJP5CFDYHHLESO3AHOAPRU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Name of the service being invoked");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Имя вызываемого сервиса");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRFGEWM5ICBCLHLJAWZWTOAPRAU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Check result");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Результат проверки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRNEPW75VSVAVPATOKIXCISFGUU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод получения кэша для пользовательских данных");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRSWDVQW2SFFSTCTOU7YMAUNP7Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Вид секции профилирования");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRYO5SVOLPVCD5GHEX2K2IE5P4Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Имя вызываемого метода");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsS3NHCSGIL5GGVJ6AXX3AF3V42U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Объект, содержащий информацию о владельце сессионной блокировки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsS3NLDZHOEBHKXPPGKWW5P4YIKA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Предыдущая локаль клиента");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSBQE4SX6QNGJ7EDVOBXF6Q4LJA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Вид секции профилирования");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSETNNCCUYZCYVLXWU2ZZE5U45Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Список дополнительных точек доступа к сервису, используемых для соединения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSFFPB6SPNZATZIRUPWSPPYNFIQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Идентификатор покидаемой секции кэширования. Если не совпадает с идентификатором последней секции кэширования - генерирует исключение т.к. это означает что парность вызовов методов enterCachingSection и leaveCachingSection нарушена");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSGFBGB34NJE3NCIVZC355UKJUI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Timestamp.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Временная отметка.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSGXOA3Q6DNH3XMTUUVUEQUWX2Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Десериализуемое значение в строковом виде");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSLYS4BT6XFGKRO7CTBCEN7YP6I"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Sets expected entity cache size. If cache size will exceed this value, warning will be printed to trace.<br>\nUse -1 to set unlimited size. Effect of the call is bound to current ARTE request and will be reset to default<br>\nbefore next request. After printing warning value will be automatically set to -1.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Устанавливает ожидаемый размер кеша ARTE. Если размер кеша превысит данное значение, в трассу будет выведен варнинг.<br>\nУстановленное значение действует в пределах запроса к ARTE. Используйте -1 для неограниченного размера.<br>\nПосле вывода варнинга значение будет сброшено в -1. Перед началом обработки следующего запроса значение будет сброшено<br>\nв значение по умолчанию.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSMATSCWHDBEHBL4KL7LETYW6IQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"XML object containing the data of the request to the service");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"XML-объект, содержащий данные запроса к сервису");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSMK67OAMVJATFHC5UHPPBLY3ZY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Устанавливаемая локаль клиента");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSQDGNOHZ2RD4DJEKS52Y2YHWHM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Returns the DB system date.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Возвращает системную дату БД.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSSJAH4QR65CU7BDTXGFMOIYCSI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"User name");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Имя пользователя");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSU7DYTXMUFGGPA2MGLULST5344"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Целевой AADC узел");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSU7KIQKLHBGZ5DW4VOEXEIFCPE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод десериализации строкого значения в объект");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSYKZ562GBVAELDQPLFFSNRI3PM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Количество миллисекунд с начала эпохи в нулевом часовом поясе (UTC)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsT2HYY44O55BHJGYQGAG4DQGUD4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Name of the service being invoked");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Имя вызываемого сервиса");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsT3FN5CS4GVE3RAOHAS2DLLFBNE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Response timeout");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Таймаут получения ответа");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsT5NA3YUMNZCXROOE45FEP6UGGI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Освобождаемый BLOB");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTB7OHLHMNZHUFN53FZFV7J6CMQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Response timeout");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Таймаут получения ответа");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTC6OSKJKCFHZPPKTSLLAZJVJNM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Время поддержания соединения в активном состоянии (keep-alive)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTU2NM7LUARB4RAJHDCZ4IMRW3Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Имя вызывающего SCP");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsU3UY55FDSBEIDMKXK45YRJOWFU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Table ID.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид. таблицы.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUHRTN2T3M5BJFBKDWTCMYD4Q4Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Фиксирует изменения в базе данных.\nПеред выполнением данного метода все изменения в кэше ARTE записываются в БД методом updateDatabase, затем транзакция фиксируется.\nЗатем вызываются обработчики события afterCommit");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUJ7LU74JJZCW7JYZIW62PGPWL4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод получения экземпляра прикладного класса по PID-у сущности и идентификатору прикладного класса. Данный метод не гарантирует существования сущности в БД - может быть создана сущность-заглушка, подробнее см. в описании класса Entity");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsULYYQMYQ6BGBBP7OOJWODWZOBM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"SQL Character Large Object.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"SQL Character Large Object.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsURVXGKZS3BBXPHHR5SOT5XKW5I"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ID of the system to which the service being called belongs");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Идентификатор системы, которой принадлежит вызываемый сервис");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUSACZ2RZO5HBPIBVNFWKSZUU74"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"During applying class loading profile \'%s\' %s errors occurred");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"При применении профиля загрузки классов \'%s\' возникло %s ошибок");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUUA7AYFIHJCHHKTCOSTLHLV6LI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Class found.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Найденный класс.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUUUQIOI6HVBPHC5JTU2IMJW4E4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Error applying the class loading profile: ");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка при применении профиля загрузки классов: ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUXFPP4SVCNHINKBQMTC56H544E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Invokes the system service");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод вызова сервиса системы");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsV2TPGTFO6FBY7G43SGYACISG4M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"SQL Binary Large Object.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"SQL Binary Large Object.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsV454YYE4XRH3XH4QAAXLU22M2Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод входа в новую секцию кэширования. Должен использоваться в паре с методом leaveCachingSection");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVAAR2MFWSFFSZIK7LIYDQAJ7JA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Дефиниция главной таблицы");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVCCUXVMQVRBNJOSXESQLR7PYLU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Экземпляр кэша для пользовательских данных");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVMQ7UQXMYRERLGF4TF6W5JM23E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Check result");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Результат проверки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVNQ54NQDVREITHJ2Y25WXM4BUI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод получения страны текущего клиента");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVTFKGYDC4VB3JBCYD45YKF4SB4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Result returned by the service");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Результат, возвращенный сервисом");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVU2TRICNSFCKXLW7GAAXTXFOQ4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Returns the nesting level for the specified savepoint.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Возвращает уровень вложенности указанной контрольной точки.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVYWDGI6A6BAVFNREGHQAVRYM5U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Creates the temporary storage for BLOB.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Создает временное хранилище для BLOB.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsW24YM4OA3NE35HWL4IDJJW2Q6I"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Имя станции текущего пользователя");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsW6U6FMISJRA3DHMBDXZCDS6IX4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Экземпляр созданного объекта");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWBXKEEKEQNEPTHBFR2LMMZFSF4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Откатывает изменения в БД, внесенные в рамках текущей транзакции после установки указанной точки отката. Метод сначала выполняет подготовленные запросы (см. sendBatches), затем откатывает транзакцию БД до указанной точки отката, затем обеспечивает консистентность кэша и базы данных:\n<ol>\n<li>Сначала вызывается обработчик Entity::onAutonomousStoreAfterRollback, позволяющий сохранить данную сущность в рамках автономной транзакции</li>\n<li>В случае, если метод onAutonomousStoreAfterRollback вернул false, то сущности, созданные в рамках данной транзакции после точки отката, сбрасываются из кэша (см. Entity::discard), для прочих затронутых после установки точки отката сущностей вызывается метод Entity::purgePropCacheOnRollback, таким образом, последующие обращения к свойствам таких сущностей приведут к чтению из БД.</li>\n</ol>\nЗатем вызываются обработчики события afterRollbackToSavepoint(id)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWCRSNFCRZ5G6BLYZNOTXOD7DOI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Result returned by the service");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Результат, возвращенный сервисом");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWDDES66YU5HYJEHN3SZ456CCRM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Шифрованные данные");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWOIQ4AXICBHWRLBCWJ3XH2UWPU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Предыдущий язык клиента");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWQ7SADO5UJDUBNSETPPIICSR6M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод вызова внутреннего сервиса системы. Внутренний сервис - это сервис, принадлежащий системе #1.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWRHLDB6YDFAZZHL5MEIT5PDAXQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Объект, содержащий результат преобразования");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWWQP22SU2RHHNAUCGSIDREH65U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"System ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Идентификатор системы");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWY2HCS75QNFLRENLMUGVNNMGWU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Initial data to be stored in the creating CLOB");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Начальные данные для хранения в создаваемом CLOB\'е");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWYG3I3CMMJCIPESOUMARJU5QMA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод получения системного времени в виде количества миллисекунд с начала эпохи (Epoch)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsX5KRL5N2XNFMDIDTUYE43NY534"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"XML object containing the data of the request to the service");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"XML-объект, содержащий данные запроса к сервису");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXNTJBPDOGVAHHNJAXNIE4ZGHDU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Освобождает временные хранилища для CLOB");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXNXLPMMXAJBOBPRXPQZJI7JDH4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Response timeout");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Таймаут ожидания ответа");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXO6WXGM5LRCAHJ4SJVXULGJRNM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Name of the service being invoked");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Имя вызываемого сервиса");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXOWP2OWXXZD5NM2SDXLCKDEUR4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Дополнительный ключ для идентификации профилируемого кода");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXSSL5DW5FFDNLDGHDAXX36KUXE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Nesting level for the current savepoint.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Уровень вложенности текущей контрольной точки.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXZR2YR7Y2JABNLG7Y7NFKPQWKI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Creates the temporary storage for CLOB.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Создает временное хранилище для CLOB.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsY7NZSVPVW5AI7NGJVDYWRRJTYM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Список значений параметров метода (в порядке объявления методом)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYHMUKPNVPFELBKGMYKSAARVZUM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод создания нового объекта указанного класса");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYMZAELVMVFGTTCGK23NF5MSNK4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Gets the DB session ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод получения идентификатора сессии в БД");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYPQLXYCBVFEJJOZTUF7CO4DTQE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Invokes the system service");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод вызова сервиса системы");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYRZWZ6GC4RDILIMRXEPJLA6HPA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод проверки что в данный момент обрабатывается EAS-запрос");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYV2T7VRTQFGSNG6TPIWPBOWDAE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Время поддержания соединения в активном состоянии (keep-alive)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYV5JC4WJA5GTZFQFJXNK3A2SWE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"XML object containing the data of the request to the service");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"XML-объект, содержащий данные запроса к сервису");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYVYO2DKR2BE4TIWLLUL6QSUJP4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Список типов параметров метода (в порядке объявления методом)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZ2W5NXSEDVFANKSP3XVJZUKWUA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Sleep time.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Время приостановки.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZ3JEPFSAP5CSHNG2TSKQBT7BCU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Имя вызываемого метода");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZ3N6LCUAP5EGXOIMNUJGAEH4CM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Предыдущая страна клиента");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZ5NFICBMARAILHMDKX62HS7ZFA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Name of the service being invoked");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Имя вызываемого сервиса");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZ6SYSVXKI5BP3ENYVSKODTJTME"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Nesting level for the savepoint.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Уровень вложенности контрольной точки.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZDPNCRXTNNBLXLW4C2VOJNSTMQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Страна клиента");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZDUZMXR6GRARBDXJJXRPJ7IM3M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Время поддержания соединения в активном состоянии (keep-alive)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZEHWZWI6H5H2TPT7BFWZP33EDQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Преобразуемый SQML");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZG2M2LEQKFAEVOM4IR5GECEPF4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Список освобождаемых BLOB");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZNIYCDLR5VB3BBD522Y3CAAQRI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Результат, возвращенный вызванным методом");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZNLIFHBHHBGY5EQASB22CA6URM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Обновляет БД. Для всех модифицированных сущностей, находящихся в кэше ARTE вызывается метод Entity::update (Entity::create, если сущность еще не помещена в БД). Не записываются в БД сущности, для которых сброшен флаг isAutoUpdateEnabled (см. метод Entity::setIsAutoUpdateEnabled). Данный метод не фиксирует транзакцию, таким образом записанные в БД данные могут быть зафиксированы или откачены позднее.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZPVSOOUF6VBJFP2LHKZCZOAA2A"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Name of the service being invoked");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Имя вызываемого сервиса");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZUMAUDE67BAXDO27V5MXOIWTSQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Saves the current state as the savepoint.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сохраняет текущее состояние как контрольную точку.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZVMRV2JOFNHXNGHEC52UEBGPZA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Check result");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Результат проверки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZX54E5S3ZRABHG7MNFK2KCEIRI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(Arte - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbpdcArte______________________"),"Arte - Localizing Bundle",$$$items$$$);
}
