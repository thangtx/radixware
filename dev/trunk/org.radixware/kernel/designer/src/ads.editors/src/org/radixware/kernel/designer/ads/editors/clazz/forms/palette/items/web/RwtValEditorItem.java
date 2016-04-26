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

package org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.web;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.ui.AdsMetaInfo;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtWidgetDef;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.DrawUtil;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.Group;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.Item;


public class RwtValEditorItem extends Item {

    public static final RwtValEditorItem STR_EDITOR = new RwtValEditorItem("String value editor", AdsMetaInfo.RWT_VAL_STR_EDITOR);
    public static final RwtValEditorItem BOOL_EDITOR = new RwtValEditorItem("Boolean value editor", AdsMetaInfo.RWT_VAL_BOOL_EDITOR);
    public static final RwtValEditorItem BIN_EDITOR = new RwtValEditorItem("Binary value editor", AdsMetaInfo.RWT_VAL_BIN_EDITOR);
    public static final RwtValEditorItem ENUM_EDITOR = new RwtValEditorItem("Enumeration editor", AdsMetaInfo.RWT_VAL_ENUM_EDITOR);
    public static final RwtValEditorItem INT_EDITOR = new RwtValEditorItem("Int value editor", AdsMetaInfo.RWT_VAL_INT_EDITOR);
    public static final RwtValEditorItem LIST_EDITOR = new RwtValEditorItem("List editor", AdsMetaInfo.RWT_VAL_LIST_EDITOR);
    public static final RwtValEditorItem NUM_EDITOR = new RwtValEditorItem("Num value editor", AdsMetaInfo.RWT_VAL_NUM_EDITOR);
    public static final RwtValEditorItem REF_EDITOR = new RwtValEditorItem("Reference value editor", AdsMetaInfo.RWT_VAL_REF_EDITOR);
    public static final RwtValEditorItem TIME_EDITOR = new RwtValEditorItem("Time value editor", AdsMetaInfo.RWT_VAL_TIME_EDITOR);
    public static final RwtValEditorItem TIME_INTERVAL_EDITOR = new RwtValEditorItem("Time interval value editor", AdsMetaInfo.RWT_VAL_TIME_INTERVAL_EDITOR);
    public static final RwtValEditorItem DATE_TIME_EDITOR = new RwtValEditorItem("Date/Time value editor", AdsMetaInfo.RWT_VAL_DATE_TIME_EDITOR);
    public static final RwtValEditorItem FILE_PATH_EDITOR = new RwtValEditorItem("File path value editor", AdsMetaInfo.RWT_VAL_FILE_PATH_EDITOR);
    public static final RwtValEditorItem ARR_EDITOR = new RwtValEditorItem("Array editor", AdsMetaInfo.RWT_VAL_ARR_EDITOR);

    public RwtValEditorItem(String title, String className) {
        super(Group.GROUP_WEB_RADIX_WIDGETS, title, className);
    }

    @Override
    public RadixObject createObjectUI(RadixObject context) {
        AdsRwtWidgetDef widget = (AdsRwtWidgetDef) super.createObjectUI(context);
        return widget;
    }

    @Override
    public void paintBackground(Graphics2D graphics, Rectangle rect, RadixObject node) {
        if (AdsMetaInfo.RWT_VAL_BOOL_EDITOR.equals(getClazz())) {
            DrawUtil.drawPlainRoundRect(graphics, new Rectangle(rect.x, rect.y, rect.height, rect.height), DrawUtil.COLOR_DARK, 1, Color.WHITE);
        } else {
            DrawUtil.drawPlainRoundRect(graphics, rect, DrawUtil.COLOR_DARK, 1, Color.WHITE);
        }
    }

    @Override
    public void paintBorder(Graphics2D graphics, Rectangle rect, RadixObject node) {
        super.paintBorder(graphics, rect, node);
    }

    @Override
    public void paintWidget(Graphics2D graphics, Rectangle rect, RadixObject node) {
       
        super.paintWidget(graphics, rect, node);
    }
}
