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

import java.text.MessageFormat;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.exceptions.RadixError;


public class RadixResourceBundle {

    private static final EIsoLanguage locale = EIsoLanguage.getForValue(System.getProperty("user.language"));

    public static final EIsoLanguage getLocale() {
        return locale;
    }

    public static final String getMessage(Class locator, String propertyName) {
        return getMessage(locator, propertyName, null);
    }

    public static final String getMessage(Class locator, String propertyName, Object argument) {
        return getMessage(locator, propertyName, new Object[]{argument});
    }

    public static final String getMessage(Class locator, String propertyName, Object[] arguments) {
        ResourceBundle bundle = PropertyResourceBundle.getBundle(locator.getPackage().getName() + ".Bundle", Locale.getDefault(), locator.getClassLoader());
        String result = bundle.getString(propertyName);
        if (result == null) {
            throw new RadixError("No resource bundle item for key \"" + propertyName + "\"");
        }
        if (arguments != null && arguments.length > 0) {
            try {
                return MessageFormat.format(result, arguments);
            } catch (IllegalArgumentException e) {
                throw new RadixError("Error on resource string formatting: " + locator.getName() + " " + propertyName);
            }
        } else {
            return result;
        }
    }
}
