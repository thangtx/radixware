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

import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityGroupClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsPropertyCommandDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsScopeCommandDef;
import org.radixware.kernel.common.enums.ECommandScope;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.Creature;
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.CreatureSetupStep;


public class ScopeCommandCreature extends Creature<AdsScopeCommandDef> {

    ECommandScope scope;
    ArrayList<Id> selectedProperties = new ArrayList<Id>();
    private AdsClassDef contextClass;
    private List<AdsPropertyDef> classProperties = null;
    String name = "newCommand";

    public ScopeCommandCreature(RadixObjects container) {
        super(container);
        RadixObject owner = container.getContainer();
        while (owner != null) {
            if (owner instanceof AdsClassDef) {
                contextClass = (AdsClassDef) owner;
                break;
            }
            owner = owner.getContainer();
        }
        if (contextClass instanceof AdsEntityGroupClassDef) {
            scope = ECommandScope.GROUP;
        } else {
            scope = ECommandScope.OBJECT;
        }
    }

    @Override
    public String getDisplayName() {
        return scope == ECommandScope.GROUP ? "Group Command" : "Command";
    }

    @Override
    public String getDescription() {
        switch (scope) {
            default:
                return "";
        }
    }

    @Override
    public AdsScopeCommandDef createInstance() {
        return AdsScopeCommandDef.Factory.newInstance(scope);
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.COMMAND;
    }

    @Override
    public boolean afterCreate(AdsScopeCommandDef object) {
        object.setName(name);
        if (scope == ECommandScope.PROPERTY) {
            ((AdsPropertyCommandDef) object).setUsedPropIds(selectedProperties);
        }
        return true;
    }

    @Override
    public void afterAppend(AdsScopeCommandDef object) {
        //do nothing
    }

    public List<AdsPropertyDef> propertyList() {
        synchronized (this) {
            if (classProperties == null) {
                classProperties = contextClass.getProperties().get(EScope.ALL);
            }
            return classProperties;
        }
    }

    @Override
    public WizardInfo getWizardInfo() {
        return new WizardInfo() {

            @Override
            public CreatureSetupStep createFirstStep() {
                return new SetupStep1();
            }

            @Override
            public boolean hasWizard() {
                return true;
            }
        };
    }

    private class SetupStep1 extends CreatureSetupStep<ScopeCommandCreature, ScopeCommandSetupStep1Visual> {

        @Override
        public String getDisplayName() {
            return "Setup New Command";
        }

        @Override
        protected ScopeCommandSetupStep1Visual createVisualPanel() {
            ScopeCommandSetupStep1Visual panel = new ScopeCommandSetupStep1Visual();
            panel.addChangeListener(new ChangeListener() {

                @Override
                public void stateChanged(ChangeEvent e) {
                    fireChange();
                }
            });
            return panel;
        }

        @Override
        public void open(ScopeCommandCreature creature) {
            super.open(creature);
            getVisualPanel().open(creature);
        }

        @Override
        public boolean hasNextStep() {
            return false;
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
