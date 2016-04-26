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
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JOptionPane;
import org.radixware.kernel.common.builder.api.IProgressHandle;
import org.radixware.kernel.common.builder.radixdoc.RadixdocOptions;
import org.radixware.kernel.common.builder.radixdoc.RadixdocSpecificator;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.ads.build.Cancellable;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.radixdoc.ERadixdocPhrase;
import org.radixware.kernel.common.radixdoc.IRadixdocDictionary;
import org.radixware.kernel.designer.subversion.util.RadixSvnUtils;
import org.radixware.kernel.designer.common.dialogs.build.ProgressHandleFactoryDelegate;
import org.radixware.kernel.designer.common.dialogs.components.tasks.AbstractTask;
import org.radixware.kernel.designer.common.dialogs.components.tasks.TaskProcessor;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;
import org.radixware.kernel.designer.common.general.utils.RadixMutex;
import org.radixware.kernel.radixdoc.html.FileProvider;
import org.radixware.kernel.radixdoc.html.HtmlRadixdocGenerator;
import org.radixware.kernel.radixdoc.html.LocalFileProvider;


public class RadixdocGenerator {

    private List<Module> modules;

    public RadixdocGenerator(List<Module> modules) {
        this.modules = modules;
    }

    private boolean generateXml(boolean includeDependencies, String branchLocatiopn, IProgressHandle documentationProgress, Cancellable cancellable) {
        final IRadixdocDictionary dictionary = new IRadixdocDictionary() {
            @Override
            public String getPhraseId(ERadixdocPhrase key) {
                return key.getKey();
            }
        };

        final RadixdocSpecificator specificator = RadixdocSpecificator.Factory.newInstance(
                RadixdocOptions.Factory.newInstance(modules, modules.get(0).getLayer().getLanguages(),
                branchLocatiopn, includeDependencies), dictionary, documentationProgress, cancellable);

        if (specificator.generate()) {
            modules = new ArrayList<>(specificator.getModules());

            RadixSvnUtils.refreshStatus(specificator.getFiles().toArray(new File[0]));
            return true;
        }

        return false;
    }

    private boolean generateHtml(final boolean compressToZip, final File output, final String branchLocatiopn, final IProgressHandle generationProgress, final Cancellable cancellable) {

        final Map<String, FileProvider.LayerEntry> layers = new HashMap<>();
        for (final Module module : modules) {
            FileProvider.LayerEntry layerEntry = layers.get(module.getLayer().getURI());

            if (layerEntry == null) {
                layerEntry = new FileProvider.LayerEntry(module.getLayer().getURI(), module.getLayer().getURI(), 1);
                layers.put(module.getLayer().getURI(), layerEntry);
            }
            layerEntry.addModule(new FileProvider.ModuleEntry(module.getSegmentType().getValue(), module.getName()));
        }

        if (compressToZip) {
            output.getParentFile().mkdirs();
        } else {
            output.mkdirs();
        }

        final org.radixware.kernel.radixdoc.html.RadixdocOptions options = new org.radixware.kernel.radixdoc.html.RadixdocOptions() {
            @Override
            public Set<EIsoLanguage> getLanguages() {
                return EnumSet.copyOf(modules.get(0).getLayer().getLanguages());
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
                return new LocalFileProvider(branchLocatiopn, output) {
                    @Override
                    protected Collection<FileProvider.LayerEntry> getLayers() {
                        return layers.values();
                    }
                };
            }
        };

        HtmlRadixdocGenerator.generate(options, generationProgress, cancellable);

        return true;

    }

    public void generate() {
        if (modules.isEmpty()) {
            return;
        }

        final RadixdocOptionPanel options = new RadixdocOptionPanel();
        final ModalDisplayer optionDisplayer = new ModalDisplayer(options, "Radixdoc options");

        if (!optionDisplayer.showModal()) {
            return;
        }
        
        options.saveOptions();

        if (!RadixMutex.getLongProcessLock().tryLock()) {
            DialogUtils.messageError("Radix documentation action is alredy running");
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
        final String branchLocatiopn = modules.get(0).getBranch().getDirectory().getPath();

        try {
            final TaskProcessor processor = new TaskProcessor(new AbstractTask() {
                @Override
                public void run() {
                    if (generateXml(options.includeDependencies(), branchLocatiopn,
                            documentationProgress, cancellable)) {

                        if (generateHtml(options.compressToZip(), options.getOutputFile(), branchLocatiopn, generationProgress, cancellable)) {
                            if (options.openBrowser()) {
                                options.browse(modules.get(0).getLayer().getLanguages().get(0).getValue() + "/index.html");
                            }
                        }
                    }
                }
            });

            processor.start();

        } finally {
            RadixMutex.getLongProcessLock().unlock();
        }
    }
}
