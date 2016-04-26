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

package org.radixware.kernel.common.uamp;

public abstract class UampMessage {    

    protected String charSet = null;            
        
    protected static final byte MS = 10;   // message separator '\n'
    protected static final byte PS = 16;   // parameter separator
    protected static final byte VS = '=';  // parameter value separator '='
    protected static final byte IS = 29;   // item separator
    protected static final byte FS = 28;   // field separator
    protected static final byte NI = 7;    // NULL indicator
    protected static final byte SP = 19;   // special char prefix    
}

