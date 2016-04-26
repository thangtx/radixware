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

package org.radixware.kernel.common.radixdoc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.Dependences;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProviderFactory;
import org.radixware.kernel.common.defs.localization.ILocalizedDescribable;
import org.radixware.kernel.common.defs.localization.ILocalizingBundleDef;
import org.radixware.kernel.common.enums.ERepositorySegmentType;
import org.radixware.kernel.common.utils.graphs.GraphWalker;
import org.radixware.schemas.radixdoc.Block;
import org.radixware.schemas.radixdoc.ContentContainer;
import org.radixware.schemas.radixdoc.ElementList;
import org.radixware.schemas.radixdoc.Page;
import org.radixware.schemas.radixdoc.Ref;
import org.radixware.schemas.radixdoc.Resource;
import org.radixware.schemas.radixdoc.Table;


public class ModuleRadixdoc extends RadixdocXmlPage<Module> {

    private final PageWriter<Module> writer;

    public ModuleRadixdoc(Module source, Page page, DocumentOptions options) {
        super(source, page, options);

        this.writer = new PageWriter<>(this);
    }
    
    @Override
    public void buildPage() {
        page.setName(source.getId().toString());
        page.setTitle(source.getName());
        page.setTopLevel(true);
        page.setType("Module");

        final RadixIconResource iconResource = new RadixIconResource(source.getIcon());
        addResource(iconResource);

        page.setIcon(iconResource.getKey());

        documentPageHead(page);

        documentDescription(page);

        documentBoby(page);
    }

    protected List<Module> moduleHierarchy() {
        final List<Module> modules = new ArrayList<>();
        modules.add(source);
        modules.addAll(source.findAllOverwritten());
        return modules;
    }

    protected Block documentBoby(Page page) {
        final Block body = page.addNewBlock();
        body.setStyle(DefaultStyle.CHAPTER);

        documentDependencies(body);
        documentDefinitions(body);

        return body;
    }

    protected Block documentDependencies(final ContentContainer container) {

        final SummaryChapterFactory<Module, Module> dependencies = new SummaryChapterFactory<Module, Module>(source) {
            @Override
            protected void documentElementSummary(Module elem, Table table) {
                final Table.Row row = table.addNewRow();
                final Table.Row.Cell nameCell = row.addNewCell();
                final Ref ref = nameCell.addNewRef();
                final Resource resource = ref.addNewResource();
                final IResource res = new RadixIconResource(elem.getIcon());
                resource.setSource(res.getKey());
                addResource(res);
                ref.addNewText().setStringValue(elem.getQualifiedName(source));
                ref.setPath(resolve(source, elem));

                final Table.Row.Cell descCell = row.addNewCell();
                getWriter().addText(descCell, elem.getDescription(), false);
            }

            @Override
            protected Block createChapter() {
                final Block dependenciesChapter = getWriter().createChapter(container, "Dependencies");
                getWriter().addStrTitle(dependenciesChapter, "Dependencies");
                return dependenciesChapter;
            }

            @Override
            protected String getElementTypeName() {
                return "Module";
            }

            @Override
            protected List<Module> getAllElements(Module c) {
                final List<Module> modules = new ArrayList<>();

                for (final Dependences.Dependence dependence : c.getDependences().list()) {
                    final List<Module> result = dependence.findDependenceModule(c);
                    if (result != null && !result.isEmpty()) {
                        modules.add(result.get(0));
                    }
                }
                return modules;
            }

            @Override
            protected void prepareElementsTable(Module module, Table table) {
                table.setStyle(DefaultStyle.MEMBERS);
            }
        };

        return dependencies.document(moduleHierarchy());
    }

    protected Block documentDefinitions(final ContentContainer container) {

        final SummaryChapterFactory<Module, Definition> dependencies = new SummaryChapterFactory<Module, Definition>(source) {
            @Override
            protected void documentElementSummary(Definition elem, Table table) {
                final Table.Row row = table.addNewRow();
                final Table.Row.Cell nameCell = row.addNewCell();
                final Ref ref = nameCell.addNewRef();
                final Resource resource = ref.addNewResource();
                final IResource res = new RadixIconResource(elem.getIcon());
                resource.setSource(res.getKey());
                addResource(res);
                ref.addNewText().setStringValue(elem.getQualifiedName(source));
                ref.setPath(resolve(source, elem));

                final Table.Row.Cell descCell = row.addNewCell();
                if (elem instanceof ILocalizedDescribable) {
                    final ILocalizedDescribable adsDefinition = (ILocalizedDescribable) elem;
                    if (adsDefinition.getDescriptionId() != null && elem.getDescriptionLocation() != null && elem.getDescriptionLocation().findExistingLocalizingBundle() != null) {
                        getWriter().addMslId(descCell, elem.getDescriptionLocation().findExistingLocalizingBundle().getId(), adsDefinition.getDescriptionId());
                    } else {
                        getWriter().addText(descCell, elem.getDescription());
                    }
                } else {
                    descCell.addNewText().setStringValue(elem.getDescription());
                }
            }

            @Override
            protected Block createChapter() {
                final Block definitionsChapter = getWriter().createChapter(container, "Definitions");
                getWriter().addStrTitle(definitionsChapter, "Definitions");

                return definitionsChapter;
            }

            @Override
            protected String getElementTypeName() {
                return "Definition";
            }

            @Override
            protected List<Definition> getAllElements(final Module c) {
                final ERepositorySegmentType segmentType = c.getSegment().getType();
                final List<Definition> topLevelDefs = new LinkedList<>();
                c.visit(new IVisitor() {
                    @Override
                    public void accept(RadixObject radixObject) {
                        if (radixObject == c) {
                            return;
                        }
                        if (radixObject.getModule() == null) {
                            return;
                        }
                        if (radixObject instanceof ILocalizingBundleDef) {
                            return;
                        }
                        if (radixObject instanceof Definition && ((Definition) radixObject).isPublished()) {
                            Definition match = radixObject.getOwnerDefinition();
                            if (segmentType == ERepositorySegmentType.DDS) {
                                match = match.getOwnerDefinition();
                            }
                            if (match != c) {
                                return;
                            }
                            if (segmentType == ERepositorySegmentType.DDS) {
                                if (radixObject instanceof IRadixdocProvider) {
                                    if (!((IRadixdocProvider) radixObject).isRadixdocProvider()) {
                                        return;
                                    }
                                } else {
                                    return;
                                }
                            }
                            topLevelDefs.add((Definition) radixObject);
                        }

                    }
                }, VisitorProviderFactory.createDefaultVisitorProvider());

                return topLevelDefs;
            }

            @Override
            protected void prepareElementsTable(Module module, Table table) {
                table.setStyle(DefaultStyle.MEMBERS);
            }
        };

        return dependencies.document(moduleHierarchy());
    }

    protected Block documentPageHead(Page page) {
        final Block headChapter = page.addNewBlock();
        headChapter.setStyle(DefaultStyle.HEAD);

        final Block subTitleBlock = headChapter.addNewBlock();
        subTitleBlock.setStyle(DefaultStyle.SUB_TITLE);
        subTitleBlock.addNewText().setStringValue("Layer ");
        final Ref moduleRef = subTitleBlock.addNewRef();
        moduleRef.setPath(resolve(source, source.getLayer()));
        moduleRef.addNewText().setStringValue(source.getLayer().getQualifiedName());

        final Block titleBlock = headChapter.addNewBlock();
        titleBlock.setStyle(DefaultStyle.TITLE);
        final Resource icon = titleBlock.addNewResource();
        final RadixIconResource iconResource = new RadixIconResource(source.getIcon());
        icon.setSource(iconResource.getKey());
        addResource(iconResource);

        getWriter().addStrTitle(titleBlock, source.getTypeTitle() + " " + source.getName());
        return headChapter;
    }

    protected Block documentDescription(Page page) {

        final Block descriptionChapter = page.addNewBlock();
        descriptionChapter.setStyle(DefaultStyle.DESCRIPTION);

        final List<Module> hierarchy = new LinkedList<>();

        new GraphWalker<Module>().depthWalk(new GraphWalker.NodeFilter<Module>() {
            @Override
            protected boolean accept(Module node, int level) {
                hierarchy.add(node);
                return true;
            }

            @Override
            protected Collection<Module> collectNodes(Module source) {
                final Module overwritten = source.findOverwritten();
                final List<Module> result = new ArrayList<>();
                if (overwritten != null) {
                    result.add(overwritten);
                }
                return result;
            }
        }, source);

        if (hierarchy.size() > 1) {
            final ElementList hierarchyList = descriptionChapter.addNewList();
            hierarchyList.setMeta(DefaultMeta.List.HIERARCHY);
            for (final Module node : hierarchy) {
                final ElementList.Item item = hierarchyList.addNewItem();
                if (node == source) {
                    item.addNewText().setStringValue(node.getQualifiedName());
                } else {
                    final Ref ref = item.addNewRef();
                    ref.addNewText().setStringValue(node.getQualifiedName());
                    ref.setPath(resolve(source, node));
                }
            }
        }

        if (source.getDescription() != null && !source.getDescription().isEmpty()) {
            final Block descriptionDefinition = descriptionChapter.addNewBlock();
            descriptionDefinition.setStyle(DefaultStyle.DEFINITION);

            final Block title = descriptionDefinition.addNewBlock();
            title.setMeta(DefaultMeta.Text.TITLE);
            title.setStyle(DefaultMeta.Text.TITLE);
            getWriter().addLocalizedText(title, ERadixdocPhrase.DESCRIPTION);
            getWriter().addText(title, ":");

            getWriter().addText(descriptionDefinition.addNewBlock(), source.getDescription(), false);
        }

        if (source.isTest()) {
            final Block attributes = descriptionChapter.addNewBlock();
            attributes.setStyle(DefaultStyle.DEFINITION);

            final Block title = attributes.addNewBlock();
            title.setMeta(DefaultMeta.Text.TITLE);
            getWriter().addLocalizedText(title, ERadixdocPhrase.ATTRIBUTES);
            getWriter().addText(title, ":");

            getWriter().addLocalizedText(attributes, ERadixdocPhrase.TEST_MODULE);
        }

        return descriptionChapter;
    }

    @Override
    public PageWriter<Module> getWriter() {
        return writer;
    }
}
