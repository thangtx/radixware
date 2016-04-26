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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsClassCatalogDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef;
import org.radixware.kernel.common.defs.ads.common.AdsVisitorProvider;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsChildRefExplorerItemDef;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.types.Id;


public class AdsChildRefExplorerItemLookupSupport extends AdsSelectorExplorerItemLookupSupport {

    public static class Factory {

        public static AdsChildRefExplorerItemLookupSupport newInstance(AdsChildRefExplorerItemDef item) {
            return new AdsChildRefExplorerItemLookupSupport(item);
        }

        public static AdsChildRefExplorerItemLookupSupport newInstance(AdsEditorPresentationDef epr) {
            return new AdsChildRefExplorerItemLookupSupport(epr.getOwnerClass());
        }
    }
    private AdsEntityObjectClassDef ownerClass = null;
    private AdsChildRefExplorerItemDef childRefItem;

    private AdsChildRefExplorerItemLookupSupport(AdsChildRefExplorerItemDef item) {
        super(item);
        this.childRefItem = item;
    }

    private AdsChildRefExplorerItemLookupSupport(AdsEntityObjectClassDef clazz) {
        super(null);
        this.ownerClass = clazz;
    }

    /**
     * Returns visitor provider that iterates over available classes
     */
    public VisitorProvider getAvailableClassesProvider() {
        if (childRefItem != null && childRefItem.getChildReferenceId() != null) {
            AdsEntityObjectClassDef clazz = childRefItem.findReferencedEntityClass();
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
                Set<DdsReferenceDef> refs = table.collectOutgoingReferences();

                final Set<Id> childTableIds = new HashSet<Id>();

                for (DdsReferenceDef ref : refs) {
                    childTableIds.add(ref.getChildTableId());
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
     * Returns visitor provider that iterates over available selector presentations
     */
    @Override
    public VisitorProvider getAvailablePresentationsProvider() {
        if (childRefItem != null && childRefItem.getChildReferenceId() != null && childRefItem.getContainer() != null) {
            final AdsEntityObjectClassDef clazz = childRefItem.findReferencedEntityClass();
            final Id entityId = clazz == null ? null : clazz.getEntityId();
            if (entityId == null) {
                return new StubVisitorProvider();
            }
            final List<Id> classIds = Utils.familyClassIds(clazz);
            return new AdsVisitorProvider() {

                @Override
                public boolean isTarget(RadixObject object) {
                    if (object instanceof AdsSelectorPresentationDef) {
                        AdsEntityObjectClassDef pc = ((AdsSelectorPresentationDef) object).getOwnerClass();
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
                Set<DdsReferenceDef> refs = table.collectOutgoingReferences();

                final Set<Id> childTableIds = new HashSet<Id>();

                for (DdsReferenceDef ref : refs) {
                    childTableIds.add(ref.getChildTableId());
                }
                return new AdsVisitorProvider.AdsTopLevelDefVisitorProvider() {

                    @Override
                    public boolean isTarget(RadixObject object) {
                        if (object instanceof AdsSelectorPresentationDef) {
                            AdsEntityObjectClassDef pc = ((AdsSelectorPresentationDef) object).getOwnerClass();
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

    public VisitorProvider getAvailableClassCatalogsProvider() {
        if (childRefItem != null && childRefItem.getChildReferenceId() != null && childRefItem.getContainer() != null) {
            final AdsEntityObjectClassDef clazz = childRefItem.findReferencedEntityClass();
            final Id entityId = clazz == null ? null : clazz.getEntityId();
            if (entityId == null) {
                return new StubVisitorProvider();
            }
            final List<Id> classIds = Utils.familyClassIds(clazz);
            return new AdsVisitorProvider() {

                @Override
                public boolean isTarget(RadixObject object) {
                    if (object instanceof AdsClassCatalogDef) {
                        AdsEntityObjectClassDef pc = ((AdsClassCatalogDef) object).getOwnerClass();
                        return pc.getEntityId() == entityId && classIds.contains(pc.getId());
                    } else {
                        return false;
                    }
                }
            };
        } else {
            return new StubVisitorProvider();
        }
    }
}
