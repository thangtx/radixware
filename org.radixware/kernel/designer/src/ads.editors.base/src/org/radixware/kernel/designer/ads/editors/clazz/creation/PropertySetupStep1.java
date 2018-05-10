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
package org.radixware.kernel.designer.ads.editors.clazz.creation;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsDynamicPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.CreatureSetupStep;

class PropertySetupStep1 extends CreatureSetupStep<PropertyCreature, PropertySetupStep1Visual> {

    @Override
    public String getDisplayName() {
        return "Property Setup";
    }

    @Override
    protected PropertySetupStep1Visual createVisualPanel() {
        PropertySetupStep1Visual panel = new PropertySetupStep1Visual();
        panel.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                PropertySetupStep1.this.fireChange();
            }
        });
        return panel;
    }

    @Override
    public void open(PropertyCreature creature) {
        super.open(creature);
        getVisualPanel().open(creature);
    }

    @Override
    public void apply(PropertyCreature creature) {
        super.apply(creature);
        getVisualPanel().apply();
    }

    @Override
    public boolean isComplete() {
        return getVisualPanel().isComplete();
    }

    @Override
    public boolean isFinishiable() {
        return true;
    }
}
