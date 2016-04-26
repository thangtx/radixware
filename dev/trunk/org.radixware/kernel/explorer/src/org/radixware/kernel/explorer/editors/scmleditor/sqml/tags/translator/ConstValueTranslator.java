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
import org.radixware.kernel.common.client.meta.sqml.ISqmlEnumDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlEnumItem;


public class ConstValueTranslator extends  SqmlTagTranslator{

    private ISqmlEnumItem constSetItem;
    private ISqmlEnumDef constSet;
    
    public ConstValueTranslator(ISqmlEnumDef constSet, ISqmlEnumItem constSetItem, boolean isValid){
        super(isValid);
        this.constSet=constSet;
        this.constSetItem=constSetItem;
    }
    
    @Override
    public String getDisplayString() {
        //String name;
        StringBuilder sb=new StringBuilder();
        if (isValid){            
            final String constSetName = constSet.getShortName();
            if (EDefinitionDisplayMode.SHOW_TITLES == displayMode) {
                sb.append(constSetName);
                sb.append(':');
                sb.append( constSetItem.getTitle());
                //name = constSetName + ":" + constSetItem.getTitle();
            } else if(EDefinitionDisplayMode.SHOW_SHORT_NAMES == displayMode) {
                sb.append(constSetName);
                sb.append(':');
                sb.append(constSetItem.getShortName());
                //name = constSetName + ":" + constSetItem.getShortName();
            } else {
                sb.append(constSet.getFullName());
                sb.append(':');
                sb.append(constSetItem.getShortName());
                //name = constSet.getFullName() + ":" + constSetItem.getShortName();            
            }
        }else{
            String name=null;
            if(constSet!=null){
                name=constSet.getId().toString();
            }else if(constSetItem!=null){
                name=constSetItem.getId().toString();
            }  
            sb.append("???<ConstSetId>-'<");
            sb.append(name);
            sb.append( ">'???");
            //name = "???<ConstSetId>-'<" + name + ">'???";
        }
        return sb.toString();
    }  
        

    @Override
    public String getToolTip() {
        String toolTip="";
        if (isValid){            
            final String constSetName = constSet.getShortName();//(constSet.getName()==null ? "#"+constSetItem.getId() : constSet.getName());
            if (EDefinitionDisplayMode.SHOW_TITLES == displayMode) {
                toolTip=getToolTip(constSetName, constSetItem.getShortName());
            } else if (EDefinitionDisplayMode.SHOW_SHORT_NAMES == displayMode) {
                toolTip = getToolTip(constSetName, constSetItem.getTitle());
            } else {
                toolTip = getToolTip(constSet.getTitle(), constSetItem.getTitle());
            }
        }
        return toolTip;
    }
    
    private String getToolTip(String constset, String item) {
        StringBuilder sb=new StringBuilder("<b>Constantset item:</b><br>&nbsp;&nbsp;&nbsp;&nbsp;");
        sb.append(item);
        sb.append("</br><br><b>Constantset:</b></br><br>&nbsp;&nbsp;&nbsp;&nbsp;");
        sb.append(constset);
        sb.append("</br>");
        return sb.toString();
    }
    
}
