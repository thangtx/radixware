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
package org.radixware.kernel.designer.common.dialogs.scmlnb.finder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsTitledDefinition;
import org.radixware.kernel.common.defs.ads.ITitledDefinition;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.ServerPresentationSupport;
import org.radixware.kernel.common.defs.ads.clazz.presentation.EntityPresentations;
import org.radixware.kernel.common.defs.ads.clazz.presentation.IAdsPresentableProperty;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumItemDef;
import org.radixware.kernel.common.defs.ads.ui.AdsUIItemDef;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.UiProperties;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.designer.common.general.editors.EditorsManager;

public class TitleFinder implements IFinder {

    private final Pattern pattern;

    public TitleFinder(final Pattern pattern) {
        this.pattern = pattern;
    }

    @Override
    public boolean accept(RadixObject radixObject) {
        return radixObject instanceof AdsTitledDefinition || radixObject instanceof IAdsPresentableProperty || radixObject instanceof AdsUIItemDef;
    }

    @Override
    public List<IOccurrence> list(final RadixObject radixObject) {
        if (radixObject == null || !accept(radixObject)) {
            return Collections.emptyList();
        }

        List<IOccurrence> occurrences = new ArrayList<IOccurrence>();
        List<EIsoLanguage> languages = Collections.emptyList();
        final Layer layer = radixObject.getLayer();
        if (layer != null) {
            languages = layer.getLanguages();
        }

        if (!(radixObject instanceof AdsTitledDefinition || radixObject instanceof IAdsPresentableProperty)) {
            UiProperties properties = ((AdsUIItemDef) radixObject).getProperties();
            for (int i = 0; i < properties.size(); i++) {
                if ((properties.get(i).getName().equals("windowTitle") || properties.get(i).getName().equals("toolTip")) || properties.get(i).getName().equals("text") && properties.get(i) instanceof AdsUIProperty.LocalizedStringRefProperty) {
                    AdsUIProperty.LocalizedStringRefProperty mlProp = ((AdsUIProperty.LocalizedStringRefProperty) properties.get(i));
                    if (mlProp.findLocalizedString() != null) {
                        for (EIsoLanguage lang : languages) {
                            String str = mlProp.findLocalizedString().getValue(lang);
                            if (str != null && pattern.matcher(str).find()) {
                                occurrences.add(new TitleWidgetOccurrence(radixObject, lang, pattern, str, (properties.get(i).getName())));
                            }
                        } 
                    }
                }
            }
            return occurrences;
        } else {
            ITitledDefinition titledDef = null;

            if (radixObject instanceof AdsTitledDefinition) {
                titledDef = (AdsTitledDefinition) radixObject;
            } else if (radixObject instanceof IAdsPresentableProperty) {
                final ServerPresentationSupport sps = ((IAdsPresentableProperty) radixObject).getPresentationSupport();
                if (sps != null) {
                    titledDef = sps.getPresentation();
                }
            }

            if (titledDef != null) {
                for (EIsoLanguage language : languages) {
                    final String title = titledDef.getTitle(language);
                    if (title != null && pattern.matcher(title).find()) {
                        occurrences.add(new TitleOccurrence(radixObject, titledDef, language, pattern));
                    }
                }
            }

            if (radixObject instanceof AdsEnumItemDef) {
                if (pattern.matcher(radixObject.getName()).matches()) {
                    occurrences.add(new TitleOccurrence(radixObject, null, null, pattern));
                }
            }
            
            if (radixObject instanceof AdsEntityClassDef) {
                AdsEntityClassDef entityCl = (AdsEntityClassDef) radixObject;
                EntityPresentations pres = entityCl.getPresentations();
                if (pres != null) {
                    for (EIsoLanguage language : languages) {
                        final String title = pres.getEntityTitle(language);
                        if (title != null && pattern.matcher(title).find()) {
                            occurrences.add(new TitleForPluralOccurrence(pres, entityCl, language, pattern));
                        }
                    }
                }
            }
            return occurrences;
        }
    }
    
    private static class TitleWidgetOccurrence implements IOccurrence {
        
        private final RadixObject ownerDef;
        private final EIsoLanguage language;
        private final Pattern pattern;
        private final String titleStr;
        private final String propertiesName;
        
        public TitleWidgetOccurrence(RadixObject ownerDef, EIsoLanguage language, Pattern pattern, String titleStr, String propertiesName) {
            this.ownerDef = ownerDef;
            this.language = language;
            this.pattern = pattern;
            this.titleStr = titleStr;
            this.propertiesName = propertiesName;
        } 

        @Override
        public String getDisplayText() {
            return FindUtils.html("Title for " + propertiesName + " \"" + language.getName() + "\": " + FindUtils.markPatternBold(titleStr, pattern));
        }

        @Override
        public RadixObject getOwnerObject() {
            return ownerDef;
        }

        @Override
        public void goToObject() {
            EditorsManager.getDefault().open(ownerDef);
        }
    }
    
    private static class TitleOccurrence implements IOccurrence {

        private final RadixObject ownerDef;
        protected final ITitledDefinition titledDef;
        protected final EIsoLanguage language;
        private final Pattern pattern;

        public TitleOccurrence(RadixObject ownerDef, ITitledDefinition titledDef, EIsoLanguage language, Pattern pattern) {
            this.ownerDef = ownerDef;
            this.titledDef = titledDef;
            this.pattern = pattern;
            this.language = language;
        }

        @Override
        public String getDisplayText() {
            if (language != null && titledDef != null) {
                return FindUtils.html("Title for \"" + language.getName() + "\": " + FindUtils.markPatternBold(getTitle(), pattern));
            } else {
                if (ownerDef instanceof AdsEnumItemDef) {
                    return FindUtils.html("Item name: " + FindUtils.markPatternBold(ownerDef.getName(), pattern));
                }
            }
            return "???";
        }
        
        protected String getTitle() {
            return titledDef.getTitle(language);
        }

        @Override
        public RadixObject getOwnerObject() {
            return ownerDef;
        }

        @Override
        public void goToObject() {
            EditorsManager.getDefault().open(getOwnerObject());
        }
    }
    
    private static class TitleForPluralOccurrence extends TitleOccurrence{

        public TitleForPluralOccurrence(EntityPresentations ownerDef, ITitledDefinition titledDef, EIsoLanguage language, Pattern pattern) {
            super(ownerDef, titledDef, language, pattern);
        }
        
        public EntityPresentations getPresentation() {
            return (EntityPresentations) super.getOwnerObject();
        }
        
        @Override
        public RadixObject getOwnerObject() {
            return (RadixObject) titledDef;
        }

        @Override
        protected String getTitle() {
            return getPresentation().getEntityTitle(language);
        }

    }
}
