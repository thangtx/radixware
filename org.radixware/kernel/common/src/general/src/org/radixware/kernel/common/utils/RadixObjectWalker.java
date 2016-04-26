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

import org.radixware.kernel.common.defs.RadixObject;


public class RadixObjectWalker<ItemType extends RadixObject> implements IWalker<ItemType> {

    public static class Factory {

        public static <T extends RadixObject> IWalker<T> newInstance(Class<T> cls, RadixObject object) {
            return newInstance(cls, object, false);
        }

        public static <T extends RadixObject> IWalker<T> newInstance(Class<T> cls, RadixObject object, boolean exact) {
            return new RadixObjectWalker<>(cls, object, exact);
        }
    }

    protected final RadixObject object;
    private final Class<ItemType> cls;
    private final boolean exact;

    protected RadixObjectWalker(Class<ItemType> cls, RadixObject object) {
        this(cls, object, false);
    }

    protected RadixObjectWalker(Class<ItemType> cls, RadixObject object, boolean exact) {
        this.object = object;
        this.cls = cls;
        this.exact = exact;
    }

    @Override
    public <ResultType> ResultType walk(IWalker.IAcceptor<ItemType, ResultType> acceptor) {
        if (!Utils.isNotNull(acceptor, object, cls)) {
            return null;
        }

        for (RadixObject container = object; container != null; container = container.getContainer()) {
            if (!acceptObjectType(container) || skip((ItemType) container)) {
                continue;
            }
            acceptor.accept((ItemType) container);
            if (acceptor.isStopped() || stopWalk((ItemType) container)) {
                break;
            }
        }
        return acceptor.getResult();
    }

    private boolean acceptObjectType(RadixObject object) {
        return exact ? cls == object.getClass() : cls.isInstance(object);
    }

    protected boolean skip(ItemType object) {
        return false;
    }

    protected boolean stopWalk(ItemType object) {
        return false;
    }
}
