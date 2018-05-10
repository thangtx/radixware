/*
* Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.kernel.common.client.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.radixware.kernel.common.client.tree.nodes.IExplorerTreeNode;


final class ExplorerTreeHistory {
    
    private final static int MAX_HISTORY_SIZE=10;
    
    private final List<IExplorerTreeNode> recentlyEnteredNodes = new ArrayList<>();
    private final List<IExplorerTreeNode> reenteredNodes = new ArrayList<>();
    
    public ExplorerTreeHistory(){        
    }

    public void addRecentlyEnteredNode(final IExplorerTreeNode node){
        addNode(node, recentlyEnteredNodes);
    }
    
    public void addReenteredNode(final IExplorerTreeNode node){
        addNode(node, reenteredNodes);        
    }
    
    public List<IExplorerTreeNode> getRecentlyEnteredNodes(){
        return Collections.unmodifiableList(recentlyEnteredNodes);
    }
    
    public List<IExplorerTreeNode> getReenterNodes(){
        return Collections.unmodifiableList(reenteredNodes);
    }
    
    public void removeNode(final IExplorerTreeNode node){
        recentlyEnteredNodes.remove(node);
        reenteredNodes.remove(node);
    }    
    
    public void clear(){
        recentlyEnteredNodes.clear();
        reenteredNodes.clear();
    }
    
    private static void addNode(final IExplorerTreeNode node, final List<IExplorerTreeNode> nodes){
        nodes.add(0, node);
        for (int i=nodes.size()-1; i>0; i--){
            if (i>MAX_HISTORY_SIZE-1 || nodes.get(i)==node){
                nodes.remove(i);
            }
        }
    }
    
    public void afterGoBack(final IExplorerTreeNode targetNode, final IExplorerTreeNode currentNode){
        moveNodes(recentlyEnteredNodes, reenteredNodes, targetNode);
        addReenteredNode(currentNode);
    }        
    
    public void afterGoForward(final IExplorerTreeNode targetNode, final IExplorerTreeNode currentNode){
        moveNodes(reenteredNodes, recentlyEnteredNodes, targetNode);
        addRecentlyEnteredNode(currentNode);
    }
    
    private static void moveNodes(final List<IExplorerTreeNode> sourceNodes, final List<IExplorerTreeNode> targetNodes, final IExplorerTreeNode node){
        final int index = sourceNodes.indexOf(node);
        if (index>=0){
            IExplorerTreeNode n;
            for (int i=index; i>=0; i--){
                n = sourceNodes.remove(i);
                if (i<index){
                    targetNodes.add(0,n);
                }
            }
            for (int i=targetNodes.size()-1; i>0; i--){
                if (i>MAX_HISTORY_SIZE-1){
                    targetNodes.remove(i);
                }
            }
        }
    }
}
