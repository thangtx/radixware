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
package org.radixware.kernel.server.arte;

import java.util.Collection;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.utils.BufferedPool;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.server.arte.Cache.EntityCacheItem;
import org.radixware.kernel.server.types.Entity;
import org.radixware.kernel.server.types.Pid;

final class ExistingObjectsPool {

    final class Item {

        private final long arteTranSeq;
        private final Entity object;
        final String creationStack;

        public Item(final Entity object, final long arteTranSeq, final String creationStack) {
            super();
            this.object = object;
            this.arteTranSeq = arteTranSeq;
            this.creationStack = creationStack;
        }

        long getArteTranSeq() {
            return arteTranSeq;
        }

        Entity getObject() {
            return object;
        }
    }
    private final EntityBufferedPool bufferedPool;

    ExistingObjectsPool(final BufferedPool.ERegistrationMode registrationMode) {
        super();
        bufferedPool = new EntityBufferedPool(registrationMode);
    }

    Item getByPid(final Pid pid) {
        final Cache.EntityCacheItem item = bufferedPool.findRegistration(searchKeyForPid(pid));
        if (item != null) {
            return new Item(item.object, item.arteTranSeq, item.creationStack);
        }
        return null;
    }

    void register(final Cache.EntityCacheItem it) {
        final Cache.EntityCacheItem oldReg = bufferedPool.findRegistration(it);
        if (oldReg != null && it.object != oldReg.object) {
            it.object.getArte().getTrace().put(
                    EEventSeverity.WARNING,
                    "Another object with same PID is already registered\n"
                    + "new object: \n" + it.object
                    + "\nold object: \n" + oldReg.object
                    + "\nStack: \n" + Utils.stackToString(Thread.currentThread().getStackTrace()),
                    EEventSource.ARTE);
            oldReg.object.discard();
        }
        bufferedPool.register(it);
    }

    boolean isRegistered(final Entity obj) {
        return bufferedPool.isRegistered(searchKeyForPid(obj.getPid()));
    }

    void unregister(final Entity obj) {
        bufferedPool.unregister(searchKeyForPid(obj.getPid()));
    }

    void clear() {
        bufferedPool.clear();
    }

    Collection<Cache.EntityCacheItem> flush() {
        return bufferedPool.flush();
    }

    Collection<Cache.EntityCacheItem> getRegistered() {
        return bufferedPool.getRegistered();
    }

    void setRegistrationMode(final BufferedPool.ERegistrationMode mode) {
        bufferedPool.setRegistrationMode(mode);
    }

    BufferedPool.ERegistrationMode getRegistrationMode() {
        return bufferedPool.getRegistrationMode();
    }

    private EntityCacheItem searchKeyForPid(final Pid pid) {
        return new EntityCacheItem(pid);
    }
    
    public int getSize() {
        return bufferedPool.getSize();
    }

}
