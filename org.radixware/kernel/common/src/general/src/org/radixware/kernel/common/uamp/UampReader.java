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
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.radixware.kernel.common.types.Bin;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.Hex;

/**
 * Класс испльзуется для разбора готовых UAMP - сообщений

 */
public class UampReader extends UampMessage {

	public UampReader(byte[] mess) throws UampException {
		this(mess, Defaults.DEFAULT_CHARSET);
	}

	public UampReader(byte[] mess, String charSetName) throws UampException {
		charSet = charSetName;
		inputData = mess;
		preliminaryParse();
	}

	public String[] getFieldNames() {
		String[] result = new String[fieldsLocation.size()];
		int j = 0;
		for (Iterator<String> i = fieldsLocation.keySet().iterator(); i.hasNext();) {
			result[j++] = i.next();
		}
		return result;
	}

	public boolean isFieldExists(String fieldName) {
		return fieldsLocation.get(fieldName) != null;
	}

	public boolean notEmpty() {
		return fieldsLocation.size() > 0;
	}

	public void moveToNext() throws UampException {
		preliminaryParse();
	}
	// ------------------------- Поддержка атомарных типов ------------------------- 
	public Boolean getBool(String name) throws UampException {
		return parseBool(deprepareBytes(name));
	}

	public Long getLong(String name) throws UampException {
		return parseLong(deprepareBytes(name));
	}

	public BigDecimal getBigDecimal(String name) throws UampException {
		return parseBigDecimal(deprepareBytes(name));
	}

	public String getString(String name) throws UampException {
		return parseString(deprepareBytes(name));
	}

	public Timestamp getDateTime(String name) throws UampException {
		return parseDateTime(deprepareBytes(name));
	}

	public Bin getBin(String name) throws UampException {
		return parseBin(deprepareBytes(name));
	}
/*
	public oracle.sql.RAW getRaw(String name) throws UampException {
		return parseRaw(deprepareBytes(name));
	}

	public java.sql.Blob getBlob(final Arte arte, String name) throws UampException {
		return parseBlob(arte, deprepareBytes(name));
	}

	public java.sql.Clob getClob(final Arte arte, String name) throws UampException {
		return parseClob(arte, deprepareBytes(name));
	}
 */
	// ------------------------- Поддержка массивов -------------------------   
	public Boolean[] getArrBool(String name) throws UampException {
		return (Boolean[]) parseArray(name,
				new BytesParser() {

			@Override
					public Object parse(byte[] bytes) throws UampException {
						return parseBool(bytes);
					}

			@Override
					public Object[] allocateArray(int size) {
						return new Boolean[size];
					}
				});
	}

	public Boolean getArrItemBool(String name, int idx) throws UampException {
		return (Boolean) parseArrayItem(name,
				new ArrGetter() {

			@Override
					public Object[] get(String arrName) throws UampException {
						return getArrBool(arrName);
					}
				}, idx);
	}

	public Long[] getArrLong(String name) throws UampException {
		return (Long[]) parseArray(name,
				new BytesParser() {

			@Override
					public Object parse(byte[] bytes) throws UampException {
						return parseLong(bytes);
					}

			@Override
					public Object[] allocateArray(int size) {
						return new Long[size];
					}
				});
	}

	public Long getArrItemLong(String name, int idx) throws UampException {
		return (Long) parseArrayItem(name,
				new ArrGetter() {

			@Override
					public Object[] get(String arrName) throws UampException {
						return getArrLong(arrName);
					}
				}, idx);
	}

	public BigDecimal[] getArrBigDecimal(String name) throws UampException {
		return (BigDecimal[]) parseArray(name,
				new BytesParser() {

			@Override
					public Object parse(byte[] bytes) throws UampException {
						return parseBigDecimal(bytes);
					}

			@Override
					public Object[] allocateArray(int size) {
						return new BigDecimal[size];
					}
				});
	}

	public BigDecimal getArrItemBigDecimal(String name, int idx) throws UampException {
		return (BigDecimal) parseArrayItem(name,
				new ArrGetter() {

			@Override
					public Object[] get(String arrName) throws UampException {
						return getArrBigDecimal(arrName);
					}
				}, idx);
	}

	public String[] getArrString(String name) throws UampException {
		return (String[]) parseArray(name,
				new BytesParser() {

			@Override
					public Object parse(byte[] bytes) throws UampException {
						return parseString(bytes);
					}

			@Override
					public Object[] allocateArray(int size) {
						return new String[size];
					}
				});
	}

	public String getArrItemString(String name, int idx) throws UampException {
		return (String) parseArrayItem(name,
				new ArrGetter() {

			@Override
					public Object[] get(String arrName) throws UampException {
						return getArrString(arrName);
					}
				}, idx);
	}

	public Timestamp[] getArrDateTime(String name) throws UampException {
		return (Timestamp[]) parseArray(name,
				new BytesParser() {

			@Override
					public Object parse(byte[] bytes) throws UampException {
						return parseDateTime(bytes);
					}

			@Override
					public Object[] allocateArray(int size) {
						return new Timestamp[size];
					}
				});
	}

	public Timestamp getArrItemDateTime(String name, int idx) throws UampException {
		return (Timestamp) parseArrayItem(name,
				new ArrGetter() {

			@Override
					public Object[] get(String arrName) throws UampException {
						return getArrDateTime(arrName);
					}
				}, idx);
	}

	public Bin[] getArrBin(String name) throws UampException {
		return (Bin[]) parseArray(name,
				new BytesParser() {

			@Override
					public Object parse(byte[] bytes) {
						return parseBin(bytes);
					}

			@Override
					public Object[] allocateArray(int size) {
						return new Bin[size];
					}
				});
	}

	public Bin getArrItemBin(String name, int idx) throws UampException {
		return (Bin) parseArrayItem(name,
				new ArrGetter() {

			@Override
					public Object[] get(String arrName) throws UampException {
						return getArrBin(arrName);
					}
				}, idx);
	}
/*
	public oracle.sql.RAW[] getArrRaw(String name) throws UampException {
		return (oracle.sql.RAW[]) parseArray(name,
				new BytesParser() {

					public Object parse(byte[] bytes) {
						return parseRaw(bytes);
					}

					public Object[] allocateArray(int size) {
						return new oracle.sql.RAW[size];
					}
				});
	}

	public oracle.sql.RAW getArrItemRaw(String name, int idx) throws UampException {
		return (oracle.sql.RAW) parseArrayItem(name,
				new ArrGetter() {

					public Object[] get(String arrName) throws UampException {
						return getArrRaw(arrName);
					}
				}, idx);
	}

	public java.sql.Blob[] getArrBlob(final Arte arte, String name) throws UampException {
		return (java.sql.Blob[]) parseArray(name,
				new BytesParser() {

					public Object parse(byte[] bytes) throws UampException {
						return parseBlob(arte, bytes);
					}

					public Object[] allocateArray(int size) {
						return new java.sql.Blob[size];
					}
				});

	}

	public java.sql.Blob getArrItemBlob(final Arte arte, String name, int idx) throws UampException {
		return (java.sql.Blob) parseArrayItem(name,
				new ArrGetter() {

					public Object[] get(String arrName) throws UampException {
						return getArrBlob(arte, arrName);
					}
				}, idx);
	}

	public java.sql.Clob[] getArrClob(final Arte arte, final String name) throws UampException {
		return (java.sql.Clob[]) parseArray(name,
				new BytesParser() {

					public Object parse(byte[] bytes) throws UampException {
						return parseClob(arte, bytes);
					}

					public Object[] allocateArray(int size) {
						return new java.sql.Clob[size];
					}
				});
	}

	public java.sql.Clob getArrItemClob(final Arte arte, String name, int idx) throws UampException {
		return (java.sql.Clob) parseArrayItem(name,
				new ArrGetter() {

					public Object[] get(String arrName) throws UampException {
						return getArrClob(arte, arrName);
					}
				}, idx);
	}
*/
	public int getArrItemCount(String name) {
		int result = 0;
		FieldLocation l = fieldsLocation.get(name);
		if (l != null) {
			result = l.getItemLens().size();
		}
		return result;
	}
	// Для внутреннего использования
	private Boolean parseBool(byte[] bytes) throws UampException {
		Boolean result = null;
		if (bytes != null && bytes.length != 0) {
			if (bytes.length != 1 || (bytes[0] != '1' && bytes[0] != '0')) {
				throw new UampException("Unable to parse boolean");
			}
			if (bytes[0] == '1') {
				result = Boolean.TRUE;
			} else if (bytes[0] == '0') {
				result = Boolean.FALSE;
			}
		}
		return result;
	}

	private Long parseLong(byte[] bytes) throws UampException {
		Long result = null;
		if (bytes != null && bytes.length != 0) {
			try {
				result = new Long(Long.parseLong(new String(bytes)));
			} catch (NumberFormatException e) {
				throw new UampException("Unable to parse long");
			}
		}
		return result;
	}

	private BigDecimal parseBigDecimal(byte[] bytes) throws UampException {
		BigDecimal result = null;
		if (bytes != null && bytes.length != 0) {
			try {
				result = new BigDecimal(new String(bytes));
			} catch (NumberFormatException e) {
				throw new UampException("Unable to parse big decimal");
			}
		}
		return result;
	}

	private String parseString(byte[] bytes) throws UampException {
		String result = null;
		if (bytes != null) {
			if (bytes.length == 0) {
				result = "";
			} else {
				try {
					result = new String(bytes, charSet);
				} catch (UnsupportedEncodingException e) {
					throw new UampException("Unable to paser string, unsupported encoding \"" + charSet + "\"");
				}
			}
		}
		return result;
	}

	private Timestamp parseDateTime(byte[] bytes) throws UampException {
		BigDecimal l = parseBigDecimal(bytes);
		Timestamp result = null;
		if (l != null && bytes.length != 0) {
			result = FloraTime.toTimestamp(l);
		}
		return result;
	}

	private Bin parseBin(byte[] bytes) {
		Bin result = null;
		if (bytes != null) {
			result = Bin.wrap(bytes);
		}
		return result;
	}
/*
	private oracle.sql.RAW parseRaw(byte[] bytes) {
		oracle.sql.RAW result = null;
		if (bytes != null) {
			result = new oracle.sql.RAW(bytes);
		}
		return result;
	}

	private java.sql.Blob parseBlob(final Arte arte, final byte[] bytes) throws UampException {
		java.sql.Blob result = null;
		try {
			result = arte.getDbConnection().createTemporaryBlob();
			result.setBytes(1, bytes);
		} catch (SQLException e) {
			throw new UampException("Uanble to create BLOB: " + ExceptionTextFormatter.getExceptionMess(e));
		}
		return result;
	}

	private java.sql.Clob parseClob(final Arte arte, final byte[] bytes) throws UampException {
		java.sql.Clob result = null;
		try {
			result = arte.getDbConnection().createTemporaryClob();
			result.setString(1, new String(bytes, charSet));
		} catch (SQLException e) {
			throw new UampException("Uanble to create CLOB: " + ExceptionTextFormatter.getExceptionMess(e));
		} catch (UnsupportedEncodingException e) {
			throw new UampException("Unable to paser string, unsupported encoding \"" + charSet + "\"");
		}
		return result;
	}
*/
	public Object[] parseArray(String name, BytesParser parser) throws UampException {
		Object[] result = null;
		FieldLocation arrLocation = fieldsLocation.get(name);
		byte[] tmp;
		if (arrLocation != null) {
			final FieldLocation curItem = new FieldLocation();
			final List<Integer> itemLens = arrLocation.getItemLens();
			result = parser.allocateArray(itemLens.size());
			int offs = 0, curLen;
			for (int i = 0; i < itemLens.size(); i++) {
				curItem.setPos(arrLocation.getPos() + offs);
				curLen = itemLens.get(i).intValue();
				curItem.setLen(curLen);
				tmp = deprepareBytes(curItem);
				if (tmp != null && tmp.length == 1 && tmp[ 0] == NI) {
					result[i] = null;
				} else {
					result[i] = parser.parse(tmp);
				}
				offs += curLen + 1;
			}
		}
		return result;
	}

	private Object parseArrayItem(String name, ArrGetter getter, int idx) throws UampException {
		Object[] arr = parsedArrays.get(name);
		if (arr == null) {
			arr = getter.get(name);
			parsedArrays.put(name, arr);
		}
		if (arr == null) {
			throw new UampException("Array \"" + name + "\" is empty");
		} else if (idx < 0 || idx >= arr.length) {
			throw new UampException("Index " + idx + " is out of bounds, array size: " + arr.length);
		}
		return arr[idx];
	}

	private void preliminaryParse() throws UampException {
		parsedArrays.clear();
		fieldsLocation.clear();
		List<Integer> itemLens;
		byte prevByte, curByte = 0;
		String fieldName;
		int pos = startPos - 1, nextPos, prevItemPos;
		while (pos < inputData.length) {
			pos++;
			nextPos = findByte(VS, pos);
			if (nextPos == -1 || nextPos == pos) {
				throw new UampException("Invalid UAMP message - parse failed on position " + pos);
			}
			try {
				fieldName = new String(inputData, pos, nextPos - pos, charSet);
			} catch (UnsupportedEncodingException e) {
				throw new UampException("Unable to convert field name, encoding \"" + charSet + "\"");
			}
			prevItemPos = nextPos + 1;
			pos = nextPos + 1;
			prevByte = VS;
			itemLens = new ArrayList<Integer>();
			while (pos < inputData.length) {
				curByte = inputData[pos];
				if ((curByte == PS || curByte == MS) && prevByte != SP) {
					break;
				}
				if (curByte == IS && prevByte != SP) {
					itemLens.add(new Integer(pos - prevItemPos));
					prevItemPos = pos + 1;
				}
				prevByte = curByte;
				pos++;
			}
			itemLens.add(new Integer(pos - prevItemPos));
			fieldsLocation.put(fieldName, new FieldLocation(nextPos + 1, pos - nextPos - 1, itemLens));
			if (curByte == MS) {
				break;
			}
		}
		startPos = pos + 1;
	}

	private byte[] deprepareBytes(String name) throws UampException {
		return deprepareBytes(fieldsLocation.get(name));
	}

	private byte[] deprepareBytes(FieldLocation l) throws UampException {
		byte[] result = null;
		if (l != null) {
			if (l.getLen() == 0) {
				result = new byte[0];
			} else {
				ByteArrayOutputStream bo = new ByteArrayOutputStream(l.getLen());
				byte b, b2, b3;
				int i = l.getPos();
				while (i - l.getPos() < l.getLen()) {
					b = inputData[i];
					if (b == SP) {
						if (i + 1 < inputData.length) {
							b2 = inputData[i + 1];
							if (Hex.isHexDigit(b2)) {
								if (i + 2 < inputData.length && Hex.isHexDigit(b3 = inputData[i + 2])) {
									try {
										bo.write(Hex.decode(new String(new byte[]{b2, b3})));
										i += 3;
									} catch (IOException e) {
										throw new UampException("I/O error while preparing bytes: \"" + ExceptionTextFormatter.getExceptionMess(e) + "\"");
									}
								} else {
									throw new UampException("Invalid UAMP message");
								}
							} else {
								bo.write(b2);
								i += 2;
							}
						} else {
							throw new UampException("Invalid UAMP message");
						}
					} else {
						bo.write(b);
						i++;
					}
				}
				result = bo.toByteArray();
			}
		}
		return result;
	}

	private int findByte(byte b, int offset) {
		int result = -1;
		for (int i = offset; i < inputData.length; i++) {
			if (inputData[i] == MS) {
				break;
			}
			if (inputData[i] == b) {
				result = i;
				break;
			}
		}
		return result;
	}

	public interface BytesParser {

		public Object parse(byte[] bytes) throws UampException;

		public Object[] allocateArray(int size);
	}

	private interface ArrGetter {

		public Object[] get(String name) throws UampException;
	}

	private static class FieldLocation {

		private int pos,  len;
		private List<Integer> itemLens;

		public FieldLocation(final int pos, final int len, final List<Integer> itemLens) {
			this.pos = pos;
			this.len = len;
			this.itemLens = itemLens;
		}

		public FieldLocation() {
			this.pos = 0;
			this.len = 0;
		}

		public final int getLen() {
			return len;
		}

		public final int getPos() {
			return pos;
		}

		public final void setLen(int len) {
			this.len = len;
		}

		public final void setPos(int pos) {
			this.pos = pos;
		}

		public final List<Integer> getItemLens() {
			return itemLens;
		}
	}
	private byte[] inputData;
	private int startPos;
	private HashMap<String, FieldLocation> fieldsLocation = new HashMap<String, FieldLocation>();
	private HashMap<String, Object[]> parsedArrays = new HashMap<String, Object[]>();
}
