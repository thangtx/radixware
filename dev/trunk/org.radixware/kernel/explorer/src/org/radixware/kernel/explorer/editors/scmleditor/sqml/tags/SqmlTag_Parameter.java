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
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameter;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameters;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.editors.scmleditor.TagInfo;
import org.radixware.kernel.explorer.editors.scml.IScmlItem;
import org.radixware.kernel.explorer.editors.scmleditor.sqml.tags.translator.SqmlTagParameterTranslator;
import org.radixware.schemas.xscml.Sqml.Item.Parameter;


public class SqmlTag_Parameter extends TagInfo{
    
    private Parameter parameterItem;
    private static final String PATH = "org.radixware.explorer/S_E/SYNTAX_SQML/SQML_PARAMETER";
    
    private SqmlTag_Parameter(final IClientEnvironment environment){
       super(environment); 
    }
    
     public SqmlTag_Parameter(final IClientEnvironment environment, final ISqmlParameter parameter,final ISqmlTableDef contextTable, final ISqmlParameters params) {
        super(environment);
        //parameters = params;
        //this.parameter = parameter;
        parameterItem = Parameter.Factory.newInstance();
        parameterItem.setParamId(parameter.getId());
        //this.contextTable = contextTable;
        
        translator=new SqmlTagParameterTranslator( parameter,  contextTable, params, null, environment,true);
        //setDisplayedInfo(displayMode);
    }

    public SqmlTag_Parameter(final IClientEnvironment environment, final Parameter xmlParameter, final ISqmlParameters parameters,  final ISqmlTableDef contextTable) {
        super(environment);
        //this.parameters = parameters;
        //this.contextTable = contextTable;
        parameterItem = (Parameter) xmlParameter.copy();
        final ISqmlParameter parameter = parameters.getParameterById(xmlParameter.getParamId());        
        Id patamId=null;
        if (parameter == null) {
            valid = false;
            patamId=xmlParameter.getParamId();
        }else {
            if (parameters.getParameterById(parameter.getId()) == null) {
                    valid = false;                    
            }               
        }      
        translator=new SqmlTagParameterTranslator( parameter,  contextTable, parameters, patamId, environment,valid);
    }
    

    
    @Override
    public XmlObject saveToXml() {
        return parameterItem;
    }
     
   @Override  
    public IScmlItem  getCopy() {   
        final SqmlTag_Parameter res= new SqmlTag_Parameter(environment);
        res.translator=translator;
        res.parameterItem = parameterItem;
        res.valid=valid;
        return res;
    }
    
    @Override
    protected String getSettingsPath() {
        return PATH;
    }
}
