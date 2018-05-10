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

package org.radixware.kernel.explorer.webdriver.input;

import org.radixware.kernel.explorer.webdriver.WebDrvSession;
import org.radixware.kernel.explorer.webdriver.exceptions.WebDrvException;

abstract class InputAction<T extends InputSource> {        
    
    private final T source;
    
    protected InputAction(final T inputSource){
        this.source = inputSource;
    }   
    
    protected final T getInputSource(){
        return source;
    }
    
    public abstract void dispatch(WebDrvSession session, 
                                  int tickDuration,
                                  long tickStart) throws WebDrvException;
}
