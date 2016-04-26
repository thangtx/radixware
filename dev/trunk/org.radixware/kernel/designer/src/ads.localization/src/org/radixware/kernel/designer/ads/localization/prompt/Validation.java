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

package org.radixware.kernel.designer.ads.localization.prompt;

import java.util.ArrayList;
import java.util.List;
import org.openide.util.NbBundle;


public class Validation {
    private final List<String> sourceTexts;
    private final String translationText;
    private final List<String> warningList;
    private final List<String> promptsFromPB;    

    public Validation(final String s, final List<String> sourceTexts,final List<String> promptsFromPB) {
        translationText=s;
        this.sourceTexts=sourceTexts;
        this.promptsFromPB=promptsFromPB;
        warningList=new ArrayList<>();
    }

    public boolean isValid() {
        if(translationText==null)return true;
        boolean accelerationValidation=accelerationValidation();
        boolean punctuationValidation=punctuationValidation();
        boolean placeMarkValidation=placeMarkValidation();
        boolean phraseValidation=phraseValidation();
        boolean messageFormatValidation=messageFormatValidation();
        boolean htmlValidation=htmlValidation();

        return accelerationValidation && punctuationValidation &&htmlValidation&&
               placeMarkValidation && phraseValidation && messageFormatValidation;
    }

    public List<String> getWarningList() {
        return warningList;
    }

    //checking "&"
    private boolean accelerationValidation() {
        for(String s:sourceTexts){
            if(s!=null && s.indexOf("&")!=-1){
                if( translationText.indexOf("&")==-1){
                    warningList.add(NbBundle.getMessage(Validation.class, "ACCELERATOR_MISSING_IN_TRANSLATION"));
                    return false;
                }
            }
        }
        if( translationText.indexOf("&")!=-1){
            for(String s:sourceTexts){
                if(s!=null && s.indexOf("&")==-1){
                   warningList.add(NbBundle.getMessage(Validation.class, "ACCELERATOR_MISSING_IN_SRC"));
                   return false;
                }
            }
        }
        return true;
    }

    //checking punctuation
    private boolean punctuationValidation() {
        for(String s:sourceTexts){
            if(s!=null &&(checkPunctuation(s,translationText) || checkPunctuation(translationText,s))){
                    warningList.add(NbBundle.getMessage(Validation.class, "PUNCTUATION_WARNING"));
                    return false;
            }
        }
        return true;
    }

    private boolean checkPunctuation(final String str,final String str2) {
        return (!checkEndWith(str,str2,"."))||(!checkEndWith(str,str2,"!"))||(!checkEndWith(str,str2,"?"))||
               (!checkEndWith(str,str2,":")) ;
    }

    private boolean checkEndWith(final String str1, final String str2, final String ch) {
        if(str1!=null && str1.endsWith( ch)){
             if(str2!=null && !str2.endsWith( ch)){
                    return false;
            }
       }
        return true;
    }
    
    //checking %1, %2, ..., %n
    private boolean placeMarkValidation() {
        for(String s:sourceTexts){
            if((s!=null)&& (!checkPlaceMark(s,translationText) || !checkPlaceMark(translationText,s))){
                warningList.add(NbBundle.getMessage(Validation.class, "PLACE_MARK_WARNING"));
                return false;
            }
        }
        return true;
    }

    private boolean checkPlaceMark(final String s1, final String s2) {
        int i=0, len=s1.length();
        while((i<len) && (i=s1.indexOf("%",i)) != -1){
            final String ch=s1.substring(i,i+1)+getNumb( s1, i, len);
            if( s2.indexOf(ch)==-1){
                return false;
            }
            i++;
        }
        return true;
    }

    public static String getNumb(final String str1, int index, final int len) {
        String res="";
         while(((index+1) < len)&& isNumb(str1.substring(index+1,index+2))){
             res+=str1.substring(index+1,index+2);
             index++;
         }
        return res;
    }

    public static boolean isNumb(final String s) {
        return (s.equals("1"))||(s.equals("2"))||(s.equals("3"))||(s.equals("4"))||
               (s.equals("5"))||(s.equals("6"))||(s.equals("7"))||(s.equals("8"))||
               (s.equals("9"))||(s.equals("0"));
    }

    //checking phrases from Phrase Book
    public boolean phraseValidation() {
        if((promptsFromPB==null) ||(promptsFromPB.size()<=0))return true;
        for(String s:promptsFromPB)
            if(s!=null && translationText.toUpperCase().indexOf(s.toUpperCase())!=-1)//s.equalsIgnoreCase(translationText))
                return true;
        warningList.add(NbBundle.getMessage(Validation.class, "PB_SUGGESTION_IGNORES"));
        return false;
    }

    //checking message format//////////////////////////////////////////////////////////////////////////////////////////////////////
    private boolean messageFormatValidation() {
         for(String s:sourceTexts){
            if(s!=null && (!checkFormat1(s,translationText) || !checkFormat1(translationText,s)) ){
                warningList.add(NbBundle.getMessage(Validation.class, "FORMAT_WARNING"));
                return false;
            }
        }
        return true;
    }

    private boolean checkFormat(final String s1,final String s2) {
        int i=0, len=s1.length();
        while((i<len) &&((i=s1.indexOf("{",i)) != -1)){
            /*int n=i+2;
            if((n<len)&&((n=s1.indexOf("}",n)) != -1)){
               String ch=s1.substring(i,n);
               if( s2.indexOf(ch)==-1){
                   return false;
               }
            }else break;
            i=n;*/
            int n=i+2;
            if((n<len)&& isNumb(s1.substring(i+1,n)) ){
                if((n<len)&&((n=s1.indexOf("}",n)) != -1)){
                    String ch=s1.substring(i,n);
                    if( s2.indexOf(ch)==-1){
                        return false;
                    }
                }else break;

            }
            i=n;
        }
        return true;
    }

    private boolean checkFormat1(final String s1,final String s2) {
        int i=0, len=s1.length();
        while((i+1<len) &&((i=s1.indexOf("{",i)) != -1)){
            int n=i+1;
            if((n=s1.indexOf("}",n)) != -1){
                    String ch=s1.substring(i,n+1);
                    if((isMsgFormat(s1.substring(i+1,n)))&&( s2.indexOf(ch)==-1)){
                        return false;
                    }
             }else break;

            i=n;
        }
        return true;
    }

     public static boolean isMsgFormat(final String s) {
        if(s.trim().equals("")||s.trim().endsWith(","))return false;
        String[] arr=s.split(",");
        String ch=arr[0].trim();
        for(int i=0;i<ch.length();i++){
             if(!isNumb(ch.substring(i,i+1)))
                 return false;
         }
        if(arr.length>=2){
            ch=arr[1].trim();
            if(!isFormatType(ch))
                return false;
        }
        return true;
    }

    private static boolean isFormatType(final String s) {
        return s.equals("number") ||s.equals("date") ||
               s.equals("time") || s.equals("choice");
    }

    //checking html
    public boolean htmlValidation() {
        for(String s:sourceTexts){
            if((s!=null) &&(!checkHtml(s,translationText) || !checkHtml(translationText,s)) ){
                //warningList.add("Html tag is missed.");
                return false;
            }
        }
        return true;
    }

    private boolean checkHtml(final String s1,final String s2) {
        if(!s1.trim().startsWith("<html>"))return true;
        int i=0, len=s1.length(),n=0;
        while((i<len) && (i=s1.indexOf("<",i)) != -1){
            if((i<len) && ((n=s1.indexOf(">",i)) != -1)){
                 final String ch=s1.substring(i,n+1);
                 if( s2.indexOf(ch)==-1){
                    final String s=NbBundle.getMessage(Validation.class, "HTML_WARNING", ch);
                    warningList.add(s);
                    return false;
                 }
            }else break;
            i=n;
        }
        return true;
    }

}
