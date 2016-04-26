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

package org.radixware.kernel.common.defs.ads.enumeration;

import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsApplicationClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsDetailColumnPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsTablePropertyDef;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.type.AdsEnumType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.ads.AdsSegment;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;


public class AdsEnumUtils {

    private AdsEnumUtils() {
    }

//    private static class EnumSearcher implements IVisitor {
//
//        private final Id columnId;
//        public AdsEnumDef result = null;
//
//        public EnumSearcher(Id columnId) {
//            this.columnId = columnId;
//        }
//
//        @Override
//        public void accept(RadixObject radixObject) {
//            if (radixObject instanceof AdsEntityClassDef) {
//                final AdsEntityClassDef aec = (AdsEntityClassDef) radixObject;
//                final AdsPropertyDef property = aec.getProperties().findById(columnId, EScope.LOCAL);
//                if (property != null) {
//                    final AdsType type = property.getValue().getType().resolve(property);
//                    if (type instanceof AdsEnumType) {
//                        result = ((AdsEnumType) type).getSource();
//                    }
//                }
//            }
//        }
//    }
    private static AdsEnumDef findEnumByTableAndColumnId(final Layer layer, final DdsTableDef table, final Id columnId) {
        final Id tableId = table.getId();

        // optimization instead of visit
        final AdsSegment ads = (AdsSegment) layer.getAds();
        for (AdsModule module : ads.getModules()) {
            for (AdsDefinition def : module.getDefinitions()) {
                if (def instanceof AdsEntityClassDef) {
                    final AdsEntityClassDef entity = (AdsEntityClassDef) def;
                    if (Utils.equals(entity.getEntityId(), tableId)) {
                        final AdsPropertyDef prop = entity.getProperties().getLocal().findById(columnId);
                        if (prop != null) {
                            return findPropertyEnum(prop);
                        } else {
                            return null;
                        }
                    }
                }
            }
        }

        if (table.isDetailTable()) {
            for (AdsModule module : ads.getModules()) {
                for (AdsDefinition def : module.getDefinitions()) {
                    if (def instanceof AdsApplicationClassDef) {
                        final AdsApplicationClassDef app = (AdsApplicationClassDef) def;
                        for (AdsPropertyDef prop : app.getProperties().getLocal()) {
                            if (prop instanceof AdsDetailColumnPropertyDef) {
                                final AdsDetailColumnPropertyDef detailProp = (AdsDetailColumnPropertyDef) prop;
                                if (Utils.equals(detailProp.getColumnInfo().getColumnId(), columnId)) {
                                    return findPropertyEnum(prop);
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public static AdsEnumDef findColumnEnum(DdsColumnDef column) {
        if (!column.isInBranch()) {
            return null;
        }
        final Id columnId = column.getId();
        final DdsTableDef table = column.getOwnerTable();
        final Layer layer = column.getModule().getSegment().getLayer();

        // only in current layer, because upper layer can be different in different clients
        return findEnumByTableAndColumnId(layer, table, columnId);
    }

    public static AdsEnumDef findPropertyEnum(AdsPropertyDef property) {
        final AdsType type = property.getValue().getType().resolve(property).get();
        if (type instanceof AdsEnumType) {
            return ((AdsEnumType) type).getSource();
        } else {
            return null;
        }
    }
}
