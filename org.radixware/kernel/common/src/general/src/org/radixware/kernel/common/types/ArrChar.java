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

public final class ArrChar extends  Arr<Character> {
	private static final long serialVersionUID = 995081791221465359L;

	public ArrChar(){
		super();
	}

	public ArrChar(final int initialCapacity){
		super(initialCapacity);
	}

	public ArrChar(final Character[] arr){
		super(arr);
	}

	public ArrChar(final Collection<? extends Character> c){
		super(c);
	}

	public ArrChar(final String[] arr){
		super(arrStr2arrChar(arr));
	}
	
	public static final ArrChar fromValAsStr( final String valAsStr){
		if(valAsStr == null)
			return null;
		final ArrChar arr = new ArrChar();
		restoreArrFromValAsStr(arr, valAsStr,ITEM_VAL_TYPE, new ItemAsStrParser() {
			@Override
			public Object fromStr(String asStr) {
				return ValAsStr.fromStr(asStr, ITEM_VAL_TYPE);
			}
		});
		return arr;
	}
	
	private static Character[] arrStr2arrChar(final String[] arrStr){
		if(arrStr == null)
			return null;
		final Character[] arrChar = new Character[arrStr.length];
		for(int i = 1; i < arrStr.length; i++)
			if(arrStr[i] == null || arrStr[i].length() == 0)
				arrChar[i] = null;
			else
				arrChar[i] = new Character(arrStr[i].charAt(0));
		return arrChar;
	}

	public static final EValType ITEM_VAL_TYPE = EValType.CHAR;
	@Override
	public EValType getItemValType() {
		return ITEM_VAL_TYPE;
	}
}

