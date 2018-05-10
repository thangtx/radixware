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
    final private Map<Id, Object> valsById = new HashMap<>(64, 0.3f);
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

    /**
     * A container object which may or may not contain a non-null value. If a value is present, {@code
     * isPresent()} will return {@code true} and {@code get()} will return the value.
     */
    public final static class PropOptional {
        private static final Object EMPTY_VALUE = new Object();
        /**
         * Returns an empty {@code PropOptional} instance. No value is present for this Optional.
         *
         * Though it may be tempting to do so, avoid testing if an object is empty by comparing with
         * {@code ==} against instances returned by {@code Optional.empty()}. There is no guarantee that
         * it is a singleton. Instead, use {@link #isPresent()}
         *
         * @return an empty {@code Optional}
         */
        public static PropOptional empty() {
            return new PropOptional(EMPTY_VALUE);
        }

        private final Object value;

        public boolean isPresent() {
            // Direct compare to static final EMPTY_VALUE object instance reference
            return value != EMPTY_VALUE;
        }
        private PropOptional(Object value) {
            this.value = value;
        }
        /**
         * Returns the value, even if it is EMPTY_VALUE. Check
         * {@link PropOptional#isPresent()} to ensure value is assigned.
         *
         * @return the value held by this {@code Optional}
         * @see PropOptional#isPresent()
         */
        public Object get() {
            return value;
        }
        /**
         * Indicates whether some other object is "equal to" this Optional. The other object is
         * considered equal if: <ul> <li>it is also an {@code PropOptional} and; <li>both instances have no
         * value present or; <li>the present values are "equal to" each other via {@code equals()}.
         * </ul>
         *
         * @param o an object to be tested for equality
         * @return {code true} if the other object is "equal to" this object otherwise {@code false}
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (!(o instanceof PropOptional)) {
                return false;
            }

            if (isPresent()) {
                PropOptional other = (PropOptional) o;
                return value.equals(other.value);
            }

            return false;
        }

        /**
         * Returns the hash code value of the present value, if any, or 0 (zero) if no value is
         * present.
         *
         * @return hash code value of the present value or 0 if no value is present
         */
        @Override
        public int hashCode() {
            if (isPresent()) {
                return value.hashCode();
            }

            return 0;
        }

        /**
        * Returns a non-empty string representation of this Optional suitable for debugging. The exact
        * presentation format is unspecified and may vary between implementations and versions.
        *
        * <p> If a value is present the result must include its string representation in the result.
        * Empty and present PropOptionals must be unambiguously differentiable. </p>
        *
        * @return the string representation of this instance
        */
       @Override
       public String toString() {
           if (isPresent()) {
               return "PropOptional[" + value.toString() + "]";
           }

           return "PropOptional.empty";
       }
       
       /**
        * Convenience method to create an exception in case if property is not loaded
        * @param id
        */
       public PropNotLoadedException cause(final Id id) {
           return new PropNotLoadedException("Property #" + id + " is not loaded", id);
       }
    }
    public final PropOptional getPropOptionalById(final Id propId) {
        final Object val = valsById.get(propId);
        if (val != null) {
            return new PropOptional(val);
        }
        //if val == null, it can be not loaded
        if (valsById.containsKey(propId)) // loaded but it is realy null
        {
            return new PropOptional(null);
        }
        return PropOptional.empty();//"Property #" + propId + " is not loaded");
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
        throw new PropNotLoadedException(propId);
    }

    void clear() {
        valsById.clear();
    }
    
    public boolean removePropValById(final Id propId) {
        if (valsById.containsKey(propId)) {
            valsById.remove(propId);
            return true;
        }
        return false;
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
