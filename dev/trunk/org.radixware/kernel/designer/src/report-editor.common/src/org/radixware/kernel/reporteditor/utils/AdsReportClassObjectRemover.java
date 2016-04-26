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

package org.radixware.kernel.reporteditor.utils;

import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.designer.common.annotations.registrators.ObjectRemoverFactoryRegistration;
import org.radixware.kernel.designer.common.general.nodes.operation.IObjectRemoverFactory;
import org.radixware.kernel.designer.common.general.nodes.operation.ObjectRemover;


public class AdsReportClassObjectRemover extends ObjectRemover {

    private final AdsReportClassDef report;

    public AdsReportClassObjectRemover(final AdsReportClassDef report) {
        this.report = report;
    }

    @Override
    public void removeObject() {
//        ReportsModule ownerModule = (ReportsModule) report.getModule();
//        switch (ownerModule.getStatus()) {
//            case 0:
//                //under construction;
//                //check available versions and give a choice
//                //remove currently developed version
//                //move entire report to recycle bin
//                //completely remove report
//                break;
//            case 1:
//                //production;
//                //check available versions and give a choice
//                //move entire report to recycle bin
//                //completely remove report
//                break;
//            case 2:
//                //trash
//                //check available versions and give a choice
//                //completely remove report
//                break;
//        }
    }

    @ObjectRemoverFactoryRegistration
    public static final class Factory implements IObjectRemoverFactory<AdsReportClassDef> {

        @Override
        public ObjectRemover newInstance(final AdsReportClassDef obj) {
            return new AdsReportClassObjectRemover(obj);
        }
    }
}
