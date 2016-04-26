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
package org.radixware.kernel.designer.common.general.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public final class RequestProcessor {

    public static Future<?> submit(final Runnable r) {
        return org.radixware.kernel.common.utils.RequestProcessor.submit(r);
    }

    public static <T> Future<T> submit(final Callable<T> r) {
        return org.radixware.kernel.common.utils.RequestProcessor.submit(r);
    }
}
