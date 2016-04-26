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

package org.radixware.kernel.designer.ads.editors.clazz.enumeration;

import org.openide.util.ChangeSupport;


interface IReadyStatable {
    boolean getReadyState();
    ChangeSupport getReadyStateChangeSupport();
}

class ReadyStateAdapter implements IReadyStatable {
    private ChangeSupport readyStateChangeSupport;
    protected boolean readyState;
    
    public ReadyStateAdapter(boolean readyState) {
        this.readyState = readyState;
        readyStateChangeSupport = new ChangeSupport(this);
    }
    
    public ReadyStateAdapter(IReadyStatable source, boolean readyState) {
        this.readyState = readyState;
        readyStateChangeSupport = new ChangeSupport(source);
    }

    public void setReadyState(boolean ready) {
        if (ready != getReadyState()) {
            readyState = ready;
            readyStateChangeSupport.fireChange();
        }
    }
    
    @Override
    public boolean getReadyState() {
        return readyState;
    }

    @Override
    public ChangeSupport getReadyStateChangeSupport() {
        return readyStateChangeSupport;
    }
}