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

package org.radixware.kernel.explorer.widgets.selector;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.GroupModel;


public abstract class SelectorNode {
    
    private final SelectorNode parentNode;
    private final int nestingLevel;
    private final int hashCode;
    private List<GroupModel> childGroupModels;
    private Boolean hasChildren;
    
    public SelectorNode(final SelectorNode parentNode, final int hashCode){
        this.parentNode = parentNode;
        nestingLevel = parentNode==null ? 0 : parentNode.nestingLevel+1;
        this.hashCode = (parentNode==null ? 0 : parentNode.hashCode * 67) + hashCode;
    }
    
    public final SelectorNode getParentNode(){
        return parentNode;
    }
    
    public List<GroupModel> getChildGroupModels(){
        if (childGroupModels==null){
            return Collections.emptyList();
        }else{
            return Collections.unmodifiableList(childGroupModels);
        }
    }

    public void addChildGroupModel(final GroupModel groupModel){
        if (childGroupModels==null){
            childGroupModels = new LinkedList<>();
        }
        if (groupModel.getEntitiesCount()>0 || groupModel.hasMoreRows()){
            setHasChildren(true);
        }
        childGroupModels.add(groupModel);
    }
    
    public abstract EntityModel getEntityModel();
    
    public final boolean isChildrenInited(){
        return hasChildren!=null;
    }

    public final boolean hasChildren(){
        return hasChildren==null ? false : hasChildren;
    }

    public final void setHasChildren(final boolean hasInnerNodes){
        hasChildren = hasInnerNodes;
    }    
    
    public void invalidate(){
        hasChildren = null;
    }
    
    public final int getNestingLevel(){
        return nestingLevel;
    }        

    @Override
    public int hashCode() {
        return hashCode;
    }        

    @Override
    public boolean equals(final Object obj) {
        if (obj==this){
            return true;
        }
        if (obj instanceof SelectorNode==false) {
            return false;
        }
        final SelectorNode other = (SelectorNode) obj;
        if (this.nestingLevel != other.nestingLevel || this.hashCode!=other.hashCode) {
            return false;
        }
        SelectorNode otherParent = other.parentNode;
        for (SelectorNode parent=parentNode; parent!=null; parent=parent.parentNode){
            if (otherParent==null || parent.hashCode!=otherParent.hashCode){
                return false;
            }
            otherParent = otherParent.parentNode;
        }
        return otherParent==null;
    }
    
    
}