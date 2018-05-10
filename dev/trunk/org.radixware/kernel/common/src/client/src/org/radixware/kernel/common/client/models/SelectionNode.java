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


final class SelectionNode<T> {
    
    private final T parent;
    private final HierarchicalSelection<T> hierarchy;
    
    public SelectionNode (final HierarchicalSelection<T> selection, final T parent){
        this.parent = parent;
        this.hierarchy = selection;
    }
    
    public boolean isRecursivelySelected(){
        return hierarchy.hasRecursiveSelection(parent);
    }    
    
    public boolean isExplicitSelection(){
        return hierarchy.isExplicitSelection(parent);
    }
    
    public void afterSelectAllObjects(final GroupModel groupModel, final EntityObjectsSelection selection){
        hierarchy.afterSelectAllObjects(parent, groupModel, selection);
    }
    
    public void afterUnselectAllObjects(final GroupModel groupModel, final EntityObjectsSelection selection){
        hierarchy.afterUnselectAllObjects(parent, groupModel, selection);
    }
    
    public void afterSelectObject(final GroupModel groupModel, final EntityObjectsSelection selection){
        hierarchy.afterSelectObject(parent, groupModel, selection);
    }
    
    public void afterUnselectObject(final GroupModel groupModel, final EntityObjectsSelection selection){
        hierarchy.afterUnselectObject(parent, groupModel, selection);
    }
}
