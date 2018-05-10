/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.radixware.kernel.designer.ads.localization.source;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.builder.impexp.ImportConflict;
import org.radixware.kernel.common.builder.impexp.ImportConflicts;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.environment.IMlStringBundle;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.common.dialogs.RadixWareDesignerIcon;
import org.radixware.kernel.designer.common.dialogs.components.state.StateAbstractDialog;

/**
 *
 * @author avoloshchuk
 */
public class ImportConflictsPanel extends StateAbstractDialog.StateAbstractPanel {
    private final String[] COLUMN_NAMES = {"Current value", "Value from file"};
    /**
     * Creates new form ImportConflictsPanel
     */
    public ImportConflictsPanel() {
        initComponents();
        prevBtn.setIcon(RadixWareIcons.ARROW.LEFT.getIcon(20));
        prevBtn.setEnabled(getNextRowPosition() >= 0);
        
        nextBtn.setIcon(RadixWareIcons.ARROW.RIGHT.getIcon(20));
        nextBtn.setEnabled(getNextRowPosition() >= 0);
        
        acceptCurrentValueBtn.setIcon(RadixWareIcons.MLSTRING_EDITOR.ACCEPT_CURRENT_VALUES.getIcon(20));
        
        acceptImportValueBtn.setIcon(RadixWareIcons.MLSTRING_EDITOR.ACCEPT_IMPORT_VALUES.getIcon(20));
        
        acceptBtn.setIcon(RadixWareIcons.DIALOG.OK.getIcon(20));
        
        KeyListener tableKeyListener = new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                switch(e.getKeyCode()){
                    case KeyEvent.VK_UP:
                        e.consume();
                        break;
                    case KeyEvent.VK_DOWN:
                        e.consume();
                        break;
                       
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                switch(e.getKeyCode()){
                    case KeyEvent.VK_UP:
                        prev();
                        e.consume();
                        break;
                    case KeyEvent.VK_DOWN:
                        next();
                        e.consume();
                        break;
                       
                }
                enableAcceptBtn();
            }
        };

        MouseListener tableMouseListener = new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                int row = getPrevRowPosition();
                prevBtn.setEnabled(row >= 0);
                row = getNextRowPosition();
                nextBtn.setEnabled(row >= 0);
                enableAcceptBtn();
                
                if (e.getClickCount() == 2) {
                    acceptSelectedRows();
                }
            }
            
        };
        table.addKeyListener(tableKeyListener);
        table.addMouseListener(tableMouseListener);

        ListSelectionModel cellSelectionModel = table.getSelectionModel();
         
        cellSelectionModel.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                int row = getPrevRowPosition();
                prevBtn.setEnabled(row >= 0);
                row = getNextRowPosition();
                nextBtn.setEnabled(row >= 0);
                enableAcceptBtn();
            }

        });
    }

    public void open(List<ImportConflicts> conflicts) {
        List<Object> objects = new ArrayList<>();
        for (ImportConflicts conflict : conflicts) {
            objects.add(conflict);
            objects.addAll(conflict);
        }
        Object[][] data = new Object[objects.size()][1];
        for (int i = 0; i < objects.size(); i++){
            data[i][0] = objects.get(i);
        }
        
        
        table.setModel(new TableModel(data));
        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            TableColumn c = table.getColumnModel().getColumn(i);
            c.setCellRenderer(new ColumnSpanningCellRenderer());
            c.setMinWidth(50);
        }

        table.setAutoCreateRowSorter(false);
        table.getTableHeader().setReorderingAllowed(false);
        table.setRowSelectionAllowed(true);
        table.setCellSelectionEnabled(true);
        table.setFillsViewportHeight(true);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 1));
        check();
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        jToolBar1 = new javax.swing.JToolBar();
        acceptBtn = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        prevBtn = new javax.swing.JButton();
        nextBtn = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        acceptCurrentValueBtn = new javax.swing.JButton();
        acceptImportValueBtn = new javax.swing.JButton();

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Current value", "Value from file"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table.setFillsViewportHeight(true);
        jScrollPane1.setViewportView(table);
        if (table.getColumnModel().getColumnCount() > 0) {
            table.getColumnModel().getColumn(0).setHeaderValue(org.openide.util.NbBundle.getMessage(ImportConflictsPanel.class, "ImportConflictsPanel.table.columnModel.title1")); // NOI18N
            table.getColumnModel().getColumn(1).setHeaderValue(org.openide.util.NbBundle.getMessage(ImportConflictsPanel.class, "ImportConflictsPanel.table.columnModel.title2")); // NOI18N
        }

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        org.openide.awt.Mnemonics.setLocalizedText(acceptBtn, org.openide.util.NbBundle.getMessage(ImportConflictsPanel.class, "ImportConflictsPanel.acceptBtn.text")); // NOI18N
        acceptBtn.setToolTipText(org.openide.util.NbBundle.getMessage(ImportConflictsPanel.class, "ImportConflictsPanel.acceptBtn.toolTipText")); // NOI18N
        acceptBtn.setFocusable(false);
        acceptBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        acceptBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        acceptBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                acceptBtnActionPerformed(evt);
            }
        });
        jToolBar1.add(acceptBtn);
        jToolBar1.add(jSeparator1);

        org.openide.awt.Mnemonics.setLocalizedText(prevBtn, org.openide.util.NbBundle.getMessage(ImportConflictsPanel.class, "ImportConflictsPanel.prevBtn.text")); // NOI18N
        prevBtn.setToolTipText(org.openide.util.NbBundle.getMessage(ImportConflictsPanel.class, "ImportConflictsPanel.prevBtn.toolTipText")); // NOI18N
        prevBtn.setFocusable(false);
        prevBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        prevBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        prevBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prevBtnActionPerformed(evt);
            }
        });
        jToolBar1.add(prevBtn);

        org.openide.awt.Mnemonics.setLocalizedText(nextBtn, org.openide.util.NbBundle.getMessage(ImportConflictsPanel.class, "ImportConflictsPanel.nextBtn.text")); // NOI18N
        nextBtn.setToolTipText(org.openide.util.NbBundle.getMessage(ImportConflictsPanel.class, "ImportConflictsPanel.nextBtn.toolTipText")); // NOI18N
        nextBtn.setFocusable(false);
        nextBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        nextBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        nextBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextBtnActionPerformed(evt);
            }
        });
        jToolBar1.add(nextBtn);
        jToolBar1.add(jSeparator2);
        jToolBar1.add(filler1);

        org.openide.awt.Mnemonics.setLocalizedText(acceptCurrentValueBtn, org.openide.util.NbBundle.getMessage(ImportConflictsPanel.class, "ImportConflictsPanel.acceptCurrentValueBtn.text")); // NOI18N
        acceptCurrentValueBtn.setToolTipText(org.openide.util.NbBundle.getMessage(ImportConflictsPanel.class, "ImportConflictsPanel.acceptCurrentValueBtn.toolTipText")); // NOI18N
        acceptCurrentValueBtn.setFocusable(false);
        acceptCurrentValueBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        acceptCurrentValueBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        acceptCurrentValueBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                acceptCurrentValueBtnActionPerformed(evt);
            }
        });
        jToolBar1.add(acceptCurrentValueBtn);

        org.openide.awt.Mnemonics.setLocalizedText(acceptImportValueBtn, org.openide.util.NbBundle.getMessage(ImportConflictsPanel.class, "ImportConflictsPanel.acceptImportValueBtn.text")); // NOI18N
        acceptImportValueBtn.setToolTipText(org.openide.util.NbBundle.getMessage(ImportConflictsPanel.class, "ImportConflictsPanel.acceptImportValueBtn.toolTipText")); // NOI18N
        acceptImportValueBtn.setFocusable(false);
        acceptImportValueBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        acceptImportValueBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        acceptImportValueBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                acceptImportValueBtnActionPerformed(evt);
            }
        });
        jToolBar1.add(acceptImportValueBtn);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 352, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private int getNextRowPosition(){
        int[] rows = table.getSelectedRows();
        if (rows.length > 0){
            for (int i = rows[rows.length - 1] + 1; i < table.getRowCount(); i++){
                if (!(table.getValueAt(i, 0) instanceof ImportConflicts)){
                    return i;
                }
            }
        }
        return -1;
    }
    
    private int getPrevRowPosition(){
        int row = table.getSelectedRow();
        if (row > 1){
            for (int i = row - 1; i >= 0; i--){
                if (!(table.getValueAt(i, 0) instanceof ImportConflicts)){
                    return i;
                }
            }
        }
        return -1;
    }
    
    public void next() {
        int row = getNextRowPosition();
        if (row > 0){
            table.setRowSelectionInterval(row, row);
        }
        row = getNextRowPosition();
        nextBtn.setEnabled(row >= 0);
    }
    
    public void prev(){
        int row = getPrevRowPosition();
        if (row >= 0){
            table.setRowSelectionInterval(row, row);
        }
        row = getPrevRowPosition();
        prevBtn.setEnabled(row >= 0);
    }
    
    
    private void nextBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextBtnActionPerformed
        next();
    }//GEN-LAST:event_nextBtnActionPerformed

    private void prevBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prevBtnActionPerformed
        prev();
    }//GEN-LAST:event_prevBtnActionPerformed

    private void acceptCurrentValueBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_acceptCurrentValueBtnActionPerformed
        for (int i = 0; i < table.getRowCount(); i++){
           acceptImportValue(i, false);
        }
    }//GEN-LAST:event_acceptCurrentValueBtnActionPerformed

    private void acceptImportValueBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_acceptImportValueBtnActionPerformed
        for (int i = 0; i < table.getRowCount(); i++) {
            acceptImportValue(i, true);
        }
    }//GEN-LAST:event_acceptImportValueBtnActionPerformed

    private void acceptBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_acceptBtnActionPerformed
        acceptSelectedRows();
    }//GEN-LAST:event_acceptBtnActionPerformed
    
    
    private void acceptSelectedRows(){
        if (canAccept()){
            int col = table.getSelectedColumn();
            int[] rows = table.getSelectedRows();
            for (int i : rows) {
                acceptImportValue(i, col == 1);
            }
        }
    }
    
    private void enableAcceptBtn(){
        boolean isEnable = true;
        int[] rows = table.getSelectedRows();
        for (int i : rows){
            if (table.getValueAt(i, 0) instanceof ImportConflicts){
                isEnable = false;
                break;
            }
        }
        acceptBtn.setEnabled(canAccept() && isEnable);
    }
    private boolean canAccept(){
        int[] cols = table.getSelectedColumns();
        return cols.length == 1;
    }
    private void acceptImportValue(int row, boolean value){
        Object rowValue = ((TableModel) table.getModel()).getRowValue(row);
        if (rowValue instanceof ImportConflict) {
            ImportConflict conflict = (ImportConflict) rowValue;
            conflict.setAcceptImportValue(value);
        }
        check();
    }

    @Override
    public void check() {
        boolean valid = true;
        for (int i = 0; i < table.getRowCount(); i++) {
            Object rowValue = ((TableModel) table.getModel()).getRowValue(i);
            if (rowValue instanceof ImportConflict) {
                ImportConflict conflict = (ImportConflict) rowValue;
                if (conflict.getAcceptImportValue() == null){
                    valid = false;
                    break;
                }
            }
        }
        
        if (valid){
            stateManager.ok();
        } else {
            stateManager.error("All strings must be resolved");
        }
        
        changeSupport.fireChange();
    }
    
    class TableModel extends DefaultTableModel {

        public TableModel(Object[][] data) {
            super(data, COLUMN_NAMES);
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }

        //for copy from table
        @Override
        public Object getValueAt(int row, int column) {
            Object obj = super.getValueAt(row, 0);

            if (obj instanceof ImportConflict) {
                ImportConflict importConflict = (ImportConflict) obj;
                IMultilingualStringDef string = importConflict.getContainer().getString();
                StringBuilder sb = new StringBuilder();
                switch (column) {
                    case 0:
                        sb.append(importConflict.getCurrentInformation(false));
                        sb.append("\n");
                        sb.append(string.getValue(importConflict.getLanguage()));
                        break;
                    case 1:
                        sb.append(importConflict.getImportInformation(false));
                        sb.append("\n");
                        sb.append(importConflict.getImportValue());
                        break;
                }
                return sb.toString();
            }

            return super.getValueAt(row, column);
        }
        
        public Object getRowValue(int row){
            return super.getValueAt(row, 0);
        }

    }
    
    class ColumnSpanningCellRenderer extends DefaultTableCellRenderer  {
        private final JPanel importConflictsPanel = new JPanel(new BorderLayout(0, 0));
        JPanel stringInformationPanel = new JPanel(new BorderLayout());
        private final JTextPane textArea = new JTextPane();
        private final JScrollPane scroll = new JScrollPane(textArea);
        
        private final JPanel importConflictPanel = new JPanel();
        private final JLabel acceptIconLabel = new JLabel();
        private final JTextArea valueArea = new JTextArea();
        private final JLabel optionLabel = new JLabel();
        
        private final Border border = BorderFactory.createMatteBorder(0, 0, 0, 1, Color.GRAY);
        private final Border b = BorderFactory.createEmptyBorder(0, 2, 0, 0);
        
        protected ColumnSpanningCellRenderer() {
            importConflictPanel.setLayout(new BorderLayout());
            importConflictPanel.add(optionLabel, BorderLayout.NORTH);
            importConflictPanel.add(acceptIconLabel, BorderLayout.WEST);
            importConflictPanel.add(valueArea, BorderLayout.CENTER);
            valueArea.setLineWrap(true);
            acceptIconLabel.setVerticalAlignment(JLabel.TOP);
            

            stringInformationPanel.add(textArea);
            stringInformationPanel.setOpaque(false);
            scroll.setViewportView(stringInformationPanel);
            scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
            scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scroll.setBorder(BorderFactory.createEmptyBorder());
            scroll.setViewportBorder(BorderFactory.createEmptyBorder());
            scroll.setOpaque(false);
            scroll.getViewport().setOpaque(false);

            textArea.setBorder(BorderFactory.createEmptyBorder());
            textArea.setMargin(new Insets(0, 0, 0, 0));
            textArea.setEditable(false);
            textArea.setFocusable(false);
            textArea.setOpaque(false);

            importConflictsPanel.setBackground(textArea.getBackground());
            importConflictsPanel.setOpaque(true);
            importConflictsPanel.add(scroll);
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            int mrow = table.convertRowIndexToModel(row);
            Object rowValue = ((TableModel)table.getModel()).getRowValue(mrow);
            
            if (rowValue instanceof ImportConflicts) {
                ImportConflicts conflicts = (ImportConflicts) rowValue;
                textArea.setText(conflicts.toString());

                table.setRowHeight(row, textArea.getPreferredSize().height);
                Rectangle cr = table.getCellRect(0, column, false);
                scroll.getViewport().setViewPosition(cr.getLocation());
                if (row == 0){
                    textArea.setBorder(b);
                }
                

                Dimension d = textArea.getPreferredSize();
                textArea.setMinimumSize(new Dimension(Short.MAX_VALUE, d.height));
                textArea.setPreferredSize(new Dimension(Short.MAX_VALUE, d.height));
                if (table.isRowSelected(row)) {
                    importConflictsPanel.setBackground(table.getSelectionBackground());
                    stringInformationPanel.setBackground(table.getSelectionBackground());
                    textArea.setBackground(table.getSelectionBackground());
                    textArea.setForeground(table.getSelectionForeground());
                } else {
                    importConflictsPanel.setBackground(table.getBackground());
                    stringInformationPanel.setBackground(table.getBackground());
                    textArea.setBackground(table.getBackground());
                    textArea.setForeground(table.getForeground());
                }
                return importConflictsPanel;
            }
            
            if (rowValue instanceof ImportConflict) {
                ImportConflict conflict = (ImportConflict) rowValue;
                if (column < COLUMN_NAMES.length - 1) {
                    importConflictPanel.setBorder(BorderFactory.createCompoundBorder(border, b));
                }
                Boolean acceptImportValue = conflict.getAcceptImportValue(); 
                if (acceptImportValue == null) {
                    acceptIconLabel.setIcon(RadixWareIcons.MLSTRING_EDITOR.TRANSLATION_NOT_CHECKED.getIcon());
                } 
                switch (column){
                    case 0:
                        optionLabel.setText(conflict.getCurrentInformation(true));
                        valueArea.setText("\"" + conflict.getHtmlCurrentValue() + "\"");
                        if (acceptImportValue != null) {
                            acceptIconLabel.setIcon(acceptImportValue
                                    ? RadixWareIcons.DELETE.DELETE.getIcon()
                                    : RadixWareIcons.DIALOG.OK.getIcon());
                        }
                        break;
                    case 1: 
                        optionLabel.setText(conflict.getImportInformation(true));
                        valueArea.setText("\"" + conflict.getHtmlImportValue() + "\"");
                        if (acceptImportValue != null) {
                            acceptIconLabel.setIcon(!acceptImportValue
                                    ? RadixWareIcons.DELETE.DELETE.getIcon()
                                    : RadixWareIcons.DIALOG.OK.getIcon());
                        }
                    break;
                }
                
                  
                table.setRowHeight(row, importConflictPanel.getPreferredSize().height);
                valueArea.setForeground(c.getForeground());
                valueArea.setBackground(c.getBackground());
                importConflictPanel.setBackground(c.getBackground());
                stringInformationPanel.setBackground(c.getBackground());
                optionLabel.setForeground(c.getForeground());
                return importConflictPanel;
            } else {
                if (column < COLUMN_NAMES.length - 1) {
                    setBorder(BorderFactory.createCompoundBorder(border, b));
                }
            }

            return this;
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton acceptBtn;
    private javax.swing.JButton acceptCurrentValueBtn;
    private javax.swing.JButton acceptImportValueBtn;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JButton nextBtn;
    private javax.swing.JButton prevBtn;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables
}
