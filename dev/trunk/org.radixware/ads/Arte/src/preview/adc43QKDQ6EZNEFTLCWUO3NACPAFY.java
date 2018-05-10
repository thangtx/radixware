
/* Radix::Arte::AadcManager - Server Executable*/

/*Radix::Arte::AadcManager-Server Dynamic Class*/

package org.radixware.ads.Arte.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::AadcManager")
public published class AadcManager  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return AadcManager_mi.rdxMeta;}

	/*Radix::Arte::AadcManager:Nested classes-Nested Classes*/

	/*Radix::Arte::AadcManager:EventHandler-Server Dynamic Class*/


	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::AadcManager:EventHandler")
	 static class EventHandler  implements org.radixware.kernel.server.arte.IArteEventListener,org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


		/**Metainformation accessor method*/
		@SuppressWarnings("unused")
		public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return AadcManager_mi.rdxMeta_adc2H5ONF6HSZHWPDZR4CBBEPWJP4;}

		/*Radix::Arte::AadcManager:EventHandler:Nested classes-Nested Classes*/

		/*Radix::Arte::AadcManager:EventHandler:Properties-Properties*/





























		/*Radix::Arte::AadcManager:EventHandler:Methods-Methods*/

		/*Radix::Arte::AadcManager:EventHandler:afterRequestProcessing-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::AadcManager:EventHandler:afterRequestProcessing",line=69)
		public published  void afterRequestProcessing () {
			finishSessionLocks();

		}

		/*Radix::Arte::AadcManager:EventHandler:beforeReleaseUnload-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::AadcManager:EventHandler:beforeReleaseUnload",line=78)
		public published  void beforeReleaseUnload () {
			;
		}

		/*Radix::Arte::AadcManager:EventHandler:beforeRequestProcessing-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::AadcManager:EventHandler:beforeRequestProcessing",line=86)
		public published  void beforeRequestProcessing () {
			;
		}

		/*Radix::Arte::AadcManager:EventHandler:afterDbCommit-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::AadcManager:EventHandler:afterDbCommit",line=94)
		public published  void afterDbCommit () {
			commitTranLocks();
		}

		/*Radix::Arte::AadcManager:EventHandler:afterDbRollback-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::AadcManager:EventHandler:afterDbRollback",line=102)
		public published  void afterDbRollback (java.sql.Savepoint savepoint, Str savepointId, long nesting) {
			rollbackTranLocks(nesting);

		}

		/*Radix::Arte::AadcManager:EventHandler:beforeDbCommit-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::AadcManager:EventHandler:beforeDbCommit",line=111)
		public published  void beforeDbCommit () {
			;
		}

		/*Radix::Arte::AadcManager:EventHandler:beforeDbRollback-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::AadcManager:EventHandler:beforeDbRollback",line=119)
		public published  void beforeDbRollback (java.sql.Savepoint savepoint, Str savepointId, long nesting) {
			;
		}

		/*Radix::Arte::AadcManager:EventHandler:onDbCommitError-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::AadcManager:EventHandler:onDbCommitError",line=127)
		public published  void onDbCommitError (java.sql.SQLException ex) {
			rollbackTranLocks(-1);

		}

		/*Radix::Arte::AadcManager:EventHandler:onDbRollbackError-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::AadcManager:EventHandler:onDbRollbackError",line=136)
		public published  void onDbRollbackError (java.sql.Savepoint savepoint, Str savepointId, long nesting, java.sql.SQLException ex) {
			;
		}


	}

	/*Radix::Arte::AadcManager:TranLockInfo-Server Dynamic Class*/


	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::AadcManager:TranLockInfo")
	 static class TranLockInfo  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


		/**Metainformation accessor method*/
		@SuppressWarnings("unused")
		public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return AadcManager_mi.rdxMeta_adcK37YO3AN3FAMJMP23SXE4U2NP4;}

		/*Radix::Arte::AadcManager:TranLockInfo:Nested classes-Nested Classes*/

		/*Radix::Arte::AadcManager:TranLockInfo:Properties-Properties*/

		/*Radix::Arte::AadcManager:TranLockInfo:lockId-Dynamic Property*/



		protected long lockId=0;











		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::AadcManager:TranLockInfo:lockId",line=176)
		  long getLockId() {
			return lockId;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::AadcManager:TranLockInfo:lockId",line=182)
		   void setLockId(long val) {
			lockId = val;
		}

		/*Radix::Arte::AadcManager:TranLockInfo:savePointLevel-Dynamic Property*/



		protected long savePointLevel=0;











		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::AadcManager:TranLockInfo:savePointLevel",line=204)
		  long getSavePointLevel() {
			return savePointLevel;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::AadcManager:TranLockInfo:savePointLevel",line=210)
		   void setSavePointLevel(long val) {
			savePointLevel = val;
		}









































		/*Radix::Arte::AadcManager:TranLockInfo:Methods-Methods*/

		/*Radix::Arte::AadcManager:TranLockInfo:TranLockInfo-User Method*/

		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::AadcManager:TranLockInfo:TranLockInfo",line=259)
		  TranLockInfo (long lockId, long spLevel) {
			lockId = lockId;
			savePointLevel = spLevel;

		}


	}

	/*Radix::Arte::AadcManager:Properties-Properties*/

	/*Radix::Arte::AadcManager:tranLocks-Dynamic Property*/



	protected static java.util.Map<org.radixware.kernel.server.types.Pid,org.radixware.ads.Arte.server.AadcManager.TranLockInfo> tranLocks=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::AadcManager:tranLocks",line=288)
	private static final  java.util.Map<org.radixware.kernel.server.types.Pid,org.radixware.ads.Arte.server.AadcManager.TranLockInfo> getTranLocks() {
		return tranLocks;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::AadcManager:tranLocks",line=294)
	private static final   void setTranLocks(java.util.Map<org.radixware.kernel.server.types.Pid,org.radixware.ads.Arte.server.AadcManager.TranLockInfo> val) {
		tranLocks = val;
	}

	/*Radix::Arte::AadcManager:sessLocks-Dynamic Property*/



	protected static java.util.Map<org.radixware.kernel.server.types.Pid,Int> sessLocks=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::AadcManager:sessLocks",line=316)
	private static final  java.util.Map<org.radixware.kernel.server.types.Pid,Int> getSessLocks() {
		return sessLocks;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::AadcManager:sessLocks",line=322)
	private static final   void setSessLocks(java.util.Map<org.radixware.kernel.server.types.Pid,Int> val) {
		sessLocks = val;
	}

	/*Radix::Arte::AadcManager:eventHandler-Dynamic Property*/



	protected static org.radixware.ads.Arte.server.AadcManager.EventHandler eventHandler=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::AadcManager:eventHandler",line=344)
	private static final  org.radixware.ads.Arte.server.AadcManager.EventHandler getEventHandler() {
		return eventHandler;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::AadcManager:eventHandler",line=350)
	private static final   void setEventHandler(org.radixware.ads.Arte.server.AadcManager.EventHandler val) {
		eventHandler = val;
	}

	/*Radix::Arte::AadcManager:evWait-Event Code Property*/



	protected static Str evWait=(Str)org.radixware.kernel.common.defs.value.ValAsStr.fromStr("mlbadc43QKDQ6EZNEFTLCWUO3NACPAFY-mlsHPHFNNG7MRADTAU3BDMKRQOKFI",org.radixware.kernel.common.enums.EValType.STR);











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::AadcManager:evWait",line=372)
	private static final  Str getEvWait() {
		return evWait;
	}

	/*Radix::Arte::AadcManager:suppressWaitWarning-Dynamic Property*/



	protected static boolean suppressWaitWarning=false;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::AadcManager:suppressWaitWarning",line=394)
	private static final  boolean getSuppressWaitWarning() {
		return suppressWaitWarning;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::AadcManager:suppressWaitWarning",line=400)
	private static final   void setSuppressWaitWarning(boolean val) {
		suppressWaitWarning = val;
	}























































	/*Radix::Arte::AadcManager:Methods-Methods*/

	/*Radix::Arte::AadcManager:commitTranLocks-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::AadcManager:commitTranLocks",line=463)
	private static  void commitTranLocks () {
		if (tranLocks == null || tranLocks.isEmpty())
		    return;
		long scn; 
		try (CurScnCursor c = CurScnCursor.open()) {
		    c.next();
		    scn = c.curScn.longValue();
		}

		for (java.util.Map.Entry<Types::Pid, AadcManager.TranLockInfo> e : tranLocks.entrySet()) {
		    Arte.getInstance().getInstance().getAadcManager().commitLock(e.getKey(), org.radixware.kernel.server.instance.aadc.AadcManager.LOCK_TYPE_TRAN, e.getValue().lockId, scn);
		}

		tranLocks.clear();

	}

	/*Radix::Arte::AadcManager:acquireTranLock-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::AadcManager:acquireTranLock",line=483)
	public static published  void acquireTranLock (org.radixware.kernel.server.types.Pid pid, int timeoutMs) throws org.radixware.ads.Exceptions.server.AadcLockTimeoutError {
		Int myMemberId = getMyMemberId();
		if (myMemberId == null)
		    return;
		if (eventHandler == null) {
		    eventHandler = new EventHandler();
		    Arte.registerArteEventListener(eventHandler);
		}
		if (tranLocks == null)
		    tranLocks = new java.util.HashMap<>();
		else if (tranLocks.containsKey(pid))
		    return;
		    
		Int lockId = acquireLock(myMemberId, pid, org.radixware.kernel.server.instance.aadc.AadcManager.LOCK_TYPE_TRAN, timeoutMs);
		if (lockId != null)
		    tranLocks.put(pid, new TranLockInfo(lockId.longValue(), Arte.getSavepointNesting()));
	}

	/*Radix::Arte::AadcManager:rollbackTranLocks-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::AadcManager:rollbackTranLocks",line=504)
	private static  void rollbackTranLocks (long spLevel) {
		java.util.Iterator<java.util.Map.Entry<Types::Pid, AadcManager.TranLockInfo>> it = tranLocks.entrySet().iterator();
		while (it.hasNext()) {
		    java.util.Map.Entry<Types::Pid, AadcManager.TranLockInfo> e = it.next();
		    if (e.getValue().savePointLevel >= spLevel) {
		        Arte.getInstance().getInstance().getAadcManager().rollbackLock(e.getKey(), org.radixware.kernel.server.instance.aadc.AadcManager.LOCK_TYPE_TRAN, e.getValue().lockId);
		        it.remove();
		    }
		}



	}

	/*Radix::Arte::AadcManager:finishSessionLocks-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::AadcManager:finishSessionLocks",line=521)
	private static  void finishSessionLocks () {
		if (sessLocks == null || sessLocks.isEmpty())
		    return;
		long scn; 
		try (CurScnCursor c = CurScnCursor.open()) {
		    c.next();
		    scn = c.curScn.longValue();
		}

		for (java.util.Map.Entry<Types::Pid, Int> e : sessLocks.entrySet()) {
		    Arte.getInstance().getInstance().getAadcManager().commitLock(e.getKey(), org.radixware.kernel.server.instance.aadc.AadcManager.LOCK_TYPE_SESS, e.getValue().longValue(), scn);
		}

		sessLocks.clear();

	}

	/*Radix::Arte::AadcManager:acquireSessionLock-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::AadcManager:acquireSessionLock",line=541)
	public static published  void acquireSessionLock (org.radixware.kernel.server.types.Pid pid, int timeoutMs) throws org.radixware.ads.Exceptions.server.AadcLockTimeoutError {
		Int myMemberId = getMyMemberId();
		if (myMemberId == null)
		    return;
		if (eventHandler == null) {
		    eventHandler = new EventHandler();
		    Arte.registerArteEventListener(eventHandler);
		}
		if (sessLocks == null)
		    sessLocks = new java.util.HashMap<>();
		else if (sessLocks.containsKey(pid))
		    return;
		    
		Int lockId = acquireLock(myMemberId, pid, org.radixware.kernel.server.instance.aadc.AadcManager.LOCK_TYPE_SESS, timeoutMs);
		if (lockId != null)
		    sessLocks.put(pid, lockId);

	}

	/*Radix::Arte::AadcManager:acquireSessionLock-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::AadcManager:acquireSessionLock",line=563)
	public static published  void acquireSessionLock (org.radixware.ads.Types.server.Entity obj, int timeoutMs) throws org.radixware.ads.Exceptions.server.AadcLockTimeoutError {
		acquireSessionLock(obj.getPid(), timeoutMs);

	}

	/*Radix::Arte::AadcManager:acquireTranLock-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::AadcManager:acquireTranLock",line=571)
	public static published  void acquireTranLock (org.radixware.ads.Types.server.Entity obj, int timeoutMs) throws org.radixware.ads.Exceptions.server.AadcLockTimeoutError {
		acquireTranLock(obj.getPid(), timeoutMs);

	}

	/*Radix::Arte::AadcManager:getCurrentLag-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::AadcManager:getCurrentLag",line=579)
	public static published  int getCurrentLag () {
		try {
		    return Arte.getInstance().getInstance().getAadcManager().getCurrentLag();
		} catch (Exceptions::SQLException e) {
		    throw new DatabaseError(e);
		}
	}

	/*Radix::Arte::AadcManager:getMyMemberId-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::AadcManager:getMyMemberId",line=590)
	public static published  Int getMyMemberId () {
		return Arte.getInstance().getInstance().getAadcInstMemberId();
	}

	/*Radix::Arte::AadcManager:acquireLock-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::AadcManager:acquireLock",line=597)
	private static  Int acquireLock (Int myMemberId, org.radixware.kernel.server.types.Pid pid, byte type, int timeoutMs) throws org.radixware.ads.Exceptions.server.AadcLockTimeoutError {
		long endTime = Utils::Timing.getCurrentMillis() + timeoutMs;
		int waits = 0;
		Int lockId;
		try {
		    while (true) {
		        AadcStateStmt state = AadcStateStmt.execute(myMemberId);
		        final Str tranDbId;
		        switch (type) {
		            case org.radixware.kernel.server.instance.aadc.AadcManager.LOCK_TYPE_TRAN:
		                tranDbId = state.xid;
		                break;
		            case org.radixware.kernel.server.instance.aadc.AadcManager.LOCK_TYPE_SESS:
		                tranDbId = Str.valueOf(Arte.getInstance().getDbSid()) + "." + Str.valueOf(Arte.getInstance().getDbSerial());
		                break;
		            default:
		                tranDbId = null;
		        }
		        lockId = Arte.getInstance().getInstance().getAadcManager().acquireLock(pid, type, Utils::Nvl.get(state.theirScn, Int.MIN_VALUE).longValue(), tranDbId);
		        if (lockId != null)
		            break;
		        if (++waits == 1 && !suppressWaitWarning)
		            Trace.put(evWait, pid.getTable().getName() + "[" + pid.toString() + "]");
		        if (Utils::Timing.getCurrentMillis() > endTime)
		            throw new AadcLockTimeoutError(pid);
		        try {
		            Thread.sleep(1000);
		        } catch (Exceptions::InterruptedException e) {
		            throw new AadcLockTimeoutError(pid);
		        }
		    };
		} finally {
		    suppressWaitWarning = false;
		}
		return lockId;
	}

	/*Radix::Arte::AadcManager:acquireAutonomousLock-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::AadcManager:acquireAutonomousLock",line=637)
	public static published  Int acquireAutonomousLock (org.radixware.kernel.server.types.Pid pid, int timeoutMs) throws org.radixware.ads.Exceptions.server.AadcLockTimeoutError {
		Int myMemberId = getMyMemberId();
		if (myMemberId == null)
		    return null;

		return acquireLock(myMemberId, pid, org.radixware.kernel.server.instance.aadc.AadcManager.LOCK_TYPE_TRAN, timeoutMs);

	}

	/*Radix::Arte::AadcManager:commitAutonomousLock-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::AadcManager:commitAutonomousLock",line=649)
	public static published  void commitAutonomousLock (org.radixware.kernel.server.types.Pid pid, Int lockId) {
		if (lockId == null) 
		    return;

		long scn; 
		try (CurScnCursor c = CurScnCursor.open()) {
		    c.next();
		    scn = c.curScn.longValue();
		}

		Arte.getInstance().getInstance().getAadcManager().commitLock(pid, org.radixware.kernel.server.instance.aadc.AadcManager.LOCK_TYPE_TRAN, lockId.longValue(), scn);


	}

	/*Radix::Arte::AadcManager:acquireAutonomousLock-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::AadcManager:acquireAutonomousLock",line=667)
	public static published  Int acquireAutonomousLock (org.radixware.ads.Types.server.Entity obj, int timeoutMs) throws org.radixware.ads.Exceptions.server.AadcLockTimeoutError {
		return acquireAutonomousLock(obj.getPid(), timeoutMs);

	}

	/*Radix::Arte::AadcManager:commitAutonomousLock-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::AadcManager:commitAutonomousLock",line=675)
	public static published  void commitAutonomousLock (org.radixware.ads.Types.server.Entity obj, Int lockId) {
		commitAutonomousLock(obj.getPid(), lockId);

	}

	/*Radix::Arte::AadcManager:isInAadc-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::AadcManager:isInAadc",line=683)
	public static published  boolean isInAadc () {
		return getMyMemberId() != null;
	}

	/*Radix::Arte::AadcManager:putTranLockPayload-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::AadcManager:putTranLockPayload",line=690)
	public static published  void putTranLockPayload (org.radixware.kernel.server.types.Pid pid, java.util.Map<Str,Str> payload) {
		if (!isInAadc())
		    return;
		Arte.getInstance().getInstance().getAadcManager().putLockPayload(pid, org.radixware.kernel.server.instance.aadc.AadcManager.LOCK_TYPE_TRAN, payload);

	}

	/*Radix::Arte::AadcManager:readTranLockPayload-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::AadcManager:readTranLockPayload",line=700)
	public static published  boolean readTranLockPayload (org.radixware.kernel.server.types.Pid pid, java.util.Map<Str,Str> payload) {
		if (!isInAadc())
		    return false;
		return Arte.getInstance().getInstance().getAadcManager().readLockPayload(pid, org.radixware.kernel.server.instance.aadc.AadcManager.LOCK_TYPE_TRAN, payload);

	}

	/*Radix::Arte::AadcManager:putTranLockPayload-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::AadcManager:putTranLockPayload",line=710)
	public static published  void putTranLockPayload (org.radixware.ads.Types.server.Entity obj, java.util.Map<Str,Str> payload) {
		putTranLockPayload(obj.getPid(), payload);

	}

	/*Radix::Arte::AadcManager:readTranLockPayload-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::AadcManager:readTranLockPayload",line=718)
	public static published  boolean readTranLockPayload (org.radixware.ads.Types.server.Entity obj, java.util.Map<Str,Str> payload) {
		return readTranLockPayload(obj.getPid(), payload);

	}

	/*Radix::Arte::AadcManager:getMyMember-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::AadcManager:getMyMember",line=726)
	public static published  org.radixware.kernel.common.enums.EAadcMember getMyMember () {
		Int myId = getMyMemberId();
		if (myId == null) {
		    return null;
		} else if (myId == 1) {
		    return AadcMember:FIRST;
		} else {
		    return AadcMember:SECOND;
		}
	}

	/*Radix::Arte::AadcManager:suppressWaitWarning-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Arte::AadcManager:suppressWaitWarning",line=740)
	public static published  void suppressWaitWarning () {
		suppressWaitWarning = true;

	}


}

/* Radix::Arte::AadcManager - Server Meta*/

/*Radix::Arte::AadcManager-Server Dynamic Class*/

package org.radixware.ads.Arte.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class AadcManager_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("adc43QKDQ6EZNEFTLCWUO3NACPAFY"),"AadcManager",null,

						org.radixware.kernel.common.enums.EClassType.DYNAMIC,
						null,
						null,
						null,

						/*Radix::Arte::AadcManager:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::Arte::AadcManager:tranLocks-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFD4TUNFT3ZGDLCPMOYN3VUYRGI"),"tranLocks",null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Arte::AadcManager:sessLocks-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdJ67DBTDO4ZFSLL25VLWYPT6CP4"),"sessLocks",null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Arte::AadcManager:eventHandler-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd3MHCBC3EGNEQRLFQ2XS6DF6HWE"),"eventHandler",null,org.radixware.kernel.common.enums.EValType.USER_CLASS,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Arte::AadcManager:evWait-Event Code Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdYGESL3YDYNDWHHBJKX2I6X334M"),"evWait",null,org.radixware.kernel.common.enums.EValType.STR,null,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("mlbadc43QKDQ6EZNEFTLCWUO3NACPAFY-mlsHPHFNNG7MRADTAU3BDMKRQOKFI")),org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::Arte::AadcManager:suppressWaitWarning-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd7FK6YSIJ3VF3RNFS2F5DXLWAVU"),"suppressWaitWarning",null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::Arte::AadcManager:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthOFV24DFZQBD47P7NOP6JVKG5XI"),"commitTranLocks",true,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthBQGAS7MEJRCOJMJZGHM7PRM3CQ"),"acquireTranLock",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pid",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprGWWAFTHE7VHU7DQUBCIF7DXMIQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("timeoutMs",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCRIFEOQ775CLXLV3LR6QOYMMAM"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth3IK4HDAXIRDUXDMYKKIX76MX7Y"),"rollbackTranLocks",true,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("spLevel",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5XD6IKIZYFET7GGOK2VJTWQGIY"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthI7IVQ2ZN5JETZLMWWMBM7CX3O4"),"finishSessionLocks",true,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth3ECTAFGXPJHCDOPRCCV4FJP6EA"),"acquireSessionLock",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pid",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprGWWAFTHE7VHU7DQUBCIF7DXMIQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("timeoutMs",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCRIFEOQ775CLXLV3LR6QOYMMAM"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthHZZZ6JTBXVHVFAVKRSHTWXRVFI"),"acquireSessionLock",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("obj",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprGWWAFTHE7VHU7DQUBCIF7DXMIQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("timeoutMs",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCRIFEOQ775CLXLV3LR6QOYMMAM"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthDVLXQL47AVF7JHSWXWKSAM56C4"),"acquireTranLock",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("obj",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprGWWAFTHE7VHU7DQUBCIF7DXMIQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("timeoutMs",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCRIFEOQ775CLXLV3LR6QOYMMAM"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthVR6EGK4PARFLLLWQZTCI6AAET4"),"getCurrentLag",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthQ6D343344ZADTBY4POLA3RA4YY"),"getMyMemberId",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.INT),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthMW7RY7TVHRDGHCPJOBHNSHE5FU"),"acquireLock",true,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("myMemberId",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprDQ5KBMIY7NEWBOLKV2PN7J2YXA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pid",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprGWWAFTHE7VHU7DQUBCIF7DXMIQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("type",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprZDNGHT33SRFB3NLABEP6LJODLY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("timeoutMs",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCRIFEOQ775CLXLV3LR6QOYMMAM"))
								},org.radixware.kernel.common.enums.EValType.INT),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthE2UOOKZ6FJFERIKKFDZLHGTWZM"),"acquireAutonomousLock",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pid",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprGWWAFTHE7VHU7DQUBCIF7DXMIQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("timeoutMs",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCRIFEOQ775CLXLV3LR6QOYMMAM"))
								},org.radixware.kernel.common.enums.EValType.INT),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthN2FB4GGV2NGLVIVVAFQHVLOYSU"),"commitAutonomousLock",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pid",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprGWWAFTHE7VHU7DQUBCIF7DXMIQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("lockId",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprA46RDALEXJHKJMAU4K2I2WHIVE"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthIGWGQFKEFFA5BGXV7C3CYZROIA"),"acquireAutonomousLock",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("obj",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprGWWAFTHE7VHU7DQUBCIF7DXMIQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("timeoutMs",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCRIFEOQ775CLXLV3LR6QOYMMAM"))
								},org.radixware.kernel.common.enums.EValType.INT),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthYJL7DWZ4VRGUTBL5DBFRDHSDPM"),"commitAutonomousLock",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("obj",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprGWWAFTHE7VHU7DQUBCIF7DXMIQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("lockId",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprA46RDALEXJHKJMAU4K2I2WHIVE"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthHXY2DWFQAFCULJMEJFA6EBITWY"),"isInAadc",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthHUC5TIDPMVEHLBZCDNN346LKNU"),"putTranLockPayload",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pid",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprGWWAFTHE7VHU7DQUBCIF7DXMIQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("payload",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprV5V5WE2BM5CGNIGPTMA2HP55WM"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthHCJ2WJZSIZBFBOB7IVNZXUOSXI"),"readTranLockPayload",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pid",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprGWWAFTHE7VHU7DQUBCIF7DXMIQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("payload",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprV5V5WE2BM5CGNIGPTMA2HP55WM"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthIKNAFTD7ZFFMVPSLI7LHO57Y5M"),"putTranLockPayload",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("obj",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprGWWAFTHE7VHU7DQUBCIF7DXMIQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("payload",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprV5V5WE2BM5CGNIGPTMA2HP55WM"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth3TK2X4TD5FGTXIJ7TWW2JAK2S4"),"readTranLockPayload",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("obj",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprGWWAFTHE7VHU7DQUBCIF7DXMIQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("payload",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprV5V5WE2BM5CGNIGPTMA2HP55WM"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthHDROLD2DVBBWZEHIQRFK37NDCA"),"getMyMember",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.INT),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth7VJ2AOVN7FDE3LFISYMHWZWYGI"),"suppressWaitWarning",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null)
						},
						null,
						null,null,null,false);
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta_adc2H5ONF6HSZHWPDZR4CBBEPWJP4 = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("adc2H5ONF6HSZHWPDZR4CBBEPWJP4"),"EventHandler",null,

						org.radixware.kernel.common.enums.EClassType.DYNAMIC,
						null,
						null,
						null,

						/*Radix::Arte::AadcManager:EventHandler:Properties-Properties*/
						null,

						/*Radix::Arte::AadcManager:EventHandler:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth2TMBETETMVCFBJUETJX44DZTIE"),"afterRequestProcessing",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth3DAN4ARW65GLHH6G5FWXOZTAYI"),"beforeReleaseUnload",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthDTB2PNAWSRFTJKS3SDNT37RQR4"),"beforeRequestProcessing",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthK6TSB7MSJRCWPD2RUK556BVLPE"),"afterDbCommit",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthMFXZM6O2DZFNBBVP4GVQN5CJ7U"),"afterDbRollback",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("savepoint",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprK5PTJXWA7VDXLAFZSXBMYFUSEI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("savepointId",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprWHO5OT35SZHI7MUZTOUAQZNQBA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("nesting",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprKGA76GORUBFXBMGACBJCJN7JAA"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthJYUTOOHWPNEAPAUGNEHPOJKZ4Q"),"beforeDbCommit",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthZV23TYTY5FGTROAERWUKVHZC3U"),"beforeDbRollback",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("savepoint",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprZQD3WEFEVJG5DFUFV6ASQAYMII")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("savepointId",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5EA3JL7ZTVBDTICSS25MOONOOU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("nesting",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprBV2M6GMCGVABRO3FXSFBVCGX7Y"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthKFP5DBXCV5ANVL7NYGQP2DEYAA"),"onDbCommitError",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("ex",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprYYMGA3QXWJC2LHKVZRIC5DPFXI"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthFBR6IGZX2VA2RHZABIOLGOG6MU"),"onDbRollbackError",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("savepoint",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprFQPD27N54RH5NHLIGGO3BZTDPI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("savepointId",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5PVXYMJ74NAMTMND2NSZHH5IHI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("nesting",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprB3HOYMK5GZHBXHXUUH7OP3AACY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("ex",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCMC7JEKCVFBEDBK3XE5M623PDE"))
								},null)
						},
						null,
						null,null,null,false);
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta_adcK37YO3AN3FAMJMP23SXE4U2NP4 = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("adcK37YO3AN3FAMJMP23SXE4U2NP4"),"TranLockInfo",null,

						org.radixware.kernel.common.enums.EClassType.DYNAMIC,
						null,
						null,
						null,

						/*Radix::Arte::AadcManager:TranLockInfo:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::Arte::AadcManager:TranLockInfo:lockId-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdYHAGTFUNQNG4BDWZHJ3KNGVVLQ"),"lockId",null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE,null,null,null),

								/*Radix::Arte::AadcManager:TranLockInfo:savePointLevel-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdDBT34EKGQNAVVCWV7CN6OC4GIQ"),"savePointLevel",null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE,null,null,null)
						},

						/*Radix::Arte::AadcManager:TranLockInfo:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth7D3IGFVM3NEOZFRE2U7NWAQ6ZU"),"TranLockInfo",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("lockId",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr6CRX3RRATBBWNGUHBRILCIZLGM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("spLevel",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr7ZIIJJXQFBAGZC77BHEZQDUVYM"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE)
						},
						null,
						null,null,null,false);
}

/* Radix::Arte::AadcManager - Localizing Bundle */
package org.radixware.ads.Arte.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class AadcManager - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Таймаут ожидания (мс)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Wait timeout (ms)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3ZLAUGNYC5BBJMTOWG2NFXXXVI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"D:\\RADIX\\dev-trunk\\org.radixware\\ads\\Arte"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Прочитать в");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Read to");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4GAL3NZLFRE47NYXJS3RTHF5VM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"D:\\RADIX\\dev-trunk\\org.radixware\\ads\\Arte"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Блокируемый объект");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Locked object");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4OKUYUDZA5ACXJDMCVIWDOMS3A"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"D:\\RADIX\\dev-trunk\\org.radixware\\ads\\Arte"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Таймаут ожидания (мс)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Wait timeout (ms)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5R7NHCJXR5G6TFDC4RNUAYCENA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"D:\\RADIX\\dev-trunk\\org.radixware\\ads\\Arte"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Данные");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Payload data");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls67AA3O6HKZGDVM7MRF4UYGRAYI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"D:\\RADIX\\dev-trunk\\org.radixware\\ads\\Arte"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Глобальная блокировка объекта в AADC на время сессии");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6NAK5SKPTBE6DEDVFEDZ3G6QUI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"D:\\RADIX\\dev-trunk\\org.radixware\\ads\\Arte"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"PID блокируемого объекта");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"PID of locked object");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6NP6ICDD3VBWDDPZJ5HPW5IIQY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"D:\\RADIX\\dev-trunk\\org.radixware\\ads\\Arte"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Глобальная блокировка объекта в AADC на время транзакции");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6TKBMHHWNFGCFCM27RIFEFGCXI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"D:\\RADIX\\dev-trunk\\org.radixware\\ads\\Arte"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Таймаут ожидания (мс)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Wait timeout (ms)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7HBGESRMWVGJTEZ6EK6ICPCC54"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"D:\\RADIX\\dev-trunk\\org.radixware\\ads\\Arte"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Блокируемый объект");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Locked object");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7L6SUZTCPZGH7OUCOXTY27VAII"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"D:\\RADIX\\dev-trunk\\org.radixware\\ads\\Arte"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Глобальная блокировка объекта в AADC на время транзакции");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAAXBH5NOJRG35LWVTNU6FHTS5I"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"D:\\RADIX\\dev-trunk\\org.radixware\\ads\\Arte"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"PID блокируемого объекта");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"PID of locked object");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsB4XDJHMY7RG6FBOHPK25RTRVVY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"D:\\RADIX\\dev-trunk\\org.radixware\\ads\\Arte"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Глобальная блокировка объекта в AADC на время сессии");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsB5DRI63W4BAYNJZTXXJCSBHO7Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"D:\\RADIX\\dev-trunk\\org.radixware\\ads\\Arte"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Не выдавать предупреждение об ожидании блокировки AADC в одном следующем запросе блокировки acquireXxxLock");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsB77WGVGNO5HFVAMMC57BMKC7IE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"D:\\RADIX\\dev-trunk\\org.radixware\\ads\\Arte"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Прочитать payload данные из блокировки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBVIBWNEC45GBVE6ELHDKSNUWDY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"D:\\RADIX\\dev-trunk\\org.radixware\\ads\\Arte"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Глобальная блокировка объекта в AADC на время транзакции");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDDBH6IC7CZHCHD3NZJW5MWFVI4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"D:\\RADIX\\dev-trunk\\org.radixware\\ads\\Arte"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"PID блокируемого объекта");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"PID of locked object");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEKD3J5BPKRANZKKWDZ33LK6HOE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"D:\\RADIX\\dev-trunk\\org.radixware\\ads\\Arte"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"PID блокируемого объекта");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"PID of locked object");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsETUZN6C7NJBRJAYU5SZZEKUGNM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"D:\\RADIX\\dev-trunk\\org.radixware\\ads\\Arte"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Объект %1 заблокирован в AADC");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Object %1 has AADC lock");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHPHFNNG7MRADTAU3BDMKRQOKFI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.WARNING,"App",java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"D:\\RADIX\\dev-trunk\\org.radixware\\ads\\Arte"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Блокируемый объект");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Locked object");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsICDKGDG2BZEONHD7ALALHJQZOU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"D:\\RADIX\\dev-trunk\\org.radixware\\ads\\Arte"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Таймаут ожидания (мс)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Wait timeout (ms)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIQM3AZHBIREZ5JUDSGRKKD66BY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"D:\\RADIX\\dev-trunk\\org.radixware\\ads\\Arte"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Таймаут ожидания (мс)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Wait timeout (ms)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIXFS67XUOJA3TABECRK2UJNMDA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"D:\\RADIX\\dev-trunk\\org.radixware\\ads\\Arte"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Прочитать payload данные из блокировки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJ7W5RFFQKZBZDKH72YGSJTXIOM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"D:\\RADIX\\dev-trunk\\org.radixware\\ads\\Arte"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Добавить payload данные к блокировке");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLB2WS5TE3NBN7JYNUJ7CHARZKQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"D:\\RADIX\\dev-trunk\\org.radixware\\ads\\Arte"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"PID блокируемого объекта");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"PID of locked object");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLE7LA6W4GRCVTLFDPK5PUDESC4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"D:\\RADIX\\dev-trunk\\org.radixware\\ads\\Arte"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Глобальная блокировка объекта в AADC на время транзакции");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLMELS2XIYVFMXPXV6FLUUV4BWM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"D:\\RADIX\\dev-trunk\\org.radixware\\ads\\Arte"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"PID блокируемого объекта");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"PID of locked object");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLOD66NCJAJCMJFPH6SIMKQG4JQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"D:\\RADIX\\dev-trunk\\org.radixware\\ads\\Arte"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Лаг репликации (milliseconds)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Replication lag (milliseconds)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMU2KTVHQHZGQNCCX6OBJGUCPSY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"D:\\RADIX\\dev-trunk\\org.radixware\\ads\\Arte"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Блокировка существует");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Lock exists");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNGS527ENFZBIJDQPCZQZ2LY424"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"D:\\RADIX\\dev-trunk\\org.radixware\\ads\\Arte"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Блокировка существует");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Lock exists");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOJG3YI4UA5APTFDN52J7NCOZMU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"D:\\RADIX\\dev-trunk\\org.radixware\\ads\\Arte"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"PID блокируемого объекта");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"PID of locked object");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPC5ANSU2CJE75HZYKBU6TB4HTE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"D:\\RADIX\\dev-trunk\\org.radixware\\ads\\Arte"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Менеджер AADC");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"AADC manager");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQBLYZTGGSZDC3LAWFVSIQPF6YE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"D:\\RADIX\\dev-trunk\\org.radixware\\ads\\Arte"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Таймаут ожидания (мс)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Wait timeout (ms)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRT53CN7U3VH63NVBQVGQTI5HWA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"D:\\RADIX\\dev-trunk\\org.radixware\\ads\\Arte"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"PID блокируемого объекта");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"PID of locked object");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSP4BE5YPAZASFBLMPBROCCUA7M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"D:\\RADIX\\dev-trunk\\org.radixware\\ads\\Arte"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Глобальная блокировка объекта в AADC на время сессии");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSTD6CGZ6JNDFJIJUC6AHLHNIHE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"D:\\RADIX\\dev-trunk\\org.radixware\\ads\\Arte"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Глобальная блокировка объекта в AADC на время транзакции");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsT3HWNMK5URDPZJE3P37O4XHQHY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"D:\\RADIX\\dev-trunk\\org.radixware\\ads\\Arte"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Добавить payload данные к блокировке");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTW2RZPUVDRDBRL6T7EIO663MOA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"D:\\RADIX\\dev-trunk\\org.radixware\\ads\\Arte"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"PID блокируемого объекта");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"PID of locked object");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVDYGPD63IRETZIIOMBITP7RUBA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"D:\\RADIX\\dev-trunk\\org.radixware\\ads\\Arte"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Глобальная блокировка объекта в AADC на время транзакции");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWZCTFNW4PJHCJIHZDFDRK2R6WM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"D:\\RADIX\\dev-trunk\\org.radixware\\ads\\Arte"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Данные");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Payload data");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsX5O6PFCMO5APFDE3IUG6QUUXI4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"D:\\RADIX\\dev-trunk\\org.radixware\\ads\\Arte"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Прочитать в");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Read to");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYBZJPJX7YJEODGNNNXMY4FRV4U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"D:\\RADIX\\dev-trunk\\org.radixware\\ads\\Arte"));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(AadcManager - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbadc43QKDQ6EZNEFTLCWUO3NACPAFY"),"AadcManager - Localizing Bundle",$$$items$$$);
}
