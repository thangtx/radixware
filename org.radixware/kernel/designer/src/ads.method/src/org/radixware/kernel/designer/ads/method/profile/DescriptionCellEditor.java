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

package org.radixware.kernel.designer.ads.method.profile;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.table.TableCellEditor;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.common.dialogs.components.DescriptionPanel;
import org.radixware.kernel.designer.common.dialogs.components.description.DescriptionEditor;
import org.radixware.kernel.designer.common.dialogs.components.description.DescriptionModel;
import org.radixware.kernel.designer.common.dialogs.components.values.EditorLayout;


public class DescriptionCellEditor extends AbstractCellEditor implements TableCellEditor {

    private JPanel editor = new JPanel(new EditorLayout());
    private DescriptionEditor descriptionEditor = new DescriptionPanel();
    private final JTextField textField = new JTextField();

    public DescriptionCellEditor() {
        final JButton btnChoose = new JButton(RadixWareIcons.DIALOG.CHOOSE.getIcon());
        btnChoose.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                edit();
            }
        });
        textField.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() > 1) {
                    edit();
                }
            }
        });
        textField.setEditable(false);
        editor.add(textField, EditorLayout.LEADER_CONSTRAINT);
        editor.add(btnChoose);
    }

    @Override
    public Object getCellEditorValue() {
        return descriptionEditor;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (isSelected) {
            descriptionEditor.open((DescriptionModel) value);

            textField.setText(value.toString());
            return editor;
        }
        return null;
    }

    private void edit() {
        if (DescriptionEditor.showModal(descriptionEditor)) {
            descriptionEditor.commit();
            stopCellEditing();
        } else {
            cancelCellEditing();
        }
    }
}
