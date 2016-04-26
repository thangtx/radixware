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

package org.radixware.kernel.designer.common.dialogs.choosetype;

import org.openide.util.NbBundle;


final class ChooseArrayTypeNatureStep extends ChooseTypeNatureStep {
    @Override
    public String getDisplayName() {
        return NbBundle.getMessage(ChooseArrayTypeNatureStep.class, "TypeWizard-ChooseArrayTypeNatureStep-DisplayName");
    }

    @Override
    public void open(Object settings) {
        TypeWizard.Settings s = (TypeWizard.Settings) settings;

        getVisualPanel().enableTypeParameter(s.filter.acceptNature(ETypeNature.TYPE_PARAMETER));

        getVisualPanel().enableJavaPrimitives(true);
        getVisualPanel().enableJavaClass(true);
        
        getVisualPanel().enableDimEditing(false);
        getVisualPanel().enableArrayButton(false);

        fireChange();
    }
}
