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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import java.util.concurrent.ConcurrentHashMap;
import org.radixware.kernel.common.enums.ERestriction;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.server.meta.presentations.RadCommandDef;

public class Restrictions extends Object {

    public static final Restrictions ZERO = Restrictions.Factory.newInstance(0);
    public static final Restrictions FULL = Restrictions.Factory.newInstance(0xFFFFFFFF);
    private final long bitMask;
    private final Set<Id> enabledCommandIds;
    private final Set<Id> enabledChildIds;
    private final Set<Id> enabledPageIds;

    protected Restrictions(
            final long bitMask) {
        this(bitMask, (Collection<Id>) null, (Collection<Id>) null, (Collection<Id>) null);
    }

    /**
     *
     * @param bitMask
     * @param enabledCommandIds - not null if ANY_COMMAND set
     * @param enabledChildIds - not null if ANY_CHILD set
     */
    protected Restrictions(
            final long bitMask,
            final Id[] enabledCommandIds,
            final Id[] enabledChildIds,
            final Id[] enabledPagesIds) {
        this(
                bitMask,
                enabledCommandIds == null ? null : Arrays.asList(enabledCommandIds),
                enabledChildIds == null ? null : Arrays.asList(enabledChildIds),
                enabledPagesIds == null ? null : Arrays.asList(enabledPagesIds));
    }

    /**
     *
     * @param bitMask
     * @param enabledCommandIds - not null if ANY_COMMAND set
     * @param enabledChildIds - not null if ANY_CHILD set
     */
    protected Restrictions(
            final long bitMask,
            final Collection<Id> enabledCommandIds,
            final Collection<Id> enabledChildIds,
            final Collection<Id> enabledPageIds) {
        super();
        this.bitMask = bitMask;
        if (enabledCommandIds != null) {
            this.enabledCommandIds = Collections.unmodifiableSet(new HashSet<Id>(enabledCommandIds));
        } else {
            this.enabledCommandIds = null;
        }
        if (enabledChildIds != null) {
            this.enabledChildIds = Collections.unmodifiableSet(new HashSet<Id>(enabledChildIds));
        } else {
            this.enabledChildIds = null;
        }
        if (enabledPageIds != null) {
            this.enabledPageIds = Collections.unmodifiableSet(new HashSet<Id>(enabledPageIds));
        } else {
            this.enabledPageIds = null;
        }
    }

    public boolean getIsAccessRestricted() {
        return (bitMask & ERestriction.ACCESS.getValue().longValue()) != 0;
    }

    public boolean getIsContextlessUsageRestricted() {
        return (bitMask & ERestriction.CONTEXTLESS_USAGE.getValue().longValue()) != 0;
    }

    @Deprecated
    public boolean getIsCommandRestricted(final Id cmdId) {
        return getIsCommandRestricted(cmdId, false);
    }
    
    public boolean getIsCommandRestricted(final Id cmdId, final boolean isReadOnly) {
        if ((bitMask & ERestriction.NOT_READ_ONLY_COMMANDS.getValue().longValue()) != 0 && !isReadOnly) {
            return true;
        }
        if ((bitMask & ERestriction.ANY_COMMAND.getValue().longValue()) != 0) {
            return (enabledCommandIds == null) || !enabledCommandIds.contains(cmdId);
        } else {
            return false;//все команды разрешены
        }        
    }
    
    public boolean getIsCommandRestricted(final RadCommandDef command){
        return getIsCommandRestricted(command.getId(), command.isReadOnly());
    }

    public boolean getIsEditorPageRestricted(final Id cmdId) {
        if ((bitMask & ERestriction.ANY_PAGES.getValue().longValue()) != 0) {
            return (enabledPageIds == null) || !enabledPageIds.contains(cmdId);
        } else {
            return false;//все команды разрешены
        }
    }

    public boolean getIsChildRestricted(final Id childId) {
        if ((bitMask & ERestriction.ANY_CHILD.getValue().longValue()) != 0) {
            return (enabledChildIds == null) || !enabledChildIds.contains(childId);
        } else {
            return false;//все команды разрешены
        }
    }

    public boolean getIsAllEditPagesRestricted() {
        if ((bitMask & ERestriction.ANY_PAGES.getValue().longValue()) != 0) {
            return true;
        } else {
            return false;//все вкладки разрешены
        }
    }

    public Collection<Id> getAllowedEditPages() {
        return enabledPageIds == null ? Collections.EMPTY_LIST : Collections.unmodifiableCollection(enabledPageIds);
    }

    public boolean getIsCopyRestricted() {
        return (bitMask & ERestriction.COPY.getValue().longValue()) != 0;
    }

    public boolean getIsCreateRestricted() {
        return (bitMask & ERestriction.CREATE.getValue().longValue()) != 0;
    }

    public boolean getIsDeleteRestricted() {
        return (bitMask & ERestriction.DELETE.getValue().longValue()) != 0;
    }

    public boolean getIsDeleteAllRestricted() {
        return (bitMask & ERestriction.DELETE_ALL.getValue().longValue()) != 0;
    }

    public boolean getIsMoveRestricted() {
        return (bitMask & ERestriction.MOVE.getValue().longValue()) != 0;
    }

    public boolean getIsTransferInRestricted() {
        return (bitMask & ERestriction.TRANSFER_IN.getValue().longValue()) != 0;
    }

    public boolean getIsTransferOutRestricted() {
        return (bitMask & ERestriction.TRANSFER_OUT.getValue().longValue()) != 0;
    }

    public boolean getIsTransferOutAllRestricted() {
        return (bitMask & ERestriction.TRANSFER_OUT_ALL.getValue().longValue()) != 0;
    }

    public boolean getIsUpdateRestricted() {
        return (bitMask & ERestriction.UPDATE.getValue().longValue()) != 0;
    }

    public boolean getIsViewRestricted() {
        return (bitMask & ERestriction.VIEW.getValue().longValue()) != 0;
    }

    @Deprecated
    public boolean getIsContexlessUsageRestricted() {
        return (bitMask & ERestriction.CONTEXTLESS_USAGE.getValue().longValue()) != 0;
    }

    long getBitMask() {
        return bitMask;
    }

    Collection<Id> getEnabledCommandIds() {
        return enabledCommandIds;
    }

    Collection<Id> getEnabledChildIds() {
        return enabledChildIds;
    }

    Collection<Id> getEnabledPageIds() {
        return enabledPageIds;
    }

    public static final class Factory {

        public static Restrictions newInstance(
                final long bitMask) {
            final Long bitMaskLong = Long.valueOf(bitMask);
            Restrictions res = hash.get(bitMaskLong);
            if (res != null) {
                return res;
            }
            res = new Restrictions(bitMask, (Collection<Id>) null, (Collection<Id>) null, (Collection<Id>) null);
            final Restrictions prevVal = hash.putIfAbsent(bitMaskLong, res);
            return prevVal != null ? prevVal : res;
        }

        public static Restrictions newInstance(// метод оставлен для совместимости, чтобы работали старые - прикладные роли (сохраненные в базе)
                final long bitMask, //скомпилированные со старым кодом - иначе падает с   -  no search method
                final Id[] enabledCommandIds,
                final Id[] enabledChildIds) {
            if (enabledCommandIds != null && enabledCommandIds.length != 0
                    || enabledChildIds != null && enabledChildIds.length != 0) {
                return new Restrictions(bitMask, enabledCommandIds, enabledChildIds, null);
            }
            return newInstance(bitMask);
        }

        public static Restrictions newInstance(
                final long bitMask,
                final Id[] enabledCommandIds,
                final Id[] enabledChildIds,
                final Id[] enabledPageIds) {
            if (enabledCommandIds != null && enabledCommandIds.length != 0
                    || enabledChildIds != null && enabledChildIds.length != 0
                    || enabledPageIds != null && enabledPageIds.length != 0) {
                return new Restrictions(bitMask, enabledCommandIds, enabledChildIds, enabledPageIds);
            }
            return newInstance(bitMask);
        }

        public static Restrictions sum(final Restrictions r1, final Restrictions r2) {
            if (r1 == null && r2 == null) {
                return ZERO;
            }
            if (r1 == null) {
                return r2;
            }
            if (r2 == null) {
                return r1;
            }
            if (r1.enabledCommandIds != null && !r1.enabledCommandIds.isEmpty()
                    || r2.enabledCommandIds != null && !r2.enabledCommandIds.isEmpty()
                    || r1.enabledChildIds != null && !r1.enabledChildIds.isEmpty()
                    || r2.enabledChildIds != null && !r2.enabledChildIds.isEmpty()
                    || r1.enabledPageIds != null && !r1.enabledPageIds.isEmpty()
                    || r2.enabledPageIds != null && !r2.enabledPageIds.isEmpty()) {
                final ModifiableRestrictions r = new ModifiableRestrictions(r1);
                r.add(r2);
                return new Restrictions(r.getBitMask(), r.getEnabledCommandIds(), r.getEnabledChildIds(), r.getEnabledPageIds());

            }
            return newInstance(r1.bitMask | r2.bitMask);
        }

        public static Restrictions and(final Restrictions r1, final Restrictions r2) {
            if (r1 == null || r2 == null) {
                return ZERO;
            }
            if (r1.enabledCommandIds != null && !r1.enabledCommandIds.isEmpty()
                    || r2.enabledCommandIds != null && !r2.enabledCommandIds.isEmpty()
                    || r1.enabledChildIds != null && !r1.enabledChildIds.isEmpty()
                    || r2.enabledChildIds != null && !r2.enabledChildIds.isEmpty()
                    || r1.enabledPageIds != null && !r1.enabledPageIds.isEmpty()
                    || r2.enabledPageIds != null && !r2.enabledPageIds.isEmpty()) {
                final ModifiableRestrictions r = new ModifiableRestrictions(r1);
                r.and(r2);
                return newInstance(r);

            }
            return newInstance(r1.bitMask & r2.bitMask);
        }
        private static final ConcurrentHashMap<Long, Restrictions> hash = new ConcurrentHashMap<Long, Restrictions>();

        public static Restrictions newInstance(final ModifiableRestrictions r) {
            return new Restrictions(r.getBitMask(), r.getEnabledCommandIds(), r.getEnabledChildIds(), r.getEnabledPageIds());
        }
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Restrictions other = (Restrictions) obj;
        if (this.bitMask != other.bitMask) {
            return false;
        }
        if (this.enabledCommandIds != other.enabledCommandIds && (this.enabledCommandIds == null || !this.enabledCommandIds.equals(other.enabledCommandIds))) {
            return (this.enabledCommandIds == null || this.enabledCommandIds.isEmpty())
                    && (other.enabledCommandIds == null || other.enabledCommandIds.isEmpty());
        }
        if (this.enabledChildIds != other.enabledChildIds && (this.enabledChildIds == null || !this.enabledChildIds.equals(other.enabledChildIds))) {
            return (this.enabledChildIds == null || this.enabledChildIds.isEmpty())
                    && (other.enabledChildIds == null || other.enabledChildIds.isEmpty());
        }
        if (this.enabledPageIds != other.enabledPageIds && (this.enabledPageIds == null || !this.enabledPageIds.equals(other.enabledPageIds))) {
            return (this.enabledPageIds == null || this.enabledPageIds.isEmpty())
                    && (other.enabledPageIds == null || other.enabledPageIds.isEmpty());
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + (int) (this.bitMask ^ (this.bitMask >>> 32));
        hash = 37 * hash + (this.enabledCommandIds != null ? this.enabledCommandIds.hashCode() : 0);
        hash = 37 * hash + (this.enabledChildIds != null ? this.enabledChildIds.hashCode() : 0);
        hash = 37 * hash + (this.enabledPageIds != null ? this.enabledPageIds.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "bitMask = " + Long.toBinaryString(bitMask) + "; enabled commands = " + enabledCommandIds + "; enabled children = " + enabledChildIds + "; enabled pages = " + enabledPageIds;
    }
}
