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
import java.util.ArrayList;
import java.util.List;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.check.RadixProblem.WarningSuppressionSupport;
import org.radixware.kernel.common.defs.ClipboardSupport;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.Definitions;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IFilter;
import org.radixware.kernel.common.defs.IOverwritable;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsClipboardSupport;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.AdsDefinitions;
import org.radixware.kernel.common.defs.ads.AdsTitledDefinition;
import org.radixware.kernel.common.defs.IOverridable;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinitionProblems;
import org.radixware.kernel.common.defs.ads.IClientDefinition;
import org.radixware.kernel.common.defs.ads.clazz.members.IModelPublishableProperty.Support;
import org.radixware.kernel.common.defs.ads.common.ICustomViewable.CustomViewSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsFilterModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.IModelPublishableProperty;
import org.radixware.kernel.common.defs.ads.common.ContextlessCommandUsage;
import org.radixware.kernel.common.defs.ads.common.ICustomViewable;
import org.radixware.kernel.common.defs.ads.radixdoc.FilterRadixdoc;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.defs.ads.src.clazz.presentation.AdsFilterWriter;
import org.radixware.kernel.common.defs.ads.type.AdsClassType;
import org.radixware.kernel.common.defs.ads.type.AdsDefinitionType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.type.IAdsTypedObject;
import org.radixware.kernel.common.defs.ads.ui.AdsAbstractUIDef;
import org.radixware.kernel.common.defs.ads.ui.AdsCustomFilterDialogDef;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtCustomFilterDialogDef;
import org.radixware.kernel.common.radixdoc.IRadixdocPage;
import org.radixware.kernel.common.radixdoc.IRadixdocProvider;
import org.radixware.kernel.common.radixdoc.RadixdocSupport;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.ENamingPolicy;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.radixdoc.DocumentOptions;
import org.radixware.kernel.common.sqml.ads.AdsSqmlEnvironment;
import org.radixware.kernel.common.utils.ValTypes;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.common.utils.events.RadixEventSource;
import org.radixware.schemas.adsdef.AbstractDialogDefinition;
import org.radixware.schemas.adsdef.ClassDefinition.Presentations.Filters.Filter;
import org.radixware.schemas.radixdoc.Page;

public class AdsFilterDef extends AdsPresentationsMember implements IJavaSource, IOverwritable, IOverridable, IModelPublishableProperty.Provider, ICustomViewable<AdsFilterDef, AdsAbstractUIDef>, ContextlessCommandUsage.IContextlessCommandsUser, EditorPages.IEditorPagesContainer, IClientDefinition, IRadixdocProvider {

    public static final class Problems extends AdsDefinitionProblems {

        public static final int DEFAULT_SORTING_IS_NOT_SPECIFIED = 5000;

        private Problems(AdsFilterDef prop, List<Integer> warnings) {
            super(prop);
            if (warnings != null) {
                int arr[] = new int[warnings.size()];
                for (int i = 0; i < arr.length; i++) {
                    arr[i] = warnings.get(i);
                }
                setSuppressedWarnings(arr);
            }
        }

        @Override
        public boolean canSuppressWarning(int code) {
            switch (code) {
                case DEFAULT_SORTING_IS_NOT_SPECIFIED:
                    return true;
                default:
                    return super.canSuppressWarning(code);
            }
        }
    }

    @Override
    public void afterOverwrite() {
        //do nothing
    }

    @Override
    public boolean allowOverwrite() {
        return true;
    }

    @Override
    public void afterOverride() {
        //do nothing
    }

    public static final class Factory {

        public static final AdsFilterDef loadFrom(Filter xFilter) {
            return new AdsFilterDef(xFilter);
        }

        public static final AdsFilterDef newInstance() {
            return new AdsFilterDef();
        }
    }
    AdsSqmlEnvironment env = AdsSqmlEnvironment.Factory.newInstance(this);

    public class EnabledSorting extends RadixObject {

        private final class Hint extends Sqml {

            public Hint() {
                super(EnabledSorting.this);
                setEnvironment(env);
            }
        }
        private final Sqml hint = new Hint();
        Id sortingId;

        private EnabledSorting(Filter.EnabledSorting xSorting) {
            this.hint.loadFrom(xSorting.getHint());
            this.sortingId = xSorting.getSortingId();
        }

        private EnabledSorting(AdsSortingDef sorting) {
            this.sortingId = sorting.getId();
        }

        private void appendTo(Filter.EnabledSorting xDef, ESaveMode saveMode) {
            this.hint.appendTo(xDef.addNewHint());
            xDef.setSortingId(sortingId);
        }

        public Id getSortingId() {
            return sortingId;
        }

        public AdsSortingDef findSorting() {
            return AdsFilterDef.this.findSortingById(sortingId);
        }

        public Sqml getHint() {
            return hint;
        }

        @Override
        public void visitChildren(IVisitor visitor, VisitorProvider provider) {
            this.hint.visit(visitor, provider);
        }

        @Override
        public void collectDependences(List<Definition> list) {
            super.collectDependences(list);
            AdsSortingDef sorting = findSorting();
            if (sorting != null) {
                list.add(sorting);
            }
        }
    }

    public class EnabledSortings extends RadixObjects<EnabledSorting> {

        private EnabledSortings(List<Filter.EnabledSorting> xDef) {
            super(AdsFilterDef.this);
            if (xDef != null) {
                for (Filter.EnabledSorting es : xDef) {
                    this.add(new EnabledSorting(es));
                }
            }
        }

        private void appendTo(Filter xDef, ESaveMode saveMode) {
            if (!isEmpty()) {
                for (EnabledSorting s : this) {
                    s.appendTo(xDef.addNewEnabledSorting(), saveMode);
                }
            }
        }

        public List<Id> getEnabledSortingIds() {
            ArrayList<Id> ids = new ArrayList<Id>(this.size());
            for (EnabledSorting e : this) {
                ids.add(e.getSortingId());
            }
            return ids;
        }
    }

    public static class Parameter extends AdsTitledDefinition implements IAdsTypedObject, IModelPublishableProperty, IJavaSource {

        @Override
        public JavaSourceSupport getJavaSourceSupport() {
            return new JavaSourceSupport(this) {
                @Override
                public CodeWriter getCodeWriter(UsagePurpose purpose) {
                    return new CodeWriter(this, purpose) {
                        @Override
                        public boolean writeCode(CodePrinter printer) {
                            printer.print(getId());
                            return true;
                        }

                        @Override
                        public void writeUsage(CodePrinter printer) {
                            writeCode(printer);
                        }
                    };
                }
            };
        }

        public static final class Factory {

            public static final Parameter newInstance() {
                return new Parameter();
            }

            public static final Parameter newInstance(AdsPropertyDef prop) {
                return new Parameter(prop);
            }

            public static final Parameter newTemporaryInstance(RadixObject context) {
                Parameter p = new Parameter();
                p.setContainer(context);
                return p;
            }
        }

        public final class Profile extends RadixObject {

            private Profile(Parameter owner) {
                setContainer(owner);
            }

            @Override
            public ENamingPolicy getNamingPolicy() {
                return ENamingPolicy.CALC;
            }

            @Override
            public String getName() {
                Parameter owner = (Parameter) getContainer();

                if (owner == null) {
                    return "UNDEFINED";
                }

                AdsTypeDeclaration decl = owner.getTypedObject().getType();
                if (decl == null) {
                    decl = AdsTypeDeclaration.UNDEFINED;
                }
                return decl.getQualifiedName(owner) + " " + owner.getName();
            }
        }
        private final Profile profile;

        public Profile getProfile() {
            return profile;
        }

        @Override
        public IAdsTypedObject getTypedObject() {
            return this;
        }

        @Override
        public boolean isTransferable(ERuntimeEnvironmentType env) {
            return true;
        }

        public class ParameterEditOptions extends EditOptions implements IModelPublishableProperty.IParentSelectorPresentationLookup {

            private Id parentSelectorPresentationId;

            private ParameterEditOptions(Parameter context, Filter.Parameters.Parameter.Editing xEditing) {
                super(context, xEditing);
                if (xEditing != null) {
                    this.parentSelectorPresentationId = xEditing.getParentSelectorPresentationId();
                } else {
                    this.parentSelectorPresentationId = null;
                }
            }

            private ParameterEditOptions(Parameter context) {
                super(context, (Filter.Parameters.Parameter.Editing) null);
                this.parentSelectorPresentationId = null;
            }

            @Override
            public Id getParentSelectorPresentationId() {
                return parentSelectorPresentationId;
            }

            @Override
            public boolean setParentSelectorPresentationId(Id id) {
                this.parentSelectorPresentationId = id;
                setEditState(EEditState.MODIFIED);
                return true;
            }

            private AdsEntityObjectClassDef findReferencedEntityClass() {
                if (getOwnerParameter().getType().getTypeId() != EValType.PARENT_REF) {
                    return null;
                } else {
                    final AdsType type = getOwnerParameter().getType().resolve(getOwnerParameter()).get();
                    if (type instanceof AdsClassType.EntityObjectType) {
                        return ((AdsClassType.EntityObjectType) type).getSource();
                    } else {
                        return null;
                    }
                }
            }

            @Override
            public AdsSelectorPresentationDef findParentSelectorPresentation() {
                final Id id = getParentSelectorPresentationId();
                if (id == null) {
                    return null;
                }
                final AdsEntityObjectClassDef eoc = findReferencedEntityClass();
                if (eoc == null) {
                    return null;
                } else {
                    return eoc.getPresentations().getSelectorPresentations().findById(getParentSelectorPresentationId(), EScope.ALL).get();
                }
            }

            public void appendTo(Filter.Parameters.Parameter.Editing xDef) {
                super.appendTo(xDef);
                if (parentSelectorPresentationId != null) {
                    xDef.setParentSelectorPresentationId(parentSelectorPresentationId);
                }
            }

            @Override
            public IAdsTypedObject getTypedObject() {
                return getOwnerParameter();
            }

            private Parameter getOwnerParameter() {
                return (Parameter) getOwnerDefinition();
            }
        }
        private ParameterEditOptions editOptions;
        private AdsTypeDeclaration type;
        private RadixEventSource typeChangeSupport = null;
        private ValAsStr defaultValue = null;

        public Parameter(Filter.Parameters.Parameter xDef) {
            super(xDef);
            this.editOptions = new ParameterEditOptions(Parameter.this, xDef.getEditing());
            if (xDef.getType() != null) {
                type = AdsTypeDeclaration.Factory.loadFrom(xDef.getType());
            } else {
                type = AdsTypeDeclaration.UNDEFINED;
            }
            if (xDef.getDefaultVal() != null) {
                defaultValue = ValAsStr.Factory.loadFrom(xDef.getDefaultVal());
            }
            this.profile = new Profile(this);
        }

        public Parameter() {
            super(Id.Factory.newInstance(EDefinitionIdPrefix.PARAMETER), "newParameter", null);
            this.editOptions = new ParameterEditOptions(this);
            type = AdsTypeDeclaration.UNDEFINED;
            this.profile = new Profile(this);
        }

        public Parameter(AdsPropertyDef prop) {
            super(Id.Factory.newInstance(EDefinitionIdPrefix.PARAMETER), prop.getName(), null);
            this.editOptions = new ParameterEditOptions(this);
            type = AdsTypeDeclaration.Factory.newCopy(prop.getValue().getType());
            this.profile = new Profile(this);
        }

        public ValAsStr getDefaultValue() {
            return defaultValue;
        }

        public void setDefaultValue(ValAsStr val) {
            this.defaultValue = val;
            setEditState(EEditState.MODIFIED);
        }

        protected AdsFilterDef getOwnerFilter() {
            for (RadixObject owner = getContainer(); owner != null; owner = owner.getContainer()) {
                if (owner instanceof AdsFilterDef) {
                    return (AdsFilterDef) owner;
                }
            }
            return null;
        }

        public ParameterEditOptions getEditOptions() {
            return editOptions;
        }

        @Override
        public void visitChildren(IVisitor visitor, VisitorProvider provider) {
            super.visitChildren(visitor, provider);
            this.editOptions.visit(visitor, provider);
        }

        private void appendTo(Filter.Parameters.Parameter xDef, ESaveMode saveMode) {
            super.appendTo(xDef, saveMode);
            if (defaultValue != null) {
                xDef.setDefaultVal(defaultValue.toString());
            }
            if (type != null && type != AdsTypeDeclaration.UNDEFINED) {
                type.appendTo(xDef.addNewType());
            }
            editOptions.appendTo(xDef.addNewEditing());
        }

        public RadixEventSource getTypeChangeSupport() {
            synchronized (this) {
                if (typeChangeSupport == null) {
                    typeChangeSupport = new RadixEventSource();
                }
                return typeChangeSupport;
            }
        }

        @Override
        public void collectDependences(List<Definition> list) {
            super.collectDependences(list);
            if (this.editOptions != null) {
                this.editOptions.collectDependences(list);
            }
            if (type != null) {
                AdsType resolved = type.resolve(this).get();
                if (resolved instanceof AdsDefinitionType) {
                    Definition def = ((AdsDefinitionType) resolved).getSource();
                    if (def != null) {
                        list.add(def);
                    }
                }
            }
        }

        @Override
        public boolean isTypeAllowed(EValType type) {
            if (type == null) {
                return false;
            }
            return !type.isArrayType() && (ValTypes.INNATE_PROPERTY_TYPES.contains(type) || type == EValType.PARENT_REF);
        }

        @Override
        public boolean isTypeRefineAllowed(EValType toRefine) {
            return IAdsTypedObject.TypeProviderFactory.defaultTypeRefineAllow(toRefine, getUsageEnvironment());
        }

        @Override
        public VisitorProvider getTypeSourceProvider(EValType toRefine) {
            return IAdsTypedObject.TypeProviderFactory.getDefaultTypeSourceProvider(toRefine, getUsageEnvironment());
        }

        @Override
        public AdsTypeDeclaration getType() {
            return type == null ? AdsTypeDeclaration.UNDEFINED : type;
        }

        @SuppressWarnings("unchecked")
        public void setType(AdsTypeDeclaration type) {
            synchronized (this) {
                this.type = type;
                if (typeChangeSupport != null) {
                    typeChangeSupport.fireEvent(new RadixEvent());
                }
                setEditState(EEditState.MODIFIED);
            }
        }

        @Override
        public ClipboardSupport<Parameter> getClipboardSupport() {
            return new AdsClipboardSupport<Parameter>(this) {
                @Override
                protected XmlObject copyToXml() {
                    Filter.Parameters.Parameter xDef = Filter.Parameters.Parameter.Factory.newInstance();
                    Parameter.this.appendTo(xDef, ESaveMode.NORMAL);
                    return xDef;
                }

                @Override
                protected Parameter loadFrom(XmlObject xmlObject) {
                    if (xmlObject instanceof Filter.Parameters.Parameter) {
                        return new Parameter((Filter.Parameters.Parameter) xmlObject);
                    } else {
                        return super.loadFrom(xmlObject);
                    }
                }

                @Override
                protected Method getDecoderMethod() {
                    try {
                        return AdsFilterDef.Factory.class.getDeclaredMethod("loadFrom", Filter.class);
                    } catch (NoSuchMethodException | SecurityException ex) {
                        return null;
                    }
                }
            };
        }

        @Override
        public EDefType getDefinitionType() {
            return EDefType.FILTER_PARAM;
        }

        @Override
        public RadixIcon getIcon() {
            return AdsDefinitionIcon.SQL_CLASS_CUSTOM_PARAMETER;
        }

        @Override
        public String getTypeTitle() {
            return "Filter Parameter";
        }

        @Override
        public String getTypesTitle() {
            return "Filter Parameters";
        }

        @Override
        public boolean isSuitableContainer(AdsDefinitions collection) {
            return collection instanceof Parameters;
        }
    }

    private class Parameters extends AdsDefinitions<Parameter> {

        private Parameters() {
            super(AdsFilterDef.this);
        }

        private void loadFrom(Filter.Parameters xDef) {
            if (xDef != null) {
                for (Filter.Parameters.Parameter xP : xDef.getParameterList()) {
                    add(new Parameter(xP));
                }
            }
        }

        private void appendTo(Filter xDef, ESaveMode saveMode) {
            if (!isEmpty()) {
                Filter.Parameters xSet = xDef.addNewParameters();
                for (Parameter p : this) {
                    p.appendTo(xSet.addNewParameter(), saveMode);
                }
            }
        }
    }

    private final class FilterSqml extends Sqml {

        public FilterSqml() {
            super(AdsFilterDef.this);
            setEnvironment(env);
        }
    }
    private final Sqml condition = new FilterSqml();
    private final Sqml hint = new FilterSqml();
    private boolean anyBaseSortingEnabled;
    private boolean customSortingEnabled;
    private Id defaultSortingId;
    private final EnabledSortings enabledSortings;
    private final Parameters parameters = new Parameters();
    private final EditorPages editorPages;
    private final AdsFilterModelClassDef filterModel;
    private ContextlessCommandUsage contextlessCommands;
    private final transient ICustomViewable.CustomViewSupport<AdsFilterDef, AdsAbstractUIDef> customViewSuppoort = new CustomViewSupport<AdsFilterDef, AdsAbstractUIDef>(this) {
        @Override
        protected AdsAbstractUIDef createOrLoadCustomView(AdsFilterDef context, ERuntimeEnvironmentType env, AbstractDialogDefinition xDef) {
            if (env == ERuntimeEnvironmentType.EXPLORER) {
                if (xDef == null) {
                    return AdsCustomFilterDialogDef.Factory.newInstance(context);
                } else {
                    return AdsCustomFilterDialogDef.Factory.loadFrom(context, xDef);
                }
            } else if (env == ERuntimeEnvironmentType.WEB) {
                if (xDef == null) {
                    return AdsRwtCustomFilterDialogDef.Factory.newInstance(context);
                } else {
                    return AdsRwtCustomFilterDialogDef.Factory.loadFrom(context, xDef);
                }
            } else {
                throw new UnsupportedOperationException("Not supported yet");
            }
        }
    };
    private Problems warningsSupport = null;
    private ERuntimeEnvironmentType clientEnvironment;

    protected AdsFilterDef(Filter xFilter) {
        super(xFilter);
        this.condition.loadFrom(xFilter.getCondition());
        this.anyBaseSortingEnabled = xFilter.getAnyBaseSortingEnabled();
        this.customSortingEnabled = xFilter.getCustomSortingEnabled();
        this.defaultSortingId = xFilter.getDefaultSortingId();
        this.hint.loadFrom(xFilter.getHint());
        this.enabledSortings = new EnabledSortings(xFilter.getEnabledSortingList());
        this.parameters.loadFrom(xFilter.getParameters());
        this.editorPages = EditorPages.Factory.loadFrom(this, xFilter.getEditorPages());
        this.filterModel = AdsFilterModelClassDef.Factory.loadFrom(this, xFilter.getModel());

        if (xFilter.getView() != null) {
            customViewSuppoort.loadCustomView(ERuntimeEnvironmentType.EXPLORER, xFilter.getView());
        }
        if (xFilter.getWebView() != null) {
            customViewSuppoort.loadCustomView(ERuntimeEnvironmentType.WEB, xFilter.getWebView());
        }
        this.contextlessCommands = ContextlessCommandUsage.Factory.loadFrom(this, xFilter.getUsedContextlessCommands());
        if (xFilter.isSetSuppressedWarnings()) {
            @SuppressWarnings("unchecked")
            List<Integer> list = xFilter.getSuppressedWarnings();
            if (!list.isEmpty()) {
                this.warningsSupport = new Problems(this, list);
            }
        }
        if (xFilter.isSetClientEnvironment()) {
            this.clientEnvironment = xFilter.getClientEnvironment();
        }
    }

    protected AdsFilterDef() {
        super(Id.Factory.newInstance(EDefinitionIdPrefix.FILTER), "NewFilter", null);
        this.anyBaseSortingEnabled = true;
        this.customSortingEnabled = false;
        this.defaultSortingId = null;
        this.enabledSortings = new EnabledSortings(null);
        this.editorPages = EditorPages.Factory.newInstance(this);
        this.filterModel = AdsFilterModelClassDef.Factory.newInstance(this);
        this.contextlessCommands = ContextlessCommandUsage.Factory.newInstance(this);
    }

    @Override
    public ERuntimeEnvironmentType getClientEnvironment() {
        AdsEntityObjectClassDef clazz = getOwnerClass();
        if (clazz != null) {
            ERuntimeEnvironmentType env = clazz.getClientEnvironment();
            if (env != ERuntimeEnvironmentType.COMMON_CLIENT) {
                return env;
            }
        }
        return clientEnvironment == null ? ERuntimeEnvironmentType.COMMON_CLIENT : clientEnvironment;
    }

    public boolean canChangeClientEnvironment() {
        AdsEntityObjectClassDef clazz = getOwnerClass();
        if (clazz != null) {
            ERuntimeEnvironmentType env = clazz.getClientEnvironment();
            if (env != ERuntimeEnvironmentType.COMMON_CLIENT) {
                return false;
            }
        }
        return true;
    }

    public void setClientEnvironment(ERuntimeEnvironmentType env) {
        if (env == ERuntimeEnvironmentType.COMMON_CLIENT && this.clientEnvironment == null) {
            return;
        }
        if (env != this.clientEnvironment) {
            this.clientEnvironment = env;
            setEditState(EEditState.MODIFIED);
        }
    }

    public void appendTo(Filter xDef, ESaveMode saveMode) {
        super.appendTo(xDef, saveMode);
        if (saveMode == ESaveMode.NORMAL) {
            this.condition.appendTo(xDef.addNewCondition());
            this.hint.appendTo(xDef.addNewHint());
            xDef.setCustomSortingEnabled(customSortingEnabled);
            xDef.setAnyBaseSortingEnabled(anyBaseSortingEnabled);
            if (defaultSortingId != null) {
                xDef.setDefaultSortingId(defaultSortingId);
            }
            this.enabledSortings.appendTo(xDef, saveMode);
        }
        if (saveMode == ESaveMode.NORMAL || (saveMode == ESaveMode.API && !isFinal())) {
            this.parameters.appendTo(xDef, saveMode);
            this.filterModel.appendTo(xDef.addNewModel(), saveMode);

            this.editorPages.appendTo(xDef.addNewEditorPages(), saveMode);
            if (!contextlessCommands.isEmpty()) {
                xDef.setUsedContextlessCommands(contextlessCommands.getCommandIds());
            }
            if (this.customViewSuppoort.isUseCustomView(ERuntimeEnvironmentType.EXPLORER)) {
                this.customViewSuppoort.getCustomView(ERuntimeEnvironmentType.EXPLORER).appendTo(xDef.addNewView(), saveMode);
            }
            if (this.customViewSuppoort.isUseCustomView(ERuntimeEnvironmentType.WEB)) {
                this.customViewSuppoort.getCustomView(ERuntimeEnvironmentType.WEB).appendTo(xDef.addNewWebView(), saveMode);
            }
        }
        if (this.clientEnvironment != null && this.clientEnvironment != ERuntimeEnvironmentType.COMMON_CLIENT) {
            xDef.setClientEnvironment(this.clientEnvironment);
        }
        if (saveMode == ESaveMode.NORMAL) {
            if (warningsSupport != null && !warningsSupport.isEmpty()) {
                int[] warnings = warningsSupport.getSuppressedWarnings();
                List<Integer> list = new ArrayList<Integer>(warnings.length);
                for (int w : warnings) {
                    list.add(Integer.valueOf(w));
                }
                xDef.setSuppressedWarnings(list);
            }
        }
    }

    @Override
    public WarningSuppressionSupport getWarningSuppressionSupport(boolean createIfAbsent) {
        synchronized (this) {
            if (warningsSupport == null && createIfAbsent) {
                warningsSupport = new Problems(this, null);
            }
            return warningsSupport;
        }
    }

    public boolean isAnyBaseSortingEnabled() {
        return anyBaseSortingEnabled;
    }

    public void setAnyBaseSortingEnabled(boolean anyBaseSortingEnabled) {
        this.anyBaseSortingEnabled = anyBaseSortingEnabled;
        setEditState(EEditState.MODIFIED);
    }

    public Sqml getCondition() {
        return condition;
    }

    public boolean isCustomSortingEnabled() {
        return customSortingEnabled;
    }

    public void setCustomSortingEnabled(boolean customSortingEnabled) {
        this.customSortingEnabled = customSortingEnabled;
        setEditState(EEditState.MODIFIED);
    }

    public Id getDefaultSortingId() {
        return defaultSortingId;
    }

    public AdsFilterModelClassDef getModel() {
        return filterModel;
    }

    public AdsSortingDef findDefaultSorting() {
        return findSortingById(defaultSortingId);
    }

    private AdsSortingDef findSortingById(Id id) {
        if (id == null) {
            return null;
        } else {
            return getOwnerClass().getPresentations().getSortings().findById(id, EScope.ALL).get();
        }
    }

    public void setDefaultSortingId(Id defaultSortingId) {
        this.defaultSortingId = defaultSortingId;
        setEditState(EEditState.MODIFIED);
    }

    public Sqml getHint() {
        return hint;
    }

    @Override
    public AdsEntityObjectClassDef getOwnerClass() {
        return (AdsEntityObjectClassDef) super.getOwnerClass();
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.FILTER;
    }

    public EnabledSortings getEnabledSortings() {
        return enabledSortings;
    }

    public EnabledSorting enableSorting(AdsSortingDef sorting) {
        EnabledSorting es = new EnabledSorting(sorting);
        enabledSortings.add(es);
        return es;
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        this.condition.visit(visitor, provider);
        this.hint.visit(visitor, provider);
        this.enabledSortings.visit(visitor, provider);
        this.parameters.visit(visitor, provider);
        this.editorPages.visit(visitor, provider);
        this.filterModel.visit(visitor, provider);
        if (customViewSuppoort.isUseCustomView(ERuntimeEnvironmentType.EXPLORER)) {
            customViewSuppoort.getCustomView(ERuntimeEnvironmentType.EXPLORER).visit(visitor, provider);
        }
        if (customViewSuppoort.isUseCustomView(ERuntimeEnvironmentType.WEB)) {
            customViewSuppoort.getCustomView(ERuntimeEnvironmentType.WEB).visit(visitor, provider);
        }
        this.contextlessCommands.visit(visitor, provider);
    }

    public Definitions<Parameter> getParameters() {
        return parameters;
    }

    @Override
    public JavaSourceSupport getJavaSourceSupport() {
        return new JavaSourceSupport(this) {
            @Override
            public CodeWriter getCodeWriter(UsagePurpose purpose) {
                return new AdsFilterWriter(this, AdsFilterDef.this, purpose);
            }
        };
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);
        AdsSortingDef defaultSorting = findDefaultSorting();
        if (defaultSorting != null) {
            list.add(defaultSorting);
        }
    }

    @Override
    public ClipboardSupport<AdsFilterDef> getClipboardSupport() {
        return new FilterClipboardSupport(this) {
            @Override
            protected XmlObject copyToXml() {
                Filter xDef = Filter.Factory.newInstance();
                appendTo(xDef, ESaveMode.NORMAL);
                return xDef;
            }

            @Override
            protected AdsFilterDef loadFrom(XmlObject xmlObject) {
                if (xmlObject instanceof Filter) {
                    return Factory.loadFrom((Filter) xmlObject);
                } else {
                    return super.loadFrom(xmlObject);

                }
            }
        };
    }

    @Override
    public boolean isSuitableContainer(AdsDefinitions collection) {
        return collection instanceof ExtendablePresentations.ExtendablePresentationsLocal && collection.getContainer() instanceof Filters;
    }

    @Override
    public EDefType getDefinitionType() {
        return EDefType.FILTER;
    }

    @Override
    public EditorPages getEditorPages() {
        return editorPages;
    }

    //=======================================================================
    private static final class FilterModelPublishingPropertySupport implements IModelPublishableProperty.Support {

        private final transient AdsFilterDef filter;

        public FilterModelPublishingPropertySupport(AdsFilterDef filter) {
            this.filter = filter;
        }

        @Override
        public List<? extends IModelPublishableProperty> list(ERuntimeEnvironmentType env, EScope scope, final IFilter<AdsDefinition> external) {
            if (external == null) {
                return filter.getParameters().list();
            }
            final IFilter<Parameter> filterStub = new IFilter<Parameter>() {
                @Override
                public boolean isTarget(Parameter radixObject) {
                    return external.isTarget(radixObject);
                }
            };

            return filter.getParameters().list(filterStub);
        }

        @Override
        public IModelPublishableProperty findById(Id id, EScope scope) {
            return filter.getParameters().findById(id);
        }
    }

    @Override
    public Support getModelPublishablePropertySupport() {
        return new FilterModelPublishingPropertySupport(this);
    }

//    @Deprecated
//    public Id getCustomViewId() {
//        return Id.Factory.changePrefix(getId(), EDefinitionIdPrefix.CUSTOM_FILTER_DIALOG);
//    }
    @Override
    public CustomViewSupport<AdsFilterDef, AdsAbstractUIDef> getCustomViewSupport() {
        return customViewSuppoort;
    }

    @Override
    public ContextlessCommandUsage getUsedContextlessCommands() {
        return contextlessCommands;
    }

    @Override
    protected void insertToolTipPrefix(StringBuilder sb) {
        super.insertToolTipPrefix(sb);
        final String access = getDefaultAccess().getName().toUpperCase().charAt(0) + getDefaultAccess().getName().substring(1, getDefaultAccess().getName().length());
        sb.append("<b>").append(access).append(' ').append(getClientEnvironment().getName()).append("</b>&nbsp;");
    }

    @Override
    public RadixdocSupport<? extends RadixObject> getRadixdocSupport() {
        return new RadixdocSupport<AdsFilterDef>(this) {
            @Override
            public IRadixdocPage document(Page page, DocumentOptions options) {
                return new FilterRadixdoc(getSource(), page, options);
            }
        };
    }

    @Override
    public boolean isRadixdocProvider() {
        return true;
    }
}
