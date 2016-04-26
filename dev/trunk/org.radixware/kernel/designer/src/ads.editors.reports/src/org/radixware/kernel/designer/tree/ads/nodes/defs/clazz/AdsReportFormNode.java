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

import org.openide.nodes.Children;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportForm;
import org.radixware.kernel.designer.ads.editors.clazz.report.diagram.palette.AdsReportPalette;
import org.radixware.kernel.designer.common.annotations.registrators.NodeFactoryRegistration;
import org.radixware.kernel.designer.common.general.nodes.INodeFactory;
import org.radixware.kernel.designer.common.tree.RadixObjectNode;


public class AdsReportFormNode extends RadixObjectNode {

//    private static class PaletteCookie extends AdsReportPalette.Cookie {
//
//        private final AdsReportForm form;
//
//        public PaletteCookie(final AdsReportForm form) {
//            this.form = form;
//        }
//
//        @Override
//        public AdsReportForm getForm() {
//            return form;
//        }
//    }

    protected AdsReportFormNode(final AdsReportForm form) {
        super(form, Children.LEAF);
        //addCookie(new PaletteCookie(form));
        getLookupContent().add(AdsReportPalette.getController(form));
    }

    @NodeFactoryRegistration
    public static class Factory implements INodeFactory<AdsReportForm> {

        @Override
        public RadixObjectNode newInstance(final AdsReportForm form) {
            return new AdsReportFormNode(form);
        }
    }
}
