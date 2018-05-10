create or replace package body RDX_Aadc as

	SUSPENSION_TAG constant RAW(10) := HEXTORAW('AADC0000');
	inSeqNormalization BOOLEAN;

	function suspendReplication return RAW
	is
	    prevTag RAW(10);
	begin
	    prevTag := DBMS_STREAMS.GET_TAG();
	    DBMS_STREAMS.SET_TAG(SUSPENSION_TAG);
	    return prevTag;
	end;

	procedure execNormalizeSequenceJob(
		name in varchar2
	)
	is
	  memberId integer;
	begin    
	    select AADCMEMBERID into memberId from RDX_SYSTEM where ID = 1;
	    RDX_Aadc.normalizeSequence(name, memberId);
	end;

	procedure setupSecondMember
	is
	    prevTag RAW(10);
	begin
	    prevTag := RDX_Aadc.suspendReplication;
	    update RDX_SYSTEM set AADCMEMBERID = 2 where ID = 1;
	    RDX_Aadc.normalizeAllSequences;
	    RDX_Aadc.restoreReplication(prevTag);
	end;

	procedure restoreReplication(
		prevState in RAW
	)
	is
	begin
	    DBMS_STREAMS.SET_TAG(prevState);
	end;

	procedure normalizeSequence(
		name in varchar2,
		memberId in integer
	)
	is
	    val integer;
	    minval integer; 
	    inc integer;
	begin
	    inSeqNormalization := true;
	    select min_value, increment_by into minval, inc from sys.user_sequences where sequence_name = name;
	    if minval = memberId and inc = 2 then
	        return;
	    end if;    
	    execute immediate 'alter sequence ' || name || ' MINVALUE ' || memberId;
	    loop 
	        execute immediate 'alter sequence ' || name || ' INCREMENT BY 2';
	        execute immediate 'select ' || name || '.nextVal from dual' into val;
	        exit when ((val - 1) mod 2 = memberId - 1);
	        execute immediate 'alter sequence ' || name || ' INCREMENT BY 1';
	        execute immediate 'select ' || name || '.nextVal from dual' into val;
	    end loop;  
	    inSeqNormalization := false; 
	end;

	function runNormalizeSequenceJob(
		owner in varchar2,
		name in varchar2
	) return integer
	is
	   PRAGMA AUTONOMOUS_TRANSACTION;
	   jobId integer;
	begin
	 --   dbms_job.submit(jobId, 
	 --       'begin ' || owner || '.RDX_Aadc.execNormalizeSequenceJob(''' || name || '''); end;'
	 --       );
	 --   commit;    
	    return jobId;    
	end;

	procedure removeJob(
		id in integer
	)
	is
	   PRAGMA AUTONOMOUS_TRANSACTION;
	begin
	   -- dbms_job.remove(id);
	    commit;    
	end;

	procedure postNormalizeSequence(
		owner in varchar2,
		name in varchar2
	)
	is
	    jobId number;
	    jobCnt integer;
	    failCnt integer;
	    duration integer := 0;
	begin
	    if inSeqNormalization then
	        return;
	    end if;    
	    jobId := RDX_Aadc.runNormalizeSequenceJob(owner, name);
	    /* 
	    !!! в этот момент sequence еще не создан и заблокирован !!!
	    loop    
	        SELECT count(*), max(FAILURES) into jobCnt, failCnt FROM USER_JOBS where job = jobId;    
	        exit when jobCnt < 1;
	        if failCnt > 0 then
	            RDX_Aadc.removeJob(jobId);    
	            raise_application_error(-20000, 
	                'Sequence actualization for AADC failed');
	        end if;
	        DBMS_LOCK.SLEEP(1);
	        duration := duration + 1;
	        if duration > 200 then
	            RDX_Aadc.removeJob(jobId);    
	            raise_application_error(-20000, 
	                'Sequence actualization for AADC timed out');
	        end if;
	    end loop; 
	    */   
	end;

	procedure normalizeAllSequences
	is
	  memberId integer;
	begin
	  select AADCMEMBERID into memberId from RDX_SYSTEM where ID = 1;
	  for rec in (select sequence_name from user_sequences) loop
	    RDX_Aadc.normalizeSequence(rec.sequence_name, memberId);
	  end loop;
	end;

	procedure setupFirstMember
	is
	    prevTag RAW(10);
	begin
	    prevTag := RDX_Aadc.suspendReplication;
	    update RDX_SYSTEM set AADCMEMBERID = 1 where ID = 1;
	    update RDX_INSTANCE set AADCMEMBERID = 1;
	    update RDX_JS_TASK set AADCMEMBERID = 1;
	    update RDX_JS_JOBQUEUE set AADCMEMBERID = 1;
	    RDX_Aadc.normalizeAllSequences;
	    RDX_Aadc.restoreReplication(prevTag);
	end;
end;
/

