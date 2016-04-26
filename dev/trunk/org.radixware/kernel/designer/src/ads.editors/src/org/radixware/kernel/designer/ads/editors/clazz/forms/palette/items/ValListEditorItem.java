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
 * 11/10/11 12:53 PM
 */
package org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items;

import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ads.ui.AdsMetaInfo;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.Group;

public class ValListEditorItem extends ValEditorItem {

    public static final ValListEditorItem DEFAULT = new ValListEditorItem();

   

    public ValListEditorItem() {
        super(Group.GROUP_INPUT_WIDGETS, NbBundle.getMessage(ValListEditorItem.class, "Val_List_Editor"), AdsMetaInfo.VAL_LIST_EDITOR_CLASS,
                new ValEditorPainter(), new ValEditorPropertyCollector());

        getPainter().setOwnerEditor(this);
    }

    @Override
    public final ValEditorPainter getPainter() {
        return super.getPainter();
    }
}
