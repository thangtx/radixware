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

import com.trolltech.qt.gui.QWheelEvent;
import com.trolltech.qt.gui.QWidget;
import java.text.DecimalFormatSymbols;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.EditMaskInt;
import org.radixware.kernel.common.client.meta.mask.validators.IInputValidator;
import org.radixware.kernel.common.client.meta.mask.validators.LongValidator;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EValType;


/**
 * ValIntEditor - редактор для значений типа Long.
 * За основу редактора взять виджет QSpinBox.
 * Диапозон ввода этого виджета ограничен типом int.
 * Для преодоления этого ограничения создан класс
 * SpinBox, который расширяет QSpinBox и переоределяет
 * некоторые методы.
 * */
public class ValIntEditor extends AbstractNumberEditor<Long> {

    public ValIntEditor(final IClientEnvironment environment, final QWidget parent, final EditMaskInt editMaskInt,
            final boolean mandatory, final boolean readOnly) {
        super(environment, parent, editMaskInt, mandatory, readOnly);
        setValue(null);
        afterEditMaskChanged();
    }

    public ValIntEditor(final IClientEnvironment environment, final QWidget parent){
        this(environment,parent,new EditMaskInt(),true,false);
    }

    @Override
    protected void onFocusIn() {
        if (!isReadOnly()) {
            if (getValue() == null){
                setOnlyText("", null);
            }else{
                final EditMaskInt editMask = (EditMaskInt)getEditMask();
                if (editMask.getNumberBase()==10){
                    final String valAsStr = String.valueOf(getValue());
                    final String formattedText = ((EditMaskInt)getEditMask()).formatInputString(getEnvironment(), valAsStr);
                    setOnlyText(formattedText,null);
                }
            }
        }
        super.onFocusIn();
    }

    @Override
    protected void onFocusOut() {
        super.onFocusOut();
        if (!isReadOnly()) {
            setOnlyText(getStringToShow(getValue()), null);
            getLineEdit().home(false);
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
        afterEditMaskChanged();
    }

    private void afterEditMaskChanged(){
        final EditMaskInt editMask = (EditMaskInt)getEditMask();
        setSpinButtonsVisible(editMask.getStepSize()>0);
        final IInputValidator inputValidator = new LongValidator(editMask.getMinValue(), 
                                                                 editMask.getMaxValue(), 
                                                                 editMask.getNumberBase(),
                                                                 editMask.getTriadDelimeter(getEnvironment().getLocale()));
        setInputValidator(inputValidator);
    }


    @Override
    protected void wheelEvent(final QWheelEvent wheelEvent) {
        //Do not process this event: RADIX-7110
    }

    
    /**
     * Implementation of AbstractNumberEditor
     * */    

    @Override
    protected Long parseNumber(final String inputStr) throws NumberFormatException {
        return ((EditMaskInt)getEditMask()).fromStr(getEnvironment(), inputStr);
    }

    @Override
    protected String formatInputString(final String inputStr) {
        return ((EditMaskInt)getEditMask()).formatInputString(getEnvironment(), inputStr);
    }

    @Override
    protected Character getCharacter(final ESymbol symbol) {
        switch (symbol){
            case TRIAD_DELIMETER:
                return ((EditMaskInt)getEditMask()).getTriadDelimeter(getEnvironment().getLocale());
            case DECIMAL_DELIMETER:
                return null;
            case PLUS:
                return Character.valueOf('+');
            case MINUS:{
                final char minusCharacter =
                    DecimalFormatSymbols.getInstance(getEnvironment().getLocale()).getMinusSign();
                return Character.valueOf(minusCharacter);
            }
            default:
                throw new IllegalArgumentException("Unknown symbol "+symbol.name());
        }
    }

    @Override
    protected String valueAsStr(final Long value) {
        return ValAsStr.toStr(value, EValType.INT);
    }

    @Override
    protected Long stringAsValue(final String stringVal) {
        return (Long) ValAsStr.fromStr(stringVal, EValType.INT);
    }           

    @Override
    protected boolean canSpinUp() {
        if (canSpin()) {
            if (getValue()==null){
                return true;
            }            
            final long cur;
            try {
                cur = getCurrentValue();
            } catch (NumberFormatException e) {
                return false;
            }
            final EditMaskInt maskInt = (EditMaskInt) getEditMask();
            final long step = maskInt.getStepSize();
            final long max = maskInt.getMaxValue();
            return cur + step <= max;
        }
        return false;
    }

    @Override
    protected boolean canSpinDown() {
        if (canSpin()){
            if (getValue()==null){
                return true;
            }                        
            final long cur;
            try {
                cur = getCurrentValue();
            } catch (NumberFormatException e) {
                return false;
            }
            final EditMaskInt maskInt = (EditMaskInt) getEditMask();
            final long step = maskInt.getStepSize();
            final long min = maskInt.getMinValue();
            return min <= cur - step;
        }
        return false;
    }

    @Override
    protected void doSpin(final int step) {
        if (canSpin()){
            final EditMaskInt maskInt = (EditMaskInt) getEditMask();
            if (getValue() == null) {//RADIX-2423
                final long newValue = maskInt.getMinValue() <= 0 ? 0 : maskInt.getMinValue();
                //ValIntEditor.this.setValue(newValue); Приводит к потере фокуса, поэтому текст меняем напрямую
                if (maskInt.validate(getEnvironment(),newValue)==ValidationResult.ACCEPTABLE){
                    changeValue(newValue);
                }
                return;
            }            
            final long cur;
            try {
                cur = getCurrentValue();
            } catch (NumberFormatException e) {
                return;
            }
            final long delta = step * maskInt.getStepSize();
            final long min = maskInt.getMinValue();
            final long max = maskInt.getMaxValue();
            // check overflow
            //if (step > 0 && cur + d < cur) return;
            //if (step < 0 && cur + d > cur) return;
            final long newValue = cur + delta;
            if (min <= newValue && newValue <= max) {
                changeValue(newValue);
            }
            if ((min>newValue && newValue>cur) || (max<newValue && newValue<cur)){
                changeValue(newValue);
            }
        }
    }    
    
    @Override
    protected boolean canSpin(){
        if (isReadOnly()){
            return false;
        }
        final EditMaskInt maskInt = (EditMaskInt) getEditMask();
        if (maskInt.getStepSize()>=0){
            if (getValue() == null) {//RADIX-2423
                final long newValue = maskInt.getMinValue() <= 0 ? 0 : maskInt.getMinValue();                
                return maskInt.validate(getEnvironment(),newValue)==ValidationResult.ACCEPTABLE;
            }else{
                return true;
            }
        }else{
            return false;
        }
    }     
    
    private long getCurrentValue(){
        final String text = getLineEdit().text();
        return parseNumber(text);
    }
    
    private void changeValue(final Long newValue){
        //ValIntEditor.this.setValue(newValue); Приводит к потере фокуса, поэтому текст меняем напрямую
        setOnlyValue(newValue);
        setOnlyText(getStringToShow(newValue), null);
    }       
}