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
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import net.miginfocom.swing.MigLayout;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.common.defs.utils.changelog.ChangeLog;
import org.radixware.kernel.designer.common.dialogs.components.state.StateAbstractDialog;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;

/**
 *
 * @author npopov
 */
public class ChangeLogEditorPanel extends JPanel {

    private final static String[] colNames = {"Revision Number", "Created", "Created by", "Reference Document", "Description", "Id"};
    private final int ID_COL_INDEX = colNames.length - 1;
    final static String VALUE_NOT_DEFINED = "<not defined>";
    
    private transient final List<ChangeLog.ChangeLogItem> items;
    private transient final ChangeLogMainPanel mainPanel;

    public ChangeLogEditorPanel(ChangeLog changeLog) {
        this.items = changeLog.getItems();
        this.mainPanel = new ChangeLogMainPanel(changeLog.getComments(), changeLog.getLocalNotes());
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        JTabbedPane tabs = new JTabbedPane();
        tabs.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                mainPanel.update();
            }
        });
        tabs.addTab("Main", mainPanel);
        tabs.addTab("Revisions", createRevisionsPanel());
        add(tabs, BorderLayout.CENTER);
    }

    public ChangeLog getChangeLog() {
        return ChangeLog.Factory.newInstance(mainPanel.getComments(), mainPanel.getLocalNotes(), items);
    }

    private JPanel createRevisionsPanel() {
        final DefaultTableModel model = new DefaultTableModel(colNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        final JTable changeLogTable = new JTable(model);
        changeLogTable.getTableHeader().setReorderingAllowed(false);
        changeLogTable.removeColumn(changeLogTable.getColumn(colNames[ID_COL_INDEX]));

        fillChangeLogTable(changeLogTable, items);

        JScrollPane tablePane = new JScrollPane(changeLogTable);

        final JButton editChangeLogEntryBtn = new JButton("Edit", RadixWareIcons.EDIT.EDIT.getIcon());
        editChangeLogEntryBtn.setHorizontalAlignment(SwingConstants.LEFT);
        editChangeLogEntryBtn.setEnabled(false);
        editChangeLogEntryBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final int selectedIndex = changeLogTable.getSelectedRow();
                if (selectedIndex < 0) {
                    return;
                }

                final long revId = (Long) model.getValueAt(selectedIndex, ID_COL_INDEX);
                final ChangeLog.ChangeLogItem item = ChangeLog.Utils.getItemById(revId, items);
                if (item == null) {
                    return;
                }
                final ChangeLogItemEditorPanel panel = new ChangeLogItemEditorPanel(item);
                StateAbstractDialog displayer = new StateAbstractDialog(panel, "Edit Revision") {
                    @Override
                    protected void apply() {
                        for (int index = 0; index < items.size(); index++) {
                            if (items.get(index).getId() == revId) {
                                items.set(index, panel.getResultItem());
                                break;
                            }
                        }
                        ChangeLog.Utils.sortItems(items);
                        fillChangeLogTable(changeLogTable, items);
                        changeLogTable.setRowSelectionInterval(selectedIndex, selectedIndex);
                    }
                };

                displayer.showModal();
            }
        });

        final JButton removeChangeLogEntryBtn = new JButton("Remove", RadixWareIcons.DELETE.DELETE.getIcon());
        removeChangeLogEntryBtn.setHorizontalAlignment(SwingConstants.LEFT);
        removeChangeLogEntryBtn.setEnabled(false);
        removeChangeLogEntryBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] selectedItems = changeLogTable.getSelectedRows();
                if (selectedItems.length > 0) {
                    boolean doDelete = DialogUtils.messageConfirmation("Delete selected revisions?");
                    if (!doDelete) {
                        return;
                    }
                    
                    long[] revIndexes = new long[selectedItems.length];
                    for (int selIdx = 0; selIdx < selectedItems.length; selIdx++) {
                        revIndexes[selIdx] = (Long) model.getValueAt(selectedItems[selIdx], ID_COL_INDEX);
                    }
                    for (int revIdx = 0; revIdx < revIndexes.length; revIdx++) {
                        for (int itemIdx = 0; itemIdx < items.size(); itemIdx++) {
                            if (items.get(itemIdx).getId() == revIndexes[revIdx]) {
                                items.remove(itemIdx);
                                break;
                            }
                        }
                    }
                    fillChangeLogTable(changeLogTable, items);
                    if (!items.isEmpty()) {
                        changeLogTable.setRowSelectionInterval(0, 0);
                    }
                }
            }
        });

        JButton addChangeLogEntryBtn = new JButton("Create", RadixWareIcons.CREATE.ADD.getIcon());
        addChangeLogEntryBtn.setHorizontalAlignment(SwingConstants.LEFT);
        addChangeLogEntryBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final ChangeLogItemEditorPanel panel = new ChangeLogItemEditorPanel(ChangeLog.Utils.getNextRevisionNum(items));
                StateAbstractDialog displayer = new StateAbstractDialog(panel, "Create Revision") {

                    @Override
                    protected void apply() {
                        items.add(panel.getResultItem());
                        ChangeLog.Utils.sortItems(items);
                        fillChangeLogTable(changeLogTable, items);
                        changeLogTable.setRowSelectionInterval(0, 0);
                    }
                };

                displayer.showModal();
            }
        });
        
        changeLogTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                final boolean thereIsSelectedRows = changeLogTable.getSelectedRowCount() > 0;
                editChangeLogEntryBtn.setEnabled(thereIsSelectedRows);
                removeChangeLogEntryBtn.setEnabled(thereIsSelectedRows);
            }
        });
        if (!items.isEmpty()) {
            changeLogTable.setRowSelectionInterval(0, 0);
        }
        
        JPanel revisionsPanel = new JPanel(new MigLayout("fill", "[grow][shrink]", "[][][][]"));
        revisionsPanel.setBorder(new EmptyBorder(7, 7, 7, 7));
        revisionsPanel.add(tablePane, "grow, cell 0 0 1 4");
        revisionsPanel.add(addChangeLogEntryBtn, "cell 1 0, growx, wrap");
        revisionsPanel.add(removeChangeLogEntryBtn, "cell 1 1, growx, wrap");
        revisionsPanel.add(editChangeLogEntryBtn, "cell 1 2, growx, wrap push");

        return revisionsPanel;
    }

    private void fillChangeLogTable(JTable changeLogTable, List<ChangeLog.ChangeLogItem> items) {
        final DefaultTableModel model = (DefaultTableModel) changeLogTable.getModel();
        for (int i = model.getRowCount() - 1; i >= 0; i--) {
            model.removeRow(i);
        }

        for (ChangeLog.ChangeLogItem item : items) {
            String dateAsStr = ChangeLog.Utils.formatDate(item.getDate());
            model.addRow(new Object[]{item.getRevisionNumber(), dateAsStr, item.getAuthor(), item.getRefDoc(), item.getDescription(), item.getId()});
        }
    }

    private final class ChangeLogMainPanel extends JPanel {

        private final JTextField lastRevNum = new JTextField();
        private final JTextField lastRevDate = new JTextField();
        private final JTextField lastRevAuthor = new JTextField();
        private final JTextArea commentsArea = new JTextArea();
        private final JTextArea localNotesArea = new JTextArea();

        public ChangeLogMainPanel(final String comments, final String localNotes) {
            initComponents(comments, localNotes);
        }

        private void initComponents(String comments, String localNotes) {
            setLayout(new MigLayout("fill", "[shrink][grow]", 
                    "[shrink][shrink][shrink][shrink][shrink][top, grow][top, grow]"));
            setBorder(new EmptyBorder(7, 7, 7, 7));

            lastRevNum.setEditable(false);
            add(new JLabel("Last revision number:"));
            add(lastRevNum, "growx, wrap");

            lastRevDate.setEditable(false);
            add(new JLabel("Last revision updated:"));
            add(lastRevDate, "growx, wrap");

            lastRevAuthor.setEditable(false);
            add(new JLabel("Last revision updated by:"));
            add(lastRevAuthor, "growx, wrap");

            add(new JLabel("Comments:"));
            add(createScrolledTextArea(commentsArea), "growx, wrap");

            add(new JLabel("Local notes:"));
            add(createScrolledTextArea(localNotesArea), "growx, wrap");

            commentsArea.setText(comments);
            localNotesArea.setText(localNotes);
            update();
        }

        public void update() {
            ChangeLog.ChangeLogItem lastRev = ChangeLog.Utils.getLastRevision(items);
            if (lastRev != null) {
                if (lastRev.getRevisionNumber() != null) {
                    lastRevNum.setText(lastRev.getRevisionNumber().toString());
                } else {
                    lastRevNum.setText(VALUE_NOT_DEFINED);
                }
                lastRevDate.setText(ChangeLog.Utils.formatDate(lastRev.getDate()));
                lastRevAuthor.setText(lastRev.getAuthor());
            } else {
                lastRevNum.setText(VALUE_NOT_DEFINED);
                lastRevDate.setText(VALUE_NOT_DEFINED);
                lastRevAuthor.setText(VALUE_NOT_DEFINED);
            }
        }

        public String getComments() {
            return commentsArea.getText();
        }

        public String getLocalNotes() {
            return localNotesArea.getText();
        }
    }
    
    static JScrollPane createScrolledTextArea(JTextArea area) {
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(area);
        scrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(250, 250));
        return scrollPane;
    }
}
