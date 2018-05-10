package org.radixware.kernel.designer.ads.localization.merge;

import java.sql.Timestamp;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.schemas.adsdef.LocalizedString;

public class MergeString {

    final IMultilingualStringDef oldString;
    final LocalizedString newXmlString;
    final Set<EIsoLanguage> languages;
    boolean needMerge = true;

    public MergeString(IMultilingualStringDef oldString, LocalizedString newString, Set<EIsoLanguage> language) {
        this.oldString = oldString;
        this.newXmlString = newString;
        this.languages = language;
    }

    public LocalizedString getNewXmlString() {
        return newXmlString;
    }

    public IMultilingualStringDef getOldString() {
        return oldString;
    }
    
    public boolean isModified() {
        oldString.getValues(ExtendableDefinitions.EScope.LOCAL);
        List<LocalizedString.Value> values = newXmlString.getValueList();
        for (EIsoLanguage lang : languages) {
            if (oldString.getLanguages().contains(lang)) {
                boolean isEmpty = true;
                for (LocalizedString.Value xValue : values) {
                    if (lang == null) {
                        lang = xValue.getLanguage();
                    }
                    if (xValue.getLanguage() != lang) {
                        continue;
                    }
                    isEmpty = false;
                    String newValue = oldString.getValue(lang);
                    if (newValue != null && !newValue.equals(xValue.getStringValue())) {
                        return true;
                    }

                    if (xValue.getNeedsCheck() != oldString.getNeedsCheck(lang)) {
                        return true;
                    }

                    newValue = oldString.getAuthorChangeOfStatus(lang);
                    if (newValue != null && !newValue.equals(xValue.getAuthorChangeOfStatus())) {
                        return true;
                    }

                    Timestamp newDate = oldString.getTimeChangeOfStatus(lang);
                    if (newDate != null && !newDate.equals(xValue.getDateChangeOfStatus())) {
                        return true;
                    }

                    newDate = oldString.getLastChangeTime(lang);
                    if (newDate != null && !newDate.equals(xValue.getLastModified())) {
                        return true;
                    }

                    newValue = oldString.getLastChangeAuthor(lang);
                    if (newValue != null && !newValue.equals(xValue.getAuthor())) {
                        return true;
                    }

                    if (xValue.getAgreed() != oldString.isAgreed(lang)) {
                        return true;
                    }

                    newValue = oldString.getAuthorChangeAgreedString(lang);
                    if (newValue != null && !newValue.equals(xValue.getAuthorChangeAgreedString())) {
                        return true;
                    }

                    newDate = oldString.getTimeChangeAgreedString(lang);
                    if (newDate != null && !newDate.equals(xValue.getDateChangeAgreedString())) {
                        return true;
                    }
                    long version = oldString.getVersion();
                    if (version != xValue.getVersion()) {
                        return true;
                    }
                }
                if (isEmpty) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean mergeString() {

        if (!needMerge) {
            return false;
        }
        
        oldString.getValues(ExtendableDefinitions.EScope.LOCAL);
        List<LocalizedString.Value> values = newXmlString.getValueList();
        boolean result = false;
        for (EIsoLanguage lang : languages) {
            if (oldString.getLanguages().contains(lang)) {
                boolean isEmpty = true;
                for (LocalizedString.Value xValue : values) {
                    if (xValue.getLanguage() != lang) {
                        continue;
                    }
                    isEmpty = false;
                    if (mergeValue(xValue, lang)) {
                        result = true;
                    }
                }
                if (isEmpty) {
                    LocalizedString.Value value = newXmlString.addNewValue();
                    value.setLanguage(lang);
                    if (mergeValue(value, lang)) {
                        result = true;
                    }
                }
            }
        }
        return result;
    }
    
    private boolean mergeValue(LocalizedString.Value xValue, EIsoLanguage language) {
        boolean isMerged = false;
        String newValue = oldString.getValue(language);
        if (newValue != null && !newValue.equals(xValue.getStringValue())) {
            xValue.setStringValue(newValue);
            isMerged = true;
        }

        if (xValue.getNeedsCheck() != oldString.getNeedsCheck(language)) {
            xValue.setNeedsCheck(oldString.getNeedsCheck(language));
            isMerged = true;
        }

        newValue = oldString.getAuthorChangeOfStatus(language);
        if (newValue != null && !newValue.equals(xValue.getAuthorChangeOfStatus())) {
            xValue.setAuthorChangeOfStatus(newValue);
            isMerged = true;
        }

        Timestamp newDate = oldString.getTimeChangeOfStatus(language);
        if (newDate != null && !newDate.equals(xValue.getDateChangeOfStatus())) {
            xValue.setDateChangeOfStatus(newDate);
            isMerged = true;
        }

        newDate = oldString.getLastChangeTime(language);
        if (newDate != null && !newDate.equals(xValue.getLastModified())) {
            xValue.setLastModified(newDate);
            isMerged = true;
        }

        newValue = oldString.getLastChangeAuthor(language);
        if (newValue != null && !newValue.equals(xValue.getAuthor())) {
            xValue.setAuthor(newValue);
            isMerged = true;
        }

        if (xValue.getAgreed() != oldString.isAgreed(language)) {
            xValue.setAgreed(oldString.isAgreed(language));
            isMerged = true;
        }

        newValue = oldString.getAuthorChangeAgreedString(language);
        if (newValue != null && !newValue.equals(xValue.getAuthorChangeAgreedString())) {
            xValue.setAuthorChangeAgreedString(newValue);
            isMerged = true;
        }

        newDate = oldString.getTimeChangeAgreedString(language);
        if (newDate != null && !newDate.equals(xValue.getDateChangeAgreedString())) {
            xValue.setDateChangeAgreedString(newDate);
            isMerged = true;
        }
        
        long version = oldString.getVersion();
        if (version != xValue.getVersion()) {
            xValue.setVersion(version);
            isMerged = true;
        }

        return isMerged;
    }

    public boolean isNeedMerge() {
        return needMerge;
    }

    public void setNeedMerge(boolean needMerge) {
        this.needMerge = needMerge;
    }

    @Override
    public String toString() {
        return super.toString(); //To change body of generated methods, choose Tools | Templates.
    }

    public Set<EIsoLanguage> getLanguages() {
        try {
            return EnumSet.copyOf(languages);
        } catch (IllegalArgumentException e) {
            return EnumSet.noneOf(EIsoLanguage.class);
        }
    }
}
