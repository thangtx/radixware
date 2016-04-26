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

import java.util.UUID;
import java.nio.ByteBuffer;


public final class ComPropertyUShort extends ComProperty<Integer>{

    public ComPropertyUShort(final String spec, final Integer val){
        super(spec, EComPropValType.VT_UI2, null);
		setValue(val);
    } 
    
    public ComPropertyUShort(final EComPropSpecId id, final Integer val){
        super(id, EComPropValType.VT_UI2, null);
		setValue(val);
    }
	
	private ComPropertyUShort(final long propId){
		super(propId, EComPropValType.VT_UI2, null);
	}
	
	private ComPropertyUShort(final String spec){
		this(spec, null);
	}
	
	@Override
	public void setValue(final Integer val){		
        if (val!=null && val<0){
            throw new IllegalArgumentException("Value must be not less then zero");
        }
        if (val!=null && val>Character.MAX_VALUE){
            throw new IllegalArgumentException("Value must be not greater then "+Character.MAX_VALUE);
        }
		super.setValue(val);
	}
		
	@Override
	Object getNativeValue(){//used from native code
		return getValue()==null ? null : new Character((char)getValue().intValue());
	}
}
