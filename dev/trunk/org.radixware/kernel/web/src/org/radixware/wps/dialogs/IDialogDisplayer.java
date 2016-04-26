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

package org.radixware.wps.dialogs;

import org.radixware.kernel.common.client.views.IDialog.DialogResult;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.rwt.Dialog;
import org.radixware.wps.rwt.RootPanel;


public interface IDialogDisplayer {

    public DialogResult execModal(Dialog dialog);

    public void showModal(Dialog dialog);

    //public void updateState(WaitPanel waiter);
    public RootPanel getRootPanel();

    public WpsEnvironment getEnvironment();

    public void processEvents();
}
