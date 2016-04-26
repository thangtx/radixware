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

package org.radixware.kernel.designer.ads.editors.property;

import java.awt.Dimension;
import javax.swing.BoxLayout;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.designer.common.editors.sqml.SqmlEditorPanel;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;


public class AdsSqmlReferenceConditionDialog extends ModalDisplayer {

    @Override
    public void apply() {
        
    }
    @Override
    public boolean showModal() {
        return super.showModal();
    }
    public AdsSqmlReferenceConditionDialog(RadixObject object, SqmlEditorPanel conditionEditorPanel ) {
        super(conditionEditorPanel , "...");
        //this.add(conditionEditorPanel);
       // conditionEditorPanel.setLayout(new BoxLayout(conditionEditorPanel, BoxLayout.Y_AXIS));

        /*
        SqmlEditorPanel conditionEditorPanel = new SqmlEditorPanel();
        //conditionEditorPanel.setScml(scml)
        conditionEditorPanel.setAlignmentX(0.0f);
        jPanel4.add(conditionEditorPanel);
        jPanel4.setLayout(new BoxLayout(jPanel4, BoxLayout.Y_AXIS));
        */
        this.object = object;
        this.getComponent().setMinimumSize(new Dimension(200, 200));
    }
    RadixObject object;
}
