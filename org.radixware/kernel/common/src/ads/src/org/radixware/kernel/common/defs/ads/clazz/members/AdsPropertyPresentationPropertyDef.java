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

package org.radixware.kernel.common.defs.ads.clazz.members;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.apache.commons.lang.StringEscapeUtils;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.*;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.IEnvDependent;
import org.radixware.kernel.common.defs.ads.clazz.*;
import org.radixware.kernel.common.defs.ads.clazz.entity.*;
import org.radixware.kernel.common.defs.ads.clazz.presentation.*;
import org.radixware.kernel.common.defs.ads.type.AdsDefinitionType;
import org.radixware.kernel.common.defs.ads.type.AdsEnumType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.EPropNature;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.IdPrefixes;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.common.utils.agents.IObjectAgent;
import org.radixware.schemas.adsdef.AbstractPropertyDefinition;
import org.radixware.schemas.adsdef.AbstractPropertyDefinition.DependentItems;
import org.radixware.schemas.adsdef.ClassDefinition;
import org.radixware.schemas.adsdef.PropertyDefinition;


public class AdsPropertyPresentationPropertyDef extends AdsClientSidePropertyDef implements IClassInclusive {

    private static class FixedSourceProp extends AdsPropertyPresentationPropertyDef {

        private final IModelPublishableProperty ssProp;

        FixedSourceProp(final IModelPublishableProperty source) {
            super(source);
            ssProp = source;
            EAccess access = ((AdsDefinition) source).getAccessMode();
            setAccessMode(access == EAccess.PRIVATE ? EAccess.DEFAULT : access);
        }

        @Override
        public IModelPublishableProperty findServerSideProperty() {
            return ssProp;
        }

        public boolean isTemporary() {
            return true;
        }
    }

    public static class Factory extends AdsPropertyDef.Factory {

        public static final AdsPropertyPresentationPropertyDef newInstance(IModelPublishableProperty sourceProp) {
            return new AdsPropertyPresentationPropertyDef(sourceProp);
        }

        public static final AdsPropertyPresentationPropertyDef newInstance() {
            return new AdsPropertyPresentationPropertyDef((IModelPublishableProperty) null);
        }

        public static final AdsPropertyPresentationPropertyDef newTemporaryInstance(IModelPublishableProperty sourceProp) {
            AdsPropertyPresentationPropertyDef prop = new FixedSourceProp(sourceProp);
            prop.setContainer((RadixObject) sourceProp);
            return prop;
        }

        public static final AdsPropertyPresentationPropertyDef newTemporaryInstance(AdsPropertyGroup context) {
            AdsPropertyPresentationPropertyDef prop = newInstance(null);
            prop.setContainer(context);
            return prop;
        }

        public static final AdsPropertyPresentationPropertyDef newTemporaryInstance(AdsPropertyGroup context, boolean local) {
            AdsPropertyPresentationPropertyDef prop = local ? new AdsPropertyPresentationPropertyDef(true) : newInstance(null);
            prop.setContainer(context);
            return prop;
        }
    }

    @Override
    public boolean isTemporary() {
        return super.isTemporary();
    }

    private final class PropertyPresentationPropertyClipboardSupport extends PropertyClipboardSupport {

        public PropertyPresentationPropertyClipboardSupport(AdsPropertyDef radixObject) {
            super(radixObject);
        }

        @Override
        public CanPasteResult canPaste(List<Transfer> transfers, DuplicationResolver resolver) {
            CanPasteResult canPaste = canPasteInFields(transfers, resolver);

            if (canPaste == CanPasteResult.NO) {
                canPaste = getEmbeddedClassAgent().getObject().getClipboardSupport().canPaste(transfers, resolver);
            }

            return canPaste;
        }

        @Override
        public void paste(List<Transfer> transfers, DuplicationResolver resolver) {
            if (canPasteInFields(transfers, resolver) != CanPasteResult.NO) {
                super.paste(transfers, resolver);
            }

            if (getEmbeddedClassAgent().invite(true)) {
                getEmbeddedClassAgent().getObject().getClipboardSupport().paste(transfers, resolver);
            }
        }

        private CanPasteResult canPasteInFields(List<Transfer> transfers, DuplicationResolver resolver) {
            return super.canPaste(transfers, resolver);
        }
    }
    private final Dependents dependents = new Dependents();

    AdsPropertyPresentationPropertyDef(final AbstractPropertyDefinition xProp) {
        super(xProp);

        final ClassDefinition xCLass = xProp.getEmbeddedClass();
        if (xCLass != null) {
            embeddedClass = PropertyPresentationEmbeddedClass.Factory.newInstance(this, xCLass);
        }

        if (xProp.getDependentItems() != null) {
            dependents.load(xProp.getDependentItems());
        }

        if (xProp.isSetEmbeddedProperty()) {
            this.embeddedProperty = (AdsDynamicPropertyDef) AdsPropertyDef.Factory.loadFrom(this, xProp.getEmbeddedProperty());
            embeddedProperty.getPresentationSupport().getPresentation().setPresentable(true);
        }

    }

    AdsPropertyPresentationPropertyDef(final AdsPropertyPresentationPropertyDef source) {
        super(source.getId(), source.getName());
        if (source.getEmbeddedProperty() != null) {
            this.embeddedProperty = new AdsDynamicPropertyDef(this, source.getEmbeddedProperty(), false);
            embeddedProperty.getPresentationSupport().getPresentation().setPresentable(true);
        }
    }

    AdsPropertyPresentationPropertyDef(final IModelPublishableProperty source) {
        super(source == null ? Id.Factory.newInstance(EDefinitionIdPrefix.ADS_DYNAMIC_PROP) : source.getId(), source == null ? "newPropertyPresentation" : source.getName());
//        if (source == null) {
//            embeddedProperty = new AdsDynamicPropertyDef(this, getId(), getName());
//            embeddedProperty.getPresentationSupport().getPresentation().setPresentable(true);
//        }
    }

    AdsPropertyPresentationPropertyDef(boolean local) {
        super(Id.Factory.newInstance(EDefinitionIdPrefix.ADS_DYNAMIC_PROP), "newLocalPropertyPresentation");

        embeddedProperty = new AdsDynamicPropertyDef(this, getId(), getName());
        embeddedProperty.getPresentationSupport().getPresentation().setPresentable(true);
    }

    @Override
    public EPropNature getNature() {
        return EPropNature.PROPERTY_PRESENTATION;
    }

    @Override
    protected AdsPropertyDef createOvr(final boolean forOverride) {
        return new AdsPropertyPresentationPropertyDef(this);
    }

    @Override
    public void afterOverwrite() {
        super.afterOverwrite();
        rwLock.writeLock().lock();
        try {        
            // workaround
            if (embeddedClass != null) {
                embeddedClass.afterOverwrite();
                embeddedClass = null;
            }
        } finally {
            rwLock.writeLock().unlock();
        }

        dependents.dependents.clear();
    }

    @Override
    public void afterOverride() {
        super.afterOverride();
        dependents.dependents.clear();
    }

    public boolean isLocal() {
        return embeddedProperty != null;
    }

    @Override
    public void appendTo(PropertyDefinition xDef, ESaveMode saveMode) {
        super.appendTo(xDef, saveMode);

        rwLock.readLock().lock();
        try {
            if (embeddedClass != null && embeddedClass.isUsed()) {
                ClassDefinition xClass = xDef.addNewEmbeddedClass();
                embeddedClass.appendTo(xClass, saveMode);
            }

            dependents.appentTo(xDef.addNewDependentItems());
        } finally {
            rwLock.readLock().unlock();
        }
        if (embeddedProperty != null) {
            embeddedProperty.appendExtractionTo(xDef.addNewEmbeddedProperty(), saveMode);
        }
    }
    private final IObjectAgent<AdsEmbeddedClassDef> agent = new AdsEmbeddedClassDef.ClassAgent<AdsEmbeddedClassDef>(this) {
        @Override
        protected AdsEmbeddedClassDef createTemporary() {
            return PropertyPresentationEmbeddedClass.Factory.newTemporaryInstance(AdsPropertyPresentationPropertyDef.this);
        }

        @Override
        public AdsEmbeddedClassDef getObject() {
            return getObjectSource();
        }

        @Override
        public boolean isActual() {
            return getLocalEmbeddedClass(false) != null;
        }
    };
    private volatile PropertyPresentationEmbeddedClass embeddedClass;
    private AdsDynamicPropertyDef embeddedProperty;
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();

    @Override
    public SearchResult<AdsClassDef> findEmbeddedClass(EScope scope) {
        if (embeddedClass != null) {
            return SearchResult.single((AdsClassDef) embeddedClass);
        }

        if (scope != EScope.LOCAL) {
            final List<AdsClassDef> searchInLevel = AdsEmbeddedClassDef.searchInLevel(this.getHierarchy().findOverwritten().all());

            if (!searchInLevel.isEmpty()) {
                return SearchResult.list(searchInLevel);
            }
        }
        return SearchResult.<AdsClassDef>empty();
    }
    private final Object embeddedClassLock = new Object();

    @Override
    public AdsEmbeddedClassDef getLocalEmbeddedClass(boolean create) {
        if (create) {
            rwLock.writeLock().lock();
            try {
                if (embeddedClass == null) {

                    final AdsClassDef overEmbedded = findEmbeddedClass(EScope.LOCAL_AND_OVERWRITE).get();

                    if (overEmbedded != null) {
                        embeddedClass = PropertyPresentationEmbeddedClass.Factory.newInstance(this, overEmbedded.getId());
                        embeddedClass.setOverwrite(true);
                    } else {
                        embeddedClass = PropertyPresentationEmbeddedClass.Factory.newInstance(this);
                    }
                    setEditState(EEditState.MODIFIED);
                }
                return embeddedClass;
            } finally {
                rwLock.writeLock().unlock();
            }
        } else {
            rwLock.readLock().lock();
            try {
                return embeddedClass;
            } finally {
                rwLock.readLock().unlock();
            }
        }
    }

    public interface IFilter {

        boolean accept(Id id);
    }
    public static final IFilter COMMAND_FILTER = new IFilter() {
        @Override
        public boolean accept(Id id) {
            return IdPrefixes.isCommandId(id);
        }
    };
    public static final IFilter PROPERTY_FILTER = new IFilter() {
        @Override
        public boolean accept(Id id) {
            return IdPrefixes.isAdsPropertyId(id);
        }
    };

    public class Dependents {

        private List<Id> dependents = new ArrayList<>();

        private Dependents() {
        }

        private void load(DependentItems dependentProperties) {
            dependents.addAll(dependentProperties.getDependentList());
        }

        public List<Id> getDependents(EScope scope) {
            return getDependents(scope, new IFilter() {
                @Override
                public boolean accept(Id id) {
                    return true;
                }
            });
        }

        public List<Id> getDependents(EScope scope, IFilter filter) {
            final Set<Id> result = new HashSet<>();
            switch (scope) {

                case ALL:
                    final SearchResult<AdsPropertyDef> override = AdsPropertyPresentationPropertyDef.this.getHierarchy().findOverridden();
                    if (override.get() instanceof AdsPropertyPresentationPropertyDef) {
                        result.addAll(((AdsPropertyPresentationPropertyDef) override.get()).getDependents().getDependents(scope, filter));
                    }
                case LOCAL_AND_OVERWRITE:
                    final SearchResult<AdsPropertyDef> overwrite = AdsPropertyPresentationPropertyDef.this.getHierarchy().findOverwritten();
                    if (overwrite.get() instanceof AdsPropertyPresentationPropertyDef) {
                        result.addAll(((AdsPropertyPresentationPropertyDef) overwrite.get()).getDependents().getDependents(scope, filter));
                    }
                case LOCAL:
                    for (final Id id : dependents) {
                        if (filter.accept(id)) {
                            result.add(id);
                        }
                    }
            }
            return new ArrayList<>(result);
        }

        public void addDependent(Id id) {
            dependents.add(id);
            setEditState(EEditState.MODIFIED);
        }

        public void removeDependent(Id id) {
            dependents.remove(id);
            setEditState(EEditState.MODIFIED);
        }

        private void appentTo(DependentItems dependentProperties) {
            if (!dependents.isEmpty()) {
                dependentProperties.getDependentList().addAll(dependents);
            }
        }
    }

    public Dependents getDependents() {
        return dependents;
    }

    AdsDynamicPropertyDef getEmbeddedProperty() {
        return embeddedProperty;
    }

    @Override
    public IObjectAgent<AdsEmbeddedClassDef> getEmbeddedClassAgent() {
        return agent;
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        rwLock.readLock().lock();
        try {
            if (embeddedClass != null) {
                embeddedClass.visit(visitor, provider);
            }
        } finally {
            rwLock.readLock().unlock();
        }
        if (embeddedProperty != null) {
            embeddedProperty.visit(visitor, provider);
        }
    }


    public IModelPublishableProperty.Provider findModelPropertyProvider() {
        AdsClassDef ownerClass = getOwnerClass();
        if (ownerClass == null) {
            return null;
        }

        if (ownerClass instanceof AdsGroupModelClassDef) {
            AdsSelectorPresentationDef spr = ((AdsGroupModelClassDef) ownerClass).getOwnerSelectorPresentation();
            if (spr == null) {
                return null;
            }
            AdsEntityObjectClassDef clazz = spr.getOwnerClass();
            if (clazz == null) {
                return null;
            }
            AdsEntityClassDef entity = clazz.findRootBasis();
            if (entity == null) {
                return null;
            }
            AdsEntityGroupClassDef group = entity.findEntityGroup();
            if (group == null) {
                return null;
            }
            return group;
        }
        for (RadixObject container = ownerClass.getContainer(); container != null; container = container.getContainer()) {
            if (container instanceof IModelPublishableProperty.Provider) {
                return (IModelPublishableProperty.Provider) container;
            }
        }
        return null;
    }

    public IModelPublishableProperty findServerSideProperty() {
        if (embeddedProperty != null) {
            return embeddedProperty;
        }
        final IModelPublishableProperty.Provider provider = findModelPropertyProvider();
        if (provider == null) {
            return null;
        } else {
            return provider.getModelPublishablePropertySupport().findById(getId(), EScope.ALL);
        }
    }

//    public AdsPropertyDef findServerSideProperty() {
//        final AdsClassDef clazz = findServerSideClassDef();
//        if (clazz == null) {
//            return null;
//        } else {
//            final AdsPropertyDef p = clazz.getProperties().findById(getId(), EScope.ALL);
//            if (p instanceof AdsPropertyDef) {
//                return p;
//            } else {
//                return null;
//            }
//        }
//    }
    public void setPublishedPropertyId(final Id id) {
        final RadixObject container = getContainer();
        setContainer(null);
        setId(id);
        setContainer(container);
    }

    @Override
    public void collectDependences(final List<Definition> list) {
        super.collectDependences(list);
        final IModelPublishableProperty prop = findServerSideProperty();
        if (prop instanceof Definition) {
            list.add((Definition) prop);
        }
    }

    @Override
    public ClipboardSupport<AdsPropertyDef> getClipboardSupport() {
        return new PropertyPresentationPropertyClipboardSupport(this);
    }

    @Override
    public String getTypeTitle() {
        return "Presentation Property";
    }

    public boolean isPublishedForEditing() {
        final EditorPages pagesObj = findPublishingPagesList();
        if (pagesObj == null) {
            return false;
        }
        final List<AdsEditorPageDef> pages = pagesObj.get(EScope.ALL);

        if (pages == null) {
            return false;
        } else {
            final Id id = getId();
            for (AdsEditorPageDef p : pages) {
                if (p.containsProperty(id)) {
                    return true;
                }
            }
            return false;
        }
    }

    public EditorPages findPublishingPagesList() {
        final AdsClassDef clazz = getOwnerClass();
        if (clazz instanceof AdsEntityModelClassDef) {
            final AdsEditorPresentationDef epr = ((AdsEntityModelClassDef) clazz).getOwnerEditorPresentation();
            if (epr == null) {
                return null;
            }
            return epr.getEditorPages();
        } else if (clazz instanceof AbstractFormModelClassDef) {
            final IAdsFormPresentableClass fh = (IAdsFormPresentableClass) ((AbstractFormModelClassDef) clazz).getOwnerClass();
            if (fh == null) {
                return null;
            }
            return fh.getPresentations().getEditorPages();
        } else if (clazz instanceof AdsFilterModelClassDef) {
            final AdsFilterDef filter = ((AdsFilterModelClassDef) clazz).getOwnerFilterDef();
            if (filter == null) {
                return null;
            }
            return filter.getEditorPages();
        } else {
            return null;
        }
    }
//    @Override
//    public EAccess getMinimumAccess() {
//        IModelPublishableProperty prop = findServerSideProperty();
//        if (prop instanceof AdsDefinition) {
//            return ((AdsDefinition) prop).getAccessMode();
//        }
//        return EAccess.DEFAULT;
//    }
}
