/*
* Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.kernel.common.devices;


public class CardNotPresentException extends CardException{//javax.smartcardio is not present in IBM java
    
    static final long serialVersionUID = 6078509021608697841L;

    public CardNotPresentException(final String message) {
        super(message);
    }

    public CardNotPresentException(final Throwable cause){
        super(cause);
    }
    
    public CardNotPresentException(final String message, final Throwable cause){
        super(message, cause);
    }
}
