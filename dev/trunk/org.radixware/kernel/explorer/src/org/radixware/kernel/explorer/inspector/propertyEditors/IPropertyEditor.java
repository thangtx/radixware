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

import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.explorer.inspector.WidgetProperty;

/**
 *
 * @author szotov
 */
public interface IPropertyEditor {

    public interface ValueListener {
        void onChangeValue(Object newValue);
    }

    void addValueListener(ValueListener listener);
    
    void registerChildPropEditor(final WidgetProperty parentWdgtProperty, final WidgetProperty childWdgtProperty, final IPropertyEditor childPropEditor);

    void removeValueListener(ValueListener listener);

    void setReadOnly(boolean isReadOnly);

    boolean isReadOnly();

    boolean setPropertyValue(WidgetProperty property);

    Object getValue();

    QWidget asQWidget();

}
