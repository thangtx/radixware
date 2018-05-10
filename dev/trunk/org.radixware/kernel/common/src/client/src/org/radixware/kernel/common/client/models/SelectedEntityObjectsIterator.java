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

package org.radixware.kernel.common.client.models;

import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import org.radixware.kernel.common.client.enums.EHierarchyIterationMode;


public class SelectedEntityObjectsIterator<T> implements Iterator<EntityModel>{
    
    private final static class HierarchyPosition<T>{
        public final T parentNode;
        public final int globalChildIndex;
        public final GroupModel childModel;
        public final int rowInGroupModel;

        public HierarchyPosition(final T parentNode, 
                                             final int globalChildIndex, 
                                             final GroupModel childModel,
                                             final int rowInGroupModel) {
            this.parentNode = parentNode;
            this.globalChildIndex = globalChildIndex;
            this.childModel = childModel;
            this.rowInGroupModel = rowInGroupModel;
        }
        
    }
    
    private final HierarchicalSelection.IHierarchyDelegate<T> hierarchy;
    private final HierarchicalSelection<T> selection;
    private final Stack<T> stack = new Stack<>();
    private final EHierarchyIterationMode mode;
    
    private EntityModel next;
    
    SelectedEntityObjectsIterator(final HierarchicalSelection<T> selection,
                                                 final HierarchicalSelection.IHierarchyDelegate<T> hierarchy,                                                  
                                                 final T rootNode,
                                                 final EHierarchyIterationMode mode){
        this.hierarchy = hierarchy;
        this.selection  = selection;
        this.mode = mode;
        stack.push(rootNode);
    }

    @Override
    public boolean hasNext() {
        final T node = stack.pop();
        selection.restoreSelection(node);
        if (selection.hasRecursiveSelection(node)){
            final List<T> virtualNodes = hierarchy.getVirtualChildNodes(node);
            for (T childVirtualNode: virtualNodes){
                stack.push(childVirtualNode);
            }
            stack.pop();
            //if (virtuaNodes)
        }else if (selection.isExplicitSelection(node)){
            
        }
        return false;
    }

    @Override
    public EntityModel next() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("This operation is not supported");
    }


}
