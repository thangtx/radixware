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

import java.util.HashMap;
import java.util.Map;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.sqml.ISqmlDefinitions;
import org.radixware.kernel.common.client.meta.sqml.ISqmlFunctionDef;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.editors.scmleditor.TagInfo;
import org.radixware.kernel.explorer.editors.scml.IScmlItem;
import org.radixware.kernel.explorer.editors.scmleditor.sqml.tags.translator.DbFuncCallTranslator;
import org.radixware.schemas.xscml.Sqml.Item.DbFuncCall;


public class SqmlTag_DbFuncCall extends TagInfo{
    
    private DbFuncCall xmlFuncCall;
    private static final String PATH = "org.radixware.explorer/S_E/SYNTAX_SQML/SQML_DB_FUNC_CALL";
    
    public SqmlTag_DbFuncCall(final IClientEnvironment environment, final DbFuncCall funcCall){
        super(environment);
        final MessageProvider msgProvider = environment.getMessageProvider();
        final ISqmlDefinitions definitions = environment.getSqmlDefinitions();
        final ISqmlFunctionDef  dbFunction = definitions.findFunctionById(funcCall.getFuncId());
        Map<Id, String> funcParameters =null;
        if (dbFunction == null) {
            final String message = msgProvider.translate("TraceMessage", "Data base function #%s was not found");
            environment.getTracer().error(String.format(message, funcCall.getFuncId()));
            //setDisplayedInfo(null, "???" + funcCall.getFuncId() + "()???");
            valid = false;
            xmlFuncCall = null;
        } else {
            final DbFuncCall.Params params = funcCall.getParams();
            if (params != null && params.getParamList() != null) {
                for (DbFuncCall.Params.Param param : params.getParamList()) {
                    if (dbFunction.getParameterById(param.getParamId()) == null) {
                        final String message = msgProvider.translate("TraceMessage", "Function parameter #%s was not found in '%s'");
                        environment.getTracer().error(String.format(message, param.getParamId(), dbFunction.getTitle()));
                        //setDisplayedInfo(null, dbFunction.getFullName() + "(???" + param.getParamId() + "???)");
                        valid = false;
                        xmlFuncCall = null;
                        return;
                    } else {
                        //if (funcParameters == null) {
                        funcParameters = new HashMap<>();
                        //}
                        funcParameters.put(param.getParamId(), param.getValue());
                    }
                }
            }
            xmlFuncCall = (DbFuncCall) funcCall.copy();
            //setDisplayedInfo(displayMode);
        }
        translator=new DbFuncCallTranslator(dbFunction, funcParameters,environment, valid);
    }
    
    @Override
    protected String getSettingsPath() {
        return PATH;
    }
    
    @Override  
    public IScmlItem  getCopy() {
        return new SqmlTag_DbFuncCall( environment, xmlFuncCall);
    }
    
    @Override
    public XmlObject saveToXml() {
        return xmlFuncCall;
    }
    
}
