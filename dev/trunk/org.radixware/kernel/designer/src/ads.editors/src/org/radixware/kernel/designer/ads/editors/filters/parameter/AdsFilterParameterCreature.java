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

package org.radixware.kernel.designer.ads.editors.filters.parameter;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsFilterDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsFilterDef.Parameter;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.Creature;
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.CreatureSetupStep;


public class AdsFilterParameterCreature extends Creature<AdsFilterDef.Parameter>{

    private AdsTypeDeclaration typeDecl = null;
    private AdsPropertyDef propertyDef = null;
    private String name = "newFilterParameter";
    private String displayName = "Filter Parameter";

    public AdsFilterParameterCreature(RadixObjects container) {
        super(container);
    }

    public void setProperty(AdsPropertyDef propertyDef) {
        this.propertyDef = propertyDef;
    }

    public void setTypeDecl(AdsTypeDeclaration typeDecl){
        this.typeDecl = typeDecl;
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
    public boolean afterCreate(Parameter object) {
        object.setName(name);

        if (typeDecl != null && typeDecl != AdsTypeDeclaration.UNDEFINED){
            assert(propertyDef == null);
            object.setType(typeDecl);
        }
        return true;
    }

    @Override
    public void afterAppend(Parameter object) {
    }

    @Override
    public Parameter createInstance() {
        if (typeDecl != null && typeDecl != AdsTypeDeclaration.UNDEFINED){
            assert(propertyDef == null);
            return AdsFilterDef.Parameter.Factory.newInstance();
        }else{
            assert(propertyDef != null);
            return AdsFilterDef.Parameter.Factory.newInstance(propertyDef);
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    private class AdsFilterParameterCreatureWizardStep1 extends CreatureSetupStep<AdsFilterParameterCreature, AdsFilterParameterCreatureWizardStep1Visual> {

        @Override
        public String getDisplayName() {
            return "Setup " + AdsFilterParameterCreature.this.getDisplayName();
        }

        @Override
        protected AdsFilterParameterCreatureWizardStep1Visual createVisualPanel() {
            final AdsFilterParameterCreatureWizardStep1Visual panel = new AdsFilterParameterCreatureWizardStep1Visual();
            panel.addChangeListener(new ChangeListener() {

                @Override
                public void stateChanged(ChangeEvent e) {
                    fireChange();
                }
            });
            return panel;
        }

        @Override
        public void open(AdsFilterParameterCreature creature) {
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

    @Override
    public WizardInfo getWizardInfo() {
        return new WizardInfo() {

            @Override
            public CreatureSetupStep createFirstStep() {
                return new AdsFilterParameterCreatureWizardStep1();
            }

            @Override
            public boolean hasWizard() {
                return true;
            }
        };
    }
}
