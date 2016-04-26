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


public final class ComPropertyUUID extends ComProperty<UUID>{

	public ComPropertyUUID(final String spec, final UUID val){
		super(spec, EComPropValType.VT_CLSID, val);
	}
    
    public ComPropertyUUID(final EComPropSpecId id, final UUID val){
        super(id, EComPropValType.VT_CLSID, val);
    } 
	
	private ComPropertyUUID(final long propId){
		super(propId, EComPropValType.VT_CLSID, null);
	}
	
	private ComPropertyUUID(final String spec){
		this(spec, null);
	}	
	
	@Override
	Object getNativeValue(){//used from native code
		final UUID val = getValue();
		return val==null ? null : "{"+val.toString()+"}";
	}
	
	@Override
	void setNativeValue(final Object val){//used from native code
		if (val==null){
			setValue(null);
		}else if (val instanceof String){
			String uuidAsStr = ((String)val);
			setValue( UUID.fromString( uuidAsStr.substring(1,uuidAsStr.length() - 1) ) );
		}else{
			throw new IllegalArgumentException("Unexpected argument type "+val.getClass().getName());
		}
	}
}
