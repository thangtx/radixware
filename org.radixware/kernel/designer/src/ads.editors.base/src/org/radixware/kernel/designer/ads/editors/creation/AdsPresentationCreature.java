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
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.Creature;
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.CreatureSetupStep;


public abstract class AdsPresentationCreature<T extends AdsPresentationDef> extends Creature<T> {

    public static final int EDITOR_PRESENTATION = 0;
    public static final int SELECTOR_PRESENTATION = 1;
    private String EDITOR_DISPLAY_NAME = NbBundle.getMessage(AdsPresentationCreature.class, "AdsPresentationCreature-Editor");
    private String SELECTOR_DISPLAY_NAME = NbBundle.getMessage(AdsPresentationCreature.class, "AdsPresentationCreature-Selector");
    private String name;
    private int type = 0;
    private Id base;

    public AdsPresentationCreature(RadixObjects container, String initialName, int type) {
        super(container);
        this.name = initialName;
        this.type = type;
    }

    public int getType() {
        return this.type;
    }

    @Override
    public String getDisplayName() {
        if (type == EDITOR_PRESENTATION) {
            return EDITOR_DISPLAY_NAME;
        } else if (type == SELECTOR_PRESENTATION) {
            return SELECTOR_DISPLAY_NAME;
        }
        return "";
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public RadixIcon getIcon() {
        if (type == EDITOR_PRESENTATION) {
            return AdsDefinitionIcon.EDITOR_PRESENTATION;
        } else if (type == SELECTOR_PRESENTATION) {
            return AdsDefinitionIcon.SELECTOR_PRESENTATION;
        }
        return null;
    }

    @Override
    public boolean afterCreate(T object) {
        object.setName(name);
        boolean inherit = base != null;
        if (inherit) {
            ((AdsPresentationDef) object).setBasePresentationId(base);
            ((AdsPresentationDef) object).setRestrictionsInherited(true);
        }
        if (type == EDITOR_PRESENTATION) {
            ((AdsEditorPresentationDef) object).setExplorerItemsInherited(inherit);
            ((AdsEditorPresentationDef) object).setEditorPagesInherited(inherit);
            ((AdsEditorPresentationDef) object).setObjectTitleFormatInherited(true);//from base or from entity
            ((AdsEditorPresentationDef) object).setPropertyPresentationAttributesInherited(inherit);
        } else {
            ((AdsSelectorPresentationDef) object).setCreationClassCatalogInherited(inherit);
            ((AdsSelectorPresentationDef) object).setColumnsInherited(inherit);
            ((AdsSelectorPresentationDef) object).setConditionInherited(inherit);
            ((AdsSelectorPresentationDef) object).setAddonsInherited(inherit);
        }
        return true;
    }

    @Override
    public void afterAppend(T object) {
    }

    public void setBasePresentation(Id base) {
        this.base = base;
    }

    public Id getBasePresentation() {
        return this.base;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public WizardInfo getWizardInfo() {
        return new WizardInfo() {

            @Override
            public CreatureSetupStep createFirstStep() {
                return new AdsPresentationCreatureStep();
            }

            @Override
            public boolean hasWizard() {
                return true;
            }
        };
    }

    private class AdsPresentationCreatureStep extends CreatureSetupStep<AdsPresentationCreature, AdsPresentationCreatureStepPanel> {

        @Override
        public String getDisplayName() {
            return "Setup " + AdsPresentationCreature.this.getDisplayName();
        }

        @Override
        protected AdsPresentationCreatureStepPanel createVisualPanel() {
            AdsPresentationCreatureStepPanel panel = new AdsPresentationCreatureStepPanel();
            panel.addChangeListener(new ChangeListener() {

                @Override
                public void stateChanged(ChangeEvent e) {
                    fireChange();
                }
            });
            return panel;
        }

        @Override
        public void open(AdsPresentationCreature creature) {
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
