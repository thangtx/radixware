create table RDX_QUEUEDELAYEDMESS(
	ID NUMBER(9,0) not null,
	PARTITIONID NUMBER(9,0) not null,
	QUEUEID NUMBER(9,0) not null,
	MESS CLOB null)
/

grant select, update, insert, delete on RDX_QUEUEDELAYEDMESS to &USER&_RUN_ROLE
/

create table RDX_QUEUEPARTITIONDELAY(
	ID NUMBER(9,0) not null,
	QUEUEID NUMBER(9,0) not null,
	DELAY NUMBER(1,0) default 0 not null)
/

grant select, update, insert, delete on RDX_QUEUEPARTITIONDELAY to &USER&_RUN_ROLE
/

alter table RDX_MESSAGEQUEUE
	add DELAYONERROR NUMBER(1,0) default 0 not null
/

create index IDX_RDX_QUEUEDELAYEDMESS_QUEUE on RDX_QUEUEDELAYEDMESS (QUEUEID asc, PARTITIONID asc, ID asc)
/

create unique index PK_RDX_QUEUEDELAYEDMESS on RDX_QUEUEDELAYEDMESS (ID asc)
/

create unique index PK_RDX_QUEUEPARTITIONDELAY on RDX_QUEUEPARTITIONDELAY (ID asc, QUEUEID asc)
/

alter table RDX_QUEUEDELAYEDMESS add constraint PK_RDX_QUEUEDELAYEDMESS primary key (ID) rely
/

alter table RDX_QUEUEPARTITIONDELAY add constraint PK_RDX_QUEUEPARTITIONDELAY primary key (ID, QUEUEID) rely
/

alter table RDX_QUEUEDELAYEDMESS
	add constraint FK_RDX_QUEUEDELAYEDMESS_MESSAG foreign key (QUEUEID)
	references RDX_MESSAGEQUEUE (ID) on delete cascade
/

alter table RDX_QUEUEPARTITIONDELAY
	add constraint FK_RDX_QUEUEPARTITIONDELAY_MQ foreign key (QUEUEID)
	references RDX_MESSAGEQUEUE (ID) on delete cascade
/

