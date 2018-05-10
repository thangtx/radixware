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

	-- Нумерация осуществляется с единицы
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

	-- Нумерация осуществляется с единицы
	procedure insertNum(
		res in out clob,
		x in number,
		index_ in integer
	)
	is
	begin
	  RDX_Array.insertStr(res, RDX_ValAsStr.numToValAsStr(x), index_);
	end;

	-- Нумерация осуществляется с единицы
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

	-- Нумерация осуществляется с единицы
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

	-- Нумерация осуществляется с единицы
	function getNum(
		lob in clob,
		index_ in integer
	) return number deterministic
	is
	begin
	 return RDX_ValAsStr.numFromValAsStr(RDX_Array.getStr(lob, index_));
	end;

	-- Нумерация осуществляется с единицы
	function getDate(
		lob in clob,
		index_ in integer
	) return timestamp deterministic
	is
	begin
	 return RDX_ValAsStr.dateTimeFromValAsStr(RDX_Array.getStr(lob, index_));
	end;

	-- Нумерация осуществляется с единицы
	function searchStr(
		lob in clob,
		what in varchar2,
		startIdx in integer
	) return number deterministic
	is
	begin
	 return RDX_Array.search(lob, what, startIdx, false);
	end;

	-- Нумерация осуществляется с единицы
	function searchNum(
		lob in clob,
		what in number,
		startIdx in number
	) return number deterministic
	is
	begin 
	 return RDX_Array.search(lob, what, startIdx, true);
	end;

	-- Нумерация осуществляется с единицы
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

	-- Нумерация осуществляется с единицы
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

	-- Нумерация осуществляется с единицы
	procedure setNum(
		lob in out clob,
		x in number,
		Idx in number
	)
	is
	begin
	  RDX_Array.setStr(lob, RDX_ValAsStr.numToValAsStr(x), idx);
	end;

	-- Нумерация осуществляется с единицы
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

	function getIntersect(
		lob1 in clob,
		lob2 in clob
	) return clob
	is
	    res clob;
	    arr ARR_STR;
	begin
	    dbms_lob.createTemporary(res, true);
	    arr := RDX_Array.fromStr(lob1);
	    for i in arr.first .. arr.last loop
	        if RDX_Array.search(lob2, arr(i), 1, false) > 0 then
	            RDX_Array.appendStr(res, arr(i));
	        end if;
	    end loop;
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
	    res := lob2; 
	    arr := RDX_Array.fromStr(lob1);
	    for i in arr.first .. arr.last loop
	        if RDX_Array.search(res, arr(i), 1, false) = 0 then
	            RDX_Array.appendStr(res, arr(i));
	        end if;
	    end loop;
	    return res;
	end;
end;
/

