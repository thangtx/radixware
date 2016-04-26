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

package org.radixware.kernel.common.defs.ads.module;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.defs.ClipboardSupport;
import org.radixware.kernel.common.defs.RadixObject.EEditState;
import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.defs.ads.AdsClipboardSupport;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinitions;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.ENamingPolicy;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.repository.ads.fs.IRepositoryAdsDefinition;
import org.radixware.kernel.common.repository.ads.fs.IRepositoryAdsImageDefinition;
import org.radixware.kernel.common.repository.ads.fs.IRepositoryAdsModule;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.common.utils.XmlFormatter;
import org.radixware.schemas.adsdef.AdsDefinitionElementType;
import org.radixware.schemas.adsdef.AdsImageDefinitionDocument;
import org.radixware.schemas.adsdef.ImageDefinition;


/**
 * Ads image description.
 */
public class AdsImageDef extends AdsDefinition {

    private List<String> keyWords = null;

    private AdsImageDef(final Id id, String name) {
        super(id, name);
    }

    private AdsImageDef(ImageDefinition xDef) {
        super(xDef);
        loadFrom(xDef);
    }

    private static ImageDefinition loadImageXmlDef(IRepositoryAdsDefinition rep) throws IOException {
        ImageDefinition xDef = null;

        try {
            InputStream stream = rep.getData();
            if (stream != null) {
                try {
                    xDef = AdsImageDefinitionDocument.Factory.parse(stream).getAdsImageDefinition();
                } finally {
                    stream.close();
                }
                if (xDef == null) {
                    throw new XmlException("Image definition is not defined.");
                }
            } else {
                throw new IOException("No data found for image definition " + rep.getPath());
            }
        } catch (XmlException cause) {
            throw new IOException("Unable to load image definition from '" + rep.getPath() + "'.", cause);
        }


        final Id id = rep.getId();
        final Id idFromXml = xDef.getId();
        if (!Utils.equals(id, idFromXml)) {
            throw new IOException("Image identifier '" + String.valueOf(idFromXml) + "' must be equal to its file name '" + String.valueOf(id) + "'.");
        }

        return xDef;
    }

    public static class Factory {

        private Factory() {
        }

        public static AdsImageDef newInstance() {
            final Id id = Id.Factory.newInstance(EDefinitionIdPrefix.IMAGE);
            return new AdsImageDef(id, "Untitled");
        }

        public static AdsImageDef newInstance(Id id) {
            return new AdsImageDef(id, "Untitled");
        }

        public static AdsImageDef loadFrom(IRepositoryAdsImageDefinition rep) throws IOException {
            final File file = rep.getFile();
            final long fileTime = file == null ? 0L : file.lastModified();
            ImageDefinition xDef = loadImageXmlDef(rep);
            final AdsImageDef imageDef = new AdsImageDef(xDef);
            imageDef.setFileLastModifiedTime(fileTime);
            imageDef.setEditState(EEditState.NONE);
            return imageDef;
        }
    }

    private static class RadixIconFilter extends RadixIcon {

        public RadixIconFilter(File file) {
            super(file);
        }

        @Override
        public Image getImage(int w, int h) {
            try {
                return super.getImage(w, h);
            } catch (Exception cause) {
                return RadixObjectIcon.UNKNOWN.getImage(w, h);
            }
        }

        @Override
        public Image getOriginalImage() {
            try {
                return super.getOriginalImage();
            } catch (Exception cause) {
                return RadixObjectIcon.UNKNOWN.getOriginalImage();
            }
        }
    }

    @Override
    public RadixIcon getIcon() {
        final File imageFile = getImageFile();
        if (imageFile == null) {
            return RadixObjectIcon.UNKNOWN; // RADIX-1829
        }

        return new RadixIconFilter(imageFile);
    }

    @Override
    public boolean setName(String name) {
        synchronized (this) {
            if (super.setName(name)) {
                try {
                    save();
                } catch (IOException cause) {
                    throw new DefinitionError("Unable to set image name.", this, cause);
                }
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public void setDescription(String desc) {
        synchronized (this) {
            super.setDescription(desc);

            try {
                save();
            } catch (IOException cause) {
                throw new DefinitionError("Unable to set image description.", this, cause);
            }
        }
    }

    public List<String> getKeywords() {
        if (keyWords == null) {
            return Collections.emptyList();
        } else {
            return new ArrayList<String>(keyWords);
        }
    }

    public void setKeywords(List<String> keywords) {
        synchronized (this) {
            if (keywords == null || keywords.isEmpty()) {
                this.keyWords = null;
            } else {
                this.keyWords = new ArrayList<String>(keywords);
            }

            try {
                save();
            } catch (IOException cause) {
                throw new DefinitionError("Unable to set image keywords.", this, cause);
            }
        }
    }

    /**
     * Get image description file (xml).
     * @return file or null if image module is not in branch.
     */
    @Override
    public File getFile() {
        final AdsModule module = getModule();
        if (module == null) {
            return null;
        }
        IRepositoryAdsModule mr = module.getRepository();
        if (mr == null) {
            return null;
        }
        IRepositoryAdsDefinition dr = mr.getDefinitionRepository(this);

        if (dr != null) {
            return dr.getFile();
        } else {
            return null;
        }
    }

    /**
     * Get image file.
     * @return file or null if image module is not in branch.
     */
    public File getImageFile() {

        final AdsModule module = getModule();
        if (module == null) {
            return null;
        }
        IRepositoryAdsModule mr = module.getRepository();
        if (mr == null) {
            return null;
        }
        IRepositoryAdsDefinition dr = mr.getDefinitionRepository(this);

        if (dr instanceof IRepositoryAdsImageDefinition) {
            return ((IRepositoryAdsImageDefinition) dr).getImageFile();
        } else {
            return null;
        }
    }
    public InputStream getImageData() throws IOException {

        final AdsModule module = getModule();
        if (module == null) {
            return null;
        }
        IRepositoryAdsModule mr = module.getRepository();
        if (mr == null) {
            return null;
        }
        IRepositoryAdsDefinition dr = mr.getDefinitionRepository(this);

        if (dr instanceof IRepositoryAdsImageDefinition) {
            return ((IRepositoryAdsImageDefinition) dr).getImageData();
        } else {
            return null;
        }
    }
    public String getImageFileName() throws IOException {

        final AdsModule module = getModule();
        if (module == null) {
            return null;
        }
        IRepositoryAdsModule mr = module.getRepository();
        if (mr == null) {
            return null;
        }
        IRepositoryAdsDefinition dr = mr.getDefinitionRepository(this);

        if (dr instanceof IRepositoryAdsImageDefinition) {
            return ((IRepositoryAdsImageDefinition) dr).getImageFileName();
        } else {
            return null;
        }
    }

    private void loadFrom(ImageDefinition xDef) {
        final String name = xDef.getName();
        super.setName(name != null ? name : ""); // super - to prevent save

        final String description = xDef.getDescription();
        super.setDescription(description != null ? description : ""); // super - to prevent save

        if (xDef.getKeyWords() != null) {
            this.keyWords = xDef.getKeyWords().getKeyWordList(); // directly - to prevent save
        } else {
            this.keyWords = null;
        }
    }

    private void reload(IRepositoryAdsDefinition rep) throws IOException {
        final File file = rep.getFile();
        final long fileTime = file == null ? 0L : file.lastModified();
        ImageDefinition xDef = loadImageXmlDef(rep);

        synchronized (this) {
            loadFrom(xDef);
        }

        setFileLastModifiedTime(fileTime);
        setEditState(EEditState.NONE);
    }

    public void reload() throws IOException {
        AdsModule module = getModule();
        if (module != null) {
            IRepositoryAdsModule moduleRep = module.getRepository();
            if (moduleRep != null) {
                IRepositoryAdsDefinition defRep = moduleRep.getDefinitionRepository(this);
                if (defRep != null) {
                    reload(defRep);
                }
            }
        }
    }

    @Override
    public boolean isSaveable() {
        return true;
    }

    public void appendTo(ImageDefinition xImage) {
        if (xImage == null) {
            throw new NullPointerException("Append method argument is null");
        }
        xImage.setId(getId());
        xImage.setName(getName());
        final String description = getDescription();
        if (description != null && !description.isEmpty()) {
            xImage.setDescription(description);
        }
        if (keyWords != null && !keyWords.isEmpty()) {
            ImageDefinition.KeyWords kw = xImage.addNewKeyWords();
            for (String k : keyWords) {
                kw.addKeyWord(k);
            }
        }
    }
    
    @Override
    public void save() throws IOException {
        IRepositoryAdsDefinition defRep = null;

        AdsModule module = getModule();
        if (module != null) {
            IRepositoryAdsModule moduleRep = module.getRepository();
            if (moduleRep != null) {
                defRep = moduleRep.getDefinitionRepository(this);
            }
        }
        if (defRep == null) {
            throw new IOException("No repository found for image definition " + getQualifiedName());
        }

        final AdsImageDefinitionDocument xImageDoc = AdsImageDefinitionDocument.Factory.newInstance();
        appendTo(xImageDoc.addNewAdsImageDefinition());

        final File file = getFile();
        if (file == null) {
            throw new IOException("Image definition " + getQualifiedName() + " is not file-based");
        }

        getModule().getImages().makeDirectory();

        synchronized (this) {
            XmlFormatter.save(xImageDoc, file);
            setFileLastModifiedTime(file.lastModified());
        }

        setEditState(EEditState.NONE);
    }

    @Override
    public boolean delete() {
        final File file = getFile();
        final File imageFile = getImageFile();

        if (!super.delete()) {
            return false;
        }

        if (file != null) {

            try {
                FileUtils.deleteFileExt(file);
            } catch (IOException cause) {
                throw new DefinitionError("Unable to delete image definition file.", this, cause);
            }
        }

        if (imageFile != null) {
            try {
                FileUtils.deleteFileExt(imageFile);
            } catch (IOException cause) {
                throw new DefinitionError("Unable to delete image file.", this, cause);
            }
        }

        return true;
    }

    @Override
    public EDefType getDefinitionType() {
        return EDefType.IMAGE;
    }

    private static class AdsImageTransfer extends AdsClipboardSupport.AdsTransfer<AdsImageDef> {

        private final byte[] imageData;
        private final String ext;

        public AdsImageTransfer(AdsImageDef source) {
            super(source);
            final File imageDatafile = source.getImageFile();
            if (imageDatafile != null && imageDatafile.isFile()) {
                try {
                    imageData = FileUtils.readBinaryFile(imageDatafile);
                    ext = FileUtils.getFileExt(imageDatafile);
                } catch (IOException cause) {
                    throw new DefinitionError("Unable to read image file '" + imageDatafile.getPath() + "'.", source, cause);
                }
            } else {
                imageData = null;
                ext = null;
            }
        }

        @Override
        public void afterPaste() {
            super.afterPaste();

            if (imageData != null) {
                final AdsImageDef imageDef = getObject();
                final File imageDefFile = imageDef.getFile();
                final File imageDataFile = new File(imageDefFile.getParentFile(), imageDef.getId().toString() + "." + ext);
                try {
                    FileUtils.writeBytes(imageDataFile, imageData);
                } catch (IOException cause) {
                    throw new DefinitionError("Unable to write image file '" + imageDataFile.getPath() + "'.", imageDef, cause);
                }
            }
        }
    }

    @Override
    public ClipboardSupport<AdsImageDef> getClipboardSupport() {
        return new AdsClipboardSupport<AdsImageDef>(this) {

            @Override
            protected XmlObject copyToXml() {
                ImageDefinition xDef = ImageDefinition.Factory.newInstance();
                appendTo(xDef, ESaveMode.NORMAL);
                return xDef;
            }

            @Override
            protected AdsImageDef loadFrom(XmlObject xmlObject) {
                if (xmlObject instanceof ImageDefinition) {
                    return new AdsImageDef((ImageDefinition) xmlObject);
                } else {
                    return super.loadFrom(xmlObject);
                }
            }

            @Override
            protected Transfer<AdsImageDef> newTransferInstance() {
                return new AdsImageTransfer(radixObject);
            }
        };
    }

    @Override
    public boolean isSuitableContainer(AdsDefinitions collection) {
        return collection instanceof ModuleImages;
    }

    public void importImage(File file) throws IOException {
        synchronized (this) {
            final String ext = FileUtils.getFileExt(file);
            final File oldImageFile = getImageFile();
            // RADIX-3107
            final File imagesDir = oldImageFile != null ? oldImageFile.getParentFile() : this.getModule().getImages().getDirectory();
            final File newImageFile = new File(imagesDir, getId().toString() + "." + ext.toLowerCase()); // RADIX-4977
            FileUtils.copyFile(file, newImageFile);
            if (oldImageFile != null && !Utils.equals(oldImageFile, newImageFile)) {
                FileUtils.deleteFileExt(oldImageFile);
            }
        }
    }

    @Override
    public ENamingPolicy getNamingPolicy() {
        return ENamingPolicy.FREE; // can be empty
    }

    @Override
    public EAccess getAccessMode() {
        return EAccess.PUBLIC;
    }

    @Override
    public boolean isPublished() {
        return true;
    }
}
