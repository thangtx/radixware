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

import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EDefinitionDisplayMode;
import org.radixware.kernel.common.client.meta.sqml.ISqmlColumnDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlEnumDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlEnumItem;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.explorer.editors.xscmleditor.TagInfo;


public class TypifiedValueTranslator  extends  SqmlTagTranslator{
    private final Object val;
    private final ISqmlColumnDef prop;
    private final IClientEnvironment environment;
    
    public TypifiedValueTranslator(final boolean isValid,final ISqmlColumnDef prop,final Object val,final IClientEnvironment environment){
       super(isValid);
       this.prop=prop;
       this.val=val;
       this.environment=environment;
    }
    
     
     @Override
    public String getDisplayString() {
        if(isValid){
           return getPropInfo(); 
        }
        return  "???'<" + val + ">'???";
    }

    @Override
    public String getToolTip() {
        String toolTip = "";
        if (isValid && prop.getEnums().size()==1){
            toolTip=getEnumItemValueToolTip(); 
        }
        return toolTip;
    }
    
    private String getPropInfo() {
        if (val == null) {
            return "null";
        } else if (prop.getEnums().size()==1) {
            return getEnumItemValueName();
        } else if (prop.getType() == EValType.PARENT_REF) {
            //String pid = ((Pid) val).getDefaultEntityTitle(environment.getEasSession());//((Reference) val).getPid().getDefaultEntityTitle();
            return (String)val;
        }
        return prop.getEditMask().toStr(environment,val);
    }

    private String getEnumItemValueName() {
        final ISqmlEnumDef enumDef = prop.getEnums().iterator().next();
        final ISqmlEnumItem item = enumDef.findItemByValue(val.toString());
        if (item!=null){
            final String fullName = enumDef.getFullName() + ":" + item.getShortName();
            if (EDefinitionDisplayMode.SHOW_TITLES == displayMode) {
                return TagInfo.getNameWithoutModule(enumDef.getTitle()) + ":" + item.getTitle();                    
            } else if (EDefinitionDisplayMode.SHOW_SHORT_NAMES == displayMode) {
                return TagInfo.getNameWithoutModule(fullName);
            } else {
                return fullName;            
            }                
        }
        return val.toString();
    }
    
    private String getEnumItemValueToolTip() {
        final ISqmlEnumDef enumDef = prop.getEnums().iterator().next();
        final ISqmlEnumItem item = enumDef.findItemByValue(val.toString());
        if (item!=null){
            if (EDefinitionDisplayMode.SHOW_TITLES == displayMode) {
                return enumDef.getFullName() + ":" + item.getFullName();
            } else if (EDefinitionDisplayMode.SHOW_SHORT_NAMES == displayMode) {
                return enumDef.getTitle() + ":" + item.getTitle();
            } else {
                return enumDef.getTitle() + ":" + item.getTitle();
            }            
        }
        return "";
    }
    
    
}
