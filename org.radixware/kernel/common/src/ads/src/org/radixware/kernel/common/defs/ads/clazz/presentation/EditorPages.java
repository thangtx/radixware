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

package org.radixware.kernel.common.defs.ads.clazz.presentation;

import java.lang.reflect.Method;
import java.util.*;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.defs.*;
import org.radixware.kernel.common.defs.ClipboardSupport.DuplicationResolver.Resolution;
import org.radixware.kernel.common.defs.ClipboardSupport.Transfer;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.AdsClipboardSupport;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinition.ESaveMode;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.AdsDefinitions;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.ExtendableMembers;
import org.radixware.kernel.common.defs.ads.clazz.IAdsFormPresentableClass;
import org.radixware.kernel.common.defs.ads.common.ICustomViewable;
import org.radixware.kernel.common.defs.ads.common.ICustomViewable.CustomViewSupport;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.clazz.presentation.AdsEditorPagesWriter;
import org.radixware.kernel.common.defs.ads.ui.AdsAbstractUIDef;
import org.radixware.kernel.common.enums.EEditorPageType;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.common.utils.events.RadixEventSource;


public abstract class EditorPages extends ExtendableMembers<AdsEditorPageDef> implements IJavaSource {

    public interface IEditorPagesContainer {

        public EditorPages getEditorPages();
    }

    static final class Factory {

        private Factory() {
            super();
        }

        public static EditorPages loadFrom(final AdsEditorPresentationDef owner, final org.radixware.schemas.adsdef.EditorPages xDef) {
            return new EditorPresentationPages(owner, xDef);
        }

        public static EditorPages newInstance(final AdsEditorPresentationDef owner) {
            return new EditorPresentationPages(owner, null);
        }

        public static EditorPages newInstance(final AdsFilterDef owner) {
            return new FilterPages(owner, null);
        }

        public static EditorPages loadFrom(final AdsFilterDef owner, final org.radixware.schemas.adsdef.EditorPages xDef) {
            return new FilterPages(owner, xDef);
        }

        public static EditorPages newInstance(final IAdsFormPresentableClass owner) {
            return new FormPages(owner, null);
        }

        public static EditorPages loadFrom(final IAdsFormPresentableClass owner, final org.radixware.schemas.adsdef.EditorPages xDef) {
            return new FormPages(owner, xDef);
        }
    }

    private static class EditorPresentationPages extends EditorPages {

        public EditorPresentationPages(final AdsEditorPresentationDef owner, final org.radixware.schemas.adsdef.EditorPages xDef) {
            super(owner, xDef);
        }

        @Override
        public ExtendableDefinitions<AdsEditorPageDef> findInstance(final AdsDefinition clazz) {
            if (clazz instanceof AdsEditorPresentationDef) {
                return ((AdsEditorPresentationDef) clazz).getEditorPages();
            } else {
                return null;
            }
        }

        private AdsEditorPresentationDef getOwnerEditorPresentation() {
            for (RadixObject owner = getContainer(); owner != null; owner = owner.getContainer()) {
                if (owner instanceof AdsEditorPresentationDef) {
                    return (AdsEditorPresentationDef) owner;
                }
            }
            return null;
        }

        @Override
        @SuppressWarnings("unchecked")
        protected HierarchyIterator<ExtendableDefinitions<AdsEditorPageDef>> newIterator(final EScope scope, HierarchyIterator.Mode mode) {
            return new HI(scope, mode);
        }

        private class HI extends ExtendableMembers.MemberListHierarchyIterator {

            public HI(final EScope scope, HierarchyIterator.Mode mode) {
                super(scope, mode);
            }

            @Override
            protected HierarchyIterator createInternal(final EScope scope, HierarchyIterator.Mode mode) {
                return getOwnerEditorPresentation().getHierarchyIteratorImpl(scope, mode);
            }
        }
    }

    private static class FilterPages extends EditorPages {

        public FilterPages(final AdsFilterDef owner, final org.radixware.schemas.adsdef.EditorPages xDef) {
            super(owner, xDef);
        }

        @Override
        public ExtendableDefinitions<AdsEditorPageDef> findInstance(final AdsDefinition clazz) {
            if (clazz instanceof AdsFilterDef) {
                return ((AdsFilterDef) clazz).getEditorPages();
            } else {
                return null;
            }
        }

        private AdsFilterDef getOwnerFilterDef() {
            for (RadixObject owner = getContainer(); owner != null; owner = owner.getContainer()) {
                if (owner instanceof AdsFilterDef) {
                    return (AdsFilterDef) owner;
                }
            }
            return null;
        }

        @Override
        protected HierarchyIterator<ExtendableDefinitions<AdsEditorPageDef>> newIterator(EScope scope, HierarchyIterator.Mode mode) {
            return new HierarchyIterator<ExtendableDefinitions<AdsEditorPageDef>>(mode) {
                private AdsFilterDef filter = null;

                @Override
                public boolean hasNext() {
                    return filter == null;
                }

                @Override
                public Chain<ExtendableDefinitions<AdsEditorPageDef>> next() {
                    if (filter == null) {
                        filter = getOwnerFilterDef();
                        return Chain.<ExtendableDefinitions<AdsEditorPageDef>>newInstance(filter.getEditorPages());
                    } else {
                        return Chain.empty();
                    }
                }
            };
        }
    }

    private static class FormPages extends EditorPages {

        public FormPages(final IAdsFormPresentableClass owner, final org.radixware.schemas.adsdef.EditorPages xDef) {
            super((AdsClassDef) owner, xDef);
        }

        @Override
        public ExtendableDefinitions<AdsEditorPageDef> findInstance(final AdsDefinition clazz) {
            if (clazz instanceof AdsClassDef && clazz instanceof IAdsFormPresentableClass) {
                return ((IAdsFormPresentableClass) clazz).getPresentations().getEditorPages();
            } else {
                return null;
            }
        }
    }

    public static class OrderedPage extends RadixObject implements ICustomViewable<AdsEditorPageDef, AdsAbstractUIDef> {

        private Id pageId;
        private final PageOrder children = new PageOrder(this);

        private OrderedPage(org.radixware.schemas.adsdef.EditorPages.PageOrder xDef) {
            assert xDef != null && xDef.getItemList().get(0).getLevel() == 1;
            this.pageId = xDef.getItemList().get(0).getPageId();
            List<org.radixware.schemas.adsdef.EditorPages.PageOrder.Item> items = xDef.getItemList();
            if (items != null && !items.isEmpty()) {
                children.loadFrom(items, 1, 2, new HashMap<Id, Object>());
            }
        }

        static OrderedPage loadFrom(org.radixware.schemas.adsdef.EditorPages.PageOrder xDef) {
            return new OrderedPage(xDef);
        }

        @Override
        public void collectDependences(List<Definition> list) {
            super.collectDependences(list);
            AdsEditorPageDef page = findPage();
            if (page != null) {
                list.add(page);
            }
        }

        private OrderedPage(Id pageId) {
            this.pageId = pageId;
        }

        public Id getPageId() {
            return pageId;
        }

        public OrderedPage getOwnerOrderedPage() {
            for (RadixObject obj = getContainer(); obj != null; obj = obj.getContainer()) {
                if (obj instanceof OrderedPage) {
                    return (OrderedPage) obj;
                } else if (obj instanceof EditorPages) {
                    return null;
                }
            }
            return null;
        }

        public boolean overrideOrInherit() {
            try {
                //   synchronized (this) {
                if (isOwnPage()) {
                    AdsEditorPageDef page = findPage();
                    if (page == null) {
                        return false;
                    }
                    AdsEditorPageDef ovr = (AdsEditorPageDef) page.getHierarchy().findOverridden().get();
                    if (ovr == null) {
                        ovr = (AdsEditorPageDef) page.getHierarchy().findOverwritten().get();
                    }
                    if (ovr == null) {
                        return false;
                    }
                    PageOrder order = getOwnerPageOrder();
                    int index = order.indexOf(this);
                    page.delete();
                    if (order.addPageToOrder(index, ovr)) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    AdsEditorPageDef page = findPage();
                    if (page == null) {
                        return false;
                    }
                    Definition selfOwnerDef = this.getOwnerEditorPages().getOwnerDefinition();
                    Definition pageOwnerDef = page.getOwnerEditorPages().getOwnerDefinition();

                    if (selfOwnerDef.getId() == pageOwnerDef.getId()) {
                        this.getOwnerEditorPages().overwrite(page);
                    } else {
                        this.getOwnerEditorPages().override(page);
                    }
                    return true;
                }
                //  }
            } finally {
                if (isInBranch()) {
                    EditorPagesOperationSupport.getDefault().fireChange();
                }
                CustomViewSupport<AdsEditorPageDef, AdsAbstractUIDef> support = getCustomViewSupport();
                if (support != null) {
                    support.getChangeSupport().fireEvent(new CustomViewChangedEvent());
                }
                fireNameChange();
            }
        }

        /**
         * Calls super.delete();
         *
         */
        public boolean remove() {
            return super.delete();
        }

        @Override
        public String getTypeTitle() {
            if (isOwnPage() || isOwrPage()) {
                AdsEditorPageDef page = findPage();
                if (page != null) {
                    return page.getTypeTitle();
                }
            }
            return "Page reference";
        }

        @Override
        public String getTypesTitle() {
            return "Page references";
        }

        public boolean removeFromOrder() {
            synchronized (this) {
                if (isOwnPage()) {
                    AdsEditorPageDef page = findPage();
                    if (page != null) {
                        Collection<Id> ids = this.getOwnerEditorPages().getOrder().getOrderedPageIds();
                        int count = 0;
                        for (Id id : ids) {
                            if (id == pageId) {
                                count++;
                            }
                        }
                        if (count == 1) {
                            if (page.delete()) {
                                return remove();
                            } else {
                                return false;
                            }
                        } else {
                            return remove();
                        }
                    } else {
                        return remove();
                    }
                } else {
                    return remove();
                }
            }
        }

        @Override
        public boolean setName(String name) {
            if (isOwnPage()) {
                AdsEditorPageDef page = findPage();
                if (page != null) {
                    if (page.setName(name)) {
                        fireNameChange();
                    }
                }
            }
            return false;
        }

        public PageOrder getSubpages() {
            return children;
        }

        private AdsEditorPageDef findPageById(Id id) {
            EditorPages pages = getOwnerEditorPages();
            if (pages == null) {
                return null;
            }
            return pages.findById(id, EScope.ALL).get();
        }

        public AdsEditorPageDef findPage() {
            if (pageId == null) {
                return null;
            }
            return findPageById(pageId);
        }

        private EditorPages getOwnerEditorPages() {
            for (RadixObject obj = getContainer(); obj != null; obj = obj.getContainer()) {
                if (obj instanceof EditorPages) {
                    return (EditorPages) obj;
                }
            }
            return null;
        }

        public boolean isOwnPage() {
            AdsEditorPageDef page = findPage();
            if (page != null) {
                return page.getOwnerEditorPages() == getOwnerEditorPages();
            } else {
                return false;
            }
        }

        public boolean isOwrPage() {
            AdsEditorPageDef page = findPage();
            if (page != null) {
                return page.getHierarchy().findOverridden().get() != null;
            } else {
                return false;
            }
        }

        @Override
        public void visitChildren(IVisitor visitor, VisitorProvider provider) {
            super.visitChildren(visitor, provider);
            children.visit(visitor, provider);
        }

        public boolean isNodePage() {
            AdsEditorPageDef page = findPage();
            if (page != null) {
                return page.getType() == EEditorPageType.STANDARD && page.getProperties().isEmpty();
            } else {
                return false;
            }
        }

        //for clipboard support only
        private void appendTo(org.radixware.schemas.adsdef.EditorPages.PageOrder xDef, int level) {
            org.radixware.schemas.adsdef.EditorPages.PageOrder.Item item = xDef.addNewItem();
            item.setPageId(pageId);
            item.setLevel(level);
            for (OrderedPage page : children) {
                page.appendTo(xDef, level + 1);
            }
        }

        public PageOrder getOwnerPageOrder() {
            return (PageOrder) getContainer();
        }

        public boolean pageRemoved() {
            PageOrder ownerOrder = getOwnerPageOrder();
            if (remove()) {
                for (OrderedPage page : children) {
                    page.setContainer(null);
                    ownerOrder.add(page);
                }
                return true;
            }
            return false;
        }

        private void printWithChildren(StringBuilder b, String prefix) {
            b.append(prefix);
            b.append(pageId.toString());
            AdsEditorPageDef page = findPage();
            if (page != null) {
                b.append(" - ");
                b.append(page.getQualifiedName());
            } else {
                b.append(" - <Not Found>");
            }
            b.append("\n");
            for (OrderedPage p : children) {
                p.printWithChildren(b, prefix + "  ");
            }
        }

        @Override
        public String toString() {
            StringBuilder b = new StringBuilder();
            b.append(super.toString());
            b.append("\n");
            printWithChildren(b, "");
            return b.toString();
        }

        @Override
        public CustomViewSupport<AdsEditorPageDef, AdsAbstractUIDef> getCustomViewSupport() {
            if (isOwnPage()) {
                AdsEditorPageDef page = findPage();
                if (page != null) {
                    return page.getCustomViewSupport();
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }

        private static class OrderedPageClipboardSupport extends AdsClipboardSupport<OrderedPage> {

            private static class LinkedEditorPageTransfer extends AdsTransfer<AdsEditorPageDef> {

                private final Id originalPageId;
                private final boolean originalPageWasInherited;

                public LinkedEditorPageTransfer(AdsEditorPageDef source, boolean origPageWasInherited) {
                    super(source);
                    originalPageId = source.getId();
                    originalPageWasInherited = origPageWasInherited;
                }

                @Override
                protected void afterCopy() {
                    setObject(ClipboardSupport.duplicate(Collections.singletonList(getObject())).get(0));
                    super.afterCopy();
                }

                @Override
                protected void afterDuplicate() {
                    setObject(ClipboardSupport.duplicate(Collections.singletonList(getObject())).get(0));
                    super.afterDuplicate();
                }

                @Override
                public void afterPaste() {
                    super.afterPaste();
                }
            }

            private static class OrderedPageTransfer extends AdsTransfer<OrderedPage> {

                final HashMap<Id, LinkedEditorPageTransfer> oldPageId2Page = new HashMap<>();
                final EditorPages sourcePageSet;

                protected OrderedPageTransfer(OrderedPage source) {
                    super(source);
                    sourcePageSet = source.getOwnerEditorPages();
                    source.visit(new IVisitor() {
                        @Override
                        public void accept(RadixObject radixObject) {
                            if (radixObject instanceof OrderedPage) {
                                OrderedPage op = (OrderedPage) radixObject;
                                AdsEditorPageDef page = op.findPage();
                                if (page != null) {
                                    oldPageId2Page.put(page.getId(), new LinkedEditorPageTransfer(page, !op.isOwnPage()));
                                }
                            }
                        }
                    }, VisitorProviderFactory.createDefaultVisitorProvider());
                }

                @Override
                public void afterPaste() {
                    super.afterPaste();
                    getObject().visit(new IVisitor() {
                        @Override
                        public void accept(RadixObject radixObject) {
                            if (radixObject instanceof OrderedPage) {

                                OrderedPage op = (OrderedPage) radixObject;
                                AdsEditorPageDef page = op.findPage();
                                //if (page == null) {
                                LinkedEditorPageTransfer t = oldPageId2Page.get(op.pageId);
                                if (t != null) {
                                    if (t.originalPageWasInherited && op.findPageById(t.originalPageId) != null) {
                                        op.pageId = t.originalPageId;
                                    } else {
                                        op.pageId = t.getObject().getId();
                                        AdsEditorPageDef cbP = t.getObject();
                                        if (cbP.getOwnerEditorPages() != null && op.getOwnerEditorPages().getLocal().findById(op.pageId) == null && cbP.getOwnerEditorPages() != op.getOwnerEditorPages()) {
                                            cbP.delete();
                                        }
                                        if (cbP.getOwnerEditorPages() != op.getOwnerEditorPages() && op.getOwnerEditorPages().getLocal().findById(op.pageId) == null) {
                                            List<Transfer> tl = Collections.singletonList((Transfer) t);
                                            final DuplicationResolver resolver = new DuplicationResolver() {
                                                @Override
                                                public Resolution resolveDuplication(RadixObject newObject, RadixObject oldObject) {
                                                    return Resolution.CANCEL;
                                                }
                                            };
                                            if (op.getOwnerEditorPages().getClipboardSupport().canPaste(tl, resolver) == CanPasteResult.YES) {
                                                op.getOwnerEditorPages().getClipboardSupport().paste(tl, resolver);
                                                t.afterPaste();
                                            }
                                        }
                                    }
                                }
                                //}
                            }
                        }
                    },
                            VisitorProviderFactory.createDefaultVisitorProvider());
                    if (sourcePageSet != null) {
                        sourcePageSet.restoreState();
                    }

                }

                @Override
                protected void afterCopy() {
                    super.afterCopy();



                    for (LinkedEditorPageTransfer page : oldPageId2Page.values()) {
                        page.afterCopy();
                    }
                    updatePageAssoc();
                }

                private void updatePageAssoc() {
                    final HashMap<Id, LinkedEditorPageTransfer> newList = new HashMap<>();

                    getObject().visit(new IVisitor() {
                        @Override
                        public void accept(RadixObject radixObject) {
                            if (radixObject instanceof OrderedPage) {
                                OrderedPage op = (OrderedPage) radixObject;
                                LinkedEditorPageTransfer t = oldPageId2Page.get(op.pageId);
                                if (t != null) {
                                    op.pageId = t.getObject().getId();
                                    newList.put(op.pageId, t);
                                }
                            }
                        }
                    }, VisitorProviderFactory.createDefaultVisitorProvider());
                    oldPageId2Page.clear();
                    oldPageId2Page.putAll(newList);
                }

                @Override
                protected void afterDuplicate() {
                    super.afterDuplicate();
                    for (LinkedEditorPageTransfer page : oldPageId2Page.values()) {
                        page.afterDuplicate();
                    }
                    updatePageAssoc();
                }
            }

            public OrderedPageClipboardSupport(OrderedPage radixObject) {
                super(radixObject);
            }

            @Override
            protected Transfer<OrderedPage> newTransferInstance() {
                return new OrderedPageTransfer(radixObject);
            }

            @Override
            protected XmlObject copyToXml() {
                org.radixware.schemas.adsdef.EditorPages.PageOrder xOrder = org.radixware.schemas.adsdef.EditorPages.PageOrder.Factory.newInstance();
                radixObject.appendTo(xOrder, 1);
                return xOrder;
            }

            @Override
            protected OrderedPage loadFrom(XmlObject xmlObject) {
                if (xmlObject instanceof org.radixware.schemas.adsdef.EditorPages.PageOrder) {
                    return new OrderedPage((org.radixware.schemas.adsdef.EditorPages.PageOrder) xmlObject);
                } else {
                    return null;
                }
            }

            @Override
            public CanPasteResult canPaste(List<Transfer> transfers, DuplicationResolver resolver) {

                OrderedPage page = radixObject;
                while (page != null) {
                    for (final Transfer transfer : transfers) {
                        if (transfer.getInitialObject() == page) {
                            return CanPasteResult.NO;
                        }
                    }
                    page = page.getOwnerOrderedPage();
                }

                return radixObject.children.getClipboardSupport().canPaste(transfers, resolver);
            }

            @Override
            public void paste(List<Transfer> transfers, DuplicationResolver resolver) {
                radixObject.children.getClipboardSupport().paste(transfers, resolver);
            }

            @Override
            protected Method getDecoderMethod() {
                try {
                    return OrderedPage.class.getDeclaredMethod("loadFrom", org.radixware.schemas.adsdef.EditorPages.PageOrder.class);
                } catch (NoSuchMethodException | SecurityException ex) {
                    return null;
                }
            }
        }

        @Override
        public ClipboardSupport<? extends RadixObject> getClipboardSupport() {
            return new OrderedPageClipboardSupport(this);
        }

        @Override
        public String getName() {
            AdsEditorPageDef page = findPage();

            return page == null ? pageId.toString() : page.getName();
        }

        @Override
        public RadixIcon getIcon() {
            AdsEditorPageDef page = findPage();
            return page == null ? super.getIcon() : page.getIcon();
        }
    }

    public static class PageOrder extends RadixObjects<OrderedPage> {

        private PageOrder(OrderedPage op) {
            super(op);
        }

        /**
         * return item index used to create new item of parent, -1 if list end
         * reached
         */
        private void loadFrom(org.radixware.schemas.adsdef.EditorPages.PageOrder xDef, HashMap<Id, Object> loadedPages) {
            if (xDef == null) {
                return;
            }
            List<org.radixware.schemas.adsdef.EditorPages.PageOrder.Item> items = xDef.getItemList();
            if (items != null && !items.isEmpty()) {
                loadFrom(items, 0, 1, loadedPages);
            }
        }

        private int loadFrom(List<org.radixware.schemas.adsdef.EditorPages.PageOrder.Item> items, int start, int level, HashMap<Id, Object> loadedPages) {
            if (items != null && items.size() > start) {
                for (int i = start, len = items.size(); i < len; i++) {
                    org.radixware.schemas.adsdef.EditorPages.PageOrder.Item item = items.get(i);
                    if (item.getLevel() < level) {
                        return i;
                    } else if (item.getLevel() == level) {
                        OrderedPage page = new OrderedPage(item.getPageId());
                        add(page);
                        loadedPages.put(page.pageId, null);
                        int newStart = page.children.loadFrom(items, i + 1, level + 1, loadedPages);
                        if (newStart < 0) {
                            return newStart;
                        }
                        i = newStart - 1;
                    }
                }
            }
            return -1;
        }

        private PageOrder(EditorPages owner) {
            super(owner);
        }

        public boolean isNodeOrder() {
            return getContainer() instanceof EditorPages || (getContainer() instanceof OrderedPage && ((OrderedPage) getContainer()).isNodePage());
        }

        public OrderedPage findOrderByPageId(Id pageId) {
            for (OrderedPage page : this) {
                if (page.getPageId() == pageId) {
                    return page;
                } else {
                    OrderedPage result = page.getSubpages().findOrderByPageId(pageId);
                    if (result != null) {
                        return result;
                    }
                }
            }
            return null;
        }

        public EditorPages getOwnerEditorPages() {
            for (RadixObject obj = this.getContainer(); obj != null; obj = obj.getContainer()) {
                if (obj instanceof EditorPages) {
                    return (EditorPages) obj;
                }
            }
            return null;
        }

        private void appendTo(org.radixware.schemas.adsdef.EditorPages.PageOrder xDef, int level) {
            for (OrderedPage page : this) {
                org.radixware.schemas.adsdef.EditorPages.PageOrder.Item xItem = xDef.addNewItem();
                xItem.setPageId(page.pageId);
                xItem.setLevel(level);
                page.children.appendTo(xDef, level + 1);
            }
        }

        public Collection<Id> getOrderedPageIds() {
            ArrayList<Id> ids = new ArrayList<>();
            collectOrderedPageIds(ids);
            return ids;
        }

        private void collectOrderedPageIds(Collection<Id> ids) {

            for (OrderedPage page : this) {
                ids.add(page.getPageId());
                page.children.collectOrderedPageIds(ids);
            }
        }

        private boolean pageAddedImpl(final AdsEditorPageDef page) {
            boolean result = false;
            for (OrderedPage order : this.list()) {
                if (order.pageId == page.getId()) {
                    result = true;
                    if (isInBranch()) {
                        EditorPagesOperationSupport.getDefault().fireChange();
                    }
                    break;
                }
                if (order.children.pageAddedImpl(page)) {
                    result = true;
                }
            }
            return result;
        }

        private void pageAdded(final AdsEditorPageDef page) {
            if (!pageAddedImpl(page)) {
                add(new OrderedPage(page.getId()));
            }
        }

        public boolean addPageToOrder(AdsEditorPageDef page) {
            return addPageToOrder(-1, page);
        }

        private boolean addPageToOrder(int index, AdsEditorPageDef page) {
            AdsEditorPageDef realPage = findPageById(page.getId());
            if (realPage == null) {
                return false;
            }

            OrderedPage op = getOwnerEditorPages().getOrder().findOrderByPageId(page.getId());
            if (op != null) {
                if (!op.remove()) {
                    return false;
                }
            }
            if (index > 0) {
                this.add(index, new OrderedPage(page.getId()));
                return true;
            } else {
                this.add(new OrderedPage(page.getId()));
                return true;
            }

        }

        private void pageRemoved(AdsEditorPageDef page) {
            for (OrderedPage order : this.list()) {
                if (order.pageId == page.getId()) {
                    order.pageRemoved();
                    break;
                }
                order.children.pageRemoved(page);
            }
        }

        private AdsEditorPageDef findPageById(Id id) {
            return getOwnerEditorPages().findById(id, EScope.ALL).get();
        }

        private static class PageOrderClipboardSupport extends ClipboardSupport<PageOrder> {

            public PageOrderClipboardSupport(PageOrder radixObject) {
                super(radixObject);
            }

            @Override
            public CanPasteResult canPaste(List<Transfer> transfers, DuplicationResolver resolver) {
                if (!this.radixObject.isNodeOrder()) {
                    return CanPasteResult.NO;
                }

                for (Transfer t : transfers) {
                    if (!(t instanceof OrderedPage.OrderedPageClipboardSupport.OrderedPageTransfer)) {
                        return CanPasteResult.NO;
                    }
                    final OrderedPage.OrderedPageClipboardSupport.OrderedPageTransfer tr = (OrderedPage.OrderedPageClipboardSupport.OrderedPageTransfer) t;
                    if (!(t.getObject() instanceof OrderedPage)) {
                        return CanPasteResult.NO;
                    } else {
                        final boolean result[] = new boolean[1];
                        result[0] = true;
                        OrderedPage page = (OrderedPage) t.getObject();
                        page.visit(new IVisitor() {
                            @Override
                            public void accept(RadixObject radixObject) {
                                if (radixObject instanceof OrderedPage) {
                                    OrderedPage op = (OrderedPage) radixObject;
                                    AdsEditorPageDef page = PageOrderClipboardSupport.this.radixObject.getOwnerEditorPages().getLocal().findById(op.pageId);
                                    if (page != null && page.getOwnerEditorPages() != tr.sourcePageSet) {
                                        result[0] = false;
                                    }
                                }
                            }
                        }, VisitorProviderFactory.createDefaultVisitorProvider());
                        if (!result[0]) {
                            return CanPasteResult.NO;
                        }
                    }

                }
                return CanPasteResult.YES;
            }

            @SuppressWarnings("unchecked")
            @Override
            public void paste(List<Transfer> transfers, DuplicationResolver resolver) {
                checkForCanPaste(transfers, resolver);

                for (Transfer transfer : transfers) {
                    final RadixObject objectInClipboard = transfer.getObject();
                    if (objectInClipboard instanceof OrderedPage) {
                        radixObject.add((OrderedPage) objectInClipboard);
                    }
                }
            }
        }

        @Override
        public ClipboardSupport<? extends RadixObject> getClipboardSupport() {
            return new PageOrderClipboardSupport(this);
        }
    }

    static class PageList extends AdsDefinitions<AdsEditorPageDef> {

        private PageOrder pageOrder = null;
        private boolean isSilentMode = false;

        public PageList() {
            super();
        }

        @Override
        protected void onAdd(final AdsEditorPageDef definition) {
            super.onAdd(definition);
            if (pageOrder != null && !isSilentMode) {
                pageOrder.pageAdded(definition);
            }
            if (isInBranch()) {
                EditorPagesOperationSupport.getDefault().fireChange();
            }
        }

        private void clear(boolean silent) {
            try {
                isSilentMode = silent;
                clear();
            } finally {
                isSilentMode = false;
            }
        }

        @Override
        protected void onRemove(final AdsEditorPageDef definition) {
            super.onRemove(definition);
            if (pageOrder != null && !isSilentMode) {
                pageOrder.pageRemoved(definition);
            }
            if (isInBranch()) {
                EditorPagesOperationSupport.getDefault().fireChange();
            }
        }

        @Override
        @SuppressWarnings("unchecked")
        public ClipboardSupport<? extends RadixObject> getClipboardSupport() {
            return new PageListClipboardSupport();
        }

        @Override
        protected ClipboardSupport.CanPasteResult canPaste(final List<Transfer> transfers, ClipboardSupport.DuplicationResolver resolver) {
            for (Transfer t : transfers) {
                if (!(t.getObject() instanceof AdsEditorPageDef)) {
                    if (t.getObject() instanceof OrderedPage) {
                        if (pageOrder == null || pageOrder.getClipboardSupport().canPaste(Collections.singletonList(t), resolver) != ClipboardSupport.CanPasteResult.YES) {
                            return ClipboardSupport.CanPasteResult.NO;
                        }
                    } else {
                        return ClipboardSupport.CanPasteResult.NO;
                    }
                }
            }
            return ClipboardSupport.CanPasteResult.YES;
        }

        private class PageListClipboardSupport extends Definitions.DefinitionsClipboardSupport {

            public PageListClipboardSupport() {
                super();
            }

            @Override
            @SuppressWarnings("unchecked")
            public CanPasteResult canPaste(final List transfers, DuplicationResolver resolver) {
                final List<Transfer> pages = new ArrayList<>();
                final List<Transfer> orders = new ArrayList<>();
                for (Object o : transfers) {
                    if (o instanceof Transfer) {
                        final Transfer t = (Transfer) o;
                        if (t.getObject() instanceof AdsEditorPageDef) {
                            pages.add(t);
                        } else if (t.getObject() instanceof OrderedPage) {
                            orders.add(t);
                        }
                    }
                }
                if (!pages.isEmpty()) {
                    CanPasteResult result = super.canPaste(pages, resolver);
                    if (result != CanPasteResult.YES) {
                        return result;
                    }
                }
                if (!orders.isEmpty()) {
                    if (pageOrder == null || pageOrder.getClipboardSupport().canPaste(transfers, resolver) != CanPasteResult.YES) {
                        return CanPasteResult.NO;
                    }
                }
                return CanPasteResult.YES;
            }

            @Override
            @SuppressWarnings("unchecked")
            public void paste(List transfers, DuplicationResolver resolver) {
                final List<Transfer> pages = new ArrayList<>();
                final List<Transfer> orders = new ArrayList<>();
                for (Object o : transfers) {
                    if (o instanceof Transfer) {
                        final Transfer t = (Transfer) o;
                        if (t.getObject() instanceof AdsEditorPageDef) {
                            pages.add(t);
                        } else if (t.getObject() instanceof OrderedPage) {
                            orders.add(t);
                        }
                    }
                }
                if (!pages.isEmpty()) {
                    super.paste(pages, resolver);
                }
                if (!orders.isEmpty()) {
                    if (pageOrder != null && pageOrder.getClipboardSupport().canPaste(transfers, resolver) == CanPasteResult.YES) {
                        pageOrder.getClipboardSupport().paste(orders, resolver);
                    }
                }
            }
        }
    }
    private final PageOrder pageOrder = new PageOrder(this);

    private EditorPages(AdsDefinition owner, org.radixware.schemas.adsdef.EditorPages xDef) {
        super(owner, new PageList());
        loadFrom(xDef);


    }

    protected final void loadFrom(org.radixware.schemas.adsdef.EditorPages xDef) {
        final HashMap<Id, Object> loadedPages = new HashMap<>();

        if (xDef != null) {
            final List<org.radixware.schemas.adsdef.EditorPage> xPages = xDef.getPageList();


            if (xPages != null && !xPages.isEmpty()) {
                for (org.radixware.schemas.adsdef.EditorPage xPage : xPages) {
                    getLocal().add(AdsEditorPageDef.Factory.loadFrom(xPage));
                }

            }

            pageOrder.loadFrom(xDef.getPageOrder(), loadedPages);

        }
        for (AdsEditorPageDef ep : this.getLocal()) {
            if (!loadedPages.containsKey(ep.getId())) {
                pageOrder.add(new OrderedPage(ep.getId()));
            }
        }

        ((PageList) this.getLocal()).pageOrder = pageOrder;
    }

    public void appendTo(org.radixware.schemas.adsdef.EditorPages xDef, ESaveMode saveMode) {
        this.pageOrder.appendTo(xDef.addNewPageOrder(), 1);
        for (AdsEditorPageDef page : getLocal()) {
            page.appendTo(xDef.addNewPage(), saveMode);
        }
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        pageOrder.visit(visitor, provider);
    }

    public PageOrder getOrder() {
        return pageOrder;
    }

    @Override
    public String getName() {
        return "Editor Pages";
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.EDITOR_PAGE;
    }

    @Override
    public JavaSourceSupport getJavaSourceSupport() {
        return new JavaSourceSupport(this) {
            @Override
            public CodeWriter getCodeWriter(UsagePurpose purpose) {
                return new AdsEditorPagesWriter(this, EditorPages.this, purpose);
            }
        };
    }

    private void restoreState() {
        final Collection<Id> ids = getOrder().getOrderedPageIds();

        for (AdsEditorPageDef p : getLocal()) {
            if (!ids.contains(p.getId())) {
                getOrder().addPageToOrder(p);
            }
        }
    }

    HierarchyIterator<ExtendableDefinitions<AdsEditorPageDef>> getHierarchyIterator(HierarchyIterator.Mode mode) {
        return newIterator(EScope.ALL, mode);
    }

    public static final class EditorPagesOperationSupport extends RadixEventSource {

        private static EditorPagesOperationSupport instance = new EditorPagesOperationSupport();

        public static EditorPagesOperationSupport getDefault() {
            return instance;
        }

        @SuppressWarnings("unchecked")
        public void fireChange() {
            super.fireEvent(new RadixEvent());
        }
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);
        pageOrder.collectDependences(list);
    }

    public void afterOverwrite() {
        PageList list = (PageList) getLocal();
        list.clear(true);
    }
}
