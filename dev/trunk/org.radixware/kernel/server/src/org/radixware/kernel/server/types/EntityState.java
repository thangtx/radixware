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

import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.server.exceptions.EntityObjectNotExistsError;


public final class EntityState {

    public static enum Enum { //entity states

        NEW, INITED, IN_DATABASE, DELETED, DISCARDED
    }
    private final Entity ent;
    private boolean wasRead = false;
    private Enum state = Enum.NEW;
    private boolean readRights = false;
    private boolean readAcsCoords = false;
    private boolean keepInCache = false;
    private long keepInCacheActivationTimeMillis = 0;
    private long keepInCacheMaxAgeMillis = 0;
    private boolean isAutoUpdateEnabled = true;

    EntityState(final Entity ent) {
        this.ent = ent;
    }

    void afterRead() {
        wasRead = true;
    }

    boolean wasRead() {
        return wasRead;
    }

    void assertAccessAllowed() {
        assertNotDiscarded();
    }

    void assertReadAllowed() {
        assertAccessAllowed();
        //As we can't garantee that property was loaded before delete
        //Let's always throw an exception on read if object is deleted
        //So deleted object behaviour will be predictable
        assertNotDeleted();
    }

    void assertWriteAllowed() {
        assertReadAllowed();
        if (ent.getArte().isReadonlyTransaction()) {
            throw new IllegalUsageError("Try to modify readonly object " + getEntPidStrPres());
        }
        if (!(ent instanceof EntityDetails) && !ent.getArte().getCache().isRegistered(ent)) {
            throw new IllegalUsageError("Try to modify unregistered entity object. The object only can be read " + getEntPidStrPres());
        }
    }

    void assertDbWriteAllowed() {
        assertWriteAllowed();
    }

    boolean isDiscarded() {
        return state == Enum.DISCARDED;
    }

    boolean isDeleted() {
        return state == Enum.DELETED;
    }

    boolean isInDatabase() {
        return state == Enum.IN_DATABASE;
    }

    boolean isInited() {
        return state == Enum.INITED;
    }

    boolean isNewObject() {
        return (state == Enum.NEW) || (state == Enum.INITED);
    }

    void onPurgeProps() {
        if (keepInCache) {
            keepInCacheActivationTimeMillis = System.currentTimeMillis();
        }
    }

    void set(final Enum val) {
        state = val;
        if ((state == Enum.DELETED) || (state == Enum.DISCARDED)) {
            keepInCache = false;
        }
    }

    final void assertNotDeleted() {
        if (state == Enum.DELETED) {
            throw new EntityObjectNotExistsError(ent.getPid());
        }
    }

    private final void assertNotDiscarded() {
        if (isDiscarded()) {
            throw new IllegalUsageError("Try to use discarded object " + getEntPidStrPres());
        }
    }

    private final String getEntPidStrPres() {
        try {
            final Pid p = ent.getPid();
            if (p == null) {
                return "";
            }
            return p.getEntityId() + "->" + p.toString();
        } catch (RuntimeException e) {
            return "";
        }
    }

    final void setReadRights(final boolean bOn) {
        assertReadAllowed();
        readRights = bOn;
    }

    final boolean getReadRights() {
        return readRights;
    }
    
    final void setReadAcsCoords(final boolean read) {
        assertReadAllowed();
        readAcsCoords = read;
    }
    
    final boolean getReadAcsCoords() {
        return readAcsCoords;
    }

    public final void setKeepInCache(final Long maxAgeInSeconds) {
        assertReadAllowed();
        keepInCache = true;
        if (keepInCacheActivationTimeMillis == 0) {
            keepInCacheActivationTimeMillis = System.currentTimeMillis();
        }
        if (maxAgeInSeconds == null) {
            keepInCacheMaxAgeMillis = 0;
        } else {
            keepInCacheMaxAgeMillis = maxAgeInSeconds.longValue() * 1000;
        }
    }

    public final boolean getCanBeRemovedFromCache() {
        return !keepInCache
                || keepInCacheMaxAgeMillis != 0
                && (System.currentTimeMillis() - keepInCacheActivationTimeMillis) > keepInCacheMaxAgeMillis;
    }

    final void setIsAutoUpdateEnabled(final boolean isEnabled) {
        isAutoUpdateEnabled = isEnabled;
    }

    boolean isAutoUpdateEnabled() {
        return isAutoUpdateEnabled;
    }
}
