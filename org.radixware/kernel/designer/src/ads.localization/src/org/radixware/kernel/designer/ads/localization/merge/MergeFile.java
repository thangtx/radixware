package org.radixware.kernel.designer.ads.localization.merge;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.schemas.adsdef.LocalizedString;

public class MergeFile {

    private final File file;
    private final Set<EIsoLanguage> languages;
    private List<LocalizedString> values;
    Map<IMultilingualStringDef, MergeString> mergedStrings = new HashMap<>();
    private boolean isMerged = false;
    private final boolean isLocalizing;

    
    public MergeFile(File file, Set<EIsoLanguage> languages, boolean isLocalizing) {
        this.file = file;
        this.languages = languages;
        this.isLocalizing = isLocalizing;
    }

    public boolean isLocalizing() {
        return isLocalizing;
    }
    
    public File getFile() {
        return file;
    }

    public void addSaveString(IMultilingualStringDef oldString, LocalizedString newString) {
        MergeString string = findMergeString(oldString);

        if (string == null) {
            MergeString mergeString = new MergeString(oldString, newString, languages);
            mergedStrings.put(oldString, mergeString);
        }
    }
    
     public void removeSaveString(IMultilingualStringDef oldString) {
        mergedStrings.remove(oldString);
    }
    
    public boolean isStringModified(IMultilingualStringDef oldString){
        MergeString string = findMergeString(oldString);
        if (string != null) {
            return string.isModified();
        }
        return false;
    }

    public boolean isEmpty() {
        return mergedStrings.isEmpty();
    }

    public MergeString findMergeString(IMultilingualStringDef oldString) {
        return mergedStrings.get(oldString);
    }

    public boolean mergeStrings() throws IOException {
        for (MergeString string : mergedStrings.values()) {
            if (string.mergeString()) {
                isMerged = true;
            }
        }

        return isMerged;
    }

    public boolean isMerged() {
        return isMerged;
    }

    public void setValues(List<LocalizedString> values) {
        this.values = values;
    }

    public List<LocalizedString> getValues() {
        return values;
    }

    public Set<IMultilingualStringDef> getMergeStrings() {
        return mergedStrings.keySet();
    }

    public Set<EIsoLanguage> getLanguages() {
        return languages;
    }
    
    
}
