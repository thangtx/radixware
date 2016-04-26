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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException;
import org.radixware.kernel.common.client.meta.RadClassPresentationDef;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.GroupModelReader;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.items.properties.PropertyObject;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.views.IProgressHandle;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;

public final class Clipboard implements Iterable<EntityModel> {

    public interface ChangeListener {

        public void stateChanged();
    }
    private final IClientEnvironment environment;
    private final List<EntityModel> content = new ArrayList<>();
    private RadClassPresentationDef classDef = null;
    private final LinkedList<ChangeListener> listeners = new LinkedList<>();
    private final Map<Model, LinkedList<ChangeListener>> listenersByModel = new HashMap<>();

    public void addChangeListener(final ChangeListener listener) {
        addChangeListener(listener, null);
    }

    public void addChangeListener(final ChangeListener listener, final Model contextModel) {
        synchronized (listeners) {
            if (listener != null && !listeners.contains(listener)) {
                listeners.add(listener);
                if (contextModel != null) {
                    LinkedList<ChangeListener> listenersForModel = listenersByModel.get(contextModel);
                    if (listenersForModel == null) {
                        listenersForModel = new LinkedList<>();
                        listenersByModel.put(contextModel, listenersForModel);
                    }
                    listenersForModel.add(listener);
                }
            }
        }
    }

    public void removeChangeListener(final ChangeListener listener) {
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }

    public void removeAllChangeListeners(final Model contextModel) {
        synchronized (listeners) {
            final LinkedList<ChangeListener> listenersForModel = listenersByModel.get(contextModel);
            if (listenersForModel != null) {
                listeners.removeAll(listenersForModel);
            }
            listenersByModel.remove(contextModel);
        }
    }

    public Clipboard(final IClientEnvironment environment) {
        this.environment = environment;
    }

    private void stateChanged() {
        synchronized (listeners) {
            for (ChangeListener l : listeners) {
                l.stateChanged();
            }
        }
    }

    public Id getTableId() {
        return classDef == null ? null : classDef.getTableId();
    }

    public boolean isCompatibleWith(final GroupModel group) {
        for (EntityModel entityModel : content) {
            if (group.canPaste(entityModel)) {
                return true;
            }
        }
        return false;
    }

    public boolean isCompatibleWith(final PropertyObject property) {
        if (classDef != null && content.size() == 1) {
            final Id actualClassId = property.getDefinition().getReferencedClassId();
            final RadClassPresentationDef actualClassDef =
                    property.getEnvironment().getDefManager().getClassPresentationDef(actualClassId);
            return classDef.getId().equals(actualClassDef.getId()) || actualClassDef.isAncestorOf(classDef);
        } else {
            return false;
        }
    }

    public RadClassPresentationDef getClassPresentationDef() {
        return classDef;
    }

    public int size() {
        return content.size();
    }

    public void push(final EntityModel entity) {
        if (entity != null && entity.isExists()) {
            content.clear();
            classDef = entity.getClassPresentationDef();
            content.add(entity);
            stateChanged();
        }
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
        for (int i = sizeBefore - 1; i >= 0; i--) {
            if (content.get(i).getPid().equals(pid)) {
                content.remove(i);
            }
        }
        if (content.size() != sizeBefore) {
            stateChanged();
        }
    }

    public void remove(final Collection<EntityModel> entities) {
        final int sizeBefore = content.size();
        for (int i = sizeBefore - 1; i >= 0; i--) {
            for (EntityModel entity : entities) {
                if (content.get(i).getPid().equals(entity.getPid())) {
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
}
