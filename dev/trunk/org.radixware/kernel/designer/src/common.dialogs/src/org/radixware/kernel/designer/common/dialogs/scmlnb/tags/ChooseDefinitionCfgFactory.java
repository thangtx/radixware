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

package org.radixware.kernel.designer.common.dialogs.scmlnb.tags;

import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IFilter;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.sqml.providers.SqmlVisitorProviderFactory;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;


public final class ChooseDefinitionCfgFactory {

    private ChooseDefinitionCfgFactory() {
    }

    public static ChooseDefinitionCfg createCfgForIdTag(RadixObject context, Class<? extends Definition> definitionClass, int stepCount) {
        return createCfgForIdTag(context, definitionClass, stepCount, null);
    }

    public static ChooseDefinitionCfg createCfgForIdTag(RadixObject context, Class<? extends Definition> definitionClass, int stepCount, IFilter<RadixObject> filter) {
        ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(
                context,
                SqmlVisitorProviderFactory.newIdTagProvider(definitionClass, filter));
        cfg.setStepCount(stepCount);
        return cfg;
    }

    public static ChooseDefinitionCfg createCfgForDbNameTag(RadixObject context, Class<? extends Definition> definitionClass, int stepCount) {
        ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(
                context,
                SqmlVisitorProviderFactory.newDbNameTagProvider(definitionClass));
        cfg.setStepCount(stepCount);
        return cfg;
    }
}
