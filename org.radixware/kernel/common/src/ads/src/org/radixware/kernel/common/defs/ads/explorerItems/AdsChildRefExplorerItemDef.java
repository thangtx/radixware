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
package org.radixware.kernel.common.defs.ads.explorerItems;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.defs.ClipboardSupport;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsClipboardSupport;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.AdsDefinitions;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsDetailColumnPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.ads.radixdoc.ChildRefExpItemRadixdoc;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.radixdoc.DocumentOptions;
import org.radixware.kernel.common.radixdoc.IRadixdocPage;
import org.radixware.kernel.common.radixdoc.IRadixdocProvider;
import org.radixware.kernel.common.radixdoc.RadixdocSupport;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.sqml.tags.PropSqlNameTag;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.schemas.adsdef.ChildExplorerItems;
import org.radixware.schemas.adsdef.SelectorExplorerItemDefinition;
import org.radixware.schemas.radixdoc.Page;

public class AdsChildRefExplorerItemDef extends AdsSelectorExplorerItemDef implements IRadixdocProvider {

    public static final class Factory {

        private Factory() {
        }

        public static AdsChildRefExplorerItemDef newTemporaryInstance(RadixObject container) {
            AdsChildRefExplorerItemDef item = new AdsChildRefExplorerItemDef(null, null);
            item.setContainer(container);
            return item;
        }

        public static AdsChildRefExplorerItemDef newInstance(AdsEntityObjectClassDef referencedClass, DdsReferenceDef ref) {
            DdsTableDef table = referencedClass.findTable(referencedClass);
            if (table == null) {
                throw new DefinitionError("Context table not found.");
            }
            if (!Utils.equals(table.getId(), ref.getChildTableId())) {
                throw new DefinitionError("Context class does not based on child table of given reference.");
            }

            return new AdsChildRefExplorerItemDef(referencedClass, ref);
        }

        public static AdsChildRefExplorerItemDef loadFrom(ChildExplorerItems.Item.ChildRef xDef) {
            return new AdsChildRefExplorerItemDef(xDef);
        }

        static AdsChildRefExplorerItemDef loadFrom(SelectorExplorerItemDefinition xDef, RadixObject container) {
            AdsChildRefExplorerItemDef result = new AdsChildRefExplorerItemDef(xDef);
            result.setContainer(container);
            return result;
        }
    }
    private Id childReferenceId;

    private AdsChildRefExplorerItemDef(ChildExplorerItems.Item.ChildRef xDef) {
        this((SelectorExplorerItemDefinition) xDef);
        this.childReferenceId = xDef.getChildReferenceId();
    }

    private AdsChildRefExplorerItemDef(SelectorExplorerItemDefinition xDef) {
        super(xDef);
    }

    private AdsChildRefExplorerItemDef(AdsEntityObjectClassDef clazz, DdsReferenceDef ref) {
        super(clazz);
        this.childReferenceId = ref == null ? null : ref.getId();
    }

    public Id getChildReferenceId() {
        return childReferenceId;
    }

    public void setChildReferenceId(Id id) {
        this.childReferenceId = id;
        setEditState(EEditState.MODIFIED);
    }

    public DdsReferenceDef findChildReference() {
        return AdsSearcher.Factory.newDdsReferenceSearcher(this).findById(childReferenceId).get();
    }

    public void appendTo(ChildExplorerItems.Item.ChildRef xDef, ESaveMode saveMode) {
        super.appendTo(xDef, saveMode);
        if (childReferenceId != null) {
            xDef.setChildReferenceId(childReferenceId);
        }
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.CHILD_REF_EXPLORER_ITEM;
    }

    @Override
    protected void collectDependencesImpl(final boolean direct, boolean forModule, final List<Definition> list) {
        super.collectDependencesImpl(direct, forModule, list);
        final DdsReferenceDef ref = findChildReference();
        if (ref != null) {
            list.add(ref);
        }
    }

    @Override
    public ClipboardSupport<AdsChildRefExplorerItemDef> getClipboardSupport() {
        return new AdsClipboardSupport<AdsChildRefExplorerItemDef>(this) {
            @Override
            protected XmlObject copyToXml() {
                final ChildExplorerItems.Item.ChildRef xDef = ChildExplorerItems.Item.ChildRef.Factory.newInstance();
                appendTo(xDef, ESaveMode.NORMAL);
                return xDef;
            }

            @Override
            protected AdsChildRefExplorerItemDef loadFrom(XmlObject xmlObject) {
                if (xmlObject instanceof ChildExplorerItems.Item.ChildRef) {
                    return Factory.loadFrom((ChildExplorerItems.Item.ChildRef) xmlObject);
                } else {
                    return super.loadFrom(xmlObject);
                }
            }

            @Override
            protected Method getDecoderMethod() {
                try {
                    return Factory.class.getDeclaredMethod("loadFrom", ChildExplorerItems.Item.ChildRef.class);
                } catch (NoSuchMethodException | SecurityException ex) {
                    Logger.getLogger(AdsChildRefExplorerItemDef.class.getName()).log(Level.FINE, ex.getMessage(), ex);
                }
                return null;
            }
        };
    }

    @Override
    public boolean isSuitableContainer(AdsDefinitions collection) {
        if (super.isSuitableContainer(collection)) {
            RadixObject owner = collection.getContainer();
            if (owner instanceof ExplorerItems.Children) {
                return ((ExplorerItems.Children) owner).getOwnerExplorerItems().getPlacement() == ExplorerItems.EPlacement.EDITOR_PRESENTATION;
            } else {
                return false;
            }
        }
        return false;
    }

    public AdsEntityExplorerItemDef convertToEntityItem() {
        Collection<Sqml.Item> conditionContent = getConditionByReference();
        if (conditionContent.isEmpty()) {
            return null;
        }
        AdsEntityObjectClassDef referencedClass = findReferencedEntityClass();
        if (referencedClass == null) {
            return null;
        }
        SelectorExplorerItemDefinition xDef = ChildExplorerItems.Item.ChildRef.Factory.newInstance();
        this.appendTo(xDef, ESaveMode.NORMAL);
        AdsEntityExplorerItemDef entityItem = AdsEntityExplorerItemDef.Factory.loadFrom(xDef, this);
        boolean addParent = !entityItem.getCondition().getWhere().getItems().isEmpty();

        if (addParent) {
            entityItem.getCondition().getWhere().getItems().add(0, Sqml.Text.Factory.newInstance("("));
            entityItem.getCondition().getWhere().getItems().add(Sqml.Text.Factory.newInstance(") AND ("));
        }

        for (Sqml.Item item : conditionContent) {
            entityItem.getCondition().getWhere().getItems().add(item);
        }
        if (addParent) {
            entityItem.getCondition().getWhere().getItems().add(Sqml.Text.Factory.newInstance(")"));
        }
        return entityItem;
    }

    public Collection<Sqml.Item> getConditionByReference() {
        DdsReferenceDef reference = findChildReference();
        if (reference == null) {
            return Collections.emptySet();
        }
        AdsEntityObjectClassDef ownerEntityClass = null;
        AdsDefinition def = getOwnerDef();
        while (def != null) {
            if (def instanceof AdsEntityObjectClassDef) {
                ownerEntityClass = (AdsEntityClassDef) def;
                break;
            }
            def = def.getOwnerDef();
        }

        if (ownerEntityClass == null) {
            return Collections.emptyList();
        }
        List<Sqml.Item> items = new LinkedList<>();
        for (DdsReferenceDef.ColumnsInfoItem refProp : reference.getColumnsInfo()) {

            PropSqlNameTag thisCol = createThisPropTag(ownerEntityClass.getEntityId(), refProp.getChildColumnId());
            final PropSqlNameTag parCol;
            if (ownerEntityClass.getEntityId().equals(reference.getParentTableId())) {//reference to table itself
                parCol = createParentPropTag(reference.getParentTableId(), refProp.getParentColumnId());
            } else {//reference to detail table ?                
                DdsReferenceDef mdRef = null;
                for (AdsEntityObjectClassDef.DetailReferenceInfo r : ownerEntityClass.getAllowedDetailRefs()) {
                    DdsReferenceDef detailRef = r.findReference();
                    if (detailRef == null) {
                        continue;
                    }
                    if (detailRef.getChildTableId().equals(reference.getParentTableId())) {
                        mdRef = detailRef;
                        break;
                    }
                }
                if (mdRef == null) {
                    return Collections.emptyList();
                }
                final Id masterPropId = getMasterPropIdByDetailPropId(mdRef, ownerEntityClass, refProp.getParentColumnId());
                if (masterPropId == null) {
                    return Collections.emptyList();
                }
                parCol = createParentPropTag(ownerEntityClass.getEntityId(), masterPropId);
            }

            if (!items.isEmpty()) {

                items.add(Sqml.Text.Factory.newInstance(" AND "));
            }
            items.add(thisCol);
            items.add(Sqml.Text.Factory.newInstance("="));
            items.add(parCol);

        }
        return items;
    }

    protected final PropSqlNameTag createThisPropTag(final Id entityId, final Id propId) {
        final PropSqlNameTag tag = PropSqlNameTag.Factory.newInstance();
        tag.setOwnerType(PropSqlNameTag.EOwnerType.THIS);
        tag.setPropOwnerId(entityId);
        tag.setPropId(propId);
        return tag;
    }

    protected static final PropSqlNameTag createParentPropTag(final Id entityId, final Id propId) {
        final PropSqlNameTag tag = PropSqlNameTag.Factory.newInstance();
        tag.setOwnerType(PropSqlNameTag.EOwnerType.PARENT);
        tag.setPropOwnerId(entityId);
        tag.setPropId(propId);
        return tag;
    }

    public Id getMasterPropIdByDetailPropId(final DdsReferenceDef mdRef, final AdsEntityObjectClassDef masterMeta, final Id detailsColumnId) {
        if (mdRef != null) {
            for (DdsReferenceDef.ColumnsInfoItem refProp : mdRef.getColumnsInfo()) {
                if (refProp.getChildColumnId().equals(detailsColumnId)) {
                    return refProp.getParentColumnId();
                }
            }
            for (AdsPropertyDef prop : masterMeta.getProperties().get(ExtendableDefinitions.EScope.ALL)) {
                if (prop instanceof AdsDetailColumnPropertyDef) {
                    final AdsDetailColumnPropertyDef detProp = (AdsDetailColumnPropertyDef) prop;
                    if (detProp.getColumnInfo().getColumnId() == detailsColumnId && detProp.getDetailReferenceInfo().findDetailReference() == mdRef) {
                        return prop.getId();
                    }
                }
            }
        }
        return null;
    }

    @Override
    public boolean isRadixdocProvider() {
        return true;
    }

    @Override
    public RadixdocSupport<? extends RadixObject> getRadixdocSupport() {
        return new RadixdocSupport<AdsSelectorExplorerItemDef>(this) {
            @Override
            public IRadixdocPage document(Page page, DocumentOptions options) {
                return new ChildRefExpItemRadixdoc(getSource(), page, options);
            }
        };
    }
}