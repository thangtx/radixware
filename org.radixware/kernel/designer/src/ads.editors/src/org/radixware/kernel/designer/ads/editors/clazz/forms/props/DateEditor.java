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


public class DateEditor extends PropertyEditorSupport {

    private Calendar c = Calendar.getInstance();

    public DateEditor(UIPropertySupport property) {
        super(property);
    }

    public UIPropertySupport getUIPropertySupport() {
        return (UIPropertySupport)getSource();
    }

    @Override
    public String getAsText() {
        AdsUIProperty.DateProperty prop = (AdsUIProperty.DateProperty)getValue();
        c.set(prop.year, prop.month - 1, prop.day);
        return new SimpleDateFormat("MM/dd/yyyy").format(c.getTime());
    }

    final private static HashSet<String> d = new HashSet<String>(
            Arrays.asList("date", "minimumDate", "maximumDate")
            );

    final private static HashMap<String, String> d2dt = new HashMap<String, String>();
    static {
        d2dt.put("date", "dateTime");
        d2dt.put("minimumDate", "minimumDateTime");
        d2dt.put("maximumDate", "maximumDateTime");
    }

    @Override
    public void setAsText(String s) {
        AdsUIProperty.DateProperty prop = (AdsUIProperty.DateProperty)getValue();
        try {
            c.setTime(new SimpleDateFormat("MM/dd/yyyy").parse(s));
            prop.year = c.get(Calendar.YEAR);
            prop.month = c.get(Calendar.MONTH) + 1;
            prop.day = c.get(Calendar.DAY_OF_MONTH);
        } catch (ParseException pe) {
            IllegalArgumentException iae = new IllegalArgumentException ("Could not parse date");
            throw iae;
        }
        setValue(prop);
        try {
            ((UIPropertySupport)getSource()).setValue(prop);

            String name = prop.getName();
            if (d.contains(name)) {
                Sheet.Set set = (Sheet.Set)getUIPropertySupport().getValue("set");
                for (Property property : set.getProperties()) {
                    UIPropertySupport sup = (UIPropertySupport)property;
                    AdsUIProperty p = sup.getProp();
                    if (p instanceof AdsUIProperty.DateTimeProperty && p.getName().equals(d2dt.get(name))) {
                        AdsUIProperty.DateTimeProperty dateTime = (AdsUIProperty.DateTimeProperty)p;
                        dateTime.year = prop.year;
                        dateTime.month = prop.month;
                        dateTime.day = prop.day;
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
