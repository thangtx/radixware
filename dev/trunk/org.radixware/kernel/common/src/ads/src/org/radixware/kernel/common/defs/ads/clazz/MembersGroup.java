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

package org.radixware.kernel.common.defs.ads.clazz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsClipboardSupport;
import org.radixware.kernel.common.defs.ads.AdsDefinitions;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.enums.ENamingPolicy;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.common.utils.events.IRadixEventListener;
import org.radixware.kernel.common.utils.events.RadixEventSource;
import org.radixware.schemas.adsdef.MemberGroup;


public abstract class MembersGroup<T extends AdsDefinition & IAdsClassMember> extends AdsClassMember {

    public static final class MembershipChangedEvent<T> extends RadixEvent {
    }

    public interface MembershipChangesListener extends IRadixEventListener<MembershipChangedEvent> {
    }
    private RadixEventSource<MembershipChangesListener, MembershipChangedEvent> membershipChangesSupport = null;

    public synchronized RadixEventSource<MembershipChangesListener, MembershipChangedEvent> getMembershipChangesSupport() {
        if (membershipChangesSupport == null) {
            membershipChangesSupport = new RadixEventSource<MembershipChangesListener, MembershipChangedEvent>();
        }
        return membershipChangesSupport;
    }

    public MembersGroup(MemberGroup xDef) {
        super(xDef);
        if (xDef != null) {
            List<Id> members = xDef.getMembers();
            if (members != null && !members.isEmpty()) {
                this.memberIds = new ArrayList<Id>(members);
            }
        }
    }

    @Override
    public ENamingPolicy getNamingPolicy() {
        return ENamingPolicy.FREE; //To change body of generated methods, choose Tools | Templates.
    }
    
    

    public static final class OwnerClassChangedEvent extends RadixEvent {
    }

    public interface IOwnerClassChangesListener extends IRadixEventListener<OwnerClassChangedEvent> {
    }
    private RadixEventSource<IOwnerClassChangesListener, OwnerClassChangedEvent> ownerClassChangesSupport = null;

    public synchronized RadixEventSource<IOwnerClassChangesListener, OwnerClassChangedEvent> getOwnerClassChangeSupport() {
        if (ownerClassChangesSupport == null) {
            ownerClassChangesSupport = new RadixEventSource<IOwnerClassChangesListener, OwnerClassChangedEvent>();
        }
        return ownerClassChangesSupport;
    }

    protected synchronized void fireOwnerClassChange() {
        if (ownerClassChangesSupport != null) {
            ownerClassChangesSupport.fireEvent(new OwnerClassChangedEvent());
        }
    }

    public MembersGroup(Id id, String name) {
        super(id, name);
    }
    private ArrayList<Id> memberIds = null;

    protected final MembersGroup findMemberOwner(T item) {
        return findMemberOwner(item.getId());
    }

    protected abstract MembersGroup findMemberOwner(Id memberId);

    protected abstract T findMember(Id memberId);

    @SuppressWarnings({"unchecked", "unchecked"})
    public synchronized boolean addMember(T item) {
        if (item == null) {
            return false;
        }
        if (memberIds == null) {
            memberIds = new ArrayList<Id>(10);
        }
        if (memberIds.contains(item.getId())) {
            return false;
        } else {
            removeFromOldOwner(item.getId());

            memberIds.add(item.getId());
            setEditState(EEditState.MODIFIED);
            if (membershipChangesSupport != null) {
                membershipChangesSupport.fireEvent(new MembershipChangedEvent<>());
            }
            return true;
        }
    }

    @SuppressWarnings("unchecked")
    private void removeFromOldOwner(Id id) {
        final MembersGroup group = findMemberOwner(id);
        if (group != null) {
            synchronized (group) {
                if (group.memberIds != null) {
                    group.memberIds.remove(id);
                    group.setEditState(EEditState.MODIFIED);
                    if (group.membershipChangesSupport != null) {
                        group.membershipChangesSupport.fireEvent(new MembershipChangedEvent<>());
                    }
                }
            }
        }
    }

    public abstract boolean isRegisteredInGroups(T item);

    public synchronized boolean removeMember(T item) {
        if (memberIds == null) {
            return false;
        }

        if (memberIds.remove(item.getId())) {
            if (membershipChangesSupport != null) {
                membershipChangesSupport.fireEvent(new MembershipChangedEvent());
            }
            afterRemoveMember(item);
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void setContainer(RadixObject container) {
        super.setContainer(container);
        fireOwnerClassChange();
    }

    /**
     * Removes id from list of members. NOTE: this method does not moves member
     * to root group
     */
    public synchronized boolean removeMemberId(Id id) {
        if (memberIds == null) {
            return false;
        }
        T member = findMember(id);
        if (member != null) {
            return removeMember(member);
        } else {
            if (memberIds.remove(id)) {
                setEditState(EEditState.MODIFIED);
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean containsMember(Id id) {
        synchronized (this) {
            return memberIds != null && memberIds.contains(id);
        }
    }

    public synchronized List<Id> getMemberIds() {
        if (memberIds == null) {
            return Collections.emptyList();
        } else {
            return new ArrayList<Id>(memberIds);
        }
    }

    private boolean isRootGroup() {
        AdsClassDef ownerClass = getOwnerClass();
        return ownerClass != null && (this == (MembersGroup) ownerClass.getPropertyGroup()
                || this == (MembersGroup) ownerClass.getMethodGroup());
    }

    protected void appendTo(MemberGroup g, ESaveMode saveMode) {
        super.appendTo(g, saveMode);

        if (!isRootGroup()) {
            if (memberIds != null && !memberIds.isEmpty()) {
                ArrayList<Id> res = new ArrayList<Id>();
                for (Id id : memberIds) {
                    if (res.indexOf(id) < 0) {
                        res.add(id);
                    }
                }
                g.setMembers(res);
            }
        }
    }

    protected abstract void afterRemoveMember(T item);

    @Override
    protected boolean isQualifiedNamePart() {
        return false;
    }

    public synchronized void afterPaste() {

        // remove members from other (old) group
        if (memberIds != null) {
            final ArrayList<Id> ids = memberIds;
            memberIds = null;
            for (Id id : ids) {
                removeFromOldOwner(id);
            }
            memberIds = ids;
        }

        if (membershipChangesSupport != null) {
            membershipChangesSupport.fireEvent(new MembershipChangedEvent());
        }
    }

    public void clearMembers() {
        this.memberIds = null;
        setEditState(EEditState.MODIFIED);
    }

    public void memberDeleted(T item) {
        if (memberIds != null) {
            this.memberIds.remove(item.getId());
        }
    }

    @Override
    public long getFileLastModifiedTime() {
        return super.getFileLastModifiedTime();
    }

    @Override
    public boolean isFinal() {
        return true;
    }

    @Override
    public boolean isPublished() {
        return false;
    }

    @Override
    protected EAccess getDefaultAccess() {
        return EAccess.PRIVATE;
    }

    @Override
    protected boolean getDefaultIsFinal() {
        return true;
    }

    @Override
    public EAccess getAccessMode() {
        return EAccess.PRIVATE;
    }

    @Override
    public boolean isSuitableContainer(AdsDefinitions collection) {
        if (!(collection instanceof Members)) {
            return false;
        }

        Members members = (Members) collection;

        // veto on moving method group between classes
        if (members.getOwnerClass() != getOwnerClass()) {
            return false;
        }

        // veto on moving method group to child group
        MembersGroup ownerGroup = members.getOwnerGroup();
        while (ownerGroup != null) {
            if (ownerGroup == this) {
                return false;
            }

            Members ownerGroupSet = ownerGroup.getOwnerGroupSet();
            ownerGroup = ownerGroupSet != null ? ownerGroupSet.getOwnerGroup() : null;
        }

        return true;
    }

    abstract Members<?> getOwnerGroupSet();

    protected static class MembersGroupClipboardSupport<MemberGroupType extends MembersGroup<?>> extends AdsClipboardSupport<MemberGroupType> {

        public MembersGroupClipboardSupport(MemberGroupType memberGroup) {
            super(memberGroup);
        }

        @Override
        protected XmlObject copyToXml() {
            MemberGroup xDef = MemberGroup.Factory.newInstance();
            getGroup().appendTo(xDef, ESaveMode.NORMAL);
            return xDef;
        }

        protected final MemberGroupType getGroup() {
            return radixObject;
        }
    }
}
