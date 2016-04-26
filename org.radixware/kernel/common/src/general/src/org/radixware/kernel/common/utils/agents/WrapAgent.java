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

package org.radixware.kernel.common.utils.agents;

import javax.swing.event.ChangeListener;


public abstract class WrapAgent<T, Y> implements IObjectAgent<T> {

    private final IObjectAgent<? extends Y> sourceAgent;

    public WrapAgent(IObjectAgent<? extends Y> sourceAgent) {
        this.sourceAgent = sourceAgent;
    }

    @Override
    public final boolean invite(boolean force) {
        return sourceAgent.invite(force);
    }

    @Override
    public final boolean isActual() {
        return sourceAgent.isActual();
    }

    @Override
    public final void addActualizeListener(ChangeListener listener) {
        sourceAgent.addActualizeListener(listener);
    }

    @Override
    public final void removeActualizeListener(ChangeListener listener) {
        sourceAgent.removeActualizeListener(listener);
    }

    public final Y getObjectSource() {
        return sourceAgent.getObject();
    }

    public final IObjectAgent<? extends Y> getSourceAgent() {
        return sourceAgent;
    }
}
