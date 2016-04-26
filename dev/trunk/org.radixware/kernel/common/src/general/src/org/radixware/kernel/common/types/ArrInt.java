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

public final class ArrInt extends Arr<Long> {
	private static final long serialVersionUID = -6916873930517497164L;

	public ArrInt(){
		super();
	}

	public ArrInt(final int initialCapacity){
		super(initialCapacity);
	}
	
	public ArrInt(final Long[] arr){
		super(arr);
	}

	public ArrInt(final Collection<? extends Long> c){
		super(c);
	}

	public static final EValType ITEM_VAL_TYPE = EValType.INT;
	@Override
	public EValType getItemValType() {
		return ITEM_VAL_TYPE;
	}
	
	public static final ArrInt fromValAsStr(final String valAsStr){
		if(valAsStr == null)
			return null;
		final ArrInt arr = new ArrInt();
		restoreArrFromValAsStr(arr, valAsStr,ITEM_VAL_TYPE, new ItemAsStrParser() {
			@Override
			public Object fromStr(String asStr) {
				return ValAsStr.fromStr(asStr, ITEM_VAL_TYPE);
			}
		});
		return arr;
	}
	
}

