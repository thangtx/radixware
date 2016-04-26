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

package org.radixware.kernel.designer.ads.editors.clazz.enumeration;

import java.awt.Dimension;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ads.clazz.AdsEnumClassDef;
import org.radixware.kernel.common.defs.ads.clazz.enumeration.AdsFieldParameterDef;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;


public final class AdsEnumClassParameterDisplayer extends ModalDisplayer {
    
    public AdsEnumClassParameterDisplayer(AdsEnumClassDef def, AdsFieldParameterDef param, boolean newParam) {
        super(new AdsEnumClassParameterEditorPanel(def, param));

        String title;
        
        if (newParam) {
            title = NbBundle.getMessage(AdsEnumClassParameterDisplayer.class, "AdsEnumClassAddParameterDisplayer-AddNewParameter-Title");
        } else {
            title = NbBundle.getMessage(AdsEnumClassParameterDisplayer.class, "AdsEnumClassAddParameterDisplayer-DuplicateParameter-Title");
        }

        getDialogDescriptor().setTitle(title);
        getDialogDescriptor().setValid(getComponent().getReadyState());
        
        getComponent().getReadyStateChangeSupport().addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                
                getDialogDescriptor().setValid(((IReadyStatable)e.getSource()).getReadyState());
            }
        });
    }
    
    public AdsFieldParameterDef getParameter() {
        return getComponent().getParameter();
    }

    @Override
    public AdsEnumClassParameterEditorPanel getComponent() {
        return (AdsEnumClassParameterEditorPanel) super.getComponent();
    }
    
    
}
