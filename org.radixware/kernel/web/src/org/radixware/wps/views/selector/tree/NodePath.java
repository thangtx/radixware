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

package org.radixware.wps.views.selector.tree;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.wps.rwt.tree.Node;


class NodePath {

    private static class NodeDescriptor {

        private final Collection<Pid> pids;
        private final String displayName;
        private final Object userData;

        public NodeDescriptor(final Pid pid) {
            this.pids = Collections.singleton(pid);
            displayName = null;
            userData = null;
        }
        
        public NodeDescriptor(final Collection<Pid> pids){
            this.pids = pids;
            displayName = null;
            userData = null;            
        }

        public NodeDescriptor(final Node node) {
            if (node.getUserData() instanceof EntityModel){
                pids = Collections.singleton( ((EntityModel)node.getUserData()).getPid() );
                displayName = null;
                userData = null;
            }
            else if (node.getUserData()!=null){
                userData = node.getUserData();
                displayName = null;
                pids = null;
            }
            else{
                displayName = node.getDisplayName();
                pids = null;
                userData = null;
            }
        }
        
        private boolean isMatch(final Node node){
            if (node.getUserData() instanceof EntityModel){
                return pids.contains(((EntityModel)node.getUserData()).getPid());
            }
            else if (node.getUserData()!=null){
                return Objects.equals(userData, node.getUserData());
            }
            else{
                return Objects.equals(displayName, node.getDisplayName());
            }
        }
        
        public final Node findNode(final Node.Children children){
            for (Node node: children.getCreatedNodes()){
                if (isMatch(node)){
                    return node;
                }                
            }
            return null;
        }
        
        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final NodeDescriptor other = (NodeDescriptor) obj;
            if (!Objects.equals(this.pids, other.pids)) {
                return false;
            }
            if (!Objects.equals(this.displayName, other.displayName)) {
                return false;
            }
            if (!Objects.equals(this.userData, other.userData)) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            if (pids!=null){
                for (Pid pid: pids){
                    hash = 79 * hash + Objects.hashCode(pid);
                }
            }
            hash = 79 * hash + Objects.hashCode(this.displayName);
            hash = 79 * hash + Objects.hashCode(this.userData);
            return hash;
        }
    }
    
    private final List<NodeDescriptor> path = new LinkedList<>();
    
    private NodePath(){        
    }
    
    public static NodePath fromPid(final Pid pid){
        final NodePath instance = new NodePath();
        instance.path.add(new NodeDescriptor(pid));
        return instance;
    }
    
    public static NodePath fromPids(final Collection<Pid> pids){
        final NodePath instance = new NodePath();
        instance.path.add(new NodeDescriptor(pids));
        return instance;
    }    

    public static NodePath fromNode(final Node node, final Node endNode){
        final NodePath instance = new NodePath();
        for (Node n = node; n!=null && n!=endNode; n = n.getParentNode()) {
            instance.path.add(0,new NodeDescriptor(n));
        }
        return instance;
    }
    
    public Node findNode(final Node startFrom, final boolean nearest){
        Node currentNode = startFrom, nextNode;
        for (NodeDescriptor nodeDescriptor: path){
            currentNode.expand();
            nextNode = nodeDescriptor.findNode(currentNode.getChildNodes());
            if (nextNode==null){
                return nearest ? currentNode : null;
            }
            else{
                currentNode = nextNode;
            }
        }
        return currentNode;
    }
    
    public static NodePath concatinate(final NodePath path1, final NodePath path2){
        final NodePath result = new NodePath();
        result.path.addAll(path1.path);
        result.path.addAll(path2.path);
        return result;
    }
}
