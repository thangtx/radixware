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
import org.radixware.kernel.common.client.editors.xmleditor.model.XmlModelItem;
import org.radixware.kernel.common.client.editors.xmleditor.view.IXmlTree;
import org.radixware.kernel.common.client.editors.xmleditor.view.IXmlTreeItem;


public class CreateChildAttributeOperation extends XmlOperation{
    
    private final QName name;
    private final String value;
    private final int index;
    
    public CreateChildAttributeOperation(final IXmlTreeItem parentItem, 
                                         final QName name, 
                                         String value,
                                         final int index){
        super(parentItem.getIndexPath());
        this.name = name;
        this.value = value;
        this.index = index;
    }
    
    public CreateChildAttributeOperation(final IXmlTreeItem parentItem, 
                                         final QName name, 
                                         final String value){
        this(parentItem,name,value,-1);
    }    

    @Override
    public boolean merge(final XmlOperation other) {
        return false;
    }

    @Override
    public boolean needToReinitParent() {
        return false;
    }        

    @Override
    public XmlOperation execute(final IXmlTree tree) {
        final IXmlTreeItem parentItem=tree.findByIndexPath(parentItemIndexPath, false);
        final int i = parentItem.getXmlItem().createChildAttribute(name, value, index);
        if (i >= 0) {
            final XmlModelItem attrItem = parentItem.getXmlItem().getVisibleChildItems().get(i);
            final IXmlTreeItem newItem = tree.addChildItem(parentItem, attrItem, i);            
            return new DeleteXmlNodeOperation(newItem);
        }
        return null;
    }
    
}
