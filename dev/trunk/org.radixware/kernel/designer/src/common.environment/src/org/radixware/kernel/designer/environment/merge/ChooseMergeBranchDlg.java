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

import javax.swing.JComponent;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;

public class ChooseMergeBranchDlg extends ModalDisplayer {

    @Override
    public void apply() {
    }

    @Override
    public boolean showModal() {

        return super.showModal();
    }
    JComponent component;

    public ChooseMergeBranchDlg(JComponent cmp) {
        super(cmp, "Choose Branch");
        this.component = cmp;

    }
}
