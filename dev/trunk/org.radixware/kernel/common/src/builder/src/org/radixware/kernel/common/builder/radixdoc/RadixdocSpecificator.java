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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.builder.api.IProgressHandle;
import org.radixware.kernel.common.defs.*;
import org.radixware.kernel.common.defs.ads.build.Cancellable;
import org.radixware.kernel.common.defs.dds.DdsModule;
import org.radixware.kernel.common.radixdoc.DefaultReferenceResolver;
import org.radixware.kernel.common.radixdoc.DocumentOptions;
import org.radixware.kernel.common.radixdoc.IRadixdocDictionary;
import org.radixware.kernel.common.radixdoc.IRadixdocProvider;
import org.radixware.kernel.common.radixdoc.RadixdocSupport;
import org.radixware.kernel.common.utils.Base64;
import org.radixware.kernel.common.utils.graphs.GraphWalker;
import org.radixware.schemas.radixdoc.DiagramRefItem;
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
        int processed = 0;

        for (final Map.Entry<Module, List<RadixdocSupport>> entry : documents.entrySet()) {
            final List<RadixdocSupport> pages = entry.getValue();
            final RadixdocUnitDocument document = RadixdocUnitDocument.Factory.newInstance();
            final RadixdocUnit unit = document.addNewRadixdocUnit();

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
                    radixdocSupport.document(unit.addNewPage(), new DocumentOptions(referenceResolver, dictionary)).buildPage();
                    definitions.add(referenceResolver.getIdentifier(radixdocSupport.getSource()));
                } catch (Exception e) {
                    Logger.getLogger(RadixdocSpecificator.class.getName()).log(Level.INFO, null, e);
                } finally {
                    progressHandle.progress("Document " + radixdocSupport.getSource().getQualifiedName(), ++processed);
                }
            }

            unit.setPages(definitions);

            final PageLocation location = getLocation(entry.getKey());
            if (location != null) {
                processDocument(document(document, location), entry.getKey());
            }
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

    private IDocumentPage document(RadixdocUnitDocument document, PageLocation location) {
        return new ZipXmlPage(document, location);
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
