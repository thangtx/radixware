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

package org.radixware.kernel.designer.tree.ads.nodes.defs.clazz;

import java.util.List;
import javax.swing.Action;
import org.openide.nodes.Children;
import org.openide.util.actions.SystemAction;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsSubReport;
import org.radixware.kernel.designer.ads.editors.clazz.report.diagram.palette.AdsReportPalette;
import org.radixware.kernel.designer.common.annotations.registrators.NodeFactoryRegistration;
import org.radixware.kernel.designer.common.general.nodes.INodeFactory;
import org.radixware.kernel.designer.common.tree.RadixObjectNode;
import org.radixware.kernel.designer.common.tree.RadixObjectNodeDeleteAction;
import org.radixware.kernel.designer.tree.ads.nodes.actions.AdsSubReportGoToReportAction;


public class AdsSubReportNode extends RadixObjectNode {

//    private static class PaletteCookie extends AdsReportPalette.Cookie {
//
//        private final AdsSubReport subReport;
//
//        public PaletteCookie(final AdsSubReport subReport) {
//            this.subReport = subReport;
//        }
//
//        @Override
//        public AdsReportForm getForm() {
//            return subReport.getOwnerBand().getOwnerForm();
//        }
//    }
    protected AdsSubReportNode(final AdsSubReport subReport) {
        super(subReport, Children.LEAF);
        this.addCookie(new AdsSubReportGoToReportAction.Cookie(subReport));
        //addCookie(new PaletteCookie(subReport));
        getLookupContent().add(AdsReportPalette.getController(subReport));
    }

    @Override
    public void addCustomActions(final List<Action> actions) {
        super.addCustomActions(actions);
        actions.add(SystemAction.get(RadixObjectNodeDeleteAction.class));
        actions.add(SystemAction.get(AdsSubReportGoToReportAction.class));
    }

    @NodeFactoryRegistration
    public static class Factory implements INodeFactory<AdsSubReport> {

        @Override
        public RadixObjectNode newInstance(final AdsSubReport subReport) {
            return new AdsSubReportNode(subReport);
        }
    }
}
