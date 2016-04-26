/*
* Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.wps.rwt;

import java.util.List;
import java.util.Stack;
import org.radixware.kernel.common.client.tree.IExplorerTree;
import org.radixware.kernel.common.client.tree.nodes.IExplorerTreeNode;
import org.radixware.kernel.common.client.widgets.IMainStatusBar;


public class MainStatusBar extends StatusBar implements IMainStatusBar{
    
    private IExplorerTreeNode node;
    
    private static class PathItem extends Label{
        
        private final IExplorerTreeNode node;
        
        public PathItem(final IExplorerTreeNode node){
            super(node.isValid() ? node.getView().getTitle() : "????");
            this.node = node;
            setTextWrapDisabled(true);
            getHtml().addClass("rwt-main-status-bar-path-item");
            getHtml().setCss("font-weight", "bold");
            getHtml().setAttr("onclick", "default");
        }

        @Override
        public void processAction(final String actionName, final String actionParam) {
            if (Events.EVENT_NAME_ONCLICK.equals(actionName)) {
                click();
            } else {
                super.processAction(actionName, actionParam);
            }        
        }
        
        private void click(){
            final IExplorerTree tree = node.getExplorerTree();
            if (tree.getCurrent()==node){
                tree.getActions().getGoToCurrentNodeAction().trigger();
            }else{
                node.getExplorerTree().setCurrent(node);
            }
        }
        
        public IExplorerTreeNode getNode(){
            return node;
        }
    }
    
    private static class PathDevider extends Label{
        
        public PathDevider(){
            super("/");
            getHtml().setCss("font-weight", "bold");
        }
    }

    @Override
    public void setText(final String text) {        
        super.setText(text);
        clearPathItems();
        getLabel().setVisible(true);
        node = null;
    }    
    
    private void clearPathItems(){        
        final List<UIObject> children = getLabelContainer().getChildren();
        for (int i=children.size()-1; i>0; i--){
            getLabelContainer().remove(children.get(i));
        }
    }

    @Override
    public void setHeight(final int h) {
        super.setHeight(h);
        final List<UIObject> children = getLabelContainer().getChildren();
        for (int i=children.size()-1; i>0; i--){
            if (children.get(i) instanceof Label){
                ((Label)children.get(i)).getHtml().setCss("line-height", getHeight() + "px");
            }
        }
    }

    @Override
    public void setCurrentExplorerTreeNode(final IExplorerTreeNode node) {
        if (node!=null){
            getLabel().setVisible(false);
            clearPathItems();
            final Stack<IExplorerTreeNode> path = new Stack<>();
            for (IExplorerTreeNode n=node; n!=null; n=n.getParentNode()){
                path.push(n);
            }
            while (!path.isEmpty()){
                addPathItem(path.pop());
            }
        }
        this.node = node;
    }
    
    private void addPathItem(final IExplorerTreeNode node){
        if (getLabelContainer().getChildren().size()>1){
            final Label pathDevider = new PathDevider();
            pathDevider.getHtml().setCss("line-height", getHeight() + "px");
            getLabelContainer().add(pathDevider);
        }
        final Label pathItem = new PathItem(node);
        pathItem.getHtml().setCss("line-height", getHeight() + "px");
        getLabelContainer().add(pathItem);
    }

    @Override
    public IExplorerTreeNode getCurrentExplorerTreeNode() {
        return node;
    }
    
}
