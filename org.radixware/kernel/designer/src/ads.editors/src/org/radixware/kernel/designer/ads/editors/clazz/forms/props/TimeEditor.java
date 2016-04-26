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


public class TimeEditor extends PropertyEditorSupport {

    private Calendar c = Calendar.getInstance();

    public TimeEditor(UIPropertySupport property) {
        super(property);
    }

    public UIPropertySupport getUIPropertySupport() {
        return (UIPropertySupport)getSource();
    }

    @Override
    public String getAsText() {
        AdsUIProperty.TimeProperty prop = (AdsUIProperty.TimeProperty)getValue();
        c.set(0, 0, 0, prop.hour, prop.minute, prop.second);
        return new SimpleDateFormat("HH:mm:ss").format(c.getTime());
    }

    final private static HashSet<String> t = new HashSet<String>(
            Arrays.asList("time", "minimumTime", "maximumTime")
            );

    final private static HashMap<String, String> t2dt = new HashMap<String, String>();
    static {
        t2dt.put("time", "dateTime");
        t2dt.put("minimumTime", "minimumDateTime");
        t2dt.put("maximumTime", "maximumDateTime");
    }

    @Override
    public void setAsText(String s) {
        AdsUIProperty.TimeProperty prop = (AdsUIProperty.TimeProperty)getValue();
        try {
            c.setTime(new SimpleDateFormat("HH:mm:ss").parse(s));
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
            if (t.contains(name)) {
                Sheet.Set set = (Sheet.Set)getUIPropertySupport().getValue("set");
                for (Property property : set.getProperties()) {
                    UIPropertySupport sup = (UIPropertySupport)property;
                    AdsUIProperty p = sup.getProp();
                    if (p instanceof AdsUIProperty.DateTimeProperty && p.getName().equals(t2dt.get(name))) {
                        AdsUIProperty.DateTimeProperty dateTime = (AdsUIProperty.DateTimeProperty)p;
                        dateTime.hour = prop.hour;
                        dateTime.minute = prop.minute;
                        dateTime.second = prop.second;
                        sup.setValue(dateTime);
                        set.put(sup); // update
                    }
                }
            }

        } catch (Throwable ex) {
            Exceptions.printStackTrace(ex);
        }
    }
}
