/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.common.defs.ads.radixdoc;

import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProviderFactory;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.module.AdsImageDef;
import org.radixware.kernel.common.defs.localization.ILocalizingBundleDef;
import org.radixware.kernel.common.enums.ERepositorySegmentType;
import org.radixware.kernel.common.radixdoc.DefaultStyle;
import org.radixware.kernel.common.radixdoc.DocumentOptions;
import org.radixware.kernel.common.radixdoc.IRadixdocProvider;
import org.radixware.kernel.common.radixdoc.IResource;
import org.radixware.kernel.common.radixdoc.ModuleRadixdoc;
import org.radixware.kernel.common.radixdoc.RadixIconResource;
import org.radixware.schemas.radixdoc.Block;
import org.radixware.schemas.radixdoc.ContentContainer;
import org.radixware.schemas.radixdoc.Page;
import org.radixware.schemas.radixdoc.Ref;
import org.radixware.schemas.radixdoc.Resource;
import org.radixware.schemas.radixdoc.Table;

/**
 * Don't show AdsImageDef in definitions list.
 *
 * @author npopov
 */
public class AdsModuleRadixdoc extends ModuleRadixdoc {

    public AdsModuleRadixdoc(Module source, Page page, DocumentOptions options) {
        super(source, page, options);
    }

    @Override
    protected Block documentDefinitions(final ContentContainer container) {

        final SummaryChapterFactory<Module, AdsDefinition> dependencies = new SummaryChapterFactory<Module, AdsDefinition>(source) {
            @Override
            protected void documentElementSummary(AdsDefinition elem, Table table) {
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
                AdsDefinition descriptionProvider = AdsDefinitionRadixdoc.AdsPageWriter.findOwnerDescriptionDefinition(elem);
                if (descriptionProvider == null) {
                    descriptionProvider = elem;
                }
                if (descriptionProvider.getDescriptionId() != null && descriptionProvider.getDescriptionLocation() != null && descriptionProvider.getDescriptionLocation().findExistingLocalizingBundle() != null) {
                    getWriter().addMslId(descCell, descriptionProvider, descriptionProvider.getDescriptionLocation().findExistingLocalizingBundle().getId(), descriptionProvider.getDescriptionId());
                } else {
                    getWriter().addText(descCell, descriptionProvider.getDescription());
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
            protected boolean isApiElement(AdsDefinition elem) {
                if (elem instanceof AdsImageDef) {
                    return false;
                }
                return true;
            }

            @Override
            protected List<AdsDefinition> getAllElements(final Module c) {
                final ERepositorySegmentType segmentType = c.getSegment().getType();
                final List<AdsDefinition> topLevelDefs = new LinkedList<>();
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
                            topLevelDefs.add((AdsDefinition) radixObject);
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
}
