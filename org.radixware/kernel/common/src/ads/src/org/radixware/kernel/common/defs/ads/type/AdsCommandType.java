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

import org.eclipse.jdt.core.compiler.CharOperation;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsScopeCommandDef;
import org.radixware.kernel.common.defs.ads.command.AdsCommandDef;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.type.AdsType.TypeJavaSourceSupport;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;


public class AdsCommandType extends AdsDefinitionType {

    public static class Factory {

        public static final AdsCommandType newInstance(AdsCommandDef command) {
            return new AdsCommandType(command);
        }
    }

    public AdsCommandType(AdsCommandDef source) {
        super(source);
    }

    @Override
    public TypeJavaSourceSupport getJavaSourceSupport() {
        return new TypeJavaSourceSupport(this) {
            @Override
            public char[][] getPackageNameComponents(UsagePurpose env, boolean isHumanReadable) {


                if (getSource().getDefinitionType() == EDefType.CONTEXTLESS_COMMAND) {
                    if (env.getEnvironment() == ERuntimeEnvironmentType.EXPLORER || env.getEnvironment() == ERuntimeEnvironmentType.WEB) {
                        return JavaSourceSupport.getPackageNameComponents(source, isHumanReadable, UsagePurpose.getPurpose(ERuntimeEnvironmentType.COMMON_CLIENT, env.getCodeType()));
                    } else {
                        return JavaSourceSupport.getPackageNameComponents(source, isHumanReadable, env);
                    }
                } else {
                    assert env.getEnvironment() == ERuntimeEnvironmentType.EXPLORER || env.getEnvironment() == ERuntimeEnvironmentType.WEB || env.getEnvironment() == ERuntimeEnvironmentType.COMMON_CLIENT;
                    AdsDefinition commandSrc = (AdsDefinition) source;
                    AdsDefinition ovr = commandSrc.getHierarchy().findOverridden().get();
                    while (ovr != null) {
                        commandSrc = ovr;
                        ovr = commandSrc.getHierarchy().findOverridden().get();
                    }
                    return JavaSourceSupport.getPackageNameComponents(((AdsScopeCommandDef) commandSrc).getOwnerClass(), isHumanReadable,/*
                             * UsagePurpose.getPurpose(ERuntimeEnvironmentType.COMMON_CLIENT, env.getCodeType()
                             */ env);
                }
            }

            @Override
            public char[] getLocalTypeName(UsagePurpose env, boolean isHumanReadable) {
                if (getSource().getDefinitionType() == EDefType.CONTEXTLESS_COMMAND) {
                    return source.getId().toCharArray();
                } else {
                    assert env.getEnvironment() == ERuntimeEnvironmentType.EXPLORER || env.getEnvironment() == ERuntimeEnvironmentType.WEB || env.getEnvironment() == ERuntimeEnvironmentType.COMMON_CLIENT;
                    AdsDefinition commandSrc = (AdsDefinition) source;
                    AdsDefinition ovr = commandSrc.getHierarchy().findOverridden().get();
                    while (ovr != null) {
                        commandSrc = ovr;
                        ovr = commandSrc.getHierarchy().findOverridden().get();
                    }
                    return CharOperation.concat(((AdsScopeCommandDef) commandSrc).getOwnerClass().getRuntimeLocalClassName(isHumanReadable).toCharArray(), JavaSourceSupport.getName(commandSrc, isHumanReadable, true), '.');
                }
            }
        };
    }

    @Override
    protected void check(RadixObject referenceContext, ERuntimeEnvironmentType env, IProblemHandler problemHandler) {
        super.check(referenceContext, env, problemHandler);
        if (source != null) {
            switch (env) {
                case EXPLORER:
                    break;
                case SERVER:
                    if (source instanceof AdsScopeCommandDef) {
                        error(referenceContext, problemHandler, "Scope command references are not allowed in server side context");
                    }
                    break;
            }
        }
    }

    @Override
    public AdsCommandDef getSource() {
        return (AdsCommandDef) super.getSource();
    }
}
