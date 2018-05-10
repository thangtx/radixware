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

import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlAnySimpleType;


import com.trolltech.qt.core.QRegExp;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QDialog;
import org.apache.xmlbeans.SchemaAnnotation;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.RadEnumPresentationDef;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.EditMaskConstSet;
import org.radixware.kernel.common.client.meta.mask.EditMaskDateTime;
import org.radixware.kernel.common.client.meta.mask.EditMaskInt;
import org.radixware.kernel.common.client.meta.mask.EditMaskList;
import org.radixware.kernel.common.client.meta.mask.EditMaskBool;
import org.radixware.kernel.common.client.meta.mask.EditMaskNum;
import org.radixware.kernel.common.client.meta.mask.EditMaskStr;
import org.radixware.kernel.common.enums.EDateTimeStyle;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.types.Arr;
import org.radixware.kernel.common.types.ArrBool;
import org.radixware.kernel.common.types.ArrDateTime;
import org.radixware.kernel.common.types.ArrInt;
import org.radixware.kernel.common.types.ArrNum;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.dialogs.ArrayEditorDialog;
import org.radixware.kernel.explorer.editors.ArrayEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValEditor;
import org.radixware.kernel.explorer.env.Application;


class XValueArray extends ValEditor<String> {

    private QDialog dialog = null;
    private EditMask m = null;
    
    private EValType array_type = null;
    private SchemaType[] members = null;
    private SchemaType currentMember = null;

    public XValueArray(IClientEnvironment environment, SchemaType type) {
        super(environment, null, new EditMaskStr(), false, true);        
        m = getEditMaskByType(type);
        array_type = getValType(m);
        QAction callDialog = new QAction(null, Application.translate("XmlEditor", "Array Dialog"), this);
        callDialog.triggered.connect(this, "callEditorDialog()");
        callDialog.setObjectName("edit");
        this.addButton("", callDialog);
    }

    public XValueArray(IClientEnvironment environment, SchemaType[] m) {
        super(environment, null, new EditMaskStr(), false, true);
        members = m;
        QAction callDialog = new QAction(null, Application.translate("XmlEditor", "Element Types"), this);
        callDialog.triggered.connect(this, "callUnionDialog()");
        callDialog.setObjectName("edit");
        this.addButton("", callDialog);
    }

    private EditMask getEditMaskByType(SchemaType type) {
        EditMask mask = new EditMaskStr();
        if (type != null) {
            if (type.isBuiltinType()) {
                mask = specifyMaskType(mask, type);
            } else {
                int sv = type.getSimpleVariety();     
                if (sv == SchemaType.ATOMIC) {
                    //for listed constdefs
                    SchemaAnnotation sa = type.getAnnotation();
                    if (sa != null) {
                        Id constID = XEditorBuilder.getConstSetID(sa);
                        try {
                            //editor
                            if (constID != null) {
                                RadEnumPresentationDef constDef = getEnvironment().getApplication().getDefManager().getEnumPresentationDef(constID);
                                if (constDef != null) {
                                    mask = new EditMaskConstSet(constDef.getId(), null, null, null);
                                } else {
                                    mask = getUsualAtomicMask(type);
                                }
                            } else {
                                mask = getUsualAtomicMask(type);
                            }
                        } catch (DefinitionError defError) {
                            mask = getUsualAtomicMask(type);
                            final String mess = Application.translate("XmlEditor", "Cannot get enumeration #%s");
                            getEnvironment().getTracer().error(String.format(mess, constID.toString()), defError);
                        }
                    } else {
                        mask = getUsualAtomicMask(type);
                    }
                }
                //LIST...
                if (sv == SchemaType.LIST) {
                    SchemaType listType = type.getListItemType();
                    mask = getEditMaskByType(listType);
                }
                //UNION...
                if (sv == SchemaType.UNION) {
                    SchemaType unionType = type.getUnionCommonBaseType();
                    mask = getEditMaskByType(unionType);
                }
            }
        }
        return mask;
    }

    private EditMask getUsualAtomicMask(SchemaType type) {
        EditMask mask = new EditMaskStr();
        SchemaType base = type.getBaseType();
        XmlAnySimpleType[] enums = type.getEnumerationValues();
        if (enums != null) {
            EditMaskList l_mask = new EditMaskList();
            for (int i = 0; i <= enums.length - 1; i++) {
                l_mask.addItem(enums[i].getStringValue(), enums[i].getStringValue());
            }
            mask = l_mask;
        } else {
            mask = specifyMaskType(mask, base);
        }
        return mask;
    }

    private EditMask specifyMaskType(EditMask mask, SchemaType type) {
        int code = type.getBuiltinTypeCode();
        switch (code) {
            case SchemaType.BTC_BOOLEAN: {
                mask = new EditMaskBool();
            }
            break;
            case SchemaType.BTC_DATE:
            case SchemaType.BTC_DATE_TIME:
            case SchemaType.BTC_TIME:
            case SchemaType.BTC_G_DAY:
            case SchemaType.BTC_G_MONTH:
            case SchemaType.BTC_G_MONTH_DAY:
            case SchemaType.BTC_G_YEAR:
            case SchemaType.BTC_G_YEAR_MONTH: {
                final Timestamp max = XEditorTools.getMaxDateTime(type);
                final Timestamp min = XEditorTools.getMinDateTime(type);
                final String display = XEditorTools.getDateTimePattern(type);
                if (display==null || display.isEmpty()){
                    mask = new EditMaskDateTime(EDateTimeStyle.DEFAULT, EDateTimeStyle.DEFAULT, min, max);
                }else{
                    mask = new EditMaskDateTime(display, min, max);
                }
            }
            break;
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
                long max = XEditorTools.getMaxIntValue(type);
                long min = XEditorTools.getMinIntValue(type);
                byte minLength = XEditorTools.getMinLength(type);
                long step = 1L;
                byte numBase = 10;
                Character pad = null;
                Character del = null;
                mask = new EditMaskInt(min, max, minLength, pad, step, del, numBase);
            }
            break;
            case SchemaType.BTC_DECIMAL:
            case SchemaType.BTC_DOUBLE:
            case SchemaType.BTC_FLOAT: {
                double max = XEditorTools.getMaxNumValue(type);
                double min = XEditorTools.getMinNumValue(type);
                byte precision = XEditorTools.getFractionDigits(type);
                long scale = 1;
                Character del = null;
                mask = new EditMaskNum(BigDecimal.valueOf(min), BigDecimal.valueOf(max), scale, del, precision);
            }
            break;
        }
        return mask;
    }

    private EValType getValType(EditMask mask) {
        if (mask instanceof EditMaskNum) {
            return EValType.ARR_NUM;
        }
        if (mask instanceof EditMaskInt) {
            return EValType.ARR_INT;
        }
        if (mask instanceof EditMaskDateTime) {
            return EValType.ARR_DATE_TIME;
        }
        if (mask instanceof EditMaskBool) {
            return EValType.ARR_BOOL;
        }

        return EValType.ARR_STR;
    }

    @SuppressWarnings("unused")
    private void callEditorDialog() {
        String v = getValue();
        if (v==null){
            makeNewDialog(m, null);
        } else if (v.isEmpty()){
            makeNewDialog(m, ArrayEditor.createEmptyArr(array_type));
        } else {
            try {
                Arr val = stringToArr(v);
                makeNewDialog(m, val);                
            } catch (IllegalArgumentException excep) {
                String text = Application.translate("XmlEditor", "One or more value of the list do not match to list's type. Continue to make new list?");
                if (Application.messageConfirmation(Application.translate("MessageBox", "Warning"), text)) {
                    makeNewDialog(m, ArrayEditor.createEmptyArr(array_type));                    
                }else{
                    return;
                }
            }
        }
        dialog.exec();
    }

    @SuppressWarnings("unused")
    private void callUnionDialog() {
        String v = getValue();
        SchemaType t = checkValueForUnion(v);
        if (t != null) {
            if (currentMember == null) {
                currentMember = t;
            }
            makeNewUnionDialog(v);
            dialog.exec();
        } else {
            String text = Application.translate("XmlEditor", "The value do not match to any type of the union. Continue to enter new value?");
            if (Application.messageConfirmation(Application.translate("MessageBox", "Warning"), text)) {
                makeNewUnionDialog(v);
                dialog.exec();
            }
        }
    }

    private SchemaType checkValueForUnion(String v) {
        for (SchemaType t : members) {
            if (checkMember(t, v)) {
                return t;
            }
        }
        return null;
    }

    private boolean checkMember(SchemaType t, String v) {
        if (t.isBuiltinType()) {
            return checkBuiltInType(t, v);
        } else {
            int sv = t.getSimpleVariety();
            if (sv == SchemaType.ATOMIC) {
                SchemaType base = t.getBaseType();
                XmlAnySimpleType[] enums = t.getEnumerationValues();
                if (enums != null
                        && enums.length != 0) {
                    for (XmlAnySimpleType x : enums) {
                        if (x.getStringValue().equals(v)) {
                            return true;
                        }
                    }
                } else {
                    return checkBuiltInType(base, v);
                }
            }
            if (sv == SchemaType.LIST) {
                SchemaType list = t.getListItemType();
                return checkMember(list, v);
            }
            if (sv == SchemaType.UNION) {
                if (t.getUnionMemberTypes() != null
                        && t.getUnionMemberTypes().length != 0) {
                    for (SchemaType u : t.getUnionMemberTypes()) {
                        if (checkMember(u, v)) {
                            return true;
                        }
                    }
                } else {
                    SchemaType u = t.getUnionCommonBaseType();
                    return checkMember(u, v);
                }
            }
        }
        return false;
    }

    private boolean checkBuiltInType(final SchemaType t, String v) {
        final int code = t.getBuiltinTypeCode();
        switch (code) {
            case SchemaType.BTC_BOOLEAN: {
                try {
                    Boolean.valueOf(v);
                    return true;
                } catch (IllegalArgumentException excep) {
                    return false;
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
                try {
                    if (v.contains("+")) {
                        v = v.substring(0, v.indexOf("+"));
                    }
                    if (v.contains("T")) {
                        v = v.substring(0, v.indexOf("T")) + " " + v.substring(v.indexOf("T") + 1, v.length());
                    }
                    return true;
                } catch (IndexOutOfBoundsException excep) {
                    return false;
                }
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
                try {
                    if (!v.isEmpty()) {
                        Long.valueOf(v);
                    }
                    return true;
                } catch (IllegalArgumentException excep) {
                    return false;
                }
            }
            case SchemaType.BTC_DECIMAL:
            case SchemaType.BTC_DOUBLE:
            case SchemaType.BTC_FLOAT: {
                try {
                    if (!v.isEmpty()) {
                        new BigDecimal(v);
                    }
                    return true;
                } catch (NumberFormatException excep) {
                    return false;
                }
            }
            default: {
                return true;
            }
        }
    }

    @SuppressWarnings("unused")
    private void onOkPressed() {
        ArrayEditorDialog arrDialog = (ArrayEditorDialog) dialog;
        Arr currentValue = arrDialog.getCurrentValue();
        String forEdit = arrToString(currentValue);
        this.setValue(forEdit);
    }

    @SuppressWarnings("unused")
    private void onUnionOkPressed() {
        XUnionDialog uDialog = (XUnionDialog) dialog;
        String currentValue = uDialog.getCurrentValue();
        currentMember = uDialog.getCurrentMember();
        this.setValue(currentValue);
    }

    private String arrToString(final Arr array) {
        if (array==null){
            return null;
        }
        final StringBuilder resultBuilder = new StringBuilder();
        for (int i = 0; i <= array.size() - 1; i++) {
            if (array.get(i) != null) {
                resultBuilder.append(array.get(i).toString());
                resultBuilder.append(' ');
            }
        }
        if (resultBuilder.length() == 0){
            return "";
        }
        else{
            resultBuilder.deleteCharAt(resultBuilder.length()-1);
            return resultBuilder.toString();
        }
    }

    private Arr stringToArr(String s) {
        if (!s.isEmpty()) {
            final QRegExp expression = new QRegExp();
            Arr res = ArrayEditor.createEmptyArr(array_type);
            if (array_type == EValType.ARR_DATE_TIME) {
                expression.setPattern("[0-9]{4,4}-[01][0-9]-[0-3][0-9] [0-2][0-9]:[0-5][0-9]:[0-5][0-9][.][0-5][0-9]?");
                s = separateItem(res, s, expression);
                while (s.length() > 0) {
                    s = separateItem(res, s, expression);
                }
            } else {
                expression.setPattern("[^ ]+[ ]+");
                s = separateItem(res, s, expression);
                while (s.length() > 0) {
                    s = separateItem(res, s, expression);
                }
            }
            return res;
        }
        return new ArrStr();
    }

    /*Note for array values containing empty (null?) items, replaced by whitespace characters:

    http://www.w3.org/TR/2006/REC-xml11-20060816/#AVNormalize
    qoute: "...If the attribute type is not CDATA, then the XML processor
    MUST further process the normalized attribute value by discarding any
    leading and trailing space (#x20) characters, and by replacing
    sequences of space (#x20) characters by a single space (#x20) character..."*/
    private String separateItem(Arr res, String s, QRegExp expression) {
        if (!s.contains(" ") && array_type != EValType.ARR_DATE_TIME) {
            expression.setPattern("[^ ]+ ?");
        }
        int index = expression.indexIn(s);
        if (index<0){
            return "";
        }
        int l = expression.matchedLength();
        String item = s.substring(index, index + l);
        s = l == s.length() ? "" : s.substring(index + l, s.length());
        if (item.contains(" ") && array_type != EValType.ARR_DATE_TIME) {
            int space = item.indexOf(" ");
            item = item.substring(0, space);
        }
        addItemToArr(res, item);
        return s;
    }

    private void addItemToArr(Arr a, String i) {
        switch (array_type) {
            case ARR_STR:
                if (a instanceof ArrStr) {
                    ((ArrStr) a).add(i);
                } else {
                    throw new IllegalArgumentException("array of strings expected, but \"" + a.getClass().getName() + "\" got");
                }
                break;
            case ARR_BOOL:
                if (a instanceof ArrBool) {
                    ((ArrBool) a).add(Boolean.valueOf(i));
                } else {
                    throw new IllegalArgumentException("array of booleans expected, but \"" + a.getClass().getName() + "\" got");
                }
                break;
            case ARR_NUM:
                Double db = Double.valueOf(i);
                if (a instanceof ArrNum) {
                    ((ArrNum) a).add(BigDecimal.valueOf(db));
                } else {
                    throw new IllegalArgumentException("array of decimals expected, but \"" + a.getClass().getName() + "\" got");
                }
                break;
            case ARR_INT:
                if (a instanceof ArrInt) {
                    ((ArrInt) a).add(Long.valueOf(i));
                } else {
                    throw new IllegalArgumentException("array of integers expected, but \"" + a.getClass().getName() + "\" got");
                }
                break;
            case ARR_DATE_TIME:
                if (a instanceof ArrDateTime) {
                    ((ArrDateTime) a).add(Timestamp.valueOf(i));
                } else {
                    throw new IllegalArgumentException("array of timestamp expected, but \"" + a.getClass().getName() + "\" got");
                }
                break;
            default:
                throw new IllegalArgumentException("type " + array_type.name() + " is not supported");
        }
    }

    private void makeNewDialog(EditMask mask, Arr val) {
        final ArrayEditorDialog arrayEditor = new ArrayEditorDialog(getEnvironment(), array_type, null, isReadOnly(), this);
        arrayEditor.setCurrentValue(val);
        arrayEditor.setEditMask(mask);
        dialog = arrayEditor;
        final String dialogTitle = getDialogTitle();
        if (dialogTitle != null) {
            dialog.setWindowTitle(dialogTitle);
        }
        dialog.accepted.connect(this, "onOkPressed()");
    }

    private void makeNewUnionDialog(final String v) {
        dialog = new XUnionDialog(getEnvironment(), members, currentMember, v, this, isReadOnly());
        final String dialogTitle = getDialogTitle();
        if (dialogTitle != null) {
            dialog.setWindowTitle(dialogTitle);
        }
        dialog.accepted.connect(this, "onUnionOkPressed()");
    }
}
