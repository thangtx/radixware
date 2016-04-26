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

package org.radixware.kernel.designer.ads.editors.clazz.report.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import org.openide.actions.UndoAction;
import org.openide.util.actions.SystemAction;
import org.radixware.kernel.common.utils.events.IRadixEventListener;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.designer.ads.editors.clazz.report.diagram.AdsReportFormUndoRedo;


public class AdsReportFormUndoAction extends AbstractAction {

    private final AdsReportFormUndoRedo undoRedo;
    private final IRadixEventListener<RadixEvent> listener = new IRadixEventListener<RadixEvent>() {

        @Override
        public void onEvent(final RadixEvent e) {
            setEnabled(undoRedo.canUndo());
        }
    };

    public AdsReportFormUndoAction(final AdsReportFormUndoRedo undoRedo) {
        super();
        this.undoRedo = undoRedo;
        this.putValue(AbstractAction.SMALL_ICON, SystemAction.get(UndoAction.class).getIcon());
        this.putValue(AbstractAction.NAME, "Undo");
        this.putValue(AbstractAction.SHORT_DESCRIPTION, "Undo Changes (Ctrl+Z)");
        this.putValue(AbstractAction.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_DOWN_MASK));
        this.undoRedo.addStateListener(listener);
        this.setEnabled(undoRedo.canUndo());
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        undoRedo.undo();
    }

}
