create table RDX_JMSQUEUE(
	QUEUEID NUMBER(9,0) not null,
	JNDICONNFACTORYNAME VARCHAR2(200 char) null,
	CONNFACTORYCLASSNAME VARCHAR2(200 char) null,
	CONNFACTORYOPTIONS VARCHAR2(4000 char) null,
	SUBSCRIPTIONNAME VARCHAR2(200 char) null,
	CLIENTID VARCHAR2(200 char) null)
/

grant select, update, insert, delete on RDX_JMSQUEUE to &USER&_RUN_ROLE
/

alter table RDX_MESSAGEQUEUE
	add JNDIINITIALCONTEXTFACTORY VARCHAR2(200 char) null
/

alter table RDX_MESSAGEQUEUE
	add JNDIOPTIONS VARCHAR2(4000 char) null
/

alter table RDX_MESSAGEQUEUE
	add JNDIPROVIDERURL VARCHAR2(200 char) null
/

alter table RDX_MESSAGEQUEUE
	add JNDIQUEUENAME VARCHAR2(200 char) null
/

create unique index PK_RDX_JMSQUEUE on RDX_JMSQUEUE (QUEUEID asc)
/

alter table RDX_JMSQUEUE add constraint PK_RDX_JMSQUEUE primary key (QUEUEID) rely
/

alter table RDX_JMSQUEUE
	add constraint FK_RDX_JMSQUEUE_MESSAGEQUEUE foreign key (QUEUEID)
	references RDX_MESSAGEQUEUE (ID) on delete cascade
/

