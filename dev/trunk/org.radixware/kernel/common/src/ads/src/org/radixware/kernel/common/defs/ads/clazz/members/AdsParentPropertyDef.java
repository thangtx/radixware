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
package org.radixware.kernel.common.defs.ads.clazz.members;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsPropertyGroup;
import org.radixware.kernel.common.defs.ads.type.AdsClassType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.enums.EPropNature;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.schemas.adsdef.AbstractPropertyDefinition;
import org.radixware.schemas.adsdef.PropertyDefinition;

public class AdsParentPropertyDef extends AdsTablePropertyDef {

    public class ParentPath {

        private List<Id> parentRefIds;

        private ParentPath(List<Id> parentRefIds) {
            if (parentRefIds != null) {
                this.parentRefIds = new ArrayList<Id>(parentRefIds);
            } else {
                this.parentRefIds = null;
            }
        }

        private ParentPath() {
            this.parentRefIds = null;
        }

        private void appendTo(PropertyDefinition xProp) {
            if (parentRefIds != null && !parentRefIds.isEmpty()) {
                xProp.setParentPath(new ArrayList<Id>(parentRefIds));
            }
        }

        public List<Id> getRefPropIds() {
            if (parentRefIds == null) {
                return Collections.emptyList();
            } else {
                return new ArrayList<Id>(parentRefIds);
            }
        }

        public void setRefPropIds(List<Id> ids) {
            this.parentRefIds = new ArrayList<Id>(ids);
            setEditState(EEditState.MODIFIED);
        }
    }

    public class ParentInfo {

        private Id originalPropertyId;
        private final ParentPath parentPath;

        private ParentInfo() {
            this.originalPropertyId = null;
            this.parentPath = new ParentPath();
        }

        private ParentInfo(AbstractPropertyDefinition xDef) {
            if (xDef instanceof PropertyDefinition) {
                PropertyDefinition xProp = (PropertyDefinition) xDef;
                this.originalPropertyId = xProp.getOriginalPropertyId();
                this.parentPath = new ParentPath(xProp.getParentPath());
            } else {
                this.parentPath = new ParentPath();
                this.originalPropertyId = null;
            }
        }

        private ParentInfo(ParentInfo source) {
            this.originalPropertyId = source.originalPropertyId;
            this.parentPath = new ParentPath(source.parentPath.parentRefIds);
        }

        public Id getOriginalPropertyId() {
            return originalPropertyId;
        }

        public void setOriginalPropertyId(Id id) {
            this.originalPropertyId = id;
            AdsPropertyDef origProp = findOriginalProperty();
            if (origProp != null) {
                getValue().setType(origProp.getValue().getType());
            }
            setEditState(EEditState.MODIFIED);
        }

        public ParentPath getParentPath() {
            return parentPath;
        }

        void appendAdditionalToolTip(StringBuilder sb) {
            final ArrayList<AdsPropertyDef> pathItems = new ArrayList<AdsPropertyDef>();
            final AdsPropertyDef target = findOriginalProperty(pathItems);
            if (target != null) {
                sb.append("<br>Refers to:<br><&nbsp;>").append(target.getQualifiedName());
                sb.append("<br>Through:");
                for (AdsPropertyDef prop : pathItems) {
                    sb.append("<br>&nbsp;").append(prop.getQualifiedName());
                }

            }
        }

        /**
         * Tries to find original property for the property
         *
         */
        public AdsPropertyDef findOriginalProperty() {
            return findOriginalProperty(null);
        }

        public AdsPropertyDef findOriginalProperty(List<AdsPropertyDef> pathItems) {
            return findOriginalProperty(pathItems, null);
        }

        public AdsPropertyDef findOriginalProperty(List<AdsPropertyDef> pathItems, IProblemHandler problemHandler) {
            if (originalPropertyId != null && parentPath != null && parentPath.parentRefIds != null) {
                AdsClassDef clazz = getOwnerClass();
                for (Id id : parentPath.parentRefIds) {
                    AdsPropertyDef refProp = clazz.getProperties().findById(id, EScope.ALL).get();
                    if (refProp != null) {
                        if (refProp instanceof AdsParentPropertyDef && problemHandler != null) {
                            problemHandler.accept(RadixProblem.Factory.newError(AdsParentPropertyDef.this, "Parent property can not refer to original property through another parent property " + refProp.getQualifiedName()));
                        }
                        if (pathItems != null) {
                            pathItems.add(refProp);
                        }
                        AdsType targetType = refProp.getValue().getType().resolve(clazz).get();
                        if (targetType instanceof AdsClassType) {
                            clazz = ((AdsClassType) targetType).getSource();
                            if (clazz == null) {
                                return null;
                            }
                        } else {
                            return null;
                        }
                    } else {
                        return null;
                    }
                }
                return clazz.getProperties().findById(originalPropertyId, EScope.ALL).get();
            }
            return null;
        }
        
        public AdsClassDef findOriginalPropertyOwnerClass() {
            return findOriginalPropertyOwnerClass(null);
        }

        public AdsClassDef findOriginalPropertyOwnerClass(List<AdsPropertyDef> pathItems) {
            if (originalPropertyId != null && parentPath != null && parentPath.parentRefIds != null) {
                AdsClassDef clazz = getOwnerClass();
                for (Id id : parentPath.parentRefIds) {
                    AdsPropertyDef refProp = clazz.getProperties().findById(id, EScope.ALL).get();
                    if (refProp != null) {
                        if (pathItems != null) {
                            pathItems.add(refProp);
                        }
                        AdsType targetType = refProp.getValue().getType().resolve(refProp).get();
                        if (targetType instanceof AdsClassType) {
                            clazz = ((AdsClassType) targetType).getSource();
                            if (clazz == null) {
                                return null;
                            }
                        } else {
                            return null;
                        }
                    } else {
                        return null;
                    }
                }
                return clazz;
            }
            return null;
        }

        private void appendTo(PropertyDefinition xProp) {
            if (originalPropertyId != null) {
                xProp.setOriginalPropertyId(originalPropertyId);
            }
            if (parentPath != null) {
                parentPath.appendTo(xProp);
            }
        }
    }

    public static final class Factory {

        public static AdsParentPropertyDef newInstance(List<AdsPropertyDef> parentPath, AdsPropertyDef originalProperty) {
            return new AdsParentPropertyDef(parentPath, originalProperty);
        }

        public static AdsParentPropertyDef newTemporaryInstance(AdsPropertyGroup context) {
            AdsParentPropertyDef prop = new AdsParentPropertyDef(null, null);
            prop.setContainer(context);
            return prop;
        }
    }
    private final ParentInfo parentInfo;

    AdsParentPropertyDef(AbstractPropertyDefinition xProp) {
        super(xProp);
        this.parentInfo = new ParentInfo(xProp);
    }

    AdsParentPropertyDef(List<AdsPropertyDef> parentPath, AdsPropertyDef originalProperty) {
        super(Id.Factory.newInstance(EDefinitionIdPrefix.DDS_COLUMN), originalProperty == null ? "newParentProperty" : originalProperty.getName());
        this.parentInfo = new ParentInfo();
        if (parentPath != null && !parentPath.isEmpty() && originalProperty != null) {
            ArrayList<Id> ids = new ArrayList<Id>();
            for (AdsPropertyDef ref : parentPath) {
                if (ref == null) {
                    break;
                }
                ids.add(ref.getId());
            }

            this.parentInfo.parentPath.setRefPropIds(ids);

        }
        if (originalProperty != null) {
            this.parentInfo.originalPropertyId = originalProperty.getId();
            this.getValue().setType(originalProperty.getValue().getType());
        } else {
            this.parentInfo.originalPropertyId = null;
            this.getValue().setType(AdsTypeDeclaration.UNDEFINED);
        }
    }

    AdsParentPropertyDef(AdsParentPropertyDef source, boolean forOverride) {
        super(source, forOverride);
        this.parentInfo = new ParentInfo(source.parentInfo);
    }

    @Override
    public EPropNature getNature() {
        return EPropNature.PARENT_PROP;
    }

    public ParentInfo getParentInfo() {
        return parentInfo;
    }

    @Override
    protected AdsPropertyDef createOvr(boolean forOverride) {
        return new AdsParentPropertyDef(this, forOverride);
    }

    @Override
    public void appendTo(PropertyDefinition xProp, ESaveMode saveMode) {
        super.appendTo(xProp, saveMode);
        if (parentInfo != null) {
            parentInfo.appendTo(xProp);
        }
    }

    @Override
    public boolean isConst() {
        AdsPropertyDef prop = parentInfo.findOriginalProperty();
        if (prop != null) {
            return prop.isConst();
        } else {
            return true;
        }
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);
        final ArrayList<AdsPropertyDef> path = new ArrayList<AdsPropertyDef>();
        AdsPropertyDef o = parentInfo.findOriginalProperty(path);
        if (o != null) {
            list.add(o);
        }
        
        for (AdsPropertyDef pathItem : path) {
            list.add(pathItem);
        }
    }

    @Override
    protected void appendAdditionalToolTip(StringBuilder sb) {
        parentInfo.appendAdditionalToolTip(sb);
    }

    public boolean isUnsafeForSqml(List<String> messages) {
        List<AdsPropertyDef> path = new LinkedList<AdsPropertyDef>();
        parentInfo.findOriginalProperty(path);

        for (AdsPropertyDef pathItem : path) {
            if (pathItem.getValueInheritanceRules().getInheritable()) {
                messages.add("Parent path contains property with inheritable value: " + pathItem.getQualifiedName());
            }
            if (pathItem.isGetterDefined(EScope.ALL)) {
                messages.add("Parent path contains property with explicitly defined getter: " + pathItem.getQualifiedName());
            }
        }
        return !messages.isEmpty();
    }
    
    @Override
    public boolean isDescriptionInheritable() {
        return true;
    }
    
    @Override
    public Definition getDescriptionLocation(){
        Definition owner = super.getDescriptionLocation();
        if (isDescriptionInherited() && owner == this){
            if (!isOverride() && !isOverwrite()){
                AdsPropertyDef property = getParentInfo().findOriginalProperty();
                if (property != null){
                    return property;
                }
            }
        }
        return owner;
    }
}
