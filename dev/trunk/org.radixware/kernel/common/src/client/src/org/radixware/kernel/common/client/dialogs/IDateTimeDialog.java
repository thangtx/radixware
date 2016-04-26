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

package org.radixware.kernel.common.client.dialogs;

import java.sql.Timestamp;
import java.util.Calendar;
import org.radixware.kernel.common.client.views.IDialog;


public interface IDateTimeDialog extends IDialog{

    Timestamp getCurrentDateTime();

    Timestamp getMaxDate();

    Timestamp getMinDate();

    void setCurrentDateTime(final Timestamp currentDateTime);

    void setDateRange(final Timestamp minDate, final Timestamp maxDate);

    void setDateRange(final Calendar minDate, final Calendar maxDate);

    void setMaxDate(final Timestamp maxDate);

    void setMaxDate(final Calendar calendar);

    void setMinDate(final Timestamp minDate);

    void setMinDate(final Calendar calendar);
    
}
