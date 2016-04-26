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

import org.eclipse.jdt.internal.compiler.env.NameEnvironmentAnswer;
import org.radixware.kernel.common.compiler.core.lookup.AdsJavaPackage;
import org.radixware.kernel.common.compiler.core.lookup.AdsNameEnvironment;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.repository.Layer;

public abstract class PackageLocation {

    protected final Layer contextLayer;

    public PackageLocation(Layer contextLayer) {
        this.contextLayer = contextLayer;
    }

    public abstract boolean containsPackageName(char[][] packageName);

    public abstract void createPackages(AdsJavaPackage root);

    public void createPackages(AdsJavaPackage packagesRoot, char[][] names) {
        AdsJavaPackage pkg = packagesRoot;
        for (int i = 0; i < names.length; i++) {
            pkg = pkg.findOrCreatePackage(names[i]);
            pkg.addLocation(this);
        }
        packagesRoot.addLocation(this);
    }

    public abstract NameEnvironmentAnswer findAnswer(char[][] packageName, char[][] typeName);

    public abstract NameEnvironmentAnswer findAnswer(Definition definition, String suffix);

    public abstract boolean invokeRequest(AdsNameEnvironment.NameRequest request);

    public abstract boolean invokeRequest(AdsNameEnvironment.XmlNameRequest request);
}
