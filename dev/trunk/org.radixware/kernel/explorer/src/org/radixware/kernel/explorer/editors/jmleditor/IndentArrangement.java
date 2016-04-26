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

package org.radixware.kernel.explorer.editors.jmleditor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.explorer.editors.xscmleditor.XscmlEditor;


public class IndentArrangement {
    private List<Block> blockList;
    private List<String> reservedWords;
    private List<String> lexList;
    private int curLexIndex;

    public IndentArrangement(){
        blockList=new LinkedList<>();
        reservedWords=new ArrayList<>();
        reservedWords.add("for");
        reservedWords.add("if");
        reservedWords.add("else");
        reservedWords.add("while");
        reservedWords.add("do");
        reservedWords.add("{");
        reservedWords.add("case");
        reservedWords.add("switch");
    }

    class Block{
        private int startIndent;//начальный отступ блока
        private int indent;//отступ внутри блока
        private boolean isMultyBlock;//является ли блок многострочным
        private String name;//имя блока

        Block(int startIndent,String name){
            isMultyBlock=false;
            this.startIndent=startIndent;
            this.name=name;
        }

        int getStartIndent(){
            return startIndent;
        }

        int getIndent(){
            return indent;
        }

        String getBlockName(){
            return name;
        }

        void setIndent(int indent){
            this.indent=indent;
        }

        void setIsMultiBlock(boolean isMultyBlock){
            this.isMultyBlock=isMultyBlock;
        }

        boolean getIsMultiBlock(){
            return isMultyBlock;
        }
    }
    
    public String calcIndent(String s, boolean isOpenBrace, boolean isCloseBrase,boolean isCaseBlock,long[][] tagPos){
        //String res="";
        StringBuilder sb=new StringBuilder();
        int n=0;
        curLexIndex=-1;
        blockList.clear();
        lexList=scan(s,tagPos);

        calcBlocks(true);
        if(!blockList.isEmpty()){
            if(isOpenBrace || isCloseBrase || isCaseBlock){
                Block block=blockList.get(blockList.size()-1);
                while((blockList.size()-1>0) &&  (!isCaseBlock) && (block.getBlockName().equals("case"))){
                    blockList.remove(blockList.size()-1);
                    block=blockList.get(blockList.size()-1);
                }
                if(block.getIsMultiBlock() && !isCloseBrase && !isCaseBlock){
                    n=block.getIndent();
                }else{
                    n=block.getStartIndent();
                }
            }else{
                n=blockList.get(blockList.size()-1).getIndent();
            }
        }
        for(int i=0;i<n;i++){
            sb.append(" ");
           //res+=" ";
        }
        return sb.toString();
   }

    private boolean calcBlocks(boolean isMultyBlock){
        String lex=getNextLex();
        while(lex!=null){
            if(reservedWords.contains(lex)){
                int startBlockIndex=curLexIndex;
                //начало блока
                if(checkRule1(lex,"if") || checkRule1(lex,"for") || checkRule1(lex,"while") || checkRule1(lex,"switch") ||
                   checkRule2(lex,"do") || checkRule2(lex,"else") || checkRule2(lex,"{")|| checkCase(lex)){
                    lex=getNextLex();
                    String blockName=lexList.get(startBlockIndex);
                    //расчет начального отступа у блока
                    int startIndent;
                    if("case".equals(blockName) && (!blockList.isEmpty())/*&&isPrevBlockCase()*/){
                        startIndent=calcIdentForCaseBlock(startBlockIndex);
                    }else{
                        startIndent=calcIndentForBlock(startBlockIndex);
                    }

                    //создание нового блока                    
                    Block b=new Block(startIndent,lexList.get(startBlockIndex));
                    b.setIndent(startIndent+XscmlEditor.STR_TAB.length());
                    boolean isNextLexOpenBrace=isNextLexEquals(lex,"{");
                    b.setIsMultiBlock("{".equals(blockName) ||  "case".equals(blockName) || isNextLexOpenBrace);
                    blockList.add(b);

                    if(lex==null || (isNextLexOpenBrace && (!"{".equals(blockName)) && getNextLex()==null) ){
                        return  true;
                    }
                    curLexIndex--;
                    
                    boolean res=calcBlocks(b.getIsMultiBlock());//расчет блоков внутри текущего блока
                    if (!res && !isMultyBlock) {//текущий блок закончился
                        if(!blockList.isEmpty())
                            blockList.remove(blockList.size()-1);
                        return res;
                    } 
                    if(getNextLex()==null)
                         return res;
                    curLexIndex--;
                }
            }else if("}".equals(lex)){//конец блока
                if(!blockList.isEmpty()){
                    Block block=blockList.get(blockList.size()-1);
                    while("case".equals(block.getBlockName())){
                        blockList.remove(blockList.size()-1);
                        block=blockList.get(blockList.size()-1);
                    }
                    blockList.remove(blockList.size()-1);
                }
                return false;
            }else if((!isMultyBlock) && !lexIsComment(lex) && (!"\n".equals(lex))){//конец блока
                if(!blockList.isEmpty())
                    blockList.remove(blockList.size()-1);
                while(lex!=null && !reservedWords.contains(lex) && !"}".equals(lex)&& !";".equals(lex)){
                    lex=getNextLex();
                }
                if((lex!=null)&&(!";".equals(lex)))
                    curLexIndex--;
                return false;
            }else if(isMultyBlock && ";".equals(lex)){//запоминаем отступ последней строки в текущем блоке                
                int ident=calcIndentForBlock(curLexIndex);
                Block b;
                if(!blockList.isEmpty()){
                    b=blockList.get(blockList.size()-1);
                }else{
                    b=new Block(ident,"");
                    blockList.add(b);
                }
                b.setIndent(ident);
                if(getNextLex()==null)
                    return true;
                curLexIndex--;
            }
            lex=getNextLex();
        }
        return false;
    }

    private int calcIdentForCaseBlock(int startBlockIndex){
        int res;
        if(getNextLex()==null){
            Block block=blockList.get(blockList.size()-1);
            String blockName=block.getBlockName();
            switch (blockName) {
                case "case":
                    res=blockList.get(blockList.size()-1).getStartIndent();
                    blockList.remove(blockList.size()-1);
                    break;
                case "switch":
                    res=blockList.get(blockList.size()-1).getIndent();
                    break;
                default:
                    res=calcIndentForBlock(startBlockIndex);
                    break;
            }
        } else
             res=calcIndentForBlock(startBlockIndex);
        
        curLexIndex--;
        return res;
    }

    private boolean isNextLexEquals(String lex,String s){
        int startIndex=curLexIndex;
        while((lex!=null)&&(("\n".equals(lex)) || lexIsComment(lex))){
            lex=getNextLex();
        }
        boolean res=false;
        if(lex!=null){
            res=lex.equals(s);
            if(!res){
                curLexIndex=startIndex;
            }
        }
        return res;
    }

    private boolean lexIsComment(String lex){
        return lex.startsWith("//") || lex.startsWith("/*");
    }

    private int calcIndentForBlock(int startStr){
        int blockIndent=0;
        startStr--;
        if(startStr>=0){
            String lex=lexList.get(startStr);
            while(lex!=null && !lex.equals("\n") ){
                startStr--;
                if(startStr>=0)
                    lex=lexList.get(startStr);
                else
                    break;
            }
            if(lex!=null && (lex.equals("\n") ||startStr==-1))
                startStr++;
            int n=lexList.size();
            if(startStr<n && startStr>=0){
                lex=lexList.get(startStr);
                while(lex!=null && lex.equals(" ")){
                    blockIndent++;
                    startStr++;
                    if(startStr<n){
                        lex=lexList.get(startStr);
                    } else{
                        break;
                    }
                }
            }
        }
        return blockIndent;
    }

    private String getNextLex(){
        curLexIndex++;
        while((curLexIndex<lexList.size()) && (lexList.get(curLexIndex).equals(" ")/*||(lexList.get(curLex).startsWith("//"))||(lexList.get(curLex).startsWith("/*"))*/))
           curLexIndex++;
        if(curLexIndex<lexList.size())
           return lexList.get(curLexIndex);
        return null;
    }

    private boolean checkRule1(String lex,String reservWord){//for,if, while  (reservWord(smth))
        if(lex!=null && lex.equals(reservWord)){
            lex=getNextLex();
            if(lex!=null && isNextLexEquals(lex,"(") && checkRoundBrackets()){
                return  true;
            }

        }
        return false;
    }    

    private boolean checkRoundBrackets(){
        int res=1;
        String lex=getNextLex();
         while(lex!=null){
             if(lex.equals("(")){
                 res++;
             }else if(lex.equals(")")){
                 res--;
                 if(res<=0) break;
             }
             lex=getNextLex();
        }
        return res==0; 
    }

    private boolean checkRule2(String lex,String reservWord){//else, do, {
         if(lex!=null && lex.equals(reservWord)){
             if(lex.equals("else")){
                 lex=getNextLex();
                 if((lex!=null) && isNextLexEquals(lex,"if")){
                     lex=lexList.get(curLexIndex);
                     return checkRule1(lex,"if");
                 }
                 curLexIndex--;
             }
            return true;            
        }
        return false;
    }

    private boolean checkCase(String lex){
        if((lex!=null) && lex.equals("case")){
            do{
                lex=getNextLex();
            }while((lex!=null)&&(!lex.equals(":")) );
            if(lex!=null && lex.equals(":")){
                return true;
            }
        }
        return false;
    }

    public static List<String> scan(String s,long[][] tagPos){
        List<String> listStr=new LinkedList<>();

        int n=s.length(),t=0;
        for(int i=0;i<n;i++){
            StringBuilder sb=new StringBuilder();
            //String lex="";
            char ch=s.charAt(i);
            
            if(tagPos!=null && tagPos.length>t && tagPos[t][0]-1==i){                
                for(int j=0;j<tagPos[t][1]-tagPos[t][0];j++){
                  sb.append(ch);
                  //lex+=ch;
                  i++;
                  if(n>i){                      
                      ch=s.charAt(i);
                  }else{
                      break;
                  } 
                }
                i--;
                listStr.add(sb.toString());
                t++;
            }else if(isLetter(ch)){
                while(isDigit(ch) || isLetter(ch)){
                  sb.append(ch);
                  //lex+=ch;
                  i++;
                  if(n>i){                      
                      ch=s.charAt(i);
                  }else{
                      break;
                  }
                }
                i--;
                listStr.add(sb.toString());
            }else if((isDigit(ch))){
                while(isDigit(ch)){
                  sb.append(ch);
                  //lex+=ch;
                  i++;
                  if(n>i){                      
                      ch=s.charAt(i);
                  }else{
                      break;
                  }
                }
                i--;
                listStr.add(sb.toString());
            }else if(isSeparator( ch) || isSymbol( ch) || ch=='\n'){
                 sb.append(ch);
                 //lex+=ch;
                 listStr.add(sb.toString());
            }else if(isString(s,i,'\"')){
                sb.append(ch);
                //lex+=ch;
                i++;
                while((i<n) &&(!isString(s,i,'\"'))){
                     ch=s.charAt(i);
                     sb.append(ch);
                     //lex+=ch;
                     i++;
                }
                if(i<n){
                    ch=s.charAt(i);
                    sb.append(ch);
                    //lex+=ch;
                }
                listStr.add(sb.toString());
            }else if(isString(s,i,'\'')){
                sb.append(ch);
                //lex+=ch;
                i++;
                while((i<n) &&(!isString(s,i,'\''))){
                     ch=s.charAt(i);
                     sb.append(ch);
                     //lex+=ch;
                     i++;
                }
                if(i<n){
                    ch=s.charAt(i);
                    sb.append(ch);
                    //lex+=ch;
                }
                listStr.add(sb.toString());
            }
            else if(ch=='/'){
                sb.append(ch);
                //lex+=ch;
                i++;
                if(i<n){
                    ch=s.charAt(i);
                    if(ch=='/'){
                        while (ch != '\n'){
                          sb.append(ch);
                          //lex+=ch;
                          i++;
                          if(i >= n)break;
                          ch=s.charAt(i);
                        }
                        i--;
                    }else if(ch=='*'){
                        do{
                           sb.append(ch);
                           //lex+=ch;
                           i++;
                           if(i >= n)break;
                           ch=s.charAt(i); 
                        } while ( ch != '*' && (i+1 < n) && s.charAt(i+1) != '/');
                        if(i+1 < n){
                            i++;
                            ch=s.charAt(i);
                        }
                    }else
                        i--;
                }
                listStr.add(sb.toString());
            }
        }
        return listStr;
    }

    private static boolean isString(String s,int index,char searchChar){
        char ch=s.charAt(index);
        if(ch==searchChar){
            if(index-1>=0 &&  s.charAt(index-1)=='\\')
                return false;
            return true;
        }
        return false;
    }

    private static boolean isSeparator(char ch){
        return ch==';' || ch==',' || ch=='.' || ch=='{' || ch=='}' || ch=='[' ||
               ch==']' || ch=='(' || ch==')' || ch==' ' ;
    }

    private static boolean isSymbol(char ch){  // =:+-<>?*!%&|/~
        return ch=='=' ||ch==':' ||ch=='+' ||ch=='-' ||ch=='<' ||ch=='>' ||
               ch=='?' ||ch=='*' ||ch=='!' ||ch=='%' ||ch=='&' ||ch=='|' ||
               /*ch=='/'||*/ch=='~';
    }

    private static boolean isDigit(char ch){
        return ch=='0' ||ch=='1' ||ch=='2' ||ch=='3' ||ch=='4' ||ch=='5' ||
               ch=='6' ||ch=='7' ||ch=='8' ||ch=='9';
    }

    private static boolean isLetter(char ch){
        return ch=='a' ||ch=='b' ||ch=='c' ||ch=='d' ||ch=='e' ||ch=='f' ||
               ch=='g' ||ch=='h' ||ch=='i' ||ch=='j' ||ch=='k' ||ch=='l' ||
               ch=='m' ||ch=='n' ||ch=='o' ||ch=='p' ||ch=='q' ||ch=='r' ||
               ch=='s' ||ch=='t' ||ch=='u' ||ch=='v' ||ch=='w' ||ch=='x' ||
               ch=='y' ||ch=='z' ||ch=='_' ||
               ch=='A' ||ch=='B' ||ch=='C' ||ch=='D' ||ch=='E' ||ch=='F' ||
               ch=='G' ||ch=='H' ||ch=='I' ||ch=='J' ||ch=='K' ||ch=='L' ||
               ch=='M' ||ch=='N' ||ch=='O' ||ch=='P' ||ch=='Q' ||ch=='R' ||
               ch=='S' ||ch=='T' ||ch=='U' ||ch=='V' ||ch=='W' ||ch=='X' ||
               ch=='Y' ||ch=='Z';
    }
}
