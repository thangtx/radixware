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

package org.radixware.kernel.starter.log;

import java.lang.reflect.Method;
import org.apache.commons.logging.Log;


public class RadixLogUtils {

    public static void event(Log log, Object message, Throwable t) {
        if (log == null) {
            return;
        }
        try {
            final Method eventMethod = log.getClass().getMethod("event", Object.class, Throwable.class);
            eventMethod.invoke(log, message, t);
        } catch (Exception ex) {
            log.info(message, ex);
        }
    }
}
