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

package org.radixware.kernel.designer.environment.upload;

import java.beans.PropertyChangeListener;
import java.io.IOException;
import javax.swing.JCheckBox;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ui.OpenProjects;
import org.openide.awt.StatusDisplayer;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;


class DefaultRadixObjectUploaderUI extends AbstractRadixObjectUploaderUI {

    private static boolean ignoreAll = false;

    @Override
    public void logStatus(String status) {
        StatusDisplayer.getDefault().setStatusText(status);
    }

    @Override
    public boolean confirmRetry(String message, IOException cause) {
        if (ignoreAll) {
            return false;
        }
        final JCheckBox checkbox = new JCheckBox();
        checkbox.setSelected(true);
        checkbox.setText("Show this dialog again");
        final boolean result = DialogUtils.messageConfirmation(message, cause, checkbox);
        ignoreAll = !checkbox.isSelected();
        return result;
    }

    @Override
    public boolean isIgnoreAll() {
        return ignoreAll;
    }

    @Override
    public void resetIgnoreAll() {
        ignoreAll = false;
    }

    @Override
    public boolean confirmClose(String message) {
        return DialogUtils.messageConfirmation(message);
    }

    @Override
    public boolean confirmReload(String message) {
        return DialogUtils.messageConfirmation(message);
    }

    @Override
    public void closeProject(Branch branch) {
        for (Project project : OpenProjects.getDefault().getOpenProjects()) {
            final Branch projectBranch = project.getLookup().lookup(Branch.class);
            if (branch == projectBranch) {
                OpenProjects.getDefault().close(new Project[]{project});
                break;
            }
        }
    }

    @Override
    public void addOpenProjectsChangedListener(PropertyChangeListener openProjectsListener) {
        OpenProjects.getDefault().addPropertyChangeListener(openProjectsListener);
    }
}
