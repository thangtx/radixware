create or replace package body RDX_ACS as

	Type TIdRecord IS RECORD (IdValue varchar(50));
	Type TIdRecordList is table of TIdRecord index by binary_integer;

	type str_list_type  is table of varchar2(50) index BY BINARY_INTEGER;

	function constSuperAdminRoleId return varchar2
	is
	begin
	    return 'rolSUPER_ADMIN_______________';
	end;

	function constErrorMessage return varchar2
	is
	begin
	    return 'Incorrect input string - ';
	end;

	function packAccessPartitionValue(
		val in varchar2
	) return varchar2
	is
	res varchar2(32767);
	begin 
	  res := Replace(val, '\',  '\\');
	  res := Replace(res, ')',  '\)');
	  return res;
	end;

	function contentId(
		roleId in varchar2,
		list in TIdRecordList
	) return integer
	is
	begin

	 if (list.first() is not null) then
	   for i in list.first() .. list.last() 
	   loop 
	     if list.exists(i) and list(i).IdValue = roleid then         
	        return 1;
	     end if;
	   end loop;                               
	 end if;
	 return 0;
	end;

	function unPackAccessPartitionValue(
		val in varchar2
	) return varchar2
	is
	res varchar2(32767);
	begin
	  res := Replace(val, '\)', ')');
	  res := Replace(res, '\\', '\');
	  return res;
	end;

	procedure clearInheritRights(
		pUserGroup in varchar2
	)
	is
	begin
	    delete from RDX_AC_USER2ROLE u2r where u2r.isOwn = 0 and username in (select username from RDX_AC_USER2USERGROUP where GroupName=pUserGroup);
	end;

	-- area_           - область установленная в таблице user2role
	-- largerArea_  - область сформированная сервером для текущей таблицы
	-- largerArea_    не содержит  координат с флагом запрещено.
	-- 
	-- Если для каждого семейства из области largerArea_ найдется семейство с таким же не 
	--   заприщенным разделом в области area_ или в области area_ для этого семейства нет 
	--   ограничений то вернуть 1
	-- иначе 0
	function containsPointInArea(
		area_ in TRdxAcsArea,
		largerArea_ in TRdxAcsArea
	) return boolean
	is   
	     n1        boolean;
	     n2        boolean;  
	     largerAreaSize integer;
	     areaSize       integer;
	     sFamilyID varchar2(50); 
	begin
	                                                    
	   
	   if (largerArea_ is null) then 
	     return true;         
	   end if;
	   largerAreaSize  := largerArea_.boundaries.COUNT();   
	   if (largerAreaSize = 0) then 
	     return true;
	   end if;                              
	 
	   areaSize := area_.boundaries.COUNT();
	 
	   For i in 1 .. largerAreaSize
	     loop
	       sFamilyID := largerArea_.boundaries(i).FamilyID; 
	      n1 := largerArea_.boundaries(i).KeyVal is NULL;
	      <<label1>> for j in 1 .. areaSize
	      loop
	         if sFamilyID = area_.boundaries(j).FamilyID then
	           if (area_.boundaries(j).Prohibited = 1)then
	              return false;
	           end if;
	           n2 := area_.boundaries(j).KeyVal is null; 
	           if (
	               (n1 and n2) or
	                (
	                 (not n1) and
	                 (not n2) and
	                 (largerArea_.boundaries(i).KeyVal = area_.boundaries(j).KeyVal)
	                )
	               ) then
	              exit label1;
	           end if;
	           return false;
	         end if;
	      end loop; 
	     end loop;  
	   return true; 
	end;

	-- return (largerArea_ объемлит area_)
	function containsPointInArea2(
		largerArea_ in TRdxAcsArea,
		area_ in TRdxAcsArea
	) return boolean
	is   
	    n1        boolean;
	    n2        boolean;
	    find      boolean;
	    largerAreaSize integer;
	    areaSize       integer;
	    sFamilyID varchar2(50);
	begin
	    if (largerArea_ is null) then -- не партиционируемые права
	     return true;         
	    end if;
	    largerAreaSize      := largerArea_.boundaries.COUNT();   
	    if (largerAreaSize = 0) then -- Пустая коллекция, значит включает все пространство
	     return true;
	    end if;                              
	    if (area_ is null) then 
	     return false;         
	    end if;
	    areaSize := area_.boundaries.COUNT();
	    if (areaSize = 0) then
	     return false;
	    end if;

	    For i in 1 .. largerAreaSize
	     Loop
	       sFamilyID := largerArea_.boundaries(i).FamilyID;
	       if (largerArea_.boundaries(i).Prohibited = 0) then         
	         n1 := largerArea_.boundaries(i).KeyVal is NULL;
	         find := false;
	         <<label1>> for j in 1 .. areaSize 
	           loop
	             if sFamilyID = area_.boundaries(j).FamilyID then
	               n2 := area_.boundaries(j).KeyVal is null; 
	               if (
	                   (area_.boundaries(j).Prohibited = 1)or
	                   (n1 and n2) or 
	                    (
	                     (not n1) and 
	                     (not n2) and 
	                     (largerArea_.boundaries(i).KeyVal = area_.boundaries(j).KeyVal)
	                    )  
	                  ) then
	                  begin       
	                  find := true;
	                  exit label1;
	                  end;
	               end if;
	               return false;
	             end if;
	           end loop;
	           if not find then
	             return false;
	           end if;
	       else
	         n2 := false;
	         <<label2>>for j in 1 .. areaSize 
	                    loop
	                      if (sFamilyID = area_.boundaries(j).FamilyID) then  
	                         if (area_.boundaries(j).Prohibited = 1) then
	                            n2 := true;
	                            exit label2;
	                         else
	                           return false;
	                         end if;
	                      end if;
	                    end loop; 
	         if not n2 then
	           return false;
	         end if;
	       end if;
	     end loop;   
	    return true;
	 end;

	function strToArea(
		str in varchar2,
		pos in out integer
	) return TRdxAcsArea
	is 
	    res TRdxAcsArea; 
	    prohibited integer;
	    newPos integer;
	    newPos2 integer;
	    newPos3 integer;
	    accessPartitionKey varchar2(30);
	    accessPartitionValue varchar2(32767);
	--    str varchar2(32767); 
	begin

	--z := s;
	--unpuck
	--    str :=RDX_ACS.unPackAccessPartitionValue(str_);
	--

	if SubStr(str, pos, 1)<>'(' then
	  RAISE_APPLICATION_ERROR (-20100, RDX_ACS.constErrorMessage);
	end if; 
	pos:=pos+1;
	res := TRdxAcsArea(TRdxAcsCoordinates());
	<<lbl1>>while(true)
	    loop
	      if SubStr(str , pos, 1)=')' then
	         exit lbl1;
	      end if;
	      if SubStr(str , pos, 1)<>'(' then
	         RAISE_APPLICATION_ERROR (-20100, RDX_ACS.constErrorMessage || str);
	      end if;
	      pos:=pos+1;
	      if SubStr(str , pos, 1)='0' then
	         prohibited := 0;
	      else 
	        if SubStr(str , pos, 1)='1' then
	           prohibited := 1;
	        else
	           RAISE_APPLICATION_ERROR (-20100, RDX_ACS.constErrorMessage || str);
	        end if;
	      end if;
	      pos:=pos+1;
	      if SubStr(str , pos, 1)<>',' then
	         RAISE_APPLICATION_ERROR (-20100, RDX_ACS.constErrorMessage || str);
	      end if;
	      pos:=pos+1;
	      accessPartitionKey := SubStr(str, pos,29); 
	      pos:=pos+29;
	      if SubStr(str, pos, 1)<>',' then
	         RAISE_APPLICATION_ERROR (-20100, RDX_ACS.constErrorMessage || str);
	      end if;
	      pos := pos+1;
	      newPos:=InStr(str, ')', pos);
	      if newPos = 0 then
	         RAISE_APPLICATION_ERROR (-20100, RDX_ACS.constErrorMessage || str);
	      end if;      
	      if newPos = pos then
	         accessPartitionValue := null;
	      else
	      
	      <<lbl2>>while (true)
	         loop
	         --\\\)\))
	         if (SubStr(str, newPos-1, 1)='\')then
	             newPos2 := newPos-2;
	             <<lbl3>>while (true)
	                loop
	                if (SubStr(str, newPos2, 1)<>'\')then
	                  exit lbl3;  
	                end if;
	                if (SubStr(str, newPos2-1, 1)<>'\')then
	                  exit lbl2;  
	                end if;
	                newPos2 := newPos2-2;
	                end loop;
	                newPos := newPos+1;
	                newPos:=InStr(str, ')', newPos);
	         else
	            exit lbl2;
	         end if;         
	         end loop;
	         accessPartitionValue := RDX_ACS.unPackAccessPartitionValue(SubStr(str, pos, newPos-pos));
	         
	         
	      end if;
	      pos := newPos+ 1;
	      res.boundaries.EXTEND();
	      res.boundaries(res.boundaries.COUNT()):=TRdxAcsCoordinate(prohibited, accessPartitionKey, accessPartitionValue);
	    end loop;
	return res;
	end;

	function curUserHasRoleInArea(
		pRole in varchar2,
		pPointList in TRdxAcsAreaList
	) return integer
	is     
	begin 
	  return RDX_ACS.userHasRoleInArea(RDX_Arte.getUserName(), pRole, pPointList); 
	end;

	function userHasExplicitRoleInArea(
		pUser in varchar2,
		pRole in varchar2,
		pPointList in TRdxAcsAreaList
	) return integer
	is     
	    Area TRdxAcsArea; 
	    flag boolean;
	    dd integer;
	begin 
	    flag := pPointList is null or pPointList.COUNT()=0;  
	    for ind in (Select * from RDX_AC_USER2ROLE
	             where isNew=0 and isOwn=0 and userName = pUser and  pRole = roleId)
	     loop
	       if flag then
	          return 1;  
	       end if;
	       Area := RDX_ACS_UTILS.buildAssignedAccessAreaU2R(ind);
	       for i in pPointList.FIRST()..pPointList.LAST()
	         loop
	           if RDX_ACS.containsPointInArea2(Area, pPointList(i)) then
	              return 1;
	           end if;                                          
	         end loop;  
	     end loop;   
	    return 0;
	    exception 
	      when others then 
	      RAISE_APPLICATION_ERROR (
	         -20100, 
	         'raise exception detected - ' || SQLERRM || ' code = ' ||
	         TO_CHAR(SQLCODE) || ' in userHasRoleInArea(' ||
	         pUser || ', ' || pRole ||  ', ' || RDX_ACS.areaListToStr(pPointList) || ')' 
	         );  
	         null;
	end;

	function userHasRoleInArea(
		pUser in varchar2,
		pRole in varchar2,
		pPointList in TRdxAcsAreaList
	) return integer
	is     
	    Area TRdxAcsArea; 
	    flag boolean;
	    dd integer;
	    cSuperAdminRoleId constant varchar2(30) := RDX_ACS.constSuperAdminRoleId;
	begin 
	    flag := pPointList is null or pPointList.COUNT()=0;  
	    for ind in (Select * from RDX_AC_USER2ROLE
	             where isNew=0 and isOwn=0 and userName = pUser and  pRole || cSuperAdminRoleId LIKE '%' || roleId || '%')
	     loop
	       if flag then
	          return 1;  
	       end if;
	       Area := RDX_ACS_UTILS.buildAssignedAccessAreaU2R(ind);
	       for i in pPointList.FIRST()..pPointList.LAST()
	         loop
	           if RDX_ACS.containsPointInArea2(Area, pPointList(i)) then
	              return 1;
	           end if;                                          
	         end loop;  
	     end loop;   
	    return 0;
	    exception 
	      when others then 
	      RAISE_APPLICATION_ERROR (
	         -20100, 
	         'raise exception detected - ' || SQLERRM || ' code = ' ||
	         TO_CHAR(SQLCODE) || ' in userHasRoleInArea(' ||
	         pUser || ', ' || pRole ||  ', ' || RDX_ACS.areaListToStr(pPointList) || ')' 
	         );  
	         null;
	end;

	function curUserHasAnyOfRolesInArea(
		pRolesArr in clob,
		pPointList in TRdxAcsAreaList
	) return integer
	is
	    roles RDX_Array.ARR_STR;
	begin
	    roles := RDX_Array.fromStr(pRolesArr);    
	    if roles.COUNT > 0 then
	        for idx in roles.FIRST .. roles.LAST loop
	           if RDX_ACS.curUserHasRoleInArea(roles(idx), pPointList) != 0 then
	              return 1;
	           end if;
	        end loop;
	    else
	        return RDX_ACS.curUserHasRoleInArea(RDX_ACS.constSuperAdminRoleId, pPointList);
	    end if;
	    return 0;
	end;

	function userHasAnyOfRolesInArea(
		pUser in varchar2,
		pRolesArr in clob,
		pPointList in TRdxAcsAreaList
	) return integer
	is
	    roles RDX_Array.ARR_STR;
	begin
	    roles := RDX_Array.fromStr(pRolesArr);    
	    if roles.COUNT > 0 then
	        for idx in roles.FIRST .. roles.LAST loop
	           if RDX_ACS.userHasRoleInArea(pUser, roles(idx), pPointList) != 0 then
	              return 1;
	           end if;
	        end loop;
	    else
	        return RDX_ACS.curUserHasRoleInArea(RDX_ACS.constSuperAdminRoleId, pPointList);
	    end if;
	    return 0;
	end;

	function curUserHasRoleForObject(
		pRole in varchar2,
		pPointList in TRdxAcsAreaList
	) return integer
	is     
	begin 
	    return RDX_ACS.userHasRoleForObject(RDX_Arte.getUserName(), pRole, pPointList); 
	end;

	function userHasRoleForObject(
		pUser in varchar2,
		pRole in varchar2,
		pPointList in TRdxAcsAreaList
	) return integer
	is     
	    Area TRdxAcsArea; 
	    flag boolean;
	    cSuperAdminRoleId constant varchar2(30) := RDX_ACS.constSuperAdminRoleId;
	begin 
	    flag := pPointList is null or pPointList.COUNT()=0;  
	    for ind in (Select * from RDX_AC_USER2ROLE
	             where isNew=0  and isOwn=0 and userName = pUser and  pRole || cSuperAdminRoleId LIKE '%' || roleId || '%')
	     loop
	       if flag then
	          return 1;  
	       end if;
	       Area := RDX_ACS_UTILS.buildAssignedAccessAreaU2R(ind);
	       for i in pPointList.FIRST()..pPointList.LAST()
	         loop
	           if RDX_ACS.containsPointInArea(Area, pPointList(i)) then
	              return 1;
	           end if;
	         end loop; 
	     end loop;        
	    return 0;
	end;

	function userHasAnyOfRolesForObject(
		pUser in varchar2,
		pRolesArr in clob,
		pPointList in TRdxAcsAreaList
	) return integer
	is
	    roles RDX_Array.ARR_STR;
	begin
	    roles := RDX_Array.fromStr(pRolesArr);    
	    if roles.COUNT > 0 then
	        for idx in roles.FIRST .. roles.LAST loop
	           if RDX_ACS.userHasRoleInArea(pUser, roles(idx), pPointList) != 0 then
	              return 1;
	            end if;
	        end loop;
	    else
	        return RDX_ACS.curUserHasRoleInArea(RDX_ACS.constSuperAdminRoleId, pPointList);
	    end if;
	    return 0;
	end;

	function curUserHasAnyOfRolesForObject(
		pRolesArr in clob,
		pPointList in TRdxAcsAreaList
	) return integer
	is
	    roles RDX_Array.ARR_STR;
	begin
	    roles := RDX_Array.fromStr(pRolesArr);    
	    if roles.COUNT > 0 then
	        for idx in roles.FIRST .. roles.LAST loop
	           if RDX_ACS.curUserHasRoleForObject(roles(idx), pPointList) != 0 then
	              return 1;
	           end if;
	        end loop;
	    else
	        return RDX_ACS.curUserHasRoleInArea(RDX_ACS.constSuperAdminRoleId, pPointList);
	    end if;
	    return 0;
	end;

	-- Принадлежит ли пользователь заданной группе
	function userIsInGroup(
		pUser in varchar2,
		pGroup in varchar2
	) return integer
	is
	    rez integer(1);
	begin     
	    rez:=0;
	    SELECT COUNT(*) INTO rez FROM RDX_AC_USER2USERGROUP WHERE ROWNUM <= 1 AND userName = pUser and groupName = pGroup;
	    return rez;
	end;

	-- Принадлежит ли текущий пользователь заданной группе
	function curUserIsInGroup(
		pGroup in varchar2
	) return integer
	is    
	begin  
	  return RDX_ACS.userIsInGroup(RDX_Arte.getUserName(), pGroup);
	end;

	function areaListToStr(
		areaList_ in TRdxAcsAreaList
	) return varchar2
	is
	    result_  varchar2(32767);   
	    size_    integer;
	begin
	     if areaList_ is null then
	        return null;
	     end if;
	     size_ := areaList_.COUNT(); 
	     result_ := '(';
	     for i in 1 .. size_
	       Loop 
	           result_ := result_ || RDX_ACS.areaToStr(areaList_(i));
	           if i=size_ then
	             result_ := result_ || ')'; 
	           end if;            
	       end loop;   
	    return result_;
	end;

	function areaToStr(
		area_ in TRdxAcsArea
	) return varchar2
	is
	    result_  varchar2(32767);   
	    size_    integer;
	begin
	    if area_ is null then
	      return null;
	    end if;
	    size_ := area_.boundaries.COUNT();

	    result_ := '(';
	    for i in 1 .. size_
	       Loop 
	           result_ := result_ || '(';
	           result_ := result_ || TO_CHAR(area_.boundaries(i).Prohibited) || ',';
	           result_ := result_ || area_.boundaries(i).FamilyID   || ',';
	           if area_.boundaries(i).KeyVal is not null then
	               result_ := result_ || RDX_ACS.packAccessPartitionValue(area_.boundaries(i).KeyVal);
	           end if;
	           result_ := result_ || ')'; 
	       end loop;   
	    result_ := result_ || ')';
	    return result_;
	end;

	procedure writeToClob(
		clob_ in out clob,
		val in varchar2
	)
	is
	 x VARCHAR2(32767);
	begin
	  x := val  || CHR(13);
	  DBMS_LOB.writeappend(clob_, length(x), x);    
	end;

	procedure fillCompileRightsBody(
		body_ in out clob,
		prefix_ in varchar2,
		str_list0 in str_list_type,
		exists_Package in int_list_type,
		index_main in varchar2,
		srcTableName in varchar2,
		userName_ in varchar2
	)
	is
	  x varchar2(32767);
	  n     integer;
	begin
	    n := str_list0.count();

	    For i in 1 .. n
	    loop
	        writeToClob(body_, prefix_ || ' if (' || index_main || '.MA$$' || str_list0(i) || ' = RDX_ACS.cRight_boundedByUser) then');
	        if (exists_Package(i) = 1) then    
	            writeToClob(body_, prefix_ || '    partitionsList' || to_char(i) || '.delete();');
	            writeToClob(body_, prefix_ || '    partitionsList' || to_char(i) || '.extend(1);');
	            writeToClob(body_, prefix_ || '    partitionsList' || to_char(i) || '(1):=ACS$' || str_list0(i) || '.getUserAssignment(RDX_Arte.getUserName());');
	            writeToClob(body_, prefix_ || '    mode' || to_char(i) || ':=RDX_ACS.cRight_boundedByPart;');
	        else    
	            writeToClob(body_, prefix_ || '    raise_application_error(-20000, ''' || 
	                               'System rights package ACS$' || str_list0(i) || 
	                               ' not found, but found RDX_ACS.cRight_boundedByUser bounding mode (Table=' || 
	                               srcTableName || ', Id = '' || ' || index_main || '.Id || '').'');');
	        end if; 

	        writeToClob(body_, prefix_ || ' else');
	        writeToClob(body_, prefix_ || '    RDX_ACS.fillPartitions(' || index_main || '.MA$$' || str_list0(i) || ', ' ||
	                                                     'mode' || to_char(i)  || ', ' || 
	                                                     '' || index_main || '.PA$$' || str_list0(i) || ', ' ||
	                                                     'partitionsList' || to_char(i) || ', ' || 
	                                                     '' || index_main || '.PG$$' || str_list0(i) || 
	                                                     ');');
	        writeToClob(body_, prefix_ || ' end if;');
	    end loop;
	    
	    For i in 1 .. n
	    loop
	        x := LPAD(' ', i);
	        writeToClob(body_, prefix_ || x || 'for i' || to_char(i) || 
	                 ' in partitionsList' || to_char(i) || '.first() .. partitionsList' || to_char(i) || '.last()');  
	        writeToClob(body_, prefix_ || x || 'loop'); 
	    end loop;     
	    
	        x := LPAD(' ', n) || '   INSERT INTO RDX_AC_USER2ROLE(userName, isOwn, RoleId, ID';
	        For i in 1 .. n
	        loop 
	            x := x || ', PA$$' || str_list0(i) || ', MA$$' || str_list0(i);
	        end loop; 
	        x:= x || ')';
	        writeToClob(body_, prefix_ || x);

	        x := LPAD(' ', n) || '   VALUES (' || userName_ || ', 0, ' || index_main || '.RoleId, SQN_RDX_AC_USER2ROLEID.NEXTVAL';
	        For i in 1 .. n
	        loop 
	            x := x || ', partitionsList' || to_char(i) || '(i' || to_char(i) || '), mode' || to_char(i);
	        end loop;     
	        x:= x || ');';
	        writeToClob(body_, prefix_ || x);
	    
	    For i in 1 .. n
	    loop
	        x := LPAD(' ', n - i + 1);
	        writeToClob(body_, prefix_ || x || 'end loop;'); 
	    end loop;       
	    
	end;

	function existsPackage(
		postfix_ in varchar2
	) return integer
	is
	 rez integer;
	begin
	 SELECT count(*) into rez FROM USER_OBJECTS WHERE OBJECT_TYPE = 'PACKAGE' and OBJECT_NAME = 'ACS$' || upper(postfix_) ;
	 return rez;  
	end;

	procedure fillPartitions(
		modeIn_ in integer,
		modeOut_ out integer,
		partitionIn_ in varchar2,
		partitionsOut_ in out long_str_list,
		partitionGroupId_ in integer
	)
	is
	    flag boolean;
	    partitionVals clob;
	    arr RDX_Array.ARR_STR;
	    partitionVal VARCHAR2(32767);
	begin
	    partitionsOut_.delete();
	    if (modeIn_ = RDX_ACS.cRight_unbounded) or (modeIn_ = RDX_ACS.cRight_prohibited) then
	         modeOut_:=modeIn_;
	         partitionsOut_.Extend(1);
	         partitionsOut_(1):=null;

	    elsif (modeIn_ = RDX_ACS.cRight_boundedByPart) then
	         modeOut_:=modeIn_;
	         partitionsOut_.Extend(1);
	         partitionsOut_(1):=partitionIn_;

	    elsif (modeIn_ = RDX_ACS.cRight_boundedByGroup) then
	         flag := partitionGroupId_ is not null;
	         if flag then
	            Select PARTITIONS into partitionVals from RDX_AC_PARTITIONGROUP where partitionGroupId_ = id;
	            flag := (partitionVals is not null) and (dbms_lob.getlength(partitionVals)<>0) and (partitionVals<>'[0]');
	         end if;
	         
	        if flag then        
	            modeOut_:=RDX_ACS.cRight_boundedByPart;
	            arr :=  RDX_Array.fromStr(partitionVals);
	            partitionsOut_.Extend(arr.count());
	            for i in arr.first() .. arr.last() 
	            loop
	                partitionVal:=arr(i);
	                if (partitionVal is not null) and length(partitionVal)>31 then
	                  partitionVal:=substr(partitionVal, 31);
	                else
	                  partitionVal:=null;
	                end if;
	                partitionsOut_(i) := partitionVal;
	            end loop;
	        else
	            modeOut_:=RDX_ACS.cRight_prohibited;
	            partitionsOut_.Extend(1);
	            partitionsOut_(1):=null;
	        end if;
	    else
	        raise_application_error(-20000, 'Detected invalid partition mode: ' || to_char(modeIn_)); 
	    end if;
	end;

	procedure fillCompileDefineValues(
		body_ in out clob,
		n in integer
	) deterministic
	is
	begin
	For i in 1 .. n
	    loop
	        writeToClob(body_, '  partitionsList' || to_char(i) || ' RDX_ACS.long_str_list;');
	        writeToClob(body_, '  mode' || to_char(i) || ' integer;');
	    end loop;
	end;

	procedure fillCompileInitValues(
		body_ in out clob,
		n in integer
	)
	is
	begin
	    For i in 1 .. n
	    loop        
	        writeToClob(body_, '  partitionsList' || to_char(i) || ' := RDX_ACS.long_str_list();');
	    end loop;
	end;

	procedure fillLongCycle(
		body_ in out clob,
		str_list0 in str_list_type
	)
	is
	    n  integer;
	    x varchar2(32767);
	    x2 varchar2(32767);
	begin
	n := str_list0.COUNT();

	writeToClob(body_, 'n:=calculatedList1.count;');

	writeToClob(body_, 'if (n=0) then');
	    writeToClob(body_, '  return 0;');
	    writeToClob(body_, 'end if;');
	    
	    For i in 1 .. n loop
	        x := LPAD(' ', i);
	        writeToClob(body_, x || 'for i' || to_char(i) || ' in partitionsList' || to_char(i) || '.first() .. partitionsList' || to_char(i) || '.last()');         
	        writeToClob(body_, x || 'loop');
	        writeToClob(body_, x || 'partition' || to_char(i) || ' := partitionsList' || to_char(i) || '(i' || to_char(i) || ');');
	    end loop;
	    
	    x := LPAD(' ', n);
	        
	    writeToClob(body_, x || 'ok_ := false;');
	    writeToClob(body_, x || 'for i in 1..n loop');    
	      For i in 1 .. n loop
	      writeToClob(body_, x || '  calcPartition' || to_char(i) || ' := calculatedList' || to_char(i) || '(i);');
	      writeToClob(body_, x || '  calcMode' || to_char(i) || ' := calculatedMode' || to_char(i) || '(i);');
	      end loop;
	      writeToClob(body_, x || '  if(');
	      
	      For i in 1 .. n loop
	      x2 := x || '  ((mode' || to_char(i) || ' = RDX_ACS.cRight_prohibited) or ' ||
	                     '(calcMode' || to_char(i) || ' = RDX_ACS.cRight_unbounded) or ' ||
	                            '(mode' || to_char(i) || ' = RDX_ACS.cRight_boundedByPart and ' ||
	                            'calcMode' || to_char(i) || ' = RDX_ACS.cRight_boundedByPart and ' ||
	                            'RDX_ACS.equalPartition(partition' || to_char(i) || ', calcPartition' || to_char(i) || ') = 1))';
	      if (i!=n) then
	        x2 := x2 || ' and';
	      else  
	        x2 := x2 || ') then';
	      end if;                            
	       writeToClob(body_, x || x2);      
	      end loop;
	    writeToClob(body_, x || ' ok_ := true;');
	    writeToClob(body_, x || ' exit;');
	     
	    writeToClob(body_, x || 'end if;');
	      
	    writeToClob(body_, x || 'end loop;');
	    
	    writeToClob(body_, x || 'if (not ok_) then');
	    writeToClob(body_, x || '  return 0;');
	    writeToClob(body_, x || 'end if;');
	    
	    
	    
	    
	    For i in 1 .. n loop
	        x := LPAD(' ', n - i + 1);
	        writeToClob(body_, x || 'end loop;');
	    end loop;
	end;

	procedure fillRights2Table(
		table_ in varchar2,
		func_ in varchar2,
		body_ in out clob,
		str_list0 in str_list_type,
		exists_Package in int_list_type
	)
	is
	    n  integer;
	    x varchar2(32767);
	    
	    cSuperAdminRoleId constant varchar2(32) := '''' || RDX_ACS.constSuperAdminRoleId || '''';
	begin

	    n := str_list0.COUNT();

	    writeToClob(body_, 'function ' || func_ || '(user_ in varchar2, id_ in integer) return integer');
	    writeToClob(body_, 'is');
	    For i in 1 .. n loop
	       writeToClob(body_, ' partition' || to_char(i) || ' VARCHAR2(32767);');
	       writeToClob(body_, ' mode' || to_char(i) || ' integer;');
	       writeToClob(body_, ' calcMode' || to_char(i) || ' integer;');
	       writeToClob(body_, ' groupId' || to_char(i) || ' integer;');
	       writeToClob(body_, ' partitionsList' || to_char(i) || ' RDX_ACS.long_str_list;');
	       writeToClob(body_, ' calculatedList' || to_char(i) || ' RDX_ACS.long_str_list;');
	       writeToClob(body_, ' calculatedMode' || to_char(i) || ' RDX_ACS.int_list_type;');
	       writeToClob(body_, ' calcPartition' || to_char(i) || ' VARCHAR2(32767);');
	              
	    end loop;
	    writeToClob(body_, ' roleId_ varchar(50);');
	    writeToClob(body_, ' i integer;');
	    writeToClob(body_, ' n integer;');
	    writeToClob(body_, ' ok_ boolean;');
	    writeToClob(body_, 'begin');
	    
	    x := '  select roleId';
	    For i in 1 .. n loop
	      x := x || ', MA$$' || str_list0(i) || ', PA$$' || str_list0(i) || ', PG$$' || str_list0(i);      
	    end loop;
	    x := x || ' into roleId_';
	    For i in 1 .. n loop
	      x := x || ', mode' || to_char(i) || ', partition' || to_char(i) || ', groupId' || to_char(i);
	    end loop;    
	    writeToClob(body_,  x || ' from ' || table_ || ' where id = id_;');
	        
	    For i in 1 .. n
	    loop        
	        writeToClob(body_, ' partitionsList' || to_char(i) || ' := RDX_ACS.long_str_list();');
	        writeToClob(body_, ' calculatedList' || to_char(i) || ' := RDX_ACS.long_str_list();');
	        writeToClob(body_, ' calculatedMode' || to_char(i) || ' := RDX_ACS.int_list_type();');        

	    end loop;

	    For i in 1 .. n
	    loop    
	        writeToClob(body_, '  if (mode' || to_char(i) ||  ' = RDX_ACS.cRight_boundedByUser) then');
	        if (exists_Package(i) = 1) then    
	            writeToClob(body_, '    partitionsList' || to_char(i) || '.delete();');
	            writeToClob(body_, '    partitionsList' || to_char(i) || '.extend(1);');
	            writeToClob(body_, '    partitionsList' || to_char(i) || '(1):=ACS$' || str_list0(i) || '.getUserAssignment(RDX_Arte.getUserName());');
	            writeToClob(body_, '    mode' || to_char(i) || ':=RDX_ACS.cRight_boundedByPart;');
	        else    
	            writeToClob(body_, '    raise_application_error(-20000, ''' || 
	                               'System rights package ACS$' || str_list0(i) || 
	                               ' not found, but found RDX_ACS.cRight_boundedByUser bounding mode (Table=' || 
	                               table_ || ', Id = '' || id_ || '').'');');
	        end if; 

	        writeToClob(body_, '  else');
	        writeToClob(body_, '    RDX_ACS.fillPartitions(mode' || to_char(i)  || ', calcMode' || to_char(i) || ', ' ||
	                                                     'partition' || to_char(i) || ', ' ||
	                                                     'partitionsList' || to_char(i) || ', ' || 
	                                                     'groupId' || to_char(i)|| 
	                                                     ');');
	        writeToClob(body_, '  end if;');
	    end loop;
	    
	      writeToClob(body_, '  i:=1;');
	      writeToClob(body_, '  for ind in (Select * from RDX_AC_USER2ROLE where isNew=0 and isOwn = 0 and userName = user_ and ');
	      writeToClob(body_, '                       (roleId_ = roleId or roleId = ' || cSuperAdminRoleId || ') )');
	      writeToClob(body_, '  loop');
	      
	    For i in 1 .. n      
	    loop
	      writeToClob(body_, '    calculatedList' || to_char(i) || '.Extend();');
	      writeToClob(body_, '    calculatedMode' || to_char(i) || '.Extend();');
	         
	      writeToClob(body_, '    calculatedList' || to_char(i) || '(i):=ind.PA$$' || str_list0(i) || ';');
	      writeToClob(body_, '    calculatedMode' || to_char(i) || '(i):=ind.MA$$' || str_list0(i) || ';');
	    end loop;      
	      writeToClob(body_, '    i:=i+1;');
	      writeToClob(body_, '  end loop;');
	    
	    RDX_ACS.fillLongCycle(body_, str_list0);
	    
	    writeToClob(body_, '  return 1;');
	    writeToClob(body_, 'end;');

	end;

	function isGroupExist(
		name_ in varchar2
	) return integer
	is
	rez INTEGER;
	begin
	 rez := 0;
	 SELECT COUNT(*) INTO rez FROM RDX_AC_USERGROUP WHERE ROWNUM <= 1 AND name_ = NAME;
	 return rez;
	end;

	procedure acsUtilsBuild
	is 

	    str_list0 str_list_type;
	    exists_Package int_list_type;

	    cur_index  integer;
	    n  integer;
	    i  integer;

	    cursor_name INTEGER;
	    ret	    INTEGER;

	    x varchar2(32767);
	    x2 varchar2(32767);

	    body_ clob;
	    
	    cSuperAdminRoleId constant varchar2(32) := '''' || RDX_ACS.constSuperAdminRoleId || '''';
	 
	begin

	    cur_index := 1;
	    dbms_lob.createTemporary(body_, false, dbms_lob.SESSION);
	    
	    exists_Package := int_list_type();
	     
	    for ind in (select DISTINCT table_name, column_name from user_tab_columns where  
	                  table_name = 'RDX_AC_USER2ROLE' 
	                and
	                  length(column_name) = 30 
	                and 
	                  column_name like 'PA$$%'
	                  order by column_name 
	               )                  
	    loop
	        x:=ind.column_name;
	        x:= SUBSTR(x, 5);        
	        str_list0(cur_index):=x;        
	        exists_Package.EXTEND();
	        exists_Package(cur_index):= RDX_ACS.existsPackage(x);
	        cur_index := cur_index+1;
	    end loop;
	 


	    -- body_ 
	    n := str_list0.COUNT();
	    
	    writeToClob(body_, 'create or replace package RDX_ACS_UTILS as');
	    writeToClob(body_, 'function buildAssignedAccessAreaU2R(row_ in RDX_AC_User2Role%ROWTYPE) return TRdxAcsArea;');
	    writeToClob(body_, 'PRAGMA RESTRICT_REFERENCES (buildAssignedAccessAreaU2R, WNDS);');
	    writeToClob(body_, 'function buildAssignedAccessAreaG2R(row_ in RDX_AC_UserGroup2Role%ROWTYPE) return TRdxAcsArea;');    
	    writeToClob(body_, 'PRAGMA RESTRICT_REFERENCES (buildAssignedAccessAreaG2R, WNDS);');
	    writeToClob(body_, 'procedure compileRights;');
	    writeToClob(body_, 'procedure compileRightsForGroup(pUserGroup in varchar2);');
	    writeToClob(body_, 'procedure compileRightsForUser(pUser in varchar2);');

	    writeToClob(body_, 'procedure moveRightsFromUserToGroup(user_ in varchar2, group_ in varchar2);');
	    
	    x := '';
	    For i in 1 .. n loop
	        x := x || ', mode' || to_char(i) || ' integer, partitions' || to_char(i) || ' clob';
	    end loop;
	    
	    writeToClob(body_, 'function existsRightsOnArea(user_ in varchar2, role_ in varchar2 ' || x || ') return integer;');
	    writeToClob(body_, 'PRAGMA RESTRICT_REFERENCES (existsRightsOnArea, WNDS);');
	    
	    writeToClob(body_, 'function existsRightsOnUser2Role(user_ in varchar2, id_ in integer) return integer;');
	    writeToClob(body_, 'PRAGMA RESTRICT_REFERENCES (existsRightsOnUser2Role, WNDS);');
	    
	    writeToClob(body_, 'function existsRightsOnGroup2Role(user_ in varchar2, id_ in integer) return integer;');
	    writeToClob(body_, 'PRAGMA RESTRICT_REFERENCES (existsRightsOnGroup2Role, WNDS);');
	    
	    writeToClob(body_, 'function isSuperAdmin(user_ in varchar2) return integer;');
	    writeToClob(body_, 'PRAGMA RESTRICT_REFERENCES (isSuperAdmin, WNDS);');
	        
	    For i in 1 .. n loop
	        writeToClob(body_, 'function gup_' || str_list0(i) || '(user_ in varchar2) return clob;');
	        --writeToClob(body_, 'PRAGMA RESTRICT_REFERENCES (gup_' || str_list0(i) || ', WNDS);');
	    end loop;
	    
	    
	    writeToClob(body_, 'end;');
	  
	  
	  
	    cursor_name := DBMS_SQL.OPEN_CURSOR;
	    DBMS_SQL.PARSE ( cursor_name, body_, DBMS_SQL.NATIVE );
	    ret := DBMS_SQL.EXECUTE ( cursor_name );
	    DBMS_SQL.CLOSE_CURSOR ( cursor_name );  


	    
	    dbms_lob.createTemporary(body_, false, dbms_lob.SESSION);
	  

	    writeToClob(body_, 'CREATE  OR REPLACE package body RDX_ACS_UTILS as');
	    
	    
	    For i in 1 .. n loop
	    writeToClob(body_, '/*get unbounded partitions*/');
	        writeToClob(body_, 'function gup_' || str_list0(i) || '(user_ in varchar2) return clob');
	        writeToClob(body_, 'is');
	        writeToClob(body_, ' p RDX_Array.ARR_STR;');
	        writeToClob(body_, ' i integer;');
	        writeToClob(body_, 'begin');
	        
	        x:='';
	        For j in 1 .. n loop
	            if (i<>j) then
	                x:= x || ' and MA$$' || str_list0(j) || '= RDX_ACS.cRight_unbounded';
	            end if;    
	        end loop;
	        
	        writeToClob(body_, 'p := RDX_Array.ARR_STR();');
	        writeToClob(body_, 'i := 1;');
	        
	        writeToClob(body_, 'for ind in (select PA$$' || str_list0(i) ||
	                     ' from RDX_AC_USER2ROLE where roleId = ' || cSuperAdminRoleId || ' and isNew = 0 and isOwn = 0 and userName = user_' 
	                      || ' and MA$$' || str_list0(i) || '= RDX_ACS.cRight_boundedByPart' || x || ')'
	                     );
	        writeToClob(body_, ' loop');
	        writeToClob(body_, '  p.extend();');
	        writeToClob(body_, '  p(i) := ind.PA$$' || str_list0(i) || ';');
	        writeToClob(body_, '  i := i+1;');
	        writeToClob(body_, ' end loop;');
	        
	        
	        writeToClob(body_, ' return RDX_Array.fromArrStr(p);');
	        writeToClob(body_, 'end;');    
	    end loop;
	    
	    
	 
	    writeToClob(body_, 'function isSuperAdmin(user_ in varchar2) return integer');
	    writeToClob(body_, 'is');
	    writeToClob(body_, ' count_ integer;');
	    writeToClob(body_, 'begin');
	    
	    x := '';
	    For i in 1 .. n loop
	        x := x || ' and MA$$' || str_list0(i) || '= RDX_ACS.cRight_unbounded';
	    end loop;
	    writeToClob(body_, ' select count(*) into count_ from RDX_AC_USER2ROLE where roleId = ' || cSuperAdminRoleId || ' and isNew = 0 and isOwn = 0 and userName = user_' || x || ';');
	    writeToClob(body_, ' if count_ > 0 then');
	    writeToClob(body_, '   return 1;');
	    writeToClob(body_, ' end if;');
	    
	    writeToClob(body_, ' return 0;');
	    writeToClob(body_, 'end;');

	   
	    
	    RDX_ACS.fillRights2Table('RDX_AC_USER2ROLE', 'existsRightsOnUser2Role', body_, str_list0, exists_Package);
	    RDX_ACS.fillRights2Table('RDX_AC_USERGROUP2ROLE', 'existsRightsOnGroup2Role', body_, str_list0, exists_Package);
	    



	    x := '';
	    For i in 1 .. n loop
	        x := x || ', mode' || to_char(i) || ' integer, partitions' || to_char(i) || ' clob';
	    end loop;

	    

	    --existsRightsOnArea
	    writeToClob(body_, 'function existsRightsOnArea(user_ in varchar2, role_ in varchar2 ' || x || ') return integer');
	    writeToClob(body_, 'is');
	    For i in 1 .. n loop
	       writeToClob(body_, ' partition' || to_char(i) || ' VARCHAR2(32767);');
	       writeToClob(body_, ' calcPartition' || to_char(i) || ' VARCHAR2(32767);');
	       writeToClob(body_, ' calcMode' || to_char(i) || ' integer;');
	       writeToClob(body_, ' partitionsList' || to_char(i) || ' RDX_Array.ARR_STR;');
	       writeToClob(body_, ' calculatedList' || to_char(i) || ' RDX_Array.ARR_STR;');
	       writeToClob(body_, ' calculatedMode' || to_char(i) || ' RDX_ACS.int_list_type;');
	    end loop;
	    writeToClob(body_, ' i integer;');
	    writeToClob(body_, ' n integer;');
	    writeToClob(body_, ' ok_ boolean;');
	    
	    writeToClob(body_, 'begin');
	        
	    For i in 1 .. n loop
	        writeToClob(body_, ' if mode' || to_char(i) ||' = RDX_ACS.cRight_prohibited then');
	        writeToClob(body_, '  partitionsList' || to_char(i) || ':= RDX_Array.fromStr(partitions' || to_char(i) || ');');
	        writeToClob(body_, ' else');
	        writeToClob(body_, '  partitionsList' || to_char(i) || ':= RDX_Array.ARR_STR();');
	        writeToClob(body_, '  partitionsList' || to_char(i) || '.Extend(1);');
	        writeToClob(body_, '  partitionsList' || to_char(i) || '(1):=null;');       
	        writeToClob(body_, ' end if;');
	        writeToClob(body_, ' calculatedList' || to_char(i) || ':= RDX_Array.ARR_STR();');
	        writeToClob(body_, ' calculatedMode' || to_char(i) || ':= RDX_ACS.int_list_type();');
	    end loop;
	    
	    writeToClob(body_, 'i:=1;');
	    writeToClob(body_, 'for ind in (Select * from RDX_AC_USER2ROLE where isNew=0 and isOwn = 0 and userName = user_ and ');
	    writeToClob(body_, '           (role_ = roleId or roleId = ' || cSuperAdminRoleId || ') )');
	    writeToClob(body_, 'loop');
	    For i in 1 .. n loop
	        writeToClob(body_, '   calculatedList' || to_char(i) || '.Extend();');
	        writeToClob(body_, '   calculatedMode' || to_char(i) || '.Extend();');
	        
	        writeToClob(body_, '   calculatedList' || to_char(i) || '(i):=ind.PA$$' || str_list0(i) || ';');
	        writeToClob(body_, '   calculatedMode' || to_char(i) || '(i):=ind.MA$$' || str_list0(i) || ';');
	    end loop;
	    writeToClob(body_, '   i:=i+1;');
	    writeToClob(body_, 'end loop;');
	    
	     
	    RDX_ACS.fillLongCycle(body_, str_list0);
	    
	    
	    writeToClob(body_, ' return 1;');
	    writeToClob(body_, 'end;');
	    
	    --writeToClob(body_, 'function existsRightsOnUser2Role(user_ in varchar2, id_ in integer) return integer;');
	    
	    --compileRights
	    writeToClob(body_, 'procedure compileRights');    
	    writeToClob(body_, 'is');

	    RDX_ACS.fillCompileDefineValues(body_, n);
	    
	    writeToClob(body_, 'begin');
	    
	    RDX_ACS.fillCompileInitValues(body_, n);
	    
	    writeToClob(body_, '/*clear all inherit rights*/');--comment    
	    writeToClob(body_, 'delete from RDX_AC_USER2ROLE u2r where u2r.isOwn=0;');    
	    
	    writeToClob(body_, '');
	    writeToClob(body_, '/*collect rights from RDX_AC_USER2ROLE*/');--comment
	    writeToClob(body_, 'for ind1 in (Select * from RDX_AC_USER2ROLE where isOwn=1 and isNew<>1)');
	    writeToClob(body_, 'loop');
	    RDX_ACS.fillCompileRightsBody(body_, '', str_list0, exists_Package, 'ind1', 'RDX_AC_USER2ROLE', 'ind1.userName');
	    writeToClob(body_, 'end loop;');
	        
	    writeToClob(body_, '');
	    writeToClob(body_, '/*collect rights from RDX_AC_USERGROUP2ROLE*/');--comment
	    writeToClob(body_, 'for ind1 in (Select * from RDX_AC_USERGROUP2ROLE where isNew<>1)');
	    writeToClob(body_, 'loop');
	    writeToClob(body_, '  for ind2 in (Select * from RDX_AC_USER2USERGROUP where GroupName=ind1.GroupName and state<>1)');
	    writeToClob(body_, '  loop');
	    RDX_ACS.fillCompileRightsBody(body_, '  ', str_list0, exists_Package, 'ind1', 'RDX_AC_USERGROUP2ROLE', 'ind2.userName');
	    writeToClob(body_, '  end loop;');
	    writeToClob(body_, 'end loop;');
	  
	    writeToClob(body_, 'end;');


	    --compileRightsForGroup     
	    
	    writeToClob(body_, 'procedure compileRightsForGroup(pUserGroup in varchar2)');
	    writeToClob(body_, 'is');
	    RDX_ACS.fillCompileDefineValues(body_, n);
	    
	    writeToClob(body_, 'begin');
	    
	    RDX_ACS.fillCompileInitValues(body_, n);
	    
	    writeToClob(body_, 'RDX_ACS.ClearInheritRights(pUserGroup);');
	    
	    writeToClob(body_, 'for ind1 in (select * from RDX_AC_USER2USERGROUP where GroupName=pUserGroup and state<>1)');
	    writeToClob(body_, 'loop');
	    writeToClob(body_, '  for ind2 in (select * from RDX_AC_USER2USERGROUP where UserName=ind1.UserName and state<>1)');
	    writeToClob(body_, '  loop');
	    
	    --from this user
	    writeToClob(body_, '    for ind3 in (Select * from RDX_AC_USER2ROLE where isOwn=1 and isNew<>1)');
	    writeToClob(body_, '    loop');
	    RDX_ACS.fillCompileRightsBody(body_, '    ', str_list0, exists_Package, 'ind3', 'RDX_AC_USER2ROLE', 'ind1.userName');
	    writeToClob(body_, '    end loop;');
	    writeToClob(body_, '');
	                
	    --from groups
	    writeToClob(body_, '    for ind3 in (Select * from RDX_AC_USERGROUP2ROLE where GroupName = ind2.GroupName and isNew<>1)');
	    writeToClob(body_, '    loop');
	    RDX_ACS.fillCompileRightsBody(body_, '    ', str_list0, exists_Package, 'ind3', 'RDX_AC_USERGROUP2ROLE', 'ind1.UserName');
	    writeToClob(body_, '    end loop;');
	    writeToClob(body_, '  end loop;');
	    writeToClob(body_, 'end loop;');
	    
	    writeToClob(body_, 'end;');


	    --compileRightsForUser 
	    writeToClob(body_, 'procedure compileRightsForUser(pUser in varchar2)');
	    writeToClob(body_, 'is');
	    RDX_ACS.fillCompileDefineValues(body_, n);
	    
	    writeToClob(body_, 'begin');
	    RDX_ACS.fillCompileInitValues(body_, n);

	    writeToClob(body_, 'delete from RDX_AC_USER2ROLE u2r where u2r.isOwn = 0 and username = pUser and isNew<>1;');
	    
	    writeToClob(body_, 'for ind1 in (select * from RDX_AC_USER2ROLE where username = pUser and isOwn=1 and isNew<>1)');
	    writeToClob(body_, 'loop');
	    RDX_ACS.fillCompileRightsBody(body_, '', str_list0, exists_Package, 'ind1', 'RDX_AC_USER2ROLE', 'pUser');
	    writeToClob(body_, 'end loop;');
	                                                                        
	    writeToClob(body_, 'for ind1 in (select * from RDX_AC_USER2USERGROUP where UserName=pUser and state<>1)');
	    writeToClob(body_, 'loop');
	    writeToClob(body_, '  for ind2 in (Select * from RDX_AC_USERGROUP2ROLE where GroupName = ind1.GroupName and isNew<>1)');
	    writeToClob(body_, '  loop');   
	    RDX_ACS.fillCompileRightsBody(body_, '  ', str_list0, exists_Package, 'ind2', 'RDX_AC_USERGROUP2ROLE', 'pUser');
	    writeToClob(body_, '  end loop;');    
	    writeToClob(body_, 'end loop;');
	    writeToClob(body_, 'end;');
	    
	    
	    
	    
	    -- procedure compileRightsForGrpBeforeDel
	    
	    /*
	    writeToClob(body_, 'procedure compileRightsForGrpBeforeDel(pUserGroup in varchar2)');
	    writeToClob(body_, 'is');
	    writeToClob(body_, 'begin');
	    writeToClob(body_, 'null;');
	    writeToClob(body_, 'raise_application_error(-20000, ''Not realized yet'');');
	    writeToClob(body_, 'end;');
	    */
	    
	    
	    /*
	    t1 := '      INSERT INTO RDX_AC_USER2ROLE ( userName, isOwn, RoleId, ID';
	    t2 := '               VALUES (ind1.userName, 0, ind3.RoleId, SQN_RDX_AC_USER2ROLEID.NEXTVAL';


	    For i in 1 .. n
	    loop 
	        t1 := t1 || ', P' || str_list(i) || ', M' || str_list(i);
	        t2 := t2 || ', IND3.P' || str_list(i) || ', IND3.M' || str_list(i);
	    null;
	    end loop;

	    x:='procedure compileRightsForGrpBeforeDel(' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);
	    x:='   pUserGroup    in varchar2)' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);
	    x:='is' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);
	    x:='begin' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);
	    x:='RDX_ACS.ClearInheritRights(pUserGroup);' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);
	    x:='for ind1 in (select * from RDX_AC_USER2USERGROUP where GroupName=pUserGroup and state<>1)' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);
	    x:='  loop' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);
	    x:='  for ind2 in (select * from RDX_AC_USER2USERGROUP where UserName=ind1.UserName and GroupName<>pUserGroup and state<>1)' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);
	    x:='      loop' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);
	    x:='      for ind3 in (Select * from RDX_AC_USERGROUP2ROLE where GroupName = ind2.GroupName and isNew<>1)' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);
	    x:='      loop' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);

	    x:='        ' || t1 || ')' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x); 
	    x:='        ' || t2 || ');' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x); 


	    x:='      end loop;' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);
	    x:='  end loop;' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);
	    x:='end loop;' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);
	    x:='end;' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);
	    */


	    for i in 1 .. 2 
	    loop
	       if i = 1  then
	          x:='function buildAssignedAccessAreaU2R(row_ in RDX_AC_USER2ROLE%ROWTYPE)return TRdxAcsArea';
	       else
	          x:='function buildAssignedAccessAreaG2R(row_ in RDX_AC_USERGROUP2ROLE%ROWTYPE)return TRdxAcsArea';
	       end if;
	       writeToClob(body_, x);

	       writeToClob(body_, 'is');
	       writeToClob(body_, 'res TRdxAcsArea;');
	       writeToClob(body_, 'Begin');
	       writeToClob(body_, 'res := TRdxAcsArea(TRdxAcsCoordinates());');
	       
	       for cur_index in 1 .. n 
	       loop 
	            writeToClob(body_, '      if row_.MA$$' || str_list0(cur_index) || '<>RDX_ACS.cRight_unbounded then');
	            writeToClob(body_, '        res.boundaries.EXTEND();');
	            writeToClob(body_, '        if row_.MA$$' || str_list0(cur_index) || ' = RDX_ACS.cRight_prohibited  then');
	            writeToClob(body_, '            res.boundaries(res.boundaries.Count()):=TRdxAcsCoordinate(1, ''apf'  || 
	                               str_list0(cur_index) ||  ''', null);');
	            writeToClob(body_, '        elsif row_.MA$$' || str_list0(cur_index) || ' = RDX_ACS.cRight_boundedByPart then');
	            writeToClob(body_, '            res.boundaries(res.boundaries.Count()):=TRdxAcsCoordinate(0, ''apf'  || 
	                               str_list0(cur_index) ||  ''', row_.PA$$' || str_list0(cur_index) ||');');
	            writeToClob(body_, '        else');
	            writeToClob(body_, '            raise_application_error(-20000, ''Invalid argument'');');
	            writeToClob(body_, '        end if;');
	            writeToClob(body_, '      end if;');
	       end loop;
	       writeToClob(body_, ' return res;');
	       writeToClob(body_, ' end;');
	    end loop;
	    
	    
	    --moveRightsFromUserToGroup
	    writeToClob(body_, 'procedure moveRightsFromUserToGroup(user_ in varchar2, group_ in varchar2)');
	    writeToClob(body_, 'is');
	    writeToClob(body_, 'begin');
	    writeToClob(body_, ' INSERT INTO RDX_AC_USERGROUP (name) VALUES (group_);');
	    writeToClob(body_, ' INSERT INTO RDX_AC_USER2USERGROUP (userName, groupName) VALUES (user_, group_);');
	     
	    writeToClob(body_, '  for ind in (Select * from  RDX_AC_USER2ROLE where userName = User_ and  isOwn = 1 and isNew<>1)');
	    writeToClob(body_, '     loop');
	    
	    x:='';
	    For i in 1 .. n
	    loop 
	        x := x || ', PA$$' || str_list0(i) || ', MA$$' || str_list0(i) || ', PG$$' || str_list0(i);
	    end loop;
	    writeToClob(body_, '      INSERT INTO RDX_AC_USERGROUP2ROLE (ID, GroupName, roleId' || x || ')');
	        
	    x:='';
	    For i in 1 .. n
	    loop 
	        x := x || ', IND.PA$$' || str_list0(i) || ', IND.MA$$' || str_list0(i) || ', IND.PG$$' || str_list0(i);
	    end loop;
	    writeToClob(body_, '      VALUES ( SQN_RDX_AC_USERGROUP2ROLEID.NEXTVAL, group_, IND.roleId' || x || ');');

	    writeToClob(body_, '      DELETE FROM RDX_AC_USER2ROLE WHERE id=ind.id;');
	    
	    writeToClob(body_, '     end loop;');
	    writeToClob(body_, 'end;');
	    
	    ----------------------------
	    
	    writeToClob(body_, 'end;');

	   

	    cursor_name := DBMS_SQL.OPEN_CURSOR;
	    DBMS_SQL.PARSE ( cursor_name, body_, DBMS_SQL.NATIVE );
	    ret := DBMS_SQL.EXECUTE ( cursor_name );
	    DBMS_SQL.CLOSE_CURSOR ( cursor_name );
	  
	end;

	function curUserAllRolesInAllAreas return varchar2
	is
	begin   
	    return RDX_ACS.userAllRolesInAllAreas(RDX_Arte.getUserName());  
	end;

	function userAllRolesInAllAreas(
		pUser in varchar2
	) return varchar2
	is
	    vRole  varchar2(30);
	    vRoles varchar2(4000);
	    Area TRdxAcsArea;
	begin
	    vRoles := '';
	    for ind in (select distinct ROLEID from RDX_AC_USER2ROLE where isNew=0  and isOwn=0 and  username = pUser)
	       loop
	          if vRoles is NULL then
	             vRoles := ind.ROLEID;
	          else
	             vRoles := vRoles || ',' || ind.ROLEID;
	          end if;
	       end loop; 
	    return vRoles;  
	end;

	function getCurUserAllRolesForObject(
		pPointList in TRdxAcsAreaList
	) return varchar2
	is
	begin   
	    return RDX_ACS.getAllRolesForObject(RDX_Arte.getUserName(), pPointList);  
	end;

	function getAllRolesForObject(
		pUser in varchar2,
		pPointList in TRdxAcsAreaList
	) return varchar2
	is
	    vRole  varchar2(30);
	    vRoles varchar2(4000);
	    Area TRdxAcsArea;
	    rightsList TIdRecordList;
	    curIndex integer;
	begin                                
	    vRoles := '';
	    if pPointList is null or pPointList.COUNT=0 then
	    begin             
	    for ind in (select distinct ROLEID from RDX_AC_USER2ROLE where isNew=0  and isOwn=0 and  username = pUser)
	       loop
	           if (RDX_ACS.contentId(ind.ROLEID, rightsList)=0) then

	               curIndex := rightsList.count()+1;
	               rightsList(curIndex).IdValue := ind.ROLEID;


	               if vRoles is NULL then
	                  vRoles := ind.ROLEID;
	               else
	                  vRoles := vRoles || ',' || ind.ROLEID;
	               end if;
	           end if;
	       end loop;
	    end;
	    else
	     begin                 
	     for ind in (select distinct * from RDX_AC_USER2ROLE where isNew=0 and  userName = pUser )
	        loop
	        Area := RDX_ACS_UTILS.buildAssignedAccessAreaU2R(ind);
	        for i in pPointList.FIRST..pPointList.LAST
	           loop
	              if RDX_ACS.containsPointInArea(Area, pPointList(i)) then
	                 begin
	                    if (RDX_ACS.contentId(ind.ROLEID, rightsList)=0) then

	                        curIndex := rightsList.count()+1;
	                        rightsList(curIndex).IdValue := ind.ROLEID;
	                        if vRoles is null then
	                           vRoles := ind.ROLEID;
	                        else
	                           vRoles := vRoles || ',' || ind.ROLEID;
	                        end if;
	                        EXIT;
	                     end if;
	                 end;
	              end if;
	           end loop;
	        end loop;
	     end;
	    end if;
	    return vRoles; 
	end;

	function strToArea(
		str_ in varchar2
	) return TRdxAcsArea
	is
	    pos_ integer;
	begin 
	    pos_:=1; 
	    return RDX_ACS.strToArea(str_, pos_);
	end;

	function strToAreaList(
		str in varchar2
	) return TRdxAcsAreaList
	is
	    list TRdxAcsAreaList;
	    pos_ integer;
	    area TRdxAcsArea;
	begin 
	    pos_:=1;
	    list := TRdxAcsAreaList();
	    if SubStr(str, pos_, 1)<>'(' then
	        RAISE_APPLICATION_ERROR (-20100, RDX_ACS.constErrorMessage || str );
	    end if;
	    pos_:=pos_+1;
	    <<lbl>>while (SubStr(str, pos_, 1)<>')')
	            loop 
	                area :=  RDX_ACS.strToArea(str, pos_);
	                list.extend();
	                list(list.Count()):=area;     
	                pos_:=pos_+1;
	            end loop;                
	    return list;    
	end;

	function userHasRoleForObject(
		pUser in varchar2,
		pRole in varchar2,
		pPointList in varchar2
	) return integer
	is
	begin
	return RDX_ACS.userHasRoleForObject(pUser, pRole,  RDX_ACS.strToAreaList(pPointList));
	end;

	function isCurUserHaveUserRights(
		user_ in varchar2
	) return integer
	is
	begin
	  return RDX_ACS.isUserHaveUserRights(user_, RDX_Arte.getUserName);
	end;

	function isUserHaveGroupRights(
		group_ in varchar2,
		user_ in varchar2
	) return integer
	is
	userArea TRdxAcsArea;
	groupArea TRdxAcsArea;
	flag boolean;

	cSuperAdminRoleId constant varchar2(30) := RDX_ACS.constSuperAdminRoleId;
	  
	begin
	  for g2r_row in (select DISTINCT  * from RDX_AC_USERGROUP2ROLE where isNew=0 and groupname = group_)
	    loop                                                     
	    flag := false;
	    groupArea := RDX_ACS_UTILS.buildAssignedAccessAreaG2R(g2r_row);
	    
	    <<L>>for u2r_row in (select DISTINCT  * from RDX_AC_USER2ROLE U2R where isNew=0 and isOwn=0 and username = user_ and
	                            (g2r_row.roleId = U2R.roleId or U2R.roleId = cSuperAdminRoleId))
	            loop      
	              userArea := RDX_ACS_UTILS.buildAssignedAccessAreaU2R(u2r_row);
	              if RDX_ACS.containsPointInArea2(userArea, groupArea) then
	                 flag := true;
	                 exit L;
	              end if; 
	            end loop; 
	    if not flag then
	      return 0; 
	    end if;
	    end loop;
	  return 1;
	end;

	-- user_ обладает не меньшими правами чем user2_
	function isUserHaveUserRights(
		user2_ in varchar2,
		user_ in varchar2
	) return integer
	is
	userArea TRdxAcsArea;
	lowerUserArea TRdxAcsArea;
	flag boolean;

	cSuperAdminRoleId constant varchar2(30) := RDX_ACS.constSuperAdminRoleId;
	  
	begin
	  for u2r_row1 in (select DISTINCT * from RDX_AC_USER2ROLE where isNew=0 and isOwn=0 and username = user2_)
	    loop                                                     
	    flag := false;
	    lowerUserArea := RDX_ACS_UTILS.buildAssignedAccessAreaU2R(u2r_row1);
	    <<L>>for u2r_row in (select DISTINCT * from RDX_AC_USER2ROLE U2R where isNew=0 and isOwn=0 and username = user_ and
	                            (u2r_row1.roleId = U2R.roleId or U2R.roleId = cSuperAdminRoleId))
	            loop
	              userArea := RDX_ACS_UTILS.buildAssignedAccessAreaU2R(u2r_row);
	              if RDX_ACS.containsPointInArea2(userArea, lowerUserArea) then
	                 flag := true;
	                 exit L;
	              end if; 
	            end loop; 
	    if not flag then
	      return 0; 
	    end if;
	    end loop;
	  return 1;
	end;

	function isCurUserHaveGroupRights(
		group_ in varchar2
	) return integer
	is
	begin
	  return RDX_ACS.isUserHaveGroupRights(group_, RDX_Arte.getUserName);
	end;

	function isGroupHaveRights(
		group_ in varchar2
	) return integer
	is
	rez INTEGER;
	begin
	 rez := 0;
	 SELECT COUNT(*) INTO rez FROM RDX_AC_USERGROUP2ROLE WHERE ROWNUM <= 1 AND groupName = group_;
	 return rez;
	end;

	-- User have own rights
	function isUserHaveOwnRights(
		user_ in varchar2
	) return integer
	is
	rez INTEGER;
	begin
	 rez := 0;
	 SELECT COUNT(*) INTO rez FROM RDX_AC_USER2ROLE WHERE ROWNUM <= 1 AND userName = user_ AND isOwn = 1;
	 return rez;
	end;

	function curUserGroupAdministered(
		pGroup in varchar2
	) return integer
	is
	begin
	 for ind in (Select * from RDX_AC_USER2ROLE
	             where isNew=0 and isOwn=0 and userName = RDX_Arte.getUserName())
	     loop         
	         if ind.MA$$1ZOQHCO35XORDCV2AANE2UAFXA = RDX_ACS.cRight_unbounded or
	            (ind.MA$$1ZOQHCO35XORDCV2AANE2UAFXA = RDX_ACS.cRight_boundedByPart and 
	             ind.PA$$1ZOQHCO35XORDCV2AANE2UAFXA is not null and
	             ind.PA$$1ZOQHCO35XORDCV2AANE2UAFXA = pGroup)                
	          then
	            return 1;
	          end if;
	      end loop;
	 return 0;
	end;

	function curUserNullGroupAdministered return integer
	is
	begin
	 for ind in (Select * from RDX_AC_USER2ROLE
	             where isNew=0 and isOwn=0 and userName = RDX_Arte.getUserName())
	     loop         
	         if ind.MA$$1ZOQHCO35XORDCV2AANE2UAFXA = RDX_ACS.cRight_unbounded or
	            (ind.MA$$1ZOQHCO35XORDCV2AANE2UAFXA = RDX_ACS.cRight_boundedByPart and 
	             ind.PA$$1ZOQHCO35XORDCV2AANE2UAFXA is null)                
	          then
	            return 1;
	          end if;
	      end loop;
	 return 0;
	end;

	function isNewUserOrGroup2Role(
		userTable in integer,
		Id_ in integer
	) return integer
	is
	 rez integer := 0;
	begin
	 if (userTable = 1) then
	    begin
	    select 1 into rez  from dual where exists (Select * from RDX_AC_USER2ROLE where  ID = Id_ and ISNEW <> 0);
	    exception when NO_DATA_FOUND then rez  := 0;
	    end;
	  else
	    begin
	    select 1 into rez  from dual where exists (Select * from RDX_AC_USERGROUP2ROLE where ID = Id_ and ISNEW <> 0);
	    exception when NO_DATA_FOUND then rez  := 0;
	    end;
	  end  if;       
	 return rez;
	end;

	function mayReplaceOrRevokeRole(
		userTable in integer,
		Id_ in integer
	) return integer
	is
	 rez integer := 0;
	begin
	 if (userTable = 1) then
	    begin
	    select 1 into rez  from dual where exists (Select * from RDX_AC_USER2ROLE where ISNEW=0 AND ID = Id_) and  
	                                   not exists (Select * from RDX_AC_USER2ROLE where REPLACEDID = Id_);
	    exception when NO_DATA_FOUND then rez  := 0;
	    end;
	  else
	    begin
	    select 1 into rez  from dual where exists (Select * from RDX_AC_USERGROUP2ROLE where ISNEW=0 AND ID = Id_) and  
	                                   not exists (Select * from RDX_AC_USERGROUP2ROLE where REPLACEDID = Id_);
	    exception when NO_DATA_FOUND then rez  := 0;
	    end;
	  end  if;       
	 return rez;
	end;

	function getNext2RoleId(
		userTable in integer
	) return integer
	is
	begin
	   if (userTable=1) then
	      return SQN_RDX_AC_USER2ROLEID.nextVal;
	   else 
	      return SQN_RDX_AC_USERGROUP2ROLEID.nextVal;
	   end if;  
	end;

	function usedDualControlWhenAssignRoles return integer
	is
	 rez integer := 0;
	begin 
	    begin
	    select RDX_SYSTEM.DUALCONTROLFORASSIGNROLE into rez from RDX_SYSTEM where ID=1;
	    exception when NO_DATA_FOUND then rez  := 0;
	    end;       
	 return rez;
	end;

	procedure acceptRolesAndU2G(
		userTable in integer,
		userOrGroupName in varchar2,
		ignoredRoles out varchar2,
		ignoredUsersOrGroups out varchar2,
		addedRCount out integer,
		replacedRCount out integer,
		removedRCount out integer,
		addedU2GCount out integer,
		removedU2GCount out integer
	)
	-- simple, slow and clear method

	is 

	  curIndex integer;   
	  
	  Type TUser2RoleIdRecord IS RECORD (IdValue RDX_AC_USER2ROLE.ID%Type);
	  Type TUser2RoleIdRecordList is table of TUser2RoleIdRecord index by binary_integer;
	  
	  
	  ignoredRolesList TUser2RoleIdRecordList;
	  
	  addedRolesList TUser2RoleIdRecordList;
	  replacedRolesList TUser2RoleIdRecordList; 
	  removedRolesList TUser2RoleIdRecordList;
	  

	  addedRoles integer;
	  replacedRoles integer;  
	  removedRoles integer;
	  
	  addedU2G integer;  
	  removedU2G integer;
	  
	  flag boolean;
	  

	  Type TUserOrGroupNameRecord IS RECORD (NameValue RDX_AC_USER.NAME%Type);
	  Type TUserOrGroupNameRecordList is table of TUserOrGroupNameRecord index by binary_integer;


	  ignoredUsersOrGroupsList TUserOrGroupNameRecordList;

	  addedUsersOrGroupsList TUserOrGroupNameRecordList;
	  removedUsersOrGroupsList TUserOrGroupNameRecordList;
	  
	  outputBuffer varchar(32767);

	 
	 begin
	  addedRoles := 0;
	  replacedRoles := 0;
	  removedRoles := 0;
	  
	  addedU2G := 0;
	  removedU2G := 0;

	if (userTable=1) then
	 
	 
	        for item in (Select * from RDX_AC_USER2ROLE where 
	                USERNAME = userOrGroupName and 
	                ISNEW = 1 and 
	                ISOWN = 1 
	            )
	            loop
	                --start check current editor
	                if (RDX_Arte.getUserName = item.EDITORNAME) then                    
	                    curIndex := ignoredRolesList.count()+1;
	                    if (item.REPLACEDID is null) then
	                       ignoredRolesList(curIndex).IdValue := item.ID;
	                    else
	                       ignoredRolesList(curIndex).IdValue := item.REPLACEDID;
	                    end if;   
	                    GOTO end_loop;
	                end if;
	                --finish check current editor
	                                
	                --start try add
	                if ((item.REPLACEDID is null) and (item.ROLEID is not null)) then
	                    if (RDX_ACS_UTILS.existsRightsOnUser2Role(RDX_Arte.getUserName(), item.ID)=0) then -- access denied
	                        curIndex := ignoredRolesList.count()+1;
	                        ignoredRolesList(curIndex).IdValue := item.ID;
	                    else
	                        addedRoles := addedRoles + 1;
	                        
	                        curIndex := addedRolesList.count()+1;
	                        addedRolesList(curIndex).IdValue := item.ID;
	                    end if;
	                    GOTO end_loop;
	                end if;
	                --finish try add
	                
	                
	                --start try remove                
	                if ((item.REPLACEDID is not null) and (item.ROLEID is null)) then
	                    if (RDX_ACS_UTILS.existsRightsOnUser2Role(RDX_Arte.getUserName(), item.REPLACEDID)=0) then -- access denied
	                        curIndex := ignoredRolesList.count()+1;
	                        ignoredRolesList(curIndex).IdValue := item.REPLACEDID;
	                    else
	                        removedRoles := removedRoles + 1;
	                    
	                        curIndex := removedRolesList.count()+1;
	                        removedRolesList(curIndex).IdValue := item.REPLACEDID;
	                        
	                        curIndex := removedRolesList.count()+1;
	                        removedRolesList(curIndex).IdValue := item.ID;
	                    end if;
	                    GOTO end_loop;
	                end if;
	                --finish try remove
	                
	                
	                --start try replace
	                if ((item.REPLACEDID is not null) and (item.ROLEID is not null)) then
	                    if (
	                         RDX_ACS_UTILS.existsRightsOnUser2Role(RDX_Arte.getUserName(), item.REPLACEDID)=0
	                         or
	                         RDX_ACS_UTILS.existsRightsOnUser2Role(RDX_Arte.getUserName(), item.ID)=0
	                       ) 
	                    then -- access denied
	                        curIndex := ignoredRolesList.count()+1;
	                        ignoredRolesList(curIndex).IdValue := item.REPLACEDID;
	                    else
	                        replacedRoles := replacedRoles + 1;
	                       
	                        curIndex := replacedRolesList.count()+1;
	                        replacedRolesList(curIndex).IdValue := item.ID;
	                       
	                        curIndex := removedRolesList.count()+1;
	                        removedRolesList(curIndex).IdValue := item.REPLACEDID;
	                        
	                    end if;
	                    GOTO end_loop;
	                end if;                
	                --finish try replace
	                <<end_loop>> 
	                null;
	            end loop;  
	            
	            --start accept
	            if (addedRolesList.first() is not null) then     
	                for i in addedRolesList.first() .. addedRolesList.last()
	                    loop
	                        if (addedRolesList.exists(i)) then
	                            update RDX_AC_USER2ROLE SET ISNEW=0, ACCEPTORNAME=RDX_Arte.getUserName  where 
	                                ID = addedRolesList(i).IdValue;
	                        end if;                    
	                    end loop;
	            end if; 
	            
	            
	            if (replacedRolesList.first() is not null) then
	                for i in replacedRolesList.first() .. replacedRolesList.last()
	                    loop
	                        if (replacedRolesList.exists(i)) then
	                            update RDX_AC_USER2ROLE SET ISNEW=0, ACCEPTORNAME=RDX_Arte.getUserName, REPLACEDID=null  where 
	                                ID = replacedRolesList(i).IdValue;
	                        end if;                    
	                    end loop;            
	            end if; 
	            
	            
	            if (removedRolesList.first() is not null) then     
	                for i in removedRolesList.first() .. removedRolesList.last()
	                    loop
	                        if (removedRolesList.exists(i)) then
	                            delete from RDX_AC_USER2ROLE where ID = removedRolesList(i).IdValue;
	                        end if;                    
	                    end loop;
	             end if; 
	            --finish accept
	            
	            
	            flag := true;
	            outputBuffer := '';
	            if (ignoredRolesList.first() is not null) then     
	                for i in ignoredRolesList.first() .. ignoredRolesList.last()
	                    loop
	                        if (ignoredRolesList.exists(i)) then
	                            if (flag  = true)then
	                               flag := false;
	                            else
	                               outputBuffer := outputBuffer || chr(10);
	                            end if;                        
	                            outputBuffer := outputBuffer || ignoredRolesList(i).IdValue;
	                        end if;                    
	                    end loop;
	             end if; 
	            
	            ignoredRoles := outputBuffer;
	                                    
	            addedRCount := addedRoles;
	            replacedRCount := replacedRoles;
	            removedRCount := removedRoles;
	            
	            
	        for item in (Select * from RDX_AC_USER2USERGROUP where 
	                USERNAME = userOrGroupName and 
	                STATE<>0
	            )
	            loop            
	                --start check current editor
	                if (RDX_Arte.getUserName = item.EDITORNAME) then                    
	                    curIndex := ignoredUsersOrGroupsList.count()+1;
	                    ignoredUsersOrGroupsList(curIndex).NameValue := item.GROUPNAME;
	                    goto end_loop;
	                end if;
	                --finish check current editor
	                
	                --start try add
	                if (item.STATE=1) then
	                    if (RDX_ACS.isCurUserHaveGroupRights(item.GROUPNAME)=0 or 
	                        RDX_ACS.isCurUserHaveUserRights(userOrGroupName)=0
	                       ) then -- access denied
	                    
	                        curIndex := ignoredUsersOrGroupsList.count()+1;
	                        ignoredUsersOrGroupsList(curIndex).NameValue := item.GROUPNAME;
	                    else
	                        addedU2G := addedU2G + 1;
	                        curIndex := addedUsersOrGroupsList.count()+1;
	                        addedUsersOrGroupsList(curIndex).NameValue := item.GROUPNAME;
	                    end if;
	                    goto end_loop;
	                end if;
	                --finish try add                
	                
	                --start try remove
	                if (item.STATE=2) then
	                    if (RDX_ACS.isCurUserHaveGroupRights(item.GROUPNAME)=0 or 
	                        RDX_ACS.isCurUserHaveUserRights(userOrGroupName)=0
	                       ) then -- access denied
	                        curIndex := ignoredUsersOrGroupsList.count()+1;
	                        ignoredUsersOrGroupsList(curIndex).NameValue := item.GROUPNAME;
	                    else
	                        removedU2G := removedU2G + 1;
	                        curIndex := removedUsersOrGroupsList.count()+1;
	                        removedUsersOrGroupsList(curIndex).NameValue := item.GROUPNAME;
	                    end if;
	                    goto end_loop;
	                end if;
	                --finish try replace
	                <<end_loop>> 
	                null;
	            end loop;
	            
	            --start accept
	            if (addedUsersOrGroupsList.first() is not null) then     
	                for i in addedUsersOrGroupsList.first() .. addedUsersOrGroupsList.last()
	                    loop
	                        if (addedUsersOrGroupsList.exists(i)) then
	                            update RDX_AC_USER2USERGROUP SET STATE=0, ACCEPTORNAME=RDX_Arte.getUserName  where 
	                                USERNAME = userOrGroupName and GROUPNAME = addedUsersOrGroupsList(i).NameValue;
	                        end if;                    
	                    end loop;
	            end if;
	            
	            if (removedUsersOrGroupsList.first() is not null) then
	                for i in removedUsersOrGroupsList.first() .. removedUsersOrGroupsList.last()
	                    loop
	                        if (removedUsersOrGroupsList.exists(i)) then
	                            delete from RDX_AC_USER2USERGROUP where USERNAME = userOrGroupName and GROUPNAME = removedUsersOrGroupsList(i).NameValue;
	                        end if;                    
	                    end loop;            
	            end if;
	            --finish accept  
	            
	            flag := true;
	            outputBuffer := '';
	            if (ignoredUsersOrGroupsList.first() is not null) then     
	                for i in ignoredUsersOrGroupsList.first() .. ignoredUsersOrGroupsList.last()
	                    loop
	                        if (ignoredUsersOrGroupsList.exists(i)) then
	                            if (flag  = true)then
	                               flag := false;
	                            else
	                               outputBuffer := outputBuffer || chr(10);
	                            end if;                        
	                            outputBuffer := outputBuffer || ignoredUsersOrGroupsList(i).NameValue;
	                        end if;                    
	                    end loop;
	             end if; 
	            
	            ignoredUsersOrGroups := outputBuffer;
	                                    
	            addedU2GCount := addedU2G;
	            removedU2GCount := removedU2G;            
	    RDX_ACS_UTILS.compileRightsForUser(userOrGroupName);
	    
	else --groups
	--   
	--
	--
	null;


	        for item in (Select * from RDX_AC_USERGROUP2ROLE where 
	                GROUPNAME = userOrGroupName and 
	                ISNEW = 1
	            )
	            loop
	                --start check current editor
	                if (RDX_Arte.getUserName = item.EDITORNAME) then                    
	                    curIndex := ignoredRolesList.count()+1;
	                    if (item.REPLACEDID is null) then
	                       ignoredRolesList(curIndex).IdValue := item.ID;
	                    else
	                       ignoredRolesList(curIndex).IdValue := item.REPLACEDID;
	                    end if;   
	                    goto end_loop;
	                end if;
	                --finish check current editor
	                     
	                --start try add
	                if ((item.REPLACEDID is null) and (item.ROLEID is not null)) then
	                    if (RDX_ACS_UTILS.existsRightsOnGroup2Role(RDX_Arte.getUserName(), item.ID)=0) then -- access denied
	                        curIndex := ignoredRolesList.count()+1;
	                        ignoredRolesList(curIndex).IdValue := item.ID;
	                    else
	                        addedRoles := addedRoles + 1;
	                        
	                        curIndex := addedRolesList.count()+1;
	                        addedRolesList(curIndex).IdValue := item.ID;
	                    end if;
	                    goto end_loop;
	                end if;
	                --finish try add
	                
	                                
	                --start try remove                
	                if ((item.REPLACEDID is not null) and (item.ROLEID is null)) then
	                    if (RDX_ACS_UTILS.existsRightsOnGroup2Role(RDX_Arte.getUserName(), item.REPLACEDID)=0) then -- access denied
	                        curIndex := ignoredRolesList.count()+1;
	                        ignoredRolesList(curIndex).IdValue := item.REPLACEDID;
	                    else
	                        removedRoles := removedRoles + 1;
	                    
	                        curIndex := removedRolesList.count()+1;
	                        removedRolesList(curIndex).IdValue := item.REPLACEDID;
	                        
	                        curIndex := removedRolesList.count()+1;
	                        removedRolesList(curIndex).IdValue := item.ID;
	                    end if;
	                    goto end_loop;
	                end if;
	                --finish try remove
	                
	                 
	                
	                --start try replace
	                if ((item.REPLACEDID is not null) and (item.ROLEID is not null)) then
	                    if (
	                         RDX_ACS_UTILS.existsRightsOnGroup2Role(RDX_Arte.getUserName(), item.REPLACEDID)=0
	                         or
	                         RDX_ACS_UTILS.existsRightsOnGroup2Role(RDX_Arte.getUserName(), item.ID)=0
	                       ) 
	                    then -- access denied
	                        curIndex := ignoredRolesList.count()+1;
	                        ignoredRolesList(curIndex).IdValue := item.REPLACEDID;
	                    else
	                        replacedRoles := replacedRoles + 1;
	                       
	                        curIndex := replacedRolesList.count()+1;
	                        replacedRolesList(curIndex).IdValue := item.ID;
	                       
	                        curIndex := removedRolesList.count()+1;
	                        removedRolesList(curIndex).IdValue := item.REPLACEDID;
	                        
	                    end if;
	                    goto end_loop;
	                end if;
	                <<end_loop>> 
	                null;
	                --finish try replace
	            end loop;  
	            
	            --start accept
	            if (addedRolesList.first() is not null) then     
	                for i in addedRolesList.first() .. addedRolesList.last()
	                    loop
	                        if (addedRolesList.exists(i)) then
	                            update RDX_AC_USERGROUP2ROLE SET ISNEW=0, ACCEPTORNAME=RDX_Arte.getUserName  where 
	                                ID = addedRolesList(i).IdValue;
	                        end if;                    
	                    end loop;
	            end if; 
	            
	            
	            if (replacedRolesList.first() is not null) then
	                for i in replacedRolesList.first() .. replacedRolesList.last()
	                    loop
	                        if (replacedRolesList.exists(i)) then
	                            update RDX_AC_USERGROUP2ROLE SET ISNEW=0, ACCEPTORNAME=RDX_Arte.getUserName, REPLACEDID=null  where 
	                                ID = replacedRolesList(i).IdValue;
	                        end if;                    
	                    end loop;            
	            end if; 
	            
	            
	            if (removedRolesList.first() is not null) then     
	                for i in removedRolesList.first() .. removedRolesList.last()
	                    loop
	                        if (removedRolesList.exists(i)) then
	                            delete from RDX_AC_USERGROUP2ROLE where ID = removedRolesList(i).IdValue;
	                        end if;                    
	                    end loop;
	             end if; 
	            --finish accept
	            
	            
	            flag := true;
	            outputBuffer := '';
	            if (ignoredRolesList.first() is not null) then     
	                for i in ignoredRolesList.first() .. ignoredRolesList.last()
	                    loop
	                        if (ignoredRolesList.exists(i)) then
	                            if (flag  = true)then
	                               flag := false;
	                            else
	                               outputBuffer := outputBuffer || chr(10);
	                            end if;                        
	                            outputBuffer := outputBuffer || ignoredRolesList(i).IdValue;
	                        end if;                    
	                    end loop;
	             end if; 
	            
	            ignoredRoles := outputBuffer;
	                                    
	            addedRCount := addedRoles;
	            replacedRCount := replacedRoles;
	            removedRCount := removedRoles;
	            
	            
	            
	        for item in (Select * from RDX_AC_USER2USERGROUP where 
	                GROUPNAME = userOrGroupName and 
	                STATE<>0
	            )
	            loop            
	                --start check current editor
	                if (RDX_Arte.getUserName = item.EDITORNAME) then                    
	                    curIndex := ignoredUsersOrGroupsList.count()+1;
	                    ignoredUsersOrGroupsList(curIndex).NameValue := item.USERNAME;
	                    goto end_loop;
	                end if;
	                --finish check current editor
	                
	                --start try add
	                if (item.STATE=1) then
	                    if (RDX_ACS.isCurUserHaveUserRights(item.USERNAME)=0 or 
	                        RDX_ACS.isCurUserHaveGroupRights(userOrGroupName)=0
	                       ) then -- access denied
	                    
	                        curIndex := ignoredUsersOrGroupsList.count()+1;
	                        ignoredUsersOrGroupsList(curIndex).NameValue := item.USERNAME;
	                    else
	                        addedU2G := addedU2G + 1;
	                        curIndex := addedUsersOrGroupsList.count()+1;
	                        addedUsersOrGroupsList(curIndex).NameValue := item.USERNAME;
	                    end if;
	                    goto end_loop;
	                end if;
	                --finish try add                
	                
	                --start try remove
	                if (item.STATE=2) then
	                    if (RDX_ACS.isCurUserHaveUserRights(item.USERNAME)=0 or 
	                        RDX_ACS.isCurUserHaveGroupRights(userOrGroupName)=0
	                       ) then -- access denied
	                        curIndex := ignoredUsersOrGroupsList.count()+1;
	                        ignoredUsersOrGroupsList(curIndex).NameValue := item.USERNAME;
	                    else
	                        removedU2G := removedU2G + 1;
	                        curIndex := removedUsersOrGroupsList.count()+1;
	                        removedUsersOrGroupsList(curIndex).NameValue := item.USERNAME;
	                    end if;
	                    goto end_loop;
	                end if;
	                --finish try remove  
	                <<end_loop>> 
	                null;              
	            end loop;
	            
	            --start accept
	            if (addedUsersOrGroupsList.first() is not null) then     
	                for i in addedUsersOrGroupsList.first() .. addedUsersOrGroupsList.last()
	                    loop
	                        if (addedUsersOrGroupsList.exists(i)) then
	                            update RDX_AC_USER2USERGROUP SET STATE=0, ACCEPTORNAME=RDX_Arte.getUserName  where 
	                                GROUPNAME = userOrGroupName and USERNAME = addedUsersOrGroupsList(i).NameValue;
	                                null;
	                        end if;                    
	                    end loop;
	            end if;
	            
	            if (removedUsersOrGroupsList.first() is not null) then
	                for i in removedUsersOrGroupsList.first() .. removedUsersOrGroupsList.last()
	                    loop
	                        if (removedUsersOrGroupsList.exists(i)) then
	                            delete from RDX_AC_USER2USERGROUP where GROUPNAME = userOrGroupName and USERNAME = removedUsersOrGroupsList(i).NameValue;
	                        end if;                    
	                    end loop;            
	            end if;
	            --finish accept  
	            
	            flag := true;
	            outputBuffer := '';
	            if (ignoredUsersOrGroupsList.first() is not null) then     
	                for i in ignoredUsersOrGroupsList.first() .. ignoredUsersOrGroupsList.last()
	                    loop
	                        if (ignoredUsersOrGroupsList.exists(i)) then
	                            if (flag  = true)then
	                               flag := false;
	                            else
	                               outputBuffer := outputBuffer || chr(10);
	                            end if;                        
	                            outputBuffer := outputBuffer || ignoredUsersOrGroupsList(i).NameValue;
	                        end if;                    
	                    end loop;
	             end if; 
	            
	            ignoredUsersOrGroups := outputBuffer;
	                                    
	            addedU2GCount := addedU2G;
	            removedU2GCount := removedU2G; 
	        RDX_ACS_UTILS.compileRightsForGroup(userOrGroupName);            


	end if;


	  
	   
	end;

	procedure getRolesAndU2GCount(
		userTable in integer,
		userOrGroupName in varchar2,
		acceptedRCount out integer,
		unacceptedRCount out integer,
		acceptedU2GCount out integer,
		unacceptedU2GCount out integer
	)
	is
	begin
	  if (userTable = 1) then
	  
	    Select count (*) into unacceptedRCount from RDX_AC_USER2ROLE where USERNAME = userOrGroupName and RDX_Arte.getUserName = EDITORNAME and ISNEW=1;
	    Select count (*) into acceptedRCount from RDX_AC_USER2ROLE where USERNAME = userOrGroupName and RDX_Arte.getUserName <> EDITORNAME and ISNEW=1;
	    
	    Select count (*) into unacceptedU2GCount from RDX_AC_USER2USERGROUP where USERNAME = userOrGroupName and RDX_Arte.getUserName = EDITORNAME and STATE<>0;
	    Select count (*) into acceptedU2GCount from RDX_AC_USER2USERGROUP where USERNAME = userOrGroupName and RDX_Arte.getUserName <> EDITORNAME and STATE<>0;
	    
	  else
	  
	    Select count (*) into unacceptedRCount from RDX_AC_USERGROUP2ROLE where GROUPNAME = userOrGroupName and RDX_Arte.getUserName = EDITORNAME and ISNEW=1;
	    Select count (*) into acceptedRCount from RDX_AC_USERGROUP2ROLE where GROUPNAME = userOrGroupName and RDX_Arte.getUserName <> EDITORNAME and ISNEW=1;
	    
	    Select count (*) into unacceptedU2GCount from RDX_AC_USER2USERGROUP where GROUPNAME = userOrGroupName and RDX_Arte.getUserName = EDITORNAME and STATE<>0;
	    Select count (*) into acceptedU2GCount from RDX_AC_USER2USERGROUP where GROUPNAME = userOrGroupName and RDX_Arte.getUserName <> EDITORNAME and STATE<>0;    
	    
	  end if;  
	end;

	procedure getNotAcceptedEntities(
		user2UserGroupCount out integer,
		firstUser2UserGroup out varchar2,
		userGroup2RoleCount out integer,
		firstUserGroup2Role out varchar2,
		user2RoleCount out integer,
		firstUser2Role out varchar2
	)
	is
	 rez  integer;
	 buff varchar2(200); 
	begin
	    rez:=0;

	    SELECT COUNT(*) INTO rez FROM RDX_AC_USER2USERGROUP WHERE ROWNUM <= 1 AND STATE<>0;
	    user2UserGroupCount:=rez;
	     
	    if (rez<>0) then
	      SELECT USERNAME || chr(10) || GROUPNAME INTO firstUser2UserGroup FROM RDX_AC_USER2USERGROUP WHERE ROWNUM = 1 AND STATE<>0;
	    end if;
	   
	   
	    SELECT COUNT(*) INTO rez FROM RDX_AC_USERGROUP2ROLE WHERE ROWNUM <= 1 AND ISNEW<>0;
	    userGroup2RoleCount := rez;
	   
	    if (rez<>0) then
	        SELECT GROUPNAME  || chr(10) || ROLEID INTO firstUserGroup2Role FROM RDX_AC_USERGROUP2ROLE WHERE ROWNUM = 1 AND ISNEW<>0;      
	    end if;


	    SELECT COUNT(*) INTO rez FROM RDX_AC_USER2ROLE WHERE ROWNUM <= 1 AND ISNEW<>0;
	    user2RoleCount := rez;    
	    
	    
	    if (rez<>0) then
	        SELECT USERNAME  || chr(10) || ROLEID INTO firstUser2Role FROM RDX_AC_USER2ROLE WHERE ROWNUM = 1 AND ISNEW<>0;        
	    end if;
	end;

	function haveNotAcceptedEntities return integer
	is
	rez integer;
	begin
	    rez:=0;

	    SELECT COUNT(*) INTO rez FROM RDX_AC_USER2USERGROUP WHERE ROWNUM <= 1 AND STATE<>0;
	    if (rez<>0) then
	      return 1;
	    end if;

	    SELECT COUNT(*) INTO rez FROM RDX_AC_USERGROUP2ROLE WHERE ROWNUM <= 1 AND ISNEW<>0;
	    if (rez<>0) then
	      return 1;
	    end if;
	    
	    SELECT COUNT(*) INTO rez FROM RDX_AC_USER2ROLE WHERE ROWNUM <= 1 AND ISNEW<>0;
	    if (rez<>0) then
	      return 1;
	    end if;
	    
	    return 0;
	    
	end;

	function equalPartition(
		part1 in varchar2,
		part2 in varchar2
	) return integer deterministic
	is
	begin
	 if (part1 is null) and (part2 is null) then 
	  return 1;
	 end if;
	 if (part1 is not null) and (part2 is not null) and (part1 = part2) then 
	  return 1;
	 end if;
	 return 0; 
	end;

	function readPartitions(
		partitionGroupId in integer
	) return clob
	is
	 rez clob;
	begin
	 select PARTITIONS into rez from RDX_AC_PARTITIONGROUP where ID = partitionGroupId;
	 return rez;
	exception 
	 when NO_DATA_FOUND then
	 return null; 
	end;
end;
/



declare
 count_ INTEGER;
begin
select count(*) into count_ from rdx_ddsVersion where layerUri = 'com.optt';
if count_ <> 1 then -- ignoring com.optt
  RDX_ACS.AcsUtilsBuild(); 
end if;
end;
/



grant execute on RDX_ACS_UTILS to &USER&_RUN_ROLE
/

begin
  RDX_ACS_Utils.compileRights();
end;
/

