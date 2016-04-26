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

package org.radixware.kernel.server.arte;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.radixware.kernel.common.enums.EShareabilityArea;
import org.radixware.kernel.common.environment.IRadixClassLoader;
import org.radixware.kernel.common.environment.IRadixEnvironment;
import org.radixware.kernel.common.lang.RadMetaClass;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.starter.radixloader.FileCache;
import org.radixware.kernel.starter.radixloader.RadixClassLoader;
import org.radixware.kernel.starter.radixloader.RadixLoader;
import org.radixware.schemas.product.Directory;


public class ReleaseSharedClassloader extends RadixClassLoader implements IRadixClassLoader, IVersionedClassloader {

    private static final Set<String> CLASS_GROUPS;

    static {
        final Set<String> grps = new HashSet<>();
        grps.add(Directory.FileGroups.FileGroup.GroupType.ADS_COMMON.toString());
        grps.add(Directory.FileGroups.FileGroup.GroupType.ADS_SERVER.toString());
        grps.add(Directory.FileGroups.FileGroup.GroupType.DDS.toString());
        CLASS_GROUPS = Collections.unmodifiableSet(grps);
    }
    private final Release release;

    public ReleaseSharedClassloader(Release release, RadixLoader loader, ClassLoader parent) {
        super(release.getRevisionMeta(), loader, CLASS_GROUPS, parent);
        this.release = release;
    }

    @Override
    public Class loadMetaClassById(final Id id) throws ClassNotFoundException {
        return loadClass(release.getAdsMetaClassNameById(id));
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve, final Object hint) throws ClassNotFoundException {
        Class<?> result = super.loadClass(name, resolve, hint);
        if (result.getClassLoader() == this && !isReallyShared(result)) {
            throw new ClassNotFoundException(name);
        }
        return result;
    }

    @Override
    protected boolean loadingByParentForbidden(String name, Object hint) {
        return isMetaContextProvider(name) || super.loadingByParentForbidden(name, hint);
    }

    @Override
    protected boolean loadingByThisForbidden(String name, Object hint) {
        return !isLooksLikeShared(name) || super.loadingByThisForbidden(name, hint);
    }

    private boolean isLooksLikeShared(final String name) {
        return isLooksLikeSharedMeta(name) || isMetaContextProvider(name);
    }

    private boolean isReallyShared(final Class clazz) {
        return clazz != null && (isReallySharedMetaClass(clazz) || isMetaContextProvider(clazz.getCanonicalName()));
    }

    private boolean isMetaContextProvider(final String name) {
        return MetaContextProvider.class.getCanonicalName().equals(name);
    }

    private boolean isLooksLikeSharedMeta(final String name) {
        return name != null && name.endsWith("_mi");
    }

    private boolean isReallySharedMetaClass(final Class<?> clazz) {
        final RadMetaClass radMetaClass = clazz.getAnnotation(RadMetaClass.class);
        return radMetaClass != null && radMetaClass.shareabilityArea() == EShareabilityArea.RELEASE;
    }

    @Override
    protected FileCache.ClassFileData getClassFileData(String fileName) throws IOException {
        if (classNameToFileName(MetaContextProvider.class.getCanonicalName()).equals(fileName)) {
            return getRadixLoader().readClassFileData(fileName, Collections.singleton(Directory.FileGroups.FileGroup.GroupType.KERNEL_SERVER.toString()), release.getRevisionMeta());
        } else {
            return super.getClassFileData(fileName);
        }
    }

    @Override
    public Class<?> loadExecutableClassById(Id id) throws ClassNotFoundException {
        throw new UnsupportedOperationException("Shared classloader should only be used to load 'meta' classes");
    }

    @Override
    public IRadixEnvironment getEnvironment() {
        return release.getReleaseVirtualEnviroment();
    }

    @Override
    public Release getRelease() {
        return release;
    }
}
