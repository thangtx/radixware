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

package org.radixware.kernel.designer.eas.client;

import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.env.progress.ProgressHandleManager;
import org.radixware.kernel.common.client.views.IProgressHandle;
import org.radixware.kernel.common.client.views.IProgressHandle.Cancellable;


public class DesignerProgressHandleManager implements ProgressHandleManager {

    private List<IProgressHandle> handles = new LinkedList<>();

    @Override
    public void blockProgress() {
    }

    @Override
    public void unblockProgress() {
    }

    @Override
    public IProgressHandle getActive() {
        if (handles.isEmpty()) {
            return null;
        } else {
            return handles.get(handles.size() - 1);
        }
    }

    @Override
    public IProgressHandle newProgressHandle() {
        ProgressHandleImpl impl = new ProgressHandleImpl();
        handles.add(impl);
        return impl;
    }

    @Override
    public IProgressHandle newProgressHandle(Cancellable cnclbl) {
        ProgressHandleImpl impl = new ProgressHandleImpl(cnclbl);
        handles.add(impl);
        return impl;
    }

    @Override
    public IProgressHandle newStandardProgressHandle() {
        return newProgressHandle();
    }
}
