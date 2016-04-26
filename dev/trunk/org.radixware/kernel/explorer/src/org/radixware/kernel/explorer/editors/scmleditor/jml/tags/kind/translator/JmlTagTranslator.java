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

import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.enums.EDefinitionDisplayMode;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.explorer.editors.scml.IScmlTranslator;
import org.radixware.schemas.xscml.JmlType;


public class JmlTagTranslator implements IScmlTranslator{
    protected Jml.Tag tag;
    protected EDefinitionDisplayMode displayMode;
    
    public JmlTagTranslator(Jml.Tag tag){
        this.tag=tag;
    }
    
    public XmlObject saveToXml(){
        JmlType.Item item=JmlType.Item.Factory.newInstance();
        tag.appendTo(item);
        return item;
    }

    @Override
    public String getDisplayString() {       
        String name = tag.getDisplayName();
        if ((EDefinitionDisplayMode.SHOW_SHORT_NAMES == displayMode) && (name.indexOf("::") != -1)) {
            name = getNameWithoutModule(name);            
        }
        return name;      
    }

    @Override
    public String getToolTip() {
        return tag.getToolTip();
    }
    
    public void changeDisplayMode(EDefinitionDisplayMode displayMode){
        this.displayMode=displayMode;
    }
    
    public static String getNameWithoutModule(String name) {
        String res = name;
        int start = name.lastIndexOf("::");
        if (start != -1) {
            String s = name.substring(0, start);
            int n = s.lastIndexOf(' ') == -1 ? 0 : s.lastIndexOf(' ') + 1;
            res = name.substring(0, n) + name.substring(start + 2, name.length());
        }
        return res;
    }
    
}
