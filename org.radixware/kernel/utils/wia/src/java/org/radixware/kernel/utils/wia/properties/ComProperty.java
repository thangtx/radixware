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

import java.util.Collections;
import org.radixware.kernel.utils.wia.ComException;

public class ComProperty<T> {
    //PROPSPEC
    private final EComPropSpecKind specKind;    
	private final long propSpecId;
    private final String propSpecName;
    
    //PROPVARIANT
    private final EComPropValType valType;	
    private T value;
    
    public ComProperty(final EComPropSpecId spec, final EComPropValType type, final T value){
        specKind = EComPropSpecKind.PROP_ID;
        propSpecId = spec.getValue();
        propSpecName = null;
        valType = type;
        this.value = value;
    }
	
	protected ComProperty(final long spec, final EComPropValType type, final T value){
        specKind = EComPropSpecKind.PROP_ID;
        propSpecId = spec;
        propSpecName = null;
        valType = type;
        this.value = value;		
	}
    
    public ComProperty(final String spec, final EComPropValType type, final T value){
        specKind = EComPropSpecKind.STRING;
        propSpecId = 0;
        propSpecName = spec;
        valType = type;
        this.value = value;
    }    

    public EComPropSpecKind getSpecKind() {
        return specKind;
    }        

    public EComPropSpecId getPropSpec() {
        return EComPropSpecId.fromLong(propSpecId);
    }

    public String getPropSpecName() {
        return propSpecName;
    }

    public EComPropValType getValType() {
        return valType;
    }

    public T getValue() {
        return value;
    }

    public void setValue(final T value) {
        this.value = value;
    }

	@SuppressWarnings("unused")//used from native code
    private boolean propSpecIsPropId(){
		return getSpecKind()==EComPropSpecKind.PROP_ID;
    }	
	
	@SuppressWarnings("unused")//used from native code
	private long getPropSpecId(){
	    return propSpecId;
	}
	
	@SuppressWarnings("unused")//used from native code
	private int getValTypeCode(){
		return getValType()==null ? 0 : getValType().getCode();
	}
	
	Object getNativeValue(){//used from native code
		return value;
	}
	
	@SuppressWarnings("unchecked")
	void setNativeValue(final Object val){//used from native code
		value = (T)val;
	}
	
	public final void writeValueToStorage(final WiaPropertyStorage storage) throws ComException{
		storage.writeMultiple(Collections.singleton((ComProperty)this));
	}
	
	public final boolean readValueFromStorage(final WiaPropertyStorage storage) throws ComException{
		return storage.readMultiple(Collections.singleton((ComProperty)this));
	}
	
	@Override
	public String toString(){
		StringBuilder description = new StringBuilder();
		description.append("{Name: ");
		if (getSpecKind()==EComPropSpecKind.STRING){
			if (getPropSpecName()==null){
				description.append("null");
			}else{
				description.append('"');
				description.append(getPropSpecName());
				description.append('"');
			}
		}else{
			if (getPropSpec()==EComPropSpecId.UNKNOWN){
				description.append("Unknown");				
			}else{
				description.append('"');
				description.append(getPropSpec().getDescription());
				description.append('"');
			}
		}
		description.append("; Type: ");
		description.append(getValType().getDescription());
		description.append("; Value: ");
		if (value==null){
			description.append("null");
		}else{
			description.append('"');
			description.append(String.valueOf(value));
			description.append('"');
		}
		description.append('}');
		return description.toString();
	}
}
