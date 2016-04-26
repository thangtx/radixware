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

package org.radixware.kernel.common.client.env;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import org.radixware.kernel.common.client.IClientEnvironment;


public class EnvironmentVariables {

    private static class Env2ObjMap {

        private final WeakHashMap<IClientEnvironment, Object> map = new WeakHashMap<>();

        public void put(IClientEnvironment env, Object value) {
            synchronized (map) {
                map.put(env, value);
            }
        }

        public Object get(IClientEnvironment env) {
            synchronized (map) {
                return map.get(env);
            }
        }

        public void remove(IClientEnvironment env) {
            synchronized (map) {
                map.remove(env);
            }
        }

        public boolean isEmpty() {
            synchronized (map) {
                return map.isEmpty();
            }
        }
    }
    private static final Map<String, Env2ObjMap> maps = new HashMap<>();

    public static void put(String mapId, IClientEnvironment env, Object value) {
        Env2ObjMap map;
        synchronized (maps) {
            map = maps.get(mapId);
            if (map == null) {
                map = new Env2ObjMap();
                maps.put(mapId, map);
            }
        }
        map.put(env, value);
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(String mapId, IClientEnvironment env) {
        Env2ObjMap map;
        synchronized (maps) {
            map = maps.get(mapId);
        }
        if (map != null) {
            try {
                return (T) map.get(env);
            } catch (ClassCastException e) {
                return null;
            }
        } else {
            return null;
        }
    }

    public static void remove(String mapId, IClientEnvironment env) {
        synchronized (maps) {
            final Env2ObjMap map = maps.get(mapId);
            if (map != null) {
                map.remove(env);
                if (map.isEmpty()) {
                    maps.remove(mapId);
                }
            }
        }
    }

    public static void clearAll(IClientEnvironment env) {
        synchronized (maps) {
            List<String> removeList = new LinkedList<>();
            for (Map.Entry<String, Env2ObjMap> e : maps.entrySet()) {
                Env2ObjMap v = e.getValue();
                if (v == null) {
                    removeList.add(e.getKey());
                } else {
                    v.remove(env);
                    if (v.isEmpty()) {
                        removeList.add(e.getKey());
                    }
                }
            }
            for (String r : removeList) {
                maps.remove(r);
            }
        }
    }
}
