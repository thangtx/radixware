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

import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QPicture;
import com.trolltech.qt.gui.QPixmap;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.explorer.inspector.WidgetProperty;

public class QIconPropertyEditor extends QLabel implements IPropertyEditor {

    public QIconPropertyEditor() {
        this.setSizePolicy(QSizePolicy.Policy.Fixed, QSizePolicy.Policy.Fixed);
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
    public void setReadOnly(boolean isReadOnly) {
    }

    @Override
    public boolean isReadOnly() {
        return true;
    }

    @Override
    public boolean setPropertyValue(WidgetProperty property) {
        QPixmap wIconPixmap = ((QIcon) property.getValue()).pixmap(20, 20);
        if (!wIconPixmap.isNull()) {
            this.setPixmap(wIconPixmap);
        } else {
            this.setPicture(new QPicture(-1));
        }
        return true;
    }

    @Override
    public Object getValue() {
        return this;
    }

    @Override
    public QWidget asQWidget() {
        return this;
    }
}
