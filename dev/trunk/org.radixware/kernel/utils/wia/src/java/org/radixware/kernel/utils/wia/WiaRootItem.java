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
 
package org.radixware.kernel.utils.wia;

import java.util.EnumSet;
import java.util.UUID;

public final class WiaRootItem extends WiaItem{
    
    WiaRootItem(final long pointer){
        super(pointer);
    }           
    
    public WiaItem[] deviceDlg(final long hwnd, final EnumSet<EDeviceDialogFlag> dialogFlags, final EnumSet<EImageIntent> imageIntents) throws ComException{
        return deviceDlg(getNativePointer(), hwnd, EDeviceDialogFlag.toBitMask(dialogFlags), EImageIntent.toBitMask(imageIntents));
    }
	
	public WiaDeviceCapabilities loadDeviceCapabilities() throws ComException{
		final long pointer = enumDevCaps(getNativePointer());
		return new WiaDeviceCapabilities(pointer,16);
	}
	
	public WiaItem execDeviceCommand(final UUID command) throws ComException{
		final long pointer = deviceCommand(getNativePointer(), "{"+command.toString()+"}");
		return pointer==0 ? null : new WiaItem(pointer); 
	}		
	
	private static native WiaItem[] deviceDlg(long nativePtr, long hwnd, int flagtype, long intentFlags) throws ComException;
	private static native long enumDevCaps(long pointer) throws ComException;
	private static native long deviceCommand(long pointer, String commandGuid) throws ComException;
}
