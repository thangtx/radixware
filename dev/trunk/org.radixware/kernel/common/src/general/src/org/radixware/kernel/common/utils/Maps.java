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

package org.radixware.kernel.common.utils;

import java.util.Map;
import java.util.HashMap;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.schemas.types.MapStrStr;


public class Maps {

    public static <K, V> Map<K, V> fromArrays(K[] keys, V[] values) {
        if (keys.length != values.length) {
            throw new RadixError("An attempt to create map with different count of keys and values");
        }
        HashMap<K, V> result = new HashMap<K, V>();

        for (int i = 0; i < keys.length; i++) {
            result.put(keys[i], values[i]);
        }

        return result;
    }
    
    public static MapStrStr toXml(Map<String, String> map)  {
        if (map == null)
            return null;
        final MapStrStr xMap = MapStrStr.Factory.newInstance();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            MapStrStr.Entry xEntry = xMap.addNewEntry();
            xEntry.setKey(entry.getKey());
            xEntry.setValue(entry.getValue());
        }
        return xMap;
    }
    
    public static Map<String, String> fromXml(MapStrStr xMap)  {
        if (xMap == null)
            return null;
        final Map<String, String> map = new HashMap<String, String>();
        for (MapStrStr.Entry xEntry : xMap.getEntryList()) {
            map.put(xEntry.getKey(), xEntry.getValue());
        }
        return map;
    }    
}
