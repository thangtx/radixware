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

import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.designer.common.dialogs.commitpanel.MicroCommitPanel;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;

class MergeCommitDialog extends ModalDisplayer {

    private MergeCommitDialog(MergeCommitPanel s, Definition def) {
        super(s, "Commit - " + def.getQualifiedName());
        s.setText(MicroCommitPanel.getLastCommitMessage());
    }

    private MergeCommitDialog(MergeCommitPanel s, String title, String text) {
        super(s, "Commit - " + title);
        s.setText(text);
    }

    public static String showCommitDialog(Definition def) {
        MergeCommitPanel panel = new MergeCommitPanel();
        MergeCommitDialog dlg = new MergeCommitDialog(panel, def);
        if (!dlg.showModal()) {
            return null;
        }
        panel.saveConfigurationOptions();
        return panel.getMessage();
    }

    public static String showCommitDialog(String title, String text) {
        MergeCommitPanel panel = new MergeCommitPanel();
        MergeCommitDialog dlg = new MergeCommitDialog(panel, title, text);
        if (!dlg.showModal()) {
            return null;
        }
        panel.saveConfigurationOptions();
        return panel.getMessage();
    }
}
