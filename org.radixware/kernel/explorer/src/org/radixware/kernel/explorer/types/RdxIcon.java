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

package org.radixware.kernel.explorer.types;

import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QIconEngineV2;
import com.trolltech.qt.gui.QPixmap;


public class RdxIcon extends QIcon implements org.radixware.kernel.common.client.types.Icon {
    
    private final String cacheKey;
    
    public RdxIcon(final QPixmap pixmap, final String cacheKey) {
        super(pixmap);
        this.cacheKey = cacheKey;
    }
    
    public RdxIcon(final String fileName){
        super(fileName);
        cacheKey = fileName;
    }
    
    public RdxIcon(final QPixmap pixmap) {
        this(pixmap,null);
    }

    public RdxIcon(final QIcon other) {
        super(other);
        cacheKey = other instanceof RdxIcon ? ((RdxIcon)other).getCacheKey() : null;       
    }

    public RdxIcon(final QIconEngineV2 engine, final String cacheKey) {
        super(engine);
        this.cacheKey = cacheKey;
        
    }
    
    public final String getCacheKey(){
        return cacheKey==null || cacheKey.isEmpty() ? String.valueOf(cacheKey()) : cacheKey;
    }
}