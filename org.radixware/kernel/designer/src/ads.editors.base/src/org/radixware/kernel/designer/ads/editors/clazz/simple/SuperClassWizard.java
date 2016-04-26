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

package org.radixware.kernel.designer.ads.editors.clazz.simple;

import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.designer.common.dialogs.wizards.WizardSteps;




class SuperClassWizard extends WizardSteps {

    protected AdsClassDef context;

    public SuperClassWizard(AdsClassDef context) {
        super();
        this.context = context;
    }

    public AdsClassDef getResultClassObj() {
        return ((SuperClassWizardSettings) getSettings()).getResultClassObj();
    }

    public AdsTypeDeclaration getResultClassDeclaration(){
        return ((SuperClassWizardSettings)getSettings()).getResultClassDeclaration();
    }

    @Override
    public String getDisplayName() {
        return "Choose Super Class";
    }

    @Override
    public Object createSettings() {
        return new SuperClassWizardSettings();
    }

    @Override
    public Step createInitial() {
        return new SuperClassWizardStep(context);
    }    
}
