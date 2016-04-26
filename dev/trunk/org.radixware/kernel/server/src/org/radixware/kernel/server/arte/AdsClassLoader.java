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
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.stream.XMLStreamException;
import org.radixware.kernel.common.defs.ads.src.WriterUtils;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.EShareabilityArea;
import org.radixware.kernel.common.environment.IRadixClassLoader;
import org.radixware.kernel.common.environment.IRadixEnvironment;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.server.Server;
import org.radixware.kernel.server.dbq.DbQueryBuilder;
import org.radixware.kernel.starter.meta.RevisionMeta;
import org.radixware.kernel.starter.radixloader.RadixClassLoader;
import org.radixware.kernel.starter.radixloader.RadixLoader;
import org.radixware.schemas.product.Directory.FileGroups.FileGroup.GroupType;

public class AdsClassLoader extends RadixClassLoader implements IRadixClassLoader, IVersionedClassloader {

    private static final Set<String> CLASS_GROUPS;
    private static final long ufCacheCleanupInterval = 10 * 1000;
    //
    private final ReleaseCache releaseCache;
    private final Map<String, byte[]> ufEntriesByName = new HashMap<>();
    private final Map<Id, UfInfo> ufKnownById = new HashMap<>();

    static {
        final Set<String> grps = new HashSet<>();
        grps.add(GroupType.ADS_COMMON.toString());
        grps.add(GroupType.ADS_SERVER.toString());
        grps.add(GroupType.DDS.toString());
        CLASS_GROUPS = Collections.unmodifiableSet(grps);
    }

    protected AdsClassLoader(final ReleaseCache release, final RevisionMeta revisionMeta, final RadixLoader rdxLoader, final Set<String> groupTypes, final ClassLoader parent) {
        super(revisionMeta, rdxLoader, groupTypes, parent);
        this.releaseCache = release;
    }

    public Arte getArte() {
        return releaseCache.getArte();
    }

    @Override
    public IRadixEnvironment getEnvironment() {
        return getArte();
    }

    @Override
    public Class<?> loadExecutableClassById(final Id id) throws ClassNotFoundException {
        if (id == null) {
            throw new IllegalArgumentException("id is null");
        }
        if (id.getPrefix() == EDefinitionIdPrefix.USER_FUNC_CLASS) {
            return loadUserFuncClassById(id);
        }
        if (id.getPrefix() == EDefinitionIdPrefix.USER_DEFINED_REPORT) {
            return loadUserReportClassById(id, false);
        }

        return loadClass(releaseCache.getRelease().getAdsClassNameById(id), false, EShareabilityArea.ARTE);
    }

    @Override
    public Class<?> loadMetaClassById(final Id id) throws ClassNotFoundException {
        if (id == null) {
            throw new IllegalArgumentException("id is null");
        }
        if (id.getPrefix() == EDefinitionIdPrefix.APPLICATION_ROLE) {
            return loadAppRoleClassById(id);
        }
        if (id.getPrefix() == EDefinitionIdPrefix.USER_DEFINED_REPORT) {
            return loadUserReportClassById(id, true);
        }
        if (id.getPrefix() == EDefinitionIdPrefix.ADS_LOCALIZING_BUNDLE) {
            String prefixSuffix = id.toString().substring(3, 6);
            if (EDefinitionIdPrefix.USER_DEFINED_REPORT.getValue().equals(prefixSuffix)) {
                return loadUserReportClassById(id, true);
            } else if (EDefinitionIdPrefix.USER_FUNC_CLASS.getValue().equals(prefixSuffix)) {
                return loadUserFuncClassById(id);
            }
        }
        return loadClass(releaseCache.getRelease().getAdsMetaClassNameById(id), false, WriterUtils.isShareableMeta(id) ? EShareabilityArea.RELEASE : EShareabilityArea.ARTE);
    }

//    @Override
//    protected boolean loadingByParentForbidden(String name, Object hint) {
//        return (isArteLocalClass(name, hint) && (getParent() instanceof ReleaseSharedClassloader)) || super.loadingByParentForbidden(name, hint);
//    }
    
    private boolean isArteLocalClass(final String name, final Object hint) {
        return (hint == EShareabilityArea.ARTE) || releaseCache.getRelease().getRepository().getAdsIndices().isExecutableClass(name);
    }

    @Override
    protected Class<?> findClass(final String name) throws ClassNotFoundException {
        final String jarEntryName = name.replace('.', '/') + ".class";
        final byte[] cachedData = ufEntriesByName.get(jarEntryName);
        boolean reportresult = false;
        if (cachedData != null) {//этот jar уже разбирался
            //классы два раза не грузятся /=> удаляем из кэша
            ufEntriesByName.remove(jarEntryName);
            return defineClass(name, cachedData, 0, cachedData.length);
        }
        return super.findClass(name);
    }

    private String jarEntryName2ClassName(String jarEntryName) {
        return jarEntryName.substring(0, jarEntryName.length() - 6).replace('/', '.');
    }

    private void cleanupObsoleteUfInfo() {
        if (!ufKnownById.isEmpty()) {
            long checkTime = System.currentTimeMillis();
            List<Id> ids = new LinkedList<Id>();
            for (Map.Entry<Id, UfInfo> info : ufKnownById.entrySet()) {
                if (checkTime - info.getValue().lastAccess > ufCacheCleanupInterval) {
                    ids.add(info.getKey());
                }
            }
            for (Id ufId : ids) {
                cleanUpUfInfo(ufId);
            }
        }
    }

    private void cleanUpUfInfo(Id ufId) {
        UfInfo removed = ufKnownById.remove(ufId);
        List<String> checkClassNames = new ArrayList<>(4);
        if (removed != null) {
            if (removed.className != null) {
                checkClassNames.add(removed.className);
            }
            if (removed.metaClassName != null) {
                checkClassNames.add(removed.metaClassName);
            }
            if (removed.mlbClassName != null) {
                checkClassNames.add(removed.mlbClassName);
            }
        }
        checkClassNames.add(ufId.toString());
        List<String> removeClassNames = new LinkedList<>();

        for (String idAsStr : checkClassNames) {

            for (String s : ufEntriesByName.keySet()) {
                if (s.contains(idAsStr)) {
                    removeClassNames.add(s);
                }
            }
        }
        for (String s : removeClassNames) {
            ufEntriesByName.remove(s);
        }
    }

    public DefManager.ImageData getDbImageData(final Id contextId, final Id imageId) {
        if (contextId.getPrefix() == EDefinitionIdPrefix.USER_DEFINED_REPORT) {
            return releaseCache.getDbQueryBuilder().loadUserReportImageById(contextId, imageId);
        } else {
            return null;
        }
    }

    private Class<?> loadUserFuncClassById(final Id funcOrStringId) throws ClassNotFoundException {
        boolean isMlb = funcOrStringId.getPrefix() == EDefinitionIdPrefix.ADS_LOCALIZING_BUNDLE;
        final Id id;
        if (isMlb) {
            id = Id.Factory.loadFrom(funcOrStringId.toString().substring(3));
        } else {
            id = funcOrStringId;
        }
        final UfInfo knownClassInfo = ufKnownById.get(id);
        if (knownClassInfo != null) {
            knownClassInfo.lastAccess = System.currentTimeMillis();
            if (isMlb) {
                if (knownClassInfo.mlbClassName != null) {
                    return loadClass(knownClassInfo.mlbClassName);
                } else {
                    throw new UserDefinitionNotFoundError(id);
                }
            } else {
                return loadClass(knownClassInfo.className);
            }
        }
        cleanupObsoleteUfInfo();

        final String classNameSuffix = id.toString() + ".class";
        final String stringNameSuffix = "mlb" + id.toString() + "_mi.class";


        final Map<String, byte[]> jar = releaseCache.getDbQueryBuilder().loadUserFuncJarByClassId(id);
        ufEntriesByName.putAll(jar);

        String className = null;
        String stringName = null;
        for (String name : jar.keySet()) {
            if (name.endsWith(classNameSuffix) && !isNestedClassName(name)) {
                className = jarEntryName2ClassName(name);
            } else if (name.endsWith(stringNameSuffix) && !isNestedClassName(name)) {
                stringName = jarEntryName2ClassName(name);
            }
            if (stringName != null && className != null) {
                break;
            }
        }
        if (className != null) {
            ufKnownById.put(id, new UfInfo(className, null, stringName, System.currentTimeMillis()));
            if (isMlb) {
                if (stringName != null) {
                    return loadClass(stringName);
                }
            } else {
                return loadClass(className);
            }
        }

        throw new UserDefinitionNotFoundError(id);
    }

    private boolean isNestedClassName(final String name) {
        int lastPartStartIdx = name.lastIndexOf("/") + 1;
        return name.lastIndexOf("$") > lastPartStartIdx;
    }

    private Class<?> loadAppRoleClassById(final Id id) throws ClassNotFoundException {
        cleanupObsoleteUfInfo();
        cleanUpUfInfo(id);

        final DbQueryBuilder.AppRoleClassInfo info = releaseCache.getDbQueryBuilder().loadAppRoleJarByClassId(id);
        if (info == null) {
            return null;
        }
        final String classNameSuffix = info.classId + "_mi.class";


        ufEntriesByName.putAll(info.data);

        for (String name : info.data.keySet()) {
            if (name.endsWith(classNameSuffix) && !isNestedClassName(name)) {
                String className = jarEntryName2ClassName(name);
                ufKnownById.put(id, new UfInfo(className, null, null, System.currentTimeMillis()));
                return loadClass(className);
            }
        }
        throw new UserDefinitionNotFoundError(id);
    }

    private Class<?> loadUserReportClassById(final Id id, boolean meta) throws ClassNotFoundException {
        String prefix = null;
        final Id reportId;

        if (id.getPrefix() == EDefinitionIdPrefix.ADS_LOCALIZING_BUNDLE) {
            reportId = Id.Factory.loadFrom(id.toString().substring(3));
            prefix = EDefinitionIdPrefix.ADS_LOCALIZING_BUNDLE.getValue();
        } else {
            reportId = id;
        }

        final Id actualClassId = releaseCache.getDbQueryBuilder().getUserReportClassGuid(reportId);

        final UfInfo knownClassInfo = ufKnownById.get(actualClassId);
        if (knownClassInfo != null) {
            knownClassInfo.lastAccess = System.currentTimeMillis();
            if (prefix != null) {
                if (knownClassInfo.mlbClassName != null) {
                    return loadClass(knownClassInfo.mlbClassName);
                } else {
                    throw new UserDefinitionNotFoundError(id);
                }
            }
            String actualClassName = meta ? knownClassInfo.metaClassName : knownClassInfo.className;
            if (actualClassName != null) {
                return loadClass(actualClassName);
            } else {
                throw new UserDefinitionNotFoundError(id);
            }
        }
        cleanupObsoleteUfInfo();
        Map<String, byte[]> info = releaseCache.getDbQueryBuilder().loadUserReportJarByClassId(reportId);
        ufEntriesByName.putAll(info);

        final String classNameSuffix = actualClassId + ".class";
        final String metaClassNameSuffix = actualClassId + "_mi.class";
        final String mlbClassNameSuffix = "mlb" + actualClassId + "_mi.class";
        String className = null, mlbClassName = null, metaClassName = null;
        for (String name : info.keySet()) {
            if (isNestedClassName(name)) {
                continue;
            }
            if (name.endsWith(classNameSuffix)) {
                className = jarEntryName2ClassName(name);
            } else if (name.endsWith(mlbClassNameSuffix)) {
                mlbClassName = jarEntryName2ClassName(name);
            } else if (name.endsWith(metaClassNameSuffix)) {
                metaClassName = jarEntryName2ClassName(name);
            }
            if (className != null && mlbClassName != null && metaClassName != null) {
                break;
            }
        }

        if (className != null) {
            ufKnownById.put(actualClassId, new UfInfo(className, metaClassName, mlbClassName, System.currentTimeMillis()));
            if (prefix == null) {
                String actualClassName = meta ? metaClassName : className;
                if (actualClassName != null) {
                    return loadClass(actualClassName);
                }
            } else {
                if (mlbClassName != null) {
                    return loadClass(mlbClassName);
                }
            }
        }

        throw new UserDefinitionNotFoundError(id);


    }

    @Override
    public Release getRelease() {
        return releaseCache.getRelease();
    }

    private final static class UfInfo {

        final String className;
        final String metaClassName;
        final String mlbClassName;
        long lastAccess;

        public UfInfo(String className, String metaClassName, String mlbClassName, long lastAccess) {
            this.className = className;
            this.mlbClassName = mlbClassName;
            this.metaClassName = metaClassName;
            this.lastAccess = lastAccess;
        }
    }

    private static final class Factory implements RadixClassLoader.Factory {

        private final ReleaseCache releaseCache;

        public Factory(final ReleaseCache releaseCache) {
            this.releaseCache = releaseCache;
        }

        @Override
        public RadixClassLoader createRadixClassLoader(final RevisionMeta revisionMeta, final RadixLoader rdxLoader, final Set<String> groupTypes, final ClassLoader parent) {
            return new AdsClassLoader(releaseCache, revisionMeta, rdxLoader, groupTypes, parent);
        }
    }

    public static class UserDefinitionNotFoundError extends DefinitionNotFoundError {

        public UserDefinitionNotFoundError(Id definitionId) {
            super(definitionId);
        }

        public UserDefinitionNotFoundError(Id definitionId, Throwable cause) {
            super(definitionId, cause);
        }
    }

    static final class CreateAction implements PrivilegedAction<AdsClassLoader> {

        private final ReleaseCache releaseCache;

        protected CreateAction(final ReleaseCache releaseCache) {
            super();
            this.releaseCache = releaseCache;
        }

        @Override
        public AdsClassLoader run() {
            try {
                return (AdsClassLoader) RadixLoader.getInstance().createClassLoader(
                        new AdsClassLoader.Factory(releaseCache),
                        releaseCache.getRelease().getVersion(),
                        AdsClassLoader.CLASS_GROUPS,
                        releaseCache.getRelease().getSharedClassLoader());
            } catch (XMLStreamException ex) {
                releaseCache.getArte().getTrace().put(EEventSeverity.ERROR, "Can't create AdsClassLoader : " + releaseCache.getArte().getTrace().exceptionStackToString(ex), EEventSource.ARTE);
                return null;
            } catch (IOException ex) {
                releaseCache.getArte().getTrace().put(EEventSeverity.ERROR, "Can't create AdsClassLoader : " + releaseCache.getArte().getTrace().exceptionStackToString(ex), EEventSource.ARTE);
                return null;
            }
        }
    }
}
