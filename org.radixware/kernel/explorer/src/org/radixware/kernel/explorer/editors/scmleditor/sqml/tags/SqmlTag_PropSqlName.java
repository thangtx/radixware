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

package org.radixware.kernel.explorer.editors.scmleditor.sqml.tags;

import java.util.List;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.sqml.ISqmlColumnDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.editors.scmleditor.TagInfo;
import org.radixware.kernel.explorer.editors.scml.IScmlItem;
import org.radixware.kernel.explorer.editors.scmleditor.sqml.tags.translator.PropSqlNameTranslator;
import org.radixware.schemas.xscml.Sqml.Item.PropSqlName;
import org.radixware.schemas.xscml.Sqml.Item.PropSqlName.Owner;


public class SqmlTag_PropSqlName extends TagInfo{
    
     private PropSqlName propSqlName;
     private static final String PATH = "org.radixware.explorer/S_E/SYNTAX_SQML/SQML_PROP_SQL_NAME";
     
     public SqmlTag_PropSqlName(final IClientEnvironment environment,final PropSqlName propSqlName) {
        super(environment);
        this.propSqlName=(PropSqlName)propSqlName.copy();
        valid=true;
        Id tableId = propSqlName.getTableId();
        final Id propId = propSqlName.getPropId();
        final Owner.Enum owner = propSqlName.getOwner();
        final String  tableAlias=propSqlName.getTableAlias();
        final String  propAlias= propSqlName.getPropAlias();
        ISqmlColumnDef prop=null;
        final ISqmlTableDef presentationClassDef = environment.getSqmlDefinitions().findTableById(tableId);

        if (presentationClassDef == null) {
            //final String mess = environment.getMessageProvider().translate("SqmlEditor", "table #%s not found");
            setNotValid();
        } else {
            prop = presentationClassDef.getColumns().getColumnById(propId);
            if (prop != null) {
                if ((prop.getType() == EValType.PARENT_REF) && (prop.getReferenceIndex() == null)) {
                    //final String mess = Application.translate("SqmlEditor", "Index for parent reference column #%s not found");
                    //mess = String.format(mess, propId);
                    setNotValid();
                }
                if (!prop.hasProperty()){
                    tableId = presentationClassDef.getTableId();
                }                
                this.propSqlName.setTableId(tableId);
                //setPropSqlName(propSqlName);
                
                /*String name = "";
                if (owner == Owner.TABLE) {
                    if (propSqlName.isSetTableAlias()) {
                        name = propSqlName.getTableAlias();
                    } else {
                        name = presentationClassDef.getDisplayableText(showMode);
                    }
                    name += ".";
                }
                name += prop.getDisplayableText(showMode);
                if (propSqlName.isSetPropAlias()) {
                    name += " " + propSqlName.getPropAlias();
                }
                fullName = name;
                setDisplayedInfo(showMode);*/
            } else {
                setNotValid();
            }
        }
        translator=new PropSqlNameTranslator( presentationClassDef, getOwner(prop), prop,  owner,  tableAlias,  propAlias, valid);
     }
     
    public SqmlTag_PropSqlName(final IClientEnvironment environment,final ISqmlColumnDef prop,final Owner.Enum owner,final String tableAlias) {
        super(environment);
        final ISqmlTableDef presentationClassDef = getOwner( prop);
        final Id ownerId = prop.hasProperty() ? presentationClassDef.getId() : presentationClassDef.getTableId();
        setPropSqlName(owner, prop.getId(), ownerId, tableAlias, null);
        translator=new PropSqlNameTranslator( presentationClassDef,presentationClassDef, prop,  owner,  tableAlias,  null, true);
    }
      
      private ISqmlTableDef getOwner(final ISqmlColumnDef prop) {
        ISqmlTableDef ownerTable = null;
        if(prop!=null){
            if (prop.getType() == EValType.PARENT_REF) {
                final List<ISqmlColumnDef> refColumns = prop.getReferenceColumns();
                if (!refColumns.isEmpty()) {
                    ownerTable = refColumns.get(0).getOwnerTable();
                }
            } else {
                ownerTable = prop.getOwnerTable();
            }
        }
        return ownerTable;
    }
     
    /* private void setPropSqlName(PropSqlName propSqlName) {
        this.propSqlName = PropSqlName.Factory.newInstance();
        this.propSqlName.setOwner(propSqlName.getOwner());
        this.propSqlName.setPropId(propSqlName.getPropId());
        this.propSqlName.setTableId(propSqlName.getTableId());
        if (propSqlName.isSetTableAlias()) {
            this.propSqlName.setTableAlias(propSqlName.getTableAlias());
        }
        if (propSqlName.isSetPropAlias()) {
            this.propSqlName.setPropAlias(propSqlName.getPropAlias());
        }
        if (propSqlName.isSetSql()) {
            this.propSqlName.setSql(propSqlName.getSql());
        }
    }*/
     
    private void setPropSqlName(final Owner.Enum owner,final Id propId,final Id tableId,final String tableAlias,final String propAlias) {
        propSqlName = PropSqlName.Factory.newInstance();
        this.propSqlName.setOwner(owner);
        this.propSqlName.setPropId(propId);
        this.propSqlName.setTableId(tableId);
        if ((tableAlias != null) && (!"".equals(tableAlias))) {
            this.propSqlName.setTableAlias(tableAlias);
        }
        if ((propAlias != null) && (!"".equals(propAlias))) {
            this.propSqlName.setPropAlias(propAlias);
        }
    }
     
    private void setNotValid() {
         valid=false;
        final String mess = environment.getMessageProvider().translate("SqmlEditor", "table #%s not found");
        environment.getTracer().warning(mess);

    }
      
    @Override
    protected String getSettingsPath() {
        return PATH;
    }
    
    @Override
    public XmlObject saveToXml() {
        return propSqlName;
    }
    
    @Override  
    public IScmlItem  getCopy() {   
         return new SqmlTag_PropSqlName( environment, propSqlName);
    }
    
}
