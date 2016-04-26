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

package org.radixware.kernel.common.defs.ads.userfunc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.repository.ads.fs.IRepositoryAdsDefinition;
import org.radixware.kernel.common.repository.ads.fs.IRepositoryAdsImageDefinition;
import org.radixware.kernel.common.repository.ads.fs.IRepositoryAdsModule;
import org.radixware.kernel.common.repository.fs.IJarDataProvider;
import org.radixware.kernel.common.repository.fs.JarFileDataProvider;
import org.radixware.kernel.common.repository.fs.RepositoryInjection.ModuleInjectionInfo;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.FileUtils;


class UFModuleRepository implements IRepositoryAdsModule {

    private File repositoryDir;

    private File getRepositoryDir() {
        synchronized (this) {
            if (repositoryDir == null) {
                try {
                    repositoryDir = File.createTempFile("RDX_UF_", "_RDX_UF");
                    repositoryDir.delete();
                    repositoryDir.mkdirs();
                    if (!repositoryDir.isDirectory()) {
                        throw new RadixError("Unable to create temporary directory for user function compilation needs (mkdirs() returns false)");
                    }
                } catch (IOException ex) {
                    throw new RadixError("Unable to create temporary directory for user function compilation needs", ex);
                }
            }
            return repositoryDir;
        }
    }

    @Override
    public IRepositoryAdsDefinition getDefinitionRepository(Definition def) {
        return null;
    }

    @Override
    public IRepositoryAdsDefinition[] listDefinitions() {
        return new IRepositoryAdsDefinition[0];
    }

    @Override
    public IRepositoryAdsDefinition[] listStrings() {
        return new IRepositoryAdsDefinition[0];
    }

    @Override
    public IRepositoryAdsImageDefinition[] listImages() {
        return new IRepositoryAdsImageDefinition[0];
    }

    @Override
    public File getImagesDirectory() {
        return new File(getRepositoryDir(), "img");
    }

    @Override
    public File getJavaSrcDirContainer() {
        return getRepositoryDir();
    }

    @Override
    public File getBinariesDirContainer() {
        return getRepositoryDir();
    }

    @Override
    public IJarDataProvider getJarFile(String pathInModule) throws IOException {
        File file = new File(getRepositoryDir(), pathInModule);
        if (file.exists()) {
            return JarFileDataProvider.getInstance(file);
        } else {
            return null;
        }
    }

    @Override
    public boolean containsDefinition(Id id) {
        return id.getPrefix() == EDefinitionIdPrefix.USER_DEFINED_REPORT || id.getPrefix() == EDefinitionIdPrefix.APPLICATION_ROLE
                || id.getPrefix() == EDefinitionIdPrefix.LIB_USERFUNC_PREFIX;
    }

    @Override
    public List<Id> getDefinitionIdsByIdPrefix(EDefinitionIdPrefix prefix) {
        return Collections.emptyList();
    }

    @Override
    public void close() {
        synchronized (this) {
            if (repositoryDir != null && repositoryDir.isDirectory()) {
                FileUtils.deleteDirectory(repositoryDir);
            }
        }
    }

    @Override
    public InputStream getUsagesXmlInputStream() throws IOException {
        return null;
    }

    @Override
    public File getDirectory() {
        return getRepositoryDir();
    }

    @Override
    public File getDescriptionFile() {
        return new File(getRepositoryDir(), AdsModule.MODULE_XML_FILE_NAME);
    }

    @Override
    public InputStream getDescriptionData() throws IOException {
        return new FileInputStream(getDescriptionFile());
    }

    @Override
    public InputStream getDirectoryXmlData() throws IOException {
        return new FileInputStream(new File(getRepositoryDir(), FileUtils.DIRECTORY_XML_FILE_NAME));
    }

    @Override
    public String getName() {
        return "UFModule";
    }

    @Override
    public String getPath() {
        return getRepositoryDir().getAbsolutePath();
    }

    @Override
    public boolean isInjection() {
        return false;
    }

    @Override
    public void installInjection(ModuleInjectionInfo injection) throws IOException {
    }

    @Override
    public void uninstallInjection() {
    }

    @Override
    public void processException(Throwable e) {
    }

    /*public File getAPIDirectory(){
     return getDirectory();
     }*/
    @Override
    public List<IJarDataProvider> getBinaries() {
        return Collections.emptyList();
    }
}
