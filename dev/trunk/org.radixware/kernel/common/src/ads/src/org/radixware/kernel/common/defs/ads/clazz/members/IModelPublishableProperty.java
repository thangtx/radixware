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

package org.radixware.kernel.common.defs.ads.clazz.members;

import java.util.List;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IFilter;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef;
import org.radixware.kernel.common.defs.ads.type.IAdsTypedObject;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.types.Id;


public interface IModelPublishableProperty {

    interface Provider {

        Support getModelPublishablePropertySupport();
    }

    interface Support {

        List<? extends IModelPublishableProperty> list(ERuntimeEnvironmentType clientEnv, EScope scope, IFilter<AdsDefinition> filter);

        IModelPublishableProperty findById(Id id, EScope scope);
    }

    interface IParentSelectorPresentationLookup {

        public Id getParentSelectorPresentationId();

        public boolean setParentSelectorPresentationId(Id id);

        public AdsSelectorPresentationDef findParentSelectorPresentation();
    }

    Id getId();

    String getName();

    boolean isTransferable(ERuntimeEnvironmentType env);

    IAdsTypedObject getTypedObject();
}
