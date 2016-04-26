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

package org.radixware.kernel.designer.ads.editors.clazz.simple;

import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.dds.DdsAccessPartitionFamilyDef;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;


public class AccessAreaChildDialog extends ModalDisplayer {
    @Override
    public void apply() {
        final AccessAreaChildPanel panel = (AccessAreaChildPanel) getComponent();
        panel.apply();
    }
    @Override
    public boolean showModal() {
//        this.getDialog().addPropertyChangeListener(new PropertyChangeListener() {
//
//            @Override
//            public void propertyChange(PropertyChangeEvent evt) {
//                if (evt.getPropertyName().equals("preferredSize2"))
//                {
//                    AccessAreaChildPanel panel = (AccessAreaChildPanel) getComponent();
//                    panel.reBuildTree();
//                }
//            }
//        });
        return super.showModal();
    }
    public AccessAreaChildDialog(AdsEntityClassDef clazz, String sSearchAreaName, DdsAccessPartitionFamilyDef currClassApf) {
        super(new AccessAreaChildPanel(clazz, sSearchAreaName, currClassApf));
    }
}
