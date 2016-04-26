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

package org.radixware.kernel.common.defs.ads.common;

import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsDetailColumnPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsDetailRefPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsInnateColumnPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsInnateRefPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsSelectorExplorerItemDef;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.type.ParentRefType;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.ENamingPolicy;
import org.radixware.kernel.common.enums.EValType;
import static org.radixware.kernel.common.enums.EValType.BOOL;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.SqmlCondition;


public class Prop2ValueMap extends RadixObject {

    public static class Prop2ValMapItem extends RadixObject {

        private Id propId;
        private String stringRepresentation;
        private ValAsStr valAsStr;

        public Prop2ValMapItem(AdsPropertyDef property) {
            this.propId = property.getId();
        }

        @Override
        public ENamingPolicy getNamingPolicy() {
            return ENamingPolicy.FREE;
        }

        @Override
        @SuppressWarnings("unchecked")
        public String getName() {
            String name = "Item";
            if (getContainer() instanceof RadixObjects) {
                name += "#" + ((RadixObjects) getContainer()).indexOf(this);
            }
            return name;
        }

        @Override
        public String getTypeTitle() {
            return "Declarative condition item";
        }

        @Override
        public String getTypesTitle() {
            return "Declarative condition items";
        }

        public Prop2ValMapItem(SqmlCondition.Prop2ValueCondition.Item xDef) {
            this.propId = xDef.getPropId();
            this.stringRepresentation = xDef.getValue();
        }

        public ValAsStr getValue() {
            synchronized (this) {
                if (valAsStr == null) {
                    if (stringRepresentation == null) {
                        return null;
                    } else {
                        Id tableId = getTableIdIfParentRef();
                        if (tableId != null) {
                            valAsStr = ValAsStr.Factory.loadFrom(tableId.toString() + "\n" + stringRepresentation);
                        } else {
                            valAsStr = ValAsStr.Factory.loadFrom(stringRepresentation);
                        }
                    }
                }
                return valAsStr;
            }
        }

        public Id getPropertyId() {
            return propId;
        }

        private Id getTableIdIfParentRef() {
            AdsPropertyDef prop = findProperty();
            if (prop == null) {
                return null;
            }
            if (prop.getValue().getType() == null) {
                return null;
            }
            if (prop.getValue().getType().getTypeId() == EValType.PARENT_REF) {
                AdsType type = prop.getValue().getType().resolve(prop).get();
                if (type instanceof ParentRefType) {
                    ParentRefType ref = (ParentRefType) type;
                    AdsEntityObjectClassDef clazz = ref.getSource();
                    if (clazz == null) {
                        return null;
                    }
                    DdsTableDef table = clazz.findTable(prop);
                    if (table == null) {
                        return null;
                    }
                    return table.getId();
                }
            }
            return null;
        }

        public AdsPropertyDef findProperty() {
            Prop2ValueMap ownerMap = getOwnerProp2ValueMap();
            if (ownerMap == null) {
                return null;
            }
            AdsClassDef clazz = ownerMap.findPropertiesProvider();
            if (clazz == null) {
                return null;
            }

            return clazz.getProperties().findById(propId, ExtendableDefinitions.EScope.ALL).get();
        }

        public String getStringValue() {
            return stringRepresentation;
        }

        public void setValue(ValAsStr valAsStr) {
            this.valAsStr = valAsStr;
            if (valAsStr != null) {
                Id tableId = getTableIdIfParentRef();
                if (tableId != null) {
                    String fullStr = valAsStr.toString();
                    if (fullStr.trim().isEmpty()) {
                        this.stringRepresentation = null;
                    } else {
                        int idx = fullStr.indexOf("\n");
                        if (idx > 0) {
                            fullStr = fullStr.substring(idx + 1);
                        }
                        this.stringRepresentation = fullStr;
                    }
                } else {
                    this.stringRepresentation = valAsStr.toString();
                }
            } else {
                stringRepresentation = null;
            }
            setEditState(EEditState.MODIFIED);
        }

        public Prop2ValueMap getOwnerProp2ValueMap() {
            for (RadixObject obj = getContainer(); obj != null; obj = obj.getContainer()) {
                if (obj instanceof Prop2ValueMap) {
                    return (Prop2ValueMap) obj;
                }
            }
            return null;
        }

        public void appendTo(SqmlCondition.Prop2ValueCondition.Item xDef) {
            xDef.setPropId(propId);
            xDef.setValue(stringRepresentation);
        }
    }

    public class Prop2ValMapItems extends RadixObjects<Prop2ValMapItem> {

        public Prop2ValMapItems(Prop2ValueMap container) {
            super(container);
        }

        public Prop2ValMapItem findItemByPropId(Id id) {
            for (Prop2ValMapItem item : this) {
                if (item.propId == id) {
                    return item;
                }
            }
            return null;
        }

        public List<Id> getPropIds() {
            List<Id> ids = new ArrayList<>();
            for (Prop2ValMapItem item : this) {
                ids.add(item.getPropertyId());
            }
            return ids;
        }
    }
    private final Prop2ValMapItems items = new Prop2ValMapItems(this);

    Prop2ValueMap(AdsCondition condition, SqmlCondition.Prop2ValueCondition xDef) {
        setContainer(condition);
        if (xDef != null) {
            for (SqmlCondition.Prop2ValueCondition.Item xItem : xDef.getItemList()) {
                if (xItem.getPropId() != null) {
                    items.add(new Prop2ValMapItem(xItem));
                }
            }
        }
    }

    public Prop2ValMapItems getItems() {
        return items;
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        items.visit(visitor, provider);
    }

    public AdsClassDef findPropertiesProvider() {
        Definition def = getOwnerDefinition();
        while (def != null) {
            if (def instanceof AdsPropertyDef) {
                AdsTypeDeclaration decl = ((AdsPropertyDef) def).getValue().getType();
                if (decl != null) {
                    AdsType type = decl.resolve(def).get();
                    if (type instanceof ParentRefType) {
                        return ((ParentRefType) type).getSource();
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            }
            if (def instanceof AdsSelectorPresentationDef) {
                return ((AdsSelectorPresentationDef) def).getOwnerClass();
            }
            if (def instanceof AdsSelectorExplorerItemDef) {
                return ((AdsSelectorExplorerItemDef) def).findReferencedEntityClass();
            }
            def = def.getOwnerDefinition();
        }
        return null;
    }

    public AdsSelectorPresentationDef findContextSelectorPresentation() {
        Definition def = getOwnerDefinition();
        while (def != null) {
            if (def instanceof AdsSelectorPresentationDef) {
                return (AdsSelectorPresentationDef) def;
            }
            if (def instanceof AdsSelectorExplorerItemDef) {
                return ((AdsSelectorExplorerItemDef) def).findReferencedSelectorPresentation().get();
            }
            def = def.getOwnerDefinition();
        }
        return null;
    }

    public boolean isPropertySuitableForMapInContext(AdsPropertyDef prop) {
        AdsClassDef clazz = findPropertiesProvider();
        if (clazz == null) {
            return false;
        } else {
            return isPropertySuitableForMap(prop);
        }

    }

    public boolean isPropertySuitableForMap(AdsPropertyDef prop) {
        if (prop instanceof AdsInnateColumnPropertyDef || prop instanceof AdsInnateRefPropertyDef || prop instanceof AdsDetailColumnPropertyDef) {
            if (prop.getValue().getType() == null || prop.getValue().getType().getTypeId() == null) {
                return false;
            }
            EValType valType = prop.getValue().getType().getTypeId();
            switch (valType) {
                case INT:
                case NUM:
                case STR:
                case DATE_TIME:
                case CHAR:
                case BOOL:
                case PARENT_REF:
                    return true;
                default:
                    return false;
            }
        } else {
            return false;
        }
    }

    public void appendTo(SqmlCondition.Prop2ValueCondition xDef) {
        for (Prop2ValMapItem item : getItems()) {
            item.appendTo(xDef.addNewItem());
        }
    }

    @Override
    public String getName() {
        return "Condition";
    }

    @Override
    public ENamingPolicy getNamingPolicy() {
        return ENamingPolicy.FREE;
    }

    @Override
    public String getTypeTitle() {
        return "Declarative condition";
    }

    @Override
    public String getTypesTitle() {
        return "Declarative conditions";
    }
}
