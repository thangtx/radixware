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

package org.radixware.utils.tsspellcheck;

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QKeySequence;
import com.trolltech.qt.gui.QTreeWidgetItem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.check.spelling.Word;
import org.radixware.kernel.utils.spellchecker.SimpleTokenizer;
import org.radixware.kernel.utils.spellchecker.Spellchecker;


public class TranslationToken {
    
    public static class Location{
        public final String filePath;
        public final int lineNumber;
        public Location(final String filePath, final int lineNumber){
            this.filePath = filePath;
            this.lineNumber = lineNumber;
        }

        public String toString(){
            return filePath+" ("+QApplication.translate("Spellchecker", "line")+": "+lineNumber+")";
        }
    }

    private final String contextName;
    private final String translationText;
    private final String sourceText;
    private final EIsoLanguage sourceTextLanguage;
    private final EIsoLanguage targetTextLanguage;
    private List<SimpleTokenizer.Token> invalidSourceTextTokens;
    private List<SimpleTokenizer.Token> invalidTranslationTextTokens;
    private final List<Location> locations = new LinkedList<Location>();

    public TranslationToken(final String context, final String source, final String translation, final EIsoLanguage srclanguage, final EIsoLanguage targetlanguage, final List<Location> locations){
        contextName = context;
        translationText = translation;
        sourceText = source;
        sourceTextLanguage = srclanguage;
        targetTextLanguage = targetlanguage;
        if (locations!=null)
            this.locations.addAll(locations);
        
    }

    public String getContextName(){
        return contextName;
    }

    public String getSourceText(){
        return sourceText;
    }

    public void doSpellcheck(){
        if (invalidSourceTextTokens==null)
        {
            invalidSourceTextTokens = new ArrayList<SimpleTokenizer.Token>();
            if (sourceText!=null && !sourceText.isEmpty() && !isKeySequence(sourceText)){
                final SimpleTokenizer tokenizer = new SimpleTokenizer(prepareCheckedString(sourceText));
                final List<SimpleTokenizer.Token> tokens = tokenizer.tokenize();
                for (SimpleTokenizer.Token token: tokens){
                    if (Spellchecker.checkSingleWord(sourceTextLanguage, Word.Factory.newInstance(token.text))==Spellchecker.Validity.INVALID){
                        invalidSourceTextTokens.add(token);
                    }
                }
            }
        }
        if (invalidTranslationTextTokens==null)
        {
            invalidTranslationTextTokens = new ArrayList<SimpleTokenizer.Token>();
            if (translationText!=null && !translationText.isEmpty() && !isKeySequence(translationText)){
                final SimpleTokenizer tokenizer = new SimpleTokenizer(prepareCheckedString(translationText));
                final List<SimpleTokenizer.Token> tokens = tokenizer.tokenize();
                for (SimpleTokenizer.Token token: tokens){
                    if (Spellchecker.checkSingleWord(targetTextLanguage, Word.Factory.newInstance(token.text))==Spellchecker.Validity.INVALID){
                        invalidTranslationTextTokens.add(token);
                    }
                }
            }
        }        
    }

    private static boolean isKeySequence(final String str){
        final QKeySequence sequence = QKeySequence.fromString(str);
        return sequence!=null && !sequence.isEmpty();
    }

    private static String prepareCheckedString(final String str){
        return str.replaceAll("&", "").replaceAll("%\\d+\\u0024s", "");
    }

    public List<SimpleTokenizer.Token> getInvalidSourceTextTokens(){
        if (invalidSourceTextTokens==null)
            doSpellcheck();
        return Collections.unmodifiableList(invalidSourceTextTokens);
    }

    public List<SimpleTokenizer.Token> getInvalidTranslationTextTokens(){
        if (invalidTranslationTextTokens==null)
            doSpellcheck();
        return Collections.unmodifiableList(invalidTranslationTextTokens);
    }

    public String getTranslation(){
        return translationText;
    }

    public EIsoLanguage getSourceTextLanguage(){
        return sourceTextLanguage;
    }

    public EIsoLanguage getTargetTextLanguage(){
        return targetTextLanguage;
    }

    public List<Location> getLocations(){
        return Collections.unmodifiableList(locations);
    }

    public void addTreeItems(final QTreeWidgetItem parentItem){
        QTreeWidgetItem item;
        {
            final List<SimpleTokenizer.Token> sourceTextTokens = getInvalidSourceTextTokens();
            for (SimpleTokenizer.Token spellcheckToken: sourceTextTokens){
                item = new QTreeWidgetItem(parentItem);
                item.setText(0, spellcheckTokenAsString(spellcheckToken, sourceTextLanguage));
                item.setData(0, Qt.ItemDataRole.UserRole, this);
            }
        }
        {
            final List<SimpleTokenizer.Token> translationTextTokens = getInvalidTranslationTextTokens();
            for (SimpleTokenizer.Token spellcheckToken: translationTextTokens){
                item = new QTreeWidgetItem(parentItem);
                item.setText(0, spellcheckTokenAsString(spellcheckToken, targetTextLanguage));
                item.setData(0, Qt.ItemDataRole.UserRole, this);
            }
        }
    }

    private static String spellcheckTokenAsString(final SimpleTokenizer.Token token, final EIsoLanguage lng){
        final String resultString;
        switch (lng)
        {
            case ENGLISH:
            {
                final String language = QApplication.translate("Spellchecker", "english");
                final String itemText = QApplication.translate("Spellchecker", "Unknown %s word \"%s\"");
                resultString = String.format(itemText, language, token.text);
                break;
            }
            case RUSSIAN:
            {
                final String language = QApplication.translate("Spellchecker", "russian");
                final String itemText = QApplication.translate("Spellchecker", "Unknown %s word \"%s\"");
                resultString = String.format(itemText, language, token.text);
                break;
            }
            default:
            {
                final String itemText = QApplication.translate("Spellchecker", "Unknown word \"%s\"");
                resultString = String.format(itemText, token.text);
            }
        }
        return resultString;
    }

    @Override
    public String toString() {
        return contextName+"::'"+sourceText+"'";
    }


}
