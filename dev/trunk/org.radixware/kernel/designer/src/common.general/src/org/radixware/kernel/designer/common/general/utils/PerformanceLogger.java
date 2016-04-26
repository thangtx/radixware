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

package org.radixware.kernel.designer.common.general.utils;

import java.util.HashMap;
import java.util.Map;


public class PerformanceLogger {

    private static final Map<String, PerformanceLogger> instances = new HashMap<String, PerformanceLogger>();

    public PerformanceLogger(String name) {
        synchronized (instances) {
            instances.put(name, this);
        }
    }

    public static Map<String, PerformanceLogger> getAll() {
        synchronized (instances) {
            return new HashMap<String, PerformanceLogger>(instances);
        }
    }
    private volatile long total = 0;
    private volatile long enter = 0;

    public void enter() {
        enter = System.nanoTime();
    }

    public void leave() {
        total += (System.nanoTime() - enter);
    }

    public long getTotal() {
        return total;
    }
}
