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

public final class ComLibrary implements AutoCloseable {

	private static boolean libraryLoaded;
	private static final Object SEMAPHORE = new Object();
	
	private static final ThreadLocal<Boolean> COM_INITED = new ThreadLocal<Boolean>(){
		 @Override
	     protected Boolean initialValue() {
			return Boolean.FALSE;
		 }
	};
	
	private void ComLibrary(){
	}
	
	public static boolean wasInitialized(){
		return COM_INITED.get();
	}
	
	public static ComLibrary initialize() throws ComException{
		if (wasInitialized()){
			throw new IllegalStateException("COM library was already initialized");
		}
		synchronized(SEMAPHORE){
			if (!libraryLoaded){
		        final String libPath = System.getProperty("org.radixware.kernel.utils.wia.native-library-path");
		        if (libPath==null){
		            System.loadLibrary("jwia");
		        }else{
		            System.load(libPath);
		        }		
				libraryLoaded = true;				
			}
		}
		initialize(2);//2 - COINIT_APARTMENTTHREADED  required to work with WIA objects
		COM_INITED.set(Boolean.TRUE);
		return new ComLibrary();
	}
	
    @Override
    public void close() {
		if (COM_INITED.get()){
	        uninitialize();
			COM_INITED.set(Boolean.FALSE);
		}
    }
	
	private static void processPendingException(final Throwable exception){//call from native code
		try{
			final Thread curThread = Thread.currentThread();
			if ( curThread.getUncaughtExceptionHandler() != null ){
				curThread.getUncaughtExceptionHandler().uncaughtException(curThread, exception);
			}
			else{
				curThread.getThreadGroup().uncaughtException(curThread, exception);
			}
		}catch(Throwable ex){
			System.err.println("Failed to process exception: ");
			exception.printStackTrace();
			System.err.println("Original exception: ");
			ex.printStackTrace();
		}		
	}
	
	private static native void uninitialize();
	private static native void initialize(int params) throws ComException;
	
}