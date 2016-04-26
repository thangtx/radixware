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

package org.radixware.kernel.common.builder.api;

import org.radixware.kernel.common.defs.ads.build.Cancellable;


public interface IProgressHandleFactory {

    /**
     * Create a progress ui handle for a long lasting task.
     * @param displayName to be shown in the progress UI
     * @return an instance of {@link org.radixware.kernel.common.builder.spi.IProgressHanle}, initialized but not started.
     */
    public IProgressHandle createHandle(String displayName);

    /**
     * Create a progress ui handle for a long lasting task.
     * @param allowToCancel either null, if the task cannot be cancelled or
     *          an instance of {@link org.openide.util.Cancellable} that will be called when user
     *          triggers cancel of the task.
     * @param displayName to be shown in the progress UI
     * @return an instance of {@link org.radixware.kernel.common.builder.spi.IProgressHanle}, initialized but not started.
     */
    public IProgressHandle createHandle(String displayName, Cancellable allowToCancel);
}
