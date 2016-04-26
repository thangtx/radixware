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
package org.radixware.kernel.common.client.editors.xmleditor.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;

public class XmlDocumentItem {

    public static final QName XSI_NIL = new QName("http://www.w3.org/2001/XMLSchema-instance", "xsi:nil", "xsi");
    private XmlObject node;
    private final EXmlItemType type;        

    XmlDocumentItem(final XmlObject n, final EXmlItemType type) {
        node = n;
        this.type = type;
    }

    public String getLocalName() {
        return node.getDomNode().getLocalName();
    }

    public QName getFullName() {
        return new QName(node.getDomNode().getNamespaceURI(), getLocalName(), node.getDomNode().getPrefix());
    }

    public String getDisplayString() {
        return getType() == EXmlItemType.Element ? "<" + getLocalName() + ">" : getLocalName();
    }

    public String getValue() {
        final XmlCursor cursor = node.newCursor();
        try {
            if (type == EXmlItemType.Element) {
                if (goToTextNode(cursor)) {
                    return cursor.getTextValue();
                } else {
                    return null;
                }
            } else {
                return cursor.getTextValue();
            }
        } finally {
            cursor.dispose();
        }
    }

    public XmlObject getXmlObject() {
        return node.copy();
    }

    public int getAttributesCount() {
        final XmlCursor cursor = node.newCursor();
        try {
            if (cursor.toFirstAttribute()) {
                int attrCount = 1;
                while (cursor.toNextAttribute()) {
                    attrCount++;
                }
                return attrCount;
            } else {
                return 0;
            }
        } finally {
            cursor.dispose();
        }
    }

    public int getChildCount() {
        final XmlCursor cursor = node.newCursor();
        try {
            if (cursor.toFirstChild()) {
                int childrenCount = 1;
                while (cursor.toNextSibling()) {
                    childrenCount++;
                }
                return childrenCount;
            } else {
                return 0;
            }
        } finally {
            cursor.dispose();
        }
    }

    public int getIndexOfChild(final XmlDocumentItem child) {
        final XmlCursor cursor = node.newCursor();
        final XmlCursor cursor2 = child.node.newCursor();
        try {
            if (child.getType() == EXmlItemType.Element) {
                if (cursor.toFirstChild()) {
                    int childrenIndex = 0;
                    while (!cursor.isAtSamePositionAs(cursor2)) {
                        cursor.toNextSibling();
                        childrenIndex++;
                    }
                    return childrenIndex;
                } else {
                    return -1;
                }
            } else {
                if (cursor.toFirstAttribute()) {
                    int attrIndex = 0;
                    while (!cursor.isAtSamePositionAs(cursor2)) {
                        cursor.toNextAttribute();
                        attrIndex++;
                    }
                    return attrIndex;
                } else {
                    return -1;
                }
            }
        } finally {
            cursor.dispose();
            cursor2.dispose();
        }
    }

    public void setValue(final String value) {
        final XmlCursor cursor = node.newCursor();
        try {
            if (type == EXmlItemType.Element) {
                cursor.push();
                if (goToTextNode(cursor)) {
                    cursor.removeXml();
                    cursor.insertChars(value);
                } else {
                    cursor.pop();
                    final XmlCursor.TokenType firstContentToken = cursor.toFirstContentToken();
                    if (firstContentToken == XmlCursor.TokenType.NONE) {
                        cursor.setTextValue(value);
                    } else if (firstContentToken == XmlCursor.TokenType.TEXT) {
                        cursor.removeXml();
                        cursor.insertChars(value);
                    } else {
                        cursor.insertChars(value);
                    }
                }
            } else {
                cursor.setTextValue(value);
            }
        } finally {
            cursor.dispose();
        }
    }

    private boolean goToTextNode(final XmlCursor cursor) {
        int nestedElements = 0;
        cursor.toNextToken();
        while ((!cursor.isEnd() || nestedElements > 0) && !cursor.isEnddoc()) {
            if (cursor.isStart()) {
                nestedElements++;
            }
            if (cursor.isEnd()) {
                nestedElements--;
            }
            if (cursor.isText() && nestedElements == 0) {
                final String text = cursor.getTextValue();
                if (text != null && !text.trim().isEmpty()) {
                    return true;
                }
            }
            cursor.toNextToken();
        }
        return false;
    }

    public EXmlItemType getType() {
        return type;
    }

    public String getNamespaceUriByPrefix(final String prefix) {
        if (this.getType() == EXmlItemType.Element) {
            final XmlCursor cursor = node.newCursor();
            try {
                do {
                    cursor.toNextToken();
                    if (cursor.currentTokenType() == XmlCursor.TokenType.NAMESPACE
                            && prefix.equals(cursor.getName().getLocalPart())) {
                        return cursor.getName().getNamespaceURI();
                    }
                } while (cursor.currentTokenType() != XmlCursor.TokenType.START
                        && cursor.currentTokenType() != XmlCursor.TokenType.END
                        && cursor.currentTokenType() != XmlCursor.TokenType.ENDDOC);
            } finally {
                cursor.dispose();
            }
        }
        return null;
    }

    public boolean getQNameWithoutNamespaceUri(final QName qname1, final QName qname2) {
        return qname1.getLocalPart().equals(qname2.getLocalPart()) && qname1.getPrefix().equals(qname2.getPrefix());
    }

    public List<QName> getNamespaces() {
        final List<QName> result = new ArrayList<>();
        if (this.getType() == EXmlItemType.Element) {
            final XmlCursor cursor = node.newCursor();
            try {
                do {
                    cursor.toNextToken();
                    if (cursor.currentTokenType() == XmlCursor.TokenType.NAMESPACE) {
                        result.add(cursor.getName());
                    }
                } while (cursor.currentTokenType() != XmlCursor.TokenType.START
                        && cursor.currentTokenType() != XmlCursor.TokenType.END
                        && cursor.currentTokenType() != XmlCursor.TokenType.ENDDOC);
            } finally {
                cursor.dispose();
            }
        }
        return result;
    }

    public boolean removeNamespaceUri(final String prefix) {
        if (this.getType() == EXmlItemType.Element) {
            final XmlCursor cursor = node.newCursor();
            try {
                do {
                    cursor.toNextToken();
                    if (cursor.currentTokenType() == XmlCursor.TokenType.NAMESPACE
                            && prefix.equals(cursor.getName().getLocalPart())) {
                        cursor.toPrevToken();
                        return cursor.removeXml();
                    }
                } while (cursor.currentTokenType() != XmlCursor.TokenType.START
                        && cursor.currentTokenType() != XmlCursor.TokenType.END
                        && cursor.currentTokenType() != XmlCursor.TokenType.ENDDOC);
            } finally {
                cursor.dispose();
            }
        }
        return false;
    }

    public List<XmlDocumentItem> getChildItems() {
        List<XmlDocumentItem> XmlIt;
        XmlIt = new ArrayList<>();
        if (this.getType() == EXmlItemType.Attribute) {
            return Collections.<XmlDocumentItem>emptyList();
        } else {
            final XmlCursor cursor = node.newCursor();
            try {
                cursor.push();
                if (cursor.toFirstAttribute()) {
                    do {
                        XmlDocumentItem AtIt = new XmlDocumentItem(cursor.getObject(), EXmlItemType.Attribute);
                        XmlIt.add(AtIt);
                    } while (cursor.toNextAttribute());
                }
                cursor.pop();
                if (cursor.toFirstChild()) {
                    do {
                        XmlDocumentItem ChildIt = new XmlDocumentItem(cursor.getObject(), EXmlItemType.Element);
                        XmlIt.add(ChildIt);
                    } while (cursor.toNextSibling());
                }
            } finally {
                cursor.dispose();
            }
            return XmlIt;
        }

    }

    public XmlDocumentItem getParent() {
        final XmlCursor cursor = node.newCursor();
        try {
            cursor.toParent();
            return new XmlDocumentItem(cursor.getObject(), EXmlItemType.Element);
        } finally {
            cursor.dispose();
        }
    }

    public XmlDocumentItem createAttribute(final QName name, final String value, final int index) {
        final XmlCursor cursor = node.newCursor();
        try {
            if (index < 0) {
                cursor.toLastAttribute();
                cursor.toNextToken();
            } else {
                cursor.toFirstAttribute();
                for (int i = 0; i < index; i++) {
                    cursor.toNextAttribute();
                }
                if (index == getAttributesCount()) {
                    cursor.toNextToken();
                }
            }
            cursor.insertAttributeWithValue(name, value);
            cursor.toPrevToken();
            return new XmlDocumentItem(cursor.getObject(), EXmlItemType.Attribute);
        } finally {
            cursor.dispose();
        }
    }

    public XmlDocumentItem createAttribute(final QName name, final String value) {
        return createAttribute(name, value, -1);
    }

    public XmlDocumentItem createChildElement(final QName name, final String value, final int index) {
        final XmlCursor cursor = node.newCursor();
        try {
            prepareToCreateChild(cursor, index);
            cursor.insertElementWithText(name, value);
            cursor.toPrevSibling();
            return new XmlDocumentItem(cursor.getObject(), EXmlItemType.Element);
        } finally {
            cursor.dispose();
        }
    }
    
    private void prepareToCreateChild(final XmlCursor cursor, final int index){
        if (getChildCount() == 0 || index == 0) {
            cursor.toLastAttribute();
        } else {
            if (index < 0) {
                cursor.toLastChild();                    
            } else {
                cursor.toChild(index - 1);                    
            }
            cursor.toEndToken();
        }
        cursor.toNextToken();
        while (cursor.isNamespace()) {
            cursor.toNextToken();
        }        
    }

    public XmlDocumentItem createChildElement(final QName name, final String value) {
        return createChildElement(name, value, -1);
    }

    public void delete() {
        final XmlCursor cursor = node.newCursor();
        try {
            cursor.removeXml();
        } finally {
            cursor.dispose();
        }
    }

    public void setName(QName newName) {
        final XmlCursor cursor = node.newCursor();
        try {
            cursor.setName(newName);
            node = cursor.getObject();
        } finally {
            cursor.dispose();
        }
    }

    public void setContent(XmlObject xml) {
        node.set(xml);
    }

    public boolean moveChild(final XmlDocumentItem childToMove, final XmlDocumentItem childBefore) {
        final XmlCursor cursor1 = childToMove.node.newCursor();
        final XmlCursor cursor2 = childBefore.node.newCursor();
        try {
            if (childToMove.getType() != childBefore.getType()) {
                return false;
            } else {
                if (childBefore.getType() == EXmlItemType.Element) {
                    cursor2.toEndToken();
                }
                cursor2.toNextToken();
                cursor1.moveXml(cursor2);
                if (childBefore.getType() == EXmlItemType.Element) {
                    cursor2.toPrevSibling();
                } else {
                    cursor2.toPrevToken();
                }
                childToMove.node = cursor2.getObject();
                return true;
            }
        } finally {
            cursor1.dispose();
            cursor2.dispose();
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.node);
        hash = 89 * hash + Objects.hashCode(this.type);
        return hash;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final XmlDocumentItem other = (XmlDocumentItem) obj;
        if (this.type != other.type) {
            return false;
        }
        final XmlCursor cursor1 = node.newCursor();
        final XmlCursor cursor2 = other.node.newCursor();
        try{
            return cursor1.isAtSamePositionAs(cursor2);
        }finally{
            cursor1.dispose();
            cursor2.dispose();
        }        
    }
    
    
}
