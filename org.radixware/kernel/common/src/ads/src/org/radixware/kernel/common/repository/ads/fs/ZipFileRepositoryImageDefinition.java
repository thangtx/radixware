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

package org.radixware.kernel.common.repository.ads.fs;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinition.ESaveMode;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.AdsImageDefinitionDocument;
import org.radixware.schemas.adsdef.ImageDefinition;


public class ZipFileRepositoryImageDefinition implements IRepositoryAdsImageDefinition {

    private final Id id;
    private final String zipFileName;
    private final String zipEntryName;
    private final String format;

    public ZipFileRepositoryImageDefinition(String zipFileName, String zipEntryName) {
        this.zipFileName = zipFileName;
        this.zipEntryName = zipEntryName;
        

        int extensionPos = zipEntryName.lastIndexOf('.');
        int slashPos = zipEntryName.lastIndexOf('/');
        id = Id.Factory.loadFrom(zipEntryName.substring(slashPos + 1, extensionPos));
        this.format = zipEntryName.substring(extensionPos + 1);
    }

    @Override
    public InputStream getImageData() throws IOException {
        try (ZipFile zip = new ZipFile(zipFileName)) {
            ZipEntry entry = zip.getEntry(zipEntryName);
            if (entry != null) {
                return zip.getInputStream(entry);
            } else {
                throw new IOException("No entry found: " + zipEntryName);
            }
        }
    }

    @Override
    public String getImageFileName() {
        return id.toString() + "." + format;
    }
    

    @Override
    public boolean willLoadFromAPI() {
        return false;
    }

    @Override
    public File getImageFile() {
        return null;
    }

    @Override
    public Id getId() {
        return id;
    }

    @Override
    public AdsDefinition getPreLoadedDefinition() {
        return null;
    }

    @Override
    public File getFile(ESaveMode saveMode) {
        return null;
    }

    @Override
    public String getName() {
        return "SomeImage";
    }

    @Override
    public EDefType getType() {
        return EDefType.IMAGE;
    }

    @Override
    public ESaveMode getUploadMode() {
        return ESaveMode.API;
    }

    @Override
    public IRepositoryAdsDefinition getMlsRepository() {
        return null;
    }

    @Override
    public ERuntimeEnvironmentType getEnvironment() {
        return ERuntimeEnvironmentType.COMMON;
    }

    @Override
    public File getFile() {
        return null;
    }

    @Override
    public InputStream getData() throws IOException {

        AdsImageDefinitionDocument xDoc = AdsImageDefinitionDocument.Factory.newInstance();
        ImageDefinition xDef = xDoc.addNewAdsImageDefinition();
        xDef.setFormat(format);
        xDef.setId(id);
        xDef.setName(getName());
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            xDoc.save(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    @Override
    public String getPath() {
        return "";
    }

    @Override
    public void processException(Throwable e) {
    }

    public static IRepositoryAdsImageDefinition[] listZipFileImages(File imgJar) {
        if (imgJar.isFile()) {
            ZipFile zip = null;
            try {
                zip = new ZipFile(imgJar);
                Map<Id, ZipFileRepositoryImageDefinition> images = new HashMap<>();
                Enumeration<? extends ZipEntry> entries = zip.entries();
                while (entries.hasMoreElements()) {
                    ZipEntry e = entries.nextElement();
                    if (!e.isDirectory()) {
                        String name = e.getName();
                        String[] nameComponents = name.split("/");
                        if (nameComponents.length > 0) {
                            String realName = nameComponents[nameComponents.length - 1];
                            if (realName.startsWith(EDefinitionIdPrefix.IMAGE.getValue())) {
                                ZipFileRepositoryImageDefinition image = new ZipFileRepositoryImageDefinition(imgJar.getAbsolutePath(), name);
                                ZipFileRepositoryImageDefinition ex = images.get(image.getId());
                                if (ex != null) {
                                    if (ex.zipEntryName.endsWith(".svg")) {
                                        //ignore new image
                                    } else {
                                        if (image.zipEntryName.endsWith(".svg")) {
                                            images.put(image.getId(), image);
                                        }
                                    }
                                } else {
                                    images.put(image.getId(), image);
                                }
                            }
                        }
                    }
                }
                return images.values().toArray(new IRepositoryAdsImageDefinition[images.size()]);
            } catch (IOException ex) {
                Logger.getLogger(ZipFileRepositoryImageDefinition.class.getName()).log(Level.SEVERE, "", ex);
            } finally {
                if (zip != null) {
                    try {
                        zip.close();
                    } catch (IOException ex) {
                        Logger.getLogger(ZipFileRepositoryImageDefinition.class.getName()).log(Level.FINE, ex.getMessage(), ex);
                    }
                }
            }
        }
        return new IRepositoryAdsImageDefinition[0];
    }

    @Override
    public boolean isPublished() {
        return true;
    }

    @Override
    public EAccess getAccessMode() {
        return null;
    }
}
