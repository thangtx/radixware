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

package org.radixware.kernel.server.exceptions;

import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.exceptions.WrongFormatError;


public class WrongPidFormatError extends WrongFormatError{
    
    static final long serialVersionUID = 8498198775935786713L;
    
    private final DdsTableDef tableDef;
    private final String pidAsStr;
    
    public WrongPidFormatError(final DdsTableDef tableDef, final String pidAsStr, final String message){
        super(message);
        this.tableDef = tableDef;
        this.pidAsStr = pidAsStr;
    }
    
    public DdsTableDef getTableDef(){
        return tableDef;
    }
    
    public String getPidAsStr(){
        return pidAsStr;
    }
}
