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

package org.radixware.wps.resources;

import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.eas.resources.AbstractFileDirResource;

public final class WpsFileDirResource extends AbstractFileDirResource{
    
    private final static WpsFileDirResource INSTANCE = new WpsFileDirResource();
    
    private WpsFileDirResource(){
    }
    
    public static WpsFileDirResource getInstance(){
        return INSTANCE;
    }    

    @Override
    public String select(final IClientEnvironment environment, final String title, final String dirName) {
        return null;
    }
    
}