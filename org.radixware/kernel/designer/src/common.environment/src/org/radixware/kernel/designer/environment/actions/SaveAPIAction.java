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

package org.radixware.kernel.designer.environment.actions;

import java.io.IOException;
import java.util.Collection;
import javax.swing.Action;
import org.openide.util.Lookup;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.module.AdsModule;

public final class SaveAPIAction extends GenerateModuleDeclarationAction {

    public SaveAPIAction(Collection<? extends RadixObject> contexts) {
        super(contexts);
    }

    public SaveAPIAction() {
        super();
    }

    @Override
    public Action createContextAwareInstance(Lookup actionContext) {
        return new SaveAPIAction(actionContext.lookupAll(RadixObject.class));
    }

    @Override
    protected void generateDeclaration(AdsModule module) throws IOException {
        module.generateAPI(true);
    }

    @Override
    protected String declarationDescription() {
        return "API";
    }
}
