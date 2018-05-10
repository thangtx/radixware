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
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.builder.api.IProgressHandle;
import org.radixware.kernel.common.conventions.RadixdocConventions;
import org.radixware.kernel.common.defs.*;
import org.radixware.kernel.common.defs.ads.build.Cancellable;
import org.radixware.kernel.common.defs.ads.doc.DocResources;
import org.radixware.kernel.common.defs.ads.doc.DocTopicBody;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.dds.DdsModule;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.radixdoc.DefaultReferenceResolver;
import org.radixware.kernel.common.radixdoc.DocumentOptions;
import org.radixware.kernel.common.radixdoc.IRadixdocDictionary;
import org.radixware.kernel.common.radixdoc.IRadixdocPage;
import org.radixware.kernel.common.radixdoc.IRadixdocProvider;
import org.radixware.kernel.common.radixdoc.RadixdocSupport;
import org.radixware.kernel.common.utils.Base64;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.graphs.GraphWalker;
import org.radixware.schemas.radixdoc.DiagramRefItem;
import org.radixware.schemas.radixdoc.DocumentationItems;
import org.radixware.schemas.radixdoc.ModuleDocItem;
import org.radixware.schemas.radixdoc.ModuleDocumentationItemsDocument;
import org.radixware.schemas.radixdoc.RadixdocUnit;
import org.radixware.schemas.radixdoc.RadixdocUnitDocument;

public class RadixdocSpecificator {

    private static class CancellableWraper implements Cancellable {

        private final Cancellable source;

        public CancellableWraper(Cancellable source) {
            this.source = source;
        }

        @Override
        public boolean cancel() {
            if (source != null) {
                return source.cancel();
            }
            return false;
        }

        @Override
        public boolean wasCancelled() {
            if (source != null) {
                return source.wasCancelled();
            }
            return false;
        }
    }

    public static class Factory {

        public static RadixdocSpecificator newInstance(RadixdocOptions options, IRadixdocDictionary dictionary, IProgressHandle controller, Cancellable cancellable) {
            return new RadixdocSpecificator(options, dictionary, controller, cancellable);
        }
    }

    private final RadixdocOptions options;
    private final IProgressHandle progressHandle;
    private Set<Module> modules;
    private final Cancellable cancellable;
    private final IRadixdocDictionary dictionary;
    private final Set<File> files;
    private final DefaultReferenceResolver referenceResolver;
    private final AtomicInteger processed = new AtomicInteger();

    protected RadixdocSpecificator(RadixdocOptions options, IRadixdocDictionary dictionary, IProgressHandle progressHandle, Cancellable cancellable) {
        this.options = options;
        this.progressHandle = progressHandle;
        this.cancellable = new CancellableWraper(cancellable);
        this.dictionary = dictionary;
        this.files = new HashSet<>();
        this.referenceResolver = new DefaultReferenceResolver();
    }

    protected final IProgressHandle getProgressHandle() {
        return progressHandle;
    }

    protected final Cancellable getCancellable() {
        return cancellable;
    }

    public RadixdocOptions getOptions() {
        return options;
    }

    protected void collectAllModules() {
        modules = new HashSet<>();

        if (options.withDependencies()) {
            for (final Module startModule : options.getModules()) {

                new GraphWalker<Module>().breadthWalk(new GraphWalker.NodeFilter<Module>() {
                    @Override
                    protected boolean accept(Module node, int level) {
                        if (!node.isReadOnly() && node instanceof IRadixdocProvider
                                && ((IRadixdocProvider) node).isRadixdocProvider()) {

                            modules.add(node);
                            return true;
                        }
                        return false;
                    }

                    @Override
                    protected Collection<Module> collectNodes(Module source) {
                        final List<Module> childs = new ArrayList<>();

                        for (final Dependences.Dependence dependence : source.getDependences()) {
                            final List<Module> dependenceModule = dependence.findDependenceModule(source);

                            if (!dependenceModule.isEmpty()) {
                                childs.add(dependenceModule.get(0));
                            }
                        }

                        final Module overwritten = source.findOverwritten();
                        if (overwritten != null) {
                            childs.add(overwritten);
                        }

                        return childs;
                    }
                }, startModule);
            }
        } else {
            modules.addAll(options.getModules());
        }
    }

    protected void prepareDocumentModule(Module module) {
    }

    /**
     * Обходим все дефиниции в modules и есил они документированы создаем
     * файлики radixdoc.xml и radixdoc2.xml
     */
    protected void document() {
        final Map<Module, List<RadixdocSupport>> documents = new HashMap<>();

        progressHandle.switchToIndeterminate();
        int count = 0;

        for (final Module module : modules) {
            if (module.isReadOnly()) {
                continue;
            }

            final List<RadixdocSupport> pages = new ArrayList<>();
            module.visit(new IVisitor() {
                @Override
                public void accept(final RadixObject radixObject) {
                    if (radixObject instanceof Definition) {
                        if (!((Definition) radixObject).isPublished()) {
                            return;
                        }
                    }
                    final RadixdocSupport<? extends RadixObject> radixdocSupport = ((IRadixdocProvider) radixObject).getRadixdocSupport();
                    if (radixdocSupport != null) {
                        pages.add(radixdocSupport);
                    }
                }
            }, new VisitorProvider() {
                @Override
                public boolean isTarget(RadixObject radixObject) {
                    if (cancellable.wasCancelled()) {
                        cancel();
                    }
                    return radixObject instanceof IRadixdocProvider;
                }

                @Override
                public boolean isContainer(RadixObject radixObject) {
                    return true;
                }
            });

            documents.put(module, pages);
            count += pages.size();
        }

        if (cancellable.wasCancelled()) {
            return;
        }

        progressHandle.switchToDeterminate(count);
        processed.set(0);
        final ExecutorService service = Executors.newFixedThreadPool(Math.max(1, Math.min(8, Runtime.getRuntime().availableProcessors() - 1)), new ThreadFactory() {

            final AtomicInteger counter = new AtomicInteger();

            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setName("RadixDoc generator thread #" + counter.incrementAndGet());
                return t;
            }
        });
        for (final Map.Entry<Module, List<RadixdocSupport>> entry : documents.entrySet()) {
            service.submit(new Runnable() {

                @Override
                public void run() {
                    try {
                        generateRadixdoc(entry);
                    } catch (Exception ex) {
                        Logger.getLogger(RadixdocSpecificator.class.getName()).log(Level.SEVERE, "Exception while generating radixdoc for " + entry.getKey().getQualifiedName() + ": " + ExceptionTextFormatter.throwableToString(ex));
                    }
                }
            });
        }
        service.shutdown();
        try {
            service.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }

    }

    private void generateRadixdoc(final Map.Entry<Module, List<RadixdocSupport>> entry) {
        final List<RadixdocSupport> pages = entry.getValue();
        final RadixdocUnitDocument document = RadixdocUnitDocument.Factory.newInstance();
        final RadixdocUnit unit = document.addNewRadixdocUnit();

        final ModuleDocumentationItemsDocument moduleDocItemsDocument = ModuleDocumentationItemsDocument.Factory.newInstance();
        final ModuleDocItem moduleDocItem = moduleDocItemsDocument.addNewModuleDocumentationItems();

        moduleDocItem.setId(entry.getKey().getId());
        moduleDocItem.setName(entry.getKey().getName());
        moduleDocItem.setDefinitionType(EDefType.MODULE);
        moduleDocItem.setDescriptionId(entry.getKey().getDescriptionId());
        moduleDocItem.setIsDepricated(entry.getKey().isDeprecated());
        moduleDocItem.setIsFinal(entry.getKey().isFinal());
        moduleDocItem.setIsPublished(entry.getKey().isPublished());
        moduleDocItem.setModulePath(getLocation(entry.getKey()).getModulePath().toString());
        moduleDocItem.setLayerUri(entry.getKey().getLayer().getURI());

        final DocumentationItems docItems = moduleDocItem.addNewDocumentationItems();

        prepareDocumentModule(entry.getKey());
        final List<String> definitions = new ArrayList<>();

        if (entry.getKey() instanceof DdsModule) {
            DdsModule ddsModule = (DdsModule) entry.getKey();
            String diagram = ddsModule.getDiagramHtmlPage();
            if (diagram != null) {
                try {
                    diagram = Base64.encode(diagram.getBytes("UTF-8"));
                } catch (UnsupportedEncodingException ex) {
                    diagram = null;
                }
            }
            unit.addNewDdsDiagramHtmlPage().setPageTextAsStr(diagram);
            for (final Map.Entry<String, String> refItem : ddsModule.getDdsDiagramReference().entrySet()) {
                DiagramRefItem xRefItem = unit.getDdsDiagramHtmlPage().addNewDiagramRefItem();
                xRefItem.setItemId(refItem.getKey());
                xRefItem.setValue(refItem.getValue());
            }
            definitions.add("diagram");
        }

        for (final RadixdocSupport radixdocSupport : pages) {
            if (cancellable.wasCancelled()) {
                break;
            }

            try {
                IRadixdocPage page = radixdocSupport.document(unit.addNewPage(), new DocumentOptions(referenceResolver, dictionary));
                
                if (!page.isGenerateHtmlDoc()) {
                    continue;
                }
                
                page.buildPage();
                if (page.buildDocItem() != null) {
                    docItems.getDocumentationItemList().add(page.buildDocItem());
                }

                definitions.add(referenceResolver.getIdentifier(radixdocSupport.getSource()));
            } catch (Exception e) {
                Logger.getLogger(RadixdocSpecificator.class.getName()).log(Level.INFO, null, e);
            } finally {
                synchronized (progressHandle) {
                    progressHandle.progress("Document " + radixdocSupport.getSource().getQualifiedName(), processed.incrementAndGet());
                }
            }
        }

        unit.setPages(definitions);

        List<EIsoLanguage> languages = new ArrayList<EIsoLanguage>(options.getLanguages());

        // body dir
        File bodyDir = null;
        try {
            bodyDir = File.createTempFile(RadixdocConventions.TECHDOC_BODY_DIR_NAME, "");
            bodyDir.delete();
            bodyDir.mkdir();
        } catch (IOException ex) {
            Logger.getLogger(RadixdocSpecificator.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (EIsoLanguage lang : languages) {

            File langDir = new File(bodyDir, lang.getValue());
            if (!langDir.exists()) {
                langDir.mkdir();
            }

            //for (Module module : modules) {
            Module module = entry.getKey().getModule();
            if (!(module instanceof AdsModule)) {
                continue;
            }

            // copy
            try {
                File moduleBodyDir = DocTopicBody.getParentDir((AdsModule) module, lang);
                if (moduleBodyDir.exists()) {//RADIX-14969
                    FileUtils.copyDirectory(moduleBodyDir, langDir);
                }
            } catch (IOException ex) {
                Logger.getLogger(RadixdocSpecificator.class.getName()).log(Level.SEVERE, null, ex);
            }
            //}
        }

        // resource dir
        File resourceDir = null;
        try {
            resourceDir = File.createTempFile(RadixdocConventions.TECHDOC_RESOURCES_DIR_NAME, "");
            resourceDir.delete();
            resourceDir.mkdir();
        } catch (IOException ex) {
            Logger.getLogger(RadixdocSpecificator.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (EIsoLanguage lang : languages) {
            //for (Module module : modules) {
            Module module = entry.getKey().getModule();

            if (!(module instanceof AdsModule)) {
                continue;
            }

            // langDir
            File langDir = new File(resourceDir,
                    module.getLayer().getURI() + File.separator
                    + module.getName() + File.separator
                    + lang.getValue());
            if (!langDir.exists()) {
                langDir.mkdirs();
            }

            // copy
            try {
                DocResources resources = ((AdsModule)module).getDocumentation().getResources(lang);
                File moduleResourceDir = resources.getDir();
                if (moduleResourceDir.exists()) { //RADIX-14969
                    FileUtils.copyDirectory(moduleResourceDir, langDir);
                }
            } catch (IOException ex) {
                Logger.getLogger(RadixdocSpecificator.class.getName()).log(Level.SEVERE, null, ex);
            }

            //}
        }

        final PageLocation location = getLocation(entry.getKey());
        if (location != null) {
            processDocument(document(document, moduleDocItemsDocument, bodyDir, resourceDir, location), entry.getKey());
        }
    }

    public final Set<Module> getModules() {
        return modules;
    }

    protected boolean prepare() {
        return true;
    }

    protected void finish() {
    }

    public boolean generate() {
        try {
            progressHandle.start();
            progressHandle.setDisplayName("Generate documentation...");

            if (prepare()) {
                collectAllModules();
                document();
            }
        } finally {
            progressHandle.finish();
            finish();
        }

        return true;
    }

    private PageLocation getLocation(RadixObject object) {
        if (object == null) {
            return null;
        }

        final IRadixdocProvider provider = referenceResolver.findProvider(object);
        final Module module = object.getModule();
        if (provider != null && getModules().contains(module)) {

            final Path modulePath = Paths.get(module.getLayer().getURI(), module.getSegmentType().getValue(), module.getName());
            if (provider == object) {
                return new PageLocation(modulePath, referenceResolver.getIdentifier((RadixObject) provider), null);
            }
            return new PageLocation(modulePath, referenceResolver.getIdentifier((RadixObject) provider), referenceResolver.getIdentifier(object));
        }
        return null;
    }

    private void processDocument(IDocumentPage document, RadixObject source) {
        if (document == null) {
            return;
        }
        try {
            document.save(getOptions().getRootLocation());
            if (document.getFile() != null) {
                addFile(document.getFile());
            }
        } catch (IOException ex) {
            Logger.getLogger(RadixdocSpecificator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private IDocumentPage document(RadixdocUnitDocument document, ModuleDocumentationItemsDocument docItemsDocument, File bodyDir, File resourceDir, PageLocation location) {
        return new ZipXmlPage(document, docItemsDocument, bodyDir, resourceDir, location);
    }

    void addFile(File file) {
        synchronized (files) {
            files.add(file);
        }
    }

    public final Set<File> getFiles() {
        synchronized (files) {
            return Collections.unmodifiableSet(files);
        }
    }
}
