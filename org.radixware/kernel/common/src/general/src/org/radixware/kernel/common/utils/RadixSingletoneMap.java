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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class RadixSingletoneMap<Key,C>{
    private Map<Key, Class<? extends C>> classMap = Collections.synchronizedMap(new HashMap<Key, Class<? extends C>>());
    private Map<Key, C> instanceMap = Collections.synchronizedMap(new HashMap<Key, C>());

    public RadixSingletoneMap() {
    }
    
    public void put(Key key,Class<? extends C> clazz){
        this.classMap.put(key, clazz);
    }
    
    public synchronized C findInstance(Key key) throws InstantiationException, IllegalAccessException{
        C instance = instanceMap.get(key);
        if(instance == null){
            Class<? extends C> clazz = classMap.get(key);
            if(clazz == null){
                return null;
            }else{
                instance = clazz.newInstance();
                if(instance != null){
                    instanceMap.put(key, instance);
                }
            }
        }    
        return instance;
    }
}
