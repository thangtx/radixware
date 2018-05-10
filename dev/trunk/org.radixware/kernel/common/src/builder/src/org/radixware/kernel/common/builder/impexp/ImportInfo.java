
package org.radixware.kernel.common.builder.impexp;

import java.util.Date;
import java.util.List;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.repository.Layer;

public class ImportInfo {
    
    private boolean isSetChecked;
    private boolean isSetAgreed;
    
    private Layer layer;
    private List<EIsoLanguage> languages;
    private String exportAuthor;
    private Date exportDate;

    public Layer getLayer() {
        return layer;
    }

    public void setLayer(Layer layer) {
        if (layer != null) {
            this.layer = layer;
        }
    }

    public List<EIsoLanguage> getLanguages() {
        return languages;
    }

    public void setLanguages(List<EIsoLanguage> languages) {
        this.languages = languages;
    }

    public String getExportAuthor() {
        return exportAuthor;
    }

    public void setExportAuthor(String exportAuthor) {
        this.exportAuthor = exportAuthor;
    }

    public Date getExportDate() {
        return exportDate;
    }

    public void setExportDate(Date exportDate) {
        this.exportDate = exportDate;
    }

    public boolean isSetChecked() {
        return isSetChecked;
    }

    public void setIsSetChecked(boolean isSetChecked) {
        this.isSetChecked = isSetChecked;
    }

    public boolean isSetAgreed() {
        return isSetAgreed;
    }

    public void setIsSetAgreed(boolean isSetAgreed) {
        this.isSetAgreed = isSetAgreed;
    }
}
