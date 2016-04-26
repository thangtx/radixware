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

package org.radixware.kernel.common.utils.agents;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public abstract class ProxyAgent<T, Y> implements IObjectAgent<T> {

    private List<ChangeListener> listeners = null;
    private Y temporary = null;
    private WeakReference<Y> temporaryRef = null;
    private volatile boolean isActual = false;
    private final Object lock = new Object();

    @Override
    public boolean isActual() {
        return isActual;
    }

    @Override
    public final boolean invite(boolean force) {

        synchronized (lock) {
            if (!isActual && (isUsed(getTemporary(false)) || force)) {
                return actualize(findActual(true));
            }
        }
        return isActual;
    }

    @Override
    public final void addActualizeListener(ChangeListener listener) {
        synchronized (this) {
            getListeners().add(listener);
        }
    }

    @Override
    public final void removeActualizeListener(ChangeListener listener) {
        synchronized (this) {
            getListeners().remove(listener);
        }
    }

    public final void saveTemporary() {
        synchronized (lock) {
            if (temporary == null && !isActual()) {
                temporary = getTemporary(true);
            }
        }
    }

    protected final Y getObjectSource() {
        synchronized (lock) {
            final Y actual = getActual();

            if (isActual()) {
                return actual;
            }

            return getTemporary(true);
        }
    }

    private boolean actualize(Y actual) {
        Y tmp = getTemporary(false);
        if (!isUsed(tmp) || sync(tmp, actual)) {
            temporary = null;
            if (temporaryRef != null) {
                temporaryRef.clear();
                temporaryRef = null;
            }
            isActual = true;

            fireActualize();

            return true;
        }
        return false;
    }

    private Y getTemporary(boolean create) {

        isActual = false;

        if (temporary != null) {
            return temporary;
        }

        Y ref = temporaryRef != null ? temporaryRef.get() : null;

        if (create && ref == null) {
            ref = createRef();
        }
        return ref;
    }

    private Y createRef() {
        final Y ref = createTemporary();
        temporaryRef = new WeakReference<>(ref);
        return ref;
    }

    private Y getActual() {
        final Y findActual = findActual(false);
        if (!isActual && findActual != null) {
            actualize(findActual);
        }
        return findActual;
    }

    private List<ChangeListener> getListeners() {
        if (listeners == null) {
            listeners = new LinkedList<>();
        }
        return listeners;
    }

    protected final void fireActualize() {
        final List<ChangeListener> ls;
        synchronized (this) {
            ls = getListeners();
        }

        for (final ChangeListener listener : ls) {
            listener.stateChanged(new ChangeEvent(this));
        }
    }

    protected abstract Y findActual(boolean create);

    protected abstract boolean sync(Y temporary, Y actual);

    protected abstract boolean isUsed(Y temporary);

    protected abstract Y createTemporary();
}
