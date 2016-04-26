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

import java.util.Map;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.sqml.ISqmlFunctionDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlFunctionParameter;
import org.radixware.kernel.common.client.meta.sqml.ISqmlPackageDef;
import org.radixware.kernel.common.types.Id;


public class DbFuncCallTranslator  extends  SqmlTagTranslator{
    
    private final ISqmlFunctionDef dbFunction;
    private final Map<Id, String> funcParameters;
    //private final IClientEnvironment environment_;
    
    public DbFuncCallTranslator(final ISqmlFunctionDef dbFunction,final Map<Id, String> funcParameters,final IClientEnvironment environment,final boolean isValid){
        super(isValid);
        this.dbFunction=dbFunction;
        this.funcParameters=funcParameters;
        //environment_=environment;
    }
    
  
    @Override
    public String getDisplayString() {
        //if (isValid && dbFunction != null){
            return calculateTagTitle();
        //}
    }
    
    /*private String calculateToolTip() {
        final MessageProvider msgProvider = environment_.getMessageProvider();
        final String functionStr = msgProvider.translate("SqmlEditor", "Function '%s'");
        final String locationStr = msgProvider.translate("SqmlEditor", "Location: %s");
        final StringBuilder tooltipBuilder = new StringBuilder();
        tooltipBuilder.append("<b>");
        tooltipBuilder.append(String.format(functionStr, dbFunction.getShortName()));
        tooltipBuilder.append("</b>");
        final ISqmlPackageDef packageDef = dbFunction.getPackage();
        if (packageDef != null) {
            tooltipBuilder.append("<br>&nbsp;&nbsp;&nbsp;&nbsp;");
            tooltipBuilder.append(String.format(locationStr, packageDef.getFullName()));
            tooltipBuilder.append("</br>");
        }
        return tooltipBuilder.toString();
    }*/

    private String calculateTagTitle() {
        final StringBuilder titleBuilder = new StringBuilder();
        final ISqmlPackageDef packageDef = dbFunction.getPackage();
        if (packageDef != null) {
            titleBuilder.append(packageDef.getDisplayableText(displayMode));
            titleBuilder.append('.');
        }
        titleBuilder.append(dbFunction.getShortName());
        titleBuilder.append('(');
        if (funcParameters != null) {
            boolean firstParameter = true;
            for (ISqmlFunctionParameter parameter : dbFunction.getAllParameters()) {
                if (firstParameter) {
                    firstParameter = false;
                } else {
                    titleBuilder.append(", ");
                }
                titleBuilder.append(parameter.getShortName());
                titleBuilder.append(" => ");
                titleBuilder.append(String.valueOf(funcParameters.get(parameter.getId())));
            }
        }
        titleBuilder.append(')');
        return titleBuilder.toString();
    }
    
}
