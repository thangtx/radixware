package org.radixware.kernel.designer.ads.localization.merge;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;

public class LocalizingStringOptions {
    private final Branch branch;
    private File fromBranchFile;
    private File toBranchFile;
    
    private String baseDevelopmentLayerUri;
    private Date dateFrom;
    private Date dateTo;
    private Map<Layer, List<Module>> layers;
    private String lastModifiedAuthor;

    public LocalizingStringOptions(Branch branch) {
        this.branch = branch;
        fromBranchFile = branch.getFile().getParentFile();
    }

    public File getFromBranchFile() {
        return fromBranchFile;
    }

    public File getToBranchFile() {
        return toBranchFile;
    }

    public void setToBranchFile(File toBranchFile) {
        this.toBranchFile = toBranchFile;
    }

    public String getBaseDevelopmentLayerUri() {
        return baseDevelopmentLayerUri;
    }

    public void setBaseDevelopmentLayerUri(String baseDevelopmentLayerUri) {
        this.baseDevelopmentLayerUri = baseDevelopmentLayerUri;
    }

    public long getTimeFrom() {
        if (dateFrom != null) {
            return dateFrom.getTime();
        } else {
            return -1;
        }
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public long getTimeTo() {
        if (dateTo != null) {
            return dateTo.getTime();
        } else {
            return -1;
        }
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public String getLastModifiedAuthor() {
        return lastModifiedAuthor;
    }

    public void setLastModifiedAuthor(String lastModifiedAuthor) {
        this.lastModifiedAuthor = lastModifiedAuthor;
    }

    public void setLayers(Map<Layer, List<Module>> layers, boolean showDdsSegment) {
        this.layers = new HashMap<>();
        for (Layer layer : layers.keySet()){
            List<Module> modules = layers.get(layer);
            List<Module> modulesList;
            if (modules == null) {
                modulesList = new ArrayList<>(layer.getAds().getModules().list());
                if (showDdsSegment){
                    modulesList.addAll(layer.getDds().getModules().list());
                }
            } else {
                modulesList = new ArrayList<>(modules);
            }
            this.layers.put(layer, modulesList);
        }
    }

    public Map<Layer, List<Module>> getLayers() {
        return layers;
    }
    
    public Branch getBranch() {
        return branch;
    }
    
}
