/*
* Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.kernel.common.client.meta.sqml.defs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.radixware.kernel.common.client.enums.EDefinitionDisplayMode;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.KernelIcon;
import org.radixware.kernel.common.client.meta.sqml.ISqmlFunctionDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlFunctionParameter;
import org.radixware.kernel.common.client.meta.sqml.ISqmlPackageDef;
import org.radixware.kernel.common.defs.dds.DdsDefinitionIcon;
import org.radixware.kernel.common.types.Id;
import org.xml.sax.Attributes;


final class SqmlFunctionDefImpl extends SqmlDefinitionImpl implements ISqmlFunctionDef{
    
    private final boolean isWnds;
    private final String prototype;
    private final List<ISqmlFunctionParameter> parameters;
    ISqmlPackageDef packageDef;
    
    public SqmlFunctionDefImpl(final SqmlModule module, 
                                              final Attributes attributes,
                                              final List<SqmlFunctionParameterImpl> params){
        super(module, attributes);
        final StringBuilder prototypeBuilder = new StringBuilder();
        if (params==null || params.isEmpty()) {
            parameters = Collections.emptyList();
        }else{
            prototypeBuilder.append("( ");
            boolean firstParameter = true;
            parameters = new ArrayList<>(params.size());
            for (SqmlFunctionParameterImpl parameter : params) {
                parameters.add(parameter);                
                if (firstParameter) {
                    firstParameter = false;
                } else {
                    prototypeBuilder.append(", ");
                }
                prototypeBuilder.append(parameter.getDbName());
                switch (parameter.getDirection()) {
                    case IN: {
                        prototypeBuilder.append(" in ");
                        break;
                    }
                    case OUT: {
                        prototypeBuilder.append(" out ");
                        break;
                    }
                    case BOTH: {
                        prototypeBuilder.append(" in out ");
                        break;
                    }
                }
                if (parameter.getDbType() != null && !parameter.getDbType().isEmpty()) {
                    prototypeBuilder.append(parameter.getDbType());
                }
                if (parameter.getDefaultVal() != null && !parameter.getDefaultVal().isEmpty()) {
                    prototypeBuilder.append(" = ");
                    prototypeBuilder.append(parameter.getDefaultVal());
                }
            }
            prototypeBuilder.append(" )");
        }
        final String resultDbType = attributes.getValue("ResultDbType");        
        if (resultDbType != null && !resultDbType.isEmpty()) {
            prototypeBuilder.append(" return ");
            prototypeBuilder.append(resultDbType);
        }
        prototype = prototypeBuilder.toString();
        
        final int isWndsAttrIndex = attributes.getIndex("IsWNDS");
        isWnds = isWndsAttrIndex>-1 ? "true".equals(attributes.getValue(isWndsAttrIndex)) : false;
    }

    @Override
    public ClientIcon getIcon() {
        return KernelIcon.getInstance(DdsDefinitionIcon.FUNCTION);
    }

    @Override
    public boolean isWNDS() {
        return isWnds;
    }

    @Override
    public int getParametersCount() {
        return parameters.size();
    }

    @Override
    public ISqmlFunctionParameter getParameterById(Id parameterId) {
        for (ISqmlFunctionParameter parameter: parameters){
            if (parameterId.equals(parameter.getId())){
                return parameter;
            }
        }
        return null;
    }

    @Override
    public ISqmlFunctionParameter getParameter(int index) {
        return parameters.get(index);
    }

    @Override
    public List<ISqmlFunctionParameter> getAllParameters() {
        return Collections.unmodifiableList(parameters);
    }

    @Override
    public ISqmlPackageDef getPackage() {
        return packageDef;
    }

    @Override
    public Id[] getIdPath() {
        return new Id[]{packageDef.getId(), packageDef.getId(), getId()};//DdsFunctionDef.getIdPath() returns path with double package id
    }
    
    @Override
    public String getFullName() {
        final StringBuilder nameBuilder = new StringBuilder(getModuleName());
        nameBuilder.append("::");
        nameBuilder.append(getPackage().getShortName());
        nameBuilder.append(':');
        nameBuilder.append(getShortName());
        nameBuilder.append(prototype);
        return nameBuilder.toString();
    }

    @Override
    public String getTitle() {
        return getShortName() + prototype;
    }

    @Override
    public String getDisplayableText(EDefinitionDisplayMode mode) {
        if (mode == EDefinitionDisplayMode.SHOW_FULL_NAMES) {
            return getFullName();
        } else {
            return getShortName();
        }
    }    
}
