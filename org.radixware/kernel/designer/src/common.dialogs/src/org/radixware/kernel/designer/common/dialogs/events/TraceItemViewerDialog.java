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

package org.radixware.kernel.designer.common.dialogs.events;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.logging.LogRecord;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;


public class TraceItemViewerDialog {

    private static Rectangle bounds = null;

    public static void show(LogRecord logRecord) {
        TraceItemViewerPanel traceItemViewerPanel = new TraceItemViewerPanel(logRecord);
        DialogDescriptor dialogDescriptor = new DialogDescriptor(traceItemViewerPanel,
                TraceTableModel.getSeverityByLevel(logRecord.getLevel()).getName());
        dialogDescriptor.setOptions(new Object[] { DialogDescriptor.CLOSED_OPTION } );
        dialogDescriptor.setHelpCtx(null);
        dialogDescriptor.setValid(true);
        Dialog dialog = DialogDisplayer.getDefault().createDialog(dialogDescriptor);
        dialog.setMinimumSize(new Dimension(480, 360));
        if (bounds != null)
            dialog.setBounds(bounds);
        dialog.setVisible(true);
        bounds = dialog.getBounds();
        dialog.dispose();
    }

}
