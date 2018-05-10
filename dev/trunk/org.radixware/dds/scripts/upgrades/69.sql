BEGIN
  BEGIN
    EXECUTE immediate 'alter table RDX_JS_CALENDAR add RID VARCHAR2(100 char) null';
  EXCEPTION
  WHEN OTHERS THEN
    IF SQLCODE = -01430 THEN
      NULL; -- column being added already exists in table
    ELSE
      RAISE;
    END IF;
  END;
  
  BEGIN
    EXECUTE immediate 'create index IDX_RDX_JS_CALENDAR_RID on RDX_JS_CALENDAR (RID asc)';
  EXCEPTION
  WHEN OTHERS THEN
    IF SQLCODE = -00955 THEN
      NULL; -- name is already used by an existing object
    ELSE
      RAISE;
    END IF;
  END;
  
  BEGIN
    EXECUTE immediate 'alter table RDX_JS_CALENDAR add constraint UNQ_RDX_JS_CALENDAR_RID unique (RID) rely';
  EXCEPTION
  WHEN OTHERS THEN
    IF SQLCODE = -02261 THEN
      NULL; -- such unique or primary key already exists in the table
    ELSE
      RAISE;
    END IF;
  END;
END;
/