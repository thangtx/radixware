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
import java.util.EnumSet;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsModelClassDef;
import org.radixware.kernel.common.defs.ads.command.AdsCommandDef;
import org.radixware.kernel.common.defs.ads.command.AdsCommandModelClassDef;
import org.radixware.kernel.common.defs.ads.platform.IPlatformClassPublisher;
import org.radixware.kernel.common.defs.ads.ui.AdsDialogModelClassDef;
import org.radixware.kernel.common.enums.EClassType;
import static org.radixware.kernel.common.enums.EClassType.COMMAND_MODEL;
import static org.radixware.kernel.common.enums.EClassType.DYNAMIC;
import static org.radixware.kernel.common.enums.EClassType.ENUMERATION;
import static org.radixware.kernel.common.enums.EClassType.EXCEPTION;
import static org.radixware.kernel.common.enums.EClassType.INTERFACE;
import org.radixware.kernel.common.repository.ads.AdsSegment;
import org.radixware.kernel.designer.ads.editors.creation.AdsClassCreature;

import org.radixware.kernel.designer.common.dialogs.wizards.newobject.Creature;
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.CreatureSetupStep;


public final class NestedClassCreature extends AdsClassCreature {

    public final static class Factory {

        private Factory() {
        }

        public static NestedClassCreature newInstance(AdsClassDef container, EClassType classType) {
            switch (classType) {
                case DYNAMIC:
                case INTERFACE:
                case ENUMERATION:
                case EXCEPTION:
                    return new NestedClassCreature(container, classType);
                case COMMAND_MODEL:
                    if (container instanceof AdsModelClassDef && !(container instanceof AdsDialogModelClassDef)) {
                        return new NestedClassCreature(container, classType);
                    } else {
                        return null;
                    }
                default:
                    assert false : "Unsupported class type";
                    return null;
            }
        }

        public static List<NestedClassCreature> instanceList(AdsClassDef container, EnumSet<EClassType> classTypes) {
            final List<NestedClassCreature> result = new ArrayList<>();
            for (final EClassType ct : classTypes) {
                NestedClassCreature c = newInstance(container, ct);
                if (c != null) {
                    result.add(c);
                }
            }

            return result;
        }
    }
    private AdsCommandDef selectedCommand;
    private AdsClassDef contextClass;

    private NestedClassCreature(AdsClassDef container, EClassType classType) {
        super(container.getNestedClasses().getLocal(), classType);
        contextClass = container;
    }

    public AdsClassDef getContextClass() {
        return contextClass;
    }

    @Override
    public void afterAppend(AdsDefinition object) {
        switch (classType) {
            case COMMAND_MODEL:
                AdsCommandModelClassDef clazz = (AdsCommandModelClassDef) object;
                IPlatformClassPublisher publisher = ((AdsSegment) clazz.getLayer().getAds()).getBuildPath().getPlatformPublishers().findPublisherByName("org.radixware.kernel.common.client.models.items.Command");
                if (publisher instanceof RadixObject) {
                    clazz.getModule().getDependences().add(((RadixObject) publisher).getModule());
                }

                clazz.sync();
                break;
        }
        super.afterAppend(object); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isEnabled() {
        return !getContainer().isReadOnly();
    }

    @Override
    @SuppressWarnings("unchecked")
    public WizardInfo getWizardInfo() {
        if (classType == EClassType.COMMAND_MODEL) {
            return new Creature.WizardInfo() {
                @Override
                public CreatureSetupStep createFirstStep() {
                    return new NestedClassCreature.CommandSetupStep();
                }

                @Override
                public boolean hasWizard() {
                    return true;
                }
            };
        } else {
            return super.getWizardInfo();
        }

    }

    private class CommandSetupStep extends CreatureSetupStep<NestedClassCreature, NestedClassCreatureWizardStepCommandSetupPanel> implements ChangeListener {

        CommandSetupStep() {
            super();
        }

        @Override
        public void open(NestedClassCreature creature) {
            super.open(creature);
            getVisualPanel().open(definitionName, (AdsModelClassDef) contextClass, selectedCommand, "NewCommandClass");
        }

        @Override
        public void apply(NestedClassCreature creature) {
            super.apply(creature);
            creature.definitionName = getVisualPanel().getClassName();
            creature.selectedCommand = getVisualPanel().getCommand();
        }

        @Override
        public boolean isComplete() {
            return getVisualPanel().isComplete();
        }

        @Override
        public String getDisplayName() {
            return "Setup command model class";
        }

        @Override
        protected NestedClassCreatureWizardStepCommandSetupPanel createVisualPanel() {
            NestedClassCreatureWizardStepCommandSetupPanel p = new NestedClassCreatureWizardStepCommandSetupPanel();
            p.addChangeListener(this);
            return p;
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            fireChange();
        }
    }

    @Override
    public AdsClassDef createInstance() {
        switch (classType) {
            case COMMAND_MODEL:
                return new AdsCommandModelClassDef((AdsModelClassDef) contextClass, selectedCommand);
            default:
                return super.createInstance();
        }
    }
}
