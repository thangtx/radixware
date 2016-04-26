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

package org.radixware.kernel.designer.ads.editors.clazz.members.transparent;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.CreatureSetupStep;


abstract class PublishClassMemberStep<CreatureType extends TransparentCreature> extends CreatureSetupStep<CreatureType, PublishingClassMemberPanel> implements ChangeListener {

    private final EClassMemberType classMemberType;
    private Object select;

    public PublishClassMemberStep(EClassMemberType classMemberType) {
        this.classMemberType = classMemberType;
    }

    @Override
    protected PublishingClassMemberPanel createVisualPanel() {
        final PublishingClassMemberPanel panel = new PublishingClassMemberPanel(classMemberType);
        panel.addChangeListener(this);
        return panel;
    }

    @Override
    public boolean isFinishiable() {
        return true;
    }

    @Override
    public boolean isComplete() {
        return select != null;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        final ClassMemberItem selectedMember = getVisualPanel().getSelectedMember();
        select = selectedMember != null ? getVisualPanel().getSelectedMember().getSource() : null;
        fireChange();
    }

    @Override
    public void open(CreatureType creature) {
        getVisualPanel().open(creature.getOwnerClass());
    }

    @Override
    @SuppressWarnings("unchecked")
    public void apply(CreatureType creature) {
        final ClassMemberItem selectedMember = getVisualPanel().getSelectedMember();
        if (selectedMember != null && creature != null) {
            creature.setMemberSource(selectedMember.getSource());
        }
    }

    public final EClassMemberType getClassMemberType() {
        return classMemberType;
    }
}
