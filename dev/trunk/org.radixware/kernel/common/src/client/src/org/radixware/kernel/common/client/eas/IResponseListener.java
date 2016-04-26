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

package org.radixware.kernel.common.client.eas;

import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.exceptions.ServiceClientException;


public interface IResponseListener {

    public void registerRequestHandle(final RequestHandle handle);

    public void unregisterRequestHandle(final RequestHandle handle);

    public void onResponseReceived(final XmlObject response, final RequestHandle handle);

    public void onServiceClientException(final ServiceClientException exception, final RequestHandle handle);

    public void onRequestCancelled(final XmlObject request, final RequestHandle handler);
}
