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
 * PhrasesAndGuessesPanel.java
 *
 * Created on Sep 18, 2009, 5:01:02 PM
 */

package org.radixware.kernel.designer.ads.localization.prompt;

import java.awt.Component;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.localization.AdsPhraseBookDef;
import org.radixware.kernel.common.defs.localization.ILocalizingBundleDef;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.ads.localization.MultilingualEditor;
import org.radixware.kernel.designer.ads.localization.RowString;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinition;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.components.SimpleTable;
import org.radixware.kernel.designer.common.general.editors.EditorsManager;
import org.radixware.kernel.designer.common.general.filesystem.RadixFileUtil;


public class PhrasesAndGuessesPanel extends javax.swing.JPanel {
    private PhraseTableModel tableModel;
    private AbstractAction actInsert;
    private final List<Prompt> promptList = Collections.synchronizedList(new ArrayList<Prompt>());
    private List<Prompt> shownList;    
    private List<AdsPhraseBookDef> openedPhraseBook;
    private RowString rowString;
    
    private List<EIsoLanguage> sourceLangs;
    private List<EIsoLanguage> translLangs;
    private EIsoLanguage translLang;
    private final static int iconSize=20;
    private MultilingualEditor parent;

    private AbstractAction actGoToPraseBook=new AbstractAction(NbBundle.getMessage(PhrasesAndGuessesPanel.class, "EDIT")) {
         @Override
         public void actionPerformed(ActionEvent e) {
             PhrasesAndGuessesPanel.this.goToContextDef();
         }
    };

    /** Creates new form PhrasesAndGuessesPanel */
    public PhrasesAndGuessesPanel(final AbstractAction actInsert, final MultilingualEditor parent) {
        this.parent=parent;
        initComponents();

        openedPhraseBook=parent.getPhraseBooksFromCfg();
        if(openedPhraseBook==null)
               openedPhraseBook= new ArrayList<>();
        this.actInsert=actInsert;
        MouseListener phraseTableListener=new MouseListener() {
            @Override
            public void mouseReleased(final MouseEvent event) {
                if((event.getButton()==MouseEvent.BUTTON1)&&(event.getClickCount()==2)){
                   PhrasesAndGuessesPanel.this.actInsert.actionPerformed(null);
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        };
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(final ListSelectionEvent e) {
                if(! e.getValueIsAdjusting()){
                    final int row=table.getSelectedRow();
                    if(row>=0 && row<tableModel.getRowCount()){
                        final Prompt p=tableModel.getRow(row);
                        if(p.getPhraseBook()==null)
                            actGoToPraseBook.setEnabled(false);
                        else
                            actGoToPraseBook.setEnabled(true);
                        PhrasesAndGuessesPanel.this.actInsert.setEnabled(true);
                    }else{
                        PhrasesAndGuessesPanel.this.actInsert.setEnabled(false);
                        actGoToPraseBook.setEnabled(false);
                    }
                }
            }
        });
        shownList=new ArrayList<>();
        table.addMouseListener(phraseTableListener);
        createTableUi();

        final JPopupMenu menu=createContextMenu();
        table.setComponentPopupMenu(menu);

        createToolBarUi();
    }


    private void createTableUi() {
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.getTableHeader().setReorderingAllowed(false);
        updateTable();
    }

     public final void updateTable() {
        tableModel = new PhraseTableModel(shownList,translLang);
        table.setModel(tableModel);

        table.getColumnModel().getColumn(0).setCellRenderer(new TypeCellRenderer());
        table.getColumnModel().getColumn(0).setPreferredWidth(iconSize);
        table.getColumnModel().getColumn(0).setMinWidth(iconSize);
        table.getColumnModel().getColumn(0).setMaxWidth(iconSize);
        table.getColumnModel().getColumn(0).setResizable(true);
    }

    private JButton btnOpenPhraseBook;
    private JButton btnEditPhraseBook;
    private JButton btnClosePhraseBook;
    private void  createToolBarUi() {
       Icon icon= RadixWareIcons.MLSTRING_EDITOR.OPEN_PHRASE_BOOK.getIcon(iconSize, iconSize);
       btnOpenPhraseBook=new JButton(icon);
       btnOpenPhraseBook.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                btnOpenPhraseBookActionPerformed();
                parent.setPhraseBooksToCfg(openedPhraseBook);
            }
        });
       setButton( btnOpenPhraseBook, NbBundle.getMessage(PhrasesAndGuessesPanel.class, "OPEN_PB"), true);

       boolean enable=!openedPhraseBook.isEmpty();
       icon= RadixWareIcons.MLSTRING_EDITOR.EDIT_PHRASE_BOOK.getIcon(iconSize, iconSize);
       btnEditPhraseBook=new JButton(icon);
       btnEditPhraseBook.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                    JPopupMenu menuEdit=createContextMenuEdit();
                    menuEdit.show(btnEditPhraseBook, 0, btnEditPhraseBook.getHeight());
            }
        });
        setButton( btnEditPhraseBook,NbBundle.getMessage(PhrasesAndGuessesPanel.class, "EDIT_PB"),enable);

       icon= RadixWareIcons.MLSTRING_EDITOR.CLOSE_PHRASE_BOOK.getIcon(iconSize, iconSize);
       btnClosePhraseBook= new JButton(icon);
       btnClosePhraseBook.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                JPopupMenu menuClose=createContextMenuClose();
                menuClose.show(btnClosePhraseBook, 0, btnClosePhraseBook.getHeight());
                parent.setPhraseBooksToCfg(openedPhraseBook);
            }
       });
       setButton( btnClosePhraseBook,NbBundle.getMessage(PhrasesAndGuessesPanel.class, "CLOSE_PB"),enable);
    }

    private void setButton(final AbstractButton btn, final String toolTip, final boolean enable) {
        btn.setFocusable(false);
        btn.setToolTipText(toolTip);
        btn.setMargin(new Insets(0, 0, 0, 0));
        btn.setEnabled(enable);
        toolBar.add(btn);
    }

    private JPopupMenu createContextMenu() {
        JPopupMenu menu=new JPopupMenu();
        menu.add(actInsert);
        actInsert.setEnabled(false);
        actGoToPraseBook.setEnabled(false);
        menu.add(actGoToPraseBook);
        return menu;
    }

    public void addItemToPromptList(final Prompt promp) {
            if(promptList.contains(promp))
                promptList.remove(promp);
            promptList.add(promp);

    }

    public void removeItemFromPromptList(final Prompt promp) {
        if(promptList.contains(promp)){
            promptList.remove(promp);
        }
    }

    public void update(final EIsoLanguage translLang, final RowString rowString, final List<EIsoLanguage> sourceLangs, final List<EIsoLanguage> translLangs) {
        this.sourceLangs=sourceLangs;
        this.translLangs=translLangs;
        this.translLang=translLang;
        this.rowString=rowString;

        updatePanel();
    }


    private void updatePanel() {
        addLoadPromptsFromPhraseBooks();
        shownList.clear();
        for(EIsoLanguage lang :sourceLangs){
            String s= rowString!=null ? rowString.getValue(lang):null;
            if((translLang!=null)&&(s!=null)&&(!"".equals(s))){
                findPhrases(lang,s);
            }
        }
        tableModel.setPromptList(shownList);
        tableModel.fireTableDataChanged();
        updateTable();
    }

    public boolean hasPrompts(){
        return shownList!=null && !shownList.isEmpty();
    }

    private void findPhrases(final EIsoLanguage lang, final String sourceText) {
        synchronized (promptList) {
            for (Prompt propmt : promptList) {
                if (!shownList.contains(propmt) && (propmt.hasSimilarSource(rowString, lang, sourceText))
                        && (propmt.hasTranslation(translLang)) && (!isEqualText(propmt))/*&&(!isSameTargetText( propmt, targetText))*/) {
                    shownList.add(propmt);
                }
            }
        }
    }

    /*private boolean isSameTargetText(Prompt propmt,String targetText){
        return propmt.getTranslation(translLang).equals(targetText);
    }*/

    private boolean isEqualText(final Prompt p) {
        if(p.getPhraseBook()==null)
            for(Prompt propmt : shownList){
                if(p.isEqualText(propmt,translLang))
                    return true;
            }
        return false;
    }

    public String getSelectedPhrase() {
        final int row=table.getSelectedRow();
        return (String)tableModel.getValueAt(row, 2);
    }

    public void clearPromptList() {
        synchronized (promptList) {
            promptList.clear();
        }
    }

    private void goToContextDef() {
        final int row=table.getSelectedRow();
        if((row>=0) &&(row<table.getRowCount())){
            Prompt p=tableModel.getRow(row);
            if (p != null) {
                //curPrompt=p;
                goToPhraseBook( p.getPhraseBook());
            }
        }
    }

   /* private static Prompt curPrompt;
    public static Prompt getCurPrompt() {
        return curPrompt;
    }*/

    private void goToPhraseBook(final AdsPhraseBookDef phraseBook){
        /*OpenInfo inf=new OpenInfo();
        int row=table.getSelectedRow();
        if((row>=0) &&(row<table.getRowCount())){
            Prompt p=tableModel.getRow(row);
            inf.setTarget(p.getMlString());
        }*/
        //DialogUtils.goToObject(phraseBook);
        EditorsManager.getDefault().open(phraseBook);//, inf);
    }

    public void closePhraceBook(final AdsPhraseBookDef phraseBook){
        synchronized (promptList) {
            for(int i=0;i<promptList.size();i++){
                final Prompt p=promptList.get(i);
                if((p.getPhraseBook()!=null) && (p.getPhraseBook().equals(phraseBook))){
                    promptList.remove(p);
                    i--;
                }
            }
        }
        if(openedPhraseBook.contains(phraseBook))
            openedPhraseBook.remove(phraseBook);
        if(openedPhraseBook.isEmpty()){
            btnClosePhraseBook.setEnabled(false);
            btnEditPhraseBook.setEnabled(false);
            parent.canAddStringToPhraseBook(false);
        }
        updatePanel();
        
    }

    public List<String> getPhraseBookPrompts(){
        final List<String> list=new ArrayList<>();
        for(Prompt p:shownList){
            if(p.getPhraseBook()!=null)
                list.add(p.getTranslation(translLang));
        }
        return list;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        table = new SimpleTable();
        toolBar = new javax.swing.JToolBar();

        setPreferredSize(new java.awt.Dimension(420, 120));

        jScrollPane1.setPreferredSize(new java.awt.Dimension(452, 100));

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        table.setShowHorizontalLines(false);
        table.setShowVerticalLines(false);
        jScrollPane1.setViewportView(table);

        toolBar.setFloatable(false);
        toolBar.setRollover(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(toolBar, javax.swing.GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(toolBar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnOpenPhraseBookActionPerformed(){
        final BranchesVisitor branchesVisitor = new BranchesVisitor();
        final ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(branchesVisitor, new GoToDefinitionProvider());
        final List<Definition> definitions = ChooseDefinition.chooseDefinitions(cfg);

        if(definitions!=null){
             for(Definition definition : definitions)
                if(definition instanceof AdsPhraseBookDef){
                    final AdsPhraseBookDef phraseBook=(AdsPhraseBookDef)definition;
                    if(!openedPhraseBook.contains(phraseBook)){
                        openedPhraseBook.add(phraseBook);
                        openPhraceBook( phraseBook);
                    }
                }
             if(!openedPhraseBook.isEmpty()){
                btnClosePhraseBook.setEnabled(true);
                btnEditPhraseBook.setEnabled(true);
                parent.canAddStringToPhraseBook(true);
            }
        }
        updatePanel();
    }

    private void addLoadPromptsFromPhraseBooks(){
        for(AdsPhraseBookDef phraseBook:openedPhraseBook){
            openPhraceBook( phraseBook);
        }
    }

    public void openPhraceBook(final AdsPhraseBookDef phraseBook){         
        addPhraseBookStringsToPrompt(phraseBook);
        /*while(phraseBook.getSuperPhraseBook()!=null){
            //if(!openedPhraseBook.contains(ui))
            phraseBook= findSuperPhraseBook( phraseBook);
            addPhraseBookStringsToPrompt(phraseBook);
        }   */
    }

   /* private AdsPhraseBookDef findSuperPhraseBook(AdsPhraseBookDef phraseBook){
        Branch branch=phraseBook.getModule().getSegment().getLayer().getBranch();
        return (AdsPhraseBookDef)branch.find(new PhraseBookProvider(phraseBook.getSuperPhraseBook()));
    }*/

    private void addPhraseBookStringsToPrompt(final AdsPhraseBookDef phraseBook){
        final ILocalizingBundleDef<? extends IMultilingualStringDef> mlBundle=phraseBook.findExistingLocalizingBundle();
        if(mlBundle==null) return;
        final List<? extends IMultilingualStringDef> list=mlBundle.getStrings().get(EScope.LOCAL);
        for(IMultilingualStringDef mlstring : list){
             final Prompt p=new  Prompt(mlstring,sourceLangs,translLangs,phraseBook);
             synchronized (promptList) {
             if(!promptList.contains(p))
                promptList.add(p);
             }
        }
    }

    private JPopupMenu createContextMenuClose(){
        final JPopupMenu menu=new JPopupMenu();
        for(final AdsPhraseBookDef phraseBook:openedPhraseBook){
            final JMenuItem item=new JMenuItem();
            item.setName(phraseBook.getName());
            item.setText(phraseBook.getName());
            item.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                      closePhraceBook(phraseBook);
                }
            });
            menu.add(item);
        }
        return menu;
    }
    
    private JPopupMenu createContextMenuEdit(){
        final JPopupMenu menu=new JPopupMenu();
        for(final AdsPhraseBookDef phraseBook:openedPhraseBook){
            final JMenuItem item=new JMenuItem();
            item.setName(phraseBook.getName());
            item.setText(phraseBook.getName());
            item.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                     PhrasesAndGuessesPanel.this.goToPhraseBook(phraseBook);
                }
            });
            menu.add(item);
        }
        return menu;
    }

    public List<AdsPhraseBookDef> getOpenedPhraseBook(){
        return openedPhraseBook;
    }


    private class BranchesVisitor extends RadixObject {
       @Override
        public void visitChildren(final IVisitor visitor, final VisitorProvider provider) {
            final Collection<Branch> branches = RadixFileUtil.getOpenedBranches();
            for (Branch branch : branches) {
                branch.visit(visitor, provider);
            }
            super.visitChildren(visitor, provider);
        }
    }


    private class GoToDefinitionProvider extends VisitorProvider {
        @Override
        public boolean isTarget(final RadixObject radixObject) {
            if ((radixObject instanceof AdsPhraseBookDef)&&
               (!openedPhraseBook.contains((AdsPhraseBookDef)radixObject))) {
                return true;
            }
            return  false;
        }
    }

    private class TypeCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, final int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setText("");


            final Icon icon=(Icon)((PhraseTableModel)table.getModel()).getValueAt(row, column);
            setIcon(icon);
            this.setHorizontalAlignment(SwingConstants.CENTER);

            //RowString rowString=(RowString) ((TableModel)table.getModel()).getRow(row);
            //String toolTip=rowString.getToolTip(sourceLangs, translLangs);
            //this.setToolTipText(toolTip);
            return this;
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable table;
    private javax.swing.JToolBar toolBar;
    // End of variables declaration//GEN-END:variables

    /*private class TableModel extends AbstractTableModel {
        private List<Prompt> promptList;
        private List<String> columns;
        private final int iconSize=16;
        
        public TableModel(List<Prompt> promptList){
            columns=new ArrayList();
            columns.add("");
            columns.add("Source texts");
            columns.add("Translation");
            columns.add("Comment");
            this.promptList=new ArrayList(promptList);
        }

        public void setPromptList(List<Prompt> promptList){
            this.promptList=new ArrayList(promptList);
        }

        @Override
        public int getRowCount() {
            return promptList.size();
        }

        @Override
        public int getColumnCount() {
            return columns.size();
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            if(columnIndex==0){
                if(promptList.get(rowIndex).getPhraseBook()==null)
                    return RadixWareIcons.WIDGETS.CHOOSE_LANGS.getIcon(iconSize, iconSize);
                else
                    return AdsDefinitionIcon.PHRASE_BOOK.getIcon(iconSize, iconSize);
            }else if(columnIndex==1){
                return promptList.get(rowIndex).getSourceText();
            }else if(columnIndex==2){
                return promptList.get(rowIndex).getTranslation(translLang);
            }else
                return "";
        }

        @Override
        public String getColumnName(int col) {
            return columns.get(col);
        }

        public Prompt getRow(int row){
            return promptList.get(row);
        }

        public void addRow(Prompt row){
            promptList.add(row);
        }
    }*/
}
