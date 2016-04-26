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

package org.radixware.kernel.designer.ads.editors.msdl.creation.fieldcreation;

import org.openide.util.NbBundle;
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.CreatureSetupStep;


public class FieldSetupStep1 extends CreatureSetupStep<MsdlFieldCreature, FieldSetupStep1Visual> {

    public FieldSetupStep1() {
        super();
    }

    @Override
    public FieldSetupStep1Visual createVisualPanel() {
        return new FieldSetupStep1Visual();
    }

    @Override
    public void open(MsdlFieldCreature creature) {
        getVisualPanel().open(creature);
    }

    @Override
    public void apply(MsdlFieldCreature creature) {
        getVisualPanel().apply();
    }

    @Override
    public boolean isComplete() {
        return getVisualPanel().isComplete();
    }

    @Override
    public String getDisplayName() {
        return NbBundle.getMessage(FieldSetupStep1.class, "NewFieldSettings");
    }
}
