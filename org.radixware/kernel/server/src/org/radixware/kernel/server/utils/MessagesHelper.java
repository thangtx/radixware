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

package org.radixware.kernel.server.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ResourceBundle;
import org.radixware.kernel.common.exceptions.RadixError;


public class MessagesHelper {

    @Retention(value = RetentionPolicy.RUNTIME)
    @Target(value = ElementType.FIELD)
    public static @interface NotFromBundle {
    }
    
    public static void initialize(Class messagesClazz, String bundleName) {
        ResourceBundle bundle = ResourceBundle.getBundle(bundleName);
        for (Field field : messagesClazz.getDeclaredFields()) {
            if (field.getType().equals(String.class)
                    && ((field.getModifiers() & Modifier.STATIC) != 0)
                    && !field.getName().startsWith("MLS")
                    && !field.isAnnotationPresent(NotFromBundle.class)) {
                field.setAccessible(true);
                try {
                    field.set(null, bundle.getString(field.getName()));
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    throw new RadixError("Error on loading localized messages in kernel", ex);
                }
            }
        }
    }
}
