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

package org.radixware.kernel.common.types;

import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.environment.IMlStringBundle;
import org.radixware.kernel.common.environment.IRadixEnvironment;

public class MultilingualString {

    public static final String get(final IRadixEnvironment env, final Id ownerId, final Id stringId) {
        return get(env, ownerId, stringId, null);
    }

    public static final String get(final IRadixEnvironment env, final Id ownerId, final Id stringId, final EIsoLanguage lang) {
        if (stringId == null || ownerId == null) {
            return null;
        }
        IMlStringBundle bundle = env.getDefManager().getStringBundleById(Id.Factory.loadFrom(EDefinitionIdPrefix.ADS_LOCALIZING_BUNDLE.getValue() + ownerId.toString()));
// changed by vrymar 24.11.09
//        IMlStringBundle bundle = env.getDefManager().getStringBundleById(Id.Factory.changePrefix(ownerId, EDefinitionIdPrefix.ADS_LOCALIZING_BUNDLE));
        return bundle == null ? null : bundle.get(stringId, lang == null ? env.getClientLanguage() : lang);
    }
}
