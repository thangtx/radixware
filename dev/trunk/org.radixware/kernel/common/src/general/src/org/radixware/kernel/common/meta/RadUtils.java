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

package org.radixware.kernel.common.meta;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.types.Id;

public final class RadUtils{
    public static final <MapElementType extends Definition>  Map<String, MapElementType> map2MapByName(final Map<Id, MapElementType>  hm){
        if (hm == null)
            return null;    
        final HashMap<String, MapElementType> byName = new HashMap<String, MapElementType>(hm.size()*2+1);
        for( Map.Entry<Id, MapElementType> obj : hm.entrySet()){
            byName.put(obj.getValue().getName(), obj.getValue());
        }
        return byName;
    }
    
    public static final <MapElementType extends Definition> Map<Id, MapElementType> map2MapById(final Map<String, MapElementType> hm){
        if (hm == null)
            return null;    
        final Map<Id, MapElementType> byId = new HashMap<Id, MapElementType>(hm.size()*2+1);
        final Iterator<MapElementType> iter = hm.values().iterator();
        MapElementType obj;
        while(iter.hasNext()){
            obj = iter.next();
            byId.put(obj.getId(), obj);
        }
        return byId;
    }
    
    public static final <ArrElementType extends Definition> Map<Id, ArrElementType> arr2MapById(final ArrElementType[] arr){
        if (arr == null)
            return null;    
        final HashMap<Id, ArrElementType> byId = new HashMap<Id, ArrElementType>(arr.length*2+1);
        for (int i = 0; i < arr.length; i++){
            byId.put(arr[i].getId(), arr[i]);
        }    
        return byId;
    }

    public static final <ArrElementType extends Definition> Map<String, ArrElementType> arr2MapByName(final ArrElementType[] arr){
        if (arr == null)
            return null;    
        final HashMap<String, ArrElementType> byName = new HashMap<String, ArrElementType>(arr.length*2+1);   
        for (int i = 0; i < arr.length; i++)
            byName.put(arr[i].getName(), arr[i]);
        return byName;
    }
}
