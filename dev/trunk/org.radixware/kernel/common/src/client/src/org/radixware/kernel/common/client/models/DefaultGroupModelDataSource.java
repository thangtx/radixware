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
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.eas.RequestHandle;
import org.radixware.kernel.common.client.eas.SelectRequestHandle;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.schemas.eas.SelectRs;


final class DefaultGroupModelDataSource extends GroupModelDataSource {

    private final class SelectResponseListener extends ResponseListener {

        private final GroupModelDataSource.Listener listener;

        public SelectResponseListener(final GroupModelDataSource.Listener listener) {
            super(getGroupModel().getEnvironment());
            this.listener = listener;
        }

        @Override
        public void onResponseReceived(final XmlObject response, final RequestHandle handle) {
            if (response instanceof SelectRs) {
                final GroupModelData data = parseResponse((SelectRs) response);
                listener.onDataReceived(data);
            }
        }

        @Override
        public void onServiceClientException(final ServiceClientException exception, final RequestHandle handle) {
            super.onServiceClientException(exception, handle);
            listener.onException(new InvocationTargetException(exception));
        }

        public void cancelRequest() {
            closeActiveRequestHandles(true);
        }
    }

    private static final class SelectResponseHandler implements GroupModelDataSource.Handler {

        private final SelectResponseListener listener;

        public SelectResponseHandler(final SelectResponseListener listener) {
            this.listener = listener;
        }

        @Override
        public void cancel() {
            listener.cancelRequest();
        }
    }
    
    private final int timeoutSec;

    public DefaultGroupModelDataSource(final GroupModel groupModel, final int timeOutSec) {
        super(groupModel);
        this.timeoutSec = timeOutSec;
    }

    @Override
    public Handler waitForData(final int startIndex, final int rowCount, final boolean withSelectorAddons, final Listener listener) {
        final SelectRequestHandle selectRequestHandle = RequestHandle.Factory.createForSelect(getGroupModel(), startIndex, rowCount, false);
        final SelectResponseListener responseListener = new SelectResponseListener(listener);
        selectRequestHandle.addListener(responseListener);
        getGroupModel().getEnvironment().getEasSession().sendAsync(selectRequestHandle, timeoutSec);
        return new SelectResponseHandler(responseListener);
    }
    
}
