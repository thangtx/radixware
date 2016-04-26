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

package org.radixware.kernel.designer.ads.editors.clazz.enumeration.creature;

import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsEnumClassFieldDef;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.Creature;
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.CreatureSetupStep;


public final class EnumClassFieldCreature extends Creature<AdsEnumClassFieldDef> {

    final static class EnumClassFieldModel {

        String name;
    }
    private EnumClassFieldModel model = new EnumClassFieldModel();

    public EnumClassFieldCreature(RadixObjects container) {
        super(container);
    }

    @Override
    public String getDisplayName() {
        return "Enum Item";
    }

    @Override
    public String getDescription() {
        return "Create new enum item";
    }

    @Override
    public AdsEnumClassFieldDef createInstance() {
        return AdsEnumClassFieldDef.Factory.newInstanse(getModel().name);
    }

    @Override
    public RadixIcon getIcon() {
        return RadixObjectIcon.ENUM_ITEM;
    }

    @Override
    public boolean afterCreate(AdsEnumClassFieldDef object) {
        return true;
    }

    @Override
    public void afterAppend(AdsEnumClassFieldDef object) {
    }

    @Override
    public WizardInfo getWizardInfo() {
        return new Creature.WizardInfo() {

            @Override
            public CreatureSetupStep<EnumClassFieldCreature, FieldCreaturePanel> createFirstStep() {
                return new CreatureSetupStep<EnumClassFieldCreature, FieldCreaturePanel>() {

                    @Override
                    public String getDisplayName() {
                        return "Create Enum Item";
                    }

                    @Override
                    protected FieldCreaturePanel createVisualPanel() {
                        return new FieldCreaturePanel();
                    }

                    @Override
                    public boolean isFinishiable() {
                        return true;
                    }

                    @Override
                    public boolean isComplete() {
                        return true;
                    }

                    @Override
                    public void apply(EnumClassFieldCreature creature) {
                        getModel().name = getVisualPanel().getFieldName();
                    }

                    @Override
                    public void open(EnumClassFieldCreature creature) {
                        getVisualPanel().opne();
                    }
                };
            }

            @Override
            public boolean hasWizard() {
                return true;
            }
        };
    }

    EnumClassFieldModel getModel() {
        return model;
    }
}
