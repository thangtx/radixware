/*
* Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.kernel.explorer.iad.wia;

import java.util.EnumSet;
import org.radixware.kernel.common.client.errors.IClientError;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.utils.wia.ComException;
import org.radixware.kernel.utils.wia.EHResult;


final class WiaException extends RuntimeException implements IClientError {
    
    static final long serialVersionUID = -4710376275054825856L;
    
    private static final EnumSet<EHResult> KNOWN_ERRORS = 
        EnumSet.of(EHResult.WIA_ERROR_BUSY, EHResult.WIA_ERROR_COVER_OPEN, EHResult.WIA_ERROR_DEVICE_COMMUNICATION, 
                   EHResult.WIA_ERROR_DEVICE_LOCKED, EHResult.WIA_ERROR_INCORRECT_HARDWARE_SETTING, 
                   EHResult.WIA_ERROR_ITEM_DELETED, EHResult.WIA_ERROR_LAMP_OFF, EHResult.WIA_ERROR_OFFLINE,
                   EHResult.WIA_ERROR_USER_INTERVENTION);

    
    private final String title;
    
    public WiaException(final String title, final String message, final MessageProvider mp, final Throwable cause){
        super(getErrorMessage(cause, mp, message),cause);
        this.title = title;
    }
    
    private static String getErrorMessage(final Throwable exception, final MessageProvider mp, final String defaultMessage){
        if (exception instanceof ComException){
            final EHResult result = ((ComException)exception).getResult();
            if (result==null){
                return defaultMessage==null || defaultMessage.isEmpty() ? exception.getMessage() : defaultMessage;
            }
            switch(result){
                case WIA_ERROR_BUSY:
                    return mp.translate("IAD", "The device is busy. Close any applications that are using this device or wait for it to finish and then try again.");
                case WIA_ERROR_COVER_OPEN:
                    return mp.translate("IAD", "The device`s cover is open.");
                case WIA_ERROR_DEVICE_COMMUNICATION:
                    return mp.translate("IAD", "Communication with the device failed. Make sure that the device is powered on and connected to the PC. If the problem persists, disconnect and reconnect the device.");
                case WIA_ERROR_DEVICE_LOCKED:
                    return mp.translate("IAD", "The device is locked. Close any applications that are using this device or wait for it to finish and then try again.");
                case WIA_ERROR_INCORRECT_HARDWARE_SETTING:
                    return mp.translate("IAD", "There is an incorrect setting on the device.");
                case WIA_ERROR_ITEM_DELETED:
                    return mp.translate("IAD", "The WIA device was deleted. It's no longer available.");
                case WIA_ERROR_LAMP_OFF:
                    return mp.translate("IAD", "The scanner's lamp is off.");
                case WIA_ERROR_OFFLINE:
                    return mp.translate("IAD", "The device is offline. Make sure the device is powered on and connected to the PC.");
                case WIA_ERROR_USER_INTERVENTION:
                    return mp.translate("IAD", "There is a problem with the device. Make sure that the device is turned on, online, and any cables are properly connected.");
                default:
                    return defaultMessage==null || defaultMessage.isEmpty() ? exception.getMessage() : defaultMessage;
            }
        }
        return defaultMessage==null || defaultMessage.isEmpty() ? exception.getMessage() : defaultMessage;
    }
    
    private static boolean needForExceptionDetails(final Throwable cause){
        return !(cause instanceof ComException)
                || ((ComException)cause).getResult()==null
                || !KNOWN_ERRORS.contains(((ComException)cause).getResult());
    }

    @Override
    public String getTitle(final MessageProvider messageProvider) {
        return title;
    }

    @Override
    public String getLocalizedMessage(final MessageProvider messageProvider) {
        return getMessage();
    }

    @Override
    public String getDetailMessage(final MessageProvider messageProvider) {
        return needForExceptionDetails(getCause()) ? getMessage() : "";
    }

}
