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

package org.radixware.kernel.common.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.client.meta.RadClassPresentationDef;
import org.radixware.kernel.common.client.meta.RadEditorPresentationDef;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.GroupModelReader;
import org.radixware.kernel.common.client.models.IContext;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.items.properties.PropertyObject;
import org.radixware.kernel.common.client.tree.nodes.IExplorerTreeNode;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.views.IProgressHandle;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;

public final class Clipboard implements Iterable<EntityModel> {

    public interface ChangeListener {

        public void stateChanged();
    }
    
    private final static class ListenerWrapper{
        
        private final ChangeListener listener;
        private final String callStack;
        
        public ListenerWrapper(final ChangeListener listener, final String callStack){
            this.listener = listener;
            this.callStack = callStack;
        }        
        
        public void notifyListener(){
            listener.stateChanged();
        }
        
        public String getCallStack(){
            return callStack;                    
        }
        
        public ChangeListener getListener(){
            return listener;
        }
    }
    
    private final static class Listeners implements Iterable<ListenerWrapper>{
        
        private final LinkedList<ListenerWrapper> listeners = new LinkedList<>();
        
        public Listeners(final ListenerWrapper listener){
            listeners.add(listener);
        }
        
        public void remove(final ChangeListener listener){
            for (int i=listeners.size()-1; i>=0; i--){
                if (listeners.get(i).getListener()==listener){
                    listeners.remove(i);
                }
            }
        }
        
        public boolean isEmpty(){
            return listeners.isEmpty();
        }
        
        public void add(final ListenerWrapper listener){
            for (ListenerWrapper wrapper: listeners){
                if (wrapper.getListener()==listener.getListener()){
                    return;
                }
            }
            listeners.add(listener);
        }
        
        public void notifyListeners(){
            for (ListenerWrapper wrapper: listeners){
                wrapper.notifyListener();
            }
        }

        @Override
        public Iterator<ListenerWrapper> iterator() {
            return listeners.iterator();
        }                
    }
            
    private final IClientEnvironment environment;
    private final List<EntityModel> content = new ArrayList<>();
    private RadClassPresentationDef classDef = null;
    private IExplorerTreeNode listenersContextNode;
    
    private final Map<IExplorerTreeNode, Listeners> listenersByTreeNode = new HashMap<>();
    private final Map<Model, Listeners> listenersByModel = new HashMap<>();

    public void addChangeListener(final ChangeListener listener) {
        addChangeListener(listener, null);
    }

    public void addChangeListener(final ChangeListener listener, final Model contextModel) {
        if (listenersContextNode==null){
            throw new IllegalStateException("Listeners context tree node was not set");
        }
        
        if (listener != null) {
            final ListenerWrapper wrapper = createWrapper(listener);
            Listeners listeners = listenersByTreeNode.get(listenersContextNode);
            if (listeners==null){
                listeners = new Listeners(wrapper);
                listenersByTreeNode.put(listenersContextNode, listeners);
            }else{
                listeners.add(wrapper);
            }
            if (contextModel!=null){
                listeners = listenersByModel.get(contextModel);
                if (listeners==null){
                    listeners = new Listeners(wrapper);
                    listenersByModel.put(contextModel, listeners);
                }else{
                    listeners.add(wrapper);
                }
            }
        }
    }

    public void removeChangeListener(final ChangeListener listener) {        
        removeFromListenersByModel(listener);
        removeFromListenersByTreeNode(listener);
    }
    
    private void removeFromListenersByModel(final ChangeListener listener){
        final List<Model> emptyListeners = new LinkedList<>();
        for (Map.Entry<Model,Listeners> entry: listenersByModel.entrySet()){
            entry.getValue().remove(listener);
            if (entry.getValue().isEmpty()){
                emptyListeners.add(entry.getKey());
            }
        }
        for (Model model: emptyListeners){
            listenersByModel.remove(model);
        }        
    }
    
    private void removeFromListenersByTreeNode(final ChangeListener listener){
        final List<IExplorerTreeNode> emptyListeners = new LinkedList<>();
        for (Map.Entry<IExplorerTreeNode,Listeners> entry: listenersByTreeNode.entrySet()){
            entry.getValue().remove(listener);
            if (entry.getValue().isEmpty()){
                emptyListeners.add(entry.getKey());
            }
        }
        for (IExplorerTreeNode treeNode: emptyListeners){
            listenersByModel.remove(treeNode);
        }        
    }

    public void removeAllChangeListeners(final Model contextModel) {
        final Listeners listenersForModel = listenersByModel.get(contextModel);
        if (listenersForModel != null) {
            for (ListenerWrapper wrapper: listenersForModel){
                removeFromListenersByTreeNode(wrapper.getListener());
            }
        }
        listenersByModel.remove(contextModel);
    }
    
    public void removeAllChangeListeners(final IExplorerTreeNode node){
        final Listeners listenersForNode = listenersByTreeNode.get(node);
        if (listenersForNode!=null){
            for (ListenerWrapper wrapper: listenersForNode){
                traceWarning(wrapper, node);
                removeFromListenersByModel(wrapper.getListener());
            }
            listenersByTreeNode.remove(listenersForNode);
        }
    }
    
    private void traceWarning(final ListenerWrapper wrapper, final IExplorerTreeNode node){
        final String callStack = wrapper.getCallStack();
        if (callStack!=null && !callStack.isEmpty()){
            final String message = 
                environment.getMessageProvider().translate("TraceMessage", "Clipboard event handler was not removed in '%1$s':\n%2$s");
            environment.getTracer().warning(String.format(message, node.getPath(), callStack));
        }
    }
    
    public void setListenersContext(final IExplorerTreeNode node){
        listenersContextNode = node;
    }

    public Clipboard(final IClientEnvironment environment) {
        this.environment = environment;
    }

    private void stateChanged() {
        for (Listeners listeners: listenersByTreeNode.values()){
            listeners.notifyListeners();
        }
    }

    public Id getTableId() {
        return classDef == null ? null : classDef.getTableId();
    }

    public boolean isCompatibleWith(final GroupModel group) {
        for (EntityModel entityModel : content) {
            if (entityModel.isExists() && group.canPaste(entityModel)) {
                return true;
            }
        }
        return false;
    }

    public boolean isCompatibleWith(final PropertyObject property) {
        if (classDef != null && content.size() == 1) {
            return property.canPaste(content.get(0));
        }
        return false;
        
    }

    public RadClassPresentationDef getClassPresentationDef() {
        return classDef;
    }

    public int size() {
        return content.size();
    }

    public void push(final EntityModel entity) {
        if (entity != null 
            && (entity.isExists() || isPropertyValueInNewEntityObject(entity))            
            ) {
            content.clear();
            classDef = entity.getClassPresentationDef();
            content.add(isPropertyValueInNewEntityObject(entity) ? createModelCopy(entity) : entity);
            stateChanged();
        }
    }
    
    private static EntityModel createModelCopy(final EntityModel source){
        final RadEditorPresentationDef editorPresentation = source.getEditorPresentationDef();
        final Id classId = source.getClassId();
        final String msg = "creating model for new entity with class #%s was created by editor presentation %s.";
        source.getEnvironment().getTracer().put(EEventSeverity.DEBUG, String.format(msg, classId, editorPresentation.toString()), EEventSource.EXPLORER);
        
        final EntityModel entity = editorPresentation.createModel(source.getContext());        
        entity.activateCopy(source);
        return entity;
    }
    
    private static boolean isPropertyValueInNewEntityObject(final EntityModel entity){
        return entity.isNew() 
                  && entity.getContext() instanceof IContext.ObjectPropCreating
                  && ((IContext.ObjectPropCreating)entity.getContext()).propOwner.isNew();
    }

    public void push(final GroupModel group) {
        final IProgressHandle handle = environment.getProgressHandleManager().newProgressHandle();
        handle.startProgress(environment.getMessageProvider().translate("Wait Dialog", "Coping objects..."), true);
        try {
            content.clear();
            classDef = group.getSelectorPresentationDef().getClassPresentation();
                        
            final GroupModelReader reader = 
                new GroupModelReader(group, EnumSet.of(GroupModelReader.EReadingFlags.RESPECT_SELECTION));
            for (EntityModel entityObject: reader){
                content.add(entityObject);
                if (handle.wasCanceled()) {
                    content.clear();
                    classDef = null;
                    return;
                }                
            }
            if (reader.wasInterrupted()){
                content.clear();
                classDef = null;                
            }
            if (reader.wasException()){
                group.showException(environment.getMessageProvider().translate("ExplorerError", "Can't copy objects"), reader.getServiceClientException());
                content.clear();
                classDef = null;
            }
        } finally {
            handle.finishProgress();
            stateChanged();
        }
    }

    public void remove(final Pid pid) {
        final int sizeBefore = content.size();
        Pid objectPid;
        for (int i = sizeBefore - 1; i >= 0; i--) {
            objectPid = content.get(i).getPid();
            if (objectPid!=null && objectPid.equals(pid)) {
                content.remove(i);
            }
        }
        if (content.size() != sizeBefore) {
            stateChanged();
        }
    }

    public void remove(final Collection<EntityModel> entities) {
        final int sizeBefore = content.size();
        Pid objectPid;        
        for (int i = sizeBefore - 1; i >= 0; i--) {
            objectPid = content.get(i).getPid();
            for (EntityModel entity : entities) {
                if (objectPid!=null && objectPid.equals(entity.getPid())) {
                    content.remove(i);
                    break;
                }
            }
        }
        if (content.size() != sizeBefore) {
            stateChanged();
        }
    }

    public void clear() {
        final boolean changingState = content.size() > 0;
        reset();
        if (changingState) {
            stateChanged();
        }
    }

    @Override
    public Iterator<EntityModel> iterator() {
        return content.iterator();
    }

    void reset() {
        content.clear();
        classDef = null;
    }
    
    private ListenerWrapper createWrapper(final ChangeListener listener){
        if (RunParams.isDevelopmentMode()){
            final StackTraceElement[] stack = Thread.currentThread().getStackTrace();
            return new ListenerWrapper(listener, Utils.stackToString(stack));
        }else{
            return new ListenerWrapper(listener, null);
        }
    }
}
