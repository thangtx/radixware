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

package org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.palette;

import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.AdsAppObject;
import org.radixware.kernel.common.utils.RadixResourceBundle;
import org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.dialog.EditorDialog.EditorPanel;


class ReportGeneratorItem extends AppItem {

    public static final String CLASS_NAME = AdsAppObject.REPORT_GENERATOR_CLASS_NAME;

    public ReportGeneratorItem() {
        super(CLASS_NAME, Group_.GROUP_APPBLOCKS, RadixResourceBundle.getMessage(ReportGeneratorItem.class, "ReportGenerator"), AdsDefinitionIcon.WORKFLOW.REPORT_GENERATOR);
    }

    @Override
    public EditorPanel<AdsAppObject> getPanel(AdsAppObject node) {
        return null;
    }

    @Override
    public void sync(AdsAppObject node) {
    }
}
