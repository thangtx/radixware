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

package org.radixware.kernel.common.types;

import java.sql.Timestamp;

import java.util.Collection;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EValType;

public final class ArrDateTime extends Arr<java.sql.Timestamp> {
	private static final long serialVersionUID = 3110810688303419021L;

	public ArrDateTime(){
		super();
	}

	public ArrDateTime(final int initialCapacity){
		super(initialCapacity);
	}
	
	public ArrDateTime(final Long[] arrDateTimeInFloraFormat){
		super(arrLong2arrTime(arrDateTimeInFloraFormat));
	}

	public ArrDateTime(final java.sql.Timestamp[] arr){
		super(arr);
	}

	public ArrDateTime(final Collection<? extends java.sql.Timestamp> c) {
		super(c);
	}

	public static final ArrDateTime fromValAsStr(final String valAsStr){
		if(valAsStr == null)
			return null;
		final ArrDateTime arr = new ArrDateTime();
		restoreArrFromValAsStr(arr, valAsStr,ITEM_VAL_TYPE, new ItemAsStrParser() {
			@Override
			public Object fromStr(String asStr) {
				return ValAsStr.fromStr(asStr, ITEM_VAL_TYPE);
			}
		});
		return arr;
	}
	
	private static Timestamp[] arrLong2arrTime(final Long[] arrLong){
		if(arrLong == null)
			return null;
		final Timestamp[] arrTime = new Timestamp[arrLong.length];
		for(int i = 0; i < arrLong.length; i++)
			if(arrLong[i] == null)
				arrTime[i] = null;
			else
				arrTime[i] = new Timestamp(arrLong[i].longValue());
		return arrTime;
	}

	public static final EValType ITEM_VAL_TYPE = EValType.DATE_TIME;
	@Override
	public EValType getItemValType() {
		return ITEM_VAL_TYPE;
	}
}

