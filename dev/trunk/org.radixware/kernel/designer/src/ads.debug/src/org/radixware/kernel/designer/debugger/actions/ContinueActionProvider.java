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

package org.radixware.kernel.designer.debugger.actions;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.Set;
import org.netbeans.api.debugger.ActionsManager;
import org.netbeans.spi.debugger.ContextProvider;
import org.radixware.kernel.designer.debugger.impl.ThreadsCollector;


public class ContinueActionProvider extends AsyncActionProvider {

    private ThreadsCollector threadsCollector;    
    private final PropertyChangeListener propChangeListener = new PropertyChangeListener() {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            fireChange(ActionsManager.ACTION_CONTINUE);
        }
    };

    public ContinueActionProvider(ContextProvider lookupProvider) {
        super(lookupProvider);
        this.threadsCollector = this.debugger.getThreadsCache().getThreadsCollector();
        this.threadsCollector.addPropertyChangeListener(propChangeListener);
    }

    @Override
    public Set getActions() {
        return Collections.singleton(ActionsManager.ACTION_CONTINUE);
    }

    @Override
    public void doAction(Object action) {
        debugger.resume();
    }

    @Override
    protected boolean checkIsEnabled(Object action) {
        return threadsCollector.isSomeThreadSuspended();
    }
}
