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

import com.trolltech.qt.core.QFile;
import com.trolltech.qt.core.QIODevice;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.xml.QXmlStreamReader;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;


public final class TranslationFileSpellchecker extends QXmlStreamReader{

    public static class SpellcheckResult{
        private final List<TranslationToken> tokens;
        private final EIsoLanguage sourceLanguage ,targetLanguage;

        protected SpellcheckResult(final List<TranslationToken> tokens, final EIsoLanguage sourceLng, final EIsoLanguage targetLng){
            this.tokens = tokens;
            sourceLanguage = sourceLng;
            targetLanguage = targetLng;
        }

        public List<TranslationToken> getTokens(){
            return Collections.unmodifiableList(tokens);
        }

        public EIsoLanguage getSourceLanguage(){
            return sourceLanguage;
        }

        public EIsoLanguage  getTargetLanguage(){
            return targetLanguage;
        }
    }

    private TranslationFileSpellchecker(){
        super();
    };

    private final static TranslationFileSpellchecker INSTANCE = new TranslationFileSpellchecker();

    public static TranslationFileSpellchecker getInstance(){
        return INSTANCE;
    }

    public SpellcheckResult check(final String fileName, final QObject progressHandler) throws IOException{
        final List<TranslationToken> allTokens = new LinkedList<TranslationToken>();
        final QFile file = new QFile(fileName);        
        if (!file.exists())
            throw new IOException("Can't open file "+fileName);
        if (!file.open(QIODevice.OpenModeFlag.ReadOnly))
            throw new IOException("Can't open file "+fileName);
        setDevice(file);
        EIsoLanguage targetLanguage=null, sourceLanguage=null;
        try{            
            while (!atEnd()){
                readNext();
                if (hasError()){
                    throw new IOException(error().name()+" "+errorString());
                }
                if (isStartDocument() || isDTD() || (isCharacters() && text().trim().isEmpty())){
                    continue;
                }
                if (isStartElement()) {
                    if ("TS".equals(name()) && "2.0".equals(attributes().value("version"))){
                        targetLanguage = parseLanguage(attributes().value("language"));
                        if (targetLanguage==null){
                            throw new IOException("Target language is not defined");
                        }
                        sourceLanguage = parseLanguage(attributes().value("sourcelanguage"));
                        if (sourceLanguage==null)
                            sourceLanguage = EIsoLanguage.ENGLISH;
                    }
                    else if ("context".equals(name())){
                        readNext();
                        if (isCharacters())
                            readNext();
                        final String contextName = readContextName();
                        if (contextName!=null)
                            allTokens.addAll(readContext(contextName, sourceLanguage, targetLanguage));
                    }
                }
                else if (isEndElement() && "TS".equals(name())){
                    break;
                }
                else{
                    throw new IOException("The file is not a qt translation version 2.0 file.");
                }
            }
        }
        finally{
            file.close();
        }
        
        if (progressHandler!=null)
            QApplication.postEvent(progressHandler, new SpellcheckProgressDialog.SetMaximumValueEvent(allTokens.size()));
        final List<TranslationToken> invalidTokens = new LinkedList<TranslationToken>();
        for (TranslationToken token: allTokens){
            token.doSpellcheck();
            if (!token.getInvalidSourceTextTokens().isEmpty() || !token.getInvalidTranslationTextTokens().isEmpty()){
                invalidTokens.add(token);
            }
            if (progressHandler!=null){
                QApplication.postEvent(progressHandler, new SpellcheckProgressDialog.NextStepEvent());
            }
        }
        
        return new SpellcheckResult(invalidTokens, sourceLanguage, targetLanguage);
    }

    private static EIsoLanguage parseLanguage(final String language) throws IOException{
        final EIsoLanguage isoLanguage;
        if (language!=null && !language.isEmpty()){
            final String langCode = language.substring(0, 2);
            try{
                isoLanguage = EIsoLanguage.getForValue(langCode);
            }
            catch(NoConstItemWithSuchValueError error){
                throw new IOException("Unkown language "+langCode);
            }
        }
        else{
            isoLanguage = null;
        }
        return isoLanguage;
    }

    private String readContextName() throws IOException{
        String contextName="<Unknown>";
        boolean contextNameTagPresent = false;
        while(!atEnd()){
            if (hasError()){
                throw new IOException(error().name()+" "+errorString());
            }
            if (isStartElement()){
                if (!"name".equals(name())){
                    throw new IOException("name tag expected, but '"+name()+"' found");
                }
                else{
                    contextNameTagPresent = true;
                    readNext();
                }
            }
            if (isCharacters()){
                contextName = text();
                readNext();
            }
            if (isEndElement()){
                readNext();
                break;
            }
        }
        return contextNameTagPresent ? contextName : null;
    }

    private List<TranslationToken> readContext(final String contextName, final EIsoLanguage sourceLng, EIsoLanguage targetLng) throws IOException{
        final List<TranslationToken> tokens = new LinkedList<TranslationToken>();
        
        while (!atEnd()) {
            readNext();
            if (hasError()){
                throw new IOException(error().name()+" "+errorString());
            }
            if (isEndElement()){
                break;
            }

            if (isStartElement()) {
                if ("message".equals(name()))
                    tokens.add(readMessage(contextName, sourceLng, targetLng));
            }
        }
        return tokens;
    }

    private TranslationToken readMessage(final String contextName, final EIsoLanguage sourceLng, final EIsoLanguage targetLng) throws IOException{
        final List<TranslationToken.Location> locations = new LinkedList<TranslationToken.Location>();
        final List<TranslationToken> translationTokens = new LinkedList<TranslationToken>();
        String sourceText = null, targetText = null;
        while (!atEnd()) {
            readNext();
            if (hasError()){
                throw new IOException(error().name()+" "+errorString());
            }
            if (isEndElement()){
                readNext();
                break;
            }
            
            if (isStartElement()) {
                if ("location".equals(name())){
                    locations.add(readLocation());
                }
                else if ("source".equals(name())){
                    sourceText = readText();
                }
                else if ("translation".equals(name())){
                    targetText = readText();
                }
                else{
                    ignoreElement();
                }
            }
        }

        return new TranslationToken(contextName, sourceText, targetText, sourceLng, targetLng, locations);
    }

    private TranslationToken.Location readLocation() throws IOException{
        final String filePath = attributes().value("filename");
        final String line = attributes().value("line");
        final int lineNumber;
        try{
            lineNumber = Integer.parseInt(line);
        }
        catch(NumberFormatException exception){
            throw new IOException("Can't parse line number "+line,exception);
        }
        final TranslationToken.Location location = new TranslationToken.Location(filePath, lineNumber);
        while (!atEnd()) {
            readNext();
            if (isEndElement())
                break;
            if (hasError()){
                throw new IOException(error().name()+" "+errorString());
            }
            if (isStartElement()) {
                throw new IOException("Unexpected tag "+name());
            }
        }
        return location;
    }

    private String readText() throws IOException{
        String resultText = "";
        while (!atEnd()) {
            readNext();
            if (isEndElement())
                break;
            if (hasError()){
                throw new IOException(error().name()+" "+errorString());
            }
            if (isStartElement()) {
                throw new IOException("Unexpected tag "+name());
            }
            if (isCharacters()){
                resultText = text();
            }
        }
        return resultText;
    }

    private void ignoreElement(){
        while (!atEnd()) {
            readNext();
            if (isEndElement()){
                readNext();
                break;
            }
        }
    }

    public void breakSpellcheck(){
        this.raiseError("Interrupted");
    }
}
