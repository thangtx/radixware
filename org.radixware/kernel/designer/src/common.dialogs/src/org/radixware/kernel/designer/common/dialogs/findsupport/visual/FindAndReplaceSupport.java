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

package org.radixware.kernel.designer.common.dialogs.findsupport.visual;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import org.netbeans.editor.Utilities;


public class FindAndReplaceSupport extends WindowAdapter {

    private static final FindAndReplaceSupport INSTANCE = new FindAndReplaceSupport();

    public static FindAndReplaceSupport getInstance() {
        return INSTANCE;
    }
    private final Object lock = new Object();
    private FindAndReplaceDialogSupport dialogSupport;

    protected FindAndReplaceSupport() {
    }

    @Override
    public void windowClosed(WindowEvent e) {
        synchronized (lock) {
            dialogSupport = null;
        }
        Utilities.returnFocus();
    }

    public void showReplaceDialog() {
        synchronized (lock) {
            if (dialogSupport == null) {
                ReplaceNavigator navigator = new ReplaceNavigator(null, new DocumentController());
                dialogSupport = new FindAndReplaceDialogSupport(navigator);
                dialogSupport.getDialog().addWindowListener(this);
            }
        }

        dialogSupport.showDialog();
    }
}
