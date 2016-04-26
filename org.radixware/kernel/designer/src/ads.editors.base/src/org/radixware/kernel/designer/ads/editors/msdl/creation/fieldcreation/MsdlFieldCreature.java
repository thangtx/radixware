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

package org.radixware.kernel.designer.ads.editors.msdl.creation.fieldcreation;

import javax.swing.ComboBoxModel;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.msdl.EFieldType;
import org.radixware.kernel.common.msdl.MsdlField;
import org.radixware.kernel.common.msdl.RootMsdlScheme;
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.Creature;
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.CreatureSetupStep;


public abstract class MsdlFieldCreature extends Creature<MsdlField> {

    private String name;
    private EFieldType type;

    public MsdlFieldCreature(RadixObjects owner) {
        super(owner);
    }

    @Override
    public WizardInfo getWizardInfo() {
        return new WizardInfo() {

            @Override
            public CreatureSetupStep createFirstStep() {
                return new FieldSetupStep1();
            }

            @Override
            public boolean hasWizard() {
                return true;
            }
        };
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public String getDisplayName() {
        return NbBundle.getMessage(MsdlFieldCreature.class, "Field");
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean afterCreate(MsdlField object) {
        object.setType(type);
        object.setName(name);
        return true;
    }

    @Override
    public void afterAppend(MsdlField object) {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(EFieldType type) {
        this.type = type;
    }

    public ComboBoxModel getFieldList() {
        RadixObject radixObject = getContainer().getContainer();
        RootMsdlScheme rootMsdlScheme = null;
        if (radixObject instanceof MsdlField)
            rootMsdlScheme = ((MsdlField)radixObject).getRootMsdlScheme();
        else
            rootMsdlScheme = ((MsdlField)(radixObject.getContainer())).getRootMsdlScheme();
        return rootMsdlScheme.getFieldList(rootMsdlScheme.isDbf());
    }
}
