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
package org.radixware.kernel.common.defs.ads.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.radixware.kernel.common.defs.IOverwritable;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjectInitializationPolicy;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.SearchResult;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinitions;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.IAdsFormPresentableClass;
import org.radixware.kernel.common.defs.ads.clazz.form.AdsFormHandlerClassDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AbstractFormPresentations;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPageDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParagraphExplorerItemDef;
import org.radixware.kernel.common.defs.ads.module.ModuleDefinitions;
import org.radixware.kernel.common.defs.ads.radixdoc.AdsAbstractUIRadixdoc;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.WriterUtils;
import org.radixware.kernel.common.defs.ads.src.ui.rwt.RwtWidgetWriter;
import org.radixware.kernel.common.defs.ads.type.AdsClassType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsUIType;
import org.radixware.kernel.common.defs.ads.type.IAdsTypeSource;
import org.radixware.kernel.common.defs.ads.ui.generation.AdsUIWriter;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtCustomPageEditorDef;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtUIDef;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtWidgetDef;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.radixdoc.DocumentOptions;
import org.radixware.kernel.common.radixdoc.IRadixdocPage;
import org.radixware.kernel.common.radixdoc.IRadixdocProvider;
import org.radixware.kernel.common.radixdoc.RadixdocSupport;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.AbstractDialogDefinition;
import org.radixware.schemas.radixdoc.Page;
import org.radixware.schemas.ui.Connection;
import org.radixware.schemas.ui.UI;

public abstract class AdsAbstractUIDef<T extends AdsAbstractUIDef> extends AdsDefinition implements IAdsTypeSource, IJavaSource, IRadixdocProvider, IOverwritable<T> {

    public static class WrittenWidgetSupport {

        private Set<AdsUIItemDef> writtenWidgets = new HashSet<>();

        public void writeWidgetUsage(AdsUIItemDef widget, CodePrinter printer) {
            writtenWidgets.add(widget);
            printer.print("get");
            printer.print(widget.getId());
            printer.print("()");
        }

        public void writeWidgetAccessors(AdsDefinition root, CodePrinter printer, JavaSourceSupport.UsagePurpose usagePurpose) {
            List<AdsUIItemDef> widgets = new ArrayList<>(writtenWidgets);
            Collections.sort(widgets, new Comparator<AdsUIItemDef>() {
                @Override
                public int compare(AdsUIItemDef o1, AdsUIItemDef o2) {
                    return o1.getId().compareTo(o2.getId());
                }
            });
            final Map<Id, AdsAbstractUIDef> uiAccessorIds = new HashMap<>();
            for (AdsUIItemDef w : widgets) {
                writeWidgetAccessor(root, w, printer, uiAccessorIds);
            }
            if (!uiAccessorIds.isEmpty()) {
                final List<Id> uiAccessors = new ArrayList<>(uiAccessorIds.keySet());
                Collections.sort(uiAccessors);
                for (final Id id : uiAccessors) {
                    AdsAbstractUIDef uiDef = uiAccessorIds.get(id);
                    if (uiDef != null) {
                        printer.println();
                        printer.println("@SuppressWarnings(\"deprecation\")");
                        printer.print("private ");
                        AdsType type = uiDef.getType(EValType.USER_CLASS, null);
                        JavaSourceSupport.CodeWriter writer = type.getJavaSourceSupport().getCodeWriter(usagePurpose);
                        writer.writeUsage(printer);
                        printer.print(" getCustomView$$$");
                        printer.print(id);
                        printer.print("() {return (");
                        writer.writeUsage(printer);
                        if (id.getPrefix() == EDefinitionIdPrefix.EDITOR_PAGE) {
                            printer.print(")getEditorPage(");
                            WriterUtils.writeIdUsage(printer, id);
                            printer.print(").getView();}");
                        } else {
                            printer.print(")getView();}");
                        }
                    }
                }
            }
        }

        private Id getEditorPageId(AdsUIItemDef widget) {
            RadixObject obj = widget.getOwnerDef();
            while (obj != null) {
                if (obj instanceof AdsEditorPageDef) {
                    return ((AdsEditorPageDef) obj).getId();
                }
                obj = obj.getContainer();
            }
            return null;
        }

        private AdsDefinition getParentForUI(AdsAbstractUIDef ui) {
            if (ui instanceof AdsCustomPageEditorDef) {
                return ((AdsCustomPageEditorDef) ui).getOwnerEditorPage().getOwnerDef();
            } else if (ui instanceof AdsRwtCustomPageEditorDef) {
                return ((AdsRwtCustomPageEditorDef) ui).getOwnerEditorPage().getOwnerDef();
            } else {
                return ui.getOwnerDef();
            }
        }

        private void writeWidgetAccessor(AdsDefinition root, AdsUIItemDef widget, CodePrinter printer, Map<Id, AdsAbstractUIDef> uiAccessorIds) {
            final AdsAbstractUIDef ui = widget.getOwnerUIDef();
            if (ui == null) {
                return;
            }

            try {
                AdsDefinition parent;
                Id editorPageId = null;
                if (ui.isTopLevelDefinition()) {
                    parent = ui;
                } else {
                    editorPageId = getEditorPageId(widget);
                    if (editorPageId != null) {
                        parent = getParentForUI(ui);
                    } else {
                        parent = ui.getOwnerDef();
                    }
                }
                boolean parentIsMatched = false;
                loop:
                if (root instanceof AdsDefinition) {
                    AdsDefinition rootTmp = (AdsDefinition) root;
                    while (rootTmp != null) {
                        AdsDefinition tmp = rootTmp;
                        while (tmp != null) {
                            if (tmp == parent || parent.isParentOf(tmp)) {
                                parentIsMatched = true;
                                break loop;
                            }
                            tmp = tmp.getHierarchy().findOverwritten().get();
                        }
                        if (rootTmp instanceof AdsClassDef) {
                            AdsType superType = ((AdsClassDef) rootTmp).getInheritance().getSuperClassRef().resolve(root).get();
                            if (superType instanceof AdsClassType) {
                                rootTmp = ((AdsClassType) superType).getSource();
                            } else {
                                rootTmp = null;
                            }
                        } else {
                            rootTmp = null;
                        }
                    }

                } else {
                    if (root == parent || parent.isParentOf(root)) {
                        parentIsMatched = true;
                    }
                }

                if (!parentIsMatched) {
                    printer.println("@SuppressWarnings(\"unused\")");
                }
                printer.print("private ");
                if (widget instanceof AdsWidgetDef && ui instanceof AdsUIDef) {
                    AdsUIWriter.writeWidgetType((AdsUIDef) ui, (AdsWidgetDef) widget, JavaSourceSupport.UsagePurpose.EXPLORER_EXECUTABLE, printer);
                } else if (widget instanceof AdsRwtWidgetDef && ui instanceof AdsRwtUIDef) {
                    RwtWidgetWriter.writeWidgetType((AdsRwtWidgetDef) widget, printer);
                }
                printer.printSpace();
                printer.print("get");
                printer.print(widget.getId());
                printer.println("(){");
                printer.enterBlock();

                if (parentIsMatched) {

                    printer.print("return getCustomView$$$");
                    if (editorPageId != null) {
                        uiAccessorIds.put(editorPageId, ui);
                        printer.print(editorPageId.toCharArray());
                    } else {
                        uiAccessorIds.put(widget.getOwnerUIDef().getId(), ui);
                        printer.print(widget.getOwnerUIDef().getId().toCharArray());
                    }
                    printer.print("() == null ? null : getCustomView$$$");
                    printer.print("");
                    if (editorPageId != null) {
                        printer.print(editorPageId.toCharArray());
                    } else {
                        printer.print(widget.getOwnerUIDef().getId().toCharArray());
                    }
                    printer.print("().");
                    printer.print(widget.getId().toString());
                } else {
                    printer.print("throw new UnsupportedOperationException(\"Widget '" + widget.getQualifiedName() + " (" + widget.getId().toString() + ")' is not a property of class \"  + getClass().getName())");
                }

                printer.printlnSemicolon();
            } finally {
                printer.leaveBlock();
            }
            printer.println('}');
        }
    }

    public class Connections extends RadixObjects<AdsUIConnection> {

        private Connections() {
            super(AdsAbstractUIDef.this);
        }

        private void loadFrom(UI ui) {
            if (ui != null && ui.getConnections() != null) {
                for (Connection c : ui.getConnections().getConnectionList()) {
                    add(new AdsUIConnection(c));
                }
            }
        }

        private void appendTo(UI ui) {
            if (!isEmpty()) {
                org.radixware.schemas.ui.Connections ccs = ui.addNewConnections();
                for (AdsUIConnection c : this) {
                    c.appendTo(ccs.addNewConnection());
                }
            }
        }

        public AdsUIConnection get(AdsUIItemDef widget, String signal) {
            for (AdsUIConnection conn : this) {
                if (widget.getId().equals(conn.getSenderId()) && signal.equals(conn.getSignalName())) {
                    return conn;
                }
            }
            return null;
        }

        public Set<AdsUIConnection> getBySlot(Id id) {
            Set<AdsUIConnection> set = new HashSet<>();
            for (AdsUIConnection conn : this) {
                if (id.equals(conn.getSlotId())) {
                    set.add(conn);
                }
            }
            return set;
        }
    }
    private final Connections connections = new Connections();
    private final NodeUpdateSupport nodeUpdateSupport = new NodeUpdateSupport();
    private boolean isDeprecated;

    protected AdsAbstractUIDef(Id id, AbstractDialogDefinition xDef) {
        super(id, xDef);

        if (xDef.isSetIsDeprecated()) {
            isDeprecated = xDef.getIsDeprecated();
        } else {
            isDeprecated = false;
        }
        if (!RadixObjectInitializationPolicy.get().isRuntime()){
            connections.loadFrom(xDef.getUi());
        }
    }

    public Connections getConnections() {
        return connections;
    }

    @Override
    public boolean allowOverwrite() {
        return true;
    }

    @Override
    public boolean isSuitableContainer(AdsDefinitions collection) {
        return collection instanceof ModuleDefinitions;
    }

    protected AdsAbstractUIDef(Id id, String name) {
        super(id, name);
        isDeprecated = false;
    }

    @Override
    public boolean isDeprecated() {
        return isDeprecated || super.isDeprecated();
    }

    public void setDeprecated(boolean isDeprecated) {
        if (this.isDeprecated != isDeprecated) {
            this.isDeprecated = isDeprecated;
            setEditState(EEditState.MODIFIED);
        }
    }

    public NodeUpdateSupport getNodeUpdateSupport() {
        return nodeUpdateSupport;
    }

    public void fireNodeUpdate() {
        nodeUpdateSupport.fireEvent(new NodeUpdateSupport.NodeUpdateEvent(this));
    }

    @Override
    public AdsType getType(EValType typeId, String extStr) {
        return new AdsUIType(this);
    }

    protected void appendTo(AbstractDialogDefinition xDef, ESaveMode saveMode) {
        super.appendTo(xDef, saveMode);
        if (isDeprecated) {
            xDef.setIsDeprecated(true);
        }
        if (xDef.getUi() == null) {
            xDef.addNewUi();
        }
        connections.appendTo(xDef.getUi());
    }

    public abstract AdsUIItemDef getWidget();

    protected final String getClassName() {
        return getId().toString();
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        connections.visit(visitor, provider);

    }

    public AdsModelClassDef getModelClass() {
        for (RadixObject container = getContainer(); container != null; container = container.getContainer()) {
            if (container instanceof AdsPresentationDef) {
                return ((AdsPresentationDef) container).findFinalModel();
            }
            if (container instanceof AdsParagraphExplorerItemDef) {
                return ((AdsParagraphExplorerItemDef) container).getModel();
            }
            if (container instanceof AdsFormHandlerClassDef) {
                return ((AdsFormHandlerClassDef) container).getPresentations().getModel();
            }
            if (container instanceof AdsReportClassDef) {
                return ((AdsReportClassDef) container).getPresentations().getModel();
            }
        }
        return null;
    }

    @Override
    public RadixdocSupport<? extends RadixObject> getRadixdocSupport() {
        return new RadixdocSupport<AdsAbstractUIDef>(this) {
            @Override
            public IRadixdocPage document(Page page, DocumentOptions options) {
                return new AdsAbstractUIRadixdoc(AdsAbstractUIDef.this, page, options);
            }
        };
    }

    @Override
    public boolean isRadixdocProvider() {
        return true;
    }

    @Override
    public Hierarchy<AdsAbstractUIDef> getHierarchy() {
        return new UIDefHierarchy();
    }

    private class UIDefHierarchy extends DefaultHierarchy<AdsAbstractUIDef> {

        public UIDefHierarchy() {
            super();
        }

        @Override
        public SearchResult<AdsAbstractUIDef> findOverridden() {
            return SearchResult.empty();
        }

        @Override
        public SearchResult<AdsAbstractUIDef> findOverwritten() {
            if (isTopLevelDefinition()) {
                return super.findOverwritten();
            } else {
                final AdsDefinition owner = getUIDefOwner();
                if (owner == null) {
                    return SearchResult.empty();
                } else {
                    final List<AdsAbstractUIDef> result = new LinkedList<>();
                    final List<AdsDefinition> emptyOwners = new LinkedList<>();
                    final Set<AdsDefinition> visitedOwners = new HashSet<>();
                    emptyOwners.add(owner);
                    while (!emptyOwners.isEmpty()) {
                        final List<AdsDefinition> owners = new ArrayList<>(emptyOwners);
                        emptyOwners.clear();
                        for (AdsDefinition o : owners) {
                            if (visitedOwners.contains(o)) {
                                continue;
                            }
                            visitedOwners.add(o);
                            o.getHierarchy().findOverwritten().iterate(new SearchResult.Acceptor<AdsDefinition>() {

                                @Override
                                public void accept(AdsDefinition object) {
                                    final AdsAbstractUIDef def = findInOwner(object);
                                    if (def != null) {
                                        result.add(def);
                                    } else {
                                        emptyOwners.add(object);
                                    }
                                }
                            });
                        }
                    }

                    return result.isEmpty() ? SearchResult.<AdsAbstractUIDef>empty() : SearchResult.list(result);
                }
            }
        }

        private AdsDefinition getUIDefOwner() {
            for (AdsDefinition def = getOwnerDef(); def != null; def = def.getOwnerDef()) {
                if (def instanceof AdsEditorPageDef) {
                    return def;
                } else if (def instanceof AdsPresentationDef) {
                    return def;
                } else if (def instanceof AdsParagraphExplorerItemDef) {
                    return def;
                } else if (def instanceof IAdsFormPresentableClass) {
                    return def;
                }
            }

            return null;
        }

        private AdsAbstractUIDef findInOwner(AdsDefinition owner) {
            if (owner instanceof AdsEditorPageDef) {
                AdsEditorPageDef page = (AdsEditorPageDef) owner;
                return page.getCustomViewSupport().getCustomView(getUsageEnvironment());
            } else if (owner instanceof AdsEditorPresentationDef) {
                AdsEditorPresentationDef pr = (AdsEditorPresentationDef) owner;
                if (!pr.isCustomViewInherited()) {
                    return pr.getCustomViewSupport().getCustomView(getUsageEnvironment());
                }
            } else if (owner instanceof AdsSelectorPresentationDef) {
                AdsSelectorPresentationDef pr = (AdsSelectorPresentationDef) owner;
                if (!pr.isCustomViewInherited()) {
                    return pr.getCustomViewSupport().getCustomView(getUsageEnvironment());
                }
            } else if (owner instanceof IAdsFormPresentableClass) {
                IAdsFormPresentableClass clazz = (IAdsFormPresentableClass) owner;
                if (!clazz.getPresentations().isCustomViewInherited()) {
                    return clazz.getPresentations().getCustomViewSupport().getCustomView(getUsageEnvironment());
                }
            }
            return null;
        }
    }
}
