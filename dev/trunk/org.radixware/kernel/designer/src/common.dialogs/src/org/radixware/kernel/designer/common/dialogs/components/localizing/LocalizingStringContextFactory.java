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

import java.util.Map;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.enums.EIsoLanguage;


public final class LocalizingStringContextFactory {

    public static ILocalizingStringContext newInstance(HandleInfo handleInfo) {
        assert handleInfo != null;
        return handleInfo != null ? new HandleInfoAdapter(handleInfo) : null;
    }

    public static ILocalizingStringContext newProxyInstance(HandleInfo handleInfo) {
        assert handleInfo != null;
        return handleInfo != null ? new ProxyHandleInfoAdapter(handleInfo) : null;
    }

    public static ILocalizingStringContext newProxyInstance(HandleInfo handleInfo, Map<EIsoLanguage, String> values) {
        final ILocalizingStringContext proxyHandleInfo = newProxyInstance(handleInfo);
        for (final EIsoLanguage language : values.keySet()) {
            proxyHandleInfo.setValue(language, values.get(language));
        }
        return proxyHandleInfo;
    }

    public static ILocalizingStringContext newProxyInstance(AdsDefinition definition) {
        return newProxyInstance(definition, (AdsMultilingualStringDef) null);
    }

    public static ILocalizingStringContext newProxyInstance(AdsDefinition definition, Map<EIsoLanguage, String> values, boolean isSpellcheckEnabled) {
        return new ProxyLocalizingStringContext(definition, values, isSpellcheckEnabled);
    }

    public static ILocalizingStringContext newProxyInstance(AdsDefinition definition, AdsMultilingualStringDef multilingualString) {
        return new ProxyLocalizingStringContext(definition, new ProxyMultilingualString(multilingualString));
    }
}
