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

import java.util.UUID;
import java.util.EnumSet;

public final class WiaDeviceManager extends ComObject{

	private static final class WiaDeviceManagerStream extends AbstractComObjectStream implements IComObjectStream<WiaDeviceManager>{
		public WiaDeviceManagerStream(final long pointer){
			super(pointer, EWiaIid.IID_IWiaDevMgr.getGuid());
		}
		
		@Override
		public WiaDeviceManager readObjectAndRelease() throws ComException{
			return (WiaDeviceManager)super.readObjectAndRelease();
		}
		
		@Override
		protected WiaDeviceManager wrapObject(final long pointer, final UUID iid){
			return new WiaDeviceManager(pointer);
		}
	}
    
    private WiaDeviceManager(final long pointer){
        super(pointer, EWiaIid.IID_IWiaDevMgr.getGuid());
    }	
    
    public static WiaDeviceManager newInstance() throws ComException{
		final long pointer = createWiaDevMgr();
		return pointer==0 ? null : new WiaDeviceManager(pointer);
	}
        
    
    public WiaDeviceDescription selectDevice(final long hwnd, final EWiaDeviceType deviceType, final boolean showDialogForcedly) throws ComException{
        return selectDevice(getNativePointer(), hwnd, deviceType.getValue(), showDialogForcedly);
    }
	
	public WiaDevices loadDevices() throws ComException{
		final long pointer = getDevices(getNativePointer());
		return pointer==0 ? null : new WiaDevices(pointer,4);
	}
	
	public WiaRootItem openDevice(final String deviceId) throws ComException{
		if (deviceId==null || deviceId.isEmpty()){
			throw new IllegalArgumentException("deviceId was not specified");
		}
		final long pointer = createDevice(getNativePointer(),deviceId);
		return pointer==0 ? null : new WiaRootItem(pointer);
	}
	
	@Override
	public IComObjectStream<WiaDeviceManager> writeToStream() throws ComException{
		final long pointer = marshalObject();
		return pointer==0 ? null : new WiaDeviceManagerStream(pointer);
	}	
	
	public UUID getImageDialog(final long hwnd,
							   final EWiaDeviceType deviceType,
							   final EnumSet<EDeviceDialogFlag> dialogFlags,
							   final EnumSet<EImageIntent> imageIntents,
							   final WiaRootItem rootItem,
							   final String filePath,
							   final UUID imageFormat
							   ) throws ComException{
		final String imageFormatStr = imageFormat==null ? null : ("{"+imageFormat.toString()+"}");
		final String actualFormatAsStr = getImageDialog(getNativePointer(),
														hwnd,
														deviceType.getValue(),
														EDeviceDialogFlag.toBitMask(dialogFlags),
														EImageIntent.toBitMask(imageIntents),
														rootItem==null ? 0 : rootItem.getNativePointer(),
														filePath,
														imageFormatStr);
		return actualFormatAsStr==null ? null : UUID.fromString( actualFormatAsStr.substring(1, actualFormatAsStr.length()-1) );
	}
	
	private native static long createWiaDevMgr();
	private native static WiaDeviceDescription selectDevice(final long nativePointer, final long hwnd, final int devType, final boolean showDialogForcedly) throws ComException;	
	private native static long getDevices(final long nativePointer) throws ComException;
	private native static long createDevice(final long nativePointer, final String deviceId) throws ComException;
	private native static String getImageDialog(final long nativePointer, 
												final long hwnd, 
												final int devType, 
												final int dialogFlags, 
												final long imageIntents, 
												final long rootItemPointer,
												final String filePath,
												final String imageFormat) throws ComException;
}
