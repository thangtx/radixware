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

package org.radixware.kernel.explorer.editors.valeditors;

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt.Alignment;
import com.trolltech.qt.core.Qt.AlignmentFlag;
import com.trolltech.qt.core.Qt.FocusPolicy;
import com.trolltech.qt.core.Qt.CheckState;
import com.trolltech.qt.gui.QBoxLayout;
import com.trolltech.qt.gui.QCheckBox;
import com.trolltech.qt.gui.QCloseEvent;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QPalette;
import com.trolltech.qt.gui.QWidget;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QSizePolicy.Policy;
import java.util.List;
import java.util.Objects;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.EditMaskNone;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.client.types.IEditingHistory;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.text.ExplorerFont;
import org.radixware.kernel.explorer.text.ExplorerTextOptions;

/**
 * ValBoolEditor - редактор для значений типа Boolean. содержит QCheckBox
 * который может быть в трех состояниях для значений true,false и null. Когда
 * значение равно null - дополнительно отображается QLabel с надписью
 * getNoValueStr() (использовать QCheckBox::setText не получилось т.к. надпись и
 * checkbox могут иметь разный цвет фона). Когда isMandatory=false в sizeHint
 * редактора входит размер QLabel независимо от текущего значения.
 */
public class ValBoolEditor extends ValEditor<Boolean> {
    
    private final TristateCheckBox valCheck = new TristateCheckBox(this);
    private final QLabel label = new QLabel(this);
    private String labelStyleSheet;
    private final static int SPACING = 4;

    @SuppressWarnings("LeakingThisInConstructor")
    public ValBoolEditor(final IClientEnvironment environment, final QWidget parent, final EditMask editMask, final boolean mandatory, final boolean readOnly) {
        super(environment, parent, editMask, mandatory, readOnly);//editmask instanceof EditMaskNone
        this.setFrame(false);
        getLineEdit().setReadOnly(true);
        getLineEdit().setVisible(false);
        clearBtn.setVisible(false);

        ((QBoxLayout) layout()).insertWidget(0,label);        
        ((QBoxLayout) layout()).insertWidget(0, valCheck);
        ((QBoxLayout) layout()).setSpacing(SPACING);
               
        label.setObjectName("lbNoValue");
        label.setText(getNoValueStr());
        valCheck.setSizePolicy(Policy.Maximum, Policy.Expanding);
        valCheck.setObjectName("cbValCheck");
        label.setSizePolicy(Policy.Maximum, Policy.Expanding);
        label.setAlignment(AlignmentFlag.AlignVCenter);
        
        valCheck.clicked.connect(this, "onClicked(Boolean)");
        valCheck.installEventFilter(inputEventListener);
        refresh();
        setFocusProxy(valCheck);
        valCheck.setDisabled(readOnly);
    }

    public ValBoolEditor(final IClientEnvironment environment, final QWidget parent){
        this(environment,parent, new EditMaskNone(),true,false);
    }

    public final QCheckBox getCheckBox() {
        return valCheck;
    }

    @SuppressWarnings("unused")
    private void onClicked(final Boolean b) {
        final Boolean newValue;
        if (getValue() == null) {
            newValue = true;
        } else if (!getValue().booleanValue()) {
            if (isMandatory()) {
                newValue = true;
            } else {
                newValue = null;
            }
        } else {
            newValue = false;
        }
        if (getEditMask().validate(getEnvironment(),newValue)==ValidationResult.ACCEPTABLE) {
            setOnlyValue(newValue);
            editingFinished.emit(getValue());
        } else {
            Application.beep();
        }

    }

    @Override
    protected boolean setOnlyValue(final Boolean value) {
        if (eq(value, getValue())) {
            return false;
        }
        this.value = value;
        updateValueMarkers(true);
        refresh();        
        valueChanged.emit(value);
        return true;
    }

    @Override
    public final void refresh() {
        if (value != null) {
            valCheck.setTristate(!isMandatory());
            if (value) {
                valCheck.setCheckState(CheckState.Checked);
            } else {
                valCheck.setCheckState(CheckState.Unchecked);
            }
        } else {            
            valCheck.setTristate(true);
            valCheck.setCheckState(CheckState.PartiallyChecked);
        }
        label.setVisible(value == null);
        updateGeometry();
    }

    @Override
    public void setValue(final Boolean value) {
        if (!setOnlyValue(value)) {
            return;
        }
        getLineEdit().setCursorPosition(0);
        getLineEdit().clearFocus();
    }

    @Override
    public void setReadOnly(final boolean readOnly) {
        super.setReadOnly(readOnly);
        clearBtn.setVisible(false);
        valCheck.setDisabled(readOnly);
    }
    
    @Override
    public void setMandatory(final boolean mandatory) {
        super.setMandatory(mandatory);
        clearBtn.setVisible(false);
        valCheck.setTristate(!mandatory || getValue()==null);
        updateGeometry();
    }

    @Override
    public void changeStateForGrid() {
        super.changeStateForGrid();
        ((QBoxLayout) layout()).setAlignment(valCheck, new AlignmentFlag[]{});
        valCheck.setAutoFillBackground(true);
        valCheck.setFocusPolicy(FocusPolicy.NoFocus);
        label.setAutoFillBackground(true);
        final QPalette palette = new QPalette(valCheck.palette());
        palette.setColor(QPalette.ColorRole.Base, getLineEdit().palette().color(getLineEdit().backgroundRole()));
        palette.setColor(QPalette.ColorRole.Button, getLineEdit().palette().color(getLineEdit().backgroundRole()));
        valCheck.setPalette(palette);
    }

    @Override
    public QPushButton insertButton(final String buttonText) {
        final QPushButton button = new QPushButton(buttonText, this);
        final QFont font = button.font();
        font.setPointSize(font.pointSize() - 1);
        button.setFont(font);
        ((QBoxLayout) layout()).insertWidget(0, button, 0, new Alignment(AlignmentFlag.AlignVCenter, AlignmentFlag.AlignLeft));
        button.setSizePolicy(Policy.Maximum, Policy.Fixed);
        button.setFixedHeight(8);
        button.resize(button.sizeHint().width(), 8);
        button.setFocusPolicy(FocusPolicy.NoFocus);
        pButtons.add(button);
        return button;
    }

    @Override
    protected void applyFont(final ExplorerFont newFont) {
        if (newFont!=null){
            valCheck.setFont(newFont.getQFont());
            if (labelStyleSheet!=null && !labelStyleSheet.isEmpty()){
                label.setStyleSheet(null);
            }
            final QFont labelFont = newFont.getQFont();
            label.setFont(labelFont);
            getLineEdit().setFont(newFont.getQFont());
            if (labelStyleSheet!=null && !labelStyleSheet.isEmpty()){
                changeStyleSheet(label, labelStyleSheet, labelFont);
            }
        }
    }         

    @Override
    protected void applyTextOptions(final ExplorerTextOptions options) {
        if (options.getQFont()!=null){
            valCheck.setFont(options.getQFont());
            getLineEdit().setFont(options.getQFont());
        }
        final QColor fgColor = options.getForeground();
        {//apply label options
            final String newStylesheet;
            if (isEnabled() && fgColor!=null) {
                newStylesheet = "color:  " + fgColor.name();
            } else {
                newStylesheet = "";
            }
            final boolean styleSheetChanged = !Objects.equals(labelStyleSheet, newStylesheet);
            final boolean fontChanged;
            if (options.getFont()==null){
                fontChanged = false;
            }else{
                fontChanged = !options.getQFont().toString().equals(label.font().toString());
            }
            if (styleSheetChanged || fontChanged){//optimization
                final QFont labelFont;
                if (fontChanged){
                    if (labelStyleSheet!=null && !labelStyleSheet.isEmpty()){
                        label.setStyleSheet(null);
                    }
                    if (options.getQFont()==null){
                        labelFont = label.font();
                    }else{
                        labelFont = options.getQFont();
                        label.setFont(labelFont);
                    }
                }else{
                    labelFont = label.font();
                }
                labelStyleSheet = newStylesheet;
                changeStyleSheet(label, labelStyleSheet, labelFont);
            }
        }
        {//apply checkbox options
            final QColor bgColor = options.getBackground();            
            final QPalette palette = new QPalette();
            if (bgColor != null) {
                palette.setColor(QPalette.ColorRole.Base, bgColor.clone());
                palette.setColor(QPalette.ColorRole.Button, bgColor.clone());
            }
            if (fgColor != null) {
                palette.setBrush(QPalette.ColorRole.Text, options.getForegroundBrush());
                palette.setBrush(QPalette.ColorRole.WindowText, options.getForegroundBrush());
            }            
            valCheck.applyPalette(palette);
        }
    }
    
    @Override
    public boolean event(final QEvent e) {
        if (e.type()==QEvent.Type.FontChange){
            final boolean result = super.event(e);
            if (result && !font().toString().equals(label.font().toString())){
                if (labelStyleSheet!=null && !labelStyleSheet.isEmpty()){
                    label.setStyleSheet(null);
                    label.setFont(font());
                    label.setStyleSheet(labelStyleSheet);
                }else{
                    label.setFont(font());
                }
            }
            return result;
        }
        return super.event(e);
    }      
    
    @Override
    public void setPredefinedValues(final List<Boolean> predefValues) {
        throw new UnsupportedOperationException("Unsupported operation.");
}

    @Override
    public List<Boolean> getPredefinedValues() {
        throw new UnsupportedOperationException("Unsupported operation.");
    }

    @Override
    public void setEditingHistory(final IEditingHistory history) {
        //No implementation
    }         

    @Override
    public QSize sizeHint() {
        return isInGrid() ? layout().sizeHint() : super.sizeHint();
    }    
    
    @Override    
    protected void closeEvent(final QCloseEvent event) {
        super.closeEvent(event);
        valCheck.close();
    }
}
