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

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import org.netbeans.spi.palette.DragAndDropHandler;
import org.openide.util.Lookup;
import org.openide.util.datatransfer.ExTransferable;

/**
 * Adapter that allows to create report item (cell or subreport) by palette node with the help of IAdsReportItemFactory
 */
public class AdsReportPaletteDragAndDropHandler extends DragAndDropHandler {

    public static final DataFlavor REPORT_PALETTE_DATA_FLAVOR = new DataFlavor(AdsReportAddNewItemAction.class, "ReportPaletteDataFlavor");

    @Override
    public void customize(final ExTransferable exTransferable,final Lookup lookup) {
        final AdsReportAddNewItemAction itemFactory = lookup.lookup(AdsReportAddNewItemAction.class);
        exTransferable.put(new ExTransferable.Single(REPORT_PALETTE_DATA_FLAVOR) {

            @Override
            protected Object getData() throws IOException, UnsupportedFlavorException {
                return itemFactory;
            }
        });
    }
}
