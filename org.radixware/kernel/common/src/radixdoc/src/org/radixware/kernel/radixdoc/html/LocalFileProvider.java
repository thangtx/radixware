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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.conventions.RadixdocConventions;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.schemas.adsdef.DocumentationTopicBody;

public abstract class LocalFileProvider extends FileProvider {

    private final String rootPath;
    private final File out;

    public LocalFileProvider(String rootPath, File out) {
        this.rootPath = rootPath;
        this.out = out;
    }

    @Override
    public InputStream getInputStream(String fileName) {
        try {
            return new FileInputStream(rootPath + "/" + fileName);
        } catch (FileNotFoundException e) {
//            Logger.getLogger(LocalFileProvider.class.getName()).log(Level.INFO, "", e);
            return null;
        }
    }

    @Override
    public ZipInputStream getDocDecorationInputStream(String layerUri) {
        Collection<LayerEntry> layers = getLayers();
        if (layers != null) {
            for (LayerEntry layerEntry : layers) {
                if (layerUri.equals(layerEntry.getIdentifier())) {
                    try {
                        String path = rootPath + "/" + layerEntry.getRootPath() + "/" + RadixdocConventions.RADIXDOC_DECORATIONS_PATH;
                        File decorationsDir = new File(path);
                        if (!decorationsDir.exists()) {
                            return null;
                        }
                        
                        final int random = (int) (1000000000 * Math.random());
                        File tmp = File.createTempFile("docDec" + random, ".zip");
                        FileOutputStream fos = new FileOutputStream(tmp);
                        ZipOutputStream zos = new ZipOutputStream(fos);
                        for (File decoration : decorationsDir.listFiles()) {
                            if (decoration.isDirectory()) {
                                continue;
                            }
                            zos.putNextEntry(new ZipEntry(decoration.getName()){{setMethod(DEFLATED);}});

                            try (FileInputStream fis = new FileInputStream(decoration)) {
                                FileUtils.copyStream(fis, zos);
                            }
                        }

                        zos.flush();
                        zos.close();
                        ZipInputStream result = new ZipInputStream(new TmpFileInputStreamWrapper(tmp));

                        return result;
                    } catch (IOException ex) {
                        Logger.getLogger(LocalFileProvider.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }

        return null;
    }
    
    @Override
    public File getOutput() {
        return out;
    }

    public String getRootPath() {
        return rootPath;
    }

    @Override
    protected boolean isFileExists(String filePath) {
        File file = new File(rootPath + File.separator + filePath);
        return file.exists();
    }

}
