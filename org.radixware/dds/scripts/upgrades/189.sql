create or replace package RDX_Aadc as

	procedure setupFirstMember;

	procedure setupSecondMember;

	procedure afterSequenceDdl(
		name in varchar2
	);

	procedure normalizeSequence(
		name in varchar2,
		-- null - read from database
		aadcMemberId in integer := null
	);

	procedure normalizeAllSequences;

	procedure restoreReplication(
		prevState in RAW
	);

	function suspendReplication return RAW;

	procedure setupTriggers;
end;
/

grant execute on RDX_Aadc to &USER&_RUN_ROLE
/

create or replace package RDX_Sys_Vars as

	userName varchar2(250);
	stationName varchar2(250);
	clientLanguage varchar2(3);
	clientCountry varchar2(3);
	defVersion number;
	sessionId number;

	--filled after creating jdbc connection
	instanceId integer;
	sessionOwnerType varchar2(100);
	sessionOwnerId integer;
	--other
	targetExecutorId integer := -1;
	requestInfo varchar2(500);
	PRAGMA RESTRICT_REFERENCES (DEFAULT, WNDS);
end;
/

grant execute on RDX_Sys_Vars to &USER&_RUN_ROLE
/

create or replace package body RDX_Aadc as

	SUSPENSION_TAG constant RAW(10) := HEXTORAW('AADC0000');

	procedure setupFirstMember
	is
	    prevTag RAW(10);
	begin
	    prevTag := RDX_Aadc.suspendReplication;
	    update RDX_SYSTEM set AADCMEMBERID = 1 where ID = 1;
	    RDX_Aadc.normalizeAllSequences;
	    RDX_Aadc.restoreReplication(prevTag);
	    RDX_Aadc.setupTriggers;
	    update RDX_INSTANCE set AADCMEMBERID = 1;
	    update RDX_JS_JOBQUEUE set AADCMEMBERID = 1;
	end;

	procedure setupSecondMember
	is
	    prevTag RAW(10);
	begin
	    prevTag := RDX_Aadc.suspendReplication;
	    update RDX_SYSTEM set AADCMEMBERID = 2 where ID = 1;
	    RDX_Aadc.normalizeAllSequences;
	    RDX_Aadc.restoreReplication(prevTag);
	    RDX_Aadc.setupTriggers;
	end;

	procedure afterSequenceDdl(
		name in varchar2
	)
	is
	begin
	    insert into RDX_AADC_SEQUENCEDDL(NAME, LASTTIME) values (name, systimestamp);
	end;

	procedure normalizeSequence(
		name in varchar2,
		-- null - read from database
		aadcMemberId in integer := null
	)
	is
	    memberId integer;
	    val integer;
	    minval integer; 
	    inc integer;
	    min_cur_ex EXCEPTION;
	    PRAGMA EXCEPTION_INIT(min_cur_ex, -04007);
	begin
	    memberId := aadcMemberId;
	    if memberId is null then
	        select AADCMEMBERID into memberId from RDX_SYSTEM where ID = 1;
	    end if;    
	    if memberId is null then
	        return;
	    end if;    
	    select min_value, increment_by into minval, inc from sys.user_sequences where sequence_name = upper(name);
	    if minval = memberId and inc = 2 then
	        return;
	    end if;
	    loop   
	        begin 
	            execute immediate 'alter sequence ' || name || ' MINVALUE ' || memberId;
	            exit;
	        exception 
	            when min_cur_ex then   
	                execute immediate 'select ' || name || '.nextVal from dual' into val;
	        end;        
	    end loop;    
	    loop 
	        execute immediate 'alter sequence ' || name || ' INCREMENT BY 2';
	        execute immediate 'select ' || name || '.nextVal from dual' into val;
	        exit when ((val - 1) mod 2 = memberId - 1);
	        execute immediate 'alter sequence ' || name || ' INCREMENT BY 1';
	        execute immediate 'select ' || name || '.nextVal from dual' into val;
	    end loop;  
	end;

	procedure normalizeAllSequences
	is
	    memberId integer;
	begin
	    select AADCMEMBERID into memberId from RDX_SYSTEM where ID = 1;
	    if memberId is null then
	        return;
	    end if;    
	    for rec in (select sequence_name from user_sequences) loop
	        RDX_Aadc.normalizeSequence(rec.sequence_name, memberId);
	    end loop;
	end;

	function suspendReplication return RAW
	is
	    prevTag RAW(10);
	begin
	    prevTag := DBMS_STREAMS.GET_TAG();
	    DBMS_STREAMS.SET_TAG(SUSPENSION_TAG);
	    return prevTag;
	end;

	procedure restoreReplication(
		prevState in RAW
	)
	is
	begin
	    DBMS_STREAMS.SET_TAG(prevState);
	end;

	procedure setupTriggers
	is
	begin
	    DBMS_DDL.SET_TRIGGER_FIRING_PROPERTY(
	        trig_owner => sys_context( 'userenv', 'current_schema' ), 
	        trig_name  => 'TAIR_RDX_AADC_OGGERROR',
	        fire_once  => false);
	    DBMS_DDL.SET_TRIGGER_FIRING_PROPERTY(
	        trig_owner => sys_context( 'userenv', 'current_schema' ), 
	        trig_name  => 'TBIR_RDX_AADC_OGGERROR',
	        fire_once  => false);
	end;
end;
/

