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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.AdsDefinitions;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsChildRefExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsEntityExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParagraphExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParagraphLinkExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParentRefExplorerItemDef;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.designer.ads.editors.creation.AdsNamedDefinitionCreature;
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.Creature;

import org.radixware.kernel.designer.common.dialogs.wizards.newobject.CreatureSetupStep;
import org.radixware.kernel.designer.common.general.creation.ICreatureGroup.ICreature;


public class AdsExplorerItemCreature extends Creature<AdsExplorerItemDef> {

    public static class Factory {

        public static List<ICreature> createInstances(AdsDefinitions container) {
            AdsParagraphExplorerItemDef ownerParagraph = null;
            AdsEditorPresentationDef ownerEpr = null;
            RadixObject owner = container.getOwnerDefinition();
            while (owner != null) {
                if (owner instanceof AdsParagraphExplorerItemDef) {
                    ownerParagraph = (AdsParagraphExplorerItemDef) owner;
                    break;
                } else if (owner instanceof AdsEditorPresentationDef) {
                    ownerEpr = (AdsEditorPresentationDef) owner;
                    break;
                }
                owner = owner.getOwnerDefinition();
            }
            if (ownerParagraph == null && ownerEpr == null) {
                return Collections.emptyList();
            }
            ArrayList<ICreature> result = new ArrayList<ICreature>();

            result.add(new AdsNamedDefinitionCreature(container, "newParagraph", "Paragraph") {

                @Override
                public RadixObject createInstance() {
                    return AdsParagraphExplorerItemDef.Factory.newInstance();
                }

                @Override
                public RadixIcon getIcon() {
                    return AdsDefinitionIcon.PARAGRAPH;
                }
            });

            result.add(new AdsExplorerItemCreature(container, AdsParagraphLinkExplorerItemDef.Factory.newTemporaryInstance(container), "Paragraph Link"));
            result.add(new AdsExplorerItemCreature(container, AdsEntityExplorerItemDef.Factory.newTemporaryInstance(container), "Entity Class Reference"));

            if (ownerEpr != null || ownerParagraph.findOwnerEditorPresentation() != null) {
                result.add(new AdsExplorerItemCreature(container, AdsParentRefExplorerItemDef.Factory.newTemporaryInstance(container), "Parent Reference"));
                result.add(new AdsExplorerItemCreature(container, AdsChildRefExplorerItemDef.Factory.newTemporaryInstance(container), "Child Reference"));
            }

            return result;
        }
    }
    final AdsExplorerItemDef temporary;
    private final String displayName;

    private AdsExplorerItemCreature(RadixObjects container, AdsExplorerItemDef temporary, String displayName) {
        super(container);
        this.temporary = temporary;
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
    public AdsExplorerItemDef createInstance() {
        return (AdsExplorerItemDef) temporary.getClipboardSupport().copy();
    }

    @Override
    public RadixIcon getIcon() {
        return temporary.getIcon();
    }

    @Override
    public boolean afterCreate(AdsExplorerItemDef object) {
        return true;
    }

    @Override
    public void afterAppend(AdsExplorerItemDef object) {
    }

    @Override
    @SuppressWarnings("unchecked")
    public WizardInfo getWizardInfo() {
        return new Creature.WizardInfo() {

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

    private class SetupStep1 extends CreatureSetupStep<AdsExplorerItemCreature, AdsExplorerItemCreatureStep1Visual> implements ChangeListener {

        public SetupStep1() {
        }

        @Override
        public String getDisplayName() {
            if(temporary != null)
            switch(temporary.getDefinitionType()){
                case PARAGRAPH_LINK:
                    return "Setup new paragraph link";
            }
            return "Setup up new explorer item";
        }

        @Override
        protected AdsExplorerItemCreatureStep1Visual createVisualPanel() {
            AdsExplorerItemCreatureStep1Visual panel = new AdsExplorerItemCreatureStep1Visual();
            panel.addChangeListener(this);
            return panel;
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            fireChange();
        }

        @Override
        public boolean isComplete() {
            return getVisualPanel().isComplete();
        }

        @Override
        public boolean isFinishiable() {
            return true;
        }

        @Override
        public void open(AdsExplorerItemCreature creature) {
            super.open(creature);
            getVisualPanel().open(creature);
        }
    }
}
