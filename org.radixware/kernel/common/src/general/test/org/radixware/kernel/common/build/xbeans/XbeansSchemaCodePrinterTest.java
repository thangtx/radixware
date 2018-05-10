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
package org.radixware.kernel.common.build.xbeans;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import org.apache.xmlbeans.GDate;
import org.junit.Test;
import static org.junit.Assert.*;
import org.radixware.schemas.commondef.ChangeLogItem;
import org.radixware.schemas.eas.Property;

/**
 *
 * @author npopov
 */
public class XbeansSchemaCodePrinterTest {
    
    public XbeansSchemaCodePrinterTest() {
    }
    
    @Test
    public void testDateTimeWithTimezone() {
        final String tz = "-08:00";
        Calendar dateWithTimezone = Calendar.getInstance(TimeZone.getTimeZone("GMT" + tz));
        final GDate testDate = new GDate(dateWithTimezone);
        
        ChangeLogItem xItem = ChangeLogItem.Factory.newInstance();
        xItem.setDateWithTimezone(testDate);
        assertTrue("Xml element must contain time zone info", xItem.xmlText().contains(tz));
        assertEquals("Getter with timezone should return setted value", xItem.getDateWithTimezone(), testDate);
        
        Property.ArrDateTime xItems = Property.ArrDateTime.Factory.newInstance();
        List<GDate> items = xItems.getItemWithTimezoneList();
        List<Timestamp> itemsWithOutTimezone = xItems.getItemList();
        assertEquals("Size must be zero", items.size(), 0);
        assertEquals("Size must be zero", itemsWithOutTimezone.size(), 0);
        
        items.add(testDate);
        System.out.println(xItems);
        assertEquals("Size must be one", items.size(), 1);
        assertEquals("Size must be one", itemsWithOutTimezone.size(), 1);
        assertTrue("Xml element must contain time zone info", xItems.xmlText().contains(tz));
        assertEquals("Getter with timezone should return setted value", items.get(0), testDate);
        
        final String tz2 = "-06:00";
        Calendar dateWithTimezone2 = Calendar.getInstance(TimeZone.getTimeZone("GMT" + tz2));
        final GDate testDate2 = new GDate(dateWithTimezone2);
        items.set(0, testDate2);
        assertEquals("Size must be one", items.size(), 1);
        assertEquals("Size must be one", itemsWithOutTimezone.size(), 1);
        assertTrue("Xml element must contain time zone info", xItems.xmlText().contains(tz2));
        assertEquals("Getter with timezone should return setted value", items.get(0), testDate2);
        
        items.add(testDate);
        System.out.println(xItems);
        assertEquals("Size must be two", items.size(), 2);
        assertEquals("Size must be two", itemsWithOutTimezone.size(), 2);
        assertEquals("Getter with timezone should return setted value", items.get(1), testDate);
        
        items.remove(0);
        items.remove(0);
        assertEquals("Size must be zero", items.size(), 0);
        assertEquals("Size must be zero", itemsWithOutTimezone.size(), 0);
        assertFalse("Xml element must not contain time zone info", xItems.xmlText().contains(tz2));
    }
    
}
