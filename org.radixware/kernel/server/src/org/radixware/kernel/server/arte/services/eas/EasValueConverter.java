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
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.EDdsTableExtOption;
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
import org.radixware.kernel.common.utils.XmlUtils;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.exceptions.EntityObjectNotExistsError;
import org.radixware.kernel.server.meta.clazzes.RadClassDef;
import org.radixware.kernel.server.types.ArrEntity;
import org.radixware.kernel.server.types.Entity;
import org.radixware.kernel.server.types.Pid;
import org.radixware.schemas.eas.ObjectReference;
import org.radixware.schemas.eas.Property;
import org.w3c.dom.Node;

public final class EasValueConverter {        

    public static final Set<EValType> SUPPORTED_VAL_TYPES;

    static {
        SUPPORTED_VAL_TYPES = new HashSet<>();
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
                            final ObjectReference refXml = xmlProp.addNewRef();
                            try {
                                Entity ent = (Entity) val;
                                refXml.setTitle(ent.calcTitle());
                                refXml.setPID(ent.getPid().toString());                                
                                refXml.setClassId(ent.getRadMeta().getId());
                            } catch (EntityObjectNotExistsError e) {
                                refXml.setBrokenRef(e.getBrokenRefPres());
                            }
                            xmlProp.setRef(refXml);
                        }
                    } else if (ptValInfo.isBrokenRef()) {//RADIX-3199
                        final ObjectReference refXml = xmlProp.addNewRef();
                        refXml.setBrokenRef(ptValInfo.brokenRefErr.getBrokenRefPres());
                        refXml.setAllowedActionsBitMask(0);
                    } else if (val != null || valType == EValType.PARENT_REF) {
                        final ObjectReference refXml = xmlProp.addNewRef();
                        if (val != null) {
                            refXml.setPID(val instanceof Entity ? ((Entity) val).getPid().toString() : val.toString());
                            final EnumSet<EReferencedObjectActions> allowedActions = ptValInfo.allowedActions;
                            if (!allowedActions.containsAll(EnumSet.allOf(EReferencedObjectActions.class))) {
                                refXml.setAllowedActionsBitMask(EReferencedObjectActions.toBitMask(allowedActions));
                            }
                            if (val instanceof Entity) {
                                refXml.setClassId(((Entity) val).getRadMeta().getId());
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
                        ObjectReference item;
                        Entity ent;
                        final Iterator<? extends Entity> it = arr.iterator();
                        while (it.hasNext()) {
                            item = xmlArr.addNewItem();
                            try {
                                ent = it.next();
                                if (ent != null) {
                                    if (parentInfoProducer != null) {
                                        final ParentInfo parentInfo = parentInfoProducer.getParentInfo(ent);
                                        item.setTitle(parentInfo.title);
                                    } else {
                                        item.setTitle(ent.calcTitle());                                        
                                    }
                                    item.setPID(ent.getPid().toString());
                                    item.setClassId(ent.getRadMeta().getId());
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
    
    public final static Object easPropXmlVal2ObjVal(final Arte arte, final org.radixware.schemas.eas.Property xmlProp, final Id valEntityId) {
        if (xmlProp.isSetStr()){
            return XmlUtils.parseSafeXmlString(xmlProp.getStr());
        }else if (xmlProp.isSetInt()){
            return xmlProp.getInt();
        }else if (xmlProp.isSetNum()){
            return xmlProp.getNum();
        }else if (xmlProp.isSetBool()){
            return xmlProp.getBool();
        }else if (xmlProp.isSetDateTime()){
            return xmlProp.getDateTime();
        }else if (xmlProp.isSetBin()){
            return readBin(xmlProp);
        }else if (xmlProp.isSetXml()){
            return readXml(xmlProp);
        }else if (xmlProp.isSetRef()){
            return readPid(xmlProp.getRef(), arte, valEntityId);
        }else if (xmlProp.isSetObj()){
            return xmlProp.getObj();
        } else if (xmlProp.isSetArrBin()){
            return xmlProp.isNilArrBin() ? null : readArrBin(xmlProp.getArrBin());
        }else if (xmlProp.isSetArrBool()){
            return xmlProp.isNilArrBool() ? null : readArrBool(xmlProp.getArrBool());
        }else if (xmlProp.isSetArrDateTime()){
            return xmlProp.isNilArrDateTime() ? null : readArrDateTime(xmlProp.getArrDateTime());
        }else if (xmlProp.isSetArrInt()){
            return xmlProp.isNilArrInt() ? null : readArrInt(xmlProp.getArrInt());
        }else if (xmlProp.isSetArrNum()){
            return xmlProp.isNilArrNum() ? null : readArrNum(xmlProp.getArrNum());            
        }else if (xmlProp.isSetArrRef()){
            return xmlProp.isNilArrRef() ? null : readArrRef(xmlProp.getArrRef(), arte, valEntityId);
        }else if (xmlProp.isSetArrStr()){
            return xmlProp.isNilArrStr() ? null : readArrStr(xmlProp.getArrStr());
        }else{
            return null;
        }
    }
    public final static Object easPropXmlVal2ObjVal(final Arte arte, 
                                                                           final org.radixware.schemas.eas.Property xmlProp, 
                                                                           final EValType valType, 
                                                                           final Id valEntityId){
        return easPropXmlVal2ObjVal(arte, xmlProp, valType, valEntityId, false);
    }
            
    public final static Object easPropXmlVal2ObjVal(final Arte arte, 
                                                                           final org.radixware.schemas.eas.Property xmlProp, 
                                                                           final EValType valType, 
                                                                           final Id valEntityId,
                                                                           final boolean refAsObject) {
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
                    if (xmlProp.isSetObj()){
                        return xmlProp.getObj();
                    }
                case PARENT_REF: {
                    if (xmlProp.isSetRef() && xmlProp.getRef().isSetPID()) {
                        final Pid pid = readPid(xmlProp.getRef(), arte, valEntityId);                        
                        if (pid!=null && refAsObject){
                            final Id classId = xmlProp.getRef().getClassId();
                            if (classId!=null){
                                final DdsTableDef table = pid.getTable();
                                if (table.getExtOptions().contains(EDdsTableExtOption.ENABLE_APPLICATION_CLASSES)) {
                                    return arte.getEntityObject(pid, classId.toString());
                                }
                            }
                            return arte.getEntityObject(pid);
                        }
                        return pid;
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
                    return readBin(xmlProp);
                }
                case DATE_TIME:
                    return xmlProp.getDateTime();
                case XML: {
                    return readXml(xmlProp);
                }
                case ARR_STR: {
                    return xmlProp.isSetArrStr() && !xmlProp.isNilArrStr() ? readArrStr(xmlProp.getArrStr()) : null;
                }
                case ARR_INT: {
                    return xmlProp.isSetArrInt() && !xmlProp.isNilArrInt() ? readArrInt(xmlProp.getArrInt()) : null;
                }
                case ARR_NUM: {
                    return xmlProp.isSetArrNum() && !xmlProp.isNilArrNum() ? readArrNum(xmlProp.getArrNum()) : null;
                }
                case ARR_DATE_TIME: {
                    return xmlProp.isSetArrDateTime() && !xmlProp.isNilArrDateTime() ? readArrDateTime(xmlProp.getArrDateTime()) : null;
                }
                case ARR_BOOL: {                    
                    return xmlProp.isSetArrBool() && !xmlProp.isNilArrBool() ? readArrBool(xmlProp.getArrBool()) : null;                    
                }
                case ARR_REF: {
                    if (xmlProp.isSetArrRef() && !xmlProp.isNilArrRef()) {
                        return readArrRef(xmlProp.getArrRef(), arte, valEntityId);
                    } else {
                        return null;
                    }
                }
                case ARR_CHAR: {
                    return xmlProp.isSetArrStr() && !xmlProp.isNilArrStr() ? readArrChar(xmlProp.getArrStr()) : null;
                }
                case ARR_BIN: {
                    return xmlProp.isSetArrBin() && !xmlProp.isNilArrBin() ? readArrBin(xmlProp.getArrBin()) : null;
                }
                default:
                    throw new IllegalUsageError("Can't convert XML value to Radix java value: value type \"" + valType.getName() + "\" is not supported in EasValueConverter.easPropXmlVal2ObjVal()", null);
            }
        } catch (SQLException e) {
            throw new IllegalUsageError("Can't convert XML value to Radix java value: " + ExceptionTextFormatter.getExceptionMess(e), e);
        }
    }    
    
    private static ArrBin readArrBin(final Property.ArrBin xml){
        if (xml==null){
            return null;
        }else{
            final ArrBin arr = new ArrBin(xml.getItemList().size());
            for (byte[] itemBytes : xml.getItemList()) {
                arr.add(itemBytes != null ? new Bin(itemBytes) : null);
            }
            return arr;
        }
    }
    
    private static ArrBool readArrBool(final Property.ArrBool xml){
        return xml==null ? null : new ArrBool(xml.getItemList());
    }            
    
    private static ArrChar readArrChar(final Property.ArrStr xml){
        if (xml==null){
            return null;
        }else{
            final ArrChar arr = new ArrChar(xml.getItemList().size());
            for (String itemStr : xml.getItemList()) {
                arr.add(itemStr != null && itemStr.length() != 0 ? Character.valueOf(itemStr.charAt(0)) : null);
            }
            return arr;            
        }
    }
    
    private static ArrDateTime readArrDateTime(final Property.ArrDateTime xml){
        return xml==null ? null : new ArrDateTime(xml.getItemList());
    }    
    
    private static ArrInt readArrInt(final Property.ArrInt xml){
        return xml==null ? null : new ArrInt(xml.getItemList());
    }
    
    private static ArrNum readArrNum(final Property.ArrNum xml){
        return xml==null ? null : new ArrNum(xml.getItemList());
    }
    
    private static ArrEntity readArrRef(final Property.ArrRef xml, final Arte arte, final Id entityId){
        if (xml==null){
            return null;
        }else{
            final List<Entity> entityList = new LinkedList<>();
            final Id defaultEntityId = xml.isSetTableId() ? xml.getTableId() : entityId;            
            Pid pid;
            Id classId;
            for (int i = 0; i < xml.sizeOfItemArray(); i++) {     
                pid = xml.isNilItemArray(i) ? null : readPid(xml.getItemArray(i), arte, defaultEntityId);
                if (pid==null){
                    entityList.add(null);
                }else{
                    classId = xml.getItemArray(i).getClassId();
                    final Entity object;
                    if (classId!=null){
                        final DdsTableDef table = pid.getTable();
                        if (table.getExtOptions().contains(EDdsTableExtOption.ENABLE_APPLICATION_CLASSES)) {
                            object = arte.getEntityObject(pid, classId.toString());
                        }else{
                            object = arte.getEntityObject(pid);
                        }
                    }else{
                        object = arte.getEntityObject(pid);
                    }
                    entityList.add(object);
                }
            }
            return new ArrEntity(arte, entityList);
        }
    }
    
    private static ArrStr readArrStr(final Property.ArrStr xml){
        return xml==null ? null : new ArrStr(xml.getItemList());
    }
    
    private static Pid readPid(final ObjectReference xml, final Arte arte, final Id entityId){
        if (xml==null || !xml.isSetPID() || xml.getPID()==null){
            return null;
        }else{
            if (xml.isSetTableId()){
                return new Pid(arte, xml.getTableId(), xml.getPID());
            }else if (xml.isSetClassId()){
                final RadClassDef classDef = arte.getDefManager().getClassDef(xml.getClassId());
                return new Pid(arte, classDef.getEntityId(), xml.getPID());
            }else if (entityId!=null){
                return new Pid(arte, entityId, xml.getPID());
            }else{
                throw new IllegalArgumentException("Failed to read entity object identifier. Entity id was not defined.\n"+xml.xmlText());
            }
        }
    }
    
    private static Bin readBin(final org.radixware.schemas.eas.Property xmlProp){
        if (xmlProp.isSetBin()) {
            final byte[] b = xmlProp.getBin();
            if (b != null) {
                return new Bin(b);
            }
        }
        return null;        
    }
    
    private static XmlObject readXml(final org.radixware.schemas.eas.Property xmlProp){
        if (xmlProp.getXml() == null) {
            return null;
        }
        final XmlCursor c = xmlProp.getXml().newCursor();
        try {
            return c.toFirstChild() ? c.getObject() : null;
        } finally {
            c.dispose();
        }        
    }
}
