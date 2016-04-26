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
import org.radixware.kernel.common.defs.ads.clazz.IAdsPresentableClass;
import org.radixware.kernel.common.defs.ads.clazz.members.ServerPresentationSupport;
import org.radixware.kernel.common.defs.ads.clazz.presentation.IAdsPresentableProperty;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumItemDef;
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
        return radixObject instanceof AdsTitledDefinition || radixObject instanceof IAdsPresentableProperty;
    }

    @Override
    public List<IOccurrence> list(final RadixObject radixObject) {
        if (accept(radixObject)) {
            List<IOccurrence> occurrences = new ArrayList<IOccurrence>();
            List<EIsoLanguage> languages = Collections.emptyList();
            final Layer layer = radixObject.getLayer();
            if (layer != null) {
                languages = layer.getLanguages();
            }

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
            return occurrences;
        }
        return Collections.emptyList();
    }

    private static class TitleOccurrence implements IOccurrence {

        private final RadixObject ownerDef;
        private final ITitledDefinition titledDef;
        private final EIsoLanguage language;
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
                return FindUtils.html("Title for \"" + language.getName() + "\": " + FindUtils.markPatternBold(titledDef.getTitle(language), pattern));
            } else {
                if (ownerDef instanceof AdsEnumItemDef) {
                    return FindUtils.html("Item name: " + FindUtils.markPatternBold(ownerDef.getName(), pattern));
                }
            }
            return "???";
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
}
