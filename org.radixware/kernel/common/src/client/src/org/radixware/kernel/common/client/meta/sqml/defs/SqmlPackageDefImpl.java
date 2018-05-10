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
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.KernelIcon;
import org.radixware.kernel.common.client.meta.sqml.ISqmlFunctionDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlPackageDef;
import org.radixware.kernel.common.defs.dds.DdsDefinitionIcon;
import org.radixware.kernel.common.types.Id;
import org.xml.sax.Attributes;


final class SqmlPackageDefImpl extends SqmlDefinitionImpl implements ISqmlPackageDef{
    
    private final List<ISqmlFunctionDef> functions;
    
    public SqmlPackageDefImpl(final SqmlModule module, final Attributes attributes, final List<SqmlFunctionDefImpl> funcs){
        super(module, attributes);
        if (funcs==null || funcs.isEmpty()){
            functions = Collections.emptyList();
        }else{
            functions = new ArrayList<>(funcs.size());
            for (SqmlFunctionDefImpl function: funcs){
                function.packageDef = this;
                functions.add(function);
            }
        }
    }

    @Override
    public ClientIcon getIcon() {
        return KernelIcon.getInstance(DdsDefinitionIcon.PACKAGE);
    }

    @Override
    public int getFunctionsCount() {
        return functions.size();
    }

    @Override
    public ISqmlFunctionDef getFunctionById(Id functionId) {
        for (ISqmlFunctionDef function: functions){
            if (functionId.equals(function.getId())){
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

}
