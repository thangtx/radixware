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

package org.radixware.kernel.designer.ads.editors.creation;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsClassCatalogDef;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.Creature;
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.CreatureSetupStep;


public class AdsClassCatalogCreature extends Creature<AdsClassCatalogDef> {

    public AdsClassCatalogCreature(RadixObjects container) {
        super(container);
    }
    boolean createVirtual = true; 
     String name = "newClassCatalog";

    @Override
    public WizardInfo getWizardInfo() {
        return new Creature.WizardInfo() {

            @Override
            public boolean hasWizard() {
                return true;
            }

            @Override
            public CreatureSetupStep createFirstStep() {
                return new SetupStep();
            }
        };
    }

    @Override
    public String getDisplayName() {
        return "Creation Class Catalog";
    }

    @Override
    public String getDescription() {
        return "Creation class catalog";
    }

    @Override
    public AdsClassCatalogDef createInstance() {
        if (createVirtual) {
            return AdsClassCatalogDef.Factory.newInstance();
        } else {
            return AdsClassCatalogDef.Factory.newNestedInstance();
        }
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.CLASS_CATALOG;
    }

    @Override
    public boolean afterCreate(AdsClassCatalogDef object) {
        object.setName(name);
        return true;
    }

    @Override
    public void afterAppend(AdsClassCatalogDef object) {
    }

    private class SetupStep extends CreatureSetupStep<AdsClassCatalogCreature, AdsClassCatalogCreatureSetupStep1Visual> implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            fireChange();
        }

        @Override
        public String getDisplayName() {
            return "Setup new class catalog";
        }

        @Override
        protected AdsClassCatalogCreatureSetupStep1Visual createVisualPanel() {
            AdsClassCatalogCreatureSetupStep1Visual panel = new AdsClassCatalogCreatureSetupStep1Visual();
            panel.addChangeListener(this);
            return panel;
        }

        @Override
        public void open(AdsClassCatalogCreature creature) {
            super.open(creature);
            getVisualPanel().open(creature);
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
}
