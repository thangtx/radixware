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
package org.radixware.kernel.utils.traceview.utils;

import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.radixware.kernel.utils.traceview.TraceViewSettings;
import org.radixware.kernel.common.dialogs.datetimepicker.IImageDistributor;

public class TraceViewImageDistributor implements IImageDistributor {

    private Image DATE_CLOCK_IMG = null;
    private Image DATE_CALENDAR_ICON = null;

    private Icon ARROW_LEFT_ACTIVE_1 = null;
    private Icon ARROW_LEFT_ACTIVE_2 = null;
    private Icon ARROW_RIGHT_ACTIVE_1 = null;
    private Icon ARROW_RIGHT_ACTIVE_2 = null;

    private Icon ARROW_LEFT_BLOCKED_1 = null;
    private Icon ARROW_LEFT_BLOCKED_2 = null;
    private Icon ARROW_RIGHT_BLOCKED_1 = null;
    private Icon ARROW_RIGHT_BLOCKED_2 = null;

    public TraceViewImageDistributor() {
        try {
            DATE_CLOCK_IMG = ImageIO.read(getClass().getResource(TraceViewSettings.PATH_TO_RESOURCES + "clock.png")); //150x150
            DATE_CALENDAR_ICON = ImageIO.read(getClass().getResource(TraceViewSettings.PATH_TO_RESOURCES + "calendar.png"));

            ARROW_LEFT_ACTIVE_1 = new ImageIcon(getClass().getResource(TraceViewSettings.PATH_TO_RESOURCES + "left_active_1.png"));  //20x20 
            ARROW_LEFT_ACTIVE_2 = new ImageIcon(getClass().getResource(TraceViewSettings.PATH_TO_RESOURCES + "left_active_2.png"));  //30x30
            ARROW_RIGHT_ACTIVE_1 = new ImageIcon(getClass().getResource(TraceViewSettings.PATH_TO_RESOURCES + "right_active_1.png"));
            ARROW_RIGHT_ACTIVE_2 = new ImageIcon(getClass().getResource(TraceViewSettings.PATH_TO_RESOURCES + "right_active_2.png"));

            ARROW_LEFT_BLOCKED_1 = new ImageIcon(getClass().getResource(TraceViewSettings.PATH_TO_RESOURCES + "left_blocked_1.png"));
            ARROW_LEFT_BLOCKED_2 = new ImageIcon(getClass().getResource(TraceViewSettings.PATH_TO_RESOURCES + "left_blocked_2.png"));
            ARROW_RIGHT_BLOCKED_1 = new ImageIcon(getClass().getResource(TraceViewSettings.PATH_TO_RESOURCES + "right_blocked_1.png"));
            ARROW_RIGHT_BLOCKED_2 = new ImageIcon(getClass().getResource(TraceViewSettings.PATH_TO_RESOURCES + "right_blocked_2.png"));
        } catch (IOException e) {
        }
    }

    @Override
    public Image getClockImg() {
        return DATE_CLOCK_IMG;
    }

    @Override
    public Image getCalendarIcon() {
        return DATE_CALENDAR_ICON;
    }

    @Override
    public Icon getArrowLeftActive_1() {
        return ARROW_LEFT_ACTIVE_1;
    }

    @Override
    public Icon getArrowLeftActive_2() {
        return ARROW_LEFT_ACTIVE_2;
    }

    @Override
    public Icon getArrowRightActive_1() {
        return ARROW_RIGHT_ACTIVE_1;
    }

    @Override
    public Icon getArrowRightActive_2() {
        return ARROW_RIGHT_ACTIVE_2;
    }

    @Override
    public Icon getArrowLeftBlocked_1() {
        return ARROW_LEFT_BLOCKED_1;
    }

    @Override
    public Icon getArrowLeftBlocked_2() {
        return ARROW_LEFT_BLOCKED_2;
    }

    @Override
    public Icon getArrowRightBlocked_1() {
        return ARROW_RIGHT_BLOCKED_1;
    }

    @Override
    public Icon getArrowRightBlocked_2() {
        return ARROW_RIGHT_BLOCKED_2;
    }
}
