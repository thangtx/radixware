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

package org.radixware.kernel.designer.ads.localization.source;

import java.awt.Color;
import java.awt.Component;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.Icon;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.types.Arr;
import org.radixware.kernel.designer.ads.localization.RowString;


public class MlsTableUi {

    private TableModel tableModel;
    private List<RowString> mlStrings = new ArrayList<>();;
    private List<EIsoLanguage> sourceLangs;
    private List<EIsoLanguage> translLangs;
    private JTable table;

    public MlsTableUi(final JTable table) {
        this.table=table;
        createTableUi();
    }

    public final void createTableUi() {
       table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
       table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
       table.getTableHeader().setReorderingAllowed(false);
    }
    
    public void open(final List<EIsoLanguage> sourceLangs,final List<EIsoLanguage> translLangs){
        this.sourceLangs=sourceLangs;
        this.translLangs=translLangs;
        tableModel = new TableModel(sourceLangs,translLangs);
        updateTable();
    }
  
    public void update(final List<RowString> mlStrings,final List<EIsoLanguage> sourceLangs,final List<EIsoLanguage> translLangs) {
        this.sourceLangs=sourceLangs;
        this.translLangs=translLangs;
        this.mlStrings=mlStrings;
        tableModel = new TableModel(mlStrings,sourceLangs,translLangs);
        updateTable();
    }
    
    private void updateTable() {
       table.setModel(tableModel);
       TableColumnModel columnModel = table.getColumnModel();
       TableColumn column = columnModel.getColumn(0);
       column.setCellRenderer(new MlsCellRenderer());
       column.setPreferredWidth(45);
       column.setMinWidth(45);
       column.setMaxWidth(45);
       column.setResizable(true);

       for(int i=0;i<sourceLangs.size();i++){
            columnModel.getColumn(i+1).setCellRenderer(new MlsCellRenderer());
       }
    }

    public void updateFilter(final FilterSettings filterSettings) {
        if(tableModel==null) return;
        synchronized(this){
            tableModel.clear();
            tableModel.addAllRow(FilterUtils.getFiltredData(mlStrings, filterSettings, sourceLangs, translLangs));
        }
    }
     
    public RowString getRowString(final int row) {
        return (RowString)tableModel.getRow(row);
    }

    public boolean isHtml(final int row) {
        String s=(String) tableModel.getValueAt(row, 1);
        if((s!=null )&& (s.trim().startsWith("<"))){
            s=s.substring(1).trim();
            //String tagName="";
            StringBuilder sb=new StringBuilder();
            while(!(s.charAt(0)==' ' || s.charAt(0)=='>' || s.charAt(0)=='\n')){
                sb.append(s.substring(0, 1));
                s=s.substring(1);
            }
            return isStartWithHtmlTag(sb.toString());
        }
        return false;
    }

    private boolean isStartWithHtmlTag(final String s){
        return (s!=null )&& getHtmlTagList().contains(s)/*(s.trim().startsWith("<html>"))*/;
    }

    public int getRowIndex(final RowString row) {
        return tableModel.getRowIndex(row);
    }

    public void fireTableDataChanged() {
        tableModel.fireTableDataChanged();
    }

    public void fireTableCellUpdated(final int row,final int col) {
        tableModel.fireTableCellUpdated(row, col);
    }

    public void fireTableRowsUpdated(final int row) {
        tableModel.fireTableRowsUpdated(row, row);
    }

    public void refreshTableModel(){
        synchronized(this){
            tableModel.clear();
            tableModel.addAllRow(mlStrings);
        }
    }
    
    public void addAllRow(final List<RowString> strings, final FilterSettings filter) {
        synchronized(this){
            mlStrings.addAll(strings);
            tableModel.addAllRow(FilterUtils.getFiltredData(strings,filter,sourceLangs,translLangs));
        }
    }
    
    public void addRow(final RowString rowString) {
        tableModel.addRow(rowString);
    }

    public void deleteRow(final RowString rowString) {
        tableModel.removeRow(rowString);
    }
    
    public List<RowString> getRows() {
        synchronized(this){
            return new ArrayList<>(mlStrings);
        }
    }
    
    public List<RowString> getFiltedRows(){
        synchronized(this){
            return new ArrayList<>(tableModel.getRows());
        }
    }
    
    public void clear(){
        synchronized(this){
            mlStrings.clear();
            tableModel.clear();
        }
    }
    
    private class MlsCellRenderer extends DefaultTableCellRenderer{

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            RowString rowString = ((TableModel)table.getModel()).getRow(row);
            if (value instanceof Icon){
                setText("");
                setIcon((Icon)value);
                setHorizontalAlignment(SwingConstants.CENTER);
            }
            if (value instanceof String){
                String str = (String) value;
                setText(showHtmlTag(str));
                if(rowString.getWasEdit()){
                    setForeground(Color.blue);
                }else if(rowString.isVersionChanged()){
                    setForeground(Color.red);
                }else{
                    setForeground(Color.black);
                }
            }
            if (rowString.isChangeAgreedString(sourceLangs, translLangs)){
                setBackground(new Color(255,200,200));
            } else if (rowString.isStatusChanged(sourceLangs, translLangs)){
                setBackground(new Color(255,255,224));
            } else {
                setBackground(UIManager.getColor("Table.background"));
            }
            
            if (isSelected){
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            }
            
            setToolTipText(rowString.getToolTip(sourceLangs, translLangs));
            return this;
        }
        
        private String showHtmlTag(final String s) {
            if(s.indexOf("<html>")==0){
                return "\u200B"+s;
            }
            return s;
        }
    }
    
    private class TableModel extends AbstractTableModel implements ListSelectionListener {
        private List<String> columns = new ArrayList<>();
        private final Vector<RowString> rows = new Vector<> ();
        private List<EIsoLanguage> sourceLangs;
        private List<EIsoLanguage> translLangs;

        public TableModel(final List<EIsoLanguage> sourceLangs,final List<EIsoLanguage> translLangs) {
            this.sourceLangs=sourceLangs;
            this.translLangs=translLangs;
            columns.add(NbBundle.getMessage(MlsTableUi.class, "STATE"));
            for(EIsoLanguage lang: sourceLangs)
                columns.add(NbBundle.getMessage(MlsTableUi.class, "SOURCE_TEXT_IN")+ " " + lang.getName());
        }

        public TableModel(final List<RowString> rows,final List<EIsoLanguage> sourceLangs,final List<EIsoLanguage> translLangs) {
            this(sourceLangs, translLangs);
            this.rows.addAll(rows);
        }
        
        public void setColumns(final List<String> columns) {
            this.columns=columns;
        }

        @Override
        public int getRowCount() {
            return rows.size();
        }

        public List<RowString> getRows() {
            return rows;
        }

        @Override
        public int getColumnCount() {
           return columns.size();
        }

        @Override
        public boolean isCellEditable(final int row, final int col) {
            return false;
        }

        @Override
        public Object getValueAt(final int rowIndex, final int columnIndex) {
            if(columnIndex==0){
                return rows.get(rowIndex).getIcon(sourceLangs,translLangs);
            }else{
                return rows.get(rowIndex).getValue(sourceLangs.get(columnIndex-1));
            }
        }

        public RowString getRow(final int rowIndex) {
            return rows.get(rowIndex);
        }

        public int getRowIndex(RowString row) {
            return rows.indexOf(row);
        }

        @Override
        public void setValueAt(final Object value, final int row, final int col) {
            //fireTableCellUpdated(row, col);
        }

        @Override
        public void valueChanged(final ListSelectionEvent e) {
            fireTableDataChanged();
        }

        public void addAllRow(final List<RowString> strings) {
            int firstRow = rows.size();
            rows.addAll(strings);
            this.fireTableRowsInserted(firstRow, rows.size());
        }
        
        public void addRow(final RowString str) {
            rows.add(str);
            this.fireTableRowsInserted(rows.size(), rows.size());
        }

        public void insertRow(final int index, final RowString str) {
            rows.add(index,str);
            this.fireTableRowsInserted(rows.size(), rows.size());
        }

        public void removeRow(final int row) {
            rows.remove(row);
            this.fireTableRowsDeleted(row, row);
        }

        public void removeRow(final RowString row) {
            rows.remove(row);
            this.fireTableDataChanged();
        }

        public void clear() {
            rows.clear();
            this.fireTableDataChanged();
        }

        @Override
        public String getColumnName(final int col) {
            return columns.get(col);
        }
      }

     private List<String> getHtmlTagList(){
          List<String> tagList=new ArrayList<>();
          tagList.add("!DOCTYPE");
          tagList.add("!--");
          tagList.add("a");
          tagList.add("addr");
          tagList.add("acronym");
          tagList.add("address");
          tagList.add("applet");
          tagList.add("b");
          tagList.add("basefont");
          tagList.add("bdo");
          tagList.add("big");
          tagList.add("blink");
          tagList.add("blockquote");
          tagList.add("body");
          tagList.add("br");
          tagList.add("center");
          tagList.add("cite");
          tagList.add("code");
          tagList.add("dl");
          tagList.add("del");
          tagList.add("dfn");
          tagList.add("dir");
          tagList.add("div");
          tagList.add("em");
          tagList.add("embed");
          tagList.add("fieldset");
          tagList.add("font");
          tagList.add("form");
          tagList.add("frameset");
          tagList.add("h1");
          tagList.add("h2");
          tagList.add("h3");
          tagList.add("h4");
          tagList.add("h5");
          tagList.add("h6");
          tagList.add("head");
          tagList.add("hr");
          tagList.add("html");
          tagList.add("i");
          tagList.add("iframe");
          tagList.add("img");
          tagList.add("input");
          tagList.add("ins");
          tagList.add("kbd");
          tagList.add("label");
          tagList.add("legend");
          tagList.add("link");
          tagList.add("map");
          tagList.add("marquee");
          tagList.add("nobr");
          tagList.add("noembed");
          tagList.add("noframes");
          tagList.add("noscript");
          tagList.add("object");
          tagList.add("ol");
          tagList.add("optgroup");
          tagList.add("p");
          tagList.add("plaintext");
          tagList.add("pre");
          tagList.add("q");
          tagList.add("s");
          tagList.add("samp");
          tagList.add("script");
          tagList.add("select");
          tagList.add("small");
          tagList.add("span");
          tagList.add("strike");
          tagList.add("strong");
          tagList.add("sub");
          tagList.add("sup");
          tagList.add("table");
          tagList.add("tbody");
          tagList.add("textarea");
          tagList.add("tfoot");
          tagList.add("title");
          tagList.add("tt");
          tagList.add("u");
          tagList.add("ul");
          tagList.add("var");
          tagList.add("xmp");
          return tagList;
     }
}