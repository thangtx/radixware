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

import javax.swing.JPanel;
import org.openide.DialogDescriptor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;

/**
 * Dialog which displays something.
 */
public abstract class EditorDialog extends ModalDisplayer {

    public abstract static class EditorPanel<N> extends JPanel {

        protected N obj;

        public EditorPanel(N obj) {
            this.obj = obj;
        }

        public abstract void init();
        public void activate() {}
        public abstract void apply();
        public abstract String getTitle();
        public RadixIcon getIcon() {
            return null;
        }
    }

    private EditorDialog(final EditorPanel panel) {
        super(panel, panel.getTitle());
        panel.init();
    }

    @Override
    public Object[] getOptions() {
        EditorPanel panel = (EditorPanel)getComponent();
        Object o = panel.obj;
        if (o instanceof RadixObject && ((RadixObject)o).isReadOnly()) {
            return new Object[] { DialogDescriptor.CANCEL_OPTION };
        }
        return getResultOptions();
    }

    abstract public Object[] getResultOptions();

    public static boolean execute(final EditorPanel panel) {
        new EditorDialog(panel) {
            @Override
            public Object[] getResultOptions() {
                return new Object[] { DialogDescriptor.CLOSED_OPTION };
            }
            @Override
            public void onClosing() {
                ((EditorPanel)getComponent()).apply();
            }
        }.showModal();
        return true;
    }

    public static boolean executeOkCancel(final EditorPanel panel) {
        return new EditorDialog(panel) {
            @Override
            public Object[] getResultOptions() {
                return new Object[] { DialogDescriptor.OK_OPTION,  DialogDescriptor.CANCEL_OPTION };
            }
            @Override
            public void apply() {
                ((EditorPanel)getComponent()).apply();
            }
        }.showModal();
    }

}
