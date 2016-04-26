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

import org.radixware.kernel.common.client.enums.EDefinitionDisplayMode;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.meta.sqml.ISqmlFunctionParameter;
import org.radixware.kernel.common.defs.dds.DdsParameterDef;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.client.env.KernelIcon;


final class SqmlFunctionParameterImpl implements ISqmlFunctionParameter {

    private final DdsParameterDef ddsParameter;

    public SqmlFunctionParameterImpl(final DdsParameterDef parameter) {
        ddsParameter = parameter;
    }

    @Override
    public Id getId() {
        return ddsParameter.getId();
    }

    @Override
    public String getShortName() {
        return ddsParameter.getName();
    }

    @Override
    public String getFullName() {
        return ddsParameter.getName();
    }

    @Override
    public String getTitle() {
        return getShortName();
    }

    @Override
    public String getDisplayableText(EDefinitionDisplayMode mode) {
        return getShortName();
    }

    @Override
    public ClientIcon getIcon() {
        return KernelIcon.getInstance(ddsParameter.getIcon());
    }

    @Override
    public String getDefaultVal() {
        return ddsParameter.getDefaultVal();
    }

    @Override
    public String getModuleName() {
        return ddsParameter.getModule().getQualifiedName();
    }

    @Override
    public Id[] getIdPath() {
        return ddsParameter.getIdPath();
    } 
    
    @Override
    public boolean isDeprecated() {
        return ddsParameter.isDeprecated();
    }
}
