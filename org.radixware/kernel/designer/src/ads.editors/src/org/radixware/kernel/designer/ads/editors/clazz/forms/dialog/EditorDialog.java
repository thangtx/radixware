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
package org.radixware.kernel.designer.ads.editors.clazz.forms.dialog;

import javax.swing.JPanel;
import org.openide.DialogDescriptor;
import org.radixware.kernel.common.defs.ads.ui.AdsAbstractUIDef;
import org.radixware.kernel.common.defs.ads.ui.AdsUIItemDef;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;

public class EditorDialog extends ModalDisplayer {

    public abstract static class EditorPanel<T extends AdsUIItemDef> extends JPanel {

        final protected T widget;
        final protected AdsAbstractUIDef uiDef;

        public EditorPanel(AdsAbstractUIDef uiDef, T widget) {
            this.uiDef = uiDef;
            this.widget = widget;
        }

        public abstract void init();

        public abstract String getTitle();

        protected boolean isReadOnly() {
            return widget.isReadOnly() || AdsUIUtil.isReadOnlyNode(uiDef, widget);
        }

    }

    public EditorDialog(final EditorPanel panel) {
        super(panel, panel.getTitle());
        panel.init();
    }

    @Override
    public Object[] getOptions() {
        EditorPanel panel = (EditorPanel) getComponent();
        if (panel.widget.isReadOnly()) {
            return new Object[]{DialogDescriptor.CANCEL_OPTION};
        }
        return new Object[]{DialogDescriptor.CLOSED_OPTION};
    }

    @Override
    protected void apply() {
    }

    public static boolean execute(final EditorPanel panel) {
        new EditorDialog(panel).showModal();
        return !panel.widget.isReadOnly();
    }
}
