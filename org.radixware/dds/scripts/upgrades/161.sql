drop trigger TAIR_RDX_AADC_OGGERROR
/

create or replace trigger TAIR_RDX_AADC_OGGERROR
after insert on RDX_AADC_OGGERROR
for each row
begin
    RDX_Trace.put_4word(3, 'mlbaec6FJM26WOYRD2VPIPOQNVLY6K6Y-mls65KIIJOGAVFBNCM5QZQO524YHI',
        to_char(:new.FILERBA), to_char(:new.FILESEQNO), to_char(:new.SCN),
        to_char(:new.DBERRNUM) || ' - ' || :new.DBERRMSG,
        'Server'
    );
end;
/
begin
    DBMS_DDL.SET_TRIGGER_FIRING_PROPERTY(
        trig_owner => sys_context( 'userenv', 'current_schema' ), 
        trig_name  => 'TAIR_' || 'RDX_AADC_OGGERROR',
        fire_once  => false);
end;


/

