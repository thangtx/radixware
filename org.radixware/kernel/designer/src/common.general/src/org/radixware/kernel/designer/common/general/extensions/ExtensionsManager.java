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

package org.radixware.kernel.designer.common.general.extensions;

import java.util.HashMap;
import java.util.Map;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.exceptions.RadixObjectError;

/**
 * RadixObject extensions manager.
 * Allows to find some extenstion for Radix object,
 * for example, checker, editor.
 */
public abstract class ExtensionsManager<T> {

    private final String sectionName;
    private final Class<T> extensionClass;

    /**
     * @param sectionName extenstion section name in layer.xml, for example, "Checker", "Editors".
     * @param extensionClass extenstion base class.
     */
    public ExtensionsManager(String sectionName, Class<T> extensionClass) {
        this.sectionName = sectionName;
        this.extensionClass = extensionClass;
    }

    private T findForClass(Class<?> cl) {
        String name = "";
        do {
            String simpleName = cl.getSimpleName();
            if (simpleName.isEmpty()) {
                simpleName = "$";
            }
            if (simpleName.endsWith("Def")) {
                simpleName = simpleName.substring(0, simpleName.length() - 3);
            }
            name = simpleName + name;
            cl = cl.getEnclosingClass();
        } while (cl != null);

        final Lookup lookup = Lookups.forPath("RadixWareDesigner/" + sectionName + "/" + name);
        T extension = lookup.lookup(extensionClass);
        return extension;
    }
    private final Map<Class<?>, T> class2Extension = new HashMap<Class<?>, T>();

    /**
     * Find extenstion for RadixObject class.
     * @return extenstion or null if not found.
     */
    public T find(Class<? extends RadixObject> radixObjectClass) {
        if (class2Extension.containsKey(radixObjectClass)) {
            return class2Extension.get(radixObjectClass);
        }
        for (Class<?> cl = radixObjectClass; cl != Object.class; cl = cl.getSuperclass()) {
            T extension = findForClass(cl);
            if (extension != null) {
                class2Extension.put(radixObjectClass, extension);
                return extension;
            }
        }
        class2Extension.put(radixObjectClass, null);
        return null;
    }

    /**
     * Find extenstion for RadixObject.
     * @return extenstion or null if not found.
     */
    
    public T find(RadixObject radixObject) {
        final Class radixObjectClass = radixObject.getClass();
        return (T)find(radixObjectClass);
    }

    /**
     * Find extenstion for RadixObject.
     * @return extenstion.
     * @throws RadixError if not found.
     */
    public T get(RadixObject radixObject) {
        T extension = find(radixObject);
        if (extension != null) {
            return extension;
        } else {
            throw new RadixObjectError(extensionClass.getName() + " not found.", radixObject);
        }
    }
}
