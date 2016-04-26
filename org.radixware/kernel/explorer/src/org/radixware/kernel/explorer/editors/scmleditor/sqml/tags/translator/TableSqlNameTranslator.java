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
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;


public class TableSqlNameTranslator extends  SqmlTagTranslator{
    
    private final ISqmlTableDef presentationClassDef;
    private final String alias;
    
    public TableSqlNameTranslator(final ISqmlTableDef presentationClassDef,final String alias,final boolean isValid){
       super(isValid) ;
       this.presentationClassDef=presentationClassDef;
       this.alias=alias;
    }

    @Override
    public String getDisplayString() {
        String name;
        if (isValid){
            final String fullname =  presentationClassDef.getFullName();
            final String title =  presentationClassDef.getTitle();
            final String str_alias = ((alias == null) || ("".equals(alias))) ? "" : " " + alias;
            if (EDefinitionDisplayMode.SHOW_TITLES == displayMode) {
                name=title + str_alias;
            } else if (EDefinitionDisplayMode.SHOW_SHORT_NAMES == displayMode) {
                name = getNameWithoutModule(fullname)+ str_alias;
            } else {
                name =  fullname + str_alias;
            }
        }else{
            name="???" + presentationClassDef.getId().toString() + "???";
        }
        return name;
    }
    
    private String createTitle(final String s) {
        String res="";
        if (s.indexOf('<') != -1) {
            res = s.replaceAll("<", " ");//&#60;
        }
        if (s.indexOf('>') != -1) {
            res = s.replaceAll(">", " ");//&#62;
        }
        return "<b>Table:  </b>" + res;
    }

    @Override
    public String getToolTip() {
        String toolTip="";
        if (isValid){
            final String fullname =  presentationClassDef.getFullName();
            final String title =  presentationClassDef.getTitle();
            if (EDefinitionDisplayMode.SHOW_TITLES == displayMode) {
                toolTip=createTitle(fullname);
            } else {
                toolTip=createTitle(title);
            }
        }
        return toolTip;
    }    
}
