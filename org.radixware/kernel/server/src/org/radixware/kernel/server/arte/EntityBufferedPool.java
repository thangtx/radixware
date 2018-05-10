/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.server.arte;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.WeakHashMap;
import org.radixware.kernel.common.utils.BufferedPool;
import org.radixware.kernel.common.utils.SystemPropUtils;
import org.radixware.kernel.server.types.Entity;
import org.radixware.kernel.server.types.IEntityTouchListener;

/**
 *
 * @author dsafonov
 */
public class EntityBufferedPool extends BufferedPool<Cache.EntityCacheItem> {

    private static final int HARD_SIZE = SystemPropUtils.getIntSystemProp("rdx.entity.cache.hard.size", -1);
    private static final int REBALANCE_PUTS = SystemPropUtils.getIntSystemProp("rdx.entity.cache.rebalance.puts", 100);
    private static final int REBALANCE_DIFF = SystemPropUtils.getIntSystemProp("rdx.entity.cache.rebalance.diff", 100);
    //
    private final Map<Cache.EntityCacheItem, Cache.EntityCacheItem> hardRegistered = new HashMap();
    private final Map<Cache.EntityCacheItem, SoftReference<Cache.EntityCacheItem>> softRegistered = new WeakHashMap<>();
    private Map<Cache.EntityCacheItem, Cache.EntityCacheItem> scheduledForRegistration = new HashMap();
    private final Map<Cache.EntityCacheItem, Cache.EntityCacheItem> sheduledForUnregistration = new HashMap();
    private int countedPuts = 0;

    public EntityBufferedPool(BufferedPool.ERegistrationMode registrationMode) {
        super(registrationMode);
    }

    @Override
    public void register(final Cache.EntityCacheItem item) {
        if (getRegistrationMode() == BufferedPool.ERegistrationMode.IMMEDIATELY) {
            doRegister(item);
        } else {
            scheduledForRegistration.put(item, item);
        }
    }
    
    private void doRegister(final Cache.EntityCacheItem item) {
         hardRegistered.put(item, item);
            if (HARD_SIZE >= 0 && hardRegistered.size() > HARD_SIZE) {
                countedPuts++;
                if (countedPuts > REBALANCE_PUTS) {
                    rebalance();
                }
            }
    }

    private void rebalance() {
        countedPuts = 0;
        int overCnt = hardRegistered.size() - HARD_SIZE;
        if (overCnt < REBALANCE_DIFF) {
            return;
        }
        if (1 < 2) {
            throw new UnsupportedOperationException("TODO: do not move modified objects to soft pool");
        }
        final PriorityQueue<Cache.EntityCacheItem> coldest = new PriorityQueue<>(overCnt, new Comparator<Cache.EntityCacheItem>() {

            @Override
            public int compare(Cache.EntityCacheItem o1, Cache.EntityCacheItem o2) {
                long diff = o2.object.getTouchTimeMillis() - o1.object.getTouchTimeMillis();
                if (diff < 0) {
                    return -1;
                }
                return diff > 0 ? 1 : 0;
            }
        });
        for (Cache.EntityCacheItem item : hardRegistered.keySet()) {
            coldest.add(item);
            if (coldest.size() > overCnt) {
                coldest.poll();
            }
        }
        for (Cache.EntityCacheItem item : coldest) {
            Entity.TouchListenerSetter.setTouchListener(item.object, new MakeHardTouchListener(item));
            hardRegistered.remove(item);
            softRegistered.put(item, new SoftReference<>(item));
        }

    }

    @Override
    public Cache.EntityCacheItem findRegistration(final Cache.EntityCacheItem item) {
        Cache.EntityCacheItem regObj = hardRegistered.get(item);
        if (HARD_SIZE >= 0 && regObj == null) {
            SoftReference<Cache.EntityCacheItem> softRef = softRegistered.get(item);
            if (softRef != null) {
                regObj = softRef.get();
            }
        }
        final Cache.EntityCacheItem schedForUnregObj = sheduledForUnregistration.get(item);
        if (regObj != null && schedForUnregObj == null) {
            return regObj;
        }
        return scheduledForRegistration.get(item);
    }

    @Override
    public boolean isRegistered(final Cache.EntityCacheItem item) {
        return findRegistration(item) != null;
    }

    public void unregister(final Cache.EntityCacheItem item) {
        if (getRegistrationMode() == BufferedPool.ERegistrationMode.IMMEDIATELY) {
            if (hardRegistered.remove(item) == null) {
                softRegistered.remove(item);
            }
        } else {
            sheduledForUnregistration.put(item, item);
        }
        scheduledForRegistration.remove(item);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<Cache.EntityCacheItem> getRegistered() {
        if (softRegistered.isEmpty()) {
            return hardRegistered.keySet();
        }
        if (softRegistered.isEmpty()) {
            return hardRegistered.keySet();
        }
        final ArrayList<Cache.EntityCacheItem> all = new ArrayList<>(hardRegistered.size() + softRegistered.size());
        all.addAll(hardRegistered.keySet());
        all.addAll(softRegistered.keySet());
        return all;
    }

    /**
     * Do scheduled registration actions
     *
     * @return collection of added objects
     */
    @SuppressWarnings("unchecked")
    @Override
    public final Collection<Cache.EntityCacheItem> flush() {
        for (Cache.EntityCacheItem obj : sheduledForUnregistration.keySet()) {
            if (hardRegistered.remove(obj) == null) {
                softRegistered.remove(obj);
            }
        }
        sheduledForUnregistration.clear();
        for (Cache.EntityCacheItem scheduled : scheduledForRegistration.keySet()) {
            doRegister(scheduled);
        }
        final Collection<Cache.EntityCacheItem> newObjs = Collections.unmodifiableCollection(scheduledForRegistration.keySet());
        scheduledForRegistration = new HashMap<>(128);
        return newObjs;
    }

    @Override
    public void clear() {
        hardRegistered.clear();
        softRegistered.clear();
        scheduledForRegistration.clear();
        sheduledForUnregistration.clear();
    }

    @Override
    public boolean isEmpty() {
        return hardRegistered.isEmpty() && softRegistered.isEmpty() && scheduledForRegistration.isEmpty();
    }
    
    @Override
    public String toString() {
        return "EntityBufferedPool[hardReg=" + hardRegistered.size() + ",softReg=" + softRegistered.size() + ",+=" + scheduledForRegistration.size() + ",-=" + sheduledForUnregistration.size() + "]";
    }
    
    public int getSize() {
        return hardRegistered.size() + softRegistered.size();
    }

    private class MakeHardTouchListener implements IEntityTouchListener {

        private final Cache.EntityCacheItem cacheItem;

        public MakeHardTouchListener(Cache.EntityCacheItem cacheItem) {
            this.cacheItem = cacheItem;
        }

        @Override
        public void onTouch(Entity entity) {
            hardRegistered.put(cacheItem, cacheItem);
            softRegistered.remove(cacheItem);
            Entity.TouchListenerSetter.setTouchListener(entity, null);
        }

    }

}
