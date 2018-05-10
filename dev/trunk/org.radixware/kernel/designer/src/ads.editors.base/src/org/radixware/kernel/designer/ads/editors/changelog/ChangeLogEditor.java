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
package org.radixware.kernel.designer.ads.editors.changelog;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.radixware.kernel.common.components.ExtendableTextField;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.common.defs.utils.changelog.ChangeLog;
import org.radixware.kernel.common.defs.utils.changelog.IChangeLogOwner;
import org.radixware.kernel.designer.common.dialogs.components.state.StateAbstractDialog;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;

/**
 *
 * @author npopov
 */
public class ChangeLogEditor extends ExtendableTextField {

    private transient ChangeLog changeLog;
    private transient IChangeLogOwner owner;
    private final static String CHANGE_LOG_NOT_AVAILABLE = "Change log not available";

    public static JPanel wrapPanelWithLabel(ChangeLogEditor editor) {
        final JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("Change log: "), BorderLayout.WEST);
        panel.add(editor, BorderLayout.CENTER);
        return panel;
    }

    public ChangeLogEditor(IChangeLogOwner owner) {
        super(true);
        if (owner == null) {
            throw new NullPointerException("Error on create change log editor. Change log owner is null");
        }
        this.owner = owner;
        initComponents();
        update();
    }

    private void initComponents() {
        final JButton createRevBtn = addButton(RadixWareIcons.CREATE.ADD.getIcon(13, 13));
        createRevBtn.setToolTipText("Create revision");
        createRevBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                final long nextRevNum = ChangeLog.Utils.getNextRevisionNum(changeLog.getItems());
                final ChangeLogItemEditorPanel panel = new ChangeLogItemEditorPanel(nextRevNum);
                StateAbstractDialog displayer = new StateAbstractDialog(panel, "Create Revision") {
                    @Override
                    protected void apply() {
                        final List<ChangeLog.ChangeLogItem> items = changeLog.getItems();
                        items.add(panel.getResultItem());
                        owner.setChangeLog(ChangeLog.Factory.newInstance(
                                changeLog.getComments(),
                                changeLog.getLocalNotes(),
                                items));
                        update();
                    }
                };

                displayer.showModal();
            }
        });

        final JButton editChangelogBtn = addButton(RadixWareIcons.EDIT.EDIT.getIcon(13, 13));
        editChangelogBtn.setToolTipText("Edit change log");
        editChangelogBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                final ChangeLogEditorPanel panel = new ChangeLogEditorPanel(changeLog);
                ModalDisplayer displayer = new ModalDisplayer(panel, "Edit Change Log") {
                    @Override
                    protected void apply() {
                        owner.setChangeLog(panel.getChangeLog());
                        update();
                    }
                };

                displayer.showModal();
            }
        });
    }

    private void setChangeLogValue(ChangeLog log) {
        this.changeLog = log;
        if (changeLog == null) {
            setTextFieldValue(CHANGE_LOG_NOT_AVAILABLE);
            setReadOnly(true);
        } else {
            setTextFieldValue(changeLog.getPresentationString());
            setReadOnly(false);
        }
    }

    public void update() {
        setChangeLogValue(owner.getChangeLog());
    }

    public void update(IChangeLogOwner owner) {
        this.owner = owner;
        update();
    }
}
