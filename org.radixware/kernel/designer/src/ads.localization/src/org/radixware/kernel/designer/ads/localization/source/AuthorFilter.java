package org.radixware.kernel.designer.ads.localization.source;

public class AuthorFilter extends LanguagesSettings {

    String name = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEmpty() {
        return name == null || name.isEmpty();
    }
}
