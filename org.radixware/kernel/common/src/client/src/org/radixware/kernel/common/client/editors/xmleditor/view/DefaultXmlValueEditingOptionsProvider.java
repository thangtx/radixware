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

package org.radixware.kernel.common.client.editors.xmleditor.view;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaAnnotation;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlAnySimpleType;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.impl.values.XmlObjectBase;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.EditMaskBool;
import org.radixware.kernel.common.client.meta.mask.EditMaskConstSet;
import org.radixware.kernel.common.client.meta.mask.EditMaskDateTime;
import org.radixware.kernel.common.client.meta.mask.EditMaskInt;
import org.radixware.kernel.common.client.meta.mask.EditMaskList;
import org.radixware.kernel.common.client.meta.mask.EditMaskNone;
import org.radixware.kernel.common.client.meta.mask.EditMaskNum;
import org.radixware.kernel.common.client.meta.mask.EditMaskStr;
import org.radixware.kernel.common.client.meta.mask.validators.IInputValidator;
import org.radixware.kernel.common.client.meta.mask.validators.ValidatorsFactory;
import org.radixware.kernel.common.client.editors.xmleditor.model.IXmlValueEditingOptionsProvider;
import org.radixware.kernel.common.client.editors.xmleditor.model.validators.DurationValidator;
import org.radixware.kernel.common.client.editors.xmleditor.model.validators.GDayValidator;
import org.radixware.kernel.common.client.editors.xmleditor.model.validators.GMonthDayValidator;
import org.radixware.kernel.common.client.editors.xmleditor.model.validators.GMonthValidator;
import org.radixware.kernel.common.client.editors.xmleditor.model.validators.GYearMonthValidator;
import org.radixware.kernel.common.client.editors.xmleditor.model.validators.GYearValidator;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.types.Id;


final class DefaultXmlValueEditingOptionsProvider implements IXmlValueEditingOptionsProvider {

    private final static XmlValueEditingOptions DEFAULT = new XmlValueEditingOptions(EValType.STR, new EditMaskStr(), false, true, null);
    private final static XmlValueEditingOptions DEFAULT_NILLABLE = new XmlValueEditingOptions(EValType.STR, new EditMaskStr(), false, false, null);
    private final static XmlValueEditingOptions DEFAULT_READONLY = new XmlValueEditingOptions(EValType.STR, new EditMaskStr(), true, true, null);
    private final static String TYPES_NAMESPACE = "http://schemas.radixware.org/types.xsd";
    private final static String EDIT_MASK_TAG = "EditMask";
    private final static String EDIT_MASK_PRESENTATION_TAG = "editMask";
    private final static String CLASS_TAG = "class";
    private final static QName CLASS_ID_ATTR_NAME = new QName("classId");
    private final static String PRESENT_TAG = "presentation";
    private final IClientEnvironment environment;

    public DefaultXmlValueEditingOptionsProvider(final IClientEnvironment environment) {
        this.environment = environment;
    }

    private static Id getConstSetId(final SchemaAnnotation a) {
        if (a != null) {
            XmlObject[] inf = a.getApplicationInformation();
            for (XmlObject x : inf) {
                if (x.getDomNode().getLocalName().equals("appinfo")) {
                    final XmlCursor cursor = x.newCursor();
                    try {
                        if (cursor.toChild(TYPES_NAMESPACE, CLASS_TAG)) {//<class>
                            final String classId = cursor.getAttributeText(CLASS_ID_ATTR_NAME);
                            if (classId != null) {
                                return Id.Factory.loadFrom(classId);
                            }
                            final String className = cursor.getTextValue();
                            if (className != null && !className.isEmpty()) {
                                int pointIndex = className.lastIndexOf('.');
                                if (pointIndex > -1) {
                                    return Id.Factory.loadFrom(className.substring(pointIndex + 1));
                                }
                            }
                        }
                    } finally {
                        cursor.dispose();
                    }
                }
            }
        }
        return null;
    }

    private static XmlObject getPresentationTag(final SchemaAnnotation sa) {
        if (sa != null) {
            final XmlObject[] inf = sa.getApplicationInformation();
            for (XmlObject x : inf) {
                if (x.getDomNode().getLocalName().equals("appinfo")) {
                    final XmlCursor cursor = x.newCursor();
                    try {
                        if (cursor.toChild(TYPES_NAMESPACE, PRESENT_TAG)) {
                            return cursor.getObject();
                        }
                    } finally {
                        cursor.dispose();
                    }
                }
            }
        }
        return null;
    }

    private EditMask getMaskFromPresentation(final XmlObject presentation) {
        if (presentation != null) {
            final XmlCursor cursor = presentation.newCursor();
            try {
                if (cursor.toChild(TYPES_NAMESPACE, EDIT_MASK_PRESENTATION_TAG)
                        && cursor.toChild(EDIT_MASK_TAG)
                        && cursor.toFirstChild()) {
                    final String maskText = cursor.xmlText();
                    try {
                        return EditMask.loadFrom(maskText);
                    } catch (XmlException xmle) {
                        environment.getTracer().error(xmle);
                        return null;
                    }
                }
            } finally {
                cursor.dispose();
            }
        }
        return null;
    }

    private XmlValueEditingOptions getItemAnnotation(final SchemaAnnotation itemAnnotation, final SchemaType type, final boolean isReadOnly, final boolean isNotNull) {
        Id constSetId = getConstSetId(itemAnnotation);
        if (constSetId != null) {
            final EditMaskConstSet editMask = new EditMaskConstSet(constSetId);
            final EValType valType;
            try {
                valType = editMask.getRadEnumPresentationDef(environment.getApplication()).getItemType();
                return new XmlValueEditingOptions(valType, editMask, isReadOnly, isNotNull, type);
            } catch (DefinitionError error) {
                environment.getTracer().error(error);
            }
        }
        XmlObject presentationTag = getPresentationTag(itemAnnotation);
        if (presentationTag != null) {
            EditMask editMask = getMaskFromPresentation(presentationTag);
            if (editMask != null) {
                final int xmlType = type.getBuiltinTypeCode() == 0 ? type.getBaseType().getBuiltinTypeCode() : type.getBuiltinTypeCode();
                final EValType valType = getValTypeByXmlType(xmlType, type.getSimpleVariety() == SchemaType.ATOMIC);
                return new XmlValueEditingOptions(valType, editMask, isReadOnly, isNotNull, type);
            }
        }
        return null;
    }

    @Override
    public XmlValueEditingOptions getEditingOptions(final SchemaType schemaType, final SchemaAnnotation itemAnnotation, final boolean isReadOnly, final boolean isNotNull) {
        if (schemaType == null) {
            if (isReadOnly) {
                return DEFAULT_READONLY;
            } else if (!isNotNull) {
                return DEFAULT_NILLABLE;
            } else {
                return DEFAULT;
            }
        }
        XmlValueEditingOptions edOption;
        if (itemAnnotation != null) {
            edOption = getItemAnnotation(itemAnnotation, schemaType, isReadOnly, isNotNull);
            if (edOption != null) {
                return edOption;
            }
        }
        for (SchemaType type = schemaType; type != null; type = type.getBaseType()) {
            if (type.getAnnotation() != null) {
                edOption = getItemAnnotation(type.getAnnotation(), schemaType, isReadOnly, isNotNull);
                if (edOption != null) {
                    return edOption;
                }
            }
        }
        final SchemaType type = schemaType.isSimpleType() ? schemaType : schemaType.getPrimitiveType();
        if (type != null && type.isSimpleType()) {
            final boolean isList = type.getSimpleVariety() == SchemaType.LIST;
            final XmlAnySimpleType[] enums = schemaType.getEnumerationValues();
            if (enums != null && enums.length > 0) {
                final List<EditMaskList.Item> items = new LinkedList<>();
                for (XmlAnySimpleType enumItem : enums) {
                    items.add(new EditMaskList.Item(enumItem.getStringValue(), enumItem.getStringValue()));
                }
                final EditMaskList mask = new EditMaskList(items);
                return new XmlValueEditingOptions(isList ? EValType.ARR_STR : EValType.STR, mask, false, true, schemaType);
            }
            final int xmlType;
            if (isList) {
                final SchemaType itemType = type.getListItemType();
                if (itemType.isSimpleType()){
                    if (itemType.getBuiltinTypeCode()==0 && itemType.getPrimitiveType()!=null){
                        xmlType = itemType.getPrimitiveType().getBuiltinTypeCode();
                    }else{
                        xmlType = itemType.getBuiltinTypeCode();
                    }
                }else{
                    xmlType = itemType.getPrimitiveType().getBuiltinTypeCode();
                }                
            } else {
                xmlType = type.isPrimitiveType() ? type.getBuiltinTypeCode() : type.getBaseType().getBuiltinTypeCode();
            }
            if (xmlType==SchemaType.BTC_NOT_BUILTIN){
                return DEFAULT;
            }
            final EditMask mask = getMaskByValType(xmlType);
            if (mask==null){
                return DEFAULT;
            }
            if (xmlType == SchemaType.BTC_HEX_BINARY || xmlType == SchemaType.BTC_BASE_64_BINARY) {
                return new XmlValueEditingOptions(isList ? EValType.ARR_BIN : EValType.BIN, mask, isReadOnly, isNotNull, schemaType);
            }
            switch (mask.getType()) {
                case INT:
                    XmlAnySimpleType facet = schemaType.getFacet(SchemaType.FACET_MAX_EXCLUSIVE);
                    if (facet instanceof XmlObjectBase && !((XmlObjectBase) facet).isNil()) {
                        long maxInclusive = ((XmlObjectBase) facet).getLongValue() - 1;
                        if (maxInclusive != ((EditMaskInt) mask).getMaxValue()) {
                            ((EditMaskInt) mask).setMaxValue(maxInclusive);
                        }
                    }
                    facet = schemaType.getFacet(SchemaType.FACET_MAX_INCLUSIVE);
                    if (facet instanceof XmlObjectBase && !((XmlObjectBase) facet).isNil()) {
                        long maxInclusive = ((XmlObjectBase) facet).getLongValue();
                        if (maxInclusive != ((EditMaskInt) mask).getMaxValue()) {
                            ((EditMaskInt) mask).setMaxValue(maxInclusive);
                        }
                    }
                    facet = schemaType.getFacet(SchemaType.FACET_MIN_EXCLUSIVE);
                    if (facet instanceof XmlObjectBase && !((XmlObjectBase) facet).isNil()) {
                        long minInclusive = ((XmlObjectBase) facet).getLongValue() + 1;
                        if (minInclusive != ((EditMaskInt) mask).getMinValue()) {
                            ((EditMaskInt) mask).setMinValue(minInclusive);
                        }
                    }
                    facet = schemaType.getFacet(SchemaType.FACET_MIN_INCLUSIVE);
                    if (facet instanceof XmlObjectBase && !((XmlObjectBase) facet).isNil()) {
                        long minInclusive = ((XmlObjectBase) facet).getLongValue();
                        if (minInclusive != ((EditMaskInt) mask).getMinValue()) {
                            ((EditMaskInt) mask).setMinValue(minInclusive);
                        }
                    }
                    facet = schemaType.getFacet(SchemaType.FACET_MIN_LENGTH);
                    if (facet instanceof XmlObjectBase && !((XmlObjectBase) facet).isNil()) {
                        byte minLength = ((XmlObjectBase) facet).getByteValue();
                        if (minLength != ((EditMaskInt) mask).getMinLength()) {
                            ((EditMaskInt) mask).setMinLength(minLength, null);
                        }
                    }
                    break;
                case NUM:
                    facet = schemaType.getFacet(SchemaType.FACET_MIN_INCLUSIVE);
                    if (facet instanceof XmlObjectBase && !((XmlObjectBase) facet).isNil()) {
                        BigDecimal minInclusive = ((XmlObjectBase) facet).getBigDecimalValue();
                        if (minInclusive != ((EditMaskNum) mask).getMinValue()) {
                            ((EditMaskNum) mask).setMinValue(minInclusive);
                        }
                    }
                    facet = schemaType.getFacet(SchemaType.FACET_MAX_INCLUSIVE);
                    if (facet instanceof XmlObjectBase && !((XmlObjectBase) facet).isNil()) {
                        BigDecimal maxInclusive = ((XmlObjectBase) facet).getBigDecimalValue();
                        if (maxInclusive != ((EditMaskNum) mask).getMaxValue()) {
                            ((EditMaskNum) mask).setMaxValue(maxInclusive);
                        }
                    }
                    facet = schemaType.getFacet(SchemaType.FACET_FRACTION_DIGITS);
                    if (facet instanceof XmlObjectBase && !((XmlObjectBase) facet).isNil()) {
                        int precision = ((XmlObjectBase) facet).getIntValue();
                        if (precision != ((EditMaskNum) mask).getPrecision()) {
                            ((EditMaskNum) mask).setPrecision(precision);
                        }
                    }
                    break;
                case STR:
                    facet = schemaType.getFacet(SchemaType.FACET_MAX_LENGTH);
                    if (facet instanceof XmlObjectBase && !((XmlObjectBase) facet).isNil()) {
                        int length = ((XmlObjectBase) facet).getIntValue();
                        if (length != ((EditMaskStr) mask).getMaxLength()) {
                            ((EditMaskStr) mask).setMaxLength(length);
                        }
                    }
                    break;
            }
            final EValType valType = getValTypeByXmlType(xmlType, type.getSimpleVariety() == SchemaType.ATOMIC);
            return new XmlValueEditingOptions(valType, mask, isReadOnly, isNotNull, schemaType);
        }
        return null;
    }

    private static EditMask getMaskByValType(final int valType) {
        final EditMaskStr editMaskStr;
        switch (valType) {
            //primitive
            case SchemaType.BTC_DECIMAL:
            case SchemaType.BTC_DOUBLE:
            case SchemaType.BTC_FLOAT:
                return new EditMaskNum();
            case SchemaType.BTC_BOOLEAN:
                return new EditMaskBool();
            case SchemaType.BTC_DATE_TIME:
                return new EditMaskDateTime("yyyy-MM-dd'T'hh:mm:ss", null, null);
            case SchemaType.BTC_DATE:
                return new EditMaskDateTime("yyyy-MM-dd", null, null);
            case SchemaType.BTC_TIME:
                return new EditMaskDateTime("hh:mm:ss", null, null);
            case SchemaType.BTC_G_DAY:
                editMaskStr = new EditMaskStr();
                editMaskStr.setValidator(new GDayValidator());
                return editMaskStr;
            case SchemaType.BTC_G_MONTH:
                editMaskStr = new EditMaskStr();
                editMaskStr.setValidator(new GMonthValidator());
                return editMaskStr;
            case SchemaType.BTC_G_MONTH_DAY:
                editMaskStr = new EditMaskStr();
                editMaskStr.setValidator(new GMonthDayValidator());
                return editMaskStr;
            case SchemaType.BTC_G_YEAR:
                editMaskStr = new EditMaskStr();
                editMaskStr.setValidator(new GYearValidator());
                return editMaskStr;
            case SchemaType.BTC_G_YEAR_MONTH:
                editMaskStr = new EditMaskStr();
                editMaskStr.setValidator(new GYearMonthValidator());
                return editMaskStr;
            case SchemaType.BTC_DURATION:
                editMaskStr = new EditMaskStr();
                editMaskStr.setValidator(new DurationValidator());
                return editMaskStr;
            case SchemaType.BTC_HEX_BINARY:
            case SchemaType.BTC_BASE_64_BINARY:
                return new EditMaskNone();
            case SchemaType.BTC_ANY_URI:
            case SchemaType.BTC_QNAME:
            case SchemaType.BTC_NOTATION:
                return new EditMaskStr();
            //derived
            case SchemaType.BTC_INTEGER:
                return new EditMaskNum(null, null, 0, null, (byte) 0);
            case SchemaType.BTC_LANGUAGE:
                editMaskStr = new EditMaskStr();
                IInputValidator validator = ValidatorsFactory.createRegExpValidator("[a-zA-Z]{1,8}(-[a-zA-Z0-9]{1,8})*", true);
                editMaskStr.setValidator(validator);
                return editMaskStr;
            case SchemaType.BTC_STRING:
            case SchemaType.BTC_TOKEN:
            case SchemaType.BTC_IDREF:
            case SchemaType.BTC_IDREFS:
            case SchemaType.BTC_NAME:
            case SchemaType.BTC_NORMALIZED_STRING:
            case SchemaType.BTC_NCNAME:
            case SchemaType.BTC_NMTOKENS:
            case SchemaType.BTC_NMTOKEN:
            case SchemaType.BTC_ID:
            case SchemaType.BTC_ENTITIES:
            case SchemaType.BTC_ENTITY:
                return new EditMaskStr();
            case SchemaType.BTC_NEGATIVE_INTEGER:
                return new EditMaskNum(null, BigDecimal.valueOf(-1), 0, null, (byte) 0);
            case SchemaType.BTC_NON_POSITIVE_INTEGER:
                return new EditMaskNum(null, BigDecimal.ZERO, 0, null, (byte) 0);
            case SchemaType.BTC_LONG:
                return new EditMaskNum(new BigDecimal("-9223372036854775808"), new BigDecimal("9223372036854775807"), 0, null, (byte) 0);
            case SchemaType.BTC_UNSIGNED_BYTE:
                return new EditMaskInt(Long.valueOf(0), Long.valueOf(255), (byte) 0, null, 1, null, (byte) 10);
            case SchemaType.BTC_POSITIVE_INTEGER:
                return new EditMaskNum(BigDecimal.ONE, null, 0, null, (byte) 0);
            case SchemaType.BTC_UNSIGNED_LONG:
                return new EditMaskNum(BigDecimal.ZERO, new BigDecimal("18446744073709551615"), 0, null, (byte) 0);
            case SchemaType.BTC_UNSIGNED_INT:
                return new EditMaskInt(Long.valueOf(0), Long.valueOf(4294967295L), (byte) 0, null, 1, null, (byte) 10);
            case SchemaType.BTC_UNSIGNED_SHORT:
                return new EditMaskInt(Long.valueOf(0), Long.valueOf(65535), (byte) 0, null, 1, null, (byte) 10);
            case SchemaType.BTC_BYTE:
                return new EditMaskInt(Long.valueOf(Byte.MIN_VALUE), Long.valueOf(Byte.MAX_VALUE), (byte) 0, null, 1, null, (byte) 10);
            case SchemaType.BTC_SHORT:
                return new EditMaskInt(Long.valueOf(Short.MIN_VALUE), Long.valueOf(Short.MAX_VALUE), (byte) 0, null, 1, null, (byte) 10);
            case SchemaType.BTC_INT:
                return new EditMaskInt(Long.valueOf(Integer.MIN_VALUE), Long.valueOf(Integer.MAX_VALUE), (byte) 0, null, 1, null, (byte) 10);
            case SchemaType.BTC_NON_NEGATIVE_INTEGER:
                return new EditMaskNum(BigDecimal.ZERO, null, 0, null, (byte) 0);
            default:
                return null;
        }
    }

    private static EValType getValTypeByXmlType(final int xmlType, final boolean isAtomic) {
        if (isAtomic) {
            switch (xmlType) {
                //primitive
                case SchemaType.BTC_DECIMAL:
                case SchemaType.BTC_DOUBLE:
                case SchemaType.BTC_FLOAT:
                    return EValType.NUM;
                case SchemaType.BTC_BOOLEAN:
                    return EValType.BOOL;
                case SchemaType.BTC_DATE_TIME:
                case SchemaType.BTC_DATE:
                case SchemaType.BTC_TIME:
                    return EValType.DATE_TIME;
                case SchemaType.BTC_STRING:
                case SchemaType.BTC_G_DAY:
                case SchemaType.BTC_G_MONTH:
                case SchemaType.BTC_G_MONTH_DAY:
                case SchemaType.BTC_G_YEAR:
                case SchemaType.BTC_G_YEAR_MONTH:
                case SchemaType.BTC_DURATION:
                case SchemaType.BTC_ANY_URI:
                case SchemaType.BTC_QNAME:
                case SchemaType.BTC_NOTATION:
                    return EValType.STR;
                case SchemaType.BTC_HEX_BINARY:
                case SchemaType.BTC_BASE_64_BINARY:
                    return EValType.BIN;
                //derived
                case SchemaType.BTC_TOKEN:
                case SchemaType.BTC_IDREF:
                case SchemaType.BTC_IDREFS:
                case SchemaType.BTC_NAME:
                case SchemaType.BTC_NORMALIZED_STRING:
                case SchemaType.BTC_NCNAME:
                case SchemaType.BTC_NMTOKENS:
                case SchemaType.BTC_NMTOKEN:
                case SchemaType.BTC_ID:
                case SchemaType.BTC_ENTITIES:
                case SchemaType.BTC_ENTITY:
                case SchemaType.BTC_LANGUAGE:
                    return EValType.STR;
                case SchemaType.BTC_NEGATIVE_INTEGER:
                case SchemaType.BTC_NON_POSITIVE_INTEGER:
                case SchemaType.BTC_POSITIVE_INTEGER:
                case SchemaType.BTC_UNSIGNED_LONG:
                case SchemaType.BTC_NON_NEGATIVE_INTEGER:
                case SchemaType.BTC_INTEGER:
                    return EValType.NUM;
                case SchemaType.BTC_LONG:
                case SchemaType.BTC_UNSIGNED_BYTE:
                case SchemaType.BTC_UNSIGNED_INT:
                case SchemaType.BTC_UNSIGNED_SHORT:
                case SchemaType.BTC_BYTE:
                case SchemaType.BTC_SHORT:
                case SchemaType.BTC_INT:
                    return EValType.INT;
                default:
                    throw new IllegalArgumentException("Unsupported value type " + xmlType);
            }
        } else {
            switch (xmlType) {
                //primitive
                case SchemaType.BTC_DECIMAL:
                case SchemaType.BTC_DOUBLE:
                case SchemaType.BTC_FLOAT:
                    return EValType.ARR_NUM;
                case SchemaType.BTC_BOOLEAN:
                    return EValType.ARR_BOOL;
                case SchemaType.BTC_DATE_TIME:
                case SchemaType.BTC_DATE:
                case SchemaType.BTC_TIME:
                    return EValType.ARR_DATE_TIME;
                case SchemaType.BTC_STRING:
                case SchemaType.BTC_G_DAY:
                case SchemaType.BTC_G_MONTH:
                case SchemaType.BTC_G_MONTH_DAY:
                case SchemaType.BTC_G_YEAR:
                case SchemaType.BTC_G_YEAR_MONTH:
                case SchemaType.BTC_DURATION:
                case SchemaType.BTC_ANY_URI:
                case SchemaType.BTC_QNAME:
                case SchemaType.BTC_NOTATION:
                    return EValType.ARR_STR;
                case SchemaType.BTC_HEX_BINARY:
                case SchemaType.BTC_BASE_64_BINARY:
                    return EValType.ARR_BIN;
                //derived
                case SchemaType.BTC_TOKEN:
                case SchemaType.BTC_IDREF:
                case SchemaType.BTC_IDREFS:
                case SchemaType.BTC_NAME:
                case SchemaType.BTC_NORMALIZED_STRING:
                case SchemaType.BTC_NCNAME:
                case SchemaType.BTC_NMTOKENS:
                case SchemaType.BTC_NMTOKEN:
                case SchemaType.BTC_ID:
                case SchemaType.BTC_ENTITIES:
                case SchemaType.BTC_ENTITY:
                case SchemaType.BTC_LANGUAGE:
                    return EValType.ARR_STR;
                case SchemaType.BTC_NEGATIVE_INTEGER:
                case SchemaType.BTC_NON_POSITIVE_INTEGER:
                case SchemaType.BTC_POSITIVE_INTEGER:
                case SchemaType.BTC_UNSIGNED_LONG:
                case SchemaType.BTC_NON_NEGATIVE_INTEGER:
                case SchemaType.BTC_INTEGER:
                    return EValType.ARR_NUM;
                case SchemaType.BTC_LONG:
                case SchemaType.BTC_UNSIGNED_BYTE:
                case SchemaType.BTC_UNSIGNED_INT:
                case SchemaType.BTC_UNSIGNED_SHORT:
                case SchemaType.BTC_BYTE:
                case SchemaType.BTC_SHORT:
                case SchemaType.BTC_INT:
                    return EValType.ARR_INT;
                default:
                    throw new IllegalArgumentException("Unsupported value type " + xmlType);
            }
        }
    }
}
