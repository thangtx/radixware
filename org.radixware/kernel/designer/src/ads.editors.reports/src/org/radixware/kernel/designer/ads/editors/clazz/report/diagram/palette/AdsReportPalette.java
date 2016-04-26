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
package org.radixware.kernel.designer.ads.editors.clazz.report.diagram.palette;

import org.netbeans.spi.palette.PaletteController;
import org.netbeans.spi.palette.PaletteFactory;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportForm;

public class AdsReportPalette {

    private static final PaletteController PALETTE_CONTROLLER = PaletteFactory.createPalette(
            new AdsReportPaletteRootNode(),
            new AdsReportPaletteActions(),
            null,
            new AdsReportPaletteDragAndDropHandler());

    private static AdsReportForm findForm(RadixObject obj) {
        if (obj instanceof AdsReportForm) {
            return (AdsReportForm) obj;
        }
        if (obj instanceof AdsReportClassDef) {
            return ((AdsReportClassDef) obj).getForm();
        }
        for (RadixObject o = obj; o != null; o = o.getContainer()) {
            if (o instanceof AdsReportForm) {
                return (AdsReportForm) o;
            }
        }
        return null;
    }

    public static PaletteController getController(RadixObject editorRoot) {
        return PALETTE_CONTROLLER;
    }
//    public abstract static class Cookie implements Node.Cookie {
//
//        public Cookie() {
//        }
//
//        public abstract AdsReportForm getForm();
//    }
}
