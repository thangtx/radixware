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

package org.radixware.kernel.explorer.editors.scmleditor.jml.tags.kind.translator;

import org.radixware.kernel.common.client.enums.EDefinitionDisplayMode;
import org.radixware.kernel.common.jml.Jml;


public class JmlTagDBEntityTranslator extends JmlTagTranslator{
    
    private boolean isActual;
    
    public JmlTagDBEntityTranslator(Jml.Tag tag,boolean isActual){
        super(tag);
        this.isActual=isActual;
    }
    
    @Override
    public String getDisplayString() {
        String name=tag.getDisplayName();
        if(!isActual){
             name="???"+tag.getDisplayName()+"???";
        }
        if ((EDefinitionDisplayMode.SHOW_SHORT_NAMES == displayMode) && (name.indexOf("::") != -1)) {
            String notValidPrefix="???";
            if(!name.startsWith(notValidPrefix)){
                notValidPrefix=null;
            }
            name = (notValidPrefix==null ? "" : notValidPrefix) + getNameWithoutModule(name);
        }
        return name;
    }
    
    
    
}
