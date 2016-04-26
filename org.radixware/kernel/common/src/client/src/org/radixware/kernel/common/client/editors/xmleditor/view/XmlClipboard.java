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

package org.radixware.kernel.common.client.editors.xmleditor.view;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.editors.xmleditor.model.EXmlItemType;


public abstract class XmlClipboard {
    
    public static interface Listener{
        void clipboardChanged();
    }
    
    public abstract void putXml(final XmlObject xml, final QName name, final EXmlItemType type);
    public abstract XmlObject getXml();
    
    private final List<Listener> listeners = new LinkedList<>();
    
    public final void addListener(final Listener listener){
        if (listener!=null && !listeners.contains(listener)){
            listeners.add(listener);
        }
    }
    
    public final void removeListener(final Listener listener){
        if (listener!=null){
            listeners.remove(listener);
        }
    }
    
    protected final void notifyListeners(){
        final List<Listener> copy = new ArrayList<>(listeners);
        for (Listener listener: copy){
            listener.clipboardChanged();
            
        }
    }
    
    public final QName getNameItem(){
        final XmlObject xml = getXml();
        if (xml == null) {
            return null;
        }
        final XmlCursor cursor = xml.newCursor();
        try {
            if (cursor.isStartdoc()) {
                cursor.toNextToken();
            }
            return cursor.getName();
        } finally {
            cursor.dispose();
        }        
    }
    
    public final EXmlItemType getType(){
        final XmlObject xml = getXml();
        if (xml == null) {
            return null;
        }
        final XmlCursor cursor = xml.newCursor();
        try {
            if (cursor.isStartdoc()) {
                cursor.toNextToken();
            }
            if (cursor.currentTokenType() == XmlCursor.TokenType.START) {
                return EXmlItemType.Element;
            } else if (cursor.currentTokenType() == XmlCursor.TokenType.ATTR) {
                return EXmlItemType.Attribute;
            } else {
                return null;
            }
        } finally {
            cursor.dispose();
        }        
    }
    
    protected final XmlObject createAttribute(final String text){
        final int index = text.indexOf('=');
        if (index > 0 && index < text.length() - 1) {
            final String name = text.substring(0, index);
            final String value = text.substring(index + 2, text.length() - 1);
            final XmlObject document = XmlObject.Factory.newInstance();
            final XmlCursor cursor = document.newCursor();
            try {
                cursor.toNextToken();
                cursor.beginElement("ClipboardAttribute");
                cursor.insertAttributeWithValue(name, value);
                cursor.toPrevToken();
                return cursor.getObject();
            } catch (IllegalArgumentException exception) {
                return null;
            } finally {
                cursor.dispose();
            }
        }
        return null;        
    }
    
    public final boolean isEmptyClipboard() {
         return getXml() == null;
    }       
    
    public void close(){
        listeners.clear();
    }
}
