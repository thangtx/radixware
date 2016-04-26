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
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.Creature;

import org.radixware.kernel.designer.common.dialogs.wizards.newobject.CreatureSetupStep;


public abstract class AdsNamedDefinitionCreature<T extends AdsDefinition> extends Creature<T> {

    private String name;
    private String displayName;

    public AdsNamedDefinitionCreature(RadixObjects container, String initialName, String displayName) {
        super(container);
        this.name = initialName;
        this.displayName = displayName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public RadixIcon getIcon() {
        return null;
    }

    @Override
    public boolean afterCreate(T object) {
        object.setName(name);
        return true;
    }

    protected String getName() {
        return name;
    }

    @Override
    public void afterAppend(T object) {
    }

    private class AdsNamedDefinitionCreatureWizardStep1 extends CreatureSetupStep<AdsNamedDefinitionCreature, AdsNamedDefinitionCreatureWizardStep1Visual> {

        @Override
        public String getDisplayName() {
            return "Setup " + AdsNamedDefinitionCreature.this.getDisplayName();
        }

        @Override
        protected AdsNamedDefinitionCreatureWizardStep1Visual createVisualPanel() {
            AdsNamedDefinitionCreatureWizardStep1Visual panel = new AdsNamedDefinitionCreatureWizardStep1Visual();
            panel.addChangeListener(new ChangeListener() {

                @Override
                public void stateChanged(ChangeEvent e) {
                    fireChange();
                }
            });
            return panel;
        }

        @Override
        public void open(AdsNamedDefinitionCreature creature) {
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

    void setName(String name) {
        this.name = name;
    }

    
    @Override
    public WizardInfo getWizardInfo() {
        return new WizardInfo() {

            @Override
            public CreatureSetupStep createFirstStep() {
                return new AdsNamedDefinitionCreatureWizardStep1();
            }

            @Override
            public boolean hasWizard() {
                return true;
            }
        };
    }
}
