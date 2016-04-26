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

import java.util.Collection;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.defs.*;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinitions;
import org.radixware.kernel.common.defs.ads.ITitledDefinition;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.exploreritems.AdsExplorerItemWriter;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.TitledAdsDefinition;

public abstract class AdsExplorerItemDef extends AdsDefinition implements IJavaSource, IOverridable, IOverwritable, ITitledDefinition {

    protected Id titleId = null;

    public AdsExplorerItemDef(TitledAdsDefinition xDef) {
        super(xDef);
        this.titleId = xDef.getTitleId();
    }

    public AdsExplorerItemDef(Id id, String name) {
        super(id, name);
    }

    public abstract ERuntimeEnvironmentType getClientEnvironment();

    public abstract void setClientEnvironment(ERuntimeEnvironmentType e);

    @Override
    public void afterOverride() {
        //do nothing
    }

    @Override
    public void afterOverwrite() {
        //do nothing
    }

    @Override
    public boolean allowOverwrite() {
        //return getOwnerDef() != null && getOwnerDef().isTopLevelDefinition();
//        if (isTopLevelDefinition()) {
//            return true;
//        }
//        AdsDefinition owner = getOwnerDef();
//        if (owner != null && (owner instanceof AdsEditorPresentationDef || owner.isTopLevelDefinition())) {
//            return true;
//        }
//        return canBeFinal();
        return true;
    }

    public void appendTo(TitledAdsDefinition xDef, ESaveMode saveMode) {
        super.appendTo(xDef, saveMode);
        if (titleId != null) {
            xDef.setTitleId(titleId);
        }
    }

    public boolean canChangeClientEnvironment() {
        return true;
    }

    @Override
    public Id getTitleId() {
        return titleId;
    }

    @Override
    public void setTitleId(Id titleId) {
        this.titleId = titleId;
        this.setEditState(EEditState.MODIFIED);
    }

    public AdsEditorPresentationDef findOwnerEditorPresentation() {
        AdsDefinition def = getOwnerDef();
        while (def != null) {
            if (def instanceof AdsEditorPresentationDef) {
                return (AdsEditorPresentationDef) def;
            }
            def = def.getOwnerDef();
        }
        return null;
    }

    @Override
    public abstract String getTitle(EIsoLanguage lang);

    @Override
    public boolean setTitle(EIsoLanguage language, String title) {
        this.titleId = setLocalizedStringValue(language, titleId, title);
        setEditState(EEditState.MODIFIED);
        return titleId != null;
    }

    public AdsParagraphExplorerItemDef findOwnerExplorerRoot() {
        AdsDefinition candidate = this;
        while (candidate != null) {
            if (candidate instanceof AdsParagraphExplorerItemDef && candidate.isTopLevelDefinition()) {
                return (AdsParagraphExplorerItemDef) candidate;

            } else if (candidate instanceof AdsEditorPresentationDef) {
                return null;
            } else {
                candidate = candidate.getOwnerDef();
            }
        }
        return null;
    }

    public ExplorerItems getOwnerExplorerItems() {
        for (RadixObject owner = getContainer(); owner != null; owner = owner.getContainer()) {
            if (owner instanceof ExplorerItems) {
                return (ExplorerItems) owner;
            }
        }
        return null;
    }

    @Override
    public JavaSourceSupport getJavaSourceSupport() {
        return new JavaSourceSupport(this) {
            @Override
            public CodeWriter getCodeWriter(UsagePurpose purpose) {
                return AdsExplorerItemWriter.Factory.newInstance(this, AdsExplorerItemDef.this, purpose);
            }

            @Override
            public EnumSet<ERuntimeEnvironmentType> getSupportedEnvironments() {
                return EnumSet.of(ERuntimeEnvironmentType.SERVER);
            }
        };
    }

    @Override
    public void collectUsedMlStringIds(Collection<MultilingualStringInfo> ids) {
        super.collectUsedMlStringIds(ids);
        ids.add(new MultilingualStringInfo(AdsExplorerItemDef.this) {
            @Override
            public Id getId() {
                return titleId;
            }

            @Override
            public void updateId(Id newId) {
                titleId = newId;
            }

            @Override
            public EAccess getAccess() {
                return AdsExplorerItemDef.this.getAccessMode();
            }

            @Override
            public String getContextDescription() {
                return /*
                         * getTypeTitle().concat()
                         */ "Explorer Item Title";
            }

            @Override
            public boolean isPublished() {
                return AdsExplorerItemDef.this.isPublished();
            }
        });
    }

    @Override
    public boolean isSuitableContainer(AdsDefinitions collection) {
        return collection.getContainer() instanceof ExplorerItems.Children;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Hierarchy<AdsExplorerItemDef> getHierarchy() {
        return new DefaultHierarchy<AdsExplorerItemDef>() {
            @Override
            public SearchResult< AdsExplorerItemDef> findOverridden() {
                if (isTopLevelDefinition()) {
                    return super.findOverridden();
                }

                final List<AdsExplorerItemDef> collection = new LinkedList<>();
                ExplorerItems.Children owner = object.getOwnerChildren();
                if (owner != null) {
                    HierarchyIterator<? extends ExtendableDefinitions<AdsExplorerItemDef>> iterator = owner.newIterator(ExtendableDefinitions.EScope.ALL, HierarchyIterator.Mode.FIND_ALL);
                    if (iterator.hasNext()) {
                        ExtendableDefinitions<AdsExplorerItemDef> next = iterator.next().first();
                        assert next == owner;
                    }
                    while (iterator.hasNext()) {
                        AdsExplorerItemDef ei = iterator.next().first().findById(getId(), ExtendableDefinitions.EScope.LOCAL).get();
                        if (ei != null) {
                            if (ei.getOwnerDef() != null && ei.getOwnerDef().getId() == getOwnerDef().getId()) {
                                continue;
                            }
                            collection.add(ei);
                        }

                    }
                }
                if (collection.isEmpty()) {
                    return SearchResult.empty();
                } else {
                    return SearchResult.list(collection);
                }
            }

            @Override
            public SearchResult<AdsExplorerItemDef> findOverwritten() {

                if (isTopLevelDefinition()) {
                    return super.findOverwritten();
                }
                ExplorerItems.Children children = getOwnerChildren();
                if (children == null) {

                    return SearchResult.empty();
                }
                final List<AdsExplorerItemDef> collection = new LinkedList<>();
                Definition ownerDef = children.getOwnerDefinition();
                AdsEditorPresentationDef root = findOwnerEditorPresentation();

                if (ownerDef instanceof AdsEditorPresentationDef) {
                    AdsEditorPresentationDef presentation = (AdsEditorPresentationDef) ownerDef;
                    final SearchResult<AdsEditorPresentationDef> result = presentation.getHierarchy().findOverwritten();
                    if (!result.isEmpty()) {

                        for (final AdsEditorPresentationDef ovr : result.all()) {
                            if (ovr != null) {
                                ovr.getExplorerItems().getChildren().findById(getId(), ExtendableDefinitions.EScope.LOCAL_AND_OVERWRITE).iterate(new SearchResult.Acceptor<AdsExplorerItemDef>() {

                                    @Override
                                    public void accept(AdsExplorerItemDef object) {
                                        if (!collection.contains(object)) {
                                            collection.add(object);
                                        }
                                    }
                                });
                            }
                        }
                    }
                } else if (ownerDef instanceof AdsParagraphExplorerItemDef) {
                    AdsParagraphExplorerItemDef paragraph = (AdsParagraphExplorerItemDef) ownerDef;
                    final SearchResult<AdsExplorerItemDef> result = paragraph.getHierarchy().findOverwritten();
                    if (!result.isEmpty()) {

                        for (final AdsExplorerItemDef ovr : result.all()) {
                            if (ovr instanceof AdsParagraphExplorerItemDef) {
                                ((AdsParagraphExplorerItemDef) ovr).getExplorerItems().getChildren().findById(getId(), ExtendableDefinitions.EScope.LOCAL_AND_OVERWRITE).iterate(new SearchResult.Acceptor<AdsExplorerItemDef>() {

                                    @Override
                                    public void accept(AdsExplorerItemDef object) {
                                        if (!collection.contains(object)) {
                                            collection.add(object);
                                        }
                                    }
                                });
                            }
                        }
                    }
                }

//                if (root == ownerDef && root != null) {
//                    final SearchResult<AdsEditorPresentationDef> result = root.getHierarchy().findOverwritten();
//                    if (!result.isEmpty()) {
//                        for (final AdsEditorPresentationDef ovr : result.all()) {
//                            if (ovr != null) {
//                                final ExplorerItems explorerItems = ovr.getExplorerItems();
//                                if (explorerItems != null) {
//                                    final AdsExplorerItemDef ei = explorerItems.findChildExplorerItem(getId());
//
//                                    if (ei != null) {
//                                        children = ei.getOwnerChildren();
//                                        ownerDef = children.getOwnerDefinition();
//                                        root = ei.findOwnerEditorPresentation();
//                                        if (ownerDef == root) {
//                                            if (!collection.contains(ei)) {
//                                                collection.add(ei);
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                } else {
//                    AdsParagraphExplorerItemDef paragraph = findOwnerExplorerRoot();
//                    if (paragraph != null && paragraph == ownerDef) {
//                        final SearchResult<AdsExplorerItemDef> result = paragraph.getHierarchy().findOverwritten();
//                        if (!result.isEmpty()) {
//                            for (final AdsExplorerItemDef ovr : result.all()) {
//                                if (ovr instanceof AdsParagraphExplorerItemDef) {
//                                    final ExplorerItems explorerItems = ((AdsParagraphExplorerItemDef) ovr).getExplorerItems();
//                                    if (explorerItems != null) {
//                                        final AdsExplorerItemDef ei = explorerItems.findChildExplorerItem(getId());
//
//                                        if (ei != null) {
//                                            children = ei.getOwnerChildren();
//                                            ownerDef = children.getOwnerDefinition();
//                                            paragraph = ei.findOwnerExplorerRoot();
//                                            if (ownerDef == paragraph) {
//                                                if (!collection.contains(ei)) {
//                                                    collection.add(ei);
//                                                }
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
                if (collection.isEmpty()) {
                    return SearchResult.empty();
                } else {
                    return SearchResult.list(collection);
                }
            }
        };
    }

    ExplorerItems.Children getOwnerChildren() {
        if (getContainer() != null) {
            RadixObject obj = getContainer().getContainer();
            if (obj instanceof ExplorerItems.Children) {
                return (ExplorerItems.Children) obj;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public EAccess getAccessMode() {
        if (checkAccessChangePossibility()) {
            return super.getAccessMode();
        } else {
            return EAccess.PRIVATE;
        }
    }

    private boolean checkAccessChangePossibility() {
//        ExplorerItems.Children children = getOwnerChildren();
//        if (children == null) {
//            return true;
//        }
//        Definition ownerDef = children.getOwnerDefinition();
//        AdsDefinition root = findOwnerExplorerRoot();
//        if (root == null) {
//            root = findOwnerEditorPresentation();
//        }
//        if (root == ownerDef) {
//            return true;
//        } else {
//            return false;
//        }
        return true;
    }

    @Override
    public boolean canBeFinal() {
//        ExplorerItems.Children children = getOwnerChildren();
//        if (children == null) {
//            return true;
//        }
//        Definition ownerDef = children.getOwnerDefinition();
//        AdsEditorPresentationDef root = findOwnerEditorPresentation();
//
//        if (root == ownerDef) {
//            return true;
//        } else {
//            return false;
//        }
        return false;
    }

    @Override
    public boolean canChangeAccessMode() {
        if (checkAccessChangePossibility()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean canChangeFinality() {
        return false;
    }

    @Override
    public EAccess getMinimumAccess() {
        return EAccess.PRIVATE;
    }
//    protected static class ReferenceCache {
//
//        public abstract static class Registrator {
//
//            protected abstract AdsDefinition[] registeryInfo();
//        }
//
//        public final void registerRef(Registrator registrator) {
//            AdsDefinition[] registryInfo = registrator.registeryInfo();
//        }
//    }

    protected abstract void collectDependencesImpl(final boolean direct, final boolean forModule, final List<Definition> list);

    @Override
    public final void collectDependences(final List<Definition> list) {
        super.collectDependences(list);
        this.collectDependencesImpl(false, false, list);
        if (titleId != null) {
            AdsMultilingualStringDef str = findLocalizedString(titleId);
            if (str != null) {
                list.add(str);
            }
        }
    }

    @Override
    public final void collectDirectDependences(final List<Definition> list) {
        super.collectDependences(list);
        this.collectDependencesImpl(true, false, list);
    }

    @Override
    protected void collectDependencesForModule(List<Definition> list) {
        collectDependencesImpl(true, true, list);
    }

    @Override
    protected void insertToolTipPrefix(StringBuilder sb) {
        super.insertToolTipPrefix(sb);
        String access = getDefaultAccess().getName().toUpperCase().charAt(0) + getDefaultAccess().getName().substring(1, getDefaultAccess().getName().length());
        sb.append("<b>").append(access).append(' ').append(getClientEnvironment().getName()).append("</b>&nbsp;");
    }

    @Override
    protected void appendAdditionalToolTip(StringBuilder sb) {
        super.appendAdditionalToolTip(sb);
        if (getTitleId() != null) {
            sb.append("<br>Title:");
            AdsLocalizingBundleDef bundle = findExistingLocalizingBundle();
            if (bundle != null) {
                for (EIsoLanguage lan : bundle.getLanguages()) {
                    sb.append("<br>&nbsp;(").append(lan.toString()).append("): ").append(getTitle(lan));
                }
            }
        }
    }
}
