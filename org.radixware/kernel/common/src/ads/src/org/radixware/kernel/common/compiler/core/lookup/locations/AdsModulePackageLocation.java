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
package org.radixware.kernel.common.compiler.core.lookup.locations;

import java.util.LinkedList;
import java.util.List;
import org.eclipse.jdt.internal.compiler.env.NameEnvironmentAnswer;
import org.radixware.kernel.common.compiler.core.lookup.AdsJavaPackage;
import org.radixware.kernel.common.compiler.core.lookup.AdsNameEnvironment;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.utils.CharOperations;

public abstract class AdsModulePackageLocation extends PackageLocation {

    protected final AdsModule module;
    protected final ERuntimeEnvironmentType env;
    protected char[][][] basePackageName;
    protected static final char[] PACKAGE_NAME_PREFIX_XSD = new char[]{'x', 's', 'd'};
    protected static final char[] PACKAGE_NAME_PREFIX_MSDL = new char[]{'s', 'm', 'd'};

    public AdsModulePackageLocation(AdsModule module, ERuntimeEnvironmentType env) {
        super(module.getLayer());
        this.module = module;
        this.env = env;
        List<AdsModule> modules = new LinkedList<>();
        for (AdsModule m = module; m != null; m = m.findOverwritten()) {
            modules.add(m);
        }
        basePackageName = new char[modules.size()][][];
        for (int i = 0; i < basePackageName.length; i++) {
            basePackageName[i] = JavaSourceSupport.getPackageNameComponents(modules.get(i), JavaSourceSupport.UsagePurpose.getPurpose(env, JavaSourceSupport.CodeType.EXCUTABLE), true);
        }
    }

    @Override
    public void createPackages(AdsJavaPackage root) {
        for (int i = 0; i < basePackageName.length; i++) {
            createPackages(root, basePackageName[i]);
        }
    }

    protected boolean prefixEquals(char[][] packageName) {
        main:
        for (int b = 0; b < basePackageName.length; b++) {
            final int matchCount = Math.min(packageName.length, basePackageName[b].length - 1);
            for (int i = 0; i < matchCount; i++) {
                if (!CharOperations.equals(packageName[i], basePackageName[b][i])) {
                    continue main;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public NameEnvironmentAnswer findAnswer(char[][] packageName, char[][] typeName) {
        return null;
    }

    @Override
    public boolean invokeRequest(AdsNameEnvironment.NameRequest request) {
        return true;
    }

    @Override
    public boolean invokeRequest(AdsNameEnvironment.XmlNameRequest request) {
        return true;
    }

    @Override
    public NameEnvironmentAnswer findAnswer(Definition definition, String suffix) {
        return null;
    }
}
