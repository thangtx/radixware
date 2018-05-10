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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.enums.ESelectionMode;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.types.Id;


public class HierarchicalSelection<T> {
        
    public interface IHierarchyDelegate<T>{
        T getParent(final T child);
        List<GroupModel> getChildGroupModels(final T parent);
        List<T> getVirtualChildNodes(final T parent);
        int getVirtualChildNodeIndex(T parent, T node);
        T getEntityModelNode(T parent, GroupModel childGroupModel, EntityModel entityModel, int rowInGroup);
        GroupModel getOwnerGroupModel(T node);
        EntityModel getEntityModel(T node);
        String nodeToString(T node);
        boolean isEquals(final T first, final T second);
    }
    
    public interface ISelectionListener<T>{
        void afterSelectionChanged(HierarchicalSelection<T> selection);
    }
    
    private final static class NodePath{
        
        private final static NodePath ROOT = new NodePath(null);
        
        private final String asString;
        private final ArrStr path;
        
        private NodePath(final ArrStr path){
            this.path = path==null ? null : new ArrStr(path);
            asString = path==null ? "" : path.toString();
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 79 * hash + Objects.hashCode(this.asString);
            return hash;
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final NodePath other = (NodePath) obj;
            return Objects.equals(this.asString, other.asString);
        }                
        
        public static <T> NodePath getPath(final T node, final IHierarchyDelegate<T> hierarchy){
            if (node==null){
                return ROOT;
            }else{
                final ArrStr path = new ArrStr();
                EntityModel entityModel;
                for (T currentNode=node; currentNode!=null; currentNode=hierarchy.getParent(currentNode)){
                    entityModel = hierarchy.getEntityModel(currentNode);
                    if (entityModel==null){//virtual node
                        path.add(0, " "+hierarchy.nodeToString(node));
                    }else{
                        path.add(0, entityModel.getPid().toStr());
                    }
                }
                return new NodePath(path);
            }
        }
        
        public NodePath append(final EntityModel entityModel){
            return append(entityModel.getPid());
        }
        
        public NodePath append(final Pid pid){
            final ArrStr newPath = path==null ? new ArrStr() : new ArrStr(path);
            newPath.add(pid.toStr());
            return new NodePath(newPath);            
        }
        
        public NodePath appentVirtualNode(final String nodeAsStr){
            final ArrStr newPath = path==null ? new ArrStr() : new ArrStr(path);
            newPath.add(" "+nodeAsStr);
            return new NodePath(newPath);                        
        }
        
        public boolean isChildNode(final NodePath node){
            return path==null ? true : node.asString.startsWith(asString);
        }
    }
    
    private final static class ExplicitSelections<T>{
        
        private final Map<Integer,EntityObjectsSelection> entityObjectsSelection = new HashMap<>();//selections from group models at the same level
        private final List<T> nodesSelection = new ArrayList<>();//virtual nodes
        
        public ExplicitSelections(){
        }
        
        public ExplicitSelections(final int index, final GroupModel group){
            entityObjectsSelection.put(index, group.getSelection());
        }
        
        public ExplicitSelections(final int index, final T node){
            nodesSelection.add(node);
        }
        
        public final EntityObjectsSelection add(final int index, final GroupModel group){
            final EntityObjectsSelection selection;
            if (entityObjectsSelection.containsKey(index)){
                selection = null;
            }else{
                selection = group.getSelection();
                entityObjectsSelection.put(index, selection);
            }
            return selection;
        }
        
        public final boolean add(final T node){
            if (nodesSelection.contains(node)){
                return false;                
            }else{
                nodesSelection.add(node);
                return true;
            }
        }
        
        public final boolean removeNode(final T node){
            return nodesSelection.remove(node);
        }
        
        public final boolean removeAllVirtualNodes(){
            if (nodesSelection.isEmpty()){                                
                return false;
            }else{
                nodesSelection.clear();
                return true;
            }
        }
        
        public final void removeSelection(final int index){
            entityObjectsSelection.remove(index);
        }
        
        public final void clear(){
            for (EntityObjectsSelection selection: entityObjectsSelection.values()){
                selection.clear();
            }
            entityObjectsSelection.clear();
            nodesSelection.clear();
        }
        
        public final boolean isEmpty(){
            for (EntityObjectsSelection selection: entityObjectsSelection.values()){
                if (!selection.isExplicitlyEmpty()){
                    return false;
                }
            }
            return nodesSelection.isEmpty();
        }
        
        public final boolean isSelected(final T node){
            return nodesSelection.contains(node);
        }
        
        public final boolean isSelected(final EntityModel entityModel){
            for (EntityObjectsSelection selection: entityObjectsSelection.values()){
                if (selection.isExplicitlyObjectSelected(entityModel.getPid())){
                    return true;
                }
            }
            return false;
        }
        
        public final boolean isSingleEntityObjectSelected(){
            if (nodesSelection.isEmpty() && entityObjectsSelection.size()==1){
                return entityObjectsSelection.values().iterator().next().isSingleObjectSelected();
            }else{
                return false;
            }
        }
    }
    
    private final static class StoredExplicitSelections<T> {
        
        private final Map<Integer,EntityObjectsSelection> storedEntityObjectsSelection = new HashMap<>();
        private final List<String> storedNodesSelection = new LinkedList<>();
        private final List<Id> selectorPresentationIds = new LinkedList<>();
        
        private StoredExplicitSelections(){            
        }
        
        public static <T> StoredExplicitSelections<T> getInstance(final ExplicitSelections<T> selections, final IHierarchyDelegate<T> hierarchy, final T parentNode){
            final StoredExplicitSelections<T> instance = new StoredExplicitSelections<>();
            String storedNode;
            for (T node: selections.nodesSelection){
                storedNode = hierarchy.nodeToString(node);
                if (storedNode!=null && !storedNode.isEmpty() && !instance.storedNodesSelection.contains(storedNode)){
                    instance.storedNodesSelection.add(storedNode);
                }                
            }
            final List<GroupModel> childGroupModels = hierarchy.getChildGroupModels(parentNode);
            for (GroupModel childGroup: childGroupModels){
                instance.selectorPresentationIds.add(childGroup.getSelectorPresentationDef().getId());
            }
            for (Map.Entry<Integer,EntityObjectsSelection> entry: selections.entityObjectsSelection.entrySet()){
                instance.storedEntityObjectsSelection.put(entry.getKey(), new EntityObjectsSelection(entry.getValue()));
            }            
            return instance;            
        }
    
        public ExplicitSelections<T> restore(final IHierarchyDelegate<T> hierarchy, final T parentNode){
            final ExplicitSelections<T> selections = new ExplicitSelections<>();
            final List<T> virtualChildNodes = hierarchy.getVirtualChildNodes(parentNode);
            for (T node: virtualChildNodes){
                if (storedNodesSelection.contains(hierarchy.nodeToString(node))){
                    selections.add(node);
                }
            }
            final List<GroupModel> childGroupModels = hierarchy.getChildGroupModels(parentNode);
            if (childGroupModels.size()!=selectorPresentationIds.size()){
                return selections;
            }
            for (int i=selectorPresentationIds.size()-1; i>=0; i--){
                if (!selectorPresentationIds.get(i).equals(childGroupModels.get(i).getSelectorPresentationDef().getId())){
                    return selections;
                }
            }
            EntityObjectsSelection selection;
            for (int i=childGroupModels.size()-1; i>=0; i--){
                selection = storedEntityObjectsSelection.get(i);
                if (selection!=null){
                    childGroupModels.get(i).getSelection().writeFrom(selection);
                    selections.add(i, childGroupModels.get(i));
                }
            }
            return selections;
        }
        
        public final boolean isEmpty(){
            for (EntityObjectsSelection selection: storedEntityObjectsSelection.values()){
                if (!selection.isEmpty()){
                    return false;
                }
            }
            return storedNodesSelection.isEmpty();
        }
        
        public final boolean isSingleEntityObjectSelected(){
            if (storedNodesSelection.isEmpty() && storedEntityObjectsSelection.size()==1){
                return storedEntityObjectsSelection.values().iterator().next().isSingleObjectSelected();
            }else{
                return false;
            }
        }        
    }
    
    private final List<T> recursivelySelected = new ArrayList<>();
    private final List<T> recursivelyUnselected = new ArrayList<>();
    private final Map<T,ExplicitSelections<T>> explicitSelections = new HashMap<>();
    
    private final List<NodePath> storedRecursivelySelected = new ArrayList<>();
    private final List<NodePath> storedRecursivelyUnselected = new ArrayList<>();
    private final Map<NodePath, StoredExplicitSelections<T>> storedExplicitSelections = new HashMap<>();
    
    private final List<GroupModel> subscriptions = new LinkedList<>();    
    private final IHierarchyDelegate<T> hierarchy;
    
    private final List<ISelectionListener<T>> listeners = new LinkedList<>();
    
    private boolean internalChange;

    public HierarchicalSelection(final IHierarchyDelegate<T> delegate){
        this.hierarchy = delegate;
    }
    
    public boolean selectAllChildNodes(final T parent, final boolean cascade){
        if (hasRecursiveSelection(parent)){
            return false;
        }
        if (cascade){
            clear(parent, true, true, true);
            recursivelySelected.add(parent);
            notifyListeners();
            return true;
        }else{
            final ExplicitSelections<T> selections = getSelection(parent);
            final List<GroupModel> childGroupModels = hierarchy.getChildGroupModels(parent);
            boolean selectionChanged = false;
            GroupModel groupModel;
            internalChange = true;
            try{                
                for (int i=childGroupModels.size()-1; i>=0; i--){
                    groupModel = childGroupModels.get(i);
                    if (!groupModel.getSelection().isAllObjectsSelected()){
                        groupModel.getSelection().selectAllObjectsInGroup();
                        selectionChanged = true;
                    }
                    selections.add(i, groupModel);
                }
            }finally{
                internalChange = false;
            }
            final List<T> virtualNodes = hierarchy.getVirtualChildNodes(parent);            
            for (int i=virtualNodes.size()-1; i>=0; i--){                
                if (selections.add(virtualNodes.get(i))){
                    selectionChanged = true;
                }
            }
            if (selectionChanged){
                notifyListeners();
                return true;
            }else{
                return false;
            }
        }        
    }
    
    public boolean unselectAllChildNodes(final T parent, final boolean cascade){
        T recursivelySelectedParentNode = null;
        if (!explicitSelections.containsKey(parent)){
            for (T node = parent; node!=null; node=hierarchy.getParent(node)){
                if (recursivelyUnselected.contains(node)){
                    return false;
                }
                if (recursivelySelected.contains(node)){
                    recursivelySelectedParentNode = node;
                    break;
                }
            }
        }
        final boolean isRecursivelySelected = recursivelySelectedParentNode!=null;
        if (cascade){
            clear(parent, true, true, true);
            if (isRecursivelySelected
                && !hierarchy.isEquals(recursivelySelectedParentNode, parent)){
                recursivelyUnselected.add(parent);
            }
            notifyListeners();
            return false;
        }else{
            final ExplicitSelections<T> explicitSelection = getSelection(parent);
            final List<GroupModel> childGroups = hierarchy.getChildGroupModels(parent);
            boolean selectionChanged = false;
            GroupModel groupModel;            
            internalChange = true;
            try{
                for (int i=childGroups.size()-1; i>=0; i--){
                    groupModel = childGroups.get(i);
                    if (!groupModel.getSelection().isEmpty()){
                        childGroups.get(i).getSelection().unselectAllObjectsInGroup();  
                        selectionChanged = true;
                    }                    
                    if (isRecursivelySelected){
                        explicitSelection.add(i, childGroups.get(i));
                    }
                }
            }finally{
                internalChange = false;
            }
            if (isRecursivelySelected){
                if (explicitSelection.removeAllVirtualNodes()){
                    selectionChanged = true;
                }
            }else{
                explicitSelections.remove(parent);
            }
            if (selectionChanged){
                notifyListeners();
                return true;
            }else{
                return false;
            }
        }        
    }        
    
    public boolean select(final T node){
        if (hasRecursiveSelection(node)){
            return false;
        }
        final EntityModel entityModel = hierarchy.getEntityModel(node);
        if (entityModel instanceof BrokenEntityModel){
            return false;
        }
        final GroupModel group = hierarchy.getOwnerGroupModel(node);
        final T parentNode = hierarchy.getParent(node);
        if (group!=null && entityModel!=null){
            if (group.getRestrictions().getIsMultipleSelectionRestricted() ||
                group.getSelection().isObjectSelected(entityModel.getPid())){
                return false;
            }
            internalChange = true;
            try{
                registerGroupModelSelection(parentNode, group, false);
                group.getSelection().selectObject(entityModel.getPid());
            }finally{
                internalChange = false;
            }            
            notifyListeners();
            return true;
        }else{
            final int nodeIndex = getVirtualChildNodeIndex(parentNode, node);
            if (nodeIndex>-1 && getSelection(parentNode).add(node)){
                notifyListeners();
                return true;
            }else{
                return false;
            }
        }        
    }
    
    public boolean unselect(final T node){
        final EntityModel entityModel = hierarchy.getEntityModel(node);
        if (entityModel instanceof BrokenEntityModel){
            return false;
        }                        
        final T parentNode = hierarchy.getParent(node);
        ExplicitSelections<T> selections = explicitSelections.get(parentNode);
        boolean isRecursivelySelected = false;
        if (selections==null){
            for (T parent = parentNode; parent!=null; parent=hierarchy.getParent(parent)){
                if (recursivelyUnselected.contains(parent)){
                    return false;
                }
                if (recursivelySelected.contains(parent)){
                    isRecursivelySelected = true;
                    break;
                }
            }
        }else if (entityModel!=null && !selections.isSelected(entityModel)) {
            return false;
        }
        final GroupModel group = hierarchy.getOwnerGroupModel(node);
        if (group!=null && entityModel!=null){
            internalChange = true;
            try{
                registerGroupModelSelection(parentNode, group, isRecursivelySelected);
                group.getSelection().unselectObject(entityModel.getPid());
            }finally{
                internalChange = false;
            }
            afterUnselect(parentNode, group, isRecursivelySelected);
            notifyListeners();
            return true;
        }else{
            selections = getSelection(parentNode);
            final int nodeIndex = getVirtualChildNodeIndex(parentNode, node);
            if (nodeIndex>-1 && selections.removeNode(node)){
                if (!isRecursivelySelected && selections.isEmpty()){
                    explicitSelections.remove(parentNode);
                }
                notifyListeners();
                return true;
            }else{
                return false;
            }
        }
    }
    
    public boolean invertSelection(final T node){
        final boolean isSelected = isSelected(node);
        final boolean isRecursivelySelected = isSelected && hasRecursiveSelection(node);
        final T parentNode = hierarchy.getParent(node);
        final GroupModel group = hierarchy.getOwnerGroupModel(node);
        final EntityModel entityModel = hierarchy.getEntityModel(node);
        if (group!=null && entityModel!=null){            
            if (entityModel instanceof BrokenEntityModel
                || group.getRestrictions().getIsMultipleSelectionRestricted()){
                return false;
            }            
            internalChange = true;
            try{
                registerGroupModelSelection(parentNode, group, isRecursivelySelected);
                if (isSelected){
                    group.getSelection().unselectObject(entityModel.getPid());
                }else{
                    group.getSelection().selectObject(entityModel.getPid());
                }
            }finally{
                internalChange = false;
            }
            if (isSelected){
                afterUnselect(parentNode, group);
            }else{
                notifyListeners();
            }          
            return true;
        }else{
            final int nodeIndex = getVirtualChildNodeIndex(parentNode, node);
            if (nodeIndex>-1){            
                final ExplicitSelections<T> selection = getSelection(parentNode);            
                if (isSelected){
                    selection.removeNode(node);
                    if (selection.isEmpty() && !hasRecursiveSelection(node)){
                        explicitSelections.remove(node);
                    }
                }else{
                    selection.add(node);
                }
            }
            notifyListeners();
            return true;
        }
    }
    
    public void subscribe(final T parent, final GroupModel childGroupModel){
        childGroupModel.getSelection().setHierarchicalSelectionNode(new SelectionNode<>(this, parent));
        subscriptions.add(childGroupModel);
    }
    
    public void removeSubscription(final GroupModel groupModel){
        groupModel.getSelection().setHierarchicalSelectionNode(null);
        subscriptions.remove(groupModel);
    }
    
    public void removeAllSubscriptions(){
        for (GroupModel groupModel: subscriptions){
            groupModel.getSelection().setHierarchicalSelectionNode(null);
        }
        subscriptions.clear();        
    }    
    
    public boolean isSelected(final T node){
        T parent = hierarchy.getParent(node);
        final GroupModel ownerGroup = hierarchy.getOwnerGroupModel(node);
        if (ownerGroup!=null && ownerGroup.getRestrictions().getIsMultipleSelectionRestricted()){
            return false;
        }
        final ExplicitSelections<T> selections = explicitSelections.get(parent);
        if (selections!=null){
            final EntityModel entityModel = hierarchy.getEntityModel(node);
            return entityModel==null ? selections.isSelected(node) : selections.isSelected(entityModel);
        }
        do{
            if (recursivelyUnselected.contains(parent)){
                return false;
            }else if (recursivelySelected.contains(parent)){
                return true;
            }
            if (parent==null){
                return false;
            }
            parent = hierarchy.getParent(parent);
        }while(true);
    }    

    public boolean isSomeChildNodeSelected(final T parentNode, final boolean checkStored){        
        boolean isRecursivelySelected = false;
        for (T parent = parentNode; parent!=null; parent=hierarchy.getParent(parent)){
            if (recursivelyUnselected.contains(parent)){
                isRecursivelySelected = false;
                break;
            }
            if (recursivelySelected.contains(parent)){
                isRecursivelySelected = true;
                break;
            }
        }
        if (isRecursivelySelected){
            final ExplicitSelections<T> selections = explicitSelections.get(parentNode);
            return selections==null || !selections.isEmpty();
        }        
        for (Map.Entry<T,ExplicitSelections<T>> entry: explicitSelections.entrySet()){
            if (isChild(parentNode, entry.getKey(), true) && !entry.getValue().isEmpty()){
                return true;
            }
        }
        for (T node: recursivelySelected){
            if (isChild(parentNode, node, true)){
                return true;
            }
        }
        if (checkStored
            && (!storedRecursivelySelected.isEmpty() || !storedExplicitSelections.isEmpty())){//no need to check storedRecursivelyUnselected.isEmpty here
            if (parentNode==null){
                return true;
            }
            final NodePath parentNodePath = NodePath.getPath(parentNode, hierarchy);
            for (NodePath path: storedRecursivelySelected){
                if (parentNodePath.isChildNode(path)){
                    return true;
                }
                //no need to check if path.isChildNode(parentNodePath) to update isRecursivelySelected
            }
            for (Map.Entry<NodePath,StoredExplicitSelections<T>> entry: storedExplicitSelections.entrySet()){
                if (parentNodePath.isChildNode(entry.getKey()) && !entry.getValue().isEmpty()){
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean isEmpty(){
        return recursivelySelected.isEmpty() && explicitSelections.isEmpty() 
                  && storedRecursivelySelected.isEmpty() && storedExplicitSelections.isEmpty();
    }
    
    public boolean isSingleObjectSelected(){
        if (recursivelySelected.isEmpty() && storedRecursivelySelected.isEmpty()){
            if (storedExplicitSelections.isEmpty() && explicitSelections.size()==1){
                return explicitSelections.values().iterator().next().isSingleEntityObjectSelected();
            }else if (explicitSelections.isEmpty() && storedExplicitSelections.size()==1){
                return storedExplicitSelections.values().iterator().next().isSingleEntityObjectSelected();
            }
        }
        return false;
    }
    
    public boolean isAllObjectsSelected(){
        if (recursivelySelected.contains(null)){
            return  recursivelyUnselected.isEmpty() 
                       && storedRecursivelyUnselected.isEmpty()
                       && explicitSelections.isEmpty()
                       && storedExplicitSelections.isEmpty();
                       
        }else{
            return false;
        }
    }
    
    public boolean isExplicitSelection(final T parentNode){
        return explicitSelections.containsKey(parentNode);
    }
    
    public boolean hasRecursiveSelection(final T node){
        for (T n = node; n!=null; n=hierarchy.getParent(n)){
            if (recursivelyUnselected.contains(n)){
                return false;
            }
            if (recursivelySelected.contains(n)){
                return true;
            }
        }
        return recursivelySelected.contains(null);
    }    
    
    public void close(){
        listeners.clear();
        clear(null, true, false/*leave selection in GroupModel(s)*/, true);
        removeAllSubscriptions();
    }
    
    public void clear(final T parent, final boolean inclusive, final boolean clearEntityObjectsSelection, final boolean clearStoredSelection){        
        removeChildNodes(recursivelySelected, parent, inclusive);
        removeChildNodes(recursivelyUnselected, parent, inclusive);
        final List<T> childNodes = new LinkedList<>();
        for (T node: explicitSelections.keySet()){
            if (parent==null || isChild(parent, node, inclusive)){
                childNodes.add(node);
            }
        }
        internalChange = true;
        try{
            ExplicitSelections<T> selection;
            for (T node: childNodes){
                selection = explicitSelections.remove(node);
                if (selection!=null && clearEntityObjectsSelection){
                    selection.clear();
                }
            }
        }finally{
            internalChange = false;
        }
        if (clearStoredSelection && isSomeSelectionStored()){
            if (parent==null){
                storedRecursivelySelected.clear();
                storedRecursivelyUnselected.clear();
                storedExplicitSelections.clear();
            }else{
                final NodePath parentNodePath = NodePath.getPath(parent, hierarchy);
                removeChildNodes(storedRecursivelySelected, parentNodePath);
                removeChildNodes(storedRecursivelyUnselected, parentNodePath);
                final List<NodePath> childNodesPath = new LinkedList<>();
                for (NodePath path: storedExplicitSelections.keySet()){
                    if (parentNodePath.isChildNode(path)){
                        childNodesPath.add(path);
                    }
                }
                for (NodePath path: childNodesPath){
                    storedExplicitSelections.remove(path);
                }
            }
        }
        if (clearStoredSelection){
            notifyListeners();
        }
    }
    
    public void store(final T parent){
        if (parent==null){
            clearStoredSelection();
        }
        for (T node: recursivelySelected){
            if (parent==null || isChild(parent, node, false)){
                storedRecursivelySelected.add(NodePath.<T>getPath(node, hierarchy));
            }
        }
        for (T node: recursivelyUnselected){
            if (parent==null || isChild(parent, node, false)){
                storedRecursivelyUnselected.add(NodePath.<T>getPath(node, hierarchy));
            }
        }
        NodePath path;
        StoredExplicitSelections<T> storedSelections;
        T node;
        for (Map.Entry<T,ExplicitSelections<T>> entry: explicitSelections.entrySet()){
            node = entry.getKey();
            if (parent==null || isChild(parent, node, false)){
                path = NodePath.<T>getPath(node, hierarchy);
                storedSelections = StoredExplicitSelections.<T>getInstance(entry.getValue(), hierarchy, parent);
                storedExplicitSelections.put(path, storedSelections);
            }
        }
    }        
    
    public void restoreSelection(final T node){
        final NodePath path = NodePath.getPath(node, hierarchy);
        {
            final int index = storedRecursivelySelected.indexOf(path);
            if (index>-1){
                recursivelySelected.add(node);
                storedRecursivelySelected.remove(index);
            }
        }
        {
            final int index = storedRecursivelyUnselected.indexOf(path);
            if (index>-1){
                recursivelyUnselected.add(node);
                storedRecursivelyUnselected.remove(index);
            }
        }
        {
            final StoredExplicitSelections<T> storedSelections = storedExplicitSelections.remove(path);
            if (storedSelections!=null){
                explicitSelections.put(node, storedSelections.restore(hierarchy, node));
            }
        }
    }
    
    public boolean isSomeSelectionStored(){
        return !storedRecursivelySelected.isEmpty() 
                   || !storedRecursivelyUnselected.isEmpty()
                   || !storedExplicitSelections.isEmpty();           
    }
    
    public void addListener(final ISelectionListener<T> listener){
        if (!listeners.contains(listener)){
            listeners.add(listener);
        }
    }
    
    public void removeListener(final ISelectionListener<T> listener){
        listeners.remove(listener);
    }
    
    private void clearStoredSelection(){
        storedRecursivelySelected.clear();
        storedRecursivelyUnselected.clear();
        storedExplicitSelections.clear();        
    }        
        
    private ExplicitSelections<T> getSelection(final T parent){
        ExplicitSelections<T> selections = explicitSelections.get(parent);
        if (selections==null){
            selections = new ExplicitSelections<>();
            explicitSelections.put(parent, selections);
        }
        return selections;
    }
    
    private void removeChildNodes(final List<T> nodes, final T parent, final boolean inclusive){
        if (parent==null){
            nodes.clear();
        }else{
            for (int i=nodes.size()-1; i>=0; i--){
                if (isChild(parent, nodes.get(i), inclusive)){
                    nodes.remove(i);
                }
            }
        }
    }
    
    private void removeChildNodes(final List<NodePath> nodes, final NodePath parentNodePath){
        if (parentNodePath==null){
            nodes.clear();
        }else{
            for (int i=nodes.size()-1; i>=0; i--){
                if (parentNodePath.isChildNode(nodes.get(i))){
                    nodes.remove(i);
                }
            }
        }
    }
    
    private boolean isChild(final T parent, final T child, final boolean inclusive){
        if (parent==null){
            return true;
        }
        for (T node=inclusive ? child : hierarchy.getParent(child); node!=null; node=hierarchy.getParent(node)){
            if (hierarchy.isEquals(parent, node)){//NOPMD
                return true;
            }
        }
        return false;
    }
    
    private void afterSelect(final T parentNode, final GroupModel groupModel){
        if (!internalChange && !hasRecursiveSelection(parentNode)){
            registerGroupModelSelection(parentNode, groupModel, false);
            notifyListeners();
        }
    }
    
    private void registerGroupModelSelection(final T parentNode, final GroupModel groupModel, final boolean recursivelySelected){
        final int groupModelIndex = getChildGroupModelIndex(parentNode, groupModel);
        if (groupModelIndex>=0){
            final EntityObjectsSelection selection = getSelection(parentNode).add(groupModelIndex, groupModel);
            if (recursivelySelected 
                && selection!=null 
                && selection.getSelectionMode()==ESelectionMode.NO_SELECTION){
                EntityObjectsSelection newSelection = new EntityObjectsSelection(selection);
                newSelection.selectAllObjectsInGroup();
                selection.writeFrom(newSelection);
            }
        }
    }
    
    private void afterUnselect(final T parentNode, final GroupModel groupModel){
        if (!internalChange){
            boolean isRecursivelySelected = false;
            if (!explicitSelections.containsKey(parentNode)){
                for (T parent = parentNode; parent!=null; parent=hierarchy.getParent(parent)){
                    if (recursivelyUnselected.contains(parent)){
                        return;
                    }
                    if (recursivelySelected.contains(parent)){
                        isRecursivelySelected = true;
                        break;
                    }
                }
            }
            afterUnselect(parentNode, groupModel, isRecursivelySelected);
            notifyListeners();
        }
    }    
    
    private void afterUnselect(final T parentNode, final GroupModel groupModel, final boolean isRecursivelySelected){
        final ExplicitSelections<T> explicitSelection = getSelection(parentNode);
        final EntityObjectsSelection selection = groupModel.getSelection();
        final int groupModelIndex = getChildGroupModelIndex(parentNode, groupModel);
        if (!isRecursivelySelected && selection.isEmpty()){
            if (groupModelIndex>=0){
                explicitSelection.removeSelection(groupModelIndex);
            }
            if (explicitSelection.isEmpty()){
                explicitSelections.remove(parentNode);
            }
        }else if (groupModelIndex>=0){
            explicitSelection.add(groupModelIndex, groupModel);
        }        
    }
    
    private int getChildGroupModelIndex(final T parentNode, final GroupModel groupModel){
        final List<GroupModel> childGroupModels = hierarchy.getChildGroupModels(parentNode);
        for (int i=childGroupModels.size()-1; i>=0; i--){
            if (groupModel==childGroupModels.get(i)){
                return i;
            }
        }
        return -1;
    }
    
    private int getVirtualChildNodeIndex(final T parentNode, final T virtualNode){
        final List<T> childNodes = hierarchy.getVirtualChildNodes(parentNode);
        for (int i=childNodes.size()-1; i>=0; i--){
            if (childNodes.get(i).equals(virtualNode)){
                return i;
            }
        }
        return -1;
    }    
    
    void afterSelectAllObjects(final T parentObject, final GroupModel groupModel, final EntityObjectsSelection selection){
        afterSelect(parentObject, groupModel);
    }
    
    void afterUnselectAllObjects(final T parentObject, final GroupModel groupModel, final EntityObjectsSelection selection){
        afterUnselect(parentObject, groupModel);
    }
    
    void afterSelectObject(final T parentObject, final GroupModel groupModel, final EntityObjectsSelection selection){
        afterSelect(parentObject, groupModel);
    }
    
    void afterUnselectObject(final T parentObject, final GroupModel groupModel, final EntityObjectsSelection selection){
        afterUnselect(parentObject, groupModel);
    }        
        
    private void notifyListeners(){
        if (!listeners.isEmpty()){
            final List<ISelectionListener<T>> copyListeners = new LinkedList<>(listeners);
            for (ISelectionListener<T> listener: copyListeners){
                listener.afterSelectionChanged(this);
            }
        }
    }
}