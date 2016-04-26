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

/*
 * FindAndReplacePanel.java
 *
 * Created on Apr 17, 2012, 2:26:15 PM
 */
package org.radixware.kernel.designer.common.dialogs.findsupport.visual;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import org.netbeans.editor.Utilities;
import org.openide.util.Exceptions;
import org.radixware.kernel.common.utils.PropertyStore;
import org.radixware.kernel.designer.common.dialogs.findsupport.AbstractFinder.Options;
import org.radixware.kernel.designer.common.dialogs.findsupport.FindResult;
import org.radixware.kernel.designer.common.dialogs.findsupport.FinderFactory;
import org.radixware.kernel.designer.common.dialogs.findsupport.IFinder;


final class FindAndReplacePanel extends JPanel implements ActionListener {

    public FindAndReplacePanel() {
        initComponents();
        initButtons();

        // not implemented yet
        chbHighlightResult.setEnabled(false);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        lblFindWhat = new javax.swing.JLabel();
        lblReplaceWith = new javax.swing.JLabel();
        cmbFindWhat = new javax.swing.JComboBox();
        cmbReplaceWith = new javax.swing.JComboBox();
        chbHighlightResult = new javax.swing.JCheckBox();
        chbIncrementalSearch = new javax.swing.JCheckBox();
        chbMatchCase = new javax.swing.JCheckBox();
        chbWrapAround = new javax.swing.JCheckBox();
        chbWholeWords = new javax.swing.JCheckBox();
        chbSearchSelection = new javax.swing.JCheckBox();
        chbRegExp = new javax.swing.JCheckBox();
        chbSearchBackwards = new javax.swing.JCheckBox();

        setLayout(new java.awt.GridBagLayout());

        lblFindWhat.setText(org.openide.util.NbBundle.getMessage(FindAndReplacePanel.class, "FindAndReplacePanel.lblFindWhat.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 0, 0);
        add(lblFindWhat, gridBagConstraints);

        lblReplaceWith.setText(org.openide.util.NbBundle.getMessage(FindAndReplacePanel.class, "FindAndReplacePanel.lblReplaceWith.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(6, 8, 0, 0);
        add(lblReplaceWith, gridBagConstraints);

        cmbFindWhat.setEditable(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 0, 8);
        add(cmbFindWhat, gridBagConstraints);

        cmbReplaceWith.setEditable(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(6, 8, 0, 8);
        add(cmbReplaceWith, gridBagConstraints);

        chbHighlightResult.setText(org.openide.util.NbBundle.getMessage(FindAndReplacePanel.class, "FindAndReplacePanel.chbHighlightResult.text")); // NOI18N
        chbHighlightResult.setToolTipText(org.openide.util.NbBundle.getMessage(FindAndReplacePanel.class, "FindAndReplacePanel.chbHighlightResult.toolTipText")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 4, 8);
        add(chbHighlightResult, gridBagConstraints);

        chbIncrementalSearch.setText(org.openide.util.NbBundle.getMessage(FindAndReplacePanel.class, "FindAndReplacePanel.chbIncrementalSearch.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 4, 8);
        add(chbIncrementalSearch, gridBagConstraints);

        chbMatchCase.setText(org.openide.util.NbBundle.getMessage(FindAndReplacePanel.class, "FindAndReplacePanel.chbMatchCase.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 0, 8);
        add(chbMatchCase, gridBagConstraints);

        chbWrapAround.setText(org.openide.util.NbBundle.getMessage(FindAndReplacePanel.class, "FindAndReplacePanel.chbWrapAround.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 8);
        add(chbWrapAround, gridBagConstraints);

        chbWholeWords.setText(org.openide.util.NbBundle.getMessage(FindAndReplacePanel.class, "FindAndReplacePanel.chbWholeWords.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 0, 8);
        add(chbWholeWords, gridBagConstraints);

        chbSearchSelection.setText(org.openide.util.NbBundle.getMessage(FindAndReplacePanel.class, "FindAndReplacePanel.chbSearchSelection.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 8);
        add(chbSearchSelection, gridBagConstraints);

        chbRegExp.setText(org.openide.util.NbBundle.getMessage(FindAndReplacePanel.class, "FindAndReplacePanel.chbRegExp.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 0, 8);
        add(chbRegExp, gridBagConstraints);

        chbSearchBackwards.setText(org.openide.util.NbBundle.getMessage(FindAndReplacePanel.class, "FindAndReplacePanel.chbSearchBackwards.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 8);
        add(chbSearchBackwards, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox chbHighlightResult;
    private javax.swing.JCheckBox chbIncrementalSearch;
    private javax.swing.JCheckBox chbMatchCase;
    private javax.swing.JCheckBox chbRegExp;
    private javax.swing.JCheckBox chbSearchBackwards;
    private javax.swing.JCheckBox chbSearchSelection;
    private javax.swing.JCheckBox chbWholeWords;
    private javax.swing.JCheckBox chbWrapAround;
    private javax.swing.JComboBox cmbFindWhat;
    private javax.swing.JComboBox cmbReplaceWith;
    private javax.swing.JLabel lblFindWhat;
    private javax.swing.JLabel lblReplaceWith;
    // End of variables declaration//GEN-END:variables
    ///////////////////////////////////////////////////////////////////////////
    /////////////////////// panel's buttons initialization ////////////////////
    ///////////////////////////////////////////////////////////////////////////
    private JButton cmdClose;
    private JButton cmdFind;
    private JButton cmdReplace;
    private JButton cmdReplaceAll;

    private void initButtons() {
        cmdFind = new javax.swing.JButton("Find");
        cmdReplace = new javax.swing.JButton("Replace");
        cmdReplaceAll = new javax.swing.JButton("Replace All");
        cmdClose = new javax.swing.JButton("Close");
    }

    public JButton[] getButtons() {
        return new JButton[]{ cmdFind, cmdReplace, cmdReplaceAll, cmdClose };
    }

    public void addCloseActionListener(ActionListener listener) {
        cmdClose.addActionListener(listener);
    }
    ///////////////////////////////////////////////////////////////////////////
    /////////////////////////////// process events ////////////////////////////
    ///////////////////////////////////////////////////////////////////////////
    private boolean listeningOptions = true;

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source instanceof JButton) {
            if (source == cmdFind) {
                controller.search(true);
            } else if (source == cmdReplace) {
                controller.replace();
            } else if (source == cmdReplaceAll) {
                controller.replaceAll();
            }
        }

        // save search and replace history
        if (source != cmdClose) {
            listeningOptions = false;
            SearchHistory.getInstance().addPattern(getEditorComponent(cmbFindWhat).getText());
            SearchHistory.getInstance().addReplace(getEditorComponent(cmbReplaceWith).getText());

            String pattern = getEditorComponent(cmbFindWhat).getText();
            String replace = getEditorComponent(cmbReplaceWith).getText();

            updateModels();

            cmbFindWhat.setSelectedItem(pattern);
            cmbReplaceWith.setSelectedItem(replace);
            listeningOptions = true;
        }
    }

    private void initListeners() {
        chbMatchCase.addItemListener(optionsListener);
        chbRegExp.addItemListener(optionsListener);
        chbSearchBackwards.addItemListener(optionsListener);
        chbSearchSelection.addItemListener(optionsListener);
        chbWholeWords.addItemListener(optionsListener);
        chbWrapAround.addItemListener(optionsListener);
        cmbFindWhat.addItemListener(optionsListener);
        cmbReplaceWith.addItemListener(optionsListener);
        chbIncrementalSearch.addItemListener(optionsListener);

        final JTextComponent editorComponent = getEditorComponent(cmbFindWhat);
        editorComponent.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                changedUpdate(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changedUpdate(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                if (chbIncrementalSearch.isSelected() && listeningOptions) {
                    SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            controller.updateOptions(createSingleOption(Options.SEARCH_PATTER, getEditorComponent(cmbFindWhat).getText()));
                            controller.search(false);
                        }
                    });
                }

                enableButtons(!editorComponent.getText().isEmpty());
            }
        });
    }
    private final ItemListener optionsListener = new ItemListener() {

        @Override
        public void itemStateChanged(ItemEvent e) {

            if (!listeningOptions) {
                return;
            }

            Object source = e.getSource();
            if (source == chbRegExp) {
                enableRegExpSearch(chbRegExp.isSelected());
                controller.updateOptions(createSingleOption(Options.REGEXP_SEARCH, chbRegExp.isSelected()));
            } else if (source == chbSearchSelection) {
                controller.updateOptions(createSingleOption(Options.SEARCH_SELECTION, chbSearchSelection.isSelected()));
                setOptionsPanel(controller.activate());
            } else if (source == chbWholeWords) {
                controller.updateOptions(createSingleOption(Options.WHOLE_WORDS, chbWholeWords.isSelected()));
            } else if (source == chbIncrementalSearch) {
                controller.updateOptions(createSingleOption(Options.INCREMENTAL_SEARCH, chbIncrementalSearch.isSelected()));
            } else if (source == chbSearchBackwards) {
                controller.updateOptions(createSingleOption(Options.BACK_SEARCH, chbSearchBackwards.isSelected()));
            } else if (source == chbMatchCase) {
                controller.updateOptions(createSingleOption(Options.MATCH_CASE, chbMatchCase.isSelected()));
            } else if (source == chbWrapAround) {
                controller.updateOptions(createSingleOption(Options.WRAP_AROUND, chbWrapAround.isSelected()));
            } else if (source == cmbFindWhat) {
                controller.updateOptions(createSingleOption(Options.SEARCH_PATTER, getEditorComponent(cmbFindWhat).getText()));
            } else if (source == cmbReplaceWith) {
                controller.updateOptions(createSingleOption(Options.REPLACE_STRING, getEditorComponent(cmbReplaceWith).getText()));
            }
        }
    };

    private void enableRegExpSearch(boolean enable) {
        chbWholeWords.setEnabled(!enable);
        chbSearchBackwards.setEnabled(!enable);
    }

    private void enableButtons(boolean enable) {
        cmdFind.setEnabled(enable);
        cmdReplace.setEnabled(enable);
        cmdReplaceAll.setEnabled(enable);
    }
    ///////////////////////////////////////////////////////////////////////////
    ///////////////////////// management search options ///////////////////////
    ///////////////////////////////////////////////////////////////////////////
    private NavigatorController controller;

    private PropertyStore setOptionsPanel(PropertyStore options) {
        listeningOptions = false;

        chbWrapAround.setSelected(options.get(Options.WRAP_AROUND, Boolean.TRUE));
        chbMatchCase.setSelected(options.get(Options.MATCH_CASE, Boolean.FALSE));
        chbWholeWords.setSelected(options.get(Options.WHOLE_WORDS, Boolean.FALSE));
        chbSearchSelection.setSelected(options.get(Options.SEARCH_SELECTION, Boolean.FALSE));
        chbRegExp.setSelected(options.get(Options.REGEXP_SEARCH, Boolean.FALSE));
        chbSearchBackwards.setSelected(options.get(Options.BACK_SEARCH, Boolean.FALSE));
        chbIncrementalSearch.setSelected(options.get(Options.INCREMENTAL_SEARCH, Boolean.TRUE));
        cmbFindWhat.setSelectedItem(options.get(Options.SEARCH_PATTER, ""));
        cmbReplaceWith.setSelectedItem(options.get(Options.REPLACE_STRING, ""));

        enableRegExpSearch(chbRegExp.isSelected());
        enableButtons(!getEditorComponent(cmbFindWhat).getText().isEmpty());

        listeningOptions = true;

        return options;
    }

    public void open(ReplaceNavigator replaceNavigator) {
        updateModels();

        controller = new NavigatorController(replaceNavigator,
            setOptionsPanel(SearchHistory.getInstance().getOptions()));

        initListeners();
    }

    public void activate() {
        if (controller.isSetActiveDocument()) {
            setOptionsPanel(controller.activate());
            setEnabledPanel(true);
        } else {
            setEnabledPanel(false);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    ////////////////////////////////// utilities //////////////////////////////
    ///////////////////////////////////////////////////////////////////////////
    private JTextComponent getEditorComponent(JComboBox comboBox) {
        return (JTextComponent) comboBox.getEditor().getEditorComponent();
    }

    private PropertyStore createSingleOption(String key, Object value) {
        PropertyStore single = new PropertyStore();
        single.set(key, value);
        return single.getView();
    }

    private void updateModels() {
        cmbFindWhat.setModel(new DefaultComboBoxModel(new Vector<String>(SearchHistory.INSTANCE.getPattrens())));
        cmbReplaceWith.setModel(new DefaultComboBoxModel(new Vector<String>(SearchHistory.INSTANCE.getReplaces())));
    }

    private void setEnabledPanel(boolean enable) {
        if (!enable) {
            enableButtons(enable);
        } else {
            enableButtons(!getEditorComponent(cmbFindWhat).getText().isEmpty());
        }

        for (Component component : getComponents()) {
            if (component instanceof JCheckBox || component instanceof JComboBox) {
                component.setEnabled(enable);
            }
        }

        enableRegExpSearch(chbRegExp.isSelected());
        chbHighlightResult.setEnabled(false);
    }

    private static final class NavigatorController {

        private final ReplaceNavigator replaceNavigator;

        NavigatorController(ReplaceNavigator replaceNavigator, PropertyStore initOptions) {
            this.replaceNavigator = replaceNavigator;

            PropertyStore options = new PropertyStore(initOptions);
            IFinder finder = FinderFactory.createGeneralFinder(options);
            replaceNavigator.setFinder(finder);
        }

        PropertyStore activate() {
            PropertyStore options = replaceNavigator.getFinder().getOptions();
            CharSequence selectedText = replaceNavigator.getDocumentController().getSelectedText();

            if (selectedText != null && selectedText.length() > 0) {
                if (selectedText.toString().contains("\n")) {
                    options.set(Options.SEARCH_SELECTION, Boolean.TRUE);
                } else {
                    options.set(Options.SEARCH_PATTER, selectedText);
                }
            }

            updateCursorOptions(replaceNavigator.getFinder().getOptions());
            replaceNavigator.clearResult();

            if (matchSelected()) {
                replaceNavigator.find();
            }

            return replaceNavigator.getFinder().getOptions().getView();
        }

        void updateOptions(PropertyStore options) {
            IFinder finder = replaceNavigator.getFinder();
            finder.getOptions().merge(options);
            finder.getOptions().set(Options.CURRENT_POSITION, finder.getCursor().getPosition());
            SearchHistory.getInstance().saveOptions(finder.getOptions());
        }

        private void updateCursorOptions(PropertyStore options) {

            boolean searchSelection = options.get(Options.SEARCH_SELECTION, Boolean.FALSE);
            boolean searchBackwards = options.get(Options.BACK_SEARCH, Boolean.FALSE);

            int currPos = calcPosition(searchBackwards);

            options.set(Options.CURRENT_POSITION, currPos);
            options.set(Options.START_POSITION, calcStartPosition(searchSelection));
            options.set(Options.STOP_POSITION, calcStopPosition(searchSelection));
            options.set(Options.SEQUENCE, replaceNavigator.getDocumentController().getSequence());

            replaceNavigator.getFinder().getCursor().setPosition(currPos);
        }

        boolean isSetActiveDocument() {
            IDocumentController documentController = replaceNavigator.getDocumentController();
            return !documentController.isEmpty() && documentController.getComponent() != null;
        }

        private int calcPosition(boolean searchBackwards) {
            IDocumentController sequenceController = replaceNavigator.getDocumentController();
            CharSequence selectedText = sequenceController.getSelectedText();

            if (selectedText != null && selectedText.length() > 0) {
                if (searchBackwards) {
                    return sequenceController.getSelectionEnd() + 1;
                }
                return sequenceController.getSelectionStart() - 1;
            }
            return sequenceController.getCaretPosition();
        }

        private int calcStartPosition(boolean block) {
            IDocumentController sequenceController = replaceNavigator.getDocumentController();

            if (block) {
                return sequenceController.getSelectionStart();
            }
            return 0;
        }

        private int calcStopPosition(boolean block) {
            IDocumentController sequenceController = replaceNavigator.getDocumentController();

            if (block) {
                return sequenceController.getSelectionEnd() - 1;
            }
            return sequenceController.getLength() - 1;
        }

        private boolean matchSelected() {
            IDocumentController sequenceController = replaceNavigator.getDocumentController();

            int first = sequenceController.getSelectionStart(),
                last = sequenceController.getSelectionEnd() - 1;

            FindResult result = replaceNavigator.getFinder().findFirst(first, last);
            return result.isFound() && result.first == first && result.last == last;
        }

        void search(boolean move) {
            if (isSetActiveDocument()) {
                if (replaceNavigator.find(move).isFound()) {
                    replaceNavigator.select();
                } else {
                    replaceNavigator.getDocumentController().unselect();
                    setStatus("'" + replaceNavigator.getFinder().getOptions().get(Options.SEARCH_PATTER, "") + "' not found");
                }
            }
        }

        void replace() {
            if (isSetActiveDocument()) {
                if (!replaceNavigator.isFind()) {
                    replaceNavigator.find();
                }
                replaceNavigator.replace();
                search(true);
            }
        }

        void replaceAll() {
            if (isSetActiveDocument()) {
                int replaceCount = replaceNavigator.replaceAll();
                setStatus("Total replaced: " + replaceCount);
            }
        }

        private void setStatus(String status) {
            Utilities.setStatusText(replaceNavigator.getDocumentController().getComponent(), status, 800);
        }
    }

    private static class SearchHistory {

        private static final SearchHistory INSTANCE = new SearchHistory();

        public static SearchHistory getInstance() {
            return INSTANCE;
        }
        private PropertyStore arch = new PropertyStore();
        private final Set<String> pattrens = new HashSet<String>();
        private final Set<String> replaces = new HashSet<String>();

        public PropertyStore getOptions() {
            return arch;
        }

        public Set<String> getPattrens() {
            return pattrens;
        }

        public Set<String> getReplaces() {
            return replaces;
        }

        public void saveOptions(PropertyStore options) {

            final String[] opts = { Options.BACK_SEARCH, Options.MATCH_CASE,
                Options.REGEXP_SEARCH, Options.WHOLE_WORDS, Options.SEARCH_SELECTION };

            arch = new PropertyStore();

            for (String opt : opts) {
                arch.set(opt, options.get(opt, Boolean.FALSE));
            }

            arch.set(Options.WRAP_AROUND, options.get(Options.WRAP_AROUND, Boolean.TRUE));
            arch.set(Options.INCREMENTAL_SEARCH, options.get(Options.INCREMENTAL_SEARCH, Boolean.TRUE));
            arch.set(Options.SEARCH_PATTER, options.get(Options.SEARCH_PATTER, ""));
            arch.set(Options.REPLACE_STRING, options.get(Options.REPLACE_STRING, ""));
        }

        public void addPattern(String pattern) {
            if (pattern != null && !pattern.isEmpty()) {
                pattrens.add(pattern);
            }
        }

        public void addReplace(String replace) {
            if (replace != null && !replace.isEmpty()) {
                replaces.add(replace);
            }
        }
    }
}
