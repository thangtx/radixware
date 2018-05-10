/*
* Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.kernel.common.client.types;

import java.util.Arrays;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Arr;
import org.radixware.kernel.common.types.ArrStr;

public class ArrPid extends Arr<Pid>{
    
    static final long serialVersionUID = 6770760802002569367L;
    
    private static class PidAsStrParser implements Arr.ItemAsStrParser{
        @Override
        public Pid fromStr(final String asStr) {
            return asStr==null ? null : Pid.fromStr(asStr);
        }
    }
    
    private static final PidAsStrParser ITEM_PARSER = new PidAsStrParser();
    
    public ArrPid(final int initialCapacity){
        super(initialCapacity);
    }
    
    public ArrPid(){
        super();
    }    
    
    public ArrPid(final Pid... pids){
        super(pids.length);
        addAll(Arrays.asList(pids));
    }
    
    public ArrPid(final String... pidsAsStr){
        super(pidsAsStr.length);
        for (String pidAsStr: pidsAsStr){
            add(pidAsStr==null ? null : Pid.fromStr(pidAsStr));
        }
    }
    
    public ArrPid(final ArrPid source){
        super(source);
    }
    
    @Override
    public EValType getItemValType() {
        return EValType.JAVA_CLASS;
    }
        
    @Override
    protected String getAsStr(final int i) {
        return get(i)==null ? null : get(i).toStr();
    }

    public static ArrPid fromValAsStr(final String valAsStr) {
        if (valAsStr==null){
            return null;
        }else{
            final ArrPid arr = new ArrPid();
            restoreArrFromValAsStr(arr, valAsStr, null, ITEM_PARSER);
            return arr;
        }
    }
    
    public static ArrPid fromArrRef(final ArrRef arrRef){
        if (arrRef==null){
            return null;
        }else{
            final ArrPid result = new ArrPid();
            for (Reference reference: arrRef){
                result.add(reference==null ? null : reference.getPid());
            }
            return result;
        }
    }
    
    public ArrStr toArrStr(){
        final ArrStr result = new ArrStr();
        for (Pid pid: this){
            result.add(pid==null ? null : pid.toString());
        }
        return result;
    }
    
    public ArrRef toArrRef(){
        final ArrRef result = new ArrRef();
        for (Pid pid: this){
            result.add(pid==null ? null : new Reference(pid));
        }
        return result;
    }
}
