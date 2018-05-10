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
package org.radixware.kernel.common.svn.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;

/**
 *
 * @author npopov
 */
class BranchClassLoader extends ClassLoader {

    private static final int EXPECTED_CLASS_CNT = 300000;
    
    private final BranchHolder branchHolder;
    private final ERuntimeEnvironmentType env;
    private final Map<String, ClassMetaInfo> class2MetaMap = new HashMap<>(EXPECTED_CLASS_CNT);

    public BranchClassLoader(BranchHolder branchHolder, ERuntimeEnvironmentType env) {
        this.branchHolder = branchHolder;
        this.env = env;

        initClassesMap();

        branchHolder.verifier.message("Start preload branch files...");
        final long millis = System.currentTimeMillis();
        final int nThreads = 20;
        branchHolder.preloadLayersBinares(ERuntimeEnvironmentType.SERVER, nThreads);
        branchHolder.verifier.message("Finish preload branch files for: " + (System.currentTimeMillis() - millis) + "ms");
    }
    
    private void initClassesMap() {
        final List<Utils.LayerInfo> sortedFromTop = Utils.sortAllLayers(branchHolder.layers.values());
        Collections.reverse(sortedFromTop);

        for (Utils.LayerInfo l : sortedFromTop) {
            DirectoryXmlParser parser = new DirectoryXmlParser(l, true);
            long totalClassesCnt = 0;
            for (Utils.ModuleInfo m : l.modules.values()) {
                final byte[] dirXmlData = m.findDirectoryIndexData();
                if (dirXmlData == null) {
                    branchHolder.verifier.error("Not found directory.xml for module: " + m.getDisplayName());
                    continue;
                }
                final Set<String> classes = parser.getDefinedClasses(dirXmlData);
                mergeClasses(l, classes, false);
                totalClassesCnt += classes.size();
            }
            for (ERuntimeEnvironmentType e : ERuntimeEnvironmentType.values()) {
                if (l.getKernelDirectoryXmlFiles(e) == null) {
                    continue;
                }
                for (String dirXmlPath : l.getKernelDirectoryXmlFiles(e)) {
                    final Set<String> classes = parser.getDefinedClasses(dirXmlPath);
                    mergeClasses(l, classes, true);
                    totalClassesCnt += classes.size();
                }
            }
            branchHolder.verifier.message(String.format("For layer %s found %d classes", l.uri, totalClassesCnt));
        }
    }

    private void mergeClasses(Utils.LayerInfo l, Set<String> addClasses, boolean isKernel) {
        for (String className : addClasses) {
            if (class2MetaMap.containsKey(className)) {
                continue;
            }
            class2MetaMap.put(className, new ClassMetaInfo(isKernel, l.uri));
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        final ClassMetaInfo meta = class2MetaMap.get(name);
        byte[] bytes = null;
        if (meta != null) {
            if (meta.isKernel) {
                bytes = ClassLoaderUtilExt.findClassBytesBySlashSeparatedName(name.replace('.', '/'), branchHolder.layers.get(meta.layerUri), env);
            } else {
                bytes = ClassLoaderUtilExt.findClassFromADS(name, branchHolder.layers.get(meta.layerUri), env);
            }
        }
        if (bytes == null) {
            throw new ClassNotFoundException(name);
        }
        final int index = name.lastIndexOf('.');
        if (index != -1) {
            String pkgname = name.substring(0, index);
            Package pkg = getPackage(pkgname);
            if (pkg == null) {
                definePackage(pkgname, null, null, null, null, null, null, null);
            }
        }
        return defineClass(name, bytes, 0, bytes.length);
    }

    private static class ClassMetaInfo {

        private final boolean isKernel;
        private final String layerUri;

        public ClassMetaInfo(boolean isKernel, String layerUri) {
            this.isKernel = isKernel;
            this.layerUri = layerUri;
        }
    }
}
