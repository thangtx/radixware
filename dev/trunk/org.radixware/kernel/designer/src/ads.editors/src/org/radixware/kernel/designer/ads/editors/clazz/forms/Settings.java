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

package org.radixware.kernel.designer.ads.editors.clazz.forms;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;


public final class Settings {

    private static EIsoLanguage language = null;

    private static final String PREFERENCES_KEY = "RadixWareDesigner";
    private static final String FORMS_KEY = "Forms";
    private static final String LANGUAGE_KEY = "Language";

    public static EIsoLanguage getLanguage() {
        if (language == null) {
            restore();
        }
        return language;
    }

    public static void setLanguage(EIsoLanguage language) {
        if (!Utils.equals(Settings.language, language)) {
            Settings.language = language;
            store();
        }
    }

    private static void restore() {
        language = EIsoLanguage.ENGLISH;
        try {
            if (Preferences.userRoot().nodeExists(PREFERENCES_KEY)) {
                Preferences designer = Preferences.userRoot().node(PREFERENCES_KEY);
                if (designer.nodeExists(FORMS_KEY)) {
                    Preferences forms = designer.node(FORMS_KEY);
                    language = EIsoLanguage.getForValue(forms.get(LANGUAGE_KEY, EIsoLanguage.ENGLISH.getValue()));
                }
            }
        } catch (BackingStoreException ex) {
            DialogUtils.messageError(ex);
        }
    }

    private static void store() {
        try {
            Preferences designer = Preferences.userRoot().node(PREFERENCES_KEY);
            Preferences forms = designer.node(FORMS_KEY);
            forms.put(LANGUAGE_KEY, language.getValue());
            Preferences.userRoot().flush();
        } catch (BackingStoreException ex) {
            DialogUtils.messageError(ex);
        }
    }
}
