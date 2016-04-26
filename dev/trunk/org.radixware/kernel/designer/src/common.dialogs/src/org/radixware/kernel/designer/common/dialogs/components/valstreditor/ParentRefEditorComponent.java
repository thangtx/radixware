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
package org.radixware.kernel.designer.common.dialogs.components.valstreditor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsIndexDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.types.Pid;
import org.radixware.kernel.designer.common.dialogs.RadixWareDesignerIcon;
import org.radixware.kernel.designer.common.dialogs.components.PropListValEditorPanel;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;

class ParentRefEditorComponent extends JPanel {

    private JTextField view;
    private JButton editBtn;
    private JButton resetBtn;
    private Pid value;
    private final DdsTableDef targetTable;
    private final ParentRefEditor parentEditor;

    public ParentRefEditorComponent(final ParentRefEditor parentEditor, final DdsTableDef targetTable) {
        this.targetTable = targetTable;
        this.parentEditor = parentEditor;
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 100;

        view = new JTextField();
        view.setEditable(false);
        c.fill = GridBagConstraints.HORIZONTAL;
        add(view, c);
        c.gridx++;
        c.weightx = 1;
        c.fill = GridBagConstraints.NONE;
        editBtn = new JButton("...");
        add(editBtn, c);
        c.gridx++;
        editBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final PropListValEditorPanel panel = new PropListValEditorPanel();

                List<Id> props = ParentRefUtils.getPidColumnIds(targetTable);
                Map<Id, ValAsStr> inputVals = new HashMap<>();
                String[] propVals = ParentRefUtils.getPidValues(value);
                int index = 0;

                for (Id id : props) {
                    if (index < propVals.length) {
                        if (propVals[index] == null || propVals[index].isEmpty()) {
                            continue;
                        }
                        inputVals.put(id, ValAsStr.Factory.loadFrom(propVals[index]));
                    }
                    index++;
                }

                panel.open(targetTable, props, inputVals);

                ModalDisplayer propEditorDialog = new ModalDisplayer(panel, "Reference") {
                    @Override
                    protected boolean canClose() {
                        final String[] message = new String[1];
                        if (!panel.isComplete(message)) {
                            if (message[0] != null) {
                                SwingUtilities.invokeLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        DialogUtils.messageError(message[0]);
                                    }
                                });
                            }
                            return false;
                        } else {
                            return true;
                        }
                    }
                };
                if (propEditorDialog.showModal()) {
                    Map<Id, ValAsStr> vals = panel.getPropValsById();
                    ArrayList<Object> keys = new ArrayList<>();
                    for (Id id : props) {
                        ValAsStr val = vals.get(id);
                        DdsColumnDef col = targetTable.getColumns().findById(id, ExtendableDefinitions.EScope.ALL).get();
                        if (val != null && col != null) {
                            keys.add(val.toObject(col.getValType()));
                        } else {
                            keys.add(null);
                        }
                    }
                    setValue(new Pid(targetTable.getId(), keys));
                    parentEditor.editor.setValue(ValAsStr.Factory.newInstance(value, EValType.PARENT_REF));
                }
            }
        });
        resetBtn = new JButton();
        resetBtn.setIcon(RadixWareDesignerIcon.DELETE.CLEAR.getIcon());
        resetBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setValue(null);
                parentEditor.editor.setValue(null);
            }
        });
        add(resetBtn, c);

    }

    public Pid getValue() {
        return value;
    }

    public void setValue(Pid pid) {
        this.value = pid;
        view.setText(ParentRefUtils.getPidDisplayName(targetTable, value));
    }
}
