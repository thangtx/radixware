/*
 * Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. This Source Code is distributed
 * WITHOUT ANY WARRANTY; including any implied warranties but not limited to
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Mozilla Public License, v. 2.0. for more details.
 */
 
package org.radixware.kernel.utils.wia;

import java.util.UUID;

public enum EWiaImageFormat {

    UNKNOWN(0xb96b3ca9072811d3l, 0x9d7b0000f81ef32el), 
	RAWRGB(0xbca48b55f2724371l, 0xb0f14a150d057bb4l), 
	MEMORYBMP(0xb96b3caa072811d3l, 0x9d7b0000f81ef32el),
	BMP(0xb96b3cab072811d3l, 0x9d7b0000f81ef32el), 
	EMF(0xb96b3cac072811d3l, 0x9d7b0000f81ef32el), 
	WMF(0xb96b3cad072811d3l, 0x9d7b0000f81ef32el), 
	JPEG(0xb96b3cae072811d3l, 0x9d7b0000f81ef32el), 
	PNG(0xb96b3caf072811d3l, 0x9d7b0000f81ef32el),
	GIF(0xb96b3cb0072811d3l, 0x9d7b0000f81ef32el),
    TIFF(0xb96b3cb1072811d3l, 0x9d7b0000f81ef32el), 
	EXIF(0xb96b3cb2072811d3l, 0x9d7b0000f81ef32el), 
	PHOTOCD(0xb96b3cb3072811d3l, 0x9d7b0000f81ef32el), 
	FLASHPIX(0xb96b3cb4072811d3l, 0x9d7b0000f81ef32el),
	ICO(0xb96b3cb5072811d3l, 0x9d7b0000f81ef32el), 
	CIFF(0x9821a8ab3a7e4215l, 0x94e0d27a460c03b2l), 
	PICT(0xa6bc85d86b3e40eel, 0xa95c25d482e41adcl), 
	JPEG2K(0x344ee2b239db4ddel, 0x8173c4b75f8f1e49l),
    JPEG2KX(0x43e14614c80a4850l, 0xbaf34b152dc8da27l), 
	RAW(0x6f120719f1a84e07l, 0x9ade9b64c63a3dccl), 
	JBIG(0x41e8dd922f0a43d4l, 0x8636f1614ba11e46l);
	   
	private final UUID guid;

    private EWiaImageFormat(final long mostSigBits, final long leastSigBits) {
        guid = new UUID(mostSigBits,leastSigBits);
    }

    public UUID getGuid() {
        return guid;
    }

    public static EWiaImageFormat fromGuid(final UUID guid) {
        for (EWiaImageFormat format : EWiaImageFormat.values()) {
            if (format.guid.equals(guid)) {
                return format;
            }
        }
        return UNKNOWN;
    }
}
