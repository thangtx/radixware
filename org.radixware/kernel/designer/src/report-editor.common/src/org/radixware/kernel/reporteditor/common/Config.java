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

package org.radixware.kernel.reporteditor.common;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class Config {

    @SuppressWarnings(value = {"rawtypes", "unchecked"})
    public static Object getValue(String key) {
        try {
            Class c = Thread.currentThread().getContextClassLoader().loadClass("org.radixware.kernel.utils.nblauncher.NbLauncher$ApplicationConfig");
            Method method = c.getMethod("getInstance", new Class[0]);
            Method getValueMethod = c.getMethod("getValue", new Class[]{String.class});
            Object config = method.invoke(null, new Object[0]);
            if (config != null) {
                return getValueMethod.invoke(config, key);
            } else {
                return null;
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException ex) {
        }
        return null;
    }

    @SuppressWarnings(value = {"rawtypes", "unchecked"})
    public static void putValue(String key, Object value) {
        try {
            Class c = Thread.currentThread().getContextClassLoader().loadClass("org.radixware.kernel.utils.nblauncher.NbLauncher$ApplicationConfig");
            Method method = c.getMethod("getInstance", new Class[0]);
            Method getValueMethod = c.getMethod("putValue", new Class[]{String.class, Object.class});
            Object config = method.invoke(null, new Object[0]);
            if (config != null) {
                getValueMethod.invoke(config, key, value);
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException ex) {
        }
    }
}
