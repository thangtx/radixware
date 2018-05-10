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

package org.radixware.kernel.radixdoc.html;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.radixware.kernel.common.components.ICancellable;
import org.radixware.kernel.common.components.IProgressHandle;


final class ZipGenerator extends HtmlRadixdocGenerator {

    private ZipOutputStreamWraper streamWraper;

    public ZipGenerator(IRadixdocOptions options, IProgressHandle progressHandle, ICancellable cancellable) {
        super(options, progressHandle, cancellable);
    }

    @Override
    protected boolean prepare() {
        super.prepare();

        try {
            streamWraper = new ZipOutputStreamWraper(new ZipOutputStream(new FileOutputStream(getFileProvider().getOutput())));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(HtmlRadixdocGenerator.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

        return true;
    }

    @Override
    protected void finish() {
        super.finish();
        if (streamWraper != null) {
            try {
                streamWraper.getStream().close();
            } catch (IOException ex) {
                Logger.getLogger(HtmlRadixdocGenerator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    protected boolean prepareFile(String fileName) {
        if (streamWraper == null || streamWraper.getStream() == null) {
            throw new NullPointerException();
        }

        try {
            final ZipEntry zipEntry = new ZipEntry(fileName);
            streamWraper.getZipStream().putNextEntry(zipEntry);
            return true;
        } catch (IOException ex) {
            Logger.getLogger(HtmlRadixdocGenerator.class.getName()).log(Level.WARNING, null, ex);
            return false;
        }
    }

    @Override
    protected StreamWraper<OutputStream> getOutputStreamWraper(String path) {
        return streamWraper;
    }
}
