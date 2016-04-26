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
package org.radixware.kernel.server.arte.services.eas;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.radixware.kernel.common.enums.EReferencedObjectActions;

import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.types.Arr;
import org.radixware.kernel.common.types.ArrBin;
import org.radixware.kernel.common.types.ArrBool;
import org.radixware.kernel.common.types.ArrChar;
import org.radixware.kernel.common.types.ArrDateTime;
import org.radixware.kernel.common.types.ArrInt;
import org.radixware.kernel.common.types.ArrNum;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.types.Bin;
import org.radixware.kernel.common.types.IKernelCharEnum;
import org.radixware.kernel.common.types.IKernelIntEnum;
import org.radixware.kernel.common.types.IKernelStrEnum;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.exceptions.EntityObjectNotExistsError;
import org.radixware.kernel.server.types.ArrEntity;
import org.radixware.kernel.server.types.Entity;
import org.radixware.kernel.server.types.Pid;
import org.radixware.schemas.eas.Property;
import org.radixware.schemas.eas.Property.Ref;
import org.w3c.dom.Node;

public final class EasValueConverter {

    public static final Set<EValType> SUPPORTED_VAL_TYPES;

    static {
        SUPPORTED_VAL_TYPES = new HashSet<EValType>();
        SUPPORTED_VAL_TYPES.add(EValType.STR);
        SUPPORTED_VAL_TYPES.add(EValType.CHAR);
        SUPPORTED_VAL_TYPES.add(EValType.OBJECT);
        SUPPORTED_VAL_TYPES.add(EValType.PARENT_REF);
        SUPPORTED_VAL_TYPES.add(EValType.INT);
        SUPPORTED_VAL_TYPES.add(EValType.NUM);
        SUPPORTED_VAL_TYPES.add(EValType.CLOB);
        SUPPORTED_VAL_TYPES.add(EValType.BLOB);
        SUPPORTED_VAL_TYPES.add(EValType.DATE_TIME);
        SUPPORTED_VAL_TYPES.add(EValType.BIN);
        SUPPORTED_VAL_TYPES.add(EValType.BOOL);
        SUPPORTED_VAL_TYPES.add(EValType.XML);
        SUPPORTED_VAL_TYPES.add(EValType.ARR_STR);
        SUPPORTED_VAL_TYPES.add(EValType.ARR_CHAR);
        SUPPORTED_VAL_TYPES.add(EValType.ARR_REF);
        SUPPORTED_VAL_TYPES.add(EValType.ARR_INT);
        SUPPORTED_VAL_TYPES.add(EValType.ARR_NUM);
        SUPPORTED_VAL_TYPES.add(EValType.ARR_DATE_TIME);
        SUPPORTED_VAL_TYPES.add(EValType.ARR_BOOL);
        SUPPORTED_VAL_TYPES.add(EValType.ARR_BIN);
    }

    private EasValueConverter() {
    }

    public interface IParentInfoProducer {

        ParentInfo getParentInfo(Entity value);
    }

//	Converters
    public final static void objVal2EasPropXmlVal(final Object val, final ParentInfo ptValInfo, final EValType valType, final org.radixware.schemas.eas.Property xmlProp) {
        objVal2EasPropXmlVal(val, ptValInfo, valType, xmlProp, null);
    }

    public final static void objVal2EasPropXmlVal(final Object val, final ParentInfo ptValInfo, final EValType valType, final org.radixware.schemas.eas.Property xmlProp, IParentInfoProducer parentInfoProducer) {
        // отчасти аналогична objVal2AasXmlVal - синхронизировать
        try {
            switch (valType) {
                case STR:
                case CHAR: {
                    if (val != null) {
                        if (val instanceof IKernelStrEnum) {
                            xmlProp.setStr(((IKernelStrEnum) val).getValue());
                        } else if (val instanceof IKernelCharEnum) {
                            xmlProp.setStr(String.valueOf(((IKernelCharEnum) val).getValue()));
                        } else {
                            xmlProp.setStr(val.toString());
                        }
                    } else {
                        xmlProp.setStr(null);
                    }
                }
                break;
                case OBJECT:
                case PARENT_REF: {
                    if (ptValInfo == null) { //used by report BeforeInputParams command
                        xmlProp.unsetRef();
                        if (val == null) {
                            xmlProp.setNilRef();
                        } else {
                            final Ref refXml = Ref.Factory.newInstance();
                            try {
                                Entity ent = (Entity) val;
                                refXml.setTitle(ent.calcTitle());
                                refXml.setPID(ent.getPid().toString());
                            } catch (EntityObjectNotExistsError e) {
                                refXml.setBrokenRef(e.getBrokenRefPres());
                            }
                            xmlProp.setRef(refXml);
                        }
                    } else if (ptValInfo.isBrokenRef()) {//RADIX-3199
                        final Ref refXml = xmlProp.addNewRef();
                        refXml.setBrokenRef(ptValInfo.brokenRefErr.getBrokenRefPres());
                        refXml.setAllowedActionsBitMask(0);
                    } else if (val != null || valType == EValType.PARENT_REF) {
                        final Property.Ref refXml = xmlProp.addNewRef();
                        if (val != null) {
                            refXml.setPID(val instanceof Entity ? ((Entity) val).getPid().toString() : val.toString());
                            final EnumSet<EReferencedObjectActions> allowedActions = ptValInfo.allowedActions;
                            if (!allowedActions.containsAll(EnumSet.allOf(EReferencedObjectActions.class))) {
                                refXml.setAllowedActionsBitMask(EReferencedObjectActions.toBitMask(allowedActions));
                            }
                        }
                        refXml.setTitle(ptValInfo.title);
                    } else {
                        xmlProp.setNilRef();
                    }
                    if (valType == EValType.PARENT_REF && ptValInfo != null
                            && !ptValInfo.isSelectable) {//RADIX-4882
                        xmlProp.setReadOnly(true);
                    }
                }
                break;
                case INT: {
                    if (val instanceof IKernelIntEnum) {
                        xmlProp.setInt(((IKernelIntEnum) val).getValue());
                    } else {
                        xmlProp.setInt(((Long) val));
                    }
                }
                break;
                case NUM: {
                    xmlProp.setNum((BigDecimal) val);
                }
                break;
                case CLOB: {
                    if (val != null) {
                        final java.sql.Clob c = (java.sql.Clob) val;

                        if (c.length() > Integer.MAX_VALUE) {
                            throw new IllegalUsageError("Can't convert Clob value to XML value: Clob data size is too big", null);
                        }
                        final int len = (int) c.length();
                        xmlProp.setStr(c.getSubString(1L, len));
                    } else {
                        xmlProp.setStr(null);
                    }
                }
                break;
                case BLOB: {
                    if (val != null) {
                        final java.sql.Blob b = (java.sql.Blob) val;

                        final int len = (int) b.length();
                        if (len != b.length()) {
                            throw new IllegalUsageError("Can't convert Blob value to XML value: Blob data size is too big", null);
                        }
                        xmlProp.setBin(b.getBytes(1L, len));
                    } else {
                        xmlProp.setBin(null);
                    }
                }
                break;
                case DATE_TIME: {
                    xmlProp.setDateTime((Timestamp) val);
                }
                break;
                case BIN: {
                    if (val != null) {
                        xmlProp.setBin(((Bin) val).get());
                    } else {
                        xmlProp.setBin(null);
                    }
                }
                break;
                case BOOL: {
                    xmlProp.setBool(((Boolean) val));
                }
                break;
                case XML: {
                    XmlObject xml = (XmlObject) val;
                    if (xml != null && xml.getDomNode().getNodeType() == Node.DOCUMENT_NODE/*&& xml.getClass().getName().endsWith(XML_BEANS_DOCUMENT_CLASS_POSTFIX)*/) { //документ, берем 1-го child-a (все равно без имени не копируется)
                        final XmlCursor c = xml.newCursor();
                        try {
                            if (c.toFirstChild()) {
                                xml = c.getObject();
                            } else {
                                xml = null;
                            }
                        } finally {
                            c.dispose();
                        }
                    }
                    if (xml == null) {
                        xmlProp.setNilXml();
                    } else {
                        final String localName = xml.getDomNode().getLocalName();
                        if (localName == null) {
                            throw new IllegalUsageError("Can't put XML property value into the EAS message: root node name isn't defined", null);
                        }
                        final XmlObject tagXml = xmlProp.addNewXml();
                        final String namespaceURI = xml.getDomNode().getNamespaceURI();
                        tagXml.getDomNode().appendChild(tagXml.getDomNode().getOwnerDocument().createElementNS(namespaceURI, localName));
                        final XmlObject[] valXml = tagXml.selectChildren(namespaceURI, localName);
                        if (valXml.length != 1) {
                            throw new IllegalUsageError("Can't put XML property value into the EAS message", null);
                        }
                        valXml[0].set(xml);
                    }
                }
                break;
                case ARR_STR:
                case ARR_CHAR: {
                    if (val != null) {
                        final Property.ArrStr xmlArr = xmlProp.addNewArrStr();
                        final Arr arr = (Arr) val;
                        for (Object it : arr) {
                            if (it != null) {
                                if (it instanceof IKernelCharEnum) {
                                    final Character c = ((IKernelCharEnum) it).getValue();
                                    if (c != null) {
                                        xmlArr.getItemList().add(c.toString());
                                    } else {
                                        xmlArr.getItemList().add(null);
                                    }
                                } else if (it instanceof IKernelStrEnum) {
                                    xmlArr.getItemList().add(((IKernelStrEnum) it).getValue());
                                } else {
                                    xmlArr.getItemList().add(it.toString());
                                }
                            } else {
                                xmlArr.getItemList().add(null);
                            }
                        }
                    } else {
                        xmlProp.setNilArrStr();
                    }
                }
                break;
                case ARR_REF: {
                    if (val != null) {
                        xmlProp.unsetArrRef();
                        final Property.ArrRef xmlArr = xmlProp.addNewArrRef();
                        final ArrEntity<? extends Entity> arr = (ArrEntity<? extends Entity>) val;
                        Property.ArrRef.Item item;
                        Entity ent;
                        final Iterator<? extends Entity> it = arr.iterator();
                        while (it.hasNext()) {
                            item = xmlArr.addNewItem();
                            try {
                                ent = it.next();
                                if (ent != null) {
                                    if (parentInfoProducer != null) {
                                        ParentInfo parentInfo = parentInfoProducer.getParentInfo(ent);
                                        item.setTitle(parentInfo.title);
                                    } else {
                                        item.setTitle(ent.calcTitle());                                        
                                    }
                                    item.setPID(ent.getPid().toString());
                                } else {
                                    item.setNil();
                                }
                            } catch (EntityObjectNotExistsError e) {
                                item.setBrokenRef(e.getBrokenRefPres()); //RADIX-3199
                                //item.setTitle("Can't calculate object title: " + ExceptionTextFormatter.getExceptionMess(e));
                            }
                        }
                    } else {
                        xmlProp.setNilArrRef();
                    }
                }
                break;
                case ARR_INT: {
                    if (val != null) {
                        final Property.ArrInt xmlArr = xmlProp.addNewArrInt();
                        final Arr arr = (Arr) val;
                        for (Object it : arr) {
                            if (it != null) {
                                if (it instanceof Long) {
                                    xmlArr.getItemList().add((Long) it);
                                } else {
                                    xmlArr.getItemList().add(((IKernelIntEnum) it).getValue());
                                }
                            } else {
                                xmlArr.getItemList().add(null);
                            }
                        }
                    } else {
                        xmlProp.setNilArrInt();
                    }
                }
                break;
                case ARR_NUM: {
                    if (val != null) {
                        xmlProp.addNewArrNum().getItemList().addAll((ArrNum) val);
                    } else {
                        xmlProp.setNilArrNum();
                    }
                }
                break;
                case ARR_DATE_TIME: {
                    if (val != null) {
                        xmlProp.addNewArrDateTime().getItemList().addAll((ArrDateTime) val);
                    } else {
                        xmlProp.setNilArrDateTime();
                    }
                }
                break;
                case ARR_BOOL: {
                    if (val != null) {
                        xmlProp.addNewArrBool().getItemList().addAll((ArrBool) val);
                    } else {
                        xmlProp.setNilArrBool();
                    }
                }
                break;
                case ARR_BIN: {
                    if (val != null) {
                        final Property.ArrBin xmlArr = xmlProp.addNewArrBin();
                        final ArrBin arr = (ArrBin) val;
                        for (int i = 0; i < arr.size(); i++) {
                            if (arr.get(i) != null) {
                                xmlArr.getItemList().add(arr.get(i).get());
                            } else {
                                xmlArr.getItemList().add(null);
                            }
                        }
                    } else {
                        xmlProp.setNilArrBin();
                    }
                }
                break;
                default:
                    throw new IllegalUsageError("Can't convert value to XML value: value type \"" + valType.getName() + "\" is not supported in EasValueConverter.objVal2EasPropXmlVal()", null);
            }
        } catch (SQLException e) {
            throw new IllegalUsageError("Can't convert value to XML value: " + ExceptionTextFormatter.getExceptionMess(e), e);
        }
    }

    private static XmlOptions getXmlOptions() {
        XmlOptions options = new XmlOptions();
        options.setSaveNamespacesFirst();
        options.setSaveAggressiveNamespaces();
        options.setUseDefaultNamespace();
        return options;
    }

    public static Clob xml2Clob(Arte arte, XmlObject obj) {
        if (obj == null) {
            return null;
        }
        java.io.Writer stream = null;
        try {
            java.sql.Clob tmp = arte.getDbConnection().createTemporaryClob();
            stream = tmp.setCharacterStream(1);
            obj.save(stream, getXmlOptions());
            return tmp;
        } catch (java.sql.SQLException | java.io.IOException e) {
            throw new org.radixware.kernel.common.exceptions.WrongFormatError("Unable to save xml data to CLOB", e);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (java.io.IOException e) {
                }
            }
        }
    }

    public static Blob xml2Blob(Arte arte, XmlObject obj) {
        if (obj == null) {
            return null;
        }
        java.io.OutputStream stream = null;
        try {
            java.sql.Blob tmp = arte.getDbConnection().createTemporaryBlob();
            stream = tmp.setBinaryStream(1);

            obj.save(stream, getXmlOptions());
            return tmp;
        } catch (java.sql.SQLException | java.io.IOException e) {
            throw new org.radixware.kernel.common.exceptions.WrongFormatError("Unable to save xml data to BLOB", e);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (java.io.IOException e) {
                }
            }
        }
    }

    public static Clob str2Clob(Arte arte, String obj) {
        if (obj == null) {
            return null;
        }
        try {
            final Clob tmp = arte.getDbConnection().createTemporaryClob();
            tmp.setString(1, obj);
            return tmp;
        } catch (java.sql.SQLException e) {
            throw new org.radixware.kernel.common.exceptions.WrongFormatError("Unable to save string data to CLOB", e);
        }
    }

    public final static Object easPropXmlVal2ObjVal(final Arte arte, final org.radixware.schemas.eas.Property xmlProp, final EValType valType, final Id valEntityId) {
        // отчасти аналогична aasXmlVal2ObjVal - синхронизировать
        try {
            switch (valType) {
                case STR: {
                    return xmlProp.getStr();
                }
                case CHAR: {
                    if (xmlProp.isSetStr()) {
                        final String str = xmlProp.getStr();
                        if (str != null && str.length() != 0) {
                            return Character.valueOf(str.charAt(0));
                        }
                    }
                    return null;
                }
                case OBJECT:
                case PARENT_REF: {
                    if (xmlProp.isSetRef() && xmlProp.getRef().isSetPID()) {
                        return new Pid(arte, valEntityId, xmlProp.getRef().getPID());
                    } else {
                        return null;
                    }
                }
                case CLOB: {
                    if (xmlProp.isSetStr()) {
                        final String str = xmlProp.getStr();
                        if (str != null) {
                            final Clob res = arte.getDbConnection().createTemporaryClob();
                            res.setString(1, str);
                            return res;
                        }
                    } else if (xmlProp.isSetXml()) {
                        XmlObject xml = xmlProp.getXml();
                        if (xml != null) {
                            return xml2Clob(arte, xml);
                        }
                    }
                    return null;
                }
                case INT:
                    return xmlProp.getInt();
                case NUM:
                    return xmlProp.getNum();
                case BOOL:
                    return xmlProp.getBool();
                case BLOB: {
                    if (xmlProp.isSetBin()) {
                        final byte[] b = xmlProp.getBin();
                        if (b != null) {
                            final Blob res = arte.getDbConnection().createTemporaryBlob();
                            res.setBytes(1, b);
                            return res;
                        }
                    }
                    return null;
                }
                case BIN: {
                    if (xmlProp.isSetBin()) {
                        final byte[] b = xmlProp.getBin();
                        if (b != null) {
                            return new Bin(b);
                        }
                    }
                    return null;
                }
                case DATE_TIME:
                    return xmlProp.getDateTime();
                case XML: {
                    if (xmlProp.getXml() == null) {
                        return null;
                    }
                    final XmlCursor c = xmlProp.getXml().newCursor();
                    try {
                        if (c.toFirstChild()) {
                            return c.getObject();
                        } else {
                            return null;
                        }
                    } finally {
                        c.dispose();
                    }
                }
                case ARR_STR: {
                    if (xmlProp.isSetArrStr() && !xmlProp.isNilArrStr()) {
                        final Property.ArrStr xmlArr = xmlProp.getArrStr();
                        final ArrStr arr = new ArrStr(xmlArr.getItemList().size());
                        arr.addAll(xmlArr.getItemList());
                        return arr;
                    } else {
                        return null;
                    }
                }
                case ARR_INT: {
                    if (xmlProp.isSetArrInt() && !xmlProp.isNilArrInt()) {
                        final Property.ArrInt xmlArr = xmlProp.getArrInt();
                        final ArrInt arr = new ArrInt(xmlArr.getItemList().size());
                        arr.addAll(xmlArr.getItemList());
                        return arr;
                    } else {
                        return null;
                    }
                }
                case ARR_NUM: {
                    if (xmlProp.isSetArrNum() && !xmlProp.isNilArrNum()) {
                        final Property.ArrNum xmlArr = xmlProp.getArrNum();
                        final ArrNum arr = new ArrNum(xmlArr.getItemList().size());
                        arr.addAll(xmlArr.getItemList());
                        return arr;
                    } else {
                        return null;
                    }
                }
                case ARR_DATE_TIME: {
                    if (xmlProp.isSetArrDateTime() && !xmlProp.isNilArrDateTime()) {
                        final Property.ArrDateTime xmlArr = xmlProp.getArrDateTime();
                        final ArrDateTime arr = new ArrDateTime(xmlArr.getItemList().size());
                        arr.addAll(xmlArr.getItemList());
                        return arr;
                    } else {
                        return null;
                    }
                }
                case ARR_BOOL: {
                    if (xmlProp.isSetArrBool() && !xmlProp.isNilArrBool()) {
                        final List<Boolean> items = xmlProp.getArrBool().getItemList();
                        final ArrBool res = new ArrBool(items.size());
                        res.addAll(items);
                        return res;
                    } else {
                        return null;
                    }
                }
                case ARR_REF: {
                    if (xmlProp.isSetArrRef() && !xmlProp.isNilArrRef()) {
                        final Property.ArrRef xmlArr = xmlProp.getArrRef();
                        final Pid[] arr = new Pid[xmlArr.sizeOfItemArray()];
                        for (int i = 0; i < xmlArr.sizeOfItemArray(); i++) {
                            if (!xmlArr.isNilItemArray(i) && xmlArr.getItemArray(i).isSetPID()) {
                                arr[i] = xmlArr.getItemArray(i) != null ? new Pid(arte, valEntityId, xmlArr.getItemArray(i).getPID()) : null;
                            } else {
                                arr[i] = null;
                            }
                        }
                        return new ArrEntity(arte, arr);
                    } else {
                        return null;
                    }
                }
                case ARR_CHAR: {
                    if (xmlProp.isSetArrStr() && !xmlProp.isNilArrStr()) {
                        final Property.ArrStr xmlArr = xmlProp.getArrStr();
                        final ArrChar arr = new ArrChar(xmlArr.getItemList().size());
                        for (String itemStr : xmlArr.getItemList()) {
                            arr.add(itemStr != null && itemStr.length() != 0 ? Character.valueOf(itemStr.charAt(0)) : null);
                        }
                        return arr;
                    } else {
                        return null;
                    }
                }
                case ARR_BIN: {
                    if (xmlProp.isSetArrBin() && !xmlProp.isNilArrBin()) {
                        final Property.ArrBin xmlArr = xmlProp.getArrBin();
                        final ArrBin arr = new ArrBin(xmlArr.getItemList().size());
                        for (byte[] itemBytes : xmlArr.getItemList()) {
                            arr.add(itemBytes != null ? new Bin(itemBytes) : null);
                        }
                        return arr;
                    } else {
                        return null;
                    }
                }
                default:
                    throw new IllegalUsageError("Can't convert XML value to Radix java value: value type \"" + valType.getName() + "\" is not supported in EasValueConverter.easPropXmlVal2ObjVal()", null);
            }
        } catch (SQLException e) {
            throw new IllegalUsageError("Can't convert XML value to Radix java value: " + ExceptionTextFormatter.getExceptionMess(e), e);
        }
    }
    private static final String XML_BEANS_DOCUMENT_CLASS_POSTFIX = "DocumentImpl";
}
