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

package org.radixware.kernel.server.types;

import org.radixware.kernel.common.types.*;
import java.sql.Clob;

import java.util.Collection;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.utils.SrvValAsStr;

public final class ArrClob extends Arr<Clob> {

	private static final long serialVersionUID = 1428217457114837278L;
	private final Arte arte;

	public ArrClob(final Arte arte) {
		super();
		this.arte = arte;
	}

	public ArrClob(final Arte arte, final int initialCapacity) {
		super(initialCapacity);
		this.arte = arte;
	}

	public ArrClob(final Arte arte, final Clob[] arr) {
		super(arr);
		this.arte = arte;
	}

	public ArrClob(final Arte arte, final Collection<? extends Clob> c) {
		super(c);
		this.arte = arte;
	}

	public static final ArrClob fromValAsStr(final Arte arte, final String valAsStr) {
		if (valAsStr == null) {
			return null;
		}
		final ArrClob arr = new ArrClob(arte);
		restoreArrFromValAsStr(arr, valAsStr, ITEM_VAL_TYPE, new ItemAsStrParser() {

			@Override
			public Object fromStr(final String asStr) {
				return SrvValAsStr.fromStr(arte, asStr, ITEM_VAL_TYPE);
			}
		});
		return arr;
	}
	public static final EValType ITEM_VAL_TYPE = EValType.CLOB;

	@Override
	public EValType getItemValType() {
		return ITEM_VAL_TYPE;
	}

	@Override
	protected String getAsStr(final int i) {
		return SrvValAsStr.toStr(arte, get(i), ITEM_VAL_TYPE);
	}
}
