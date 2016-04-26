/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. This Source Code is distributed
 * WITHOUT ANY WARRANTY; including any implied warranties but not limited to
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Mozilla Public License, v. 2.0. for more details.
 */

package org.radixware.kernel.common.client.types;

import java.util.Arrays;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Arr;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.types.Id;


public class ArrId extends Arr<Id>{
    
    private static final long serialVersionUID = -2765420995083303976L;
    
    private static class IdAsStrParser implements Arr.ItemAsStrParser{
        @Override
        public Id fromStr(final String asStr) {
            return asStr==null ? null : Id.Factory.loadFrom(asStr);
        }        
    }
    
    private static final IdAsStrParser ITEM_PARSER = new IdAsStrParser();
            
    public ArrId(final int initialCapacity){
        super(initialCapacity);
    }
    
    public ArrId(){
        super();
    }    
    
    public ArrId(final Id... ids){
        super(ids.length);
        addAll(Arrays.asList(ids));
    }
    
    public ArrId(final String... idsAsStr){
        super(idsAsStr.length);
        for (String idAsStr: idsAsStr){
            add(idAsStr==null ? null : Id.Factory.loadFrom(idAsStr));
        }
    }
       
    @Override
    public EValType getItemValType() {
        return EValType.JAVA_CLASS;
    }
        
    @Override
    protected String getAsStr(final int i) {
        return get(i)==null ? null : get(i).toString();
    }
    
    public static ArrId fromValAsStr(final String valAsStr) {
        if (valAsStr==null){
            return null;
        }
        final ArrId arr = new ArrId();
        restoreArrFromValAsStr(arr, valAsStr, null, ITEM_PARSER);
        return arr;
    }
    
    public ArrStr toArrStr(){
        final ArrStr result = new ArrStr();
        for (Id id: this){
            result.add(id==null ? null : id.toString());
        }
        return result;
    }
}
