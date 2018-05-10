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
package org.radixware.kernel.common.svn.utils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipFile;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.svn.RadixSvnException;
import org.radixware.kernel.common.svn.SVNRepositoryAdapter;
import org.radixware.kernel.common.svn.utils.Utils.LayerInfo;

public class BranchHolder {

    protected long revision;
    protected List<String> removedFiles = new LinkedList<>();
    protected List<String> kernelJars = new LinkedList<>();
    protected List<String> developmentLayers = new LinkedList<>();
    private List<LayerInfo> topLayers;
    final Map<String, LayerInfo> layers = new HashMap<>();
    protected final boolean verbose;
    protected final NoizyVerifier verifier;

    public BranchHolder(NoizyVerifier verifier, boolean verbose) {
        this.verbose = verbose;
        this.verifier = verifier;
    }

    public boolean initializeEx(SVNRepositoryAdapter repository, final List<String> layersPathList, ZipFile zipFile, String zipFilePath, String predefinedBaseDevLayerURI, boolean skipDevelopmentLayers) {
        return initialize(new BranchHolderParams(repository, layersPathList, zipFile, zipFilePath, skipDevelopmentLayers, predefinedBaseDevLayerURI));
    }

    public boolean initialize(SVNRepositoryAdapter repository, String branchPath, ZipFile zipFile, String zipFilePath, String predefinedBaseDevLayerURI, boolean skipDevelopmentLayers) {
        return initialize(new BranchHolderParams(repository, branchPath, null, zipFile, zipFilePath, skipDevelopmentLayers, predefinedBaseDevLayerURI));
    }
    
    public boolean initialize(BranchHolderParams bParams) {
        try {
            this.revision = bParams.getRepository().getLatestRevision();
            List<LayerInfo> infos = new LinkedList<>();
            List<String> addedAndModifiedLayerDescriptions = new LinkedList<>();
            List<org.radixware.kernel.common.svn.utils.Utils.ZipModule> zipModules = new LinkedList<>();
            
            if (!org.radixware.kernel.common.svn.utils.Utils.uploadModulesFromZipFile(verbose, verifier,  bParams.getUpgradeFile(), bParams.getUpgradeFilePath(), zipModules, removedFiles, kernelJars, addedAndModifiedLayerDescriptions)) {
                return false;
            }
            if (bParams.getBranchPath() != null) {
                if (!Utils.listLayers(verbose, bParams.getRepository(), this.revision, bParams.getBranchPath(), bParams.getPredefinedBaseDevLayerURI(), bParams.isSkipDevelopmentLayers(), verifier, infos, removedFiles, bParams.getUpgradeFile(), bParams.getUpgradeFilePath(), kernelJars, developmentLayers, addedAndModifiedLayerDescriptions, bParams.getUpgradeFile(), bParams.getUpgradeFilePath())) {
                    return false;
                }
            } else {
                if (!Utils.listLayersEx(verbose, bParams.getRepository(), this.revision, bParams.getLayersPathList(), bParams.getPredefinedBaseDevLayerURI(), bParams.isSkipDevelopmentLayers(), verifier, infos, removedFiles, bParams.getUpgradeFile(), bParams.getUpgradeFilePath(), kernelJars, developmentLayers, addedAndModifiedLayerDescriptions, bParams.getUpgradeFile(), bParams.getUpgradeFilePath())) {
                    return false;
                }
            }
            for (LayerInfo info : infos) {
                if (!Utils.uploadModules(verbose, info, verifier, zipModules, removedFiles)) {
                    return false;
                }
                layers.put(info.uri, info);
            }
            
            topLayers = Utils.topLayers(layers.values());
            
            return true;
        } catch (RadixSvnException ex) {
            verifier.error(new RadixError("Unable to obtain layer list", ex));
            return false;
        }
    }

    boolean isClassMayBeInDevelopmentLayer(String className) {
        String dotSeparatedClassName = className.replace("/", ".");

        for (String layerUri : developmentLayers) {
            String layerPackageNamePrefix = layerUri.replace("-", "_") + ".ads.mdl";
            if (dotSeparatedClassName.startsWith(layerPackageNamePrefix)) {
                return true;
            }
        }
        return false;
    }

    public void finish() {
        for (LayerInfo info : layers.values()) {
            for (ERuntimeEnvironmentType env : ERuntimeEnvironmentType.values()) {
                info.getLib(env).cleanup();
            }
        }
        removeAdsJarFiles(this);
    }

    private static void removeAdsJarFiles(BranchHolder holder) {
        for (org.radixware.kernel.common.svn.utils.Utils.LayerInfo info : holder.layers.values()) {
            for (org.radixware.kernel.common.svn.utils.Utils.ModuleInfo module : info.modules.values()) {
                module.close();
            }
        }
    }
    
    void preloadLayersBinares(final ERuntimeEnvironmentType env, int nThreads) {
        final ExecutorService exec = Executors.newFixedThreadPool(nThreads);
        for (LayerInfo layer : layers.values()) {
            layer.getLib(env).upload(exec);
            if (env != ERuntimeEnvironmentType.COMMON) {
                layer.getLib(ERuntimeEnvironmentType.COMMON).upload(exec);
            }
            for (final Utils.ModuleInfo adsModule : layer.modules.values()) {
                exec.submit(new Runnable() {
                    @Override
                    public void run() {
                        adsModule.findBinaryFile(env);
                        if (env != ERuntimeEnvironmentType.COMMON) {
                            adsModule.findBinaryFile(ERuntimeEnvironmentType.COMMON);
                        }
                    }
                });
            }
        }
        try {
            exec.shutdown();
            exec.awaitTermination(5, TimeUnit.MINUTES);
        } catch (InterruptedException ex) {
            final String mes = "Branch binaries preload was interrupted";
            if (verifier != null) {
                verifier.error(mes);
            }
            Logger.getLogger(BranchHolder.class.getName()).log(Level.SEVERE, mes, ex);
        }
    }
    
    List<LayerInfo> getTopLayers() {
        return topLayers;
    }

}
