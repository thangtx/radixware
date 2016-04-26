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

package org.radixware.kernel.designer.ads.editors.msdl.creation;

import org.openide.util.NbBundle;
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.CreatureSetupStep;


public class SetupStep1 extends CreatureSetupStep<MsdlCreature, SetupStep1Visual> {

    private SetupStep1Visual visualPanel;

    public SetupStep1() {
        super();
        visualPanel = new SetupStep1Visual();
    }

    @Override
    public SetupStep1Visual createVisualPanel() {
        return visualPanel;
    }

    @Override
    public void apply(MsdlCreature creature) {
        visualPanel.apply(creature);
    }


    @Override
    public void open(MsdlCreature creature) {
        visualPanel.open(creature);
    }

    @Override
    public boolean isComplete() {
        return getVisualPanel().isComplete();
    }

    @Override
    public String getDisplayName() {
        return NbBundle.getMessage(SetupStep1.class, "NewSchemeSettings");
    }
}
