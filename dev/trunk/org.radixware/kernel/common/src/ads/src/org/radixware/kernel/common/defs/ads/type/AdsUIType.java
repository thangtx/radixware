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

package org.radixware.kernel.common.defs.ads.type;

import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.type.AdsType.TypeJavaSourceSupport;
import org.radixware.kernel.common.defs.ads.ui.AdsAbstractUIDef;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;


public class AdsUIType extends AdsDefinitionType {

    public AdsUIType(final AdsAbstractUIDef source) {
        super(source);
    }

    @Override
    public TypeJavaSourceSupport getJavaSourceSupport() {//changed by yremizov
        return new TypeJavaSourceSupport(this) {

            @Override
            public char[][] getPackageNameComponents(final UsagePurpose env, boolean isHumanReadable) {
                return JavaSourceSupport.getPackageNameComponents(AdsUIType.this.source, isHumanReadable, env);
            }

            @Override
            public char[] getLocalTypeName(final UsagePurpose env, boolean isHumanReadable) {
                if (env.getEnvironment() == ERuntimeEnvironmentType.EXPLORER || env.getEnvironment() == ERuntimeEnvironmentType.WEB) {
//                    if (AdsUIType.this.source instanceof AdsCustomPageEditorDef) {
//                        return (AdsUIType.this.source.getId().toString() + "_" + ((AdsCustomPageEditorDef) AdsUIType.this.source).getOwnerEditorPage().getOwnerDef().getId()).toCharArray();
//                    } else {
                    return JavaSourceSupport.getName(source, isHumanReadable);
                    //}
                } else {
                    return "???".toCharArray();
                }
            }
        };
    }

    @Override
    public AdsAbstractUIDef getSource() {
        return (AdsAbstractUIDef) super.getSource();
    }

    @Override
    protected void check(final RadixObject referenceContext, final ERuntimeEnvironmentType env, final IProblemHandler problemHandler) {
        super.check(referenceContext, env, problemHandler);
        if (getSource() != null) {
            if (env != getSource().getUsageEnvironment()) {
                error(referenceContext, problemHandler, "Custom view based type references are allowed in specific client side context only");
            }
        }
    }
}
