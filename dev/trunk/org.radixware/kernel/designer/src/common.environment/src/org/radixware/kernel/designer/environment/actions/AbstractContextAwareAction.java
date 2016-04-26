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

package org.radixware.kernel.designer.environment.actions;

import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;
import org.openide.util.ContextAwareAction;


public abstract class AbstractContextAwareAction implements ContextAwareAction {

    private boolean enabled = false;
    private Map<String, Object> values = new HashMap<String, Object>();

    @Override
    public Object getValue(String key) {
        return values.get(key);
    }

    @Override
    public void putValue(String key, Object value) {
        values.put(key, value);
    }

    @Override
    public void setEnabled(boolean b) {
        enabled = b;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
    }
}
