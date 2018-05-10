create or replace package body RDX_PC_Utils as

	function findSuitableChannelId(
		pKind in varchar2,
		pAddress in varchar2,
		pImportance in integer,
		pExcludeUnitId in integer,
		pPrevPriority in integer
	) return integer
	is
	    res number(9,0);
	begin
	    select unitId into res 
	    from (select * from
	    (select RDX_PC_CHANNELUNIT.ID unitId from RDX_PC_CHANNELUNIT inner join RDX_UNIT on RDX_PC_CHANNELUNIT.ID=RDX_UNIT.ID 
	    where 
	        RDX_UNIT.USE != 0 
	        and (pExcludeUnitId is null or RDX_PC_CHANNELUNIT.ID != pExcludeUnitId) 
	        and RDX_PC_CHANNELUNIT.KIND = pKind
	        and regexp_like(RDX_PC_Utils.extractActualAddress(pKind, pAddress), RDX_PC_CHANNELUNIT.MESSADDRESSREGEXP) 
	        and pImportance >= RDX_PC_CHANNELUNIT.MINIMPORTANCE and pImportance <= RDX_PC_CHANNELUNIT.MAXIMPORTANCE 
	    order by 
	        (case when pPrevPriority is null or RDX_PC_CHANNELUNIT.ROUTINGPRIORITY < pPrevPriority then 1 else 0 end) desc, 
	        RDX_UNIT.STARTED desc, 
	        (RDX_PC_CHANNELUNIT.ROUTINGPRIORITY * 10 + dbms_random.value) desc, 
	        RDX_PC_CHANNELUNIT.ID
	    )) where rownum < 2;
	    return res;
	exception when others then return null;
	end;

	function extractActualAddress(
		kind in varchar2,
		address in varchar2
	) return varchar2
	is
	    actualAddress varchar2(1000);
	    addressXml XmlType;
	begin
	    if kind in ('Gcm', 'Apns', 'Wns') then
	        select xmltype(address) into addressXml from dual;
	        select extractValue(addressXml, '/pc:PushNotificationAddress/pc:AppName', 'xmlns:pc="http://schemas.radixware.org/personalcommunications.xsd"') into actualAddress from dual;
	    else
	        actualAddress := address;
	    end if;
	    return actualAddress;
	    exception when others then return null;
	end;
end;
/

