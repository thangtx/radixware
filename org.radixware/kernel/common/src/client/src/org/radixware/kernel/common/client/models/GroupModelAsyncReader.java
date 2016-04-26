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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.enums.ERestriction;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;

public final class GroupModelAsyncReader {

    public static abstract class Listener {

        public void onServiceClientException(final ServiceClientException exception) {
        }

        public void readingWasInterrupted() {
        }

        public void readingWasFinished() {
        }

        public void beforeChangeGroupModel() {
        }

        public void afterChangeGroupModel() {
        }
    }

    private class DataListener extends GroupModelDataSource.Listener {

        @Override
        public void onException(InvocationTargetException exception) {
            GroupModelAsyncReader.this.onException(exception.getCause());
        }

        @Override
        public void onDataReceived(final GroupModelData data) {
            GroupModelAsyncReader.this.onDataReceived(data);
        }
    };

    private final GroupModel groupModel;
    private final int timeoutSec;
    private final List<EntityModel> entities = new LinkedList<>();
    //private SelectRequestHandle currentRequestHandle;
    private GroupModelDataSource.Handler dataHandler;
    private AbstractReaderController readerController;
    private GroupModelDataSource dataSource;
    private boolean hasMore = true;
    private final List<Listener> listeners = new LinkedList<>();
    private final List<Id> disabledCommands = new ArrayList<>();
    private final EnumSet<ERestriction> restrictions = EnumSet.noneOf(ERestriction.class);
    private boolean readingWasFinished = false;

    public GroupModelAsyncReader(final GroupModel groupModel, final int timeoutSec) {
        this.groupModel = groupModel;
        this.timeoutSec = timeoutSec;
        readerController = new DefaultReaderController();
    }

    public GroupModelAsyncReader(final GroupModel groupModel) {
        this(groupModel, 0);
    }

    public void start() {
        clean();
        readingWasFinished = false;
        dataSource = new DefaultGroupModelDataSource(groupModel, timeoutSec);
        readMoreAsync();
    }

    public void start(final AbstractReaderController readerController) {
        clean();
        readingWasFinished = false;
        this.readerController = readerController;
        dataSource = new DefaultGroupModelDataSource(groupModel, timeoutSec);
        readMoreAsync();
    }

    public void start(final AbstractReaderController readerController, final GroupModelDataSource dataSource) {
        clean();
        readingWasFinished = false;
        this.readerController = readerController;
        this.dataSource = dataSource;
        readMoreAsync();
    }

    public boolean inProgress() {
        return dataHandler != null;
    }

    public int getEntitiesCount() {
        return entities.size();
    }

    public boolean canReadMore() {
        //return readerController.has
        return hasMore;
    }

    public void reset() {
        final boolean inProgress = inProgress();
        clean();
        if (inProgress) {
            readMoreAsync();
        }
    }

    public void stop() {
        if (inProgress()) {
            dataHandler.cancel();
            dataHandler = null;
            notifyReadingWasCancelled();
        }
    }

    public void clean() {
        stop();
        entities.clear();
        disabledCommands.clear();
        restrictions.clear();
        hasMore = true;
    }

    public void addListener(final Listener listener) {
        listeners.add(listener);
    }

    public void removeListener(final Listener listener) {
        listeners.remove(listener);
    }

    public void writeEntitiesToGroupModel() {
        beforeChangeGroupModel();
        groupModel.mergeEntities(entities, disabledCommands, restrictions);
        afterChangeGroupModel();
    }

    public boolean writeEntitiesToGroupModelIfReady() {
        if (readingWasFinished) {
            writeEntitiesToGroupModel();
            return true;
        } else {
            return false;
        }
    }

    private void readMoreAsync() {
        dataHandler = dataSource.waitForData(getEntitiesCount() + 1, groupModel.getReadPageSize(), false, new DataListener());
    }

    private void onDataReceived(final GroupModelData data) {
        updateEntities(data);
        if (hasMore) {
            readMoreAsync();
        } else {
            dataHandler = null;
            disabledCommands.addAll(data.getDisabledCommands());
            restrictions.addAll(data.getRestrictions());
            readingWasFinished = true;
            notifyReadingWasFinished();
        }
    }

    private void onException(final Throwable exception) {
        readMoreAsync();
        if (exception instanceof ServiceClientException) {
            notifyException((ServiceClientException) exception);
        }
    }

    private void updateEntities(final GroupModelData result) {
        final List<EntityModel> newEntities;

        if (readerController == null) {
            hasMore = result.hasMore();
            newEntities = result.getEntityModels();
        } else {
            hasMore = readerController.updateEntities(result);
            newEntities = readerController.getLastEntities();
        }

        for (int i = 0; i < newEntities.size(); i++) {
            final EntityModel curEntity = newEntities.get(i);

            final int idx = GroupModel.listContainsModel(entities, curEntity);
            if (idx >= 0) {
                entities.set(idx, curEntity);
            } else {
                entities.add(curEntity);
            }
        }
        if (readerController != null) {
            readerController.clear();
        }
    }

    private void notifyException(final ServiceClientException exception) {
        for (Listener l : listeners) {
            l.onServiceClientException(exception);
        }
    }

    private void notifyReadingWasCancelled() {
        for (Listener l : listeners) {
            l.readingWasInterrupted();
        }
    }

    private void notifyReadingWasFinished() {
        for (Listener l : listeners) {
            l.readingWasFinished();
        }
    }

    private void beforeChangeGroupModel() {
        for (Listener l : listeners) {
            l.beforeChangeGroupModel();
        }
    }

    private void afterChangeGroupModel() {
        for (Listener l : listeners) {
            l.afterChangeGroupModel();
        }
    }

    AbstractReaderController getReaderController() {
        return this.readerController;
    }

    public IReaderController.ScrollPosition getScrollPosition() {
        return this.readerController.scrollPosition();
    }
}
