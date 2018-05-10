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
import java.awt.image.BufferedImage;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public class DefaultImageDistributor implements IImageDistributor {
    
    private final Image defaultImage_150x150;
    private final Icon defaultIcon_30x30;
    private final Icon defaultIcon_20x20;
    
    public DefaultImageDistributor() {
        defaultImage_150x150 = new BufferedImage(150, 150, BufferedImage.TYPE_INT_ARGB);
        defaultIcon_30x30 = new ImageIcon(new BufferedImage(30, 30, BufferedImage.TYPE_INT_ARGB));
        defaultIcon_20x20 = new ImageIcon(new BufferedImage(20, 20, BufferedImage.TYPE_INT_ARGB));
    }

    @Override
    public Image getClockImg() {
        return defaultImage_150x150;
    }

    @Override
    public Image getCalendarIcon() {
        return defaultImage_150x150;
    }

    @Override
    public Icon getArrowLeftActive_1() {
        return defaultIcon_20x20;
    }

    @Override
    public Icon getArrowLeftActive_2() {
        return defaultIcon_30x30;
    }

    @Override
    public Icon getArrowRightActive_1() {
        return defaultIcon_20x20;
    }

    @Override
    public Icon getArrowRightActive_2() {
        return defaultIcon_30x30;
    }

    @Override
    public Icon getArrowLeftBlocked_1() {
        return defaultIcon_20x20;
    }

    @Override
    public Icon getArrowLeftBlocked_2() {
        return defaultIcon_30x30;
    }

    @Override
    public Icon getArrowRightBlocked_1() {
        return defaultIcon_20x20;
    }

    @Override
    public Icon getArrowRightBlocked_2() {
        return defaultIcon_30x30;
    }
    
}
