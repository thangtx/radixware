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
package org.radixware.kernel.common.defs.ads.common;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.RestrictionsWriter;
import org.radixware.kernel.common.enums.ERestriction;
import org.radixware.kernel.common.exceptions.RadixObjectError;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;

public class Restrictions extends RadixObject implements IJavaSource {

    public static final class Factory {

        /**
         * Creates new instance of restrictions handler with no support of
         * commands
         */
        public static final Restrictions newInstance(final RadixObject owner, final long bitmask) {
            final Restrictions r = new Restrictions(bitmask, null, null, null);
            if (owner != null) {
                r.setContainer(owner);
            }
            return r;

        }

        /**
         * Creates new instance of restriction handler with command support
         */
        public static final Restrictions newInstance(final RadixObject owner, final long bitmask, final List<Id> enabledCommandIds, final List<Id> enabledChildIds, final List<Id> enabledPageIds) {
            final Restrictions r = new Restrictions(
                    bitmask,
                    enabledCommandIds == null ? new ArrayList<Id>() : new ArrayList<Id>(enabledCommandIds),
                    enabledChildIds == null ? new ArrayList<Id>() : new ArrayList<Id>(enabledChildIds),
                    enabledPageIds == null ? new ArrayList<Id>() : new ArrayList<Id>(enabledPageIds)
            );
            if (owner != null) {
                r.setContainer(owner);
            }
            return r;
        }

        /**
         * Creates new instance of restrictions handler with no support of
         * commands
         */
        public static final Restrictions newInstance(final long bitmask) {
            return newInstance(null, bitmask);

        }

        /**
         * Creates new instance of restriction handler with command support
         */
        public static final Restrictions newInstance(final long bitmask, final List<Id> enabledCommandIds, final List<Id> enabledChildIds, final List<Id> enabledPageIds) {
            return newInstance(null, bitmask, enabledCommandIds, enabledChildIds, enabledPageIds);
        }

        /**
         * Creates unmodifiable copy of restriction handler
         */
        public static final Restrictions unmodifiableInstance(final Restrictions source) {
            return new UnmodifiableRestriction(source);
        }

        /**
         * Creates restriction handler complementing two restriction handlers
         * Restrictions - by logical or Commands List - by logical and
         */
        public static final Restrictions complementarInstance(final Restrictions source, final Restrictions complement) {
            return new Restrictions(source, complement);
        }
    }

    /**
     * UNmodifiable restriction set internal class
     */
    private static class UnmodifiableRestriction extends Restrictions {

        public UnmodifiableRestriction(final Restrictions source) {
            super(source, null);
        }

        @Override
        public boolean allow(final ERestriction flag) {
            throw new RadixObjectError("Restrictions is readonly in current context.", this);
        }

        @Override
        public boolean canAllow(final ERestriction flag) {
            return false;
        }

        @Override
        public boolean deny(final ERestriction flag) {
            throw new RadixObjectError("Restrictions is readonly in current context.", this);
        }

        @Override
        public boolean canDeny(final ERestriction flag) {
            return false;
        }

        @Override
        public synchronized boolean canEnableCommand(final Id commandId) {
            return false;
        }

        @Override
        public synchronized boolean canEnableChild(final Id childId) {
            return false;
        }

        @Override
        public synchronized boolean setCommandEnabled(final Id commandId, final boolean enable) {
            throw new RadixObjectError("Restrictions is readonly in current context.", this);
        }

        @Override
        public synchronized boolean setChildEnabled(final Id childId, final boolean enable) {
            throw new RadixObjectError("Restrictions is readonly in current context.", this);
        }

        @Override
        public boolean isReadOnly() {
            return true;
        }
    }
    private final EnumSet<ERestriction> restriction;
    private Restrictions complement;
    private Restrictions source = null;
    /**
     * null value means that commands not supported at this level and should not
     * be used in calculations
     */
    private final List<Id> enabledCommandIds;
    private final List<Id> enabledChildIds;
    private final List<Id> enabledPageIds;

    private Restrictions(final long restriction, final List<Id> enabledCommandIds, final List<Id> enabledChildIds, final List<Id> enabledPageIds) {
        this.restriction = ERestriction.fromBitField(restriction);
        this.enabledCommandIds = enabledCommandIds;
        this.enabledChildIds = enabledChildIds;
        this.enabledPageIds = enabledPageIds;

    }

    private Restrictions(final Restrictions source, final Restrictions complement) {
        this.complement = complement;
        this.source = source;
        this.restriction = null;
        this.enabledCommandIds = null;
        this.enabledChildIds = null;
        this.enabledPageIds = null;
    }

    private EnumSet<ERestriction> getSourceRestrictionMask() {
        if (source != null) {
            return source.getSourceRestrictionMask();
        } else {
            return restriction;
        }
    }

    private synchronized List<Id> getSourceEnabledCommandsIds() {
        if (source != null) {
            return source.getSourceEnabledCommandsIds();
        } else {
            return enabledCommandIds;
        }
    }

    private synchronized List<Id> getSourceEnabledChildsIds() {
        if (source != null) {
            return source.getSourceEnabledChildsIds();
        } else {
            return enabledChildIds;
        }
    }

    private synchronized List<Id> getSourceEnabledPagesIds() {
        if (source != null) {
            return source.getSourceEnabledPagesIds();
        } else {
            return enabledPageIds;
        }
    }

    public static boolean contains(Restrictions larger, Restrictions smaller) {
        if (larger == null) {
            larger = Restrictions.Factory.newInstance(0xffffffff);
        }
        if (smaller == null) {
            smaller = Restrictions.Factory.newInstance(0xffffffff);
        }
        if (!larger.getRestriction().containsAll(smaller.getRestriction())) {
            return false;
        }
        if (!larger.isDenied(ERestriction.ACCESS)
                && !smaller.isDenied(ERestriction.ACCESS)) {
            if (larger.isDenied(ERestriction.ANY_COMMAND)
                    && smaller.isDenied(ERestriction.ANY_COMMAND)) {
                final List<Id> largerCmdList = larger.getEnabledCommandIds();
                final List<Id> smallerCmdList = smaller.getEnabledCommandIds();
                if (largerCmdList != null && !largerCmdList.isEmpty()) {
                    if (smallerCmdList == null || smallerCmdList.isEmpty()) {
                        return false;
                    }
                    if (!smallerCmdList.containsAll(largerCmdList)) {
                        return false;
                    }
                }
            }
            if (larger.isDenied(ERestriction.ANY_CHILD)
                    && smaller.isDenied(ERestriction.ANY_CHILD)) {
                final List<Id> largerChildrenList = larger.getEnabledChildIds();
                final List<Id> smallerChildrenList = smaller.getEnabledChildIds();
                if (largerChildrenList != null && !largerChildrenList.isEmpty()) {
                    if (smallerChildrenList == null || smallerChildrenList.isEmpty()) {
                        return false;
                    }
                    if (!smallerChildrenList.containsAll(largerChildrenList)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Returns true if given option is denied false otherwise
     */
    public boolean isDenied(final ERestriction flag) {
        if (complement == null) {
            if (source != null) {
                return source.isDenied(flag);
            }
            return restriction.contains(flag);
        } else {
            if (complement.isDenied(flag)) {
                return true;
            }
            if (source != null) {
                return source.isDenied(flag);
            }
            return restriction.contains(flag);
        }
    }

    /**
     * Denies given option. Return true if option denied in result of this
     * operation
     */
    public boolean deny(final ERestriction flag) {
        this.getSourceRestrictionMask().add(flag);
        updateEditState(EEditState.MODIFIED);
        return true;
    }

    /**
     * Allows given option. Returns true if option allowed in result of
     * operation
     */
    public boolean allow(final ERestriction flag) {
        if (complement != null && complement.isDenied(flag)) {
            return false;
        }
        this.getSourceRestrictionMask().remove(flag);
        updateEditState(EEditState.MODIFIED);
        return true;
    }

    /**
     * Returns true if option may be allowed
     */
    public synchronized boolean canAllow(final ERestriction flag) {
        if (complement == null) {
            return true;
        } else {
            return !complement.isDenied(flag);
        }
    }

    /**
     * Returns true if option may be denied
     */
    public boolean canDeny(final ERestriction flag) {
        return true;
    }

    /**
     * Returns true if command is enabled If this restriction handler has
     * complement restrictions will return true if command is enabled in both
     * this object and complemented restriction set
     */
    public synchronized boolean isCommandEnabled(final Id commandId) {
        if (!isDenied(ERestriction.ANY_COMMAND)) {
            return true;
        }
        if (getSourceEnabledCommandsIds() == null) {
            return true;
        }
        if (complement != null) {
            if (complement.isCommandEnabled(commandId)) {
                return getSourceEnabledCommandsIds().contains(commandId);
            } else {
                return false;
            }
        } else {
            return getSourceEnabledCommandsIds().contains(commandId);
        }
    }

    /**
     * Returns true if child is enabled If this restriction handler has
     * complement restrictions will return true if child is enabled in both this
     * object and complemented restriction set
     */
    public synchronized boolean isChildEnabled(final Id childId) {
        if (!isDenied(ERestriction.ANY_CHILD)) {
            return true;
        }
        if (getSourceEnabledChildsIds() == null) {
            return true;
        }
        if (complement != null) {
            if (complement.isChildEnabled(childId)) {
                return getSourceEnabledChildsIds().contains(childId);
            } else {
                return false;
            }
        } else {
            return getSourceEnabledChildsIds().contains(childId);
        }
    }

    /**
     * Returns true if page is enabled If this restriction handler has
     * complement restrictions will return true if page is enabled in both this
     * object and complemented restriction set
     */
    public synchronized boolean isPageEnabled(final Id pageId) {
        if (!isDenied(ERestriction.ANY_PAGES)) {
            return true;
        }
        if (getSourceEnabledPagesIds() == null) {
            return true;
        }
        if (complement != null) {
            if (complement.isPageEnabled(pageId)) {
                return getSourceEnabledPagesIds().contains(pageId);
            } else {
                return false;
            }
        } else {
            return getSourceEnabledPagesIds().contains(pageId);
        }
    }

    /**
     * Tries to setgiven command enabled returns true if command enabled in
     * result of this operation Returns false if any command is enabled by
     * restriction option Returns false if commands not supported by this
     * restriction set Returns false if given command is already enabled localy
     */
    public synchronized boolean setCommandEnabled(final Id commandId, final boolean enable) {
        if (!isDenied(ERestriction.ANY_COMMAND)) {
            return false;
        }
        if (enable) {
            if (isCommandEnabled(commandId)) {
                return false;
            }
            if (getSourceEnabledCommandsIds() == null) {
                return false;
            }
            if (complement != null) {
                if (!complement.isCommandEnabled(commandId)) {
                    return false;
                }
            }
            getSourceEnabledCommandsIds().add(commandId);
            updateEditState(EEditState.MODIFIED);
            return true;
        } else {
            if (!isCommandEnabled(commandId)) {
                return false;
            }
            if (getSourceEnabledCommandsIds().remove(commandId)) {
                updateEditState(EEditState.MODIFIED);
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Tries to set given child enabled returns true if child enabled in result
     * of this operation Returns false if any child is enabled by restriction
     * option Returns false if children not supported by this restriction set
     * Returns false if given child is already enabled localy
     */
    public synchronized boolean setChildEnabled(final Id childId, final boolean enable) {
        if (!isDenied(ERestriction.ANY_CHILD)) {
            return false;
        }
        if (enable) {
            if (isChildEnabled(childId)) {
                return false;
            }
            if (getSourceEnabledChildsIds() == null) {
                return false;
            }
            if (complement != null && !complement.isChildEnabled(childId)) {
                return false;
            }
            getSourceEnabledChildsIds().add(childId);
            updateEditState(EEditState.MODIFIED);
            return true;
        } else {
            if (!isChildEnabled(childId)) {
                return false;
            }
            if (getSourceEnabledChildsIds().remove(childId)) {
                updateEditState(EEditState.MODIFIED);
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Tries to set given page enabled returns true if page enabled in result of
     * this operation Returns false if any page is enabled by restriction option
     * Returns false if page not supported by this restriction set Returns false
     * if given page is already enabled localy
     */
    public synchronized boolean setPageEnabled(final Id pageId, final boolean enable) {
        if (!isDenied(ERestriction.ANY_PAGES)) {
            return false;
        }
        if (enable) {
            if (isPageEnabled(pageId)) {
                return false;
            }
            if (getSourceEnabledPagesIds() == null) {
                return false;
            }
            if (complement != null && !complement.isPageEnabled(pageId)) {
                return false;
            }
            getSourceEnabledPagesIds().add(pageId);
            updateEditState(EEditState.MODIFIED);
            return true;
        } else {
            if (!isPageEnabled(pageId)) {
                return false;
            }
            if (getSourceEnabledPagesIds().remove(pageId)) {
                updateEditState(EEditState.MODIFIED);
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Returns true if command may be enabled and it's enablement is not
     * meanless
     */
    public synchronized boolean canEnableCommand(final Id commandId) {
        if (!isDenied(ERestriction.ANY_COMMAND)) {
            return false;
        }
        if (complement != null && !complement.isCommandEnabled(commandId)) {
            return false;
        }
        return true;
    }

    /**
     * Returns true if child may be enabled and it's enablement is not meanless
     */
    public synchronized boolean canEnableChild(final Id childId) {
        if (!isDenied(ERestriction.ANY_CHILD)) {
            return false;
        }
        if (complement != null && !complement.isChildEnabled(childId)) {
            return false;
        }
        return true;
    }

    /**
     * Returns true if page may be enabled and it's enablement is not meanless
     */
    public synchronized boolean canEnablePage(final Id pageId) {
        if (!isDenied(ERestriction.ANY_PAGES)) {
            return false;
        }
        if (complement != null && !complement.isPageEnabled(pageId)) {
            return false;
        }
        return true;
    }

    /**
     * Returns enums set with current restriction settings
     */
    public synchronized EnumSet<ERestriction> getRestriction() {
        return EnumSet.copyOf(getSourceRestrictionMask());
    }

    /**
     * Returns enabled commmand ids or null if commands not supported
     */
    public synchronized List<Id> getEnabledCommandIds() {
        return getSourceEnabledCommandsIds() == null ? null : new ArrayList<Id>(getSourceEnabledCommandsIds());
    }

    /**
     * Returns enabled children ids or null if children not supported
     */
    public synchronized List<Id> getEnabledChildIds() {
        return getSourceEnabledChildsIds() == null ? null : new ArrayList<Id>(getSourceEnabledChildsIds());
    }

    /**
     * Returns enabled pages ids or null if pages not supported
     */
    public synchronized List<Id> getEnabledPageIds() {
        return getSourceEnabledPagesIds() == null ? null : new ArrayList<Id>(getSourceEnabledPagesIds());
    }

    public class EnabledCommandsOrder {

        public boolean moveUp(Id commandId) {
            return moveCommandId(commandId, -1);
        }

        public boolean moveDn(Id commandId) {
            return moveCommandId(commandId, 1);
        }
    }

    public EnabledCommandsOrder getCommandsOrder() {
        return new EnabledCommandsOrder();
    }

    private boolean moveCommandId(Id id, int dir) {
        if (isReadOnly()) {
            return false;

        }
        if (source != null) {
            return source.moveCommandId(id, dir);
        } else {
            int index = enabledCommandIds.indexOf(id);
            if (index >= 0) {
                if (index + dir >= 0 && index + dir < enabledCommandIds.size()) {
                    Id oldid = enabledCommandIds.get(index + dir);
                    enabledCommandIds.set(index + dir, id);
                    enabledCommandIds.set(index, oldid);
                    setEditState(EEditState.MODIFIED);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean updateEditState(EEditState newState) {
        if (source != null) {
            return source.setEditState(newState);
        } else {
            return super.setEditState(newState);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        if (source != null) {
            sb.append("\nSource restriction: ").append(source.getQualifiedName());
            sb.append("\n");
            sb.append(source.toString());
            sb.append("------------------------------------------------------\n");
        }
        if (complement != null) {
            sb.append("\nComplement restriction: ").append(complement.getQualifiedName());
            sb.append("\n");
            sb.append(complement.toString());
            sb.append("------------------------------------------------------\n");
        }
        sb.append("\nOwn Options:\n");
        if (restriction != null) {
            for (ERestriction r : restriction) {
                sb.append(r.name());
                sb.append(" ");
            }
        } else {
            sb.append("<Not Defined>");
        }
        return sb.toString();
    }

    public static boolean equalIgnoringOrder(Restrictions r1, Restrictions r2) {
        if (r1 == null && r2 == null) {
            return true;
        }
        if (r1 == null || r2 == null) {
            return false;
        }
        if (r1.toBitField() == r2.toBitField()) {
            if (r1.isDenied(ERestriction.ANY_CHILD)) {
                if (!Utils.equalsCollection(r1.enabledChildIds, r2.enabledChildIds)) {
                    return false;
                }
            }
            if (r1.isDenied(ERestriction.ANY_COMMAND)) {
                if (!Utils.equalsCollection(r1.enabledCommandIds, r2.enabledCommandIds)) {
                    return false;
                }
            }
            if (r1.isDenied(ERestriction.ANY_PAGES)) {
                if (!Utils.equalsCollection(r1.enabledPageIds, r2.enabledPageIds)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public JavaSourceSupport getJavaSourceSupport() {
        return new JavaSourceSupport(this) {

            @Override
            public CodeWriter getCodeWriter(final UsagePurpose purpose) {
                return new RestrictionsWriter(this, Restrictions.this, purpose);
            }
        };
    }

    public long toBitField() {
        return ERestriction.toBitField(getRestriction());
    }
}
