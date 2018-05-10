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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.eas.EntityObjectTitles;
import org.radixware.kernel.common.client.eas.EntityObjectTitlesProvider;
import org.radixware.kernel.common.client.errors.ObjectNotAccessibleError;
import org.radixware.kernel.common.client.errors.ObjectNotFoundError;
import org.radixware.kernel.common.client.meta.RadEditorPresentationDef;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.IContext;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.exceptions.WrongFormatError;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.clientstate.ExplorerTreePath;
import org.radixware.schemas.clientstate.ExplorerTreeState;


final class ChoosenObjectsProvider {
    
    private static final class ObjectInfo{        
        
        private final Pid pid;
        private final Id editorPresentationId;
        
        public ObjectInfo(final org.radixware.schemas.clientstate.ExplorerTreeNode.Object xmlObj){
            editorPresentationId = xmlObj.getEditorPresentationId();
            final Id tableId = xmlObj.getTableId();
            pid = new Pid(tableId, xmlObj.getPID());
        }

        public Pid getPid() {
            return pid;
        }

        public Id getEditorPresentationId() {
            return editorPresentationId;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 89 * hash + Objects.hashCode(this.pid);
            hash = 89 * hash + Objects.hashCode(this.editorPresentationId);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final ObjectInfo other = (ObjectInfo) obj;
            if (!Objects.equals(this.pid, other.pid)) {
                return false;
            }
            return Objects.equals(this.editorPresentationId, other.editorPresentationId);
        }

        @Override
        public String toString() {
            final String asStr="{ PID=\'%1$s\' tableId=#%2$s  editorPresentationId=%3$s }";
            return String.format(asStr, pid.toString(), pid.getTableId().toString(), editorPresentationId.toString());
        }                
    }
    
    private static final class ObjectTitlesRequestParams{
        
        private final Id tableId;
        private final List<ObjectInfo> objectList = new LinkedList<>();
        
        public ObjectTitlesRequestParams(final ObjectInfo objectInfo){
            tableId = objectInfo.getPid().getTableId();
            objectList.add(objectInfo);
        }
        
        public boolean addObjectInfo(final ObjectInfo newObjectInfo){
            if (!tableId.equals(newObjectInfo.getPid().getTableId())){
                return false;
            }
            for (ObjectInfo objectInfo: objectList){
                if (objectInfo.getPid().equals(newObjectInfo.getPid())){
                    return objectInfo.getEditorPresentationId().equals(newObjectInfo.getEditorPresentationId());
                }
            }
            objectList.add(newObjectInfo);
            return true;
        }
        
        public Map<ObjectInfo,String> getTitles(final IClientEnvironment environment) throws InterruptedException, ServiceClientException{
            final EntityObjectTitlesProvider provider = new EntityObjectTitlesProvider(environment, tableId);
            for (ObjectInfo objectInfo: objectList){
                try{
                    environment.getDefManager().getEditorPresentationDef(objectInfo.getEditorPresentationId());
                }catch(DefinitionError error){
                    //Todo trace debug
                    continue;
                }
                provider.addEntityObjectPid(objectInfo.getPid(), objectInfo.getEditorPresentationId());
            }
            final EntityObjectTitles titles = provider.getTitles();            
            final Map<ObjectInfo, String> result = new HashMap<>();
            if (titles!=EntityObjectTitles.EMPTY){
                for (ObjectInfo objectInfo: objectList){
                    try{
                        result.put(objectInfo, titles.getEntityObjectTitle(objectInfo.getPid()));
                    }catch(ObjectNotFoundError error){
                        final String traceMessage = environment.getMessageProvider().translate("TraceMessage", "Choosen object %1$s not found");
                        environment.getTracer().debug(String.format(traceMessage, objectInfo.toString()));
                    }catch (ObjectNotAccessibleError error){
                        final String traceMessage = environment.getMessageProvider().translate("TraceMessage", "Choosen object %1$s is not accessible");
                        environment.getTracer().debug(String.format(traceMessage, objectInfo.toString()));
                    }
                }
            }
            return result;
        }
    }    
    
    private final List<ObjectTitlesRequestParams> objTitlesRqParams = new LinkedList<>();
    private final Map<ObjectInfo,String> titleForObject = new HashMap<>();            
    private final IClientEnvironment environment;
    
    public ChoosenObjectsProvider(final IClientEnvironment env){
        environment = env;
    }
    
    private void registerObject(final org.radixware.schemas.clientstate.ExplorerTreeNode.Object xmlObj){
        final ObjectInfo objectInfo = new ObjectInfo(xmlObj);
        for (ObjectTitlesRequestParams params: objTitlesRqParams){
            if (params.addObjectInfo(objectInfo)){
                return;
            }
        }
        final ObjectTitlesRequestParams params = new ObjectTitlesRequestParams(objectInfo);
        objTitlesRqParams.add(params);
    }
    
    public void readInsertedObjects(final ExplorerTreeState.InsertedObjects insertedObjects){
        objTitlesRqParams.clear();
        titleForObject.clear();
        if (insertedObjects!=null){
            final List<ExplorerTreePath> pathList = insertedObjects.getPathList();
            if (pathList!=null && !pathList.isEmpty()){
                List<org.radixware.schemas.clientstate.ExplorerTreeNode> nodes;
                org.radixware.schemas.clientstate.ExplorerTreeNode lastNode;
                for (ExplorerTreePath path: pathList){
                    nodes = path.getNodeList();
                    if (nodes!=null && !nodes.isEmpty()){
                        lastNode = nodes.get(nodes.size()-1);
                        if (lastNode!=null  && lastNode.getObject()!=null){
                            registerObject(lastNode.getObject());
                        }
                    }
                }
            }
        }
        if (!objTitlesRqParams.isEmpty()){
            requestTitles();
        }
    }
    
    private void requestTitles(){
        for (ObjectTitlesRequestParams params: objTitlesRqParams){                        
            try{
                titleForObject.putAll(params.getTitles(environment));                
            }catch(InterruptedException ex){
                return;
            }catch(ServiceClientException ex){
                final String traceMessage = environment.getMessageProvider().translate("TraceMessage", "Failed to get object titles");
                environment.getTracer().debug(traceMessage, ex, EEventSource.CLIENT);
            }          
        }
    }
    
    public EntityModel createChoosedEntityModel(final org.radixware.schemas.clientstate.ExplorerTreeNode.Object xmlObj){        
        final ObjectInfo objInfo = new ObjectInfo(xmlObj);
        
        if (!titleForObject.containsKey(objInfo)){
            return null;
        }
        
        final IContext.Entity context;
        try{
            context = IContext.Entity.Factory.loadFromStr(environment, xmlObj.getContext());
        }catch(XmlException | WrongFormatError ex){
            final String traceMessage = environment.getMessageProvider().translate("TraceMessage", "Failed to restore context for choosen object %1$s");
            environment.getTracer().debug(String.format(traceMessage, objInfo.toString()), ex, EEventSource.CLIENT);
            return null;
        }
        
        final RadEditorPresentationDef editorPresentation;
        try{
            editorPresentation = 
                environment.getDefManager().getEditorPresentationDef(objInfo.getEditorPresentationId());
        }catch(DefinitionError error){
            final String traceMessage = environment.getMessageProvider().translate("TraceMessage", "Editor presentation for choosen object %1$s was not found");
            environment.getTracer().debug(String.format(traceMessage, objInfo.toString()), error, EEventSource.CLIENT);
            return null;
        }        
        
        final EntityModel entityModel = editorPresentation.createModel(context);
        entityModel.activate(xmlObj.getPID(), titleForObject.get(objInfo), xmlObj.getClassId(), null);
        return entityModel;
    }
    
}
