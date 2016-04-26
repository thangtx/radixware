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
package org.radixware.kernel.common.defs.ads.clazz;

import java.text.MessageFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.*;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.ModelClassInheritance.FilterModelClassInheritance;
import org.radixware.kernel.common.defs.ads.clazz.ModelClassInheritance.FormModelClassInheritance;
import org.radixware.kernel.common.defs.ads.clazz.ModelClassInheritance.ReportModelClassInheritance;
import org.radixware.kernel.common.defs.ads.clazz.algo.AdsAlgoClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.*;
import org.radixware.kernel.common.defs.ads.clazz.enumeration.EnumFieldEmbeddedClass;
import org.radixware.kernel.common.defs.ads.clazz.form.AdsFormHandlerClassDef;
import org.radixware.kernel.common.defs.ads.clazz.form.AdsFormModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.PropertyPresentationEmbeddedClass;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsCursorClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsProcedureClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsSqlClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsStatementClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportModelClassDef;
import org.radixware.kernel.common.defs.ads.command.AdsCommandModelClassDef;
import org.radixware.kernel.common.defs.ads.common.AdsVisitorProviders;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParagraphModelClassDef;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.module.AdsPath;
import org.radixware.kernel.common.defs.ads.platform.IPlatformClassPublisher;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration.TypeArgument;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration.TypeArguments;
import org.radixware.kernel.common.defs.ads.type.*;
import org.radixware.kernel.common.defs.ads.ui.AdsCustomWidgetModelClassDef;
import org.radixware.kernel.common.defs.ads.ui.AdsDialogModelClassDef;
import org.radixware.kernel.common.defs.ads.ui.AdsPropEditorModelClassDef;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.RadixObjectError;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.Segment;
import org.radixware.kernel.common.repository.ads.AdsSegment;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.ClassDefinition;
import org.radixware.schemas.xscml.TypeDeclaration;

public class Inheritance extends RadixObject {

    public abstract class SuperClassLookupContext implements IAdsTypedObject {

        protected transient AdsTypeDeclaration typeRef;

        protected SuperClassLookupContext() {
        }

        @Override
        public AdsTypeDeclaration getType() {
            return null;
        }

        public void setType(final AdsTypeDeclaration type) {
            this.typeRef = type;
        }

        @Override
        public boolean isTypeAllowed(final EValType type) {
            return type == EValType.USER_CLASS;
        }

        @Override
        public boolean isTypeRefineAllowed(final EValType type) {
            return true;
        }

        protected Inheritance getOwnerInheritance() {
            return Inheritance.this;
        }

        protected void appendTo(final TypeDeclaration xDef) {
            if (typeRef != null && typeRef != AdsTypeDeclaration.Factory.undefinedType()) {
                this.typeRef.appendTo(xDef);
            }
        }

        protected void loadFrom(final TypeDeclaration type) {
            this.typeRef = AdsTypeDeclaration.Factory.loadFrom(type);
        }
    }

    public static final class Factory {

        private Factory() {
            super();
        }

        public static Inheritance loadFrom(final AdsClassDef context, final ClassDefinition xDef) {
            if (xDef == null) {
                return newInstance(context);
            }
            final AdsTypeDeclaration superClass = AdsTypeDeclaration.Factory.loadFrom(xDef.getExtends(), null);
            List<AdsTypeDeclaration> superInterfaces = null;
            if (xDef.getImplements() != null) {
                final List<TypeDeclaration> xDecls = xDef.getImplements().getInterfaceList();
                if (xDecls != null && !xDecls.isEmpty()) {
                    superInterfaces = new LinkedList<>();
                    for (TypeDeclaration xDecl : xDecls) {
                        superInterfaces.add(AdsTypeDeclaration.Factory.loadFrom(xDecl));
                    }
                }
            }
            return newInstance(context, superClass, superInterfaces);
        }

        public static Inheritance newInstance(final AdsClassDef context, final AdsTypeDeclaration superClass, final List<AdsTypeDeclaration> superInterfaces) {
            if (context instanceof AdsCommandModelClassDef) {
                return new ModelClassInheritance.CommandModelInheritance((AdsCommandModelClassDef) context, superInterfaces);
            } else if (context instanceof AdsFilterModelClassDef) {
                return new FilterModelClassInheritance((AdsFilterModelClassDef) context, superInterfaces);
            } else if (context instanceof AdsReportModelClassDef) {
                return new ReportModelClassInheritance((AdsReportModelClassDef) context, superInterfaces);
            } else if (context instanceof AdsFormHandlerClassDef) {
                return new PredefinedClassInheritance.FormClassInheritance((AdsFormHandlerClassDef) context, superClass, superInterfaces);
            } else if (context instanceof AdsFormModelClassDef) {
                return new FormModelClassInheritance((AdsFormModelClassDef) context, superInterfaces);
            } else if (context instanceof AdsEntityModelClassDef) {
                return new ModelClassInheritance.EntityModelInheritance((AdsEntityModelClassDef) context, superInterfaces);
            } else if (context instanceof AdsGroupModelClassDef) {
                return new ModelClassInheritance.GroupModelInheritance((AdsGroupModelClassDef) context, superInterfaces);
            } else if (context instanceof AdsParagraphModelClassDef) {
                return new ModelClassInheritance.ParagraphModelInheritance((AdsParagraphModelClassDef) context, superInterfaces);
            } else if (context instanceof AdsCustomWidgetModelClassDef) {
                return new ModelClassInheritance.CustomWidgetModelClassInheritance((AdsCustomWidgetModelClassDef) context, superInterfaces);
            } else if (context instanceof AdsPropEditorModelClassDef) {
                return new ModelClassInheritance.PropEditorModelClassInheritance((AdsPropEditorModelClassDef) context, superInterfaces);
            } else if (context instanceof AdsDialogModelClassDef) {
                return new ModelClassInheritance.DialogModelClassInheritance((AdsDialogModelClassDef) context, superInterfaces);
            } else if (context instanceof AdsAlgoClassDef) {
                return new PredefinedClassInheritance.AlgoClassInheritance((AdsAlgoClassDef) context, superClass, superInterfaces);
            } else if (context instanceof AdsPresentationEntityAdapterClassDef) {
                return new PredefinedClassInheritance.PresentationEntityAdapterClassInheritance((AdsPresentationEntityAdapterClassDef) context, superInterfaces);
            } else if (context instanceof AdsEntityClassDef) {
                return new PredefinedClassInheritance.EntityClassInheritance((AdsEntityClassDef) context, superClass, superInterfaces);
            } else if (context instanceof AdsEntityGroupClassDef) {
                return new PredefinedClassInheritance.EntityGroupClassInheritance((AdsEntityGroupClassDef) context, superClass, superInterfaces);
            } else if (context instanceof AdsReportClassDef) {
                return new PredefinedClassInheritance.ReportClassInheritance((AdsReportClassDef) context, superClass, superInterfaces);
            } else if (context instanceof AdsCursorClassDef) {
                return new PredefinedClassInheritance.CursorClassInheritance((AdsCursorClassDef) context, superClass, superInterfaces);
            } else if (context instanceof PropertyPresentationEmbeddedClass) {
                return new PredefinedClassInheritance.PresentationPropertyClassInheritance(context);
            } else if (context instanceof EnumFieldEmbeddedClass) {
                return new PredefinedClassInheritance.EnumClassFieldInheritance(context);
//            } else if (context instanceof AdsEnumClassDef) {
//                return new PredefinedClassInheritance.EnumClassInheritance(context);
            } else {
                return new Inheritance(context, superClass, superInterfaces);
            }
        }

        public static final Inheritance newInstance(final AdsClassDef context) {
            return newInstance(context, null, null);
        }
    }
    private transient AdsTypeDeclaration superClass;
    private transient List<AdsTypeDeclaration> superInterfaces;

    protected Inheritance(final AdsClassDef context, final AdsTypeDeclaration superClass, final List<AdsTypeDeclaration> superInterfaces) {
        super();
        setContainer(context);
        if (superClass == null && context instanceof AdsEntityClassDef) {
            this.superClass = AdsTypeDeclaration.Factory.newPlatformClass(AdsEntityClassDef.PLATFORM_CLASS_NAME);
        } else if (superClass == null && context instanceof AdsReportClassDef) {
            this.superClass = AdsTypeDeclaration.Factory.newPlatformClass(AdsReportClassDef.PLATFORM_CLASS_NAME);
        } else if (superClass == null && context instanceof AdsCursorClassDef) {
            this.superClass = AdsTypeDeclaration.Factory.newPlatformClass(AdsCursorClassDef.PLATFORM_CLASS_NAME);
        } else if (superClass == null && context instanceof AdsStatementClassDef) {
            this.superClass = AdsTypeDeclaration.Factory.newPlatformClass(AdsStatementClassDef.PLATFORM_CLASS_NAME);
        } else if (superClass == null && (context instanceof AdsProcedureClassDef)) {
            this.superClass = AdsTypeDeclaration.Factory.newPlatformClass(AdsSqlClassDef.PLATFORM_CLASS_NAME);
        } else if (superClass == null && (context instanceof AdsEntityGroupClassDef)) {
            this.superClass = AdsTypeDeclaration.Factory.newPlatformClass(AdsEntityGroupClassDef.PLATFORM_CLASS_NAME);
        } else {
            this.superClass = superClass;
        }
        synchronized (this) {
            if (superInterfaces != null) {
                this.superInterfaces = new ArrayList<>(superInterfaces.size());
                for (AdsTypeDeclaration decl : superInterfaces) {
                    this.superInterfaces.add(decl);
                }
            } else {
                this.superInterfaces = null;
            }
        }
        if (context instanceof AdsEntityGroupClassDef) {
            AdsEntityGroupClassDef group = (AdsEntityGroupClassDef) context;
            AdsTypeDeclaration.TypeArguments args = AdsTypeDeclaration.TypeArguments.Factory.newInstance(null);
            args.add(AdsTypeDeclaration.TypeArgument.Factory.newInstance(AdsTypeDeclaration.Factory.newEntityGroupArgument(group)));
            this.superClass = this.superClass.toGenericType(args);
        }
    }

    void appendTo(final ClassDefinition xDef) {
        synchronized (this) {
            if (superClass != null && superClass != AdsTypeDeclaration.Factory.undefinedType()) {
                superClass.appendTo(xDef.addNewExtends());
            }

            if (superInterfaces != null && !superInterfaces.isEmpty()) {
                final ClassDefinition.Implements xImpl = xDef.addNewImplements();
                for (AdsTypeDeclaration type : superInterfaces) {
                    type.appendTo(xImpl.addNewInterface());
                }
            }
        }
    }

    /**
     * Returns reference to superclass
     */
    private class SuperClassRefLink extends RadixObjectLink<AdsTypeDeclaration> {

        @Override
        protected AdsTypeDeclaration search() {
            final AdsClassDef overriden = getOwnerClass().getHierarchy().findOverwritten().get();
            if (overriden != null) {
                return overriden.getInheritance().getSuperClassRef();
            } else {
                if (AdsTransparence.isTransparent(getOwnerClass())) {
                    final AdsTransparence transparence = getOwnerClass().getTransparence();
                    if (transparence.isExtendable()) {
                        return AdsTypeDeclaration.Factory.newPlatformClass(transparence.getPublishedName());
                    } else {
                        AdsModule module = getOwnerClass().getModule();
                        if (module == null) {
                            return null;
                        }
                        AdsSegment segment = (AdsSegment) module.getSegment();
                        if (segment == null) {
                            return null;
                        }
                        return segment.getBuildPath().getPlatformClassBaseClass(transparence.getPublishedName(), getOwnerClass().getUsageEnvironment());
                    }
                } else {
                    return superClass == null ? getDefaultSuperClass() : superClass;
                }
            }
        }
    }
    private final SuperClassRefLink superRefLink = new SuperClassRefLink();
    //holder for supertype reference (because link superRefLink is cleared by gc)
    private AdsTypeDeclaration superReference;

    public synchronized AdsTypeDeclaration getSuperClassRef() {
        superReference = superRefLink.find();
        return superReference;
    }

    /**
     * Sets current class's superclass to given superclass reference
     */
    public synchronized boolean setSuperClassRef(final AdsTypeDeclaration superClass) {
        if (!getOwnerClass().getHierarchy().findOverwritten().isEmpty()) {
            throw new RadixObjectError("Superclass cannot be set to class overriding another class.", this);
        }
        if (superClass == null || isSuperclassAllowed(superClass)) {
            this.superClass = superClass;
            getOwnerClass().onSuperClassChanged(this.superClass);
            setEditState(EEditState.MODIFIED);
            superRefLink.reset();
            return true;
        } else {
            return false;
        }
    }

    public boolean isSubclassOf(AdsClassDef clazz) {
        AdsClassDef refContext = getOwnerClass();
        AdsTypeDeclaration decl = getSuperClassRef();
        while (decl != null) {
            AdsType type = decl.resolve(refContext).get();
            if (type instanceof AdsClassType) {
                AdsClassDef superClazz = ((AdsClassType) type).getSource();
                if (superClazz.getId() == clazz.getId()) {
                    return true;
                } else if (superClazz.getTransparence() != null) {
                    AdsTransparence t = superClazz.getTransparence();
                    if (t.isExtendable()) {
                        return false;
                    } else {
                        decl = superClazz.getInheritance().getSuperClassRef();
                    }
                } else {
                    decl = superClazz.getInheritance().getSuperClassRef();
                }
            } else {
                return false;
            }
        }
        return false;
    }

    public boolean isSuperclassAllowed(final AdsTypeDeclaration decl) {
        return decl != null && decl.isBasedOn(EValType.USER_CLASS);
    }

    /**
     * Returns list of super interfaces
     */
    public List<AdsTypeDeclaration> getInerfaceRefList(final EScope scope) {
        synchronized (this) {
            if (scope == EScope.LOCAL) {
                List<AdsTypeDeclaration> ifaces = this.superInterfaces == null ? new ArrayList<AdsTypeDeclaration>() : new ArrayList<>(this.superInterfaces);
                return ifaces;
            } else {
                final ArrayList<AdsTypeDeclaration> decls = new ArrayList<>();
                collectInterfaceRefs(getOwnerClass(), decls, false);
                return decls;
            }
        }
    }

    public List<AdsTypeDeclaration> getOwnAndPlatformInerfaceRefList(final EScope scope) {
        synchronized (this) {
            if (scope == EScope.LOCAL) {
                List<AdsTypeDeclaration> ifaces = this.superInterfaces == null ? new ArrayList<AdsTypeDeclaration>() : new ArrayList<>(this.superInterfaces);
                collectPlatformInterfaces(getOwnerClass(), ifaces);
                return ifaces;
            } else {
                final ArrayList<AdsTypeDeclaration> decls = new ArrayList<>();
                collectInterfaceRefs(getOwnerClass(), decls, true);
                return decls;
            }
        }
    }

    private void collectPlatformInterfaces(AdsDefinition context, List<AdsTypeDeclaration> list) {

        if (AdsTransparence.isTransparent(getOwnerClass())) {

            AdsModule module = getOwnerClass().getModule();
            if (module == null) {
                return;
            }
            AdsSegment segment = (AdsSegment) module.getSegment();
            if (segment == null) {
                return;
            }
            final AdsTransparence transparence = getOwnerClass().getTransparence();
            AdsTypeDeclaration[] supers = segment.getBuildPath().getPlatformClassBaseInterfaces(transparence.getPublishedName(), getOwnerClass().getUsageEnvironment());
            if (supers != null) {
                for (int i = 0; i < supers.length; i++) {
                    AdsTypeDeclaration decl = supers[i];
                    boolean found = false;
                    for (AdsTypeDeclaration ex : list) {
                        if (decl.equalsTo(context, ex)) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        list.add(decl);

                    }
                }
            }
        } else {
            //ignore
        }
    }

    private void collectInterfaceRefs(final AdsDefinition context, final List<AdsTypeDeclaration> list, final boolean withPlatform) {
        synchronized (this) {
            if (this.superInterfaces != null) {
                for (AdsTypeDeclaration decl : superInterfaces) {
                    boolean found = false;

                    for (AdsTypeDeclaration ex : list) {
                        if (decl.equalsTo(context, ex)) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        list.add(decl);
                    }
                }
            }
            if (withPlatform) {
                collectPlatformInterfaces(context, list);
            }
            getOwnerClass().getHierarchy().findOverwritten().iterate(new SearchResult.Acceptor<AdsClassDef>() {
                @Override
                public void accept(AdsClassDef object) {
                    object.getInheritance().collectInterfaceRefs(context, list, withPlatform);
                }
            });
        }
    }

    /**
     * Adds new superinterface to class interface list
     */
    public boolean addSuperInterfaceRef(final AdsTypeDeclaration decl) {
        synchronized (this) {
            if (isSuperInterfaceAllowed(decl)) {
                if (this.superInterfaces == null) {
                    this.superInterfaces = new ArrayList<>(3);
                }
                superInterfaces.add(decl);
                this.setEditState(EEditState.MODIFIED);
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Removes interface reference from interface list
     */
    public void removeSuperInterfaceRef(final AdsTypeDeclaration decl) {
        synchronized (this) {
            if (superInterfaces != null) {

                for (int i = 0; i < superInterfaces.size(); i++) {
                    final AdsTypeDeclaration d = superInterfaces.get(i);
                    if (d.equals(decl)) {
                        superInterfaces.remove(i);
                        this.setEditState(EEditState.MODIFIED);
                        return;
                    }
                }
            }
        }
    }

    public boolean isSuperInterfaceAllowed(final AdsTypeDeclaration decl) {
        if (getOwnerClass() == null) {
            return true;
        }
        if (decl != null && decl.isBasedOn(EValType.USER_CLASS)) {
            final AdsType type = decl.resolve(getOwnerClass()).get();
            if (type instanceof AdsClassType.InterfaceType) {
                return ((AdsClassType.InterfaceType) type).getSource() != null;
            }
        }
        return false;
    }

    public AdsClassDef getOwnerClass() {
        for (RadixObject owner = getContainer(); owner != null; owner = owner.getContainer()) {
            if (owner instanceof AdsClassDef) {
                return (AdsClassDef) owner;
            }
        }
        return null;
    }

    public SearchResult<AdsClassDef> findSuperClass() {
        final AdsTypeDeclaration superClassRef = getSuperClassRef();

        if (superClassRef == null) {
            return SearchResult.empty();
        }
        final List<AdsType> types = superClassRef.resolveAll(getOwnerClass());
        if (types.size() > 0) {
            List<AdsClassDef> classes = new ArrayList<>();
            for (AdsType type : types) {
                if (type instanceof AdsClassType) {
                    classes.add(((AdsClassType) type).getSource());
                }
            }
            if (classes.isEmpty()) {
                return SearchResult.empty();
            } else {
                return SearchResult.list(classes);
            }
        } else {
            return SearchResult.empty();
        }
    }

    public boolean setSuperClass(final AdsClassDef superClass) {
        if (superClass == null) {
            return setSuperClassRef(null);
        } else {
            return setSuperClassRef(AdsTypeDeclaration.Factory.newInstance(superClass));
        }
    }

    protected String getDefaultSuperClassJavaClassName() {
        return null;
    }

    protected AdsTypeDeclaration getDefaultSuperClass() {
        final String name = getDefaultSuperClassJavaClassName();
        if (name == null) {
            return null;
        } else {
            return AdsTypeDeclaration.Factory.newPlatformClass(name);
        }
    }

    private class DefaultSuperInterfaceLookupContext extends SuperClassLookupContext {

//        private final transient ERuntimeEnvironmentType lookupEnv;
        private final transient List<AdsTypeDeclaration> ifaces;
        private final transient AdsClassDef clazz;

        DefaultSuperInterfaceLookupContext() {
//            lookupEnv = getOwnerClass().getUsageEnvironment();
            ifaces = getInerfaceRefList(EScope.LOCAL);
            clazz = getOwnerClass();
        }

        private final boolean isCyclic(final AdsClassDef candidate) {
            AdsClassDef clazzCandidate = candidate;
            while (clazzCandidate != null) {
                if (clazzCandidate == clazz) {
                    return true;
                }
                final AdsTypeDeclaration decl = clazzCandidate.getInheritance().getSuperClassRef();
                if (decl == null) {
                    return false;
                }
                final AdsType type = decl.resolve(clazz).get();
                if (type instanceof AdsClassType) {
                    clazzCandidate = ((AdsClassType) type).getSource();
                } else {
                    return false;
                }
            }
            return false;
        }

        @Override
        public VisitorProvider getTypeSourceProvider(final EValType toRefine) {
            return new VisitorProvider() {
                @Override
                public boolean isTarget(final RadixObject object) {
                    if (object instanceof AdsInterfaceClassDef) {
                        final AdsClassDef candidate = (AdsClassDef) object;
                        for (AdsTypeDeclaration decl : ifaces) {
                            final AdsType type = decl.resolve(clazz).get();
                            if (type instanceof AdsClassType && ((AdsClassType) type).getSource() == candidate) {
                                return false;
                            }
                        }

                        return !isCyclic(candidate) && environmentCompatibility(getOwnerClass(), candidate);
                    } else {
                        return false;
                    }
                }

                boolean environmentCompatibility(AdsClassDef base, AdsClassDef child) {
                    return ERuntimeEnvironmentType.compatibility(base.getUsageEnvironment(), child.getUsageEnvironment())
                            || base.getUsageEnvironment() == ERuntimeEnvironmentType.COMMON_CLIENT && base.isDual() && child.getUsageEnvironment().isClientEnv();
                }
            };
        }
    }

    /**
     * Получить список доступных интерфейсов, исключая текущий и его предков
     */
    public SuperClassLookupContext createSuperInterfaceLookupContext() {
        return new DefaultSuperInterfaceLookupContext();
    }

    protected class DefaultSuperclassLookupContext extends SuperClassLookupContext {

        private final transient EClassType classType;

        DefaultSuperclassLookupContext(final EClassType classType) {
            this.classType = classType;
        }

        private final boolean isCyclic(final AdsClassDef candidate) {
            final AdsClassDef currentClazz = getOwnerInheritance().getOwnerClass();
            AdsClassDef clazz = candidate;
            while (clazz != null) {
                if (clazz == currentClazz) {
                    return true;
                }
                final AdsTypeDeclaration decl = clazz.getInheritance().getSuperClassRef();
                if (decl == null) {
                    return false;
                }
                final AdsType type = decl.resolve(currentClazz).get();
                if (type instanceof AdsClassType) {
                    clazz = ((AdsClassType) type).getSource();
                } else {
                    return false;
                }
            }
            return false;
        }

        @Override
        public VisitorProvider getTypeSourceProvider(final EValType toRefine) {
            return new VisitorProvider() {
                @Override
                public boolean isTarget(final RadixObject object) {
                    if (classType == EClassType.ENTITY) {
                        return false;
                    }
                    if (classType == EClassType.ENTITY_GROUP) {
                        return false;
                    }
                    if (classType == EClassType.PRESENTATION_ENTITY_ADAPTER) {
                        return false;
                    }

                    if (object instanceof AdsClassDef) {
                        final AdsClassDef candidate = (AdsClassDef) object;
                        if (classType == EClassType.APPLICATION) {
                            if (candidate instanceof AdsEntityObjectClassDef) {
                                final AdsApplicationClassDef thisClass = (AdsApplicationClassDef) getOwnerClass();
                                final AdsEntityObjectClassDef ec = (AdsEntityObjectClassDef) candidate;
                                return ec.getEntityId() == thisClass.getEntityId() && !DefaultSuperclassLookupContext.this.isCyclic(ec);
                            } else {
                                return false;
                            }
                        } else {
                            return candidate.getClassDefType() == classType && !DefaultSuperclassLookupContext.this.isCyclic(candidate);
                        }
                    } else {
                        return false;
                    }
                }
            };
        }
    }

    public SuperClassLookupContext createSuperClassLookupContext() {
        return new DefaultSuperclassLookupContext(getOwnerClass().getClassDefType());
    }

    public List<AdsClassDef> findDirectSubclasses() {
        final List<AdsClassDef> list = new ArrayList<>();
        final AdsModule module = getOwnerClass().getModule();
        if (module != null) {
            final Segment segment = module.getSegment();
            if (segment != null) {
                final Layer layer = segment.getLayer();
                if (layer != null) {

                    final Branch branch = layer.getBranch();
                    if (branch != null) {
                        final AdsClassDef thisClass = getOwnerClass();
                        final HashMap<Id, AdsClassDef> classes = new HashMap<>();
                        branch.visit(new IVisitor() {
                            @Override
                            public void accept(final RadixObject object) {
                                if (object instanceof AdsClassDef) {
                                    final AdsClassDef clazz = (AdsClassDef) object;
                                    if (classes.get(clazz.getId()) == null) {
                                        list.add(clazz);
                                        classes.put(clazz.getId(), clazz);
                                    }
                                }
                            }
                        }, new VisitorProvider() {
                            @Override
                            public boolean isTarget(final RadixObject object) {
                                if (object instanceof AdsClassDef) {
                                    final AdsClassDef clazz = (AdsClassDef) object;
                                    final AdsClassDef superClass = clazz.getInheritance().findSuperClass().get();
                                    if (superClass != null && superClass.getId() == thisClass.getId()) {
                                        return true;
                                    }

                                }
                                return false;
                            }
                        });
                    }
                }
            }
        }
        return list;
    }

    public static class ClassHierarchySupport {

        private Map<Id, ClassInfo> knownClasses = null;
        private final transient IFilter<AdsClassDef> filter;
        private final transient EnumSet<EClassType> classTypes;

        public ClassHierarchySupport() {
            this.filter = null;
            this.classTypes = null;
        }

        public ClassHierarchySupport(final IFilter<AdsClassDef> filter) {
            this.filter = filter;
            this.classTypes = null;
        }

        public ClassHierarchySupport(final EnumSet<EClassType> classTypes, final IFilter<AdsClassDef> filter) {
            this.filter = filter;
            this.classTypes = classTypes;
        }

        private class ClassInfo {

            private ClassInfo(AdsClassDef clazz) {
                this.thisClass = clazz;
                this.superClass = thisClass.getInheritance().findSuperClass().get();
            }
            private final AdsClassDef superClass;
            private final AdsClassDef thisClass;
            private Map<Id, AdsClassDef> directSubclasses;
            private Map<Id, AdsClassDef> directImplementations;

            private synchronized Collection<AdsClassDef> findDirectSubclasses() {
                if (directSubclasses == null) {
                    return Collections.emptyList();
                } else {
                    return Collections.unmodifiableCollection(directSubclasses.values());
                }
            }

            private synchronized Collection<AdsClassDef> findDirectImplementations() {
                if (directImplementations == null) {
                    return Collections.emptyList();
                } else {
                    return Collections.unmodifiableCollection(directImplementations.values());
                }
            }
        }

        private void buildTree(final AdsClassDef entryPoint) {
            knownClasses = new HashMap<>();
            knownClasses.put(entryPoint.getId(), new ClassInfo(entryPoint));
            final AdsModule module = entryPoint.getModule();
            if (module != null) {
                final Segment segment = module.getSegment();
                if (segment != null) {
                    final Layer layer = segment.getLayer();
                    if (layer != null) {

                        final Branch branch = layer.getBranch();
                        if (branch != null) {
                            VisitorProvider visitorProvider = AdsVisitorProviders.newClassVisitorProvider(classTypes, filter);

                            branch.visit(new IVisitor() {
                                @Override
                                public void accept(final RadixObject object) {
                                    if (object instanceof AdsClassDef) {
                                        final AdsClassDef clazz = (AdsClassDef) object;

                                        ClassInfo info = knownClasses.get(clazz.getId());

                                        if (info == null) {
                                            info = new ClassInfo(clazz);
                                            knownClasses.put(clazz.getId(), info);
                                        }

                                        final AdsClassDef superClass = clazz.getInheritance().findSuperClass().get();
                                        if (superClass != null) {
                                            info = knownClasses.get(superClass.getId());

                                            if (info == null) {
                                                info = new ClassInfo(superClass);
                                                knownClasses.put(superClass.getId(), info);
                                            }

                                            if (info.directSubclasses == null) {
                                                info.directSubclasses = new HashMap<>();
                                            }

                                            if (info.directSubclasses.get(clazz.getId()) == null) {
                                                info.directSubclasses.put(clazz.getId(), clazz);
                                            }
                                        }

                                        List<AdsTypeDeclaration> interfaces = clazz.getInheritance().getInerfaceRefList(EScope.LOCAL);
                                        if (interfaces != null) {
                                            for (AdsTypeDeclaration interfaceDef : interfaces) {
                                                final AdsClassType classType = (AdsClassType) interfaceDef.resolve(clazz).get();
                                                if (classType == null) {
                                                    continue;
                                                }
                                                final AdsClassDef interfaceClassDef = classType.getSource();
                                                if (interfaceClassDef == null) {
                                                    continue;
                                                }
                                                info = knownClasses.get(interfaceClassDef.getId());
                                                if (info == null) {
                                                    info = new ClassInfo(interfaceClassDef);
                                                    knownClasses.put(interfaceClassDef.getId(), info);
                                                }
                                                if (info.directImplementations == null) {
                                                    info.directImplementations = new HashMap<>();
                                                }

                                                if (info.directImplementations.get(clazz.getId()) == null) {
                                                    info.directImplementations.put(clazz.getId(), clazz);
                                                }
                                            }
                                        }
                                    }
                                }
                            }, visitorProvider);
                        }
                    }
                }
            }
        }

        public Collection<AdsClassDef> findDirectSubclasses(final AdsClassDef clazz) {
            synchronized (this) {
                if (knownClasses == null) {
                    buildTree(clazz);
                }
                return knownClasses.get(clazz.getId()).findDirectSubclasses();
            }
        }

        public Collection<AdsClassDef> findDirectImplementations(final AdsClassDef clazz) {
            synchronized (this) {
                if (knownClasses == null) {
                    buildTree(clazz);
                }
                return knownClasses.get(clazz.getId()).findDirectImplementations();
            }
        }
    }

    @Override
    public void collectDependences(final List<Definition> list) {
        super.collectDependences(list);

        final ArrayList<AdsTypeDeclaration> decls = new ArrayList<>();
        final AdsTypeDeclaration decl = getSuperClassRef();
        if (decl != null) {
            decls.add(decl);
        }
        synchronized (this) {
            if (superInterfaces != null) {
                for (AdsTypeDeclaration iface : superInterfaces) {
                    if (iface != null) {
                        decls.add(iface);
                    }
                }
            }
        }
        final AdsClassDef oc = getOwnerClass();
        for (AdsTypeDeclaration sc : decls) {
            final AdsType type = sc.resolve(oc).get();
            if (type instanceof AdsDefinitionType) {
                final Definition src = ((AdsDefinitionType) type).getSource();
                if (src != null) {
                    list.add(src);
                }
            }
        }
    }

    /**
     * Builds declaration path for class from inheritance hierarachy
     *
     * @return reverse array of declarations or null if given class is not in
     * hierarchy Example:<br>
     * <code>class A{}</code> <code>class B extends A{}</code>
     * <code>class C extends B{}</code>
     * <code>C.getInheritance().declarationPathFor(A)</code> will return
     * <code>{B,A}</code>
     */
    public AdsTypeDeclaration[] declarationPathFor(final AdsClassDef clazz) {
        final ArrayList<AdsTypeDeclaration> decls = new ArrayList<>();
        if (declarationPath(getOwnerClass(), clazz, decls) != null) {
            final AdsTypeDeclaration[] result = new AdsTypeDeclaration[decls.size()];
            for (int i = result.length - 1; i >= 0; i--) {
                result[i] = decls.get(result.length - i - 1);
            }
            return result;
        } else {
            return null;
        }
    }

    private AdsTypeDeclaration declarationPath(final AdsClassDef context, final AdsClassDef clazz, final List<AdsTypeDeclaration> hierarchy) {
        synchronized (this) {
            final AdsTypeDeclaration superClassRef = getSuperClassRef();

            int size = superClassRef == null ? 0 : 1;
            List<AdsTypeDeclaration> interfaces = this.getOwnAndPlatformInerfaceRefList(EScope.ALL);

            if (interfaces != null) {
                size += interfaces.size();
            }

            if (size != 0) {
                final AdsTypeDeclaration[] decls = new AdsTypeDeclaration[size];
                int index = 0;
                if (superClassRef != null) {
                    decls[index] = superClassRef;
                    index++;
                }

                if (interfaces != null) {
                    for (int i = 0; index < decls.length; index++, i++) {
                        decls[index] = interfaces.get(i);
                    }
                }

                final AdsPath path = new AdsPath(clazz.getIdPath());
                for (final AdsTypeDeclaration decl : decls) {
                    if (decl.getPath() != null) {
                        if (decl.getPath().equals(path)) {
                            hierarchy.add(decl);
                            return decl;
                        }
                    } else {
                        if (decl.getTypeId() == EValType.JAVA_CLASS && decl.getExtStr() != null) {
                            IPlatformClassPublisher publisher = ((AdsSegment) context.getModule().getSegment()).getBuildPath().getPlatformPublishers().findPublisherByName(decl.getExtStr());
                            if (publisher instanceof AdsClassDef) {
                                AdsPath classpath = new AdsPath((AdsClassDef) publisher);
                                if (classpath.equals(path)) {
                                    hierarchy.add(decl);
                                    return decl;
                                }
                            }
                        }
                    }
                }
                for (AdsTypeDeclaration decl : decls) {
                    final AdsType type = decl.resolve(context).get();
                    if (type instanceof AdsClassType) {
                        final AdsClassDef sc = ((AdsClassType) type).getSource();
                        if (sc != null) {
                            final AdsTypeDeclaration result = sc.getInheritance().declarationPath(context, clazz, hierarchy);
                            if (result != null) {
                                hierarchy.add(decl);
                                return result;
                            }
                        }
                    }
                }
            }

            return null;
        }
    }

    private class A<T> {

        void foo(final T obj) {
        }
    }

    private class B<Y> extends A<Y> {
    }

    private class C<Z> extends A<Z> {
    }

    private class D extends C<AdsTypeDeclaration> {

        @Override
        void foo(final AdsTypeDeclaration obj) {
            super.foo(obj);
        }
    }

    public AdsTypeDeclaration getActualArgumentType(final AdsClassDef argumentDeclatator, final String argumentName) {
        final AdsClassDef context = getOwnerClass();
        final AdsTypeDeclaration[] path = declarationPathFor(argumentDeclatator);
        if (path != null) {
            String realArgName = argumentName;
            AdsTypeDeclaration argumentType = null;
            for (int di = path.length - 1; di >= 0; di--) {
                final AdsTypeDeclaration decl = path[di];

                final AdsType type = decl.resolve(context).get();
                AdsClassDef clazz = null;
                if (type instanceof AdsClassType) {
                    clazz = ((AdsClassType) type).getSource();
                }
                if (clazz == null) {
                    break;
                }

                final TypeArguments typeArguments = clazz.getTypeArguments();
                if (typeArguments != null && !typeArguments.isEmpty()) {
                    final int argumentIndex = argumentIndex(typeArguments, realArgName);
                    if (argumentIndex >= 0) {
                        final TypeArguments genericArguments = decl.getGenericArguments();
                        if (genericArguments != null && !genericArguments.isEmpty()) {
                            final AdsTypeDeclaration.TypeArgument arg = decl.getGenericArguments().getArgumentList().get(argumentIndex);
                            argumentType = arg.getType();
                            if (argumentType == null || argumentType.isTypeArgument()) {
                                if (clazz == context) {
                                    argumentType = AdsTypeDeclaration.Factory.newTypeParam(arg.getName());
                                    break;
                                }
                                realArgName = arg.getName();
                            } else {
                                realArgName = null;
                                break;
                            }
                        } else {
                            final TypeArgument argument = typeArguments.getArgumentList().get(argumentIndex);
                            return argument.getType() != null
                                    ? argument.getType()
                                    : AdsTypeDeclaration.Factory.newPlatformClass("java.lang.Object");
                        }
                    } else {
                        break;
                    }
                } else {
                    break;
                }
            }
            if (argumentType != null) {
                return argumentType;
            } else {
                if (realArgName != null) {//look for argument in context type
                    final int argumentIndex = argumentIndex(context.getTypeArguments(), realArgName);
                    if (argumentIndex >= 0) {
                        return AdsTypeDeclaration.Factory.newTypeParam(realArgName);
                    }
                }
            }
        }

        if (argumentDeclatator.isInner() && AdsTypeDeclaration.findArgument(argumentDeclatator.getOwnerClass(), argumentName) != null) {
            return AdsTypeDeclaration.Factory.newTypeParam(argumentName);
        }

        // can be exception!!!
        Logger.getLogger(Inheritance.class.getName()).log(Level.WARNING,
                MessageFormat.format("Unable to determine the actual type of argument {0}", argumentName));

        return AdsTypeDeclaration.Factory.newPlatformClass("java.lang.Object");
    }

    private static int argumentIndex(final TypeArguments arguments, final String name) {
        if (arguments != null) {
            final List<AdsTypeDeclaration.TypeArgument> args = arguments.getArgumentList();
            for (int i = 0, len = args.size(); i < len; i++) {
                if (name.equals(args.get(i).getName())) {
                    return i;

                }
            }
        }
        return -1;
    }
}
