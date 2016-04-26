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

public final class ArrBin extends Arr<Bin>{
	public final static EValType ITEM_VAL_TYPE = EValType.BIN;

	private static final long serialVersionUID = -3082490002437906913L;


	public ArrBin(){
		super();
	}

	public ArrBin(final int initialCapacity){
		super(initialCapacity);
	}

	public ArrBin(final Bin[] arr){
		super(arr);
	}

	public ArrBin(final Collection<? extends Bin> c){
		super(c);
	}

	public static final ArrBin fromValAsStr(final String valAsStr){
		if(valAsStr == null)
			return null;
		final ArrBin arr = new ArrBin();
		restoreArrFromValAsStr(arr, valAsStr,ITEM_VAL_TYPE, new ItemAsStrParser() {
			@Override
			public Object fromStr(String asStr) {
				return ValAsStr.fromStr(asStr, ITEM_VAL_TYPE);
			}
		});
		return arr;
	}

	@Override
	public EValType getItemValType() {
		return ITEM_VAL_TYPE;
	}

}
