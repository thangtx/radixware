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

package org.radixware.kernel.explorer.dialogs;

import com.trolltech.qt.core.QDate;
import com.trolltech.qt.core.QDateTime;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.Qt.DayOfWeek;
import com.trolltech.qt.gui.QComboBox;
import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QDialogButtonBox;
import com.trolltech.qt.gui.QLayout;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QWidget;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Locale;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.dialogs.IDateTimeDialog;
import org.radixware.kernel.common.client.enums.ETextOptionsMarker;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.EditMaskDateTime;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.enums.EDateTimeStyle;
import org.radixware.kernel.common.exceptions.WrongFormatException;
import org.radixware.kernel.explorer.editors.valeditors.ValTimeEditor;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.text.DefaultTextOptionsProvider;
import org.radixware.kernel.explorer.utils.ValueConverter;
import org.radixware.kernel.explorer.widgets.propeditors.IDisplayStringProvider;

public class DateTimeDialog extends QtDialog implements IDateTimeDialog {
    
    private static class Icons extends ClientIcon.CommonOperations {
        
        private Icons(final String fileName) {
            super(fileName, true);
        }
        
        public static final ClientIcon CURRENT_TIME = new Icons("classpath:images/current_time.svg");
    }
    
    private static class TimeDisplayProvider implements IDisplayStringProvider{                
        
        private final Locale locale;
        
        public TimeDisplayProvider(final Locale locale){
            this.locale = locale;
        }
        
        @Override
        public String format(final EditMask mask, final Object value, final String defaultDisplayString) {
            if (((EditMaskDateTime)mask).halfDayFieldPresent(locale)){
                return defaultDisplayString.substring(0, defaultDisplayString.length()-3);
            }else{
                return defaultDisplayString;
            }
        }
    }
    
    private final static Long NOON_TIME_MILLIS = Long.valueOf(12l * 60l * 60l * 1000l);

    private final Ui_DateTimeDialog uiCreator = new Ui_DateTimeDialog();
    private final IClientEnvironment environment;    
    private final ValTimeEditor timeEditor;
    private final QComboBox amPmEditor = new QComboBox(this);
    private final static Timestamp DEFAULT_MIN_DATE = new Timestamp(getTimeInMillis(1, 0, 1));
    private final static Timestamp DEFAULT_MAX_DATE = new Timestamp(getTimeInMillis(9999, 11, 31));
    private boolean noValue;
    private Timestamp value;    

    @SuppressWarnings("LeakingThisInConstructor")
    public DateTimeDialog(final IClientEnvironment environment, final QWidget parent) {
        super(parent);
        setAttribute(Qt.WidgetAttribute.WA_DeleteOnClose, true);
        this.environment = environment;
        uiCreator.setupUi(this);
        uiCreator.gridLayout.setSizeConstraint(QLayout.SizeConstraint.SetFixedSize);
        final DayOfWeek firstDay = calendarDay2QtDay(Calendar.getInstance().getFirstDayOfWeek());
        uiCreator.calendarWidget.setFirstDayOfWeek(firstDay);        
        final EditMaskDateTime editMaskTime = new EditMaskDateTime(EDateTimeStyle.NONE, EDateTimeStyle.DEFAULT, null, null);        
        timeEditor = new ValTimeEditor(environment, uiCreator.gbTime, editMaskTime, true, false){
            @Override
            protected String getInputText(final Long value){
                final String inputText = super.getInputText(value);
                if (((EditMaskDateTime)getEditMask()).halfDayFieldPresent(getEnvironment().getLocale())){
                    return inputText.substring(0, inputText.length()-3);
                }else{
                    return inputText;
                }
            }

            @Override
            protected ValidationResult validateInputText(final String text) {
                if (((EditMaskDateTime)getEditMask()).halfDayFieldPresent(getEnvironment().getLocale())){
                    return super.validateInputText(text+" "+amPmEditor.currentText());
                }else{
                    return super.validateInputText(text);
                }
            }    

            @Override
            protected Timestamp getValueFromInputText(final String inputText) throws WrongFormatException {
                if (((EditMaskDateTime)getEditMask()).halfDayFieldPresent(getEnvironment().getLocale())){
                    return super.getValueFromInputText(inputText+" "+amPmEditor.currentText());
                }else{
                    return super.getValueFromInputText(inputText);
                }
            }            
            
            @Override
            protected String getInputMask() {
                final String inputMask = super.getInputMask().replace(" AM", "");
                return inputMask.replace("AM", "");
            }
        };
        timeEditor.setSizePolicy(QSizePolicy.Policy.MinimumExpanding, QSizePolicy.Policy.Fixed);        
        timeEditor.valueChanged.connect(this,"updateAmPm()");
        uiCreator.gbTime.layout().addWidget(timeEditor);
        
        amPmEditor.addItem("AM");
        amPmEditor.addItem("PM");
        amPmEditor.currentIndexChanged.connect(this,"onAmPmChanged()");                
        amPmEditor.setSizePolicy(QSizePolicy.Policy.Fixed, QSizePolicy.Policy.Fixed);
        DefaultTextOptionsProvider.getInstance().getOptions(ETextOptionsMarker.EDITOR).applyTo(amPmEditor);
        uiCreator.gbTime.layout().addWidget(amPmEditor);
        setupButtons();
    }
    
    private void setupButtons(){
        uiCreator.buttonBox.button(QDialogButtonBox.StandardButton.Ok).setIcon(ExplorerIcon.getQIcon(ClientIcon.Dialog.BUTTON_OK));
        uiCreator.buttonBox.button(QDialogButtonBox.StandardButton.Cancel).setIcon(ExplorerIcon.getQIcon(ClientIcon.Dialog.BUTTON_CANCEL));        
        uiCreator.pushButtonNow.setIcon(ExplorerIcon.getQIcon(Icons.CURRENT_TIME));
        uiCreator.pushButtonReset.setIcon(ExplorerIcon.getQIcon(Icons.UNDO));
        uiCreator.pushButtonClear.setIcon(ExplorerIcon.getQIcon(Icons.CLEAR));
    }

    @SuppressWarnings({"unused","PMD.MethodNamingConventions"})
    //slot for ui.pushButtonNow.clicked
    private void on_pushButtonNow_clicked() {
        final Timestamp currentTime = environment.getCurrentServerTime();
        uiCreator.calendarWidget.setSelectedDate(ValueConverter.timestamp2QtDate(currentTime));
        timeEditor.setValueAsTimestamp(currentTime);
    }

    @SuppressWarnings({"unused","PMD.MethodNamingConventions"})
    //slot for ui.pushButtonReset
    private void on_pushButtonReset_clicked() {
        setCurrentDateTime(value);
    }

    @SuppressWarnings({"unused","PMD.MethodNamingConventions"})
    //slot for ui.pushButtonClear.clicked
    private void on_pushButtonClear_clicked() {
        noValue = true;
        accept();
    }

    @SuppressWarnings({"unused","PMD.MethodNamingConventions"})
    //slot for ui.calendarWidget.currentPageChanged
    private void on_calendarWidget_currentPageChanged(final int year, final int month) {
        QDate date = uiCreator.calendarWidget.selectedDate();
        date = date.addYears(year - date.year());
        date = date.addMonths(month - date.month());
        uiCreator.calendarWidget.setSelectedDate(date);
    }
        
    private void updateAmPm(){
        if (!amPmEditor.isHidden()){
            final Long value = timeEditor.getValue();
            if (value!=null && timeEditor.hasAcceptableInput()){
                noValue = false;
                amPmEditor.blockSignals(true);
                try{
                    amPmEditor.setCurrentIndex(value>=NOON_TIME_MILLIS ? 1 : 0);
                }finally{
                    amPmEditor.blockSignals(false);
                }
            }
        }
    }
    
    @SuppressWarnings("unused")
    private void onAmPmChanged(){
        final Long value = timeEditor.getValue();
        if (value!=null && timeEditor.hasAcceptableInput()){
            final boolean isPm = amPmEditor.currentIndex()==1;
            timeEditor.blockSignals(true);
            try{
                if (value<NOON_TIME_MILLIS && isPm){
                    timeEditor.setValue(value+NOON_TIME_MILLIS);
                }else if (value>=NOON_TIME_MILLIS && !isPm){
                    timeEditor.setValue(value-NOON_TIME_MILLIS);
                }
            }finally{
                timeEditor.blockSignals(false);
            }
        }
    }    

    @Override
    public void done(final int result) {
        if (result==QDialog.DialogCode.Accepted.value()){
            if (uiCreator.gbTime.isVisible() && !timeEditor.checkInput()){
                return;
            }
            finishEdit();
        }else{
            setCurrentDateTime(value);
        }
        super.done(result);
    }
    
    private void finishEdit(){
        if (noValue) {
            value = null;
        } else {
            final Timestamp date = ValueConverter.qtDate2Timestamp(uiCreator.calendarWidget.selectedDate());
            if (timeEditor.getValue()==null){
                value = date;
            }else{
                value = new Timestamp(date.getTime() + timeEditor.getValue());
            }
        }        
    }

    public void setTimeDisplayFormat(final String format) {
        final EditMaskDateTime mask = new EditMaskDateTime(format, null, null);
        amPmEditor.setVisible(mask.halfDayFieldPresent(environment.getLocale()));        
        timeEditor.setDisplayStringProvider(new TimeDisplayProvider(environment.getLocale()));
        timeEditor.setEditMask(mask);        
        updateAmPm();
    }

    public void setTimeFieldVisible(final boolean visible) {
        uiCreator.gbTime.setVisible(visible);
    }

    public void setMandatory(final boolean isMandatory) {
        uiCreator.pushButtonClear.setVisible(!isMandatory);
    }

    @Deprecated
    public void setDateRange(final QDate minDate, final QDate maxDate) {
        setDateRange(ValueConverter.qtDate2Timestamp(minDate), ValueConverter.qtDate2Timestamp(maxDate));        
    }
    
    @Deprecated
    public void setDateTime(final QDateTime dateTime) {
        setCurrentDateTime(ValueConverter.qtTime2Timestamp(dateTime));
    }

    @Deprecated
    public QDateTime getDateTime() {         
        return ValueConverter.timestamp2QtTime(getCurrentDateTime());
    }

    private DayOfWeek calendarDay2QtDay(final int dayOfWeek) {
        switch (dayOfWeek) {
            case Calendar.SUNDAY:
                return DayOfWeek.Sunday;
            case Calendar.MONDAY:
                return DayOfWeek.Monday;
            case Calendar.TUESDAY:
                return DayOfWeek.Tuesday;
            case Calendar.WEDNESDAY:
                return DayOfWeek.Wednesday;
            case Calendar.THURSDAY:
                return DayOfWeek.Thursday;
            case Calendar.FRIDAY:
                return DayOfWeek.Friday;
            case Calendar.SATURDAY:
                return DayOfWeek.Saturday;
            default:
                throw new IllegalArgumentException("invalid day of week constant: " + dayOfWeek);
        }
    }
    
    private static long getTimeInMillis(final int year, final int month, final int day) {
        final Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(year, month, day);
        return cal.getTimeInMillis();
    }    
    
    //Implementation of IDateTimeDialog

    @Override
    public Timestamp getCurrentDateTime() {
        return value;        
    }

    @Override
    public Timestamp getMaxDate() {
        return ValueConverter.qtDate2Timestamp(uiCreator.calendarWidget.maximumDate());
    }

    @Override
    public Timestamp getMinDate() {
        return ValueConverter.qtDate2Timestamp(uiCreator.calendarWidget.minimumDate());
    }

    @Override
    public void setCurrentDateTime(final Timestamp currentDateTime) {
        if (currentDateTime != null) {
            noValue = false;
            value = new Timestamp(currentDateTime.getTime());
            uiCreator.calendarWidget.setSelectedDate(ValueConverter.timestamp2QtDate(value));
            timeEditor.setValueAsTimestamp(value);
        }else{
            timeEditor.setValue(0l);
        }
        uiCreator.pushButtonReset.setDisabled(currentDateTime == null);        
        updateAmPm();
    }

    @Override
    public void setDateRange(final Timestamp minDate, final Timestamp maxDate) {        
        final QDate qtMinDate;
        if (minDate==null || minDate.before(DEFAULT_MIN_DATE) || minDate.after(DEFAULT_MAX_DATE)){
            qtMinDate = ValueConverter.timestamp2QtDate(DEFAULT_MIN_DATE);
        }else{
            qtMinDate = ValueConverter.timestamp2QtDate(minDate);
        }
        
        final QDate qtMaxDate;
        if (maxDate==null || maxDate.before(DEFAULT_MIN_DATE) || maxDate.after(DEFAULT_MAX_DATE)){
            qtMaxDate = ValueConverter.timestamp2QtDate(DEFAULT_MAX_DATE);
        }else{
            qtMaxDate = ValueConverter.timestamp2QtDate(maxDate);
        }        
        uiCreator.calendarWidget.setDateRange(qtMinDate, qtMaxDate);
    }

    @Override
    public void setDateRange(final Calendar minDate, final Calendar maxDate) {
        setDateRange(new Timestamp(minDate.getTimeInMillis()), new Timestamp(maxDate.getTimeInMillis()));
    }

    @Override
    public void setMaxDate(final Timestamp maxDate) {
        uiCreator.calendarWidget.setMaximumDate(ValueConverter.timestamp2QtDate(maxDate));
    }

    @Override
    public void setMaxDate(final Calendar calendar) {
        setMaxDate(new Timestamp(calendar.getTimeInMillis()));
    }

    @Override
    public void setMinDate(final Timestamp minDate) {
        uiCreator.calendarWidget.setMinimumDate(ValueConverter.timestamp2QtDate(minDate));
    }

    @Override
    public void setMinDate(final Calendar calendar) {
        setMinDate(new Timestamp(calendar.getTimeInMillis()));
    }        
}
