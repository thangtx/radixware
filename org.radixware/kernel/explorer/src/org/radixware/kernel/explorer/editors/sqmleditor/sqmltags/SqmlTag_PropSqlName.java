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

package org.radixware.kernel.explorer.editors.sqmleditor.sqmltags;

import com.trolltech.qt.gui.QDialog;
import java.util.ArrayList;
import java.util.List;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EDefinitionDisplayMode;
import org.radixware.kernel.common.client.meta.sqml.ISqmlColumnDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.html.Html;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.editors.sqmleditor.tageditors.TableOrProperty_Dialog;
import org.radixware.kernel.explorer.editors.xscmleditor.XscmlEditor;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.schemas.xscml.Sqml;
import org.radixware.schemas.xscml.Sqml.Item.PropSqlName;
import org.radixware.schemas.xscml.Sqml.Item.PropSqlName.Owner;


public class SqmlTag_PropSqlName extends SqmlTag {

    private PropSqlName propSqlName;
    private ISqmlTableDef presentationClassDef;
    private ISqmlColumnDef prop;
    private Owner.Enum owner;
    private String tableAlias;
    private static final String path = "org.radixware.explorer/S_E/SYNTAX_SQML/SQML_PROP_SQL_NAME";

    public SqmlTag_PropSqlName(final IClientEnvironment environment, ISqmlColumnDef prop, long pos, Owner.Enum owner, EDefinitionDisplayMode showMode, String tableAlias) {
        super(environment, pos,prop==null?false :prop.isDeprecated());
        this.prop = prop;
        this.presentationClassDef = prop==null ? null : prop.getOwnerTable();
        this.owner = owner;
        this.tableAlias = tableAlias;
        setPropSqlNameInfo(showMode, null);
        //createPropSqlName(showMode,tableAlias);
    }

    public SqmlTag_PropSqlName(final IClientEnvironment environment, PropSqlName propSqlName, long pos, EDefinitionDisplayMode showMode) {
        super(environment, pos);
        Id tableId = propSqlName.getTableId();
        final Id propId = propSqlName.getPropId();
        presentationClassDef = environment.getSqmlDefinitions().findTableById(tableId);
        if (presentationClassDef == null) {
            final String mess = environment.getMessageProvider().translate("SqmlEditor", "table #%s not found");
            setNotValid(tableId, propId, String.format(mess, tableId));
        } else {
            prop = presentationClassDef.getColumns().getColumnById(propId);
            if (prop != null) {
                if (!prop.hasProperty()){
                    tableId = presentationClassDef.getTableId();
                }
                setIsDeprecated(prop.isDeprecated());
                if ((prop.getType() == EValType.PARENT_REF) && (prop.getReferenceIndex() == null)) {
                    String mess = Application.translate("SqmlEditor", "Index for parent reference column #%s not found");
                    mess = String.format(mess, propId);
                    setNotValid(tableId, propId, mess);
                }
                owner = propSqlName.getOwner();
                final String tblAlias = propSqlName.isSetTableAlias() ? propSqlName.getTableAlias() : null;
                final String propAlias = propSqlName.isSetPropAlias() ? propSqlName.getPropAlias() : null;
                final String propSql = propSqlName.isSetSql() ? propSqlName.getSql() : null;                
                //String name = "";
                StringBuilder sb= new StringBuilder();                
                if (owner == Owner.TABLE) {
                    if (tblAlias!=null) {
                        sb.append(tblAlias);
                    } else {
                        sb.append(presentationClassDef.getDisplayableText(showMode));
                    }
                    sb.append('.');
                }
                sb.append(prop.getDisplayableText(showMode));
                if (propAlias!=null) {
                    sb.append(' ').append(propAlias);
                }
                fullName = sb.toString();
                setPropSqlName(owner, propId, tableId, tblAlias, propAlias, propSql);
                setDisplayedInfo(showMode);                
            } else {
                String mess = Application.translate("SqmlEditor", "column #%s was not found in table %s #%s");
                mess = String.format(mess, propId, presentationClassDef.getFullName(), presentationClassDef.getId());
                setNotValid(tableId, propId, mess);
            }
        }
    }

    private void setNotValid(final Id tableId, final Id propId, final String mess) {
        setValid(false);
        String s = "???<" + tableId + ">.<" + propId + ">???";
        environment.getTracer().warning(mess);
        setDisplayedInfo(s, s);
    }

    /*private void createPropSqlName(EDefinitionDisplayMode showMode,String tableAlias){
    try{
    presentationClassDef=Environment.defManager.getClassPresentationDef(prop.getOwnerClassId());
    setPropSqlNameInfo(showMode,tableAlias,null);
    }catch(DefinitionError ex){
    valid=false;
    Environment.tracer.put(EEventSeverity.WARNING,"tableId not found",prop.getOwnerClassId().toString());
    String s="???<" + prop.getOwnerClassId() + ">.<" +prop.getId() +">???";
    setDisplayedInfo(s,s);
    }
    }*/
    private void setPropSqlNameInfo(EDefinitionDisplayMode showMode, String propAlias) {
        final Id ownerId = prop.hasProperty() ? presentationClassDef.getId() : presentationClassDef.getTableId();
        setPropSqlName(owner, prop.getId(), ownerId, tableAlias, propAlias, "");
        //String name = "";
        StringBuilder sb = new StringBuilder();
        String defName = tableAlias;
        if ((defName == null) || (defName.equals(""))) {
            defName = presentationClassDef.getDisplayableText(showMode);
        }
        if (owner == Owner.TABLE) {
            sb.append(defName).append('.');
        }
        sb.append(prop.getDisplayableText(showMode));
        fullName = sb.toString();
        if ((propAlias != null) && (!"".equals(propAlias))) {
            fullName = fullName + " " + propAlias;
        }
        setDisplayedInfo(showMode);
    }

    @Override
    public boolean showEditDialog(XscmlEditor editText, EDefinitionDisplayMode showMode) {
        final boolean openForCurTable = propSqlName.getOwner() == Owner.THIS || tableAlias != null;        
        final String alias = openForCurTable ? propSqlName.getPropAlias() : propSqlName.getTableAlias();
        final ISqmlTableDef classDef;
        if (tableAlias!=null && (!presentationClassDef.hasAlias() || !tableAlias.equals(presentationClassDef.getAlias()))){
            classDef = presentationClassDef.createCopyWithAlias(tableAlias);
        }else{
            classDef = presentationClassDef;
        }
        final TableOrProperty_Dialog dialog = new TableOrProperty_Dialog(editText.getEnvironment(), 
                                                                                                             classDef, 
                                                                                                             prop,
                                                                                                             alias,
                                                                                                             openForCurTable,
                                                                                                             showMode,
                                                                                                             editText.isReadOnly(),
                                                                                                             editText);
        if (dialog.exec() == QDialog.DialogCode.Accepted.value()) {
            prop = dialog.getProperty();
            presentationClassDef = prop==null ? null : prop.getOwnerTable();
            if (presentationClassDef == null) {
                return false;
            }
            setPropSqlNameInfo(showMode, dialog.getAlias());
            return true;
        }
        return false;
    }

    private void setPropSqlName(Owner.Enum owner, 
                                Id propId, 
                                Id tableId, 
                                String tableAlias, 
                                String propAlias,
                                String sql) {
        propSqlName = PropSqlName.Factory.newInstance();
        this.propSqlName.setOwner(owner);
        this.propSqlName.setPropId(propId);
        this.propSqlName.setTableId(tableId);
        if (tableAlias != null && !tableAlias.isEmpty()) {
            this.propSqlName.setTableAlias(tableAlias);
        }
        if (propAlias != null && !propAlias.isEmpty()) {
            this.propSqlName.setPropAlias(propAlias);
        }
        if (sql!=null && !sql.isEmpty()){
            propSqlName.setSql(sql);
        }
    }

    private String createTitle(String sTable, String sProp, String sPropType, List<String> refColumnNameList) {
        if (!isValid()) {
            return "";
        }
        sTable = Html.string2HtmlString(sTable);
        sProp = Html.string2HtmlString(sProp);
        final StringBuilder sb = new StringBuilder(180);
        sb.append("<b>Property:</b><br>&nbsp;&nbsp;&nbsp;&nbsp;").append(sProp).append("</br>");
        if (!refColumnNameList.isEmpty()) {
            sb.append("<br><b>Reference columns:</b></br>");
            for (String refColumnName : refColumnNameList) {
                refColumnName = Html.string2HtmlString(refColumnName);
                sb.append("<br>&nbsp;&nbsp;&nbsp;&nbsp;").append(refColumnName).append("</br>");
            }
        }
        sb.append("<br><b>Table:</b></br><br>&nbsp;&nbsp;&nbsp;&nbsp;").append(sTable);
        sb.append("</br><br><b>Property type:</b></br><br>&nbsp;&nbsp;&nbsp;&nbsp;").append(sPropType).append("</br>");
        return sb.toString();
    }

    @Override
    public final boolean setDisplayedInfo(EDefinitionDisplayMode showMode) {
        if (isValid()){
            ISqmlTableDef ownerTable = getOwner();
            String propAlias = "";
            if (propSqlName.isSetPropAlias()) {
                propAlias = " " + propSqlName.getPropAlias();
            }
            String getTagName = getTagName(ownerTable, showMode) + propAlias;
            final String propTypeName = prop.getType().getName();
            List<String> refColumnNameList = getColumnsName(showMode);
            if (EDefinitionDisplayMode.SHOW_TITLES == showMode) {
                final String defName = propSqlName.isSetTableAlias() ? propSqlName.getTableAlias() : presentationClassDef.getFullName();
                final String propName = prop.getShortName() + propAlias;
                setDisplayedInfo(createTitle(defName, propName, propTypeName, refColumnNameList), getTagName);
            } else {
                final String defTitle = presentationClassDef.getTitle();//propSqlName.isSetTableAlias()? propSqlName.getTableAlias():presentationClassDef.getTitle();
                final String propTitle = prop.getTitle() + propAlias;
                setDisplayedInfo(createTitle(defTitle, propTitle, propTypeName, refColumnNameList), getTagName);
            }
            return true;
        }
        return false;
    }

    private List<String> getColumnsName(EDefinitionDisplayMode showMode) {
        List<String> refColumnNameList = new ArrayList<>();
        if (prop.getType() == EValType.PARENT_REF) {
            List<ISqmlColumnDef> refColumns = prop.getReferenceColumns();
            for (int i = 0; i < refColumns.size(); i++) {
                ISqmlColumnDef col = refColumns.get(i);
                String parentRefPropName = /*getOwnerName( ownerTable, showMode)+"."+*/ (EDefinitionDisplayMode.SHOW_TITLES == showMode ? col.getShortName() : col.getTitle());
                refColumnNameList.add(parentRefPropName);
            }
        }
        return refColumnNameList;
    }

    private String getTagName(ISqmlTableDef ownerTable, EDefinitionDisplayMode showMode) {
        //String tagName = "";
        String ownerName = "";
        if (owner == Owner.TABLE) {
            ownerName = getOwnerName(ownerTable, showMode) + ".";
        }
        StringBuilder sb = new StringBuilder();
        if (prop.getType() == EValType.PARENT_REF) {
            List<ISqmlColumnDef> refColumns = prop.getReferenceColumns();
            StringBuilder tagName = new StringBuilder();
            for (int i = 0; i < refColumns.size(); i++) {
                ISqmlColumnDef col = refColumns.get(i);
                tagName.append(ownerName).append((EDefinitionDisplayMode.SHOW_TITLES == showMode ? col.getTitle() : col.getShortName()));
                if (i < refColumns.size() - 1) {
                    tagName.append(", ");
                }
            }
            if (refColumns.size() > 1) {
                sb.append('(').append(tagName.toString()).append(')');
            }else{
                sb.append(tagName.toString());
            }
        } else {
            sb.append(ownerName).append((EDefinitionDisplayMode.SHOW_TITLES == showMode ? prop.getTitle() : prop.getShortName()));
        }
        return sb.toString();
    }

    private String getOwnerName(ISqmlTableDef ownerTable, EDefinitionDisplayMode showMode) {
        String ownerName = "";
        if (owner != null) {
            if (propSqlName.isSetTableAlias()/*&& (prop.getType()!=EValType.PARENT_REF)*/) {
                ownerName = propSqlName.getTableAlias();
            } else {
                ownerName = ownerTable.getDisplayableText(showMode);
            }
        }
        return ownerName;
    }

    private ISqmlTableDef getOwner() {
        ISqmlTableDef ownerTable = null;
        if (prop.getType() == EValType.PARENT_REF) {
            List<ISqmlColumnDef> refColumns = prop.getReferenceColumns();
            if (!refColumns.isEmpty()) {
                ownerTable = refColumns.get(0).getOwnerTable();
            }
        } else {
            ownerTable = presentationClassDef;
        }
        return ownerTable;
    }

    private SqmlTag_PropSqlName(final IClientEnvironment environment, SqmlTag_PropSqlName source) {
        super(environment, source);
    }

    @Override
    public SqmlTag_PropSqlName copy() {
        SqmlTag_PropSqlName res = new SqmlTag_PropSqlName(environment, this);
        res.propSqlName = propSqlName;
        res.prop = prop;
        res.presentationClassDef = presentationClassDef;
        res.owner = owner;
        res.tableAlias = tableAlias;
        return res;
    }

    @Override
    public void addTagToSqml(XmlObject itemTag) {
        Sqml.Item item = (Sqml.Item) itemTag;
        item.setPropSqlName(this.propSqlName);
    }

    @Override
    protected String getSettingsPath() {
        return path;
    }
}