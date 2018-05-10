
package org.radixware.kernel.designer.api.filters;

import org.radixware.kernel.common.enums.EIsoLanguage;


public class DescriptionMode {
    public static final DescriptionMode SHOW_ALL = new DescriptionMode("Show all", null);
    public static final DescriptionMode LEGACY_DESCRIPTION = new DescriptionMode("With legacy (non-localized) descriptions", null);
    public static final String TRANSLATED_START = "With ";
    public static final String TRANSLATED_END = " descriptions";
    private final String value;
    private final EIsoLanguage language;

    public DescriptionMode(String value, EIsoLanguage language) {
        this.value = value;
        this.language = language;
    }

    @Override
    public String toString() {
        return value;
    }

    public EIsoLanguage getLanguage() {
        return language;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DescriptionMode){
            DescriptionMode mode = (DescriptionMode) obj;
            if (mode.language != language){
                return false;
            }
            if (mode.value == null ? value != null : !mode.value.equals(value)){
                return false;
            }
            
            return true;
        }
        return false;
    }

}
