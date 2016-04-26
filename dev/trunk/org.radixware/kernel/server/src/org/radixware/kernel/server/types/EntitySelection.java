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

package org.radixware.kernel.server.types;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.enums.ESelectionMode;
import org.radixware.schemas.eas.SelectedObjects;


public final class EntitySelection {
    
    public static final EntitySelection EMPTY = new EntitySelection(null, ESelectionMode.NO_SELECTION);
    
    public static class Factory{
        
        private Factory(){
        }
        
        public static EntitySelection loadFromXml(final SelectedObjects xml){
            if (xml==null || xml.getObjectPidList()==null || xml.getObjectPidList().isEmpty() || xml.getSelectionMode()==ESelectionMode.NO_SELECTION){
                return EMPTY;
            }
            final List<String> selection = new LinkedList<>(xml.getObjectPidList());
            return new EntitySelection(selection, xml.getSelectionMode());
        }        
    }

    private final Collection<String> selection;
    private final ESelectionMode mode;

    private EntitySelection(final Collection<String> selectedObjects, final ESelectionMode filteringMode){
        selection = selectedObjects;
        mode = filteringMode;
    }
    
    public boolean isEntitySelected(final Pid pid){
        switch (mode){
            case INCLUSION:
                return selection!=null && selection.contains(pid.toString());
            case EXCLUSION:
                return selection==null || !selection.contains(pid.toString());
            default:
                return false;
        }
    }
    
    public boolean isEntitySelected(final Entity entityObject){
        return isEntitySelected(entityObject.getPid());
    }
    
    public boolean isEmpty(){
        return selection==null || mode==ESelectionMode.NO_SELECTION;
    }    
    
    public ESelectionMode getMode(){
        return mode;
    }
        
    public Collection<String> getSelection(){
        return selection==null ? Collections.<String>emptyList() : Collections.unmodifiableCollection(selection);
    }
        
}
