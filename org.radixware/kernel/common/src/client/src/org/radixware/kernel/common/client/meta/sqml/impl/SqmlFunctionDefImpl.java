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

package org.radixware.kernel.common.client.meta.sqml.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EDefinitionDisplayMode;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.meta.sqml.ISqmlFunctionDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlFunctionParameter;
import org.radixware.kernel.common.client.meta.sqml.ISqmlPackageDef;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.dds.DdsFunctionDef;
import org.radixware.kernel.common.defs.dds.DdsPackageDef;
import org.radixware.kernel.common.defs.dds.DdsParameterDef;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.client.env.KernelIcon;


final class SqmlFunctionDefImpl implements ISqmlFunctionDef {

    private final DdsFunctionDef ddsFunction;
    private final List<ISqmlFunctionParameter> parameters = new ArrayList<>();
    private final String prototype;
    private final IClientEnvironment environment;

    public SqmlFunctionDefImpl(final IClientEnvironment environment, final DdsFunctionDef function) {
        this.environment = environment;
        ddsFunction = function;
        final StringBuilder prototypeBuilder = new StringBuilder();
        if (!function.getParameters().isEmpty()) {
            prototypeBuilder.append("( ");
            boolean firstParameter = true;
            for (DdsParameterDef parameterDef : function.getParameters()) {
                parameters.add(new SqmlFunctionParameterImpl(parameterDef));
                if (firstParameter) {
                    firstParameter = false;
                } else {
                    prototypeBuilder.append(", ");
                }
                prototypeBuilder.append(parameterDef.getDbName());
                switch (parameterDef.getDirection()) {
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
                if (parameterDef.getDbType() != null && !parameterDef.getDbType().isEmpty()) {
                    prototypeBuilder.append(parameterDef.getDbType());
                }
                if (parameterDef.getDefaultVal() != null && !parameterDef.getDefaultVal().isEmpty()) {
                    prototypeBuilder.append(" = ");
                    prototypeBuilder.append(parameterDef.getDefaultVal());
                }
            }
            prototypeBuilder.append(" )");
        }
        if (function.getResultDbType() != null && !function.getResultDbType().isEmpty()) {
            prototypeBuilder.append(" return ");
            prototypeBuilder.append(function.getResultDbType());
        }
        prototype = prototypeBuilder.toString();
    }

    @Override
    public boolean isWNDS() {
        return ddsFunction.getPurityLevel().isWNDS();
    }

    @Override
    public Id getId() {
        return ddsFunction.getId();
    }

    @Override
    public String getShortName() {
        return ddsFunction.getName();
    }

    @Override
    public String getFullName() {
        return ddsFunction.getQualifiedName() + prototype;
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

    @Override
    public ClientIcon getIcon() {
        return KernelIcon.getInstance(ddsFunction.getIcon());
    }

    @Override
    public int getParametersCount() {
        return parameters.size();
    }

    @Override
    public ISqmlFunctionParameter getParameterById(Id parameterId) {
        for (ISqmlFunctionParameter parameter : parameters) {
            if (parameter.getId().equals(parameterId)) {
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
        for (Definition ownerDef = ddsFunction.getOwnerDefinition(); ownerDef != null; ownerDef = ownerDef.getOwnerDefinition()) {
            if (ownerDef instanceof DdsPackageDef) {
                return environment.getSqmlDefinitions().findPackageById(ownerDef.getId());
            }
        }
        return null;
    }

    @Override
    public String getModuleName() {
        return ddsFunction.getModule().getQualifiedName();
    }

    @Override
    public Id[] getIdPath() {
        return ddsFunction.getIdPath();
    }
    
    @Override
    public boolean isDeprecated() {
        return ddsFunction.isDeprecated();
    }
}
