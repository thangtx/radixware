package org.radixware.kernel.common.repository.ads.fs;

import java.io.File;
import java.io.FilenameFilter;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.types.Id;

public class FSRepositoryAdsLocalizedDef {
    
    public static void checkLocalizingLayers(final File moduleFile, Id mlbId, final FSRepositoryAdsLocaleDefinition localeRepository) {
        final File adsFile = moduleFile.getParentFile();
        final File layerFile = moduleFile.getParentFile().getParentFile();
        if (layerFile != null) {
            File branchDir = layerFile.getParentFile();
            if (branchDir != null) {
                FilenameFilter filter = new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        try {
                            String str = layerFile.getName() + Layer.LOCALE_LAYER_SUFFIX;
                            return name.startsWith(str) && EIsoLanguage.getForValue(name.substring(str.length())) != null;
                        } catch (NoConstItemWithSuchValueError ex) {
                            return false;
                        }
                    }
                };
                for (File layerDir : branchDir.listFiles(filter)) {
                    String licalizingModuleDir = layerDir.getPath() + File.separatorChar + adsFile.getName() + File.separatorChar + moduleFile.getName().substring(moduleFile.getName().indexOf(File.separatorChar) + 1);
                    loadRepositoryFromLocalizingLayer(new File(licalizingModuleDir), mlbId, localeRepository);
                }
            }
        }
    }
    
    private static void loadRepositoryFromLocalizingLayer(final File moduleDir, Id mlbId, FSRepositoryAdsLocaleDefinition localeRepository) {
        if (moduleDir.exists()) {
            File mlsDir = new File(moduleDir, AdsModule.LOCALE_DIR_NAME);
            if (mlsDir.exists()) {
                FSRepositoryAdsLocaleDefinition licalizingRepository = new FSRepositoryAdsLocaleDefinition(mlsDir, mlbId);
                if (!licalizingRepository.getRepositories().isEmpty()) {
                    for (EIsoLanguage lang : licalizingRepository.getRepositories().keySet()) {
                        if (!localeRepository.getRepositories().containsKey(lang)) {
                            localeRepository.add(licalizingRepository.getRepositories().get(lang), lang);
                        }
                    }
                } /*else {
                 File mlsFile = new File(moduleDir, mlbId.toString()+".xml");
                 if (mlsFile.exists()) {
                 localeRepository.add(new FSRepositoryAdsDefinition(mlsFile));
                 }
                 }*/

            }
        }
    }
}
