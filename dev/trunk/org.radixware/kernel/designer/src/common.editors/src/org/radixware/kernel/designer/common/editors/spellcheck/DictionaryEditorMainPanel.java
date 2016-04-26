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
 * DictionaryEditorMainPanel.java
 *
 * Created on Dec 15, 2011, 4:24:31 PM
 */
package org.radixware.kernel.designer.common.editors.spellcheck;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ComboBoxModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.openide.util.NbBundle;
import org.radixware.kernel.utils.spellchecker.LayerDictionary;
import org.radixware.kernel.common.check.spelling.Word;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.common.dialogs.RadixWareDesignerIcon;
import org.radixware.kernel.designer.common.dialogs.components.DictionaryLanguageListModel;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.utils.spellchecker.DictionariesSuite;


final class DictionaryEditorMainPanel extends javax.swing.JPanel {

    private final class LangChangeListener implements ListDataListener {

        @Override
        public void intervalAdded(ListDataEvent e) {
        }

        @Override
        public void intervalRemoved(ListDataEvent e) {
        }

        @Override
        public void contentsChanged(ListDataEvent e) {
            final Object selItem = langList.getSelectedItem();

            if (selItem instanceof EIsoLanguage) {
                dictionaries.setSelectedLanguage((EIsoLanguage) selItem);
            } else {
                dictionaries.setSelectedLanguage(null);
            }
            initDictionary();
        }
    };

    private final class WordChangeListener implements ListDataListener {

        @Override
        public void intervalAdded(ListDataEvent e) {
            contentsChanged(e);
        }

        @Override
        public void intervalRemoved(ListDataEvent e) {
            contentsChanged(e);
        }

        @Override
        public void contentsChanged(ListDataEvent e) {
            wordListStateChanged();
        }
    }

    private final DictionariesSuite dictionaries;
    private WordListModel wordListModel;
    private final WordChangeListener wordChangeListener = new WordChangeListener();

    public DictionaryEditorMainPanel(DictionariesSuite dictionariesSuite) {
        initComponents();

        assert dictionariesSuite != null;

        this.dictionaries = dictionariesSuite;

        initPanel();

        initDictionary();

        initListeners();
    }

    private void initListeners() {
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {

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
                ((WordListModel) wordList.getModel()).setFilter(txtSearch.getText().toLowerCase());
            }
        });

        txtSearch.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    txtSearch.setText("");
                }
            }
        });

        wordList.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                wordListStateChanged();
            }
        });
    }

    private void initPanel() {
        btnAddWord.setIcon(RadixWareDesignerIcon.CREATE.ADD.getIcon());
        btnAddWord.setText("");

        btnRemoveWord.setIcon(RadixWareDesignerIcon.DELETE.DELETE.getIcon());
        btnRemoveWord.setText("");

        btnEditWord.setIcon(RadixWareDesignerIcon.EDIT.EDIT.getIcon());
        btnEditWord.setText("");

        btnSortOrder.setIcon(RadixWareIcons.DICTIONARY.SORTING.getIcon());
        btnSortOrder.setText("");

        initLangList();
        updateButtons();
    }

    private void initDictionary() {
        initWordList();
    }

    private void initWordList() {
        LayerDictionary dictionary = dictionaries.getSelectedDictionary();
        if (dictionary != null) {
            wordListModel = new WordListModel(wordList, dictionary, txtSearch.getText());
            wordListModel.addListDataListener(wordChangeListener);
            wordList.setModel(wordListModel);
        }
    }

    private void initLangList() {
        Layer layer = dictionaries.getLayer();
        if (layer != null) {

            ComboBoxModel model = new DictionaryLanguageListModel(layer.getLanguages());
            langList.setModel(model);

            model.setSelectedItem(dictionaries.getSelectedLanguage());
            model.addListDataListener(new LangChangeListener());
        }
    }

    private void addWord() {

        AddWordDialog dialog = new AddWordDialog("Add new word");
        if (dialog.showModal()) {
            Word word = dialog.getWord();
            wordListModel.addWord(word, dialog.getDescription());
        }
    }

    private void editWord() {
        int selIndex = wordList.getSelectedIndex();
        LayerDictionary dictionary = dictionaries.getSelectedDictionary();

        Word word = wordListModel.getElementAt(selIndex);

        String comment = dictionary.getComment(word);

        if (comment == null) {
            comment = "";
        }

        AddWordDialog dialog = new AddWordDialog(word, comment, "Edit word");
        if (dialog.showModal()) {
            wordListModel.removeWords(new Word[] { word });
            wordListModel.addWord(dialog.getWord(), dialog.getDescription());
        }
    }

    private void removeWord() {
        Word[] selectedWords = getSelectedWords();
        if (selectedWords != null) {
            String message;
            if (selectedWords.length > 1) {
                message = NbBundle.getMessage(DictionaryEditorMainPanel.class, "MessageConfirmation.RemoveWords");
            } else {
                Word word = selectedWords[0];
                message = NbBundle.getMessage(DictionaryEditorMainPanel.class, "MessageConfirmation.RemoveWord") + " '" + word + "'?";
            }

            boolean dialogResult = DialogUtils.messageConfirmation(message);
            if (dialogResult) {
                wordListModel.removeWords(selectedWords);
            }
        }
    }

    private void switchSortOrder() {
        WordListModel model = (WordListModel) wordList.getModel();
        if (model.getSortOrder() == WordListModel.SortOrder.ASC) {
            model.setSortOrder(WordListModel.SortOrder.DESC);
        } else {
            model.setSortOrder(WordListModel.SortOrder.ASC);
        }
    }

    private void wordListStateChanged() {
        String comment = null;

        Word word = (Word) wordList.getSelectedValue();

        if (word != null) {
            LayerDictionary dictionary = dictionaries.getSelectedDictionary();
            if (dictionary != null) {
                comment = dictionary.getComment(word);
            }
        }
        wordDescription.setText(comment != null ? comment : "");
        updateButtons();
    }

    private void updateButtons() {
        int len = wordList.getSelectedIndices().length;

        btnRemoveWord.setEnabled(len > 0);
        btnEditWord.setEnabled(len == 1);
    }

    private Word[] getSelectedWords() {
        Object[] selectedValues = wordList.getSelectedValues();

        int len = selectedValues.length;
        if (len > 0) {
            Word[] words = new Word[len];
            System.arraycopy(selectedValues, 0, words, 0, len);
            return words;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        leftPanel = new javax.swing.JPanel();
        wordListPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        wordList = new javax.swing.JList();
        searchPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();
        wordEditToolBar = new javax.swing.JToolBar();
        btnAddWord = new javax.swing.JButton();
        btnRemoveWord = new javax.swing.JButton();
        btnEditWord = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btnSortOrder = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));
        lblLanguage = new javax.swing.JLabel();
        langList = new javax.swing.JComboBox();
        rightPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        wordDescription = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();

        setMinimumSize(new java.awt.Dimension(550, 340));
        setLayout(new java.awt.BorderLayout());

        jSplitPane1.setDividerLocation(400);

        leftPanel.setLayout(new java.awt.BorderLayout());

        wordListPanel.setLayout(new java.awt.BorderLayout());

        wordList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        jScrollPane1.setViewportView(wordList);

        wordListPanel.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        searchPanel.setLayout(new javax.swing.BoxLayout(searchPanel, javax.swing.BoxLayout.LINE_AXIS));

        jLabel1.setLabelFor(txtSearch);
        jLabel1.setText(org.openide.util.NbBundle.getMessage(DictionaryEditorMainPanel.class, "DictionaryEditorMainPanel.jLabel1.text")); // NOI18N
        jLabel1.setToolTipText(org.openide.util.NbBundle.getMessage(DictionaryEditorMainPanel.class, "DictionaryEditorMainPanel.jLabel1.toolTipText")); // NOI18N
        jLabel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4));
        searchPanel.add(jLabel1);

        txtSearch.setText(org.openide.util.NbBundle.getMessage(DictionaryEditorMainPanel.class, "DictionaryEditorMainPanel.txtSearch.text")); // NOI18N
        searchPanel.add(txtSearch);

        wordListPanel.add(searchPanel, java.awt.BorderLayout.PAGE_START);

        leftPanel.add(wordListPanel, java.awt.BorderLayout.CENTER);

        wordEditToolBar.setFloatable(false);
        wordEditToolBar.setRollover(true);

        btnAddWord.setText(org.openide.util.NbBundle.getMessage(DictionaryEditorMainPanel.class, "DictionaryEditorMainPanel.btnAddWord.text")); // NOI18N
        btnAddWord.setToolTipText(org.openide.util.NbBundle.getMessage(DictionaryEditorMainPanel.class, "DictionaryEditorMainPanel.btnAddWord.toolTipText")); // NOI18N
        btnAddWord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddWordActionPerformed(evt);
            }
        });
        wordEditToolBar.add(btnAddWord);

        btnRemoveWord.setText(org.openide.util.NbBundle.getMessage(DictionaryEditorMainPanel.class, "DictionaryEditorMainPanel.btnRemoveWord.text")); // NOI18N
        btnRemoveWord.setToolTipText(org.openide.util.NbBundle.getMessage(DictionaryEditorMainPanel.class, "DictionaryEditorMainPanel.btnRemoveWord.toolTipText")); // NOI18N
        btnRemoveWord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveWordActionPerformed(evt);
            }
        });
        wordEditToolBar.add(btnRemoveWord);

        btnEditWord.setText(org.openide.util.NbBundle.getMessage(DictionaryEditorMainPanel.class, "DictionaryEditorMainPanel.btnEditWord.text")); // NOI18N
        btnEditWord.setToolTipText(org.openide.util.NbBundle.getMessage(DictionaryEditorMainPanel.class, "DictionaryEditorMainPanel.btnEditWord.toolTipText")); // NOI18N
        btnEditWord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditWordActionPerformed(evt);
            }
        });
        wordEditToolBar.add(btnEditWord);
        wordEditToolBar.add(jSeparator1);

        btnSortOrder.setText(org.openide.util.NbBundle.getMessage(DictionaryEditorMainPanel.class, "DictionaryEditorMainPanel.btnSortOrder.text")); // NOI18N
        btnSortOrder.setToolTipText(org.openide.util.NbBundle.getMessage(DictionaryEditorMainPanel.class, "DictionaryEditorMainPanel.btnSortOrder.toolTipText")); // NOI18N
        btnSortOrder.setFocusable(false);
        btnSortOrder.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSortOrder.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSortOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSortOrderActionPerformed(evt);
            }
        });
        wordEditToolBar.add(btnSortOrder);
        wordEditToolBar.add(jSeparator2);
        wordEditToolBar.add(filler1);

        lblLanguage.setLabelFor(langList);
        lblLanguage.setText(org.openide.util.NbBundle.getMessage(DictionaryEditorMainPanel.class, "DictionaryEditorMainPanel.lblLanguage.text")); // NOI18N
        lblLanguage.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 4));
        wordEditToolBar.add(lblLanguage);

        langList.setToolTipText(org.openide.util.NbBundle.getMessage(DictionaryEditorMainPanel.class, "DictionaryEditorMainPanel.langList.toolTipText")); // NOI18N
        wordEditToolBar.add(langList);

        leftPanel.add(wordEditToolBar, java.awt.BorderLayout.PAGE_START);

        jSplitPane1.setLeftComponent(leftPanel);

        rightPanel.setLayout(new java.awt.BorderLayout());

        wordDescription.setColumns(20);
        wordDescription.setEditable(false);
        wordDescription.setRows(5);
        jScrollPane2.setViewportView(wordDescription);

        rightPanel.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jLabel2.setText(org.openide.util.NbBundle.getMessage(DictionaryEditorMainPanel.class, "DictionaryEditorMainPanel.jLabel2.text")); // NOI18N
        jLabel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(7, 10, 7, 4));
        rightPanel.add(jLabel2, java.awt.BorderLayout.PAGE_START);

        jSplitPane1.setRightComponent(rightPanel);

        add(jSplitPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddWordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddWordActionPerformed
        addWord();
    }//GEN-LAST:event_btnAddWordActionPerformed

    private void btnRemoveWordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveWordActionPerformed
        removeWord();
    }//GEN-LAST:event_btnRemoveWordActionPerformed

    private void btnEditWordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditWordActionPerformed
        editWord();
    }//GEN-LAST:event_btnEditWordActionPerformed

    private void btnSortOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSortOrderActionPerformed
        switchSortOrder();
    }//GEN-LAST:event_btnSortOrderActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddWord;
    private javax.swing.JButton btnEditWord;
    private javax.swing.JButton btnRemoveWord;
    private javax.swing.JButton btnSortOrder;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JComboBox langList;
    private javax.swing.JLabel lblLanguage;
    private javax.swing.JPanel leftPanel;
    private javax.swing.JPanel rightPanel;
    private javax.swing.JPanel searchPanel;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextArea wordDescription;
    private javax.swing.JToolBar wordEditToolBar;
    private javax.swing.JList wordList;
    private javax.swing.JPanel wordListPanel;
    // End of variables declaration//GEN-END:variables
}
