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

package org.radixware.kernel.explorer.editors.editmask.datetimeeditor;

import com.trolltech.qt.gui.QComboBox;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.enums.EDateTimeStyle;
import org.radixware.kernel.explorer.editors.editmask.IEditMaskEditorSetting;


abstract class AbstractDateTimeStyleSetting extends QWidget implements IEditMaskEditorSetting {
    
    public final Signal1<EDateTimeStyle> valueChanged = new Signal1<>();
    private final QComboBox styles;
    protected final QLabel label;
    
    public AbstractDateTimeStyleSetting(final IClientEnvironment env, final QWidget parent) {
        super(parent);
        
        final QVBoxLayout layout = new QVBoxLayout();
        layout.setMargin(0);
        setLayout(layout);
        
        label = new QLabel();
        label.setParent((QWidget) this);
        layout.addWidget(label);
        
        styles = new QComboBox((QWidget)this);
        for(EDateTimeStyle e : EDateTimeStyle.values()) {
            styles.addItem(getLocalizedOption(env.getMessageProvider(), e), e);
        }
        styles.activatedIndex.connect(this, "onValueChanged(Integer)");
        layout.addWidget(styles);
    }
    
    @Override
    public final void setDefaultValue() {
        styles.setCurrentIndex(0);
    }

    @Override
    public final Object getValue() {
        return styles.itemData(styles.currentIndex());
    }
    
    public final void setValue(final EDateTimeStyle style) {
        styles.setCurrentIndex(style.getValue().intValue());
    }
    
    @SuppressWarnings("unused")
    private void onValueChanged(final Integer index) {
        final EDateTimeStyle newValue = (EDateTimeStyle) getValue();
        valueChanged.emit(newValue);
    }
    
    private static String getLocalizedOption(final MessageProvider msgProvider, final EDateTimeStyle dateTimeStyle) {
        switch(dateTimeStyle) {
            case DEFAULT:
                return msgProvider.translate("EditMask", "Default");
            case NONE:
                return msgProvider.translate("EditMask", "Do not show");
            case SHORT:
                return msgProvider.translate("EditMask", "Short");
            case LONG:
                return msgProvider.translate("EditMask", "Long");
            case MEDIUM:
                return msgProvider.translate("EditMask", "Medium");
            case FULL:
                return msgProvider.translate("EditMask", "Full");
            case CUSTOM:
                return msgProvider.translate("EditMask", "User-defined");
            default:
                throw new IllegalArgumentException("Unknown delimiter type");
        }
    }
}
