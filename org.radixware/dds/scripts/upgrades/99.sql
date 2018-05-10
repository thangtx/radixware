rename RDX_AADCOGGERROR to RDX_AADC_OGGERROR
/

grant select, update, insert, delete on RDX_AADC_OGGERROR to &USER&_RUN_ROLE
/

alter index PK_RDX_AADCOGGERROR rename to PK_RDX_AADC_OGGERROR
/

alter table RDX_AADC_OGGERROR rename constraint PK_RDX_AADCOGGERROR to PK_RDX_AADC_OGGERROR
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

