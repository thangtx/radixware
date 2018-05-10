package org.radixware.kernel.designer.ads.localization.merge;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import org.apache.xmlbeans.XmlException;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.util.Cancellable;
import org.radixware.kernel.common.constants.FileNames;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.Definitions;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import org.radixware.kernel.common.defs.dds.DdsLocalizingBundleDef;
import org.radixware.kernel.common.defs.dds.DdsModelManager;
import org.radixware.kernel.common.defs.dds.DdsModule;
import org.radixware.kernel.common.defs.localization.ILocalizingBundleDef;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.ads.fs.FSRepositoryAdsLocaleDefinition;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.common.svn.RadixSvnException;
import org.radixware.kernel.common.svn.SVN;
import org.radixware.kernel.common.svn.client.ISvnFSClient;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.Reference;
import org.radixware.kernel.common.utils.XmlFormatter;
import org.radixware.kernel.designer.ads.localization.source.MlsTablePanel;
import org.radixware.kernel.designer.common.dialogs.commitpanel.MergeCommitPanel;
import org.radixware.kernel.designer.common.dialogs.commitpanel.MicroCommitPanel;
import org.radixware.kernel.designer.common.dialogs.components.state.StateAbstractDialog;
import org.radixware.kernel.designer.common.dialogs.utils.InputOutputPrinter;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;
import org.radixware.kernel.designer.common.general.filesystem.RadixFileUtil;
import org.radixware.kernel.designer.subversion.util.RadixSvnUtils;
import org.radixware.kernel.designer.tree.actions.dds.DdsModuleCancelCaptureAction;
import org.radixware.kernel.designer.tree.actions.dds.DdsModuleCaptureAction;
import org.radixware.schemas.adsdef.AdsDefinitionDocument;
import org.radixware.schemas.adsdef.AdsDefinitionElementType;
import org.radixware.schemas.adsdef.LocalizedString;
import org.radixware.schemas.adsdef.LocalizingBundleDefinition;
import org.radixware.schemas.ddsdef.ModelDocument;
import org.radixware.schemas.ddsdef.ModelDocument.Model;
import org.tigris.subversion.svnclientadapter.SVNClientException;

public class LocalizingStringMergeAction extends AbstractAction {

    private final String STRING_MERGE = "Merge Strings";
    private final String PREPARE = "Prepare...";
    private final String COLLCTING_STRINGS = "Collecting strings...";
    private final MlsTablePanel component;
    private boolean isWork = false;


    public LocalizingStringMergeAction(MlsTablePanel panel) {
        super("MergeStrings", RadixWareIcons.MLSTRING_EDITOR.MERGE_STRINGS.getIcon(20));
        component = panel;
    }
    
    

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isWork) return;
        
        isWork = true;
        Map<Layer, List<Module>> selected = component.getSelectedLayers();
        Branch branch = RadixFileUtil.getOpenedBranches().iterator().next();
        final LocalizingStringMergeOptions optionsPanel = new LocalizingStringMergeOptions(branch, selected);
        StateAbstractDialog options = new StateAbstractDialog(optionsPanel, "Options") {};
        if (options.showModal()) {
          
            new Thread(new Runnable() {
                final Canceller cancellable = new Canceller();

                @Override
                public void run() {
                    synchronized (LocalizingStringMergeAction.this) {
                        final ProgressHandle progressHandle = ProgressHandleFactory.createHandle(STRING_MERGE, cancellable);
                        InputOutputPrinter inputOutputPrinter = new InputOutputPrinter(STRING_MERGE);
                        try {
                            progressHandle.start();
                            inputOutputPrinter.select();
                            inputOutputPrinter.reset();
                            inputOutputPrinter.printlnInfo("Operation 'Merge Strings' started.");
                            doMerge(optionsPanel.getOptions(), inputOutputPrinter, progressHandle, cancellable);
                            inputOutputPrinter.printlnInfo("Operation 'Merge Strings' finished.");
                        } catch (RadixSvnException | SVNClientException | URISyntaxException | IOException ex) {
                            try {
                                inputOutputPrinter.printlnError(ExceptionTextFormatter.exceptionStackToString(ex));
                            } catch (IOException ex1) {
                                Logger.getLogger(getClass().getName()).log(Level.INFO, ex.getMessage(), ex);
                            }
                        } finally{
                             progressHandle.finish();
                             isWork = false;
                        }
                        
                    }
                }
            }).start();
        } else {
            isWork = false;
        }

    }
    
    
    private void doMerge(final LocalizingStringOptions options, InputOutputPrinter inputOutput, ProgressHandle progressHandle, final Canceller cancellable) throws RadixSvnException, ISvnFSClient.SvnFsClientException, URISyntaxException, IOException, SVNClientException {
        Branch currentBranch = options.getBranch();
        final File fromBranchFile   = options.getFromBranchFile();
        final File toBranchFile     = options.getToBranchFile();
        final ISvnFSClient client;
        final RadixSvnUtils svn;
        final Map<DdsModule, MergeBundle> capturedModules = new HashMap<>();
        try {
            client = org.radixware.kernel.designer.subversion.util.SvnBridge.getClientAdapter(fromBranchFile);
            svn = RadixSvnUtils.Factory.newInstance(toBranchFile);
        } catch (SVNClientException | RuntimeException ex) {
            return;
        }
        if (svn == null) {
            inputOutput.printlnError("Branch file" + toBranchFile.getAbsolutePath() + "is not versioned");
            return;
        }
        
        if (cancellable.isCancelled){
            cancel(inputOutput, capturedModules);
            return;
        }


        progressHandle.progress(COLLCTING_STRINGS);
        inputOutput.println(COLLCTING_STRINGS);
        Map<ILocalizingBundleDef, List<IMultilingualStringDef>> filteredMap = new HashMap<>();
        int count = collectStrings(filteredMap, currentBranch, options, progressHandle, cancellable);
        if (cancellable.isCancelled){
            cancel(inputOutput, capturedModules);
            return;
        }
        
        inputOutput.printlnInfo("Found " + count + " source strings");
        inputOutput.println("Update bundles...");
        
        if (cancellable.isCancelled) {
            cancel(inputOutput, capturedModules);
            return;
        }
        progressHandle.progress("Collecting files...");
        List<MergeBundle> bundles = updateBundels(client, fromBranchFile, toBranchFile, filteredMap, progressHandle, cancellable);
        if (cancellable.isCancelled) {
            cancel(inputOutput, capturedModules);
            return;
        }
        inputOutput.println("Search strings in target bundles...");
        progressHandle.progress("Search strings...");
        int countNotFoundStrings = 0;
        List<MergeBundle> notEmptyBundles = new ArrayList<>();
        progressHandle.switchToDeterminate(bundles.size());
        int i = 0;
        Set<IMultilingualStringDef> notChangedStrings = new HashSet<>();
        Reference<Branch> branchRef = new Reference<>();
        for (MergeBundle mergeBundle : bundles){
            if (cancellable.isCancelled) {
                break;
            }
            ILocalizingBundleDef bundle = mergeBundle.getOldBundle();
            boolean isModifiedBundle = false;
            Set<IMultilingualStringDef> notChangedStringsInBundle = null;
            List<IMultilingualStringDef> modifiedStrings = filteredMap.get(bundle);
            List<IMultilingualStringDef> notFoundString = new ArrayList<>(modifiedStrings);
            if (bundle instanceof AdsLocalizingBundleDef) {
                final AdsLocalizingBundleDef adsBundle = (AdsLocalizingBundleDef) bundle;
                String message = "Search strings in " + adsBundle.getQualifiedName();
                progressHandle.progress(message, i);

                if (cancellable.isCancelled) {
                    break;
                }
                for (MergeFile file : mergeBundle.getBundleFiles()) {
                    Set<IMultilingualStringDef> notChangeInFile = new HashSet<>();
                    readAdsBundle(file, modifiedStrings, notFoundString, notChangeInFile, inputOutput, cancellable, true, mergeBundle, fromBranchFile, toBranchFile);
                    if (!file.isEmpty()) {
                        isModifiedBundle = true;
                    }

                    if (notChangedStringsInBundle == null) {
                        notChangedStringsInBundle = new HashSet<>(notChangeInFile);
                    } else {
                        notChangedStringsInBundle.retainAll(notChangeInFile);
                    }
                }

                if (cancellable.isCancelled) {
                    break;
                }
                countNotFoundStrings += notFoundString.size();
            } else if (bundle instanceof DdsLocalizingBundleDef) {
                DdsLocalizingBundleDef ddsLocalizingBundleDef = (DdsLocalizingBundleDef) bundle;
                String message = "Search strings in " + ddsLocalizingBundleDef.getQualifiedName();
                progressHandle.progress(message, i);
                File ddsModuleDir = mergeBundle.getDdsModuleDir();
                File modelfile = new File(ddsModuleDir, FileNames.DDS_MODEL_XML);
                DdsModule toModule;
                try {
                    inputOutput.println("Loading module from '" + modelfile.getAbsolutePath() + "'");
                    toModule = DdsModelManager.Support.loadAndGetDdsModule(modelfile, branchRef);
                    if (toModule == null) {
                        inputOutput.printlnError("Can not load DDS Module from '" + modelfile.getAbsolutePath() + "' file");
                        continue;
                    }
                    final Layer ownerLayer = toModule.getLayer();
                    if (ownerLayer == null) {
                        continue;
                    }
                    final Branch b = ownerLayer.getBranch();
                    if (b == null) {
                        continue;
                    }
                    final File layerDir = ownerLayer.getDirectory();
                    if (!toModule.isReadOnly()) {
                        if (cancellable.isCancelled) {
                            break;
                        }
                        capturedModules.put(toModule, mergeBundle);
                        inputOutput.println("Capturing " + toModule.getQualifiedName() + "...");
                        final DdsModuleCaptureAction.Cookie cookie = new DdsModuleCaptureAction.Cookie(toModule, false);
                        cookie.run();
                        DdsModuleCaptureAction.Cookie.Status status = cookie.getStatus();
                        if (status != DdsModuleCaptureAction.Cookie.Status.COMPLITE) {
                            StringBuilder sb = new StringBuilder("Unable to capture DDS module '");
                            sb.append(toModule.getName());
                            sb.append("'. ");
                            switch (status) {
                                case LOCALE_DELETE:
                                    sb.append("File ")
                                            .append(DdsModelManager.MODIFIED_MODEL_XML_FILE_NAME)
                                            .append("exists in svn repository, but locally has been deleted.");
                                    break;
                                case IS_NOT_UP_TO_DATE:
                                    sb.append("Unable to capture DDS module '")
                                            .append(toModule.getName())
                                            .append("'because it's owner DDS segment versioning status is not up to date.");
                                    break;
                                case RECAPTURE:
                                    sb.append("DDS module '").append(toModule.getName()).append("' is already captured");
                            }
                            inputOutput.printlnWarning(sb.toString());
                            capturedModules.remove(toModule);
                            continue;
                        }
                        final File modifyFile = new File(ddsModuleDir + "/" + FileNames.DDS_MODEL_MODIFIED_XML);
                        final ModelDocument modelDocument = ModelDocument.Factory.parse(modifyFile);
                        mergeBundle.setModel(modelDocument);
                        
                        if (cancellable.isCancelled) {
                            break;
                        }

                        //collect languges
                        Set<EIsoLanguage> languages = new HashSet<>();
                        
                        languages.addAll(ownerLayer.getLanguages());
                        final String modulebsolutePath = ddsModuleDir.getAbsolutePath();
                        final String postfix = modulebsolutePath.substring(layerDir.getAbsolutePath().length());
                        for (Layer l : b.getLayers()) {
                            if (l.isLocalizing() && l.getBaseLayerURIs().contains(ownerLayer.getURI())) {
                                final File lDir = l.getDirectory();
                                if (lDir != null) {
                                    File file = new File(lDir.getAbsoluteFile() + postfix); 
                                    mergeBundle.addLocalizingDir(file);
                                    languages.addAll(l.getLanguages());
                                }
                            }
                        }
                        if (cancellable.isCancelled) {
                            break;
                        }

                        mergeBundle.addBundleFile(new MergeFile(modifyFile, languages, false));
                    } else {
                        for (Layer l : b.getLayers()) {
                            if (!l.isReadOnly() && l.isLocalizing() && l.getBaseLayerURIs().contains(ownerLayer.getURI())) {
                                List<EIsoLanguage> languages = l.getLanguages();
                                EIsoLanguage language = languages.get(0);
                                File localizingLayerDir = l.getDirectory();
                                String licalizingModuleDir = localizingLayerDir.getPath() + File.separatorChar + ddsModuleDir.getParentFile().getName() + File.separatorChar
                                        + ddsModuleDir.getName().substring(ddsModuleDir.getName().indexOf(File.separatorChar) + 1) + File.separatorChar;
                                String path = licalizingModuleDir + FileNames.DDS_LOCALE_DIR + File.separatorChar + language.getValue() + ".xml";
                                File modifyFile = new File(path);
                                mergeBundle.addBundleFile(new MergeFile(modifyFile, EnumSet.of(language), true));
                            }
                        }
                    }

                } catch (XmlException ex) {
                    message = "Can not read file'" + modelfile.getAbsolutePath() + "'";
                    inputOutput.printlnError(message);
                    Logger.getLogger(getClass().getName()).log(Level.INFO, message, ex);
                    continue;
                }
                
                List<MergeFile> files = mergeBundle.getBundleFiles();
                if (files.isEmpty()) {
                    continue;
                }
                
                
                for (MergeFile file : mergeBundle.getBundleFiles()) {
                    if (cancellable.isCancelled) {
                        break;
                    }
                    Set<IMultilingualStringDef> notChangeInFile = new HashSet<>();
                    final File toFile = file.getFile();
                    final boolean notExist = !toFile.exists();
                    if (notExist && !file.isLocalizing()) {
                        continue;
                    }
                    ModelDocument modelDocument = mergeBundle.getModel();
                    if (modelDocument != null) {
                        final Model xModel = modelDocument.getModel();
                        if (xModel.isSetStringBundle()) {
                            LocalizingBundleDefinition xBundle = xModel.getStringBundle();
                            findStrings(xBundle, file, modifiedStrings, notFoundString, notChangeInFile, cancellable);
                            if (!file.isEmpty()) {
                                isModifiedBundle = true;
                            }
                            if (notChangedStringsInBundle == null) {
                                notChangedStringsInBundle = new HashSet<>(notChangeInFile);
                            } else {
                                notChangedStringsInBundle.retainAll(notChangeInFile);
                            }
                        }
                        if (!isModifiedBundle) {
                            inputOutput.println("Cancel capture " + toModule.getQualifiedName());
                            final DdsModuleCancelCaptureAction.Cookie Cookie = new DdsModuleCancelCaptureAction.Cookie(toModule, true);
                            Cookie.run();
                            capturedModules.remove(toModule);
                        }
                    } else {
                        mergeBundle.setDestBundle(toModule.findExistingLocalizingBundle());
                        readAdsBundle(file, modifiedStrings, notFoundString, notChangeInFile, inputOutput, cancellable, false, mergeBundle, fromBranchFile, toBranchFile);
                        if (!file.isEmpty()) {
                            isModifiedBundle = true;
                        }

                        if (notChangedStringsInBundle == null) {
                            notChangedStringsInBundle = new HashSet<>(notChangeInFile);
                        } else {
                            notChangedStringsInBundle.retainAll(notChangeInFile);
                        }
                    }
                }
                
                countNotFoundStrings += notFoundString.size();
            }
            
            if (isModifiedBundle){
               notEmptyBundles.add(mergeBundle);
            }
           
            if (notChangedStringsInBundle != null) {
                notChangedStrings.addAll(notChangedStringsInBundle);
            }
            i++;
        }
        
        if (cancellable.isCancelled) {
            cancel(inputOutput, capturedModules);
            return;
        }
        progressHandle.progress("Search strings...");
        progressHandle.switchToIndeterminate();
        
        inputOutput.printlnInfo(countNotFoundStrings + " strings not found in target branch");
        inputOutput.printlnInfo(notChangedStrings.size() + " strings in target branch have no differences");
        
        if (notEmptyBundles.isEmpty()){
            inputOutput.printlnWarning("There are no modified strings");
            cancelCapture(inputOutput, capturedModules);
            return;
        }

        
        if (cancellable.isCancelled) {
            cancel(inputOutput, capturedModules);
            return;
        }

       progressHandle.progress("Merge strings...");
        //Show Commit Message
        ModalDisplayer modal = new ModalDisplayer(new MergeStringsInfoPanel(bundles), "Modified strings") {};
        if (!modal.showModal()) {
            cancel(inputOutput, capturedModules);
            return;
        }
        
        if (cancellable.isCancelled) {
            cancel(inputOutput, capturedModules);
            return;
        }
        
        progressHandle.switchToDeterminate(notEmptyBundles.size());
        i = 0;
        bundles.clear();
         int fileCount = 0;
        for (MergeBundle bundle : notEmptyBundles) {
            i++;
            ILocalizingBundleDef bundleDef = bundle.getOldBundle();
            String name = "";
            if (bundleDef instanceof RadixObject) {
                final RadixObject adsBundle = (RadixObject) bundleDef;
                name = adsBundle.getQualifiedName();
            }
            progressHandle.progress("Merge " + name, i);
            for (MergeFile file : bundle.getBundleFiles()) {
                if (file.mergeStrings()) {
                    fileCount++;
                    if (!bundles.contains(bundle)) {
                        bundles.add(bundle);
                    }
                }
            }
        }
        inputOutput.println("Merging " + fileCount + " files");
        if (bundles.isEmpty()){
            inputOutput.printlnWarning("There are no modified files");
            cancelCapture(inputOutput, capturedModules);
            return;
        }
        
        progressHandle.progress("Committing...");
        MergeCommitPanel panel = new MergeCommitPanel();
        panel.setText(MicroCommitPanel.getLastCommitMessage());
        ModalDisplayer dlg = new ModalDisplayer(panel, "Commit");
        if (!dlg.showModal()) {
            cancel(inputOutput, capturedModules);
            return;
        }
        panel.saveConfigurationOptions();
        String comment = panel.getMessage();
        
        if (cancellable.isCancelled) {
            cancel(inputOutput, capturedModules);
            return;
        }
        
        inputOutput.println("Save files..."); 
//        final SVNRepositoryAdapter.Editor editor = repository.createEditor(comment);
        List<File> filesForUpdate = new ArrayList<>();
        try {
            for (MergeBundle bundle : bundles) {
                ILocalizingBundleDef oldBundle = bundle.getOldBundle();
                if (oldBundle instanceof AdsLocalizingBundleDef) {
                    for (MergeFile file : bundle.getBundleFiles()) {
                        saveBundle(file, (AdsLocalizingBundleDef) oldBundle, svn, filesForUpdate);
                    }
                } else if (oldBundle instanceof DdsLocalizingBundleDef) {
                    List<MergeFile> files = bundle.getBundleFiles();
                    ModelDocument modelDocument = bundle.getModel();
                    for (MergeFile file : files) {
                        if (file.isMerged()) {
                            if (modelDocument != null) {
                                File toFile = file.getFile();
                                Model xModel = modelDocument.getModel();
                                if (xModel.isSetStringBundle()) {
                                    LocalizingBundleDefinition xBundle = xModel.getStringBundle();
                                    xBundle.assignStringList(file.getValues());
                                    XmlFormatter.save(modelDocument, toFile);
                                }
                            } else {
                                saveBundle(file, (DdsLocalizingBundleDef) oldBundle, svn, filesForUpdate);
                            }
                        }
                    }
                }
            }
            for (DdsModule module : capturedModules.keySet()) {
                MergeBundle bundle = capturedModules.get(module);
                module.getModelManager().reloadModels();
                module.getModelManager().switchModelToFixedState();
                File file = module.getDirectory();
                filesForUpdate.add(file);
                for (File dir : bundle.getLocalizingDirs()) {
                    if (svn.isNewFile(dir)) {
                        svn.addDirectory(dir);
                    }
                    filesForUpdate.add(dir);
                }
                
            }
        } catch(RuntimeException e){
            inputOutput.printlnError(ExceptionTextFormatter.exceptionStackToString(e));
            try {
                cancelCapture(inputOutput, capturedModules);
                return;
            } catch (Exception ex) {
                Logger.getLogger(LocalizingStringMergeAction.class.getName()).log(Level.FINE, ex.getMessage(), ex);
                inputOutput.printlnError(ExceptionTextFormatter.exceptionStackToString(ex));
            }
        }
        inputOutput.println("Commiting changes..."); 
        File[] files = filesForUpdate.toArray(new File[filesForUpdate.size()]);
        SVN.commit(client, files, comment);
    }
    
    private void findStrings(LocalizingBundleDefinition xBundle, MergeFile file, List<IMultilingualStringDef> modifiedStrings, List<IMultilingualStringDef> notFoundString, Set<IMultilingualStringDef> notChangeInFile, final Canceller cancellable) {
        List<LocalizedString> list = xBundle.getStringList();
        file.setValues(list);

        for (IMultilingualStringDef string : modifiedStrings) {

            if (cancellable.isCancelled) {
                break;
            }

            for (LocalizedString localizedString : list) {
                if (localizedString.getId().compareTo(string.getId()) == 0) {
                    file.addSaveString(string, localizedString);
                    notFoundString.remove(string);
                    if (!file.isStringModified(string)) {
                        notChangeInFile.add(string);
                        file.removeSaveString(string);
                    }
                    break;
                }
            }
        }

    }
    
    private void cancel(InputOutputPrinter inputOutput, final Map<DdsModule, MergeBundle> capturedModules) throws IOException{
        inputOutput.printlnError("Operation cancelled by user");
        cancelCapture(inputOutput, capturedModules);
    }
    
    private void cancelCapture(InputOutputPrinter inputOutput, final Map<DdsModule, MergeBundle> capturedModules) throws IOException {
        if (capturedModules.isEmpty()) {
            return;
        }
        inputOutput.println("Cancel capture...");
        for (DdsModule module : capturedModules.keySet()) {
            final DdsModuleCancelCaptureAction.Cookie cookie = new DdsModuleCancelCaptureAction.Cookie(module, true);
            cookie.run();        
        }
    }
    
    private int collectStrings(final Map<ILocalizingBundleDef, List<IMultilingualStringDef>> filteredMap, Branch currentBranch, final LocalizingStringOptions options, final ProgressHandle progressHandle,  final Canceller cancellable) {
        final long from = options.getTimeFrom();
        final long to = options.getTimeTo();
        final String lastModifiedAuthor = options.getLastModifiedAuthor();
        Map<Layer, List<Module>> layer2Modules = options.getLayers();
        List<Module> modulesList;
        final int[] count = new int[1];
        count[0] = 0;
        for (Layer layer : currentBranch.getLayers().getInOrder()) {
            if (layer.isLocalizing()) {
                continue;
            }
            if (layer2Modules == null || layer2Modules.containsKey(layer)) {
                modulesList = layer2Modules == null ? null : layer2Modules.get(layer);
                final List<EIsoLanguage> langs = new LinkedList<>();
                if (!layer.isReadOnly()) {
                    langs.addAll(layer.getLanguages());
                }
                for (Layer l : currentBranch.getLayers()) {
                    if (l.isLocalizing() && l.getBaseLayerURIs().contains(layer.getURI())) {
                        langs.addAll(l.getLanguages());
                    }
                }
                if (langs.isEmpty()) {
                    continue;
                }
                
                if (modulesList != null){
                    
                    progressHandle.switchToDeterminate(modulesList.size());
                    int i = 1;
                    for (Module module : modulesList) {
                        progressHandle.progress("Collecting strings from " + module.getQualifiedName(), i);
                        module.visit(new IVisitor() {

                            @Override
                            public void accept(RadixObject radixObject) {
                                ILocalizingBundleDef bundle = (ILocalizingBundleDef) radixObject;
                                Definitions<? extends IMultilingualStringDef> list = bundle.getStrings().getLocal();
                                List<IMultilingualStringDef> modifiedStrings = new ArrayList<>();
                                for (IMultilingualStringDef stringDef : list) {
                                    if (stringDef != null) {
                                        boolean skip = true;
                                        for (EIsoLanguage lang : langs) {
                                            boolean skipByTime = true;
                                            if (from > 0 || to > 0) {
                                                Date lct = stringDef.getLastChangeTime(lang);
                                                if (lct != null) {
                                                    long lmt = lct.getTime();
                                                    if (from > 0) {
                                                        if (to > 0) {
                                                            if (lmt >= from && lmt <= to) {
                                                                skipByTime = false;
                                                            }
                                                        } else {
                                                            if (lmt >= from) {
                                                                skipByTime = false;
                                                            }
                                                        }
                                                    } else {
                                                        if (to > 0) {
                                                            if (lmt <= to) {
                                                                skipByTime = false;
                                                            }
                                                        } else {
                                                            skipByTime = false;
                                                        }
                                                    }
                                                }
                                            } else {
                                                skipByTime = false;
                                            }
                                            if (!skipByTime) {
                                                String lca = stringDef.getLastChangeAuthor(lang);
                                                if (lastModifiedAuthor == null) {
                                                    skip = false;
                                                    break;
                                                } else {
                                                    if (lca != null) {
                                                        if (lastModifiedAuthor.trim().toUpperCase().equals(lca.trim().toUpperCase())) {
                                                            skip = false;
                                                            break;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        if (skip) {
                                            continue;
                                        }

                                        modifiedStrings.add(stringDef);
                                    }
                                }

                                if (!modifiedStrings.isEmpty()) {
                                    if (filteredMap.containsKey(bundle)) {
                                        List<IMultilingualStringDef> strings = filteredMap.get(bundle);
                                        for (IMultilingualStringDef string : modifiedStrings) {
                                            if (!strings.contains(string)) {
                                                strings.add(string);
                                            }
                                        }
                                        count[0] += strings.size();
                                        filteredMap.put(bundle, strings);
                                    } else {
                                        count[0] += modifiedStrings.size();
                                        filteredMap.put(bundle, modifiedStrings);
                                    }

                                }
                            }

                        }, new VisitorProvider() {

                            @Override
                            public boolean isTarget(RadixObject radixObject) {
                                return radixObject instanceof ILocalizingBundleDef;
                            }

                            @Override
                            public boolean isCancelled() {
                                return cancellable.isCancelled;
                            }
                            
                            
                        });
                        if (cancellable.isCancelled){
                            break;
                        }
                        i++;
                    }
                    
                }
                progressHandle.progress(COLLCTING_STRINGS);
                progressHandle.switchToIndeterminate();
                if (cancellable.isCancelled) {
                    break;
                }
            }
        }
        return count[0];
    }
    
    private List<MergeBundle> updateBundels(ISvnFSClient client, final File fromBranchFile, final File toBranchFile, Map<ILocalizingBundleDef, List<IMultilingualStringDef>> filteredMap, final ProgressHandle progressHandle, final Canceller cancellable) throws URISyntaxException, RadixSvnException {
        
        List<File> filesForUpdate = new ArrayList<>();
        List<MergeBundle> bundles = new ArrayList<>();
        progressHandle.switchToDeterminate(filteredMap.keySet().size());
        int i = 0;
        for (ILocalizingBundleDef bundle : filteredMap.keySet()){
            i++;
            if (cancellable.isCancelled) {
                break;
            }
            MergeBundle mergeBundle = new MergeBundle(bundle);
            if (bundle instanceof AdsLocalizingBundleDef) {
                final AdsLocalizingBundleDef adsBundle = (AdsLocalizingBundleDef) bundle;
                
                progressHandle.progress("Collecting files from " + adsBundle.getQualifiedName(), i);
                if (cancellable.isCancelled){
                    break;
                }
                List<File> files = new ArrayList<>();
                List<File> localizingFiles = new ArrayList<>();
                adsBundle.collectFiles(false, files, localizingFiles);
                files.addAll(localizingFiles);
                for (File file : files) {
                    if (cancellable.isCancelled) {
                        break;
                    }
                    final File toFile = getToFile(file, fromBranchFile, toBranchFile);

                    EIsoLanguage language;
                    File parent = toFile.getParentFile();
                    if (parent.exists()) {
                        if (!toFile.exists()) {
                            filesForUpdate.add(parent);
                        }
                    } else {
                        File segmentDir = parent.getParentFile().getParentFile().getParentFile();
                        if (localizingFiles.contains(file) && segmentDir.exists()) {
                            filesForUpdate.add(segmentDir);
                        } else {
                            continue;
                        }
                    }
                    
                    File parentFile = toFile.getParentFile();
                    try {
                        language = EIsoLanguage.getForValue(parentFile.getName());
                    } catch (NoConstItemWithSuchValueError ex) {
                        Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                        continue;
                    }

                    
                    if (localizingFiles.contains(file)) {
                        mergeBundle.addBundleFile(new MergeFile(toFile, EnumSet.of(language), true));
                    } else {
                        mergeBundle.addBundleFile(new MergeFile(toFile, EnumSet.of(language), false));
                    }
                    filesForUpdate.add(toFile);
                }

            } else if (bundle instanceof DdsLocalizingBundleDef) {
                final DdsLocalizingBundleDef ddsBundle = (DdsLocalizingBundleDef) bundle;
                Layer layer = ddsBundle.getLayer();
                if (layer == null) {
                    continue;
                }
                File layerDir = layer.getDirectory();
                if (layerDir == null) {
                    continue;
                }
                
                List<Layer> layers = ddsBundle.getLayers();
                if (layers.isEmpty()) {
                    continue;
                }
                
                
                DdsModule ddsModule = ddsBundle.getModule();
                if (ddsModule == null) {
                    continue;
                }
                   
                for (Layer l : layers) {
                    File dir = l.getDirectory();
                    if (dir == null) {
                        continue;
                    }
                    File ddsDir = new File(dir, FileNames.DDS_SEGMENT_NAME);
                    if (!ddsDir.exists()) {
                        continue;
                    }
                    final File toDir = getToFile(ddsDir, fromBranchFile, toBranchFile);
                    if (!filesForUpdate.contains(toDir)){
                        filesForUpdate.add(toDir);
                    }
                }
                
                File moduleDir = ddsModule.getDirectory();
                mergeBundle.setDdsModuleDir(getToFile(moduleDir, fromBranchFile, toBranchFile)); 
            }
            
            bundles.add(mergeBundle);
        }
        
        progressHandle.progress("Update...");  
        progressHandle.switchToIndeterminate();
        
        
        if (!cancellable.isCancelled){
            File[] files = filesForUpdate.toArray(new File[filesForUpdate.size()]);
            SVN.update(client, files);
        }
        
        return bundles;
    }
    
    private File getToFile(final File file, final File fromBranchFile, final File toBranchFile) {
        final String toFileAbsolutePath = file.getAbsolutePath();
        final String postfix = toFileAbsolutePath.substring(fromBranchFile.getAbsolutePath().length());
        return new File(toBranchFile.getAbsolutePath() + postfix);
    }
    
    private boolean readAdsBundle(MergeFile file, List<IMultilingualStringDef> modifiedStrings, List<IMultilingualStringDef> notFoundString, Set<IMultilingualStringDef> notChangeInFile, InputOutputPrinter inputOutput, final Canceller cancellable, boolean isAds, MergeBundle mergeBundle, File fromBranch, File toBranch) throws IOException {
        if (cancellable.isCancelled) {
            return false;
        }
        final File toFile = file.getFile();
        final boolean notExist = !toFile.exists();
        AdsDefinitionDocument definitionDocument = null;
        if (notExist) {
            if (file.isLocalizing()) {
                ILocalizingBundleDef bundle = mergeBundle.getDestBundle();
                if (bundle == null) {
                    loadAdsBundle(mergeBundle, fromBranch, toBranch, cancellable);
                    bundle = mergeBundle.getDestBundle();
                }
                if (bundle != null) {
                    EIsoLanguage lang = file.getLanguages().iterator().next();
                    definitionDocument = AdsDefinitionDocument.Factory.newInstance();
                    AdsDefinitionElementType xDef = definitionDocument.addNewAdsDefinition();
                    if (bundle instanceof AdsLocalizingBundleDef) {
                        ((AdsLocalizingBundleDef) bundle).appendTo(xDef.addNewAdsLocalizingBundleDefinition(), AdsDefinition.ESaveMode.NORMAL, lang);
                    } else {
                        ((DdsLocalizingBundleDef) bundle).appendTo(xDef.addNewAdsLocalizingBundleDefinition(), lang);
                    }
                }
                if (definitionDocument == null) {
                    return false;
                }
            }
        }
        if (definitionDocument == null) {
            try {
                definitionDocument = AdsDefinitionDocument.Factory.parse(toFile);
            } catch (XmlException | IOException ex) {
                inputOutput.printlnError("Can not read file " + toFile.getAbsolutePath());
                Logger.getLogger(getClass().getName()).log(Level.INFO, "Can not read file " + toFile.getAbsolutePath(), ex);
                return false;
            }
        }

        if (cancellable.isCancelled) {
            return false;
        }
        final AdsDefinitionElementType element = definitionDocument.getAdsDefinition();
        if (element.isSetAdsLocalizingBundleDefinition()) {
            LocalizingBundleDefinition xBundle = element.getAdsLocalizingBundleDefinition();
            if (isAds) {
                ILocalizingBundleDef toBundle = mergeBundle.getDestBundle();
                if (toBundle == null) {
                    toBundle = AdsLocalizingBundleDef.Factory.loadFrom(xBundle);
                } else {
                    ((AdsLocalizingBundleDef)toBundle).loadStrings(xBundle);
                }
                mergeBundle.setDestBundle(toBundle);
            }
            findStrings(xBundle, file, modifiedStrings, notFoundString, notChangeInFile, cancellable);
        }
        return true;
    }
    
    private void saveBundle(MergeFile file, Definition oldBundle, RadixSvnUtils svn, List<File> filesForUpdate) throws IOException, SVNClientException {
        if (file.isMerged()) {
            AdsDefinitionDocument xDoc = AdsDefinitionDocument.Factory.newInstance();
            AdsDefinitionElementType xDef = xDoc.addNewAdsDefinition();
            final LocalizingBundleDefinition xBundle = xDef.addNewAdsLocalizingBundleDefinition();

            xBundle.setId(oldBundle.getId());
            if (oldBundle instanceof AdsLocalizingBundleDef) {
                AdsLocalizingBundleDef oldBundleDef = (AdsLocalizingBundleDef) oldBundle;
                xDef.setFormatVersion(oldBundleDef.getFormatVersion());
                if (oldBundleDef.isOverwrite()) {
                    xBundle.setIsOverwrite(oldBundleDef.isOverwrite());
                }
            }
            xBundle.assignStringList(file.getValues());
            File modifyFile = file.getFile();
            File moduleDir = null;
            if (!modifyFile.exists()) {
                if (file.isLocalizing()) {
                    Module m = oldBundle.getModule();
                    File module = m.getFile();
                    String path = modifyFile.getAbsolutePath();
                    path = path.substring(0, path.indexOf(File.separatorChar + FileNames.DDS_LOCALE_DIR));
                    moduleDir = new File(path);
                    File f = new File(moduleDir, Module.MODULE_XML_FILE_NAME);
                    if (!moduleDir.exists()) {
                        FileUtils.copyFile(module, f);
                    }
                } else {
                    return;
                }
            }
            XmlFormatter.save(xDoc, modifyFile);
            if (moduleDir != null) {
                if (svn.isNewFile(moduleDir)) {
                    svn.addDirectory(moduleDir);
                    filesForUpdate.add(moduleDir);
                    return;
                }
                if (svn.isNewFile(modifyFile)) {
                    svn.addFile(modifyFile);
                }
            }
            filesForUpdate.add(modifyFile);
        }
    }
    
    private void loadAdsBundle(MergeBundle mergeBundle, File fromBranchFile, File toBranchFile, Canceller cancellable) {
        ILocalizingBundleDef bundle = mergeBundle.getOldBundle();
        if (bundle instanceof AdsLocalizingBundleDef) {
            AdsLocalizingBundleDef adsLocalizingBundleDef = (AdsLocalizingBundleDef) bundle;
            List<File> files = ((AdsLocalizingBundleDef) bundle).getFiles();
            for (File file : files) {
                if (cancellable.isCancelled) {
                    break;
                }
                final AdsDefinitionDocument definitionDocument;
                final File toFile = getToFile(file, fromBranchFile, toBranchFile);
                FSRepositoryAdsLocaleDefinition rep = new FSRepositoryAdsLocaleDefinition(toFile, adsLocalizingBundleDef.getId());
                try (InputStream is = rep.getData()) {
                    definitionDocument = AdsDefinitionDocument.Factory.parse(is);
                } catch (XmlException | IOException ex) {
                    continue;
                }
                final AdsDefinitionElementType element = definitionDocument.getAdsDefinition();
                if (element.isSetAdsLocalizingBundleDefinition()) {
                    LocalizingBundleDefinition xBundle = element.getAdsLocalizingBundleDefinition();
                    ILocalizingBundleDef toBundle = mergeBundle.getDestBundle();
                    if (toBundle == null) {
                        toBundle = AdsLocalizingBundleDef.Factory.loadFrom(xBundle);
                    } else {
                        ((AdsLocalizingBundleDef) toBundle).loadStrings(xBundle);
                    }
                    mergeBundle.setDestBundle(toBundle);
                }
            }
        }
    }
    
    private class Canceller implements Cancellable {

        private boolean isCancelled = false;

        @Override
        public boolean cancel() {
            isCancelled = true;
            return true;
        }
    }
}
