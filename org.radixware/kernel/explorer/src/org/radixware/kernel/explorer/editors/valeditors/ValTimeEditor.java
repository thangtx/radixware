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

import java.sql.Timestamp;

import com.trolltech.qt.gui.QWidget;
import java.util.Calendar;
import java.util.Locale;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.EditMaskDateTime;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.client.utils.CalendarUtils;

public class ValTimeEditor extends AbstractDateTimeEditor<Long> {
    
    private static final long DEFAULT_MIN_VALUE = 0;
    private static final long DEFAULT_MAX_VALUE = CalendarUtils.getMillisForUnit(Calendar.DAY_OF_MONTH)-1;
    
    private final long timeZoneOffsetMillis = CalendarUtils.getTimeZoneOffsetInMillis();
    
    public ValTimeEditor(final IClientEnvironment environment, final QWidget parent, final EditMaskDateTime editMaskDateTime,
            final boolean mandatory, final boolean readOnly) {
        super(environment, parent, getEditMaskTime(editMaskDateTime, environment), mandatory, readOnly);        
    }

    public ValTimeEditor(final IClientEnvironment environment, final QWidget parent){
        this(environment,parent,new EditMaskDateTime(),true,false);
    }
    
    public void setValueAsTimestamp(final Timestamp value){
        setValue(value == null ? null : Long.valueOf(CalendarUtils.getNormalizedTimeInMillis(value.getTime())));
    }
    
    public Timestamp getValueAsTimestamp(){
        final Long value = getValue();
        return value == null ? null : new Timestamp(value.longValue());
    }
    
    @Override
    protected String getInputText(final Long value){
        final Timestamp timestamp = getTimestampFromValue(value - timeZoneOffsetMillis);
        final EditMaskDateTime mask = (EditMaskDateTime)getEditMask();
        final String inputText = mask.getInputTextForValue(timestamp, getEnvironment());
        return inputText.replace('_', '0');
    }

    @Override
    protected String getInputMask() {
        return super.getInputMask().replace('_', '0');
    }
        
    @Override
    public void setEditMask(final EditMask editMask) {
        super.setEditMask( getEditMaskTime( (EditMaskDateTime)editMask, getEnvironment()) );        
    }

    @Override
    protected void onTextEdited(final String newText) {
        super.onTextEdited(getLineEdit().displayText());
    }    
    
    @Override
    protected ValidationResult calcValidationResult(final Long value) {
        return super.calcValidationResult(value==null ? null : value-timeZoneOffsetMillis);
    }    

    @Override
    protected String getStringToShow(final Object value) {
        if (value instanceof Long){
            final Timestamp timestamp = getTimestampFromValue((Long)value - timeZoneOffsetMillis);
            return super.getStringToShow(timestamp);
        }else{
            return super.getStringToShow(value);
        }
    }   
       
    @Override
    protected String valueAsStr(final Long value) {
        return value==null ? null : String.valueOf(value);
    }

    @Override
    protected Long stringAsValue(final String stringVal) {        
        return stringVal==null ? null : Long.parseLong(stringVal);
    }

    @Override
    protected Timestamp getTimestampFromValue(final Long value) {
        if (value==null){
            return null;
        }
        return new Timestamp(value.longValue());
    }

    @Override
    protected Long getValueFromTimestamp(final Timestamp timestamp) {
        if (timestamp==null){
            return null;
        }
        return Long.valueOf(CalendarUtils.getNormalizedTimeInMillis(timestamp.getTime()));
    }
            
    private static EditMaskDateTime getEditMaskTime(final EditMaskDateTime editMask, final IClientEnvironment environment){
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
        return new EditMaskDateTime(editMask.getInputTimeFormat(environment), minTime, maxTime);
    }    
}