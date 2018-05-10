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

import com.trolltech.qt.gui.QCursor;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.mask.EditMaskStr;
import org.radixware.kernel.explorer.editors.valeditors.ValStrEditor;
import org.radixware.kernel.explorer.inspector.WidgetProperty;


public class QCursorPropertyEditor extends ValStrEditor implements IPropertyEditor {
    
    public QCursorPropertyEditor(IClientEnvironment environment, QWidget parent) {
        super(environment, parent, new EditMaskStr(), false, true);
        this.setMinimumHeight(20);
    }
    
    @Override
    public void addValueListener(ValueListener listener) {
    }
    

    @Override
    public void registerChildPropEditor(final WidgetProperty parentWdgtProperty, final WidgetProperty childWdgtProperty, final IPropertyEditor childPropEditor) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removeValueListener(ValueListener listener) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean setPropertyValue(WidgetProperty property) {
        this.setValue(((QCursor)property.getValue()).shape().toString());   
        return true;
    }

    @Override
    public QWidget asQWidget() {
        return this;
    }
}