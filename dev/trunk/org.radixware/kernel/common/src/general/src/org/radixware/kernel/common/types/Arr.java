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

package org.radixware.kernel.common.types;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.WrongFormatError;

public abstract class Arr<E> extends ArrayList<E> {

    public interface IArrayListener {

        public void arrayModified(Arr arr);
    }
    private final Object notificationLock = new Object();
    private Map<IArrayListener, Object> listeners = null;

    public void addListener(IArrayListener listener) {
        synchronized (notificationLock) {
            if (listeners == null) {
                listeners = new WeakHashMap<IArrayListener, Object>();
            }
            listeners.put(listener, null);
        }
    }

    public void removeListener(IArrayListener listener) {
        synchronized (notificationLock) {
            if (listeners != null) {
                listeners.remove(listener);
                if (listeners.isEmpty()) {
                    listeners = null;
                }
            }
        }
    }

    protected Arr(int initialCapacity) {
        super(initialCapacity);
    }

    protected Arr() {
        super();
    }

    protected Arr(E[] array) {
        this(array == null ? 10 : array.length);
        if (array != null) {
            addAll(java.util.Arrays.asList(array));
        }
    }

    protected Arr(final Collection<? extends E> c) {
        super(c);
    }

    protected static interface ItemAsStrParser {

        Object fromStr(String asStr);
    }

    @SuppressWarnings("unchecked")
    protected static <ArrElementType extends Object> void restoreArrFromValAsStr(Arr<ArrElementType> to, String asStr, EValType valType, final ItemAsStrParser itemAsStrParser) {
        if (asStr.length() > 0 && !asStr.equals("[0]")) {
            int pos1 = asStr.indexOf('[');
            if (pos1 < 0) {
                pos1 = asStr.length();
                //throw new WrongFormatError("Wrong format of array string presentation. Can't parse array size.", null);
            }
            int size;
            try {
                size = Integer.parseInt(asStr.substring(0, pos1));
                to.ensureCapacity(size);
            } catch (NumberFormatException e) {
                throw new WrongFormatError("Wrong format of array string presentation. Wrong array size format.", e);
            }
            int pos2 = asStr.indexOf(']', pos1);
            String lenStr;
            int len;
            boolean isNull;
            String itemAsStr;
            while (pos2 != -1) {
                if (pos2 - pos1 < 1) {
                    throw new WrongFormatError("Wrong format of array string presentation", null);
                }
                try {
                    lenStr = asStr.substring(pos1 + 1, pos2);
                    isNull = (lenStr.length() == 0);
                    if (!isNull) {
                        len = Integer.parseInt(lenStr);
                    } else {
                        len = 0;
                    }
                } catch (NumberFormatException e) {
                    throw new WrongFormatError("Wrong format of array string presentation. Can't parse item length.", e);
                }
                if (!isNull) {
                    if (len == 0) {
                        itemAsStr = "";
                    } else {
                        if (pos2 + len >= asStr.length()) {
                            throw new WrongFormatError("Wrong format of array string presentation. Wrong item length.", null);
                        }
                        itemAsStr = asStr.substring(pos2 + 1, pos2 + 1 + len);
                    }
                    to.add((ArrElementType) itemAsStrParser.fromStr(itemAsStr));
                } else {
                    to.add(null);
                }
                pos1 = pos2 + len + 1;
                if (pos1 >= asStr.length()) {
                    break;
                }
                pos2 = asStr.indexOf(']', pos1);
            }
            if (to.size() != size) {
                throw new WrongFormatError("Wrong format of array string presentation. Wrong array item count.", null);
            }
        } else {
            to.clear();
        }
    }

    @Override
    public String toString() {
        if (size() == 0) {
            return "[0]";
        }
        final StringBuilder t = new StringBuilder();
        t.append(String.valueOf(size()));
        String valAsStr;
        for (int i = 0; i < size(); i++) {
            valAsStr = getAsStr(i);
            if (valAsStr != null) {
                t.append('[');
                t.append(String.valueOf(valAsStr.length()));
                t.append(']');
                t.append(valAsStr);
            } else {
                t.append("[]");
            }
        }
        return t.toString();
    }

    protected String getAsStr(int i) {
        return ValAsStr.toStr(get(i), getItemValType());
    }

    abstract public EValType getItemValType();

    private void notifyModified() {
        final List<IArrayListener> keys;
        synchronized (notificationLock) {
            if (listeners != null) {
                keys = new ArrayList<IArrayListener>(listeners.keySet());
            } else {
                keys = null;
            }
        }
        if (keys != null) {
            for (IArrayListener l : keys) {
                if (l != null) {
                    l.arrayModified(this);
                }
            }
        }
    }

    @Override
    public boolean add(E e) {
        if (super.add(e)) {
            notifyModified();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void add(int index, E element) {
        super.add(index, element);
        notifyModified();
    }

    @Override
    public final boolean addAll(Collection<? extends E> c) {
        if (super.addAll(c)) {
            notifyModified();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (super.addAll(index, c)) {
            notifyModified();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void clear() {
        super.clear();
        notifyModified();
    }

    @Override
    public E remove(int index) {
        E old = super.remove(index);
        notifyModified();
        return old;
    }

    @Override
    public boolean remove(Object o) {
        if (super.remove(o)) {
            notifyModified();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (super.removeAll(c)) {
            notifyModified();
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        super.removeRange(fromIndex, toIndex);
        notifyModified();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (super.retainAll(c)) {
            notifyModified();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public E set(int index, E element) {
        E result = super.set(index, element);
        notifyModified();
        return result;
    }

    protected void refine(int index, E element) {
        super.set(index, element);
    }
}
