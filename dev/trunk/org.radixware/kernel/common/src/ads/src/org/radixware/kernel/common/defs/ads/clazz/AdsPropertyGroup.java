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
import org.radixware.kernel.common.defs.ClipboardSupport;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinitions;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.schemas.adsdef.MemberGroup;


public class AdsPropertyGroup extends MembersGroup<AdsPropertyDef> {

    public static class Factory {

        public static final AdsPropertyGroup loadFrom(MemberGroup xDef) {
            return new AdsPropertyGroup(xDef);
        }

        /**
         * creates new Property group with given name associated with given
         * class defintion NOTE: this Properties dont adds created group to
         * class's group list use {@linkplain ChildGroups#addMember(org.radixware.kernel.designer.ads.definitions.algorithmic.clazz.Properties.AdsPropertyGroup)
         * } to register new group
         */
        public static final AdsPropertyGroup newInstance(String name) {
            return new AdsPropertyGroup(name);
        }

        static final AdsPropertyGroup newInstance(AdsClassDef owner) {
            AdsPropertyGroup g = new AdsPropertyGroup("Root");
            g.setContainer(owner);
            return g;
        }

        static final AdsPropertyGroup loadFrom(AdsClassDef owner, MemberGroup xDef) {
            if (xDef == null) {
                return newInstance(owner);
            }
            AdsPropertyGroup g = new AdsPropertyGroup(xDef);
            g.setContainer(owner);
            return g;
        }
    }
    private final PropertyGroups childGroups;

    @Override
    protected MembersGroup findMemberOwner(final Id itemId) {
        RadixObject obj = getOwnerClass().getPropertyGroup().find(new VisitorProvider() {
            @Override
            public boolean isTarget(RadixObject radixObject) {
                return radixObject instanceof AdsPropertyGroup && ((AdsPropertyGroup) radixObject).containsMember(itemId);
            }
        });
        if (obj instanceof AdsPropertyGroup) {
            return (AdsPropertyGroup) obj;
        }
        return null;
    }

    @Override
    protected AdsPropertyDef findMember(Id memberId) {
        if (getOwnerClass() == null) {
            return null;
        }
        return getOwnerClass().getProperties().findById(memberId, EScope.ALL).get();
    }

    protected AdsPropertyGroup(MemberGroup xDef) {
        super(xDef);
        this.childGroups = new PropertyGroups(this, xDef);
    }

    protected AdsPropertyGroup(String name) {
        super(Id.Factory.newInstance(EDefinitionIdPrefix.ADS_PROPERTY_GROUP), name);
        this.childGroups = new PropertyGroups(this);
    }

    /**
     * @return child group set
     */
    public PropertyGroups getChildGroups() {
        return childGroups;
    }

    @Override
    public void appendTo(MemberGroup xDef, ESaveMode saveMode) {
        super.appendTo(xDef, saveMode);
        this.childGroups.appendTo(xDef, saveMode);
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        childGroups.visit(visitor, provider);
    }

    @Override
    protected PropertyGroups getOwnerGroupSet() {
        RadixObject container = getContainer();

        if (container instanceof PropertyGroups) {
            return (PropertyGroups) getContainer();
        }
        return null;
    }

    public List<AdsPropertyDef> listMembers() {
        if (getOwnerClass() == null) {
            return Collections.emptyList();
        }
        List<Id> ids = getMemberIds();
        ArrayList<AdsPropertyDef> list = new ArrayList<AdsPropertyDef>(ids.size());
        for (Id id : ids) {
            AdsPropertyDef prop = getOwnerClass().getProperties().findById(id, EScope.LOCAL).get();
            if (prop != null) {
                list.add(prop);
            }
        }
        return list;
    }

    @Override
    public boolean isRegisteredInGroups(AdsPropertyDef item) {
        synchronized (this) {
            if (this.getMemberIds().contains(item.getId())) {
                return true;
            }
            if (childGroups != null) {
                for (AdsPropertyGroup g : childGroups) {
                    if (g.isRegisteredInGroups(item)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    protected void afterRemoveMember(AdsPropertyDef item) {
        if (item != null) {
            ArrayList<AdsPropertyGroup> owners = new ArrayList<AdsPropertyGroup>();
            getOwnerClass().getPropertyGroup().collectOwners(item, owners);
            if (owners.isEmpty() && getOwnerClass() != null) {
                getOwnerClass().getPropertyGroup().addMember(item);
            }
        }
    }

    private void collectOwners(AdsPropertyDef item, ArrayList<AdsPropertyGroup> owners) {
        synchronized (this) {
            if (this.getMemberIds().contains(item.getId())) {
                owners.add(this);

            }
            if (childGroups != null) {
                for (AdsPropertyGroup g : this.childGroups) {
                    g.collectOwners(item, owners);
                }
            }
        }
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.PROPERTY_GROUP;
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);
//        List<Id> ids = getMemberIds();
//        if (ids != null) {
//            for (Id id : ids) {
//                AdsPropertyDef property = getOwnerClass().getProperties().findById(id, EScope.ALL);
//                if (property != null) {
//                    list.add(property);
//                }
//            }
//        }
    }

    @Override
    public ClipboardSupport<AdsPropertyGroup> getClipboardSupport() {
        return new PropertyGroupClipboardSupport(this);
    }

    @Override
    public boolean isSuitableContainer(AdsDefinitions collection) {
        return super.isSuitableContainer(collection)
                && collection instanceof PropertyGroups;
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
        return EDefType.PROPERTY_GROUP;
    }

    @Override
    public void memberDeleted(AdsPropertyDef item) {
        super.memberDeleted(item);
        if (childGroups != null) {
            for (AdsPropertyGroup mg : childGroups) {
                mg.memberDeleted(item);
            }
        }
    }

    private static final class PropertyGroupClipboardSupport extends MembersGroupClipboardSupport<AdsPropertyGroup> {

        public PropertyGroupClipboardSupport(AdsPropertyGroup group) {
            super(group);
        }

        @Override
        protected AdsPropertyGroup loadFrom(XmlObject xmlObject) {
            if (xmlObject instanceof MemberGroup) {
                return AdsPropertyGroup.Factory.loadFrom((MemberGroup) xmlObject);
            } else {
                return super.loadFrom(xmlObject);
            }
        }

        @Override
        public void paste(List<Transfer> transfers, DuplicationResolver resolver) {
            final List<Transfer> properties = new ArrayList<Transfer>();
            final List<Transfer> groups = new ArrayList<Transfer>();

            for (Transfer transfer : transfers) {
                final RadixObject object = transfer.getObject();

                if (object instanceof AdsPropertyGroup) {
                    groups.add(transfer);
                } else if (object instanceof AdsPropertyDef) {
                    properties.add(transfer);
                }
            }
            AdsClassDef ownerClass = getGroup().getOwnerClass();

            getGroup().childGroups.getClipboardSupport().paste(groups, resolver);
            ownerClass.getProperties().getClipboardSupport().paste(properties, resolver);

            for (Transfer prop : properties) {
                AdsPropertyDef property = (AdsPropertyDef) prop.getObject();
                if (property.getOwnerClass() == ownerClass) {
                    getGroup().addMember(property);
                }
            }
        }
    }
}
