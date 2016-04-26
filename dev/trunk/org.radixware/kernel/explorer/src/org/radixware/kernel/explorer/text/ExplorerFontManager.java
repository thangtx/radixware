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

package org.radixware.kernel.explorer.text;

import java.util.EnumSet;
import org.radixware.kernel.common.client.enums.EFontStyle;
import org.radixware.kernel.common.client.enums.EFontWeight;
import org.radixware.kernel.common.client.enums.ETextDecoration;
import org.radixware.kernel.common.client.text.AbstractFontManager;


public final class ExplorerFontManager extends AbstractFontManager{
    
    private final static ExplorerFontManager INSTANCE = new ExplorerFontManager();
    
    private ExplorerFontManager(){        
    }

    @Override
    public ExplorerFont getDefaultFont() {        
        return ExplorerFont.Factory.getDefault();
    }

    @Override
    public ExplorerFont getFont(final String family, final int pointSize, final EFontWeight weight, final EFontStyle style, final EnumSet<ETextDecoration> decoration) {
        return ExplorerFont.Factory.getFont(family, pointSize, weight, style, decoration);
    }

    @Override
    public ExplorerFont getFontFromStr(final String fontAsStr) {
        return ExplorerFont.Factory.fromStr(fontAsStr);
    }
    
    public static ExplorerFontManager getInstance(){
        return INSTANCE;
    }    
}
