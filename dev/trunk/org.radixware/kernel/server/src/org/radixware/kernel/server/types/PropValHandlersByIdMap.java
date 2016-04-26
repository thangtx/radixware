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

package org.radixware.kernel.server.types;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.radixware.kernel.common.types.Id;


public class PropValHandlersByIdMap implements Map<Id, PropValHandler>{

    private final Map<Id, PropValHandler> map;

    public PropValHandlersByIdMap(final Map<Id, PropValHandler> map) {
        this.map = new HashMap(map);
    }
    public PropValHandlersByIdMap() {
        this.map = new HashMap();
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(final Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(final Object value) {
        return map.containsValue(value);
    }

    @Override
    public PropValHandler get(final Object key) {
        return map.get(key);
    }

    @Override
    public PropValHandler put(final Id key, final PropValHandler value) {
        return map.put(key, value);
    }

    @Override
    public PropValHandler remove(final Object key) {
        return map.remove(key);
    }

    @Override
    public void putAll(final Map<? extends Id, ? extends PropValHandler> m) {
        map.putAll(m);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Set<Id> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<PropValHandler> values() {
        return map.values();
    }

    @Override
    public Set<Entry<Id, PropValHandler>> entrySet() {
        return map.entrySet();
    }

}
