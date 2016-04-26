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

package org.radixware.kernel.explorer.editors.editmask.controls;

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.Qt.AlignmentFlag;
import com.trolltech.qt.gui.QCheckBox;
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.radixware.kernel.common.client.enums.EEditMaskOption;


abstract public class AbstractComplexCheckableSetting extends QWidget{
    private QGridLayout mainLayout;
    private QVBoxLayout ctrlsLayout;
    private QCheckBox checkBox;
    private final Map<EEditMaskOption, QWidget> setOfControls = new EnumMap<EEditMaskOption, QWidget>(EEditMaskOption.class);
        
    public AbstractComplexCheckableSetting(final QWidget parent) {
        super(parent);
        setUpUi();
        this.setLayout(mainLayout);
        checkBox.stateChanged.connect((QWidget)this, "onCheckBoxStateChanged()");
    }
    
           
    public final void addWidget(final QWidget widget, final EEditMaskOption type) {
        setOfControls.put(type, widget);
        ctrlsLayout.addWidget(widget, 0, AlignmentFlag.AlignTop);
    }
    
    public final void enable(final boolean flag) {
        final Collection<QWidget> controls = setOfControls.values();
        for(QWidget w : controls) {
            w.setEnabled(flag);
        }
        checkBox.setChecked(flag);
    }
    
    abstract protected void onCheckBoxStateChanged();
    
    private void setUpUi() {
        final Qt.Alignment topAlign = new Qt.Alignment(Qt.AlignmentFlag.AlignTop, Qt.AlignmentFlag.AlignLeft);
        mainLayout = new QGridLayout();
        mainLayout.setMargin(0);
        mainLayout.setAlignment(topAlign);
        ctrlsLayout = new QVBoxLayout();
        ctrlsLayout.setMargin(0);
        ctrlsLayout.setAlignment(topAlign);
        mainLayout.addLayout(ctrlsLayout, 1, 1);
        checkBox = new QCheckBox(this);
        mainLayout.addWidget(checkBox, 0, 0, 1, 2, topAlign);
        
        enable(false);
        
    }
    
    public final void setText(final String text) {
        checkBox.setText(text);
    }
    
    public final boolean isChecked() {
        return checkBox.isChecked();
    }
    
    public final void setState(final Qt.CheckState state) {
        checkBox.setCheckState(state);
    }
    
    public final Qt.CheckState getState() {
        return checkBox.checkState();
    }
}
