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

package org.radixware.kernel.common.client.models;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException;
import org.radixware.kernel.common.exceptions.ServiceClientException;


public class GroupModelReader implements Iterable<EntityModel> {

    public static enum EReadingFlags {
        SHOW_SERVICE_CLIENT_EXCEPTION,
        TRACE_SERVICE_CLIENT_EXCEPTION,
        STOP_ON_BROKEN_ENTITY,
        DONT_LOAD_MORE,
        RESPECT_SELECTION
    }
    
    private final static int DEFAULT_READ_PAGE_SIZE_FOR_LOAD_ALL_ENTITIES = 200;
    private final GroupModel group;
    private boolean wasInterrupted, wasBrokenEntities;
    private ServiceClientException serviceClientException;
    private final EnumSet<EReadingFlags> readingFlags;        

    public final class EntityModelIteratorImpl implements Iterator<EntityModel> {

        private int index = -1;
        private EntityModel nextEntityModel;
        private final EntityObjectsSelection selection;
        
        public EntityModelIteratorImpl(final EntityObjectsSelection selection){
            this.selection = selection;
        }

        @Override
        public boolean hasNext() {
            try {
                for (;;) {
                    if (readingFlags.contains(EReadingFlags.DONT_LOAD_MORE) && index >= group.getEntitiesCount() - 1) {
                        nextEntityModel = null;
                        return false;
                    }
                    try {
                        final EntityModel candidate = group.getEntity(index + 1);
                        if (candidate==null){
                            nextEntityModel = null;
                            return false;
                        }
                        if (selection==null || selection.isObjectSelected(candidate)){
                            nextEntityModel = candidate;                            
                            return true;
                        }else{
                            index++;
                            continue;
                        }         
                    } catch (BrokenEntityObjectException ex) {
                        wasBrokenEntities = true;
                        index++;
                        if (readingFlags.contains(EReadingFlags.STOP_ON_BROKEN_ENTITY)) {
                            return false;
                        }
                        //else ignoring;
                    }
                }
            } catch (ServiceClientException exception) {
                processException(exception);
                nextEntityModel = null;
                return false;
            } catch (InterruptedException exception) {
                wasInterrupted = true;
                nextEntityModel = null;
                return false;
            }
        }

        @Override
        public EntityModel next() {
            if (nextEntityModel == null) {
                try {
                    for (;;) {
                        if (readingFlags.contains(EReadingFlags.DONT_LOAD_MORE) && index >= group.getEntitiesCount() - 1) {
                            throw new NoSuchElementException();
                        }
                        try {
                            final EntityModel entityModel = group.getEntity(++index);
                            if (entityModel == null) {
                                throw new NoSuchElementException();
                            } else if (selection==null || selection.isObjectSelected(entityModel)){
                                return entityModel;
                            } else{
                                continue;
                            }
                        } catch (BrokenEntityObjectException ex) {
                            wasBrokenEntities = true;
                            if (readingFlags.contains(EReadingFlags.STOP_ON_BROKEN_ENTITY)) {
                                throw new NoSuchElementException();
                            }
                            //ignoring;
                        }
                    }
                } catch (ServiceClientException exception) {
                    processException(exception);
                    throw new NoSuchElementException();
                } catch (InterruptedException exception) {
                    wasInterrupted = true;
                    throw new NoSuchElementException();
                }
            } else {
                final EntityModel result = nextEntityModel;
                nextEntityModel = null;
                index++;
                return result;
            }
        }

        private void processException(ServiceClientException exception) {
            serviceClientException = exception;
            if (readingFlags.contains(EReadingFlags.SHOW_SERVICE_CLIENT_EXCEPTION)) {
                group.showException(exception);
            }
            if (readingFlags.contains(EReadingFlags.TRACE_SERVICE_CLIENT_EXCEPTION)) {
                group.getEnvironment().getTracer().error(exception);
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
    
    public GroupModelReader(final GroupModel groupModel, final EnumSet<EReadingFlags> flags) {
        group = groupModel;
        readingFlags = flags == null ? EnumSet.noneOf(EReadingFlags.class) : flags.clone();
    }        

    public GroupModelReader(final GroupModel groupModel) {
        this(groupModel,null);
    }

    @Override
    public Iterator<EntityModel> iterator() {
        wasInterrupted = false;
        wasBrokenEntities = false;
        serviceClientException = null;
        final EntityObjectsSelection selection = group.getSelection();
        if (readingFlags.contains(EReadingFlags.RESPECT_SELECTION)){            
            return new EntityModelIteratorImpl(selection.isEmpty() ? null : selection);
        }else{
            return new EntityModelIteratorImpl(null);
        }
    }

    public int getTotalEntitiesCount() throws ServiceClientException, InterruptedException {
        loadEntireGroup();
        return group.getEntitiesCount();
    }

    public EntityModel getFirstEntityModel() throws ServiceClientException, InterruptedException {
        for (int i = 0;; i++) {
            try {
                if (group.getEntity(i) == null) {
                    return null;
                } else {
                    return group.getEntity(i);
                }
            } catch (BrokenEntityObjectException ex) {
                //just ignoring
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
        }
    }

    public void loadEntireGroup() throws ServiceClientException, InterruptedException {
        loadEntireGroup(DEFAULT_READ_PAGE_SIZE_FOR_LOAD_ALL_ENTITIES);
    }

    public void loadEntireGroup(final int readPageSize) throws ServiceClientException, InterruptedException {
        final int keepReadPageSize = group.getReadPageSize();
        group.setReadPageSize(readPageSize);
        try {
            while (group.hasMoreRows()) {
                try {
                    group.getEntity(group.getEntitiesCount());
                } catch (BrokenEntityObjectException ex) {
                    Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                }
            }
        } finally {
            group.setReadPageSize(keepReadPageSize);
        }
    }

    public boolean wasInterrupted() {
        return wasInterrupted;
    }

    public boolean wasBrokenEntities() {
        return wasBrokenEntities;
    }

    public boolean wasException() {
        return serviceClientException != null || wasInterrupted;
    }

    public ServiceClientException getServiceClientException() {
        return serviceClientException;
    }
}
