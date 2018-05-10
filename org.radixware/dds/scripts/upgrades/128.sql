create or replace package body RDX_ADS_META as

	function isDefInDomain(
		pDefId in varchar2,
		pDomainId in varchar2
	) return integer
	is
	    version integer := RDX_Arte.getVersion();
	begin
	    if pDefId = pDomainId then
	        return 1;
	    end if;
	    for cur in (select DOMAINID domainId from RDX_DEF2DOMAIN where VERSIONNUM = version and DEFID = pDefId) loop
	      if RDX_ADS_META.isDefInDomain(cur.domainId, pDomainId) != 0 then  
	          return 1;
	      end if;
	    end loop;
	    return 0;
	end;

	function isClassExtends(
		pClsId in varchar2,
		pBaseClsId in varchar2
	) return integer
	is
	    version integer := RDX_Arte.getVersion();
	begin
	    if pClsId = pBaseClsId then
	        return 1;
	    end if;
	    for cur in (select /*+ INDEX(RDX_CLASSANCESTOR PK_RDX_CLASSANCESTOR) */ ANCESTORID from RDX_CLASSANCESTOR where VERSIONNUM = version and CLASSID = pClsId) loop
	      if RDX_ADS_META.isClassExtends(cur.ancestorId, pBaseClsId) != 0 then  
	          return 1;
	      end if;
	    end loop;
	    return 0;
	end;

	function areVersionIndicesDeployed(
		pVersionNum in integer
	) return integer
	is
	begin
	    for cur in (select 1 from dual 
	        where   exists (select * from RDX_CLASSANCESTOR where VERSIONNUM = pVersionNum) or 
	                exists (select * from RDX_DEF2DOMAIN where VERSIONNUM = pVersionNum) or
	                exists (select * from RDX_ENUMITEM2DOMAIN where VERSIONNUM = pVersionNum) or
	                exists (select * from RDX_ENTITYUSERREF where RDX_ENTITYUSERREF.VERSIONNUM = pVersionNum) or
	                exists (select * from RDX_ENTITYARRUSERREF where  RDX_ENTITYARRUSERREF.VERSIONNUM = pVersionNum) or
	                exists (select * from RDX_EVENTCODE2EVENTPARAMS where  RDX_EVENTCODE2EVENTPARAMS.VERSIONNUM = pVersionNum)
	    ) loop
	        return 1;
	    end loop;
	    return 0;
	end;

	function isEnumValueInDomain(
		pEnumId in varchar2,
		pEnumItemValAsStr in varchar2,
		pDomainId in varchar2
	) return integer
	is
	    version integer := RDX_Arte.getVersion();
	begin
	    for cur in (select DOMAINID enumItemDomainId from RDX_ENUMITEM2DOMAIN where 
	        VERSIONNUM = version and ENUMID = pEnumId and
	        ENUMITEMVALASSTR = pEnumItemValAsStr) 
	    loop
	        if (RDX_ADS_META.isDefInDomain(pDefId => cur.enumItemDomainId, pDomainId => pDomainId) <> 0) then        
	            return 1;
	        end if;
	    end loop;
	    return 0;
	end;

	function getIndicesOldestVersion return integer
	is
	  tmp integer;
	  res integer;
	begin
	    select min(VERSIONNUM ) into res from RDX_CLASSANCESTOR;
	    select min(VERSIONNUM) into tmp from RDX_DEF2DOMAIN;
	    if (tmp < res) then
	        res := tmp;
	    end if;    
	    select min(VERSIONNUM) into tmp from RDX_ENUMITEM2DOMAIN;
	    if (tmp < res) then
	        res := tmp;
	    end if;    
	    select min(RDX_ENTITYUSERREF.VERSIONNUM) into tmp from RDX_ENTITYUSERREF;
	    if (tmp < res) then
	        res := tmp;
	    end if;    
	    select min(RDX_ENTITYARRUSERREF.VERSIONNUM) into tmp from RDX_ENTITYARRUSERREF;
	    if (tmp < res) then
	        res := tmp;
	    end if;
	    select min(RDX_EVENTCODE2EVENTPARAMS.VERSIONNUM) into tmp from RDX_EVENTCODE2EVENTPARAMS;
	    if (tmp < res) then
	        res := tmp;
	    end if;     
	    return res;
	end;

	function getIndexedVersionsCnt return integer
	is
	  res integer;
	begin
	  select count(*) into res from (
	    select distinct versionnum from (
	         select distinct VERSIONNUM from RDX_CLASSANCESTOR union
	         select distinct VERSIONNUM from RDX_DEF2DOMAIN union
	         select distinct VERSIONNUM from RDX_ENUMITEM2DOMAIN union
	         select distinct RDX_ENTITYUSERREF.VERSIONNUM from RDX_ENTITYUSERREF union
	         select distinct RDX_ENTITYARRUSERREF.VERSIONNUM from RDX_ENTITYARRUSERREF union
	         select distinct RDX_EVENTCODE2EVENTPARAMS.VERSIONNUM from RDX_EVENTCODE2EVENTPARAMS
	    )
	  );
	  return res;
	end;

	procedure truncDeployedIndices
	is
	    oldestVer integer;
	begin
	   if RDX_ADS_META.getIndexedVersionsCnt() > 4 then
	      oldestVer := RDX_ADS_META.getIndicesOldestVersion();
	      delete from RDX_CLASSANCESTOR where VERSIONNUM = oldestVer;  
	      delete from RDX_DEF2DOMAIN where VERSIONNUM = oldestVer;  
	      delete from RDX_ENUMITEM2DOMAIN where VERSIONNUM = oldestVer;
	      delete from RDX_ENTITYUSERREF where RDX_ENTITYUSERREF.VERSIONNUM = oldestVer;
	      delete from RDX_ENTITYARRUSERREF where RDX_ENTITYARRUSERREF.VERSIONNUM = oldestVer;
	      delete from RDX_EVENTCODE2EVENTPARAMS where RDX_EVENTCODE2EVENTPARAMS.VERSIONNUM = oldestVer;
	   end if;
	end;
end;
/

