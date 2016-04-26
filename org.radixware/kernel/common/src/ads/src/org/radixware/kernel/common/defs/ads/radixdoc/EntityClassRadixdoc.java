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

package org.radixware.kernel.common.defs.ads.radixdoc;

import java.util.List;
import org.radixware.kernel.common.defs.DefinitionSearcher;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityBasedClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsObjectTitleFormatDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.EntityPresentations;
import org.radixware.kernel.common.defs.ads.common.Restrictions;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.EAccessAreaType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.ERestriction;
import org.radixware.kernel.common.radixdoc.DefaultMeta;
import org.radixware.kernel.common.radixdoc.DefaultStyle;
import org.radixware.kernel.common.radixdoc.DocumentOptions;
import org.radixware.kernel.common.radixdoc.RadixIconResource;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.radixdoc.Block;
import org.radixware.schemas.radixdoc.ContentContainer;
import org.radixware.schemas.radixdoc.ElementList;
import org.radixware.schemas.radixdoc.Page;
import org.radixware.schemas.radixdoc.Table;


public class EntityClassRadixdoc extends EntityObjectClassRadixdoc {

    public EntityClassRadixdoc(AdsClassDef source, Page page, DocumentOptions options) {
        super(source, page, options);
    }

    @Override
    protected void writeClassDefInfo(ContentContainer overview, Table overviewTable) {
        super.writeClassDefInfo(overview, overviewTable);

        AdsEntityClassDef entityCl = (AdsEntityClassDef) source;
        Restrictions restrictions = entityCl.getPresentations().getRestrictions();
        writeRestrictionsInfo(overviewTable, restrictions);
        writeAccessAreasInfo(overview, entityCl);
        writePresentationsInfo(overview, entityCl);
    }

    @Override
    protected void writeTitles(AdsEntityBasedClassDef entityBasedDef, Table titlesTable) {
        super.writeTitles(entityBasedDef, titlesTable);

        AdsEntityClassDef entityCl = (AdsEntityClassDef) entityBasedDef;
        EntityPresentations pres = entityCl.getPresentations();
        if (pres != null && entityCl.getLocalizingBundleId() != null) {
            getClassWriter().addStr2MslIdRow(titlesTable, "Plural", entityCl.getLocalizingBundleId(), pres.getEntityTitleId());
            if (pres.getObjectTitleFormat() != null && pres.getObjectTitleFormat().getNullValTitleId() != null) {
                getClassWriter().addStr2MslIdRow(titlesTable, "Missing", entityCl.getLocalizingBundleId(), pres.getObjectTitleFormat().getNullValTitleId());
            } else {
                getClassWriter().addAllStrRow(titlesTable, "Missing", "");
            }
        }
    }

    private void writeRestrictionsInfo(Table overviewTable, Restrictions restrictions) {
        if (restrictions != null) {
            final long restrs = ERestriction.CREATE.getValue()
                    | ERestriction.COPY.getValue()
                    | ERestriction.MULTIPLE_COPY.getValue()
                    | ERestriction.MOVE.getValue()
                    | ERestriction.DELETE.getValue()
                    | ERestriction.UPDATE.getValue();
            getClassWriter().addAllStrRow(overviewTable, "Prohibited actions", getWriter().getRestrictionsAsStr(restrs & restrictions.toBitField()));
        }
    }

    private void writeAccessAreasInfo(ContentContainer overview, AdsEntityClassDef entityCl) {
        AdsEntityClassDef.AccessAreas accessAreas = entityCl.getAccessAreas();
        Block accessAreasBlock = overview.addNewBlock();
        Table accessAreasTable = getClassWriter().setBlockCollapsibleAndAddTable(accessAreasBlock, "Access Areas");
        getClassWriter().addAllStrRow(accessAreasTable, "Definition mode", accessAreas.getType().getName());
        if (EAccessAreaType.INHERITED.equals(accessAreas.getType())) {
            getClassWriter().addStr2RefRow(accessAreasTable, "Parent reference", accessAreas.findInheritReference(), source);
        }

        if (!EAccessAreaType.NONE.equals(accessAreas.getType())) {
            Block accessTreeBlock = accessAreasBlock.addNewBlock();
            accessTreeBlock.setMeta(DefaultMeta.Block.CONTENT);
            ElementList accessList = accessTreeBlock.addNewList();
            accessList.setStyle(DefaultStyle.TREE_TITLE);

            if (!EAccessAreaType.OWN.equals(accessAreas.getType())) {
                DdsReferenceDef ref = accessAreas.findInheritReference();
                DdsTableDef parentTable = ref.findParentTable(entityCl);
                Id entityId = Id.Factory.changePrefix(parentTable.getId(), EDefinitionIdPrefix.ADS_ENTITY_CLASS);
                DefinitionSearcher<AdsEntityClassDef> adsSearcher = AdsSearcher.Factory.newEntityClassSearcher(entityCl.getModule());
                AdsEntityClassDef parentEntity = adsSearcher.findById(entityId).get();
                if (parentEntity != null) {
                    doWriteAccessAreasInfo(accessList, parentEntity, true);
                }
            }

            doWriteAccessAreasInfo(accessList, entityCl, false);
        }
    }

    private void doWriteAccessAreasInfo(ElementList accessList, AdsEntityClassDef entityCl, boolean isInherited) {
        AdsEntityClassDef.AccessAreas accessAreas = entityCl.getAccessAreas();
        for (AdsEntityClassDef.AccessAreas.AccessArea area : accessAreas) {
            ElementList.Item areaItem = accessList.addNewItem();
            if (isInherited) {
                areaItem.setStyle(DefaultStyle.ACCESS_AREA_ITEM_INHERITED);
            } else {
                areaItem.setStyle(DefaultStyle.ACCESS_AREA_ITEM);
            }
            areaItem.addNewText().setStringValue(area.getName());

            if (!area.getPartitions().isEmpty()) {
                ElementList partList = areaItem.addNewList();
                partList.setStyle(DefaultStyle.TREE_TITLE);
                DefinitionSearcher<DdsReferenceDef> searcher = AdsSearcher.Factory.newDdsReferenceSearcher(entityCl);
                for (AdsEntityClassDef.AccessAreas.AccessArea.Partition par : area.getPartitions()) {
                    StringBuilder parInfo = new StringBuilder();
                    parInfo.append(par.findReferencedClass().getName());

                    ElementList.Item refListItem = partList.addNewItem();
                    List<Id> parRefs = par.getReferenceIds();
                    boolean first = true;
                    for (Id refId : parRefs) {
                        DdsReferenceDef refDef = searcher.findById(refId).get();
                        if (refDef != null) {
                            if (first) {
                                first = false;
                            } else {
                                ElementList refList = refListItem.addNewList();
                                refList.setStyle(DefaultStyle.TREE_TITLE);
                                refListItem = refList.addNewItem();
                            }
                            refListItem.addNewText().setStringValue(refDef.findParentTable(refDef.getModule()).getName() + " (");
                            getClassWriter().addRef(refListItem, refDef, refDef.getModule());
                            refListItem.addNewText().setStringValue(")");
                        }
                    }
                    AdsPropertyDef prop = par.findReferencedProperty();
                    ElementList.Item propItem = refListItem.addNewList().addNewItem();
                    final RadixIconResource resource = new RadixIconResource(prop.getIcon());
                    propItem.addNewResource().setSource(resource.getKey());
                    getClassWriter().addRef(propItem, prop, prop.getOwnerClass());
                }
            }
        }
    }

    private void writePresentationsInfo(ContentContainer overview, AdsEntityClassDef entityCl) {
        EntityPresentations pres = entityCl.getPresentations();
        if (pres != null) {
            Block presInfoBlock = overview.addNewBlock();
            Table presInfoTable = getClassWriter().setBlockCollapsibleAndAddTable(presInfoBlock, "Presentation");
            getClassWriter().addStr2RefRow(presInfoTable, "Default selector presentation", pres.findDefaultSelectorPresentation(), source);
            if (pres.getObjectTitleFormat() != null) {
                int num = 1;
                for (AdsObjectTitleFormatDef.TitleItem item : pres.getObjectTitleFormat().getItems()) {
                    AdsPropertyDef prop = item.findProperty();
                    if (prop != null) {
                        Table.Row titleFormatRow = presInfoTable.addNewRow();
                        titleFormatRow.addNewCell().addNewText().setStringValue("Title format #" + num++);
                        Table.Row.Cell refCell = titleFormatRow.addNewCell();
                        getClassWriter().addRef(refCell, prop, source);
                        getClassWriter().addText(refCell, " = " + item.getPattern());
                    }
                }
            }
        }
    }
}