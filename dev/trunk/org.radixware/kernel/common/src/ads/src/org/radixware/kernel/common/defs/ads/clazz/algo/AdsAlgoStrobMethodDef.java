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

package org.radixware.kernel.common.defs.ads.clazz.algo;

import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.members.*;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.clazz.algo.generation.AdsStrobMethodWriter;
import org.radixware.kernel.common.enums.EMethodNature;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.AbstractMethodDefinition;


public class AdsAlgoStrobMethodDef extends AdsUserMethodDef {

    public static class Factory {

        public static AdsAlgoStrobMethodDef newInstance() {
            return new AdsAlgoStrobMethodDef("newStrob");
        }

        public static AdsAlgoStrobMethodDef newTemporaryInstance(RadixObject container) {
            AdsAlgoStrobMethodDef m = newInstance();
            m.setContainer(container);
            return m;
        }
    }
    private static final Id processIdParamId = Id.Factory.loadFrom("mprProcessId_________________");

    public AdsAlgoStrobMethodDef(String name) {
        super(name, false);

        Profile profile = getProfile();
        profile.getAccessFlags().setStatic(true);

        AdsMethodParameters params = profile.getParametersList();
        params.add(MethodParameter.Factory.newInstance(processIdParamId, "processId", AdsTypeDeclaration.Factory.newInstance(EValType.INT)));
    }

    public AdsAlgoStrobMethodDef(AbstractMethodDefinition xMethod) {
        super(xMethod);
    }

    private static AbstractMethodDefinition preprocess(AbstractMethodDefinition xMethod) {
        if (xMethod.getParameters() != null && xMethod.getParameters().getParameterList().size() == 1 && "processId".equals(xMethod.getParameters().getParameterList().get(0).getName())) {
            xMethod.getParameters().getParameterList().get(0).setId(processIdParamId);
        }
        return xMethod;
    }

    protected class StrobMethodJavaSourceSupport extends MethodJavaSourceSupport {

        public StrobMethodJavaSourceSupport(AdsMethodDef method) {
            super(method);
        }

        @Override
        public CodeWriter getCodeWriter(UsagePurpose purpose) {
            return new AdsStrobMethodWriter(this, AdsAlgoStrobMethodDef.this, purpose);
        }
    }

    @Override
    public JavaSourceSupport getJavaSourceSupport() {
        return new StrobMethodJavaSourceSupport(this);
    }

    @Override
    public EMethodNature getNature() {
        return EMethodNature.ALGO_STROB;
    }
    private static final String TYPE_TITLE = "Algorithm Strob Method";

    @Override
    public String getTypeTitle() {
        return TYPE_TITLE;
    }
}
