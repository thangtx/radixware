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

import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.sqml.ISqmlColumnDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.types.Reference;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.editors.scmleditor.TagInfo;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.editors.scml.IScmlItem;
import org.radixware.kernel.explorer.editors.scmleditor.sqml.tags.translator.ParentConditionTranslator;
import org.radixware.schemas.xscml.Sqml.Item.ParentCondition;
import org.radixware.schemas.xscml.Sqml.Item.ParentCondition.Operator;


public class SqmlTag_ParentCondition extends TagInfo{
    
    private ParentCondition parentCondition;
    private static final String PATH =  "org.radixware.explorer/S_E/SYNTAX_SQML/SQML_PARENT_CONDITION";
    
    public SqmlTag_ParentCondition(final IClientEnvironment environment,final ParentCondition parentCondition) {
        super(environment);
        final Id tableId = parentCondition.getTableId();
        final Id propId = parentCondition.getPropId();
        String objTitle="";
        this.parentCondition=(ParentCondition)parentCondition.copy();
        //setPropSqlName(parentCondition);
        final ISqmlTableDef presentationClassDef = environment.getSqmlDefinitions().findTableById(tableId);
        ISqmlColumnDef prop=null;
        if (presentationClassDef == null) {
            final String mess = Application.translate("SqmlEditor", "table or entity #%s not found");
            environment.getTracer().warning(String.format(mess, tableId));
            valid = false;
        } else {
            prop = presentationClassDef.getColumns().getColumnById(propId);
            if (prop == null) {
                final String mess = Application.translate("SqmlEditor", "property #%s was not found in entity %s #%s");
                environment.getTracer().warning(String.format(mess, propId, presentationClassDef.getFullName(), presentationClassDef.getId()));
                valid = false;
            } else {
                if ((prop.getType() == EValType.PARENT_REF)&&(parentCondition.isSetParentPid())) {
                   final Pid pid = new Pid(prop.getReferencedTableId(), parentCondition.getParentPid());
                   try {
                      objTitle = pid.getDefaultEntityTitle(environment.getEasSession());
                   } catch (ServiceClientException ex) {
                      objTitle = "???" + pid + "???";
                      environment.processException(ex);
                   } catch (InterruptedException ex) {
                      objTitle = "???" + pid + "???";                            
                   }
                }
                valid = false;
            }
        }
        translator=new ParentConditionTranslator( presentationClassDef,  null, 
                       prop, objTitle, null, parentCondition.getOperator(), valid);
    }
    
    public SqmlTag_ParentCondition(final IClientEnvironment environment,final ISqmlColumnDef prop,
            final Operator.Enum operator,final Reference entityReference,final String tableAlias) {
        super(environment);
        ISqmlTableDef presentationClassDef=null;
        final String objTitle="";         
        if (prop != null) {
            presentationClassDef = prop.getOwnerTable();
        }
        setParentConditionInfo(prop,presentationClassDef,operator, entityReference == null ? null : entityReference.getPid());
        translator=new ParentConditionTranslator( presentationClassDef,  entityReference, 
                       prop, objTitle, tableAlias, parentCondition.getOperator(), valid);
    } 
    
    
    public final void setParentConditionInfo(final ISqmlColumnDef prop,final ISqmlTableDef presentationClassDef,
           final Operator.Enum operator,final Pid pid) {
        if ((presentationClassDef != null) && (prop != null) && (prop.getType() == EValType.PARENT_REF)) {
            final String str_pid = pid == null ? null : pid.toString();
            setPropSqlName(operator, str_pid, prop.getId(), presentationClassDef.getId());
            valid = true;
        } else {
            valid = false;
        }
        
    }
    
    private void setPropSqlName(final Operator.Enum operator,final String parentPid,final Id propId,final Id tableId) {
        parentCondition = ParentCondition.Factory.newInstance();
        this.parentCondition.setOperator(operator);
        if (parentPid != null) {
            this.parentCondition.setParentPid(parentPid);
        }
        this.parentCondition.setPropId(propId);
        this.parentCondition.setTableId(tableId);
    }
    
     @Override  
    public IScmlItem  getCopy() {
        return new SqmlTag_ParentCondition( environment, parentCondition);
    }
     
     @Override
    protected String getSettingsPath() {
        return PATH;
    }
    
}
