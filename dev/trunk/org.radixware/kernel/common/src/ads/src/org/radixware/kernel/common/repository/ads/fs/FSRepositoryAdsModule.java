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
package org.radixware.kernel.common.repository.ads.fs;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.eclipse.jdt.internal.compiler.util.SuffixConstants;
import org.radixware.kernel.common.compiler.CompilerConstants;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import org.radixware.kernel.common.defs.ads.module.APISupport;
import org.radixware.kernel.common.defs.ads.module.AdsImageDef;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.module.Loader;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.repository.fs.FSRepositoryModule;
import org.radixware.kernel.common.repository.fs.IJarDataProvider;
import org.radixware.kernel.common.repository.fs.JarFileDataProvider;
import org.radixware.kernel.common.repository.fs.RepositoryInjection.ModuleInjectionInfo;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.schemas.adsdef.APIDocument;
import org.radixware.schemas.adsdef.AdsDefinitionElementType;

public class FSRepositoryAdsModule extends FSRepositoryModule<AdsModule> implements IRepositoryAdsModule {
    
    private static class Def2Rep {
        
        private final Definition def;
        private final FSRepositoryAdsDefinition rep;
        
        public Def2Rep(Definition def, FSRepositoryAdsDefinition rep) {
            this.def = def;
            this.rep = rep;
        }
    }
    
    private static class Def2RepMap {
        
        private List<Def2Rep> def2Reps;
        
        public Def2RepMap(Map<Definition, FSRepositoryAdsDefinition> map) {
            def2Reps = new ArrayList<>(map.size());
            add(map);
        }

        public void add(Map<Definition, FSRepositoryAdsDefinition> map) {
            for (Map.Entry<Definition, FSRepositoryAdsDefinition> e : map.entrySet()) {
                def2Reps.add(new Def2Rep(e.getKey(), e.getValue()));
            }
        }
        
        public FSRepositoryAdsDefinition get(Definition def) {
            for (Def2Rep dr : def2Reps) {
                if (dr.def == def) {
                    return dr.rep;
                }
            }
            return null;
        }
        
        public IRepositoryAdsDefinition[] toArray() {
            IRepositoryAdsDefinition[] arr = new IRepositoryAdsDefinition[def2Reps.size()];
            for (int i = 0; i < arr.length; i++) {
                arr[i] = def2Reps.get(i).rep;
            }
            return arr;
        }
    }
    private Def2RepMap repositories = null;
    private Set<Id> ids = null;
    
    public FSRepositoryAdsModule(File moduleDir) {
        super(moduleDir);
    }
    
    public FSRepositoryAdsModule(AdsModule module) {
        super(module);
    }
    protected static final String suffix = ".xml";
    protected static final int len = 33;
    
    private File[] listFiles(File dir) {
        return dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                final String name = pathname.getName();
                if (name == null) {
                    return false;
                }
                if (name.endsWith(suffix) && !name.startsWith("mlb")) {
                    if (name.length() == len || name.startsWith("img")) {
                        return true;
                    }
                }
                return false;
            }
        });
    }
    
    @Override
    public IRepositoryAdsDefinition[] listStrings() {
        final File moduleDir = getDirectory();
        File mlsFile = new File(moduleDir, AdsModule.LOCALE_DIR_NAME);
        HashMap<String, IRepositoryAdsLocaleDefinition> loadedFiles = new HashMap<>();
        if (mlsFile.exists()) {
            for (File langDir : mlsFile.listFiles()) {
                try {
                    EIsoLanguage lang = EIsoLanguage.getForValue(langDir.getName());
                    if (langDir.isDirectory() && lang != null) {
                        for (File file : langDir.listFiles()) {
                            if (file.isDirectory()) {
                                continue;
                            }
                            String name = file.getName();
                            int index = name.indexOf(".xml");
                            name = index > 0 ? name.substring(0, index) : name;
                            if (!loadedFiles.keySet().contains(name)) {
                                Id id = Id.Factory.loadFrom(name);
                                loadedFiles.put(name, new FSRepositoryAdsLocaleDefinition(mlsFile, id));
                            }
                        }
                    }
                } catch (NoConstItemWithSuchValueError ex) {
                    Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                }
            }
        } else {
            loadedFiles = loadFilesFromBin();
        }
        return loadedFiles.values().toArray(new IRepositoryAdsDefinition[loadedFiles.size()]);
    }
    
    private HashMap<String, IRepositoryAdsLocaleDefinition> loadFilesFromBin() {
        final HashMap<String, IRepositoryAdsLocaleDefinition> loadedFiles = new HashMap<>();
        File binFile = getBinariesDirContainer();
        if (binFile.exists()) {
            File[] langDirs = FSRepositoryAdsLocaleDefinition.getBinLocaleFiles(binFile);
            for (File langDir : langDirs) {
                try {
                    JarFile jf = new JarFile(langDir);
                    Enumeration<JarEntry> entries = jf.entries();
                    while (entries.hasMoreElements()) {
                        JarEntry entry = entries.nextElement();
                        String name = entry.getName();
                        int index = name.indexOf(".");
                        if (index > 0) {
                            name = new String(name.substring(0, index));
                            index = name.lastIndexOf(File.separatorChar);
                            if (index != -1) {
                                name = new String(name.substring(index + 1));
                                if (!loadedFiles.keySet().contains(name)) {
                                    Id id = Id.Factory.loadFrom(name);
                                    loadedFiles.put(name, new FSRepositoryAdsLocaleDefinition(new File(binFile.getParentFile(), AdsModule.LOCALE_DIR_NAME), id));
                                }
                            }
                        }
                    }
                } catch (IOException ex) {
                    Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                }
            }
        }
        return loadedFiles;
    }
    
    @Override
    public IRepositoryAdsDefinition[] listDefinitions() {
        final File moduleDir = getDirectory();
        if (moduleDir == null) {
            return null; // module removed before definitions loaded
        }
        File srcDir = new File(moduleDir, AdsModule.SOURCES_DIR_NAME);
        
        if (srcDir.isDirectory() && (injectionInfo == null || !injectionInfo.isHasAPIXml())) {
            
            final HashMap<String, FSRepositoryAdsDefinition> loadedFiles = new HashMap<>();
            
            File[] definitionFiles = listFiles(srcDir);
            
            if (definitionFiles != null) {
                for (File file : definitionFiles) {
                    loadedFiles.put(file.getName(), new FSRepositoryAdsDefinition(file));
                }
            }
            return loadedFiles.values().toArray(new IRepositoryAdsDefinition[0]);
        } else {
            try {
                if (repositories == null && module != null && !module.getLayer().isLocalizing()) {
                    Map<Definition, FSRepositoryAdsDefinition> repsMap = new HashMap<>();
                    if (ids == null) {
                        ids = new HashSet<>();
                    } else {
                        ids.clear();
                    }
                    InputStream stream = getAPIXmlInputStream();
                    if (stream != null) {
                        APIDocument xDoc = APIDocument.Factory.parse(stream);
                        if (xDoc != null && xDoc.getAPI() != null) {
                            for (AdsDefinitionElementType xDef : xDoc.getAPI().getDefinitionList()) {
                                try {
                                    AdsDefinition def = Loader.loadFrom(xDef, true);
                                    if (def.getDefinitionType() != EDefType.LOCALIZING_BUNDLE) {
                                        repsMap.put(def, new FSRepositoryAdsDefinition(def, true));
                                        ids.add(def.getId());
                                    }
                                } catch (Throwable e) {//definition load error
                                    continue;
                                }
                            }
                        }
                    }
                    repositories = new Def2RepMap(repsMap);
                }
                registerAsAPI();
                
                return repositories == null ? new IRepositoryAdsDefinition[0] : repositories.toArray();
            } catch (IOException | XmlException ex) {
                return new IRepositoryAdsDefinition[0];
            }
        }
    }
    
    private void listIds() {
        synchronized (this) {
            if (ids == null) {
                InputStream stream;
                try {
                    stream = getAPIXmlInputStream();
                    if (stream != null) {
                        APISupport.Header header = APISupport.readHeader(stream);
                        if (header != null && header.ids != null && header.ids.length > 0) {
                            this.ids = new HashSet<>();
                            this.ids.addAll(Arrays.asList(header.ids));
                        }
                    }
                } catch (IOException ex) {
                    Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                }
            }
            if (ids == null) {//no id info
                listDefinitions();
            }
        }
    }
    
    private InputStream getAPIXmlInputStream() throws IOException {
        if (injectionInfo != null && injectionInfo.isHasAPIXml()) {
            return injectionInfo.getAPIXmlFileStream();
        }
        return new FileInputStream(new File(getDirectory(), AdsModule.API_FILE_NAME));
    }
    
    @Override
    public IRepositoryAdsDefinition getDefinitionRepository(Definition def) {
        if (injectionInfo != null && injectionInfo.isHasAPIXml() && repositories == null) {
            listDefinitions();
        }
        if (repositories != null) {
            IRepositoryAdsDefinition rep = repositories.get(def);
            if (rep != null) {
                return rep;
            }
        }
        if (def instanceof AdsImageDef) {
            return new FSRepositoryAdsImageDefinition((AdsImageDef) def);
        } else if (def instanceof AdsLocalizingBundleDef) {
            return new FSRepositoryAdsLocaleDefinition((AdsLocalizingBundleDef) def, ((AdsLocalizingBundleDef) def).wasLoadedFromAPI());
        } else {
            return new FSRepositoryAdsDefinition((AdsDefinition) def, ((AdsDefinition) def).wasLoadedFromAPI());
        }
    }
    
    @Override
    public IRepositoryAdsImageDefinition[] listImages() {
        final File moduleDir = getDirectory();
        if (moduleDir == null) {
            return null; // module removed before definitions loaded
        }
        File srcDir = new File(moduleDir, AdsModule.IMAGES_DIR_NAME);
        if (srcDir.isDirectory() && (injectionInfo == null || !injectionInfo.isHasImgJar())) {
            
            final HashMap<String, FSRepositoryAdsDefinition> loadedFiles = new HashMap<>();
            
            File[] definitionFiles = listFiles(srcDir);
            
            if (definitionFiles != null) {
                for (File file : definitionFiles) {
                    loadedFiles.put(file.getName(), new FSRepositoryAdsImageDefinition(file));
                }
            }
            return loadedFiles.values().toArray(new IRepositoryAdsImageDefinition[loadedFiles.size()]);
        } else {
            File imgJar = new File(new File(getBinariesDirContainer(), AdsModule.BINARIES_DIR_NAME), AdsModule.IMG_JAR_NAME);
            return ZipFileRepositoryImageDefinition.listZipFileImages(imgJar);
        }
    }
    
    @Override
    public File getImagesDirectory() {
        File moduleDir = getDirectory();
        if (moduleDir == null) {
            return null;
        } else {
            return new File(moduleDir, AdsModule.IMAGES_DIR_NAME);
        }
    }
    
    @Override
    public File getJavaSrcDirContainer() {
        return getDirectory();
    }
    private File injectionBinariesDir = null;
    
    private static void saveStreamToFile(InputStream in, File file) throws IOException {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            FileUtils.copyStream(in, out);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(FSRepositoryAdsModule.class.getName()).log(Level.FINE, ex.getMessage(), ex);
            }
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(FSRepositoryAdsModule.class.getName()).log(Level.FINE, ex.getMessage(), ex);
            }
        }
    }
    
    private void copyBinary(File dest) throws IOException {
        File binDir = new File(getDirectory(), AdsModule.BINARIES_DIR_NAME);
        if (!binDir.exists()) {
            return;
        }
        File src = new File(binDir, dest.getName());
        if (src.exists()) {
            FileUtils.copyFile(src, dest);
        }
    }
    
    @Override
    public File getBinariesDirContainer() {
        synchronized (this) {
            if (injectionInfo != null && injectionInfo.hasAnyBinary()) {
                if (injectionBinariesDir == null) {
                    try {
                        injectionBinariesDir = File.createTempFile("rdx", "rdx");
                        injectionBinariesDir.delete();
                        if (!injectionBinariesDir.mkdirs()) {
                            return getDirectory();
                        }
                        File binDir = new File(injectionBinariesDir, AdsModule.BINARIES_DIR_NAME);
                        if (!binDir.mkdirs()) {
                            return getDirectory();
                        }
                        try {
                            File destFile = new File(binDir, AdsModule.COMMON_JAR_NAME);
                            if (injectionInfo.isHasCommonJar()) {
                                saveStreamToFile(injectionInfo.getCommonJarFileStream(), destFile);
                            } else {
                                copyBinary(destFile);
                            }
                        } catch (IOException ex) {
                            Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                        }
                        try {
                            File destFile = new File(binDir, AdsModule.SERVER_JAR_NAME);
                            if (injectionInfo.isHasServerJar()) {
                                saveStreamToFile(injectionInfo.getServerJarFileStream(), destFile);
                            } else {
                                copyBinary(destFile);
                            }
                        } catch (IOException ex) {
                            Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                        }
                        try {
                            File destFile = new File(binDir, AdsModule.EXPLORER_JAR_NAME);
                            if (injectionInfo.isHasExplorerJar()) {
                                saveStreamToFile(injectionInfo.getExplorerJarFileStream(), destFile);
                            } else {
                                copyBinary(destFile);
                            }
                        } catch (IOException ex) {
                            Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                        }
                        try {
                            File destFile = new File(binDir, AdsModule.IMG_JAR_NAME);
                            if (injectionInfo.isHasImgJar()) {
                                saveStreamToFile(injectionInfo.getImgJarFileStream(), destFile);
                            } else {
                                copyBinary(destFile);
                            }
                        } catch (IOException ex) {
                            Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                        }
                    } catch (IOException ex) {
                        return getDirectory();
                    }
                }
                return injectionBinariesDir;
            }
            return getDirectory();
        }
    }
    
    @Override
    public IJarDataProvider getJarFile(String pathInModule) throws IOException {
        File file = new File(getDirectory(), pathInModule);
        if (file.exists() && file.getName().endsWith(CompilerConstants.SUFFIX_STRING_JAR)) {
            return JarFileDataProvider.getInstance(file);
        } else {
            return null;
        }
    }
    
    @Override
    public boolean containsDefinition(Id id) {
        if (id == null) {
            return false;
        }
        File srcFile = new File(getDirectory(), AdsModule.SOURCES_DIR_NAME);
        if (srcFile.isDirectory() && (injectionInfo == null || !injectionInfo.isHasAPIXml())) {
            File defFile = new File(srcFile, id.toString() + ".xml");
            return defFile.exists() && defFile.isFile();
        } else {
            listIds();
            return ids != null ? ids.contains(id) : false;
        }
    }
    
    @Override
    public void processException(Throwable e) {
        //ignore
    }
    
    @Override
    public List<Id> getDefinitionIdsByIdPrefix(EDefinitionIdPrefix prefix) {
        if (prefix == null) {
            return Collections.emptyList();
        } else {
            final String asStr = prefix.getValue();
            final String[] fileNames = getDirectory().list(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.startsWith(asStr) && name.endsWith(".xml");
                }
            });
            if (fileNames != null) {
                final List<Id> result = new ArrayList<>(fileNames.length);
                for (String s : fileNames) {
                    Id id = Id.Factory.loadFrom(s.substring(0, s.length() - 4));
                    result.add(id);
                }
                return result;
            } else {
                return Collections.emptyList();
            }
        }
    }
    
    @Override
    public void close() {
        if (this.injectionBinariesDir != null) {
            FileUtils.deleteDirectory(injectionBinariesDir);
        }
    }
    
    @Override
    public InputStream getUsagesXmlInputStream() throws IOException {
        if (injectionInfo != null && injectionInfo.isHasUsagesXml()) {
            return injectionInfo.getUsagesXmlFileStream();
        }
        return new FileInputStream(new File(getDirectory(), AdsModule.USAGES_FILE_NAME));
    }
    
    @Override
    public void installInjection(ModuleInjectionInfo injection) throws IOException {
        super.installInjection(injection);
        this.module.reload();
    }
    
    @Override
    public void uninstallInjection() {
        synchronized (this) {
            super.uninstallInjection();
            
            if (this.injectionBinariesDir != null) {
                FileUtils.deleteDirectory(injectionBinariesDir);
            }
            
            this.repositories = null;
            this.ids = null;
            
            try {
                this.module.reload();
            } catch (IOException ex) {
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
        }
    }
    
    protected void register(Map<Definition, FSRepositoryAdsDefinition> map) {
        if (repositories == null) {
            repositories = new Def2RepMap(map);
        } else {
            repositories.add(map);
        }
        
    }
}
