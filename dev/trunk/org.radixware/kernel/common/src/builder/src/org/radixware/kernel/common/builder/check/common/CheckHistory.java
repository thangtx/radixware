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

package org.radixware.kernel.common.builder.check.common;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Allows to check for definitions with duplicated database names and
 * identifiers.
 *
 */
public class CheckHistory {

    private final Map<Object, Object> historyItems = new ConcurrentHashMap<Object, Object>();

    @SuppressWarnings("unchecked")
    public <T> T findItemByClass(Class<T> cl) {
        synchronized (historyItems) {
            return (T) historyItems.get(cl);
        }

    }

    public void registerItemByClass(Object obj) {
        synchronized (historyItems) {
            historyItems.put(obj.getClass(), obj);
        }
    }

    public void clear() {
        synchronized (historyItems) {
            historyItems.clear();
        }
    }
    
    public Object get(Object key) {
        synchronized (historyItems) {
            return historyItems.get(key);
        }
    }
    
    public void put(Object key, Object value) {
        synchronized (historyItems) {
            historyItems.put(key, value);
        }
    }
    
    public Map<Object, Object> getMap() {
        return historyItems;
    }

    protected CheckHistory() {
    }
}