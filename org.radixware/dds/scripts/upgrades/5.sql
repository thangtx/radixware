declare
  calendarId  number(9,0);
  scheduleId  number(9,0);
  unitId  number(9,0);
  taskPresent  number(1,0);
begin
  begin 
    select 1 into taskPresent from dual where exists ( 
      select id from rdx_js_task where classguid='aclQ2LMF6ZNW5HOVEPVPVFG666UKE');
  exception    
    when NO_DATA_FOUND  then
       taskPresent := null;     
  end;
  if taskPresent is null then       
    DBMS_OUTPUT.PUT_LINE('No license report task found. Will create one'); 
    -- look for suitable JOB Scheduler unit
    select x.id into unitId from (select id 
                                    from rdx_unit
                                   where type=1001 
                                     and classguid='acl7QS5XAHQJPOBDCIUAALOMT5GDM' 
                                order by use desc, 
                                        started desc) x 
    where rownum=1;
          
    if unitId is not null then	
        DBMS_OUTPUT.PUT_LINE('Will use job scheduler unit #'||unitId); 
        -- get value for calendar and schedule PKs		
        select SQN_RDX_JS_CALENDARID.NEXTVAL into calendarId from dual;
        select SQN_RDX_JS_EVENTSCHDID.NEXTVAL into scheduleId from dual;
        -- create calendar for license report task
        insert into RDX_JS_CALENDAR (ID,GUID,TITLE,CLASSGUID,LASTUPDATETIME) 
        values (calendarId,rawtohex(sys_guid()),'License Report Calendar','aclVV67ZO4QF5GJ7KY7J2AW6UMQUI',sysdate);
        -- create execution point: last day of every month
        insert into RDX_JS_CALENDARITEM (ID,CALENDARID,SEQ,OPER,OFFSET,OFFSETDIR,CLASSGUID)
        values (SQN_RDX_JS_CALENDARITEMID.NEXTVAL,calendarId,1,'+',1,-1,'aclPJITOKTE6ZDY5OXI7Z53SB222I');
        -- create event schedule with our calendar
        insert into RDX_JS_EVENTSCHD (ID,GUID,TITLE,LASTUPDATETIME) 
        values (scheduleId,rawtohex(sys_guid()),'License Report',sysdate);
        -- our schedule will contain only one item
        insert into RDX_JS_EVENTSCHDITEM (ID,SCHEDULEID,CALENDARID)
        values (SQN_RDX_JS_EVENTSCHDITEMID.NEXTVAL,scheduleId,calendarId);
        -- create license report export task	
        insert into RDX_JS_TASK (ID,TITLE,CLASSGUID,SCHEDULEID,UNITID)
            values(SQN_RDX_JS_SCHDUNITJOBID.NEXTVAL,'License Report','aclQ2LMF6ZNW5HOVEPVPVFG666UKE',scheduleId,unitId);
        DBMS_OUTPUT.PUT_LINE('License report task successfully created '); 
    else
       DBMS_OUTPUT.PUT_LINE('No used job scheduler unit found. License report task was not created '); 
    end if;
  else 
    DBMS_OUTPUT.PUT_LINE('License report task found. Will not create new one'); 
  end if;
end;
/