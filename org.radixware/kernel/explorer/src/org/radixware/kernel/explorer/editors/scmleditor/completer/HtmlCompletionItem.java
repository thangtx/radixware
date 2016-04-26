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

package org.radixware.kernel.explorer.editors.scmleditor.completer;

import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QFontMetrics;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.explorer.editors.scml.IScmlCompletionItem;


public class HtmlCompletionItem {
    private Column column1;
    private Column column2;
    private IScmlCompletionItem item;
    
    private final static int columnSpace=20;
    
    private class Column{
        private String text;
        private int size;
        private QFont font;

        Column(String text, QFont font){
            this.font=font;
            this.text=changeColor(text);
            size=getColumnWidth();
        }            
        
        
         private int getColumnWidth(){
             int res=0;
             StringBuilder sbBs=new StringBuilder();
             StringBuilder sbOs=new StringBuilder();             
             if(text!=null || text.isEmpty()){
                 int end=0, start=text.indexOf("<b>",end);
                 while((end<text.length()) && (end>=0) && (start!=-1)){
                     sbOs.append(text.substring(end, start));
                     //os+=text.substring(end, start);
                     end=text.indexOf("</b>",start);
                     if(end!=-1){
                        sbBs.append(text.substring(start+3, end));
                        //bs+=text.substring(start+3, end);
                        end+=4;
                     }else{
                         sbBs.append(text.substring(start/*+3*/, text.length()));
                        //bs+=text.substring(start/*+3*/, text.length());
                        break;
                     }
                     start=text.indexOf("<b>",end);
                 }
                 if((end<text.length()) && (end>=0)){
                     sbOs.append(text.substring(end, start));
                     //os+=text.substring(end,text.length());
                 }
                 font.setBold(true);
                 String bs, os;
                 bs= getHint(sbBs.toString());
                 bs=getRightStr(bs);
                 QFontMetrics fm=new QFontMetrics(font);
                 res=fm.boundingRect(bs).width();

                 os= getHint(sbOs.toString());
                 os=getRightStr(os);
                 font.setBold(false);
                 fm=new QFontMetrics(font);
                 res+=fm.boundingRect(os).width();
             }
             return res;
         }
         
         private  String getHint(String s){           
            int start=s.indexOf('<'),end;
            while(start!=-1){
                end=s.indexOf('>');
                if(end!=-1){
                   s=(s.substring(0, start)+s.substring(end+1, s.length()));
                }
                start=s.indexOf('<');
            }
            return s;
        }
         
         private  String getColumnName(int columnWidth){
            int len=0, start, end=0;
            String bs, os;
            font.setBold(true);
            QFontMetrics bfm=new QFontMetrics(font);
            font.setBold(false);
            QFontMetrics ofm=new QFontMetrics(font);
            columnWidth=columnWidth-ofm.boundingRect("...").width();
            start=text.indexOf("<b>",end);
            while((end<text.length())&&(end>=0)&&(start!=-1)){
                os=text.substring(end, start);
                Object obj=calcColumn(os,ofm,columnWidth,len);
                if(obj instanceof String)
                    return text.substring(0,end)+(String)obj;
                len=len+((Integer) obj).intValue();
                end=text.indexOf("</b>",start);
                if((start<text.length()) && (end!=-1)){
                    bs=text.substring(start+3, end);
                    obj=calcColumn(bs,bfm,columnWidth,len);
                    if(obj instanceof String){
                        return text.substring(0,start/*end*/)+(String)obj;
                    }
                    len=len+((Integer) obj).intValue();
                    end+=4;
                }
                start=text.indexOf("<b>",end);
            }
            if(end<text.length()&&(end>=0)){
                os=text.substring(end, text.length());
                Object obj=calcColumn(os,ofm,columnWidth,len);
                if(obj instanceof String)
                    return text.substring(0,end)+(String)obj;
                else
                    len+=(Integer) obj;
            }
            return text;
        }
        
        int getSize(){            
           return size;
        }
        
        String getText(){
            return text;
        }
    }
    
    public HtmlCompletionItem(IScmlCompletionItem item,QFont font){
        this.item=item;
        this.column1=new Column(item.getLeadText(),font);
        this.column2=new Column(item.getTailText(),font);       
    } 
    
    public HtmlCompletionItem(IScmlCompletionItem item,String type,QFont font){
        this.item=item;
        this.column1=new Column(type,font);
        this.column2=new Column(null,font);       
    } 
    
    public HtmlCompletionItem(String text1,String text2,QFont font){
        this.column1=new Column(text1,font);
        this.column2=new Column(text2,font);       
    } 
    
    public String getColumn1(){
        return column1.getText();
    }
    
    public String getColumn2(){
        return column2.getText();
    }
       
    public IScmlCompletionItem getCompletionItem(){
        return item;
    }
        
    public int getLenght(){
        int space= column1.getSize()>0 && column2.getSize()>0 ? columnSpace:0;
        return column1.getSize()+column2.getSize()+space;
    }
    
    public String getText(int tableSize){
        List<String> columns= calcColumnName(tableSize);
        return "<table WIDTH=\""+tableSize+"\" HEIGHT=\""+ItemDelegate.height+"\"><tr><td valign=\"middle\" align=\"left\">"+columns.get(0)+"</td><td valign=\"middle\" align=\"right\">"+columns.get(1)+"</td></tr></table>";
    } 
    
    private List<String> calcColumnName(int tableSize){
        String columnhtml1=column1.getText(), columnhtml2=column2.getText();
        int colSize1=column1.getSize();
        int colSize2= column2.getSize();
        int minColumnSize=tableSize/2-columnSpace;
        if((colSize1+colSize2+columnSpace)>tableSize){
            if(colSize1<minColumnSize){
                 colSize2=tableSize-colSize1-columnSpace;
                 columnhtml2=column2.getColumnName(colSize2);
            }else if(colSize2 <minColumnSize){
                colSize1=tableSize-colSize2-columnSpace;
                columnhtml1=column1.getColumnName(colSize1);
            }else{
                colSize1=tableSize/2-columnSpace;
                colSize2=tableSize/2-columnSpace;
                columnhtml1=column1.getColumnName(colSize1);
                columnhtml2=column2.getColumnName(colSize2);
            }
        }
        List<String> columns=new ArrayList<>();
        columns.add(columnhtml1);columns.add(columnhtml2);
        return columns;
    }

     public static String changeColor(String s){
            int start,n=0;
            //String res="";
            StringBuilder sb=new StringBuilder();
            if(s!=null){                
                List<String> strList=parseString( s);
                for(int i=0;i<strList.size();i++){
                     String str=strList.get(i);
                     if(str.charAt(0)=='<'){
                         start=str.indexOf("color=\"",n);
                         if((start!=-1) && (str.length()>start+7+8)){
                             str= str.substring(0, start+7)+"#"+str.substring(start+7, start+7+6)+str.substring(start+7+8, str.length());
                         }
                     }
                     sb.append(str);
                     //res+=str;
                }
            }
            return sb.toString();
        }
    
    public static String removeColor(String s){
        StringBuilder sb=new StringBuilder();
        if(s!=null){
            int start,n=0;
            List<String> strList=parseString( s);
            for(int i=0;i<strList.size();i++){
                 String str=strList.get(i);
                 if(str.charAt(0)=='<'){
                     start=str.indexOf("color=\"",n);
                     if((start!=-1)&&(str.length()>start+7+8)){
                         int l=str.substring(start+7).indexOf("\"");
                         str= str.substring(0, start)+str.substring(l, str.length());
                     }
                 }
                 sb.append(str);//res+=str;
            }
        }
        return sb.toString();
    }

    private static Object calcColumn(String s,QFontMetrics fm,int columnWidth,int len){
         List<String> strList=parseString( s);
         for(int i=0;i<strList.size();i++){
             String str=strList.get(i);
             if(str.charAt(0)!='<'){
                 String rightStr=getRightStr(str);
                 int strLen=fm.boundingRect(rightStr).width();
                 len+=strLen;
                 if(len>columnWidth){
                     int h=0,j=0;
                     while((h<strLen-(len-columnWidth))&&(rightStr.length()>j)){
                         h+=fm.charWidth(rightStr, j);
                         j++;
                     }
                     if(str.length()>j){
                         if(str.substring(0, j).contains("&lt;"))j+=3;
                         if(str.substring(0, j).contains("&gt;"))j+=3;
                         if(j>0)
                            j--;
                     }else//  if(str.length()<j)
                         j=str.length();
                     return getStrFromList(strList,i) + str.substring(0, j)+ "...";
                 }
             }
         }
         return len;
    }
    
    private static String getStrFromList(List<String> strList,int n){
        if(n==0) 
            return "";
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<n;i++){
            sb.append(strList.get(i));
        }
        for(int i=n;i<strList.size();i++){//clouse all html tags
            if(strList.get(i).startsWith("<"))
                sb.append(strList.get(i));
        }
        return sb.toString();
    }

    private static String getRightStr(String s){
            s=s.replaceAll("&lt;", "<");
            s=s.replaceAll("&gt;", ">");
            return s;
   }
    
   private static List<String> parseString(String s){
            int start,end=0;
            List<String> strList=new LinkedList<>();
            start=s.indexOf('<',end);
            while((end<s.length())&&(end>=0) && (start!=-1)){
                if(end < start)
                   strList.add(s.substring(end,start));
                end=s.indexOf('>',start);
                if(end!=-1){
                   end++;
                   strList.add(s.substring(start, end));
                }
                start=s.indexOf('<',end);
            }
            if((end< s.length())&&(end>=0)){
                strList.add(s.substring(end,s.length()));
            }
            return strList;
   }
    
}
