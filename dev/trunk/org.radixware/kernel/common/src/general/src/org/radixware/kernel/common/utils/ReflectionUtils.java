/*
 * Copyright (c) 2008-2017, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */

package org.radixware.kernel.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.exceptions.ShouldNeverHappenError;

public class ReflectionUtils {
    
    private static List<Class> getHierarchy(Class cls, Class exclusiveBound) {
        final List<Class> res = new ArrayList<>();
        while (cls != exclusiveBound) {
            res.add(cls);
            cls = cls.getSuperclass();
        }
        return res;
    }
    
    public static List<Class> getHierarchy(Class cls) {
        return getHierarchy(cls, Object.class);
    }

    private static List<Field> getFields(List<Class> hierarchy, boolean includeStatic, boolean includeFinal) {
        final List<Field> res = new ArrayList<>();
        for (Class c : hierarchy) {
            for (Field f : c.getDeclaredFields()) {
                if ( (!Modifier.isStatic(f.getModifiers()) || includeStatic) &&
                        (!Modifier.isFinal(f.getModifiers()) || includeFinal) ) {
                    f.setAccessible(true);
                    res.add(f);
                }
            }
        }
        return res;
    }
    
    public static List<Field> getFields(List<Class> hierarchy) {
        return getFields(hierarchy, false, true);
    }
    
    public static void copyFields(List<Field> fields, Object from, Object to) {
        for (Field field : fields) {
            try {
                Object value = field.get(from);
                field.set(to, value);
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                throw new ShouldNeverHappenError("Failed to copy field: fieldName = " + field.getName()
                        + ", from.class = " + from.getClass().getCanonicalName()
                        + ", to.class = " + to.getClass().getCanonicalName(), ex);
            }
        }
    }

}
