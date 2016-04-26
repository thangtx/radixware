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

package org.radixware.kernel.common.defs.ads.clazz;

import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IFilter;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.IModelPublishableProperty;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.types.Id;


class AdsClassModelPropertyPublishingSupport implements IModelPublishableProperty.Support {

    private final transient AdsClassDef clazz;

    AdsClassModelPropertyPublishingSupport(AdsClassDef clazz) {
        this.clazz = clazz;
    }

    @Override
    public List<? extends IModelPublishableProperty> list(final ERuntimeEnvironmentType clientEnv, EScope scope, final IFilter<AdsDefinition> external) {
        final List<AdsPropertyDef> allProps = clazz.getProperties().get(scope, new IFilter<AdsPropertyDef>() {

            @Override
            public boolean isTarget(final AdsPropertyDef radixObject) {
                if (radixObject instanceof IModelPublishableProperty) {
                    boolean simpleMatch = external == null ? true : external.isTarget(radixObject);
                    if (simpleMatch) {
                        ERuntimeEnvironmentType propEnv = radixObject.getClientEnvironment();
                        if (propEnv == ERuntimeEnvironmentType.COMMON_CLIENT || propEnv == clientEnv) {
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        });
        return new ArrayList<IModelPublishableProperty>(allProps);
    }

    @Override
    public IModelPublishableProperty findById(Id id, EScope scope) {
        AdsPropertyDef prop = clazz.getProperties().findById(id, scope).get();
        if (prop instanceof IModelPublishableProperty) {
            return (IModelPublishableProperty) prop;
        } else {
            return null;
        }
    }
}
