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

package org.radixware.kernel.common.client.models.items.properties;

import java.sql.Timestamp;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.RadClassPresentationDef;
import org.radixware.kernel.common.client.meta.filters.RadFilterParamDef;
import org.radixware.kernel.common.client.meta.RadParentRefPropertyDef;
import org.radixware.kernel.common.client.meta.RadPropertyDef;
import org.radixware.kernel.common.client.types.ArrRef;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.types.Reference;
import org.radixware.kernel.common.client.types.ResolvableReference;
import org.radixware.kernel.common.client.utils.ValueConverter;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Arr;
import org.radixware.kernel.common.types.ArrDateTime;
import org.radixware.kernel.common.types.ArrNum;
import org.radixware.kernel.common.types.IKernelEnum;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;

/**
 Класс для хранения значения свойства и метаинформации об
 * этом значении. Используется при активации EntityModel
 */
public class PropertyValue {

    public static class InheritableValue {

        private final Pid ownerEntityPid;
        private final String ownerEntityTitle;
        private final RadClassPresentationDef ownerClassDef;
        private final RadPropertyDef ownerPropertyDef;
        private final Object inheritedValue;

        public InheritableValue(final IClientEnvironment environment, 
                                final org.radixware.schemas.eas.Property.InheritableValue.Path inheritancePath, 
                                final org.radixware.schemas.eas.Property finalProperty) {
            final org.radixware.schemas.eas.Property.InheritableValue.Path.Item.ReferenceValue firstRefererence = 
                    inheritancePath.getItemList().get(0).getReferenceValue();
            ownerClassDef = 
                environment.getDefManager().getClassPresentationDef(firstRefererence.getClassId());
            ownerEntityPid = new Pid(ownerClassDef.getTableId(), firstRefererence.getPID());
            ownerEntityTitle = firstRefererence.getTitle();       
            final org.radixware.schemas.eas.Property.InheritableValue.Path.Item.ReferenceValue lastRefererence = 
                    inheritancePath.getItemList().get(inheritancePath.getItemList().size()-1).getReferenceValue();
            final RadClassPresentationDef finalOwnerClassDef = 
                environment.getDefManager().getClassPresentationDef(lastRefererence.getClassId());
            ownerPropertyDef = finalOwnerClassDef.getPropertyDefById(finalProperty.getId());
            final Id valTableId;
            if (ownerPropertyDef instanceof RadParentRefPropertyDef) {
                valTableId = ((RadParentRefPropertyDef) ownerPropertyDef).getReferencedTableId();
            } else {
                valTableId = null;
            }
            inheritedValue = ValueConverter.easPropXmlVal2ObjVal(finalProperty, ownerPropertyDef.getType(), valTableId);                
        }

        public Object getInheritedValue() {
            return inheritedValue;
        }

        public RadClassPresentationDef getOwnerClassDef() {
            return ownerClassDef;
        }

        public Pid getOwnerEntityPid() {
            return ownerEntityPid;
        }

        public String getOwnerEntityTitle() {
            return ownerEntityTitle;
        }

        public RadPropertyDef getOwnerPropertyDef() {
            return ownerPropertyDef;
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj instanceof InheritableValue) {
                final InheritableValue value = (InheritableValue) obj;
                return ownerEntityPid.equals(value.ownerEntityPid)
                        && Utils.equals(ownerEntityTitle, value.ownerEntityTitle)
                        && ownerClassDef == value.ownerClassDef
                        && ownerPropertyDef == value.ownerPropertyDef
                        && valuesEqual(inheritedValue,
                        value.inheritedValue,
                        ownerPropertyDef.getType(),
                        ownerPropertyDef.getConstSet() != null);
            }
            return false;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 71 * hash + (this.ownerEntityPid != null ? this.ownerEntityPid.hashCode() : 0);
            hash = 71 * hash + (this.ownerEntityTitle != null ? this.ownerEntityTitle.hashCode() : 0);
            hash = 71 * hash + (this.ownerClassDef != null ? this.ownerClassDef.hashCode() : 0);
            hash = 71 * hash + (this.ownerPropertyDef != null ? this.ownerPropertyDef.hashCode() : 0);
            hash = 71 * hash + (this.inheritedValue != null ? this.inheritedValue.hashCode() : 0);
            return hash;
        }
    }
    private final RadPropertyDef def;
    private final InheritableValue inheritableValue;
    private final boolean defined;
    private Object value;
    private boolean own;
    private boolean readonly;

    public PropertyValue(final RadPropertyDef definition,
            final Object value,
            final boolean isOwn,
            final boolean isDefined,
            final boolean isReadonly) {
        def = definition;
        this.value = value;
        own = isOwn;
        defined = isDefined;
        readonly = isReadonly;
        inheritableValue = null;
    }

    public PropertyValue(final RadPropertyDef definition,
            final Object value) {
        this(definition, value, true, true, false);
    }

    public PropertyValue(final PropertyValue source) {
        def = source.getPropertyDef();
        value = source.getCopyOfValue();
        own = source.isOwn();
        defined = source.isDefined();
        readonly = source.isReadonly();
        inheritableValue = source.getInheritedValue();
    }

    public PropertyValue(final Property property) {
        def = property.getDefinition();
        value = copyValue(property.getValueObject(), def.getType());
        own = property.hasOwnValue();
        defined = property.isValueDefined();
        readonly = property.isReadonly();
        inheritableValue = property.getInheritableValue();
    }

    public PropertyValue(final IClientEnvironment environment, final org.radixware.schemas.eas.Property property, final RadPropertyDef definition, final Id ownerEntityId) {
        def = definition;
        final Id tableId;
        if (definition instanceof RadParentRefPropertyDef) {
            tableId = ((RadParentRefPropertyDef) definition).getReferencedTableId();
        } else if ((definition instanceof RadFilterParamDef) && definition.getType() == EValType.PARENT_REF) {
            tableId = ((RadFilterParamDef) definition).getReferencedTableId();
        } else {
            tableId = ownerEntityId;
        }
        value = ValueConverter.easPropXmlVal2ObjVal(property, definition.getType(), tableId);
        own = property.isSetIsOwnVal() ? property.getIsOwnVal() : true;
        defined = property.isSetIsDefined() ? property.getIsDefined() : true;
        readonly = property.isSetReadOnly() ? property.getReadOnly() : false;

        if (property.getInheritableValue() != null) {
            final org.radixware.schemas.eas.Property.InheritableValue.Path path = property.getInheritableValue().getPath();
            if (path != null && path.getItemList() != null && !path.getItemList().isEmpty()) {
                final org.radixware.schemas.eas.Property.InheritableValue.Path.Item.ReferenceValue firstRefValue = 
                        path.getItemList().get(0).getReferenceValue();
                if (firstRefValue != null && firstRefValue.getPID() != null && !firstRefValue.getPID().isEmpty() && firstRefValue.getClassId() != null) {
                    final org.radixware.schemas.eas.Property.InheritableValue.Path.Item.ReferenceValue lastRefValue = 
                        path.getItemList().get(path.getItemList().size()-1).getReferenceValue();
                    if (lastRefValue!=null && lastRefValue.getClassId()!=null){
                        inheritableValue = 
                            new InheritableValue(environment, path, property.getInheritableValue().getProperty());
                    }else{
                        inheritableValue = null;
                    }
                } else {
                    inheritableValue = null;
                }
            } else {
                inheritableValue = null;
            }
        } else {
            inheritableValue = null;
        }
    }

    public boolean isDefined() {
        return defined;
    }

    public boolean isOwn() {
        return own;
    }

    public void setOwn(final boolean isOwn) {
        own = isOwn;
    }

    public boolean isReadonly() {
        return readonly;
    }

    public void setReadonly(final boolean isReadonly) {
        readonly = isReadonly;
    }

    public RadPropertyDef getPropertyDef() {
        return def;
    }

    public InheritableValue getInheritedValue() {
        return inheritableValue;
    }

    public boolean isValueInherited() {
        return !own && inheritableValue != null;
    }

    public Object getValue() {
        return value;
    }

    public Object getCopyOfValue() {
        return copyValue(value, def.getType());
    }

    public void setValue(final Object newValue) {
        value = newValue;
        own = true;
    }

    public void refineValue(final Object typifiedValue) {
        value = typifiedValue;
    }

    public static Object copyValue(final Object from, final EValType valType) {
        if (from == null) {
            return null;
        }
        switch (valType) {
            case PARENT_REF:
            case OBJECT:
                if (from instanceof ResolvableReference) {
                    return new ResolvableReference((ResolvableReference) from);
                } else if (from instanceof Reference) {
                    return new Reference((Reference) from);
                } else {
                    throw new IllegalArgumentException("Reference value expected for type " + valType.getName() + ", but " + from.getClass().getName() + " found");
                }
            case DATE_TIME:
                if (from instanceof Timestamp) {
                    final Timestamp source = (Timestamp) from;
                    final Timestamp result = new Timestamp(source.getTime());
                    result.setNanos(source.getNanos());
                    return result;
                } else {
                    throw new IllegalArgumentException("Timestamp value expected for type " + valType.getName() + ", but " + from.getClass().getName() + " found");
                }
            case XML:
                if (from instanceof XmlObject) {
                    return ((XmlObject) from).copy();
                } else {
                    throw new IllegalArgumentException("XmlObject value expected for type " + valType.getName() + ", but " + from.getClass().getName() + " found");
                }
            case ARR_STR:
            case ARR_CLOB:
            case ARR_INT:
            case ARR_CHAR:
            case ARR_NUM:
            case ARR_BOOL:
            case ARR_BIN:
            case ARR_BLOB:
                if (from instanceof Arr) {
                    return ((Arr) from).clone();
                } else {
                    throw new IllegalArgumentException("Array value expected for type " + valType.getName() + ", but " + from.getClass().getName() + " found");
                }
            case ARR_DATE_TIME:
                if (from instanceof ArrDateTime) {
                    final ArrDateTime source = (ArrDateTime) from;
                    final ArrDateTime result = new ArrDateTime();
                    Timestamp time;
                    for (Timestamp item : source) {
                        if (item != null) {
                            time = new Timestamp(item.getTime());
                            time.setNanos(item.getNanos());
                            result.add(time);
                        } else {
                            result.add(null);
                        }
                    }
                    return result;
                } else {
                    throw new IllegalArgumentException("Array of Timestamp expected for type " + valType.getName() + ", but " + from.getClass().getName() + " found");
                }
            case ARR_REF:
                if (from instanceof ArrRef) {
                    final ArrRef source = (ArrRef) from;
                    final ArrRef result = new ArrRef();
                    for (Reference item : source) {
                        result.add(item != null ? new Reference(item) : null);
                    }
                    return result;
                } else {
                    throw new IllegalArgumentException("Array of References expected for type " + valType.getName() + ", but " + from.getClass().getName() + " found");
                }

            default:
                return from;
        }

    }

    private static Object getUntypified(final Object val) {
        if (val instanceof IKernelEnum) {
            return ((IKernelEnum) val).getValue();
        } else if (val instanceof XmlObject) {
            return ((XmlObject) val).xmlText();
        }
        return val;
    }

    @SuppressWarnings("unchecked")
    private static boolean valuesEqual(final Object val1, final Object val2, final EValType type, final boolean isEnum) {
        if (type.isArrayType() && isEnum && val1 != null && val2 != null) {
            final Arr arr1 = (Arr) val1, arr2 = (Arr) val2;
            if (arr1.size() != arr2.size()) {
                return false;
            }
            Object item1, item2;
            for (int i = arr1.size() - 1; i >= 0; i--) {
                item1 = arr1.get(i);
                item2 = arr2.get(i);
                if (item1 != item2 && !Utils.equals(getUntypified(item1), getUntypified(item2))) {//NOPMD
                    return false;
                }
            }
            return true;
        } else {
            if (val1==val2){//NOPMD
                return true;
            }
            final Object normalizedVal1 = getUntypified(val1);
            final Object normalizedVal2 = getUntypified(val2);
            return Utils.equals(normalizedVal1, normalizedVal2)
                   ||(normalizedVal1 instanceof Comparable && normalizedVal2 instanceof Comparable && ((Comparable)normalizedVal1).compareTo(normalizedVal2)==0);
        }
    }

    private boolean exactlySameValue(final Object compareWith) {
        final EValType valType = def.getType();
        if (!valuesEqual(value, compareWith, valType, def.getConstSet() != null)) {
            return false;
        }
        if (value == null) {
            return true;
        }
        if (valType == EValType.NUM){
            return Utils.equals(value, compareWith);
        }
        if (valType == EValType.ARR_NUM){
            final ArrNum arr1 = (ArrNum) value, arr2 = (ArrNum) compareWith;
            for (int i = arr1.size() - 1; i >= 0; i--) {
                if (!Utils.equals(arr1.get(i), arr2.get(i))){
                    return false;
                }
            }
        }
        if (valType == EValType.PARENT_REF || valType == EValType.OBJECT) {
            return Reference.exactlyMatch((Reference) value, (Reference) compareWith);
        }
        
        if (valType == EValType.ARR_REF) {
            final ArrRef arr1 = (ArrRef) value, arr2 = (ArrRef) compareWith;
            Reference ref1, ref2;
            for (int i = arr1.size() - 1; i >= 0; i--) {
                ref1 = arr1.get(i);
                ref2 = arr2.get(i);
                if (ref1 != null && !Utils.equals(ref1.getTitle(), ref2.getTitle())) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof PropertyValue) {
            final PropertyValue compareWith = (PropertyValue) obj;
            return def == compareWith.def
                    && Utils.equals(inheritableValue, compareWith.inheritableValue)
                    && exactlySameValue(compareWith.value)
                    && own == compareWith.own
                    && defined == compareWith.defined
                    && readonly == compareWith.readonly;
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (this.def != null ? this.def.hashCode() : 0);
        hash = 89 * hash + (this.inheritableValue != null ? this.inheritableValue.hashCode() : 0);
        hash = 89 * hash + (this.value != null ? this.value.hashCode() : 0);
        hash = 89 * hash + (this.own ? 1 : 0);
        hash = 89 * hash + (this.defined ? 1 : 0);
        hash = 89 * hash + (this.readonly ? 1 : 0);
        return hash;
    }

    public boolean hasSameValue(final Object compareWith) {
        if (compareWith instanceof NoValue) {
            return false;
        }
        if (compareWith instanceof PropertyValue) {
            final PropertyValue val = (PropertyValue) compareWith;
            return def == val.def
                    && own == val.own
                    && valuesEqual(value, val.value, def.getType(), def.getConstSet() != null);
        }
        return valuesEqual(value, compareWith, def.getType(), def.getConstSet() != null);
    }
}
