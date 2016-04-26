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

package org.radixware.kernel.common.client.meta.mask;

import java.util.EnumSet;
import java.util.Objects;
import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.client.IClientApplication;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.RadEnumPresentationDef;
import org.radixware.kernel.common.client.meta.mask.validators.EValidatorState;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.client.models.items.ModelItem;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMaskTimeInterval.Scale;
import org.radixware.kernel.common.enums.EEditMaskType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.IllegalArgumentError;
import org.radixware.kernel.common.types.Arr;
import org.radixware.kernel.common.types.Id;

public abstract class EditMask {

    public static EditMask newCopy(final EditMask source) {
        final EditMask copy = createCopy(source);
        if (source.owner instanceof Property) {//TWRBS-3834
            final Property property = ((Property) source.owner);
            if (!property.isMandatory() || property.isReadonly() || !property.isEnabled()) {
                copy.noValueStr = source.noValueStr;
            }
        } else {
            copy.noValueStr = source.noValueStr;
        }
        copy.emptyArrStr = source.emptyArrStr;
        copy.noArrItemValueStr = source.noArrItemValueStr;
        return copy;
    }

    public static EditMask newInstance(final EValType type) {
        switch (type) {
            case INT:
            case ARR_INT:
                return new EditMaskInt();
            case NUM:
            case ARR_NUM:
                return new EditMaskNum();
            case DATE_TIME:
            case ARR_DATE_TIME:
                return new EditMaskDateTime();
            case STR:
            case ARR_STR:
            case CHAR:
            case ARR_CHAR:
            case CLOB:
            case ARR_CLOB:
                return new EditMaskStr();
            case BOOL:
            case ARR_BOOL:
                return new EditMaskBool();
            case PARENT_REF:
            case ARR_REF:
                return new EditMaskRef();
            default:
                return new EditMaskNone();
        }
    }

    public static EditMask newInstance(final EEditMaskType maskType) {
        switch (maskType) {
            case DATE_TIME:
                return new EditMaskDateTime();
            case INT:
                return new EditMaskInt();
            case LIST:
                return new EditMaskList();
            case NUM:
                return new EditMaskNum();
            case STR:
                return new EditMaskStr();
            case TIME_INTERVAL:
                return new EditMaskTimeInterval(Scale.SECOND);
            case BOOL:
                return new EditMaskBool();
            case FILE_PATH:
                return new EditMaskFilePath();
            case OBJECT_REFERENCE:
                return new EditMaskRef();
            default:
                throw new IllegalArgumentError("Mask type " + maskType.name() + " is not supported");
        }
    }

    protected static EditMask createCopy(final EditMask source) {
        final EditMask copy;
        if (source instanceof EditMaskDateTime) {
            copy = new EditMaskDateTime((EditMaskDateTime) source);
        } else if (source instanceof EditMaskConstSet) {
            copy = new EditMaskConstSet((EditMaskConstSet) source);
        } else if (source instanceof EditMaskInt) {
            copy = new EditMaskInt((EditMaskInt) source);
        } else if (source instanceof EditMaskList) {
            copy = new EditMaskList((EditMaskList) source);
        } else if (source instanceof EditMaskNum) {
            copy = new EditMaskNum((EditMaskNum) source);
        } else if (source instanceof EditMaskStr) {
            copy = new EditMaskStr((EditMaskStr) source);
        } else if (source instanceof EditMaskTimeInterval) {
            copy = new EditMaskTimeInterval((EditMaskTimeInterval) source);
        } else if (source instanceof EditMaskBool) {
            copy = new EditMaskBool((EditMaskBool) source);
        } else if (source instanceof EditMaskFilePath) {
            copy = new EditMaskFilePath((EditMaskFilePath) source);
        } else if (source instanceof EditMaskRef) {
            copy = new EditMaskRef((EditMaskRef) source);
        } else {
            copy = new EditMaskNone();
        }
        copy.setEmptyArrayString(source.getEmptyArrayString(null));
        copy.setArrayItemNoValueStr(source.getArrayItemNoValueStr(null));
        return copy;
    }

    public static EditMask loadFrom(final String xml) throws XmlException {
        final org.radixware.schemas.editmask.RadixEditMaskDocument editMaskDoc =
                org.radixware.schemas.editmask.RadixEditMaskDocument.Factory.parse(xml);
        return loadFrom(editMaskDoc);
    }

    public static EditMask loadFrom(final org.radixware.schemas.editmask.RadixEditMaskDocument editMaskDoc) {
        final org.radixware.schemas.editmask.EditMask editMask = editMaskDoc.getRadixEditMask();
        if (editMask.getInt() != null) {
            return new EditMaskInt(editMask.getInt());
        } else if (editMask.getNum() != null) {
            return new EditMaskNum(editMask.getNum());
        } else if (editMask.getBoolean() != null) {
            return new EditMaskBool(editMask.getBoolean());
        } else if (editMask.getStr() != null) {
            return new EditMaskStr(editMask.getStr());
        } else if (editMask.getDateTime() != null) {
            return new EditMaskDateTime(editMask.getDateTime());
        } else if (editMask.getFilePath() != null) {
            return new EditMaskFilePath(editMask.getFilePath());
        } else if (editMask.getTimeInterval() != null) {
            return new EditMaskTimeInterval(editMask.getTimeInterval());
        } else if (editMask.getReference() != null) {            
            return new EditMaskRef(editMask.getReference());
        } else if (editMask.getEnum() != null) {
            throw new IllegalArgumentException("Not supported for edit mask enum");
        } else if (editMask.getList() != null) {
            throw new IllegalArgumentException("Not supported for edit mask list");
        }
        return new EditMaskNone();
    }

    public static EditMask loadEditMaskConstSetFrom(final Id enumId, final String xml) throws XmlException {
        final org.radixware.schemas.editmask.RadixEditMaskDocument editMaskDoc =
                org.radixware.schemas.editmask.RadixEditMaskDocument.Factory.parse(xml);
        return loadEditMaskConstSetFrom(enumId, editMaskDoc);
    }

    public static EditMask loadEditMaskConstSetFrom(final Id enumId, final org.radixware.schemas.editmask.RadixEditMaskDocument xml) {
        final org.radixware.schemas.editmask.EditMask editMask = xml.getRadixEditMask();
        if (editMask.getEnum() != null) {
            return new EditMaskConstSet(enumId, editMask.getEnum());
        }
        throw new IllegalArgumentException("edit mask enum expected");
    }

    public static EditMask loadEditMaskList(final IClientEnvironment environment, final EValType type, final Id titleOwnerId, final String xml) throws XmlException {
        final org.radixware.schemas.editmask.RadixEditMaskDocument editMaskDoc =
                org.radixware.schemas.editmask.RadixEditMaskDocument.Factory.parse(xml);
        return loadEditMaskList(environment, type, titleOwnerId, editMaskDoc);
    }

    public static EditMask loadEditMaskList(final IClientEnvironment environment,
            final EValType type,
            final Id titleOwnerId,
            final org.radixware.schemas.editmask.RadixEditMaskDocument xml) {
        final org.radixware.schemas.editmask.EditMask editMask = xml.getRadixEditMask();
        if (editMask.getList() != null) {
            return new EditMaskList(environment, editMask.getList(), type, titleOwnerId);
        }
        throw new IllegalArgumentException("edit mask list expected");
    }
    
    protected ModelItem owner;
    private String noValueStr;
    private String noArrItemValueStr;
    private String emptyArrStr = "";

    public void setOwnerModelItem(final ModelItem owner) {
        this.owner = owner;
    }

    protected void afterModify() {
        if (owner != null) {
            owner.afterModify();
        }
    }

    protected String arrToStr(final IClientEnvironment context, final Arr arr) {//RADIX-1077
        if (arr.isEmpty()) {
            return emptyArrStr;
        } else {
            final StringBuilder strValue = new StringBuilder();
            final boolean needQuote = this.getType() == EEditMaskType.STR || this.getType() == EEditMaskType.ENUM;
            for (int i = 0; i < arr.size(); ++i) {
                if (needQuote) {
                    strValue.append('\'');
                }
                if (arr.get(i) == null && noArrItemValueStr != null) {
                    strValue.append(noArrItemValueStr);
                } else {
                    strValue.append(toStr(context, arr.get(i)));
                }
                if (needQuote) {
                    strValue.append('\'');
                }
                if (i < (arr.size() - 1)) {
                    strValue.append("; ");
                }
            }
            return strValue.toString();
        }
    }

    protected final ValidationResult validateArray(final IClientEnvironment environment, final Arr arr) {
        if (arr == null) {
            return ValidationResult.ACCEPTABLE;
        }
        ValidationResult result;
        for (int i = 0; i < arr.size(); ++i) {
            result = validate(environment, arr.get(i));
            if (result.getState() != EValidatorState.ACCEPTABLE) {
                return result;
            }
        }
        return ValidationResult.ACCEPTABLE;
    }

    public abstract String toStr(IClientEnvironment environment, Object o);

    public abstract ValidationResult validate(IClientEnvironment environment, Object o);

    public abstract EEditMaskType getType();

    public abstract EnumSet<EValType> getSupportedValueTypes();

    public boolean isSpecialValue(final Object o) {
        return o == null;// || wasInherited();
    }

    public final void setNoValueStr(final String noValueTitle) {
        noValueStr = noValueTitle;
        afterModify();
    }

    public final String getNoValueStr(final MessageProvider mp) {
        if (mp != null && owner instanceof Property) {//TWRBS-3834
            final Property property = ((Property) owner);
            if (property.isMandatory() && !property.isReadonly() && property.isEnabled()) {
                return mp.translate("Value", "<not defined>");
            }
        }
        return noValueStr == null ? (mp == null ? null : mp.translate("Value", "<not defined>")) : noValueStr;
    }

    public final void setArrayItemNoValueStr(final String noValueTitle) {
        noArrItemValueStr = noValueTitle;
        afterModify();
    }

    public final String getArrayItemNoValueStr(final MessageProvider mp) {
        return noArrItemValueStr == null ? (mp == null ? null : mp.translate("Value", "<not defined>")) : noArrItemValueStr;
    }

    public final String getEmptyArrayString(final MessageProvider mp) {
        return emptyArrStr == null ? (mp == null ? null : mp.translate("Value", "<empty>")) : emptyArrStr;
    }

    public final void setEmptyArrayString(final String str) {
        emptyArrStr = str;
        afterModify();
    }

    @Override
    public String toString() {
        return "";
    }

    public RadEnumPresentationDef getRadEnumPresentationDef(final IClientApplication context) {
        return null;
    }

    public abstract void writeToXml(final org.radixware.schemas.editmask.EditMask editMask);

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.owner);
        hash = 29 * hash + Objects.hashCode(this.noValueStr);
        hash = 29 * hash + Objects.hashCode(this.noArrItemValueStr);
        hash = 29 * hash + Objects.hashCode(this.emptyArrStr);
        return hash;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EditMask other = (EditMask) obj;
        if (owner instanceof Property) {
            final Property property = ((Property) owner);
            if (!property.isMandatory() || property.isReadonly() || !property.isEnabled()) {
                if (!Objects.equals(this.noValueStr, other.noValueStr)) {
                    return false;
                }
            }
        } else if (other.owner instanceof Property) {
            final Property property = ((Property) other.owner);
            if (!property.isMandatory() || property.isReadonly() || !property.isEnabled()) {
                if (!Objects.equals(this.noValueStr, other.noValueStr)) {
                    return false;
                }
            }
        } else {
            if (!Objects.equals(this.noValueStr, other.noValueStr)) {
                return false;
            }
        }
        if (!Objects.equals(this.noArrItemValueStr, other.noArrItemValueStr)) {
            return false;
        }
        return Objects.equals(this.emptyArrStr, other.emptyArrStr);
    }
}
