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

package org.radixware.kernel.server.types;

import java.sql.Blob;
import java.sql.Clob;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.radixware.kernel.common.build.xbeans.IXBeansChangeEmitter;
import org.radixware.kernel.common.build.xbeans.IXBeansChangeListener;
import org.radixware.kernel.common.build.xbeans.XBeansChangeEvent;
import org.radixware.kernel.common.types.Arr;
import org.radixware.kernel.common.types.Arr.IArrayListener;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.server.exceptions.PropNotLoadedException;


public final class EntityPropVals implements IArrayListener, IXBeansChangeListener {

    private static final long serialVersionUID = -612899817970765117L;
    final private Map<Id, Object> valsById = new HashMap<Id, Object>();
    //-----------------RADIX-5629------------------
    private final Entity owner;

    public EntityPropVals(Entity owner) {
        this.owner = owner;
    }
    //-----------------RADIX-5629------------------

    public EntityPropVals() {
        this(null);
    }

    public boolean containsPropById(final Id id) {
        return valsById.containsKey(id);
    }

    public boolean isEmpty() {
        return valsById.isEmpty();
    }

    public int size() {
        return valsById.size();
    }

    public Map<Id, Object> asMap() {
        return Collections.unmodifiableMap(valsById);
    }

    public final void putPropValById(final Id propId, final Object val) {
        Object oldVal = valsById.put(propId, val);
        //-----------------RADIX-5629------------------
        stopListenValueChanges(oldVal);
        startListenValueChanges(val);
        //-----------------RADIX-5629------------------
    }

    public final Object getPropValById(final Id propId) throws PropNotLoadedException {
        final Object val = valsById.get(propId);
        if (val != null) {
            return val;
        }
        //if val == null, it can be not loaded
        if (valsById.containsKey(propId)) // loaded but it is realy null
        {
            return null;
        }
        throw new PropNotLoadedException("Property #" + propId + " is not loaded", propId);
    }

    void clear() {
        valsById.clear();
    }

    void removeModifiedLobs(final Collection<Id> modifiedProps) {
        for (Id propId : modifiedProps) {
            final Object val = valsById.get(propId);
            if (val instanceof Clob || val instanceof Blob) {
                valsById.remove(propId);
            }
        }
    }

    void putAll(final EntityPropVals propVals) {
        //-----------------RADIX-5629------------------
        for (Map.Entry<Id, Object> e : propVals.valsById.entrySet()) {
            stopListenValueChanges(valsById.get(e.getKey()));
            startListenValueChanges(e.getValue());
        }
        //-----------------RADIX-5629------------------
        valsById.putAll(propVals.valsById);
    }

    Id getPropIdByVal(Object value) {
        for (Map.Entry<Id, Object> e : valsById.entrySet()) {
            if (e.getValue() == value) {
                return e.getKey();
            }
        }
        return null;
    }

    @Override
    public void arrayModified(Arr arr) {
        if (owner != null) {
            Id propId = getPropIdByVal(arr);
            if (propId != null) {
                owner.setPropModified(propId);
            }
        }
    }

    private void startListenValueChanges(Object obj) {
        if (obj != null && owner != null) {
            if (obj instanceof Arr) {
                ((Arr) obj).addListener(this);
            } else if (obj instanceof IXBeansChangeEmitter) {
                ((IXBeansChangeEmitter) obj).addXBeansChangeListener(this);
            }
        }
    }

    private void stopListenValueChanges(Object obj) {
        if (obj != null && owner != null) {
            if (obj instanceof Arr) {
                ((Arr) obj).removeListener(this);
            } else if (obj instanceof IXBeansChangeEmitter) {
                ((IXBeansChangeEmitter) obj).removeXBeansChangeListener(this);
            }
        }
    }

    @Override
    public void xBeansChange(XBeansChangeEvent event) {
        if (owner != null && event != null && event.getEmitter() != null) {
            Id propId = getPropIdByVal(event.getEmitter());
            if (propId != null) {
                owner.setPropModified(propId);
            }
        }
    }

    @Override
    public boolean beforeXBeansChange(XBeansChangeEvent event) {
        owner.state.assertWriteAllowed();
        return true;
    }
}
