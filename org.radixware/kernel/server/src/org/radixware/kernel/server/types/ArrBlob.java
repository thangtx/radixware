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
import java.sql.Blob;

import java.util.Collection;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.utils.SrvValAsStr;

public final class ArrBlob extends Arr<Blob> {

	private static final long serialVersionUID = -3765852335158736918L;
	private final Arte arte;

	public ArrBlob(final Arte arte) {
		super();
		this.arte = arte;
	}

	public ArrBlob(final Arte arte, final int initialCapacity) {
		super(initialCapacity);
		this.arte = arte;
	}

	public ArrBlob(final Arte arte, final Blob[] arr) {
		super(arr);
		this.arte = arte;
	}

	public ArrBlob(final Arte arte, final Collection<? extends Blob> c) {
		super(c);
		this.arte = arte;
	}

	public static final ArrBlob fromValAsStr(final Arte arte, final String valAsStr) {
		if (valAsStr == null) {
			return null;
		}
		final ArrBlob arr = new ArrBlob(arte);
		restoreArrFromValAsStr(arr, valAsStr, ITEM_VAL_TYPE, new ItemAsStrParser() {

			@Override
			public Object fromStr(final String asStr) {
				return SrvValAsStr.fromStr(arte, asStr, ITEM_VAL_TYPE);
			}
		});
		return arr;
	}
	public static final EValType ITEM_VAL_TYPE = EValType.BLOB;

	@Override
	public EValType getItemValType() {
		return ITEM_VAL_TYPE;
	}

	@Override
	protected String getAsStr(final int i) {
		return SrvValAsStr.toStr(arte, get(i), ITEM_VAL_TYPE);
	}
}
