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

package org.radixware.kernel.common.defs.value;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.radixware.kernel.common.enums.EValType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.exceptions.WrongFormatError;
import org.radixware.kernel.common.types.ArrBin;
import org.radixware.kernel.common.types.ArrBool;
import org.radixware.kernel.common.types.ArrChar;
import org.radixware.kernel.common.types.ArrDateTime;
import org.radixware.kernel.common.types.ArrInt;
import org.radixware.kernel.common.types.ArrNum;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.types.Bin;
import org.radixware.kernel.common.types.IKernelEnum;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.types.Pid;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.schemas.eas.Property;

/**
 * Value in "RadixWare Value As String" format. Used for the RadixWare object
 * values. Supports any of {@link EValType}. Pattern: Immutable.
 *
 */
public class ValAsStr {

    protected final String valAsStr; // can't contain null

    protected ValAsStr(String valAsStr) {
        super();
        this.valAsStr = valAsStr;
    }

    public static Object fromStr(final String valAsStr, final EValType valType) {
        return fromStr(ValAsStr.Factory.loadFrom(valAsStr), valType);
    }

    public static Object fromStr(final ValAsStr valAsStr, final EValType valType) {
        if (valAsStr == null) {
            return null;
        }
        return valAsStr.toObject(valType);
    }

    public static String toStr(final Object obj, final EValType valType) {
        final ValAsStr v = ValAsStr.Factory.newInstance(obj, valType);
        if (v == null) {
            return null;
        }
        return v.toString();
    }

    @Override
    public String toString() {
        return valAsStr;
    }

    public static final class Factory {

        private Factory() {
        }

        /**
         * Restore ValAsStr from string.
         *
         * @return new instance of ValAsStr or null is valAsStr is null or
         * empty.
         */
        public static ValAsStr loadFrom(String valAsStr) {
            if (valAsStr == null /*|| valAsStr.isEmpty() //  by BAO to preserve empty str*/) {
                return null;
            }
            return new ValAsStr(valAsStr);
        }

        /**
         * Restore ValAsStr from object
         *
         * @return new instance of ValAsStr or null is object is null.
         */
        public static ValAsStr newInstance(Object obj, EValType valType) {
            if (obj == null) {
                return null;
            }

            if (obj instanceof IKernelEnum) {
                final IKernelEnum e = (IKernelEnum) obj;
                obj = e.getValue();
            }

            String valAsStr;

            switch (valType) {
                case DATE_TIME:
                    valAsStr = new SimpleDateFormat(DATE_FORMAT).format((java.util.Date) obj);
                    break;
                case BOOL: {
                    if (((Boolean) obj).booleanValue()) {
                        valAsStr = "1";
                    } else {
                        valAsStr = "0";
                    }
                    break;
                }
                case CHAR:
                case INT:
                case NUM:
                case STR:
                case ARR_STR:
                case ARR_INT:
                case ARR_NUM:
                case ARR_DATE_TIME:
                case ARR_BOOL:
                case ARR_CHAR:
                case ARR_BIN:
                case JAVA_CLASS:
                case JAVA_TYPE:
                    valAsStr = String.valueOf(obj);
                    break;
                case BIN:
                    valAsStr = ((Bin) obj).getAsBase64();
                    break;
                case XML:
                    valAsStr = ((XmlObject) obj).xmlText(xmlSaveOptions);
                    break;
                case PARENT_REF:
                    valAsStr = ((Pid) obj).toStr();
                    break;
                default:
                    throw new RadixError("String value is not supported for value type \"" + valType.getName() + "\"");
            }

            return new ValAsStr(valAsStr);
        }

        public static ValAsStr newCopy(ValAsStr val) {
            if (val == null) {
                return null;
            }
            return loadFrom(val.toString());
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj instanceof ValAsStr) {
            return this.valAsStr.equals(((ValAsStr) obj).valAsStr);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.valAsStr.hashCode();
    }
    // SimpleDateFormat is not threadsafe
    //private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    private static final XmlOptions xmlSaveOptions;

    static {
        xmlSaveOptions = new XmlOptions();
        final HashMap<String, String> m = new HashMap<String, String>();
        m.put("http://schemas.xmlsoap.org/soap/envelope/", "SOAP-ENV");
        m.put("http://www.w3.org/2001/XMLSchema-instance", "xsi");
        m.put("http://schemas.radixware.org/eas.xsd", "eas");
        m.put("http://schemas.radixware.org/aas.xsd", "aas");
        m.put("http://schemas.radixware.org/dbudef.xsd", "dbu");
        m.put("http://schemas.radixware.org/types.xsd", "t");
        xmlSaveOptions.setSaveSuggestedPrefixes(m);
        xmlSaveOptions.setSaveNamespacesFirst();
        xmlSaveOptions.setUseDefaultNamespace();
    }

    /**
     * Restore value
     *
     * @throws WrongFormatError
     */
    public Object toObject(EValType valType) {
        if (valAsStr == null /*|| valAsStr.isEmpty() by BAO to preserve empty str */) {
            return null;
        }
        switch (valType) {
            case STR:
                return valAsStr;
            case INT:
                if (valAsStr.isEmpty()) //  by BAO to preserve empty str
                {
                    return null;
                }
                try {
                    return Long.decode(valAsStr);
                } catch (NumberFormatException e) {
                    throw new WrongFormatError("Wrong string Int format: " + valAsStr + ", \n" + e.getMessage(), e);
                }
            case NUM:
                if (valAsStr.isEmpty()) //  by BAO to preserve empty str
                {
                    return null;
                }
                try {
                    return new BigDecimal(valAsStr);
                } catch (NumberFormatException e) {
                    throw new WrongFormatError("Wrong string Num format: " + valAsStr + ", \n" + e.getMessage(), e);
                }
            case DATE_TIME:
                if (valAsStr.isEmpty()) //  by BAO to preserve empty str
                {
                    return null;
                }
                try {
                    return new Timestamp(new SimpleDateFormat(DATE_FORMAT).parse(valAsStr).getTime());
                } catch (ParseException e) {
                    throw new WrongFormatError("Wrong string DateTime format: " + valAsStr + ", \n" + e.getMessage(), e);
                }
            case BOOL:
                if (valAsStr.isEmpty()) //  by BAO to preserve empty str
                {
                    return null;
                } else if (valAsStr.equals("0")) {
                    return Boolean.FALSE;
                } else {
                    return Boolean.TRUE;
                }
            case CHAR:
                if (valAsStr.isEmpty()) //  by BAO to preserve empty str
                {
                    return null;
                }
                return new Character(valAsStr.charAt(0));
            case BIN:
                if (valAsStr.isEmpty()) //  by BAO to preserve empty str
                {
                    return null;
                }
                return new Bin(valAsStr);
            case ARR_STR:
                return ArrStr.fromValAsStr(valAsStr);
            case ARR_INT:
                return ArrInt.fromValAsStr(valAsStr);
            case ARR_NUM:
                return ArrNum.fromValAsStr(valAsStr);
            case ARR_DATE_TIME:
                return ArrDateTime.fromValAsStr(valAsStr);
            case ARR_BOOL:
                return ArrBool.fromValAsStr(valAsStr);
            case ARR_CHAR:
                return ArrChar.fromValAsStr(valAsStr);
            case ARR_BIN:
                return ArrBin.fromValAsStr(valAsStr);
            case XML:
                if (valAsStr.isEmpty()) //  by BAO to preserve empty str
                {
                    return null;
                }
                try {
                    return XmlObject.Factory.parse(valAsStr);
                } catch (XmlException e) {
                    throw new WrongFormatError("Wrong XML string format: " + valAsStr + ", \n" + ExceptionTextFormatter.getExceptionMess(e), e);
                }
            case PARENT_REF:
                if (valAsStr.isEmpty()) {
                    return null;
                }
                final int pos = valAsStr.lastIndexOf('\n');
                if (pos == -1 || pos >= valAsStr.length()) {
                    throw new WrongFormatError("Can't restore object PID from string", null);
                }
                return new Pid(Id.Factory.loadFrom(valAsStr.substring(0, pos)), valAsStr.substring(pos + 1));
            default:
                throw new RadixError("String value is not supported for value type \"" + valType.getName() + "\"");
        }
    }
}
