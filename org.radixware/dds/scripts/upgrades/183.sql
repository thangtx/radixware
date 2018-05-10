create or replace package RDX_Array as

	Type ARR_STR IS TABLE OF VARCHAR2(32767);
	Type ARR_CLOB IS TABLE OF CLOB;

	function merge(
		e1 in varchar2
	) return CLOB;

	function merge(
		e1 in varchar2,
		e2 in varchar2
	) return CLOB;

	function merge(
		e1 in varchar2,
		e2 in varchar2,
		e3 in varchar2
	) return CLOB;

	function merge(
		e1 in varchar2,
		e2 in varchar2,
		e3 in varchar2,
		e4 in varchar2
	) return CLOB;

	function merge(
		e1 in number
	) return CLOB;

	function merge(
		e1 in number,
		e2 in number
	) return CLOB;

	function merge(
		e1 in number,
		e2 in number,
		e3 in number
	) return CLOB;

	function fromStr(
		lob in clob
	) return ARR_STR deterministic;
	PRAGMA RESTRICT_REFERENCES (fromStr, WNDS);

	function fromArrStr(
		arr_ in ARR_STR
	) return clob;

	procedure appendStr(
		res in out clob,
		x in varchar2
	);

	procedure appendNum(
		res in out clob,
		x in number
	);

	procedure appendDate(
		res in out clob,
		x in timestamp
	);

	procedure appendRef(
		res in out clob,
		tableGuid in varchar2,
		x in varchar2
	);

	procedure insertStr(
		res in out clob,
		x in varchar2,
		index_ in integer
	);

	procedure insertNum(
		res in out clob,
		x in number,
		index_ in integer
	);

	procedure insertDate(
		res in out clob,
		x in timestamp,
		index_ in integer
	);

	procedure insertRef(
		res in out clob,
		tableGuid in varchar2,
		x in varchar2,
		index_ in integer
	);

	function getStr(
		lob in clob,
		index_ in integer
	) return varchar2 deterministic;
	PRAGMA RESTRICT_REFERENCES (getStr, WNDS);

	function getNum(
		lob in clob,
		index_ in integer
	) return number deterministic;
	PRAGMA RESTRICT_REFERENCES (getNum, WNDS);

	function getDate(
		lob in clob,
		index_ in integer
	) return timestamp deterministic;
	PRAGMA RESTRICT_REFERENCES (getDate, WNDS);

	function searchStr(
		lob in clob,
		what in varchar2,
		startIdx in integer
	) return number deterministic;
	PRAGMA RESTRICT_REFERENCES (searchStr, WNDS);

	function searchNum(
		lob in clob,
		what in number,
		startIdx in number
	) return number deterministic;
	PRAGMA RESTRICT_REFERENCES (searchNum, WNDS);

	function searchDate(
		lob in clob,
		what in timestamp,
		startIdx in number
	) return number deterministic;
	PRAGMA RESTRICT_REFERENCES (searchDate, WNDS);

	function searchRef(
		lob in clob,
		tableGuid in varchar2,
		what in varchar2,
		startIdx in integer
	) return number deterministic;
	PRAGMA RESTRICT_REFERENCES (searchRef, WNDS);

	function getArraySize(
		lob in clob
	) return integer;
	PRAGMA RESTRICT_REFERENCES (getArraySize, RNDS);
	PRAGMA RESTRICT_REFERENCES (getArraySize, RNPS);
	PRAGMA RESTRICT_REFERENCES (getArraySize, WNDS);
	PRAGMA RESTRICT_REFERENCES (getArraySize, WNPS);

	procedure setStr(
		lob in out clob,
		x in varchar2,
		Idx in number
	);

	procedure setNum(
		lob in out clob,
		x in number,
		Idx in number
	);

	procedure setDate(
		lob in out clob,
		x in timestamp,
		idx in number
	);

	procedure setRef(
		lob in out clob,
		tableGuid in varchar2,
		x in varchar2,
		idx in number
	);

	procedure remove(
		res in out clob,
		index_ in integer
	);

	procedure removeAll(
		res in out clob,
		val in varchar2
	);

	function createRef(
		tableGuid in varchar2,
		x in varchar2
	) return varchar2 deterministic;
	PRAGMA RESTRICT_REFERENCES (createRef, WNDS);

	function search(
		lob in clob,
		what in varchar2,
		startIdx in integer,
		asNumber in boolean
	) return number deterministic;
	PRAGMA RESTRICT_REFERENCES (search, WNDS);

	function getIntersection(
		lob1 in clob,
		lob2 in clob
	) return clob;

	function getUnion(
		lob1 in clob,
		lob2 in clob
	) return clob;

	function toStrTable(
		-- Array content to split
		lob in clob,
		-- Soft mode for array parsing:
		-- - 0 - raise exceptions for any errors
		-- - 1 - ignore extra elements in the array structure
		softMode in integer := 0,
		-- Length to crop for array content. Need be in the range 0..4000. 0 means no cropping
		cropLength in integer := 0
	) return RDX_STR_TABLE deterministic;

	function fromStrToArrClob(
		lob in clob
	) return ARR_CLOB deterministic;
	PRAGMA RESTRICT_REFERENCES (fromStrToArrClob, WNDS);
end;
/

grant execute on RDX_Array to &USER&_RUN_ROLE
/

create or replace package body RDX_Array as

	function createRef(
		tableGuid in varchar2,
		x in varchar2
	) return varchar2 deterministic
	is
	begin
	  return tableGuid || chr(10) || x;
	end;

	function prepareClobArr(
		pSize in integer
	) return CLOB
	is
	  res CLOB; 
	begin
	  dbms_lob.createTemporary(res, true); 
	  dbms_lob.append(res,to_char(pSize));
	  return res;
	end;

	function getArraySize(
		lob in clob
	) return integer
	is
	   pos integer;
	begin
	   if (lob is null) or (DBMS_LOB.GETLENGTH(lob) < 1) then
	       return 0;
	   end if;   
	   pos := INSTR(lob, '['); 
	   if (pos < 2) then
	      return 0;
	   end if;   
	   return TO_NUMBER(SUBSTR(lob, 1, pos-1));
	end;

	function search(
		lob in clob,
		what in varchar2,
		startIdx in integer,
		asNumber in boolean
	) return number deterministic
	is
	 arrSize integer;
	 ind_    integer;
	 pos1 integer;
	 pos2 integer;
	 len integer;
	 whatLen integer;
	 temp varchar2(30);
	 item VARCHAR2(32767);
	 tempWhat number; 
	 idx number;
	begin
	 idx := greatest(1, startIdx);
	 IF asNumber THEN
	   BEGIN		  
	     tempWhat :=  Rdx_Valasstr.numFromValAsStr(what);
	     EXCEPTION WHEN VALUE_ERROR THEN		
	       RETURN 0 ;
	   END;
	 END IF;

	 arrSize := RDX_Array.getArraySize(lob);
	 if (idx>arrSize)then
	  return 0;
	 end if;
	 whatLen := LENGTH(what);

	 pos1 := LENGTH(arrSize) + 1;

	 for  ind_ in 1 ..  idx-1  loop
	    pos2  := INSTR(lob, ']', pos1);

	    temp := SUBSTR(lob, pos1+1, pos2-pos1-1);
	    if (temp is not null and LENGTH(temp) > 0) then
	       len := TO_NUMBER(temp);
	     else  
	       len := 0;
	    end if;
	    pos1 := pos2 + len + 1;        
	 end loop;
	 
	 for  ind_ in idx .. arrSize loop
	    pos2  := INSTR(lob, ']', pos1);

	    temp := SUBSTR(lob, pos1+1, pos2-pos1-1);
	    if (temp is not null and LENGTH(temp) > 0) then
	       len := TO_NUMBER(temp);
	    else
	       len := 0;
	    end if;    
	    item := SUBSTR(lob, pos2+1, len);
	    IF (what IS NULL AND item IS NULL ) THEN     
	       return ind_;    
	    end if;    
	    IF (what IS not NULL and item IS not NULL ) THEN     
	         if (asNumber) then         
	            BEGIN            
	             IF (RDX_ValAsStr.numFromValAsStr(item) = tempWhat) THEN     
	               return ind_;    
	             end if;
	             EXCEPTION WHEN VALUE_ERROR THEN
	               NULL;
	             END;
	          else
	             IF (whatLen = len AND item = what) THEN     
	               return ind_;    
	             end if;          
	          end if;
	    end if;     
	    pos1 := pos2 + len + 1;        
	 end loop;
	 return 0;
	end;

	procedure incrementHeader(
		res in out clob,
		oldHeaderSize in integer
	)
	is
	  newItemCount varchar2(30);
	  newItemCountSize integer;
	  oldItemCountSize integer;
	  moveSize integer;
	  tempClob clob;
	  oldSize integer;

	begin
	     newItemCount := to_char(oldHeaderSize + 1);
	     newItemCountSize := LENGTH(newItemCount );
	     oldItemCountSize := LENGTH(to_char(oldHeaderSize));
	     if (oldItemCountSize <> newItemCountSize) then
	           -- slow branch
	           dbms_lob.createTemporary(tempClob, true); 
	           oldSize := DBMS_LOB.GETLENGTH(res);

	           moveSize := oldSize - oldItemCountSize;
	           dbms_lob.copy(tempClob, res, moveSize, 1, oldItemCountSize+1);
	           dbms_lob.copy(res, tempClob, moveSize, newItemCountSize+1, 1);
	     end if;
	     dbms_lob.WRITE (res, newItemCountSize ,1, newItemCount);
	end;

	procedure decrementHeader(
		res in out clob,
		oldHeaderSize in integer
	)
	is
	  newItemCount varchar2(30);
	  newItemCountSize integer;
	  oldItemCountSize integer;
	  moveSize integer;
	  tempClob clob;
	  oldSize integer;

	begin
	     newItemCount := to_char(oldHeaderSize - 1);
	     newItemCountSize := LENGTH(newItemCount );
	     oldItemCountSize := LENGTH(to_char(oldHeaderSize));
	     if (oldItemCountSize <> newItemCountSize) then
	           -- slow branch
	           dbms_lob.createTemporary(tempClob, true); 
	           oldSize := DBMS_LOB.GETLENGTH(res);

	           moveSize := oldSize - oldItemCountSize;
	           dbms_lob.copy(tempClob, res, moveSize, 1, oldItemCountSize+1);
	           dbms_lob.copy(res, tempClob, moveSize, newItemCountSize+1, 1);

	       dbms_lob.trim(res, oldSize-1); 
	 end if;
	     dbms_lob.WRITE (res, newItemCountSize ,1, newItemCount);
	end;

	procedure appendWithoutHeaderModifying(
		res in out clob,
		x in varchar2
	)
	is
	len_ integer;
	begin
	  len_ := length(x);
	  dbms_lob.append(res,'[');
	  if (x is not null) and (len_>0)  then 
		  dbms_lob.append(res,to_char(len_));
		  dbms_lob.append(res,']');
		  dbms_lob.append(res,x);
	  else
		  dbms_lob.append(res,'0]');
	  end if;
	end;

	procedure insertWithoutHeaderModifing(
		rez in out clob,
		pos_ in integer,
		x in varchar2
	)
	is
	len_     integer;
	oldSize  integer;
	tempClob clob;
	begin
	 len_ := length(x);
	 oldSize:=DBMS_LOB.GETLENGTH(rez);
	 dbms_lob.createTemporary(tempClob, true);
	 
	 DBMS_LOB.copy(tempClob, rez, oldSize, 1, 1);
	 
	 DBMS_LOB.copy(rez, tempClob, pos_-1, 1, 1);
	 DBMS_LOB.write(rez, len_, pos_,  x);
	 DBMS_LOB.copy(rez, tempClob, oldSize - pos_ + 1, pos_+ len_, pos_ );
	 
	end;

	function merge(
		e1 in varchar2
	) return CLOB
	is
	  res CLOB; 
	begin
	  res:=RDX_Array.prepareClobArr(1);  
	  RDX_Array.appendWithoutHeaderModifying(res,e1);
	  return res;
	end;

	function merge(
		e1 in number,
		e2 in number
	) return CLOB
	is
	  res CLOB; 
	begin
	  res:=RDX_Array.prepareClobArr(2);
	  RDX_Array.appendWithoutHeaderModifying(res,to_char(e1));
	  RDX_Array.appendWithoutHeaderModifying(res,to_char(e2));
	  return res;
	end;

	function merge(
		e1 in varchar2,
		e2 in varchar2
	) return CLOB
	is
	  res CLOB; 
	begin
	  res:=RDX_Array.prepareClobArr(2);
	  RDX_Array.appendWithoutHeaderModifying(res,e1);
	  RDX_Array.appendWithoutHeaderModifying(res,e2);
	  return res;
	end;

	function merge(
		e1 in number,
		e2 in number,
		e3 in number
	) return CLOB
	is
	  res CLOB; 
	begin
	  res:=RDX_Array.prepareClobArr(3);
	  RDX_Array.appendWithoutHeaderModifying(res,to_char(e1));
	  RDX_Array.appendWithoutHeaderModifying(res,to_char(e2));
	  RDX_Array.appendWithoutHeaderModifying(res,to_char(e3));
	  return res;
	end;

	function merge(
		e1 in varchar2,
		e2 in varchar2,
		e3 in varchar2
	) return CLOB
	is
	  res CLOB; 
	begin
	  res:=RDX_Array.prepareClobArr(3);
	  RDX_Array.appendWithoutHeaderModifying(res,e1);
	  RDX_Array.appendWithoutHeaderModifying(res,e2);
	  RDX_Array.appendWithoutHeaderModifying(res,e3);
	  return res;
	end;

	function merge(
		e1 in varchar2,
		e2 in varchar2,
		e3 in varchar2,
		e4 in varchar2
	) return CLOB
	is
	  res CLOB; 
	begin
	  res:=RDX_Array.prepareClobArr(4);
	  RDX_Array.appendWithoutHeaderModifying(res,e1);
	  RDX_Array.appendWithoutHeaderModifying(res,e2);
	  RDX_Array.appendWithoutHeaderModifying(res,e3);
	  RDX_Array.appendWithoutHeaderModifying(res,e4);
	  return res;
	end;

	function merge(
		e1 in number
	) return CLOB
	is
	  res CLOB; 
	begin
	  res:=RDX_Array.prepareClobArr(1);  
	  RDX_Array.appendWithoutHeaderModifying(res,to_char(e1));
	  return res;
	end;

	function fromStr(
		lob in clob
	) return ARR_STR deterministic
	is
	   arr ARR_STR := ARR_STR();
	   item VARCHAR2(32767);
	   temp VARCHAR2(30);
	   
	   pos1   INTEGER;
	   pos2   INTEGER;
	   len    INTEGER;   
	   ind_   INTEGER; 
	   size_  INTEGER; 
	begin
	   if (lob is null) or (DBMS_LOB.GETLENGTH(lob) < 2) then
	       return arr;
	   end if;
	   
	   pos1 := INSTR(lob, '[');
	   
	   if (pos1<2) then
	       return arr;
	   end if;   
	   
	   pos2  := INSTR(lob, ']', pos1);
	   
	   temp  := SUBSTR(lob, 1, pos1-1);
	   

	   
	   size_:= TO_NUMBER(temp);
	   
	   arr.Extend(size_);
	   
	   FOR ind_ IN 1 .. size_ 
	    loop        
	       len := -1;
	       temp := SUBSTR(lob, pos1+1, pos2-pos1-1);
	       if (temp is not null and LENGTH(temp) > 0) then
	           len := TO_NUMBER(temp);
	       else
	           len := 0;
	       end if;
	       
	       item := null;
	       if (len > -1) then
	           item := SUBSTR(lob, pos2+1, len);
	       end if;       
	 

	       pos1 := pos2 + len + 1;
	       pos2 := INSTR(lob, ']', pos1);

	       arr(ind_) := item;       
	   end loop;
	   
	   return arr;
	end;

	function fromArrStr(
		arr_ in ARR_STR
	) return clob
	is
	   res clob;
	   size_ integer;
	   temp VARCHAR2(30);
	begin
	 dbms_lob.createTemporary(res, true); 
	 if (arr_ is null) then
	   return res;
	 end if;
	 size_ := arr_.count();
	 temp := to_char(size_);
	 DBMS_LOB.writeappend(res, length(temp), temp);
	 if size_<>0 then
	    FOR element# IN arr_.FIRST() .. arr_.LAST() 
	       LOOP 
	          IF (arr_.EXISTS(element#))
	          THEN 
	             size_ := length(arr_(element#));             
	             if ((size_ is null) or (size_ = 0)) then
	                DBMS_LOB.writeappend(res, 3, '[0]');
	             else
	                temp := to_char(size_);
	                DBMS_LOB.writeappend(res, 1, '[');
	                DBMS_LOB.writeappend(res, length(temp), temp);
	                DBMS_LOB.writeappend(res, 1, ']');
	                DBMS_LOB.writeappend(res, size_, arr_(element#));         
	             end if;
	             
	          END IF;
	       END LOOP; 
	end if;

	return res;
	end;

	procedure appendStr(
		res in out clob,
		x in varchar2
	)
	is
	 arrSize integer;
	  
	begin
	 
	 arrSize := RDX_Array.getArraySize(res);
	 
	 RDX_Array.incrementHeader(res, arrSize);
	 
	 RDX_Array.appendWithoutHeaderModifying(res, x);     
	end;

	procedure appendNum(
		res in out clob,
		x in number
	)
	is
	begin
	    RDX_Array.appendStr(res, RDX_ValAsStr.numToValAsStr(x));
	end;

	procedure appendDate(
		res in out clob,
		x in timestamp
	)
	is
	begin
	    RDX_Array.appendStr(res, RDX_ValAsStr.dateTimeToValAsStr(x));
	end;

	procedure appendRef(
		res in out clob,
		tableGuid in varchar2,
		x in varchar2
	)
	is
	begin
	    RDX_Array.appendStr(res, RDX_Array.createRef(tableGuid, x));
	end;

	procedure insertStr(
		res in out clob,
		x in varchar2,
		index_ in integer
	)
	is
	 arrSize integer;
	 ind_    integer;
	 pos1 integer;
	 pos2 integer;
	 len integer;
	 temp varchar2(30);
	begin
	arrSize := RDX_Array.getArraySize(res);

	if index_ > arrSize then
	     RDX_Array.appendStr(res, x);
	else
	     RDX_Array.incrementHeader(res, arrSize);
	     arrSize:=arrSize+1;
	     
	     
	     pos1 := LENGTH(arrSize) + 1;
	     for  ind_ in 1 ..  index_-1  loop
	        pos2  := INSTR(res, ']', pos1);
	        
	        --len := 0;
	        temp := SUBSTR(res, pos1+1, pos2-pos1-1);
	        if (temp is not null and LENGTH(temp) > 0) then
	           len := TO_NUMBER(temp);
	        else
	           len := 0;
	        end if;
	      
	        pos1 := pos2 + len + 1;        
	     end loop;
	     if (x is null) then
	         RDX_Array.insertWithoutHeaderModifing(res, pos1, '[0]');
	     else
	         RDX_Array.insertWithoutHeaderModifing(res, pos1,'[' || to_char(LENGTH(x)) || ']' || x);
	     end if;
	     
	end if; 
	end;

	procedure insertNum(
		res in out clob,
		x in number,
		index_ in integer
	)
	is
	begin
	  RDX_Array.insertStr(res, RDX_ValAsStr.numToValAsStr(x), index_);
	end;

	procedure insertDate(
		res in out clob,
		x in timestamp,
		index_ in integer
	)
	is
	begin
	  RDX_Array.insertStr(res, RDX_ValAsStr.dateTimeToValAsStr(x), index_);
	end;

	procedure insertRef(
		res in out clob,
		tableGuid in varchar2,
		x in varchar2,
		index_ in integer
	)
	is
	begin
	  RDX_Array.insertStr(res, RDX_Array.createRef(tableGuid, x), index_);
	end;

	function getStr(
		lob in clob,
		index_ in integer
	) return varchar2 deterministic
	is
	 arrSize integer;
	 ind_    integer;
	 pos1 integer;
	 pos2 integer;
	 len integer;
	 temp varchar2(30);
	begin

	 arrSize := RDX_Array.getArraySize(lob);
	 if (index_>arrSize or index_<1)then
	  return null;
	 end if;


	 pos1 := LENGTH(arrSize) + 1;

	 for  ind_ in 1 ..  index_-1  loop
	    pos2  := INSTR(lob, ']', pos1);

	    temp := SUBSTR(lob, pos1+1, pos2-pos1-1);
	    if (temp is not null and LENGTH(temp) > 0) then
	       len := TO_NUMBER(temp);
	    else   
	       len := 0;
	    end if;

	    pos1 := pos2 + len + 1;        
	 end loop;

	 pos2  := INSTR(lob, ']', pos1);
	 temp := SUBSTR(lob, pos1+1, pos2-pos1-1);
	 if (temp is not null and LENGTH(temp) > 0) then
	     len := TO_NUMBER(temp);
	 else    
	     len := 0;
	 end if;

	 return SUBSTR(lob, pos2+1, len);
	  
	end;

	function getNum(
		lob in clob,
		index_ in integer
	) return number deterministic
	is
	begin
	 return RDX_ValAsStr.numFromValAsStr(RDX_Array.getStr(lob, index_));
	end;

	function getDate(
		lob in clob,
		index_ in integer
	) return timestamp deterministic
	is
	begin
	 return RDX_ValAsStr.dateTimeFromValAsStr(RDX_Array.getStr(lob, index_));
	end;

	function searchStr(
		lob in clob,
		what in varchar2,
		startIdx in integer
	) return number deterministic
	is
	begin
	 return RDX_Array.search(lob, what, startIdx, false);
	end;

	function searchNum(
		lob in clob,
		what in number,
		startIdx in number
	) return number deterministic
	is
	begin 
	 return RDX_Array.search(lob, what, startIdx, true);
	end;

	function searchDate(
		lob in clob,
		what in timestamp,
		startIdx in number
	) return number deterministic
	is
	begin
	  return RDX_Array.search(lob, RDX_ValAsStr.dateTimeToValAsStr(what), startIdx, false);
	end;

	function searchRef(
		lob in clob,
		tableGuid in varchar2,
		what in varchar2,
		startIdx in integer
	) return number deterministic
	is
	begin
	 return RDX_Array.search(lob, RDX_Array.createRef(tableGuid, what), startIdx, false);
	end;

	procedure setStr(
		lob in out clob,
		x in varchar2,
		Idx in number
	)
	is
	 allClobSize INTEGER;
	 arrSize INTEGER;
	 ind_    INTEGER;
	 pos1 INTEGER;
	 pos2 INTEGER;
	 len INTEGER;
	 newLen INTEGER;
	 tempPos INTEGER;
	 temp VARCHAR2(30);
	 tempPos2 INTEGER;
	 moveSize INTEGER;
	tempClob CLOB; 

	BEGIN
	 
	 arrSize := Rdx_Array.getArraySize(LOB);
	 IF (idx > arrSize) THEN
	   RETURN;
	 END IF;
	 
	 allClobSize:=DBMS_LOB.GETLENGTH(LOB);   
	 pos1 := LENGTH(arrSize) + 1;

	 FOR  ind_ IN 1 ..  idx-1  LOOP
	    pos2  := INSTR(LOB, ']', pos1);
	    temp := SUBSTR(LOB, pos1+1, pos2-pos1-1);
	    IF (temp IS NOT NULL AND LENGTH(temp) > 0) THEN
	       len := TO_NUMBER(temp);
	    else
	       len := 0;
	    END IF;

	    pos1 := pos2 + len + 1;        
	 END LOOP;


	 
	 IF x IS NULL THEN
	    newLen := 0;
	 ELSE
	    newLen := LENGTH(x);
	 END IF;
	 
	 pos2  := INSTR(LOB, ']', pos1);
	 temp := SUBSTR(LOB, pos1+1, pos2-pos1-1);
	 IF (temp IS NOT NULL AND LENGTH(temp) > 0) THEN
	     len := TO_NUMBER(temp);
	 ELSE
	     len := 0; 
	 END IF; 
	 
	 IF len = newLen THEN
	   IF len > 0 THEN
	     DBMS_LOB.WRITE(LOB, len, pos2+1,  x);
	   END IF;
	 ELSE   
	    temp := TO_CHAR(newLen);
	    tempPos2 := LENGTH(temp);
	    
	   IF (arrSize <> idx) THEN
	      dbms_lob.createTemporary(tempClob, TRUE);
	      tempPos := pos2 + len + 1;
		  moveSize := allClobSize - (tempPos) +2;
	      dbms_lob.copy(tempClob, LOB,  moveSize, 1, tempPos);
	         

	      dbms_lob. WRITE(LOB, tempPos2, pos1+1, temp);
	      dbms_lob.WRITE(LOB, 1, pos1+1+tempPos2, ']');
		  IF (newLen <> 0 )THEN
	        dbms_lob.WRITE(LOB, newLen, pos1+2+tempPos2, x);
		  END IF;
		   

	      dbms_lob. copy(LOB, tempClob, allClobSize - (tempPos) +2 ,  pos1 + 2 + tempPos2 + newLen, 1);
		  IF (newlen<len)THEN
	        dbms_lob.trim(LOB, newLen + pos1+ tempPos2 + moveSize);
	      END IF;  

	   ELSE 
	      dbms_lob. WRITE(LOB, tempPos2, pos1+1 , temp);
	      dbms_lob.WRITE(LOB, 1, pos1 +tempPos2+1, ']');
	      if x is not null then
	         dbms_lob.WRITE(LOB, newLen, pos1+2+tempPos2, x);
	      end if;      
	      IF (newLen < len) THEN
	        dbms_lob.trim(LOB, newLen + pos1+1+tempPos2);
	      END IF;  
	        
	   END IF;
	 END IF; 
	END;

	procedure setNum(
		lob in out clob,
		x in number,
		Idx in number
	)
	is
	begin
	  RDX_Array.setStr(lob, RDX_ValAsStr.numToValAsStr(x), idx);
	end;

	procedure setDate(
		lob in out clob,
		x in timestamp,
		idx in number
	)
	is
	begin
	 RDX_Array.setStr(lob, RDX_ValAsStr.dateTimeToValAsStr(x), idx);
	end;

	procedure setRef(
		lob in out clob,
		tableGuid in varchar2,
		x in varchar2,
		idx in number
	)
	is
	begin
	  RDX_Array.setStr(lob, RDX_Array.createRef(tableGuid, x), idx);
	end;

	procedure remove(
		res in out clob,
		index_ in integer
	)
	is
	  allClobSize INTEGER;
	 arrSize INTEGER;
	 ind_    INTEGER;
	 pos1 INTEGER;
	 pos2 INTEGER;
	 len INTEGER;
	 preffixLen integer; 
	 temp VARCHAR2(30); 
	 tempClob CLOB;
	 tailLen integer;  
	BEGIN 
	 arrSize := Rdx_Array.getArraySize(res);
	 IF (index_ > arrSize) THEN
	    RETURN;
	 END IF;
	     
	 preffixLen := length(to_char(arrSize-1));     
	decrementHeader(res, arrSize);
	 allClobSize:=DBMS_LOB.GETLENGTH(res);

	 pos1 := preffixLen + 1;

	 FOR  ind_ IN 1 ..  index_-1  LOOP
	    pos2  := INSTR(res, ']', pos1);
	    temp := SUBSTR(res, pos1+1, pos2-pos1-1);
	    IF (temp IS NOT NULL AND LENGTH(temp) > 0) THEN
	       len := TO_NUMBER(temp);
	    END IF;
	    pos1 := pos2 + len + 1;        
	 END LOOP;

	IF (arrSize = index_) THEN
	    dbms_lob.trim(res, pos1-1);

	else
	     pos2  := INSTR(res, ']', pos1);
	     temp := SUBSTR(res, pos1+1, pos2-pos1-1);
	     IF (temp IS NOT NULL AND LENGTH(temp) > 0) THEN
	         len := TO_NUMBER(temp);
	     ELSE
	         len := 0; 
	     END IF; 
	     len := len + pos2-pos1 + 1;
	     dbms_lob.createTemporary(tempClob, TRUE);
	     dbms_lob.copy(tempClob, res,  pos1 - 1, 1, 1);
	     tailLen := allClobSize - (pos1 + len - 1);     
	     dbms_lob.copy(tempClob, res,  tailLen,pos1,  pos1 + len );
	     dbms_lob.copy(res,  tempClob, pos1 + len);
	     dbms_lob.trim(res, allClobSize - len);
	end if;
	END;

	procedure removeAll(
		res in out clob,
		val in varchar2
	)
	IS
	   arrSize       INTEGER;
	   preffixLen    INTEGER;
	   tempStr       VARCHAR2 (32767);
	   pos1          INTEGER;
	   pos2          INTEGER;
	   temp          VARCHAR2 (30);
	   len           INTEGER;
	   removeCount   INTEGER;

	   TYPE ArrInt IS TABLE OF INTEGER
	                     INDEX BY BINARY_INTEGER;

	   fromArr       ArrInt;
	   lenArr        ArrInt;

	   ind_          INTEGER;
	   tempClob      CLOB;
	   flag          BOOLEAN;
	BEGIN
	   removeCount := 0;

	   arrSize := Rdx_Array.getArraySize (res);
	   preffixLen := LENGTH (TO_CHAR (arrSize));

	   ind_ := 1;

	   pos1 := preffixLen + 1;

	   fromArr (ind_) := pos1;

	   FOR i_ IN 1 .. arrSize
	   LOOP
	      pos2 := INSTR (res, ']', pos1);
	      temp := SUBSTR (res, pos1 + 1, pos2 - pos1 - 1);
	      flag := TRUE;

	      IF (temp IS NOT NULL AND LENGTH (temp) > 0)
	      THEN
	         len := TO_NUMBER (temp);
	      else   
	         len := 0;
	      END IF;   

	     tempStr := DBMS_LOB.SUBSTR (res, len, pos2 + 1);

	     IF ( (val IS NULL AND tempStr IS NULL)
	         OR (val IS NOT NULL AND tempStr IS NOT NULL AND tempStr = val))
	     THEN
	        lenArr (ind_) := pos1 - fromArr (ind_);
	        ind_ := ind_ + 1;
	        fromArr (ind_) := pos2 + len + 1;
	        removeCount := removeCount + 1;
	        flag := FALSE;
	     END IF;
	      

	      pos1 := pos2 + len + 1;
	   END LOOP;

	   IF removeCount = 0
	   THEN
	      RETURN;
	   END IF;

	   IF (flag)
	   THEN
	      lenArr (ind_) := pos1 - fromArr (ind_ - 1);
	   ELSE
	      lenArr (ind_) := 0;
	   END IF;

	   DBMS_LOB.createTemporary (tempClob, TRUE);

	   temp := TO_CHAR (arrSize - removeCount);
	   DBMS_LOB.append (tempClob, temp);
	   pos1 := LENGTH (temp) + 1;
	 
	   FOR i_ IN 1 .. ind_
	   LOOP
	      IF lenArr (i_) <> 0
	      THEN
	         DBMS_LOB.COPY (tempClob,
	                        res,
	                        lenArr (i_),
	                        pos1,
	                        fromArr (i_));
	         pos1 := pos1 + lenArr (i_);
	      END IF;
	   END LOOP;

	   pos1 := DBMS_LOB.GETLENGTH (tempClob);
	   DBMS_LOB.COPY (res, tempClob, pos1);
	   DBMS_LOB.TRIM (res, pos1);

	END;

	function getIntersection(
		lob1 in clob,
		lob2 in clob
	) return clob
	is
	    res clob;
	    arr ARR_STR;
	begin
	    dbms_lob.createTemporary(res, true);
	    if lob1 is not null then 
	        arr := RDX_Array.fromStr(lob1);
	        for i in arr.first .. arr.last loop
	            if RDX_Array.search(lob2, arr(i), 1, false) > 0 then
	                RDX_Array.appendStr(res, arr(i));
	            end if;
	        end loop;
	    end if;
	    return res;
	end;

	function getUnion(
		lob1 in clob,
		lob2 in clob
	) return clob
	is
	    res clob;
	    arr ARR_STR;
	begin
	    if lob2 is not null then
	        res := lob2;
	    else
	        dbms_lob.createTemporary(res, true);
	    end if;  
	    if lob1 is not null then 
	        arr := RDX_Array.fromStr(lob1);
	        for i in arr.first .. arr.last loop
	            if RDX_Array.search(res, arr(i), 1, false) = 0 then
	                RDX_Array.appendStr(res, arr(i));
	            end if;
	        end loop;
	    end if;
	    return res;
	end;

	function toStrTable(
		-- Array content to split
		lob in clob,
		-- Soft mode for array parsing:
		-- - 0 - raise exceptions for any errors
		-- - 1 - ignore extra elements in the array structure
		softMode in integer := 0,
		-- Length to crop for array content. Need be in the range 0..4000. 0 means no cropping
		cropLength in integer := 0
	) return RDX_STR_TABLE deterministic
	is
	  pos integer;
	  b integer;
	  e integer;
	  len integer;
	  croppedLen integer;
	  i integer;
	  sz integer;
	  res RDX_STR_TABLE;
	begin
	  res := RDX_STR_TABLE();
	  
	  if lob is null then
	    return res;
	  elsif softMode not in (0, 1) then
	    RAISE_APPLICATION_ERROR (-20100, 'illegal softMode parameter value ('||to_char(softMode)||')! Only 0 and 1 are valid');
	  elsif not cropLength between 0 and 4000 then
	    RAISE_APPLICATION_ERROR (-20100, 'illegal cropLength parameter value ('||to_char(cropLength)||')! Only 0..4000 is valid');
	  end if;

	  sz := Rdx_Array.getArraySize(lob);
	  if (sz <= 0) then 
	    return res; 
	  end if;

	  pos := 1;
	  i := 1;
	  res.extend(sz);

	  loop
	    b := instr(lob, '[', pos);
	    e := instr(lob, ']', pos);

	    if (b = 0) then 
	        if (i - 1 < sz) then
	          RAISE_APPLICATION_ERROR (-20100, 'illegal array content! Array contains less elements ('||to_char(i-1)||') than were declared in the array header ('||to_char(sz)||')');
	        elsif e > 0 then 
	          RAISE_APPLICATION_ERROR (-20100, 'illegal array content! Unpaired brackets at the '||to_char(i-1)||'-th element of the array content');
	        else
	          return res; 
	        end if;
	    end if;
	    
	    if e = 0 then
	        RAISE_APPLICATION_ERROR (-20100, 'illegal array content! Unpaired brackets at the '||to_char(i)||'-th element of the array content');
	    elsif e < b then
	        RAISE_APPLICATION_ERROR (-20100, 'illegal array content! Unpaired brackets at the '||to_char(i)||'-th element of the array content');
	    else 
	      len := to_number(substr(lob, b+1, e-b-1));
	      
	      if cropLength = 0 then
	        if len > 4000 then
	          RAISE_APPLICATION_ERROR (-20100, 'illegal array content! The '||to_char(i)||'-th element of array is more than 4000 chars');
	        else
	          croppedLen := len;
	        end if;
	      elsif len < cropLength then
	        croppedLen := len;
	      else 
	        croppedLen := cropLength;
	      end if;
	      
	      pos := 1 + e + len;
	      
	      if (i > sz) then 
	        if softMode = 1 then
	            return res; 
	        else
	            RAISE_APPLICATION_ERROR (-20100, 'illegal array content! Array contains more elements ('||to_char(i)||') than were declared in the array header ('||to_char(sz)||')');
	        end if;
	      end if;
	      res(i) := substr(lob, e+1, croppedLen);
	      i := i + 1;
	    end if;
	  end loop;
	-- test set:
	-- select * from table(Rdx_Array.toStrTable('2[3]100[2]AS',0,0)) -- 2 lines: 100, AS
	-- select * from table(Rdx_Array.toStrTable('3[3]100[2]AS',0,0)) -- exception (less than)
	-- select * from table(Rdx_Array.toStrTable('1[3]100[2]AS',0,0)) -- exception (more than)
	-- select * from table(Rdx_Array.toStrTable('3[3]100[2]AS',1,0)) -- exception (less than)
	-- select * from table(Rdx_Array.toStrTable('1[3]100[2]AS',1,0)) -- 1 line: 100

	-- select * from table(Rdx_Array.toStrTable('2[3]100[2]AS',0,2)) -- 2 lines: 10, AS
	-- select * from table(Rdx_Array.toStrTable('2[3]100[2]AS',100,0)) -- exception (illegal softmode)
	-- select * from table(Rdx_Array.toStrTable('2[3]100[2]AS',1,-2)) -- exception (illegal cropLength)
	-- select * from table(Rdx_Array.toStrTable('2[3]100[2AS',1,0)) -- exception (unpaired '[')
	-- select * from table(Rdx_Array.toStrTable('2[3]100[2AS',0,0)) -- exception (unpaired '[')
	-- select * from table(Rdx_Array.toStrTable('2[3]100[2]AS3]',0,0)) -- exception (unpaired ']')
	end;

	function fromStrToArrClob(
		lob in clob
	) return ARR_CLOB deterministic
	is
	   arr ARR_CLOB := ARR_CLOB();
	   item clob;
	   temp VARCHAR2(30);
	   
	   pos1   INTEGER;
	   pos2   INTEGER;
	   len    INTEGER;   
	   ind_   INTEGER; 
	   size_  INTEGER; 
	begin
	   if (lob is null) or (DBMS_LOB.GETLENGTH(lob) < 2) then
	       return arr;
	   end if;
	   
	   pos1 := INSTR(lob, '[');
	   
	   if (pos1<2) then
	       return arr;
	   end if;   
	   
	   pos2  := INSTR(lob, ']', pos1);
	   
	   temp  := SUBSTR(lob, 1, pos1-1);
	   

	   
	   size_:= TO_NUMBER(temp);
	   
	   arr.Extend(size_);
	   
	   FOR ind_ IN 1 .. size_ 
	    loop        
	       len := -1;
	       temp := SUBSTR(lob, pos1+1, pos2-pos1-1);
	       if (temp is not null and LENGTH(temp) > 0) then
	           len := TO_NUMBER(temp);
	       else
	           len := 0;
	       end if;
	       
	       item := null;
	       if (len > -1) then
	           item := SUBSTR(lob, pos2+1, len);
	       end if;       
	 

	       pos1 := pos2 + len + 1;
	       pos2 := INSTR(lob, ']', pos1);

	       arr(ind_) := item;       
	   end loop;
	   
	   return arr;
	end;
end;
/

create or replace package body RDX_Trace as

	function put_internal(
		pCode in varchar2,
		pWords in clob,
		pComponent in varchar2,
		pSeverity in number,
		pContextTypes in varchar2,
		pContextIds in varchar2,
		pTime in timestamp := NULL,
		pUserName in varchar2 := NULL,
		pStationName in varchar2 := NULL,
		pIsSensitive in integer := 0
	) return integer
	is
	   vId  integer;                          
	   time TIMESTAMP;
	   sensTraceOn  integer;
	   pt  integer;
	   pt2 integer;
	   pi  integer;
	   pi2 integer;
	   ct  varchar2(250);
	   ci  varchar2(250);                                   
	   eDublicated         exception;
	   PRAGMA EXCEPTION_INIT(eDublicated,-1);  -- ORA-00001 unique constraint
	   PRAGMA AUTONOMOUS_TRANSACTION;
	begin
	    if pIsSensitive != 0 then
	        select ENABLESENSITIVETRACE into sensTraceOn from RDX_SYSTEM where ID = 1;
	        if sensTraceOn = 0 then 
	            commit;    
	            return null;
	        end if;    
	    end if;
	   select SQN_RDX_EVENTLOGID.NextVal into vId from DUAL;
	   if pTime is null then
	       time:=SYSTIMESTAMP;
	   else   
	       time := pTime;              
	   end if;
	   insert into RDX_EVENTLOG (RAISETIME, ID, CODE, WORDS, SEVERITY, COMPONENT, USERNAME, STATIONNAME, ISSENSITIVE)
	                 values (time, vId, pCode, pWords, pSeverity, pComponent, pUserName, pStationName, pIsSensitive);
	   pt:=1; pi:=1;
	   loop
	      exit when pContextTypes is null or pt>=length(pContextTypes) or
	                pContextIds is null or pi >= length(pContextIds);
	      pt2:=instr(pContextTypes,chr(0),pt);
	      ct:=substr(pContextTypes,pt,pt2-pt);
	      pi2:=instr(pContextIds,chr(0),pi); 
	      if (pi2 > pi) then
	         ci:=substr(pContextIds,pi,pi2-pi);
	      else
	         ci:='-';
	      end if;
	      pt:=pt2+1;  pi:=pi2+1;      
	      begin
	          insert into RDX_EVENTCONTEXT (TYPE, ID, RAISETIME, EVENTID)
	                            values (ct,ci,time,vId);
	      exception
	          when eDublicated then continue;
	      end;                      
	   end loop;   
	   commit;
	   return vId;
	exception
	   when OTHERS then
	      rollback;
	      return NULL;
	end;

	procedure put(
		pSeverity in integer,
		pCode in varchar2,
		pMess in varchar2,
		pSource in varchar2,
		pIsSensitive in integer := 0
	)
	is
	begin
	  RDX_Trace.put_1word(pSeverity => pSeverity, pCode => pCode, pWord1 => pMess, pSource => pSource, pIsSensitive => pIsSensitive);
	end;

	procedure put(
		pSeverity in integer,
		pMess in varchar2,
		pSource in varchar2,
		pIsSensitive in integer := 0
	)
	is
	begin
	  RDX_Trace.put(pSeverity => pSeverity, pCode => NULL, pMess => pMess, pSource => pSource, pIsSensitive => pIsSensitive); 
	end;

	procedure put_1word(
		pSeverity in integer,
		pCode in varchar2,
		pWord1 in varchar2,
		pSource in varchar2,
		pIsSensitive in integer := 0
	)
	is
	  vId integer;
	begin
	  vId := RDX_Trace.put_internal(pCode, RDX_Array.merge(e1 => pWord1), pSource, pSeverity, null, null, null, null, null, pIsSensitive); 
	end;

	procedure put_2word(
		pSeverity in integer,
		pCode in varchar2,
		pWord1 in varchar2,
		pWord2 in varchar2,
		pSource in varchar2,
		pIsSensitive in integer := 0
	)
	is
	  vId integer;
	begin
	  vId := RDX_Trace.put_internal(pCode, RDX_Array.merge(e1 => pWord1, e2 => pWord2), pSource, pSeverity, null, null, null, null, null, pIsSensitive); 
	end;

	procedure put_3word(
		pSeverity in integer,
		pCode in varchar2,
		pWord1 in varchar2,
		pWord2 in varchar2,
		pWord3 in varchar2,
		pSource in varchar2,
		pIsSensitive in integer := 0
	)
	is
	  vId integer;
	begin
	  vId := RDX_Trace.put_internal(pCode, RDX_Array.merge(e1 => pWord1, e2 => pWord2, e3 => pWord3), pSource, pSeverity, null, null, null, null, null, pIsSensitive); 
	end;

	procedure put_4word(
		pSeverity in integer,
		pCode in varchar2,
		pWord1 in varchar2,
		pWord2 in varchar2,
		pWord3 in varchar2,
		pWord4 in varchar2,
		pSource in varchar2,
		pIsSensitive in integer := 0
	)
	is
	  vId integer;
	begin
	  vId := RDX_Trace.put_internal(pCode, RDX_Array.merge(e1 => pWord1, e2 => pWord2, e3 => pWord3, e4 => pWord4), pSource, pSeverity, null, null, null, null, null, pIsSensitive); 
	end;

	procedure deleteDebug
	is
	begin
	   loop  
	      delete from RDX_EVENTLOG where RDX_EVENTLOG.SEVERITY = 0 and ROWNUM <= 1000;
	      exit when SQL%NOTFOUND;
	      commit;
	   end loop;
	end;

	procedure clearSensitiveData(
		pForce in boolean := false
	)
	is
	   isOn integer default 0;
	begin
	   if not pForce then
	       select ENABLESENSITIVETRACE
	         into isOn 
	         from RDX_SYSTEM where ID=1;
	   end if;  
	   if isOn = 0 then
	       loop  
	          update /*+ index(RDX_EVENTLOG IDX_RDX_EVENTLOG_HASSENSITIVE) */  RDX_EVENTLOG set WORDS = null where ISSENSITIVE > 0 and WORDS is not null and ROWNUM <= 10000;
	          exit when SQL%NOTFOUND;
	          commit;
	       end loop;
	   end if;    
	end;

	procedure createPartition(
		d in date
	)
	is
	begin
	  commit;
	  insert into RDX_EVENTLOG(RAISETIME, ID, WORDS, SEVERITY, COMPONENT) values (d, SQN_RDX_EVENTLOGID.NextVal, 'x',0,'x');
	  insert into RDX_EVENTCONTEXT(RAISETIME, EVENTID, TYPE, ID) values (d, SQN_RDX_EVENTLOGID.CurrVal, 'x','x');
	  rollback;
	end;

	procedure dropEventPartitionsOlderThan(
		d in date
	)
	is
	 NOT_EXIST exception;   PRAGMA EXCEPTION_INIT(NOT_EXIST, -2149);
	 THE_ONLY exception;    PRAGMA EXCEPTION_INIT(THE_ONLY, -14083);
	 THE_LAST exception;    PRAGMA EXCEPTION_INIT(THE_LAST, -14758);
	 upperExclusiveBoundary date;
	begin
	    for r in (select *
	              from   all_tab_partitions tp
	              where  (tp.table_name = 'RDX_EVENTLOG' or tp.table_name = 'RDX_EVENTCONTEXT')
	              and    tp.table_owner = (SYS_CONTEXT('USERENV', 'CURRENT_SCHEMA')))
	    loop
	        execute immediate 'SELECT ' || r.high_value || ' from dual'
	            into upperExclusiveBoundary;
	        if upperExclusiveBoundary <= d then
	            if upper(r.partition_name) not like '%' || upper('RDX_EVENTLOG') || '%' and  upper(r.partition_name) not like '%' || upper('RDX_EVENTCONTEXT') || '%' then
	                begin                                      
	                    execute immediate 'alter table ' || r.table_owner || '.' || r.table_name || ' drop partition ' ||
	                                  r.partition_name;
	                    RDX_Trace.put(0,  'Partition of ' || r.table_name || ' for less than ' || r.high_value ||' dropped', 'App.Db');
	                exception when NOT_EXIST or THE_ONLY or THE_LAST then null;
	                end;
	            END IF;
	        END IF;
	    END LOOP;
	end;

	procedure maintenance
	as
	  d date; 
	  res boolean;
	begin
	  --удаление устаревших разделов
	  select trunc(sysdate)-RDX_SYSTEM.EVENTSTOREDAYS into d from RDX_SYSTEM where id=1;
	  #IF DB_TYPE == "ORACLE" AND isEnabled("org.radixware\\Partitioning") THEN
	  RDX_Trace.dropEventPartitionsOlderThan(d);   
	  --подготовка будущих разделов
	  RDX_Trace.createPartition(sysdate+1);
	  RDX_Trace.createPartition(sysdate+2);
	  #ELSE
	  loop  
	     delete from RDX_EVENTCONTEXT where RDX_EVENTCONTEXT.RAISETIME < d and ROWNUM < 10000;
	     exit when SQL%NOTFOUND;
	     commit;
	  end loop;
	  loop  
	     delete from RDX_EVENTLOG where RDX_EVENTLOG.RAISETIME < d and ROWNUM < 10000;
	     exit when SQL%NOTFOUND;
	     commit;
	  end loop;
	  #ENDIF
	end;

	function put_records(
		pRecords in RDX_EVENT_LOG_RECORDS
	) return integer
	is
	  rec RDX_EVENT_LOG_RECORD;
	  vId integer := null;  
	begin
	  if pRecords is null then return null; end if;
	  for idx in 1 .. pRecords.count
	  loop
	    rec := pRecords(idx);
	    vId := rdx_trace.put_internal(rec.code, rec.words, rec.component, rec.severity, rec.contextTypes, rec.contextIds, rec.time, rec.userName, rec.stationName, rec.isSensitive);
	  end loop;
	  return vId;
	end;

	function translate(
		pBundleId in varchar2,
		pStringId in varchar2,
		pLanguage in varchar2,
		pWords in clob
	) return clob
	is
	    version integer := RDX_Arte.getVersion();
	    wordsArrStr RDX_Array.ARR_STR := null;
	    wordsArrClob RDX_Array.ARR_CLOB := null;
	    template varchar2(32767);
	    result clob := null;
	    word clob := null;
	    i integer := 1;
	    j integer;
	    wi integer;
	begin
	    begin
	        wordsArrStr := RDX_Array.fromStr(pWords);
	    exception
	        when OTHERS then
	            wordsArrClob := RDX_Array.fromStrToArrClob(pWords);
	    end;
	            
	    if pBundleId is NULL or pStringId is NULL then
	        if wordsArrStr is not NULL and wordsArrStr.count > 0 then
	            return wordsArrStr(1);
	        elsif wordsArrClob is not NULL and wordsArrClob.count > 0 then
	            return wordsArrClob(1);    
	        end if;
	        return null;
	    else
	        select STRINGVALUE into template from RDX_EVENTCODEMLS
	        where VERSIONNUM = version and BUNDLEID = pBundleId and STRINGID = pStringId and LANGUAGE = pLanguage;
	        
	        while i <= length(template) loop
	            j := instr(template, '%', i);
	            if j = 0 then
	                j := length(template) + 1;
	            end if;
	            result := result || substr(template, i, j - i);

	            if j < length(template) and substr(template, j + 1, 1) in ('1', '2', '3', '4', '5', '6', '7', '8', '9') then
	                wi := to_number(substr(template, j + 1, 1));
	                if wordsArrStr is not null and wordsArrStr.count >= wi then
	                    word := wordsArrStr(wi);
	                elsif wordsArrClob is not null and wordsArrClob.count >= wi then
	                    word := wordsArrClob(wi);
	                else
	                    word := 'null';
	                end if;
	                result := result || word;
	                i := j + 2;
	            elsif j <= length(template) then
	                result := result || '%';
	                i := j + 1;
	            else
	                i := j + 1;
	            end if;
	        end loop;
	    end if;
	    return result;
	exception
	    when NO_DATA_FOUND then
	        return '??? (code=' || pBundleId || '-' || pStringId || ', args = ' || pWords || ')';
	end;

	function translate(
		pQualifiedId in varchar2,
		pLanguage in varchar2,
		pWords in clob
	) return clob
	is
	    delimPos integer := instr(pQualifiedId, '-');
	    bundleId varchar2(100) := substr(pQualifiedId, 1, delimPos - 1);
	    stringId varchar2(100) := substr(pQualifiedId, delimPos + 1);
	    result clob := RDX_Trace.translate(bundleId, stringId, pLanguage, pWords);
	begin
	    return result;
	end;
end;
/

