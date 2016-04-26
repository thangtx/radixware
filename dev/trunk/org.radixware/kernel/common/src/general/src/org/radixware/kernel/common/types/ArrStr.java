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

public final class ArrStr extends Arr<String> {
	private static final long serialVersionUID = 5719144392511191230L;
	public ArrStr(){
		super();
	}
	public ArrStr(final int initialCapacity){
		super(initialCapacity);
	}
	public ArrStr(final String[] arr){
		super(arr);
	}
	public ArrStr(final String item1){
		super(1);
		add(item1);
	}
	public ArrStr(final String item1, final String item2){
		super(2);
		add(item1);
		add(item2);
	}
	public ArrStr(final String item1, final String item2, final String item3){
		super(3);
		add(item1);
		add(item2);
		add(item3);
	}
	public ArrStr(final String item1, final String item2, final String item3, final String item4){
		super(4);
		add(item1);
		add(item2);
		add(item3);
		add(item4);
	}
	public ArrStr(final String item1, final String item2, final String item3, final String item4, final String item5){
		super(5);
		add(item1);
		add(item2);
		add(item3);
		add(item4);
		add(item5);
	}
	public ArrStr(final String item1, final String item2, final String item3, final String item4, final String item5, final String item6){
		super(6);
		add(item1);
		add(item2);
		add(item3);
		add(item4);
		add(item5);
		add(item6);
	}
	public ArrStr(final Collection<? extends String> c){
		super(c);
	}

	public static final ArrStr fromValAsStr(final String valAsStr){
		if(valAsStr == null) {
			return null;
        }
		final ArrStr arr = new ArrStr();
		restoreArrFromValAsStr(arr, valAsStr,ITEM_VAL_TYPE, new ItemAsStrParser() {
			@Override
			public Object fromStr(final String asStr) {
				return ValAsStr.fromStr(asStr, ITEM_VAL_TYPE);
			}
		});
		return arr;
	}
	
	public static final EValType ITEM_VAL_TYPE = EValType.STR;
	@Override
	public EValType getItemValType() {
		return ITEM_VAL_TYPE;
	}
}

