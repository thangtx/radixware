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

/*
 * 11/20/11 10:11 AM
 */
package org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items;

import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ads.ui.AdsMetaInfo;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.Group;
import org.radixware.kernel.designer.ads.editors.clazz.forms.widget.drawing.IWidgetPainter;
import org.radixware.kernel.designer.ads.editors.clazz.forms.widget.drawing.WidgetPropertyCollector;

public class ValNumEditorItem extends ValEditorItem {

    public static final ValNumEditorItem DEFAULT = new ValNumEditorItem();


    protected ValNumEditorItem(Group group, String title, String clazz, IWidgetPainter painter, WidgetPropertyCollector propertyCollector) {
        super(group, title, clazz, painter, propertyCollector);
    }

    public ValNumEditorItem() {
        this(Group.GROUP_INPUT_WIDGETS, NbBundle.getMessage(ValIntEditorItem.class, "Val_Num_Editor"), AdsMetaInfo.VAL_NUM_EDITOR_CLASS,
                new ValEditorPainter(), new ValEditorPropertyCollector());
        getPainter().setOwnerEditor(this);
    }
}
