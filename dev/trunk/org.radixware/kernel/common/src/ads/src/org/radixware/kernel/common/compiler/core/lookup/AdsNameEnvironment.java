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

package org.radixware.kernel.common.compiler.core.lookup;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.internal.compiler.env.INameEnvironment;
import org.eclipse.jdt.internal.compiler.env.NameEnvironmentAnswer;
import org.radixware.kernel.common.compiler.lookup.AdsWorkspace;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.HierarchyWalker;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.fs.IJarDataProvider;
import org.radixware.kernel.common.types.Id;


public class AdsNameEnvironment implements INameEnvironment {

    private final Layer context;
    private AdsJavaPackage packagesRoot;
    private ERuntimeEnvironmentType env;

    public AdsNameEnvironment(Layer context, ERuntimeEnvironmentType env) {
        this.context = context;
        this.env = env;
        this.packagesRoot = new AdsJavaPackage(context, env);
    }

    public ERuntimeEnvironmentType getEnvironment() {
        return env;
    }

    public Layer getLayer() {
        return context;
    }

    @Override
    public NameEnvironmentAnswer findType(char[][] compoundTypeName) {
        char[][] packageName;
        char[][] useName = compoundTypeName;
        char[][] lastNameParts = CharOperation.splitOn('$', compoundTypeName[compoundTypeName.length - 1]);
        int startFrom = 1;
        if (lastNameParts.length > 1) {
            final char[][] newname = new char[compoundTypeName.length + lastNameParts.length - 1][];
            System.arraycopy(compoundTypeName, 0, newname, 0, compoundTypeName.length - 1);
            System.arraycopy(lastNameParts, 0, newname, compoundTypeName.length - 1, lastNameParts.length);
            useName = newname;
            startFrom = lastNameParts.length;
        }
        for (int iteration = startFrom; iteration < useName.length; iteration++) {
            packageName = new char[useName.length - iteration][];
            System.arraycopy(useName, 0, packageName, 0, packageName.length);
            final char[][] typeName = new char[iteration][];
            System.arraycopy(useName, useName.length - iteration, typeName, 0, typeName.length);

            final NameEnvironmentAnswer answer = findClass(packageName, typeName);
            if (answer != null) {
                return answer;
            }
        }
        return null;
    }

    public void ensureClassPath(final AdsWorkspace ws) {
        Layer.HierarchyWalker walker = new Layer.HierarchyWalker();
        final Set<Id> addedModules = new HashSet<>();
        walker.go(getLayer(), new Layer.HierarchyWalker.DefaultAcceptor() {
            @Override
            public void accept(HierarchyWalker.Controller<Object> controller, Layer radixObject) {
                for (Module module : radixObject.getAds().getModules()) {
                    if (!addedModules.contains(module.getId())) {
                        packagesRoot.attachLocations(module, ws);
                        addedModules.add(module.getId());
                    }
                }
            }
        });
    }

    @Override
    public NameEnvironmentAnswer findType(char[] typeName, char[][] packageName) {

        return findClass(packageName, new char[][]{typeName});
    }

    public NameEnvironmentAnswer findType(Definition definition, String suffix) {
        return packagesRoot.findAnswer(definition, suffix);

    }

    @Override
    public boolean isPackage(char[][] parentPackageName, char[] packageName) {
        if (parentPackageName == null) {//root
            return packagesRoot.findPackage(packageName) != null;
        } else {
            AdsJavaPackage knownPackage = packagesRoot.findPackage(parentPackageName);
            if (knownPackage != null) {//parent package is well known
                AdsJavaPackage childPackage = knownPackage.findPackage(packageName);
                if (childPackage != null) {
                    return true;
                }
            }
            return false;
        }
    }

    private NameEnvironmentAnswer findClass(char[][] packageName, char[][] typeName) {
        AdsJavaPackage pkg = packagesRoot.findPackage(packageName);
        if (pkg != null) {
            return pkg.findAnswer(typeName);
        } else {
            return null;
        }
    }

    public interface NameRequest {

        boolean accept(char[][] packageName, char[] className, ClassDataProvider provider);
    }

    public interface PackageNameRequest extends NameRequest {

        public char[][] getPackageName();
    }

    public interface PackageNamePackageRequest extends PackageNameRequest {

        boolean accept(char[][] packageName);
    }

    public interface XmlNameRequest {

        boolean accept(XBeansDataProvider provider);
    }

    public interface ClassDataProvider {

        NameEnvironmentAnswer getAnswer();

        byte[] getClassFileBytes();
    }

    public interface XBeansDataProvider {

        public String getTypeSystemName();

        public String[] getNamespaces();

        public String[] getSourceNames();

        public InputStream getElementDataStream(String path);

        public InputStream getXmlSourceStream(String sourceName);

        public IJarDataProvider getDataSource();
    }

    public void invokeRequest(NameRequest request) {
        packagesRoot.invokeRequest(request);
    }

    public void invokeRequest(XmlNameRequest request) {
        packagesRoot.invokeRequest(request);
    }

    @Override
    public void cleanup() {
    }
}
