/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.radixware.kernel.common.lang;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.FileUtils;

/**
 *
 * @author npopov
 */
public class ClassLoaderUtil {

    public static byte[] findClassFromADS(String name, ClassLinkageAnalyzer.LayerInfo layer, ERuntimeEnvironmentType env) throws ClassNotFoundException {
        int layerPackageNameLen = checkLayerName(layer, name);
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
                ClassLinkageAnalyzer.ModuleInfo[] modules = findModuleByPackageNameId(layer, moduleId);
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

                for (ClassLinkageAnalyzer.ModuleInfo module : modules) {
                    if (!org.radixware.kernel.common.utils.Utils.equals(module.getLayer().getURI(), layer.getURI())) {
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
                                    return FileUtils.getZipEntryByteContent(e, jar);
                                }
                            }
                        } finally {
                            jar.close();
                        }
                    } catch (IOException ex) {
                        failCause = ex;
                        //throw new ClassNotFoundException(name, ex);
                    }
                }
                throw new ClassNotFoundException(name, failCause);
            }
            throw new ClassNotFoundException(name, failCause);
        }
        throw new ClassNotFoundException(name, failCause);
    }

    private static int checkLayerName(ClassLinkageAnalyzer.LayerInfo layer, String name) {
        String layerPackage = getLayerPackageName(layer);
        if (name.startsWith(layerPackage)) {
            return layerPackage.length();
        }
        return checkAllLayersName(layer, name);
    }

    private static int checkAllLayersName(ClassLinkageAnalyzer.LayerInfo info, String name) {
        List<ClassLinkageAnalyzer.LayerInfo> prevs = info.findPrevLayer();
        for (ClassLinkageAnalyzer.LayerInfo prev : prevs) {
            String layerPackage = getLayerPackageName(prev);
            if (name.startsWith(layerPackage)) {
                return layerPackage.length();
            }
        }
        for (ClassLinkageAnalyzer.LayerInfo prev : prevs) {
            int len = checkAllLayersName(prev, name);
            if (len > 0) {
                return len;
            }
        }
        return -1;
    }

    private static String getLayerPackageName(ClassLinkageAnalyzer.LayerInfo layer) {
        return layer.getURI().replace('-', '_');
    }

    private static ClassLinkageAnalyzer.ModuleInfo[] findModuleByPackageNameId(ClassLinkageAnalyzer.LayerInfo layer, Id moduleId) {
        ClassLinkageAnalyzer.ModuleInfo[] module = layer.findModulesByPackageNameId(moduleId);
        if (module != null && module.length > 0) {
            return module;
        }
        return null;
    }
}
