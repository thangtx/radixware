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

package org.radixware.kernel.designer.common.editors;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import org.openide.DialogDescriptor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.utils.events.IRadixEventListener;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;


class RadixObjectModalDisplayer extends ModalDisplayer {

    private final RadixObjectModalEditor editor;

    public RadixObjectModalDisplayer(RadixObjectModalEditor editor) {
        super(editor);
        this.editor = editor;

        setTitle(editor.getTitle());
        setIcon(editor.getRadixObject().getIcon());
    }

    // hack to close by Enter and ESC
    private static class CloseButton extends Object {

        @Override
        public String toString() {
            return "Close";
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof CloseButton) {
                return true;
            }
            if (super.equals(obj)) {
                return true;
            }
            if (obj == DialogDescriptor.OK_OPTION || obj == DialogDescriptor.CANCEL_OPTION) {
                return true;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return DialogDescriptor.OK_OPTION.hashCode();
        }
    }

    @Override
    public Object[] getOptions() {
        final RadixObject radixObject = editor.getRadixObject();
        if (radixObject.isReadOnly()) {
            return new Object[]{DialogDescriptor.CANCEL_OPTION};
        } else if (editor.isCancelable()) {
            return new Object[]{DialogDescriptor.OK_OPTION, DialogDescriptor.CANCEL_OPTION};
        } else {
            final Object[] result = new Object[]{new CloseButton()};
            return result;
        }
    }

    @Override
    protected void apply() {
        editor.apply();
    }

    @Override
    public void onClosing() {
        editor.onClosed();
    }

    public boolean open(final OpenInfo openInfo) {
        final WindowListener windowListener = (new WindowAdapter() {

            @Override
            public void windowOpened(WindowEvent e) {
                RadixObjectModalDisplayer.this.editor.open(openInfo);
            }
        });

        final IRadixEventListener completeListener = (new IRadixEventListener() {

            @Override
            public void onEvent(RadixEvent e) {
                getDialogDescriptor().setValid(editor.isComplete());
            }
        });

        getDialogDescriptor().setValid(editor.isComplete());

        editor.getCompleteSupport().addEventListener(completeListener);
        getDialog().addWindowListener(windowListener);

        final boolean okButtonPressed = showModal();

        editor.getCompleteSupport().removeEventListener(completeListener);
        getDialog().removeWindowListener(windowListener);

        return okButtonPressed;
    }
}
