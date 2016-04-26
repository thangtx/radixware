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
package org.radixware.kernel.common.builder.check.ads.clazz.presentations;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.radixware.kernel.common.builder.check.ads.CheckUtils;
import org.radixware.kernel.common.builder.check.common.RadixObjectChecker;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProviderFactory;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPageDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.EditorPages;
import org.radixware.kernel.common.enums.EEditorPageType;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.ColumnInfo;
import org.radixware.kernel.common.defs.ads.clazz.members.ColumnProperty;
import org.radixware.kernel.common.defs.ads.clazz.members.ParentRefProperty;
import org.radixware.kernel.common.defs.ads.clazz.members.ParentReferenceInfo;
import org.radixware.kernel.common.defs.ads.clazz.members.ServerPresentationSupport;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.IAdsPresentableProperty;
import org.radixware.kernel.common.defs.ads.clazz.presentation.PropertyUsageSupport;
import org.radixware.kernel.common.defs.ads.clazz.presentation.PropertyUsageSupport.PropertyRef;
import org.radixware.kernel.common.defs.ads.common.AdsUtils;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsExplorerItemDef;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.enums.EEditPossibility;
import org.radixware.kernel.common.enums.ERestriction;
import org.radixware.kernel.common.types.Id;

@RadixObjectCheckerRegistration
public class EditorPagesChecker extends RadixObjectChecker<EditorPages> {

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return EditorPages.class;
    }

    @Override
    public void check(final EditorPages pages, final IProblemHandler problemHandler) {
        super.check(pages, problemHandler);
        final List<Id> usedPageIds = new ArrayList<>();
        final Definition pagesOwner = pages.getOwnerDefinition();
        final boolean isEditorPresentatonPages = pagesOwner instanceof AdsEditorPresentationDef;
        final AdsEditorPresentationDef editorPresentation = isEditorPresentatonPages ? (AdsEditorPresentationDef) pagesOwner : null;
        final Map<Id, AdsPropertyDef> allProps = new HashMap<>();
        final boolean shouldCheckPropDoubleEdit = editorPresentation != null && !editorPresentation.getRestrictions().isDenied(ERestriction.UPDATE);
        pages.visit(new IVisitor() {
            @Override
            public void accept(final RadixObject radixObject) {
                if (radixObject instanceof EditorPages.OrderedPage) {
                    final EditorPages.OrderedPage orderedPage = (EditorPages.OrderedPage) radixObject;

                    final AdsEditorPageDef page = orderedPage.findPage();
                    if (page == null) {
                        problemHandler.accept(RadixProblem.Factory.newError(radixObject, "Unknown page in page order: #" + orderedPage.getPageId().toString()));
                    } else {
//                        EditorPages.OrderedPage ownerOrder = orderedPage.getOwnerOrderedPage();
//                        if (ownerOrder != null) {                            
//                            AdsEditorPageDef ownerPage = ownerOrder.findPage();
//                            if (ownerPage != null) {
//                                if (ownerPage.getClientEnvironment() != ERuntimeEnvironmentType.COMMON_CLIENT && ownerPage.getClientEnvironment() != page.getClientEnvironment()) {
//                                    problemHandler.accept(RadixProblem.Factory.newError(radixObject, "Editor page with client environment " + page.getClientEnvironment().getName() + " can not be contained by page with client environment " + ownerPage.getClientEnvironment().getName()));
//                                }
//                            }
//                        }

                        AdsUtils.checkAccessibility(pages, page, false, problemHandler);
//                        RADIX-10977
//                        if (!orderedPage.getSubpages().isEmpty()) {
//                            if ((page.getType() == EEditorPageType.STANDARD && !page.getProperties().isEmpty()) || page.getType() == EEditorPageType.CONTAINER) {
//                                problemHandler.accept(RadixProblem.Factory.newError(radixObject, "Editor page " + orderedPage.getQualifiedName() + " must not contain child pages"));
//                            }
//                        }
                        if (page.getType() == EEditorPageType.CONTAINER) {
                            if (isEditorPresentatonPages) {
                                if (!orderedPage.isOwnPage() && !orderedPage.isOwrPage()) {
                                    final Id eiId = page.getEmbeddedExplorerItemId();
                                    if (eiId != null) {
                                        AdsExplorerItemDef ei = editorPresentation.getExplorerItems().findChildExplorerItem(eiId);
                                        if (ei == null) {
                                            ei = page.findEmbeddedExplorerItem();
                                            if (ei == null) {
                                                problemHandler.accept(RadixProblem.Factory.newError(orderedPage, MessageFormat.format("An explorer item, referenced from {0} can not be used in context of presentation {1}", orderedPage.getQualifiedName(), editorPresentation.getQualifiedName())));
                                            } else {
                                                problemHandler.accept(RadixProblem.Factory.newError(orderedPage, MessageFormat.format("Explorer item {0},referenced from {1} can not be used in context of presentation {2}", ei.getQualifiedName(), orderedPage.getQualifiedName(), editorPresentation.getQualifiedName())));
                                            }
                                        }
                                    }
                                }
                            } else {
                                problemHandler.accept(RadixProblem.Factory.newError(radixObject, "Container editor pages are allowed only for editor presentation page set. Change page type or remove this page"));
                            }
                        } else if (shouldCheckPropDoubleEdit && page.getType() == EEditorPageType.STANDARD) {
                            PropertyUsageSupport pus = page.getUsedProperties();
                            for (PropertyRef ref : pus.get()) {
                                AdsDefinition def = ref.findProperty();
                                if (def instanceof AdsPropertyDef) {
                                    if (!allProps.containsKey(def.getId())) {
                                        allProps.put(def.getId(), (AdsPropertyDef) def);
                                    }
                                }
                            }
                        }
                    }
                    if (usedPageIds.contains(orderedPage.getPageId())) {
                        if (page == null) {
                            problemHandler.accept(RadixProblem.Factory.newError(radixObject, "Duplicate page reference: #" + orderedPage.getPageId()));
                        } else {
                            problemHandler.accept(RadixProblem.Factory.newError(radixObject, "Duplicate page reference: #" + page.getQualifiedName(orderedPage)));
                        }
                    }
                    usedPageIds.add(orderedPage.getPageId());
                }
            }
        }, VisitorProviderFactory.createDefaultVisitorProvider());

        final List<AdsEditorPageDef> visiblePages = pages.get(EScope.LOCAL);

        if (visiblePages.size() > 1) {
            for (AdsEditorPageDef page : visiblePages) {
                if (!page.isTitleInherited()) {
                    CheckUtils.titleShouldBeDefined(page, page.getTitleId(), problemHandler, getHistory());
                }
            }
        }
        if (shouldCheckPropDoubleEdit) {
            //check ref prop edit possibilities
            Map<DdsReferenceDef, List<AdsPropertyDef>> ref2Prop = null;
            Map<DdsColumnDef, List<AdsPropertyDef>> editableColumns = null;

            //columns, published with EditPossibility==NEVER
            Set<DdsColumnDef> publishedReadonly = null;

            for (AdsPropertyDef prop : allProps.values()) {
                if (prop instanceof IAdsPresentableProperty) {
                    ServerPresentationSupport support = ((IAdsPresentableProperty) prop).getPresentationSupport();
                    if (support != null && support.getPresentation() != null) {
                        EEditPossibility eps = support.getPresentation().getEditOptions().getEditPossibility();
                        if (eps == EEditPossibility.NEVER || eps == EEditPossibility.ON_CREATE) {
                            if (prop instanceof ColumnProperty) {
                                ColumnInfo colInfo = ((ColumnProperty) prop).getColumnInfo();
                                if (colInfo != null) {
                                    DdsColumnDef col = colInfo.findColumn();
                                    if (col != null) {
                                        if (publishedReadonly == null) {
                                            publishedReadonly = new HashSet<DdsColumnDef>();
                                        }
                                        publishedReadonly.add(col);
                                    }
                                }
                            }
                            continue;
                        }
                    }
                }

                if (prop instanceof ParentRefProperty) {
                    ParentReferenceInfo refInfo = ((ParentRefProperty) prop).getParentReferenceInfo();
                    if (refInfo != null) {
                        DdsReferenceDef ref = refInfo.findParentReference();
                        if (ref != null) {
                            if (ref2Prop == null) {
                                ref2Prop = new HashMap<DdsReferenceDef, List<AdsPropertyDef>>();
                            }
                            List<AdsPropertyDef> props = ref2Prop.get(ref);
                            if (props == null) {
                                props = new LinkedList<AdsPropertyDef>();
                                ref2Prop.put(ref, props);
                            }
                            props.add(prop);
                        }
                    }
                } else if (prop instanceof ColumnProperty) {
                    ColumnInfo colInfo = ((ColumnProperty) prop).getColumnInfo();
                    if (colInfo != null) {
                        DdsColumnDef col = colInfo.findColumn();
                        if (col != null) {
                            if (editableColumns == null) {
                                editableColumns = new HashMap<DdsColumnDef, List<AdsPropertyDef>>();
                            }
                            List<AdsPropertyDef> columnEditors = editableColumns.get(col);
                            if (columnEditors == null) {
                                columnEditors = new LinkedList<AdsPropertyDef>();
                                editableColumns.put(col, columnEditors);
                            }
                            if (!columnEditors.contains(prop)) {
                                columnEditors.add(prop);
                            }
                        }
                    }
                }
            }

            if (ref2Prop != null) {
                List<DdsReferenceDef> refList = new ArrayList<DdsReferenceDef>(ref2Prop.keySet());

                class Info {

                    List<AdsPropertyDef> refs;
                    List<AdsPropertyDef> simples;

                    void addRef(AdsPropertyDef ref) {
                        if (refs == null) {
                            refs = new LinkedList<AdsPropertyDef>();
                            refs.add(ref);
                        } else if (!refs.contains(ref)) {
                            refs.add(ref);
                        }
                    }

                    void addSimple(AdsPropertyDef simple) {
                        if (simples == null) {
                            simples = new LinkedList<AdsPropertyDef>();
                            simples.add(simple);
                        } else if (!simples.contains(simple)) {
                            simples.add(simple);
                        }
                    }

                    boolean hasRefs() {
                        return refs != null && refs.size() > 1;
                    }

                    boolean hasOneRef() {
                        return refs != null && !refs.isEmpty();
                    }

                    boolean hasSimples() {
                        return simples != null && !simples.isEmpty();
                    }
                }
                Map<DdsColumnDef, Info> warnings = new HashMap<DdsColumnDef, Info>();
                for (int i = 0, len = refList.size(); i < len; i++) {
                    DdsReferenceDef ref1 = refList.get(i);
                    Id tableId1 = ref1.getChildTableId();

                    List<DdsColumnDef> columns = null;
                    for (int j = i + 1; j < len; j++) {
                        DdsReferenceDef ref2 = refList.get(j);
                        Id tableId2 = ref2.getChildTableId();
                        if (tableId1 == tableId2) {
                            if (columns == null) {
                                columns = new LinkedList<DdsColumnDef>();
                                DdsReferenceDef.ColumnsInfoItems columnList1 = ref1.getColumnsInfo();
                                for (DdsReferenceDef.ColumnsInfoItem item1 : columnList1) {
                                    DdsColumnDef column = item1.findChildColumn();
                                    if (column == null) {
                                        continue;
                                    }
                                    columns.add(column);
                                }
                            }

                            DdsReferenceDef.ColumnsInfoItems columnList2 = ref2.getColumnsInfo();

                            for (DdsColumnDef column : columns) {
                                if ((publishedReadonly != null && publishedReadonly.contains(column)) || (editableColumns == null || !editableColumns.containsKey(column))) {
                                    continue;
                                }
                                Info warningProps = warnings.get(column);
                                for (DdsReferenceDef.ColumnsInfoItem item2 : columnList2) {
                                    if (column.getId() == item2.getChildColumnId()) {

                                        if (warningProps == null) {
                                            warningProps = new Info();
                                            warnings.put(column, warningProps);
                                        }
                                        for (List<AdsPropertyDef> list : new List[]{ref2Prop.get(ref1), ref2Prop.get(ref2)}) {
                                            for (AdsPropertyDef p : list) {
                                                warningProps.addRef(p);
                                            }
                                        }
                                    }
                                }
                                if (editableColumns != null) {
                                    List<AdsPropertyDef> props = editableColumns.get(column);
                                    if (props != null) {
                                        if (warningProps == null) {
                                            warningProps = new Info();
                                            warnings.put(column, warningProps);
                                        }
                                        for (AdsPropertyDef p : props) {
                                            warningProps.addSimple(p);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                for (Map.Entry<DdsColumnDef, Info> e : warnings.entrySet()) {
                    Info info = e.getValue();
                    if (info.hasRefs()) {
                        StringBuilder message = new StringBuilder();
                        boolean first = true;
                        for (AdsPropertyDef prop : info.refs) {
                            if (first) {
                                first = false;
                            } else {
                                message.append(", ");
                            }
                            message.append(prop.getQualifiedName());
                        }
                        if (!editorPresentation.isWarningSuppressed(AdsEditorPresentationDef.Problems.COLUMN_MULTI_EDIT_BY_REF)) {
                            warning(editorPresentation, problemHandler, AdsEditorPresentationDef.Problems.COLUMN_MULTI_EDIT_BY_REF, e.getKey().getQualifiedName(), message.toString());
                        }
                    }
                    if (info.hasSimples() && info.hasOneRef()) {
                        StringBuilder message = new StringBuilder();
                        boolean first = true;
                        for (AdsPropertyDef prop : info.refs) {
                            if (first) {
                                first = false;
                            } else {
                                message.append(", ");
                            }
                            message.append(prop.getQualifiedName());
                        }
                        if (!editorPresentation.isWarningSuppressed(AdsEditorPresentationDef.Problems.COLUMN_MULTI_EDIT_BY_REF_AND_PROP)) {
                            warning(editorPresentation, problemHandler, AdsEditorPresentationDef.Problems.COLUMN_MULTI_EDIT_BY_REF_AND_PROP, e.getKey().getQualifiedName(), message.toString());
                        }
                    }
                }
            }
        }
    }
}
