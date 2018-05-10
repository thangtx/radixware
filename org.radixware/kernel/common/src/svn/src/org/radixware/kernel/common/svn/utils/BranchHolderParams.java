/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.radixware.kernel.common.svn.utils;

import java.util.List;
import java.util.zip.ZipFile;
import org.radixware.kernel.common.svn.SVNRepositoryAdapter;

/**
 *
 * @author npopov
 */
public final class BranchHolderParams {
    
    private final SVNRepositoryAdapter repository;
    private final String branchPath;
    private final List<String> layersPathList;
    private final ZipFile upgradeFile;
    private final String upgradeFilePath;
    private final boolean skipDevelopmentLayers;
    private final String predefinedBaseDevLayerURI;

    public BranchHolderParams(SVNRepositoryAdapter repository, String branchPath, List<String> layersPathList, ZipFile upgradeFile, String upgradeFilePath, boolean skipDevelopmentLayers, String predefinedBaseDevLayerURI) {
        this.repository = repository;
        this.branchPath = branchPath;
        this.layersPathList = layersPathList;
        this.upgradeFile = upgradeFile;
        this.upgradeFilePath = upgradeFilePath;
        this.skipDevelopmentLayers = skipDevelopmentLayers;
        this.predefinedBaseDevLayerURI = predefinedBaseDevLayerURI;
    }
    
    public BranchHolderParams(SVNRepositoryAdapter repository, String branchPath, ZipFile upgradeFile, boolean skipDevelopmentLayers) {
        this(repository, branchPath, null, upgradeFile, null, skipDevelopmentLayers, Utils.NO_BASE_DEVELOPMENT_LAYER_SPECIFIED);
    }
        
    public BranchHolderParams(SVNRepositoryAdapter repository, String branchPath, String upgradeFilePath, boolean skipDevelopmentLayers, String predefinedBaseDevLayerURI) {
        this(repository, branchPath, null, null, upgradeFilePath, skipDevelopmentLayers, predefinedBaseDevLayerURI);
    }
    
    public BranchHolderParams(SVNRepositoryAdapter repository, List<String> layersPathList, ZipFile upgradeFile, String upgradeFilePath, boolean skipDevelopmentLayers, String predefinedBaseDevLayerURI) {
        this(repository, null, layersPathList, upgradeFile, upgradeFilePath, skipDevelopmentLayers, predefinedBaseDevLayerURI);
    }

    public SVNRepositoryAdapter getRepository() {
        return repository;
    }
    
    public String getBranchPath() {
        return branchPath;
    }
    
    public List<String> getLayersPathList() {
        return layersPathList;
    }

    public ZipFile getUpgradeFile() {
        return upgradeFile;
    }

    public String getUpgradeFilePath() {
        return upgradeFilePath;
    }
    
    public boolean isSkipDevelopmentLayers() {
        return skipDevelopmentLayers;
    }

    public String getPredefinedBaseDevLayerURI() {
        return predefinedBaseDevLayerURI;
    }
    
}
