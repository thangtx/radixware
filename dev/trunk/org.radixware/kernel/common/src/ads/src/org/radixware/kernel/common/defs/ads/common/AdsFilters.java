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

package org.radixware.kernel.common.defs.ads.common;

import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IFilter;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyPresentationPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.IModelPublishableProperty;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef;


public class AdsFilters {

    private AdsFilters() {
        super();
    }

    public static final IFilter newBasePresentationFilter(final AdsPresentationDef context) {
        if (context instanceof AdsEditorPresentationDef) {
            return new IFilter<AdsEditorPresentationDef>() {

                @Override
                public boolean isTarget(final AdsEditorPresentationDef object) {
                    AdsEditorPresentationDef base = object.findBaseEditorPresentation().get();
                    while (base != null) {
                        if (base.getId() == context.getId()) {
                            return false;
                        }
                        base = base.findBaseEditorPresentation().get();
                    }
                    return true;
                }
            };
        } else {
            return new IFilter<AdsSelectorPresentationDef>() {

                @Override
                public boolean isTarget(final AdsSelectorPresentationDef object) {
                    AdsSelectorPresentationDef base = object.findBaseSelectorPresentation().get();
                    while (base != null) {
                        if (base.getId() == context.getId()) {
                            return false;
                        }
                        base = base.findBaseSelectorPresentation().get();
                    }
                    return true;
                }
            };
        }
    }

    private static class ServerSidePropertyFilter implements IFilter<AdsDefinition> {

        private final AdsPropertyDef property;

        ServerSidePropertyFilter(final AdsPropertyDef property) {
            this.property = property;
        }

        @Override
        public boolean isTarget(final AdsDefinition def) {
            if (def instanceof IModelPublishableProperty) {
                IModelPublishableProperty object = (IModelPublishableProperty) def;
//                if (!object.isTransferable(property.getClientEnvironment())) {
//                    return false;
//                }
                return property.getValue().isTypeAllowed(object.getTypedObject().getType().getTypeId()) && property.getOwnerClass().getProperties().findById(object.getId(), EScope.LOCAL).get() == null;
            } else {
                return false;
            }
//            switch (object.getNature()) {
//                case INNATE:
//                case DYNAMIC:
//                case USER:
//                case FORM_PROPERTY:
//                case PARENT_PROP:
//                case DETAIL_PROP:
//                case REPORT_PARAMETER:
//
//                default:
//                    return false;
//            }
        }
    }

    public static IFilter<AdsDefinition> newAvailablePublishablePropertyFilter(final AdsPropertyPresentationPropertyDef prop) {
        return new ServerSidePropertyFilter(prop);
    }
}
