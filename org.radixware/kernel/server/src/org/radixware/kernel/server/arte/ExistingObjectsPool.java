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
import java.util.HashMap;
import java.util.Map;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.utils.BufferedPool;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.server.types.Entity;
import org.radixware.kernel.server.types.Pid;

final class ExistingObjectsPool {

    final class Item {

        private final long arteTranSeq;
        private final Entity object;

        public Item(final Entity object) {
            super();
            this.object = object;
            this.arteTranSeq = object.getArte().getTransactionSeqNumber();
        }

        long getArteTranSeq() {
            return arteTranSeq;
        }

        Entity getObject() {
            return object;
        }
    }
    private final Map<Pid, Item> objsByPid;
    private final BufferedPool<Cache.Item<Entity>> bufferedPool;

    ExistingObjectsPool(final BufferedPool.ERegistrationMode registrationMode) {
        super();
        bufferedPool = new BufferedPool<Cache.Item<Entity>>(registrationMode);
        objsByPid = new HashMap<Pid, Item>();
    }

    Item getByPid(final Pid pid) {
        return objsByPid.get(pid);
    }

    void register(final Cache.Item<Entity> it) {
        bufferedPool.register(it);
        final Item oldReg = objsByPid.put(it.object.getPid(), new Item(it.object));
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
    }

    boolean isRegistered(final Entity obj) {
        return bufferedPool.isRegistered(new Cache.Item<Entity>(obj, null));
    }

    void unregister(final Entity obj) {
        bufferedPool.unregister(new Cache.Item<Entity>(obj, null));
        objsByPid.remove(obj.getPid());
    }

    void clear() {
        bufferedPool.clear();
        objsByPid.clear();
    }

    int size() {
        return objsByPid.size();
    }

    Collection<Cache.Item<Entity>> flush() {
        return bufferedPool.flush();
    }

    Collection<Cache.Item<Entity>> getRegistered() {
        return bufferedPool.getRegistered();
    }

    void setRegistrationMode(final BufferedPool.ERegistrationMode mode) {
        bufferedPool.setRegistrationMode(mode);
    }

    BufferedPool.ERegistrationMode getRegistrationMode() {
        return bufferedPool.getRegistrationMode();
    }
}
