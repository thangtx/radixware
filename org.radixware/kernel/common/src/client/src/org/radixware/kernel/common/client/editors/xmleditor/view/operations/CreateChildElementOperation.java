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

package org.radixware.kernel.common.client.editors.xmleditor.view.operations;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.editors.xmleditor.model.XmlModelItem;
import org.radixware.kernel.common.client.editors.xmleditor.view.IXmlTree;
import org.radixware.kernel.common.client.editors.xmleditor.view.IXmlTreeItem;


public class CreateChildElementOperation extends XmlOperation {

    private final QName name;
    private final String value;
    private final XmlObject content;
    private final int index;

    public CreateChildElementOperation(final IXmlTreeItem parentItem, final QName name, final String value, final int index) {
        super(parentItem.getIndexPath());
        this.name = name;
        this.value = value;
        this.content = null;
        this.index = index;
    }

    public CreateChildElementOperation(final IXmlTreeItem parentItem,
            final QName name,
            final String value) {
        this(parentItem, name, value, -1);
    }

    public CreateChildElementOperation(final IXmlTreeItem parentItem,
            final QName name,
            final XmlObject content) {
        this(parentItem, name, content, -1);
    }
    
    public CreateChildElementOperation(final IXmlTreeItem parentItem, final QName name, final XmlObject content, final int index) {
        super(parentItem.getIndexPath());
        this.content = content;
        this.name = name;
        value = null;
        this.index = index;
    }    

    @Override
    public boolean merge(final XmlOperation other) {
        return false;
    }

    @Override
    public boolean needToReinitParent() {
        return true;
    }

    @Override
    public XmlOperation execute(final IXmlTree tree) {
        final IXmlTreeItem parentItem = tree.findByIndexPath(parentItemIndexPath, false);    
        final int i = parentItem.getXmlItem().createChildElement(name, value, index);
        if (i >= 0) {
            final XmlModelItem newModelItem = parentItem.getXmlItem().getVisibleChildItems().get(i);
            if (content != null) {
                newModelItem.getXmlNode().setContent(content);
            }
            final IXmlTreeItem newTreeItem = tree.addChildItem(parentItem, newModelItem, i);
            return new DeleteXmlNodeOperation(newTreeItem);
        }
        return null;
    }
}
