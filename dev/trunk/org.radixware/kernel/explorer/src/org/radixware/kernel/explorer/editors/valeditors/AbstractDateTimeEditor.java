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

import com.trolltech.qt.core.QByteArray;
import java.sql.Timestamp;

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QMimeData;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.Qt.KeyboardModifier;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QFontMetrics;
import com.trolltech.qt.gui.QKeyEvent;
import com.trolltech.qt.gui.QLineEdit;
import com.trolltech.qt.gui.QWidget;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.errors.IClientError;
import org.radixware.kernel.common.client.exceptions.WrongAmPmFieldValue;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.EditMaskDateTime;
import org.radixware.kernel.common.client.meta.mask.validators.IInputValidator;
import org.radixware.kernel.common.client.meta.mask.validators.InvalidValueReason;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.exceptions.WrongFormatException;
import org.radixware.kernel.common.utils.SystemTools;

abstract class AbstractDateTimeEditor<T> extends ValEditor<T> {

    private static class DateTimeValidator implements IInputValidator {
            
        private final EditMaskDateTime editMask;
        
        public DateTimeValidator(final EditMaskDateTime editMask) {
            this.editMask = editMask;
        }
        
        @Override
        public String fixup(final String input) {
            return input;
        }

        @Override
        public ValidationResult validate(
                final IClientEnvironment environment,
                final String input,
                final int position) {                                   
            try {
                editMask.getValueForInputText(input, environment);
            } catch(WrongAmPmFieldValue ex){
                return ValidationResult.Factory.newInvalidResult(InvalidValueReason.WRONG_FORMAT);
            } catch (WrongFormatException ex) {
                return ValidationResult.Factory.newIntermediateResult(InvalidValueReason.WRONG_FORMAT);
            }
            return ValidationResult.ACCEPTABLE;            
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 53 * hash + Objects.hashCode(this.editMask);
            return hash;
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj == null) {
                return false;
            }
            if (obj == this){
                return true;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final DateTimeValidator other = (DateTimeValidator) obj;
            return Objects.equals(this.editMask, other.editMask);
        }
    }
    
    private static class WidthHintCalculator implements IWidthHintCalculator{
        
        private WidthHintCalculator(){            
        }
        
        private static final Timestamp VALUE = new Timestamp(0);
                
        public static final WidthHintCalculator INSTANCE = new WidthHintCalculator();
        
        @Override
        public int getWidthHint(final QFontMetrics fontMetrics, final EditMask editMask, final IClientEnvironment environment) {
            final int minWidth = 
                DefaultWidthHintCalculator.INSTANCE.getWidthHint(fontMetrics, editMask, environment);
            if (editMask instanceof EditMaskDateTime==false){//NOPMD
                return minWidth;
            }else{
                final int strValueWidth  = fontMetrics.width(editMask.toStr(environment, VALUE));
                return Math.max( minWidth, strValueWidth);
            }
        }
    }
    
    public AbstractDateTimeEditor(final IClientEnvironment environment, final QWidget parent, final EditMaskDateTime editMaskDateTime,
            final boolean mandatory, final boolean readOnly) {
        super(environment, parent, editMaskDateTime, mandatory, readOnly);        
        setWidthHintCalculator(WidthHintCalculator.INSTANCE);
        updateValidator();
    }

    public AbstractDateTimeEditor(final IClientEnvironment environment, final QWidget parent){
        this(environment,parent,new EditMaskDateTime(),true,false);
    }

    private void updateValidator() {
        setInputValidator(new DateTimeValidator((EditMaskDateTime)getEditMask()));
    }

    @Override
    protected void onTextEdited(final String newText) {
        super.onTextEdited(newText);
        final Timestamp newValue;
        try {
            newValue = getValueFromInputText(newText);
        } catch (WrongFormatException ex) {
            return;
        }
        setOnlyValue(getValueFromTimestamp(newValue));
    }
    
    protected Timestamp getValueFromInputText(final String inputText) throws WrongFormatException{
        return ((EditMaskDateTime) getEditMask()).getValueForInputText(inputText, getEnvironment());
    }

    @Override
    protected ValidationResult validateInputText(final String text) {
        final EditMaskDateTime editMask = (EditMaskDateTime) getEditMask();        
        try {
            editMask.getValueForInputText(text, getEnvironment());
        } catch (WrongFormatException ex) {
            final String reason;
            if (ex instanceof IClientError){//NOPMD
                reason = ((IClientError)ex).getLocalizedMessage(getEnvironment().getMessageProvider());
            }else{
                reason = ex.getMessage();
            }
            if (reason==null || reason.isEmpty()){
                return ValidationResult.Factory.newIntermediateResult(InvalidValueReason.WRONG_FORMAT);
            }else{
                return ValidationResult.Factory.newIntermediateResult(reason);
            }            
        }
        return ValidationResult.ACCEPTABLE;
    }    

    @Override
    protected void onFocusIn() {
        if (!isReadOnly()) {
            final QLineEdit lineEdit = getLineEdit();            
            final String inputMask = getInputMask();
            if (hasAcceptableInput()){                
                setOnlyText(getInputText(getValue()), inputMask);
            }else{
                setOnlyText(getUnacceptableInput().getText(), inputMask);
            }
            lineEdit.home(false);
        }
        updateValueMarkers(false);
    }    

    @Override
    protected void onFocusOut() {
        super.onFocusOut();
        if(!isReadOnly() && hasAcceptableInput()) {
            setOnlyText(getStringToShow(getValue()),"");
        }
    }
    
    protected String getInputMask(){
        return ((EditMaskDateTime) getEditMask()).getInputMask(getEnvironment());
    }

    @Override
    public void setValue(final T value) {
        if (!isReadOnly() && !hasAcceptableInput() && !getLineEdit().hasFocus()){
            setOnlyText(getStringToShow(getValue()),"");
        }
        super.setValue(value);
    }        
    
    @Override
    public void setReadOnly(final boolean readOnly) {
        getLineEdit().setReadOnly(readOnly);        
        super.setReadOnly(readOnly);
    }
        
    @Override
    public void setEditMask(final EditMask editMask) {
        super.setEditMask(editMask);
        updateValidator();
        refresh();
    }    
    
    @Override
    protected ValidationResult calcValidationResult(final T value) {
        return getEditMask().validate(getEnvironment(), getTimestampFromValue(value));
    }    

    @Override
    public boolean eventFilter(final QObject obj, final QEvent event) {
        if (isMandatory()) {
            return super.eventFilter(obj, event);
        }
        if (isReadOnly()) {
            return super.eventFilter(obj, event);
        }
        if (event instanceof QKeyEvent==false || event.type() != QEvent.Type.KeyPress) {//NOPMD
            return super.eventFilter(obj, event);
        }
        final QKeyEvent keyEvent = (QKeyEvent) event;
        final boolean isControl = keyEvent.modifiers().isSet(KeyboardModifier.ControlModifier)
                || (keyEvent.modifiers().isSet(KeyboardModifier.MetaModifier) && SystemTools.isOSX);
        final boolean isSpace = keyEvent.key() == Qt.Key.Key_Space.value();
        if (isControl && isSpace) {
            setValue(null);
            return false;
        } 
        return super.eventFilter(obj, event);
    }
    
    @Override
    protected String beforeCopyToClipboard(final String text) {
        if (Objects.equals(text, getLineEdit().displayText()) && hasAcceptableInput()){
            final Timestamp value;
            if (isReadOnly()){
                value = getTimestampFromValue(getValue());
                if (value==null){
                    return super.beforeCopyToClipboard(text);
                }
            }else{                
                final EditMaskDateTime editMask = (EditMaskDateTime)getEditMask();            
                try{
                    value = editMask.getValueForInputText(text, getEnvironment());
                }catch(WrongFormatException exception){
                    return super.beforeCopyToClipboard(text);
                }
            }
            final ByteBuffer buffer = ByteBuffer.allocate(8);            
            buffer.putLong(value.getTime());            
            final QByteArray binaryData = new QByteArray(buffer.array());            
            final QMimeData mimeData = new QMimeData();
            mimeData.setData("timestamp", binaryData);
            mimeData.setText(text);
            QApplication.clipboard().setMimeData(mimeData);
            return "";
        }
        return super.beforeCopyToClipboard(text);
    }        

    @Override
    protected boolean beforePasteFromClipboard(final String text) {
        final EditMaskDateTime editMask = (EditMaskDateTime)getEditMask();        
        final IClientEnvironment environmnet = getEnvironment();        
        final QMimeData mimeData = QApplication.clipboard().mimeData();
        final QByteArray binaryData =  mimeData.data("timestamp");
        if (binaryData!=null && binaryData.size()==8){
            final ByteBuffer buffer = ByteBuffer.wrap(binaryData.toByteArray());
            Timestamp value = null;
            try{
                value = new Timestamp(buffer.getLong());
            }catch(BufferUnderflowException exception){//NOPMD
                //ignoring exception
            }
            if (value!=null){
                setValue(getValueFromTimestamp(value));
                return false;
            }
        }
        try{
            final Timestamp value = 
                editMask.getValueForInputText(text,  environmnet);
            setValue(getValueFromTimestamp(value));
            return false;
        }catch(WrongFormatException exception){//NOPMD
            //ignoring exception            
        }
        final String pattern = editMask.getDisplayFormat(environmnet);
        final SimpleDateFormat format = new SimpleDateFormat(pattern,  environmnet.getLocale());
        format.setLenient(false);
        final Date date;
        try{
            date = format.parse(text);
        }catch(ParseException exception){
            return super.beforePasteFromClipboard(text);
        }
        final Timestamp value = new Timestamp(date.getTime());
        setValue(getValueFromTimestamp(value));
        return false;
    }
 
    protected abstract Timestamp getTimestampFromValue(T value);
    
    protected abstract T getValueFromTimestamp(Timestamp timestamp);
    
    protected abstract String getInputText(final T value);
}