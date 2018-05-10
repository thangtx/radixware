package org.radixware.kernel.common.defs.uds.module;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.radixware.kernel.common.defs.ClipboardSupport.CanPasteResult;
import org.radixware.kernel.common.defs.ClipboardSupport.DuplicationResolver;
import org.radixware.kernel.common.defs.ClipboardSupport.Transfer;
import org.radixware.kernel.common.defs.IDirectoryRadixObject;
import org.radixware.kernel.common.defs.IFileSystemRadixObject;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.IDefinitionContainer;
import org.radixware.kernel.common.defs.ads.ITopContainer;
import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import org.radixware.kernel.common.defs.ads.xml.IXmlDefinition;
import org.radixware.kernel.common.defs.uds.IUdsDirectoryRadixObject;
import org.radixware.kernel.common.defs.uds.UdsDefinition;
import org.radixware.kernel.common.defs.uds.UdsDefinitionsUtils;
import org.radixware.kernel.common.defs.uds.files.UdsFile;
import org.radixware.kernel.common.defs.uds.files.UdsFileRadixObjects;
import org.radixware.kernel.common.exceptions.RadixObjectError;
import org.radixware.kernel.common.repository.ads.fs.IRepositoryAdsDefinition;
import org.radixware.kernel.common.repository.uds.IRepositoryUdsModule;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.XmlFormatter;
import org.radixware.kernel.common.utils.XmlUtils;
import org.radixware.schemas.adsdef.AdsDefinitionDocument;
import org.radixware.schemas.adsdef.AdsDefinitionElementType;
import org.radixware.schemas.udsdef.UdsDefinitionDocument;

public class UdsFiles extends RadixObjects<RadixObject> implements IUdsDirectoryRadixObject, IFileSystemRadixObject, ITopContainer, IDefinitionContainer {    
    
    protected UdsFiles(UdsModule owner, boolean upload) {
        super(owner);
        if (upload) {
            upload();
        }
        
    }
    
    @Override
    public UdsModule getModule() {
        return (UdsModule) super.getModule();
    }

    @Override
    public File getFile() {
        return getModule().getEtcDir();
    }
    
    private void upload() {
        synchronized (this) {
            if (getLayer() != null && getLayer().isLocalizing()) {
                return;
            }
            IRepositoryUdsModule repository = getModule().getRepository();
            if (repository != null) {
                IRepositoryAdsDefinition[] definitionRepositories = repository.getListFiles();
                for (IRepositoryAdsDefinition definitionRepository : definitionRepositories) {
                    try {
                        addFromRepository(definitionRepository);
                    } catch (IOException ex) {
                        repository.processException(ex);
                    }
                    
                }
            }
        }
    }

    @Override
    protected void onAdd(RadixObject object) {
        if (!loading) {
           if (object.getFile() != null) {
                try {
                    object.save();
                } catch (IOException cause) {
                    throw new RadixObjectError("Unable to save UDS file.", object, cause);
                }
                getModule().getDependences().addRequired(); // add required dependencies after paste, and for new definitions.
            }
        }
    }
    
    public RadixObject addFromRepository(IRepositoryAdsDefinition definitionRepository) throws IOException {
        File file = definitionRepository.getFile();

        RadixObject radixObject = Loader.parseFile(file, this);
        if (radixObject == null){
            return null;
        }
        synchronized (this) {
            loading = true;
            try {
                super.add(radixObject);
            } finally {
                loading = false;
            }
        }
        return radixObject;
    }
    
    protected boolean loading = false;
    
    
    @Override
    public RadixObject reload(AdsDefinition oldDef) throws IOException {
        return reload((RadixObject)oldDef);
    }

    
    public RadixObject reload(RadixObject oldDefinition) throws IOException {
        File file = oldDefinition.getFile();
        if (file == null) {
            return null;
        }
        IUdsDirectoryRadixObject dir = getOwnerDirectory(oldDefinition);
        if (dir == null){
            return null;
        }
        Module module = getModule();
        if (module == null || !(module instanceof UdsModule)) {
            return null;
        }
        if (oldDefinition instanceof AdsDefinition) {

            final RadixObject radixObject = Loader.parseFile(file, ((UdsModule) module).getUdsFiles());
            if (radixObject != null) {
                safelyReplace((AdsDefinition) oldDefinition, radixObject, dir);
            }
            return radixObject;
        } else {
            final RadixObject radixObject = Loader.parseFile(file, this);
            if (radixObject != null) {
                synchronized (this) {
                    loading = true;
                    dir.remove(oldDefinition);
                    dir.add(radixObject);
                    loading = false;
                }
                return radixObject;
            }
        }

        return null;
    }
    
    public IUdsDirectoryRadixObject getOwnerDirectory(RadixObject def) {
        for (RadixObject owner = def.getContainer(); owner != null; owner = owner.getContainer()) {
            if (owner instanceof IUdsDirectoryRadixObject) {
                return (IUdsDirectoryRadixObject) owner;
            }
        }
        return null;
    }
    
    protected void safelyReplace(AdsDefinition oldDef, RadixObject newDef, IUdsDirectoryRadixObject dir) {
        synchronized (this) {
            loading = true;
            try {
                org.radixware.kernel.common.defs.ads.module.ModuleDefinitions defs = getModule().getDefinitions();
                defs.removeBundle(oldDef);
                dir.remove(oldDef);
                defs.refreshStringsCache(oldDef);
                dir.add(newDef);
            } finally {
                loading = false;
            }
        }
    }
    
    

    @Override
    public File getDirectory() {
        return getFile();
    }

    @Override
    public IDirectoryRadixObject getOwnerDirectory() {
        return null;
    }

    @Override
    public RadixObject getRadixObjectByFileName(String name) {
        return UdsDefinitionsUtils.getRadixObjectByFileName(name, list());
    }

    @Override
    public File getSourceFile(AdsDefinition def, AdsDefinition.ESaveMode saveMode) {
        for (RadixObject object = def; object != null; object = object.getContainer()) {
            if (object instanceof IUdsDirectoryRadixObject) {
                return new File(((IUdsDirectoryRadixObject)object).getDirectory() + File.separator + UdsDefinitionsUtils.getDefinitionFileName(def));
            }
        }
        return null;
    }
    
    @Override
    public void save(AdsDefinition def, AdsDefinition.ESaveMode saveMode) throws IOException {
        File sourceFile = getSourceFile(def, null);
        if (def instanceof UdsDefinition) {
            if (sourceFile == null) {
                throw new IOException("Definition " + def.getQualifiedName() + " is not file-based");
            }
            FileUtils.mkDirs(sourceFile.getParentFile());

            UdsDefinitionDocument xDoc = UdsDefinitionDocument.Factory.newInstance();
            UdsDefinitionDocument.UdsDefinition xDef = xDoc.addNewUdsDefinition();
            ((UdsDefinition) def).appendTo(xDef);
                XmlFormatter.save(xDoc, sourceFile);
                def.setFileLastModifiedTime(sourceFile.lastModified());
            def.setEditState(EEditState.NONE);
        } else {
            AdsDefinitionDocument xDoc = AdsDefinitionDocument.Factory.newInstance();
            AdsDefinitionElementType xDef = xDoc.addNewAdsDefinition();
            xDef.setFormatVersion(def.getFormatVersion());
            def.appendTo(xDef, AdsDefinition.ESaveMode.NORMAL);
            save(xDoc, def, sourceFile);
            AdsLocalizingBundleDef lb = getModule().getDefinitions().findLocalizingBundleDef(def, false);
            if (lb != null) {
                getModule().save(lb);
            }
        }
    }
    
    private void save(AdsDefinitionDocument xDoc, AdsDefinition def,  File sourceFile) throws IOException {
        synchronized (def) {
            if (def instanceof IXmlDefinition) {
                XmlUtils.saveWithoutCR(xDoc, null, sourceFile);
            } else {
                XmlFormatter.save(xDoc, sourceFile);
            }
            def.setFileLastModifiedTime(sourceFile.lastModified());
        }
        def.setEditState(EEditState.NONE);
    }
    
    @Override
    public AdsDefinition findById(Id id) {
        if (id == null || isEmpty()) {
            return null;
        }
        for (RadixObject radixObject : list()){
            if (radixObject instanceof AdsDefinition){
                AdsDefinition def = (AdsDefinition) radixObject;
                if (id.compareTo(def.getId()) == 0){
                    return def;
                }
            } else if (radixObject instanceof UdsFile){
                AdsDefinition result = ((UdsFile) radixObject).findById(id);
                if (result != null){
                    return result;
                }
            } else if (radixObject instanceof UdsFileRadixObjects){
                AdsDefinition result = ((UdsFileRadixObjects) radixObject).findById(id);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }
    
    
    @Override
    protected CanPasteResult canPaste(List<Transfer> transfers, DuplicationResolver resolver) {
        CanPasteResult result = super.canPaste(transfers, resolver);
        if (result != CanPasteResult.YES) {
            return result;
        }
        if (isReadOnly()) {
            return CanPasteResult.NO;
        }
        for (Transfer transfer : transfers) {
            final RadixObject radixObject = transfer.getObject();
            if (radixObject instanceof AdsDefinition){
                final AdsDefinition def = (AdsDefinition) radixObject;
                if (findById(def.getId()) != null) {
                    return CanPasteResult.NO_DUPLICATE;
                }
            } else {
                if (getRadixObjectByFileName(radixObject.getName())!= null){
                    return CanPasteResult.NO_DUPLICATE;
                }
            }
        }
        return CanPasteResult.YES;
    }
}
