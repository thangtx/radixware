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
package org.radixware.kernel.designer.common.dialogs.radixdoc;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JOptionPane;
import org.openide.util.Exceptions;
import org.radixware.kernel.common.builder.api.IProgressHandle;
import org.radixware.kernel.common.builder.radixdoc.RadixdocOptions;
import org.radixware.kernel.common.builder.radixdoc.RadixdocSpecificator;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.ads.build.Cancellable;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.exceptions.RadixdocGenerationException;
import org.radixware.kernel.common.radixdoc.ERadixdocPhrase;
import org.radixware.kernel.common.radixdoc.IDocLogger;
import org.radixware.kernel.common.radixdoc.IRadixdocDictionary;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.ads.common.radixdoc.DesignerTechdocGenerationContext;
import org.radixware.kernel.designer.subversion.util.RadixSvnUtils;
import org.radixware.kernel.designer.common.dialogs.build.ProgressHandleFactoryDelegate;
import org.radixware.kernel.designer.common.dialogs.components.tasks.AbstractTask;
import org.radixware.kernel.designer.common.dialogs.components.tasks.TaskProcessor;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;
import org.radixware.kernel.designer.common.general.utils.RadixMutex;
import org.radixware.kernel.radixdoc.generator.ERadixdocOutputFormat;
import org.radixware.kernel.radixdoc.generator.RadixdocGenerationContext;
import org.radixware.kernel.radixdoc.generator.RadixdocGenerationTask;
import org.radixware.kernel.radixdoc.generator.TechdocGenerator;
import org.radixware.kernel.radixdoc.html.FileProvider;
import org.radixware.kernel.radixdoc.html.HtmlRadixdocGenerator;
import org.radixware.kernel.radixdoc.html.LocalFileProvider;

public class RadixdocGenerator {

    public final String TECHNICAL_DOCUMENTATION_DIR_NAME = "TechnicalDocumentation";
    public final String RADIX_DOCUMENTATION_DIR_NAME = "RadixDocumentation";
    public final String TECHNICAL_DOCUMENTATION_RESOURCES_DIR_NAME = "Resources";

    private List<Module> modules;

    public RadixdocGenerator(List<Module> modules) {
        this.modules = modules;
    }

    private boolean generateXml(boolean includeDependencies, List<EIsoLanguage> languages, String branchLocation,
            IProgressHandle documentationProgress, Cancellable cancellable) {
        final IRadixdocDictionary dictionary = new IRadixdocDictionary() {
            @Override
            public String getPhraseId(ERadixdocPhrase key) {
                return key.getKey();
            }
        };

        final RadixdocSpecificator specificator = RadixdocSpecificator.Factory.newInstance(
                RadixdocOptions.Factory.newInstance(modules, languages,
                        branchLocation, includeDependencies), dictionary, documentationProgress, cancellable);

        if (specificator.generate()) {
            modules = new ArrayList<>(specificator.getModules());

            RadixSvnUtils.refreshStatus(specificator.getFiles().toArray(new File[0]));
            return true;
        }

        return false;
    }

    private boolean generateTechnicalDoc(File outputFile, List<EIsoLanguage> languages, File radixDocDir, File resourcesDir, ERadixdocOutputFormat format) {

        // task
        Branch branch = modules.get(0).getBranch();

        List<String> layerUriList = new LinkedList<>();
        List<Id> rootMapIdList = new ArrayList<>();
        for (Module module : modules) {

            Layer layer = module.getLayer();
            Id rootMapId = layer.getRootDocMapDefId();
            if (rootMapId != null && !rootMapIdList.contains(rootMapId)) {
                rootMapIdList.add(rootMapId);
            }

            String layerUri = layer.getURI();
            if (!layerUriList.contains(layerUri)) {
                layerUriList.add(layerUri);
            }
        }
        List<List<Id>> inputRootMapIdList = new ArrayList<>();
        inputRootMapIdList.add(rootMapIdList);

        RadixdocGenerationTask docGenTask = new RadixdocGenerationTask(
                inputRootMapIdList,
                languages,
                outputFile,
                format,
                null,
                radixDocDir
        );

        // context-layerEntries
        final Map<String, FileProvider.LayerEntry> layerEntries = new HashMap<>();
        List<Layer> localizingLayers = new ArrayList<>();

        for (Layer layer : branch.getLayers()) {
            List<FileProvider.ModuleEntry> modules = new ArrayList<>();
            for (Module module : layer.getAds().getModules()) {
                modules.add(new FileProvider.ModuleEntry(layer.getAds().getName().toLowerCase(), module.getName()));
            }

            if (!layer.isLocalizing()) {
                layerEntries.put(layer.getURI(), new FileProvider.LayerEntry(layer.getURI(), layer.getURI(), 1, modules));
            } else {
                localizingLayers.add(layer);
            }
        }

        for (Layer layer : localizingLayers) {
            if (layer.getBaseLayerURIs() != null && !layer.getBaseLayerURIs().isEmpty()) {
                FileProvider.LayerEntry layerEntry = layerEntries.get(layer.getBaseLayerURIs().get(0));
                if (layer.getLanguages() != null && !layer.getLanguages().isEmpty()) {
                    layerEntry.addLocalizingLayer(layer.getURI(), layer.getLanguages().get(0));
                }
            }
        }

        // context-provider
        final LocalFileProvider provider;
        String path = branch.getFile().getParent();
        provider = new LocalFileProvider(path, null) {

            @Override
            public Collection<FileProvider.LayerEntry> getLayers() {
                return layerEntries.values();
            }
        };

        // context
        DesignerTechdocGenerationContext context = new DesignerTechdocGenerationContext(provider, layerUriList);

        // generate
        try {
            TechdocGenerator.generate(docGenTask, context);
        } catch (RadixdocGenerationException ex) {
            Exceptions.printStackTrace(ex);
            return false;
        }

        return true;
    }

    private boolean generateHtml(final boolean compressToZip, final File output,
            final List<EIsoLanguage> languages, final String branchLocatiopn,
            final IProgressHandle generationProgress, final Cancellable cancellable) {
        List<Layer> localizingLayers = new ArrayList<>();
        if (!modules.isEmpty()) {
            if (modules.get(0).getBranch() != null) {
                for (Layer layer : modules.get(0).getBranch().getLayers()) {
                    if (layer.isLocalizing()) {
                        localizingLayers.add(layer);
                    }
                }
            }
        }

        final Map<String, FileProvider.LayerEntry> layers = new HashMap<>();
        for (final Module module : modules) {
            if (!module.getLayer().isLocalizing()) {
                FileProvider.LayerEntry layerEntry = layers.get(module.getLayer().getURI());

                if (layerEntry == null) {
                    layerEntry = new FileProvider.LayerEntry(module.getLayer().getURI(), module.getLayer().getURI(), 1);
                    layers.put(module.getLayer().getURI(), layerEntry);
                }
                layerEntry.addModule(new FileProvider.ModuleEntry(module.getSegmentType().getValue(), module.getName()));
            }
        }

        for (Layer layer : localizingLayers) {
            if (layer.getBaseLayerURIs() != null && !layer.getBaseLayerURIs().isEmpty()) {
                FileProvider.LayerEntry layerEntry = layers.get(layer.getBaseLayerURIs().get(0));
                if (layerEntry != null && layer.getLanguages() != null && !layer.getLanguages().isEmpty()) {
                    layerEntry.addLocalizingLayer(layer.getURI(), layer.getLanguages().get(0));
                }
            }
        }

        if (compressToZip) {
            output.getParentFile().mkdirs();
        } else {
            output.mkdirs();
        }

        final org.radixware.kernel.radixdoc.html.IRadixdocOptions options = new org.radixware.kernel.radixdoc.html.IRadixdocOptions() {

            private final LocalFileProvider provider = new LocalFileProvider(branchLocatiopn, output) {
                @Override
                public Collection<FileProvider.LayerEntry> getLayers() {
                    return layers.values();
                }
            };

            @Override
            public Set<EIsoLanguage> getLanguages() {
                return EnumSet.copyOf(languages);
            }

            @Override
            public boolean isCompressToZip() {
                return compressToZip;
            }
            final Map<String, String> cache = new HashMap<>();

            @Override
            public String resolve(String layerUri) {

                if (!cache.containsKey(layerUri)) {
                    final String ref = JOptionPane.showInputDialog("Enter path to documentation location of '" + layerUri + "'");
                    cache.put(layerUri, ref);

                    return ref;
                } else {
                    return cache.get(layerUri);
                }
            }

            @Override
            public FileProvider getFileProvider() {
                return provider;
            }

            @Override
            public IDocLogger getDocLogger() {
                return null;
            }
        };

        HtmlRadixdocGenerator.generate(options, generationProgress, cancellable);

        return true;

    }

    public void generate() {
        if (modules.isEmpty()) {
            return;
        }

        final RadixdocOptionPanel options = new RadixdocOptionPanel(getAvailableLanguages(modules.get(0).getBranch()));
        final ModalDisplayer optionDisplayer = new ModalDisplayer(options, "Documentation options");

        if (!optionDisplayer.showModal()) {
            return;
        }

        options.saveOptions();

        if (!RadixMutex.getLongProcessLock().tryLock()) {
            DialogUtils.messageError("Radix documentation action is already running");
            return;
        }

        final ProgressHandleFactoryDelegate factory = new ProgressHandleFactoryDelegate();
        final Cancellable cancellable = new Cancellable() {
            private volatile boolean cancel = false;

            @Override
            public boolean cancel() {
                return cancel = true;
            }

            @Override
            public boolean wasCancelled() {
                return cancel;
            }
        };

        final IProgressHandle documentationProgress = factory.createHandle("Radix documentation", cancellable);
        final IProgressHandle generationProgress = factory.createHandle("Radix documentation", cancellable);
        final String branchLocation = modules.get(0).getBranch().getDirectory().getPath();
        final List<EIsoLanguage> languages = options.getExportLanguages();
        Collections.sort(languages);

        try {

            if (options.needsExportDoc() && languages.isEmpty()) {
                return;
            }

            final TaskProcessor processor = new TaskProcessor(new AbstractTask() {

                private void openRadixdocIfNeed(File file) {
                    if (options.openDocumentation()) {
                        if (languages != null && !languages.isEmpty()) {
                            options.browse(file.getName() + File.separator + languages.get(0).getValue() + "/index.html");
                        } else {
                            options.browse(file.getName() + File.separator + modules.get(0).getLayer().getLanguages().get(0).getValue() + "/index.html");
                        }
                    }
                }

                private void openTechdocIfNeed(File dir, String fileName) {
                    if (options.openDocumentation()) {
                        if (languages != null && languages.size() > 1) {
                            //options.browse(file.getName() + File.separator + languages.get(0).getValue() + "/index.html");
                            options.open(dir.getName() + File.separator + languages.get(0).getValue() + File.separator + fileName);
                        } else {
                            //options.browse(file.getName() + "/index.html");
                            options.open(dir.getName() + File.separator + fileName);
                        }
                    }
                }

                @Override
                public void run() {
                    if (generateXml(options.includeDependencies(), languages, branchLocation, documentationProgress, cancellable)) {

                        // HtmlDoc
                        File radixDocDir = new File(options.getOutputFile(), RADIX_DOCUMENTATION_DIR_NAME);
                        if (options.needsExportHTMLDoc()
                                && generateHtml(options.compressToZip(), radixDocDir, languages, branchLocation, generationProgress, cancellable)) {
                            openRadixdocIfNeed(radixDocDir);
                        }

                        // TechDoc
                        File techDocDir = new File(options.getOutputFile(), TECHNICAL_DOCUMENTATION_DIR_NAME);
                        File resourcesDir = new File(techDocDir, TECHNICAL_DOCUMENTATION_RESOURCES_DIR_NAME);

                        // Html
                        if (options.needsExportTechDocHtml() && generateTechnicalDoc(techDocDir, languages, radixDocDir, resourcesDir, ERadixdocOutputFormat.HTML5)) {
                            openTechdocIfNeed(techDocDir, "index.html");
                        }
                        // Pdf
                        if (options.needsExportTechDocPdf() && generateTechnicalDoc(techDocDir, languages, radixDocDir, resourcesDir, ERadixdocOutputFormat.PDF)) {
                            // TODO: !!
                            openTechdocIfNeed(techDocDir, "map.pdf");
                        }
                        // Doc
                        if (options.needsExportTechDocDoc() && generateTechnicalDoc(techDocDir, languages, radixDocDir, resourcesDir, ERadixdocOutputFormat.DOCX)) {
                            // TODO: !!
                            openTechdocIfNeed(techDocDir, "map.docx");
                        }
                    }
                }
            }
            );

            processor.start();

        } finally {
            RadixMutex.getLongProcessLock().unlock();
        }
    }

    private List<EIsoLanguage> getAvailableLanguages(Branch branch) {
        Set<EIsoLanguage> result = new HashSet<>();
        for (Layer layer : branch.getLayers()) {
            result.addAll(layer.getLanguages());
        }

        List<EIsoLanguage> resultList = new ArrayList<>(result);
        Collections.sort(resultList);

        return resultList;
    }
}
