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

package org.radixware.kernel.common.client.env;

import java.io.ByteArrayOutputStream;
import java.io.File;
import org.radixware.kernel.common.client.IClientApplication;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLStreamException;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.compiler.core.lookup.locations.KernelModulePackageLocation;
import org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;

import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.environment.IRadixEnvironment;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.starter.meta.RevisionMeta;
import org.radixware.kernel.starter.radixloader.RadixClassLoader;
import org.radixware.kernel.starter.radixloader.RadixLoader;

import org.radixware.schemas.product.Directory.FileGroups.FileGroup.GroupType;
import org.radixware.schemas.reports.ReadReportRuntimeRq;
import org.radixware.schemas.reports.ReadReportRuntimeRqDocument;
import org.radixware.schemas.reports.ReadReportRuntimeRs;
import org.radixware.schemas.reports.ReadReportRuntimeRsDocument;


public class AdsClassLoader extends RadixClassLoader implements IAdsClassLoader {

    public static final class Factory implements RadixClassLoader.Factory {

        private final IClientApplication clientEnvironment;

        public Factory(final IClientApplication clientEnvironment) {
            this.clientEnvironment = clientEnvironment;
        }

        @Override
        public RadixClassLoader createRadixClassLoader(final RevisionMeta revisionMeta, final RadixLoader radixLoader, final Set<String> groupTypes, final ClassLoader parent) {
            return new AdsClassLoader(clientEnvironment, revisionMeta, radixLoader, groupTypes, parent);
        }
    }
    //  private static final Set<String> CLASS_GROUPS;
//    static {
//        final Set<String> grps = new HashSet<String>();
//        grps.add(GroupType.ADS_COMMON.toString());
//        grps.add(GroupType.ADS_CLIENT.toString());
//        grps.add(GroupType.ADS_EXPLORER.toString());
//        CLASS_GROUPS = Collections.unmodifiableSet(grps);
//    }
    private ReleaseRepository repository;
    private final IClientApplication clientEnvironment;
    private final Map<String, byte[]> userReportEntriesByName = new HashMap<>();

    private static Set<String> getClassGroups(IClientApplication app) {
        final Set<String> grps = new HashSet<>();
        grps.add(GroupType.ADS_COMMON.toString());
        grps.add(GroupType.ADS_CLIENT.toString());
        if (app.getRuntimeEnvironmentType() == ERuntimeEnvironmentType.EXPLORER) {
            grps.add(GroupType.ADS_EXPLORER.toString());
        } else {
            grps.add(GroupType.ADS_WEB.toString());
        }
        return Collections.unmodifiableSet(grps);
    }

    protected AdsClassLoader(final IClientApplication clientEnvironment, final RevisionMeta revisionMeta, final RadixLoader radixLoader, final Set<String> groupTypes, final ClassLoader parent) {
        super(revisionMeta, radixLoader, groupTypes, parent);
        this.repository = new ReleaseRepository(clientEnvironment, revisionMeta, revisionMeta.getNum());
        this.clientEnvironment = clientEnvironment;
    }

    public static IAdsClassLoader createInstance(final IClientApplication clientEnvironment, final long version) throws IOException, XMLStreamException {
        return (IAdsClassLoader) RadixLoader.getInstance().createClassLoader(
                new AdsClassLoader.Factory(clientEnvironment),
                version,
                getClassGroups(clientEnvironment),
                IClientApplication.class.getClassLoader());
    }

    @Override
    public IRadixEnvironment getEnvironment() {
        return clientEnvironment;
    }

    @Override
    public Class<?> loadExecutableClassById(Id id) throws ClassNotFoundException {
        try {
            String className = repository.getExecutableClassNameByDefId(id);
            return loadClass(className);
        } catch (DefinitionError e) {
            if (id.getPrefix() == EDefinitionIdPrefix.ADS_REPORT_MODEL_CLASS) {
                return loadUserReportExecutableClassById(id);
            } else {
                throw e;
            }
        }
    }

    @Override
    public Class<?> loadMetaClassById(Id id) throws ClassNotFoundException {
        if (id.getPrefix() == EDefinitionIdPrefix.USER_DEFINED_REPORT) {
            return loadUserReportMetaClassById(id);
        }
        if (id.getPrefix() == EDefinitionIdPrefix.ADS_LOCALIZING_BUNDLE) {
            if (id.toString().startsWith("mlb" + EDefinitionIdPrefix.USER_DEFINED_REPORT.getValue())) {
                return loadUserReportMetaClassById(id);
            }
        }
        return loadClass(repository.getMetaClassNameByDefId(id));
    }

    @Override
    public RadixClassLoader getRadixClassLoader() {
        return this;
    }

    @Override
    public ReleaseRepository getRepository() {
        return repository;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        final String jarEntryName = name.replace('.', '/') + ".class";
        final byte[] cachedData = userReportEntriesByName.get(jarEntryName);
        if (cachedData != null) {//этот jar уже разбирался
            //классы два раза не грузятся /=> удаляем из кэша
            userReportEntriesByName.remove(jarEntryName);
            return defineClass(name, cachedData, 0, cachedData.length);
        }

        return super.findClass(name);
    }

    private final static class UserReportInfo {

        final String metaClassName;
        final String execClassName;
        final String modelClassName;
        final String mlbClassName;
        final Id currentClassGuid;
        long lastAccess;

        public UserReportInfo(Id currentClassGuid, String metaClassName, String execClassName, String modelClassName, String mlbClassName, long lastAccess) {
            this.currentClassGuid = currentClassGuid;
            this.metaClassName = metaClassName;
            this.execClassName = execClassName;
            this.modelClassName = modelClassName;
            this.mlbClassName = mlbClassName;
            this.lastAccess = lastAccess;
        }
    }
    private final Map<Id, UserReportInfo> userReoirtsKnownById = new HashMap<>();
    private static final long ufCacheCleanupInterval = 1 * 60 * 1000;

    private void cleanupObsoleteUserReportInfo() {
        if (!userReoirtsKnownById.isEmpty()) {
            long checkTime = System.currentTimeMillis();
            List<Id> ids = new LinkedList<>();
            for (Map.Entry<Id, UserReportInfo> info : userReoirtsKnownById.entrySet()) {
                if (checkTime - info.getValue().lastAccess > ufCacheCleanupInterval) {
                    ids.add(info.getKey());
                }
            }
            for (Id ufId : ids) {
                userReoirtsKnownById.remove(ufId);
                cleanupObsoleteReportData(ufId);
            }
        }
    }

    private String jarEntryName2ClassName(String jarEntryName) {
        return jarEntryName.substring(0, jarEntryName.length() - 6).replace('/', '.');
    }

    private Class<?> loadUserReportExecutableClassById(final Id id) throws ClassNotFoundException {
        final Id ownerReportId = Id.Factory.changePrefix(id, EDefinitionIdPrefix.USER_DEFINED_REPORT);

        UserReportInfo knownClassInfo = findCachedReportData(ownerReportId);


        cleanupObsoleteUserReportInfo();

        if (knownClassInfo != null && (knownClassInfo.modelClassName != null || knownClassInfo.execClassName != null)) {
            return loadClass(knownClassInfo.modelClassName != null ? knownClassInfo.modelClassName : knownClassInfo.execClassName);
        }
        throw new DefinitionNotFoundError(id);
    }

    private UserReportInfo findReportDataInJar(Id reportClassId, Map<String, byte[]> jar) {
        final String execClassNameSuffix = "/" + reportClassId.toString() + ".class";
        final String metaClassNameSuffix = "/" + reportClassId.toString() + "_mi.class";
        final String mlbClassNameSuffix = "/mlb" + reportClassId.toString() + "_mi.class";
        final String modelClassNameSuffix = "/" + Id.Factory.changePrefix(reportClassId, EDefinitionIdPrefix.ADS_REPORT_MODEL_CLASS).toString() + ".class";

        String execClassName = null;
        String metaClassName = null;
        String modelClassName = null;
        String mlbClassName = null;
        for (String name : jar.keySet()) {
            if (isNestedClassName(name)) {
                continue;
            }
            if (name.endsWith(execClassNameSuffix)) {
                execClassName = jarEntryName2ClassName(name);
            } else if (name.endsWith(metaClassNameSuffix)) {
                metaClassName = jarEntryName2ClassName(name);
            } else if (name.endsWith(modelClassNameSuffix)) {
                modelClassName = jarEntryName2ClassName(name);
            } else if (name.endsWith(mlbClassNameSuffix)) {
                mlbClassName = jarEntryName2ClassName(name);
            }
            if (execClassName != null && metaClassName != null && modelClassName != null && mlbClassName != null) {
                break;
            }
        }
        return new UserReportInfo(reportClassId, metaClassName, execClassName, modelClassName, mlbClassName, System.currentTimeMillis());
    }
    
    private boolean isNestedClassName(final String name) {
        int lastPartStartIdx = name.lastIndexOf("/") + 1;
        return name.lastIndexOf("$") > lastPartStartIdx;
    }

    private UserReportInfo findCachedReportData(Id ownerReportId) {
        UserReportInfo knownClassInfo = userReoirtsKnownById.get(ownerReportId);

        final Map<String, byte[]> jar;
        Id[] runtimeId = new Id[1];
        if (knownClassInfo != null) {
            jar = loadUserReportJarByClassId(ownerReportId, knownClassInfo.currentClassGuid, runtimeId);
        } else {
            jar = loadUserReportJarByClassId(ownerReportId, null, runtimeId);
        }
        if (jar != null && runtimeId[0] != null) {
            userReportEntriesByName.putAll(jar);
            Id obsoleteReportId = null;
            if (knownClassInfo != null) {
                obsoleteReportId = knownClassInfo.currentClassGuid;
            }
            if (obsoleteReportId != null) {
                cleanupObsoleteReportData(obsoleteReportId);
            }
            knownClassInfo = findReportDataInJar(runtimeId[0], jar);
            if (knownClassInfo != null) {
                userReoirtsKnownById.put(ownerReportId, knownClassInfo);
            }
        } else {
            if(knownClassInfo!=null)
               knownClassInfo.lastAccess = System.currentTimeMillis();
        }
        return knownClassInfo;
    }

    private void cleanupObsoleteReportData(Id obsoleteReportId) {
        String idAsStr = obsoleteReportId.toString().substring(3);

        List<String> removeClassNames = new LinkedList<>();
        for (String s : userReportEntriesByName.keySet()) {
            if (s.contains(idAsStr)) {
                removeClassNames.add(s);
            }
        }
        for (String s : removeClassNames) {
            userReportEntriesByName.remove(s);
        }
    }

    private Class<?> loadUserReportMetaClassById(final Id id) throws ClassNotFoundException {
        final Id ownerReportId;
        boolean isString = false;
        if (id.getPrefix() == EDefinitionIdPrefix.ADS_LOCALIZING_BUNDLE) {
            ownerReportId = Id.Factory.loadFrom(id.toString().substring(3));
            isString = true;
        } else {
            ownerReportId = Id.Factory.changePrefix(id, EDefinitionIdPrefix.USER_DEFINED_REPORT);
        }

        UserReportInfo knownClassInfo = findCachedReportData(ownerReportId);

        cleanupObsoleteUserReportInfo();
        if (knownClassInfo != null) {
            String className = isString ? knownClassInfo.mlbClassName : knownClassInfo.metaClassName;
            if (className != null) {
                return loadClass(className);
            }
        }
        throw new DefinitionNotFoundError(id);
    }
    private static final Id cmdId = Id.Factory.loadFrom("clc37A2AEDWFZCHFP23ARBCVXK4OM");

    private Map<String, byte[]> loadUserReportJarByClassId(Id id, Id currentClassGuid, Id[] reportClassGuid) {
        IClientEnvironment env = IClientEnvironment.Locator.getEnvironment();
        if (env == null) {
            return Collections.emptyMap();
        } else {
            File tmpJarFile = null;
            try {
                tmpJarFile = File.createTempFile("RadixWare", "reportdata");
            } catch (IOException ex) {
                Logger.getLogger(AdsClassLoader.class.getName()).log(Level.SEVERE, null, ex);
                env.getTracer().error(new RadixError("Unable to create temporary file for report runtime ReportID=#" + id.toString(), ex));
                return Collections.emptyMap();
            }
            try {
                try {
                    ReadReportRuntimeRqDocument inDoc = ReadReportRuntimeRqDocument.Factory.newInstance();
                    ReadReportRuntimeRq rq = inDoc.addNewReadReportRuntimeRq();
                    rq.setReportClassGuid(currentClassGuid);
                    rq.setReportId(id);
                    rq.setTmpFileName(tmpJarFile.getAbsolutePath());

                    ReadReportRuntimeRsDocument outDoc = (ReadReportRuntimeRsDocument) env.getEasSession().executeContextlessCommand(cmdId, inDoc, ReadReportRuntimeRsDocument.class);
                    if (outDoc == null) {
                        return Collections.emptyMap();
                    }
                    ReadReportRuntimeRs rs = outDoc.getReadReportRuntimeRs();

                    if (rs.getStatus() == 0) {//actual currentClassGuid. No file transmission performed
                        return null;
                    } else if (rs.getStatus() == -1) {//error while processing
                        return Collections.emptyMap();
                    }

                    Id runtimeId = rs.getReportClassGuid();
                    if (runtimeId == null) {
                        return Collections.emptyMap();
                    }
                    reportClassGuid[0] = runtimeId;

                    try {
                        try (JarFile file = new JarFile(tmpJarFile)) {
                            Enumeration<JarEntry> enumerations = file.entries();
                            Map<String, byte[]> result = new HashMap<>();
                            ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
                            String ignoreServerStr = "/server/";
                            String ignoreEnv = env.getApplication().getRuntimeEnvironmentType() == ERuntimeEnvironmentType.EXPLORER ? "/web/" : "/explorer/";
                            while (enumerations.hasMoreElements()) {
                                JarEntry e = enumerations.nextElement();
                                String name = e.getName();
                                if (name.contains(ignoreServerStr) || name.contains(ignoreEnv)) {
                                    continue;
                                }
                                try (InputStream stream = file.getInputStream(e)) {
                                    FileUtils.copyStream(stream, out);
                                    result.put(name, out.toByteArray());
                                    out.reset();
                                }

                            }
                            return result;
                        }
                    } catch (IOException ex) {
                        env.getTracer().error(new RadixError("Unable to read temporary file for report runtime ReportID=#" + id.toString() + " file: " + tmpJarFile.getAbsolutePath(), ex));
                        Logger.getLogger(AdsClassLoader.class.getName()).log(Level.SEVERE, null, ex);
                        return Collections.emptyMap();
                    }
                } catch (ServiceClientException | InterruptedException ex) {
                    env.getTracer().error(new RadixError("Unable read report runtime. ReportID=#" + id.toString(), ex));
                    Logger.getLogger(AdsClassLoader.class.getName()).log(Level.SEVERE, null, ex);
                    return Collections.emptyMap();
                }
            } finally {
                if (tmpJarFile != null) {
                    FileUtils.deleteFile(tmpJarFile);
                }
            }
        }
    }

    @Override
    public void close() {
        if (repository!=null){
            repository.close();
        }
        repository = null;
        AdsUserFuncDef.Lookup.clearCaches();
        KernelModulePackageLocation.resetCaches();
    }
}