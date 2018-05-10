/*
 * Copyright (coffee) 2008-2016, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.explorer.inspector.propertyEditors;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

class ValueListenerController {

    List<IPropertyEditor.ValueListener> valueListenerList = new CopyOnWriteArrayList<>(); //method notifyListener can change valueListenerList

    public void add(IPropertyEditor.ValueListener listener) {
        if (listener != null && !valueListenerList.contains(listener)) {
            valueListenerList.add(listener);
        }
    }

    public void remove(IPropertyEditor.ValueListener listener) {
        if (listener != null && valueListenerList.contains(listener)) {
            valueListenerList.remove(listener);
        }
    }

    public void notifyListener(Object newValue) {
        for (IPropertyEditor.ValueListener valueListener : valueListenerList) {
            valueListener.onChangeValue(newValue);
        }
    }
}
