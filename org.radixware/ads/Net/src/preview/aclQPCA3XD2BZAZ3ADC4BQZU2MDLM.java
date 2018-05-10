
/* Radix::Net::Unit.NetHub - Server Executable*/

/*Radix::Net::Unit.NetHub-Application Class*/

package org.radixware.ads.Net.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.utils.Maps;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub")
public abstract published class Unit.NetHub  extends org.radixware.ads.System.server.Unit.AbstractServiceDriver  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {

	final static protected long UNCORRELATED_MESSSAGE = 1;
	final static protected long DUPLICATED_MESSAGE = 2;
	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return Unit.NetHub_mi.rdxMeta;}

	/*Radix::Net::Unit.NetHub:Nested classes-Nested Classes*/

	/*Radix::Net::Unit.NetHub:Properties-Properties*/

	/*Radix::Net::Unit.NetHub:unitId-Detail Column Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:unitId")
	public published  Int getUnitId() {
		return unitId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:unitId")
	public published   void setUnitId(Int val) {
		unitId = val;
	}

	/*Radix::Net::Unit.NetHub:sapId-Detail Column Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:sapId")
	public published  Int getSapId() {
		return sapId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:sapId")
	public published   void setSapId(Int val) {
		sapId = val;
	}

	/*Radix::Net::Unit.NetHub:echoTestPeriod-Detail Column Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:echoTestPeriod")
	public published  Int getEchoTestPeriod() {
		return echoTestPeriod;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:echoTestPeriod")
	public published   void setEchoTestPeriod(Int val) {
		echoTestPeriod = val;
	}

	/*Radix::Net::Unit.NetHub:extPortAddress-Detail Column Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:extPortAddress")
	public published  Str getExtPortAddress() {
		return extPortAddress;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:extPortAddress")
	public published   void setExtPortAddress(Str val) {
		extPortAddress = val;
	}

	/*Radix::Net::Unit.NetHub:extPortFrame-Detail Column Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:extPortFrame")
	public published  Str getExtPortFrame() {
		return extPortFrame;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:extPortFrame")
	public published   void setExtPortFrame(Str val) {
		extPortFrame = val;
	}

	/*Radix::Net::Unit.NetHub:extPortIsServer-Detail Column Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:extPortIsServer")
	public published  Bool getExtPortIsServer() {
		return extPortIsServer;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:extPortIsServer")
	public published   void setExtPortIsServer(Bool val) {
		extPortIsServer = val;
	}

	/*Radix::Net::Unit.NetHub:inSeanceCnt-Detail Column Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:inSeanceCnt")
	public published  Int getInSeanceCnt() {
		return inSeanceCnt;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:inSeanceCnt")
	public published   void setInSeanceCnt(Int val) {
		inSeanceCnt = val;
	}

	/*Radix::Net::Unit.NetHub:outSeanceCnt-Detail Column Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:outSeanceCnt")
	public published  Int getOutSeanceCnt() {
		return outSeanceCnt;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:outSeanceCnt")
	public published   void setOutSeanceCnt(Int val) {
		outSeanceCnt = val;
	}

	/*Radix::Net::Unit.NetHub:outTimeout-Detail Column Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:outTimeout")
	public published  Int getOutTimeout() {
		return outTimeout;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:outTimeout")
	public published   void setOutTimeout(Int val) {
		outTimeout = val;
	}

	/*Radix::Net::Unit.NetHub:reconnectNoEchoCnt-Detail Column Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:reconnectNoEchoCnt")
	public published  Int getReconnectNoEchoCnt() {
		return reconnectNoEchoCnt;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:reconnectNoEchoCnt")
	public published   void setReconnectNoEchoCnt(Int val) {
		reconnectNoEchoCnt = val;
	}

	/*Radix::Net::Unit.NetHub:connected-Detail Column Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:connected")
	public published  Bool getConnected() {
		return connected;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:connected")
	public published   void setConnected(Bool val) {
		connected = val;
	}

	/*Radix::Net::Unit.NetHub:lastInUniqueKey-Detail Column Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:lastInUniqueKey")
	public published  Str getLastInUniqueKey() {
		return lastInUniqueKey;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:lastInUniqueKey")
	public published   void setLastInUniqueKey(Str val) {
		lastInUniqueKey = val;
	}

	/*Radix::Net::Unit.NetHub:lastOutStan-Detail Column Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:lastOutStan")
	public published  Int getLastOutStan() {
		return lastOutStan;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:lastOutStan")
	public published   void setLastOutStan(Int val) {
		lastOutStan = val;
	}

	/*Radix::Net::Unit.NetHub:curOutSessionCnt-Detail Column Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:curOutSessionCnt")
	public published  Int getCurOutSessionCnt() {
		return curOutSessionCnt;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:curOutSessionCnt")
	public published   void setCurOutSessionCnt(Int val) {
		curOutSessionCnt = val;
	}

	/*Radix::Net::Unit.NetHub:stmtGetOutStan-Dynamic Property*/



	protected static org.radixware.ads.Types.server.SqlStatement stmtGetOutStan=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:stmtGetOutStan")
	private static final  org.radixware.ads.Types.server.SqlStatement getStmtGetOutStan() {

		if (internal[stmtGetOutStan] == null) {
		    internal[stmtGetOutStan] = Types::SqlStatement.prepare(
		        "SELECT " + dbName[lastOutStan] + " FROM " + dbName[Radix::System::NetHub] + " WHERE " + dbName[id] + "=?", false
		    );
		}
		return internal[stmtGetOutStan];
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:stmtGetOutStan")
	private static final   void setStmtGetOutStan(org.radixware.ads.Types.server.SqlStatement val) {
		stmtGetOutStan = val;
	}

	/*Radix::Net::Unit.NetHub:stmtSetOutStan-Dynamic Property*/



	protected static org.radixware.ads.Types.server.SqlStatement stmtSetOutStan=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:stmtSetOutStan")
	private static final  org.radixware.ads.Types.server.SqlStatement getStmtSetOutStan() {

		if (internal[stmtSetOutStan] == null) {
		    internal[stmtSetOutStan] = Types::SqlStatement.prepare(
		        "DECLARE PRAGMA AUTONOMOUS_TRANSACTION;" +
		        "BEGIN UPDATE " + dbName[Radix::System::NetHub] + " SET " + dbName[lastOutStan] + "=? WHERE " + dbName[id] + "=?;" +
		        "COMMIT; END;", 
		        false
		    );
		}
		return internal[stmtSetOutStan];
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:stmtSetOutStan")
	private static final   void setStmtSetOutStan(org.radixware.ads.Types.server.SqlStatement val) {
		stmtSetOutStan = val;
	}

	/*Radix::Net::Unit.NetHub:stmtIncOutSessionCnt-Dynamic Property*/



	protected static org.radixware.ads.Types.server.SqlStatement stmtIncOutSessionCnt=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:stmtIncOutSessionCnt")
	private static final  org.radixware.ads.Types.server.SqlStatement getStmtIncOutSessionCnt() {

		if (internal[stmtIncOutSessionCnt] == null) {
		    internal[stmtIncOutSessionCnt] = Types::SqlStatement.prepare(
		            "DECLARE PRAGMA AUTONOMOUS_TRANSACTION;" +
		            " BEGIN UPDATE " + dbName[Radix::System::NetHub] + 
		            " SET " + dbName[curOutSessionCnt] + "=nvl(" + dbName[curOutSessionCnt] + ",0)+1" +
		            " WHERE " + dbName[id] + "=?; COMMIT; END;",
		            false);
		}
		return internal[stmtIncOutSessionCnt];
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:stmtIncOutSessionCnt")
	private static final   void setStmtIncOutSessionCnt(org.radixware.ads.Types.server.SqlStatement val) {
		stmtIncOutSessionCnt = val;
	}

	/*Radix::Net::Unit.NetHub:stmtDecOutSessionCnt-Dynamic Property*/



	protected static org.radixware.ads.Types.server.SqlStatement stmtDecOutSessionCnt=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:stmtDecOutSessionCnt")
	private static final  org.radixware.ads.Types.server.SqlStatement getStmtDecOutSessionCnt() {

		if (internal[stmtDecOutSessionCnt] == null) {
		    internal[stmtDecOutSessionCnt] = Types::SqlStatement.prepare(
		            "DECLARE PRAGMA AUTONOMOUS_TRANSACTION;" +
		            " BEGIN UPDATE " + dbName[Radix::System::NetHub] + 
		            " SET " + dbName[curOutSessionCnt] + "=" + dbName[curOutSessionCnt] + "-1" +
		            " WHERE " + dbName[id] + "=?; COMMIT; END;",
		            false);
		}
		return internal[stmtDecOutSessionCnt];
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:stmtDecOutSessionCnt")
	private static final   void setStmtDecOutSessionCnt(org.radixware.ads.Types.server.SqlStatement val) {
		stmtDecOutSessionCnt = val;
	}

	/*Radix::Net::Unit.NetHub:stmtSetConnected-Dynamic Property*/



	protected static org.radixware.ads.Types.server.SqlStatement stmtSetConnected=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:stmtSetConnected")
	private static final  org.radixware.ads.Types.server.SqlStatement getStmtSetConnected() {

		if (internal[stmtSetConnected] == null) {
		    internal[stmtSetConnected] = Types::SqlStatement.prepare(
		        "DECLARE PRAGMA AUTONOMOUS_TRANSACTION;" +
		        "BEGIN UPDATE " + dbName[Radix::System::NetHub] + " SET " + dbName[connected] + "=? WHERE " + dbName[id] + "=?;" +
		        "COMMIT; END;", 
		        false
		    );
		}
		return internal[stmtSetConnected];
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:stmtSetConnected")
	private static final   void setStmtSetConnected(org.radixware.ads.Types.server.SqlStatement val) {
		stmtSetConnected = val;
	}

	/*Radix::Net::Unit.NetHub:stmtGetInStan-Dynamic Property*/



	protected static org.radixware.ads.Types.server.SqlStatement stmtGetInStan=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:stmtGetInStan")
	private static final  org.radixware.ads.Types.server.SqlStatement getStmtGetInStan() {

		if (internal[stmtGetInStan] == null) {
		    internal[stmtGetInStan] = Types::SqlStatement.prepare(
		        "SELECT " + dbName[lastInUniqueKey] + " FROM " + dbName[Radix::System::NetHub] + " WHERE " + dbName[id] + "=?", false
		    );
		}
		return internal[stmtGetInStan];
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:stmtGetInStan")
	private static final   void setStmtGetInStan(org.radixware.ads.Types.server.SqlStatement val) {
		stmtGetInStan = val;
	}

	/*Radix::Net::Unit.NetHub:stmtSetInStan-Dynamic Property*/



	protected static org.radixware.ads.Types.server.SqlStatement stmtSetInStan=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:stmtSetInStan")
	private static final  org.radixware.ads.Types.server.SqlStatement getStmtSetInStan() {

		if (internal[stmtSetInStan] == null) {
		    internal[stmtSetInStan] = Types::SqlStatement.prepare(
		        "DECLARE PRAGMA AUTONOMOUS_TRANSACTION;" +
		        "BEGIN UPDATE " + dbName[Radix::System::NetHub] + " SET " + dbName[lastInUniqueKey] + "=? WHERE " + dbName[id] + "=?;" +
		        "COMMIT; END;", 
		        false
		    );
		}
		return internal[stmtSetInStan];
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:stmtSetInStan")
	private static final   void setStmtSetInStan(org.radixware.ads.Types.server.SqlStatement val) {
		stmtSetInStan = val;
	}

	/*Radix::Net::Unit.NetHub:stmtResetOutSessionCnt-Dynamic Property*/



	protected static org.radixware.ads.Types.server.SqlStatement stmtResetOutSessionCnt=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:stmtResetOutSessionCnt")
	private static final  org.radixware.ads.Types.server.SqlStatement getStmtResetOutSessionCnt() {

		if (internal[stmtResetOutSessionCnt] == null) {
		    internal[stmtResetOutSessionCnt] = Types::SqlStatement.prepare(
		        "DECLARE PRAGMA AUTONOMOUS_TRANSACTION;" +
		        "BEGIN UPDATE " + dbName[Radix::System::NetHub] + " SET " + dbName[curOutSessionCnt] + "=0 WHERE " + dbName[id] + "=?;" +
		        "COMMIT; END;", 
		        false
		    );
		}
		return internal[stmtResetOutSessionCnt];
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:stmtResetOutSessionCnt")
	private static final   void setStmtResetOutSessionCnt(org.radixware.ads.Types.server.SqlStatement val) {
		stmtResetOutSessionCnt = val;
	}

	/*Radix::Net::Unit.NetHub:toProcessConnect-Detail Column Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:toProcessConnect")
	public published  Bool getToProcessConnect() {
		return toProcessConnect;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:toProcessConnect")
	public published   void setToProcessConnect(Bool val) {
		toProcessConnect = val;
	}

	/*Radix::Net::Unit.NetHub:toProcessDisconnect-Detail Column Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:toProcessDisconnect")
	public published  Bool getToProcessDisconnect() {
		return toProcessDisconnect;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:toProcessDisconnect")
	public published   void setToProcessDisconnect(Bool val) {
		toProcessDisconnect = val;
	}

	/*Radix::Net::Unit.NetHub:toProcessDuplicatedRq-Detail Column Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:toProcessDuplicatedRq")
	public published  Bool getToProcessDuplicatedRq() {
		return toProcessDuplicatedRq;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:toProcessDuplicatedRq")
	public published   void setToProcessDuplicatedRq(Bool val) {
		toProcessDuplicatedRq = val;
	}

	/*Radix::Net::Unit.NetHub:toProcessUncorrelatedRs-Detail Column Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:toProcessUncorrelatedRs")
	public published  Bool getToProcessUncorrelatedRs() {
		return toProcessUncorrelatedRs;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:toProcessUncorrelatedRs")
	public published   void setToProcessUncorrelatedRs(Bool val) {
		toProcessUncorrelatedRs = val;
	}

	/*Radix::Net::Unit.NetHub:stmtLock-Dynamic Property*/



	protected static org.radixware.ads.Types.server.SqlBlock stmtLock=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:stmtLock")
	private static final  org.radixware.ads.Types.server.SqlBlock getStmtLock() {

		if (internal[stmtLock] == null)
		    internal[stmtLock] = Types::SqlBlock.prepare("declare res integer; begin res := DBMS_LOCK.REQUEST(?, DBMS_LOCK.X_MODE, DBMS_LOCK.MAXWAIT, false); end;", false);
		return internal[stmtLock];
	}

	/*Radix::Net::Unit.NetHub:toProcessStart-Detail Column Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:toProcessStart")
	public published  Bool getToProcessStart() {
		return toProcessStart;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:toProcessStart")
	public published   void setToProcessStart(Bool val) {
		toProcessStart = val;
	}

	/*Radix::Net::Unit.NetHub:toProcessStop-Detail Column Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:toProcessStop")
	public published  Bool getToProcessStop() {
		return toProcessStop;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:toProcessStop")
	public published   void setToProcessStop(Bool val) {
		toProcessStop = val;
	}

	/*Radix::Net::Unit.NetHub:stmtUnlock-Dynamic Property*/



	protected static org.radixware.ads.Types.server.SqlBlock stmtUnlock=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:stmtUnlock")
	private static final  org.radixware.ads.Types.server.SqlBlock getStmtUnlock() {

		if (internal[stmtUnlock] == null)
		    internal[stmtUnlock] = Types::SqlBlock.prepare("declare res integer; begin res := DBMS_LOCK.RELEASE(?); end;", false);
		return internal[stmtUnlock];
	}

	/*Radix::Net::Unit.NetHub:stan-Dynamic Property*/



	protected Int stan=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:stan")
	public published  Int getStan() {
		return stan;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:stan")
	public published   void setStan(Int val) {
		stan = val;
	}















































































































































































































	/*Radix::Net::Unit.NetHub:Methods-Methods*/

	/*Radix::Net::Unit.NetHub:getServiceAccessibility-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:getServiceAccessibility")
	protected published  org.radixware.kernel.common.enums.EServiceAccessibility getServiceAccessibility () {
		return System::ServiceAccessibility:INTRA_SYSTEM;
	}

	/*Radix::Net::Unit.NetHub:getUri-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:getUri")
	protected published  Str getUri () {
		return getWsdl() + "#" + id.toString();
	}

	/*Radix::Net::Unit.NetHub:getWsdl-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:getWsdl")
	protected published  Str getWsdl () {
		return "http://schemas.radixware.org/nethub.wsdl";
	}

	/*Radix::Net::Unit.NetHub:getSapId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:getSapId")
	protected published  Int getSapId () {
		return sapId;
	}

	/*Radix::Net::Unit.NetHub:setSapId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:setSapId")
	protected published  void setSapId (Int sapId) {
		sapId = sapId;
	}

	/*Radix::Net::Unit.NetHub:afterInit-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:afterInit")
	protected published  void afterInit (org.radixware.kernel.server.types.Entity src, org.radixware.kernel.common.enums.EEntityInitializationPhase phase) {
		super.afterInit(src, phase);
		type = System::UnitType:NetHub;
	}

	/*Radix::Net::Unit.NetHub:onConnect-User Method*/

	@org.radixware.kernel.common.lang.ReflectiveCallable
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:onConnect")
	public abstract published  void onConnect ();

	/*Radix::Net::Unit.NetHub:onDisconnect-User Method*/

	@org.radixware.kernel.common.lang.ReflectiveCallable
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:onDisconnect")
	public abstract published  void onDisconnect ();

	/*Radix::Net::Unit.NetHub:onInactivityTimer-User Method*/

	@org.radixware.kernel.common.lang.ReflectiveCallable
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:onInactivityTimer")
	public published  void onInactivityTimer () {
		if (reconnectNoEchoCnt == null)
		    return;

		Bin echoRq = prepareEchoTestRq(); 
		if (echoRq == null)
		    return;

		int retry = 0;
		Bin mess = null;

		do {
		    if (retry > 0)
		        traceDebug("Repeat Echo Test: " + String.valueOf(retry));

		    try {
		        mess = invokeRequest(echoRq);
		        break;
		    } catch(Exceptions::ServiceCallFault e) {
		        traceError("Echo test service call fault: " + e.getMessage());
		    } catch(Exceptions::ServiceCallException e) {
		        traceError("Echo test service call exception: " + e.getMessage());
		    } catch(Exceptions::ServiceCallTimeout e) {    
		        traceError("Echo test service call timeout: " + e.getMessage());
		    } catch(Exceptions::InterruptedException e) {
		        traceError("Echo test interrupted exception: " + e.getMessage());
		    }    
		} while (retry++ < reconnectNoEchoCnt.intValue());

		if (mess == null) { 
		    reconnectExtPort();
		    return;
		}

		traceDebug("Echo test complete");
	}

	/*Radix::Net::Unit.NetHub:onMessage-User Method*/

	@org.radixware.kernel.common.lang.ReflectiveCallable
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:onMessage")
	public abstract published  org.radixware.kernel.common.types.Bin onMessage (org.radixware.kernel.common.types.Bin mess);

	/*Radix::Net::Unit.NetHub:onInvalidMessage-User Method*/

	@org.radixware.kernel.common.lang.ReflectiveCallable
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:onInvalidMessage")
	public abstract published  org.radixware.kernel.common.types.Bin onInvalidMessage (org.radixware.kernel.common.types.Bin mess, Int errorType);

	/*Radix::Net::Unit.NetHub:bundle-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:bundle")
	protected  void bundle () {
		//мультиязычные строки используемые в пакете org.radixware.kernel.server.units.nethub
		Str[] s = new Str[100];
		s[0] = eventCode["Error starting the socket %1: %2"];
		s[1] = eventCode["I/O error in socket \"%1\": %2"];
		s[2] = eventCode["Error processing the external system request occurred in \"%1\": %2"];
		s[3] = eventCode["Service Server: timeout of response from external system exceeded (STAN=%1)"];
		s[4] = eventCode["External port: receive timeout exceeded (STAN=%1)"];
		s[5] = eventCode["Error reading the parameters of the service \"%1\": %2"];
		s[6] = eventCode["Duplicate request (STAN=%2) received from external port of \"%1\""];
		s[7] = eventCode["Late response detected in \"%1\""];
		s[8] = eventCode["Limit of requests from external system (%1) exceeded in \"%2\""];
		s[9] = eventCode["Limit of requests to external system (%1) exceeded in \"%2\""];
		s[10] = eventCode["External host rejected message (STAN=%2) of \"%1\""];
		s[11] = eventCode["Connected to external system: %1"];
		s[12] = eventCode["External system disconnected"];
	}

	/*Radix::Net::Unit.NetHub:getUsedAddresses-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:getUsedAddresses")
	protected published  java.util.Collection<org.radixware.ads.System.server.AddressInfo> getUsedAddresses () {
		final List<System::AddressInfo> addresses = new ArrayList<System::AddressInfo>();
		if (extPortIsServer == Bool.TRUE && extPortAddress != null)
		    addresses.add(new AddressInfo(extPortAddress, System::SapChannelType:TCP));
		if (sapId != null) {
		    addresses.add(System::ServerSapUtils.getSapAddressInfo(sapId));
		}
		return addresses;
	}

	/*Radix::Net::Unit.NetHub:onAfterRecv-User Method*/

	@org.radixware.kernel.common.lang.ReflectiveCallable
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:onAfterRecv")
	public published  org.radixware.schemas.nethub.OnRecvRsDocument onAfterRecv (org.radixware.schemas.nethub.OnRecvRqDocument.OnRecvRq rq) {
		final Bin mess = Bin.wrap(rq.Mess);
		final Map<Bin, Map<Str, Str>> correlatedMess = new HashMap<Bin, Map<Str, Str>>();
		for (NetHubXsd:OnRecvRqDocument.OnRecvRq.RqMess xMess : rq.RqMessList) {
		    correlatedMess.put(Bin.wrap(xMess.Mess), Maps.fromXml(xMess.ParsingVars));
		}

		NetHubXsd:OnRecvRsDocument xDoc = NetHubXsd:OnRecvRsDocument.Factory.newInstance();
		NetHubXsd:OnRecvRsDocument.OnRecvRs rs = xDoc.addNewOnRecvRs();

		final Map<Str,Str> parsingVars = new HashMap<Str,Str>();
		rs.IsRequest = isRequest(mess, parsingVars);

		if (rs.IsRequest.booleanValue()) {
		    rs.UniqueKey = extractUniqueKey(mess);
		    // save uniqueKey
		    stmtSetInStan.setStr(1, rs.UniqueKey);
		    stmtSetInStan.setInt(2, id);
		    stmtSetInStan.executeUpdate();        
		} else {
		    for (Map.Entry<Bin, Map<Str,Str>> e : correlatedMess.entrySet()) {
		        if (isCorrelated(e.getKey(), e.getValue(), mess, parsingVars)) {
		            rs.RqMess = e.getKey().get();
		            break;
		        }
		    }
		}
		return xDoc;
	}

	/*Radix::Net::Unit.NetHub:invokeService-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:invokeService")
	protected published  org.apache.xmlbeans.XmlObject invokeService (org.apache.xmlbeans.XmlObject rq, java.lang.Class<? extends java.lang.Object> resultClass) throws java.lang.InterruptedException,org.radixware.kernel.common.exceptions.ServiceCallException,org.radixware.kernel.common.exceptions.ServiceCallFault,org.radixware.kernel.common.exceptions.ServiceCallSendException,org.radixware.kernel.common.exceptions.ServiceCallTimeout {
		return Arte::Arte.invokeInternalService(rq, resultClass, getUri(), 20, outTimeout.intValue(), null);
	}

	/*Radix::Net::Unit.NetHub:invokeRequest-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:invokeRequest")
	protected published  org.radixware.kernel.common.types.Bin invokeRequest (org.radixware.kernel.common.types.Bin mess) throws java.lang.InterruptedException,org.radixware.kernel.common.exceptions.ServiceCallException,org.radixware.kernel.common.exceptions.ServiceCallFault,org.radixware.kernel.common.exceptions.ServiceCallSendException,org.radixware.kernel.common.exceptions.ServiceCallTimeout {
		stan = null;
		NetHubWsdl:InvokeDocument doc = NetHubWsdl:InvokeDocument.Factory.newInstance();
		NetHubXsd:InvokeRq rq = doc.addNewInvoke().addNewInvokeRq();

		final Map<Str, Str> parsingVars = new HashMap<Str, Str>();
		if (!isRequest(mess, parsingVars))
		    throw new NetHubError("Message is not request: " + new Str(mess.get()));

		rq.ParsingVars = Maps.toXml(parsingVars);
		stmtLock.setInt(1,id.longValue()%1073741824);
		stmtLock.executeUpdate();
		try {
		    stmtGetOutStan.setInt(1, id);
		    Types::SqlResultSet rs = stmtGetOutStan.executeQuery();
		    if (rs.next())
		        stan = rs.getInt(1);
		    if (stan == null)
		        stan = 0;

		    final byte[] data = mess.get();
		    stan = insertStan(data, stan.intValue());

		    rq.Stan = stan;
		    rq.Body = data;

		    stmtSetOutStan.setInt(1, stan);
		    stmtSetOutStan.setInt(2, id);
		    stmtSetOutStan.executeUpdate();
		} finally {
		    stmtUnlock.setInt(1,id.longValue()%1073741824);
		    stmtUnlock.executeUpdate();
		}

		try {
		    stmtIncOutSessionCnt.setInt(1, id);
		    stmtIncOutSessionCnt.executeUpdate();    
		    doc.set(invokeService(doc, NetHubWsdl:InvokeDocument.class));
		} finally {
		    stmtDecOutSessionCnt.setInt(1, id);
		    stmtDecOutSessionCnt.executeUpdate();    
		}

		NetHubXsd:InvokeRs rs = doc.Invoke.InvokeRs;
		return Bin.wrap(rs.Body);
	}

	/*Radix::Net::Unit.NetHub:insertStan-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:insertStan")
	public abstract published  int insertStan (byte[] mess, int prevStan);

	/*Radix::Net::Unit.NetHub:onBeforeStart-User Method*/

	@org.radixware.kernel.common.lang.ReflectiveCallable
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:onBeforeStart")
	public abstract published  void onBeforeStart ();

	/*Radix::Net::Unit.NetHub:onBeforeStop-User Method*/

	@org.radixware.kernel.common.lang.ReflectiveCallable
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:onBeforeStop")
	public abstract published  void onBeforeStop ();

	/*Radix::Net::Unit.NetHub:reconnectExtPort-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:reconnectExtPort")
	protected published  boolean reconnectExtPort () {
		traceDebug("Reconnect with host");
		NetHubWsdl:ReconnectDocument doc = NetHubWsdl:ReconnectDocument.Factory.newInstance();
		doc.addNewReconnect().addNewReconnectRq(); 
		try {
		    invokeService(doc, NetHubWsdl:ReconnectDocument.class);
		} catch(Exceptions::ServiceCallFault e) {
		    traceError("Reconnect service call fault: " + e.getMessage());
		    return false;
		} catch(Exceptions::ServiceCallException e) {
		    traceError("Reconnect service call exception: " + e.getMessage());
		    return false;
		} catch(Exceptions::ServiceCallTimeout e) {    
		    traceError("Reconnect service call timeout: " + e.getMessage());
		    return false;
		} catch(Exceptions::InterruptedException e) {
		    traceError("Reconnect interrupted exception: " + e.getMessage());
		    return false;
		}
		return true;
	}

	/*Radix::Net::Unit.NetHub:prepareEchoTestRq-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:prepareEchoTestRq")
	public abstract published  org.radixware.kernel.common.types.Bin prepareEchoTestRq ();

	/*Radix::Net::Unit.NetHub:extractUniqueKey-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:extractUniqueKey")
	public abstract published  Str extractUniqueKey (org.radixware.kernel.common.types.Bin mess);

	/*Radix::Net::Unit.NetHub:isCorrelated-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:isCorrelated")
	public abstract published  boolean isCorrelated (org.radixware.kernel.common.types.Bin rqMess, java.util.Map<Str,Str> rqParsingVars, org.radixware.kernel.common.types.Bin rsMess, java.util.Map<Str,Str> rsParsingVars) throws org.radixware.kernel.server.units.nethub.errors.NetHubFormatError;

	/*Radix::Net::Unit.NetHub:isRequest-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:isRequest")
	public abstract published  boolean isRequest (org.radixware.kernel.common.types.Bin mess, java.util.Map<Str,Str> parsingVars) throws org.radixware.kernel.server.units.nethub.errors.NetHubFormatError;

	/*Radix::Net::Unit.NetHub:traceDebug-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:traceDebug")
	  void traceDebug (Str mess) {
		trace(Arte::EventSeverity:Debug, mess);
	}

	/*Radix::Net::Unit.NetHub:trace-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:trace")
	  void trace (org.radixware.kernel.common.enums.EEventSeverity severity, Str mess) {
		Int traceLevel = Arte::Trace.getMinSeverity(Arte::EventSource:NetHubHandler);
		if (traceLevel.intValue() < severity.Value.intValue())
		    return;

		Arte::Trace.enterContext(Arte::EventContextType:SystemUnit, id);
		Object traceTargetHandler = Arte::Trace.addContextProfile(dbTraceProfile, this);
		try {
		    Arte::Trace.put(severity, mess, Arte::EventSource:NetHubHandler);
		} finally {
		    Arte::Trace.leaveContext(Arte::EventContextType:SystemUnit, id);
		    Arte::Trace.delContextProfile(traceTargetHandler);
		}
	}

	/*Radix::Net::Unit.NetHub:traceEvent-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:traceEvent")
	  void traceEvent (Str mess) {
		trace(Arte::EventSeverity:Event, mess);
	}

	/*Radix::Net::Unit.NetHub:traceError-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:traceError")
	  void traceError (Str mess) {
		trace(Arte::EventSeverity:Error, mess);
	}

	/*Radix::Net::Unit.NetHub:onCommand_EchoTest-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:onCommand_EchoTest")
	  org.radixware.ads.Net.common.NetHubCmdXsd.EchoTestRs onCommand_EchoTest (org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValHandlersById) {
		final NetHubCmdXsd:EchoTestRs rs = NetHubCmdXsd:EchoTestRs.Factory.newInstance();
		rs.Result = System::InstanceControlServiceXsd:ActionStateEnum.FAILED;

		final Bin echoRq = prepareEchoTestRq(); 
		if (echoRq == null) {
		    traceError("No data for Echo test");    
		    return rs;
		}

		try {
		    final Bin mess = invokeRequest(echoRq);
		    if (mess != null)
		        rs.Result = System::InstanceControlServiceXsd:ActionStateEnum.DONE;
		} catch(Exceptions::ServiceCallFault e) {
		    traceError("Echo test service call fault: " + e.getMessage());
		} catch(Exceptions::ServiceCallException e) {
		    traceError("Echo test service call exception: " + e.getMessage());
		} catch(Exceptions::ServiceCallTimeout e) {    
		    traceError("Echo test service call timeout: " + e.getMessage());
		} catch(Exceptions::InterruptedException e) {
		    traceError("Echo test interrupted exception: " + e.getMessage());
		}    

		return rs;
	}

	/*Radix::Net::Unit.NetHub:onCommand_Reconnect-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:onCommand_Reconnect")
	  org.radixware.ads.Net.common.NetHubCmdXsd.ReconnectRs onCommand_Reconnect (org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValHandlersById) {
		final NetHubCmdXsd:ReconnectRs rs = NetHubCmdXsd:ReconnectRs.Factory.newInstance();
		rs.Result = reconnectExtPort() ? 
		    System::InstanceControlServiceXsd:ActionStateEnum.DONE :
		    System::InstanceControlServiceXsd:ActionStateEnum.FAILED;
		return rs;
	}

	/*Radix::Net::Unit.NetHub:sendMessage-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:sendMessage")
	protected published  void sendMessage (org.radixware.kernel.common.types.Bin mess) throws java.lang.InterruptedException,org.radixware.kernel.common.exceptions.ServiceCallException,org.radixware.kernel.common.exceptions.ServiceCallFault,org.radixware.kernel.common.exceptions.ServiceCallSendException,org.radixware.kernel.common.exceptions.ServiceCallTimeout {
		stan = null;
		NetHubWsdl:SendDocument doc = NetHubWsdl:SendDocument.Factory.newInstance();
		NetHubXsd:SendRq rq = doc.addNewSend().addNewSendRq();

		final Map<Str, Str> parsingVars = new HashMap<Str, Str>();
		if (!isRequest(mess, parsingVars))
		    throw new NetHubError("Message is not request: " + new Str(mess.get()));
		stmtLock.setInt(1,id.longValue()%1073741824);
		stmtLock.executeUpdate();
		try {
		    stmtGetOutStan.setInt(1, id);
		    Types::SqlResultSet rs = stmtGetOutStan.executeQuery();
		    if (rs.next())
		        stan = rs.getInt(1);
		    if (stan == null)
		        stan = 0;

		    final byte[] data = mess.get();
		    stan = insertStan(data, stan.intValue());

		    rq.Body = data;

		    stmtSetOutStan.setInt(1, stan);
		    stmtSetOutStan.setInt(2, id);
		    stmtSetOutStan.executeUpdate();
		} finally {
		    stmtUnlock.setInt(1,id.longValue()%1073741824);
		    stmtUnlock.executeUpdate();
		}

		invokeService(doc, NetHubWsdl:SendDocument.class);
	}




	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{
		if(cmdId == cmd65WESESVCFDAJOK2HI7OTV3WYE){
			org.radixware.ads.Net.common.NetHubCmdXsd.ReconnectRs result = onCommand_Reconnect(newPropValsById);
			if(result != null)
				output.set(result);
			return null;
		} else if(cmdId == cmdPYD77VZHWJG5FD7PCRQETZXWOE){
			org.radixware.ads.Net.common.NetHubCmdXsd.EchoTestRs result = onCommand_EchoTest(newPropValsById);
			if(result != null)
				output.set(result);
			return null;
		} else 
			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::Net::Unit.NetHub - Server Meta*/

/*Radix::Net::Unit.NetHub-Application Class*/

package org.radixware.ads.Net.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Unit.NetHub_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),"Unit.NetHub",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsU75MXOL3GBDWBHUUNP6WZTE4OA"),

						org.radixware.kernel.common.enums.EClassType.APPLICATION,

						/*Radix::Net::Unit.NetHub:Presentations-Entity Object Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),
							/*Owner Class Name*/
							"Unit.NetHub",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsU75MXOL3GBDWBHUUNP6WZTE4OA"),
							/*Property presentations*/

							/*Radix::Net::Unit.NetHub:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::Net::Unit.NetHub:unitId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGWZKWFUHPZC4PN6Q2XLFCI5O3Q"),org.radixware.kernel.common.enums.EEditPossibility.ON_CREATE,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Net::Unit.NetHub:sapId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2EFE2C2G4RBCRCTW3P4PPP75QE"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Net::Unit.NetHub:echoTestPeriod:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHBG4KM4VQVF35OPCYQH7VNEYC4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Net::Unit.NetHub:extPortAddress:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXKVMIMGKP5BYTEPJUIHNLX6HSA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Net::Unit.NetHub:extPortFrame:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKIKX6HU5WNCZXFGF75N4OHSQCE"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Net::Unit.NetHub:extPortIsServer:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4BU63OQ3QNCUTALKLHR3B3SSKQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Net::Unit.NetHub:inSeanceCnt:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKMV7W4EEOVHS3IDZV4RSCNSR6E"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Net::Unit.NetHub:outSeanceCnt:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBI2OPIVYRJEGHBIACAYHAKVKZA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Net::Unit.NetHub:outTimeout:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVJY3C7ZGJFCR3PQNKQ4ME6C43Q"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Net::Unit.NetHub:reconnectNoEchoCnt:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYEGHE3TXIFHIXKQDATLI5XSSXI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Net::Unit.NetHub:connected:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colURL5B4BJYJGXVJQ6LLJJ7OR424"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Net::Unit.NetHub:lastInUniqueKey:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFR7V6AFFJRHL3LEQPOO4OPNXTY"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Net::Unit.NetHub:lastOutStan:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHAHRYM7CWVAKZOCTVWAGXZYYCI"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Net::Unit.NetHub:curOutSessionCnt:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLRFAIJEK75H35OIHCW533VHPQQ"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Net::Unit.NetHub:toProcessConnect:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDXTQN2WF2RFH7BS3NQSNUU6QPU"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Net::Unit.NetHub:toProcessDisconnect:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3Z4VZLS7Y5GIXD4V5PC6YAEHJM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Net::Unit.NetHub:toProcessDuplicatedRq:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGAOGFV575VBG5IHEZAOU7BYRQU"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Net::Unit.NetHub:toProcessUncorrelatedRs:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUDBAD6URAJD3XGIU6MGDULW6VI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Net::Unit.NetHub:toProcessStart:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3QZLKSDOIRCOZM4F63D56D2A6E"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Net::Unit.NetHub:toProcessStop:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2CD7SWVNNNAD3CPORJQEHOTZL4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Net::Unit.NetHub:stan:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd557QWPUWRNBQ5NJASG2E6UI7VM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT))
							},
							/*Commands*/
							new org.radixware.kernel.server.meta.presentations.RadCommandDef[]{

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::Net::Unit.NetHub:Reconnect-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmd65WESESVCFDAJOK2HI7OTV3WYE"),"Reconnect",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::Net::Unit.NetHub:EchoTest-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdPYD77VZHWJG5FD7PCRQETZXWOE"),"EchoTest",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),false,true)
							},
							/*Sortings*/
							null,
							/*Filters*/
							null,
							/*Editor presentations*/
							new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::Net::Unit.NetHub:Create-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprTLC3CDS7M5DGBJBASBOSAYDODI"),
									"Create",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprKRWKXDGWONEAPFO67QVMISKWOA"),
									40114,
									null,

									/*Radix::Net::Unit.NetHub:Create:Children-Explorer Items*/
										null
									,
									null,
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.FROM_REPLACED,
									null),

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::Net::Unit.NetHub:Edit-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprKRWKXDGWONEAPFO67QVMISKWOA"),
									"Edit",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprE7BZV3MWA7OBDCGDAALOMT5GDM"),
									35984,
									null,

									/*Radix::Net::Unit.NetHub:Edit:Children-Explorer Items*/
										null
									,
									org.radixware.kernel.server.types.Restrictions.Factory.newInstance(16,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdCJC7DXYHFJGQLPCZGJ6WHNYPBI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdF3SRJU4KOXOBDCJRAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdQWDXKHRSO7OBDCJTAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdTZE2P7EKOXOBDCJRAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cmd65WESESVCFDAJOK2HI7OTV3WYE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdPYD77VZHWJG5FD7PCRQETZXWOE")},null,null),
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.FROM_REPLACED,
									null)
							},
							/*Selector presentations*/
							null,
							/*Default selector presentation Id*/
							null,
							/*Presentation Map*/
							org.radixware.kernel.common.utils.Maps.fromArrays(new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprS6PW3X2EKJFUNPGCXYDBACXY7A"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXCFCDDGTRHNRDB5MAALOMT5GDM")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprTLC3CDS7M5DGBJBASBOSAYDODI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprKRWKXDGWONEAPFO67QVMISKWOA")}),
							/*Object title format*/
							null,
							/*Class catalogs*/
							null,
							/*Restrictions*/
							null,null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclXIUFMDMMA7OBDCGDAALOMT5GDM"),

						/*Radix::Net::Unit.NetHub:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::Net::Unit.NetHub:unitId-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGWZKWFUHPZC4PN6Q2XLFCI5O3Q"),"unitId",null,org.radixware.kernel.common.enums.EValType.INT,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("ref3NOPXRFGSVESDPBEZ5CEGCLWHY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colZDYKYPTDNVGZDJ3FHG2KAO2WJE"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Net::Unit.NetHub:sapId-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2EFE2C2G4RBCRCTW3P4PPP75QE"),"sapId",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWHMWLM4WWBEA3LHZH4YKVIGMZM"),org.radixware.kernel.common.enums.EValType.INT,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("ref3NOPXRFGSVESDPBEZ5CEGCLWHY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colBBSLHM5KRFC6VCPLABSQU5OUIY"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Net::Unit.NetHub:echoTestPeriod-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHBG4KM4VQVF35OPCYQH7VNEYC4"),"echoTestPeriod",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXU3GEUJM7FAHHF2EZAWRNFJHTQ"),org.radixware.kernel.common.enums.EValType.INT,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("ref3NOPXRFGSVESDPBEZ5CEGCLWHY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("col2656S67TYBFETI32WUT7QBS4NI"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Net::Unit.NetHub:extPortAddress-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXKVMIMGKP5BYTEPJUIHNLX6HSA"),"extPortAddress",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5X45BFYOLBCWHFECAWGZJXKHWQ"),org.radixware.kernel.common.enums.EValType.STR,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("ref3NOPXRFGSVESDPBEZ5CEGCLWHY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colWKEOMGUMXJEZHHVPQB34HRVO6E"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Net::Unit.NetHub:extPortFrame-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKIKX6HU5WNCZXFGF75N4OHSQCE"),"extPortFrame",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYSW4ETJVDVAXLE7AD3GBNLWBUQ"),org.radixware.kernel.common.enums.EValType.STR,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("ref3NOPXRFGSVESDPBEZ5CEGCLWHY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colEYKFXJJWWVEJVDYCS3R6OSY7DQ"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("%[2R]L%P")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Net::Unit.NetHub:extPortIsServer-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4BU63OQ3QNCUTALKLHR3B3SSKQ"),"extPortIsServer",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWGGO36T7VNCTVMCN2W2HVKLDWQ"),org.radixware.kernel.common.enums.EValType.BOOL,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("ref3NOPXRFGSVESDPBEZ5CEGCLWHY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colOWYHBSFA4JDVVNZC54DLMULQWE"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Net::Unit.NetHub:inSeanceCnt-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKMV7W4EEOVHS3IDZV4RSCNSR6E"),"inSeanceCnt",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEX2IG6OYHFAIRE2MLEXMMHXSNM"),org.radixware.kernel.common.enums.EValType.INT,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("ref3NOPXRFGSVESDPBEZ5CEGCLWHY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colQGN23GPTXNCRVOXRVAGJIF3GBY"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("3")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Net::Unit.NetHub:outSeanceCnt-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBI2OPIVYRJEGHBIACAYHAKVKZA"),"outSeanceCnt",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWQT3UWEBYZAA7LUVDGU7GTZRAQ"),org.radixware.kernel.common.enums.EValType.INT,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("ref3NOPXRFGSVESDPBEZ5CEGCLWHY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("col2IN3M6N2HRGYZHR7OXN3EG57PE"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("3")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Net::Unit.NetHub:outTimeout-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVJY3C7ZGJFCR3PQNKQ4ME6C43Q"),"outTimeout",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls26YMXAT4GBEVRJ5LKMEYIL2F2A"),org.radixware.kernel.common.enums.EValType.INT,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("ref3NOPXRFGSVESDPBEZ5CEGCLWHY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colELNDYCFJWZGVZP2MXGXSP6MG7A"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("30")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Net::Unit.NetHub:reconnectNoEchoCnt-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYEGHE3TXIFHIXKQDATLI5XSSXI"),"reconnectNoEchoCnt",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsITXLMFXHZRC45HMHSU7PQZ6S3U"),org.radixware.kernel.common.enums.EValType.INT,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("ref3NOPXRFGSVESDPBEZ5CEGCLWHY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colXM5GWJUKIJFDZBJLJT4A6UQTXE"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Net::Unit.NetHub:connected-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colURL5B4BJYJGXVJQ6LLJJ7OR424"),"connected",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsULO6JJ4FURENBG5TEYNHBDAQHY"),org.radixware.kernel.common.enums.EValType.BOOL,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("ref3NOPXRFGSVESDPBEZ5CEGCLWHY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colYN3D63AKWVGTFLUGW4ZV6BED44"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Net::Unit.NetHub:lastInUniqueKey-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFR7V6AFFJRHL3LEQPOO4OPNXTY"),"lastInUniqueKey",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRM5VSCQ6PFELHNOTFMA6DNACWM"),org.radixware.kernel.common.enums.EValType.STR,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("ref3NOPXRFGSVESDPBEZ5CEGCLWHY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("col7HDZV2EW75E47PZXZACL6R3JD4"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Net::Unit.NetHub:lastOutStan-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHAHRYM7CWVAKZOCTVWAGXZYYCI"),"lastOutStan",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHR4LKGN7JFAABEWT7YNW5I3GJU"),org.radixware.kernel.common.enums.EValType.INT,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("ref3NOPXRFGSVESDPBEZ5CEGCLWHY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colFFCWEIKLXBGQ3IIBXRS3O6H2GU"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Net::Unit.NetHub:curOutSessionCnt-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLRFAIJEK75H35OIHCW533VHPQQ"),"curOutSessionCnt",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3QDGSYW3IFDHHBFXU4ZS7U4MFU"),org.radixware.kernel.common.enums.EValType.INT,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("ref3NOPXRFGSVESDPBEZ5CEGCLWHY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colPFH4223P7ZEEHJGCXO5NI7PN2M"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Net::Unit.NetHub:stmtGetOutStan-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdPNQ2EWPMN5FXDB4KJSEPCD4BHI"),"stmtGetOutStan",null,org.radixware.kernel.common.enums.EValType.USER_CLASS,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Net::Unit.NetHub:stmtSetOutStan-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdC7NUB2KSOBD5JDK73E6BT2B3HE"),"stmtSetOutStan",null,org.radixware.kernel.common.enums.EValType.USER_CLASS,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Net::Unit.NetHub:stmtIncOutSessionCnt-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd6OBCWBN5NVAIVIJ77552VZ7NA4"),"stmtIncOutSessionCnt",null,org.radixware.kernel.common.enums.EValType.USER_CLASS,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Net::Unit.NetHub:stmtDecOutSessionCnt-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdTK6HQ2DVGVG45EMSU7QFEO7BIE"),"stmtDecOutSessionCnt",null,org.radixware.kernel.common.enums.EValType.USER_CLASS,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Net::Unit.NetHub:stmtSetConnected-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdGKH4KRAADRD7RJO6FJIEUTR2MM"),"stmtSetConnected",null,org.radixware.kernel.common.enums.EValType.USER_CLASS,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Net::Unit.NetHub:stmtGetInStan-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdMWH5VEGBONBX3O4TANGDK7TGKQ"),"stmtGetInStan",null,org.radixware.kernel.common.enums.EValType.USER_CLASS,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Net::Unit.NetHub:stmtSetInStan-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZGCQUQZGMFA7VFNBSV2TYO4MNU"),"stmtSetInStan",null,org.radixware.kernel.common.enums.EValType.USER_CLASS,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Net::Unit.NetHub:stmtResetOutSessionCnt-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdOKDZGOQNMVA6RMFBN5KZR43W5Q"),"stmtResetOutSessionCnt",null,org.radixware.kernel.common.enums.EValType.USER_CLASS,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Net::Unit.NetHub:toProcessConnect-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDXTQN2WF2RFH7BS3NQSNUU6QPU"),"toProcessConnect",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHXJ6OWRENFE6HKZ5K34TB2RSN4"),org.radixware.kernel.common.enums.EValType.BOOL,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("ref3NOPXRFGSVESDPBEZ5CEGCLWHY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colJ2KACST3PRDMNFRVS3FZECI6FQ"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Net::Unit.NetHub:toProcessDisconnect-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3Z4VZLS7Y5GIXD4V5PC6YAEHJM"),"toProcessDisconnect",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZG2HCVGKOZAXXFVL34X3YS6NJE"),org.radixware.kernel.common.enums.EValType.BOOL,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("ref3NOPXRFGSVESDPBEZ5CEGCLWHY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVDYXWL265H43JX6M2UNL6OIKM"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Net::Unit.NetHub:toProcessDuplicatedRq-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGAOGFV575VBG5IHEZAOU7BYRQU"),"toProcessDuplicatedRq",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXYUMEXEHF5BW5DH73NGAPPEXAE"),org.radixware.kernel.common.enums.EValType.BOOL,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("ref3NOPXRFGSVESDPBEZ5CEGCLWHY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colTZUJ7AHXCZBFVLG2NXUO6HIRPM"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Net::Unit.NetHub:toProcessUncorrelatedRs-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUDBAD6URAJD3XGIU6MGDULW6VI"),"toProcessUncorrelatedRs",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXBCYMSYR4RE4BAFBTTLZ6QYLOI"),org.radixware.kernel.common.enums.EValType.BOOL,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("ref3NOPXRFGSVESDPBEZ5CEGCLWHY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colJIN66JIJDRG3NKMUSEUOG2ST2Y"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Net::Unit.NetHub:stmtLock-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdOLV3TKZRVZDRTNEQKEV7MJO52A"),"stmtLock",null,org.radixware.kernel.common.enums.EValType.USER_CLASS,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::Net::Unit.NetHub:toProcessStart-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3QZLKSDOIRCOZM4F63D56D2A6E"),"toProcessStart",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsI5B42OEQVVCWRFTBMMGUZH5ZTA"),org.radixware.kernel.common.enums.EValType.BOOL,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("ref3NOPXRFGSVESDPBEZ5CEGCLWHY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colGG2K5KRRVRAQVCC5MSP3O4GDQA"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Net::Unit.NetHub:toProcessStop-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2CD7SWVNNNAD3CPORJQEHOTZL4"),"toProcessStop",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGB5HJE2LNBBBFE63QUMU4EEPEY"),org.radixware.kernel.common.enums.EValType.BOOL,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("ref3NOPXRFGSVESDPBEZ5CEGCLWHY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colHULW7SPNVFD27B27E37SUJ3FOA"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Net::Unit.NetHub:stmtUnlock-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdDX4DXWULDVAS7A3USHEYGTJHD4"),"stmtUnlock",null,org.radixware.kernel.common.enums.EValType.USER_CLASS,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::Net::Unit.NetHub:stan-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd557QWPUWRNBQ5NJASG2E6UI7VM"),"stan",null,org.radixware.kernel.common.enums.EValType.INT,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::Net::Unit.NetHub:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthQETN7HP66VD2FCEUQ7LE43SVTM"),"getServiceAccessibility",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.CHAR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthACRIT5KA6ZCVHNUXCLLEQCS72M"),"getUri",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthGREMDFUPA7OBDCGDAALOMT5GDM"),"getWsdl",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthH3KBLJ4UA7OBDCGDAALOMT5GDM"),"getSapId",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.INT),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthTD7AAPMUA7OBDCGDAALOMT5GDM"),"setSapId",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("sapId",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprC24MBXJ42JBCVI2L27PCZM3PHY"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthD77F2HBL2FGSVPJHR3SEAMPWMA"),"afterInit",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprTFKL2ABIXJCD7KIJ5YIPDIVZ24")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("phase",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprYW64A7INNFFQBCXZN333W62YTQ"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth5POAOKEZZZBZVDZITYHWB2RPDI"),"onConnect",false,true,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthCKEBJQQXHFCLRKM2UQOS3NHNII"),"onDisconnect",false,true,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth4UZXEJLOWBHSHPVDBJ52MD35DA"),"onInactivityTimer",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthJPQKNSOSMNDQDDXP63J7HRWGXE"),"onMessage",false,true,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("mess",org.radixware.kernel.common.enums.EValType.BIN,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPOGSKL7PIRBAVPNRUCZQ4W3C6U"))
								},org.radixware.kernel.common.enums.EValType.BIN),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth7NQSQ3LO4RC6XKR4PA3J56MUXQ"),"onInvalidMessage",false,true,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("mess",org.radixware.kernel.common.enums.EValType.BIN,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprGZ5WDNYARVF7FHXHWDZQUCNEUQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("errorType",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLDQNNF6PGNFPDCTD4GWELWQ474"))
								},org.radixware.kernel.common.enums.EValType.BIN),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthK56QVX7IBNFH5DUELG3WOJ72XY"),"bundle",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthQIF7CPRFE5GUTIY26PT5SYUD6E"),"getUsedAddresses",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth3J6OGUTWVVAQ7JHAKHPDINN4IQ"),"onAfterRecv",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("rq",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprALTFP75GYFFSZGQMZTO3W6K6EY"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthCFQOLMYWQBE47HX5T7U3424H6A"),"invokeService",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("rq",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprUZH5EG2QXVEVTL3GVW42RY7WGA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("resultClass",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprOI26GAPHSFCCFJFWIZMMO3L5HU"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthALZC3SUYT5EATIEMHDRUVEZCPM"),"invokeRequest",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("mess",org.radixware.kernel.common.enums.EValType.BIN,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprSIKY6CUNFNCG5J3HLDYDPJ5UP4"))
								},org.radixware.kernel.common.enums.EValType.BIN),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthI7BNAEIZBFFILNVZTUODXB5ZQY"),"insertStan",false,true,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("mess",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprOPZU2OCRONDH3ORVQDN4PN2AI4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("prevStan",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRTCOESZNORBZJDTHBE2FZPWQLU"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthLMYZXCJVQFCDROJZUSHFGIDRWQ"),"onBeforeStart",false,true,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth3AO5UTYDKRGRHHNN2XUZLIGXKY"),"onBeforeStop",false,true,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth2A4SAAEM6ZDCFKME5OTPICHL4Y"),"reconnectExtPort",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthTSBWOOQSIJCQZHP2I6HROM7OXM"),"prepareEchoTestRq",false,true,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.BIN),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth26JJ5FWMSFEHNEVDOFZDWB3HBE"),"extractUniqueKey",false,true,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("mess",org.radixware.kernel.common.enums.EValType.BIN,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr6ZRBFNMDYJE2PEE2BFOMPGS2G4"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthFHO53RU6OFH2FFIIE2M6RKIY2M"),"isCorrelated",false,true,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("rqMess",org.radixware.kernel.common.enums.EValType.BIN,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQ4FHR6OPZBAEHNRBL4RLKPACJM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("rqParsingVars",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprUKGWXJWSOBHZ7C3MGG4CSH63TA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("rsMess",org.radixware.kernel.common.enums.EValType.BIN,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprYWBMPQ2FNJGXHOHL35UAPKNP2I")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("rsParsingVars",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2VZ5SK2LWRBI7N5GUKXCDZ4SPM"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthIZLS7B3UUJHHFIBEWZPRMPO2G4"),"isRequest",false,true,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("mess",org.radixware.kernel.common.enums.EValType.BIN,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQXCRUSU37JFQ5JCVSS4OWD7N3U")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("parsingVars",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr4JBFL373AVB6FDX3BWNDBM7HKU"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthHUAWAZE5SNFBFIMI3XNYXNMLYY"),"traceDebug",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("mess",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprOTI433GX4NHELK36TJCB44RBGE"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthEI6PCDVMHBFD5LXNH3BVHFXWKQ"),"trace",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("severity",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr25GX5IHAYZEQVBSDG63VVCUJTU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("mess",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprMAB6RTOB7ZFKHOLAUZQJU7G4EI"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthZP5HXBIKTVGKPCGLJQD5PYXOQE"),"traceEvent",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("mess",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr25XFEDKQMVHRLNF7QN5FE3XQVY"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthWGTO3WZXGJFGZL5Q72M4NOQGBA"),"traceError",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("mess",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprFVI6NFC7KNB4FNMP2IYVQ3E42A"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmdPYD77VZHWJG5FD7PCRQETZXWOE"),"onCommand_EchoTest",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newPropValHandlersById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr4TTZOROJEBCWHGVVRCFXIYKHLU"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmd65WESESVCFDAJOK2HI7OTV3WYE"),"onCommand_Reconnect",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newPropValHandlersById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprDRFT4JCXWZGHNL225VDEZTHRFM"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthJAVCXG2JBJEVPJWRR42XLLET3U"),"sendMessage",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("mess",org.radixware.kernel.common.enums.EValType.BIN,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprSIKY6CUNFNCG5J3HLDYDPJ5UP4"))
								},null)
						},
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("ref3NOPXRFGSVESDPBEZ5CEGCLWHY")},
						null,null,null,false);
}

/* Radix::Net::Unit.NetHub - Desktop Executable*/

/*Radix::Net::Unit.NetHub-Application Class*/

package org.radixware.ads.Net.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub")
public interface Unit.NetHub   extends org.radixware.ads.System.explorer.Unit.AbstractServiceDriver  {























































































































































































































































































































	/*Radix::Net::Unit.NetHub:toProcessStop:toProcessStop-Presentation Property*/


	public class ToProcessStop extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public ToProcessStop(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Bool dummy = ((Bool)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:toProcessStop:toProcessStop")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:toProcessStop:toProcessStop")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public ToProcessStop getToProcessStop();
	/*Radix::Net::Unit.NetHub:sapId:sapId-Presentation Property*/


	public class SapId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public SapId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:sapId:sapId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:sapId:sapId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public SapId getSapId();
	/*Radix::Net::Unit.NetHub:toProcessStart:toProcessStart-Presentation Property*/


	public class ToProcessStart extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public ToProcessStart(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Bool dummy = ((Bool)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:toProcessStart:toProcessStart")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:toProcessStart:toProcessStart")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public ToProcessStart getToProcessStart();
	/*Radix::Net::Unit.NetHub:toProcessDisconnect:toProcessDisconnect-Presentation Property*/


	public class ToProcessDisconnect extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public ToProcessDisconnect(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Bool dummy = ((Bool)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:toProcessDisconnect:toProcessDisconnect")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:toProcessDisconnect:toProcessDisconnect")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public ToProcessDisconnect getToProcessDisconnect();
	/*Radix::Net::Unit.NetHub:extPortIsServer:extPortIsServer-Presentation Property*/


	public class ExtPortIsServer extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public ExtPortIsServer(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Bool dummy = ((Bool)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:extPortIsServer:extPortIsServer")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:extPortIsServer:extPortIsServer")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public ExtPortIsServer getExtPortIsServer();
	/*Radix::Net::Unit.NetHub:outSeanceCnt:outSeanceCnt-Presentation Property*/


	public class OutSeanceCnt extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public OutSeanceCnt(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:outSeanceCnt:outSeanceCnt")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:outSeanceCnt:outSeanceCnt")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public OutSeanceCnt getOutSeanceCnt();
	/*Radix::Net::Unit.NetHub:toProcessConnect:toProcessConnect-Presentation Property*/


	public class ToProcessConnect extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public ToProcessConnect(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Bool dummy = ((Bool)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:toProcessConnect:toProcessConnect")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:toProcessConnect:toProcessConnect")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public ToProcessConnect getToProcessConnect();
	/*Radix::Net::Unit.NetHub:lastInUniqueKey:lastInUniqueKey-Presentation Property*/


	public class LastInUniqueKey extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public LastInUniqueKey(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:lastInUniqueKey:lastInUniqueKey")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:lastInUniqueKey:lastInUniqueKey")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public LastInUniqueKey getLastInUniqueKey();
	/*Radix::Net::Unit.NetHub:toProcessDuplicatedRq:toProcessDuplicatedRq-Presentation Property*/


	public class ToProcessDuplicatedRq extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public ToProcessDuplicatedRq(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Bool dummy = ((Bool)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:toProcessDuplicatedRq:toProcessDuplicatedRq")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:toProcessDuplicatedRq:toProcessDuplicatedRq")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public ToProcessDuplicatedRq getToProcessDuplicatedRq();
	/*Radix::Net::Unit.NetHub:unitId:unitId-Presentation Property*/


	public class UnitId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public UnitId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:unitId:unitId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:unitId:unitId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public UnitId getUnitId();
	/*Radix::Net::Unit.NetHub:lastOutStan:lastOutStan-Presentation Property*/


	public class LastOutStan extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public LastOutStan(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:lastOutStan:lastOutStan")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:lastOutStan:lastOutStan")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public LastOutStan getLastOutStan();
	/*Radix::Net::Unit.NetHub:echoTestPeriod:echoTestPeriod-Presentation Property*/


	public class EchoTestPeriod extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public EchoTestPeriod(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:echoTestPeriod:echoTestPeriod")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:echoTestPeriod:echoTestPeriod")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public EchoTestPeriod getEchoTestPeriod();
	/*Radix::Net::Unit.NetHub:extPortFrame:extPortFrame-Presentation Property*/


	public class ExtPortFrame extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ExtPortFrame(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:extPortFrame:extPortFrame")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:extPortFrame:extPortFrame")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ExtPortFrame getExtPortFrame();
	/*Radix::Net::Unit.NetHub:inSeanceCnt:inSeanceCnt-Presentation Property*/


	public class InSeanceCnt extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public InSeanceCnt(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:inSeanceCnt:inSeanceCnt")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:inSeanceCnt:inSeanceCnt")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public InSeanceCnt getInSeanceCnt();
	/*Radix::Net::Unit.NetHub:curOutSessionCnt:curOutSessionCnt-Presentation Property*/


	public class CurOutSessionCnt extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public CurOutSessionCnt(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:curOutSessionCnt:curOutSessionCnt")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:curOutSessionCnt:curOutSessionCnt")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public CurOutSessionCnt getCurOutSessionCnt();
	/*Radix::Net::Unit.NetHub:toProcessUncorrelatedRs:toProcessUncorrelatedRs-Presentation Property*/


	public class ToProcessUncorrelatedRs extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public ToProcessUncorrelatedRs(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Bool dummy = ((Bool)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:toProcessUncorrelatedRs:toProcessUncorrelatedRs")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:toProcessUncorrelatedRs:toProcessUncorrelatedRs")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public ToProcessUncorrelatedRs getToProcessUncorrelatedRs();
	/*Radix::Net::Unit.NetHub:connected:connected-Presentation Property*/


	public class Connected extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public Connected(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Bool dummy = ((Bool)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:connected:connected")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:connected:connected")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public Connected getConnected();
	/*Radix::Net::Unit.NetHub:outTimeout:outTimeout-Presentation Property*/


	public class OutTimeout extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public OutTimeout(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:outTimeout:outTimeout")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:outTimeout:outTimeout")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public OutTimeout getOutTimeout();
	/*Radix::Net::Unit.NetHub:extPortAddress:extPortAddress-Presentation Property*/


	public class ExtPortAddress extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ExtPortAddress(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:extPortAddress:extPortAddress")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:extPortAddress:extPortAddress")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ExtPortAddress getExtPortAddress();
	/*Radix::Net::Unit.NetHub:reconnectNoEchoCnt:reconnectNoEchoCnt-Presentation Property*/


	public class ReconnectNoEchoCnt extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ReconnectNoEchoCnt(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:reconnectNoEchoCnt:reconnectNoEchoCnt")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:reconnectNoEchoCnt:reconnectNoEchoCnt")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ReconnectNoEchoCnt getReconnectNoEchoCnt();
	/*Radix::Net::Unit.NetHub:stan:stan-Presentation Property*/


	public class Stan extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public Stan(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:stan:stan")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:stan:stan")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public Stan getStan();
	public static class Reconnect extends org.radixware.kernel.common.client.models.items.Command{
		protected Reconnect(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.ads.Net.common.NetHubCmdXsd.ReconnectRs send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.ads.Net.common.NetHubCmdXsd.ReconnectRs)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.ads.Net.common.NetHubCmdXsd.ReconnectRs.class);
		}

	}

	public static class EchoTest extends org.radixware.kernel.common.client.models.items.Command{
		protected EchoTest(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.ads.Net.common.NetHubCmdXsd.EchoTestRs send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.ads.Net.common.NetHubCmdXsd.EchoTestRs)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.ads.Net.common.NetHubCmdXsd.EchoTestRs.class);
		}

	}



}

/* Radix::Net::Unit.NetHub - Desktop Meta*/

/*Radix::Net::Unit.NetHub-Application Class*/

package org.radixware.ads.Net.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Unit.NetHub_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::Net::Unit.NetHub:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),
			"Radix::Net::Unit.NetHub",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclXIUFMDMMA7OBDCGDAALOMT5GDM"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsU75MXOL3GBDWBHUUNP6WZTE4OA"),null,null,0,

			/*Radix::Net::Unit.NetHub:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::Net::Unit.NetHub:unitId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGWZKWFUHPZC4PN6Q2XLFCI5O3Q"),
						"unitId",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ON_CREATE,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Net::Unit.NetHub:unitId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::Unit.NetHub:sapId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2EFE2C2G4RBCRCTW3P4PPP75QE"),
						"sapId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWHMWLM4WWBEA3LHZH4YKVIGMZM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Net::Unit.NetHub:sapId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::Unit.NetHub:echoTestPeriod:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHBG4KM4VQVF35OPCYQH7VNEYC4"),
						"echoTestPeriod",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXU3GEUJM7FAHHF2EZAWRNFJHTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Net::Unit.NetHub:echoTestPeriod:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::Unit.NetHub:extPortAddress:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXKVMIMGKP5BYTEPJUIHNLX6HSA"),
						"extPortAddress",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5X45BFYOLBCWHFECAWGZJXKHWQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Net::Unit.NetHub:extPortAddress:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,150,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::Unit.NetHub:extPortFrame:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKIKX6HU5WNCZXFGF75N4OHSQCE"),
						"extPortFrame",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYSW4ETJVDVAXLE7AD3GBNLWBUQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("%[2R]L%P"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Net::Unit.NetHub:extPortFrame:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,200,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::Unit.NetHub:extPortIsServer:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4BU63OQ3QNCUTALKLHR3B3SSKQ"),
						"extPortIsServer",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWGGO36T7VNCTVMCN2W2HVKLDWQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Net::Unit.NetHub:extPortIsServer:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::Unit.NetHub:inSeanceCnt:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKMV7W4EEOVHS3IDZV4RSCNSR6E"),
						"inSeanceCnt",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEX2IG6OYHFAIRE2MLEXMMHXSNM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("3"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Net::Unit.NetHub:inSeanceCnt:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::Unit.NetHub:outSeanceCnt:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBI2OPIVYRJEGHBIACAYHAKVKZA"),
						"outSeanceCnt",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWQT3UWEBYZAA7LUVDGU7GTZRAQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("3"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Net::Unit.NetHub:outSeanceCnt:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::Unit.NetHub:outTimeout:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVJY3C7ZGJFCR3PQNKQ4ME6C43Q"),
						"outTimeout",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls26YMXAT4GBEVRJ5LKMEYIL2F2A"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("30"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Net::Unit.NetHub:outTimeout:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::Unit.NetHub:reconnectNoEchoCnt:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYEGHE3TXIFHIXKQDATLI5XSSXI"),
						"reconnectNoEchoCnt",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsITXLMFXHZRC45HMHSU7PQZ6S3U"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Net::Unit.NetHub:reconnectNoEchoCnt:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::Unit.NetHub:connected:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colURL5B4BJYJGXVJQ6LLJJ7OR424"),
						"connected",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsULO6JJ4FURENBG5TEYNHBDAQHY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Net::Unit.NetHub:connected:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::Unit.NetHub:lastInUniqueKey:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFR7V6AFFJRHL3LEQPOO4OPNXTY"),
						"lastInUniqueKey",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRM5VSCQ6PFELHNOTFMA6DNACWM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Net::Unit.NetHub:lastInUniqueKey:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::Unit.NetHub:lastOutStan:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHAHRYM7CWVAKZOCTVWAGXZYYCI"),
						"lastOutStan",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHR4LKGN7JFAABEWT7YNW5I3GJU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Net::Unit.NetHub:lastOutStan:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::Unit.NetHub:curOutSessionCnt:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLRFAIJEK75H35OIHCW533VHPQQ"),
						"curOutSessionCnt",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3QDGSYW3IFDHHBFXU4ZS7U4MFU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Net::Unit.NetHub:curOutSessionCnt:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::Unit.NetHub:toProcessConnect:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDXTQN2WF2RFH7BS3NQSNUU6QPU"),
						"toProcessConnect",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHXJ6OWRENFE6HKZ5K34TB2RSN4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Net::Unit.NetHub:toProcessConnect:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::Unit.NetHub:toProcessDisconnect:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3Z4VZLS7Y5GIXD4V5PC6YAEHJM"),
						"toProcessDisconnect",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZG2HCVGKOZAXXFVL34X3YS6NJE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Net::Unit.NetHub:toProcessDisconnect:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::Unit.NetHub:toProcessDuplicatedRq:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGAOGFV575VBG5IHEZAOU7BYRQU"),
						"toProcessDuplicatedRq",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXYUMEXEHF5BW5DH73NGAPPEXAE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Net::Unit.NetHub:toProcessDuplicatedRq:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::Unit.NetHub:toProcessUncorrelatedRs:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUDBAD6URAJD3XGIU6MGDULW6VI"),
						"toProcessUncorrelatedRs",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXBCYMSYR4RE4BAFBTTLZ6QYLOI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Net::Unit.NetHub:toProcessUncorrelatedRs:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::Unit.NetHub:toProcessStart:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3QZLKSDOIRCOZM4F63D56D2A6E"),
						"toProcessStart",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsI5B42OEQVVCWRFTBMMGUZH5ZTA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Net::Unit.NetHub:toProcessStart:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::Unit.NetHub:toProcessStop:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2CD7SWVNNNAD3CPORJQEHOTZL4"),
						"toProcessStop",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGB5HJE2LNBBBFE63QUMU4EEPEY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Net::Unit.NetHub:toProcessStop:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::Unit.NetHub:stan:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd557QWPUWRNBQ5NJASG2E6UI7VM"),
						"stan",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Net::Unit.NetHub:stan:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(null,null,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			new org.radixware.kernel.common.client.meta.RadCommandDef[]{
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Net::Unit.NetHub:Reconnect-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmd65WESESVCFDAJOK2HI7OTV3WYE"),
						"Reconnect",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMGRFXXEJ5RF35K3ZQCMBTKI5TI"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("img6OAQVUCHR5ABXCLHBQNP26QE3Q"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,
						true),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Net::Unit.NetHub:EchoTest-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdPYD77VZHWJG5FD7PCRQETZXWOE"),
						"EchoTest",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKIWGXKEVH5CTLJG62QDPPKFQWI"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgWZZPS3ZBBNFVNHFNSKTITBMHQE"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,
						true)
			},
			null,
			null,
			null,
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprTLC3CDS7M5DGBJBASBOSAYDODI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprKRWKXDGWONEAPFO67QVMISKWOA")},
			true,true,false);
}

/* Radix::Net::Unit.NetHub - Web Executable*/

/*Radix::Net::Unit.NetHub-Application Class*/

package org.radixware.ads.Net.web;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub")
public interface Unit.NetHub   extends org.radixware.ads.System.web.Unit.AbstractServiceDriver  {























































































































































































































































































































	/*Radix::Net::Unit.NetHub:toProcessStop:toProcessStop-Presentation Property*/


	public class ToProcessStop extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public ToProcessStop(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Bool dummy = ((Bool)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:toProcessStop:toProcessStop")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:toProcessStop:toProcessStop")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public ToProcessStop getToProcessStop();
	/*Radix::Net::Unit.NetHub:sapId:sapId-Presentation Property*/


	public class SapId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public SapId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:sapId:sapId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:sapId:sapId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public SapId getSapId();
	/*Radix::Net::Unit.NetHub:toProcessStart:toProcessStart-Presentation Property*/


	public class ToProcessStart extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public ToProcessStart(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Bool dummy = ((Bool)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:toProcessStart:toProcessStart")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:toProcessStart:toProcessStart")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public ToProcessStart getToProcessStart();
	/*Radix::Net::Unit.NetHub:toProcessDisconnect:toProcessDisconnect-Presentation Property*/


	public class ToProcessDisconnect extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public ToProcessDisconnect(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Bool dummy = ((Bool)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:toProcessDisconnect:toProcessDisconnect")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:toProcessDisconnect:toProcessDisconnect")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public ToProcessDisconnect getToProcessDisconnect();
	/*Radix::Net::Unit.NetHub:extPortIsServer:extPortIsServer-Presentation Property*/


	public class ExtPortIsServer extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public ExtPortIsServer(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Bool dummy = ((Bool)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:extPortIsServer:extPortIsServer")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:extPortIsServer:extPortIsServer")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public ExtPortIsServer getExtPortIsServer();
	/*Radix::Net::Unit.NetHub:outSeanceCnt:outSeanceCnt-Presentation Property*/


	public class OutSeanceCnt extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public OutSeanceCnt(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:outSeanceCnt:outSeanceCnt")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:outSeanceCnt:outSeanceCnt")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public OutSeanceCnt getOutSeanceCnt();
	/*Radix::Net::Unit.NetHub:toProcessConnect:toProcessConnect-Presentation Property*/


	public class ToProcessConnect extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public ToProcessConnect(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Bool dummy = ((Bool)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:toProcessConnect:toProcessConnect")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:toProcessConnect:toProcessConnect")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public ToProcessConnect getToProcessConnect();
	/*Radix::Net::Unit.NetHub:lastInUniqueKey:lastInUniqueKey-Presentation Property*/


	public class LastInUniqueKey extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public LastInUniqueKey(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:lastInUniqueKey:lastInUniqueKey")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:lastInUniqueKey:lastInUniqueKey")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public LastInUniqueKey getLastInUniqueKey();
	/*Radix::Net::Unit.NetHub:toProcessDuplicatedRq:toProcessDuplicatedRq-Presentation Property*/


	public class ToProcessDuplicatedRq extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public ToProcessDuplicatedRq(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Bool dummy = ((Bool)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:toProcessDuplicatedRq:toProcessDuplicatedRq")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:toProcessDuplicatedRq:toProcessDuplicatedRq")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public ToProcessDuplicatedRq getToProcessDuplicatedRq();
	/*Radix::Net::Unit.NetHub:unitId:unitId-Presentation Property*/


	public class UnitId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public UnitId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:unitId:unitId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:unitId:unitId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public UnitId getUnitId();
	/*Radix::Net::Unit.NetHub:lastOutStan:lastOutStan-Presentation Property*/


	public class LastOutStan extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public LastOutStan(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:lastOutStan:lastOutStan")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:lastOutStan:lastOutStan")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public LastOutStan getLastOutStan();
	/*Radix::Net::Unit.NetHub:echoTestPeriod:echoTestPeriod-Presentation Property*/


	public class EchoTestPeriod extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public EchoTestPeriod(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:echoTestPeriod:echoTestPeriod")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:echoTestPeriod:echoTestPeriod")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public EchoTestPeriod getEchoTestPeriod();
	/*Radix::Net::Unit.NetHub:extPortFrame:extPortFrame-Presentation Property*/


	public class ExtPortFrame extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ExtPortFrame(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:extPortFrame:extPortFrame")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:extPortFrame:extPortFrame")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ExtPortFrame getExtPortFrame();
	/*Radix::Net::Unit.NetHub:inSeanceCnt:inSeanceCnt-Presentation Property*/


	public class InSeanceCnt extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public InSeanceCnt(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:inSeanceCnt:inSeanceCnt")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:inSeanceCnt:inSeanceCnt")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public InSeanceCnt getInSeanceCnt();
	/*Radix::Net::Unit.NetHub:curOutSessionCnt:curOutSessionCnt-Presentation Property*/


	public class CurOutSessionCnt extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public CurOutSessionCnt(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:curOutSessionCnt:curOutSessionCnt")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:curOutSessionCnt:curOutSessionCnt")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public CurOutSessionCnt getCurOutSessionCnt();
	/*Radix::Net::Unit.NetHub:toProcessUncorrelatedRs:toProcessUncorrelatedRs-Presentation Property*/


	public class ToProcessUncorrelatedRs extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public ToProcessUncorrelatedRs(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Bool dummy = ((Bool)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:toProcessUncorrelatedRs:toProcessUncorrelatedRs")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:toProcessUncorrelatedRs:toProcessUncorrelatedRs")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public ToProcessUncorrelatedRs getToProcessUncorrelatedRs();
	/*Radix::Net::Unit.NetHub:connected:connected-Presentation Property*/


	public class Connected extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public Connected(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Bool dummy = ((Bool)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:connected:connected")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:connected:connected")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public Connected getConnected();
	/*Radix::Net::Unit.NetHub:outTimeout:outTimeout-Presentation Property*/


	public class OutTimeout extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public OutTimeout(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:outTimeout:outTimeout")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:outTimeout:outTimeout")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public OutTimeout getOutTimeout();
	/*Radix::Net::Unit.NetHub:extPortAddress:extPortAddress-Presentation Property*/


	public class ExtPortAddress extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ExtPortAddress(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:extPortAddress:extPortAddress")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:extPortAddress:extPortAddress")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ExtPortAddress getExtPortAddress();
	/*Radix::Net::Unit.NetHub:reconnectNoEchoCnt:reconnectNoEchoCnt-Presentation Property*/


	public class ReconnectNoEchoCnt extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ReconnectNoEchoCnt(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:reconnectNoEchoCnt:reconnectNoEchoCnt")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:reconnectNoEchoCnt:reconnectNoEchoCnt")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ReconnectNoEchoCnt getReconnectNoEchoCnt();
	/*Radix::Net::Unit.NetHub:stan:stan-Presentation Property*/


	public class Stan extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public Stan(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:stan:stan")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:stan:stan")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public Stan getStan();
	public static class Reconnect extends org.radixware.kernel.common.client.models.items.Command{
		protected Reconnect(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.ads.Net.common.NetHubCmdXsd.ReconnectRs send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.ads.Net.common.NetHubCmdXsd.ReconnectRs)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.ads.Net.common.NetHubCmdXsd.ReconnectRs.class);
		}

	}

	public static class EchoTest extends org.radixware.kernel.common.client.models.items.Command{
		protected EchoTest(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.ads.Net.common.NetHubCmdXsd.EchoTestRs send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.ads.Net.common.NetHubCmdXsd.EchoTestRs)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.ads.Net.common.NetHubCmdXsd.EchoTestRs.class);
		}

	}



}

/* Radix::Net::Unit.NetHub - Web Meta*/

/*Radix::Net::Unit.NetHub-Application Class*/

package org.radixware.ads.Net.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Unit.NetHub_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::Net::Unit.NetHub:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),
			"Radix::Net::Unit.NetHub",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclXIUFMDMMA7OBDCGDAALOMT5GDM"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsU75MXOL3GBDWBHUUNP6WZTE4OA"),null,null,0,

			/*Radix::Net::Unit.NetHub:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::Net::Unit.NetHub:unitId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGWZKWFUHPZC4PN6Q2XLFCI5O3Q"),
						"unitId",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ON_CREATE,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Net::Unit.NetHub:unitId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::Unit.NetHub:sapId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2EFE2C2G4RBCRCTW3P4PPP75QE"),
						"sapId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWHMWLM4WWBEA3LHZH4YKVIGMZM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Net::Unit.NetHub:sapId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::Unit.NetHub:echoTestPeriod:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHBG4KM4VQVF35OPCYQH7VNEYC4"),
						"echoTestPeriod",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXU3GEUJM7FAHHF2EZAWRNFJHTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Net::Unit.NetHub:echoTestPeriod:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::Unit.NetHub:extPortAddress:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXKVMIMGKP5BYTEPJUIHNLX6HSA"),
						"extPortAddress",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5X45BFYOLBCWHFECAWGZJXKHWQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Net::Unit.NetHub:extPortAddress:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,150,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::Unit.NetHub:extPortFrame:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKIKX6HU5WNCZXFGF75N4OHSQCE"),
						"extPortFrame",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYSW4ETJVDVAXLE7AD3GBNLWBUQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("%[2R]L%P"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Net::Unit.NetHub:extPortFrame:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,200,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::Unit.NetHub:extPortIsServer:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4BU63OQ3QNCUTALKLHR3B3SSKQ"),
						"extPortIsServer",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWGGO36T7VNCTVMCN2W2HVKLDWQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Net::Unit.NetHub:extPortIsServer:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::Unit.NetHub:inSeanceCnt:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKMV7W4EEOVHS3IDZV4RSCNSR6E"),
						"inSeanceCnt",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEX2IG6OYHFAIRE2MLEXMMHXSNM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("3"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Net::Unit.NetHub:inSeanceCnt:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::Unit.NetHub:outSeanceCnt:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBI2OPIVYRJEGHBIACAYHAKVKZA"),
						"outSeanceCnt",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWQT3UWEBYZAA7LUVDGU7GTZRAQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("3"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Net::Unit.NetHub:outSeanceCnt:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::Unit.NetHub:outTimeout:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVJY3C7ZGJFCR3PQNKQ4ME6C43Q"),
						"outTimeout",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls26YMXAT4GBEVRJ5LKMEYIL2F2A"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("30"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Net::Unit.NetHub:outTimeout:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::Unit.NetHub:reconnectNoEchoCnt:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYEGHE3TXIFHIXKQDATLI5XSSXI"),
						"reconnectNoEchoCnt",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsITXLMFXHZRC45HMHSU7PQZ6S3U"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Net::Unit.NetHub:reconnectNoEchoCnt:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::Unit.NetHub:connected:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colURL5B4BJYJGXVJQ6LLJJ7OR424"),
						"connected",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsULO6JJ4FURENBG5TEYNHBDAQHY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Net::Unit.NetHub:connected:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::Unit.NetHub:lastInUniqueKey:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFR7V6AFFJRHL3LEQPOO4OPNXTY"),
						"lastInUniqueKey",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRM5VSCQ6PFELHNOTFMA6DNACWM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Net::Unit.NetHub:lastInUniqueKey:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::Unit.NetHub:lastOutStan:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHAHRYM7CWVAKZOCTVWAGXZYYCI"),
						"lastOutStan",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHR4LKGN7JFAABEWT7YNW5I3GJU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Net::Unit.NetHub:lastOutStan:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::Unit.NetHub:curOutSessionCnt:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLRFAIJEK75H35OIHCW533VHPQQ"),
						"curOutSessionCnt",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3QDGSYW3IFDHHBFXU4ZS7U4MFU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Net::Unit.NetHub:curOutSessionCnt:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::Unit.NetHub:toProcessConnect:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDXTQN2WF2RFH7BS3NQSNUU6QPU"),
						"toProcessConnect",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHXJ6OWRENFE6HKZ5K34TB2RSN4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Net::Unit.NetHub:toProcessConnect:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::Unit.NetHub:toProcessDisconnect:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3Z4VZLS7Y5GIXD4V5PC6YAEHJM"),
						"toProcessDisconnect",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZG2HCVGKOZAXXFVL34X3YS6NJE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Net::Unit.NetHub:toProcessDisconnect:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::Unit.NetHub:toProcessDuplicatedRq:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGAOGFV575VBG5IHEZAOU7BYRQU"),
						"toProcessDuplicatedRq",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXYUMEXEHF5BW5DH73NGAPPEXAE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Net::Unit.NetHub:toProcessDuplicatedRq:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::Unit.NetHub:toProcessUncorrelatedRs:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUDBAD6URAJD3XGIU6MGDULW6VI"),
						"toProcessUncorrelatedRs",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXBCYMSYR4RE4BAFBTTLZ6QYLOI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Net::Unit.NetHub:toProcessUncorrelatedRs:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::Unit.NetHub:toProcessStart:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3QZLKSDOIRCOZM4F63D56D2A6E"),
						"toProcessStart",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsI5B42OEQVVCWRFTBMMGUZH5ZTA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Net::Unit.NetHub:toProcessStart:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::Unit.NetHub:toProcessStop:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2CD7SWVNNNAD3CPORJQEHOTZL4"),
						"toProcessStop",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGB5HJE2LNBBBFE63QUMU4EEPEY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Net::Unit.NetHub:toProcessStop:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::Unit.NetHub:stan:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd557QWPUWRNBQ5NJASG2E6UI7VM"),
						"stan",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Net::Unit.NetHub:stan:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(null,null,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			new org.radixware.kernel.common.client.meta.RadCommandDef[]{
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Net::Unit.NetHub:Reconnect-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmd65WESESVCFDAJOK2HI7OTV3WYE"),
						"Reconnect",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMGRFXXEJ5RF35K3ZQCMBTKI5TI"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("img6OAQVUCHR5ABXCLHBQNP26QE3Q"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,
						true),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Net::Unit.NetHub:EchoTest-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdPYD77VZHWJG5FD7PCRQETZXWOE"),
						"EchoTest",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKIWGXKEVH5CTLJG62QDPPKFQWI"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgWZZPS3ZBBNFVNHFNSKTITBMHQE"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,
						true)
			},
			null,
			null,
			null,
			null,
			true,true,false);
}

/* Radix::Net::Unit.NetHub:Create - Desktop Meta*/

/*Radix::Net::Unit.NetHub:Create-Editor Presentation*/

package org.radixware.ads.Net.explorer;
public final class Create_mi{
	private static final class Create_DEF extends org.radixware.kernel.common.client.meta.RadEditorPresentationDef{
		Create_DEF(){
			super(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprTLC3CDS7M5DGBJBASBOSAYDODI"),
			"Create",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("eprKRWKXDGWONEAPFO67QVMISKWOA"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
			null,
			null,
			null,
			null,

			/*Radix::Net::Unit.NetHub:Create:Children-Explorer Items*/
				null
			,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
			null,
			0,
			null,
			(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
			40114,0,0);
		}
		@Override
		protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.Net.explorer.Edit:Model(userSession,this);
		}
	}
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new Create_DEF(); 
;
}
/* Radix::Net::Unit.NetHub:Create - Web Meta*/

/*Radix::Net::Unit.NetHub:Create-Editor Presentation*/

package org.radixware.ads.Net.web;
public final class Create_mi{
	private static final class Create_DEF extends org.radixware.kernel.common.client.meta.RadEditorPresentationDef{
		Create_DEF(){
			super(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprTLC3CDS7M5DGBJBASBOSAYDODI"),
			"Create",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("eprKRWKXDGWONEAPFO67QVMISKWOA"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
			null,
			null,
			null,
			null,

			/*Radix::Net::Unit.NetHub:Create:Children-Explorer Items*/
				null
			,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
			null,
			0,
			null,
			(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
			40114,0,0);
		}
		@Override
		protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.Net.web.Edit:Model(userSession,this);
		}
	}
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new Create_DEF(); 
;
}
/* Radix::Net::Unit.NetHub:Edit - Desktop Meta*/

/*Radix::Net::Unit.NetHub:Edit-Editor Presentation*/

package org.radixware.ads.Net.explorer;
public final class Edit_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprKRWKXDGWONEAPFO67QVMISKWOA"),
	"Edit",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("eprE7BZV3MWA7OBDCGDAALOMT5GDM"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
	null,
	null,

	/*Radix::Net::Unit.NetHub:Edit:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::Net::Unit.NetHub:Edit:TraceOptions-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgUJB2RCH6KZHD5IMTQTE76DQSZA"),"TraceOptions",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNCWR775PCZAUFER2UWMUFW5GOU"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col33BGOAFDTZGY3IZ3IUZLVC2UPM"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZF4DFPFNQZFWXENBIJJQ7HAYSY"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6L4O4LIEOFAGTJ4KMHSY5RMTZE"),0,2,1,false,false)
			},null),

			/*Radix::Net::Unit.NetHub:Edit:Settings-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgB7PLODNX2RFE5A5WPL634HVXJI"),"Settings",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsK4HS4TNRRFHH5B5IYETZYE46QQ"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXKVMIMGKP5BYTEPJUIHNLX6HSA"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKIKX6HU5WNCZXFGF75N4OHSQCE"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4BU63OQ3QNCUTALKLHR3B3SSKQ"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHBG4KM4VQVF35OPCYQH7VNEYC4"),0,6,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKMV7W4EEOVHS3IDZV4RSCNSR6E"),0,4,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBI2OPIVYRJEGHBIACAYHAKVKZA"),0,5,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVJY3C7ZGJFCR3PQNKQ4ME6C43Q"),0,3,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colURL5B4BJYJGXVJQ6LLJJ7OR424"),0,17,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYEGHE3TXIFHIXKQDATLI5XSSXI"),0,7,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFR7V6AFFJRHL3LEQPOO4OPNXTY"),0,15,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHAHRYM7CWVAKZOCTVWAGXZYYCI"),0,16,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLRFAIJEK75H35OIHCW533VHPQQ"),0,14,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDXTQN2WF2RFH7BS3NQSNUU6QPU"),0,10,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3Z4VZLS7Y5GIXD4V5PC6YAEHJM"),0,11,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGAOGFV575VBG5IHEZAOU7BYRQU"),0,13,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUDBAD6URAJD3XGIU6MGDULW6VI"),0,12,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3QZLKSDOIRCOZM4F63D56D2A6E"),0,8,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2CD7SWVNNNAD3CPORJQEHOTZL4"),0,9,1,false,false)
			},null),

			/*Radix::Net::Unit.NetHub:Edit:General-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgOCDVZAOTBXOBDNTPAAMPGXSZKU"),"General",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZ2VQT7OPZVCXNEFQCSABY7ABBY"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVLUYADHR5VDBNSIAAUMFADAIA"),0,0,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdU7QY4J3ZXDORDF72ABIFNQAABA"),0,2,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colI6NERFZYS5VDBFCXAAUMFADAIA"),0,3,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colY5NZZOJBXTNRDB6QAALOMT5GDM"),0,4,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVX5YH2SS5VDBN2IAAUMFADAIA"),0,5,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdBWQ33IP5GJER3MDGKP5K7RNDHQ"),0,6,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd65KAVNFIANDIFIOL2PUOBZWAZI"),1,6,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTYUCIQQYTZCDVDV7MJQVDW2M5A"),1,5,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVA2Y4NZKIZAS7DIOUYDAT6VCQI"),0,1,2,false,false)
			},null)
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgOCDVZAOTBXOBDNTPAAMPGXSZKU")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgB7PLODNX2RFE5A5WPL634HVXJI")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgKYIKI4FJUFFNVLRYCIE7H5KCVM")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgUJB2RCH6KZHD5IMTQTE76DQSZA")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgJ4BHY7HBLBBBXIIQZEPCKTW2MU"))}
	,

	/*Radix::Net::Unit.NetHub:Edit:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	16,
	new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdCJC7DXYHFJGQLPCZGJ6WHNYPBI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdF3SRJU4KOXOBDCJRAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdQWDXKHRSO7OBDCJTAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdTZE2P7EKOXOBDCJRAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cmd65WESESVCFDAJOK2HI7OTV3WYE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdPYD77VZHWJG5FD7PCRQETZXWOE")},
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	35984,0,0);
}
/* Radix::Net::Unit.NetHub:Edit - Web Meta*/

/*Radix::Net::Unit.NetHub:Edit-Editor Presentation*/

package org.radixware.ads.Net.web;
public final class Edit_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprKRWKXDGWONEAPFO67QVMISKWOA"),
	"Edit",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("eprE7BZV3MWA7OBDCGDAALOMT5GDM"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
	null,
	null,

	/*Radix::Net::Unit.NetHub:Edit:Editor Pages-Editor Presentation Pages*/
	null,
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
	}
	,

	/*Radix::Net::Unit.NetHub:Edit:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	16,
	new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdCJC7DXYHFJGQLPCZGJ6WHNYPBI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdF3SRJU4KOXOBDCJRAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdQWDXKHRSO7OBDCJTAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdTZE2P7EKOXOBDCJRAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cmd65WESESVCFDAJOK2HI7OTV3WYE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdPYD77VZHWJG5FD7PCRQETZXWOE")},
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	35984,0,0);
}
/* Radix::Net::Unit.NetHub:Edit:Model - Desktop Executable*/

/*Radix::Net::Unit.NetHub:Edit:Model-Entity Model Class*/

package org.radixware.ads.Net.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:Edit:Model")
public class Edit:Model  extends org.radixware.ads.Net.explorer.Unit.NetHub.Unit.NetHub_DefaultModel.eprE7BZV3MWA7OBDCGDAALOMT5GDM_ModelAdapter {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Edit:Model_mi.rdxMeta; }



	public Edit:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::Net::Unit.NetHub:Edit:Model:Nested classes-Nested Classes*/

	/*Radix::Net::Unit.NetHub:Edit:Model:Properties-Properties*/

	/*Radix::Net::Unit.NetHub:Edit:Model:Methods-Methods*/

	/*Radix::Net::Unit.NetHub:Edit:Model:onCommand_EchoTest-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:Edit:Model:onCommand_EchoTest")
	protected  void onCommand_EchoTest (org.radixware.ads.Net.explorer.Unit.NetHub.EchoTest command) {
		try {
		    final NetHubCmdXsd:EchoTestRs output = command.send();
		    if (output.Result == System::InstanceControlServiceXsd:ActionStateEnum.DONE)
		        Explorer.Dialogs::SimpleDlg.messageInformation(Environment,"Echo test ok");
		    else
		        Explorer.Dialogs::SimpleDlg.messageError(Environment,"Echo test failed");     
		} catch(Exceptions::ServiceClientException e) {
		    showException(e);
		} catch(Exceptions::InterruptedException e) {
		    showException(e);
		}
	}

	/*Radix::Net::Unit.NetHub:Edit:Model:onCommand_Reconnect-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:Edit:Model:onCommand_Reconnect")
	protected  void onCommand_Reconnect (org.radixware.ads.Net.explorer.Unit.NetHub.Reconnect command) {
		try {
		    final NetHubCmdXsd:ReconnectRs output = command.send();
		    if (output.Result == System::InstanceControlServiceXsd:ActionStateEnum.DONE)
		        Explorer.Dialogs::SimpleDlg.messageInformation(Environment,"Reconnected");
		    else
		        Explorer.Dialogs::SimpleDlg.messageError(Environment,"Reconnection error");     
		} catch(Exceptions::ServiceClientException e) {
		    showException(e);
		} catch(Exceptions::InterruptedException e) {
		    showException(e);
		}
	}

	/*Radix::Net::Unit.NetHub:Edit:Model:afterRead-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:Edit:Model:afterRead")
	protected  void afterRead () {
		super.afterRead();
		getCommand(idof[Unit.NetHub:EchoTest]).setEnabled(started.Value == Bool.TRUE);
		getCommand(idof[Unit.NetHub:Reconnect]).setEnabled(started.Value == Bool.TRUE);
	}
	public final class Reconnect extends org.radixware.ads.Net.explorer.Unit.NetHub.Reconnect{
		protected Reconnect(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_Reconnect( this );
		}

	}

	public final class EchoTest extends org.radixware.ads.Net.explorer.Unit.NetHub.EchoTest{
		protected EchoTest(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_EchoTest( this );
		}

	}















}

/* Radix::Net::Unit.NetHub:Edit:Model - Desktop Meta*/

/*Radix::Net::Unit.NetHub:Edit:Model-Entity Model Class*/

package org.radixware.ads.Net.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Edit:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemKRWKXDGWONEAPFO67QVMISKWOA"),
						"Edit:Model",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aemE7BZV3MWA7OBDCGDAALOMT5GDM"),
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Net::Unit.NetHub:Edit:Model:Properties-Properties*/
						null,
						null,
						null,
						null,
						null,
						null,
						false,
						false,
						false
						);
}

/* Radix::Net::Unit.NetHub:Edit:Model - Web Executable*/

/*Radix::Net::Unit.NetHub:Edit:Model-Entity Model Class*/

package org.radixware.ads.Net.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:Edit:Model")
public class Edit:Model  extends org.radixware.ads.Net.web.Unit.NetHub.Unit.NetHub_DefaultModel.eprE7BZV3MWA7OBDCGDAALOMT5GDM_ModelAdapter {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Edit:Model_mi.rdxMeta; }



	public Edit:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::Net::Unit.NetHub:Edit:Model:Nested classes-Nested Classes*/

	/*Radix::Net::Unit.NetHub:Edit:Model:Properties-Properties*/

	/*Radix::Net::Unit.NetHub:Edit:Model:Methods-Methods*/

	/*Radix::Net::Unit.NetHub:Edit:Model:onCommand_EchoTest-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:Edit:Model:onCommand_EchoTest")
	protected  void onCommand_EchoTest (org.radixware.ads.Net.web.Unit.NetHub.EchoTest command) {
		try {
		    final NetHubCmdXsd:EchoTestRs output = command.send();
		    if (output.Result == System::InstanceControlServiceXsd:ActionStateEnum.DONE)
		        Explorer.Dialogs::SimpleDlg.messageInformation(Environment,"Echo test ok");
		    else
		        Explorer.Dialogs::SimpleDlg.messageError(Environment,"Echo test failed");     
		} catch(Exceptions::ServiceClientException e) {
		    showException(e);
		} catch(Exceptions::InterruptedException e) {
		    showException(e);
		}
	}

	/*Radix::Net::Unit.NetHub:Edit:Model:onCommand_Reconnect-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:Edit:Model:onCommand_Reconnect")
	protected  void onCommand_Reconnect (org.radixware.ads.Net.web.Unit.NetHub.Reconnect command) {
		try {
		    final NetHubCmdXsd:ReconnectRs output = command.send();
		    if (output.Result == System::InstanceControlServiceXsd:ActionStateEnum.DONE)
		        Explorer.Dialogs::SimpleDlg.messageInformation(Environment,"Reconnected");
		    else
		        Explorer.Dialogs::SimpleDlg.messageError(Environment,"Reconnection error");     
		} catch(Exceptions::ServiceClientException e) {
		    showException(e);
		} catch(Exceptions::InterruptedException e) {
		    showException(e);
		}
	}

	/*Radix::Net::Unit.NetHub:Edit:Model:afterRead-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::Unit.NetHub:Edit:Model:afterRead")
	protected  void afterRead () {
		super.afterRead();
		getCommand(idof[Unit.NetHub:EchoTest]).setEnabled(started.Value == Bool.TRUE);
		getCommand(idof[Unit.NetHub:Reconnect]).setEnabled(started.Value == Bool.TRUE);
	}
	public final class Reconnect extends org.radixware.ads.Net.web.Unit.NetHub.Reconnect{
		protected Reconnect(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_Reconnect( this );
		}

	}

	public final class EchoTest extends org.radixware.ads.Net.web.Unit.NetHub.EchoTest{
		protected EchoTest(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_EchoTest( this );
		}

	}















}

/* Radix::Net::Unit.NetHub:Edit:Model - Web Meta*/

/*Radix::Net::Unit.NetHub:Edit:Model-Entity Model Class*/

package org.radixware.ads.Net.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Edit:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemKRWKXDGWONEAPFO67QVMISKWOA"),
						"Edit:Model",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aemE7BZV3MWA7OBDCGDAALOMT5GDM"),
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Net::Unit.NetHub:Edit:Model:Properties-Properties*/
						null,
						null,
						null,
						null,
						null,
						null,
						false,
						false,
						false
						);
}

/* Radix::Net::Unit.NetHub - Localizing Bundle */
package org.radixware.ads.Net.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Unit.NetHub - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Response timeout for outbound request (s)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ожидание ответа на исходящий запрос (c)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls26YMXAT4GBEVRJ5LKMEYIL2F2A"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Number of active outbound sessions");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Текущее количество исходящих сессий");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3QDGSYW3IFDHHBFXU4ZS7U4MFU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"No data for Echo test");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Нет данных для Echo test");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4FFFYQZZVRHMVD624PLCTMY7XI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"External host rejected message (STAN=%2) of \"%1\"");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Внешняя система отвергла запрос (STAN=%2) \"%1\"");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4TKBOUN5WBCX3FECXM37LKB3PY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.ERROR,"App.ServiceBus",java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Address");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Адрес");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5X45BFYOLBCWHFECAWGZJXKHWQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Reconnection error");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка при переустановлении соединения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7HT6GQCPYVBL3PUEEC325ZZH5U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Echo test failed");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Echo test не прошел");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7YQEVTNA7VE65KYL6CFTISPOMM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"External system disconnected");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Соединение с внешней системой разорвано");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAKLMU6TAHBFELE347TYELA3EB4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.EVENT,"App.ServiceBus",java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"External port: receive timeout exceeded (STAN=%1)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Внешний порт: превышен тайм-аут на чтение (STAN=%1)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBZWLEFFJKBA5LFPYM6NKY7OXYY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.ERROR,"App.ServiceBus",java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Echo test ok");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Echo test прошел успешно");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCLDS3JPKCZFTZAKNBRQ42HSKYM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Echo test complete");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Завершилась операция Echo test");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCZTRRIO3MBF7XIW3ZI7ZUPPQXY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"I/O error in socket \"%1\": %2");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка ввода-вывода сокета \"%1\": %2");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsE6EYWA5GTREFZA4E3WWJSVRVOE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.ERROR,"App.ServiceBus",null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Maximum inbound requests");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Максимум входящих запросов");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEX2IG6OYHFAIRE2MLEXMMHXSNM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Process stop");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Обрабатывать остановку");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGB5HJE2LNBBBFE63QUMU4EEPEY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Reconnected");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Соединение переустановлено");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGNIXTXZ4DBHXHKVVWMWMAVUV4E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Last outbound STAN");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Последний исходящий STAN");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHR4LKGN7JFAABEWT7YNW5I3GJU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Process connection");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Обрабатывать соединение");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHXJ6OWRENFE6HKZ5K34TB2RSN4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Process startup");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Обрабатывать запуск");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsI5B42OEQVVCWRFTBMMGUZH5ZTA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Number of echo test attempts");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Количество попыток Echo test");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsITXLMFXHZRC45HMHSU7PQZ6S3U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Settings");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Настройки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsK4HS4TNRRFHH5B5IYETZYE46QQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Echo Test");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Эхо тест");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKIWGXKEVH5CTLJG62QDPPKFQWI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Error reading the parameters of the service \"%1\": %2");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка при чтении параметров сервиса \"%1\": %2");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLX6NEI4I6BDXHLE7WHWNLQ752Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.ERROR,"App.ServiceBus",null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Late response detected in \"%1\"");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"В \"%1\" обнаружен \"опоздавший\" ответ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLYXXGYB4EZHUHI5MDIPW7A2FWY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.ERROR,"App.ServiceBus",java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Reconnect");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Переустановить соединение");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMGRFXXEJ5RF35K3ZQCMBTKI5TI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Trace Settings");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Настройки трассы");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNCWR775PCZAUFER2UWMUFW5GOU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Repeat Echo Test: ");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Операция Echo test: ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNPKVEUN4CFG4DLDOIRKTG6ADMQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Duplicate request (STAN=%2) received from external port of \"%1\"");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"В \"%1\" запрос внешней системы (STAN=%2)  получен дважды");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOLQPB4C5WZGT5DYPDN4K33PC4E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.ERROR,"App.ServiceBus",java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Limit of requests to external system (%1) exceeded in \"%2\"");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"В \"%2\" превышен лимит количества запросов к внешней системе(%1)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOO7AELILWNELLAF2DNSACWVMEQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.ERROR,"App.ServiceBus",java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Reconnect with host");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Переустановка соединения с хостом");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOPV646YJ2FFRJKATLUNXCET5SA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Error starting the socket %1: %2");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка при запуске сокета %1: %2");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQKCN47IA4FHPTEHAWZF6KPR7WQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.ERROR,"App.ServiceBus",null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Last inbound unique key");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Последний входящий уникальный ключ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRM5VSCQ6PFELHNOTFMA6DNACWM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Limit of requests from external system (%1) exceeded in \"%2\"");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"В \"%2\" превышен лимит количества запросов от внешней системы(%1)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsS2A4H4U425AQRBIKU67LNQYQOM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.ERROR,"App.ServiceBus",java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Network Hub");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сетевой коммутатор");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsU75MXOL3GBDWBHUUNP6WZTE4OA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Connected");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Соединен");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsULO6JJ4FURENBG5TEYNHBDAQHY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Error processing the external system request occurred in \"%1\": %2");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"В \"%1\" возникла ошибка при обработке запроса от внешней системы: %2");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsV4NXBYIMNJFOJC4OXBEJQOMDDQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.ERROR,"App.ServiceBus",null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Service Server: timeout of response from external system exceeded (STAN=%1)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сервер сервиса: тайм-аут приема от внешней системы  (STAN=%1)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVE2URHXMUFBQLDVKSVE5DFNBEA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.ERROR,"App.ServiceBus",java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Server port");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Серверный порт");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWGGO36T7VNCTVMCN2W2HVKLDWQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"SAP ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"SAP ID");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWHMWLM4WWBEA3LHZH4YKVIGMZM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Maximum outgoing requests");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Максимум исходящих запросов");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWQT3UWEBYZAA7LUVDGU7GTZRAQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Process uncorrelated responses");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Обрабатывать нескоррелированные ответы");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXBCYMSYR4RE4BAFBTTLZ6QYLOI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Echo test period (s)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Период посылки Echo test (с)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXU3GEUJM7FAHHF2EZAWRNFJHTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Process duplicated requests");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Обрабатывать дублированные запросы");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXYUMEXEHF5BW5DH73NGAPPEXAE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Frame");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Формат пакета");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYSW4ETJVDVAXLE7AD3GBNLWBUQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"General");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Общее");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZ2VQT7OPZVCXNEFQCSABY7ABBY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Process disconnection");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Обрабатывать разъединение");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZG2HCVGKOZAXXFVL34X3YS6NJE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Connected to external system: %1");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Установлено соединение с внешней системой: %1");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZNX3KPRR4FBRVANPNYT4SGSXTY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.EVENT,"App.ServiceBus",java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(Unit.NetHub - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaclQPCA3XD2BZAZ3ADC4BQZU2MDLM"),"Unit.NetHub - Localizing Bundle",$$$items$$$);
}
