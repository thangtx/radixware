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
import org.radixware.kernel.designer.common.dialogs.wizards.WizardSettings;


class SuperClassWizardSettings extends WizardSettings {

    private AdsClassDef result = null;
    private AdsTypeDeclaration resultDeclaration = null;

    public AdsClassDef getResultClassObj() {
        return result;
    }

    public void setResultClass(AdsClassDef result){
        this.result = result;
    }

    public void setResultClassDeclaration(AdsTypeDeclaration resultDeclaration){
        this.resultDeclaration = resultDeclaration;
    }

    public AdsTypeDeclaration getResultClassDeclaration() {
        return resultDeclaration;
    }


}