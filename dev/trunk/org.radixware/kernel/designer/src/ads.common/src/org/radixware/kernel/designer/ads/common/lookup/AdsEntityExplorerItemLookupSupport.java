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

import java.util.List;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsClassCatalogDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef;
import org.radixware.kernel.common.defs.ads.common.AdsVisitorProvider;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsEntityExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParagraphExplorerItemDef;
import org.radixware.kernel.common.types.Id;


public class AdsEntityExplorerItemLookupSupport extends AdsSelectorExplorerItemLookupSupport {

    public static class Factory {

        public static AdsEntityExplorerItemLookupSupport newInstance(AdsEntityExplorerItemDef item) {
            return new AdsEntityExplorerItemLookupSupport(item);
        }

        public static AdsEntityExplorerItemLookupSupport newInstance(AdsParagraphExplorerItemDef owner) {
            return new AdsEntityExplorerItemLookupSupport();
        }

        public static AdsEntityExplorerItemLookupSupport newInstance(AdsEditorPresentationDef owner) {
            return new AdsEntityExplorerItemLookupSupport();
        }
    }

    private AdsEntityExplorerItemLookupSupport(AdsEntityExplorerItemDef item) {
        super(item);
    }

    private AdsEntityExplorerItemLookupSupport() {
        super(null);
    }

    /**
     * Returns visitor provider that iterates over available classes
     */
    public VisitorProvider getAvailableClassesProvider() {
        return new AdsVisitorProvider.AdsTopLevelDefVisitorProvider() {

            @Override
            public boolean isTarget(RadixObject object) {
                return object instanceof AdsEntityObjectClassDef;
            }
        };
    }

    /**
     * Returns visitor provider that iterates over available selector presentations
     */
    public VisitorProvider getAvailablePresentationsProvider() {
        if (item != null) {
            final AdsEntityObjectClassDef clazz = item.findReferencedEntityClass();
            if (clazz == null) {
                return new DefaultPresentationLookupProvider();
            }
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
        } else {
            return new DefaultPresentationLookupProvider();
        }
    }

    private class DefaultPresentationLookupProvider extends VisitorProvider {

        @Override
        public boolean isTarget(RadixObject object) {
            return object instanceof AdsSelectorPresentationDef;
        }
    }

    private class DefaultCCLookupProvider extends VisitorProvider {

        @Override
        public boolean isTarget(RadixObject object) {
            return object instanceof AdsClassCatalogDef;
        }
    }

    @Override
    public VisitorProvider getAvailableClassCatalogsProvider() {
        if (item != null) {
            final AdsEntityObjectClassDef clazz = item.findReferencedEntityClass();
            if (clazz == null) {
                return new DefaultCCLookupProvider();
            }
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
            return new DefaultCCLookupProvider();
        }
    }
}
