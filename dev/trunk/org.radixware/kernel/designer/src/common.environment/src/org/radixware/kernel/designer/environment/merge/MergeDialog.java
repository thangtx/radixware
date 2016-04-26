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
package org.radixware.kernel.designer.environment.merge;

import org.netbeans.api.progress.ProgressUtils;
import org.openide.DialogDescriptor;
import org.openide.util.Exceptions;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;

public class MergeDialog extends ModalDisplayer {

    MergePanel panel;

    @Override
    public void apply() {
    }

    @Override
    public Object[] getOptions() {
        return new Object[]{DialogDescriptor.CLOSED_OPTION};
    }

    @Override
    public boolean showModal() {
        return super.showModal();
    }

    public MergeDialog(final MergePanel panel) {
        super(panel, "Merge Changes");

        final MergeDialog dlg = this;
        this.panel = panel;
        panel.setDialog(dlg);

    }

    @Override
    protected void beforeShow() {
        panel.beforeShow();
    }

    public MergePanel getPanel() {
        return panel;
    }
}
