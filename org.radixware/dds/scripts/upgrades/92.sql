create table RDX_AMQPQUEUE(
	QUEUEID NUMBER(9,0) not null,
	DELIVERYMODE NUMBER(1,0) default 1 null,
	PRIORITY NUMBER(1,0) null,
	APPID VARCHAR2(200 char) null,
	WRITEUSERID NUMBER(1,0) default 0 not null,
	REPLYTO VARCHAR2(100 char) null,
	VIRTUALHOST VARCHAR2(200 char) null)
/

grant select, update, insert, delete on RDX_AMQPQUEUE to &USER&_RUN_ROLE
/

alter table RDX_MESSAGEQUEUE
	add COPYIDTOCORRID NUMBER(1,0) default 0 not null
/

alter table RDX_MESSAGEQUEUE
	add UUIDMESSID NUMBER(1,0) default 0 not null
/

create unique index PK_RDX_AMQPQUEUE on RDX_AMQPQUEUE (QUEUEID asc)
/

alter table RDX_AMQPQUEUE add constraint PK_RDX_AMQPQUEUE primary key (QUEUEID) rely
/

alter table RDX_AMQPQUEUE
	add constraint FK_RDX_AMQPQUEUE_MESSAGEQUEUE foreign key (QUEUEID)
	references RDX_MESSAGEQUEUE (ID) on delete cascade
/

