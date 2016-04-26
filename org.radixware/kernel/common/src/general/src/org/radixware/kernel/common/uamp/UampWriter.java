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

package org.radixware.kernel.common.uamp;

import org.radixware.kernel.common.utils.FloraTime;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.types.ArrBin;
import org.radixware.kernel.common.types.ArrBool;
import org.radixware.kernel.common.types.ArrDateTime;
import org.radixware.kernel.common.types.ArrInt;
import org.radixware.kernel.common.types.ArrNum;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.types.Arr;
import org.radixware.kernel.common.types.Bin;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;

/**
 * Класс используется для создания новых UAMP сообщений
   
 */
public class UampWriter extends UampMessage {

	public UampWriter() {
		this(Defaults.DEFAULT_CHARSET);
	}

	public UampWriter(String charSetName) {
		super();
		charSet = charSetName;
		outputStream = new ByteArrayOutputStream();
	}

	public byte[] get() throws UampException {
		flushArray();
		return outputStream.toByteArray();
	}

	public void newMessage() throws UampException {
		flushArray();
		fieldNo = 0;
		addingFields.clear();
		outputStream.write(MS);
	}
	// -------------------------  Поддержка атомарных типов -------------------------  
	public void addBool(String name, Boolean value) throws UampException {
		addAtomic(name, value == null ? null : (value.equals(Boolean.TRUE) ? "1" : "0"));
	}

	public void addBool(String name, boolean value) throws UampException {
		addBool(name, Boolean.valueOf(value));
	}

	public void addLong(String name, Long value) throws UampException {
		addAtomic(name, value == null ? null : value.toString());
	}

	public void addLong(String name, long value) throws UampException {
		addLong(name, new Long(value));
	}

	public void addInteger(String name, Integer value) throws UampException {
		addAtomic(name, value == null ? null : value.toString());
	}

	public void addInteger(String name, int value) throws UampException {
		addInteger(name, new Integer(value));
	}

	public void addBigDecimal(String name, BigDecimal value) throws UampException {
		addAtomic(name, value == null ? null : value.toString());
	}

	public void addDouble(String name, Double value) throws UampException {
		addAtomic(name, value == null ? null : value.toString());
	}

	public void addDouble(String name, double value) throws UampException {
		addDouble(name, new Double(value));
	}

	public void addString(String name, String value) throws UampException {
		addAtomic(name, prepareString(value));
	}

	public void addChar(String name, Character value) throws UampException {
		String s = null;
		if (value != null) {
			s = new String(new char[]{value.charValue()});
		}
		addString(name, s);
	}

	public void addChar(String name, char value) throws UampException {
		addChar(name, new Character(value));
	}

	public void addDateTime(String name, Timestamp value) throws UampException {
		Double d = null;
		if (value != null) {
			d = new Double(FloraTime.getForValue(value).doubleValue());
		}
		addDouble(name, d);
	}

	public void addBytes(String name, byte[] value) throws UampException {
		addAtomic(name, prepareBytes(value));
	}

	public void addBin(String name, Bin value) throws UampException {
		addBytes(name, value == null ? null : value.get());
	}
/*
	public void addRaw(String name, oracle.sql.RAW value) throws UampException {
		addBytes(name, value == null ? null : value.getBytes());
	}

	public void addBlob(String name, java.sql.Blob value) throws UampException {
		try {
			addBytes(name, value == null ? null : value.getBytes(1, (int) value.length()));
		} catch (SQLException e) {
			throw new UampException("Unable to get BLOB data: " + ExceptionTextFormatter.getExceptionMess(e));
		}
	}

	public void addClob(String name, java.sql.Clob value) throws UampException {
		try {
			if (value.length() > Integer.MAX_VALUE) {
				throw new UampException("Can't add Clob value : Clob data size is too big");
			}
			addAtomic(name, value == null ? null : prepareString(value.getSubString(1, (int) value.length())));
		} catch (SQLException e) {
			throw new UampException("Unable to get CLOB data: " + ExceptionTextFormatter.getExceptionMess(e));
		}
	}
 */
	// -------------------------  Поддержка массивов -------------------------
	public void addArrBool(String name, Boolean[] value) throws UampException {
		addArray(name, value, false);
	}

	public void addArrBool(String name, ArrBool value) throws UampException {
		addArray(name, value, false);
	}

	public void addArrBool(String name) throws UampException {
		prepareToAddArray(name, "Boolean[]", false);
	}

	public void addArrItemBool(Boolean value) throws UampException {
		addArrayItem(value, "Boolean[]");
	}

	public void addArrItemBool(boolean value) throws UampException {
		addArrItemBool(Boolean.valueOf(value));
	}

	public void addArrLong(String name, ArrInt value) throws UampException {
		addArray(name, value, false);
	}

	public void addArrLong(String name, Long[] value) throws UampException {
		addArray(name, value, false);
	}

	public void addArrLong(String name) throws UampException {
		prepareToAddArray(name, "Long[]", false);
	}

	public void addArrItemLong(Long value) throws UampException {
		addArrayItem(value, "Long[]");
	}

	public void addArrItemLong(long value) throws UampException {
		addArrItemLong(new Long(value));
	}

	public void addArrBigDecimal(String name, ArrNum value) throws UampException {
		addArray(name, value, false);
	}

	public void addArrBigDecimal(String name, BigDecimal[] value) throws UampException {
		addArray(name, value, false);
	}

	public void addArrBigDecimal(String name) throws UampException {
		prepareToAddArray(name, "BigDecimal[]", false);
	}

	public void addArrItemBigDecimal(BigDecimal value) throws UampException {
		addArrayItem(value, "BigDecimal[]");
	}

	public void addArrItemDouble(double value) throws UampException {
		addArrItemBigDecimal(new BigDecimal(value));
	}

	public void addArrString(String name, ArrStr value) throws UampException {
		addArray(name, value, true);
	}

	public void addArrString(String name, String[] value) throws UampException {
		addArray(name, value, true);
	}

	public void addArrString(String name) throws UampException {
		prepareToAddArray(name, "String[]", true);
	}

	public void addArrItemString(String value) throws UampException {
		addArrayItem(value, "String[]");
	}

	public void addArrDateTime(String name, ArrDateTime value) throws UampException {
		BigDecimal[] dates = null;
		if (value != null) {
			dates = new BigDecimal[value.size()];
			for (int i = 0; i < value.size(); i++) {
				if (value.get(i) == null) {
					dates[i] = null;
				} else {
					dates[i] = new BigDecimal(String.valueOf(FloraTime.getForValue(value.get(i))));
				}
			}
		}
		addArrBigDecimal(name, dates);
	}

	public void addArrDateTime(String name, Timestamp[] value) throws UampException {
		BigDecimal[] dates = null;
		if (value != null) {
			dates = new BigDecimal[value.length];
			for (int i = 0; i < value.length; i++) {
				if (value[i] == null) {
					dates[i] = null;
				} else {
					dates[i] = new BigDecimal(String.valueOf(FloraTime.getForValue(value[i])));
				}
			}
		}
		addArrBigDecimal(name, dates);
	}

	public void addArrDateTime(String name) throws UampException {
		prepareToAddArray(name, "Date[]", false);
	}

	public void addArrItemDateTime(Timestamp value) throws UampException {
		Double d = null;
		if (value != null) {
			d = new Double(FloraTime.getForValue(value).doubleValue());
		}
		addArrayItem(d, "Date[]");
	}

	public void addArrBin(String name, ArrBin value) throws UampException {
		addArray(name, value, true);
	}

	public void addArrBin(String name, Bin[] value) throws UampException {
		addArray(name, value, true);
	}

	public void addArrBin(String name) throws UampException {
		prepareToAddArray(name, "Bin[]", true);
	}

	public void addArrItemBin(Bin value) throws UampException {
		addArrayItem(value, "Bin[]");
	}
/*
	public void addArrRaw(String name, oracle.sql.RAW[] value) throws UampException {
		addArray(name, value, true);
	}

	public void addArrRaw(String name) throws UampException {
		prepareToAddArray(name, "Raw[]", true);
	}
	public void addArrItemRaw(oracle.sql.RAW value) throws UampException {
		addArrayItem(value, "Raw[]");
	}

	public void addArrBlob(String name, ArrBlob value) throws UampException {
		addArray(name, value, true);
	}

	public void addArrBlob(String name, java.sql.Blob[] value) throws UampException {
		addArray(name, value, true);
	}

	public void addArrBlob(String name) throws UampException {
		prepareToAddArray(name, "BLOB[]", true);
	}
	public void addArrItemBlob(java.sql.Blob value) throws UampException {
		addArrayItem(value, "BLOB[]");
	}

	public void addArrClob(String name, ArrClob value) throws UampException {
		addArray(name, value, true);
	}

	public void addArrClob(String name, java.sql.Clob[] value) throws UampException {
		addArray(name, value, true);
	}

	public void addArrClob(String name) throws UampException {
		prepareToAddArray(name, "CLOB[]", true);
	}

	public void addArrItemClob(java.sql.Clob value) throws UampException {
		addArrayItem(value, "CLOB[]");
	}
*/
	private static final byte[] INSTEAD_00 = {SP, '0', '0'};
	private static final byte[] INSTEAD_0A = {SP, '0', 'A'};
	private static final byte[] INSTEAD_0D = {SP, '0', 'D'};

	private byte[] prepareString(String data) throws UampException {
		byte[] b;
		try {
			if (data == null) {
				b = null;
			} else if (data.length() == 0) {
				b = new byte[0];
			} else {
				b = data.getBytes(charSet);
			}
		} catch (UnsupportedEncodingException e) {
			throw new UampException("Unable to convert value, encoding \"" + charSet + "\"");
		}
		return prepareBytes(b);
	}

	private byte[] prepareBytes(byte[] data) throws UampException {
		final byte[] result;
		if (data == null) {
			result = null;
		} else {
			final ByteArrayOutputStream bo = new ByteArrayOutputStream(data.length * 3);
			try {
				byte b;
				for (int i = 0; i < data.length; i++) {
					b = data[i];
					switch (b) {
						case 0:
							bo.write(INSTEAD_00);
							break;
						case 0x0A:
							bo.write(INSTEAD_0A);
							break;
						case 0x0D:
							bo.write(INSTEAD_0D);
							break;
						case PS:
						case IS:
						case FS:
						case NI:
						case SP:
							bo.write(SP);
							bo.write(b);
							break;
						default:
							bo.write(b);
							break;
					}
				}
			} catch (IOException e) {
				throw new UampException("I/O error while preparing bytes: \"" + ExceptionTextFormatter.getExceptionMess(e) + "\"");
			}
			result = bo.toByteArray();
		}
		return result;
	}

	private void addAtomic(String name, String value) throws UampException {
		try {
			addAtomic(name, value == null ? null : value.getBytes(charSet));
		} catch (UnsupportedEncodingException e) {
			throw new UampException("Unable to convert value, encoding \"" + charSet + "\"");
		}
	}

	private void addAtomic(String name, byte[] value) throws UampException {
		beforeAddField(name, true);
		flushArray();
		if (value != null) {
			try {
				if (fieldNo != 0) {
					outputStream.write(PS);
				}
				outputStream.write(name.getBytes(charSet));
				outputStream.write(VS);
				if (value.length > 0) // пустая строка
				{
					outputStream.write(value);
				}
				fieldNo++;
			} catch (UnsupportedEncodingException e) {
				throw new UampException("Unable to convert field name, encoding \"" + charSet + "\"");
			} catch (IOException e) {
				throw new UampException("I/O error: \"" + ExceptionTextFormatter.getExceptionMess(e) + "\"");
			}
		}
	}

	private void prepareToAddArray(String name, String type, boolean useNI) throws UampException {
		flushArray();
		beforeAddField(name, false); // rememberName == false так как поле запоминается в addAtomic     
		tmpArrayName = name;
		tmpArrayType = type;
		tmpArrayUseNI = useNI;
	}

	private void writeArrayItem(OutputStream outStream, Object itemVal, boolean useNI) throws UampException {
		try {
			if (itemVal == null) {
				if (useNI) {
					outStream.write(NI);
				}
			} else {
				if (itemVal instanceof Boolean) {
					boolean b = ((Boolean) itemVal).booleanValue();
					outStream.write(b ? '1' : '0');
				} else if (itemVal instanceof Bin) {
					outStream.write(prepareBytes(((Bin) itemVal).get()));
				} else if (itemVal instanceof Clob) {
					Clob x = ((Clob) itemVal);
					outStream.write(prepareBytes(x.getSubString(1, (int) x.length()).getBytes()));
				} else if (itemVal instanceof Blob) {
					Blob x = ((Blob) itemVal);
					outStream.write(prepareBytes(x.getBytes(1, (int) x.length())));
				//} else if (itemVal instanceof RAW) {
				//	outStream.write(prepareBytes(((RAW) itemVal).getBytes()));
				} else {
					outStream.write(prepareString(itemVal.toString()));
				}
			}
		} catch (IOException e) {
			throw new UampException("I/O error: \"" + ExceptionTextFormatter.getExceptionMess(e) + "\"");
		} catch (SQLException e) {
			throw new UampException("SQL error: \"" + ExceptionTextFormatter.getExceptionMess(e) + "\"");
		}
	}

	private void addArray(String name, Object[] values, boolean useNI) throws UampException {
		if (values != null && values.length > 0) {
			final ByteArrayOutputStream bo = new ByteArrayOutputStream();
			for (int i = 0; i < values.length; i++) {
				if (i > 0) {
					bo.write(IS);
				}
				writeArrayItem(bo, values[i], useNI);
			}
			addAtomic(name, bo.toByteArray());
		}
	}

	private void addArray(String name, Arr values, boolean useNI) throws UampException {
		if (values != null && values.size() > 0) {
			final ByteArrayOutputStream bo = new ByteArrayOutputStream();
			for (int i = 0; i < values.size(); i++) {
				if (i > 0) {
					bo.write(IS);
				}
				writeArrayItem(bo, values.get(i), useNI);
			}
			addAtomic(name, bo.toByteArray());
		}
	}

	private void addArrayItem(Object value, String type) throws UampException {
		if (tmpArrayType == null) {
			throw new UampException("Current field not array, use \"addArrXXX\" first");
		}
		if (!tmpArrayType.equals(type)) {
			throw new UampException("Inconsistence data types: current array type - \"" + tmpArrayType + "\", array type for adding item - \"" + type + "\"");
		}
		tmpArrayData.add(value);
	}

	private void flushArray() throws UampException {
		if (tmpArrayName != null) {
			String tmp = tmpArrayName;  // предотвращаем зацикливание 
			tmpArrayName = null;
			addArray(tmp, tmpArrayData.toArray(), tmpArrayUseNI);
			tmpArrayName = null;
			tmpArrayType = null;
			tmpArrayData.clear();
			tmpArrayUseNI = false;
		}
	}

	private void beforeAddField(String name, boolean rememberName) throws UampException {
		if (addingFields.indexOf(name) != -1) {
			throw new UampException("Field \"" + name + "\" already exists in message");
		} else if (rememberName) {
			addingFields.add(name);
		}
	}
	private ByteArrayOutputStream outputStream = null;
	private int fieldNo = 0;
	private List<String> addingFields = new ArrayList<String>();
	private List<Object> tmpArrayData = new ArrayList<Object>();
	private String tmpArrayName = null;
	private boolean tmpArrayUseNI = false;
	private String tmpArrayType = null;
}
