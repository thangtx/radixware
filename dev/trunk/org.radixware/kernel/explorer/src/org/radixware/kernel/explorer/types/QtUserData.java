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

package org.radixware.kernel.explorer.types;

import org.radixware.kernel.common.exceptions.RadixError;

public final class QtUserData extends java.lang.Object{
	private final Object data;

	public QtUserData(Object data){
		this.data = data;
	}
	public Object get(){
		return data;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
        if (data==null)
            return new QtUserData(null);
		try {
			return new QtUserData(data.getClass().getMethod("clone", new Class<?>[]{}).invoke(data, new Object[]{}));
		} catch (Exception e) {
			throw new RadixError("Cannot invoke clone method.",e);
		}
	}

	@Override
	public boolean equals(Object obj) {
		try {
            if (this==obj)
                return true;
            if (obj instanceof QtUserData){
                if (data==null){
                    return ((QtUserData)obj).data==null;
                }
                else{
                    return (Boolean)data.getClass().getMethod("equals", new Class<?>[]{Object.class}).invoke(data, new Object[]{((QtUserData)obj).data});
                }
            }
            if (data==null)
                return obj==null;
            return (Boolean)data.getClass().getMethod("equals", new Class<?>[]{Object.class}).invoke(data, new Object[]{obj});
		} catch (Exception e) {
			throw new RadixError("Cannot invoke equals method.",e);
		}
	}

	@Override
	public int hashCode() {
        if (data==null)
            return 0;
		try {
			return (Integer)data.getClass().getMethod("hashCode", new Class<?>[]{}).invoke(data, new Object[]{});
		} catch (Exception e) {
			throw new RadixError("Cannot invoke hashCode method.",e);
		}
	}

	@Override
	public String toString() {
        if (data==null)
            return String.valueOf((Object)null);
		try {
			return (String)data.getClass().getMethod("toString", new Class<?>[]{}).invoke(data, new Object[]{});
		} catch (Exception e) {
			throw new RadixError("Cannot invoke toString method.",e);
		}
	}
}
