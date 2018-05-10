create or replace package body RDX_Arte as

	procedure setSessionIsActive(
		pSessionId in integer,
		pIsActive in integer
	)
	is
	    pragma autonomous_transaction;
	begin
	    update RDX_EASSESSION
	       set ISACTIVE=pIsActive, LASTCONNECTTIME=SYSDATE  
	       where ID = pSessionId;
	    commit;
	end;

	procedure registerSession(
		pUserName in varchar2,
		pStationName in varchar2,
		pClientLanguage in varchar2,
		pClientCountry in varchar2,
		pSessionId in integer,
		pDefVersion in integer,
		pSapId in integer,
		pClientAddress in varchar2,
		pUnitId in integer
	)
	is
	begin
	   RDX_Arte_Vars.setUserName(pUserName);
	   RDX_Arte_Vars.setStationName(pStationName);
	   RDX_Arte_Vars.setClientLanguage(pClientLanguage);
	   RDX_Arte_Vars.setClientCountry(pClientCountry);
	   RDX_Arte_Vars.setSessionId(pSessionId);
	   RDX_Arte_Vars.setDefVersion(pDefVersion);
	   --dbms_application_info.set_client_info('ARTE #'||pArteInstSeq||': SAP-'||pSapId||' CLIENT-'||pClientAddress||' DB-'||dbms_debug_jdwp.current_session_id||'/'||dbms_debug_jdwp.current_session_serial);
	   RDX_Environment.setRequestInfo(requestInfo => 'U#'|| pUnitId || '-S#' || pSapId || '-' || pClientAddress);
	   if pSessionId is not null then
	        RDX_Arte.setSessionIsActive(pSessionId => pSessionId, pIsActive => 1);
	   end if;
	end;

	procedure registerSession(
		pUserName in varchar2,
		pStationName in varchar2,
		pClientLanguage in varchar2,
		pClientCountry in varchar2,
		pSessionId in integer,
		pDefVersion in integer,
		pSapId in integer,
		pClientAddress in varchar2,
		pUnitId in integer,
		pJobId in integer,
		pTaskId in integer
	)
	is
	begin
	   RDX_Arte_Vars.setUserName(pUserName);
	   RDX_Arte_Vars.setStationName(pStationName);
	   RDX_Arte_Vars.setClientLanguage(pClientLanguage);
	   RDX_Arte_Vars.setClientCountry(pClientCountry);
	   RDX_Arte_Vars.setSessionId(pSessionId);
	   RDX_Arte_Vars.setDefVersion(pDefVersion);
	   --dbms_application_info.set_client_info('ARTE #'||pArteInstSeq||': SAP-'||pSapId||' CLIENT-'||pClientAddress||' DB-'||dbms_debug_jdwp.current_session_id||'/'||dbms_debug_jdwp.current_session_serial);
	   RDX_Environment.setRequestInfo( 'U#'|| pUnitId ||  '/J#' || nvl(to_char(pJobId), '-') || '/T#' || nvl(to_char(pTaskId), '-') || '/UN#' || nvl(to_char(pUserName), '-') );
	   if pSessionId is not null then
	        RDX_Arte.setSessionIsActive(pSessionId => pSessionId, pIsActive => 1);
	   end if;
	end;

	function getUserName return varchar2
	is
	begin
	  return RDX_Arte_Vars.getUserName;
	end;

	function getVersion return integer
	is
	begin
	    if RDX_Arte_Vars.getDefVersion is null then
	       raise_application_error(-20001,'Definition version is not deployed to database. '||chr(10)||'onStartRequestExecution() method of Arte (java class) should have been called  '||chr(10)||'before the database request executing.');
	    end if;
	  return RDX_Arte_Vars.getDefVersion;
	end;

	function getStationName return varchar2
	is
	begin
	  return RDX_Arte_Vars.getStationName;
	end;

	function getClientLanguage return varchar2
	is
	begin
	  return RDX_Arte_Vars.getClientLanguage;
	end;

	function getSessionId return integer
	is
	begin
	  return RDX_Arte_Vars.getSessionId;
	end;

	procedure unregisterSession(
		pEasSessionId in integer
	)
	is
	begin
	    if pEasSessionId is not null then
	        RDX_Arte.setSessionIsActive(pSessionId => pEasSessionId, pIsActive => 0);
	    end if;
	    RDX_Environment.setRequestInfo(null);
	end;
end;
/

