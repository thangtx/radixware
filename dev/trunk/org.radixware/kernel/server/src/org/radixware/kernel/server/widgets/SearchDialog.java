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

package org.radixware.kernel.server.widgets;

import java.awt.Component;
import java.awt.Frame;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import java.util.ResourceBundle;
import java.util.regex.PatternSyntaxException;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.JCheckBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.KeyStroke;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;


public class SearchDialog {

    private static final long serialVersionUID = -3289341625056120129L;

    private static final class SearchComboBox extends JComboBox {

        private static final long serialVersionUID = 2497487852365430768L;

        private static final class SearchComboBoxModel extends DefaultComboBoxModel {

            private static final long serialVersionUID = 4825742569764694897L;

            @Override
            public Object getElementAt(int index) {
                return super.getElementAt(super.getSize() - index - 1);
            }
        }

        public SearchComboBox() {
            super();
            setModel(new SearchComboBoxModel());
            setMaximumRowCount(4);
            setEditable(true);
        }

        public void addItem(String item) {
            for (int i = 0; i < getItemCount(); i++) {
                if (item.equals(getItemAt(i))) {
                    removeItem(getItemAt(i));
                    break;
                }
            }
            super.addItem(item);
            setSelectedIndex(0);
        }
    }

    private final class SearchMachine {

        private final JTable table;
        private boolean isForward;
        private boolean isCaseSensitive;
        private boolean isRegex;
        private int prevFoundRow;
        private String prevSearchingString;

        public SearchMachine(JTable table) {
            this.table = table;
            isForward = true; //������ ��������� � forwardRadioButton.isSelected()
            isCaseSensitive = false;
            isRegex = false;
            prevFoundRow = -1;
            prevSearchingString = "";
        }

        public void find(String searchingString) {
            cbSearch.addItem(searchingString);
            if (table.getRowCount() == 0) {
                return;
            }
            if (!prevSearchingString.equals(searchingString)) {
                prevFoundRow = -1;
                prevSearchingString = searchingString;
            }
            int start = table.getSelectedRow() >= 0 ? table.getSelectedRow() : 0;
            if (isRegex) {
                searchingString = "(.)*" + searchingString + "(.)*";
                if (isForward) {
                    int r = start, end = table.getRowCount();
                    try {
                        for (r = start; r < end; r++) {
                            if (findRegexAtRow(searchingString, r) && r != prevFoundRow) {
                                prevFoundRow = r;
                                break;
                            }
                        }
                    } catch (PatternSyntaxException e) {
                        JOptionPane.showMessageDialog(dialog, e.getMessage(), Messages.MES_REGEX_ERROR, JOptionPane.ERROR_MESSAGE);
                        cbSearch.grabFocus();
                    }
                    if (r != end) {
                        table.firePropertyChange(table.getName(), -1, r);
                    } else {
                        JOptionPane.showMessageDialog(dialog, Messages.MES_NOT_FOUND + "\"" + cbSearch.getSelectedItem().toString() + "\"", Messages.TITLE_MESSAGE, JOptionPane.INFORMATION_MESSAGE);
                    }
                } else { // backward
                    int r = start;
                    try {
                        for (; r >= 0; r--) {
                            if (findRegexAtRow(searchingString, r) && r != prevFoundRow) {
                                prevFoundRow = r;
                                break;
                            }
                        }
                    } catch (PatternSyntaxException e) {
                        JOptionPane.showMessageDialog(dialog, e.getMessage(), Messages.MES_REGEX_ERROR, JOptionPane.ERROR_MESSAGE);
                        cbSearch.grabFocus();
                    }
                    if (r != -1) {
                        table.firePropertyChange(table.getName(), -1, r);
                    } else {
                        JOptionPane.showMessageDialog(dialog, Messages.MES_NOT_FOUND.toString() + "\"" + cbSearch.getSelectedItem().toString() + "\"", Messages.TITLE_MESSAGE, JOptionPane.INFORMATION_MESSAGE);
                    }
                }
                return;
            }
            if (!isCaseSensitive) {
                searchingString = searchingString.toLowerCase();
            }
            if (isForward) {
                int r, end = table.getRowCount();
                for (r = start; r < end; r++) {
                    if (findStringAtRow(searchingString, r) && r != prevFoundRow) {
                        prevFoundRow = r;
                        break;
                    }
                }
                if (r != end) {
                    table.firePropertyChange(table.getName(), -1, r);
                } else {
                    JOptionPane.showMessageDialog(dialog, Messages.MES_NOT_FOUND.toString() + "\"" + cbSearch.getSelectedItem().toString() + "\"", Messages.TITLE_MESSAGE, JOptionPane.INFORMATION_MESSAGE);
                }
            } else { // backward
                int r;
                for (r = start; r >= 0; r--) {
                    if (findStringAtRow(searchingString, r) && r != prevFoundRow) {
                        prevFoundRow = r;
                        break;
                    }
                }
                if (r != -1) {
                    table.firePropertyChange(table.getName(), -1, r);
                } else {
                    JOptionPane.showMessageDialog(dialog, Messages.MES_NOT_FOUND.toString() + "\"" + cbSearch.getSelectedItem().toString() + "\"", Messages.TITLE_MESSAGE.toString(), JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }

        private boolean findStringAtRow(String string, int row) {
            for (int c = 0; c < table.getColumnCount(); c++) {
                if (!isCaseSensitive && table.getValueAt(row, c).toString().toLowerCase().indexOf(string) >= 0) {
                    return true;
                } else if (table.getValueAt(row, c).toString().indexOf(string) >= 0) {
                    return true;
                }
            }
            return false;
        }

        private boolean findRegexAtRow(String regex, int row) throws PatternSyntaxException {
            for (int c = 0; c < table.getColumnCount(); c++) {
                if ((table.getValueAt(row, c).toString()).matches(regex)) {
                    return true;
                }
            }
            return false;
        }

        public void setForward(boolean val) {
            isForward = val;
        }

        public void setCaseSensitive(boolean val) {
            isCaseSensitive = val;
        }

        public void setRegex(boolean val) {
            isRegex = val;
        }
    }
    private JDialog dialog = null;
    private Object parent;
    private SearchComboBox cbSearch;
    private SearchMachine searchMachine;

    public SearchDialog(Object parent) {
        this.parent = parent;
    }

    private JPanel createSearchingStringPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.add(new JLabel(Messages.LBL_FIND, JLabel.RIGHT), BorderLayout.WEST);
        cbSearch = new SearchComboBox();
        cbSearch.setPreferredSize(new Dimension(300, 24));
        panel.add(cbSearch, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createDirectionRadioButtons() {
        JPanel panel = new JPanel();
        panel.setBorder(new CompoundBorder(new TitledBorder(null, Messages.TITLE_DIRECTION,
                TitledBorder.LEFT, TitledBorder.TOP), new EmptyBorder(0, 5, 5, 5)));
        panel.setLayout(new GridLayout(1, 2, 20, 0));
        ButtonGroup buttonGroup = new ButtonGroup();
        final JRadioButton forwardRadioButton = new JRadioButton(Messages.TITLE_FORWARD);
        forwardRadioButton.setSelected(true);
        forwardRadioButton.addChangeListener(
                new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                searchMachine.setForward(forwardRadioButton.isSelected());
            }
        });
        buttonGroup.add(forwardRadioButton);
        panel.add(forwardRadioButton);
        JRadioButton backwardRadioButton = new JRadioButton(Messages.TITLE_BACKWARD);
        buttonGroup.add(backwardRadioButton);
        panel.add(backwardRadioButton);
        return panel;
    }

    private JPanel createOptionCheckBoxes() {
        JPanel panel = new JPanel();
        panel.setBorder(new CompoundBorder(new TitledBorder(null, Messages.TITLE_OPTIONS,
                TitledBorder.LEFT, TitledBorder.TOP), new EmptyBorder(0, 5, 5, 5)));
        panel.setLayout(new GridLayout(2, 1, 0, 2));
        final JCheckBox caseCheckBox = new JCheckBox(Messages.TITLE_CASE_SENSITIVE);
        caseCheckBox.addChangeListener(
                new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                searchMachine.setCaseSensitive(caseCheckBox.isSelected());
            }
        });
        panel.add(caseCheckBox);
        final JCheckBox regexCheckBox = new JCheckBox(Messages.TITLE_REGEX);
        regexCheckBox.addChangeListener(
                new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                searchMachine.setRegex(regexCheckBox.isSelected());
                caseCheckBox.setEnabled(!regexCheckBox.isSelected());
            }
        });
        panel.add(regexCheckBox);
        return panel;
    }

    private JPanel createButtonBar() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBorder(new EmptyBorder(3, 0, 0, 0));
        JButton findButton = new JButton(Messages.BTN_FIND);
//                findButton.setPreferredSize(new Dimension(50, 24));
//                findButton.setMaximumSize(new Dimension(50, 24));
        findButton.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cbSearch.getSelectedItem() != null && !cbSearch.getSelectedItem().toString().equals("")) {
                    searchMachine.find(cbSearch.getSelectedItem().toString());
                }
            }
        });
        panel.add(findButton);
        return panel;
    }

    private JPanel createContentPane() {
        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(createSearchingStringPanel());
        panel.add(Box.createVerticalStrut(5));
        panel.add(createDirectionRadioButtons());
        panel.add(Box.createVerticalStrut(5));
        panel.add(createOptionCheckBoxes());
        panel.add(Box.createVerticalStrut(5));
        panel.add(createButtonBar());
        return panel;
    }

    public void show() {
        if (dialog == null) {
            searchMachine = new SearchMachine(((TraceView) parent).getTraceTable());
            while (parent != null && !(parent instanceof Frame)) {
                parent = ((Component) parent).getParent();
            }
            dialog = new JDialog((Frame) parent, Messages.TITLE_FIND, false);
            dialog.setContentPane(createContentPane());
            dialog.pack();
            dialog.setSize(300, dialog.getHeight());
            dialog.setLocationRelativeTo(null);
            dialog.setResizable(false);
            ActionListener actionListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    dialog.setVisible(false);
                }
            };
            KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
            dialog.getRootPane().registerKeyboardAction(actionListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
        }
        cbSearch.grabFocus();
        dialog.setVisible(true);
    }

    public void findNext() {
        if (dialog == null || cbSearch.getItemCount() == 0) {
            return;
        }
        searchMachine.find(cbSearch.getItemAt(0).toString());
    }

    private static final class Messages {

        static {
            final ResourceBundle bundle = ResourceBundle.getBundle("org.radixware.kernel.server.widgets.mess.messages");

            TITLE_FIND = bundle.getString("TITLE_FIND");
            LBL_FIND = bundle.getString("LBL_FIND");
            TITLE_DIRECTION = bundle.getString("TITLE_DIRECTION");
            TITLE_FORWARD = bundle.getString("TITLE_FORWARD");
            TITLE_BACKWARD = bundle.getString("TITLE_BACKWARD");
            TITLE_OPTIONS = bundle.getString("TITLE_OPTIONS");
            TITLE_CASE_SENSITIVE = bundle.getString("TITLE_CASE_SENSITIVE");
            TITLE_REGEX = bundle.getString("TITLE_REGEX");
            BTN_FIND = bundle.getString("BTN_FIND");
            //BTN_CLOSE = bundle.getString("BTN_CLOSE");
            MES_NOT_FOUND = bundle.getString("MES_NOT_FOUND");
            TITLE_MESSAGE = bundle.getString("TITLE_MESSAGE");
            MES_REGEX_ERROR = bundle.getString("MES_REGEX_ERROR");
        }
        static final String TITLE_FIND;
        static final String LBL_FIND;
        static final String TITLE_DIRECTION;
        static final String TITLE_FORWARD;
        static final String TITLE_BACKWARD;
        static final String TITLE_OPTIONS;
        static final String TITLE_CASE_SENSITIVE;
        static final String TITLE_REGEX;
        static final String BTN_FIND;
        //static final String BTN_CLOSE;
        static final String MES_NOT_FOUND;
        static final String TITLE_MESSAGE;
        static final String MES_REGEX_ERROR;
    }
}
