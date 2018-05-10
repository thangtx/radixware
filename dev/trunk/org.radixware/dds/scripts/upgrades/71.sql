BEGIN
  BEGIN
    EXECUTE immediate 'alter table RDX_TESTCASE add AUTHORNAME VARCHAR2(250 char) null';
  EXCEPTION
  WHEN OTHERS THEN
    IF SQLCODE = -01430 THEN
      NULL; -- column being added already exists in table
    ELSE
      RAISE;
    END IF;
  END;
  BEGIN
    EXECUTE immediate 'alter table RDX_TESTCASE add LASTISEXECDATE DATE null';
  EXCEPTION
  WHEN OTHERS THEN
    IF SQLCODE = -01430 THEN
      NULL; -- column being added already exists in table
    ELSE
      RAISE;
    END IF;
  END;
  BEGIN
    EXECUTE immediate 'alter table RDX_TESTCASE add LASTISSUCCESSDATE DATE null';
  EXCEPTION
  WHEN OTHERS THEN
    IF SQLCODE = -01430 THEN
      NULL; -- column being added already exists in table
    ELSE
      RAISE;
    END IF;
  END;
  BEGIN
    EXECUTE immediate 'alter table RDX_TESTCASE add NOTIFICATIONEMAIL VARCHAR2(100 char) null';
  EXCEPTION
  WHEN OTHERS THEN
    IF SQLCODE = -01430 THEN
      NULL; -- column being added already exists in table
    ELSE
      RAISE;
    END IF;
  END;
  BEGIN
    EXECUTE immediate 'alter table RDX_TESTCASE add SEQISFAILCOUNT NUMBER(9,0) default 0 null';
  EXCEPTION
  WHEN OTHERS THEN
    IF SQLCODE = -01430 THEN
      NULL; -- column being added already exists in table
    ELSE
      RAISE;
    END IF;
  END;
  BEGIN
    EXECUTE immediate 'alter table RDX_TESTCASE add constraint FK_RDX_TESTCASE_USER foreign key (AUTHORNAME) references RDX_AC_USER (NAME) on delete cascade';
  EXCEPTION
  WHEN OTHERS THEN
    IF SQLCODE = -02275 THEN
      NULL; -- such a referential constraint already exists in the table
    ELSE
      RAISE;
    END IF;
  END;
END;
/