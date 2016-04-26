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

package org.radixware.kernel.common.design.msdleditor;

import java.util.ArrayList;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle;


public class DefaultLayout {
    public static void doLayout(JPanel parent, ArrayList<JLabel> labels, ArrayList<JComponent> editors, boolean upperGap) {
        GroupLayout layout = new GroupLayout(parent);
        parent.setLayout(layout);

        ParallelGroup labelsGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING, false);
        for (JLabel cur : labels) {
            cur.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            if (!cur.getText().endsWith(":")) {
                cur.setText(cur.getText() + ":");
            }
            labelsGroup = labelsGroup.addComponent(cur, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);
        }
        ParallelGroup editorsGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        for (JComponent cur : editors)
            editorsGroup = editorsGroup.addComponent(cur, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(labelsGroup)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(editorsGroup)
                .addContainerGap())
        );
        SequentialGroup group = layout.createSequentialGroup();
        if (upperGap) {
            group = group.addContainerGap();
        }
        for (int i=0; i<labels.size(); i++) {
            group = group.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addComponent(labels.get(i), GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(editors.get(i), GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
            group = group.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
        }
        group = group.addContainerGap();
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(group)
        );

    }
}
