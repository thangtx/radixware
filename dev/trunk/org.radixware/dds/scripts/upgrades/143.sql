create or replace package body RDX_Arte as

		userName varchar2(250);
		stationName varchar2(250);
		clientLanguage varchar2(3);
	        clientCountry varchar2(3);
		defVersion number;
		sessionId number;

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
	   userName:=pUserName;
	   stationName:=pStationName;
	   clientLanguage:=pClientLanguage;
	   clientCountry:=pClientCountry;
	   sessionId:=pSessionId;
	   defVersion:=pDefVersion;
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
	   userName:=pUserName;
	   stationName:=pStationName;
	   clientLanguage:=pClientLanguage;
	   clientCountry:=pClientCountry;
	   sessionId:=pSessionId;
	   defVersion:=pDefVersion;
	   --dbms_application_info.set_client_info('ARTE #'||pArteInstSeq||': SAP-'||pSapId||' CLIENT-'||pClientAddress||' DB-'||dbms_debug_jdwp.current_session_id||'/'||dbms_debug_jdwp.current_session_serial);
	   RDX_Environment.setRequestInfo( 'U#'|| pUnitId ||  '/J#' || nvl(to_char(pJobId), '-') || '/T#' || nvl(to_char(pTaskId), '-') );
	   if pSessionId is not null then
	        RDX_Arte.setSessionIsActive(pSessionId => pSessionId, pIsActive => 1);
	   end if;
	end;

	function getUserName return varchar2
	is
	begin
	  return userName;
	end;

	function getVersion return integer
	is
	begin
	    if defVersion is null then
	       raise_application_error(-20001,'Definition version is not deployed to database. '||chr(10)||'onStartRequestExecution() method of Arte (java class) should have been called  '||chr(10)||'before the database request executing.');
	    end if;
	  return defVersion;
	end;

	function getStationName return varchar2
	is
	begin
	  return stationName;
	end;

	function getClientLanguage return varchar2
	is
	begin
	  return clientLanguage;
	end;

	function getSessionId return integer
	is
	begin
	  return sessionId;
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

