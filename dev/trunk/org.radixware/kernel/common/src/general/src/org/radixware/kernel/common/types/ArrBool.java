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

import java.util.Collection;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EValType;

public final class ArrBool extends Arr<Boolean>{
	private static final long serialVersionUID = 545025619553664089L;

	public ArrBool(){
		super();
	}

	public ArrBool(final int initialCapacity){
		super(initialCapacity);
	}

	public ArrBool(final Boolean[] arr){
		super(arr);
	}

	public ArrBool(final Long[] arr){
		super(arrLong2arrBool(arr));
	}

	public ArrBool(final Collection<? extends Boolean> c){
		super(c);
	}
	
	public static final ArrBool fromValAsStr(final String valAsStr){
		if(valAsStr == null)
			return null;
		final ArrBool arr = new ArrBool();
		restoreArrFromValAsStr(arr, valAsStr,ITEM_VAL_TYPE, new ItemAsStrParser() {
			@Override
			public Object fromStr(String asStr) {
				return ValAsStr.fromStr(asStr, ITEM_VAL_TYPE);
			}
		});
		return arr;
	}
	
	private static Boolean[] arrLong2arrBool(final Long[] arrLong){
		if(arrLong == null)
			return null;
		final Boolean[] arrBool = new Boolean[arrLong.length];
		for(int i = 1; i < arrLong.length; i++){
			if (arrLong[i] == null)
				arrBool[i] = null;
			else
				arrBool[i] = (arrLong[i].longValue() == 0) ? Boolean.valueOf(false) : Boolean.valueOf(true);
		}	
		return arrBool;
	}
	

	public static final EValType ITEM_VAL_TYPE = EValType.BOOL;
	@Override
	public EValType getItemValType() {
		return ITEM_VAL_TYPE;
	}

}
