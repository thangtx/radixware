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
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsApplicationClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityBasedClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityGroupClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsPresentationEntityAdapterClassDef;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.Creature;
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.CreatureSetupStep;


class AdsEntityClassCreature extends AdsClassCreature {

    private DdsTableDef selectedEntity = null;
    private AdsEntityObjectClassDef selectedbasis = null;
    private String defaultName;

    AdsEntityClassCreature(AdsModule module, EClassType classType) {
        super(module, classType, false);
        this.module = module;
        switch (classType) {
            case ENTITY:
                this.definitionName = NbBundle.getMessage(AdsEntityClassCreature.class, "NewEntityClass");
                break;
            case ENTITY_GROUP:
                this.definitionName = NbBundle.getMessage(AdsEntityClassCreature.class, "NewEntityGroupClass");
                break;
            case APPLICATION:
                this.definitionName = NbBundle.getMessage(AdsEntityClassCreature.class, "NewApplicationClass");
                break;
            case PRESENTATION_ENTITY_ADAPTER:
                this.definitionName = NbBundle.getMessage(AdsEntityClassCreature.class, "NewPresentationEntityAdapterClass");
                break;
        }
        this.defaultName = definitionName;
    }

    @Override
    public String getDisplayName() {
        switch (classType) {
            case ENTITY:
                return NbBundle.getMessage(AdsEntityClassCreature.class, "Entity_Class");
            case ENTITY_GROUP:
                return NbBundle.getMessage(AdsEntityClassCreature.class, "Entity_Group_Class");
            case APPLICATION:
                return NbBundle.getMessage(AdsEntityClassCreature.class, "Application_Class");
            case PRESENTATION_ENTITY_ADAPTER:
                return NbBundle.getMessage(AdsEntityClassCreature.class, "Presentation_Entity_Adapter_Class");
        }
        return NbBundle.getMessage(AdsEntityClassCreature.class, "Entity_Based_Class");
    }

    @Override
    public String getDescription() {
        StringBuilder builder = new StringBuilder();
        switch (classType) {
            case ENTITY:
                return NbBundle.getMessage(AdsEntityClassCreature.class, "Entiy_Description");
            case ENTITY_GROUP:
                return NbBundle.getMessage(AdsEntityClassCreature.class, "Group_Description");
            case APPLICATION:
                return NbBundle.getMessage(AdsEntityClassCreature.class, "Application_Description");
            case PRESENTATION_ENTITY_ADAPTER:
                return NbBundle.getMessage(AdsEntityClassCreature.class, "Adapter_Description");
        }
        return builder.toString();
    }

    @Override
    public AdsEntityBasedClassDef createInstance() {
        AdsEntityBasedClassDef entityClass = null;
        switch (classType) {
            case ENTITY:
                entityClass = AdsEntityClassDef.Factory.newInstance(selectedEntity);
                break;
            case ENTITY_GROUP:
                entityClass = AdsEntityGroupClassDef.Factory.newInstance((AdsEntityClassDef) selectedbasis);
                break;
            case APPLICATION:
                entityClass = AdsApplicationClassDef.Factory.newInstance(selectedbasis);
                break;
            case PRESENTATION_ENTITY_ADAPTER:
                entityClass = AdsPresentationEntityAdapterClassDef.Factory.newInstance(selectedbasis);
                break;
        }
        entityClass.setName(definitionName);
        return entityClass;
    }

    @Override
    @SuppressWarnings("unchecked")
    public WizardInfo getWizardInfo() {


        return new Creature.WizardInfo() {

            @Override
            public CreatureSetupStep createFirstStep() {
                return new EntitySetupStep();
            }

            @Override
            public boolean hasWizard() {
                return true;
            }
        };
    }

    @Override
    public void afterAppend(AdsDefinition object) {
        super.afterAppend(object);
        if (classType.equals(EClassType.ENTITY_GROUP)) {
            updateEntityGroupAfterCreate((AdsEntityGroupClassDef) object, selectedbasis);
        }

    }

    private class EntitySetupStep extends CreatureSetupStep<AdsEntityClassCreature, AdsEntityClassWizardStep1Panel> implements ChangeListener {

        EntitySetupStep() {
            super();
        }

        @Override
        public void open(AdsEntityClassCreature creature) {
            super.open(creature);
            getVisualPanel().open(definitionName, module, selectedEntity, selectedbasis, AdsEntityClassCreature.this.classType, defaultName);
        }

        @Override
        public void apply(AdsEntityClassCreature creature) {
            super.apply(creature);
            creature.definitionName = getVisualPanel().getCurrentName();
            creature.selectedEntity = getVisualPanel().getSelectedTable();
            creature.selectedbasis = getVisualPanel().getSelectedBasis();
        }

        @Override
        public boolean isComplete() {
            return getVisualPanel().isComplete();
        }

        @Override
        public String getDisplayName() {
            return NbBundle.getMessage(AdsEntityClassCreature.class, "Setup_New_Entity_Class");
        }

        @Override
        protected AdsEntityClassWizardStep1Panel createVisualPanel() {
            AdsEntityClassWizardStep1Panel p = new AdsEntityClassWizardStep1Panel();
            p.addChangeListener(this);
            return p;
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            fireChange();
        }
    }
}
