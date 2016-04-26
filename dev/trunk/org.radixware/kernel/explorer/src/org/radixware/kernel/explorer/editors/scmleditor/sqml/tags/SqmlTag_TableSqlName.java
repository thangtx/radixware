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

import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableReference;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.editors.scmleditor.TagInfo;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.editors.scml.IScmlItem;
import org.radixware.kernel.explorer.editors.scmleditor.sqml.tags.translator.TableSqlNameTranslator;
import org.radixware.schemas.xscml.Sqml.Item.TableSqlName;


public class SqmlTag_TableSqlName extends TagInfo{
    
    private TableSqlName tableSqlName;
    private static final String PATH = "org.radixware.explorer/S_E/SYNTAX_SQML/SQML_TABLE_SQL_NAME";
    
    public SqmlTag_TableSqlName(final IClientEnvironment environment,final TableSqlName tableSqlName){
        super(environment);
        this.tableSqlName=(TableSqlName)tableSqlName.copy();
        ISqmlTableDef presentationClassDef=null;
        valid=false;
        final Id tableId = tableSqlName.getTableId();
        try {
            presentationClassDef = environment.getSqmlDefinitions().findTableById(tableId);
            if (presentationClassDef != null) {
                valid=true;
            }           
        } catch (DefinitionError ex) { 
            final String mess = Application.translate("SqmlEditor", "table #%s not found");
            environment.getTracer().put(EEventSeverity.WARNING, String.format(mess, tableId), tableId.toString());
        } finally{
            translator=new TableSqlNameTranslator( presentationClassDef,tableSqlName.getTableAlias(), valid);
        }
    }
    
    public SqmlTag_TableSqlName(final IClientEnvironment environment,final ISqmlTableDef presentationClassDef){
        super(environment);
        valid=false;
        String alias=null;
        if (presentationClassDef != null) {
            valid=true;
            alias=presentationClassDef.getAlias();
            setTableSqlName(presentationClassDef.getId(), alias);            
        }
        translator=new TableSqlNameTranslator( presentationClassDef,alias, valid);
    }
    
    public SqmlTag_TableSqlName(final IClientEnvironment environment, final ISqmlTableReference presentationClassDef) {
        super(environment);
        ISqmlTableDef classDef=null;
        try {
            if (presentationClassDef != null) {
                setTableSqlName(presentationClassDef.getReferencedTableId(), null);            
                classDef = presentationClassDef.findReferencedTable();            
            }
        } catch (DefinitionError ex) {
            final String mess = Application.translate("SqmlEditor", "table #%s not found");
            environment.getTracer().put(EEventSeverity.WARNING, String.format(mess, presentationClassDef.findReferencedTable()), presentationClassDef.getReferencedTableId().toString());
        }finally{
            translator=new TableSqlNameTranslator( classDef, null, valid);
        }
    }   
    
    private void setTableSqlName(final Id tableId,final String alias) {
        tableSqlName = TableSqlName.Factory.newInstance();
        Id id=tableId;
        if (tableId.getPrefix() != EDefinitionIdPrefix.DDS_TABLE) {
            id = Id.Factory.changePrefix(tableId, EDefinitionIdPrefix.DDS_TABLE);
        }
        tableSqlName.setTableId(id);
        if ((alias != null) && (!alias.equals(""))) {
            tableSqlName.setTableAlias(alias);
        }
    }
    
    @Override
    protected String getSettingsPath() {
        return PATH;
    }
    
    @Override
    public XmlObject saveToXml() {
        return tableSqlName;
    }
    
    @Override  
    public IScmlItem  getCopy() {   
        return new SqmlTag_TableSqlName( environment, tableSqlName);
        
    }
}
