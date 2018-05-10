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
package org.radixware.kernel.common.dialogs.datetimepicker;

import java.awt.Image;
import javax.swing.Icon;

public interface IImageDistributor {

    public Image getClockImg(); //150

    public Image getCalendarIcon(); 

    public Icon getArrowLeftActive_1(); //20

    public Icon getArrowLeftActive_2(); //30

    public Icon getArrowRightActive_1();

    public Icon getArrowRightActive_2();

    public Icon getArrowLeftBlocked_1();

    public Icon getArrowLeftBlocked_2();

    public Icon getArrowRightBlocked_1();

    public Icon getArrowRightBlocked_2();

}
