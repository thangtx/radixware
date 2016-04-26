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

package org.radixware.kernel.common.utils;

import java.text.DecimalFormat;
import java.text.Format;
import java.text.MessageFormat;


public class TitleItemFormatter {

    public static String format(String pattern, Object itemVal) {
        /*		if (pattern == null)
         return String.valueOf(itemVal);
         if (pattern.isEmpty())
         return MessageFormat.format("{0}", new Object[] {itemVal});//by ygalkina RADIX-1830
         if (itemVal instanceof Long ){//RADIX-1908
         final MessageFormat mf=new MessageFormat(pattern);
         final Format f[] = mf.getFormats();
         if (f.length==1 && f[0]==null) {
         f[0]=new DecimalFormat("#");
         mf.setFormats(f);
         }
         return  mf.format(new Object[] {itemVal});
         }
         return MessageFormat.format(pattern, new Object[] {itemVal});*/
        return format(pattern, itemVal, null, 0);
    }

    public static String format(String pattern, Object itemVal, String groupSeparator, int groupSize) {
        if (pattern == null) {
            return String.valueOf(itemVal);
        }
        if (pattern.isEmpty()) {
            return MessageFormat.format("{0}", new Object[]{itemVal});//by ygalkina RADIX-1830
        }
        if (itemVal instanceof Long) {//RADIX-1908
            final MessageFormat mf = new MessageFormat(pattern);
            final Format f[] = mf.getFormats();
            if (f.length == 1 && f[0] == null) {
                f[0] = new DecimalFormat("#");
                mf.setFormats(f);
            }
            if ((groupSeparator != null && !groupSeparator.isEmpty())) {
                mf.setFormatByArgumentIndex(0, createFormat(f[0], groupSize, groupSeparator.charAt(0)));
            }
            return mf.format(new Object[]{itemVal});
        }
        final MessageFormat mf = new MessageFormat(pattern);
        final Format f[] = mf.getFormats();
        if (groupSeparator != null && !groupSeparator.isEmpty()) {
            mf.setFormatByArgumentIndex(0, createFormat(f[0], groupSize, groupSeparator.charAt(0)));
        }
        return mf.format(new Object[]{itemVal});
    }

    private static Format createFormat(Format format, int groupSize, char groupSeparator) {
        //int start=messagePattern.indexOf("{");
        //int end= start>=0 ? messagePattern.indexOf("}",start):-1;
        //String pattern= (start>=0 && end>0) ? messagePattern.substring(start+1, end) : "";
        //String[] strArr=pattern.split(",");
        //String numbPattern= strArr.length== 3 ? strArr[strArr.length-1].trim(): "";         

        java.text.DecimalFormat weirdFormatter;
        if (format == null || !(format instanceof DecimalFormat)) {
            weirdFormatter = new DecimalFormat();
        } else {
            weirdFormatter = (DecimalFormat) format;
        }
        java.text.DecimalFormatSymbols unusualSymbols = new java.text.DecimalFormatSymbols();
        unusualSymbols.setGroupingSeparator(groupSeparator);
        weirdFormatter.setDecimalFormatSymbols(unusualSymbols);
        weirdFormatter.setGroupingSize(groupSize);
        weirdFormatter.setGroupingUsed(true);

        return weirdFormatter;
    }
}
