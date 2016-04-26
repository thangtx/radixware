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

import org.radixware.kernel.common.defs.ads.localization.AdsPhraseBookDef;
import java.util.LinkedHashMap;
import java.util.List;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.designer.ads.localization.RowString;


public class Prompt {
    private LinkedHashMap<EIsoLanguage,String> translations;
    private LinkedHashMap<EIsoLanguage,String> sourceTexts;
    private String comment;
    private IMultilingualStringDef mlstring;
    private AdsPhraseBookDef phraseBook=null;

    public Prompt(final IMultilingualStringDef mlstring, final List<EIsoLanguage> sourceLangs, final List<EIsoLanguage> translLangs){
        this.sourceTexts=new LinkedHashMap<>();
        this.translations=new LinkedHashMap<>();
        this.mlstring=mlstring;
        for(EIsoLanguage lang : sourceLangs){
            if(!mlstring.getNeedsCheck(lang))
                sourceTexts.put(lang, mlstring.getValue(lang));
        }
        for(EIsoLanguage lang : translLangs){
            if(!mlstring.getNeedsCheck(lang))
                translations.put(lang, mlstring.getValue(lang));
        }
    }

    public Prompt(final RowString rowString, final List<EIsoLanguage> sourceLangs, final List<EIsoLanguage> translLangs){
        this.sourceTexts=new LinkedHashMap<>();
        this.translations=new LinkedHashMap<>();
        this.mlstring=rowString.getMlStrings().get(0);
        for(EIsoLanguage lang : sourceLangs){
            if(!rowString.needsCheck(lang))
                sourceTexts.put(lang, rowString.getValue(lang));
        }
        for(EIsoLanguage lang : translLangs){
            if(!rowString.needsCheck(lang))
                translations.put(lang, rowString.getValue(lang));
        }
    }

     public Prompt(final IMultilingualStringDef mlstring, final List<EIsoLanguage> sourceLangs, final List<EIsoLanguage> translLangs, final AdsPhraseBookDef phraseBook){
         this( mlstring, sourceLangs,  translLangs);
         this.phraseBook=phraseBook;
     }

    public boolean hasSimilarSource(final RowString rowString, final EIsoLanguage lang, final String s){
       final String pStr= sourceTexts.get(lang);
       if(pStr!=null && !mlstring.equals(rowString.getMlString(lang))){
            if(s.equals(pStr)){
                return true;
            }else{
                 final String[] arrStr= s.split(" ");
                 for(int i=0;i<arrStr.length;i++){//не учитывает разделители
                     if(arrStr[i].equals(pStr)){
                         return true;
                     }
                 }                 
            }
       }
       return false;
    }
    
    

    public String getSourceText(){
        
        final String  spacer=";  ";
        final StringBuilder sb=new StringBuilder();
        for(String sourceText : sourceTexts.values()){
            sb.append(sourceText);
            sb.append(spacer);
            //s+=sourceText+spacer;
        }
        String str=sb.toString();
        if(!"".equals(str)){
            str=str.substring(0, str.length()-spacer.length());
        }
        return str;
    }

     public IMultilingualStringDef getMlString(){
         return mlstring;
     }

    public String getTranslation(EIsoLanguage lang){
         return translations.get(lang);
    }

    public String getComment(){
        return comment;
    }

   public boolean hasTranslation(EIsoLanguage translLang){
       return translations.containsKey(translLang); 
   }

   @Override
   public boolean equals(Object p){
       if(!(p instanceof Prompt))
           return false;
       return mlstring.equals(((Prompt)p).mlstring)&&(isPhraseBookEquals(((Prompt)p).getPhraseBook()));
   }

   private boolean isPhraseBookEquals(AdsPhraseBookDef phraseBook){
       return (phraseBook!=null && this.phraseBook!=null && this.phraseBook.equals(phraseBook))||
              (phraseBook==null && this.phraseBook==null);
   }

   public boolean isEqualText(Prompt p,EIsoLanguage translLang){
       return ((p.getSourceText().equals(this.getSourceText()))&&
           (p.getTranslation(translLang).equals(this.getTranslation(translLang)))&&
           (checkComment(p.getComment())));
   }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (this.mlstring != null ? this.mlstring.hashCode() : 0);
        return hash;
    }

    public AdsPhraseBookDef getPhraseBook(){
        return phraseBook;
    }

   private boolean checkComment(String text){
       return (comment==null)&&(text==null)||((comment!=null)&&(text!=null)&&(comment.equals(text)));
   }
}
