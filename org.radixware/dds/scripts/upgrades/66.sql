alter table RDX_TESTCASE
	add SEQ NUMBER(9,0) default 0 not null
/

UPDATE RDX_TESTCASE T1
SET seq=
  (SELECT COUNT(*)
  FROM rdx_testcase tmp
  WHERE tmp.groupId = T1.groupId
  AND tmp.id       <= T1.id
  )
WHERE T1.GROUPID IS NOT NULL;
/