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

import java.util.HashMap;
import java.util.Map;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.enums.EIsoLanguage;


public class ProxyLocalizingStringContext implements ILocalizingStringContext {

    private ProxyMultilingualString proxyMultilingualString;
    private final Definition definition;

    ProxyLocalizingStringContext(Definition definition, ProxyMultilingualString proxyMultilingualString) {
        this.definition = definition;
        this.proxyMultilingualString = proxyMultilingualString;
    }

    protected ProxyLocalizingStringContext(Definition definition, Map<EIsoLanguage, String> values, boolean isSpellcheckEnabled) {
        this(definition, (ProxyMultilingualString) (values != null ? new ProxyMultilingualString(values, isSpellcheckEnabled) : null));
    }

    protected ProxyLocalizingStringContext(Definition definition) {
        this(definition, (ProxyMultilingualString) null);
    }

    @Override
    public Definition getAdsDefinition() {
        return definition;
    }

    @Override
    public boolean commit() {
        return false;
    }

    @Override
    public void enableSpellcheck(boolean enable) {
        if (hasValue()) {
            getProxyString().enableSpellcheck(enable);
        }
    }

    @Override
    public String getValue(EIsoLanguage language) {
        if (hasValue()) {
            return getProxyString().getValue(language);
        }
        return null;
    }

    @Override
    public void setValue(EIsoLanguage language, String value) {
        if (!hasValue()) {
            create();
        }
        getProxyString().setValue(language, value);
    }

    @Override
    public void create() {
        if (!hasValue()) {
            proxyMultilingualString = new ProxyMultilingualString();
        }
    }

    @Override
    public void remove() {
        if (hasValue() && isRemovable()) {
            proxyMultilingualString = null;
        }
    }

    @Override
    public boolean hasValue() {
        return proxyMultilingualString != null;
    }

    @Override
    public boolean isSpellcheckEnable() {
        return hasValue() && proxyMultilingualString.isSpellcheckEnabled();
    }

    @Override
    public final boolean isProxy() {
        return true;
    }

    @Override
    public Map<EIsoLanguage, String> getValueMap() {
        if (hasValue()) {
            return new HashMap<>(proxyMultilingualString.getValues());
        }
        return null;
    }

    @Override
    public boolean isRemovable() {
        return true;
    }

    ProxyMultilingualString getProxyString() {
        return proxyMultilingualString;
    }

    @Override
    public ILocalizedStringInfo getStringInfo() {
        return null;
    }
}
