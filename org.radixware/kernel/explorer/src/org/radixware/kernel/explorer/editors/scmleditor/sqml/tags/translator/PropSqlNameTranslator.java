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

import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.client.enums.EDefinitionDisplayMode;
import org.radixware.kernel.common.client.meta.sqml.ISqmlColumnDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.xscml.Sqml.Item.PropSqlName.Owner;


public class PropSqlNameTranslator extends  SqmlTagTranslator{
    
    private ISqmlColumnDef prop;
    private Owner.Enum owner;
    private String tableAlias;
    private String propAlias;
    private ISqmlTableDef presentationClassDef;
    private ISqmlTableDef ownerTable;
    
    public PropSqlNameTranslator(ISqmlTableDef presentationClassDef,ISqmlTableDef ownerTable,ISqmlColumnDef prop, Owner.Enum owner, String tableAlias, String propAlias,boolean isValid){
        super(isValid);
        this.prop = prop;
        this.presentationClassDef=presentationClassDef;
        this.ownerTable = ownerTable;
        this.owner = owner;
        this.tableAlias = tableAlias; 
        this.propAlias=propAlias;
    }
    
    @Override
    public String getDisplayString() {
         if (isValid){
            String strPropAlias="";
            if ( isAliasSet()) {
                strPropAlias = " " + propAlias;
            }
            String getTagName = getTagName(ownerTable) + strPropAlias;
            /*final String propTypeName = prop.getType().getName();
            List<String> refColumnNameList = getColumnsName(displayMode);
            if (EDefinitionDisplayMode.SHOW_TITLES == displayMode) {
                final String defName = isAliasSet(tableAlias) ? tableAlias : presentationClassDef.getFullName();
                final String propName = prop.getShortName() + strPropAlias;
               return getTagName;
            } else {
                final String defTitle = presentationClassDef.getTitle();//propSqlName.isSetTableAlias()? propSqlName.getTableAlias():presentationClassDef.getTitle();
                final String propTitle = prop.getTitle() + strPropAlias;
                setDisplayedInfo(createTitle(defTitle, propTitle, propTypeName, refColumnNameList), getTagName);
            }*/
            return getTagName;
        }
        Id tableId = presentationClassDef.getId();
        Id propId = prop.getId();
        return "???<" + tableId + ">.<" + propId + ">???";
    }
    
    private boolean isAliasSet(/*String alias*/){
        return propAlias!=null && !propAlias.isEmpty();
    }
    
    private String getTagName(ISqmlTableDef ownerTable) {
        //String tagName = "";
        StringBuilder sb=new StringBuilder();
        String ownerName = "";
        if (owner == Owner.TABLE) {
            ownerName = getOwnerName(ownerTable) + ".";
        }
        if (prop.getType() == EValType.PARENT_REF) {
            List<ISqmlColumnDef> refColumns = prop.getReferenceColumns();
            StringBuilder sb1=new StringBuilder();
            for (int i = 0; i < refColumns.size(); i++) {
                ISqmlColumnDef col = refColumns.get(i);
                sb1.append(ownerName);
                sb1.append(EDefinitionDisplayMode.SHOW_TITLES == displayMode ? col.getTitle() : col.getShortName());
                //tagName += ownerName + (EDefinitionDisplayMode.SHOW_TITLES == displayMode ? col.getTitle() : col.getShortName());
                if (i < refColumns.size() - 1) {
                    sb1.append(", ");
                    //tagName += ", ";
                }
            }
            
            if (refColumns.size() > 1) {
                sb.append('(');
                sb.append(sb1.toString());
                sb.append(')');
                //tagName = "(" + tagName + ")";
            }
        } else {
            sb.append(ownerName);
            sb.append(EDefinitionDisplayMode.SHOW_TITLES == displayMode ? prop.getTitle() : prop.getShortName());
            //tagName += ownerName + (EDefinitionDisplayMode.SHOW_TITLES == displayMode ? prop.getTitle() : prop.getShortName());
        }
        return sb.toString();
    }

    private String getOwnerName(ISqmlTableDef ownerTable) {
        String ownerName = "";
        if (owner != null) {
            if (isAliasSet()) {
                ownerName = tableAlias;
            } else {
                ownerName = ownerTable.getDisplayableText(displayMode);
            }
        }
        return ownerName;
    }
    
    @Override
    public String getToolTip() {
        if (isValid){
            //ISqmlTableDef ownerTable = getOwner();
            String strPropAlias="";
            if ( isAliasSet()) {
                strPropAlias = " " + propAlias;
            }
            //String getTagName = getTagName(ownerTable) + strPropAlias;
            final String propTypeName = prop.getType().getName();
            List<String> refColumnNameList = getColumnsName();
            if (EDefinitionDisplayMode.SHOW_TITLES == displayMode) {
                final String defName = isAliasSet() ? tableAlias : presentationClassDef.getFullName();
                final String propName = prop.getShortName() + strPropAlias;
                return createTitle(defName, propName, propTypeName, refColumnNameList);
            } else {
                final String defTitle = presentationClassDef.getTitle();//propSqlName.isSetTableAlias()? propSqlName.getTableAlias():presentationClassDef.getTitle();
                final String propTitle = prop.getTitle() + strPropAlias;
                return createTitle(defTitle, propTitle, propTypeName, refColumnNameList);
            }
            
        }
        return "";
  
    }
    
    private String createTitle(String sTable, String sProp, String sPropType, List<String> refColumnNameList) {
        if (!isValid) {
            return "";
        }
        if (sTable.indexOf('<') != -1) {
            sTable = sTable.replaceAll("<", "&#60;");
        }
        if (sProp.indexOf('<') != -1) {
            sProp = sProp.replaceAll("<", "&#60;");
        }
        StringBuilder sb=new StringBuilder("<b>Property:</b><br>&nbsp;&nbsp;&nbsp;&nbsp;");
        sb.append(sProp);
        sb.append("</br>");
        //String res = "<b>Property:</b>";
        //res += "<br>&nbsp;&nbsp;&nbsp;&nbsp;" + sProp + "</br>";
        if (!refColumnNameList.isEmpty()) {
            sb.append("<br><b>Reference columns:</b></br>");             
           // res += "<br><b>Reference columns:</b></br>";
            for (String refColumnName : refColumnNameList) {
                if (refColumnName.indexOf('<') != -1) {
                    refColumnName = refColumnName.replaceAll("<", "&#60;");
                }
                //res += "<br>&nbsp;&nbsp;&nbsp;&nbsp;" + refColumnName + "</br>";
                sb.append("<br>&nbsp;&nbsp;&nbsp;&nbsp;");
                sb.append(refColumnName);
                sb.append("</br>");
            }
        }
        sb.append("<br><b>Table:</b></br><br>&nbsp;&nbsp;&nbsp;&nbsp;");
        sb.append(sTable);
        sb.append("</br><br><b>Property type:</b></br><br>&nbsp;&nbsp;&nbsp;&nbsp;");
        sb.append(sPropType);
        sb.append("</br>");
        //res += "<br><b>Table:</b></br>";
        //res += "<br>&nbsp;&nbsp;&nbsp;&nbsp;" + sTable + "</br>";
        //res += "<br><b>Property type:</b></br>";
        //res += "<br>&nbsp;&nbsp;&nbsp;&nbsp;" + sPropType + "</br>";
        return sb.toString();
    }
    
    private List<String> getColumnsName() {
        List<String> refColumnNameList = new ArrayList<>();
        if (prop.getType() == EValType.PARENT_REF) {
            List<ISqmlColumnDef> refColumns = prop.getReferenceColumns();
            for (int i = 0; i < refColumns.size(); i++) {
                ISqmlColumnDef col = refColumns.get(i);
                String parentRefPropName = /*getOwnerName( ownerTable, showMode)+"."+*/ (EDefinitionDisplayMode.SHOW_TITLES == displayMode ? col.getShortName() : col.getTitle());
                refColumnNameList.add(parentRefPropName);
            }
        }
        return refColumnNameList;
    }    
}
