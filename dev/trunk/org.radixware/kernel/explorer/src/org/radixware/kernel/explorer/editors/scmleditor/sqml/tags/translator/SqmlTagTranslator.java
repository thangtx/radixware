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

package org.radixware.kernel.explorer.editors.scmleditor.sqml.tags.translator;

import org.radixware.kernel.common.client.enums.EDefinitionDisplayMode;
import org.radixware.kernel.explorer.editors.scml.IScmlTranslator;


public class SqmlTagTranslator  implements IScmlTranslator{
    
    protected EDefinitionDisplayMode displayMode;
    protected boolean isValid;
    
    protected SqmlTagTranslator(final boolean isValid){
        this.isValid=isValid;
    }
    
    @Override
    public String getDisplayString() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getToolTip() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void changeDisplayMode(final EDefinitionDisplayMode displayMode){
        this.displayMode=displayMode;
    }
    
    public static String getNameWithoutModule(final String name) {
        String res = name;
        final int start = name.lastIndexOf("::");
        if (start != -1) {
            final String s = name.substring(0, start);
            final int n = s.lastIndexOf(' ') == -1 ? 0 : s.lastIndexOf(' ') + 1;
            res = name.substring(0, n) + name.substring(start + 2, name.length());
        }
        return res;
    }
    
}
