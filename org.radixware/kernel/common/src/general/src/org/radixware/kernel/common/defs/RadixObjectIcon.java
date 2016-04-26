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

package org.radixware.kernel.common.defs;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.resources.icons.RadixIcon;


public final class RadixObjectIcon extends RadixIcon {

    public static final RadixObjectIcon LAYER = new RadixObjectIcon("object/layer.svg");
    public static final RadixObjectIcon KERNEL_MODULE = new RadixObjectIcon("kernel/module.svg");
    public static final RadixObjectIcon KERNEL_SEGMENT = new RadixObjectIcon("kernel/segment.svg");
    public static final RadixObjectIcon BRANCH = new RadixObjectIcon("object/branch.svg");
    public static final RadixObjectIcon SQML = new RadixObjectIcon("object/sqml.svg");
    public static final RadixObjectIcon ENUM = new RadixObjectIcon("object/enum.svg");
    public static final RadixObjectIcon ARR_ENUM = new RadixObjectIcon("object/arr_enum.svg");
    public static final RadixObjectIcon ENUM_ITEM = new RadixObjectIcon("object/enum_item.svg");
    public static final RadixObjectIcon ENUM_CHAR = new RadixObjectIcon("object/enum_char.svg");
    public static final RadixObjectIcon ENUM_ITEM_CHAR = new RadixObjectIcon("object/enum_item_char.svg");
    public static final RadixObjectIcon ENUM_INT = new RadixObjectIcon("object/enum_int.svg");
    public static final RadixObjectIcon ENUM_ITEM_INT = new RadixObjectIcon("object/enum_item_int.svg");
    public static final RadixObjectIcon ENUM_STR = new RadixObjectIcon("object/enum_str.svg");
    public static final RadixObjectIcon ENUM_ITEM_STR = new RadixObjectIcon("object/enum_item_str.svg");
    public static final RadixObjectIcon UNKNOWN = new RadixObjectIcon("object/unknown.svg");

    public static class JAVA {

        public static final RadixObjectIcon JAVA = new RadixObjectIcon("java/java.svg");
        public static final RadixObjectIcon PACKAGE = new RadixObjectIcon("java/package.svg");
        public static final RadixObjectIcon CLASS = new RadixObjectIcon("java/class.svg");
        public static final RadixObjectIcon DEPRECATE = new RadixObjectIcon("java/deprecate.svg");
        public static final RadixObjectIcon UNDEPRECATE = new RadixObjectIcon("java/undeprecate.svg");
    }

    public static RadixIcon getForValType(EValType valType) {
        return RadixEnumIcon.getForValue(valType);
    }

    public static RadixIcon getForSeverity(EEventSeverity severity) {
        return RadixEnumIcon.getForValue(severity);
    }

    private RadixObjectIcon(String uri) {
        super(uri);
    }
    private static final ImageObserver IMAGE_OBSERVER = new ImageObserver() {
        @Override
        public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
            return true;
        }
    };

    public static Image annotatePublished(Image image) {
        final BufferedImage buf = new BufferedImage(22, 16, BufferedImage.TYPE_INT_ARGB);
        //BufferedImage buf = ((Graphics2D) image.getGraphics()).getDeviceConfiguration().createCompatibleImage(22, 16);
        Graphics g = buf.getGraphics();
        //Image tmp = image.getScaledInstance(16, 16, Image.SCALE_SMOOTH);
        g.drawImage(image, 0, 0, 16, 16, IMAGE_OBSERVER);
        g.setColor(Color.BLUE);
        g.drawLine(19, 2, 19, 7);
        g.drawLine(17, 4, 19, 2);
        g.drawLine(21, 4, 19, 2);
        return buf;
    }
}
