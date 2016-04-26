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
import org.radixware.kernel.common.defs.ClipboardSupport.DuplicationResolver;
import org.radixware.kernel.common.defs.ClipboardSupport.Transfer;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.*;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.AdsDefinitions;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.MemberGroup;


public class AdsMethodGroup extends MembersGroup<AdsMethodDef> {

    @Override
    protected MembersGroup findMemberOwner(final Id memberId) {
        RadixObject obj = getOwnerClass().getMethodGroup().find(new VisitorProvider() {
            @Override
            public boolean isTarget(RadixObject radixObject) {
                return radixObject instanceof AdsMethodGroup && ((AdsMethodGroup) radixObject).containsMember(memberId);
            }
        });
        if (obj instanceof AdsMethodGroup) {
            return (AdsMethodGroup) obj;
        }
        return null;
    }

    public AdsMethodGroup findGroupByMethodId(Id id) {
        MembersGroup g = findMemberOwner(id);
        if (g instanceof AdsMethodGroup) {
            return (AdsMethodGroup) g;
        } else {
            return null;
        }
    }

    @Override
    protected AdsMethodDef findMember(Id memberId) {
        return getOwnerClass().getMethods().findById(memberId, EScope.ALL).get();
    }

    public static final class Factory {

        /**
         * creates new method group with given name associated with given class
         * defintion NOTE: this methods dont adds created group to class's group
         * list use {@linkplain ChildGroups#addMember(org.radixware.kernel.designer.ads.definitions.algorithmic.clazz.methods.AdsMethodGroup)
         * } to addMember new group
         */
        public static AdsMethodGroup newInstance(String name) {
            return new AdsMethodGroup(name);
        }

        protected static final AdsMethodGroup newInstance(AdsClassDef owner) {
            AdsMethodGroup g = new AdsMethodGroup("Root");
            g.setContainer(owner);
            return g;
        }

        static AdsMethodGroup loadFrom(AdsClassDef owner, MemberGroup xDef) {
            if (xDef == null) {
                return newInstance(owner);
            }
            AdsMethodGroup g = new AdsMethodGroup(xDef);
            g.setContainer(owner);
            return g;
        }

        public static AdsMethodGroup loadFrom(MemberGroup xDef) {

            return new AdsMethodGroup(xDef);
        }
    }
    private final MethodGroups childGroups;

    private AdsMethodGroup(MemberGroup xDef) {
        super(xDef);
        this.childGroups = new MethodGroups(this, xDef);
    }

    private AdsMethodGroup(String name) {
        super(Id.Factory.newInstance(EDefinitionIdPrefix.ADS_METHOD_GROUP), name);
        this.childGroups = new MethodGroups(this);
    }

    /**
     * @return child group set
     */
    public MethodGroups getChildGroups() {
        return childGroups;
    }

    @Override
    public void appendTo(MemberGroup xDef, ESaveMode saveMode) {
        super.appendTo(xDef, saveMode);
        if (this.childGroups != null) {
            this.childGroups.appendTo(xDef, saveMode);
        }
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        if (childGroups != null) {
            childGroups.visit(visitor, provider);
        }
    }

    @Override
    public boolean isRegisteredInGroups(AdsMethodDef item) {
        synchronized (this) {
            if (this.getMemberIds().contains(item.getId())) {
                return true;
            }
            if (childGroups != null) {
                for (AdsMethodGroup g : childGroups) {
                    if (g.isRegisteredInGroups(item)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    protected MethodGroups getOwnerGroupSet() {
        RadixObject container = getContainer();

        if (container instanceof MethodGroups) {
            return (MethodGroups) getContainer();
        }
        return null;
    }

    public List<AdsMethodDef> listMembers() {
        if (getOwnerClass() == null) {
            return Collections.emptyList();
        }
        List<Id> ids = getMemberIds();
        ArrayList<AdsMethodDef> list = new ArrayList<>(ids.size());
        for (Id id : ids) {
            AdsClassDef ownerClass = getOwnerClass();
            if (ownerClass != null) {
                AdsMethodDef method = ownerClass.getMethods().findById(id, EScope.LOCAL).get();
                if (method != null) {
                    list.add(method);
                }
            }
        }
        return list;
    }

    @Override
    protected void afterRemoveMember(AdsMethodDef item) {
        if (item != null) {
            ArrayList<AdsMethodGroup> owners = new ArrayList<>();
            getOwnerClass().getMethodGroup().collectOwners(item, owners);
            if (owners.isEmpty() && getOwnerClass() != null) {
                getOwnerClass().getMethodGroup().addMember(item);
            }
        }
    }

    private void collectOwners(AdsMethodDef item, ArrayList<AdsMethodGroup> owners) {
        synchronized (this) {
            if (this.getMemberIds().contains(item.getId())) {
                owners.add(this);
            }
            if (childGroups != null) {
                for (AdsMethodGroup g : this.childGroups) {
                    g.collectOwners(item, owners);
                }
            }
        }
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.METHOD_GROUP;
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);
//        List<Id> ids = getMemberIds();
//        if (ids != null) {
//            for (Id id : ids) {
//                AdsMethodDef method = getOwnerClass().getMethods().findById(id, EScope.ALL);
//                if (method != null) {
//                    list.add(method);
//                }
//            }
//        }
    }

    @Override
    public ClipboardSupport<AdsMethodGroup> getClipboardSupport() {
        return new MethodGroupClipboardSupport(this);
    }

    @Override
    public boolean isSuitableContainer(AdsDefinitions collection) {
        return super.isSuitableContainer(collection)
                && collection instanceof MethodGroups;
    }

    @Override
    protected synchronized void fireOwnerClassChange() {
        super.fireOwnerClassChange();
        if (childGroups != null) {
            childGroups.fireOwnerClassChange();
        }
    }

    @Override
    public EDefType getDefinitionType() {
        return EDefType.METHOD_GROUP;
    }

    @Override
    public void memberDeleted(AdsMethodDef item) {
        super.memberDeleted(item);
        if (childGroups != null) {
            for (AdsMethodGroup mg : childGroups) {
                mg.memberDeleted(item);
            }
        }
    }

    private static final class MethodGroupClipboardSupport extends MembersGroupClipboardSupport<AdsMethodGroup> {

        public MethodGroupClipboardSupport(AdsMethodGroup group) {
            super(group);
        }

        @Override
        protected AdsMethodGroup loadFrom(XmlObject xmlObject) {
            if (xmlObject instanceof MemberGroup) {
                return AdsMethodGroup.Factory.loadFrom((MemberGroup) xmlObject);
            } else {
                return super.loadFrom(xmlObject);
            }
        }

        @Override
        public void paste(List<Transfer> transfers, DuplicationResolver resolver) {
            final List<Transfer> methods = new ArrayList<>();
            final List<Transfer> groups = new ArrayList<>();

            for (Transfer transfer : transfers) {
                final RadixObject object = transfer.getObject();

                if (object instanceof AdsMethodGroup) {
                    groups.add(transfer);
                } else if (object instanceof AdsMethodDef) {
                    methods.add(transfer);
                }
            }
            AdsClassDef ownerClass = getGroup().getOwnerClass();

            getGroup().getChildGroups().getClipboardSupport().paste(groups, resolver);
            ownerClass.getMethods().getClipboardSupport().paste(methods, resolver);

            for (Transfer mdTransfer : methods) {
                AdsMethodDef method = (AdsMethodDef) mdTransfer.getObject();
                if (method.getOwnerClass() == ownerClass) {
                    getGroup().addMember(method);
                }
            }
        }
    }
}
