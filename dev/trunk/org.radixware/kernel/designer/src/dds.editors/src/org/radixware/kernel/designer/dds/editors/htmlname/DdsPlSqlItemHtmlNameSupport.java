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

package org.radixware.kernel.designer.dds.editors.htmlname;

import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObject.RenameEvent;
import org.radixware.kernel.common.defs.dds.DdsPlSqlObjectDef;
import org.radixware.kernel.common.defs.dds.DdsPlSqlObjectItemDef;
import org.radixware.kernel.designer.common.general.displaying.HtmlNameSupport;


public class DdsPlSqlItemHtmlNameSupport extends HtmlNameSupport {

    public DdsPlSqlItemHtmlNameSupport(DdsPlSqlObjectItemDef plSqlObjectItem) {
        super(plSqlObjectItem);
    }

    public DdsPlSqlObjectItemDef getPlSqlObjectItem() {
        return (DdsPlSqlObjectItemDef) getRadixObject();
    }
    private RadixObject.RenameListener ownerRenameListener = null;

    @Override
    public String getEditorDisplayName() {
        DdsPlSqlObjectItemDef item = getPlSqlObjectItem();
        DdsPlSqlObjectDef plSqlObject = item.getOwnerPlSqlObject();
        if (plSqlObject != null) {
            if (ownerRenameListener == null) {
                ownerRenameListener = new RadixObject.RenameListener() {

                    @Override
                    public void onEvent(RenameEvent e) {
                        fireChanged();
                    }
                };
                plSqlObject.addRenameListener(ownerRenameListener);
            }

            return plSqlObject.getName() + '.' + item.getName();
        } else {
            return getDisplayName();
        }
    }
}
