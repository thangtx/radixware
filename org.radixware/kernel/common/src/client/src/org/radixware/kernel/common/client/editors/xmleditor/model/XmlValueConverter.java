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

package org.radixware.kernel.common.client.editors.xmleditor.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.apache.xmlbeans.GDate;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.common.ValidationContext;
import org.apache.xmlbeans.impl.values.XmlDateImpl;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.types.ArrRef;
import org.radixware.kernel.common.client.types.Reference;
import org.radixware.kernel.common.client.utils.ValueConverter;
import org.radixware.kernel.common.enums.EEditMaskType;
import org.radixware.kernel.common.enums.EValType;
import static org.radixware.kernel.common.enums.EValType.CHAR;
import static org.radixware.kernel.common.enums.EValType.INT;
import org.radixware.kernel.common.exceptions.WrongFormatError;
import org.radixware.kernel.common.types.Arr;
import org.radixware.kernel.common.types.ArrBin;
import org.radixware.kernel.common.types.ArrBool;
import org.radixware.kernel.common.types.ArrChar;
import org.radixware.kernel.common.types.ArrDateTime;
import org.radixware.kernel.common.types.ArrInt;
import org.radixware.kernel.common.types.ArrNum;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.types.Bin;
import org.radixware.kernel.common.utils.Base64;


public final class XmlValueConverter {

    private final static class ValidationContextImpl implements ValidationContext {

        private String invalidValueReason = null;

        @Override
        public void invalid(final String string) {
            invalidValueReason = string;
        }

        @Override
        public void invalid(final String string, final Object[] os) {
            invalidValueReason = string;
        }

        public boolean isValid() {
            return invalidValueReason == null;
        }

        public String getInvalidValueReason() {
            return invalidValueReason;
        }
    }
    private static final XmlValueConverter INSTANCE = new XmlValueConverter();

    private XmlValueConverter() {
    }

    public static XmlValueConverter getInstance() {
        return INSTANCE;
    }

    public String valueToXmlString(final Object value, final EditMask mask, final IClientEnvironment environment, final SchemaType schemaType) {
        if (value == null) {
            return null;
        }
        if (value instanceof Arr) {
            final StringBuilder valueStr = new StringBuilder();
            final SchemaType itemType = schemaType.getListItemType();
            for (Object itemValue : (Arr) value) {
                if (valueStr.length() > 0) {
                    valueStr.append(' ');
                }
                valueStr.append(valueToXmlString(itemValue, mask, environment, itemType));
            }
            return valueStr.toString();
        }
        if (schemaType == null) {
            return String.valueOf(value);
        }
        SchemaType baseType = schemaType;
        while(baseType.getBuiltinTypeCode()==0 && baseType.getBaseType()!=null){
            baseType = baseType.getBaseType();
        }
        if (baseType.getBuiltinTypeCode() == SchemaType.BTC_BASE_64_BINARY) {
            return ((Bin) value).getAsBase64();
        } else if (baseType.getBuiltinTypeCode() == SchemaType.BTC_HEX_BINARY) {
            return ValueConverter.arrByte2Str(((Bin) value).get(), "");
        } else {
            switch (mask.getType()) {
                case STR:
                case LIST:
                case ENUM:
                case FILE_PATH:
                case TIME_INTERVAL:
                case BOOL:
                case NUM:
                case INT:
                    return String.valueOf(value);
                case DATE_TIME:
                    return mask.toStr(environment, value);
                case OBJECT_REFERENCE:
                    return ((Reference)value).toValAsStr();
                default:
                    throw new IllegalArgumentException("Unsupported mask type " + mask.getType());
            }
        }
    }

    private static Object xmlStringToValueImpl(final String valAsStr, final EEditMaskType maskType, final EValType valType, final SchemaType schemaType) {
        if (valAsStr == null) {
            return null;
        }
        SchemaType baseType = schemaType;
        while(baseType.getBuiltinTypeCode()==0 && baseType.getBaseType()!=null){
            baseType = baseType.getBaseType();
        }
        if (baseType.getBuiltinTypeCode() == SchemaType.BTC_BASE_64_BINARY) {
            byte b[] = Base64.decode(valAsStr);
            if (b == null || b.length == 0) {
                throw new WrongFormatError("Wrong Base64");
            }
            return Bin.wrap(b);
        } else if (baseType.getBuiltinTypeCode() == SchemaType.BTC_HEX_BINARY) {
            return ValueConverter.hexadecimalString2Bin(valAsStr, "");
        } else {
            switch (maskType) {
                case STR:
                case FILE_PATH:
                case LIST:
                    return valAsStr;
                case BOOL:
                    if ("1".equals(valAsStr) || "true".equals(valAsStr)) {
                        return Boolean.TRUE;
                    }
                    if ("0".equals(valAsStr) || "false".equals(valAsStr)) {
                        return Boolean.FALSE;
                    }
                    throw new WrongFormatError("\"" + valAsStr + "\" is not correct boolean value");
                case NUM:
                    return new BigDecimal(valAsStr);
                case INT:
                case TIME_INTERVAL:
                    return Long.valueOf(valAsStr);
                case OBJECT_REFERENCE:
                    return Reference.fromValAsStr(valAsStr);
                case DATE_TIME:                    
                    final ValidationContextImpl _vc = new ValidationContextImpl();                  
                    final GDate date = XmlDateImpl.lex(valAsStr, schemaType, _vc);//NOPMD
                    if (!_vc.isValid()) {
                        throw new WrongFormatError(_vc.getInvalidValueReason());
                    }                    
                    if (date != null) {
                        final long mills = date.getCalendar().getTimeInMillis();
                        return new Timestamp(mills);
                    }
                    return null;
                case ENUM:
                    switch (valType) {
                        case INT:
                            return Long.valueOf(valAsStr);
                        case CHAR:
                            return Character.valueOf(valAsStr.charAt(0));
                        default:
                            return valAsStr;
                    }
            }
            throw new IllegalArgumentException("Unsupported mask type " + maskType);
        }
    }

    private static Arr createArrInstance(final EEditMaskType maskType, final EValType valType) {
        if (valType == EValType.BIN || valType == EValType.BLOB) {
            return new ArrBin();
        }
        switch (maskType) {
            case STR:
            case FILE_PATH:
                return new ArrStr();
            case OBJECT_REFERENCE:
                return new ArrRef();
            case LIST:
                switch (valType) {
                    case INT:
                        return new ArrInt();
                    case NUM:
                        return new ArrNum();
                    case CHAR:
                        return new ArrChar();
                    default:
                        return new ArrStr();
                }
            case BOOL:
                return new ArrBool();
            case NUM:
                return new ArrNum();
            case INT:
            case TIME_INTERVAL:
                return new ArrInt();
            case DATE_TIME:
                return new ArrDateTime();
            case ENUM:
                switch (valType) {
                    case INT:
                        return new ArrInt();
                    case CHAR:
                        return new ArrChar();
                    default:
                        return new ArrStr();
                }
            default:
                return new ArrStr();
        }
    }

    @SuppressWarnings("unchecked")
    public Object xmlStringToValue(final String valAsStr, final EEditMaskType maskType, final EValType valType, final SchemaType schemaType) {
        if (schemaType == null) {
            return valAsStr;
        }
        if (valAsStr == null) {
            return null;
        }
        if (valType.isArrayType()) {
            final String[] items = valAsStr.split(" ");
            final Arr arr = createArrInstance(maskType, valType.getArrayItemType());
            for (String item : items) {
                arr.add(xmlStringToValueImpl(item, maskType, valType, schemaType.getListItemType()));
            }
            return arr;
        } else {
            return xmlStringToValueImpl(valAsStr, maskType, valType, schemaType);
        }

    }
}