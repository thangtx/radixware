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

import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.ErrorManager;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.exceptions.RadixObjectError;
import org.radixware.kernel.common.utils.agents.DefaultAgent;
import org.radixware.kernel.common.utils.agents.IObjectAgent;
import org.radixware.kernel.designer.common.general.creation.ICreatureGroup.ICreature;


public abstract class Creature<T extends RadixObject> implements ICreature<T> {

    public static abstract class WizardInfo implements ICreature.WizardInfo {

        @Override
        public abstract CreatureSetupStep createFirstStep();
    }

    private final IObjectAgent<? extends RadixObjects> containerAgent;

    public Creature(IObjectAgent<? extends RadixObjects> agent) {
        this.containerAgent = agent;
    }

    public Creature(RadixObjects container) {
        this(new DefaultAgent<>(container));
    }

    @Override
    public WizardInfo getWizardInfo() {
        return null;
    }

    /**
     * The final step of object creation Creates object instance, performs pre-
     * and post-additioanl setup Returns true if object successfully added to
     * container, false otherwise
     *
     * @throws {@linkplain DefinitionError} if any exceptions a thrown by setup
     * and addition
     */
    @Override
    public final T commit() {
        try {
            if (!containerAgent.invite(true)) {
                throw new RadixObjectError("Unable to access the parent object.");
            }

            T object = createInstance();
            if (object == null) {
                return null;
            }
            if (acceptObject(object)) {
                if (afterCreate(object)) {
                    containerAgent.getObject().add(object);
                    afterAppend(object);
                    return object;
                }
            }
            return object;
        } catch (Throwable e) {
            ErrorManager.getDefault().notify(e);
            Logger.getLogger(Creature.class.getName()).log(Level.WARNING, null, new RadixObjectError("Exception on creation.", e));
            return null;
        }
    }

    @Override
    public boolean isEnabled() {
        return !containerAgent.getObject().isReadOnly();
    }

    public final RadixObjects getContainer() {
        return containerAgent.getObject();
    }

    protected boolean acceptObject(T object) {
        return !containerAgent.getObject().contains(object);
    }
}
