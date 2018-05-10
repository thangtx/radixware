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

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.*;
import com.trolltech.qt.gui.QTextCursor.MoveOperation;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.EditMaskStr;
import org.radixware.kernel.common.client.widgets.IMemoController;
import org.radixware.kernel.common.utils.Utils;


import org.radixware.kernel.explorer.utils.WidgetUtils;
import org.radixware.kernel.explorer.widgets.ExplorerPlainTextEdit;

/**
 * ValStrEditor - редактор для значений типа String.
 * Возможность работы в режиме ввода пароля.
 * Показ текстового поля для ввода больших строковых данных.
 *
 * */
public class ValStrEditor extends ValEditor<String> {

    private Memo memo;
    private QToolButton memoButton;   
    private IMemoController memoController;

    @SuppressWarnings("LeakingThisInConstructor")
    public ValStrEditor(final IClientEnvironment environment, final QWidget parent, final EditMaskStr editMaskStr,
            final boolean mandatory, final boolean readOnly) {
        super(environment, parent, editMaskStr, mandatory, readOnly);        
        //актуализация маски производится в updateEditMask
        getLineEdit().setReplaceNonPrintable(false);
        getLineEdit().cursorPositionChanged.connect(this, "onCursorPositionChanged(Integer,Integer)");
        //colorNullValued(getValue());
    }

    public ValStrEditor(final IClientEnvironment environment, final QWidget parent){
        this (environment,parent, new EditMaskStr(), true, false);
    }

    public void setMemoController(final IMemoController memoController) {
        this.memoController = memoController;
    }

    public IMemoController getMemoController() {
        return memoController;
    }
    
    @Override
    protected void onTextEdited(final String newText) {        
        final EditMaskStr mask = (EditMaskStr) getEditMask();
        final String newValue;
        if (mask.isPassword()) {
            newValue = newText;
        } else {
            final String displayString = getLineEdit().displayText();
            final boolean saveSeparators = mask.isSaveSeparators();
            newValue = mask.getInputMask().stripDisplayString(displayString,saveSeparators);//RADIX-3667
        }
        //It is not a user input when current value is null and new value equals to an empty string,
        //so we must to ignore this event.
        if (getValue()!=null || !"".equals(newValue)){
            setOnlyValue(newValue);
        }
        updateMask(true);
    }

    @Override
    protected void onFocusIn() {
//        restoreColor();
        if (!isReadOnly()) {            
            final EditMaskStr editMask = (EditMaskStr) getEditMask();
            if (editMask.isSpecialValue(getValue())) {
                setOnlyText("", null);
            } else if (!editMask.isPassword()){//TWRBS-1449
                setOnlyText(getStringToShow(getValue()), null);
            }
            updateMask(true);
        }
        super.onFocusIn();
    }

    @Override
    protected void onFocusOut() {
        super.onFocusOut();
        if (!isReadOnly()) {
            final EditMaskStr editMask = (EditMaskStr) getEditMask();
            final String formattedText = editMask.toStr(getEnvironment(),getValue());
            if ( (!editMask.isPassword() || editMask.isSpecialValue(getValue()))
                  && !formattedText.equals(getLineEdit().displayText())) {
                updateMask(false);
                if (editMask.isPassword()){
                    setOnlyText(getStringToShow(getValue()), null);
                }
                getLineEdit().home(false);
            }
        }
    }

    @SuppressWarnings("unused")
    private void onCursorPositionChanged(final Integer oldPosition, final Integer newPosition) {
        final EditMaskStr mask = (EditMaskStr) getEditMask();
        if (!mask.getInputMask().isEmpty() && hasFocus() && newPosition != null && newPosition > 0) {
            final int firstBlankCharPosition = 
                mask.getInputMask().getPositionOfFirstBlankCharInDisplayString(getLineEdit().displayText());
            if (firstBlankCharPosition > -1 && newPosition > firstBlankCharPosition) {
                getLineEdit().blockSignals(true);
                try {
                    getLineEdit().cursorBackward(getLineEdit().hasSelectedText(), newPosition - firstBlankCharPosition);
                } finally {
                    getLineEdit().blockSignals(false);
                }
            }
        }
    }

    @Override
    public void setReadOnly(final boolean readOnly) {
        super.setReadOnly(readOnly);
        getLineEdit().setReadOnly(readOnly);
    }

    @Override
    public void setEditMask(final EditMask editMask) {
        super.setEditMask(editMask);
        updateMask(getLineEdit().hasFocus());
    }

    @Override
    public void refresh() {
        updateMask(getLineEdit().hasFocus());
        final EditMaskStr mask = (EditMaskStr)getEditMask();
        if (mask.isPassword() && !mask.isSpecialValue(getValue())) {
            setOnlyText(getValue(), null);
            getLineEdit().setCursorPosition(0);
            getLineEdit().clearFocus();
            clearBtn.setVisible(!isMandatory() && !isReadOnly() && getValue() != null);
        } else {
            super.refresh();
        }
    }

    private void updateMask(final boolean inFocus) {
        final QLineEdit lineEdit = getLineEdit();
        final String currentValue = getValue();
        final int savePos = lineEdit.cursorPosition();
        final EditMaskStr mask = (EditMaskStr) getEditMask();
        lineEdit.blockSignals(true);
        try{
            if (inFocus) {
                boolean textUpdateRequired = false;
                final String inputMask = mask.getInputMask().getPattern();
                if (!Utils.equals(lineEdit.inputMask(), inputMask)) {
                    final QCompleter completer = lineEdit.completer();
                    if (completer!=null){
                        lineEdit.setCompleter(null);
                    }
                    try{
                        lineEdit.setInputMask(inputMask);
                    }finally{
                        if (completer!=null){
                            lineEdit.setCompleter(completer);
                        }
                    }
                    textUpdateRequired = true;
                }
                setEchoMode(mask.isPassword() ? QLineEdit.EchoMode.Password : QLineEdit.EchoMode.Normal);
                if (mask.getValidator()!=null){
                    setInputValidator(mask.getValidator());
                }
                if (inputMask==null || inputMask.isEmpty()) {
                    final int maxLength = mask.getMaxLength();
                    if (maxLength != lineEdit.maxLength()) {
                        lineEdit.setMaxLength(maxLength);
                        textUpdateRequired = true;                        
                    }
                }
                if (textUpdateRequired && !mask.isPassword() && !mask.isSpecialValue(currentValue)){
                    lineEdit.setText(getStringToShow(currentValue));
                }
            } else {
                lineEdit.setInputMask(null);
                setInputValidator(null);

                if (mask.isSpecialValue(currentValue)) {
                    if (lineEdit.maxLength()>-1){
                        lineEdit.setMaxLength(-1);
                    }
                    setEchoMode(QLineEdit.EchoMode.Normal);
                    setOnlyValue(null);
                    lineEdit.setText(getStringToShow(currentValue));
                } else {//TWRBS-1449
                    if (lineEdit.maxLength()!=mask.getMaxLength()){
                        lineEdit.setMaxLength(mask.getMaxLength());
                    }
                    setEchoMode(mask.isPassword() ? QLineEdit.EchoMode.Password : QLineEdit.EchoMode.Normal);
                    if (!mask.isPassword()){
                        lineEdit.setText(getStringToShow(currentValue));
                    }
                }
            }
        }finally{
            lineEdit.blockSignals(false);
        }
        lineEdit.setCursorPosition(savePos);
    }
    
    private void setEchoMode(final QLineEdit.EchoMode echoMode){
        if (getLineEdit().echoMode()!=echoMode){
            getLineEdit().setEchoMode(echoMode);
        }
    }

    @SuppressWarnings("unused")
    private void onTextChanged() {
        final String currentValue = getValue();
        final String valueInMemo = memo.getText();
        if (!eq(currentValue, valueInMemo)) {
            getLineEdit().setText(valueInMemo);
            //setValue(valueInMemo);
        }
    }

    @SuppressWarnings("unused")
    private void showMemo() {
        memo = new Memo(getLineEdit());
        memo.setText(memoController == null ? getValue() : memoController.prepareTextForMemo(getValue()));
        final EditMaskStr mask = (EditMaskStr) getEditMask();
        memo.setMaxTextLength( mask.isMaxLengthDefined() ? mask.getMaxLength() : -1 );
        
        if (isReadOnly()) {
            memo.setReadOnly(true);
        } else {
            memo.textChanged.connect(this, "onTextChanged()");
        }

        memo.finished.connect(this, "onMemoClosed()");
        memo.show();
    }

    @SuppressWarnings("unused")
    private void onValueChanged() {
        if (memoButton != null) {
            memoButton.setDisabled(getValue() == null);
        }
    }

    @SuppressWarnings("unused")
    private void onMemoClosed() {
        final String newValue = memoController == null ? memo.getText() : memoController.getFinalText(memo.getText());

        if (!isReadOnly() && !eq(getValue(), newValue)) {
            setValue(newValue);
        }
    }

    public void addMemo() {
        if (memoButton == null) {
            final QAction action = new QAction(this);
            action.triggered.connect(this, "showMemo()");
            memoButton = addButton("...", action);
            memoButton.setObjectName("memoButton");
            memoButton.setDisabled(getValue() == null);
        }
        memoButton.setVisible(true);
        valueChanged.connect(this, "onValueChanged()");
    }

    public void removeMemo() {
        if (memoButton != null) {
            memoButton.setVisible(false);
        }
    }

    public boolean hasMemo() {
        return memoButton != null && memoButton.isVisible();
    }

    final private static class Memo extends QDialog {

        final private static int HEIGHT = 200;
        
        final public Signal0 textChanged = new Signal0();
        final private ExplorerPlainTextEdit textEdit = new ExplorerPlainTextEdit(this);
        final private QVBoxLayout layout;
        
        @SuppressWarnings("LeakingThisInConstructor")
        Memo(final QWidget parent) {
            super(parent);
            this.setWindowFlags(new Qt.WindowFlags(Qt.WindowType.Popup));
            layout = WidgetUtils.createVBoxLayout(this);
            textEdit.setObjectName("memoEditor");
            textEdit.setFont(parent.font());
            textEdit.setPalette(parent.palette());
            textEdit.moveCursor(MoveOperation.End);
            textEdit.textChanged.connect(textChanged);            
            layout.addWidget(textEdit);
            if (!parent.visibleRegion().isEmpty()) {
                setGeometry(WidgetUtils.calcPopupGeometry(parent, HEIGHT));
                textEdit.setFocus();
            }
            setVisible(false);
        }
        
        public void setReadOnly(final boolean isReadOnly){
            textEdit.setReadOnly(isReadOnly);
        }
        
        public boolean isReadOnly(){
            return textEdit.isReadOnly();
        }

        public String getText() {
            return textEdit.toPlainText();
        }
        
        public void setText(final String text){
            textEdit.setPlainText(text);
        }
        
        public void setMaxTextLength(final int length){
            textEdit.setMaxLength(length);
        }
        
        public int getMaxTextLength(){
            return textEdit.getMaxLength();
        }
    }
    
    @Override
    protected String valueAsStr(final String value) {
        return value;
    }

    @Override
    protected String stringAsValue(final String stringVal) {
        return stringVal;
    }

    @Override
    protected boolean isButtonCanChangeValue(final QAbstractButton button) {
        return super.isButtonCanChangeValue(button) && (button!=memoButton || !isReadOnly());//NOPMD
    }    
}