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

package org.radixware.kernel.explorer.editors.editmask.streditor;

import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt.Alignment;
import com.trolltech.qt.core.Qt.AlignmentFlag;
import com.trolltech.qt.gui.QComboBox;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QStackedLayout;
import com.trolltech.qt.gui.QStandardItem;
import com.trolltech.qt.gui.QStandardItemModel;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EEditMaskOption;
import org.radixware.kernel.common.client.meta.mask.EditMaskInt;
import org.radixware.kernel.explorer.editors.editmask.IEditMaskEditorSetting;
import org.radixware.kernel.explorer.editors.editmask.controls.AbstractCheckableEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValIntEditor;


final class StrValueControlWidget extends QWidget {
    public static final int DEFAULT = 0;
    public static final int INT = 1;
    public static final int NUM = 2;
    public static final int REGEXP = 3;
    
    private final Signal1<Integer> optionsChanged = new Signal1<Integer>();
    private List<IStrOptionsContainer> optionsContainer;
    private QVBoxLayout mainLayout;
    private QStackedLayout optionsLayout;
    private QComboBox comboValControlOptions;
    public StrValueControlWidget(final IClientEnvironment environment, final QWidget parent) {
        super(parent);
        setUpUi(environment);
        this.setLayout(mainLayout);
        mainLayout.addStretch();
        mainLayout.update();
    }
    
    private void setUpUi(final IClientEnvironment environment) {
        mainLayout = new QVBoxLayout();
        optionsLayout = new QStackedLayout();        
        final QHBoxLayout layoutComboBox = new QHBoxLayout();
        layoutComboBox.setAlignment(new Alignment(AlignmentFlag.AlignLeft));
        final QLabel labelValueControl = new QLabel(this);
        final String labelText = environment.getMessageProvider().translate("EditMask", "Value control:");
        labelValueControl.setText(labelText);
        layoutComboBox.addWidget(labelValueControl);
        //Combo box settings
        comboValControlOptions = new QComboBox(this);
        QSize[] sizeHints = new QSize[4];
        String[] signs = new String[4];
        signs[0] = environment.getMessageProvider().translate("EditMask", "Default");
        signs[1] = environment.getMessageProvider().translate("EditMask", "Integer");
        signs[2] = environment.getMessageProvider().translate("EditMask", "Number");
        signs[3] = environment.getMessageProvider().translate("EditMask", "Regular expression");
        for(String s : signs) { comboValControlOptions.addItem(s); }
        comboValControlOptions.setEditable(false);
        
        comboValControlOptions.currentIndexChanged.connect(this, "onCurrentIndexChange(int)");
        comboValControlOptions.currentIndexChanged.connect(optionsChanged);
        layoutComboBox.addWidget(comboValControlOptions);
        mainLayout.addLayout(layoutComboBox);
        //Changing widgets
        optionsContainer = new ArrayList<IStrOptionsContainer>();
        
        final StrDefaultSettings defaultSettings = new StrDefaultSettings(environment, this);
        sizeHints[0] = defaultSettings.sizeHint();
        optionsLayout.addStackedWidget(defaultSettings);
        optionsContainer.add(defaultSettings);
        
        final StrIntegerSettings intSettings = new StrIntegerSettings(environment, this);
        sizeHints[1] = intSettings.sizeHint();
        optionsLayout.addStackedWidget(intSettings);
        optionsContainer.add(intSettings);
        
        final StrNumberSettings numSettings = new StrNumberSettings(environment, this);
        sizeHints[2] = numSettings.sizeHint();
        optionsLayout.addStackedWidget(numSettings);
        optionsContainer.add(numSettings);
        
        final StrRegexpSettings regexpSettings = new StrRegexpSettings(environment, this);
        sizeHints[3] = regexpSettings.sizeHint();
        optionsLayout.addStackedWidget(regexpSettings);
        optionsContainer.add(regexpSettings);
        
        setOptimalSize(sizeHints);
        mainLayout.addLayout(optionsLayout);
        optionsLayout.setCurrentIndex(comboValControlOptions.currentIndex());
    }
    
    private void setOptimalSize(final QSize[] sizes) {
        int maxHeight = 0, maxWidth = 0;
        for(QSize s : sizes) {
            maxHeight = Math.max(maxHeight, s.height());
            maxWidth = Math.max(maxWidth, s.width());
        }
        
        final QSize maxSize = new QSize(maxWidth, maxHeight);
        for(IStrOptionsContainer c : optionsContainer) {
            c.setMinimumSize(maxSize);
        }
    }
    
    public void onCurrentIndexChange(final int index) {
        optionsLayout.setCurrentIndex(index);
    }

    public void addToXml(final org.radixware.schemas.editmask.EditMask editMask) {
        final int currentIndex = comboValControlOptions.currentIndex();
        final IStrOptionsContainer currentContainer
                = optionsContainer.get(currentIndex);
        final org.radixware.schemas.editmask.EditMaskStr editMaskStr
                = editMask.getStr();
        editMaskStr.setValidatorType(currentIndex);
        currentContainer.addToXml(editMask);        
    }
    
    public void loadFromXml(final org.radixware.schemas.editmask.EditMask editMask) {
        final org.radixware.schemas.editmask.EditMaskStr editMaskStr = 
                editMask.getStr();
        int index;
        if(editMaskStr.isSetValidatorType()) { 
            index = editMaskStr.getValidatorType();
        } else {
            index = 0;
        }
        comboValControlOptions.setCurrentIndex(index);
        optionsContainer.get(comboValControlOptions.currentIndex()).loadFromXml(editMask);
    }

     public void setHiddenOptions(final EnumSet<EEditMaskOption> options) {
        ((IStrOptionsContainer)optionsLayout.currentWidget()).setHiddenOptions(options);
     }

    
    public void setVisibleOptions(final EnumSet<EEditMaskOption> options) {
        ((IStrOptionsContainer)optionsLayout.currentWidget()).setVisibleOptions(options);
    }
    
    public void setEnabledOptions(final EnumSet<EEditMaskOption> options) {
        for(IStrOptionsContainer c : optionsContainer) {
            c.setEnabledOptions(options);
        }
    }
    
    public void setDisabledOptions(final EnumSet<EEditMaskOption> options) {
        for(IStrOptionsContainer c : optionsContainer) {
            c.setDisabledOptions(options);
        }
    }

    
    public boolean checkOptions() {
        final int index = comboValControlOptions.currentIndex();
        return optionsContainer.get(index).checkOptions();
    }
    
    public void onMaxLengthSet(final boolean flag) {
        //disable Default and Regexp options on max length set
        //((QStandardItemModel)comboValControlOptions.model()).item(DEFAULT).setEnabled(!flag);
        ((QStandardItemModel)comboValControlOptions.model()).item(REGEXP).setEnabled(!flag);
    }
    
    public Signal1<Integer> getIndexSignal() {
        return optionsChanged;
    }
    
    public IEditMaskEditorSetting getSetting(final EEditMaskOption option) {
        return optionsContainer.get(comboValControlOptions.currentIndex()).getSetting(option);
    }
    
    public void setIsChar(final boolean isChar) {
        ((StrIntegerSettings)optionsContainer.get(INT)).setIsChar(isChar);
        if(isChar) {
            final EditMaskInt mask = new EditMaskInt();
            mask.setMaxValue(9); // 9 is max numeric char
            mask.setMinValue(0); // 0 is min numeric char
            //'hide' value control for NUM as unnecessary
            ((QStandardItemModel)comboValControlOptions.model()).item(NUM).setEnabled(false);
            
            //set limits 0-9 to a CHAR as INTEGER and default values
            final AbstractCheckableEditor minValEditor = ((AbstractCheckableEditor)optionsContainer
                    .get(INT)
                    .getSetting(EEditMaskOption.STR_VC_INTEGER_MINIMUM));
            minValEditor.setClientEditMask(mask);
            
            final AbstractCheckableEditor maxValEditor = ((AbstractCheckableEditor)optionsContainer
                    .get(INT)
                    .getSetting(EEditMaskOption.STR_VC_INTEGER_MAXIMUM));
            maxValEditor.setClientEditMask(mask);
        } else {
            ((QStandardItemModel)comboValControlOptions.model()).item(NUM).setEnabled(true);
            optionsContainer.get(INT).getSetting(EEditMaskOption.STR_VC_INTEGER_MINIMUM).setDefaultValue();
            optionsContainer.get(INT).getSetting(EEditMaskOption.STR_VC_INTEGER_MAXIMUM).setDefaultValue();
        }
    }
}
