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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BufferedPool<T> {

	public static enum ERegistrationMode{
		IMMEDIATELY, SHEDULE
	}
	public BufferedPool(final ERegistrationMode registrationMode){
		super();
		registered = new HashSet<T>(256);
		sheduledForRegistration = new HashSet<T>(128);
		sheduledForUnregistration = new HashSet<T>(128);
		this.registrationMode = registrationMode;
	}
	
	private ERegistrationMode registrationMode;
	private final Set<T> registered;
	private Set<T> sheduledForRegistration;
	private final Set<T> sheduledForUnregistration;
	
	public final ERegistrationMode  getRegistrationMode(){
        return registrationMode;
    }

	public final void setRegistrationMode(final ERegistrationMode mode){
		this.registrationMode = mode;
		if (mode == ERegistrationMode.IMMEDIATELY)
			flush();
	}
	
	public void register(final T o){ 
		if (registrationMode == ERegistrationMode.IMMEDIATELY) {
			registered.add(o);
        } else {
			sheduledForRegistration.add(o);
        }
	}

	public T findRegistration(final T o){
		if (registered.contains(o) && !sheduledForUnregistration.contains(o)){
            return findObjInSet(registered, o);
        }
		if (sheduledForRegistration.contains(o)) {
			return findObjInSet(sheduledForRegistration, o);
        }
		return null;
    }

    private final T findObjInSet(final Set<T> set, final T o) {
        for (T i : set) {
            if (i.equals(o)) {
                return i;
            }
        }
        return null;
    }

	public boolean isRegistered(final T o){
		return
           (registered.contains(o) && !sheduledForUnregistration.contains(o)) ||
           (sheduledForRegistration.contains(o));
	}
	
	public void unregister(final T o){ 
		if (registrationMode == ERegistrationMode.IMMEDIATELY){
			registered.remove(o);
        } else {
			sheduledForUnregistration.add(o);
        }
		sheduledForRegistration.remove(o);
	}
	
	@SuppressWarnings("unchecked")
	public Collection<T> getRegistered(){
		return Collections.unmodifiableCollection(registered);
	}

	
	/**
	 * Do sheduled registration actions
	 * @return collection of added objects  
	 */
	@SuppressWarnings("unchecked")
	public final Collection<T> flush(){
		registered.removeAll(sheduledForUnregistration);
		sheduledForUnregistration.clear();
		registered.addAll(sheduledForRegistration);
		final Collection<T> newObjs = Collections.unmodifiableCollection(sheduledForRegistration); 
		sheduledForRegistration = new HashSet<T>(128);
		return newObjs;
	}

	public void clear() {
		registered.clear();
		sheduledForRegistration.clear();
		sheduledForUnregistration.clear();
	}

	public boolean isEmpty() {
		return registered.isEmpty() && sheduledForRegistration.isEmpty();
	}
}
