--Migrate user property changelog from Report to current report version
DECLARE
TYPE ChangelogSet
IS
  TABLE OF RDX_CM_CHANGELOG%ROWTYPE;
  newChangelogs ChangelogSet;
TYPE RevisionSet
IS
  TABLE OF RDX_CM_REVISION%ROWTYPE;
  newRevisions RevisionSet;
  errors     NUMBER;
  dml_errors EXCEPTION;
  PRAGMA EXCEPTION_INIT(dml_errors,          -24381);
  UNIQUE_CONSTR_VIOLATION CONSTANT NUMBER       := -1;
  OLD_DEF_ID              CONSTANT VARCHAR2(29) := 'pruDSLY3APDHJBBHCOGYEMUDASI5Q';
  NEW_DEF_ID              CONSTANT VARCHAR2(29) := 'pruG7SSVR4UVFGQ5LEPLRP322MBKQ';
  OLD_TABLE_ID            CONSTANT VARCHAR2(29) := 'tblRHH7SYO5I5EFRGYIBOSVUXKD7U';
  NEW_TABLE_ID            CONSTANT VARCHAR2(29) := 'tblM2NL42YXRRA5ZH27LCKIW5CQNI';
BEGIN
  -- 1. Create new changelogs collection (changelog owner - current version)
  SELECT * BULK COLLECT
  INTO newChangelogs
  FROM
    (SELECT NEW_DEF_ID UPDEFID,
      NEW_TABLE_ID UPOWNERENTITYID,
      (UPOWNERPID
      || '~'
      ||
      (SELECT RDX_USERREPORT.CURVERSION
      FROM RDX_USERREPORT
      WHERE RDX_USERREPORT.GUID = UPOWNERPID
      )) UPOWNERPID,
      COMMENTS,
      LOCALNOTES
    FROM RDX_CM_CHANGELOG
    WHERE UPDEFID      = OLD_DEF_ID
    AND UPOWNERENTITYID=OLD_TABLE_ID
    ) S1
  WHERE NOT EXISTS
    (SELECT UPOWNERPID
    FROM RDX_CM_CHANGELOG
    WHERE UPDEFID      = NEW_DEF_ID
    AND UPOWNERENTITYID=NEW_TABLE_ID
    AND UPOWNERPID     = S1.UPOWNERPID
    );
  -- 2. Create new revisions collection
  SELECT * BULK COLLECT
  INTO newRevisions
  FROM
    (SELECT NEW_DEF_ID UPDEFID,
      NEW_TABLE_ID UPOWNERENTITYID,
      (RDX_CM_REVISION.UPOWNERPID
      || '~'
      ||
      (SELECT RDX_USERREPORT.CURVERSION
      FROM RDX_USERREPORT
      WHERE RDX_USERREPORT.GUID = RDX_CM_REVISION.UPOWNERPID
      )) UPOWNERPID,
      RDX_CM_REVISION.SEQ,
      RDX_CM_REVISION.TIME,
      RDX_CM_REVISION.DESCRIPTION,
      RDX_CM_REVISION.AUTHOR,
      RDX_CM_REVISION.DOCREF,
      RDX_CM_REVISION.APPVER,
      RDX_CM_REVISION.LOCALNOTES
    FROM RDX_CM_REVISION
    WHERE UPDEFID      =OLD_DEF_ID
    AND UPOWNERENTITYID=OLD_TABLE_ID
    ) S1
  WHERE NOT EXISTS
    (SELECT UPOWNERPID
    FROM RDX_CM_REVISION
    WHERE UPDEFID      = NEW_DEF_ID
    AND UPOWNERENTITYID=NEW_TABLE_ID
    AND UPOWNERPID     = S1.UPOWNERPID
    );
  -- 3. Delete changelogs from reports
  DELETE
  FROM RDX_CM_CHANGELOG
  WHERE UPDEFID      =OLD_DEF_ID
  AND UPOWNERENTITYID=OLD_TABLE_ID;
  --4. Update UpValRef table changelog references
  BEGIN
    FORALL i IN newChangelogs.FIRST .. newChangelogs.LAST SAVE EXCEPTIONS
    UPDATE RDX_UPVALREF
    SET DEFID      =NEW_DEF_ID,
      OWNERENTITYID=NEW_TABLE_ID,
      OWNERPID     =newChangelogs(i).UPOWNERPID,
      VAL          =NEW_DEF_ID
      ||'~'
      ||NEW_TABLE_ID
      ||'~'
      || SUBSTR(newChangelogs(i).UPOWNERPID, 0, 29)
      || '\'
      || SUBSTR(newChangelogs(i).UPOWNERPID, 30)
    WHERE DEFID =OLD_DEF_ID
    AND OWNERPID=SUBSTR(newChangelogs(i).UPOWNERPID, 0, 29);
  EXCEPTION
  WHEN dml_errors THEN
    errors := SQL%BULK_EXCEPTIONS.COUNT;
    DBMS_OUTPUT.PUT_LINE ('Number of statements that failed: ' || errors);
    FOR i IN 1..errors
    LOOP
      IF  -SQL%BULK_EXCEPTIONS(i).ERROR_CODE = UNIQUE_CONSTR_VIOLATION THEN
        DBMS_OUTPUT.PUT_LINE('Error #' || i || ' ' || SQLERRM(-SQL%BULK_EXCEPTIONS(i).ERROR_CODE));
      ELSE
        RAISE;
      END IF;
    END LOOP;
  END;
  -- 5. Delete report changelogs from upValRef
  DELETE
  FROM RDX_UPVALREF
  WHERE DEFID      =OLD_DEF_ID
  AND OWNERENTITYID=OLD_TABLE_ID;
  -- 6. Insert new changelogs
  BEGIN
    FORALL i IN newChangelogs.FIRST .. newChangelogs.LAST SAVE EXCEPTIONS
    INSERT INTO RDX_CM_CHANGELOG VALUES newChangelogs(i);
  EXCEPTION
  WHEN dml_errors THEN
    errors := SQL%BULK_EXCEPTIONS.COUNT;
    DBMS_OUTPUT.PUT_LINE ('Number of statements that failed: ' || errors);
    FOR i IN 1..errors
    LOOP
      IF -SQL%BULK_EXCEPTIONS(i).ERROR_CODE = UNIQUE_CONSTR_VIOLATION THEN
        DBMS_OUTPUT.PUT_LINE('Error #' || i || ' ' || SQLERRM(-SQL%BULK_EXCEPTIONS(i).ERROR_CODE));
      ELSE
        RAISE;
      END IF;
    END LOOP;
  END;
  -- 7. Insert new revisions
  BEGIN
    FORALL i IN newRevisions.FIRST .. newRevisions.LAST SAVE EXCEPTIONS
    INSERT INTO RDX_CM_REVISION VALUES newRevisions(i);
  EXCEPTION
  WHEN dml_errors THEN
    errors := SQL%BULK_EXCEPTIONS.COUNT;
    DBMS_OUTPUT.PUT_LINE ('Number of statements that failed: ' || errors);
    FOR i IN 1..errors
    LOOP
      IF -SQL%BULK_EXCEPTIONS(i).ERROR_CODE = UNIQUE_CONSTR_VIOLATION THEN
        DBMS_OUTPUT.PUT_LINE('Error #' || i || ' ' || SQLERRM(-SQL%BULK_EXCEPTIONS(i).ERROR_CODE));
      ELSE
        RAISE;
      END IF;
    END LOOP;
  END;
  COMMIT;
END;
/