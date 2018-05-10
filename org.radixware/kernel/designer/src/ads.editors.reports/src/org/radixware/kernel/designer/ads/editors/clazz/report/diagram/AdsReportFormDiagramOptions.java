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

package org.radixware.kernel.designer.ads.editors.clazz.report.diagram;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.common.utils.events.IRadixEventListener;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.common.utils.events.RadixEventSource;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;


public class AdsReportFormDiagramOptions {

    private static final AdsReportFormDiagramOptions INSTANCE = new AdsReportFormDiagramOptions();
    private int scalePercent = 100;
    private final RadixEventSource<IRadixEventListener<RadixEvent>, RadixEvent> changeSupport = new RadixEventSource<>();
    private EIsoLanguage defaultLanguage = EIsoLanguage.ENGLISH;

    public static AdsReportFormDiagramOptions getDefault() {
        return INSTANCE;
    }

    private AdsReportFormDiagramOptions() {
        super();
        load();
    }
    private static final String PREFERENCES_KEY = "ReportFormDiagram";
    private static final String SCALE_PERCENT = "ScalePercent";
    private static final String LANGUAGE = "Language";

    private void load() {
        try {
            final Preferences preferences = Utils.findPreferences(PREFERENCES_KEY);
            if (preferences == null) {
                return;
            }

            this.scalePercent = preferences.getInt(SCALE_PERCENT, scalePercent);
            this.defaultLanguage = EIsoLanguage.getForValue(preferences.get(LANGUAGE, defaultLanguage.getValue()));
        } catch (BackingStoreException ex) {
            DialogUtils.messageError(ex);
        }
    }

    private void save() {
        final Preferences preferences = Utils.findOrCreatePreferences(PREFERENCES_KEY);
        preferences.putInt(SCALE_PERCENT, scalePercent);
        preferences.put(LANGUAGE, defaultLanguage.getValue());

        try {
            Preferences.userRoot().flush();
        } catch (BackingStoreException ex) {
            DialogUtils.messageError(ex);
        }
    }

    public int getScalePercent() {
        return scalePercent;
    }

    public void setPercent(int percent) {
        if (this.scalePercent != percent) {
            this.scalePercent = percent;
            onChanged();
        }
    }

    private void onChanged() {
        changeSupport.fireEvent(new RadixEvent());
        save();
    }

    public synchronized void addChangeListener(IRadixEventListener<RadixEvent> listener) {
        changeSupport.addEventListener(listener);
    }

    public synchronized void removeChangeListener(IRadixEventListener<RadixEvent> listener) {
        changeSupport.removeEventListener(listener);
    }

    public EIsoLanguage getDefaultLanguage() {
        return defaultLanguage;
    }

    public void setDefaultLanguage(EIsoLanguage language) {
        if (!Utils.equals(this.defaultLanguage, language)) {
            this.defaultLanguage = language;
            //onChanged();
        }
    }
}
