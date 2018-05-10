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

package org.radixware.wps.views.editors.valeditors;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Locale;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.errors.IClientError;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.mask.EditMaskDateTime;
import org.radixware.kernel.common.client.meta.mask.validators.InvalidValueReason;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.client.utils.CalendarUtils;
import org.radixware.kernel.common.exceptions.WrongFormatException;
import org.radixware.wps.rwt.InputBox.DisplayController;
import org.radixware.wps.rwt.InputBox.InvalidStringValueException;
import org.radixware.wps.rwt.InputBox.ValueController;
import org.radixware.wps.rwt.InputFormat;


public class ValTimeEditorController extends InputBoxController<Long, EditMaskDateTime> {
    
    private static final long DEFAULT_MIN_VALUE = 0;
    private static final long DEFAULT_MAX_VALUE = CalendarUtils.getMillisForUnit(Calendar.DAY_OF_MONTH)-1;
    
    private final long timeZoneOffsetMillis = CalendarUtils.getTimeZoneOffsetInMillis();
    
    
    public ValTimeEditorController(final IClientEnvironment env) {
        super(env);
        setEditMask( getEditMaskTime(new EditMaskDateTime(), env) );
        setValue(Long.valueOf(0));
    }

    @Override
    protected ValueController<Long> createValueController() {
        return new ValueController<Long>(){
            @Override
            public Long getValue(String text) throws InvalidStringValueException {
                return parseInput(text);
            }
        };
    }
    
    protected Long parseInput(final String inputText) throws InvalidStringValueException{
        final EditMaskDateTime maskDateTime = getEditMask();
        final Timestamp timestamp;
        try {
            timestamp = maskDateTime.getValueForInputText(inputText, getEnvironment());            
        } catch (WrongFormatException ex) {
            final MessageProvider mp =getEnvironment().getMessageProvider();
            final String reason;
            if (ex instanceof IClientError){//NOPMD
                reason = ((IClientError)ex).getLocalizedMessage(mp);
            }else{
                reason = ex.getMessage();
            }
            if (reason==null || reason.isEmpty()){
                throw new InvalidStringValueException(mp, InvalidValueReason.Factory.createForWrongFormatValue(getEnvironment()));//NOPMD
            }else{
                throw new InvalidStringValueException(mp, InvalidValueReason.Factory.createForInvalidValue(reason));//NOPMD
            }
        }
        return getValueFromTimestamp(timestamp);
    }
        
    @Override
    protected ValidationResult calcValidationResult(Long value) {
        final Timestamp timestamp = getTimestampFromValue(value==null ? null : value - timeZoneOffsetMillis);
        return getEditMask().validate(getEnvironment(), timestamp);
    }        

    @Override
    protected DisplayController<Long> createDisplayController() {
        return new DisplayController<Long>(){
            @Override
            public String getDisplayValue(Long value, boolean isFocused, boolean isReadOnly) {
                if (isFocused && !isReadOnly){
                    return calcFocusedText(value, getEditMask());
                }else{
                    return getDisplayString(value);
                }
            }
        };
    }
        
    protected String getDisplayString(final Long value){
        final EditMaskDateTime mask = getEditMask();
        if (mask==null){
            return "";
        }        
        final Timestamp timestamp = value==null ? null : getTimestampFromValue(value - timeZoneOffsetMillis);
        return mask.toStr(getEnvironment(), timestamp);
    }
    
    public void setTime(final Timestamp time){
        setValue(time == null ? null : Long.valueOf(CalendarUtils.getNormalizedTimeInMillis(time.getTime())));
    }   

    @Override
    public final void setEditMask(final EditMaskDateTime editMask) {
        super.setEditMask(getEditMaskTime(editMask, getEnvironment()));
    }
    
    protected String getInputMaskString(){
        final String inputMask = getEditMask().getInputMask(getEnvironment());
        return inputMask.replace('_', '0');
    }
        
    @Override
    protected InputFormat createInputFormat() {
        return InputFormat.Factory.maskInputFormat(getInputMaskString());
    }

    @Override
    protected String calcFocusedText(Long value, EditMaskDateTime editMask) {
        if (editMask==null){
            return "";
        }
        final Timestamp timestamp = value==null ? null : getTimestampFromValue(value - timeZoneOffsetMillis);
        final String inputText = editMask.getInputTextForValue(timestamp, getEnvironment());
        return inputText.replace('_', '0');
    }
        
    private static EditMaskDateTime getEditMaskTime(final EditMaskDateTime editMask, final IClientEnvironment environmnet){
        if (editMask==null){
            return null;
        }
        final long timeZomeOffset = CalendarUtils.getTimeZoneOffsetInMillis();
        final Timestamp minValue = editMask.getMinimumTime();
        final Timestamp minTime;
        if (minValue==null || minValue.getTime()<DEFAULT_MIN_VALUE || minValue.getTime()>DEFAULT_MAX_VALUE){
            minTime = new Timestamp(DEFAULT_MIN_VALUE - timeZomeOffset);
        }else{
            minTime = new Timestamp(minValue.getTime() - timeZomeOffset);
        }        
        final Timestamp maxValue = editMask.getMaximumTime();
        final Timestamp maxTime;
        if (maxValue==null || maxValue.getTime()<DEFAULT_MIN_VALUE || maxValue.getTime()>DEFAULT_MAX_VALUE){
            maxTime = new Timestamp(DEFAULT_MAX_VALUE - timeZomeOffset);
        }else{
            maxTime = new Timestamp(maxValue.getTime() - timeZomeOffset);
        }
        return new EditMaskDateTime(editMask.getInputTimeFormat(environmnet), minTime, maxTime);
    }    
    
    private Long getValueFromTimestamp(final Timestamp timestamp) {
        if (timestamp==null){
            return null;
        }
        return Long.valueOf(CalendarUtils.getNormalizedTimeInMillis(timestamp.getTime()));
    }
            
    private Timestamp getTimestampFromValue(final Long value) {
        if (value==null){
            return null;
        }
        return new Timestamp(value.longValue());
    }
    
}
