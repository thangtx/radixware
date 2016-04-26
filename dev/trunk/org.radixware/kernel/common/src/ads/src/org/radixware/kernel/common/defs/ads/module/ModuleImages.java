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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.defs.ClipboardSupport.CanPasteResult;
import org.radixware.kernel.common.defs.ClipboardSupport.DuplicationResolver;
import org.radixware.kernel.common.defs.ClipboardSupport.Transfer;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IDirectoryRadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.AdsDefinitions;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.repository.ads.fs.FSRepositoryAdsImageDefinition;
import org.radixware.kernel.common.repository.ads.fs.IRepositoryAdsImageDefinition;
import org.radixware.kernel.common.repository.ads.fs.IRepositoryAdsModule;
import org.radixware.kernel.common.utils.FileUtils;

/**
 * Ads module image set
 */
public class ModuleImages extends AdsDefinitions<AdsImageDef> implements IDirectoryRadixObject {

    public ModuleImages(AdsModule owner) {
        super(owner);
        upload();
    }

    @Override
    public String getName() {
        return "Images";
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.MODULE_IMAGES;
    }

    /**
     * Get images directory location.
     *
     * @return directory or null if module is not in branch.
     */
    @Override
    public File getDirectory() {
        AdsModule module = getModule();
        if (module == null) {
            return null;
        }
        IRepositoryAdsModule rep = module.getRepository();
        if (rep == null) {
            return null;
        }
        return rep.getImagesDirectory();
    }

    void reload() {
        synchronized (this) {
            List<AdsImageDef> images = list();
            for (AdsImageDef image : images) {
                this.remove(image);
            }
            upload();
        }
    }

    void makeDirectory() throws IOException {
        final File imagesDir = getDirectory();

        if (imagesDir == null) {
            throw new IOException("Image set is not file-based");
        }

        FileUtils.mkDirs(imagesDir);
    }

    public AdsImageDef importImage(File file) throws IOException {
        synchronized (this) {
            makeDirectory();

            final AdsImageDef imageDef = AdsImageDef.Factory.newInstance();
            final Id id = imageDef.getId();
            final String ext = FileUtils.getFileExt(file);
            final File imagesDir = getDirectory();
            if (imagesDir == null) {
                throw new IOException("Images set is not file-based");
            }
            final File imageFile = new File(imagesDir, id.toString() + "." + ext);

            FileUtils.copyFile(file, imageFile);

            add(imageDef);
            return imageDef;
        }
    }
    private boolean loading = false;

    @Override
    protected void onAdd(final AdsImageDef imageDef) {
        super.onAdd(imageDef);

        if (!loading && imageDef.getFile() != null) {
            try {
                imageDef.save();
            } catch (IOException cause) {
                throw new DefinitionError("Unable to save image definition.", imageDef, cause);
            }
        }
    }

    private AdsImageDef loadFromRepository(IRepositoryAdsImageDefinition repo, boolean checkForDuplication) throws IOException {
        try {

            AdsImageDef imageDef = null;
            if (repo instanceof FSRepositoryAdsImageDefinition) {
                Definition def = ((FSRepositoryAdsImageDefinition) repo).getPreLoadedDefinition();
                if (def instanceof AdsImageDef) {
                    imageDef = (AdsImageDef) def;
                }
            }
            if (imageDef == null) {
                imageDef = AdsImageDef.Factory.loadFrom(repo);
            }

            if (checkForDuplication && this.findById(imageDef.getId()) != null) {
                throw new IllegalStateException("Image with identifier '" + imageDef.getId() + "' is already loaded.");
            }
            return imageDef;
        } catch (Throwable cause) {
            throw new IOException("Unable to load image definition from '" + repo.getPath() + "'", cause);
        }
    }

    public AdsImageDef addFromRepository(IRepositoryAdsImageDefinition repo) throws IOException {
        final boolean checkForDuplication = true;
        final AdsImageDef imageDef = loadFromRepository(repo, checkForDuplication);

        synchronized (this) {
            loading = true;
            try {
                super.add(imageDef);
            } finally {
                loading = false;
            }
        }

        return imageDef;
    }

    public File[] collectImageDefFiles() {
        AdsModule module = getModule();
        if (module == null) {
            return new File[0];
        }
        IRepositoryAdsModule moduleRep = module.getRepository();
        if (moduleRep == null) {
            return new File[0];
        }

        IRepositoryAdsImageDefinition[] images = moduleRep.listImages();
        ArrayList<File> files = new ArrayList<File>();
        for (IRepositoryAdsImageDefinition image : images) {

            File file = image.getFile();
            if (file != null) {
                files.add(file);
            }

        }
        return files.toArray(new File[0]);
    }

    private void upload() {
        synchronized (this) {
            AdsModule module = getModule();
            if (module == null) {
                return;
            }
            IRepositoryAdsModule moduleRep = module.getRepository();
            if (moduleRep == null) {
                return;
            }

            IRepositoryAdsImageDefinition[] images = moduleRep.listImages();
            for (IRepositoryAdsImageDefinition image : images) {
                try {
                    addFromRepository(image);
                } catch (IOException ex) {
                    moduleRep.processException(ex);
                }
            }
        }
    }

    @Override
    protected boolean isPersistent() {
        return false;
    }

    @Override
    public void add(AdsImageDef imageDef) {
        if (findById(imageDef.getId()) != null) {
            throw new DefinitionError("Duplicate image: " + imageDef.getId().toString(), imageDef);
        }
        super.add(imageDef);
    }

    @Override
    protected CanPasteResult canPaste(List<Transfer> transfers, DuplicationResolver resolver) {
        CanPasteResult result = super.canPaste(transfers, resolver);
        if (result != CanPasteResult.YES) {
            return result;
        }
        for (Transfer transfer : transfers) {
            final AdsImageDef image = (AdsImageDef) transfer.getObject();
            if (findById(image.getId()) != null) {
                return CanPasteResult.NO_DUPLICATE;
            }
        }
        return CanPasteResult.YES;
    }
}
