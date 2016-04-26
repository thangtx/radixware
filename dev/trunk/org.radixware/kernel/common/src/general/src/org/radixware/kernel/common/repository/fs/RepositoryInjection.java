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

package org.radixware.kernel.common.repository.fs;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.enums.ERepositorySegmentType;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.ssl.KeystoreController;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.schemas.product.ModuleDocument;


public class RepositoryInjection {

    public abstract static class ModuleInjectionInfo {

        private String name;
        private boolean hasAPIXml = false;
        private boolean hasModuleXml = false;
        private boolean hasDirectoryXml = false;
        private boolean hasUsagesXml = false;
        private boolean hasCommonJar = false;
        private boolean hasServerJar = false;
        private boolean hasExplorerJar = false;
        private boolean hasImgJar = false;
        private Id id;
        private static final Id NO_ID = Id.Factory.loadFrom("mdlNO_ID_FOR_MODULE_INJECTION_FOUND_STATIC_MARKER");

        public ModuleInjectionInfo(String name) {
            this.name = name;
        }

        public boolean hasImportantChanges() {
            return hasAPIXml || hasModuleXml || hasUsagesXml || hasCommonJar || hasServerJar || hasExplorerJar || hasImgJar;
        }

        /**
         * returns module id or null if id is unknown
         */
        public Id getId() {
            if (isHasModuleXml()) {
                if (id == null) {
                    InputStream stream = null;
                    try {
                        stream = getAPIXmlFileStream();
                        ModuleDocument xDoc = ModuleDocument.Factory.parse(stream);
                        id = Id.Factory.loadFrom(xDoc.getModule().getId());
                        if (id == null) {
                            id = NO_ID;
                        }
                    } catch (XmlException ex) {
                        id = NO_ID;
                    } catch (IOException ex) {
                        id = NO_ID;
                    } finally {
                        if (stream != null) {
                            try {
                                stream.close();
                            } catch (IOException ex) {
                                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                            }
                        }
                    }
                }
                return id == NO_ID ? null : id;
            } else {
                return null;
            }
        }

        public abstract ERepositorySegmentType getSegmentType();

        public abstract String getLayerURI();

        public abstract InputStream getDescriptionFileStream() throws IOException;

        public abstract InputStream getDirectoryXmlFileStream() throws IOException;

        public abstract InputStream getAPIXmlFileStream() throws IOException;

        public abstract InputStream getUsagesXmlFileStream() throws IOException;

        public abstract InputStream getImgJarFileStream() throws IOException;

        public abstract InputStream getServerJarFileStream() throws IOException;

        public abstract InputStream getExplorerJarFileStream() throws IOException;

        public abstract InputStream getCommonJarFileStream() throws IOException;

        public boolean isHasAPIXml() {
            return hasAPIXml;
        }

        public boolean isHasCommonJar() {
            return hasCommonJar;
        }

        public boolean isHasDirectoryXml() {
            return hasDirectoryXml;
        }

        public boolean isHasExplorerJar() {
            return hasExplorerJar;
        }

        public boolean isHasImgJar() {
            return hasImgJar;
        }

        public boolean isHasModuleXml() {
            return hasModuleXml;
        }

        public boolean isHasServerJar() {
            return hasServerJar;
        }

        public boolean hasAnyBinary() {
            return hasServerJar || hasCommonJar || hasExplorerJar || hasImgJar;
        }

        public boolean isHasUsagesXml() {
            return hasUsagesXml;
        }

        public String getName() {
            return name;
        }

        public void setHasAPIXml(boolean hasAPIXml) {
            this.hasAPIXml = hasAPIXml;
        }

        public void setHasCommonJar(boolean hasCommonJar) {
            this.hasCommonJar = hasCommonJar;
        }

        public void setHasDirectoryXml(boolean hasDirectoryXml) {
            this.hasDirectoryXml = hasDirectoryXml;
        }

        public void setHasExplorerJar(boolean hasExplorerJar) {
            this.hasExplorerJar = hasExplorerJar;
        }

        public void setHasImgJar(boolean hasImgJar) {
            this.hasImgJar = hasImgJar;
        }

        public void setHasModuleXml(boolean hasModuleXml) {
            this.hasModuleXml = hasModuleXml;
        }

        public void setHasServerJar(boolean hasServerJar) {
            this.hasServerJar = hasServerJar;
        }

        public void setHasUsagesXml(boolean hasUsagesXml) {
            this.hasUsagesXml = hasUsagesXml;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("Injection for module ").append(name);
            return builder.toString();
        }
    }
    private final Map<String, List<ModuleInjectionInfo>> elements;

    public RepositoryInjection(Collection<? extends ModuleInjectionInfo> injection) {
        if (injection == null) {
            this.elements = null;
        } else {
            this.elements = new HashMap<String, List<ModuleInjectionInfo>>();
            for (ModuleInjectionInfo m : injection) {
                List<ModuleInjectionInfo> list = this.elements.get(m.getLayerURI());
                if (list == null) {
                    list = new LinkedList<ModuleInjectionInfo>();
                    this.elements.put(m.getLayerURI(), list);
                }
                list.add(m);
            }
        }
    }

    public <T extends Module> ModuleInjectionInfo findInjection(ERepositorySegmentType st, String layerURI, Id moduleId, String moduleName) {
        if (elements == null) {
            return null;
        }
        List<? extends ModuleInjectionInfo> list = elements.get(layerURI);
        if (list == null) {
            return null;
        }
        //first search by id
        List<ModuleInjectionInfo> idlessList = new LinkedList<ModuleInjectionInfo>();
        for (ModuleInjectionInfo e : list) {
            if (e.getSegmentType() == st) {
                Id infoId = e.getId();
                if (infoId != null) {
                    if (infoId == moduleId) {
                        return e;
                    }
                } else {
                    idlessList.add(e);
                }
            }
        }
        //search by name
        for (ModuleInjectionInfo e : idlessList) {
            if (Utils.equals(moduleName, e.getName())) {
                return e;
            }
        }

        return null;
    }
    private WeakReference<Branch> installBranch = null;

    public void install(Branch branch) throws IOException {
        List<Module> modules = new LinkedList<Module>();
        for (Layer layer : branch.getLayers()) {
            for (Module module : layer.getAds().getModules()) {
                modules.add(module);
            }
            for (Module module : layer.getDds().getModules()) {
                modules.add(module);
            }
        }
        for (Module module : modules) {
            RepositoryInjection.ModuleInjectionInfo injectionInfo = findInjection(module.getSegment().getType(), module.getSegment().getLayer().getURI(), module.getId(), module.getName());
            if (injectionInfo != null) {
                try {
                    module.getRepository().installInjection(injectionInfo);
                } catch (IOException e) {
                    for (Module m : modules) {
                        m.getRepository().uninstallInjection();
                    }
                    throw e;
                }
            }
        }
        installBranch = new WeakReference<Branch>(branch);
    }

    public void uninstall() {
        if (installBranch != null) {
            Branch branch = installBranch.get();
            if (branch != null) {
                List<Module> modules = new LinkedList<Module>();
                for (Layer layer : branch.getLayers()) {
                    for (Module module : layer.getAds().getModules()) {
                        if (module.getRepository().isInjection()) {
                            modules.add(module);
                        }
                    }
                    for (Module module : layer.getDds().getModules()) {
                        if (module.getRepository().isInjection()) {
                            modules.add(module);
                        }
                    }
                }
                for (Module module : modules) {
                    module.getRepository().uninstallInjection();
                }
            }
        }
    }
}
