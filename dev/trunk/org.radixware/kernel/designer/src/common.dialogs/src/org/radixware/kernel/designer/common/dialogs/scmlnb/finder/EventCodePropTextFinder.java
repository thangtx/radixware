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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsEventCodePropertyDef;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.designer.common.general.editors.EditorsManager;


public class EventCodePropTextFinder implements IFinder {

    private final Pattern pattern;

    public EventCodePropTextFinder(final Pattern pattern) {
        this.pattern = pattern;
    }

    @Override
    public boolean accept(RadixObject radixObject) {
        return radixObject instanceof AdsEventCodePropertyDef;
    }

    @Override
    public List<IOccurrence> list(final RadixObject radixObject) {
        final List<IOccurrence> occurences = new ArrayList<>();
        if (radixObject instanceof AdsEventCodePropertyDef) {
            final AdsEventCodePropertyDef propDef = (AdsEventCodePropertyDef) radixObject;
            final AdsMultilingualStringDef strinDef = (propDef).findEventCode();
            if (strinDef != null) {
                final List<AdsMultilingualStringDef.StringStorage> stringStorageList = strinDef.getValues(ExtendableDefinitions.EScope.LOCAL);
                if (stringStorageList != null) {
                    for (AdsMultilingualStringDef.StringStorage stringStorage : stringStorageList) {
                        Matcher matcher = pattern.matcher(stringStorage.getValue());
                        while (matcher.find()) {
                            final FindUtils.ContainingLineInfo lineInfo = FindUtils.getContainingLine(stringStorage.getValue(), matcher.start());
                            occurences.add(new EventCodePropOccurence(propDef, stringStorage.getLanguage(), lineInfo.getLine(), pattern));
                        }
                    }
                }
            }
        }
        return occurences;
    }

    private static class EventCodePropOccurence implements IOccurrence {

        private final AdsEventCodePropertyDef def;
        private final String line;
        private final Pattern pattern;
        private final EIsoLanguage lang;

        public EventCodePropOccurence(AdsEventCodePropertyDef def, final EIsoLanguage lang, String containigLine, Pattern pattern) {
            this.def = def;
            this.lang = lang;
            this.line = containigLine;
            this.pattern = pattern;
        }

        @Override
        public String getDisplayText() {
            return FindUtils.html("For language \"" + lang.getName() + "\": " + FindUtils.markPatternBold(line, pattern));
        }

        @Override
        public RadixObject getOwnerObject() {
            return def;
        }

        @Override
        public void goToObject() {
            EditorsManager.getDefault().open(def);
        }
    }
}
