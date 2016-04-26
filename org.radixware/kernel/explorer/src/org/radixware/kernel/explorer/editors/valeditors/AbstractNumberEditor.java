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

import com.trolltech.qt.QNoSuchEnumValueException;
import com.trolltech.qt.core.Qt;
import static com.trolltech.qt.core.Qt.Key.Key_Down;
import static com.trolltech.qt.core.Qt.Key.Key_PageDown;
import static com.trolltech.qt.core.Qt.Key.Key_PageUp;
import static com.trolltech.qt.core.Qt.Key.Key_Up;
import com.trolltech.qt.gui.QAbstractSpinBox;
import com.trolltech.qt.gui.QKeyEvent;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QSpinBox;
import com.trolltech.qt.gui.QValidator;
import com.trolltech.qt.gui.QWheelEvent;
import com.trolltech.qt.gui.QWidget;
import java.util.Objects;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.mask.EditMask;


abstract class AbstractNumberEditor<T> extends ValEditor<T> {
    
    private final class SpinBox extends QSpinBox {

        SpinBox(final QWidget parent) {
            super(parent);            
        }

        @Override
        public QValidator.State validate(final QValidator.QValidationData data) {
            return QValidator.State.Acceptable;
        }

        @Override
        protected void wheelEvent(final QWheelEvent qwe) {
            //Do not process this event: RADIX-7110
        }                

        @Override
        protected String textFromValue(final int value) {
            return this.lineEdit().text();
        }
        
        /*private long getCurrentValue(final EditMaskInt maskInt) throws NumberFormatException{
            final String text = getLineEdit().text();
            return maskInt.fromStr(getEnvironment(), text);            
        }*/

        @Override
        public void stepBy(final int step) {
            if (isReadOnly()) {
                return;
            }
            doSpin(step);
            /*
            final EditMaskInt maskInt = (EditMaskInt) getEditMask();
            if (getValue() == null) {//RADIX-2423
                final long newValue = maskInt.getMinValue() <= 0 ? 0 : maskInt.getMinValue();
                //ValIntEditor.this.setValue(newValue); Приводит к потере фокуса, поэтому текст меняем напрямую
                if (maskInt.validate(getEnvironment(),newValue)==ValidationResult.ACCEPTABLE){
                    changeValue(newValue,maskInt);
                }
                return;
            }
            final long delta = step * maskInt.getStepSize();
            long cur = 0;
            try {
                cur = getCurrentValue(maskInt);
            } catch (NumberFormatException e) {
                return;
            }
            final long min = maskInt.getMinValue();
            final long max = maskInt.getMaxValue();
            // check overflow
            //if (step > 0 && cur + d < cur) return;
            //if (step < 0 && cur + d > cur) return;
            final long newValue = cur + delta;
            if (min <= newValue && newValue <= max) {
                changeValue(newValue,maskInt);
            }
            if ((min>newValue && newValue>cur) || (max<newValue && newValue<cur)){
                changeValue(newValue,maskInt);
            }*/
        }

        /*
        private void changeValue(final Long newValue, final EditMaskInt maskInt){
            ValIntEditor.this.setValue(newValue); Приводит к потере фокуса, поэтому текст меняем напрямую
            setOnlyValue(newValue);
            setOnlyText(getStringToShow(newValue), null);
        }*/

        @Override
        protected QAbstractSpinBox.StepEnabled stepEnabled() {
            final QAbstractSpinBox.StepEnabled flag = new QAbstractSpinBox.StepEnabled(QAbstractSpinBox.StepEnabledFlag.StepNone);
            if (canSpinUp()){
                flag.set(QAbstractSpinBox.StepEnabledFlag.StepUpEnabled);
            }
            if (canSpinDown()){
                flag.set(QAbstractSpinBox.StepEnabledFlag.StepDownEnabled);
            }
            return flag;
            /*
            final EditMaskInt maskInt = (EditMaskInt) getEditMask();
            final QAbstractSpinBox.StepEnabled flag = new QAbstractSpinBox.StepEnabled(QAbstractSpinBox.StepEnabledFlag.StepNone);
            if (isReadOnly()) {
                return flag;
            }
            if (maskInt.getStepSize() < 1) {
                return flag;
            }
            long cur = 0;
            try {
                cur = getCurrentValue(maskInt);
            } catch (NumberFormatException e) {
                return flag;
            }
            final long step = maskInt.getStepSize();
            final long min = maskInt.getMinValue();
            final long max = maskInt.getMaxValue();
            if (cur + step <= max) {
                flag.set(QAbstractSpinBox.StepEnabledFlag.StepUpEnabled);
            }
            if (min <= cur - step) {
                flag.set(QAbstractSpinBox.StepEnabledFlag.StepDownEnabled);
            }
            return flag;*/
        }
    }
    
    public static enum ESymbol{
        TRIAD_DELIMETER, DECIMAL_DELIMETER, PLUS, MINUS
    }
    
    final private static int SPIN_BUTTONS_WIDTH = 18;//may be it shoud depends from height?
    final private static String STYLE_SHEET = "QSpinBox {border: 0px;}\n"
            + "QSpinBox::up-button {width: " + SPIN_BUTTONS_WIDTH + "px;}\n"
            + "QSpinBox::down-button {width: " + SPIN_BUTTONS_WIDTH + "px;}";    
    
    final private SpinBox spinBoxButtons = new SpinBox(this);
    private boolean isSpinButtonsVisible;
            
    public AbstractNumberEditor(final IClientEnvironment environment, final QWidget parent, final EditMask editMask, final boolean mandatory,
            final boolean readOnly){
        super(environment,parent,editMask,mandatory,readOnly);
        spinBoxButtons.setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Expanding);
        spinBoxButtons.setReadOnly(readOnly);
        spinBoxButtons.setVisible(false);
        spinBoxButtons.setObjectName("spinBox");
        spinBoxButtons.setStyleSheet(STYLE_SHEET);
        spinBoxButtons.setFixedWidth(SPIN_BUTTONS_WIDTH);
        spinBoxButtons.setFocusProxy(getLineEdit());
        addWidget(spinBoxButtons);
        
    }
    
    protected final void setSpinButtonsVisible(final boolean isVisible){
        isSpinButtonsVisible = isVisible;
        spinBoxButtons.setVisible(isSpinButtonsVisible && canSpin());
    }
    
    public final boolean isSpinButtonsVisible(){
        return spinBoxButtons.isVisible();
    }

    @Override
    public void setReadOnly(final boolean readOnly) {
        super.setReadOnly(readOnly);
        spinBoxButtons.setVisible(isSpinButtonsVisible && canSpin());
    }

    @Override
    public void setEditMask(final EditMask editMask) {
        super.setEditMask(editMask);        
        final boolean isSpinVisible = isSpinButtonsVisible && canSpin();
        spinBoxButtons.setVisible(isSpinVisible);
        if (isSpinVisible){
            spinBoxButtons.repaint();
        }
    }

    @Override
    public void setValue(final T value) {
        super.setValue(value);
        spinBoxButtons.repaint();
    }        
    
    @Override
    protected void onTextEdited(final String newText) {
        T newValue;
        if (newText == null || newText.isEmpty()) {
            setOnlyValue(null);
            return;
        }        
        try{                
            newValue = parseNumber(newText);
        }
        catch(NumberFormatException ex){
            return;
        }
        if (newValue==null){
            setOnlyValue(null);
            return;
        }          
        final Character triadDelimeterChar = getCharacter(ESymbol.TRIAD_DELIMETER);
        int cursorPosition = getLineEdit().cursorPosition();            
        String inputText = newText;
        String formattedText = formatInputString(inputText);
        boolean forcedChangeOnBackspace = false;            
        if (sameAsCurrentNumber(newValue) && triadDelimeterChar!=null){
            final char triadDelimeter = triadDelimeterChar.charValue();
            if (formattedText.length()==newText.length()+1){//one character was removed
                int expectedTriadDelimetersCount = 0;
                for (int i=formattedText.length()-1; i>=0; i--){
                    if (formattedText.charAt(i)==triadDelimeter){
                        expectedTriadDelimetersCount++;
                    }
                }
                int actualTriadDelimetersCount = 0;
                for (int i=newText.length()-1; i>=0; i--){
                    if (newText.charAt(i)==triadDelimeter){
                        actualTriadDelimetersCount++;
                    }
                }
                if (expectedTriadDelimetersCount>actualTriadDelimetersCount){//triad delimeter was removed
                    if (getLastKey()==Qt.Key.Key_Delete.value() && cursorPosition<inputText.length()-1){
                        //removing character at cursorPosition
                        inputText = inputText.substring(0, cursorPosition)+inputText.substring(cursorPosition+1);                    
                    }else if (getLastKey()==Qt.Key.Key_Backspace.value() && cursorPosition>0 && cursorPosition<inputText.length()){
                        //removing character at cursorPosition-1
                        inputText = inputText.substring(0, cursorPosition-1)+inputText.substring(cursorPosition);
                        cursorPosition--;
                        forcedChangeOnBackspace = true;
                    }
                    if (!newText.equals(inputText)){
                        try{
                            newValue = parseNumber(inputText);
                        }
                        catch(NumberFormatException ex){
                            return;
                        }
                        formattedText = formatInputString(inputText);
                    }
                }
            }//if (formattedNewText.length()==newText.length()+1)
        }//if (newValue.equals(getValue()) && parser.getTriadDelimeter()!=null)
        setOnlyValue(newValue);
        int absolutePosition = 0;
        if (triadDelimeterChar!=null){                                
            for (int i=0; i<cursorPosition && i<inputText.length(); i++){
                if (inputText.charAt(i)!=triadDelimeterChar.charValue()){
                    absolutePosition++;
                }
            }
        }else{
            absolutePosition = cursorPosition;
        }
        boolean leadingDecimalDelimeter=false;
        final Character decimalDelimeter = getCharacter(ESymbol.DECIMAL_DELIMETER);
        if (decimalDelimeter!=null){
            final char plusCharacter = getCharacter(ESymbol.PLUS).charValue();
            final char minusCharacter = getCharacter(ESymbol.MINUS).charValue();            
            for (int i=cursorPosition-1; i>=0; i--){
                if (decimalDelimeter.charValue()==inputText.charAt(i) || inputText.charAt(i)=='.'){
                    leadingDecimalDelimeter=true;
                }else if (inputText.charAt(i)!=plusCharacter && 
                          inputText.charAt(i)!=minusCharacter){
                    leadingDecimalDelimeter = false;
                    break;
                }                    
            }
        }
        int newCursorPosition = 0;
        if (triadDelimeterChar!=null){
            final char triadDelimeter = triadDelimeterChar.charValue();
            final boolean isCursorAfterDoubleDelimeter = cursorPosition>1 && 
                                                         inputText.charAt(cursorPosition-1)==triadDelimeter &&
                                                         inputText.charAt(cursorPosition-2)==triadDelimeter;
            for (int i=absolutePosition,j=0; i>0 && j<formattedText.length(); j++){
                if (formattedText.charAt(j)!=triadDelimeter){
                    i--;
                }
                newCursorPosition++;
            }
            if ((isCursorAfterDoubleDelimeter || forcedChangeOnBackspace) && 
                newCursorPosition<formattedText.length() && 
                formattedText.charAt(newCursorPosition)==triadDelimeter){
                newCursorPosition++;                    
            }
        }else{
            newCursorPosition = cursorPosition;                        
        }

        if (leadingDecimalDelimeter){
            newCursorPosition++;
        }

        setOnlyText(formattedText, null);
        if (newCursorPosition<formattedText.length()){
            getLineEdit().setCursorPosition(newCursorPosition);
        }        
    }        
    
    @Override
    protected void keyPressEvent(final QKeyEvent keyEvent) {
        if (isReadOnly() || !spinBoxButtons.isVisible()) {
            super.keyPressEvent(keyEvent);
            return;
        }

        final Qt.Key key;
        try {
            key = Qt.Key.resolve(keyEvent.key());
        } catch (QNoSuchEnumValueException exception) {
            super.keyPressEvent(keyEvent);
            return;
        }
        
        if(keyEvent.modifiers().isSet(Qt.KeyboardModifier.ControlModifier)) {
            super.keyPressEvent(keyEvent);
        } else {
            switch (key) {
                case Key_PageUp:
                case Key_PageDown:
                case Key_Up:
                case Key_Down: {
                    keyEvent.accept();
                    int steps = 1;
                    if (key == Qt.Key.Key_PageUp || key == Qt.Key.Key_PageDown) {
                        steps = 10;
                    }

                    final QAbstractSpinBox.StepEnabledFlag stepFlag;
                    if (key == Qt.Key.Key_Down || key == Qt.Key.Key_PageDown) {
                        steps *= -1;
                        stepFlag = QAbstractSpinBox.StepEnabledFlag.StepDownEnabled;
                    } else {
                        stepFlag = QAbstractSpinBox.StepEnabledFlag.StepUpEnabled;
                    }

                    if (spinBoxButtons.stepEnabled().isSet(stepFlag)) {
                        spinBoxButtons.stepBy(steps);
                    }
                    return;
                }
                default:
                    super.keyPressEvent(keyEvent);
            }
        }
        
    }    
    
    protected boolean sameAsCurrentNumber(final T number){
        return Objects.equals(getValue(), number);
    }
    
    protected abstract T parseNumber(final String inputStr) throws NumberFormatException;
    
    protected abstract String formatInputString(final String inputStr);
    
    protected abstract Character getCharacter(final ESymbol symbol);
    
    protected abstract boolean canSpinUp();
    
    protected abstract boolean canSpinDown();
    
    protected abstract boolean canSpin();
    
    protected abstract void doSpin(final int step);
}