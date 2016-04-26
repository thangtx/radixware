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

public final class ComPropertyFloat extends ComProperty<Float>{

    public ComPropertyFloat(final String spec, final Float val){
        super(spec, EComPropValType.VT_R4, val);
    }	
	
	public ComPropertyFloat(final EComPropSpecId id, final Float val){
		super(id, EComPropValType.VT_R4, val);
	}
	
	private ComPropertyFloat(final long propId){
		super(propId, EComPropValType.VT_R4, null);
	}
	
	private ComPropertyFloat(final String spec){
		this(spec, null);
	}
}
