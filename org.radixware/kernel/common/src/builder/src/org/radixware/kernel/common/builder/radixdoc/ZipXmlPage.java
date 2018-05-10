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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Deque;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import static org.apache.poi.hssf.usermodel.HeaderFooter.file;
import org.radixware.kernel.common.conventions.RadixdocConventions;
import org.radixware.kernel.common.utils.XmlFormatter;
import org.radixware.schemas.radixdoc.ModuleDocumentationItemsDocument;
import org.radixware.schemas.radixdoc.RadixdocUnitDocument;

final class ZipXmlPage implements IDocumentPage {

    private PageLocation location;
    private RadixdocUnitDocument document;
    private ModuleDocumentationItemsDocument docItemsDocument;
    private File topicBodyDir;
    private File resourceDir;
    private Path path;

    public ZipXmlPage(RadixdocUnitDocument xmlDocument, ModuleDocumentationItemsDocument docItemsDocument, File topicBodyDir, File resourceDir, PageLocation location) {
        this.location = location;
        this.document = xmlDocument;
        this.docItemsDocument = docItemsDocument;
        this.topicBodyDir = topicBodyDir;
        this.resourceDir = resourceDir;
        this.path = null;
    }

    public ZipXmlPage(RadixdocUnitDocument xmlDocument, PageLocation location) {
        this(xmlDocument, null, null, null, location);
    }

    @Override
    public boolean save(String root) throws IOException {
        path = Paths.get(root).resolve(location.getOutputPath());
        Files.createDirectories(path.getParent());

        try (final ZipOutputStream stream = new ZipOutputStream(new FileOutputStream(getFile()))) {
            save(stream);
        }
        return true;
    }

    @Override
    public boolean save(OutputStream stream) throws IOException {
        if (!(stream instanceof ZipOutputStream)) {
            return false;
        }

        ZipOutputStream zipStream = (ZipOutputStream) stream;

        try {
            // raduxdoc 1
            ByteArrayOutputStream tmpStream = new ByteArrayOutputStream();
            XmlFormatter.save(document, tmpStream);

            zipStream.putNextEntry(new ZipEntry(RadixdocConventions.RADIXDOC_XML_FILE));
            zipStream.write(tmpStream.toByteArray());
            zipStream.closeEntry();

            // raduxdoc 2
            tmpStream = new ByteArrayOutputStream();
            XmlFormatter.save(docItemsDocument, tmpStream);

            zipStream.putNextEntry(new ZipEntry(RadixdocConventions.RADIXDOC_2_XML_FILE));
            zipStream.write(tmpStream.toByteArray());
            zipStream.closeEntry();

            // topicBodyDir
            directoryToZip(topicBodyDir, zipStream, RadixdocConventions.TECHDOC_BODY_DIR_NAME);
            
            // resourceDir
            directoryToZip(resourceDir, zipStream, RadixdocConventions.TECHDOC_RESOURCES_DIR_NAME);

        } catch (Exception ex) {
            Logger.getLogger(ZipXmlPage.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            zipStream.close();
        }

        return true;

    }

    // TODO: !!! нужно перенести этот метод в подходящий класс
    private static void directoryToZip(File directory, ZipOutputStream zout, String nameBaseDir) throws IOException {
        URI base = directory.toURI();
        Deque<File> queue = new LinkedList<File>();
        queue.push(directory);

        while (!queue.isEmpty()) {
            directory = queue.pop();
            for (File child : directory.listFiles()) {
                String name = nameBaseDir + "/" + base.relativize(child.toURI()).getPath();
                if (child.isDirectory()) {
                    queue.push(child);
                    name = name.endsWith("/") ? name : name + "/";
                    zout.putNextEntry(new ZipEntry(name));
                } else {
                    zout.putNextEntry(new ZipEntry(name));

                    InputStream in = new FileInputStream(child);
                    try {
                        byte[] buffer = new byte[1024];
                        while (true) {
                            int readCount = in.read(buffer);
                            if (readCount < 0) {
                                break;
                            }
                            zout.write(buffer, 0, readCount);
                        }
                    } finally {
                        in.close();
                    }
                    zout.closeEntry();
                }
            }

        }
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
