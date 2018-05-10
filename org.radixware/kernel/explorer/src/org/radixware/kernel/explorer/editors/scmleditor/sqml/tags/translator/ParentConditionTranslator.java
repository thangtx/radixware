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
import org.radixware.kernel.common.client.meta.sqml.ISqmlColumnDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.common.client.types.Reference;
import org.radixware.kernel.common.html.Html;
import org.radixware.schemas.xscml.Sqml.Item.ParentCondition.Operator;


public class ParentConditionTranslator extends  SqmlTagTranslator{
    
    private final ISqmlTableDef presentationClassDef;
    private final Reference entityReference;
    private final ISqmlColumnDef prop;
    private final String objTitle;
    private final String tableAlias;
    private final Operator.Enum operator;
    private static final String STR_NULL="null";
    
    public ParentConditionTranslator(final ISqmlTableDef presentationClassDef, final Reference entityReference, 
            final ISqmlColumnDef prop, final String objTitle,final String tableAlias,
            final Operator.Enum operator,final boolean isValid){
        super(isValid);
        this.presentationClassDef=presentationClassDef;        
        this.entityReference=entityReference;
        this.prop=prop;
        this.objTitle=objTitle;
        this.tableAlias=tableAlias;
        this.operator=operator;
                
    }   
    
     @Override
    public String getDisplayString() {
        String name;
        if (isValid){
            final String str_operator = getStrOperator(operator);
            final String propName = prop.getShortName();
            String defName = tableAlias == null ? presentationClassDef.getFullName() : tableAlias;
            final String defTitle = tableAlias == null ? presentationClassDef.getTitle() : tableAlias;
            final String propTitle = prop.getTitle();
            String str_objTitle = ((objTitle == null) || (objTitle.equals(STR_NULL))) ? "" : " " + objTitle;
            if (EDefinitionDisplayMode.SHOW_TITLES == displayMode) {
                name = defTitle + "." + propTitle + " " + str_operator + str_objTitle;                
            } else if (EDefinitionDisplayMode.SHOW_SHORT_NAMES == displayMode) {                
                defName = getNameWithoutModule(defName);
                str_objTitle = getNameWithoutModule(str_objTitle);
                name= defName + "." + propName + " " + str_operator + str_objTitle;
                
            } else {
                name= defName + "." + propName + " " + str_operator + str_objTitle;
            }            
        }else{
            name=getNotValidString();
        }
        return name;
    }
     
    @Override
    public String getToolTip() {
         String toolTip;
        if (isValid){           
            final String str_operator = getStrOperator(operator);
            final String propName = prop.getShortName();
            final String defName = tableAlias == null ? presentationClassDef.getFullName() : tableAlias;
            final String str_objTitle = ((objTitle == null) || (objTitle.equals(STR_NULL))) ? "" : " " + objTitle;
            if (EDefinitionDisplayMode.SHOW_TITLES == displayMode) {
                toolTip= defName + "." + propName + " " + str_operator + str_objTitle;
           // } else if (EDefinitionDisplayMode.SHOW_SHORT_NAMES == displayMode) {
            //    toolTip= presentationClassDef.getTitle() + "." + prop.getTitle() + " " + str_operator + str_objTitle;
            } else {
                toolTip= presentationClassDef.getTitle() + "." + prop.getTitle() + " " + str_operator + str_objTitle;
            }
            toolTip = Html.string2HtmlString(toolTip);
        }else{
            toolTip=getNotValidString();
        }
        return toolTip;
         
    }
    
    private String getNotValidString() {
       final String str_operator = getStrOperator(operator);
       final String propName = prop != null ? prop.getId().toString() : STR_NULL;
       final String defName = presentationClassDef != null ? presentationClassDef.getId().toString() : STR_NULL;
       final String pidStr = (entityReference == null) || (entityReference.getPid() == null) ? "" : " " + entityReference.getPid();
        return  "???" + defName + "-" + propName + " " + str_operator + pidStr + "???";//"???<TableId>-<PropertyId> <operator> <ParentPid>???"
        
    }
     
     private static String getStrOperator(final Operator.Enum operator) {
        if (operator == Operator.IS_NOT_NULL) {
            return "is not null";
        } else if (operator == Operator.IS_NULL) {
            return "is null";
        } else if (operator == Operator.EQUAL) {
            return "=";
        } else if (operator == Operator.NOT_EQUAL) {
            return "<>";
        } else {
            return "";
        }
    }
    
}
