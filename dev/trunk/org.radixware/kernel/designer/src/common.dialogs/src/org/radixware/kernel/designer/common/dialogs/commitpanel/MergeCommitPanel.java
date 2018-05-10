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
package org.radixware.kernel.designer.common.dialogs.commitpanel;

import java.awt.BorderLayout;
import static javax.swing.BoxLayout.Y_AXIS;
import javax.swing.*;

public class MergeCommitPanel extends JPanel {

    private MicroCommitPanel microPanel = new MicroCommitPanel();
    private JTextPane jTextPane = new JTextPane();

    public MergeCommitPanel() {

        setLayout(new BoxLayout(this, Y_AXIS));
        add(microPanel);
        //microPanel.addAndSetMergeMode();

        JPanel p = new JPanel();
        p.setLayout(new BorderLayout());
        p.add(new JLabel("Commit message:"), BorderLayout.NORTH);

        JScrollPane sp1 = new JScrollPane();
        sp1.setViewportView(jTextPane);
        p.add(sp1, BorderLayout.CENTER);

        add(p);
    }

    public String getMessage() {
        return microPanel.getCommitMessageEx(jTextPane.getText());
    }

    public String getSimpleMessage() {
        return jTextPane.getText();
    }

    public void setText(String text) {
        jTextPane.setText(text);
    }

    public void saveConfigurationOptions() {
        microPanel.saveConfigurationOptions();
        MicroCommitPanel.setLastCommitMessage(jTextPane.getText());
    }
}
