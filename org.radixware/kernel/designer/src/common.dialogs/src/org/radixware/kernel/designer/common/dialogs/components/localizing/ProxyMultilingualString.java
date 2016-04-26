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

package org.radixware.kernel.designer.common.dialogs.components.localizing;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EIsoLanguage;


final class ProxyMultilingualString {

    private Map<EIsoLanguage, String> values = new EnumMap<>(EIsoLanguage.class);
    private boolean spellcheck;

    public ProxyMultilingualString() {
        spellcheck = true;
    }

    public ProxyMultilingualString(IMultilingualStringDef source) {
        if (source != null) {
            for (final EIsoLanguage lang : source.getLanguages()) {
                setValue(lang, source.getValue(lang));
            }
            spellcheck = source.isSpellCheckEnabled();
        }
    }

    public ProxyMultilingualString(Map<EIsoLanguage, String> values, boolean isSpellcheckEnabled) {
        this();
        if (values != null) {
            this.values = new EnumMap<>(values);
        }
        this.spellcheck = isSpellcheckEnabled;
    }

    public void enableSpellcheck(boolean enable) {
        spellcheck = enable;
    }

    public boolean isSpellcheckEnabled() {
        return spellcheck;
    }

    public String getValue(EIsoLanguage language) {
        return values.get(language);
    }

    public void setValue(EIsoLanguage language, String value) {
        values.put(language, value);
    }

    public Map<EIsoLanguage, String> getValues() {
        return Collections.unmodifiableMap(values);
    }
}
