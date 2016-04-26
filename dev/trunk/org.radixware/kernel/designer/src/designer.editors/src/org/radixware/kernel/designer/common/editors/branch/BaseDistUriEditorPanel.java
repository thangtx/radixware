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

package org.radixware.kernel.designer.common.editors.branch;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JTextField;
import org.apache.commons.lang.StringUtils;
import org.radixware.kernel.designer.common.dialogs.components.AdvancePanel;
import org.radixware.kernel.designer.common.dialogs.components.values.EditorLayout;


final class BaseDistUriEditorPanel extends AdvancePanel {

    private final JTextField editor = new JTextField();
    private final JButton button = new JButton("...");
    private BaseDistUriManager manager;

    public BaseDistUriEditorPanel() {

        setLayout(new EditorLayout());
        add(editor, EditorLayout.LEADER_CONSTRAINT);
        add(button);

        editor.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    edit();
                }
            }

        });

        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                edit();
            }
        });

        editor.setEditable(false);
    }

    public void open(BaseDistUriManager manager) {
        this.manager = manager;
        update();
    }

    private void update() {
        editor.setText(StringUtils.join(manager.getModel().getBaseDistrUris(), ", "));
    }

    private void edit() {
        BaseDistUriEditor.edit(manager);
        update();
    }
}
