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
package org.radixware.kernel.starter.radixloader;

import com.sun.jna.Native;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.security.cert.Certificate;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.logging.LogFactory;
import org.radixware.kernel.starter.log.SafeLogger;
import org.radixware.kernel.starter.meta.FileMeta;
import org.radixware.kernel.starter.meta.RevisionMeta;
import org.radixware.kernel.starter.url.UrlFactory;
import org.radixware.kernel.starter.utils.SystemTools;

public class RadixClassLoader extends java.lang.ClassLoader {

    private static final AtomicInteger counter = new AtomicInteger();
    private final ProtectionDomain pd;
//    private final Map<String, Object> loadedObjects = new HashMap<>();

    private static enum EKernelModule {

        EXPLORER("explorer"),
        SERVER("server"),
        COMMON("common"),
        WEB("web");
        private final String moduleName;

        private EKernelModule(String moduleName) {
            this.moduleName = moduleName;
        }

        public String getModuleName() {
            return moduleName;
        }
    }

    public static interface Factory { // by BAO

        RadixClassLoader createRadixClassLoader(RevisionMeta revisionMeta, RadixLoader radixLoader, Set<String> groupTypes, ClassLoader parent);
    }

    public static final class DefaultFactory implements Factory { // by BAO

        @Override
        public final RadixClassLoader createRadixClassLoader(final RevisionMeta revisionMeta, final RadixLoader radixLoader, final Set<String> groupTypes, final ClassLoader parent) {
            return new RadixClassLoader(revisionMeta, radixLoader, groupTypes, parent);
        }
    }
    private final RevisionMeta revisionMeta;
    private final RadixLoader radixLoader;
    private final Set<String> groupTypes;
    private final Map<String, String> loadedLibs = new HashMap<>();//map repository library name to temp file path
    private boolean selfLoader = false;
    private final int id;//for debugging
    static private String[] own_packages = {"org.radixware.kernel.starter",
        "org.radixware.kernel.common.svn",
        "org.tigris.subversion",
        "org.apache.commons.logging",
        "org.apache.commons.codec",
        "org.apache.http",
        "org.tmatesoft.svn",
        "com.trilead.ssh2",
        "sun.net.www.protocol.starter"};

    static private boolean isOwn(String name) {
        if (name == null) {
            return false;
        }
        for (String pkg : own_packages) {
            if (name.startsWith(pkg)) {
                return true;
            }
        }

        return false;
    }

    public RadixClassLoader(RevisionMeta revisionMeta, RadixLoader loader, Set<String> groupTypes, ClassLoader parent) {
        super(parent);

        pd = new ProtectionDomain(null, null);

        this.revisionMeta = revisionMeta;
        this.radixLoader = loader;
        this.groupTypes = groupTypes;
        this.id = counter.incrementAndGet();
    }

    public RadixLoader getRadixLoader() {
        return radixLoader;
    }

    public Set<String> getGroupTypes() {
        return groupTypes;
    }

    public RevisionMeta getRevisionMeta() {
        return revisionMeta;
    }

    public void setSelfLoader(boolean selfLoader) {
        this.selfLoader = selfLoader;
    }

    @Override
    protected final Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        return loadClass(name, resolve, null);
    }

    protected Class<?> loadClass(String name, boolean resolve, final Object hint) throws ClassNotFoundException {

        Class c = findLoadedClass(name);

        if (c == null) {
            try {
                if (getParent() != null && (!selfLoader || !isOwn(name)) && !loadingByParentForbidden(name, hint)) {
                    c = getParent().loadClass(name);
                }
            } catch (ClassNotFoundException e) {
            }

            if (c == null && !loadingByThisForbidden(name, hint)) {
                c = findClass(name);
            }
        }
        if (c != null) {
            if (resolve) {
                resolveClass(c);
            }
            return c;
        }
        throw new ClassNotFoundException(name);
    }

    protected boolean loadingByParentForbidden(final String name, final Object hint) {
        return LibraryLoader.class.getName().equals(name);
    }

    protected boolean loadingByThisForbidden(final String name, final Object hint) {
        return false;
    }

    protected String classNameToFileName(final String className) {
        return className.replace('.', '/') + ".class";
    }

    protected Object getRadixClassloadingLock() {
        return this;
    }

//    public void registerLoadedObject(final String key, final Object object) {
//        if (key != null && object != null) {
//            loadedObjects.put(key, object);
//        }
//    }
//
//    public Object getLoadedObject(final String key) {
//        return loadedObjects.get(key);
//    }
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if (!selfLoader || isOwn(name)) {
            Class c = null;
            String file_name = classNameToFileName(name);
            FileCache.ClassFileData data = null;
            try {
                data = getClassFileData(file_name);
            } catch (IOException e) {
                throw new ClassNotFoundException("RadixClassLoader.loadClass: " + name, e);
            }
            if (data != null) {
                synchronized (getRadixClassloadingLock()) {
                    c = findLoadedClass(name); // check loaded classes again
                    if (c == null) {
                        int i = name.lastIndexOf('.');
                        if (i != -1) {
                            String pkgname = name.substring(0, i);
                            Package pkg = getPackage(pkgname);
                            if (pkg == null) {
                                definePackage(pkgname, null, null, null, null, null, null, null);
                            }
                        }
                        c = defineClass(name, data.data, 0, data.data.length, pd);
                        afterDefine(c);
                    }
                }
            }
            return c;
        }
        throw new ClassNotFoundException(name);
    }

    protected void afterDefine(final Class clazz) {
    }

    protected FileCache.ClassFileData getClassFileData(final String fileName) throws IOException {
        return radixLoader.readClassFileData(fileName, groupTypes, revisionMeta);
    }

    @Override
    public URL getResource(String name) {
        URL url = null;
        if (getParent() != null && (!selfLoader || !isOwn(name))) {
            url = getParent().getResource(name);
        }
        if (url == null) {
            url = findResource(name);
        }
        return url;
    }

    @Override
    protected URL findResource(String name) {
        if (!selfLoader || isOwn(name)) {
            try {
                Enumeration<URL> urls = findResources(name);
                if (urls.hasMoreElements()) {
                    return urls.nextElement();
                }
            } catch (IOException e) {
                SafeLogger.getInstance().info(RadixClassLoader.class, "RadixClassLoader.findResource: " + e.getMessage());
            }
        }
        return null;
    }

    @Override
    public Enumeration<URL> getResources(String name) throws IOException {
        if (getParent() != null && (!selfLoader || !isOwn(name))) {
            Enumeration[] tmp = new Enumeration[2];
            tmp[0] = getParent().getResources(name);
            tmp[1] = findResources(name);
            return new CompoundEnumeration(tmp);
        }
        return findResources(name);
    }

    @Override
    protected Enumeration<URL> findResources(String name) throws IOException {
        Vector<URL> urls = new Vector<>();
        if (!selfLoader || isOwn(name)) {
            FileMeta meta = revisionMeta.findResources(name);
            while (meta != null) {
                if (groupTypes == null || groupTypes.contains(meta.getGroupType())) {
                    urls.add(new URL(UrlFactory.protocolForRev(revisionMeta.getNum()), null, 0, "/" + meta.getArchive() + ';' + meta.getName()));
                }
                meta = meta.getNext();
            }
        }
        return urls.elements();
    }

    @Override
    protected String findLibrary(String libname) {
        for (EKernelModule kernelModule : EKernelModule.values()) {
            try {
                final String loadedLibPath = tryLoadNativeLibrary(kernelModule.getModuleName(), libname, true);
                if (loadedLibPath != null) {
                    return loadedLibPath;
                }
            } catch (RadixLoaderException ex) {
                throw new RuntimeException(ex);
            }
        }
        return super.findLibrary(libname);
    }

    public synchronized void loadNativeLibrary(String kernelModuleName, String libShortName) throws RadixLoaderException {
        tryLoadNativeLibrary(kernelModuleName, libShortName, false);
    }

    private String tryLoadNativeLibrary(String kernelModuleName, String libShortName, boolean ignoreNotFoundEx) throws RadixLoaderException {
        String file = SystemTools.getNativeLibLayerPath(kernelModuleName, libShortName);
        String libFile = loadedLibs.get(file);
        if (libFile == null) {
            final FileMeta file_meta;
            try {
                file_meta = getNativeLibFileMeta(kernelModuleName, libShortName);
            } catch (RadixLoaderException ex) {
                if (ignoreNotFoundEx) {
                    return null;
                } else {
                    throw ex;
                }
            }
            libFile = radixLoader.getNativeLibrary(file_meta, revisionMeta);
            try {
                getLibraryLoaderClass().getMethod("loadLibrary", String.class).invoke(null, libFile);
            } catch (Exception ex) {
                throw new RadixLoaderException("Call of LibraryLoader.loadlibrary() error", ex);
            }
            loadedLibs.put(file, libFile);
        }
        return libFile;
    }

    private FileMeta getNativeLibFileMeta(final String kernelModuleName, final String libShortName) throws RadixLoaderException {
        final String file = SystemTools.getNativeLibLayerPath(kernelModuleName, libShortName);
        FileMeta file_meta = revisionMeta.findOverFile(file);
        if (file_meta == null) {
            file_meta = revisionMeta.findResources(file);
        }
        if (file_meta == null) {
            throw new RadixLoaderException("Native library " + file + " was not specified in directory.xml files");
        }
        return file_meta;
    }

    public synchronized Object loadNativeJNALibrary(final String kernelModuleName, final String libShortName, final Class type) throws RadixLoaderException {
        final FileMeta file_meta = getNativeLibFileMeta(kernelModuleName, libShortName);
        final String libAbsPath = radixLoader.getNativeLibrary(file_meta, revisionMeta);
        final File libFile = new File(libAbsPath);
        if (!libFile.exists()) {
            throw new RadixLoaderException("Library file not exists: " + libAbsPath);
        }
        try {
            LogFactory.getLog(RadixClassLoader.class).info("Loading native library '" + libShortName + "' from module '" + kernelModuleName + "'...");
            return getLibraryLoaderClass().getMethod("loadJNANativeLibrary", String.class, Class.class).invoke(null, new File(libAbsPath).getAbsolutePath(), type);
        } catch (Throwable t) {
            LogFactory.getLog(RadixClassLoader.class).info("Unable to load native library '" + libShortName + "' from module '" + kernelModuleName + "' via absolute path, trying to set jna.library.path...");
        }
        System.setProperty("jna.library.path", libFile.getParentFile().getAbsolutePath());
        try {
            return getLibraryLoaderClass().getMethod("loadJNANativeLibrary", String.class, Class.class).invoke(null, new File(libAbsPath).getAbsolutePath(), type);
        } catch (Exception ex) {
            throw new RadixLoaderException("Error calling LibraryLoader.loadJNANativeLibrary()", ex);
        }
    }

    private Class getLibraryLoaderClass() throws ClassNotFoundException {
        RadixClassLoader classLoader = this;
        Class library_loader_class = classLoader.findClass(LibraryLoader.class.getCanonicalName());
        while (library_loader_class == null && classLoader.getParent() instanceof RadixClassLoader) {
            classLoader = (RadixClassLoader) classLoader.getParent();
            library_loader_class = classLoader.findClass(LibraryLoader.class.getCanonicalName());
        }
        if (library_loader_class == null) {
            throw new ClassNotFoundException(LibraryLoader.class.getCanonicalName());
        }
        return library_loader_class;
    }

    class CompoundEnumeration<E> implements Enumeration<E> {

        private Enumeration[] enums;
        private int index = 0;

        public CompoundEnumeration(Enumeration[] enums) {
            this.enums = enums;
        }

        private boolean next() {
            while (index < enums.length) {
                if (enums[index] != null && enums[index].hasMoreElements()) {
                    return true;
                }
                index++;
            }
            return false;
        }

        @Override
        public boolean hasMoreElements() {
            return next();
        }

        @Override
        public E nextElement() {
            if (!next()) {
                throw new NoSuchElementException();
            }
            return (E) enums[index].nextElement();
        }
    }

    public static RevisionMeta getRevisionMeta(final Object o) {
        if (o == null) {
            return null;
        }
        final RadixClassLoader classLoader;
        if (o instanceof RadixClassLoader) {
            classLoader = (RadixClassLoader) o;
        } else if (o instanceof Class && ((Class) o).getClassLoader() instanceof RadixClassLoader) {
            classLoader = ((RadixClassLoader) (((Class) o).getClassLoader()));
        } else if (o.getClass().getClassLoader() instanceof RadixClassLoader) {
            classLoader = (RadixClassLoader) o.getClass().getClassLoader();
        } else {
            return null;
        }
        return classLoader.getRevisionMeta();
    }
}
