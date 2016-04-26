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

package org.radixware.kernel.common.design.msdleditor.field.panel.simple;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import org.radixware.kernel.common.utils.Hex;


public class HexInputVerifier extends InputVerifier {

    @Override
    public boolean verify(JComponent input) {
        JTextField tf = (JTextField) input;
        String hex = tf.getText();
        if (hex.length() == 0)
            return true;
        try {
            byte[] tmp = Hex.decode(hex);
        }
        catch (Exception ex) {
            JOptionPane.showMessageDialog(null,"Wrong hex");
            return false;
        }
        return true;
    }

}
