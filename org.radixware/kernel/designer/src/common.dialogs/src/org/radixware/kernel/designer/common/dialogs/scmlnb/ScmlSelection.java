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

package org.radixware.kernel.designer.common.dialogs.scmlnb;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import org.radixware.kernel.common.defs.ClipboardSupport;
import org.radixware.kernel.common.defs.ads.AdsClipboardSupport.AdsTransfer;
import org.radixware.kernel.common.scml.Scml;

/**
 * Transfarable for Scml
 **/
public class ScmlSelection implements Transferable {

    final static public DataFlavor SCML_FLAVOR = new DataFlavor(Scml.class, "Scml");
    private DataFlavor[] supportedFlavors = {SCML_FLAVOR, ClipboardSupport.SERIALIZED_RADIX_TRANSFER, DataFlavor.stringFlavor};
    private AdsTransfer<Scml> scmlTransfer;
    private String scmlString;
    private String scmlXml;

    public ScmlSelection(AdsTransfer<Scml> scmlTransfer, String scmlString) {
        this.scmlTransfer = scmlTransfer;
        this.scmlString = scmlString;
        this.scmlXml = scmlTransfer.encode();
    }

    public ScmlSelection(AdsTransfer<Scml> scmlTransfer) {
        this.scmlTransfer = scmlTransfer;
        this.scmlXml = scmlTransfer.encode();
        this.scmlString = "No string representation available";
    }

    @Override
    public synchronized DataFlavor[] getTransferDataFlavors() {
        return supportedFlavors;
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return flavor.equals(SCML_FLAVOR) || flavor.equals(ClipboardSupport.SERIALIZED_RADIX_TRANSFER);// || flavor.equals(DataFlavor.stringFlavor);
    }

    @Override
    public synchronized Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
        boolean result = DataFlavor.stringFlavor.equals(ClipboardSupport.SERIALIZED_RADIX_TRANSFER);
        if (flavor.equals(DataFlavor.stringFlavor)) {
            return scmlString;
        }
        if (flavor.equals(SCML_FLAVOR)) {
            return scmlTransfer.copy();
        }
        if (flavor.equals(ClipboardSupport.SERIALIZED_RADIX_TRANSFER)) {
            return scmlXml;
        }

        throw new UnsupportedFlavorException(flavor);
    }
}
