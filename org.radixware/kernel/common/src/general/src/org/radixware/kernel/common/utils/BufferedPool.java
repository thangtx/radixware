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

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class BufferedPool<T> {

    public static enum ERegistrationMode {

        IMMEDIATELY, SHEDULE
    }

    public BufferedPool(final ERegistrationMode registrationMode) {
        super();
        registered = new LinkedHashMap<>(256);
        scheduledForRegistration = new HashMap<>(128);
        sheduledForUnregistration = new HashMap<>(128);
        this.registrationMode = registrationMode;
    }

    private ERegistrationMode registrationMode;
    private final Map<T, T> registered;
    private Map<T, T> scheduledForRegistration;
    private final Map<T, T> sheduledForUnregistration;

    public final ERegistrationMode getRegistrationMode() {
        return registrationMode;
    }

    public final void setRegistrationMode(final ERegistrationMode mode) {
        this.registrationMode = mode;
        if (mode == ERegistrationMode.IMMEDIATELY) {
            flush();
        }
    }

    public void register(final T o) {
        if (registrationMode == ERegistrationMode.IMMEDIATELY) {
            registered.put(o, o);
        } else {
            scheduledForRegistration.put(o, o);
        }
    }

    public T findRegistration(final T o) {
        final T regObj = registered.get(o);
        final T schedForUnregObj = sheduledForUnregistration.get(o);
        if (regObj != null && schedForUnregObj == null) {
            return regObj;
        }
        return scheduledForRegistration.get(o);
    }

    public boolean isRegistered(final T o) {
        return (registered.containsKey(o) && !sheduledForUnregistration.containsKey(o))
                || (scheduledForRegistration.containsKey(o));
    }

    public void unregister(final T o) {
        if (registrationMode == ERegistrationMode.IMMEDIATELY) {
            registered.remove(o);
        } else {
            sheduledForUnregistration.put(o, o);
        }
        scheduledForRegistration.remove(o);
    }

    @SuppressWarnings("unchecked")
    public Collection<T> getRegistered() {
        return Collections.unmodifiableCollection(registered.keySet());
    }

    /**
     * Do scheduled registration actions
     *
     * @return collection of added objects
     */
    @SuppressWarnings("unchecked")
    public Collection<T> flush() {
        for (T obj : sheduledForUnregistration.keySet()) {
            registered.remove(obj);
        }
        sheduledForUnregistration.clear();
        registered.putAll(scheduledForRegistration);
        final Collection<T> newObjs = Collections.unmodifiableCollection(scheduledForRegistration.keySet());
        scheduledForRegistration = new HashMap<>(128);
        return newObjs;
    }

    public void clear() {
        registered.clear();
        scheduledForRegistration.clear();
        sheduledForUnregistration.clear();
    }

    public boolean isEmpty() {
        return registered.isEmpty() && scheduledForRegistration.isEmpty();
    }

    @Override
    public String toString() {
        return "BufferedPool[reg=" + registered.size() + ",+=" + scheduledForRegistration.size() + ",-=" + sheduledForUnregistration.size() + "]";
    }

}
