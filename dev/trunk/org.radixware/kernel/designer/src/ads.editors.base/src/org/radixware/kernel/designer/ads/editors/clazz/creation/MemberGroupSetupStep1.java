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
import org.openide.util.NbBundle;
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.CreatureSetupStep;


class MemberGroupSetupStep1 extends CreatureSetupStep<MemberGroupCreature, MethodGroupSetupStep1Visual> {

    @Override
    protected MethodGroupSetupStep1Visual createVisualPanel() {
        return new MethodGroupSetupStep1Visual();
    }

    @Override
    public void apply(MemberGroupCreature creature) {
        creature.setGroupName(getVisualPanel().getCurrentName());
    }

    @Override
    public void open(MemberGroupCreature creature) {
        getVisualPanel().open(creature.getGroupName());
        getVisualPanel().addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                MemberGroupSetupStep1.this.fireChange();
            }
        });
    }

    @Override
    public String getDisplayName() {
        return NbBundle.getMessage(MemberGroupSetupStep1.class, "MethodGroupCreatureStep");
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
