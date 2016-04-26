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

package org.radixware.kernel.designer.common.dialogs.wizards.newobject;

import org.radixware.kernel.designer.common.dialogs.components.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.designer.common.dialogs.utils.IAdvancedAcceptor;

import org.radixware.kernel.designer.common.dialogs.utils.NameAcceptorFactory;

/**
 * Creature that required only to specify name.
 */
public abstract class NamedRadixObjectCreature<T extends RadixObject> extends Creature<T> {

    private String name = "";
    private final String displayName;

    public NamedRadixObjectCreature(final RadixObjects<T> container, final String displayName) {
        super(container);
        this.displayName = displayName;
    }

    @Override
    public WizardInfo getWizardInfo() {
        return new Creature.WizardInfo() {

            @Override
            public CreatureSetupStep createFirstStep() {
                return new SpecifyNameStep(getContainer());
            }

            @Override
            public boolean hasWizard() {
                return true;
            }
        };
    }

    @Override
    public void afterAppend(T object) {
    }

    public abstract T createInstance(final String name);

    @Override
    public final T createInstance() {
        return createInstance(name);
    }

    @Override
    public boolean afterCreate(T radixObject) {
        return true;
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    public static class SpecifyNameStep extends CreatureSetupStep<NamedRadixObjectCreature, SpecifyNamePanel> {

        private final RadixObjects<RadixObject> container;

        public SpecifyNameStep(RadixObjects<RadixObject> container) {
            this.container = container;
        }

        @Override
        protected SpecifyNamePanel createVisualPanel() {
            final SpecifyNamePanel specifyNamePanel = new SpecifyNamePanel();
            final IAdvancedAcceptor<String> nameAcceptor = NameAcceptorFactory.newCreateAcceptor(container, null);
            specifyNamePanel.setNameAcceptor(nameAcceptor);
            return specifyNamePanel;
        }

        @Override
        public void open(final NamedRadixObjectCreature creature) {
            super.open(creature);
            this.getVisualPanel().setCurrentName(creature.name);

            this.getVisualPanel().addChangeListener(new ChangeListener() {

                @Override
                public void stateChanged(ChangeEvent e) {
                    creature.name = getVisualPanel().getCurrentName();
                    fireChange();
                }
            });
        }

        @Override
        public boolean isComplete() {
            return getVisualPanel().isComplete();
        }

        @Override
        public String getDisplayName() {
            return "Name";
        }
    }
}
