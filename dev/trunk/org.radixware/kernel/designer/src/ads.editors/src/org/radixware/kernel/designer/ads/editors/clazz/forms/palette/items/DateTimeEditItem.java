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
package org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items;

import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.*;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.ui.AdsMetaInfo;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty.DateProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.defs.ads.ui.enums.EAlignment;
import org.radixware.kernel.common.utils.PropertyStore;

public class DateTimeEditItem extends Item {

    public static final DateTimeEditItem DEFAULT = new DateTimeEditItem();

    

    public DateTimeEditItem() {
        super(Group.GROUP_INPUT_WIDGETS, NbBundle.getMessage(DateTimeEditItem.class, "Date_Time_Edit"), AdsMetaInfo.DATE_TIME_EDIT_CLASS);
    }

    @Override
    public void paintBackground(Graphics2D gr, Rectangle r, RadixObject node) {
        AdsUIProperty.BooleanProperty calendarPopup_prop = (AdsUIProperty.BooleanProperty) AdsUIUtil.getUiProperty(node, "calendarPopup");
        paintBackground(gr, r, node, calendarPopup_prop.value);
    }

    public void paintBackground(Graphics2D gr, Rectangle r, RadixObject node, boolean isCalendarPopup) {

        if (isCalendarPopup) {
            ComboBoxItem.DEFAULT.getPainter().paintBackground(gr, r, true);
        } else {
            SpinBoxItem.DEFAULT.paintBackground(gr, r, node);
        }
    }

    @Override
    public void paintWidget(Graphics2D gr, Rectangle r, RadixObject node) {
        AdsUIProperty.BooleanProperty calendarPopup_prop = (AdsUIProperty.BooleanProperty) AdsUIUtil.getUiProperty(node, "calendarPopup");
        paint(gr, r, node, calendarPopup_prop.value);
    }

    public void paint(Graphics2D gr, Rectangle r, RadixObject node, boolean isСalendarPopup) {
        AdsUIProperty.DateTimeProperty dateTime = (DateProperty.DateTimeProperty) AdsUIUtil.getUiProperty(node, "dateTime");
        AdsUIProperty.DateTimeProperty minDateTime = (DateProperty.DateTimeProperty) AdsUIUtil.getUiProperty(node, "minimumDateTime");
        boolean isCurValMin = false;
        if ((dateTime.year <= minDateTime.year) && (dateTime.month <= minDateTime.month) && (dateTime.day <= minDateTime.day)
                && (dateTime.hour <= minDateTime.hour) && (dateTime.minute <= minDateTime.minute) && (dateTime.second <= minDateTime.second)) {
            isCurValMin = true;
        }

        AdsUIProperty.DateTimeProperty maxDateTime = (DateProperty.DateTimeProperty) AdsUIUtil.getUiProperty(node, "maximumDateTime");
        boolean isCurValMax = false;
        if ((dateTime.year >= maxDateTime.year) && (dateTime.month >= maxDateTime.month) && (dateTime.day >= maxDateTime.day)
                && (dateTime.hour >= maxDateTime.hour) && (dateTime.minute >= maxDateTime.minute) && (dateTime.second >= maxDateTime.second)) {
            isCurValMax = true;
        }
        String str = "";
        if (dateTime != null) {
            AdsUIProperty.StringProperty displayFormat_prop = (AdsUIProperty.StringProperty) AdsUIUtil.getUiProperty(node, "displayFormat");
            displayFormat = displayFormat_prop.value;// getTextById(node, displayFormat_prop.stringId);
            str = dataTimeToStr(dateTime);
        }
        paint(gr, r, node, str, isCurValMin, isCurValMax, isСalendarPopup);
    }

    private void paint(Graphics2D gr, Rectangle r, RadixObject node, String str, boolean isCurValMin, boolean isCurValMax, boolean isCalendarPopup) {

        if (isCalendarPopup) {
            PropertyStore props = new PropertyStore();

            props.set("label", str);
            props.set("image", null);

            AdsUIProperty.BooleanProperty enabled = (AdsUIProperty.BooleanProperty) AdsUIUtil.getUiProperty(node, "enabled");
            assert enabled != null;
            props.set("enabled", enabled.value);

            props.set("editable", true);

            AdsUIProperty.RectProperty geometry = (AdsUIProperty.RectProperty) AdsMetaInfo.getPropByName(AdsUIUtil.getQtClassName(node), "geometry", node);
            assert enabled != null;
            props.set("widgetHeight", geometry.height);

            props.set("iconSize", null);

            EAlignment halignment = EAlignment.AlignLeft;
            EAlignment valignment = EAlignment.AlignVCenter;

            props.set("halignment", halignment);
            props.set("valignment", valignment);

            ComboBoxItem.DEFAULT.getPainter().paintWidget(gr, r, props);
        } else {
            SpinBoxItem.DEFAULT.paint(gr, r, node, str, isCurValMin, isCurValMax);
        }
    }
    private String displayFormat;

    private String dataTimeToStr(AdsUIProperty.DateTimeProperty dateTime) {
        String str = "";
        while (displayFormat.length() > 0) {
            if (isNextSimbDay(displayFormat)) {
                str = dayToStr(dateTime.day, str);
            } else if (isNextSimbMonth(displayFormat)) {
                str = monthToStr(dateTime.month, str);
            } else if (isNextSimbYear(displayFormat)) {
                str = yearToStr(dateTime.year, str);
            } else if (isNextSimbHour(displayFormat)) {
                /*str+=dateTime.hour;
                 while((displayFormat.length()>0)&&isNextSimbHour(displayFormat)){
                 if(displayFormat.length()>=1)
                 displayFormat=displayFormat.substring(1, displayFormat.length());
                 }*/
                str = timeItemToStr(dateTime.hour, str, 0);
            } else if (isNextSimbMinute(displayFormat)) {
                /* str+=dateTime.minute;
                 while((displayFormat.length()>0)&&isNextSimbMinute(displayFormat)){
                 if(displayFormat.length()>=1)
                 displayFormat=displayFormat.substring(1, displayFormat.length());
                 }*/
                str = timeItemToStr(dateTime.minute, str, 1);
            } else if (isNextSimbSecond(displayFormat)) {
                /* str+=dateTime.timeItemToStr(int day,String str,int timeItem){;
                 while((displayFormat.length()>0)&&isNextSimbSecond(displayFormat)){
                 if(displayFormat.length()>=1)
                 displayFormat=displayFormat.substring(1, displayFormat.length());
                 }*/
                str = timeItemToStr(dateTime.second, str, 2);
            } else {
                while ((displayFormat.length() > 0) && (!isNextSimbDay(displayFormat)) && (!isNextSimbMonth(displayFormat)) && (!isNextSimbYear(displayFormat))
                        && (!isNextSimbHour(displayFormat)) && (!isNextSimbMinute(displayFormat)) && (!isNextSimbSecond(displayFormat))) {
                    str += displayFormat.charAt(0);
                    if (displayFormat.length() >= 1) {
                        displayFormat = displayFormat.substring(1, displayFormat.length());
                    }
                }
            }
        }
        return str;
    }

    private String dayToStr(int day, String str) {
        int format = 0;
        while ((displayFormat.length() > 0) && isNextSimbDay(displayFormat)) {
            if (displayFormat.length() >= 1) {
                displayFormat = displayFormat.substring(1, displayFormat.length());
                format++;
            }
            if (format == 4) {
                str += DateFormat.getDay(day, format);
                format = 0;
            }
        }
        if (format != 0) {
            str += DateFormat.getDay(day, format);
        }
        return str;
    }

    private String timeItemToStr(int day, String str, int timeItem) {
        int format = 0;
        while ((displayFormat.length() > 0) && isNextSimbTimeItem(timeItem)) {
            if (displayFormat.length() >= 1) {
                displayFormat = displayFormat.substring(1, displayFormat.length());
                format++;
            }
            if (format == 2) {
                str += TimeFormat.getTimeItem(day, format);
                format = 0;
            }
        }
        if (format != 0) {
            str += TimeFormat.getTimeItem(day, format);
        }
        return str;
    }

    private boolean isNextSimbTimeItem(int timeItem) {
        switch (timeItem) {
            case 0:
                return isNextSimbHour(displayFormat);
            case 1:
                return isNextSimbMinute(displayFormat);
            case 2:
                return isNextSimbSecond(displayFormat);
            default:
                return false;
        }

    }

    private String monthToStr(int month, String str) {
        int format = 0;
        while ((displayFormat.length() > 0) && isNextSimbMonth(displayFormat)) {
            if (displayFormat.length() >= 1) {
                displayFormat = displayFormat.substring(1, displayFormat.length());
                format++;
            }
            if (format == 4) {
                str += DateFormat.getMonth(month, format);
                format = 0;
            }
        }
        if (format != 0) {
            str += DateFormat.getMonth(month, format);
        }
        return str;
    }

    private String yearToStr(int year, String str) {
        int format = 0;
        while ((displayFormat.length() > 1) && isNextSimbYear(displayFormat)) {
            if (displayFormat.length() >= 2) {
                displayFormat = displayFormat.substring(2, displayFormat.length());
                format++;
            }
            if (format == 2) {
                str += DateFormat.getYear(year, format);
                format = 0;
            }
        }
        if (format != 0) {
            str += DateFormat.getYear(year, format);
        }
        return str;
    }

    private boolean isNextSimbDay(String displayFormat) {
        return (displayFormat.charAt(0) == 'd');
    }

    private boolean isNextSimbMonth(String displayFormat) {
        return (displayFormat.charAt(0) == 'M');
    }

    private boolean isNextSimbYear(String displayFormat) {
        return (displayFormat.charAt(0) == 'y') && (displayFormat.charAt(1) == 'y');//(displayFormat.indexOf("y")!=-1)||(displayFormat.indexOf("Y")!=-1);
    }

    private boolean isNextSimbHour(String displayFormat) {
        return (displayFormat.charAt(0) == 'h') || (displayFormat.charAt(0) == 'H');//(displayFormat.indexOf("h")!=-1)||(displayFormat.indexOf("H")!=-1);
    }

    private boolean isNextSimbMinute(String displayFormat) {
        return (displayFormat.charAt(0) == 'm');//(displayFormat.indexOf("m")!=-1);
    }

    private boolean isNextSimbSecond(String displayFormat) {
        return (displayFormat.charAt(0) == 's');//(displayFormat.indexOf("s")!=-1)||(displayFormat.indexOf("S")!=-1);
    }

    public static class TimeFormat {

        public static String getTimeItem(int day, int dayFormat) {
            String res = "";
            if (dayFormat == 1) {
                res += day;
            } else if (dayFormat == 2) {
                res += day;
                if (res.length() == 1) {
                    res = "0" + res;
                }
            }
            return res;
        }
    }

    public static class DateFormat {

        public static String getDay(int day, int dayFormat) {
            String res = "";
            if (dayFormat == 1) {
                res += day;
            } else if (dayFormat == 2) {
                res += day;
                if (res.length() == 1) {
                    res = "0" + res;
                }
            } else if (dayFormat == 3) {
                res = shortDayName(day);
            } else if (dayFormat == 4) {
                res = longDayName(day);
            }
            return res;
        }

        public static String shortDayName(int day) {
            switch (day) {
                case 1:
                    return NbBundle.getMessage(DateTimeEditItem.class, "Monday_Short");
                case 2:
                    return NbBundle.getMessage(DateTimeEditItem.class, "Tuesday_Short");
                case 3:
                    return NbBundle.getMessage(DateTimeEditItem.class, "Wednesday_Short");
                case 4:
                    return NbBundle.getMessage(DateTimeEditItem.class, "Thursday_Short");
                case 5:
                    return NbBundle.getMessage(DateTimeEditItem.class, "Friday_Short");
                case 6:
                    return NbBundle.getMessage(DateTimeEditItem.class, "Saturday_Short");
                case 7:
                    return NbBundle.getMessage(DateTimeEditItem.class, "Sunday_Short");
            }
            return "";
        }

        public static String longDayName(int day) {
            switch (day) {
                case 1:
                    return NbBundle.getMessage(DateTimeEditItem.class, "Monday");
                case 2:
                    return NbBundle.getMessage(DateTimeEditItem.class, "Tuesday");
                case 3:
                    return NbBundle.getMessage(DateTimeEditItem.class, "Wednesday");
                case 4:
                    return NbBundle.getMessage(DateTimeEditItem.class, "Thursday");
                case 5:
                    return NbBundle.getMessage(DateTimeEditItem.class, "Friday");
                case 6:
                    return NbBundle.getMessage(DateTimeEditItem.class, "Saturday");
                case 7:
                    return NbBundle.getMessage(DateTimeEditItem.class, "Sunday");
            }
            return "";
        }

        public static String getMonth(int month, int monthFormat) {
            String res = "";
            if (monthFormat == 1) {
                res += month;
            } else if (monthFormat == 2) {
                res += month;
                if (res.length() == 1) {
                    res = "0" + res;
                }
            } else if (monthFormat == 3) {
                res = shortMonthName(month);
            } else if (monthFormat == 4) {
                res = longMonthName(month);
            }
            return res;
        }

        public static String shortMonthName(int day) {
            switch (day) {
                case 1:
                    return NbBundle.getMessage(DateTimeEditItem.class, "January_Short");
                case 2:
                    return NbBundle.getMessage(DateTimeEditItem.class, "February_Short");
                case 3:
                    return NbBundle.getMessage(DateTimeEditItem.class, "March_Short");
                case 4:
                    return NbBundle.getMessage(DateTimeEditItem.class, "April_Short");
                case 5:
                    return NbBundle.getMessage(DateTimeEditItem.class, "May_Short");
                case 6:
                    return NbBundle.getMessage(DateTimeEditItem.class, "June_Short");
                case 7:
                    return NbBundle.getMessage(DateTimeEditItem.class, "July_Short");
                case 8:
                    return NbBundle.getMessage(DateTimeEditItem.class, "August_Short");
                case 9:
                    return NbBundle.getMessage(DateTimeEditItem.class, "September_Short");
                case 10:
                    return NbBundle.getMessage(DateTimeEditItem.class, "October_Short");
                case 11:
                    return NbBundle.getMessage(DateTimeEditItem.class, "November_Short");
                case 12:
                    return NbBundle.getMessage(DateTimeEditItem.class, "December_Short");
            }
            return "";
        }

        public static String longMonthName(int day) {
            switch (day) {
                case 1:
                    return NbBundle.getMessage(DateTimeEditItem.class, "January");
                case 2:
                    return NbBundle.getMessage(DateTimeEditItem.class, "February");
                case 3:
                    return NbBundle.getMessage(DateTimeEditItem.class, "March");
                case 4:
                    return NbBundle.getMessage(DateTimeEditItem.class, "April");
                case 5:
                    return NbBundle.getMessage(DateTimeEditItem.class, "May");
                case 6:
                    return NbBundle.getMessage(DateTimeEditItem.class, "June");
                case 7:
                    return NbBundle.getMessage(DateTimeEditItem.class, "July");
                case 8:
                    return NbBundle.getMessage(DateTimeEditItem.class, "August");
                case 9:
                    return NbBundle.getMessage(DateTimeEditItem.class, "September");
                case 10:
                    return NbBundle.getMessage(DateTimeEditItem.class, "October");
                case 11:
                    return NbBundle.getMessage(DateTimeEditItem.class, "November");
                case 12:
                    return NbBundle.getMessage(DateTimeEditItem.class, "December");
            }
            return "";
        }

        public static String getYear(int year, int yearFormat) {
            String res = "";
            if (yearFormat == 1) {
                String str_year = Integer.valueOf(year).toString();
                res += str_year.substring(2, str_year.length());
            } else if (yearFormat == 2) {
                res += year;
            }
            return res;
        }
    }
}
