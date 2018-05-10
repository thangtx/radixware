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

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import net.miginfocom.swing.MigLayout;
import org.radixware.kernel.common.defs.utils.changelog.ChangeLog;
import org.radixware.kernel.common.enums.EChangelogItemKind;
import org.radixware.kernel.common.userreport.common.UserExtensionManagerCommon;
import org.radixware.kernel.designer.common.dialogs.components.state.StateAbstractDialog;
import org.radixware.kernel.designer.common.dialogs.components.state.StateDisplayer;

/**
 *
 * @author npopov
 */
final class ChangeLogItemEditorPanel extends StateAbstractDialog.StateAbstractPanel {

    private final ChangeLog.ChangeLogItem item;
    private final long nextRevNum;
    private final long date;
    private final EChangelogItemKind kind;
    private JTextField revNumEditor;
    private JTextField dateEditor;
    private JTextField authorEditor;
    private JTextArea descEditor;
    private JTextField refDocEditor;
    private JTextField appVerEditor;
    private JTextField localNotesEditor;
    private StateDisplayer stateDisplayer;
    
    
    public ChangeLogItemEditorPanel(final long nextRevNum) {
        item = null;
        date = System.currentTimeMillis();
        kind = EChangelogItemKind.MODIFY;
        this.nextRevNum = nextRevNum;
        initComponents();
    }

    public ChangeLogItemEditorPanel(ChangeLog.ChangeLogItem item) {
        if (item == null) {
            throw new NullPointerException("Change log item is null");
        }
        this.item = item;
        date = item.getDate();
        kind = item.getKind();
        nextRevNum = -1;
        initComponents();
    }

    private void initComponents() {
        final JLabel revNumLabel = new JLabel("Revison number:");
        revNumEditor = new JTextField();
        revNumEditor.setEditable(false);
        final JLabel dateLabel = new JLabel("Created:");
        dateEditor = new JTextField();
        dateEditor.setEditable(false);
        final JLabel authorLabel = new JLabel("Created by:");
        authorEditor = new JTextField();

        final JLabel descLabel = new JLabel("Description:");
        descEditor = new JTextArea();
        descEditor.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                check();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                check();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                check();
            }
        });
        JScrollPane descPane = ChangeLogEditorPanel.createScrolledTextArea(descEditor);

        final JLabel refDocLabel = new JLabel("Reference document:");
        refDocEditor = new JTextField();
        final JLabel appVerLabel = new JLabel("Application version:");
        appVerEditor = new JTextField();
        appVerEditor.setEditable(false);
        final JLabel localNotesLabel = new JLabel("Local notes:");
        localNotesEditor = new JTextField();

        stateDisplayer = new StateDisplayer();

        if (item != null) {
            if (item.getRevisionNumber() != null) {
                revNumEditor.setText(item.getRevisionNumber().toString());
            } else {
                revNumEditor.setText(ChangeLogEditorPanel.VALUE_NOT_DEFINED);
            }
            dateEditor.setText(ChangeLog.Utils.formatDate(item.getDate()));
            authorEditor.setText(item.getAuthor());
            descEditor.setText(item.getDescription());
            refDocEditor.setText(item.getRefDoc());
            appVerEditor.setText(item.getAppVer());
            localNotesEditor.setText(item.getLocalNotes());
        } else {
            revNumEditor.setText(Long.toString(nextRevNum));
            dateEditor.setText(ChangeLog.Utils.formatDate(System.currentTimeMillis()));
            authorEditor.setText(System.getProperty("user.name"));
            appVerEditor.setText(UserExtensionManagerCommon.getInstance().getLayerVersionsString());
        }

        setLayout(new MigLayout("fill", "[shrink][grow]",
                "[shrink][shrink][shrink][top, grow][shrink][shrink][shrink]"));
        setBorder(new EmptyBorder(7, 7, 7, 7));
        add(revNumLabel);
        add(revNumEditor, "growx, wrap");
        add(authorLabel);
        add(authorEditor, "growx, wrap");
        add(dateLabel);
        add(dateEditor, "growx, wrap");
        add(descLabel);
        add(descPane, "growx, growy, wrap");
        add(refDocLabel);
        add(refDocEditor, "growx, wrap");
        add(appVerLabel);
        add(appVerEditor, "growx, wrap");
        add(localNotesLabel);
        add(localNotesEditor, "growx, wrap");
        add(stateDisplayer, "growx, wrap");

        check();
    }

    public ChangeLog.ChangeLogItem getResultItem() {
        final Long revNum;
        if (ChangeLogEditorPanel.VALUE_NOT_DEFINED.equals(revNumEditor.getText())) {
            revNum = null;
        } else {
            revNum = Long.valueOf(revNumEditor.getText());
        }
        return ChangeLog.ChangeLogItem.Factory.newInstance(
                revNum, date,
                getStringFromTextField(authorEditor.getText()),
                getStringFromTextField(descEditor.getText()),
                getStringFromTextField(refDocEditor.getText()),
                getStringFromTextField(appVerEditor.getText()),
                getStringFromTextField(localNotesEditor.getText()),
                kind);
    }

    @Override
    public void check() {
        if (descEditor.getText() == null || descEditor.getText().isEmpty()) {
            stateManager.error("Description is empty");
        } else {
            stateManager.ok();
        }
        changeSupport.fireChange();
    }

    private String getStringFromTextField(final String str) {
        return "".equals(str) ? null : str;
    }
}
