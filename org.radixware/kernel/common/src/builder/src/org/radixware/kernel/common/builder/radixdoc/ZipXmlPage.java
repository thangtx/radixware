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

package org.radixware.kernel.common.builder.radixdoc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.radixware.kernel.common.conventions.RadixdocConventions;
import org.radixware.kernel.common.utils.XmlFormatter;
import org.radixware.schemas.radixdoc.RadixdocUnitDocument;


final class ZipXmlPage implements IDocumentPage {

    private PageLocation location;
    private RadixdocUnitDocument document;
    private Path path;

    public ZipXmlPage(RadixdocUnitDocument xmlDocument, PageLocation location) {
        this.location = location;
        this.document = xmlDocument;
        this.path = null;
    }

    @Override
    public boolean save(String root) throws IOException {
        path = Paths.get(root).resolve(location.getOutputPath());
        Files.createDirectories(path.getParent());

        try (final ZipOutputStream stream = new ZipOutputStream(new FileOutputStream(getFile()))) {
            stream.putNextEntry(new ZipEntry(RadixdocConventions.RADIXDOC_XML_FILE));
            save(stream);
        }
        return true;
    }

    @Override
    public boolean save(OutputStream stream) throws IOException {
        XmlFormatter.save(document, stream);
        return true;
    }

    @Override
    public PageLocation getLocation() {
        return location;
    }

    @Override
    public String getTitle() {
        return "radixdoc";
    }

    @Override
    public File getFile() {
        return path.toFile();
    }
}
