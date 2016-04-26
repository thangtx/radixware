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

package org.radixware.kernel.common.builder.check.ads.rs;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.radixware.kernel.common.builder.check.common.RadixObjectChecker;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.ColumnInfo;
import org.radixware.kernel.common.defs.ads.clazz.members.ColumnProperty;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.ads.type.AdsEnumType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.dds.DdsAccessPartitionFamilyDef;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.EAccessAreaType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;


@RadixObjectCheckerRegistration
public class AdsEntityClassAccessAreasChecker extends RadixObjectChecker<AdsEntityClassDef.AccessAreas> {

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return AdsEntityClassDef.AccessAreas.class;
    }

    @Override
    public void check(AdsEntityClassDef.AccessAreas areas, IProblemHandler problemHandler) {
        super.check(areas, problemHandler);
        List<String> priorNames = new ArrayList<String>();
        checkInheritedAreas(areas, areas, new HashMap<Id, AdsEntityClassDef>(), problemHandler, priorNames);

        if (areas.getType() == EAccessAreaType.OWN || areas.getType() == EAccessAreaType.INHERITED) {
            if (priorNames.size() > AdsEntityClassDef.AccessAreas.MAX_AREAS_COUNT) {
                error(areas, problemHandler, "Too much number of access areas for a single object");
            }

            for (AdsEntityClassDef.AccessAreas.AccessArea area : areas) {
                AdsEntityClassDef.AccessAreas.AccessArea.Partitions partitions = area.getPartitions();
                if (partitions.isEmpty()) {
                    error(area, problemHandler, "No access partitions defined");
                } else {
                    for (AdsEntityClassDef.AccessAreas.AccessArea.Partition p : partitions) {
                        DdsAccessPartitionFamilyDef family = p.findApf();
                        if (family == null) {
                            error(p, problemHandler, "Can not find access partition family");
                        } else {
                            AdsPropertyDef prop = p.findReferencedProperty();
                            AdsEntityClassDef clazz = p.findReferencedClass();
//                            if (clazz!=null && clazz.getAccessAreas().getType().equals(EAccessAreaType.NONE))
//                                {
//                                error(p, problemHandler, "Incorrect parent entity class");
//                                }
                            if (prop == null) {
                                if (clazz != null) {
                                    error(p, problemHandler, MessageFormat.format("Can not find referenced property in class {0}", clazz.getQualifiedName()));
                                } else {
                                    error(p, problemHandler, "Can not find referenced class");
                                }
                            } else {
                                Definition head = family.findHead();
                                if (head instanceof AdsEnumDef) {
                                    AdsType type = prop.getValue().getType().resolve(area.getOwnerClass()).get();

                                    boolean invalidType = true;
                                    if (type instanceof AdsEnumType) {
                                        AdsEnumDef adsEnum = ((AdsEnumType) type).getSource();
                                        if (Utils.equals(head.getId(), adsEnum.getId())) {
                                            invalidType = false;
                                        }
                                    }
                                    if (invalidType) {
                                        error(p, problemHandler, MessageFormat.format("Property type should be based on ADS enumeration based on DDS enumeration {0}", head.getQualifiedName()));
                                    }
                                } else if (head instanceof DdsTableDef) {
                                    if (prop instanceof ColumnProperty) {
                                        ColumnInfo info = ((ColumnProperty) prop).getColumnInfo();
                                        if (!info.isPrimaryKey()) {
                                            error(p, problemHandler, MessageFormat.format("Referenced property is not a part of primary key of table {0}", head.getQualifiedName()));
                                        }
                                    }
                                } else {
                                    error(p, problemHandler, "Invalid access partition family head");
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void checkInheritedAreas(AdsEntityClassDef.AccessAreas areas, AdsEntityClassDef.AccessAreas problemSource, HashMap<Id, AdsEntityClassDef> map, IProblemHandler problemHandler, List<String> priorAreaNames) {

        map.put(areas.getOwnerClass().getId(), areas.getOwnerClass());
        for (AdsEntityClassDef.AccessAreas.AccessArea area : areas) {
            if (priorAreaNames.contains(area.getName())) {
                error(problemSource, problemHandler, MessageFormat.format("Dublicate access area name {0}", area.getName()));
            }
            priorAreaNames.add(area.getName());
        }
        if (areas.getType() == EAccessAreaType.INHERITED) {
            DdsReferenceDef ref = areas.findInheritReference();
            if (ref == null) {
                error(problemSource, problemHandler, "Can not find access area inheritance reference");
            } else {
                Id targetClassId = Id.Factory.changePrefix(ref.getParentTableId(), EDefinitionIdPrefix.ADS_ENTITY_CLASS);

                AdsClassDef clazz = AdsSearcher.Factory.newAdsClassSearcher(areas.getOwnerClass()).findById(targetClassId).get();

                if (clazz instanceof AdsEntityClassDef) {
                    if (map.containsKey(targetClassId)) {
                        error(problemSource, problemHandler, MessageFormat.format("Cycle in access area inheritance at class {0}", clazz.getQualifiedName()));
                    } else {
                        checkInheritedAreas(((AdsEntityClassDef) clazz).getAccessAreas(), problemSource, map, problemHandler, priorAreaNames);
                    }

                } else {
                    error(problemSource, problemHandler, MessageFormat.format("Can not find element of access area inheritance followed by access areas of {0}", areas.getOwnerClass().getQualifiedName()));
                }
            }
        }
    }
}
