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

package org.radixware.kernel.explorer.editors.xml;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlAnySimpleType;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.RadEnumPresentationDef;
import org.radixware.kernel.common.client.meta.mask.EditMaskConstSet;
import org.radixware.kernel.common.client.meta.mask.EditMaskDateTime;
import org.radixware.kernel.common.client.meta.mask.EditMaskInt;
import org.radixware.kernel.common.client.meta.mask.EditMaskList;
import org.radixware.kernel.common.client.meta.mask.EditMaskNone;
import org.radixware.kernel.common.client.meta.mask.EditMaskNum;
import org.radixware.kernel.common.client.meta.mask.EditMaskStr;
import org.radixware.kernel.common.enums.EDateTimeStyle;

import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.types.Bin;

import org.radixware.kernel.explorer.editors.valeditors.*;
import org.radixware.kernel.explorer.env.Application;


import org.radixware.kernel.explorer.utils.ValueConverter;

public class XEditorTools {

    static public final int STR_EDITOR = 0;
    static public final int INT_EDITOR = 1;
    static public final int NUM_EDITOR = 2;
    static public final int BOOL_EDITOR = 3;
    static public final int DATE_EDITOR = 4;
    static public final int BIN_EDITOR = 5;
    static public final int FILE_PATH_EDITOR = 6;
    static public final String ANY_SIMPLE = "anySimpleType";
    static public final byte NUM_PRECISION = -1;

    static public void setProperValueToEditor(ValEditor e, String v) {
        if (e instanceof ValConstSetEditor) {
            //e.setValue(v); commented by yremizov
            //changed by yremizov
            if (v != null) {
                final EditMaskConstSet mask = (EditMaskConstSet) ((ValConstSetEditor) e).getEditMask();
                final RadEnumPresentationDef constSet = mask.getRadEnumPresentationDef(e.getEnvironment().getApplication());
                switch (constSet.getItemType()) {
                    case INT:
                        try {
                            ((ValConstSetEditor) e).setValue(Long.parseLong(v));
                        } catch (NumberFormatException exception) {
                            final String info = e.getEnvironment().getMessageProvider().translate("XmlEditor", "Cannot parse long value from string \"%s\"  for enumeration %s");
                            e.getEnvironment().getTracer().put(EEventSeverity.DEBUG, String.format(info, v, constSet.toString()), EEventSource.EXPLORER);
                        }
                        break;
                    case CHAR:
                        if (!v.isEmpty()) {
                            ((ValConstSetEditor) e).setValue(Character.valueOf(v.charAt(0)));
                        }
                        break;
                    default:
                        ((ValConstSetEditor) e).setValue(v);
                }
            }
            //end of chenged by yremizov
        }

        if (e instanceof XValueArray) {
            ((XValueArray) e).setValue(v);
        }
        if (e instanceof ValStrEditor) {
            ((ValStrEditor) e).setValue(v);
        }
        if (e instanceof ValFilePathEditor) {
            ((ValFilePathEditor) e).setValue(v);
        }

        if (e instanceof ValBinEditor) {
            Bin v_bin = ValueConverter.hexadecimalString2Bin(v, "");
            ((ValBinEditor) e).setValue(v_bin);
        }
        if (e instanceof ValListEditor) {
            EditMaskList list = (EditMaskList) e.getEditMask();
            if (v == null || v.isEmpty()) {
                ((ValListEditor) e).setValue(v);
            } else {
                checkListValue(list, v);
                ((ValListEditor) e).setValue(v);
            }
        } else if (v != null && !v.isEmpty()) {
            try {
                if (e instanceof ValIntEditor) {
                    ((ValIntEditor) e).setValue(Long.valueOf(v));
                }
                if (e instanceof ValNumEditor) {
                    ((ValNumEditor) e).setValue(new BigDecimal(v));
                }
                if (e instanceof ValDateTimeEditor) {
                    if (v.contains("+")) {
                        v = v.substring(0, v.indexOf("+"));
                    }
                    if (v.contains("T")) {
                        v = v.substring(0, v.indexOf("T")) + " " + v.substring(v.indexOf("T") + 1, v.length());
                    }
                    ((ValDateTimeEditor) e).setValue(Timestamp.valueOf(v));
                }
                if (e instanceof ValBoolEditor) {
                    if (v.equals("true")
                            || v.equals("false")) {
                        boolean bv = Boolean.valueOf(v);
                        ((ValBoolEditor) e).setValue(bv);
                    }
                }
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(XEditorTools.class.getName()).log(Level.FINE, ex.getMessage(), ex);
            }
        }
    }

    public static void checkListValue(EditMaskList list, String v) {
        int i = 0;
        boolean stop = false;
        while (i <= list.getItems().size() - 1
                && !stop) {
            if (list.getValueByIdx(i).toString().equals(v)) {
                stop = true;
            } else {
                i++;
            }
        }
        if (!stop) {
            String text = "Item out of list";
            throw new IllegalArgumentException(Application.translate("XmlEditor", text));
        }
    }

    static public ValEditor getRelevantEditor(IClientEnvironment environment, SchemaType t) {
        ValEditor edit = null;
        if (t != null) {
            if (t.isBuiltinType()) {
                edit = specifyEditorType(environment, edit, t);
            } else {
                int sv = t.getSimpleVariety();
                if (sv == SchemaType.ATOMIC) {
                    SchemaType base = t.getBaseType();
                    XmlAnySimpleType[] enums = t.getEnumerationValues();
                    if (enums != null) {
                        EditMaskList mask = new EditMaskList();
                        for (int i = 0; i <= enums.length - 1; i++) {
                            mask.addItem(enums[i].getStringValue(), enums[i].getStringValue());
                        }
                        edit = new ValListEditor(environment, null, mask, false, false);
                    } else {
                        edit = specifyEditorType(environment, edit, base);
                    }
                }
                //LIST...
                if (sv == SchemaType.LIST) {
                    SchemaType listType = t.getListItemType();
                    edit = new XValueArray(environment, listType);
                }
                //UNION...
                if (sv == SchemaType.UNION) {
                    SchemaType[] members = t.getUnionMemberTypes();
                    if (members != null
                            && members.length != 0) {
                        edit = new XValueArray(environment, members);
                    } else {
                        SchemaType unionType = t.getUnionCommonBaseType();
                        if (unionType != null) {
                            edit = new XValueArray(environment, unionType);
                        }
                    }
                }
            }
        }
        return edit == null ? new ValStrEditor(environment, null, new EditMaskStr(), false, false) : edit;
    }

    static private ValEditor specifyEditorType(IClientEnvironment environment, ValEditor edit, SchemaType t) {
        int code = t.getBuiltinTypeCode();
        if (code == SchemaType.BTC_NOT_BUILTIN) {
            SchemaType base = t.getBaseType();
            if (base != null) {
                code = base.getBuiltinTypeCode();
                while (code == SchemaType.BTC_NOT_BUILTIN
                        && !base.getName().getLocalPart().equals(ANY_SIMPLE)) {
                    base = base.getBaseType();
                    code = base.getBuiltinTypeCode();
                }
            }
        }

        int editortype = getEditorType(code);
        if (editortype == INT_EDITOR) {
            long max = getMaxIntValue(t);
            long min = getMinIntValue(t);
            byte minLength = getMinLength(t);
            long step = 1L;
            byte numBase = 10;
            EditMaskInt iMask = new EditMaskInt(min, max, minLength, null, step, null, numBase);
            edit = new ValIntEditor(environment, null, iMask, false, false);
        }
        if (editortype == NUM_EDITOR) {
            double max = getMaxNumValue(t);
            double min = getMinNumValue(t);
            byte precision = getFractionDigits(t);
            long scale = 1;
            EditMaskNum nMask = new EditMaskNum(BigDecimal.valueOf(min), BigDecimal.valueOf(max), scale, null, precision);
            edit = new ValNumEditor(environment, null, nMask, false, false);
        }
        if (editortype == BOOL_EDITOR) {
            edit = new ValBoolEditor(environment, null, new EditMaskNone(), false, false);
        }
        if (editortype == DATE_EDITOR) {
            final Timestamp max = getMaxDateTime(t);
            final Timestamp min = getMinDateTime(t);
            final String format = getDateTimePattern(t);
            final EditMaskDateTime dtMask;
            if (format==null || format.isEmpty()){
                dtMask = new EditMaskDateTime(EDateTimeStyle.DEFAULT, EDateTimeStyle.DEFAULT, min, max);
            }else{
                dtMask = new EditMaskDateTime(format, min, max);
            }
            edit = new ValDateTimeEditor(environment, null, dtMask, false, false);
        }
        if (editortype == BIN_EDITOR) {
            edit = new XValBinEditor(environment, null, new EditMaskNone(), false, false);
        }
        return edit;
    }

    static public int getBuiltInTypeCode(SchemaType t) {
        if (t != null) {
            if (t.isBuiltinType()) {
                return t.getBuiltinTypeCode();
            } else {
                int sv = t.getSimpleVariety();
                if (sv == SchemaType.ATOMIC) {
                    SchemaType base = t.getBaseType();
                    return base.getBuiltinTypeCode();
                }
                if (sv == SchemaType.LIST) {
                    SchemaType listType = t.getListItemType();
                    if (listType != null) {
                        return getBuiltInTypeCode(listType);
                    }
                }
//                if (sv == SchemaType.UNION) {
//                }
            }
        }
        return SchemaType.BTC_ANY_SIMPLE;
    }

    static public long getMaxIntValue(SchemaType t) {
        if (t.getFacet(SchemaType.FACET_MAX_INCLUSIVE) != null) {
            XmlAnySimpleType res = t.getFacet(SchemaType.FACET_MAX_INCLUSIVE);
            Long iRes = Long.valueOf(res.getStringValue());
            return iRes;
        }
        if (t.getFacet(SchemaType.FACET_MAX_EXCLUSIVE) != null) {
            XmlAnySimpleType res = t.getFacet(SchemaType.FACET_MAX_EXCLUSIVE);
            Long iRes = Long.valueOf(res.getStringValue());
            return iRes;
        }
        return Long.MAX_VALUE;
    }

    static public Long getMinIntValue(SchemaType t) {
        if (t.getFacet(SchemaType.FACET_MIN_INCLUSIVE) != null) {
            XmlAnySimpleType res = t.getFacet(SchemaType.FACET_MIN_INCLUSIVE);
            Long iRes = Long.valueOf(res.getStringValue());
            return iRes;
        }
        if (t.getFacet(SchemaType.FACET_MIN_EXCLUSIVE) != null) {
            XmlAnySimpleType res = t.getFacet(SchemaType.FACET_MIN_EXCLUSIVE);
            Long iRes = Long.valueOf(res.getStringValue());
            return iRes;
        }
        return Long.MIN_VALUE;
    }

    static public double getMaxNumValue(SchemaType t) {
        if (t.getFacet(SchemaType.FACET_MAX_INCLUSIVE) != null) {
            XmlAnySimpleType res = t.getFacet(SchemaType.FACET_MAX_INCLUSIVE);
            Double dRes = Double.valueOf(res.getStringValue());
            return dRes;
        }
        if (t.getFacet(SchemaType.FACET_MAX_EXCLUSIVE) != null) {
            XmlAnySimpleType res = t.getFacet(SchemaType.FACET_MAX_EXCLUSIVE);
            Double dRes = Double.valueOf(res.getStringValue());
            return dRes;
        }
        return Double.MAX_VALUE;
    }

    static public byte getFractionDigits(SchemaType t) {
        if (t.getFacet(SchemaType.FACET_FRACTION_DIGITS) != null) {
            XmlAnySimpleType fd = t.getFacet(SchemaType.FACET_FRACTION_DIGITS);
            Byte inByte = Byte.decode(fd.getStringValue());
            return inByte;
        }
        return NUM_PRECISION;
    }

    static public double getMinNumValue(SchemaType t) {
        if (t.getFacet(SchemaType.FACET_MIN_INCLUSIVE) != null) {
            XmlAnySimpleType res = t.getFacet(SchemaType.FACET_MIN_INCLUSIVE);
            Double dRes = Double.valueOf(res.getStringValue());
            return dRes;
        }
        if (t.getFacet(SchemaType.FACET_MIN_EXCLUSIVE) != null) {
            XmlAnySimpleType res = t.getFacet(SchemaType.FACET_MIN_EXCLUSIVE);
            Double dRes = Double.valueOf(res.getStringValue());
            return dRes;
        }
        return -Double.MAX_VALUE;
    }

    static public Timestamp getMaxDateTime(SchemaType t) {
        if (t.getFacet(SchemaType.FACET_MAX_INCLUSIVE) != null) {
            XmlAnySimpleType res = t.getFacet(SchemaType.FACET_MAX_INCLUSIVE);
            Timestamp tRes = Timestamp.valueOf(res.getStringValue());
            return tRes;
        }
        if (t.getFacet(SchemaType.FACET_MAX_EXCLUSIVE) != null) {
            XmlAnySimpleType res = t.getFacet(SchemaType.FACET_MAX_EXCLUSIVE);
            Timestamp tRes = Timestamp.valueOf(res.getStringValue());
            return tRes;
        }
        return null;
    }

    static public Timestamp getMinDateTime(SchemaType t) {
        if (t.getFacet(SchemaType.FACET_MIN_INCLUSIVE) != null) {
            XmlAnySimpleType res = t.getFacet(SchemaType.FACET_MIN_INCLUSIVE);
            Timestamp tRes = Timestamp.valueOf(res.getStringValue());
            return tRes;
        }
        if (t.getFacet(SchemaType.FACET_MIN_EXCLUSIVE) != null) {
            XmlAnySimpleType res = t.getFacet(SchemaType.FACET_MIN_EXCLUSIVE);
            Timestamp tRes = Timestamp.valueOf(res.getStringValue());
            return tRes;
        }
        return null;
    }

    static public String getDateTimePattern(final SchemaType t) {
        if (t.getFacet(SchemaType.FACET_PATTERN) != null) {
            XmlAnySimpleType res = t.getFacet(SchemaType.FACET_PATTERN);
            return res.getStringValue();
        }else{
            return null;
        }
    }

    static public byte getMinLength(SchemaType t) {
        if (t.getFacet(SchemaType.FACET_MIN_LENGTH) != null) {
            XmlAnySimpleType res = t.getFacet(SchemaType.FACET_MIN_LENGTH);
            byte iRes = Byte.decode(res.getStringValue());
            return iRes;
        }
        if (t.getFacet(SchemaType.FACET_LENGTH) != null) {
            XmlAnySimpleType res = t.getFacet(SchemaType.FACET_LENGTH);
            byte iRes = Byte.decode(res.getStringValue());
            return iRes;
        }
        //changed by yremizov
        //old code:
        //return Long.SIZE;
        //new code:
        return 0;
    }

    static public int getEditorType(int builtin) {
        switch (builtin) {
            case SchemaType.BTC_BOOLEAN: {
                return BOOL_EDITOR;
            }
            case SchemaType.BTC_DATE:
            case SchemaType.BTC_DATE_TIME:
            case SchemaType.BTC_TIME:
            case SchemaType.BTC_G_DAY:
            case SchemaType.BTC_G_MONTH:
            case SchemaType.BTC_G_MONTH_DAY:
            case SchemaType.BTC_G_YEAR:
            case SchemaType.BTC_G_YEAR_MONTH: {
                return DATE_EDITOR;
            }
            case SchemaType.BTC_BYTE:
            case SchemaType.BTC_INT:
            case SchemaType.BTC_INTEGER:
            case SchemaType.BTC_LONG:
            case SchemaType.BTC_NEGATIVE_INTEGER:
            case SchemaType.BTC_NON_NEGATIVE_INTEGER:
            case SchemaType.BTC_NON_POSITIVE_INTEGER:
            case SchemaType.BTC_POSITIVE_INTEGER:
            case SchemaType.BTC_SHORT:
            case SchemaType.BTC_UNSIGNED_BYTE:
            case SchemaType.BTC_UNSIGNED_INT:
            case SchemaType.BTC_UNSIGNED_LONG:
            case SchemaType.BTC_UNSIGNED_SHORT: {
                return INT_EDITOR;
            }
            case SchemaType.BTC_DECIMAL:
            case SchemaType.BTC_DOUBLE:
            case SchemaType.BTC_FLOAT: {
                return NUM_EDITOR;
            }
            //case SchemaType.BTC_BASE_64_BINARY:
            case SchemaType.BTC_HEX_BINARY: {
                return BIN_EDITOR;
            }
        }
        return STR_EDITOR;
    }

    public static String getInitialValue(SchemaType def) {
        int typecode = getBuiltInTypeCode(def);
        switch (typecode) {
            case SchemaType.BTC_BOOLEAN: {
                return "false";
            }
            case SchemaType.BTC_BYTE:
            case SchemaType.BTC_INT:
            case SchemaType.BTC_INTEGER:
            case SchemaType.BTC_LONG:
            case SchemaType.BTC_NEGATIVE_INTEGER:
            case SchemaType.BTC_NON_NEGATIVE_INTEGER:
            case SchemaType.BTC_NON_POSITIVE_INTEGER:
            case SchemaType.BTC_POSITIVE_INTEGER:
            case SchemaType.BTC_SHORT:
            case SchemaType.BTC_UNSIGNED_BYTE:
            case SchemaType.BTC_UNSIGNED_INT:
            case SchemaType.BTC_UNSIGNED_LONG:
            case SchemaType.BTC_UNSIGNED_SHORT: {
                Long min = getMinIntValue(def);
                if (min >= 0) {
                    return min.toString();
                } else {
                    return "0";
                }
            }
            case SchemaType.BTC_DECIMAL:
            case SchemaType.BTC_DOUBLE:
            case SchemaType.BTC_FLOAT: {
                Double min = getMinNumValue(def);
                if (min >= 0) {
                    return min.toString();
                } else {
                    return "0";
                }
            }
            case SchemaType.BTC_DATE:
            case SchemaType.BTC_DATE_TIME:
            case SchemaType.BTC_TIME:
            case SchemaType.BTC_G_DAY:
            case SchemaType.BTC_G_MONTH:
            case SchemaType.BTC_G_MONTH_DAY:
            case SchemaType.BTC_G_YEAR:
            case SchemaType.BTC_G_YEAR_MONTH: {
                Timestamp min = getMinDateTime(def);
                if (min != null) {
                    return min.toString();
                }
            }
        }
        return "";
    }
}
