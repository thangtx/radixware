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

package org.radixware.kernel.designer.ads.common.lookup;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef;
import org.radixware.kernel.common.defs.ads.command.AdsCommandDef;
import org.radixware.kernel.common.defs.ads.common.AdsVisitorProvider;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParentRefExplorerItemDef;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.types.Id;


public class AdsParentRefExplorerItemLookupSupport extends AdsCommandsLookupSupport {

    public static class Factory {

        public static AdsParentRefExplorerItemLookupSupport newInstance(AdsParentRefExplorerItemDef item) {
            return new AdsParentRefExplorerItemLookupSupport(item);
        }

        public static AdsParentRefExplorerItemLookupSupport newInstance(AdsEditorPresentationDef epr) {
            return new AdsParentRefExplorerItemLookupSupport(epr.getOwnerClass());
        }
    }
    private AdsParentRefExplorerItemDef item = null;
    private AdsEntityObjectClassDef ownerClass = null;

    private AdsParentRefExplorerItemLookupSupport(AdsParentRefExplorerItemDef item) {
        this.item = item;
    }

    private AdsParentRefExplorerItemLookupSupport(AdsEntityObjectClassDef clazz) {
        this.ownerClass = clazz;
    }

    /**
     * Returns visitor provider that iterates over available classes
     */
    public VisitorProvider getAvailableClassesProvider() {
        if (item != null && item.getParentReferenceId() != null) {
            AdsEntityObjectClassDef clazz = item.findReferencedEntityClass();
            final Id entityId = clazz == null ? null : clazz.getEntityId();
            if (entityId == null) {
                return new StubVisitorProvider();
            }
            return new AdsVisitorProvider.AdsTopLevelDefVisitorProvider() {
                @Override
                public boolean isTarget(RadixObject object) {
                    return object instanceof AdsEntityObjectClassDef && ((AdsEntityObjectClassDef) object).getEntityId() == entityId;
                }
            };
        } else if (ownerClass != null) {
            DdsTableDef table = ownerClass.findTable(ownerClass);
            if (table == null) {
                return new StubVisitorProvider();
            } else {
                Set<DdsReferenceDef> refs = table.collectIncomingReferences();

                final Set<Id> childTableIds = new HashSet<Id>();

                for (DdsReferenceDef ref : refs) {
                    childTableIds.add(ref.getParentTableId());
                }
                return new AdsVisitorProvider.AdsTopLevelDefVisitorProvider() {
                    @Override
                    public boolean isTarget(RadixObject object) {
                        return object instanceof AdsEntityObjectClassDef && childTableIds.contains(((AdsEntityObjectClassDef) object).getEntityId());
                    }
                };
            }
        } else {
            return new StubVisitorProvider();
        }
    }

    /**
     * Returns visitor provider that iterates over available selector
     * presentations
     */
    public VisitorProvider getAvailablePresentationsProvider() {
        if (item != null && item.getParentReferenceId() != null && item.getContainer() != null) {
            final AdsEntityObjectClassDef clazz = item.findReferencedEntityClass();

            final Id entityId = clazz == null ? null : clazz.getEntityId();
            if (entityId == null) {
                return new StubVisitorProvider();
            }
            final List<Id> classIds = Utils.familyClassIds(clazz);
            return new AdsVisitorProvider() {
                @Override
                public boolean isTarget(RadixObject object) {
                    if (object instanceof AdsEditorPresentationDef) {
                        AdsEntityObjectClassDef pc = ((AdsEditorPresentationDef) object).getOwnerClass();
                        return pc.getEntityId() == entityId && classIds.contains(pc.getId());
                    } else {
                        return false;
                    }
                }
            };
        } else if (ownerClass != null) {
            DdsTableDef table = ownerClass.findTable(ownerClass);
            if (table == null) {
                return new StubVisitorProvider();
            } else {
                Set<DdsReferenceDef> refs = table.collectIncomingReferences();

                final Set<Id> childTableIds = new HashSet<Id>();

                for (DdsReferenceDef ref : refs) {
                    childTableIds.add(ref.getParentTableId());
                }
                return new AdsVisitorProvider.AdsTopLevelDefVisitorProvider() {
                    @Override
                    public boolean isTarget(RadixObject object) {
                        if (object instanceof AdsSelectorPresentationDef) {
                            AdsEntityObjectClassDef pc = ((AdsEditorPresentationDef) object).getOwnerClass();
                            return childTableIds.contains(pc.getEntityId());
                        } else {
                            return false;
                        }
                    }
                };
            }
        } else {
            return new StubVisitorProvider();
        }
    }

    @Override
    public Collection<AdsCommandDef> getAvailableCommandList() {
        return getAvailableCommandList(item);
    }
}
