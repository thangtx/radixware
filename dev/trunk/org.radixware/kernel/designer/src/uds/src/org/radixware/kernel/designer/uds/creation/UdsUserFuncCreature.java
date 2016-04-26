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

package org.radixware.kernel.designer.uds.creation;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsUserPropertyDef;
import org.radixware.kernel.common.defs.uds.UdsDefinitionIcon;
import org.radixware.kernel.common.defs.uds.userfunc.UdsUserFuncDef;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.CreatureSetupStep;


public class UdsUserFuncCreature extends UdsDefinitionCreature {

    private AdsUserPropertyDef userProperty;
    private AdsMethodDef method;

    public UdsUserFuncCreature(RadixObjects container) {
        super(container, "User-Defined Function Sample", "");
        setName("newUserDefinedFunctionSample");
    }

    @Override
    public void afterAppend(RadixObject object) {
    }

    @Override
    public boolean afterCreate(RadixObject object) {
        return true;
    }

    @Override
    public RadixObject createInstance() {
        return UdsUserFuncDef.Factory.newInstance(getName(), userProperty, method);
    }

    @Override
    public RadixIcon getIcon() {
        return UdsDefinitionIcon.USERFUNC;
    }

    public AdsUserPropertyDef getUserProperty() {
        return userProperty;
    }

    public void setUserProperty(AdsUserPropertyDef userProperty) {
        this.userProperty = userProperty;
    }

    public AdsMethodDef getMethod() {
        return method;
    }

    public void setMethod(AdsMethodDef method) {
        this.method = method;
    }

    @Override
    public WizardInfo getWizardInfo() {
        return new org.radixware.kernel.designer.common.dialogs.wizards.newobject.Creature.WizardInfo() {

            @Override
            public CreatureSetupStep createFirstStep() {
                return new Step1();
            }

            @Override
            public boolean hasWizard() {
                return true;
            }
        };
    }

    private class Step1 extends CreatureSetupStep<UdsUserFuncCreature, UserFuncWizardStep1Panel> implements ChangeListener {

        private UserFuncWizardStep1Panel dialog;

        @Override
        public String getDisplayName() {
            return "Setup new User-Defined Function Sample";
        }

        @Override
        protected UserFuncWizardStep1Panel createVisualPanel() {
            if (dialog == null) {
                dialog = new UserFuncWizardStep1Panel(UdsUserFuncCreature.this);
                dialog.addChangeListener(this);
            }
            return dialog;
        }

        @Override
        public boolean isComplete() {
            return dialog == null ? false : dialog.isComplete();
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            fireChange();
        }
    }
}
