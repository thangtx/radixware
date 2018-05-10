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

package org.radixware.kernel.designer.common.dialogs.components.description;

import java.util.Map;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.designer.common.dialogs.components.localizing.ProxyLocalizingStringContext;


public class ModelProxyDescriptionHandleInfo extends MixedDescriptionHandleInfo<DescriptionModel> {

    public ModelProxyDescriptionHandleInfo(DescriptionModel model) {
        super(model, true);
    }

    @Override
    protected IDescriptionHandleInfo createUnderstudy(final DescriptionModel object, EDescriptionType type, boolean proxy) {
        if (type == EDescriptionType.LOCALIZED) {
                return new LocalizedDescriptionHandleInfo(object, true);
//            return new LocalizedDescriptionHandleInfo(object,
//                    new ProxyLocalizingStringContext(object.getDescriptionLocation(),
//                        object.getLocalizedDescriptions(), object.isSpellcheckEnabled()) {
//
//                        @Override
//                        public boolean commit() {
//
//                            if (hasValue()) {
//                                object.createDescription();
//                                object.setDescriptionType(EDescriptionType.LOCALIZED);
//
//                                final Map<EIsoLanguage, String> valueMap = getValueMap();
//                                for (final EIsoLanguage lang : valueMap.keySet()) {
//                                    object.setDescription(lang, valueMap.get(lang));
//                                }
//
//                                object.setSpellcheckEnabled(isSpellcheckEnable());
//
//                            } else {
//                                object.removeDescription();
//                            }
//
//                            return true;
//                        }
//                    });
        }
        return super.createUnderstudy(object, type, proxy);
    }
}
