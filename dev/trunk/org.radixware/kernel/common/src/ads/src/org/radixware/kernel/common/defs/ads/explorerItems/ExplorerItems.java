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
package org.radixware.kernel.common.defs.ads.explorerItems;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.*;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinition.ESaveMode;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.AdsDefinitions;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsPresentationDef;

//import org.radixware.kernel.common.defs.ads.explorerItems.ExplorerItemsOrder.ExplorerItemsOperationSupport;
import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.exploreritems.ExplorerItemsWriter;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.RequestProcessor;
import org.radixware.schemas.adsdef.ChildExplorerItems;
import org.radixware.schemas.adsdef.ChildExplorerItems.Item;

public class ExplorerItems extends RadixObject implements IJavaSource {

    public static final class Factory {

        public static ExplorerItems newInstance(AdsDefinition owner) {
            return new ExplorerItems(owner, null);
        }

        public static ExplorerItems loadFrom(AdsDefinition owner, ChildExplorerItems xDef) {
            ExplorerItems eiis = new ExplorerItems(owner, xDef);
            return eiis;
        }
    }

    private static class LocalChildren extends AdsDefinitions<AdsExplorerItemDef> {

        @Override
        protected void onAdd(AdsExplorerItemDef definition) {
            super.onAdd(definition);
            fireEntireChange(this);
        }

        @Override
        protected void onRemove(AdsExplorerItemDef definition) {
            super.onRemove(definition); //To change body of generated methods, choose Tools | Templates.
            fireEntireChange(this);
        }

        @Override
        protected void onClear() {
            super.onClear(); //To change body of generated methods, choose Tools | Templates.v
            fireEntireChange(this);
        }

        @Override
        protected AdsDefinition findByIdForPasteAction(Id id) {
            AdsDefinition result = findById(id);
            if (result != null) {
                return result;
            } else {
                ExplorerItems.Children children = (ExplorerItems.Children) getContainer();
                return children.findById(id, EScope.ALL).get();
            }
        }
    }

    private static void fireEntireChange(RadixObject sender) {
        if (sender.isInBranch()) {
            EntireChangesSupport.getInstance(AdsExplorerItemDef.class).fireChange(sender);
        }
    }

    public class Children extends ExtendableDefinitions<AdsExplorerItemDef> {

        private final EditStateChangeListener listener = new EditStateChangeListener() {
            @Override
            public void onEvent(EditStateChangedEvent e) {
                if (e.oldEditState == EEditState.NEW && e.newEditState == EEditState.NONE) {
                    if (isInBranch()) {
                        fireEntireChange(Children.this);
                    } else {
                        //await until loaded into module
                        RequestProcessor.submit(new Runnable() {
                            @Override
                            public void run() {
                                Thread.currentThread().setContextClassLoader(null);
                                while (true) {
                                    if (Children.this.isInBranch()) {
                                        fireEntireChange(Children.this);
                                        return;
                                    } else {
                                        try {
                                            Thread.sleep(20);
                                        } catch (InterruptedException ex) {
                                            return;
                                        }
                                    }
                                }
                            }
                        });
                    }
                }
            }
        };

        public Children() {
            super(ExplorerItems.this, new LocalChildren());
            this.addEditStateListener(listener);
        }

        private void loadFrom(ChildExplorerItems xDef) {
            List<Item> items = xDef.getItemList();
            if (items != null && !items.isEmpty()) {
                for (Item item : items) {
                    AdsExplorerItemDef ei = null;
                    if (item.isSetChildRef()) {
                        ei = AdsChildRefExplorerItemDef.Factory.loadFrom(item.getChildRef());
                    } else if (item.isSetEntity()) {
                        ei = AdsEntityExplorerItemDef.Factory.loadFrom(item.getEntity());
                    } else if (item.isSetParagraph()) {
                        ei = AdsParagraphExplorerItemDef.Factory.loadFrom(item.getParagraph());
                    } else if (item.isSetParagraphLink()) {
                        ei = AdsParagraphLinkExplorerItemDef.Factory.loadFrom(item.getParagraphLink());
                    } else if (item.isSetParentRef()) {
                        ei = AdsParentRefExplorerItemDef.Factory.loadFrom(item.getParentRef());
                    }
                    if (ei != null) {
                        this.getLocal().add(ei);
                    }
                }
            }
            fireEntireChange(this);
            //EntireChangesSupport.getInstance(AdsExplorerItemDef.class).fireChange();
        }

        public void appendTo(ChildExplorerItems xDef, ESaveMode saveMode) {
            for (AdsExplorerItemDef def : this.getLocal()) {                
                if (def instanceof AdsChildRefExplorerItemDef) {
                    ((AdsChildRefExplorerItemDef) def).appendTo(xDef.addNewItem().addNewChildRef(), saveMode);
                } else if (def instanceof AdsParentRefExplorerItemDef) {
                    ((AdsParentRefExplorerItemDef) def).appendTo(xDef.addNewItem().addNewParentRef(), saveMode);
                } else if (def instanceof AdsEntityExplorerItemDef) {
                    ((AdsEntityExplorerItemDef) def).appendTo(xDef.addNewItem().addNewEntity(), saveMode);
                } else if (def instanceof AdsParagraphLinkExplorerItemDef) {
                    ((AdsParagraphLinkExplorerItemDef) def).appendTo(xDef.addNewItem().addNewParagraphLink(), saveMode);
                } else if (def instanceof AdsParagraphExplorerItemDef) {
                    ((AdsParagraphExplorerItemDef) def).appendTo(xDef.addNewItem().addNewParagraph(), saveMode);
                }
            }
        }

        @Override
        public String getName() {
            return "Children";
        }

        @Override
        public RadixIcon getIcon() {
            return AdsDefinitionIcon.PARAGRAPH;
        }

        public ExplorerItems getOwnerExplorerItems() {
            return ExplorerItems.this;
        }

        @Override
        public ExtendableDefinitions<AdsExplorerItemDef> findInstance(Definition owner) {
            if (owner instanceof AdsEditorPresentationDef) {
                return ((AdsEditorPresentationDef) owner).getExplorerItems().getChildren();
            } else {
                return null;
            }
        }

        private class EICHierarchyIterator extends HierarchyIterator<Children> {

            List<Children> next = null;
            private HierarchyIterator<AdsEditorPresentationDef> internalEpr = null;
            private HierarchyIterator<AdsExplorerItemDef> internalRoot = null;
            private boolean singleMatch = false;
            AdsEditorPresentationDef prevPresentation = null;
            private boolean isEprMode = false;
            private boolean isRootMode = false;

            public EICHierarchyIterator(EScope scope, HierarchyIterator.Mode mode) {
                super(mode);
                AdsDefinition def = getOwnerDef();
                if (def instanceof AdsEditorPresentationDef) {
                    isEprMode = true;
                    AdsEditorPresentationDef epr = (AdsEditorPresentationDef) def;
                    // if (scope == EScope.ALL && !epr.isExplorerItemsInherited()) {
                    //       internalEpr = epr.<AdsEditorPresentationDef>getHierarchyIterator(EScope.LOCAL_AND_OVERWRITE, mode);
                    //    } else {
                    internalEpr = epr.<AdsEditorPresentationDef>getHierarchyIterator(scope, mode);
                    //     }
                } else {
                    //if (getOwnerDef().isTopLevelDefinition()) {
                    internalRoot = new AdsDefinition.DefaultHierarchyIterator<AdsExplorerItemDef>((AdsExplorerItemDef) getOwnerDef(), EScope.ALL, mode);
                    // }
                    isRootMode = true;
                }
            }

            @Override
            public boolean hasNext() {
                if (isEprMode) {
                    return hasNextEpr();
                } else {
                    if (isRootMode) {
                        if (internalRoot == null) {
                            if (!singleMatch) {
                                next = Collections.singletonList(Children.this);
                                singleMatch = true;
                                return true;
                            } else {
                                return next != null;
                            }
                        }
                        return internalRoot.hasNext();
                    } else {
                        return false;
                    }
                }
            }

            private boolean hasNextEpr() {
                if (internalEpr == null && !singleMatch) {
                    next = Collections.singletonList(Children.this);
                    singleMatch = true;
                } else {
                    if (this.next == null && internalEpr != null && internalEpr.hasNext()) {
//                        if (prevPresentation != null && !prevPresentation.isExplorerItemsInherited()) {
//                            AdsEditorPresentationDef ovr = prevPresentation.getHierarchy().findOverwritten().get();
//                            if (ovr == null) {
//                                return false;
//                            }
//                        }
                        List<Children> result = new LinkedList<>();

                        while (internalEpr.hasNext()) {
                            Chain<AdsEditorPresentationDef> nextPresentations = internalEpr.next();
                            for (AdsPresentationDef pr : nextPresentations) {
                                Children children = ((AdsEditorPresentationDef) pr).getExplorerItems().getChildren();
                                if (pr != null && !result.contains(children)) {
                                    result.add(children);
                                }
                            }
                            if (!nextPresentations.isEmpty()) {
                                prevPresentation = nextPresentations.first();
                            }
                            if (!result.isEmpty()) {
                                break;
                            }
                        }
                        if (result.isEmpty()) {
                            next = null;
                        } else {
                            next = new ArrayList<>(result);
                        }
                    }
                }
                return next != null;
            }

            @Override
            public Chain<Children> next() {
                if (isEprMode) {
                    return nextEpr();
                } else {
                    if (isRootMode) {
                        if (hasNext()) {
                            if (internalRoot == null) {
                                List<Children> result = next;
                                next = null;
                                return Chain.newInstance(result);
                            }
                            Chain<AdsExplorerItemDef> result = internalRoot.next();
                            List<Children> next = new LinkedList<>();
                            for (AdsExplorerItemDef e : result) {
                                if (e instanceof AdsParagraphExplorerItemDef) {
                                    Children c = ((AdsParagraphExplorerItemDef) e).getExplorerItems().getChildren();
                                    next.add(c);
                                }
                            }
                            return Chain.newInstance(next);
                        } else {
                            return Chain.empty();
                        }
                    } else {
                        return Chain.empty();
                    }
                }
            }

            public Chain<Children> nextEpr() {
                if (hasNext()) {
                    if (internalEpr == null) {
                        List<Children> result = next;
                        next = null;
                        return Chain.newInstance(result);
                    }
                    Chain<Children> result = Chain.newInstance(next);
                    next = null;
                    return result;
                } else {
                    return Chain.empty();
                }
            }
        }

        @Override
        public HierarchyIterator<? extends ExtendableDefinitions<AdsExplorerItemDef>> newIterator(EScope scope, HierarchyIterator.Mode mode) {
            return new EICHierarchyIterator(scope, mode);
        }
    }
    final Children children;
    private final ExplorerItemsOrder explorerItemsOrder;
    private final Set<Id> explicitlyInheritedItems = new HashSet<>();

    private ExplorerItems(AdsDefinition owner, ChildExplorerItems xDef) {
        super();
        this.setContainer(owner);
        this.children = new Children();
        this.explorerItemsOrder = new ExplorerItemsOrder(this, xDef);
        if (xDef != null) {
            this.children.loadFrom(xDef);
            if (xDef.isSetExplicitlyInherited()) {
                for (ChildExplorerItems.ExplicitlyInherited.Item xItem : xDef.getExplicitlyInherited().getItemList()) {
                    if (xItem.getId() != null) {
                        this.explicitlyInheritedItems.add(xItem.getId());
                    }
                }
            }
        }
    }

    public Children getChildren() {
        return children;
    }
 
    public void appendTo(ChildExplorerItems xDef, ESaveMode saveMode) {
        this.children.appendTo(xDef, saveMode);
        if (saveMode == ESaveMode.NORMAL) {
            this.explorerItemsOrder.appendTo(xDef);
        }
        if (this.explicitlyInheritedItems != null && !this.explicitlyInheritedItems.isEmpty()) {
            List<Id> ids = new ArrayList<>(this.explicitlyInheritedItems);

            Collections.sort(ids);
            ChildExplorerItems.ExplicitlyInherited xItems = xDef.addNewExplicitlyInherited();
            for (Id id : ids) {
                xItems.addNewItem().setId(id);
            }

        }
    }

    public boolean isChildInherited(Id id) {
        synchronized (explicitlyInheritedItems) {
            return explicitlyInheritedItems.contains(id);
        }
    }

    public List<Id> getInheritedExplorerItemIds() {
        synchronized (explicitlyInheritedItems) {
            return new ArrayList<>(explicitlyInheritedItems);
        }
    }

    public void inheritChild(Id id) {
        synchronized (explicitlyInheritedItems) {
            explicitlyInheritedItems.add(id);
            setEditState(EEditState.MODIFIED);
            EntireChangesSupport.getInstance(AdsExplorerItemDef.class).fireChange(this);
        }
    }

    public void uninheritChild(Id id) {
        synchronized (explicitlyInheritedItems) {
            explicitlyInheritedItems.remove(id);
            setEditState(EEditState.MODIFIED);
            EntireChangesSupport.getInstance(AdsExplorerItemDef.class).fireChange(this);
        }
    }

    public AdsExplorerItemDef findChildExplorerItem(final Id id) {
        AdsExplorerItemDef ei = children.findById(id, EScope.ALL).get();
        if (ei != null) {
            return ei;
        } else {
            for (AdsExplorerItemDef item : children.getLocal()) {
                if (item instanceof AdsParagraphExplorerItemDef) {
                    ei = ((AdsParagraphExplorerItemDef) item).getExplorerItems().findChildExplorerItem(id);
                    if (ei != null) {
                        return ei;
                    }
                } else if (item instanceof AdsParagraphLinkExplorerItemDef) {
                    AdsParagraphExplorerItemDef par = ((AdsParagraphLinkExplorerItemDef) item).findReferencedParagraph();
                    if (par != null) {
                        if (par.getId() == id) {
                            return par;
                        }
                        ei = par.getExplorerItems().findChildExplorerItem(id);
                        if (ei != null) {
                            return ei;
                        }
                    }
                }
            }
        }
        return null;
    }

    public AdsParagraphExplorerItemDef findChildParagraphById(Id id) {
        return findChildParagraphById(id, EScope.LOCAL);
    }

    public AdsParagraphExplorerItemDef findChildParagraphById(Id id, EScope scope) {
        AdsExplorerItemDef ei = children.findById(id, scope).get();
        if (ei instanceof AdsParagraphExplorerItemDef) {
            return (AdsParagraphExplorerItemDef) ei;
        } else {
            for (AdsExplorerItemDef item : children.get(scope)) {
                if (item instanceof AdsParagraphExplorerItemDef) {
                    AdsParagraphExplorerItemDef par = ((AdsParagraphExplorerItemDef) item).getExplorerItems().findChildParagraphById(id, scope);
                    if (par != null) {
                        return par;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        this.children.visit(visitor, provider);
        this.explorerItemsOrder.visit(visitor, provider);
    }

    AdsDefinition getOwnerDef() {
        RadixObject owner = getContainer();
        while (owner != null) {
            if (owner instanceof AdsDefinition) {
                return (AdsDefinition) owner;
            }
            owner = owner.getContainer();
        }
        return null;
    }

    AdsLocalizingBundleDef findLocalizingBundle() {
        AdsDefinition owner = getOwnerDef();
        if (owner != null) {
            return owner.findExistingLocalizingBundle();
        } else {
            return null;
        }
    }

    @Override
    public JavaSourceSupport getJavaSourceSupport() {
        return new JavaSourceSupport(this) {
            @Override
            public CodeWriter getCodeWriter(UsagePurpose purpose) {
                return new ExplorerItemsWriter(this, ExplorerItems.this, purpose);
            }
        };
    }

    public enum EPlacement {

        EXPLORER_ROOT,
        EDITOR_PRESENTATION
    }

    public EPlacement getPlacement() {

        Definition od = getOwnerDefinition();
        if (od instanceof AdsEditorPresentationDef) {
            return EPlacement.EDITOR_PRESENTATION;
        } else if (od instanceof AdsParagraphExplorerItemDef) {
            AdsParagraphExplorerItemDef par = (AdsParagraphExplorerItemDef) od;
            if (par.findOwnerEditorPresentation() != null) {
                return EPlacement.EDITOR_PRESENTATION;
            }
        }
        return EPlacement.EXPLORER_ROOT;
    }

    public AdsEditorPresentationDef findOwnerEditorPresentation() {
        Definition od = getOwnerDefinition();
        if (od instanceof AdsEditorPresentationDef) {
            return (AdsEditorPresentationDef) od;
        } else if (od instanceof AdsParagraphExplorerItemDef) {
            return ((AdsParagraphExplorerItemDef) od).findOwnerEditorPresentation();
        } else {
            return null;
        }
    }

    public AdsParagraphExplorerItemDef findOwnerTopLevelParagraph() {
        for (AdsDefinition od = getOwnerDef(); od != null; od = od.getOwnerDef()) {
            if (od instanceof AdsParagraphExplorerItemDef) {
                if (od.isTopLevelDefinition()) {
                    return (AdsParagraphExplorerItemDef) od;
                }
            }
        }
        return null;
    }

    @Override
    protected boolean isQualifiedNamePart() {
        return false;
    }

    @Override
    public String getName() {
        return "Children";
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.PARAGRAPH;
    }

    public ExplorerItemsOrder getItemsOrder() {
        return explorerItemsOrder;
    }

    public boolean canChangeItemOrderInheritance(ExplorerItems rootContext) {
        return !isReadOnly() && !rootContext.isParentOf(this);
    }
}
