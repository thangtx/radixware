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

package org.radixware.wps.rwt;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.mask.EditMaskStr;
import org.radixware.kernel.common.client.meta.mask.validators.IInputValidator;
import org.radixware.kernel.common.client.meta.mask.validators.ValidatorsFactory;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.common.html.Div;
import org.radixware.wps.views.editors.valeditors.ValStrEditorController;


public class CalendarWidget extends UIObject {

    public interface DateChangeListener {

        public void dateChanged(Date oldDate, Date newDate);
    }

    private static enum EAttrName {

        MIN_VALUE("minvalue"),
        MAX_VALUE("maxvalue"),
        CURRENT_VALUE("currentvalue"),
        SELECTED_DATES("selectedDates");
        private final String attrName;

        private EAttrName(final String attrName) {
            this.attrName = attrName;
        }

        public String getAttrName() {
            return attrName;
        }
    }
    private final static long DEFAULT_MIN_DATE = getTimeInMillis(1586, 0, 1);
    private final static long DEFAULT_MAX_DATE = getTimeInMillis(9999, 11, 31);
    private final ValStrEditorController yearEditor;
    private final IClientEnvironment environment;
    private List<DateChangeListener> listeners;
    private Date currentDate, minDate, maxDate;
    private final List<Date> selectedDates = new LinkedList<Date>();

    public CalendarWidget(final IClientEnvironment environment) {
        super(new Div());
        this.environment = environment;
        html.layout("$RWT.calendarWidget.layout");
        yearEditor = new ValStrEditorController(environment);
        final IInputValidator validator =
                ValidatorsFactory.createLongValidator(Long.valueOf(1), Long.valueOf(9999));
        yearEditor.setEditMask(new EditMaskStr(validator, false, 4, false));
        yearEditor.setMandatory(true);
        final UIObject yearEditorWidget = ((UIObject) yearEditor.getValEditor());
        html.add(yearEditorWidget.getHtml());
        yearEditorWidget.setVisible(false);
        final int firstDay =
                Calendar.getInstance(new Locale(environment.getLanguage().getValue())).getFirstDayOfWeek() - 1;
        html.setAttr("firstDay", firstDay);
        setMinDate(new Date(DEFAULT_MIN_DATE));
        setMaxDate(new Date(DEFAULT_MAX_DATE));
        setCurrentDate(environment.getCurrentServerTime());
    }

    public final void addDateChangeListener(final DateChangeListener listener) {
        if (listener != null && (listeners == null || !listeners.contains(listener))) {
            if (listeners == null) {
                listeners = new LinkedList<DateChangeListener>();
            }
            listeners.add(listener);
        }
    }

    public final void removeDateChangeListener(final DateChangeListener listener) {
        if (listeners != null) {
            listeners.remove(listener);
        }
    }

    private void invokeDateChange(final Date oldDate) {
        if (listeners != null) {
            for (DateChangeListener listener : listeners) {
                listener.dateChanged(oldDate, currentDate);
            }
        }
    }

    public final Date getCurrentDate() {
        return new Date(currentDate.getTime());
    }

    public final void setCurrentDate(final Date currentDate) {
        if (!Utils.equals(currentDate, this.currentDate)
                && currentDate.getTime() <= maxDate.getTime() && currentDate.getTime() >= minDate.getTime()) {
            final Date oldDate = this.currentDate;
            this.currentDate = getNormalizedDate(currentDate);
            invokeDateChange(oldDate);
            html.setAttr(EAttrName.CURRENT_VALUE.getAttrName(), String.valueOf(getNormalizedTime(currentDate)));
        }
    }

    public final void setCurrentDate(final Calendar calendar) {
        setCurrentDate(new Date(calendar.getTimeInMillis()));
    }

    public final void setCurrentDate(final int year, final int month, final int day) {
        setCurrentDate(new Date(getTimeInMillis(year, month, day)));
    }

    public final Date getMaxDate() {
        return new Date(maxDate.getTime());
    }

    public final void setMaxDate(final Date maxDate) {
        if (maxDate != null && maxDate.getTime() > DEFAULT_MAX_DATE) {
            throw new IllegalArgumentException("Date can not be more than 12/31/9999");
        }
        if (maxDate == null) {
            this.maxDate = new Date(DEFAULT_MAX_DATE);
            html.setAttr(EAttrName.MAX_VALUE.getAttrName(), String.valueOf(getNormalizedTime(this.maxDate)));
        } else if (!Utils.equals(maxDate, this.maxDate)) {
            this.maxDate = getNormalizedDate(maxDate);
            html.setAttr(EAttrName.MAX_VALUE.getAttrName(), String.valueOf(getNormalizedTime(maxDate)));
        }
    }

    public final void setMaxDate(final Calendar calendar) {
        setMaxDate(new Date(calendar.getTimeInMillis()));
    }

    public final void setMaxDate(final int year, final int month, final int day) {
        setMaxDate(new Date(getTimeInMillis(year, month, day)));
    }

    public final Date getMinDate() {
        return new Date(minDate.getTime());
    }

    public final void setMinDate(final Date minDate) {
        if (minDate != null && minDate.getTime() < DEFAULT_MIN_DATE) {
            throw new IllegalArgumentException("Date can not be less than 01/01/1586");
        }
        if (minDate == null) {
            this.minDate = new Date(DEFAULT_MIN_DATE);
            html.setAttr(EAttrName.MAX_VALUE.getAttrName(), String.valueOf(getNormalizedTime(this.minDate)));
        } else if (!Utils.equals(minDate, this.minDate)) {
            this.minDate = getNormalizedDate(minDate);
            html.setAttr(EAttrName.MIN_VALUE.getAttrName(), String.valueOf(getNormalizedTime(minDate)));
        }
    }

    public final void setMinDate(final Calendar calendar) {
        setMinDate(new Date(calendar.getTimeInMillis()));
    }

    public final void setMinDate(final int year, final int month, final int day) {
        setMinDate(new Date(getTimeInMillis(year, month, day)));
    }

    public final void setDateRange(final Date minDate, final Date maxDate) {
        setMinDate(minDate);
        setMaxDate(maxDate);
    }

    public final void setDateRange(final Calendar minDate, final Calendar maxDate) {
        setMinDate(minDate);
        setMaxDate(maxDate);
    }

    @Override
    public void processAction(final String actionName, final String actionParam) {
        if ("change".equals(actionName) && actionParam != null && !actionParam.isEmpty()) {
            try {
                final long milliseconds = Long.parseLong(actionParam);//convert local time to utc time
                setCurrentDate(new Date(milliseconds - getTimeZoneOffsetMillis(milliseconds)));
            } catch (NumberFormatException exception) {
            }
        } else {
            super.processAction(actionName, actionParam);
        }
    }

    @Override
    protected String[] clientScriptsRequired() {
        final EIsoLanguage language = environment.getLanguage();
        if (language == EIsoLanguage.ENGLISH) {
            return super.clientScriptsRequired();
        }
        return new String[]{"org/radixware/wps/rwt/jquery.ui.datepicker-" + language.getValue() + ".js"};
    }

    public final void selectDate(final Date date) {
        if (date != null && !isDateSelected(date)) {
            selectedDates.add(new Date(getNormalizedTimeUTC(date)));
            onChangeSelectedDates();
        }
    }

    public final void selectDate(final Calendar calendar) {
        selectDate(new Date(calendar.getTimeInMillis()));
    }

    public final void selectDate(final int year, final int month, final int day) {
        selectedDates.add(new Date(getLocalTimeInMillis(year, month, day)));
        onChangeSelectedDates();
    }

    public final void selectDates(final Collection<Date> dates) {
        if (dates != null) {
            for (Date date : dates) {
                if (date != null && !isDateSelected(date)) {
                    selectedDates.add(new Date(getNormalizedTimeUTC(date)));
                }
            }
            onChangeSelectedDates();
        }
    }

    public final void removeSelection(final Date date) {
        final int index = findSelectedDate(date);
        if (index > -1) {
            selectedDates.remove(index);
            onChangeSelectedDates();
        }
    }

    public final void removeSelection(final Calendar calendar) {
        removeSelection(new Date(calendar.getTimeInMillis()));
    }

    public final void removeSelection(final int year, final int month, final int day) {
        removeSelection(new Date(getTimeInMillis(year, month, day)));
    }

    public final void removeSelection(final Collection<Date> dates) {
        if (dates != null) {
            for (Date date : dates) {
                final int index = findSelectedDate(date);
                if (index > -1) {
                    selectedDates.remove(index);
                }
            }
            onChangeSelectedDates();
        }
    }

    public final boolean isDateSelected(final Date t) {
        return findSelectedDate(t) > -1;
    }

    public final boolean isDateSelected(final Calendar calendar) {
        return isDateSelected(new Date(calendar.getTimeInMillis()));
    }

    public final boolean isDateSelected(final int year, final int month, final int day) {
        return isDateSelected(new Date(getTimeInMillis(year, month, day)));
    }

    public final void clearSelections() {
        selectedDates.clear();
        onChangeSelectedDates();
    }

    private int findSelectedDate(final Date t) {
        final long time = getNormalizedTime(t);
        int index = 0;
        for (Date date : selectedDates) {
            if (date.getTime() == time) {
                return index;
            }
            index++;
        }
        return -1;
    }

    private void onChangeSelectedDates() {
        if (selectedDates.isEmpty()) {
            html.setAttr(EAttrName.SELECTED_DATES.getAttrName(), null);
        } else {
            final StringBuilder jsSelectedDatesArrayBuilder = new StringBuilder();
            jsSelectedDatesArrayBuilder.append("[");
            boolean firstDate = true;
            for (Date t : selectedDates) {
                if (!firstDate) {
                    jsSelectedDatesArrayBuilder.append(",");
                } else {
                    firstDate = false;
                }
                jsSelectedDatesArrayBuilder.append(String.valueOf(t.getTime()));
            }
            jsSelectedDatesArrayBuilder.append("]");
            html.setAttr(EAttrName.SELECTED_DATES.getAttrName(), jsSelectedDatesArrayBuilder.toString());
        }
    }

    private static long getTimeZoneOffsetMillis(final long value) {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(value);
        return cal.get(Calendar.DST_OFFSET) + cal.get(Calendar.ZONE_OFFSET);
    }

    private static long getLocalTimeInMillis(final int year, final int month, final int day) {
        final Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(year, month, day);
        final long utcTime = cal.getTimeInMillis();
        return utcTime + getTimeZoneOffsetMillis(utcTime);
    }

    private static long getTimeInMillis(final int year, final int month, final int day) {
        final Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(year, month, day);
        return cal.getTimeInMillis();
    }

    private static long getLocalTimeInMillis(final Calendar calendar) {        
        final long utcTime = getUTCInMillis(calendar);
        return utcTime + getTimeZoneOffsetMillis(utcTime);
    }
    private static long getUTCInMillis(final Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();        
    }


    private static Date getNormalizedDate(Date d) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(d.getTime());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Date(calendar.getTimeInMillis());
    }

    private static long getNormalizedTime(Date d) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(d.getTime());
        return getLocalTimeInMillis(calendar);
    }
    private static long getNormalizedTimeUTC(Date d) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(d.getTime());
        return getUTCInMillis(calendar);
    }
}