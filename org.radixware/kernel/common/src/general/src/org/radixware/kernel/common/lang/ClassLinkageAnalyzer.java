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
package org.radixware.kernel.common.lang;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.build.directory.DirectoryFileSigner;
import org.radixware.kernel.common.components.ICancellable;
import org.radixware.kernel.common.constants.FileNames;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.Hex;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.schemas.product.Classes;
import org.radixware.schemas.product.Definitions;
import org.radixware.schemas.product.DefinitionsDocument;
import org.radixware.schemas.product.Directory;
import org.radixware.schemas.product.Directory.FileGroups.FileGroup;
import org.radixware.schemas.product.DirectoryDocument;
import org.radixware.schemas.product.JavaClasses;

public abstract class ClassLinkageAnalyzer implements ICancellable {

    public interface LayerInfo {

        public String getURI();

        public ModuleInfo[] findModulesByPackageNameId(Id id);

        public List<LayerInfo> findPrevLayer();

        public String getDisplayName();
    }

    public interface ModuleInfo {

        public Id getId();

        public byte[] findDefinitionsIndexData();
        
        public byte[] findDirectoryIndexData();
                
        public File findBinaryFile(ERuntimeEnvironmentType env);

        public LayerInfo getLayer();

        public String getDisplayName();

        public void close();
    }
    private final Set<String> resolvedClasses = new HashSet<String>();
    protected final Map<String, Class> loadedClasses = new HashMap<String, Class>();

    protected abstract class RdxClassLoader extends ClassLoader {

        protected final LayerInfo layer;
        protected final ERuntimeEnvironmentType env;

        public RdxClassLoader(LayerInfo layer, ERuntimeEnvironmentType env) {
            super(null);
            this.layer = layer;
            this.env = env;
        }

        protected void registerClass(String name, Class c) {
            synchronized (loadedClasses) {
                loadedClasses.put(name, c);
            }
        }

        protected final Class lookup(String name) {
            synchronized (loadedClasses) {
                Class c = loadedClasses.get(name);
                return c;
            }
        }

        void resolve(Class c) {
            if (wasCancelled()) {
                return;
            }
            synchronized (resolvedClasses) {
                if (resolvedClasses.contains(c.getName())) {
                    return;
                }
                resolvedClasses.add(c.getName());
                try {
                    this.resolveClass(c);
                    c.getDeclaredClasses();
                    c.getDeclaredAnnotations();
                    c.getDeclaredConstructors();
                    c.getDeclaredFields();
                    c.getDeclaredMethods();
                    c.getDeclaringClass();
                } catch (LinkageError e) {
                    final String problemMessage = e.getMessage();
                    if (!canIgnoreProblem(c, problemMessage)) {
                        acceptProblem("Class linkage error(" + c.getName() + "): \'" + problemMessage + "\'");
                    }
                }
            }
        }

        protected abstract void acceptProblem(String message);

        protected abstract void acceptMessage(String message);

        protected abstract void close();

        protected abstract void summary();
    }

    protected boolean canIgnoreProblem(final Class c, final String problem) {
        return false;
    }

    protected abstract class KernelClassLoader extends RdxClassLoader {

        public KernelClassLoader(LayerInfo layer, ERuntimeEnvironmentType env) {
            super(layer, env);
        }
        protected final Map<String, Class> loadedClasses = new HashMap<String, Class>();

        private Class loadClassFromKernel(String name) throws ClassNotFoundException {
            byte[] bytes = findClassBytesBySlashSeparatedName(name.replace('.', '/'));
            if (bytes == null) {
                throw new ClassNotFoundException(name);
            }
            try {
                Class c = defineClass(name, bytes, 0, bytes.length);
                registerClass(name, c);
                return c;
            } catch (Throwable ex) {
                acceptProblem("Can not find class " + name);
            }
            throw new ClassNotFoundException(name);
        }

        @Override
        protected void registerClass(String name, Class c) {
            super.registerClass(name, c);
            synchronized (loadedClasses) {
                loadedClasses.put(name, c);
            }
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            Class c = lookup(name);
            if (c != null) {
                return c;
            }
            if (verbose) {
                acceptMessage("Looking for class " + name + "...");
            }
            return loadClassFromKernel(name);
        }

        protected abstract byte[] findClassBytesBySlashSeparatedName(String name) throws ClassNotFoundException;
    }

    protected abstract class AdsClassLoader extends RdxClassLoader {

        private KernelClassLoader kernelLoader = null;

        protected AdsClassLoader(LayerInfo layer, ERuntimeEnvironmentType env) {
            super(layer, env);
        }

        private String getLayerPackageName(LayerInfo layer) {
            return layer.getURI().replace('-', '_');
        }

        private ModuleInfo[] findModuleByPackageNameId(Id moduleId) {

            ModuleInfo[] module = layer.findModulesByPackageNameId(moduleId);
            if (module != null && module.length > 0) {
                return module;
            }

            return findModuleByPackageNameId(layer, moduleId);
        }

        private ModuleInfo[] findModuleByPackageNameId(LayerInfo info, Id moduleId) {

            List<LayerInfo> prevs = info.findPrevLayer();
            for (LayerInfo prev : prevs) {
                ModuleInfo[] module = prev.findModulesByPackageNameId(moduleId);
                if (module != null && module.length > 0) {
                    return module;
                }
            }
            for (LayerInfo prev : prevs) {
                ModuleInfo[] module = findModuleByPackageNameId(prev, moduleId);
                if (module != null && module.length > 0) {
                    return module;
                }
            }
            return null;

        }

        private int checkLayerName(String name) {
            String layerPackage = getLayerPackageName(layer);
            if (name.startsWith(layerPackage)) {
                return layerPackage.length();
            }
            return checkLayerName(layer, name);
        }

        private int checkLayerName(LayerInfo info, String name) {
            List<LayerInfo> prevs = info.findPrevLayer();
            for (LayerInfo prev : prevs) {
                String layerPackage = getLayerPackageName(prev);
                if (name.startsWith(layerPackage)) {
                    return layerPackage.length();
                }
            }
            for (LayerInfo prev : prevs) {
                int len = checkLayerName(prev, name);
                if (len > 0) {
                    return len;
                }
            }
            return -1;
        }

        private Class loadClassFromADS(String name) throws ClassNotFoundException {
            int layerPackageNameLen = checkLayerName(name);
            Throwable failCause = null;
            if (layerPackageNameLen > 0) {
                String suffix = name.substring(layerPackageNameLen + 1);
                if (suffix.startsWith("ads")) {
                    suffix = suffix.substring(4);
                    String[] components = suffix.split("\\.");
                    if (components.length == 0) {
                        throw new ClassNotFoundException(name);
                    }
                    if (!components[0].startsWith("mdl") || components[0].length() != 29) {
                        throw new ClassNotFoundException(name);
                    }
                    Id moduleId;
                    try {
                        moduleId = Id.Factory.loadFrom(components[0]);
                    } catch (NoConstItemWithSuchValueError e) {
                        throw new ClassNotFoundException(name, e);
                    }
                    ModuleInfo[] modules = findModuleByPackageNameId(moduleId);
                    if (modules == null || modules.length == 0) {
                        throw new ClassNotFoundException(name);
                    }

                    if (components.length < 2) {
                        throw new ClassNotFoundException(name);
                    }
                    ERuntimeEnvironmentType environment;
                    if ("common".equals(components[1])) {
                        environment = ERuntimeEnvironmentType.COMMON;
                    } else if ("server".equals(components[1])) {
                        environment = ERuntimeEnvironmentType.SERVER;
                        if (env != ERuntimeEnvironmentType.SERVER) {
                            throw new ClassNotFoundException(name);
                        }
                    } else if ("explorer".equals(components[1])) {
                        environment = ERuntimeEnvironmentType.EXPLORER;
                        if (env != ERuntimeEnvironmentType.EXPLORER) {
                            throw new ClassNotFoundException(name);
                        }
                    } else if ("common_client".equals(components[1])) {
                        environment = ERuntimeEnvironmentType.COMMON_CLIENT;
                        if (env != ERuntimeEnvironmentType.COMMON_CLIENT && env != ERuntimeEnvironmentType.EXPLORER && env != ERuntimeEnvironmentType.WEB) {
                            throw new ClassNotFoundException(name);
                        }
                    } else if ("web".equals(components[1])) {
                        environment = ERuntimeEnvironmentType.WEB;
                        if (env != ERuntimeEnvironmentType.WEB) {
                            throw new ClassNotFoundException(name);
                        }
                    } else {
                        throw new ClassNotFoundException(name);
                    }

                    for (ModuleInfo module : modules) {
                        if (!Utils.equals(module.getLayer().getURI(), layer.getURI())) {
                            continue;
                        }
                        File file = module.findBinaryFile(environment);
                        if (file == null || !file.exists()) {
                            continue;
                        }
                        try {
                            final JarFile jar = new JarFile(file);
                            try {
                                final Enumeration<JarEntry> entries = jar.entries();
                                final String classEntryName = name.replace('.', '/') + ".class";
                                while (entries.hasMoreElements()) {
                                    JarEntry e = entries.nextElement();
                                    String entryName = e.getName();
                                    if (!e.isDirectory() && entryName.equals(classEntryName)) {
                                        byte[] entryBytes = FileUtils.getZipEntryByteContent(e, jar);
                                        try {
                                            Class c = defineClass(name.replace('/', '.'), entryBytes, 0, entryBytes.length);
                                            if (c != null) {
                                                registerClass(name, c);
                                                return c;
                                            } else {
                                                continue;
                                            }
                                        } catch (Throwable ex) {
                                            failCause = ex;
                                            continue;
                                            //acceptProblem("Can not find class " + name);
                                        }
                                    }
                                }
                            } finally {
                                jar.close();
                            }
                        } catch (IOException ex) {
                            failCause = ex;
                            continue;
                            //throw new ClassNotFoundException(name, ex);
                        }
                    }
                    throw new ClassNotFoundException(name, failCause);
                }
                throw new ClassNotFoundException(name, failCause);
            }
            throw new ClassNotFoundException(name, failCause);
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            Class c = lookup(name);
            if (c != null) {
                return c;
            }
            if (verbose) {
                acceptMessage("Looking for class " + name + "...");
            }
            AdsClassLoader loader = this;

            try {
                return loader.loadClassFromADS(name);
            } catch (ClassNotFoundException e) {//not in this layer
                c = tryLoadFromLayerTree(layer, name);
                if (c != null) {
                    return c;
                }

            }
            return getKernelClassLoader().loadClass(name);
        }

        private Class<?> tryLoadFromLayerTree(LayerInfo layer, String name) {
            List<LayerInfo> prevs = layer.findPrevLayer();
            for (LayerInfo info : prevs) {
                AdsClassLoader loader = getAdsLoader(info, env);
                try {
                    return loader.loadClassFromADS(name);
                } catch (ClassNotFoundException ex) {//not in this layer
                    Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                }
            }
            for (LayerInfo info : prevs) {
                Class<?> c = tryLoadFromLayerTree(info, name);
                if (c != null) {
                    return c;
                }
            }
            return null;

        }

        private KernelClassLoader getKernelClassLoader() {
            synchronized (this) {
                if (kernelLoader == null) {
                    kernelLoader = createKernelClassLoader(layer, env);
                }
                return kernelLoader;
            }
        }

        @Override
        protected void close() {
            synchronized (this) {
                if (kernelLoader != null) {
                    kernelLoader.close();
                    kernelLoader = null;
                }
            }
        }

        @Override
        protected void summary() {
            if (kernelLoader != null) {
                kernelLoader.summary();
            }
        }
    }

    protected abstract KernelClassLoader createKernelClassLoader(LayerInfo layer, ERuntimeEnvironmentType env);

    protected abstract AdsClassLoader createAdsClassLoader(LayerInfo layer, ERuntimeEnvironmentType env);
    private final Object loadersLock = new Object();
    private final Map<LayerInfo, AdsClassLoader> sloaders = new HashMap<>();
    private final Map<LayerInfo, AdsClassLoader> eloaders = new HashMap<>();
    private final Map<LayerInfo, AdsClassLoader> cloaders = new HashMap<>();
    private final Map<LayerInfo, AdsClassLoader> wloaders = new HashMap<>();
    private final Map<LayerInfo, AdsClassLoader> ccloaders = new HashMap<>();

    protected AdsClassLoader getAdsLoader(LayerInfo layer, ERuntimeEnvironmentType env) {
        synchronized (loadersLock) {
            Map<LayerInfo, AdsClassLoader> loaders;
            switch (env) {
                case EXPLORER:
                    loaders = eloaders;
                    break;
                case WEB:
                    loaders = wloaders;
                    break;
                case COMMON_CLIENT:
                    loaders = ccloaders;
                    break;
                case SERVER:
                    loaders = sloaders;
                    break;
                default:
                    loaders = cloaders;
            }
            AdsClassLoader loader = loaders.get(layer);
            if (loader == null) {
                loader = createAdsClassLoader(layer, env);
                loaders.put(layer, loader);
            }
            return loader;
        }
    }

    protected abstract void acceptProblem(ModuleInfo module, String message);

    protected abstract void acceptMessage(String message);
    private boolean cancelled;
    protected final boolean verbose;

    protected ClassLinkageAnalyzer(boolean verbose) {
        this.verbose = verbose;
    }

    @Override
    public boolean cancel() {
        return cancelled = true;
    }

    @Override
    public boolean wasCancelled() {
        return cancelled;
    }
    

    protected boolean canCheckSignatureJarFile(){
        return false;
    }

    private void checkSignatureJarFile(final ModuleInfo module) {
        if (!canCheckSignatureJarFile()) {
            return;
        }

        final byte[] directoryIndexData = module.findDirectoryIndexData();
        if (directoryIndexData == null) {
            acceptProblem(module, "No found file " + FileNames.DIRECTORY_XML);
        }

        try {
            final DirectoryDocument xDirectoryDocument = DirectoryDocument.Factory.parse(new ByteArrayInputStream(directoryIndexData));
            final Directory xDirectory = xDirectoryDocument.getDirectory();
            if (xDirectory != null && xDirectory.getFileGroups() != null) {
                final List<FileGroup> xFileGroupList = xDirectory.getFileGroups().getFileGroupList();

                if (xFileGroupList != null) {
                    for (ERuntimeEnvironmentType env : ERuntimeEnvironmentType.values()) {

                        File entry = module.findBinaryFile(env);
                        if (entry != null) {
                            final String searchingShortName = FileNames.MODULE_BIN + "/" + env.getValue() + ".jar";

                            boolean digitsFound = false;
                            byte[] xmlDigits = null;

                            l:
                            for (FileGroup xFileGroup : xFileGroupList) {
                                List<FileGroup.File> xFileList = xFileGroup.getFileList();
                                if (xFileList != null) {
                                    for (FileGroup.File xFile : xFileList) {
                                        if (Utils.equals(xFile.getName(), searchingShortName)) {
                                            xmlDigits = xFile.getDigest();
                                            digitsFound = true;
                                            break l;
                                        }
                                    }
                                }
                            }
                            if (digitsFound) {
                                final byte[] jarDigits = DirectoryFileSigner.readJarDigest(entry);

                                if (!Arrays.equals(xmlDigits, jarDigits)) {
                                    acceptProblem(module, "Invalid signature of file " + searchingShortName
                                            + ": in " + FileNames.DIRECTORY_XML + " " + Hex.encode(xmlDigits) + ", in jar-file " + Hex.encode(jarDigits));
                                }
                            } else {
                                acceptProblem(module, "Not found digest for file " + searchingShortName + " in " + FileNames.DIRECTORY_XML);
                            }
                            
                            if (wasCancelled()) {
                                return;
                            }                            
                        }
                    }
                }
            }

        } catch (XmlException | IOException ex) {
            final String detailsText = ExceptionTextFormatter.throwableToString(ex);
            acceptMessage("Verifing module " + ex.getMessage() + detailsText + module.getDisplayName());
        }

    }


    protected void process(ModuleInfo module) {

        if (verbose) {
            acceptMessage("Verifing module " + module.getDisplayName());
        }
        
        checkSignatureJarFile(module);
        
        for (ERuntimeEnvironmentType env : ERuntimeEnvironmentType.values()) {
            byte[] definitionsIndexData = module.findDefinitionsIndexData();
            if (definitionsIndexData == null) {
                acceptProblem(module, "No definitions.xml file found");
                return;
            }
            try {
                if (wasCancelled()) {
                    return;
                }
                DefinitionsDocument xDoc = DefinitionsDocument.Factory.parse(new ByteArrayInputStream(definitionsIndexData));
                Definitions xDefs = xDoc.getDefinitions();
                if (env == ERuntimeEnvironmentType.SERVER && xDefs.getServerFactory() != null) {
                    checkClass(module, env, xDefs.getServerFactory());
                }
                for (Definitions.Definition xDef : xDefs.getDefinitionList()) {
                    if (wasCancelled()) {
                        return;
                    }
                    Classes xClasses = null;
                    switch (env) {
                        case SERVER:
                            xClasses = xDef.getServerClasses();
                            break;
                        case EXPLORER:
                            xClasses = xDef.getExplorerClasses();
                            break;

                        case COMMON:
                            xClasses = xDef.getCommonClasses();
                            break;
                        case COMMON_CLIENT:
                            xClasses = xDef.getCommonClientClasses();
                            break;
                        case WEB:
                            xClasses = xDef.getWebClasses();
                            break;

                    }
                    if (xClasses != null) {
                        JavaClasses xClassList = xClasses.getMeta();
                        if (xClassList != null) {
                            for (JavaClasses.Class xClass : xClassList.getClass1List()) {
                                if (wasCancelled()) {
                                    return;
                                }
                                checkClass(module, env, xClass.getName());

                            }
                        }
                        xClassList = xClasses.getExecutable();
                        if (xClassList != null) {
                            for (JavaClasses.Class xClass : xClassList.getClass1List()) {
                                if (wasCancelled()) {
                                    return;
                                }
                                checkClass(module, env, xClass.getName());
                            }
                        }
                    }
                }
            } catch (XmlException ex) {
                acceptProblem(module, "Invalid definitions.xml file format");
            } catch (IOException ex) {
                acceptProblem(module, "Unable to read definitions.xml file");
            } finally {
                close();
            }
        }
    }

    private void checkClass(ModuleInfo module, ERuntimeEnvironmentType env, String name) {
        if (wasCancelled()) {
            return;
        }
        try {
            AdsClassLoader loader = getAdsLoader(module.getLayer(), env);

            Class c = null;
            String[] nameComponents = null;
            String searchName = name;
            int index = 0;
            while (c == null) {
                try {
                    c = loader.findClass(searchName);
                } catch (ClassNotFoundException e) {

                    if (nameComponents == null) {
                        nameComponents = name.split("\\.");
                        index = nameComponents.length - 1;
                    }
                    StringBuilder result = new StringBuilder();
                    for (int i = 0; i
                            < nameComponents.length; i++) {
                        if (i > 0) {
                            if (i >= index) {
                                result.append('$');
                            } else {
                                result.append('.');
                            }
                        }
                        result.append(nameComponents[i]);
                    }
                    searchName = result.toString();
                    index--;
                    if (index == 0) {
                        throw new ClassNotFoundException(name);
                    }
                }
            }
            try {
                loader.resolve(c);
            } catch (RuntimeException e) {
                StringBuilder builder = new StringBuilder();
                builder.append(e.getMessage());
                Throwable cause = e.getCause();

                while (cause != null) {
                    builder.append(" because of:\n");
                    builder.append("    ").append(cause.getMessage()).append("\n");
                    cause = cause.getCause();
                }

                acceptProblem(module, builder.toString());
            }
        } catch (ClassNotFoundException ex) {
            StringBuilder builder = new StringBuilder();
            builder.append("Can not find class ").append(name);
            Throwable cause = ex.getCause();
            if (cause != null) {
                builder.append(" because of:\n");
                while (cause != null) {
                    builder.append("    ").append(cause.getMessage()).append("\n");
                }
                cause = cause.getCause();
            }

            acceptProblem(module, builder.toString());

        }
    }

    public void close() {
        for (AdsClassLoader loader : cloaders.values()) {
            loader.close();
        }
        cloaders.clear();
        for (AdsClassLoader loader : eloaders.values()) {
            loader.close();

        }
        eloaders.clear();
        for (AdsClassLoader loader : sloaders.values()) {
            loader.close();
        }
        sloaders.clear();
        for (AdsClassLoader loader : wloaders.values()) {
            loader.close();
        }
        wloaders.clear();
        for (AdsClassLoader loader : ccloaders.values()) {
            loader.close();
        }
        ccloaders.clear();
        loadedClasses.clear();
        resolvedClasses.clear();
    }

    private void collectKernelClassNames(Collection<AdsClassLoader> loaders, Set<String> names) {
        for (AdsClassLoader loader : loaders) {
            if (loader.kernelLoader != null) {
                for (String s : loader.kernelLoader.loadedClasses.keySet()) {
                    names.add(s);
                }
            }
        }
    }

    protected void summary() {
        if (verbose) {
            Set<String> names = new HashSet<String>();
            collectKernelClassNames(
                    cloaders.values(), names);
            collectKernelClassNames(
                    eloaders.values(), names);
            collectKernelClassNames(
                    sloaders.values(), names);
            List<String> list = new ArrayList<String>(names);
            Collections.sort(list);
            acceptMessage("Used kernel classes:");

            for (String s : list) {
                acceptMessage("   " + s);
            }
        }
    }
}
