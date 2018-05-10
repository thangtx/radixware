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

package org.radixware.kernel.common.client.models;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.enums.ESelectionMode;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.schemas.eas.SelectedObjects;


public final class EntityObjectsSelection {            
    
    private final GroupModel groupModel;
    private ESelectionMode mode = ESelectionMode.NO_SELECTION;    
    private List<Pid> selection;
    private SelectionNode hierarchicalSelectionNode;
    
    EntityObjectsSelection(final GroupModel groupModel){
        this.groupModel = groupModel;
    }
    
    EntityObjectsSelection(final EntityObjectsSelection copy){
        groupModel = null;
        writeFrom(copy);
    }
    
    public void selectAllObjectsInGroup(){
        if (groupModel==null){            
            mode = ESelectionMode.EXCLUSION;
            selection = null;
        }else if (!groupModel.isEmpty()) {
            final EntityObjectsSelection newSelection;
            if (groupModel.hasMoreRows()){
                newSelection = copy();
                newSelection.selectAllObjectsInGroup();                                
            }else{
                newSelection = new EntityObjectsSelection((GroupModel)null);
                final GroupModelReader reader = 
                    new GroupModelReader(groupModel,EnumSet.of(GroupModelReader.EReadingFlags.DONT_LOAD_MORE));
                for (EntityModel entityModel: reader){
                    newSelection.selectObject(entityModel.getPid());
                }
            }
            if (groupModel.notifyBeforeChangeSelection(newSelection)){
                writeFrom(newSelection);
                if (hierarchicalSelectionNode!=null){
                    hierarchicalSelectionNode.afterSelectAllObjects(groupModel, this);
                }
                groupModel.notifyAfterChangeSelection();
            }
        }
    };
    
    public void selectObject(final Pid objectPid){
        if (objectPid==null){
            throw new IllegalArgumentException("Pid argument must be not null");
        }
        if (isObjectSelected(objectPid)){
            return;
        }
        if (groupModel==null){
            if (mode==ESelectionMode.EXCLUSION){
                if (selection!=null){
                    selection.remove(objectPid);
                    if (selection.isEmpty()){
                        selection = null;
                    }
                }
            }else{
                if (selection==null){
                    selection = new LinkedList<>();                
                }
                selection.add(objectPid);
                mode = ESelectionMode.INCLUSION;
            }
        }else{
            final EntityObjectsSelection newSelection = copy();
            newSelection.selectObject(objectPid);
            if (groupModel.notifyBeforeChangeSelection(newSelection)){
                writeFrom(newSelection);
                if (hierarchicalSelectionNode!=null){
                    hierarchicalSelectionNode.afterSelectObject(groupModel, this);
                }
                groupModel.notifyAfterChangeSelection();            
            }
        }
    };
    
    public void unselectAllObjectsInGroup(){
        if (groupModel==null){
            selection = null;
            mode = ESelectionMode.NO_SELECTION;
        }else{
            final EntityObjectsSelection newSelection = copy();
            newSelection.unselectAllObjectsInGroup();
            if (groupModel.notifyBeforeChangeSelection(newSelection)){
                writeFrom(newSelection);
                if (hierarchicalSelectionNode!=null){
                    hierarchicalSelectionNode.afterUnselectAllObjects(groupModel, this);
                }
                groupModel.notifyAfterChangeSelection();                        
            }
        }
    }
    
    public void unselectObject(final Pid objectPid){
        if (objectPid!=null && isObjectSelected(objectPid)){
            if (groupModel==null){
                if (mode==ESelectionMode.EXCLUSION){
                    if (selection==null){
                        selection = new LinkedList<>();
                    }
                    selection.add(objectPid);
                }else if (selection!=null){
                    selection.remove(objectPid);
                    if (selection.isEmpty()){
                        selection = null;
                        mode = ESelectionMode.NO_SELECTION;
                    }
                }
            }else{
                final EntityObjectsSelection newSelection = copy();
                newSelection.unselectObject(objectPid);
                if (groupModel.notifyBeforeChangeSelection(newSelection)){
                    writeFrom(newSelection);
                    if (hierarchicalSelectionNode!=null){
                        hierarchicalSelectionNode.afterUnselectObject(groupModel, this);
                    }
                    groupModel.notifyAfterChangeSelection();                
                }
            }
        }
    };
    
    public void invertSelection(final Pid objectPid){
        if (isObjectSelected(objectPid)){
            unselectObject(objectPid);
        }else{
            selectObject(objectPid);
        }
    }
    
    public ESelectionMode getSelectionMode(){
        return isRecursivelySelected() ? ESelectionMode.EXCLUSION :  mode;
    };    
    
    public Collection<Pid> getSelectedObjects(){
        if (mode==ESelectionMode.EXCLUSION){
            throw new IllegalUsageError("All objects in this group was selected");
        }else{
            return selection==null ? Collections.<Pid>emptyList() : Collections.unmodifiableList(selection);
        }
    };
    
    public Collection<Pid> getUnselectedObjects(){
        if (mode==ESelectionMode.INCLUSION){
            throw new IllegalUsageError("All objects in this group was unselected");
        }else{
            return selection==null ? Collections.<Pid>emptyList() : Collections.unmodifiableList(selection);
        }
    };    
    
    public boolean isObjectSelected(final Pid objectPid){
        if (isRecursivelySelected()){
            return true;
        }
        return isExplicitlyObjectSelected(objectPid);
    };
    
    boolean isExplicitlyObjectSelected(final Pid objectPid){
        if (mode==ESelectionMode.EXCLUSION){
            return selection==null || !selection.contains(objectPid);
        }else if (mode==ESelectionMode.INCLUSION){
            return selection!=null && selection.contains(objectPid);
        }else{            
            return false;
        }        
    }
    
    public boolean isObjectSelected(final EntityModel object){
        return isObjectSelected(object.getPid());
    };
    
    private boolean allRowsWasLoaded(){
        return groupModel!=null && !groupModel.isEmpty() && !groupModel.hasMoreRows();
    }
    
    public boolean isEmpty(){
        if (isRecursivelySelected()){
            return false;
        }
        return isExplicitlyEmpty();        
    };
    
    boolean isExplicitlyEmpty(){
        if (mode==ESelectionMode.EXCLUSION && selection!=null && allRowsWasLoaded()){
            final GroupModelReader reader = new GroupModelReader(groupModel);
            for (EntityModel entity: reader){
                if (!selection.contains(entity.getPid())){
                    return false;
                }
            }
            return true;
        }        
        return mode==ESelectionMode.NO_SELECTION || (mode==ESelectionMode.INCLUSION && (selection==null || selection.isEmpty()));        
    }
    
    public boolean isAllObjectsSelected(){
        if (mode==ESelectionMode.EXCLUSION && (selection==null || selection.isEmpty())){
            return true;
        }else if (mode==ESelectionMode.INCLUSION && selection!=null && allRowsWasLoaded()){
            //if groupModel contains broken entity object then not all objects was selected
            return selection.size()==groupModel.getEntitiesCount();
        }else{
            return isRecursivelySelected();
        }
    }
    
    private boolean isRecursivelySelected(){
        return selection==null 
                   && hierarchicalSelectionNode!=null 
                   && !hierarchicalSelectionNode.isExplicitSelection()
                   && hierarchicalSelectionNode.isRecursivelySelected();
    }
    
    public boolean isSingleObjectSelected(){
        if (mode==ESelectionMode.INCLUSION){
            return selection!=null && selection.size()==1;
        }else if (mode==ESelectionMode.EXCLUSION && selection!=null && allRowsWasLoaded()){
            final GroupModelReader reader = new GroupModelReader(groupModel);
            boolean objectSelected = false;
            for (EntityModel entity: reader){
                if (!selection.contains(entity.getPid())){
                    if (objectSelected){
                        return false;
                    }else{
                        objectSelected = true;
                    }
                }
            }
            return true;
        }
        return false;
    }
    
    private EntityObjectsSelection copy(){
        return new EntityObjectsSelection(this);
    };
    
    EntityObjectsSelection normalize(final GroupModel groupModel){
        if (groupModel==null){
            return this;
        }
        final EntityObjectsSelection normalized = copy();
        if (normalized.mode==ESelectionMode.NO_SELECTION){
            normalized.selection = null;
            return normalized;
        }else if (normalized.mode==ESelectionMode.INCLUSION){
            if (normalized.selection==null || normalized.selection.isEmpty()){
                normalized.selection = null;
                normalized.mode = ESelectionMode.NO_SELECTION;
            }
            return normalized;
        }else if (groupModel.hasMoreRows()){
            if (normalized.selection!=null && normalized.selection.isEmpty()){
                normalized.selection = null;
            }
            return normalized;
        } else if (groupModel.isEmpty()){//normalized.mode==ESelectionMode.EXCLUSION && !groupModel.hasMoreRows()
            normalized.mode = ESelectionMode.NO_SELECTION;
            normalized.selection = null;
            return normalized;
        } else {//normalized.mode==ESelectionMode.EXCLUSION && !groupModel.hasMoreRows() && !groupModel.isEmpty()            
            normalized.selection = new LinkedList<>();
            final GroupModelReader reader = new GroupModelReader(groupModel);            
            for (EntityModel entity: reader){
                if (selection==null || !selection.contains(entity.getPid())){
                    normalized.selection.add(entity.getPid());
                }
            }
            if (normalized.selection.isEmpty()){
                normalized.mode = ESelectionMode.NO_SELECTION;
                normalized.selection = null;
            }else{
                normalized.mode = ESelectionMode.INCLUSION;
            }
            return normalized;        
        }
    }
    
    public EntityObjectsSelection getNormalized(){
        return normalize(groupModel);
    }
    
    void writeFrom(final EntityObjectsSelection copy){
        if (copy.selection==null && copy.isRecursivelySelected()){
            mode = ESelectionMode.EXCLUSION;
            selection = null;
        }else{
            mode = copy.mode;
            if (copy.selection==null){
                selection = null;
            }else{
                selection = new LinkedList<>(copy.selection);
            }
        }
    }
    
    public void clear(){
        if (groupModel==null){
            internalClear();
        }else{
            final EntityObjectsSelection newSelection = new EntityObjectsSelection((GroupModel)null);
            if (groupModel.notifyBeforeChangeSelection(newSelection)){
                writeFrom(newSelection);
                if (hierarchicalSelectionNode!=null){
                    hierarchicalSelectionNode.afterUnselectAllObjects(groupModel, this);
                }
                groupModel.notifyAfterChangeSelection();
            }
        }
    }
    
    void internalClear(){
        mode = ESelectionMode.NO_SELECTION;
        selection = null;
    }

    public void writeToXml(final SelectedObjects xml){
        final EntityObjectsSelection normalized = normalize(groupModel);
        if (!normalized.isEmpty()){
            xml.setSelectionMode(normalized.mode);
            if (normalized.selection!=null){
                for (Pid pid: normalized.selection){
                    xml.addObjectPid(pid.toString());
                }
            }
        }
    }
    
    void setHierarchicalSelectionNode(final SelectionNode node){
        hierarchicalSelectionNode = node;
    }
}