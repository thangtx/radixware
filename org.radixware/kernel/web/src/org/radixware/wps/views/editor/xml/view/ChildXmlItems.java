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

import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.wps.rwt.tree.Node;
import org.radixware.kernel.common.client.editors.xmleditor.model.XmlModelItem;
import org.radixware.kernel.common.client.editors.xmleditor.view.XmlTreeController;


public class ChildXmlItems extends XmlTreeItem.Children {
    
    private final XmlModelItem parentNode;
    private final XmlTreeController treeController;
    private IClientEnvironment environment;

    public ChildXmlItems(XmlModelItem ll, XmlTreeController controller) {
        parentNode = ll;
        treeController = controller;
        environment=controller.getEnvironment();
    }

    public XmlTreeController getTreeController() {
        return treeController;
    }

    @Override
    protected List<Node> createNodes() {        
        final List<Node> nodes = new ArrayList<>();        
        if (!parentNode.getVisibleChildItems().isEmpty()) {
           for (XmlModelItem xmlItem : parentNode.getVisibleChildItems()){                
                nodes.add(setNodes(xmlItem, treeController));
            }
        }
        return nodes;
    }
    
    public XmlTreeItem addNewXmlItem(final XmlModelItem xmlItem, final int index){
        final XmlTreeItem treeItem = setNodes(xmlItem,treeController);
        if (index<0){
            add(treeItem);
        }else{
            add(index, treeItem);
        }        
        return treeItem;
    }

    private XmlTreeItem setNodes(XmlModelItem xmlItem, XmlTreeController treeController) {
        final XmlTreeItem treeNode = new XmlTreeItem();
        treeController.setupXmlTreeItem(treeNode, xmlItem);
        if (!xmlItem.getVisibleChildItems().isEmpty()) {
            treeNode.setChildNodes(new ChildXmlItems(xmlItem, treeController));
        }
        return treeNode;
    }
    
    public IClientEnvironment getEnvironment(){
        return environment;
    }
}
