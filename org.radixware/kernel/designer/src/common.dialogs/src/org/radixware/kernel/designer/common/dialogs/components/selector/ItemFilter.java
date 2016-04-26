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

package org.radixware.kernel.designer.common.dialogs.components.selector;

import java.awt.Component;
import javax.swing.event.ChangeListener;
import org.openide.util.ChangeSupport;


public class ItemFilter<TItem> implements IItemFilter<TItem> {
    private final ChangeSupport changeSupport = new ChangeSupport(this);

    @Override
    public void addChangeListener(ChangeListener listener) {
        changeSupport.addChangeListener(listener);
    }

    @Override
    public void removeChangeListener(ChangeListener listener) {
        changeSupport.removeChangeListener(listener);
    }

    protected final void fireChange() {
        changeSupport.fireChange();
    }

    @Override
    public Component getComponent() {
        return null;
    }

    @Override
    public boolean accept(TItem value) {
        return true;
    }

    @Override
    public void reset() {
    }
}
