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

/*
 * 11/17/11 9:36 AM
 */
package org.radixware.kernel.common.utils;

import java.util.*;

/**
 * Simple collection for mapping pairs(key, value) based on Map<TKey, TVal>
 *
 */
public class PropertyStore {

    private static final class PropertyStoreView extends PropertyStore {

        public PropertyStoreView(PropertyStore source) {
            super(Collections.unmodifiableMap(source.data));
        }

        @Override
        public Object set(String name, Object value) {
            throw new UnsupportedOperationException("Attempt to change immutable object");
        }

        @Override
        public PropertyStore merge(PropertyStore store) {
            throw new UnsupportedOperationException("Attempt to change immutable object");
        }

        @Override
        public Object remove(String name) {
            throw new UnsupportedOperationException("Attempt to change immutable object");
        }

        @Override
        public PropertyStore getView() {
            return this;
        }
    }

    public interface ICondition {

        boolean acceptValue(String key, Object value);
    }

    public interface IMultyCondition {

        boolean accept(String... keys);
    }

    public static abstract class Condition implements ICondition, IMultyCondition {

        public static final ICondition TRUE = new ICondition() {

            @Override
            public boolean acceptValue(String key, Object value) {
                return getBool(value);
            }
        };

        public static final ICondition FALSE = new ICondition() {

            @Override
            public boolean acceptValue(String key, Object value) {
                return !getBool(value);
            }
        };

        public static final ICondition NOT_NULL = new ICondition() {

            @Override
            public boolean acceptValue(String key, Object value) {
                return value != null;
            }
        };

        public static final ICondition NULL = new ICondition() {

            @Override
            public boolean acceptValue(String key, Object value) {
                return value == null;
            }
        };
    }

    static abstract class MultyCondition extends Condition {

        final PropertyStore store;
        final boolean all;

        MultyCondition(PropertyStore store, boolean all) {
            this.store = store;
            this.all = all;
        }

        class AcceptIterator implements Iterator<Boolean> {

            String[] keys;
            int current;

            public AcceptIterator(String[] keys) {
                this.keys = keys;
                current = 0;
            }

            @Override
            public boolean hasNext() {
                return keys != null && keys.length > current;
            }

            @Override
            public Boolean next() {
                final String key = keys[current++];
                return MultyCondition.this.acceptValue(key, store.get(key));
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Not supported operation.");
            }
        }

        Iterable<Boolean> getIterable(final String... keys) {
            return new Iterable<Boolean>() {

                @Override
                public Iterator<Boolean> iterator() {
                    return new MultyCondition.AcceptIterator(keys);
                }
            };
        }

        @Override
        public boolean accept(String... keys) {
            for (final Boolean result : getIterable(keys)) {
                if (all ^ result.booleanValue()) {
                    return result.booleanValue();
                }
            }
            return all;
        }
    }

    static class WrapCondition extends MultyCondition {

        private final ICondition condition;

        public WrapCondition(PropertyStore store, ICondition condition, boolean all) {
            super(store, all);

            this.condition = condition != null ? condition : Condition.FALSE;
        }

        @Override
        public boolean acceptValue(String key, Object value) {
            return condition.acceptValue(key, value);
        }
    }


    public static boolean getBool(Object val) {
        return val instanceof Boolean && (Boolean) val;
    }

    public static String getString(Object val) {
        return val instanceof String ? (String) val : "";
    }

    public static int getInt(Object val) {
        return val instanceof Integer ? ((Integer) val).intValue() : 0;
    }


    private final Map<String, Object> data;
    private PropertyStore view;

    public PropertyStore() {
        data = new HashMap<>();
    }

    public PropertyStore(PropertyStore source) {
        data = new HashMap<>();

        if (source != null) {
            data.putAll(source.data);
        }
    }

    private PropertyStore(Map<String, Object> data) {
        this.data = data;
    }

    public Object set(String name, Object value) {
        return data.put(name, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String name) {
        return (T) data.get(name);
    }

    /**
     *
     * @param <T> type of taken property value
     * @param name name of property
     * @param defValue default value of property
     * @return the value to which the specified name is mapped, or
     *         {@code defValue} if this store contains no mapping for the name
     */
    public <T> T get(String name, T defValue) {
        T val = (T) data.get(name);
        return val != null ? val : defValue;
    }

    public <T> T get(String name, Class<T> cls) {
        Object val = data.get(name);
        if (val != null && cls != null && cls.isInstance(val)) {
            return (T) val;
        } else {
            return null;
        }
    }

    public final Condition condition(ICondition source, boolean all) {
        return new WrapCondition(this, source, all);
    }

    public final Condition condition(boolean isTrue, boolean all) {
        return new WrapCondition(this, isTrue ? Condition.TRUE : Condition.FALSE, all);
    }

    public boolean isTrue(String key) {
        return getBool(get(key));
    }

    public boolean contains(String name) {
        return data.containsKey(name);
    }

    public boolean contains(String name, Class<?> cls) {
        return get(name, cls) != null;
    }

    public final Set<String> getKeySet() {
        return data.keySet();
    }

    public final boolean checkRequirement(String... props) {
        for (final String prop : props) {
            if (!data.containsKey(prop)) {
                return false;
            }
        }
        return true;
    }

    public PropertyStore merge(PropertyStore store) {
        if (store != null) {
            data.putAll(store.data);
        }
        return this;
    }

    public Object remove(String name) {
        return data.remove(name);
    }

    /**
     * @return immutable representation of properties
     */
    public PropertyStore getView() {
        if (view == null) {
            view = new PropertyStoreView(this);
        }
        return view;
    }
}
