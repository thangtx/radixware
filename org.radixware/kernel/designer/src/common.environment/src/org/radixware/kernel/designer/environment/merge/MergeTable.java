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
package org.radixware.kernel.designer.environment.merge;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.prefs.Preferences;
import javax.swing.DefaultCellEditor;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.openide.DialogDescriptor;
import org.radixware.kernel.common.svn.RadixSvnException;
import org.radixware.kernel.common.svn.SVN;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.designer.common.dialogs.text.TextEditor;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;

public class MergeTable extends JTable implements MouseMotionListener {

    private final AbstractMergeChangesOptions options;
    private final int index;
    private final boolean isSrc;
    private final DefaultTableModel jTableModel;
    private final List<MergeItemWrapper> items;
    private final static String P_KEY = "MergeTableKey";
    private boolean isMustSaveColumnWidth = false;
    private final String path;
//////    private final String mlbPath;
    private final long lastRevision;
    private final boolean isMlbWithDifferentFormat;
    private MergePanel panel;
    boolean lastIsEquel;

    MergeTable(MergePanel panel, AbstractMergeChangesOptions options, int index, List<MergeItemWrapper> items, boolean isSrc_, long lastRevision_, boolean lastIsEquel, boolean isMlbWithDifferentFormat) {
        this.options = options;
        this.panel = panel;
        this.index = index;
        this.isSrc = isSrc_;
        this.items = items;
        this.lastRevision = lastRevision_;
        this.lastIsEquel = lastIsEquel;
        this.isMlbWithDifferentFormat = isMlbWithDifferentFormat;
        String[] colNames;

        if (isSrc) {

            path = options.getFromPathByIndex(index);
//////            mlbPath = options.getList().get(index).getFromMlbPath();
        } else {
            path = options.getToPathByIndex(index);
//////            mlbPath = options.getList().get(index).getToMlbPath();
        }

        if (isSrc) {
            colNames = new String[]{"Select", "Revision", "Author", "Date", "Comment", "View", "Compare"};
        } else {
            colNames = new String[]{"Revision", "Author", "Date", "Comment", "View", "Compare"};
        }

        jTableModel = new javax.swing.table.DefaultTableModel(new Object[][]{}, colNames) {
            @Override
            public Class getColumnClass(int columnIndex) {
                return java.lang.Object.class;
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                int i = 0;
                if (isSrc) {
                    if (columnIndex == 0) {
                        if (rowIndex == this.getRowCount() - 1) //return lastRevision!=-1 && !MergeTable.this.isMlbWithDifferentFormat;
                        {
                            return false;
                        }
                        return true;
                    }
                    i++;
                }
                if (columnIndex < 4 + i) {
                    return false;
                }
                if (columnIndex == 5 + i && rowIndex == this.getRowCount() - 1) {
                    return lastRevision != -1 && !MergeTable.this.isMlbWithDifferentFormat;
                }
                return true;
            }

            @Override
            public void setValueAt(Object aValue, int row, int column) {
                super.setValueAt(aValue, row, column);
            }
        };
        this.setModel(jTableModel);
        getTableHeader().setReorderingAllowed(false);
        fillTable();

        Preferences pref = Utils.findPreferencesWithoutException(P_KEY + (isSrc ? "From" : "To"));
        if (pref != null) {
            isMustSaveColumnWidth = false;

            final TableColumnModel tableColumnModel2 = getColumnModel();
            TableColumn col;
            for (int i = 0; i < tableColumnModel2.getColumnCount(); i++) {
                col = tableColumnModel2.getColumn(i);
                col.setPreferredWidth(pref.getInt("ColumnWidth" + String.valueOf(i), col.getPreferredWidth()));
                col.setWidth(pref.getInt("ColumnWidth" + String.valueOf(i), col.getWidth()));
            }

            isMustSaveColumnWidth = true;
        }

        TableColumnModelListener tableColumnModelListener = new TableColumnModelListener() {
            @Override
            public void columnAdded(TableColumnModelEvent e) {
            }

            @Override
            public void columnRemoved(TableColumnModelEvent e) {
            }

            @Override
            public void columnMoved(TableColumnModelEvent e) {
            }

            @Override
            public void columnSelectionChanged(ListSelectionEvent e) {
            }

            @Override
            public void columnMarginChanged(ChangeEvent e) {
                Preferences preferences = org.radixware.kernel.common.utils.Utils.findOrCreatePreferences(P_KEY + (isSrc ? "From" : "To"));
                if (preferences != null && isMustSaveColumnWidth) {
                    TableColumn col;

                    for (int i = 0; i < getColumnModel().getColumnCount(); i++) {
                        col = getColumnModel().getColumn(i);
                        preferences.putInt("ColumnWidth" + String.valueOf(i), col.getWidth());
                    }
                }
            }
        };
        getColumnModel().addColumnModelListener(tableColumnModelListener);
        setBackground(new Color(242, 241, 240));
        this.setRowHeight(24);
        init();
    }

    private void init() {
        registerComponent(this);
        this.addMouseMotionListener(this);

        JCheckBox checkBox = new JCheckBox();
        //final int size1 = 60;
        final int size = 70;
        final int size2 = 80;

        int i = 0;
        if (isSrc) {
            i++;
        }
        TableColumn c;
        c = this.getColumnModel().getColumn(4 + i);
        c.setCellEditor(new ButtonEditor(checkBox, "View", null));
        c.setCellRenderer(new ButtonRenderer("View", 4 + i));
        c.setMaxWidth(size);
        c.setMinWidth(size);

        c = this.getColumnModel().getColumn(5 + i);
        c.setCellEditor(new ButtonEditor(checkBox, "Compare", null));
        c.setCellRenderer(new ButtonRenderer("Compare", 5 + i));
        c.setMaxWidth(size2);
        c.setMinWidth(size2);

        int columnInd = 0;
        if (isSrc) {
            c = this.getColumnModel().getColumn(0);
            c.setCellEditor(new DefaultCellEditor(checkBox));

            checkBox.setHorizontalAlignment(SwingConstants.CENTER);
            c.setCellRenderer(new BooleanRendere());
            columnInd++;
        }
        for (int j = 0; j < 4; j++) {
            TableColumn c2;
            c2 = this.getColumnModel().getColumn(columnInd + j);
            c2.setCellRenderer(new SimpleCellRenderer());
        }

        this.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private class SimpleCellRenderer extends DefaultTableCellRenderer implements TableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                if (lastIsEquel && row == table.getRowCount() - 1 && table.getRowCount() == 1) {
                    setForeground(table.getForeground());
                    setBackground(Color.GREEN);
                } else {
                    setForeground(table.getSelectionForeground());
                    setBackground(table.getSelectionBackground());
                }
            } else {
                setForeground(table.getForeground());
                setBackground(UIManager.getColor("Button.background"));
                if (lastIsEquel && row == table.getRowCount() - 1) {
                    setBackground(Color.GREEN);
                }

            }
            setText(String.valueOf(value));

            return this;
        }
    }

    public void getSelectedRevisions(List<Long> prior, List<Long> curr) {
        if (isSrc) {
            boolean prionIsSet = false;
            for (int i = this.getRowCount() - 1; i >= 0; i--) {
                Boolean fl = (Boolean) getValueAt(i, 0);

                if (fl != null && fl) {
                    long p = lastRevision;
                    if (i != this.getRowCount() - 1) {
                        p = items.get(i + 1).revision;
                    }

                    if (prionIsSet && !curr.isEmpty()) {
                        curr.set(curr.size() - 1, items.get(i).revision);
                    } else {
                        curr.add(items.get(i).revision);
                        prior.add(p);
                        prionIsSet = true;
                    }
                } else {
                    prionIsSet = false;
                }

            }
        }

    }

    public void getSelectedRevisionsForConvertMlbAsXml(List<Long> prior, List<String> priorAsStr, List<Long> curr, List<String> currAsStr) {
        if (isSrc) {
            boolean prionIsSet = false;
            for (int i = this.getRowCount() - 1; i >= 0; i--) {
                Boolean fl = (Boolean) getValueAt(i, 0);

                if (fl != null && fl) {
                    long p = lastRevision;
                    if (i != this.getRowCount() - 1) {
                        p = items.get(i + 1).revision;
                    }

                    if (prionIsSet && !curr.isEmpty()) {
                        curr.set(curr.size() - 1, items.get(i).revision);
                        currAsStr.set(currAsStr.size() - 1, items.get(i).xmlAsString);
                    } else {
                        curr.add(items.get(i).revision);
                        currAsStr.add(items.get(i).xmlAsString);

                        prior.add(p);
                        priorAsStr.add(items.get(i + 1).xmlAsString);

                        prionIsSet = true;
                    }
                } else {
                    prionIsSet = false;
                }

            }
        }

    }

    private class BooleanRendere extends JCheckBox
            implements TableCellRenderer, Serializable {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            this.setHorizontalAlignment(SwingConstants.CENTER);
            setSelected((value != null && ((Boolean) value).booleanValue()));

            //if (!isSelected)
//                if (lastIsEquel && row == table.getRowCount()-1){
//                    this.setBackground(new Color(0, 150, 0));
//                }
            return this;
        }
    }

    private void fillTable() {
        int n = items.size();
        jTableModel.setRowCount(n);

        for (int i = 0; i < n; i++) {
            MergeItemWrapper item = items.get(i);
            int j = 0;
            if (isSrc) {
                j++;
            }
            this.setValueAt(String.valueOf(item.revision), i, 0 + j);
            this.setValueAt(String.valueOf(item.author), i, 1 + j);
            this.setValueAt(String.valueOf(item.date), i, 2 + j);
            this.setValueAt(String.valueOf(item.comment), i, 3 + j);
        }
        if (n > 0) {
            this.getSelectionModel().setSelectionInterval(0, 0);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
        JComponent c = (JComponent) e.getComponent();
        {
            int row = e.getY() / this.getRowHeight();
            int x = 0;
            int x0 = e.getX();
            for (int i = 0; i < this.getColumnCount(); i++) {
                TableColumn col = this.getColumnModel().getColumn(i);
                if (x0 > x && x + col.getWidth() > x0) {
                    break;
                }
                x += col.getWidth();
            }
            if (row == -1 || row >= items.size()) {
                c.setToolTipText("");
                return;
            }

            String s = //String.valueOf(ind) + "   " +
                    items.get(row).toString();
            c.setToolTipText(s);

        }
    }

    private static void registerComponent(JComponent c) {
        InputMap imap = c.getInputMap();
        boolean removeKeyStroke = false;
        KeyStroke[] ks = imap.keys();
        if (ks == null || ks.length == 0) {
            imap.put(KeyStroke.getKeyStroke(
                    KeyEvent.VK_BACK_SLASH, 0), "backSlash");
            removeKeyStroke = true;
        }
        ToolTipManager.sharedInstance().registerComponent(c);
        if (removeKeyStroke) {
            imap.remove(KeyStroke.getKeyStroke(
                    KeyEvent.VK_BACK_SLASH, 0));
        }
    }

    private void view(int curRow) throws RadixSvnException {
        //String content = SVN.getFileAsStr(this.options.getRepository(), path, items.get(curRow).revision);
        String content;
        if (items.get(curRow).xmlAsString != null) {
            content = items.get(curRow).xmlAsString;
        } else {
            byte[] bytes = SVN.getFile(this.options.getRepository(), path, items.get(curRow).revision);

            try {
                content = new String(bytes, "UTF8");
            } catch (UnsupportedEncodingException ex) {
                MergeUtils.messageError(ex);
                return;
            }
        }
        String content2 = "";
//////            try{
//////                //content2 = SVN.getFileAsStr(this.options.getRepository(), mlbPath, items.get(curRow).revision);
//////                byte[] bytes2 = SVN.getFile(this.options.getRepository(), mlbPath, items.get(curRow).revision);
//////                content2 = new String (bytes2, "UTF8");                        
//////            }
//////            catch(SVNException | UnsupportedEncodingException ex){
//////               // DialogUtils.messageError(ex);
//////            }    
//////        
        if (content2.isEmpty()) {
            //if (!this.options.getList().get(index).isMlStringExists()){            
            TextEditor.editTextModal(content,
                    "View Revision - " + String.valueOf(items.get(curRow).revision),
                    "text/xml", false, new Object[]{DialogDescriptor.CLOSED_OPTION});
        } else {
            JPanel p = new JPanel();
            p.setLayout(new BorderLayout());
            JTabbedPane tabSet = new JTabbedPane();
            p.add(tabSet, BorderLayout.CENTER);

            JEditorPane textPane1 = new JEditorPane();
            textPane1.setEditable(false);
            JScrollPane sp1 = new JScrollPane();
            tabSet.add("Definition", sp1);
            sp1.setViewportView(textPane1);

            textPane1.setContentType("text/xml");
            textPane1.setText(content);

            JEditorPane textPane2 = new JEditorPane();
            textPane2.setEditable(false);
            JScrollPane sp2 = new JScrollPane();
            tabSet.add("Multilanguage strings", sp2);
            sp2.setViewportView(textPane2);

            textPane2.setContentType("text/xml");
            textPane2.setText(content2);

            ModalDisplayer dg = new ModalDisplayer(p, "View Revision - " + String.valueOf(items.get(curRow).revision), new Object[]{DialogDescriptor.CLOSED_OPTION});
            dg.showModal();
        }
    }

    private void compare(int curRow) throws RadixSvnException {

        String s1;
        if (items.get(curRow).xmlAsString != null) {
            s1 = items.get(curRow).xmlAsString;
        } else {
            byte[] bytes = SVN.getFile(this.options.getRepository(), path, items.get(curRow).revision);
            try {
                s1 = new String(bytes, "UTF8");
            } catch (UnsupportedEncodingException ex) {
                MergeUtils.messageError(ex);
                return;
            }
        }

        long rev;
        if (curRow == this.getRowCount() - 1) {
            rev = lastRevision;
        } else {
            rev = items.get(curRow + 1).revision;
        }

        String s2 = "";
        if (curRow + 1 < items.size() && items.get(curRow + 1).xmlAsString != null) {
            s2 = items.get(curRow + 1).xmlAsString;
        } else {
            //String s2 = SVN.getFileAsStr(this.options.getRepository(), path, rev);
            byte[] bytes2 = SVN.getFile(this.options.getRepository(), path, rev);
            try {
                s2 = new String(bytes2, "UTF8");
            } catch (UnsupportedEncodingException ex) {
                MergeUtils.messageError(ex);
                //return;
            }
        }

//        String s11 = "";
//        //s11 = SVN.getFileAsStr(this.options.getRepository(), mlbPath, items.get(curRow).revision);
//        
////////        try{
////////            byte[] bytes11 = SVN.getFile(this.options.getRepository(), mlbPath, items.get(curRow).revision);
////////            s11 = new String (bytes11, "UTF8");
////////       }
////////        catch(SVNException | UnsupportedEncodingException ex){
////////            DialogUtils.messageError(ex);
////////            return;
////////        }        
//             
//        String s22 = "";
//        //s22 = SVN.getFileAsStr(this.options.getRepository(), mlbPath, rev);
//        
////////        try{
////////            byte[] bytes22 = SVN.getFile(this.options.getRepository(), mlbPath, rev);
////////        s22 = new String (bytes22, "UTF8");
////////       }
////////        catch(SVNException | UnsupportedEncodingException ex){
////////            DialogUtils.messageError(ex);
////////            return;
////////        }        
//                            
//        
//        //if (this.options.getList().get(index).isMlStringExists()){
//        if (!s11.isEmpty() || !s22.isEmpty() ){
//
//            
//            String t21 = options.getList().get(index).getDef().getQualifiedName() + " ("+
//                        (isSrc ? options.getFromBranchShortName() : options.getToBranchShortName()) + ") "
//                            + String.valueOf(rev);             
//            String t22 = t21 + " - multilanguage string";
//            
//            List<String> ls1 = new ArrayList<String>(2); ls1.add(s1); ls1.add(s11);
//            List<String> ls2 = new ArrayList<String>(2); ls2.add(s2); ls2.add(s22);
//            
//            List<String> tabTitles = new ArrayList<String>(2); tabTitles.add("Definition"); tabTitles.add("Multilanguage string");
//            List<String> titles2 = new ArrayList<String>(2); titles2.add(t21); titles2.add(t22);
//            List<String> titles1 = new ArrayList<String>(2); titles1.add(getRowTitle(curRow)); titles1.add(getRowTitle(curRow) + " - multilanguage string");
//            DiffManager.diff(ls2, ls1, tabTitles, titles2, titles1, "Revision Compare");
//            
//        }
//        else
        {

            String t2 = options.getNameByIndex(index) + " ("
                    + (isSrc ? options.getFromBranchShortName() : options.getToBranchShortName()) + ") "
                    + String.valueOf(rev);

            DiffManager.diff(s2, s1, t2, getRowTitle(curRow), "Revision Compare");
        }
    }

    public String getRevisionAsStr(int x) throws RadixSvnException {
        //return SVN.getFileAsStr(this.options.getRepository(), path, items.get(x).revision);
        if (items.get(x).xmlAsString != null) {
            return items.get(x).xmlAsString;
        }
        byte[] bytes11 = SVN.getFile(this.options.getRepository(), path, items.get(x).revision);
        try {
            return new String(bytes11, "UTF8");
        } catch (UnsupportedEncodingException ex) {
            MergeUtils.messageError(ex);
            return "";
        }
    }

//    public  String getMlbRevisionAsStr(int x) throws SVNException{
//        
//////////        try{
//////////            byte[] bytes11 = SVN.getFile(this.options.getRepository(), mlbPath, items.get(x).revision);
//////////         return new String (bytes11, "UTF8");
//////////       }
//////////        catch(SVNException ex){
//////////         return "";
//////////        } 
//////////        catch(Exception ex){
//////////         DialogUtils.messageError(ex);
//////////         return "";
//////////        }
//        
//        return "";
//
//    }    
    public String getRowTitle(int x) {
        return options.getNameByIndex(index) + " ("
                + (isSrc ? options.getFromBranchShortName() : options.getToBranchShortName()) + ") "
                + String.valueOf(items.get(x).revision);
    }

    private class ButtonEditor extends DefaultCellEditor {

        protected JButton button;
        private int currRow;
        private String currText;

        public ButtonEditor(JCheckBox checkBox, String text, String hint) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (currRow != -1) {
                        try {
                            if ("View".equals(currText)) {
                                view(currRow);
                            } else {
                                compare(currRow);
                            }
                        } catch (RadixSvnException ex) {
                            MergeUtils.messageError(ex);
                        }

                    }
                }
            });
            currRow = -1;
            button.setText(text);
            this.currText = text;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            if (isSelected) {
                button.setForeground(table.getSelectionForeground());
                button.setBackground(table.getSelectionBackground());
            } else {
                button.setForeground(table.getForeground());
                button.setBackground(table.getBackground());
            }
            currRow = row;
            button.setText(currText);

            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return currText;
        }
    }

    private class ButtonRenderer extends JButton implements TableCellRenderer {

        String title;
        int col;

        public ButtonRenderer(String title, int col) {
            setOpaque(true);
            this.title = title;
            this.col = col;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(UIManager.getColor("Button.background"));
            }
            setText(title);

            if (column == 5 + (isSrc ? 1 : 0) && table.getRowCount() - 1 == row && (lastRevision == -1 || isMlbWithDifferentFormat)) {
                this.setEnabled(false);
            } else {
                this.setEnabled(true);
            }

            return this;
        }
    }
}
