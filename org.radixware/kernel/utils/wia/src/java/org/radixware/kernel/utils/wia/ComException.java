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

public class ComException extends Exception{

	static final long serialVersionUID = 5716126720859044266L;

	private final int hResultCode;
	private final EHResult hResult;
	private final int lastErrorCode;
	private final String lastErrorMessage;
    
    private ComException (final int hResult, final int lastErrorCode, final String lastErrorMessage){
        super(buildErrorMessage(hResult, lastErrorCode, lastErrorMessage));
		this.hResult = EHResult.fromCode(hResult);
		hResultCode = hResult;
		this.lastErrorCode = lastErrorCode;
		this.lastErrorMessage = lastErrorMessage;
    }
	
	private static String buildErrorMessage(final int hResultCode, final int lastErrorCode, final String lastErrorMessage){
		final StringBuilder messageBuilder = new StringBuilder();
		final EHResult hresult = EHResult.fromCode(hResultCode);		
		if (hresult==null){
			messageBuilder.append("HRESULT value = 0x");
			messageBuilder.append(Integer.toHexString(hResultCode));
		}else{
			messageBuilder.append("HRESULT value = ");
			messageBuilder.append(hresult.name());
			messageBuilder.append(" (");
			messageBuilder.append(hresult.getDescription());
			messageBuilder.append(')');
		}
		if (lastErrorMessage==null){
			if (lastErrorCode!=0){
				messageBuilder.append(". Error code: ");
				messageBuilder.append(Integer.toHexString(lastErrorCode));
			}
		}else{
			messageBuilder.append(". Error message: \"");
			messageBuilder.append(lastErrorMessage);
			messageBuilder.append('\"');		
		}
		return messageBuilder.toString();
	}
	
	public int getResultCode(){
		return hResultCode;
	}
	
	public EHResult getResult(){
		return hResult;
	}
	
	public int getLastErrorCode(){
		return lastErrorCode;
	}
	
	public String getLastErrorMessage(){
		return lastErrorMessage;
	}
}