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

package org.radixware.kernel.utils.spellchecker;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.radixware.kernel.common.check.spelling.IDictionaryProvider;
import org.radixware.kernel.common.check.spelling.IDictionarySet;
import org.radixware.kernel.common.check.spelling.IWordContainer;
import org.radixware.kernel.common.check.spelling.Word;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.enums.EIsoLanguage;


public class Spellchecker {

    public enum Validity {

        VALID,
        INVALID
    }

    static public Validity checkSingleWord(EIsoLanguage lang, Word token) {
        IDictionaryProvider provider = DictionaryProvider.getInstance(lang);
        return checkSingleWord(provider.getDictionarySet(null), token);
    }
    
    
    static Validity checkSingleWord(IWordContainer container, Word token) {
        if (token == null) {
            return Validity.INVALID;
        }
        
        if (token.charAt(0) == '%') {
            int len = token.length();
            if (len < 2){
                return Validity.VALID;
            } else {
                if (SimpleTokenizer.conversationLength(token.subSequence(len-1, len).toString()) > 0 || (SimpleTokenizer.conversationLength(token.subSequence(len-2, len).toString()) > 0) && len > 2){
                    return Validity.VALID;
                }
                else {
                    return Validity.INVALID;
                }
            }
        }

        if (container != null) {
            if (container.containsWord(token)) {
                return Validity.VALID;
            }
        }
        if (token.charAt(token.length() - 1) == '.') {
            return checkSingleWord(container, token.subSequence(0, token.length() - 1));
        }
        return Validity.INVALID;
    }

    static Validity checkSingleWord(EIsoLanguage lang, RadixObject context, Word token) {
        IDictionaryProvider provider = DictionaryProvider.getInstance(lang);
        IDictionarySet dictionarySet = provider.getDictionarySet(context);

        return checkSingleWord(dictionarySet, token);
    }
    private final EIsoLanguage lang;
    private final IWordContainer wordContainer;
    private final IWordContainer wordContainerEn;

    private Spellchecker(EIsoLanguage lang, IWordContainer wordContainer, IWordContainer wordContainerEn) {
        this.lang = lang;
        this.wordContainer = wordContainer;
        this.wordContainerEn = wordContainerEn;
    }

    public Validity check(Word token) {
        
        if (checkSingleWord(wordContainer, token) == Validity.VALID) {
            return Validity.VALID;
        }
        
        return checkSingleWord(wordContainerEn, token);
    }

    public Validity check(CharSequence sequence) {
        if (sequence == null || sequence.length() == 0) {
            return Validity.INVALID;
        }
        Word wordToken = Word.Factory.newInstance(sequence);

        return check(wordToken);
    }

    public final EIsoLanguage getLanguage() {
        return lang;
    }
    
    public static Spellchecker getInstance(EIsoLanguage lang, RadixObject context) {
        IDictionaryProvider provider = DictionaryProvider.getInstance(lang);
        IDictionarySet dictionarySet = provider.getDictionarySet(context);

        IDictionarySet dictionarySetEn = null;
        if (lang != EIsoLanguage.ENGLISH) {
            provider = DictionaryProvider.getInstance(EIsoLanguage.ENGLISH);
            dictionarySetEn = provider.getDictionarySet(context);
        }

        return new Spellchecker(lang, dictionarySet, dictionarySetEn);
    }
}