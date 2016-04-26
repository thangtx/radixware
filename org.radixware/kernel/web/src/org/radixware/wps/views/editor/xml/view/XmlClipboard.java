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
package org.radixware.wps.views.editor.xml.view;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.editors.xmleditor.model.EXmlItemType;

class XmlClipboard extends org.radixware.kernel.common.client.editors.xmleditor.view.XmlClipboard {

    private XmlObject obj;
    private String text;

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
                    obj = x.copy();
                } else {
                    final String value = source.getTextValue();
                    c.toNextToken();
                    c.beginElement("ClipboardAttribute");
                    c.insertAttributeWithValue(name, value);
                    c.toPrevToken();
                    obj = c.getObject();
                }
                text = null;
                notifyListeners();
            } finally {
                c.dispose();
                source.dispose();
            }
        }
    }

    @Override
    public XmlObject getXml() {
        if (obj != null) {
            return obj;
        } else if (text != null && !text.isEmpty()) {
            try {
                obj = XmlObject.Factory.parse(text);
            } catch (XmlException exception) {
                obj = createAttribute(text);
            }
            return obj;
        }
        return null;
    }

    public void putText(final String text) {
        this.text = text;
        obj = null;
        notifyListeners();
    }

    public String getText() {
        if (obj == null) {
            return text;
        } else {
            if (getType() == EXmlItemType.Attribute) {
                final XmlCursor cursor = obj.newCursor();
                final String value;
                try {
                    value = cursor.getTextValue();
                } finally {
                    cursor.dispose();
                }
                return getNameItem().getLocalPart() + "=\"" + value + "\"";
            } else {
                return obj.xmlText();
            }
        }
    }
}
