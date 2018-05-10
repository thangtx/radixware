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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipException;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileReader;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFormatException;
import org.eclipse.jdt.internal.compiler.env.NameEnvironmentAnswer;
import org.eclipse.jdt.internal.compiler.util.SuffixConstants;
import org.radixware.kernel.common.build.xbeans.XbeansSchemaCompiler;
import org.radixware.kernel.common.compiler.core.lookup.AdsJavaPackage;
import org.radixware.kernel.common.compiler.core.lookup.AdsNameEnvironment;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.fs.IJarDataProvider;
import org.radixware.kernel.common.utils.CharOperations;
import org.radixware.kernel.common.utils.Utils;

public abstract class JarBasedPackageLocation extends PackageLocation {

    private List<IJarDataProvider> knownJarFiles;
    private final List<JarBasedPackageLocation.PackageInfo> roots = new LinkedList<>();

    public JarBasedPackageLocation(Layer contextLayer) {
        super(contextLayer);
    }

    private static class ClassInfo implements AdsNameEnvironment.ClassDataProvider {

        private final char[] className;
        private final PackageInfo parent;
        private IJarDataProvider fileLocation;
        private PackageLocation owner;

        public ClassInfo(PackageLocation owner, char[] className, PackageInfo parent, IJarDataProvider location) {
            this.className = className;
            this.parent = parent;
            this.fileLocation = location;
            this.owner = owner;
        }

        @Override
        public NameEnvironmentAnswer getAnswer() {
            try {
                ClassFileReader reader = new ClassFileReader(getClassFileBytes(), getClassFileName('/'), true);
                return new NameEnvironmentAnswer(reader, null);
            } catch (ClassFormatException ex) {
                return null;
            }
        }

        @Override
        public byte[] getClassFileBytes() {
            try {
                return fileLocation.getEntryData(String.valueOf(getClassFileName('/')) + SuffixConstants.SUFFIX_STRING_class);
            } catch (ZipException ex) {
            } catch (IOException ex) {
            }
            return null;
        }

        private char[] getClassFileName(char delimiter) {
            return CharOperations.merge(parent.getFullName(delimiter), className, delimiter);
        }

        @Override
        public String toString() {
            return String.valueOf(getClassFileName('/')); //To change body of generated methods, choose Tools | Templates.
        }
    }

    private static class XBeansTsDirEntry {

        char[] name;
        IJarDataProvider location;

        public XBeansTsDirEntry(char[] name, IJarDataProvider location) {
            this.name = name;
            this.location = location;
        }
    }

    private static class XBeansTsDir extends PackageInfo {

        final IJarDataProvider fileLocation;
        List<char[]> entryNames;

        public XBeansTsDir(PackageInfo parent, char[] name, final IJarDataProvider fileLocation) {
            super(parent, name);
            this.fileLocation = fileLocation;
        }

        public void addEntry(char[] name) {
            if (entryNames == null) {
                entryNames = new LinkedList<>();
            }
            entryNames.add(name);
        }
    }

    private static class XbeansTsDirContainer extends PackageInfo {

        List<XBeansTsDirEntry> entries;

        public XbeansTsDirContainer(PackageInfo parent, char[] name) {
            super(parent, name);
        }

        @Override
        public boolean isXbeansDirContainer() {
            return true;
        }

        public void addEntry(char[] name, IJarDataProvider fileLocation) {
            if (entries == null) {
                entries = new LinkedList<>();
            }
            entries.add(new XBeansTsDirEntry(name, fileLocation));
        }
    }

    private static class XBeansTsDirBunch {

        public String tsName;
        public List<String> namespaces;
        public String fileNamel;
    }

    private static class XBeansDataRoot extends PackageInfo {

        public XBeansDataRoot(PackageInfo parent, char[] name) {
            super(parent, name);
        }

        @Override
        public boolean isXbeansDataRoot() {
            return true;
        }

        @Override
        public boolean invokeRequest(AdsNameEnvironment.XmlNameRequest request) {
            if (this.children == null) {
                return false;
            }
            for (PackageInfo info : this.children) {
                if (info.isXbeansDirContainer()) {
                    if (CharOperations.equals("system".toCharArray(), info.name)) {
                        if (info.children != null) {
                            for (PackageInfo systemName : info.children) {
                                if (systemName instanceof XBeansTsDir) {
                                    if (request.accept(new XmlTsData((XBeansTsDir) systemName))) {
                                        return true;
                                    }
                                }
                            }
                        }
                        return false;
                    }
                }
            }
            return false;
        }
    }

    private static class XmlTsData implements AdsNameEnvironment.XBeansDataProvider {

        private XBeansTsDir systemNameDir;
        private String[] namespaces;

        public XmlTsData(XBeansTsDir systemNameDir) {
            this.systemNameDir = systemNameDir;
        }

        @Override
        public String getTypeSystemName() {
            return String.valueOf(systemNameDir.name);
        }

        @Override
        public String[] getSourceNames() {
            PackageInfo sourcesCache = systemNameDir.parent.parent.findChild("src".toCharArray());
            if (sourcesCache != null && sourcesCache instanceof XbeansTsDirContainer) {
                XbeansTsDirContainer dir = (XbeansTsDirContainer) sourcesCache;
                if (dir.entries != null) {
                    List<String> result = new LinkedList<>();

                    for (XBeansTsDirEntry e : dir.entries) {
                        if (Utils.equals(e.location, this.systemNameDir.fileLocation)) {
                            result.add(String.valueOf(e.name));
                        }
                    }
                    return result.toArray(new String[result.size()]);
                }
            }
            return new String[0];
        }

        @Override
        public InputStream getElementDataStream(String path) {
            IJarDataProvider provider = getDataSource();
            if (provider.entryExists(path)) {
                try {
                    return provider.getEntryDataStream(path);
                } catch (IOException ex) {
                    Logger.getLogger(JarBasedPackageLocation.class.getName()).log(Level.SEVERE, null, ex);
                    return null;
                }
            } else {
                return null;
            }
        }

        @Override
        public InputStream getXmlSourceStream(String sourceName) {
            PackageInfo sourcesCache = systemNameDir.parent.parent.findChild("src".toCharArray());
            IJarDataProvider provider = getDataSource();
            String name = String.valueOf(sourcesCache.getFullName('/')) + "/" + sourceName;
            if (provider.entryExists(name)) {
                try {
                    return provider.getEntryDataStream(name);
                } catch (IOException ex) {
                    Logger.getLogger(JarBasedPackageLocation.class.getName()).log(Level.SEVERE, null, ex);
                    return null;
                }
            } else {
                return null;
            }
        }

        @Override
        public String[] getNamespaces() {
            if (namespaces == null) {
                PackageInfo namespaceCache = systemNameDir.parent.parent.findChild("namespace".toCharArray());
                if (namespaceCache.children != null) {
                    List<String> namespaces = new LinkedList<>();
                    for (PackageInfo child : namespaceCache.children) {
                        if (child instanceof XBeansTsDir) {
                            XBeansTsDir dir = (XBeansTsDir) child;
                            if (Utils.equals(dir.fileLocation, systemNameDir.fileLocation)) {
                                namespaces.add(String.valueOf(dir.name));
                            }
                        }
                    }
                    this.namespaces = new String[namespaces.size()];
                    for (int i = 0; i < this.namespaces.length; i++) {
                        this.namespaces[i] = namespaces.get(i).replace("_3A", ":").replace("_2f", "\\").replace("_2E", ".");
                    }
                } else {
                    this.namespaces = new String[0];
                }
            }
            return this.namespaces;
        }

        @Override
        public IJarDataProvider getDataSource() {
            return systemNameDir.fileLocation;
        }
    }
    private static final char[] STS_DIR_NAME = "system".toCharArray();
    private static final char[] XBEANS_DIR_NAME = XbeansSchemaCompiler.SCHEMAS_DIR_NAME.toCharArray();

    private static class PackageInfo {

        public final char[] name;
        int dept;
        public List<PackageInfo> children;
        public Map<String, ClassInfo> types;
        public final PackageInfo parent;

        public boolean isXbeansDataRoot() {
            return false;
        }

        public boolean isXbeansDirContainer() {
            return false;
        }

        protected PackageInfo(PackageInfo parent, char[] name) {
            this.parent = parent;
            this.name = name;
        }

        public PackageInfo findChild(char[] name) {
            if (children == null) {
                return null;
            }
            for (PackageInfo child : children) {
                if (CharOperations.equals(name, child.name)) {
                    return child;
                }
            }
            return null;
        }

        public boolean isXmlTsContainer() {
            return dept == 3 && CharOperations.equals(parent.name, STS_DIR_NAME) && CharOperations.equals(parent.parent.name, XBEANS_DIR_NAME);
        }

        public boolean fullNameEqualsTo(char[][] packageName) {
            if (dept != packageName.length - 1) {
                return false;
            }
            PackageInfo p = this;
            for (int i = packageName.length - 1; i >= 0; i--) {
                if (!CharOperations.equals(p.name, packageName[i])) {
                    return false;
                }
                p = p.parent;
            }
            return true;
        }

        private void addClass(ClassInfo clazz) {
            if (types == null) {
                types = new HashMap<>();
            }
            final String key = String.valueOf(clazz.className);
            ClassInfo registered = types.get(key);
            if (registered != null) {
                return;
            }
            types.put(key, clazz);
        }

        public NameEnvironmentAnswer findClass(char[][] typeName) {
            char[] simpleName = CharOperations.merge(typeName, '$');
            if (types != null) {
                String key = String.valueOf(simpleName);
                ClassInfo info = types.get(key);
                if (info != null) {
                    return info.getAnswer();
                }
            }
            return null;
        }

        public char[] getFullName(char delimiter) {
            if (parent == null) {
                return name;
            } else {
                return CharOperations.merge(parent.getFullName(delimiter), name, delimiter);
            }
        }

        public char[][] getFullName() {
            if (parent == null) {
                return new char[][]{name};
            } else {
                char[][] name = new char[dept + 1][];
                PackageInfo p = this;
                for (int i = name.length - 1; i >= 0; i--) {
                    name[i] = p.name;
                    p = p.parent;
                }
                return name;
            }
        }

        public PackageInfo findPackage(char[][] packageName) {
            return findPackage(packageName, 0);
        }

        private PackageInfo findPackage(char[][] packageName, int startFrom) {
            if (startFrom < packageName.length) {
                char[] match = packageName[startFrom];
                if (CharOperations.equals(match, name)) {
                    if (startFrom == packageName.length - 1) {
                        return this;
                    } else {
                        if (children != null) {
                            for (JarBasedPackageLocation.PackageInfo child : children) {
                                JarBasedPackageLocation.PackageInfo result = child.findPackage(packageName, startFrom + 1);
                                if (result != null) {
                                    return result;
                                }
                            }
                        }
                        return this;
                    }
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }

        @Override
        public String toString() {
            return String.valueOf(name) + "(" + (children == null ? "leaf" : children.size() + " children") + ")";
        }

        public boolean invokeRequest(AdsNameEnvironment.NameRequest request) {
            char[][] externalName = null;
            if (request instanceof AdsNameEnvironment.PackageNameRequest) {
                externalName = ((AdsNameEnvironment.PackageNameRequest) request).getPackageName();
            }

            if (externalName != null && (externalName.length <= dept || !CharOperations.equals(externalName[dept], name))) {
                return false;
            }

            if (externalName == null || (externalName != null && externalName.length == dept + 1)) {
                if (types != null) {
                    char[][] packageName = getFullName();
                    for (final ClassInfo ci : new ArrayList<>(types.values())) {
                        if (request.accept(packageName, ci.className, ci)) {
                            return true;
                        }
                    }
                }
            }

            if (children != null) {
                boolean needsPackages = request instanceof AdsNameEnvironment.PackageNamePackageRequest;
                for (PackageInfo p : children) {
                    if (needsPackages) {
                        if (externalName == null || externalName != null && externalName.length == dept + 1) {
                            ((AdsNameEnvironment.PackageNamePackageRequest) request).accept(p.getFullName());
                        }
                    }
                    if (p.invokeRequest(request)) {
                        return true;
                    }
                }
            }

            return false;
        }

        public boolean invokeRequest(AdsNameEnvironment.XmlNameRequest request) {

            return false;
        }
    }

    protected abstract List<IJarDataProvider> getJarFiles();

    private PackageInfo findPackageInCash(char[][] packageName) {
        synchronized (this) {
            for (PackageInfo root : roots) {
                PackageInfo result = root.findPackage(packageName);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        List<IJarDataProvider> files = getJarFiles();

        for (IJarDataProvider f : files) {
            sb.append(f.getName()).append("\n");
        }
        return sb.toString();
    }

    private PackageInfo findPackage(char[][] packageName) {

        PackageInfo cachedInstance = findPackageInCash(packageName);
        if (cachedInstance != null && cachedInstance.dept == packageName.length - 1) {
            return cachedInstance;
        } else {
            return null;
        }
    }
    private final String SCHEMA_SOURCE_CONTAINER_PREFIX = XbeansSchemaCompiler.SCHEMAS_DIR_NAME + "/src";

    private void loadJarInfo(IJarDataProvider jar) {
        Collection<IJarDataProvider.IJarEntry> entries = jar.entries();
        PackageInfo packageForType = null;
        for (IJarDataProvider.IJarEntry e : entries) {
            if (e.isDirectory()) {
                String[] parts = e.getName().split("/");
                char[][] packageName = new char[parts.length][];
                for (int i = 0; i < parts.length; i++) {
                    packageName[i] = parts[i].toCharArray();
                }
                packageForType = registerPackage(packageName, jar);
            } else {
                if (e.getName().endsWith(".class")) {
                    String[] parts = e.getName().split("/");
                    char[][] packageName = new char[parts.length - 1][];
                    for (int i = 0; i < parts.length - 1; i++) {
                        packageName[i] = parts[i].toCharArray();
                    }
                    char[] typeName = parts[parts.length - 1].substring(0, parts[parts.length - 1].length() - 6).toCharArray();
                    packageForType = registerType(packageName, typeName, jar, packageForType);
                } else if (e.getName().startsWith(SCHEMA_SOURCE_CONTAINER_PREFIX)) {
                    String[] parts = e.getName().split("/");
                    char[][] packageName = new char[parts.length - 1][];
                    for (int i = 0; i < parts.length - 1; i++) {
                        packageName[i] = parts[i].toCharArray();
                    }
                    char[] fileName = parts[parts.length - 1].toCharArray();
                    packageForType = registerXmlData(packageName, fileName, jar, packageForType);
                }
            }
        }
    }
    public static long time = 0;

    private PackageInfo registerType(char[][] packageName, char[] className, IJarDataProvider jarFile, PackageInfo lastUsedPackage) {
        long t = System.currentTimeMillis();
        PackageInfo targetPackage = null;
        if (lastUsedPackage != null && lastUsedPackage.fullNameEqualsTo(packageName)) {
            targetPackage = lastUsedPackage;
        } else {
            registerPackage(packageName, jarFile);
            targetPackage = findPackage(packageName);
        }
        if (targetPackage != null) {
            targetPackage.addClass(new ClassInfo(this, className, targetPackage, jarFile));
        }
        time += System.currentTimeMillis() - t;
        return targetPackage;
    }

    private PackageInfo registerXmlData(char[][] packageName, char[] entryName, IJarDataProvider jarFile, PackageInfo lastUsedPackage) {
        long t = System.currentTimeMillis();
        PackageInfo targetPackage = null;
        if (lastUsedPackage != null && lastUsedPackage.fullNameEqualsTo(packageName)) {
            targetPackage = lastUsedPackage;
        } else {
            registerPackage(packageName, jarFile);
            targetPackage = findPackage(packageName);
        }
        if (targetPackage instanceof XBeansTsDir) {
            ((XBeansTsDir) targetPackage).addEntry(entryName);
        } else if (targetPackage.isXbeansDirContainer()) {
            ((XbeansTsDirContainer) targetPackage).addEntry(entryName, jarFile);
        }
        time += System.currentTimeMillis() - t;
        return targetPackage;
    }

    private PackageInfo registerPackage(char[][] packageName, IJarDataProvider jarFile) {
        if (packageName.length == 0) {
            return null;
        }
        PackageInfo target = null;
        if (roots != null) {
            for (PackageInfo root : roots) {
                if (CharOperations.equals(root.name, packageName[0])) {
                    target = root;
                    break;
                }
            }
        }
        if (target == null) {
            if (CharOperations.equals(packageName[0], XBEANS_DIR_NAME)) {
                target = new XBeansDataRoot(null, packageName[0]);
            } else {
                target = new PackageInfo(null, packageName[0]);
            }
            target.dept = 0;
            roots.add(target);
        }

        if (packageName.length > 0) {
            PackageInfo parent = target;
            for (int i = 1; i < packageName.length; i++) {
                PackageInfo child = null;
                if (parent.children != null) {
                    for (PackageInfo c : parent.children) {
                        if (CharOperations.equals(c.name, packageName[i])) {
                            child = c;
                            break;
                        }
                    }
                } else {
                    parent.children = new LinkedList<>();
                }
                if (child == null) {
                    if (parent.isXbeansDataRoot()) {
                        child = new XbeansTsDirContainer(parent, packageName[i]);
                    } else if (parent.isXbeansDirContainer()) {
                        child = new XBeansTsDir(parent, packageName[i], jarFile);
                    } else {
                        child = new PackageInfo(parent, packageName[i]);
                    }
                    child.dept = i;
                    parent.children.add(child);
                }

                parent = child;
            }
            return parent;
        }
        return null;
    }

    @Override
    public boolean containsPackageName(char[][] packageName) {
        return findPackage(packageName) != null;
    }

    @Override
    public NameEnvironmentAnswer findAnswer(char[][] packageName, char[][] typeName) {
        PackageInfo info = findPackage(packageName);
        if (info == null) {
            return null;
        }
        NameEnvironmentAnswer answer = info.findClass(typeName);
        return answer;
    }

    @Override
    public boolean invokeRequest(AdsNameEnvironment.NameRequest request) {
        for (PackageInfo root : roots) {
            if (root.invokeRequest(request)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean invokeRequest(AdsNameEnvironment.XmlNameRequest request) {
        for (PackageInfo root : roots) {
            if (root.isXbeansDataRoot()) {
                return root.invokeRequest(request);
            }
        }
        return false;
    }

    @Override
    public void createPackages(AdsJavaPackage rootRegistry) {
        synchronized (this) {
            if (knownJarFiles == null) {
                knownJarFiles = getJarFiles();
            }
            while (!knownJarFiles.isEmpty()) {
                IJarDataProvider file = knownJarFiles.remove(0);
                loadJarInfo(file);
            }

            for (PackageInfo root : roots) {
                registerPackages(root, rootRegistry);
            }
            rootRegistry.addLocation(this);
        }
    }

    private void registerPackages(PackageInfo pkg, AdsJavaPackage current) {
        AdsJavaPackage adsPkg = current.findOrCreatePackage(pkg.name);
        adsPkg.addLocation(this);
        if (pkg.children != null) {
            for (PackageInfo info : pkg.children) {
                registerPackages(info, adsPkg);
            }
        }
    }
}
