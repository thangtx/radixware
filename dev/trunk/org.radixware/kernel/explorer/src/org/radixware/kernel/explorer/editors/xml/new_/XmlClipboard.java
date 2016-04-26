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
package org.radixware.kernel.explorer.editors.xml.new_;

import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QClipboard;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.editors.xmleditor.model.EXmlItemType;

class XmlClipboard extends org.radixware.kernel.common.client.editors.xmleditor.view.XmlClipboard {

    public XmlClipboard() {
        QApplication.clipboard().changed.connect(this, "clipboardChanged()");
    }
       
    @Override
    public void putXml(final XmlObject xml, final QName name, final EXmlItemType type) {
        if (xml != null) {
            final XmlObject x = XmlObject.Factory.newInstance();
            final XmlCursor c = x.newCursor();
            final XmlCursor source = xml.newCursor();
            try {
                if (type == EXmlItemType.Element) {
                    c.toNextToken();
                    c.beginElement(name);
                    do {
                        if (source.isStart()) {
                            if (!source.toNextSibling()) {
                                source.toEndToken();
                                source.toNextToken();
                            }
                        } else {
                            source.toNextToken();
                        }
                        if (source.currentTokenType() != XmlCursor.TokenType.ENDDOC) {
                            source.copyXml(c);
                        }
                    } while (source.hasNextToken());
                    QApplication.clipboard().setText(x.xmlText(), QClipboard.Mode.Clipboard);
                } else {
                    final String text = name.getLocalPart() + "=\"" +  source.getTextValue() + "\"";
                    QApplication.clipboard().setText(text, QClipboard.Mode.Clipboard);
                }
                notifyListeners();
            } finally {
                c.dispose();
                source.dispose();
            }
        }
    }

    @Override
    public XmlObject getXml() {
        final String text = QApplication.clipboard().text();
        if (text != null && !text.isEmpty()) {
            try {
                return XmlObject.Factory.parse(text);
            } catch (XmlException exception) {
                return createAttribute(text);
            }
        }
        return null;
    }
    
    @SuppressWarnings("unused")
    private void clipboardChanged(){
        notifyListeners();
    }
    
    @Override
    public void close(){
        super.close();
        QApplication.clipboard().disconnect(this);
    }
       
}
