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
package org.radixware.kernel.release.fs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.module.APISupport;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.module.Loader;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.repository.ads.fs.IRepositoryAdsDefinition;
import org.radixware.kernel.common.repository.ads.fs.IRepositoryAdsImageDefinition;
import org.radixware.kernel.common.repository.ads.fs.IRepositoryAdsModule;
import org.radixware.kernel.common.repository.fs.IJarDataProvider;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.starter.meta.FileMeta;
import org.radixware.kernel.starter.radixloader.RadixLoaderException;
import org.radixware.schemas.adsdef.APIDocument;
import org.radixware.schemas.adsdef.AdsDefinitionElementType;

class RfsRepositoryAdsModule extends RfsRepositoryModule<AdsModule> implements IRepositoryAdsModule {

    private Map<Definition, RfsRepositoryAdsDefinition> repositories = null;
    private Set<Id> ids = null;
    private AdsModule owner = null;

    public RfsRepositoryAdsModule(RfsRepositorySegment<AdsModule> segment, String dirName) {
        super(segment, dirName);
    }

    @Override
    public IRepositoryAdsDefinition getDefinitionRepository(Definition def) {
        synchronized (this) {
            if (repositories == null) {
                listDefinitions();
            }
            if (def instanceof AdsDefinition) {
                return repositories.get(def);
            } else {
                return null;
            }
        }
    }

    private void listIds() {
        synchronized (this) {
            if (ids == null) {
                final String apiXmlName = dirName + AdsModule.API_FILE_NAME;
                FileMeta meta = getRelease().getRevisionMeta().findFile(apiXmlName);
                if (meta != null) {
                    InputStream stream;
                    try {
                        stream = getRelease().getRepositoryFileStream(apiXmlName);
                        if (stream != null) {
                            APISupport.Header header = APISupport.readHeader(stream);
                            if (header != null && header.ids != null && header.ids.length > 0) {
                                this.ids = new HashSet<Id>();
                                this.ids.addAll(Arrays.asList(header.ids));
                            }
                        }
                    } catch (RadixLoaderException ex) {
                        Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
                    } catch (IOException ex) {
                        Logger.getLogger(RfsRepositoryAdsModule.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if (ids == null) {//no id info
                    listDefinitions();
                }
            }
        }
    }

    @Override
    public IRepositoryAdsDefinition[] listDefinitions() {
        synchronized (this) {
            try {
                if (repositories == null) {
                    repositories = new HashMap<>();
                    if (ids == null) {
                        ids = new HashSet<>();
                    } else {
                        ids.clear();
                    }
                    //final ArrayList<RfsRepositoryAdsDefinition> defs = new ArrayList<RfsRepositoryAdsDefinition>();
                    //final HashMap<Id, AdsLocalizingBundleDef> bundles = new HashMap<>();
                    final String apiXmlName = dirName + AdsModule.API_FILE_NAME;
                    FileMeta meta = getRelease().getRevisionMeta().findFile(apiXmlName);
                    if (meta != null) {
                        InputStream stream = getRelease().getRepositoryFileStream(apiXmlName);
                        if (stream != null) {
                            APIDocument xDoc = APIDocument.Factory.parse(stream);
                            if (xDoc != null && xDoc.getAPI() != null) {
                                for (AdsDefinitionElementType xDef : xDoc.getAPI().getDefinitionList()) {
                                    try {
                                        AdsDefinition def = Loader.loadFrom(xDef, true);
                                        if (def.getDefinitionType() != EDefType.LOCALIZING_BUNDLE) {
                                            //bundles.put(def.getId(), (AdsLocalizingBundleDef) def);
                                            //} else {
                                            repositories.put(def, new RfsRepositoryAdsDefinition(def, getRelease(), dirName));
                                            ids.add(def.getId());
                                        }
                                    } catch (Throwable e) {//definition load error
                                        continue;
                                    }
                                }
                                //for (RfsRepositoryAdsDefinition def : repositories.values()) {
                                //   AdsLocalizingBundleDef bundle = bundles.get(def.pointer.getLocalizingBundleId());
                                //   if (bundle != null) {
                                //       def.setBundle(bundle);
                                //       ids.add(bundle.getId());
                                //   }
                                //}

                            }
                        }
                    }
                }
                return repositories.values().toArray(new IRepositoryAdsDefinition[repositories.size()]);
            } catch (Exception ex) {
                return new IRepositoryAdsDefinition[0];
            }
        }
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
        return null;
    }

    @Override
    public File getJavaSrcDirContainer() {
        return getTemporaryStorage();
    }

    @Override
    public File getBinariesDirContainer() {
        return getTemporaryStorage();
    }

    @Override
    public IJarDataProvider getJarFile(String pathInModule) throws IOException {
        String jarFileName = dirName + pathInModule;
        FileMeta fileMeta = getRelease().getRevisionMeta().findFile(jarFileName);
        if (fileMeta == null) {
            return null;
        }
        return new RfsJarFileDataProvider(getRelease(), fileMeta);
    }

    @Override
    public boolean containsDefinition(Id id) {
        synchronized (this) {
            listIds();
            return ids.contains(id);
        }
    }

    @Override
    public void processException(Throwable e) {
        getRelease().processException(e);
    }

    @Override
    public List<Id> getDefinitionIdsByIdPrefix(EDefinitionIdPrefix prefix) {
        synchronized (this) {
            listIds();
            List<Id> result = new LinkedList<Id>();
            for (Id id : ids) {
                if (id.getPrefix() == prefix) {
                    result.add(id);
                }
            }
            return result;
        }
    }

    @Override
    public void close() {
    }

    @Override
    public InputStream getUsagesXmlInputStream() throws IOException {
        final String usagesXmlFileName = dirName + AdsModule.API_FILE_NAME;
        FileMeta meta = getRelease().getRevisionMeta().findFile(usagesXmlFileName);
        if (meta != null) {
            return getRelease().getRepositoryFileStream(usagesXmlFileName);
        } else {
            throw new FileNotFoundException(usagesXmlFileName);
        }
    }

    @Override
    public IRepositoryAdsDefinition getMlsRepository() {
        if (owner != null){
            return new RfsRepositoryAdsLocaleDefinition(owner, getRelease(), dirName);
        }
        return null;
    }
    
    @Override
    public void setModule(AdsModule module) {
        owner = module;
    }
}
