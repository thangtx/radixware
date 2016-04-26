/*
 * Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. This Source Code is distributed
 * WITHOUT ANY WARRANTY; including any implied warranties but not limited to
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Mozilla Public License, v. 2.0. for more details.
 */

package org.radixware.kernel.utils.wia.properties;

public final class ComPropertyInt extends ComProperty<Integer>{

    public ComPropertyInt(final String spec, final Integer val){
        super(spec, EComPropValType.VT_I4, val);
    } 
	
    public ComPropertyInt(final EComPropSpecId id, final Integer val){
        super(id, EComPropValType.VT_I4, val);
    }
	
	private ComPropertyInt(final long propId){
		super(propId, EComPropValType.VT_I4, null);
	}
	
	private ComPropertyInt(final String spec){
		this(spec, null);
	}
}
