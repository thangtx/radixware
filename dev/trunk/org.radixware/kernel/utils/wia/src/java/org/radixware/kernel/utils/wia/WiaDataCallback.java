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

public abstract class WiaDataCallback{
        
    protected abstract boolean headerCallback(final WiaDataCallbackHeader header) throws ComException;
    
    protected abstract boolean bandedDataCallback(final EWiaDataCallbackMessage message, final EWiaDataCallbackStatus status, final long lPercentComplete, final long lOffset, final long lLength,
            final java.nio.ByteBuffer pbBuffer) throws ComException;
            
    private boolean bandedDataCallback(final long lMessage, final long lStatus, final long lPercentComplete, final long lOffset, final long lLength,
            final java.nio.ByteBuffer pbBuffer) throws ComException{
        return bandedDataCallback(EWiaDataCallbackMessage.fromLong(lMessage), EWiaDataCallbackStatus.fromLong(lStatus), lPercentComplete, lOffset, lLength, pbBuffer);
    }
}
