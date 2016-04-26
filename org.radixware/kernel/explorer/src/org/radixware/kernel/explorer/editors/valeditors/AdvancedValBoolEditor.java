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
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QBoxLayout;
import com.trolltech.qt.gui.QCheckBox;
import com.trolltech.qt.gui.QCloseEvent;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QLayout;
import com.trolltech.qt.gui.QPalette;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QWidget;
import java.util.Objects;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.EditMaskBool;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.client.types.IEditingHistory;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.text.ExplorerFont;
import org.radixware.kernel.explorer.text.ExplorerTextOptions;

/**
 Редактор для значений типа Boolean, Long, BigDecimal и String. Тип возвращаемых значений,
 * собственно значения, заголовки значений задаются в маске редактрования EditMaskBool. Значение, которое
 * возвращает редактор, будет зависеть от состояния галочки в редакторе - в состоянии Checked редактор вернет
 * значение свойства, указанное в маске редактрования как TrueValue, в состоянии Unchecked - значение
 * FalseValue, для состояния PartiallyChecked возвращаемое значение всегда null, заголовок для значения null
 * берется из редактора свойства.
 */
public class AdvancedValBoolEditor<T extends Object> extends ValEditor<T> {

    private final TristateCheckBox valCheck = new TristateCheckBox(this);
    private final QLabel label = new QLabel(this);
    private String labelStyleSheet;
    private final static int SPACING = 4;

    @SuppressWarnings("LeakingThisInConstructor")
    public AdvancedValBoolEditor(final IClientEnvironment environment, final QWidget parent, final EditMask editMask, final boolean mandatory, final boolean readOnly) {
        super(environment, parent, editMask, mandatory, readOnly);
        if (!(editMask instanceof EditMaskBool)) {
            throw new IllegalArgumentException("Edit mask type " + editMask.getType() + " is not expected here. Use EditMaskBool instead.");
        }

        this.setFrame(false);
        getLineEdit().setReadOnly(true);
        getLineEdit().setVisible(false);
        clearBtn.setVisible(false);

        ((QBoxLayout) layout()).insertWidget(0, label);        
        ((QBoxLayout) layout()).insertWidget(0, valCheck);        
        ((QBoxLayout) layout()).setSpacing(SPACING);
        
        label.setObjectName("lbNoValue");        

        valCheck.setSizePolicy(QSizePolicy.Policy.Maximum, QSizePolicy.Policy.Expanding);
        valCheck.setObjectName("cbValCheck");
        label.setSizePolicy(QSizePolicy.Policy.Maximum, QSizePolicy.Policy.Expanding);
        label.setAlignment(Qt.AlignmentFlag.AlignVCenter);        

        valCheck.clicked.connect(this, "onClicked(Boolean)");
        valCheck.installEventFilter(inputEventListener);
        refresh();        
        valCheck.setDisabled(readOnly);
    }

    public AdvancedValBoolEditor(final IClientEnvironment environment, final QWidget parent) {
        this(environment, parent, new EditMaskBool(), true, false);
    }

    public final QCheckBox getCheckBox() {
        return valCheck;
    }

    @Override
    protected boolean setOnlyValue(final T value) {
        if (eq(value, getValue())) {
            return false;
        }
        this.value = value;
        updateValueMarkers(true);
        refresh();
        valueChanged.emit(value);
        getLineEdit().setCursorPosition(0);
        getLineEdit().clearFocus();
        return true;
    }

    /**
     * Метод возвращает значение определенного типа, указанного в маске редактирования EditMaskBool. Список
     * разрешенных возвращаемых типов содержится в перечислении ECompatibleTypesForBool.
     */
    @Override
    @SuppressWarnings("unchecked")
    public T getValue() {
        T retVal = null;
        if (value != null) {
            final EditMaskBool mask = (EditMaskBool) getEditMask();
            if (value.equals(mask.getTrueValue())) {
                retVal = (T) mask.getTrueValue();
            } else if (value.equals(mask.getFalseValue())) {
                retVal = (T) mask.getFalseValue();
            }
        }
        return retVal;
    }

    @Override
    public final void refresh() {
        if (value != null) {
            valCheck.setTristate(!isMandatory());
            if (value.equals(((EditMaskBool) getEditMask()).getTrueValue())) {
                valCheck.setCheckState(Qt.CheckState.Checked);
            } else {
                valCheck.setCheckState(Qt.CheckState.Unchecked);
            }
        } else {
            valCheck.setTristate(true);
            valCheck.setCheckState(Qt.CheckState.PartiallyChecked);
        }
        if (value == null) {
            label.setVisible(true);
            label.setText(getNoValueStr());
        } else {
            final String text;
            final EditMaskBool mask = (EditMaskBool) getEditMask();
            if (mask.getValueTitleVisible()) {
                if (value.equals(mask.getTrueValue())) {
                    text = mask.getTrueTitle(getEnvironment().getDefManager());
                } else if (value.equals(mask.getFalseValue())) {
                    text = mask.getFalseTitle(getEnvironment().getDefManager());
                } else {
                    text = getNoValueStr();
                }
                if (text != null && !text.isEmpty()) {
                    label.setText(text);
                    label.setVisible(true);
                } else {
                    label.setVisible(false);
                }
            } else {
                label.setVisible(false);
            }
        }
        updateGeometry();
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
        final boolean tristate = !mandatory || getValue() == null;
        valCheck.setTristate(tristate);
        updateGeometry();
    }

    @Override
    public void changeStateForGrid() {
        super.changeStateForGrid();
        ((QBoxLayout) layout()).setAlignment(valCheck, new Qt.AlignmentFlag[]{});
        valCheck.setAutoFillBackground(true);
        valCheck.setFocusPolicy(Qt.FocusPolicy.NoFocus);
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
        ((QBoxLayout) layout()).insertWidget(0, button, 0, new Qt.Alignment(Qt.AlignmentFlag.AlignVCenter, Qt.AlignmentFlag.AlignLeft));
        button.setSizePolicy(QSizePolicy.Policy.Maximum, QSizePolicy.Policy.Fixed);
        button.setFixedHeight(8);
        button.resize(button.sizeHint().width(), 8);
        button.setFocusPolicy(Qt.FocusPolicy.NoFocus);
        pButtons.add(button);
        return button;
    }

    @Override
    protected void applyFont(final ExplorerFont newFont) {
        if (newFont != null) {
            valCheck.setFont(newFont.getQFont());
            if (labelStyleSheet != null && !labelStyleSheet.isEmpty()) {
                label.setStyleSheet(null);
            }
            final QFont labelFont = newFont.getQFont();
            label.setFont(labelFont);
            getLineEdit().setFont(newFont.getQFont());
            if (labelStyleSheet != null && !labelStyleSheet.isEmpty()) {
                changeStyleSheet(label, labelStyleSheet, labelFont);
            }
        }
    }

    @SuppressWarnings({"unused","unchecked"})
    private void onClicked(final Boolean b) {
        final T newValue;
        final EditMaskBool mask = (EditMaskBool) getEditMask();
        if (value == null) {
            newValue = (T) mask.getTrueValue();
        } else if (value.equals(mask.getFalseValue())) {
            if (isMandatory()) {
                newValue = (T) mask.getTrueValue();
            } else {
                newValue = null;
            }
        } else {
            newValue = (T) mask.getFalseValue();
        }
        if (getEditMask().validate(getEnvironment(), newValue) == ValidationResult.ACCEPTABLE) {
            setOnlyValue(newValue);
            editingFinished.emit(getValue());
        } else {
            Application.beep();
        }
    }

    @Override
    protected void applyTextOptions(final ExplorerTextOptions options) {
        if (options.getQFont() != null) {
            valCheck.setFont(options.getQFont());
            getLineEdit().setFont(options.getQFont());
        }
        final QColor fgColor = options.getForeground();
        {//apply label options
            final String newStylesheet;
            if (isEnabled() && fgColor != null) {
                newStylesheet = "color:  " + fgColor.name();
            } else {
                newStylesheet = "";
            }
            final boolean styleSheetChanged = !Objects.equals(labelStyleSheet, newStylesheet);
            final boolean fontChanged;
            if (options.getFont() == null) {
                fontChanged = false;
            } else {
                fontChanged = !options.getQFont().toString().equals(label.font().toString());                
            }
            if (styleSheetChanged || fontChanged) {//optimization
                final QFont labelFont;
                if (fontChanged) {
                    if (labelStyleSheet != null && !labelStyleSheet.isEmpty()) {
                        label.setStyleSheet(null);
                    }
                    if (options.getQFont() == null) {
                        labelFont = label.font();
                    } else {
                        labelFont = options.getQFont();
                        label.setFont(labelFont);
                    }
                } else {
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
        if (e.type() == QEvent.Type.FontChange) {
            final boolean result = super.event(e);
            if (result && !font().toString().equals(label.font().toString())) {
                if (labelStyleSheet != null && !labelStyleSheet.isEmpty()) {
                    label.setStyleSheet(null);
                    label.setFont(font());
                    label.setStyleSheet(labelStyleSheet);
                } else {
                    label.setFont(font());
                }
            }
            return result;
        }
        return super.event(e);
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
