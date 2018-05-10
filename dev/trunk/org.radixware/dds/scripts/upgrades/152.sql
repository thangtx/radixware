insert into RDX_AMQPQueue (queueId)
	select id from RDX_MessageQueue where queueKind = 'AMQP' and id not in (select queueId from RDX_AMQPQueue)
/
