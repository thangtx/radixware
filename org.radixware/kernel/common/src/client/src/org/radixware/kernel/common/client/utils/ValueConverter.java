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
package org.radixware.kernel.common.client.utils;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.dialogs.IMessageBox;
import org.radixware.kernel.common.client.types.ArrRef;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.types.Reference;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.enums.EReferencedObjectActions;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.IllegalArgumentError;
import org.radixware.kernel.common.exceptions.WrongFormatError;
import org.radixware.kernel.common.types.*;
import org.radixware.kernel.common.utils.ValueFormatter;
import org.radixware.kernel.common.utils.XmlObjectProcessor;

public class ValueConverter {
//	Converters

    public static double floraValueOf(final java.util.Date val) {
        return org.radixware.kernel.common.utils.ValueConverter.floraValueOf(val);
    }

    public static void objVal2EasPropXmlVal(final Object val, final EValType valType, final org.radixware.schemas.eas.Property xmlProp) {
        // отчасти аналогична objVal2AasXmlVal - синхронизировать
        switch (valType) {
            case STR:
            case CLOB:
            case CHAR: {
                if (val instanceof IKernelEnum) {
                    xmlProp.setStr(((IKernelEnum) val).getValue().toString());
                } else if (val != null) {
                    xmlProp.setStr(val.toString());
                } else {
                    xmlProp.setStr(null);
                }
            }
            break;
            case OBJECT:
            case PARENT_REF: {
                if (val != null || valType == EValType.PARENT_REF) {
                    final org.radixware.schemas.eas.Property.Ref refXml = xmlProp.addNewRef();
                    if (val != null) {
                        final Reference ref = (Reference) val;
                        final Pid pid = ref.getPid();
                        if (pid != null) {
                            refXml.setPID(pid.toString());
                        }
                        if (ref.isBroken()) {
                            refXml.setBrokenRef("");
                        }
                    }
                } else {
                    xmlProp.setNilRef();
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
                xmlProp.setNum(ValueFormatter.normalizeBigDecimal((BigDecimal) val));
            }
            break;
            case DATE_TIME: {
                xmlProp.setDateTime(val == null ? null : (Timestamp) val);
            }
            break;
            case BLOB:
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
                xmlProp.setXml(((XmlObject) val));
            }
            break;
            case ARR_STR:
            case ARR_CHAR: {
                if (val != null) {
                    final org.radixware.schemas.eas.Property.ArrStr xmlArr = xmlProp.addNewArrStr();
                    final Arr arr = (Arr) val;
                    for (Object it : arr) {
                        if (it instanceof IKernelEnum) {
                            xmlArr.getItemList().add(((IKernelEnum) it).getValue().toString());
                        } else if (it != null) {
                            xmlArr.getItemList().add(it.toString());
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
                    final org.radixware.schemas.eas.Property.ArrRef xmlArr = xmlProp.addNewArrRef();
                    final ArrRef arr = (ArrRef) val;
                    org.radixware.schemas.eas.Property.ArrRef.Item item;
                    for (Reference ref : arr) {
                        if (ref != null && ref.getPid() != null) {
                            item = xmlArr.addNewItem();
                            item.setPID(ref.getPid().toString());
                        } else {
                            xmlArr.addNewItem().setNil();
                        }
                    }
                } else {
                    xmlProp.setNilArrStr();
                }
            }
            break;
            case ARR_INT: {
                if (val != null) {
                    final org.radixware.schemas.eas.Property.ArrInt xmlArr = xmlProp.addNewArrInt();
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
                    xmlProp.addNewArrNum().getItemList().addAll(ValueFormatter.normalizeArrNum((ArrNum) val));
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
                    final org.radixware.schemas.eas.Property.ArrBin xmlArr = xmlProp.addNewArrBin();
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
                throw new WrongFormatError("Can't convert value to XML value: value type \"" + valType.getName() + "\" is not supported in DbpValueConverter.objVal2DasPropXmlVal()", null);
        }
    }

    public static Object easPropXmlVal2ObjVal(final org.radixware.schemas.eas.Property xmlProp, final EValType valType, final Id valTableId) {
        switch (valType) {
            case STR:
            case CLOB: {
                return xmlProp.getStr();
            }
            case CHAR: {
                if (xmlProp.isSetStr()) {
                    final String str = xmlProp.getStr();
                    if (str != null && !str.isEmpty()) {
                        return Character.valueOf(str.charAt(0));
                    }
                }
                return null;
            }
            case OBJECT:
            case PARENT_REF: {
                if (valTableId == null) {
                    throw new WrongFormatError("Cannot convert to reference: entity identifier is required.", null);
                }
                if (xmlProp.isSetRef()) {
                    if (xmlProp.getRef().isSetPID()) {
                        return new Reference(new Pid(valTableId, xmlProp.getRef().getPID()),
                                xmlProp.getRef().getTitle(),
                                xmlProp.getRef().getBrokenRef(),
                                EReferencedObjectActions.fromBitMask(xmlProp.getRef().getAllowedActionsBitMask()));
                    } else if (xmlProp.getRef().isSetTitle() && xmlProp.getRef().getTitle() != null) {
                        //В презентационных атрибутах свойства может быть задан
                        //формат заголовка при нулевом значении
                        return new Reference((Pid) null, xmlProp.getRef().getTitle(), xmlProp.getRef().getBrokenRef());
                    } else if (xmlProp.getRef().isSetBrokenRef() && xmlProp.getRef().getBrokenRef() != null) {//RADIX-3199
                        //У ссылки на несуществующий объект может быть задано сообщение об ошибке
                        return new Reference((Pid) null, null, xmlProp.getRef().getBrokenRef());
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
            case BIN:
            case BLOB: {
                if (xmlProp.isSetBin()) {
                    final byte[] b = xmlProp.getBin();
                    if (b != null) {
                        return new Bin(b);
                    }
                }
                return null;
            }
            case DATE_TIME: {
                return xmlProp.getDateTime();
            }
            case XML:
                return XmlObjectProcessor.getXmlObjectFirstChild(xmlProp.getXml());
            case ARR_STR: {
                if (xmlProp.isSetArrStr() && !xmlProp.isNilArrStr()) {
                    final org.radixware.schemas.eas.Property.ArrStr xmlArr = xmlProp.getArrStr();
                    final ArrStr arr = new ArrStr(xmlArr.getItemList().size());
                    arr.addAll(xmlArr.getItemList());
                    return arr;
                } else {
                    return null;
                }
            }
            case ARR_INT: {
                if (xmlProp.isSetArrInt() && !xmlProp.isNilArrInt()) {
                    final org.radixware.schemas.eas.Property.ArrInt xmlArr = xmlProp.getArrInt();
                    final ArrInt arr = new ArrInt(xmlArr.getItemList().size());
                    arr.addAll(xmlArr.getItemList());
                    return arr;
                } else {
                    return null;
                }
            }
            case ARR_NUM: {
                if (xmlProp.isSetArrNum() && !xmlProp.isNilArrNum()) {
                    final org.radixware.schemas.eas.Property.ArrNum xmlArr = xmlProp.getArrNum();
                    final ArrNum arr = new ArrNum(xmlArr.getItemList().size());
                    arr.addAll(xmlArr.getItemList());
                    return arr;
                } else {
                    return null;
                }
            }
            case ARR_DATE_TIME: {
                if (xmlProp.isSetArrDateTime() && !xmlProp.isNilArrDateTime()) {
                    final org.radixware.schemas.eas.Property.ArrDateTime xmlArr = xmlProp.getArrDateTime();
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
                    if (valTableId == null) {
                        throw new WrongFormatError("Cannot convert to reference array: entity identifier is required.", null);
                    }
                    final org.radixware.schemas.eas.Property.ArrRef xmlArr = xmlProp.getArrRef();
                    final Reference[] arr = new Reference[xmlArr.sizeOfItemArray()];
                    org.radixware.schemas.eas.Property.ArrRef.Item xmlRef;
                    for (int i = 0; i < xmlArr.sizeOfItemArray(); i++) {
                        if (!xmlArr.isNilItemArray(i) && xmlArr.getItemArray(i) != null) {
                            xmlRef = xmlArr.getItemArray(i);
                            if (xmlRef.isSetPID()) {
                                arr[i] = new Reference(new Pid(valTableId, xmlRef.getPID()),
                                        xmlRef.getTitle(),
                                        xmlRef.getBrokenRef());
                            } else if (xmlRef.isSetTitle() && xmlRef.getTitle() != null) {
                                //В презентационных атрибутах свойства может быть задан
                                //формат заголовка при нулевом значении
                                arr[i] = new Reference((Pid) null, xmlRef.getTitle(), xmlRef.getBrokenRef());
                            } else if (xmlRef.isSetBrokenRef()) {//RADIX-3199
                                //У ссылки на несуществующий объект может быть задано сообщение об ошибке
                                arr[i] = new Reference((Pid) null, null, xmlRef.getBrokenRef());
                            } else {
                                arr[i] = null;
                            }
                        } else {
                            arr[i] = null;
                        }
                    }
                    return new ArrRef(arr);
                } else {
                    return null;
                }
            }
            case ARR_CHAR: {
                if (xmlProp.isSetArrStr() && !xmlProp.isNilArrStr()) {
                    final org.radixware.schemas.eas.Property.ArrStr xmlArr = xmlProp.getArrStr();
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
                    final org.radixware.schemas.eas.Property.ArrBin xmlArr = xmlProp.getArrBin();
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
                throw new WrongFormatError("Can't convert XML value to DBP java value: value type \"" + valType.getName() + "\" is not supported in DbpValueConverter.dasPropXmlVal2ObjVal()", null);
        }
    }

    public static Calendar timestamp2Calendar(final Timestamp time) {
        final Calendar result = Calendar.getInstance();
        result.setTime(time);
        return result;
    }

    public static String arrByte2Str(final byte bytes[], final String space) {
        final StringBuffer result = new StringBuffer("");
        boolean first = true;
        for (byte b : bytes) {
            int digit = b;
            if (digit < 0) {
                digit += 256;
            }
            if (first) {
                first = false;
            } else if (space != null && !space.isEmpty()) {
                result.append(space);
            }
            result.append(Character.forDigit(digit / 16, 16));
            result.append(Character.forDigit(digit % 16, 16));
        }
        return result.toString();
    }

    public static IMessageBox.StandardButton dialogButtonEnum2StandardButton(final EDialogButtonType buttonKind) {
        return IMessageBox.StandardButton.getForButtonType(buttonKind);
    }

    public static EValType serverValType2ClientValType(final EValType serverValType) {
        switch (serverValType) {
            case CLOB:
                return EValType.STR;
            case ARR_CLOB:
                return EValType.ARR_STR;
            case BLOB:
                return EValType.BIN;
            case ARR_BLOB:
                return EValType.ARR_BIN;
            default:
                return serverValType;
        }
    }

    public static Bin hexadecimalString2Bin(final String hexadecimal, final String splitter) {
        if (hexadecimal == null) {
            throw new IllegalArgumentError("Invalid Bin source ");
        }

        final ByteBuffer byteBuffer;
        if (splitter == null || splitter.isEmpty()) {
            if (hexadecimal.length() % 2 > 0) {
                throw new IllegalArgumentError("Invalid Bin source ");
            }
            byteBuffer = ByteBuffer.allocateDirect(hexadecimal.length() / 2);
            String b;
            for (int i = 0; i < hexadecimal.length(); i += 2) {
                b = hexadecimal.substring(i, i + 2);
                byteBuffer.put((byte) (int) Integer.valueOf(b, 16));
            }
        } else {
            String[] a = ClientValueFormatter.split(hexadecimal, splitter.charAt(0));
            byteBuffer = ByteBuffer.allocateDirect(a.length);
            for (String b : a) {
                if (b.length() == 2) {
                    byteBuffer.put((byte) (int) Integer.valueOf(b, 16));
                } else {
                    throw new IllegalArgumentError("Invalid Bin source ");
                }
            }
        }
        byteBuffer.rewind();//move current position to zero to be able read from buffer
        return new Bin(byteBuffer);
    }

    public static byte[] str2ArrByte(final String s, final String splitter) {
        return hexadecimalString2Bin(s, splitter).get();
    }

    public static char[] arrByte2arrChar(final byte[] arrByte, final String encoding) {
        //do not create String instance here to avoid of sensitive data caching
        final Charset charset = Charset.forName(encoding == null || encoding.isEmpty() ? "UTF-8" : encoding);
        final CharBuffer cb = charset.decode(ByteBuffer.wrap(arrByte));
        final char[] content = cb.array();
        final char[] result = new char[cb.limit()];
        System.arraycopy(content, cb.arrayOffset(), result, 0, cb.limit());
        Arrays.fill(content, ' ');
        return result;
    }

    public static BigDecimal parseBigDecimal(final String str, final NumberFormat format) throws NumberFormatException {
        if (str == null || str.isEmpty()) {
            return null;
        }
        if (format == null) {
            return new BigDecimal(str);
        }
        final char decimalDelimeter;
        final char minusCharacter;
        final char plusCharacter = '+';
        final Character triadDelimeter;
        if (format instanceof DecimalFormat) {
            final DecimalFormatSymbols symbols = ((DecimalFormat) format).getDecimalFormatSymbols();
            minusCharacter = symbols.getMinusSign();
            decimalDelimeter = symbols.getDecimalSeparator();
            if (format.isGroupingUsed()) {
                triadDelimeter = Character.valueOf(symbols.getGroupingSeparator());
            } else {
                triadDelimeter = null;
            }
        } else {
            minusCharacter = '-';
            decimalDelimeter = '.';
            triadDelimeter = null;
        }
        if (str.equals(".") || String.valueOf(decimalDelimeter).equals(str)) {
            return BigDecimal.ZERO;
        }
        final boolean forcedPositive = str.charAt(0) == plusCharacter;
        final boolean signSymbol = forcedPositive || str.charAt(0) == minusCharacter;
        int decimalDelimeterPos = str.lastIndexOf('.');
        if (decimalDelimeter != '.') {
            decimalDelimeterPos =
                    Math.max(decimalDelimeterPos, str.lastIndexOf(decimalDelimeter));
        }
        if (signSymbol && decimalDelimeterPos == 1 && str.length() == 2) {
            return BigDecimal.ZERO;
        }
        if (triadDelimeter == null) {
            return parseFormattedString(str, format, null, forcedPositive);
        } else {
            if (decimalDelimeterPos > -1 && decimalDelimeterPos < str.length() - 2) {
                final String fractionalPart = str.substring(decimalDelimeterPos + 1, str.length());
                final String stringToParse =
                        str.substring(0, decimalDelimeterPos + 1) + fractionalPart.replaceAll(String.valueOf(triadDelimeter), "");
                return parseFormattedString(stringToParse, format, triadDelimeter, forcedPositive);
            } else {
                return parseFormattedString(str, format, triadDelimeter, forcedPositive);
            }
        }
    }

    private static BigDecimal parseFormattedString(final String input, final NumberFormat format, final Character triadDelimeter, final boolean forcedPositive) throws NumberFormatException {
        final ParsePosition pos = new ParsePosition(0);
        final String stringToParse;
        if (forcedPositive) {
            stringToParse = input.substring(1);
        } else {
            stringToParse = input;
        }

        final Object valObj = format.parseObject(stringToParse, pos);
        if (pos.getIndex() == stringToParse.length() && (valObj instanceof BigDecimal)) {
            return (BigDecimal) valObj;
        } else {
            return new BigDecimal(stringToParse.replaceAll(String.valueOf(triadDelimeter), ""));
        }
    }
}
