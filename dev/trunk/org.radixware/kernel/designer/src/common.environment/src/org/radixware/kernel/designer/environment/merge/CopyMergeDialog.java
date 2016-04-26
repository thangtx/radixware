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

import org.openide.DialogDescriptor;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;

public class CopyMergeDialog extends ModalDisplayer {

    @Override
    public void apply() {
    }

    @Override
    public Object[] getOptions() {
        return new Object[]{DialogDescriptor.CLOSED_OPTION};
    }

    @Override
    public boolean showModal() {
        final boolean rez = super.showModal();
        if (this.getComponent() instanceof DdsCopyMergePanel){
            final DdsCopyMergePanel panel = (DdsCopyMergePanel) this.getComponent();
            final DdsMergeChangesOptions options = panel.getCurrOptions();
            options.captureDestinationModules();
        }
        return rez;
    }

    public CopyMergeDialog(final AdsMergeChangesOptions options) {
        super(new AdsCopyMergePanel(options), "Merge Changes from \'" + options.getFromBranchShortName() + "' to '" + options.getToBranchShortName() + "\'");

        final AdsCopyMergePanel panel = (AdsCopyMergePanel) this.getComponent();
        
        panel.setDialog(this);
    }

    public CopyMergeDialog(final DdsMergeChangesOptions options) {
        super(new DdsCopyMergePanel(options), "Merge Changes from \'" + options.getFromBranchShortName() + "' to '" + options.getToBranchShortName() + "\'");

        final DdsCopyMergePanel panel = (DdsCopyMergePanel) this.getComponent();
        panel.setDialog(this);        
        
    }

}
