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

package org.radixware.kernel.designer.ads.method.profile;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;


final class NewParameterDysplayer extends ModalDisplayer implements ChangeListener {

    public NewParameterDysplayer() {
        super(new NewMethodParameterPanel(), "New Parameter");

        getComponent().addChangeListener(this);

        getDialogDescriptor().setValid(getComponent().isComplete());
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource().equals(getComponent())) {
            getDialogDescriptor().setValid(getComponent().isComplete());
        }
    }

    @Override
    public NewMethodParameterPanel getComponent() {
        return (NewMethodParameterPanel) super.getComponent();
    }

    public MethodParametersPanel.ParameterModel createParameter(AdsMethodDef method) {
        getComponent().open(method);
        if (showModal()) {
            return getComponent().getParameter();
        }
        return null;
    }
}