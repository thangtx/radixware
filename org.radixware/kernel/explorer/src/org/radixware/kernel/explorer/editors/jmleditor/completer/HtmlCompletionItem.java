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

package org.radixware.kernel.explorer.editors.jmleditor.completer;

import com.trolltech.qt.gui.QFontMetrics;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.scml.ScmlCompletionProvider.CompletionItem;


public class HtmlCompletionItem {

    private Column column1;
    private Column column2;
    private CompletionItem item;
    private boolean isDeprecated=false;
    private final int columnSpace ;
    private final static String STRIKE_TAG="<s>";
    private final static String STRIKE_TAG_END="</s>";
    private final static String BOLD_TAG="<b>";
    private final static String BOLD_TAG_END="</b>";
    private String resultText;
    private boolean showTextAsItIs = false;

    private class Column {

        private String text;
        private int size = -1;
        private QFontMetrics fontMetrics;
        private QFontMetrics boldMetrics;
        
        Column(final String text, final QFontMetrics nm, final QFontMetrics bm, final boolean isDeprecated, final boolean isStrikeAllText) {
            this( text,  nm,  bm); 
            if(isDeprecated){
                this.text=isStrikeAllText ? STRIKE_TAG + this.text + STRIKE_TAG_END : strikeText(this.text) ;
            }            
        }

        Column(final String text, final QFontMetrics nm, final QFontMetrics bm) {
            this.fontMetrics = nm;
            this.boldMetrics = bm;
            this.text = changeColor(text);           
        }

        private int getColumnWidth() {
            int res = 0, start;
            final StringBuilder sbOs=new StringBuilder();
            final StringBuilder sbBs=new StringBuilder();
            if (text != null && !text.isEmpty()) {
                final String[] arrStr = text.split(BOLD_TAG_END);
                for (String part : arrStr) {
                    if ((start = part.indexOf(BOLD_TAG)) != -1) {
                        sbOs.append(part.substring(0, start));
                        sbBs.append(part.substring(start + 3, part.length()));
                    } else {
                        sbOs.append(part);
                    }
                }

                res = boldMetrics.width(sbBs.toString());
                res += fontMetrics.width(sbOs.toString());
            }
            return res;
        }

       /* private String getHint(String s) {
            //int start=0,end=0;
            //QRegExp reg5 = new QRegExp();
            //reg5.setPattern("<b>[^</b>]*");
            s = s.replaceAll("<[^>]*", "");
            s = s.replaceAll(">", "");
            //s=s.replaceAll(BOLD_TAG_END, "");
            //while((start=s.indexOf("<"))!=-1){
            //    if((end=s.indexOf(">"))!=-1)
            //       s=(s.substring(0, start)+s.substring(end+1, s.length()));
            //}
            return s;
        }*/

        private String getColumnName(int columnWidth) {
            int len = 0, start = 0, end = 0;
            String bs, os ;
            
            QFontMetrics bfm = boldMetrics;            
            QFontMetrics ofm = fontMetrics;
            columnWidth = columnWidth - ofm.boundingRect("...").width();
            while ((end < text.length()) && (end >= 0) && (start = text.indexOf(BOLD_TAG, end)) != -1) {
                os = text.substring(end, start);
                Object obj = calcColumn(os, ofm, columnWidth, len);
                if (obj instanceof String) {
                    return text.substring(0, end) + (String) obj;
                }
                len = len + ((Integer) obj).intValue();
                if ((start < text.length()) && (end = text.indexOf(BOLD_TAG_END, start)) != -1) {
                    bs = text.substring(start + 3, end);
                    obj = calcColumn(bs, bfm, columnWidth, len);
                    if (obj instanceof String) {
                        return text.substring(0, start/*
                                 * end
                                 */) + (String) obj;
                    }
                    len = len + ((Integer) obj).intValue();
                    end += 4;
                }
            }
            if (end < text.length() && (end >= 0)) {

                os = text.substring(end, text.length());
                final Object obj = calcColumn(os, ofm, columnWidth, len);
                if (obj instanceof String) {
                    return text.substring(0, end) + (String) obj;
                } else {
                    len += (Integer) obj;
                }
            }
            return text;
        }

        int getSize() {
            if (size < 0) {
                size = getColumnWidth();
            }
            return size;
        }

        String getText() {
            return text;
        }
        
        void setText(final String text) {
            this.text=text;
        }
    }

    private final QFontMetrics fontMetrics;
    public HtmlCompletionItem(final CompletionItem item, final QFontMetrics fontMetrics, final QFontMetrics boldMetrics, final boolean isDepricated) {
        this.item = item;
        this.column1 = new Column(item.getLeadDisplayText(), fontMetrics,boldMetrics,isDepricated,(item instanceof SuggestCompletionItem));
        this.column2 = new Column(item.getTailDisplayText(), fontMetrics,boldMetrics);
        this.isDeprecated=isDepricated;
        this.fontMetrics=fontMetrics;
        columnSpace=fontMetrics.height()+ItemDelegate.SPACE_HEIGHT;
    }
    
    public HtmlCompletionItem(final CompletionItem item, final QFontMetrics fontMetrics, final QFontMetrics boldMetrics, final boolean isDepricated, final boolean showTextAsItIs) {
        this(item, fontMetrics, boldMetrics, isDepricated);
        this.showTextAsItIs = showTextAsItIs;
    }

    public HtmlCompletionItem(final CompletionItem item, final String type, final QFontMetrics fontMetrics, final QFontMetrics boldMetrics, final boolean isDepricated) {
        this.item = item;
        this.column1 = new Column(type, fontMetrics,boldMetrics,isDepricated,(item instanceof SuggestCompletionItem));
        this.column2 = new Column(null, fontMetrics,boldMetrics);
        this.isDeprecated=isDepricated;
        this.fontMetrics=fontMetrics;
        columnSpace=fontMetrics.height()+ItemDelegate.SPACE_HEIGHT;
    }

    public HtmlCompletionItem(final String text1, final String text2, final QFontMetrics fontMetrics, final QFontMetrics boldMetrics, final boolean isDepricated) {
        this.column1 = new Column(text1, fontMetrics,boldMetrics);
        this.column2 = new Column(text2, fontMetrics,boldMetrics);
        this.isDeprecated=isDepricated;
        this.fontMetrics=fontMetrics;
        columnSpace=fontMetrics.height()+ItemDelegate.SPACE_HEIGHT;
    }

    public String getColumn1() {
        return column1.getText();
    }
    
    public void setColumn1(final String text) {
         column1.setText(text);
    }

    public String getColumn2() {
        return column2.getText();
    }
    
    public CompletionItem getCompletionItem() {
        return item;
    }
    
    public int getLenghtInChars() {
        int res = column1.getText() != null ? column1.getText().length() : 0;
        return res + (column2.getText() != null ? column2.getText().length() : 0);
    }
    
    public String getPlainText() {
        StringBuilder sb = new StringBuilder();
        String col1 = getColumn1();
        if (col1 != null && !col1.isEmpty()) {
            sb.append(col1);
        }
        String col2 = getColumn2();
        if (col2 != null && !col2.isEmpty()) {
            if (sb.length() > 0) {
                sb.append(" ");
            }
            sb.append(col2);
        }
        return sb.toString();
    }
    
    public int getLenght() {
        final int space = column1.getSize() > 0 && column2.getSize() > 0 ? columnSpace : 0;
        return column1.getSize() + column2.getSize() + space;
    }
    
    private String strikeText(String text){
        final int index=text.indexOf('(');                
        if(index!=-1){
           text=STRIKE_TAG+text.substring(0,index)+STRIKE_TAG_END+text.substring(index);
        }else{
           text=STRIKE_TAG+text+STRIKE_TAG_END; 
        }
        return text;
    }

    public boolean isShowTextAsItIs() {
        return showTextAsItIs;
    }
    
    public String getText(final int tableSize, final boolean forCompletion) {
        if (resultText != null) {
            return resultText;
        }
        
        if (showTextAsItIs) {
            resultText = getPlainText();
            return resultText;
        }
        
        final List<String> columns = calcColumnName(tableSize);
        final String name = columns.get(0);
        final String moduleName = columns.get(1);

        final StringBuilder sbRes = new StringBuilder("<table WIDTH=\"");
        sbRes.append(tableSize).append("\" HEIGHT=\"")
                .append(fontMetrics.height() + ItemDelegate.SPACE_HEIGHT)
                .append("\"><tr><td valign=\"middle\" align=\"left\">");
        if (isDeprecated) {
            if (forCompletion) {
                sbRes.append(strikeText(name));
            } else {
                sbRes.append(STRIKE_TAG).append(name).append(STRIKE_TAG_END);
            }
            //name= forCompletion ? strikeText( name):STRIKE_TAG + name + STRIKE_TAG_END;
        } else {
            sbRes.append(name);
        }
        sbRes.append("</td><td valign=\"middle\" align=\"right\">")
                .append(moduleName).append("</td></tr></table>");
        resultText = sbRes.toString();
        
        return resultText;
    }
        
    private List<String> calcColumnName(final int tableSize) {
        String columnhtml1 = column1.getText(), columnhtml2 = column2.getText();
        int colSize1 = column1.getSize();
        int colSize2 = column2.getSize();
        int columnSpace = colSize1 > 0 && colSize2 > 0 ? this.columnSpace : 0;
        final int minColumnSize = tableSize / 2 - columnSpace;
        if ((colSize1 + colSize2 + columnSpace) > tableSize) {
            if (colSize1 < minColumnSize) {
                colSize2 = tableSize - colSize1 - columnSpace;
                columnhtml2 = column2.getColumnName(colSize2);
            } else if (colSize2 < minColumnSize) {
                colSize1 = tableSize - colSize2 - columnSpace;
                columnhtml1 = column1.getColumnName(colSize1);
            } else {
                colSize1 = tableSize / 2 - columnSpace;
                colSize2 = tableSize / 2 - columnSpace;
                columnhtml1 = column1.getColumnName(colSize1);
                columnhtml2 = column2.getColumnName(colSize2);
            }
        }
        final List<String> columns = new ArrayList<>();
        columns.add(columnhtml1);
        columns.add(columnhtml2);
        return columns;
    }

    public static String changeColor(final String s) {
       int start, n = 0;
        final StringBuilder res = new StringBuilder();
        if (s != null) {
            final List<String> strList = parseString(s);
            for (int i = 0; i < strList.size(); i++) {
                String str = strList.get(i);
                if (str.charAt(0)=='<') {
                    if (((start = str.indexOf("color=\"", n)) != -1) && !str.contains("#") && (str.length() >= start + 7 + 8)) {
                       // int n1=str.indexOf("\"", start + 7);
                        str = str.substring(0, start + 7) + "#" + str.substring(start + 7, /*start + 7 + n1) + str.substring(start + 7 + n1+2,*/ str.length());
                    //if (((start = str.indexOf("color=\"", n)) != -1) && (str.length() >= start + 7 + 8)) {
                    //    str = str.substring(0, start + 7) + "#" + str.substring(start + 7, start + 7 + 6) + str.substring(start + 7 + 8, str.length());
                    }
                }
                res.append(str);
            }
        }
        return res.toString();
    }

    private static Object calcColumn( final String s, final QFontMetrics fm, final int columnWidth, int len) {
        final List<String> strList = parseString(s);
        for (int i = 0; i < strList.size(); i++) {
            final String str = strList.get(i);
            if (str.charAt(0)!='<') {
                final String rightStr = getRightStr(str);
                final int strLen = fm.boundingRect(rightStr).width();
                len += strLen;
                if (len > columnWidth) {
                    int h = 0, j = 0;
                    while ((h < strLen - (len - columnWidth)) && (rightStr.length() > j)) {
                        h += fm.charWidth(rightStr, j);
                        j++;
                    }
                    if (str.length() > j) {
                        if (str.substring(0, j).contains("&lt;")) {
                            j += 3;
                        }
                        if (str.substring(0, j).contains("&gt;")) {
                            j += 3;
                        }
                        if (j > 0) {
                            j--;
                        }
                    } else//  if(str.length()<j)
                    {
                        j = str.length();
                    }
                    return getStrFromList(strList, i) + str.substring(0, j) + "...";
                }
            }
        }
        return len;
    }

    private static String getStrFromList(final List<String> strList, final int n) {       
        if (n == 0) {
            return "";
        }
        //String res = "";
        final StringBuilder sb=new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append( strList.get(i));
            //res += strList.get(i);
        }
        for (int i = n; i < strList.size(); i++) {//clouse all html tags
            if (strList.get(i).startsWith("<")) {
                sb.append(strList.get(i));
                //res += strList.get(i);
            }
        }
        return sb.toString();
    }

    private static String getRightStr(String s) {
        return s.replaceAll("&lt;", "<").replaceAll("&gt;", ">");
    }

    private static List<String> parseString(final String s) {
        int start = 0, end = 0;
        final LinkedList<String> strList = new LinkedList<>();
        while ((end < s.length()) && (end >= 0) && (start = s.indexOf('<', end)) != -1) {
            if (end < start) {
                strList.add(s.substring(end, start));
            }
            if ((end = s.indexOf('>', start)) != -1) {
                end++;
                strList.add(s.substring(start, end));
            }
        }
        if ((end < s.length()) && (end >= 0)) {
            strList.add(s.substring(end, s.length()));
        }
        return strList;
    }
}
