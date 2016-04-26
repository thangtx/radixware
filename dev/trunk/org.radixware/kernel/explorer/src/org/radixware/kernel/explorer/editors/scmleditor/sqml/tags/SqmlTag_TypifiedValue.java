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

import java.util.Collection;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.meta.sqml.ISqmlColumnDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlEnumDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.utils.ValueConverter;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.exceptions.WrongFormatError;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.editors.scmleditor.TagInfo;
import org.radixware.kernel.explorer.editors.scml.IScmlItem;
import org.radixware.kernel.explorer.editors.scmleditor.sqml.tags.translator.TypifiedValueTranslator;
import org.radixware.schemas.xscml.Sqml.Item.TypifiedValue;


public class SqmlTag_TypifiedValue extends TagInfo{
    
    private static final String PATH = "org.radixware.explorer/S_E/SYNTAX_SQML/SQML_TYPIFIED_VALUE";
    private TypifiedValue typifiedValue;     
    
    public SqmlTag_TypifiedValue(final IClientEnvironment environment,final TypifiedValue typifiedValue) {
        super(environment);
        this.typifiedValue = (TypifiedValue)typifiedValue.copy();
        final Id tableId = typifiedValue.getTableId();
        final Id propId = typifiedValue.getPropId();
        valid = false;
        ISqmlColumnDef prop=null;
        Object val = null;
        try {
            final ISqmlTableDef presentationClassDef = environment.getSqmlDefinitions().findTableById(tableId);
            prop = presentationClassDef.getColumns().getColumnById(propId);
            if (prop != null) {
                val = ValAsStr.fromStr(typifiedValue.getValue(), ValueConverter.serverValType2ClientValType(prop.getType()));
                //boolean isLiteral = typifiedValue.getLiteral();
                //setTypifiedValue(typifiedValue);
                getPropInfo( prop, val);                
            } else {
                valid = false;
            }            
        } catch (DefinitionError ex) {
            final String mess = environment.getMessageProvider().translate("SqmlEditor", "table #%s not found");
            environment.getTracer().warning(String.format(mess, tableId));
            valid = false;
        } catch (WrongFormatError error) {
            final String mess = environment.getMessageProvider().translate("SqmlEditor", "Can't restore typified value '%s': %s");
            final String reason = ClientException.getExceptionReason(environment.getMessageProvider(), error);
            environment.getTracer().error(String.format(mess, typifiedValue.getValue(), reason));
            valid = false;
        }
        translator=new TypifiedValueTranslator(valid,  prop,val,environment);
    }
    
    public SqmlTag_TypifiedValue(final IClientEnvironment environment,final ISqmlColumnDef prop,final Object value){
        super(environment);
        try {
            final ISqmlTableDef presentationClassDef = prop.getOwnerTable();
            if (presentationClassDef != null) {
                final Id tableId = presentationClassDef.getId();
                setTypifiedValue(prop, tableId,value, true);
                getPropInfo(prop,value);
            } else {
                valid=false;
            }
        } catch (DefinitionError ex) {
            final String mess = environment.getMessageProvider().translate("SqmlEditor", "table #%s not found");
            environment.getTracer().put(EEventSeverity.WARNING, String.format(mess, prop.getOwnerTableId()), prop.getOwnerTableId().toString());
            valid=false;
        }
        translator=new TypifiedValueTranslator(valid,  prop,value,environment);
    }    
        
    private void getPropInfo(final ISqmlColumnDef prop,final Object value) {
        final Collection<ISqmlEnumDef> enumDefs = prop.getEnums();
        if (enumDefs.size()==1) {
            final ISqmlEnumDef enumDef = enumDefs.iterator().next();
            if (enumDef.findItemByValue(value.toString()) == null){
                valid = false;
                throw new NoConstItemWithSuchValueError("Constant item with Value=\"" + value + "\" was not found", value);
            }            
        } else if (prop.getType() == EValType.PARENT_REF) {
            try {
                //value=
                ((Pid) value).getDefaultEntityTitle(environment.getEasSession());                
            } catch (ServiceClientException ex) {
                valid = false;
                environment.processException(ex);
            } catch (InterruptedException ex) {
                valid = false;
            }
        }       
    }   

    private void setTypifiedValue(final ISqmlColumnDef prop,final Id tableId,final Object value,final boolean isLiteral) {
        this.typifiedValue = TypifiedValue.Factory.newInstance();
        this.typifiedValue.setTableId(tableId);
        this.typifiedValue.setPropId(prop.getId());
        this.typifiedValue.setLiteral(isLiteral);
        if (value != null) {
            this.typifiedValue.setValue(ValAsStr.toStr(value, prop.getType())/*ValueConverter.objVal2ValAsStr(setValue(val),prop.getType())*/);
        }
    }

    @Override
    protected String getSettingsPath() {
        return PATH;
    }
    
    @Override
    public XmlObject saveToXml() {
        return typifiedValue;
    }
    
    @Override  
    public IScmlItem  getCopy() {   
        return new SqmlTag_TypifiedValue(environment, typifiedValue);
    }
    
}
