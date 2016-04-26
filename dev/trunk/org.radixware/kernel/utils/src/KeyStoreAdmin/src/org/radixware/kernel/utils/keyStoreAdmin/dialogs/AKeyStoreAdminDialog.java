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

package org.radixware.kernel.utils.keyStoreAdmin.dialogs;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.JComponent;
import javax.swing.InputMap;
import javax.swing.KeyStroke;



abstract public class AKeyStoreAdminDialog extends javax.swing.JDialog{
    
    /** A return status code - returned if Cancel button has been pressed */
    public static final int RET_CANCEL = 0;
    /** A return status code - returned if OK button has been pressed */
    public static final int RET_OK = 1;

    public AKeyStoreAdminDialog(java.awt.Frame parent) {
        super(parent, true);
        InputMap iMap = this.getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "escape");
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "enter");

        ActionMap aMap = this.getRootPane().getActionMap();
        aMap.put("escape", new AbstractAction(){
            public void actionPerformed(ActionEvent e){
                doClose(RET_CANCEL);
            };
        });
        aMap.put("enter", new AbstractAction(){
            public void actionPerformed(ActionEvent e){
                doClose(RET_OK);
            };
        });
    }

    abstract protected void doClose(int retStatus);
}
