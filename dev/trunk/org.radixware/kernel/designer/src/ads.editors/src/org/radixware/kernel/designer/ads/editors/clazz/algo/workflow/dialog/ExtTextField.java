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

package org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.dialog;

import java.awt.event.ActionListener;
import javax.swing.JButton;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.components.ExtendableTextField;
import org.radixware.kernel.common.resources.RadixWareIcons;


public class ExtTextField extends ExtendableTextField {

    public ExtTextField() {
        super();
    }

    public JButton addChooseButton(ActionListener l) {
        JButton btn = addButton(RadixWareIcons.DIALOG.CHOOSE.getIcon());
        btn.setFocusable(false);
        if (l != null)
            btn.addActionListener(l);
        return btn;
    }

    public JButton addResetButton(ActionListener l) {
        JButton btn = addButton(RadixWareIcons.DELETE.DELETE.getIcon());
        btn.setFocusable(false);
        if (l != null)
            btn.addActionListener(l);
        return btn;
    }

    public void setEmpty() {
        if (getEditorType() == ExtendableTextField.EDITOR_TEXTFIELD)
            setTextFieldValue(NbBundle.getMessage(getClass(), "Panel.empty.text"));
        else
            setComboBoxSelectedItem(null);
    }
}
