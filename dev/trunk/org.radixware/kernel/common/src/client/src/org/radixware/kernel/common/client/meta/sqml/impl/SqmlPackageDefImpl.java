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
import org.radixware.kernel.common.client.meta.sqml.ISqmlPackageDef;
import org.radixware.kernel.common.defs.dds.DdsFunctionDef;
import org.radixware.kernel.common.defs.dds.DdsPackageDef;
import org.radixware.kernel.common.defs.dds.DdsPlSqlObjectItemDef;
import org.radixware.kernel.common.defs.dds.DdsPrototypeDef;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.client.env.KernelIcon;


final class SqmlPackageDefImpl implements ISqmlPackageDef {

    private final DdsPackageDef ddsPackage;
    private final List<ISqmlFunctionDef> functions = new ArrayList<ISqmlFunctionDef>();

    public SqmlPackageDefImpl(final IClientEnvironment environment, final DdsPackageDef packageDef) {
        ddsPackage = packageDef;
        DdsFunctionDef function;
        for (DdsPlSqlObjectItemDef sqlItem : packageDef.getHeader().getItems()) {
            if (sqlItem instanceof DdsPrototypeDef) {
                function = ((DdsPrototypeDef) sqlItem).findFunction();
                if (function != null && function.isPublic()) {
                    functions.add(new SqmlFunctionDefImpl(environment, function));
                }
            }
        }
    }

    @Override
    public Id getId() {
        return ddsPackage.getId();
    }

    @Override
    public String getShortName() {
        return ddsPackage.getName();
    }

    @Override
    public String getFullName() {
        return ddsPackage.getQualifiedName();
    }

    @Override
    public String getTitle() {
        return getShortName();
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
        return KernelIcon.getInstance(ddsPackage.getIcon());
    }

    @Override
    public int getFunctionsCount() {
        return functions.size();
    }

    @Override
    public ISqmlFunctionDef getFunctionById(Id functionId) {
        for (ISqmlFunctionDef function : functions) {
            if (function.getId().equals(functionId)) {
                return function;
            }
        }
        return null;
    }

    @Override
    public ISqmlFunctionDef getFunction(int index) {
        return functions.get(index);
    }

    @Override
    public List<ISqmlFunctionDef> getAllFunctions() {
        return Collections.unmodifiableList(functions);
    }

    @Override
    public String getModuleName() {
        return ddsPackage.getModule().getQualifiedName();
    }

    @Override
    public Id[] getIdPath() {
        return ddsPackage.getIdPath();
    }
    
    @Override
    public boolean isDeprecated() {
        return ddsPackage.isDeprecated();
    }
}
