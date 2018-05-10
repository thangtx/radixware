
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
package org.radixware.kernel.common.svn.client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.radixware.kernel.common.svn.RadixSvnException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SvnHttpReplayHandler extends DefaultHandler {

    private final static String ERROR_MESSAGE = "Malformed response data.";
    private final SvnEditor myEditor;

    private String myPath;
    //private String myPropertyName;
    private boolean myIsDirectory;

    public SvnHttpReplayHandler(final SvnEditor editor
            //, final SvnHttpRepository repository
    ) {
        super();
        this.myEditor = editor;
        //this.repository = repository;
    }

    private static long findLongAttribute(final Attributes attributes, final String attributeName) throws SAXException {
        final String revisionAsStr = attributes.getValue(attributeName);
        if (revisionAsStr == null) {
            return -1;
        }
        try {
            return Long.parseLong(revisionAsStr);
        } catch (NumberFormatException nfe) {
            throw new SAXException(ERROR_MESSAGE + " - Invalid " + attributeName + " attribute.", nfe);
        }
    }

    private static long getLongAttribute(final Attributes attributes, final String attributeName) throws SAXException {
        final String revisionAsStr = attributes.getValue(attributeName);
        if (revisionAsStr == null) {
            throw new SAXException(ERROR_MESSAGE + " - Missing " + attributeName + " attribute.");
        }

        try {
            return Long.parseLong(revisionAsStr);
        } catch (NumberFormatException nfe) {
            throw new SAXException(ERROR_MESSAGE + " - Invalid " + attributeName + " attribute.", nfe);
        }
    }

    private static String getNameStringAttribute(final Attributes attributes, final String attributeName) throws SAXException {
        final String name = attributes.getValue(attributeName);
        if (name == null) {
            throw new SAXException(ERROR_MESSAGE + " - Missing " + attributeName + " attribute.");
        }
        return name;
    }

    private static void error(final String message) throws SAXException {
        throw new SAXException(ERROR_MESSAGE + " - " + message + ".");
    }

    private static long getRevisionAttribute(final Attributes attributes) throws SAXException {
        return getLongAttribute(attributes, "rev");
    }

    private static long findRevisionAttribute(final Attributes attributes) throws SAXException {
        return findLongAttribute(attributes, "rev");
    }

    private static long findCopyFromRevisionAttribute(final Attributes attributes) throws SAXException {
        return findLongAttribute(attributes, "copyfrom-rev");
    }

    private static String getNameAttribute(final Attributes attributes) throws SAXException {
        return getNameStringAttribute(attributes, "name");
    }
    
    private static boolean findDelAttribute(final Attributes attributes) {
        return attributes.getValue("del")!=null;
    }
    
    private static String findCopyFromPathAttribute(final Attributes attributes) throws SAXException {
        final String name = attributes.getValue("copyfrom-path");
        return name;
    }

    private static String findCheckSumAttribute(final Attributes attributes) throws SAXException {
        return attributes.getValue("checksum");
    }
 
    @Override
    public void startElement(final String uri, final String localName, final String qName, final Attributes attributes) throws SAXException {
        try {
            //System.out.println("OPEN uri  " + uri + "localName  " + localName + " qName  " + qName + "  attributes: " + attributesToString_debug(attributes));
            if (qName.endsWith(":editor-report")) {
                //do nothing ??
            } else if (qName.endsWith(":target-revision")) {
                //final long revision = 
                getRevisionAttribute(attributes);
                //myEditor.targetRevision(revision);                      
            } else if (qName.endsWith(":open-root")) {
                final long revision = getRevisionAttribute(attributes);
                myEditor.openRoot(revision);

                myPath = "";
                myIsDirectory = true;

            } else if (qName.endsWith(":add-directory")) {
                final String path = getNameAttribute(attributes);

                final String fromPath = findCopyFromPathAttribute(attributes);
                final long fromRevision = findCopyFromRevisionAttribute(attributes);

                myEditor.addDir(path, fromPath, fromRevision);

                myPath = path;
                myIsDirectory = true;
            } else if (qName.endsWith(":open-directory")) {
                final String path = getNameAttribute(attributes);
                final long revision = findRevisionAttribute(attributes);
                myEditor.openDir(path, revision);
                myPath = path;
                myIsDirectory = true;
            } else if (qName.endsWith(":close-directory")) {
                if (!myIsDirectory) {
                    error("Trying to close the incorrect directory");
                } else {
                    myEditor.closeDir();
                }
            } else if (qName.endsWith(":add-file")) {
                final String path = getNameAttribute(attributes);

                final String fromPath = findCopyFromPathAttribute(attributes);
                final long fromRevision = findCopyFromRevisionAttribute(attributes);
                myEditor.addFile(path, fromPath, fromRevision);
                myIsDirectory = false;
                myPath = path;
            } else if (qName.endsWith(":open-file")) {
                final String path = getNameAttribute(attributes);
                final long revision = findRevisionAttribute(attributes);
                myEditor.openFile(path, revision);
                myIsDirectory = false;
                myPath = path;
            } else if (qName.endsWith(":apply-textdelta")) {                
                if (myIsDirectory) {
                    error("Trying to change directory");
                }
                
                final String checkSum = findCheckSumAttribute(attributes);
                ((ISvnDeltaConsumer) myEditor).applyTextDelta(myPath, checkSum);
                
                startDeltaCollection();
                
                //do nothing
            } else if (qName.endsWith(":close-file")) {
                
                
                if (myIsDirectory) {
                    error("Trying to close the incorrect directory");
                }

                final String checkSum = findCheckSumAttribute(attributes);
                myEditor.closeFile(myPath, checkSum);                
                myIsDirectory = true;
                
                finishDeltaCollection();                
            
            } else if (qName.endsWith(":delete-entry")) {
                final String path = getNameAttribute(attributes);
                final long rev = getRevisionAttribute(attributes);                
                myEditor.deleteEntry(path, rev);
            } else if (qName.endsWith(":change-file-prop") ) {
                final String name = getNameAttribute(attributes);
                if (findDelAttribute(attributes)){                    
                    ((SvnHttpEditor)myEditor).changeFileProperty(myPath, name, null);
                    myPropertyName = null;
                }
                else {
                    myPropertyName = name;
                    startPropertiesCollection();
                }
            }
            else if (qName.endsWith(":change-dir-prop")) {
                final String name = getNameAttribute(attributes);
                if (findDelAttribute(attributes)){                    
                     ((SvnHttpEditor)myEditor).changeDirProperty(name, null);
                     myPropertyName = null;
                }
                else {
                    myPropertyName = name;
                    startPropertiesCollection();
                }                
            }
            else {                 
                throw new RuntimeException("Unknow tag " + qName + ", attributes: " + attributesToString_debug(attributes));
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    
    private final SvnDeltaReader deltaReader = new SvnDeltaReader();
    private String myPropertyName = null;    
    private final StringBuilder myDeltaOutputStream = new StringBuilder();
    
    ByteArrayOutputStream propertiesValueBuffer = new ByteArrayOutputStream();
    
    private boolean deltaCollection = false;
    private boolean propertiesCollection = false;
    
    
    private void startPropertiesCollection() throws RadixSvnException{
        if (propertiesCollection){
            throw new RadixSvnException("Attempt to set the property twice");
        }
        propertiesCollection = true;
        deltaReader.reset(myPath, (ISvnDeltaConsumer) myEditor);
        myDeltaOutputStream.delete(0, myDeltaOutputStream.length());
        propertiesValueBuffer.reset();
    }
    
    private void finishPropertiesCollection() throws RadixSvnException{
        if (!propertiesCollection){
            throw new RadixSvnException("Attempt to unset the property twice");
        }
        propertiesCollection = false;
    }
    
    private void startDeltaCollection() throws RadixSvnException{
        deltaCollection = true;
        deltaReader.reset(myPath, (ISvnDeltaConsumer) myEditor);
        myDeltaOutputStream.delete(0, myDeltaOutputStream.length());
    }
    private void finishDeltaCollection(){
        deltaCollection = false;
    }
    
    
    


    private byte[] charactersCommonDeltaCollection(char[] ch, int start, int length) throws SAXException {
         
        int offset = start;

        for (int i = start; i < start + length; i++) {
            if (ch[i] == '\r' || ch[i] == '\n') {
                myDeltaOutputStream.append(ch, offset, i - offset);
                offset = i + 1;
                if (i + 1 < (start + length) && ch[i + 1] == '\n') {
                    offset++;
                    i++;
                }
            }
        }        
        
        if (offset < start + length) {
            myDeltaOutputStream.append(ch, offset, start + length - offset);
        }

        int stored = myDeltaOutputStream.length();
        if (stored < 4) {
            return null;
        }
        int segmentsCount = stored / 4;
        int remains = stored - (segmentsCount * 4);

        StringBuffer toDecode = new StringBuffer();
        toDecode.append(myDeltaOutputStream);
        toDecode.delete(myDeltaOutputStream.length() - remains, myDeltaOutputStream.length());

        int index = 0;
        while (index < toDecode.length() && Character.isWhitespace(toDecode.charAt(index))) {
            index++;
        }
        if (index > 0) {
            toDecode = toDecode.delete(0, index);
        }
        index = toDecode.length() - 1;
        while (index >= 0 && Character.isWhitespace(toDecode.charAt(index))) {
            toDecode.delete(index, toDecode.length());
            index--;
        }
        byte[] buffer = javax.xml.bind.DatatypeConverter.parseBase64Binary(toDecode.toString());
        myDeltaOutputStream.delete(0, toDecode.length());
        
        return buffer;
        
    }
    
    
    private void charactersDeltaCollection(char[] ch, int start, int length) throws SAXException {
        byte[] buffer = charactersCommonDeltaCollection(ch, start, length);
        if (buffer==null){
            return;
        }
        
        try {
          
          int decodedLength = buffer.length;
            deltaReader.nextWindow(buffer, 0, decodedLength, myPath, ((ISvnDeltaConsumer) myEditor));
        } catch (IllegalArgumentException | RadixSvnException e) {
            throw new SAXException(e);
        }
    }
    
    private void propertiesDeltaCollection(char[] ch, int start, int length) throws SAXException {
        byte[] buffer = charactersCommonDeltaCollection(ch, start, length);
        if (buffer==null){
            return;
        }
        try {
          propertiesValueBuffer.write(buffer);
        } catch (IOException e) {
            throw new SAXException(e);
        }
    }
    

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        
        if (deltaCollection){
            charactersDeltaCollection(ch, start, length);
            return;
        }
        
        if (propertiesCollection){
           propertiesDeltaCollection(ch, start, length);
        }
        super.characters(ch, start, length);
    }
    
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
//        if (qName.endsWith("apply-textdelta")) { 
//        }
//        else
        if (qName.endsWith(":change-file-prop")) {
            try { 
                if (propertiesCollection){
                    finishPropertiesCollection();                    
                    final SvnProperties.Value value = new SvnProperties.Value(null, propertiesValueBuffer.toByteArray());
                    ((SvnHttpEditor)myEditor).changeFileProperty(myPath, myPropertyName, value );
                    
                } 
                
            } catch (Exception ex) {
                throw new SAXException(ex);
            }
       }     
        else if (qName.endsWith(":change-dir-prop")) {
            try { 
                if (propertiesCollection){
                    finishPropertiesCollection();                    
                    final SvnProperties.Value value = new SvnProperties.Value(null, propertiesValueBuffer.toByteArray());
                    ((SvnHttpEditor)myEditor).changeDirProperty(myPropertyName, value );
                    
                } 
                
            } catch (Exception ex) {
                throw new SAXException(ex);
            }            
        }    
        //System.out.println("CLOSE uri  " + uri + "localName  " + localName + " qName  " + qName);
    }

    private static String attributesToString_debug(final Attributes attributes) {
        final StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < attributes.getLength(); i++) {
            if (i != 0) {
                stringBuilder.append("\n");
            }
            stringBuilder.append(attributes.getLocalName(i)).append("=").append(attributes.getValue(i));
        }
        return stringBuilder.toString();
    }

};
