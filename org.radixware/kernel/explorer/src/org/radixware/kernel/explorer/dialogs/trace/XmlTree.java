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

package org.radixware.kernel.explorer.dialogs.trace;

import com.trolltech.qt.gui.QTreeWidget;
import com.trolltech.qt.gui.QTreeWidgetItem;
import com.trolltech.qt.gui.QWidget;
import com.trolltech.qt.xml.QDomDocument;
import com.trolltech.qt.xml.QDomDocument.Result;
import com.trolltech.qt.xml.QDomElement;
import com.trolltech.qt.xml.QDomNamedNodeMap;
import com.trolltech.qt.xml.QDomNode;


final class XmlTree extends QTreeWidget implements ITokenProvider {

    private QTreeWidgetItem currentItem = null;
    
    private class TokenItem implements IToken {

        @Override
        public String getValue() {
            return currentItem.text(0);
        }

        @Override
        public void select() {
            XmlTree.this.setCurrentItem(currentItem);
        }
        
    }
    
    public XmlTree(final QWidget parent) {
        super(parent);
        this.setColumnCount(1);
        this.headerItem().setHidden(true);
        this.itemSelectionChanged.connect(this, "itemSelectionChanged()");
    }
    
    private void itemSelectionChanged() {
        currentItem = this.currentItem();
    }
    
    public void setXml(String xml) {
        this.clear();
        xml = prepare(xml);
        if (xml == null)
            return;
        QDomDocument document = new QDomDocument();
        Result res = document.setContent(xml);
        if (!res.success)
            return;
        QDomElement element = document.documentElement();
        QTreeWidgetItem item = new QTreeWidgetItem(this);
        item.setText(0, getDescription(element));
        if (element.hasChildNodes()) {
            QDomNode node = element.firstChild();
            while (!node.isNull()) {
                    rec(node, item);
                node = node.nextSibling();
            }
        } 
        item.setExpanded(true);
    }
    
    private String getDescription(QDomNode node) {
        QDomElement element = node.toElement();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<" + element.nodeName());
        if (element.hasAttributes()) {
            QDomNamedNodeMap map = element.attributes();
            for (int i = 0; i < map.count(); ++i) {
                QDomNode attr = map.item(i);
                stringBuffer.append(" " + attr.nodeName() + "=\"" + attr.nodeValue() + "\"");
            }
        }
        stringBuffer.append(">");
        return stringBuffer.toString();
    }

    private void rec(QDomNode node, QTreeWidgetItem parent) {
        QTreeWidgetItem item = new QTreeWidgetItem(parent);
        if (node.isText()) {
            item.setText(0, node.nodeValue());
            return;
        }
        item.setText(0, getDescription(node));
        if (node.hasChildNodes()) {
            QDomNode child = node.firstChild();
            while (!child.isNull()) {
                    rec(child, item);
                child = child.nextSibling();
            }
        }
        item.setExpanded(true);
    }
    
    private String prepare(String str) {
        int beg = str.indexOf("<");
        int end = str.lastIndexOf(">");
        if (beg == -1 || end == -1 || beg > end)
            return null;
        return str.substring(beg, end + 1);
    }

    @Override
    public IToken getNextToken() {
        return new TokenItem();
    }

    @Override
    public IToken getPrevToken() {
        return new TokenItem();
    }

    @Override
    public boolean hasNextToken() {
        if (this.topLevelItemCount() == 0)
            return false;
        if (currentItem == null) {
            currentItem = this.topLevelItem(0);
            return true;
        }
        QTreeWidgetItem item = this.itemBelow(currentItem);
        if (item == null) {
            currentItem = this.currentItem();
            return false;
        }
        currentItem = item;
        return true;
    }

    @Override
    public boolean hasPrevToken() {
        if (this.topLevelItemCount() == 0)
            return false;
        if (currentItem == null)
            return false;
        QTreeWidgetItem item = this.itemAbove(currentItem);
        if (item == null) {
            currentItem = this.currentItem();
            return false;
        }
        currentItem = item;
        return true;
    }
    
}
