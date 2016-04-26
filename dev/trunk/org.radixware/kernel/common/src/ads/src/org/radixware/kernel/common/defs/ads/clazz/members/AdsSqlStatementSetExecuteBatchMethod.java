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

package org.radixware.kernel.common.defs.ads.clazz.members;

import org.radixware.kernel.common.defs.ads.clazz.sql.AdsSqlClassDef;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.schemas.adsdef.AbstractMethodDefinition;


public class AdsSqlStatementSetExecuteBatchMethod extends AdsSqlMethodDef {

    private final AdsMethodDef.Profile profile = new ProfileImpl();
    private static final Id sizeParamId = Id.Factory.loadFrom("mprSize______________________");

    private class ProfileImpl extends AdsMethodDef.Profile {

        public ProfileImpl() {
            getAccessFlags().setStatic(true);
        }

        @Override
        public AdsMethodParameters getParametersList() {
            final AdsMethodParameters parameters = super.getParametersList();
            if (parameters.size() == 1) {
                final MethodParameter existingParam = parameters.get(0);
                if (Utils.equals(existingParam.getName(), "size") && Utils.equals(existingParam.getType(), AdsTypeDeclaration.Factory.newPrimitiveType("int"))) {

                    return parameters;
                }
            }
            parameters.add(MethodParameter.Factory.newInstance(sizeParamId, "size", AdsTypeDeclaration.Factory.newPrimitiveType("int")));
            return parameters;
        }

        @Override
        protected boolean isPersistent() {
            return false;
        }
    }

    protected AdsSqlStatementSetExecuteBatchMethod() {
        super(AdsSystemMethodDef.ID_STMT_SET_EXECUTE_BATCH, "setExecuteBatch");
    }

    protected AdsSqlStatementSetExecuteBatchMethod(AbstractMethodDefinition xMethod) {
        super(preprocess(xMethod));
    }

    private static AbstractMethodDefinition preprocess(AbstractMethodDefinition xMethod) {
        if (xMethod.getParameters() != null && xMethod.getParameters().getParameterList().size() == 1 && "size".equals(xMethod.getParameters().getParameterList().get(0).getName())) {
            xMethod.getParameters().getParameterList().get(0).setId(sizeParamId);
        }
        return xMethod;
    }

    @Override
    public AdsSqlClassDef getOwnerClass() {
        return (AdsSqlClassDef) super.getOwnerClass();
    }

    @Override
    public Profile getProfile() {
        return profile;
    }

    @Override
    public boolean canDelete() {
        return false;
    }

    @Override
    protected boolean isPersistent() {
        return false;
    }
}
