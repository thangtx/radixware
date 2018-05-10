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
package org.radixware.kernel.common.dialogs;

import java.awt.Image;
import javax.swing.Icon;
import org.radixware.kernel.common.dialogs.datetimepicker.IImageDistributor;
import org.radixware.kernel.common.resources.icons.RadixIcon;

public class RadixImageDistributor implements IImageDistributor {

    private class DatePickerIcon extends RadixIcon {

        public DatePickerIcon(String uri) {
            super(uri);
        }
    }

    private final DatePickerIcon DATE_CLOCK_IMG;
    private final DatePickerIcon DATE_CALENDAR_ICON;

    private final DatePickerIcon ARROW_LEFT_ACTIVE_1;
    private final DatePickerIcon ARROW_LEFT_ACTIVE_2;
    private final DatePickerIcon ARROW_RIGHT_ACTIVE_1;
    private final DatePickerIcon ARROW_RIGHT_ACTIVE_2;

    private final DatePickerIcon ARROW_LEFT_BLOCKED_1;
    private final DatePickerIcon ARROW_LEFT_BLOCKED_2;
    private final DatePickerIcon ARROW_RIGHT_BLOCKED_1;
    private final DatePickerIcon ARROW_RIGHT_BLOCKED_2;

    public RadixImageDistributor() {
        DATE_CLOCK_IMG = new DatePickerIcon("dialog/clock.svg");
        DATE_CALENDAR_ICON = new DatePickerIcon("dialog/calendar.svg");

        ARROW_LEFT_ACTIVE_1 = new DatePickerIcon("arrow/left_active_1.svg");
        ARROW_LEFT_ACTIVE_2 = new DatePickerIcon("arrow/left_active_2.svg");
        ARROW_RIGHT_ACTIVE_1 = new DatePickerIcon("arrow/right_active_1.svg");
        ARROW_RIGHT_ACTIVE_2 = new DatePickerIcon("arrow/right_active_2.svg");

        ARROW_LEFT_BLOCKED_1 = new DatePickerIcon("arrow/left_blocked_1.svg");
        ARROW_LEFT_BLOCKED_2 = new DatePickerIcon("arrow/left_blocked_2.svg");
        ARROW_RIGHT_BLOCKED_1 = new DatePickerIcon("arrow/right_blocked_1.svg");
        ARROW_RIGHT_BLOCKED_2 = new DatePickerIcon("arrow/right_blocked_2.svg");
    }

    @Override
    public Image getClockImg() {
        return DATE_CLOCK_IMG.getImage(150, 150);
    }

    @Override
    public Image getCalendarIcon() {
        return DATE_CALENDAR_ICON.getImage();
    }

    @Override
    public Icon getArrowLeftActive_1() {
        return ARROW_LEFT_ACTIVE_1.getIcon(20, 20);
    }

    @Override
    public Icon getArrowLeftActive_2() {
        return ARROW_LEFT_ACTIVE_2.getIcon(30, 30);
    }

    @Override
    public Icon getArrowRightActive_1() {
        return ARROW_RIGHT_ACTIVE_1.getIcon(20, 20);
    }

    @Override
    public Icon getArrowRightActive_2() {
        return ARROW_RIGHT_ACTIVE_2.getIcon(30, 30);
    }

    @Override
    public Icon getArrowLeftBlocked_1() {
        return ARROW_LEFT_BLOCKED_1.getIcon(20, 20);
    }

    @Override
    public Icon getArrowLeftBlocked_2() {
        return ARROW_LEFT_BLOCKED_2.getIcon(30, 30);
    }

    @Override
    public Icon getArrowRightBlocked_1() {
        return ARROW_RIGHT_BLOCKED_1.getIcon(20, 20);
    }

    @Override
    public Icon getArrowRightBlocked_2() {
        return ARROW_RIGHT_BLOCKED_2.getIcon(30, 30);
    }
}
