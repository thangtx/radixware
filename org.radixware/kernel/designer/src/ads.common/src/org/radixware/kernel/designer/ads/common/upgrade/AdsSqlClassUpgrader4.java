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

package org.radixware.kernel.designer.ads.common.upgrade;

import java.util.HashMap;
import java.util.Map;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsFieldPropertyDef;
import org.radixware.kernel.common.defs.ads.module.AdsPath;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.jml.JmlTagInvocation;
import org.radixware.kernel.common.sqml.tags.PropSqlNameTag;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.upgrade.IRadixObjectUpgrader;
import org.radixware.kernel.common.utils.XmlUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Change GUID of AdsFieldPropDef
 */
class AdsSqlClassUpgrader4 implements IRadixObjectUpgrader {

    private static class Ids {

        final Id sqlClassId;
        final Id oldFieldId;

        public Ids(Id sqlClassId, Id oldFieldId) {
            this.sqlClassId = sqlClassId;
            this.oldFieldId = oldFieldId;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Ids) {
                final Ids oldId = (Ids) obj;
                return oldId.oldFieldId.equals(oldFieldId) && oldId.sqlClassId.equals(sqlClassId);
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return sqlClassId.hashCode() + oldFieldId.hashCode();
        }
    }
    private Map<Ids, Id> oldIds2newFieldId = new HashMap<Ids, Id>();
    private static final Id UNIT_ID = Id.Factory.loadFrom("accVVDQV2J6RBDEFI4CPVH3KUBLE4");
    private static final Id UNIT_STARTED_OLD_ID = Id.Factory.loadFrom("prfSTARTED");
    private static final Id UNIT_STARTED_NEW_ID = Id.Factory.loadFrom("prfAADQV2J6RBDEFI4CPVH3KUBLE4");

    @Override
    public void firstStep(Element root) {
        oldIds2newFieldId.put(new Ids(UNIT_ID, UNIT_STARTED_OLD_ID), UNIT_STARTED_NEW_ID);

        final Node adsClassDefinitionNode = XmlUtils.findChildByLocalName(root, "AdsClassDefinition");
        final Id sqlClassId = Id.Factory.loadFrom(XmlUtils.getString(adsClassDefinitionNode, "Id"));

        final Node propertiesNode = XmlUtils.findChildByLocalName(adsClassDefinitionNode, "Properties");
        if (propertiesNode != null) {
            for (Node propertyNode = propertiesNode.getFirstChild(); propertyNode != null; propertyNode = propertyNode.getNextSibling()) {
                if ("Property".equals(propertyNode.getLocalName())) {
                    Element propertyElement = (Element) propertyNode;
                    final Id oldFieldId = Id.Factory.loadFrom(propertyElement.getAttribute("Id"));
                    if (oldFieldId.getPrefix() == EDefinitionIdPrefix.ADS_CURSOR_FIELD_PROP) {
                        final Ids oldIds = new Ids(sqlClassId, oldFieldId);
                        final Id newFieldId;
                        if (sqlClassId.equals(UNIT_ID) && oldFieldId.equals(UNIT_STARTED_OLD_ID)) {
                            newFieldId = UNIT_STARTED_NEW_ID;
                        } else {
                            newFieldId = Id.Factory.newInstance(EDefinitionIdPrefix.ADS_CURSOR_FIELD_PROP);

                        }
                        oldIds2newFieldId.put(oldIds, newFieldId);

                        propertyElement.setAttribute("Id", newFieldId.toString());
                    }
                }
            }
        }
    }

    @Override
    public void finalStep(RadixObject radixObject) {
        if (radixObject instanceof PropSqlNameTag) {
            final PropSqlNameTag tag = (PropSqlNameTag) radixObject;
            final Ids oldIds = new Ids(tag.getPropOwnerId(), tag.getPropId());
            final Id newFieldId = oldIds2newFieldId.get(oldIds);
            if (newFieldId != null) {
                tag.setPropId(newFieldId);
            }
        } else if (radixObject instanceof JmlTagInvocation) {
            final JmlTagInvocation tag = (JmlTagInvocation) radixObject;
            final Id[] ids = tag.getPath().asArray();
            if (ids.length == 2) {
                final Ids oldIds = new Ids(ids[0], ids[1]);
                final Id newFieldId = oldIds2newFieldId.get(oldIds);
                if (newFieldId != null) {
                    ids[1] = newFieldId;
                    tag.setPath(new AdsPath(ids));
                }
            }
        } else if (radixObject instanceof AdsFieldPropertyDef) {
            final AdsFieldPropertyDef field = (AdsFieldPropertyDef) radixObject;
            for (Map.Entry<Ids, Id> entry : oldIds2newFieldId.entrySet()) {
                if (entry.getValue().equals(field.getId())) {
                    field.getOwnerClass().getPropertyGroup().removeMemberId(entry.getKey().oldFieldId);
                }
            }
        }
    }
}
