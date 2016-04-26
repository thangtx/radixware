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

package org.radixware.kernel.designer.ads.editors.clazz.forms.props;

import java.util.Calendar;
import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import org.openide.nodes.Node.Property;
import org.openide.nodes.Sheet;
import org.openide.util.Exceptions;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;


public class DateTimeEditor extends PropertyEditorSupport {

    private Calendar c = Calendar.getInstance();

    public DateTimeEditor(UIPropertySupport property) {
        super(property);
    }

    public UIPropertySupport getUIPropertySupport() {
        return (UIPropertySupport)getSource();
    }

    @Override
    public String getAsText() {
        AdsUIProperty.DateTimeProperty prop = (AdsUIProperty.DateTimeProperty)getValue();
        c.set(prop.year, prop.month - 1, prop.day, prop.hour, prop.minute, prop.second);
        return new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(c.getTime());
    }

    final private static HashSet<String> dt = new HashSet<String>(
            Arrays.asList("dateTime", "minimumDateTime", "maximumDateTime")
            );
    
    final private static HashMap<String, String> dt2d = new HashMap<String, String>();
    static {
        dt2d.put("dateTime", "date");
        dt2d.put("minimumDateTime", "minimumDate");
        dt2d.put("maximumDateTime", "maximumDate");
    }
    final private static HashMap<String, String> dt2t = new HashMap<String, String>();
    static {
        dt2t.put("dateTime", "time");
        dt2t.put("minimumDateTime", "minimumTime");
        dt2t.put("maximumDateTime", "maximumTime");
    }

    @Override
    public void setAsText(String s) {
        AdsUIProperty.DateTimeProperty prop = (AdsUIProperty.DateTimeProperty)getValue();
        try {
            c.setTime(new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse(s));
            prop.year = c.get(Calendar.YEAR);
            prop.month = c.get(Calendar.MONTH) + 1;
            prop.day = c.get(Calendar.DAY_OF_MONTH);
            prop.hour = c.get(Calendar.HOUR_OF_DAY);
            prop.minute = c.get(Calendar.MINUTE);
            prop.second = c.get(Calendar.SECOND);
        } catch (ParseException pe) {
            IllegalArgumentException iae = new IllegalArgumentException ("Could not parse date");
            throw iae;
        }
        setValue(prop);
        try {
            ((UIPropertySupport)getSource()).setValue(prop);

            String name = prop.getName();
            if (dt.contains(name)) {
                Sheet.Set set = (Sheet.Set)getUIPropertySupport().getValue("set");
                for (Property property : set.getProperties()) {
                    UIPropertySupport sup = (UIPropertySupport)property;
                    AdsUIProperty p = sup.getProp();
                    if (p instanceof AdsUIProperty.DateProperty && p.getName().equals(dt2d.get(name))) {
                        AdsUIProperty.DateProperty date = (AdsUIProperty.DateProperty)p;
                        date.year = prop.year;
                        date.month = prop.month;
                        date.day = prop.day;
                        sup.setValue(date);
                        set.put(sup); // update
                    }
                    if (p instanceof AdsUIProperty.TimeProperty && p.getName().equals(dt2t.get(name))) {
                        AdsUIProperty.TimeProperty time = (AdsUIProperty.TimeProperty)p;
                        time.hour = prop.hour;
                        time.minute = prop.minute;
                        time.second = prop.second;
                        sup.setValue(time);
                        set.put(sup); // update
                    }
                }
            }

        } catch (Throwable ex) {
            Exceptions.printStackTrace(ex);
        }
    }
}
