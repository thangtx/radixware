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

import com.sun.jdi.request.StepRequest;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.Set;
import org.netbeans.api.debugger.ActionsManager;
import org.netbeans.spi.debugger.ContextProvider;
import org.radixware.kernel.designer.debugger.impl.ThreadsCollector;


public class StepActionProvider extends AsyncActionProvider {

    private ThreadsCollector threadsCollector;
    private final PropertyChangeListener propChangeListener = new PropertyChangeListener() {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            fireChange(ActionsManager.ACTION_STEP_OVER);
            fireChange(ActionsManager.ACTION_STEP_OUT);
            fireChange(ActionsManager.ACTION_STEP_INTO);
        }
    };

    public StepActionProvider(ContextProvider contextProvider) {
        super(contextProvider);
        this.threadsCollector = this.debugger.getThreadsCache().getThreadsCollector();
        this.threadsCollector.addPropertyChangeListener(propChangeListener);
    }

    @Override
    protected boolean checkIsEnabled(Object action) {
        return threadsCollector.isSomeThreadSuspended();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set getActions() {
        HashSet set = new HashSet();
        set.add(ActionsManager.ACTION_STEP_OVER);
        set.add(ActionsManager.ACTION_STEP_OUT);
        set.add(ActionsManager.ACTION_STEP_INTO);
        return set;
    }

    @Override
    public void doAction(Object action) {
        int step;
        if (action == ActionsManager.ACTION_STEP_OUT) {
            step = StepRequest.STEP_OUT;
        } else if (action == ActionsManager.ACTION_STEP_OVER) {
            step = StepRequest.STEP_OVER;
        } else if (action == ActionsManager.ACTION_STEP_INTO) {
            step = StepRequest.STEP_INTO;
        } else {
            return;
        }

        debugger.step(step);
    }
}
