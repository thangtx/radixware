drop trigger TAIR_RDX_AADC_SEQUENCEDDL
/

create or replace trigger TAIR_RDX_AADC_SEQUENCEDDL
after insert on RDX_AADC_SEQUENCEDDL
for each row
DECLARE
    pragma AUTONOMOUS_TRANSACTION;
    oggState RAW(100);
BEGIN
    oggState := RDX_Aadc.suspendReplication;
    RDX_Aadc.normalizeSequence(:new.NAME);
    RDX_Aadc.restoreReplication(oggState);
    commit;
EXCEPTION
    WHEN OTHERS THEN 
        RDX_Aadc.restoreReplication(oggState);
        RAISE;
END;
/

begin
    DBMS_DDL.SET_TRIGGER_FIRING_PROPERTY(
        trig_owner => sys_context( 'userenv', 'current_schema' ), 
        trig_name  => 'TAIR_' || 'RDX_AADC_SEQUENCEDDL',
        fire_once  => false);
end;


/

