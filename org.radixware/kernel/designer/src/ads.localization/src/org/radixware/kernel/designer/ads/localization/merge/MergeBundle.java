package org.radixware.kernel.designer.ads.localization.merge;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.defs.localization.ILocalizingBundleDef;
import org.radixware.schemas.ddsdef.ModelDocument;

public class MergeBundle {

    private final ILocalizingBundleDef srcBundle;
    ILocalizingBundleDef destBundle;
    LinkedList<MergeFile> bundleFiles = new LinkedList<>();
    private ModelDocument model;
    private File ddsModuleDir;
    private final List<File> localizingDirs = new ArrayList<>();

    public MergeBundle(ILocalizingBundleDef oldBundle) {
        this.srcBundle = oldBundle;
    }

    public ILocalizingBundleDef getDestBundle() {
        return destBundle;
    }

    public void setDestBundle(ILocalizingBundleDef newBundle) {
        this.destBundle = newBundle;
    }

    public void addBundleFile(MergeFile file) {
        bundleFiles.add(file);
    }

    public ILocalizingBundleDef getOldBundle() {
        return srcBundle;
    }

    public List<MergeFile> getBundleFiles() {
        return bundleFiles;
    }

    public boolean isEmpty() {
        return bundleFiles.isEmpty();
    }

    public ModelDocument getModel() {
        return model;
    }

    public void setModel(ModelDocument model) {
        this.model = model;
    }

    public File getDdsModuleDir() {
        return ddsModuleDir;
    }

    public void setDdsModuleDir(File ddsModuleDir) {
        this.ddsModuleDir = ddsModuleDir;
    }

    public List<File> getLocalizingDirs() {
        return localizingDirs;
    }

    public void addLocalizingDir(File localizingDir) {
        this.localizingDirs.add(localizingDir);
    }
    
    
}
