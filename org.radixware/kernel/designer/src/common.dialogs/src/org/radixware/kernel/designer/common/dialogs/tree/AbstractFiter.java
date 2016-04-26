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

package org.radixware.kernel.designer.common.dialogs.tree;

import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public abstract class AbstractFiter implements INavigatorFilter {

    private final transient List<ChangeListener> listenerList = new ArrayList<ChangeListener>();

    @Override
    public void addChangeListener(final ChangeListener changeListener) {
        listenerList.add(changeListener);
    }

    protected void notifyListeners() {
        for (ChangeListener cl : listenerList) {
            cl.stateChanged(new ChangeEvent(this));
        }
    }

    @Override
    public boolean expandTree() {
        return true;
    }
}
