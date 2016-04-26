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

package org.radixware.kernel.server.utils;

import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.exceptions.WrongFormatError;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Base64;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.exceptions.DatabaseError;
import org.radixware.kernel.server.types.ArrBlob;
import org.radixware.kernel.server.types.ArrClob;
import org.radixware.kernel.server.types.ArrEntity;
import org.radixware.kernel.server.types.Entity;
import org.radixware.kernel.server.types.Pid;

/**
 * Value in "Server Value As String" format.
 * Used for the Server object values.
 * Supports any of {@link EValType}.
 * Pattern: Immutable.
 */
public class SrvValAsStr extends ValAsStr {

	private final Arte arte;

	protected SrvValAsStr(final Arte arte, final String valAsStr) {
		super(valAsStr);
		this.arte = arte;
	}

	public static final class Factory {

		private Factory() {
		}

		/**
		 * Restore ValAsStr from string.
		 * @return new instance of ValAsStr or null is valAsStr is null or empty.
		 */
		public static ValAsStr loadFrom(final Arte arte, final String valAsStr) {
			if (valAsStr == null || valAsStr.isEmpty()) {
				return null;
			}
			return new SrvValAsStr(arte, valAsStr);
		}

		/**
		 * Restore ValAsStr from object
		 * @return new instance of ValAsStr or null is object is null.
		 */
		public static ValAsStr newInstance(final Arte arte, final Object obj, final EValType valType) {
			if (obj == null) {
				return null;
			}
			switch (valType) {
				case CLOB: {
					final Clob tmp = (java.sql.Clob) obj;
					final int len;
					try {
						if (tmp.length() > Integer.MAX_VALUE) {
							throw new WrongFormatError("Can't convert Clob value to string: Clob data size is too big", null);
						}
						len = (int) tmp.length();
						return new SrvValAsStr(arte, tmp.getSubString(1, len));
					} catch (SQLException e) {
						throw new DatabaseError("Can't convert Clob value to string: " + ExceptionTextFormatter.getExceptionMess(e), e);
					}
				}
				case BLOB: {
					final Blob tmp = (java.sql.Blob) obj;
					final int len;
					try {
						len = (int) tmp.length();
						if (len != tmp.length()) {
							throw new WrongFormatError("Can't convert Blob value to string: Blob data size is too big", null);
						}
						return new SrvValAsStr(arte, Base64.encode(tmp.getBytes(1, len)));
					} catch (SQLException e) {
						throw new DatabaseError("Can't convert Blob value to string: " + ExceptionTextFormatter.getExceptionMess(e), e);
					}
				}
				case OBJECT:
				case PARENT_REF: {
					final Pid pid;
					if (obj instanceof Entity) {
						pid = ((Entity) obj).getPid();
					} else {
						pid = (Pid) obj;
					}
					return new SrvValAsStr(arte, pid.getEntityId() + "\n" + pid.toString());
				}
				case ARR_REF:
				case ARR_CLOB:
				case ARR_BLOB:
					return new SrvValAsStr(arte, String.valueOf(obj));
                default:
        			return ValAsStr.Factory.newInstance(obj, valType);
			}
		}
	}

	public static Object fromStr(final Arte arte, final String valAsStr, final EValType valType) {
		return fromStr(SrvValAsStr.Factory.loadFrom(arte, valAsStr), valType);
	}

	public static Object fromStr(final ValAsStr valAsStr, final EValType valType) {
		if (valAsStr == null) {
			return null;
		}
		return valAsStr.toObject(valType);
	}

	public static String toStr(final Arte arte, final Object obj, final EValType valType) {
		final ValAsStr v = SrvValAsStr.Factory.newInstance(arte, obj, valType);
		if (v == null) {
			return null;
		}
		return v.toString();
	}

	private Arte getArte() {
		if (!arte.isInTransaction()) {
			throw new IllegalUsageError("SrvValAsStr can be restored during ARTE seance only");
		}
		return arte;
	}

	@Override
	public Object toObject(final EValType valType) {
		if (valAsStr == null || valAsStr.isEmpty()) {
			return null;
		}
		switch (valType) {
			case CLOB:
				try {
					final Clob valAsObj = getArte().getDbConnection().createTemporaryClob();
					valAsObj.setString(1, valAsStr);
					return valAsObj;
				} catch (SQLException e) {
					throw new DatabaseError("Can't restore Clob value from string: " + ExceptionTextFormatter.getExceptionMess(e), e);
				}
			case BLOB:
				try {
					final Blob valAsObj = getArte().getDbConnection().createTemporaryBlob();
					valAsObj.setBytes(1, Base64.decode(valAsStr));
					return valAsObj;
				} catch (SQLException e) {
					throw new DatabaseError("Can't restore Blob value from string: " + ExceptionTextFormatter.getExceptionMess(e), e);
				}
			case OBJECT:
			case PARENT_REF: {
				final int pos = valAsStr.lastIndexOf('\n');
				if (pos == -1 || pos >= valAsStr.length()) {
					throw new WrongFormatError("Can't restore object PID from string", null);
				}
				return new Pid(getArte(), Id.Factory.loadFrom(valAsStr.substring(0, pos)), valAsStr.substring(pos + 1));
			}
			case ARR_REF:
				return ArrEntity.fromValAsStr(getArte(), valAsStr);
			case ARR_CLOB:
				return ArrClob.fromValAsStr(getArte(), valAsStr);
			case ARR_BLOB:
				return ArrBlob.fromValAsStr(getArte(), valAsStr);
            default:
        		return super.toObject(valType);
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SrvValAsStr){
			return super.equals(obj) && (arte == ((SrvValAsStr)obj).arte);
		}
		return false ;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 79 * hash + (this.arte != null ? this.arte.hashCode() : 0);
		return hash;
	}


}
