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
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EIsoLanguage;


class ProxyHandleInfoAdapter extends ProxyLocalizingStringContext {

    private final HandleInfo handleInfo;

    private static ProxyMultilingualString createProxyString(HandleInfo handleInfo) {
        final IMultilingualStringDef multilingualString = handleInfo.getAdsMultilingualStringDef();
        return multilingualString != null ? new ProxyMultilingualString(multilingualString) : null;
    }

    protected ProxyHandleInfoAdapter(HandleInfo handleInfo) {
        super(handleInfo.getAdsDefinition(), createProxyString(handleInfo));

        this.handleInfo = handleInfo;
    }

    @Override
    public boolean commit() {

        if (hasValue()) {
            if (getHandleInfo().getAdsMultilingualStringDef() == null) {
                getHandleInfo().createAdsMultilingualStringDef();
            }

            final Map<EIsoLanguage, String> values = getProxyString().getValues();
            for (EIsoLanguage lang : values.keySet()) {
                getHandleInfo().getAdsMultilingualStringDef().setValue(lang, values.get(lang));
            }
            getHandleInfo().getAdsMultilingualStringDef().setSpellCheckEnabled(isSpellcheckEnable());
        } else {
            getHandleInfo().removeAdsMultilingualStringDef();
        }

        return true;
    }

    public HandleInfo getHandleInfo() {
        return handleInfo;
    }

    @Override
    public boolean isRemovable() {
        return handleInfo.isBundleResettable();
    }
}
