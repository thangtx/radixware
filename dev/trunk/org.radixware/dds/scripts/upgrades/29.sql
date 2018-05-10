alter table RDX_AC_USER
	modify (PWDHASH VARCHAR2(64 char))
/

alter table RDX_AC_USER
	add PWDHASHALGO VARCHAR2(16 char) default 'SHA-1' null
/

