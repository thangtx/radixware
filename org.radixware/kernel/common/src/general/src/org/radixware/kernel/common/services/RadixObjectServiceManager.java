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

package org.radixware.kernel.common.services;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.WeakHashMap;
import org.radixware.kernel.common.defs.RadixObject;


public class RadixObjectServiceManager<T extends IRadixObjectService> {

    private Class<? extends T> serviceClass;
    private Map<Class<? extends RadixObject>, List<T>> radixObject2services;

    protected RadixObjectServiceManager(Class<? extends T> serviceClass) {
        this.serviceClass = serviceClass;
    }

    public Iterator<T> iterator(RadixObject radixObject) {
        if (radixObject == null) {
            return null;
        }
        synchronized (this) {
            if (radixObject2services == null) {
                init();
            }
        }
        Class radixObjectClass = radixObject.getClass();
        while (radixObjectClass != null && RadixObject.class.isAssignableFrom(radixObjectClass)) {
            List<T> serviceList = radixObject2services.get(radixObjectClass);
            if (serviceList != null) {
                return serviceList.iterator();
            }
            radixObjectClass = radixObjectClass.getSuperclass();
        }
        return null;
    }

    /**
     * @param radixObject
     * @return First service from
     * {@code RadixObjectService.iterator(radixObject)} or null if it doesn't
     * exist.
     */
    public T first(RadixObject radixObject) {
        Iterator<T> it = iterator(radixObject);
        if (it == null || !it.hasNext()) {
            return null;
        }
        return it.next();
    }

    private void init() {
        radixObject2services = new WeakHashMap<Class<? extends RadixObject>, List<T>>();
        ServiceLoader<? extends T> serviceLoader = ServiceLoader.load(serviceClass);
        Iterator<? extends T> it = serviceLoader.iterator();
        while (it.hasNext()) {
            IRadixObjectService serviceCandidate = it.next();

            if (serviceClass.isAssignableFrom(serviceCandidate.getClass())) {
                T service = (T) serviceCandidate;
                Class<? extends RadixObject> supportedClass = service.getSupportedClass();
                if (supportedClass != null) {
                    addEntry(supportedClass, service);
                } else {
                    throw new IllegalStateException("getSupportedClass returned null in" + service);
                }
            }
        }
    }

    private void addEntry(Class<? extends RadixObject> radixObjectClass, T service) {
        List<T> list = radixObject2services.get(radixObjectClass);
        if (list == null) {
            list = new LinkedList<T>();
            radixObject2services.put(radixObjectClass, list);
        }
        list.add(service);
    }
}
