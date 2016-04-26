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
package org.radixware.kernel.designer.environment.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Action;
import org.openide.util.Lookup;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.environment.merge.AbstractMergeChangesOptions;
import org.radixware.kernel.designer.environment.merge.ChooseMergeBranchDlg;
import org.radixware.kernel.designer.environment.merge.MergeUtils;

public class MergeChangesToBranchAction extends AbstractContextAwareAction implements ActionListener {

    //private boolean useLocaRepositary;
    ChooseMergeBranchDlg chooseMergeBranchDlg = null;

    private void doAction() throws Exception {

        if (!isEnabled()) {
            return;
        }
        MergeUtils.doWithDefinitions();
        AbstractMergeChangesOptions.freeSVNRepositoryOptions();
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        try {
            doAction();
        } catch (Exception ex) {
            DialogUtils.messageError(ex);
        }
    }

    @Override
    public Action createContextAwareInstance(final Lookup lkp) {
        return null;//new MergeChangesToBranchAction();
    }

    @Override
    public Object getValue(final String key) {
        if ("Name".equals(key)) {
            return "Merge Changes ...";
//            if (useLocaRepositary)                
//                return "Merge Changes to Local Branch";
//            return "Merge Changes to Subversion Branch";
        } else {
            return super.getValue(key);
        }
    }

    public MergeChangesToBranchAction() {
    }

    @Override
    public boolean isEnabled() {
        return MergeUtils.canMergeAds() || MergeUtils.canMergeDds();
    }

}
