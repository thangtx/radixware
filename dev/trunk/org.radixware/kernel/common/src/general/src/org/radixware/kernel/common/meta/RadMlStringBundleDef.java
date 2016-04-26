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
package org.radixware.kernel.common.meta;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.ELocalizedStringKind;
import org.radixware.kernel.common.environment.IMlStringBundle;
import org.radixware.kernel.common.environment.IRadixClassLoader;
import org.radixware.kernel.common.environment.IRadixEnvironment;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.schemas.adsdef.AdsDefinitionDocument;
import org.radixware.schemas.adsdef.AdsDefinitionElementType;
import org.radixware.schemas.adsdef.LocalizedString;
import org.radixware.schemas.adsdef.LocalizingBundleDefinition;

public class RadMlStringBundleDef extends RadDefinition implements IMlStringBundle {

    public static class MultilingualString implements IMlStringBundle.IMultilingualString {

        private final ELocalizedStringKind kind;
        private final EEventSeverity eventSeverity;
        private final String eventSource;
        private static final EIsoLanguage DEFAULT_LANG = EIsoLanguage.ENGLISH;
        private final HashMap<EIsoLanguage, String> stringsByLang = new HashMap<>(11);
        private final Set<EIsoLanguage> uncheckedLanguages;

        public String get(EIsoLanguage lang) {
            String res = stringsByLang.get(lang);
            if (res != null) {
                return res;
            }
            return "";
        }

        @Override
        public String toString() {
            String res = get(DEFAULT_LANG);
            return res == null ? ""/*|| res.equals("") ?  getInAnotherLanguages() */ : res;
        }

        public MultilingualString(final Map<EIsoLanguage, String> content, final ELocalizedStringKind kind, final EEventSeverity eventSeverity, final String eventSource, final String moduleDirName) {
            this(content, kind, eventSeverity, eventSource, null, moduleDirName);
        }

        public MultilingualString(final Map<EIsoLanguage, String> content, final ELocalizedStringKind kind, final EEventSeverity eventSeverity, final String eventSource, final Set<EIsoLanguage> uncheckedLangs, final String moduleDirName) {
            stringsByLang.putAll(content);
            this.eventSeverity = eventSeverity;
            this.eventSource = eventSource;
            this.kind = kind;
            this.uncheckedLanguages = uncheckedLangs;
        }

        @Deprecated
        public MultilingualString(final Map<EIsoLanguage, String> content, final ELocalizedStringKind kind, final EEventSeverity eventSeverity, final String eventSource, final Set<EIsoLanguage> uncheckedLangs) {
            this(content, kind, eventSeverity, eventSource, uncheckedLangs, null);
        }

        public MultilingualString(final String value, final EIsoLanguage lang, final ELocalizedStringKind kind, final EEventSeverity eventSeverity, final String eventSource, final Set<EIsoLanguage> uncheckedLangs, final String moduleDirName) {
            stringsByLang.put(lang, value);
            this.eventSeverity = eventSeverity;
            this.eventSource = eventSource;
            this.kind = kind;
            this.uncheckedLanguages = uncheckedLangs;
        }

        public MultilingualString(final String russian, final String english, final EEventSeverity eventSeverity, final String eventSource, final String moduleDirName) {
            this(russian, english, eventSeverity, eventSource, null, moduleDirName);
        }

        public MultilingualString(final String russian, final String english, final EEventSeverity eventSeverity, final String eventSource, final Set<EIsoLanguage> uncheckedLangs, final String moduleDirName) {
            stringsByLang.put(EIsoLanguage.ENGLISH, english);
            stringsByLang.put(EIsoLanguage.RUSSIAN, russian);
            this.eventSeverity = eventSeverity;
            this.eventSource = eventSource;
            this.kind = null;
            this.uncheckedLanguages = uncheckedLangs;
        }

        /**
         * @return the kind
         */
        @Override
        public ELocalizedStringKind getKind() {
            return kind;
        }

        /**
         * @return the eventSeverity
         */
        @Override
        public EEventSeverity getEventSeverity() {
            return eventSeverity;
        }

        /**
         * @return the eventSource
         */
        @Override
        public String getEventSource() {
            return eventSource;
        }

        @Override
        public String getValue(EIsoLanguage language) {
            return stringsByLang.get(language);
        }

        public void addValue(String value, EIsoLanguage language) {
            stringsByLang.put(language, value);
        }

        @Override
        public boolean isChecked(EIsoLanguage language) {
            return uncheckedLanguages == null || !uncheckedLanguages.contains(language);
        }
    }
    private final Map<Id, RadMlStringBundleDef.MultilingualString> stringsById = new HashMap<>(11);
    private final IRadixEnvironment predefinedEnv;
    private final Class context;

    public RadMlStringBundleDef(final Class context, final Id id, final String name, final Map<Id, RadMlStringBundleDef.MultilingualString> stringsById) {
        super(id, name);
        this.context = context;
        this.predefinedEnv = ((IRadixClassLoader) context.getClassLoader()).getEnvironment();
        this.stringsById.putAll(stringsById);
    }

    private IRadixEnvironment getEnv() {
        if (predefinedEnv != null) {
            return predefinedEnv;
        }
        if (Thread.currentThread() instanceof IRadixEnvironment) {
            return (IRadixEnvironment) Thread.currentThread();
        }
        return null;
    }

    public String get(final Id stringId) {
        RadMlStringBundleDef.MultilingualString s = stringsById.get(stringId);
        if (s == null) {
            getEnv().getTrace().put(EEventSeverity.DEBUG, "Localized string not found " + getId().toString() + ":"
                    + stringId.toString(), EEventSource.DEF_MANAGER);
            return "";
        } else {
            EIsoLanguage lang = getClientLanguage();
            String res = s.get(lang);
            if (res == null || res.equals("")) {
                if (!loadedLangs.contains(lang)) {
                    loadStrings(lang);
                    res = s.get(lang);

                }
                res = res == null || res.equals("") ? getInAnotherLanguages(lang, s) : res;
            }
            return res;
        }
    }

    private EIsoLanguage getClientLanguage() {
        final Thread ct = Thread.currentThread();
        if (ct instanceof ILanguageContext) {
            return ((ILanguageContext) ct).getLanguage();
        } else {
            return getEnv().getClientLanguage();
        }
    }
    private Set<EIsoLanguage> loadedLangs = new HashSet<>();

    @Override
    public String get(final Id stringId, final EIsoLanguage lang) {
        final RadMlStringBundleDef.MultilingualString s = stringsById.get(stringId);
        if (s == null) {
            getEnv().getTrace().put(EEventSeverity.DEBUG, "Localized string not found " + getId().toString() + ":"
                    + stringId.toString(), EEventSource.DEF_MANAGER);
            return "";
        } else {
            String res = s.get(lang);
            if (res == null || res.equals("")) {
                if (!loadedLangs.contains(lang)) {
                    loadStrings(lang);
                    res = s.get(lang);
                }
                res = res == null || res.equals("") ? getInAnotherLanguages(lang, s) : res;
            }
            return res;
        }
    }

    private String getXmlResourcePath() {
        return this.context.getName().substring(0, this.context.getName().length() - 3).replace('.', '/') + ".xml";
    }

    private Enumeration<URL> getXmlResources() throws IOException {
        return this.context.getClassLoader().getResources(getXmlResourcePath());
    }

    private String getInAnotherLanguages(EIsoLanguage srcLang, MultilingualString mlString) {
        String res;
        List<EIsoLanguage> anotherLangs = getAnotherLanguages();
        boolean look = false;
        for (EIsoLanguage l : anotherLangs) {
            if (l == srcLang) {
                look = true;
                continue;
            }
            if (!look) {
                continue;
            }
            res = mlString.get(l);
            if (res != null && !"".equals(res)) {
                return res;
            }
            if (!loadedLangs.contains(l)) {
                if ((res == null || "".equals(res))) {
                    loadStrings(l);
                    res = mlString.get(l);
                }
                if (res != null && !"".equals(res)) {
                    return res;
                }
            }
        }
        return "";
    }

    private List<EIsoLanguage> getAnotherLanguages() {
        final IRadixEnvironment env = getEnv();
        if (env != null) {
            List<EIsoLanguage> langs = env.getLanguages();
            if (langs != null) {
                return Collections.unmodifiableList(langs);
            }
        }
        return Collections.emptyList();
    }

    private void loadStrings(EIsoLanguage lang) {
        try {
            Enumeration<URL> allResourcePathes = getXmlResources();
            while (allResourcePathes.hasMoreElements()) {
                URL resource = allResourcePathes.nextElement();
                InputStream in = null;
                try {
                    in = resource.openStream();
                    try {
                        AdsDefinitionDocument xDoc = AdsDefinitionDocument.Factory.parse(in);
                        AdsDefinitionElementType xDef = xDoc.getAdsDefinition();
                        if (xDef != null) {
                            LocalizingBundleDefinition xBundle = xDef.getAdsLocalizingBundleDefinition();
                            if (xBundle != null && xBundle.getStringList() != null) {
                                for (LocalizedString str : xBundle.getStringList()) {
                                    for (LocalizedString.Value value : str.getValueList()) {
                                        if (value.getLanguage().equals(lang)) {
                                            String translatedValue = value.getStringValue();
                                            if (translatedValue != null && !translatedValue.isEmpty()) {
                                                MultilingualString mlString = stringsById.get(str.getId());
                                                if (mlString != null) {
                                                    String current = mlString.get(lang);
                                                    if (current == null || current.isEmpty()) {
                                                        mlString.addValue(translatedValue, lang);
                                                    }
                                                }
                                            }
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    } catch (XmlException ex) {
                        getEnv().getTrace().put(EEventSeverity.DEBUG, "Unable to obtain localization resources for string bundle " + getId().toString() + "from URL " + resource.toString() + ". Wrong xml file format:"
                                + ExceptionTextFormatter.exceptionStackToString(ex), EEventSource.DEF_MANAGER);
                    }
                } catch (IOException e) {
                    getEnv().getTrace().put(EEventSeverity.DEBUG, "Unable to obtain localization resources for string bundle " + getId().toString() + "from URL " + resource.toString() + ":"
                            + ExceptionTextFormatter.exceptionStackToString(e), EEventSource.DEF_MANAGER);
                } finally {
                    if (in != null) {
                        in.close();
                    }
                }
            }
        } catch (IOException e) {
            getEnv().getTrace().put(EEventSeverity.DEBUG, "Unable to obtain localization resources for string bundle " + getId().toString() + ":"
                    + ExceptionTextFormatter.exceptionStackToString(e), EEventSource.DEF_MANAGER);
            loadedLangs.add(lang);
        }
    }

    @Override
    public IMlStringBundle.IMultilingualString getStringSet(final Id stringId) {
        MultilingualString s = stringsById.get(stringId);
        return s;
    }

    public EEventSeverity getEventSeverity(final Id stringId) {
        final RadMlStringBundleDef.MultilingualString s = stringsById.get(stringId);
        if (s == null) {
            return EEventSeverity.ERROR;
        }
        return s.getEventSeverity();
    }

    public String getEventSource(final Id stringId) {
        final RadMlStringBundleDef.MultilingualString s = stringsById.get(stringId);
        if (s == null) {
            return EEventSource.APP.getValue();
        }
        return s.getEventSource();
    }

    public List<String> getEventCodeList() {
        return getEventCodeList(null);
    }

    public List<String> getEventCodeList(final String eventSource) {
        final List<String> codes = new LinkedList<>();
        for (Map.Entry<Id, RadMlStringBundleDef.MultilingualString> e : stringsById.entrySet()) {
            if (e.getValue().getKind() == ELocalizedStringKind.EVENT_CODE
                    && (eventSource == null || eventSource.isEmpty()
                    || eventSource.equals(e.getValue().getEventSource())
                    || e.getValue().getEventSource() != null && e.getValue().getEventSource().startsWith(eventSource + "."))) {
                codes.add(getId() + "-" + e.getKey());
            }
        }
        return Collections.unmodifiableList(codes);
    }

    public final Set<Id> getEventCodesIds() {
        final Set<Id> set = new HashSet<>();
        for (Map.Entry<Id, RadMlStringBundleDef.MultilingualString> e : stringsById.entrySet()) {
            if (e.getValue().getKind() == ELocalizedStringKind.EVENT_CODE) {
                set.add(e.getKey());
            }
        }
        return Collections.unmodifiableSet(set);
    }
}
