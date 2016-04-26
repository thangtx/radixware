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

package org.radixware.kernel.designer.ads.editors.module;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.nodes.ChildFactory;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.module.AdsPath;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.types.Id;


abstract class DefinitionNodeFactory extends ChildFactory<AdsPath> implements ChangeListener {

    private final AdsModule module;
    private final ERuntimeEnvironmentType environment;

    public DefinitionNodeFactory(AdsModule module, ERuntimeEnvironmentType environment) {
        this.module = module;
        this.environment = environment;

        module.getServicesCatalog().addChangeListener(this);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        refresh(true);
    }

    public final AdsModule getModule() {
        return module;
    }

    public final ERuntimeEnvironmentType getEnvironment() {
        return environment;
    }
}
