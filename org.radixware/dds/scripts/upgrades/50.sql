create sequence SQN_RDX_CM_REVISIONID
	order
/

grant select on SQN_RDX_CM_REVISIONID to &USER&_RUN_ROLE
/

alter table RDX_CM_REVISION
	add ID NUMBER(9,0) null
/

DECLARE
  CURSOR myCursor
  IS
    SELECT ID FROM RDX_CM_REVISION ORDER BY UPOWNERPID, SEQ;
BEGIN
  FOR rec IN myCursor
  LOOP
    UPDATE RDX_CM_REVISION
    SET ID = SQN_RDX_CM_REVISIONID.nextval;
  END LOOP;
END;
/

alter table RDX_CM_REVISION drop constraint PK_RDX_CM_REVISION cascade
/

drop index PK_RDX_CM_REVISION
/

alter table RDX_CM_REVISION
	modify (ID not null)
/

alter table RDX_CM_REVISION
	modify (SEQ null)
/

create unique index PK_RDX_CM_REVISION on RDX_CM_REVISION (UPDEFID asc, UPOWNERENTITYID asc, UPOWNERPID asc, ID asc)
/

alter table RDX_CM_REVISION add constraint PK_RDX_CM_REVISION primary key (UPDEFID, UPOWNERENTITYID, UPOWNERPID, ID) rely
/

COMMIT;