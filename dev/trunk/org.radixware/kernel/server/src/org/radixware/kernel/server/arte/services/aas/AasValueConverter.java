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

package org.radixware.kernel.server.arte.services.aas;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Map;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.types.*;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.Maps;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.types.ArrEntity;
import org.radixware.kernel.server.types.Entity;
import org.radixware.kernel.server.types.Pid;
import org.radixware.schemas.aas.Value;


public final class AasValueConverter {
//	Converters
	public final static void objVal2AasXmlVal(final Object val, final Value xmlValue) {
		// отчасти аналогична objVal2DasPropXmlVal - синхронизировать
		if (val == null) {
			xmlValue.setNil();
			return;
		}
		try {
			if (val instanceof String || val instanceof Character) {
				xmlValue.setStr(val.toString());
				return;
			}
			if (val instanceof XmlObject) {
				XmlObject xml = (XmlObject) val;
				if ( xml.getClass().getName().endsWith(XML_BEANS_DOCUMENT_CLASS_POSTFIX)) { //документ, берем 1-го child-a (все равно без имени не копируется)
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
					xmlValue.setNilXml();
				} else {
					final XmlObject tagXml = xmlValue.addNewXml();
					tagXml.getDomNode().appendChild(tagXml.getDomNode().getOwnerDocument().createElementNS(xml.getDomNode().getNamespaceURI(), xml.getDomNode().getLocalName()));
					final XmlObject[] valXml = tagXml.selectChildren(xml.getDomNode().getNamespaceURI(), xml.getDomNode().getLocalName());
					if (valXml.length != 1) {
						throw new IllegalUsageError("Can't put XML property value in AAS message", null);
					}
					valXml[0].set(xml);
				}
				return;
			}
			if (val instanceof Pid) {
				final Value.Ref refXml = xmlValue.addNewRef();
				refXml.setEntityId(((Pid) val).getEntityId().toString());
				refXml.setPID(((Pid) val).toString());
				return;
			}
			if (val instanceof Entity) {
				final Value.Ref refXml = xmlValue.addNewRef();
				refXml.setEntityId(((Entity) val).getPid().getEntityId().toString());
				refXml.setPID(((Entity) val).getPid().toString());
				return;
			}
			if (val instanceof Long) {
				xmlValue.setInt(((Long) val));
				return;
			}
			if (val instanceof IKernelIntEnum) {
				xmlValue.setInt(((IKernelIntEnum) val).getValue());
				return;
			}
			if (val instanceof IKernelStrEnum) {
				xmlValue.setStr(((IKernelStrEnum) val).getValue());
				return;
			}
			if (val instanceof IKernelCharEnum) {
				xmlValue.setStr(String.valueOf(((IKernelCharEnum) val).getValue()));
				return;
			}
			if (val instanceof BigDecimal) {
				xmlValue.setNum(((BigDecimal) val));
				return;
			}
			if (val instanceof Clob) {
				final java.sql.Clob c = (java.sql.Clob) val;

				if (c.length() > Integer.MAX_VALUE) {
					throw new IllegalUsageError("Can't convert Clob value to XML value: Clob data size is too big", null);
				}
				final int len = (int) c.length();
				xmlValue.setStr(c.getSubString(1L, len));
				return;
			}
			if (val instanceof Blob) {
				final java.sql.Blob b = (java.sql.Blob) val;

				final int len = (int) b.length();
				if (len != b.length()) {
					throw new IllegalUsageError("Can't convert Blob value to XML value: Blob data size is too big", null);
				}
				xmlValue.setBin(b.getBytes(1L, len));
				return;
			}
			if (val instanceof Timestamp) {
				xmlValue.setDateTime((Timestamp) val);
				return;
			}
			if (val instanceof Bin) {
				xmlValue.setBin(((Bin) val).get());
				return;
			}
			if (val instanceof Boolean) {
				xmlValue.setBool(((Boolean) val));
				return;
			}
			if (val instanceof ArrStr || val instanceof ArrChar) {
				final Value.ArrStr xmlArr = xmlValue.addNewArrStr();
				final Arr arr = (Arr) val;
				for (Object it : arr) {
					if (it != null) {
						xmlArr.getItemList().add(it.toString());
					} else {
						xmlArr.getItemList().add(null);
					}
				}
				return;
			}
			if (val instanceof ArrEntity) {
				final Value.ArrRef xmlArr = xmlValue.addNewArrRef();
				final ArrEntity<? extends Entity> arr = (ArrEntity) val;
				Value.ArrRef.Item item;
				for (Entity e : arr) {
					if (e != null) {
						item = xmlArr.addNewItem();
						item.setEntityId(e.getEntityId().toString());
						item.setPID(e.toString());
					} else {
						xmlArr.addNewItem().setNil();
					}
				}
				return;
			}
			if (val instanceof ArrInt) {
				xmlValue.addNewArrInt().getItemList().addAll((ArrInt) val);
				return;
			}
			if (val instanceof ArrNum) {
				xmlValue.addNewArrNum().getItemList().addAll((ArrNum) val);
				return;
			}
			if (val instanceof ArrDateTime) {
				xmlValue.addNewArrDateTime().getItemList().addAll((ArrDateTime) val);
				return;
			}
			if (val instanceof ArrBool) {
				xmlValue.addNewArrBool().getItemList().addAll((ArrBool) val);
				return;
			}
			if (val instanceof ArrBin) {
				final Value.ArrBin xmlArr = xmlValue.addNewArrBin();
				final ArrBin arr = (ArrBin) val;
				for (int i = 0; i < arr.size(); i++) {
					if (arr.get(i) != null) {
						xmlArr.getItemList().add(arr.get(i).get());
					} else {
						xmlArr.getItemList().add(null);
					}
				}
				return;
			}
                        if (val instanceof Map) {
                            xmlValue.setMapStrStr(Maps.toXml((Map)val));
                            return;
                        }
			throw new IllegalUsageError("Can't convert value to XML value: value class \"" + val.getClass().getName() + "\" is not supported in DbpValueConverter.objVal2AasXmlVal()", null);
		} catch (SQLException e) {
			throw new IllegalUsageError("Can't convert value to XML value: " + ExceptionTextFormatter.getExceptionMess(e), e);
		}
	}

	public final static Object aasXmlVal2ObjVal(final Arte arte, final Value xmlValue) {
		// отчасти аналогична dasPropXmlVal2ObjVal - синхронизировать
		if (xmlValue.isNil()) {
			return null;
		}
		if (xmlValue.isSetArte()) {
			return arte;
		}
		if (xmlValue.isSetXml()) {
			if (xmlValue.getXml() == null) {
				return null;
			}
			final XmlCursor c = xmlValue.getXml().newCursor();
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
		if (xmlValue.isSetStr()) {
			return xmlValue.getStr();
		}
		if (xmlValue.isSetRef()) {
			if (xmlValue.getRef().isNil() || xmlValue.getRef().getPID() == null || xmlValue.getRef().getPID().length() == 0) {
				return null;
			}
			return arte.getEntityObject(new Pid(arte, Id.Factory.loadFrom(xmlValue.getRef().getEntityId()), xmlValue.getRef().getPID()));
		}
		if (xmlValue.isSetInt()) {
			return xmlValue.getInt();
		}
		if (xmlValue.isSetNum()) {
			return xmlValue.getNum();
		}
		if (xmlValue.isSetBool()) {
			return xmlValue.getBool();
		}
		if (xmlValue.isSetDateTime()) {
			return xmlValue.getDateTime();
		}
		if (xmlValue.isSetBin()) {
			final byte[] b = xmlValue.getBin();
			if (b == null) {
				return null;
			}
			return new Bin(b);
		}
		if (xmlValue.isSetArrStr()) {
			if (xmlValue.isNilArrStr()) {
				return null;
			}
			final ArrStr arr = new ArrStr();
			arr.addAll(xmlValue.getArrStr().getItemList());
			return arr;
		}
		if (xmlValue.isSetArrInt()) {
			if (xmlValue.isNilArrInt()) {
				return null;
			}
			final ArrInt arr = new ArrInt();
			arr.addAll(xmlValue.getArrInt().getItemList());
			return arr;
		}
		if (xmlValue.isSetArrNum()) {
			if (xmlValue.isNilArrNum()) {
				return null;
			}
			final ArrNum arr = new ArrNum();
			arr.addAll(xmlValue.getArrNum().getItemList());
			return arr;
		}
		if (xmlValue.isSetArrDateTime()) {
			if (xmlValue.isNilArrDateTime()) {
				return null;
			}
			final ArrDateTime arr = new ArrDateTime();
			arr.addAll(xmlValue.getArrDateTime().getItemList());
			return arr;
		}
		if (xmlValue.isSetArrBool()) {
			if (xmlValue.isNilArrBool()) {
				return null;
			}
			final ArrBool arr = new ArrBool();
			arr.addAll(xmlValue.getArrBool().getItemList());
			return arr;
		}
		if (xmlValue.isSetArrRef()) {
			if (xmlValue.isNilArrRef()) {
				return null;
			}
			final Value.ArrRef xmlArr = xmlValue.getArrRef();
			final Pid[] arr = new Pid[xmlArr.sizeOfItemArray()];
			for (int i = 0; i < xmlArr.sizeOfItemArray(); i++) {
				if (!xmlArr.isNilItemArray(i)) {
					arr[i] = xmlArr.getItemArray(i) != null ? new Pid(arte, Id.Factory.loadFrom(xmlArr.getItemArray(i).getEntityId()), xmlArr.getItemArray(i).getPID()) : null;
				} else {
					arr[i] = null;
				}
			}
			return new ArrEntity(arte, arr);
		}
		if (xmlValue.isSetArrBin()) {
			if (xmlValue.isNilArrBin()) {
				return null;
			}
			final Value.ArrStr xmlArr = xmlValue.getArrStr();
			final Bin[] arr = new Bin[xmlArr.getItemList().size()];
			for (int i = 0; i < xmlArr.getItemList().size(); i++) {
				arr[i] = new Bin(xmlArr.getItemList().get(i));
			}
			return new ArrBin(arr);
		}
		if (xmlValue.isSetMapStrStr()) {
                    return Maps.fromXml(xmlValue.getMapStrStr());
		}
                
		//если нет вложеннных тагов, то null
		final XmlCursor c = xmlValue.newCursor();
		try {
			if (!c.toFirstChild()) {
				return null;
			}
		} finally {
			c.dispose();
		}

		throw new IllegalUsageError("Can't convert AAS XML value \"" + xmlValue.toString() + "\" to DBP java value", null);
	}

	private static final String XML_BEANS_DOCUMENT_CLASS_POSTFIX = "DocumentImpl";
}
