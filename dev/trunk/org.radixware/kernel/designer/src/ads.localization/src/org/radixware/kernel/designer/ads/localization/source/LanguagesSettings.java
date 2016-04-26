
package org.radixware.kernel.designer.ads.localization.source;

import java.util.List;
import org.radixware.kernel.common.enums.EIsoLanguage;


public class LanguagesSettings {

    private boolean isLanguageSelected = false;
    private List<EIsoLanguage> languages;
    private FilterSettings.GroupLangsEnum groupLangs;

    public void setLanguageSelected(final boolean isLangSelected) {
        this.isLanguageSelected = isLangSelected;
    }

    public void setLanguage(final List<EIsoLanguage> languages) {
        this.languages = languages;
    }

    public void setGrupLangs(final FilterSettings.GroupLangsEnum grupLangs) {
        this.groupLangs = grupLangs;
    }

    public List<EIsoLanguage> getLanguages() {
        return languages;
    }

    public FilterSettings.GroupLangsEnum getGroupLangType() {
        return groupLangs;
    }

    public boolean isLanguageSelected() {
        return isLanguageSelected;
    }
}
