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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.check.RadixProblem.ProblemFixSupport;
import org.radixware.kernel.common.check.RadixProblem.WarningSuppressionSupport;
import org.radixware.kernel.common.defs.*;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.*;
import static org.radixware.kernel.common.defs.ads.AdsDefinition.getLocalizedDescriptionForToolTip;
import org.radixware.kernel.common.defs.ads.clazz.*;
import org.radixware.kernel.common.defs.ads.clazz.algo.AdsAlgoMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.algo.AdsAlgoStartMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.algo.AdsAlgoStrobMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodThrowsList.ThrowsListItem;
import org.radixware.kernel.common.defs.ads.common.AdsVisitorProviders;
import org.radixware.kernel.common.defs.ads.common.JavaSignatures;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.ads.profiling.AdsProfileSupport;
import org.radixware.kernel.common.defs.ads.profiling.AdsProfileSupport.IProfileable;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.clazz.AdsMethodWriter;
import org.radixware.kernel.common.defs.ads.type.AdsAccessFlags;
import org.radixware.kernel.common.defs.ads.type.AdsClassType;
import org.radixware.kernel.common.defs.ads.type.AdsTransparence;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.localization.ILocalizedDef;
import org.radixware.kernel.common.defs.localization.ILocalizedDescribable;
import org.radixware.kernel.common.enums.*;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.scml.LineMatcher.DefaultLocationDescriptor;
import org.radixware.kernel.common.scml.LineMatcher.ILocationDescriptor;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.RadixObjectsUtils;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.schemas.adsdef.AbstractMethodDefinition;
import org.radixware.schemas.adsdef.AccessRules;
import org.radixware.schemas.adsdef.MethodDefinition;
import org.radixware.schemas.adsdef.UsageDescription;
import org.radixware.schemas.xscml.TypeDeclaration;

/**
 * Base class for all supported kinds of methods Provides following basic
 * features: <ul> <li>Return type and parameters</li> <li>Throws signature</li>
 * <li>{@linkplain AdsAccessFlags Access flags}
 * </li> <li>"Override" annotation handling</li> </ul>
 *
 */
public abstract class AdsMethodDef extends AdsClassMember implements IJavaSource, IOverridable<AdsMethodDef>, IOverwritable<AdsMethodDef>, IProfileable, ITransparency, IAccessible {

    public interface MethodComponent {

        public AdsMethodDef getOwnerMethod();
    }

    @Override
    public void afterOverride() {
        getProfile().syncToOvr();
        setOverride(true);

        if (isOverwrite()) {
            if (getHierarchy().findOverwritten().isEmpty()) {
                setOverwrite(false);
            }
        }

        // RADIX-6194
        if (getOwnerClass() == null || getOwnerClass().isDual()) {
            final SearchResult<AdsMethodDef> overridden = getHierarchy().findOverridden();
            if (!overridden.isEmpty()) {
                final AdsClassDef owner = RadixObjectsUtils.findContainer(overridden.get(), AdsClassDef.class);

                if (owner != null && owner.getClassDefType() == EClassType.INTERFACE) {
                    final ERuntimeEnvironmentType environment = owner.getUsageEnvironment();

                    switch (environment) {
                        case EXPLORER:
                        case WEB:
                            this.setUsageEnvironment(environment);
                    }
                }
            }
        }
    }

    @Override
    public void afterOverwrite() {
        setOverwrite(true);
    }

    @Override
    public boolean allowOverwrite() {
        return true;
    }

    /**
     * Base class for method parameter and method return value
     */
    public static abstract class MethodValue extends Value implements MethodComponent, ILocalizedDescribable, IDescribable {

        private String description;
        private Id descriptionId;

//        protected MethodValue(String name, String description) {
//            super(name);
//            this.description = description;
//        }
//        protected MethodValue(TypeDeclaration xDef, String description) {
//            this(xDef, description, null);
//        }
        protected MethodValue(AdsTypeDeclaration type, String name, String description, Id descriptionId) {
            super(name, type);
            this.description = description;
            this.descriptionId = descriptionId;
        }

        protected MethodValue(AdsTypeDeclaration type) {
            super(type);
        }

        protected MethodValue(TypeDeclaration xDef, AdsTypeDeclaration defaultType, String description, Id descriptionId) {
            super(AdsTypeDeclaration.Factory.loadFrom(xDef, defaultType == null ? AdsTypeDeclaration.Factory.undefinedType() : defaultType));
            this.description = description;
            this.descriptionId = descriptionId;
        }

        @Override
        public final String getDescription(EIsoLanguage language) {
            return getDescriptionLocation().getLocalizedStringValue(language, getDescriptionId());
        }

        @Override
        public Id getDescriptionId() {
            return descriptionId;
        }

        @Override
        public final boolean setDescription(EIsoLanguage language, String description) {
            final Id id = getDescriptionLocation().setLocalizedStringValue(language, getDescriptionId(), description, ELocalizedStringKind.DESCRIPTION);
            setDescriptionId(id);
            return id != null;
        }

        @Override
        public void setDescriptionId(Id id) {
            if (!Objects.equals(id, descriptionId)) {
                this.descriptionId = id;
                setEditState(EEditState.MODIFIED);
            }
        }
        
        public boolean isDescriptionIdChanged(){
            return getDescriptionId() != null;
        }

        @Override
        public AdsDefinition getDescriptionLocation() {
            return RadixObjectsUtils.findContainer(this, AdsDefinition.class);
        }

        @Override
        public String getDescription() {
            return description;
        }

        protected final void removeDescriptionString() {
            final AdsMultilingualStringDef localizedString = getDescriptionLocation().findLocalizedString(getDescriptionId());
            if (localizedString != null) {
                getDescriptionLocation().findLocalizingBundle().getStrings().getLocal().remove(localizedString);
            }
        }

        @Override
        public void setDescription(String description) {
            this.description = description;
            setEditState(EEditState.MODIFIED);
        }

        @Override
        public boolean isTypeAllowed(EValType type) {
            return type != null && type.isAllowedForMethodParameter();
        }

        @Override
        public VisitorProvider getTypeSourceProvider(EValType toRefine) {
            if (toRefine == null) {
                return VisitorProviderFactory.createEmptyVisitorProvider();
            }
            if (toRefine.isEnumAssignableType()) {
                return AdsVisitorProviders.newEnumBasedTypeProvider(toRefine);
            } else if (toRefine == EValType.PARENT_REF || toRefine == EValType.ARR_REF) {
                return AdsVisitorProviders.newEntityObjectTypeProvider(null);
            } else if (toRefine == EValType.XML) {
                return AdsVisitorProviders.newXmlBasedTypesProvider(getOwnerMethod().getUsageEnvironment());
            } else if (toRefine == EValType.USER_CLASS) {
                if (getOwnerMethod() != null) {
                    return AdsVisitorProviders.newClassBasedTypesProvider(getOwnerMethod().getUsageEnvironment());
                }
                return AdsVisitorProviders.newClassBasedTypesProvider(ERuntimeEnvironmentType.COMMON);
            } else {
                return VisitorProviderFactory.createEmptyVisitorProvider();
            }
        }

        @Override
        public boolean isTypeRefineAllowed(EValType type) {
            switch (type) {
                case ARR_REF:
                case ARR_INT:
                case ARR_CHAR:
                case ARR_STR:
                case INT:
                case STR:
                case CHAR:
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public AdsMethodDef getOwnerMethod() {
            for (RadixObject owner = getContainer(); owner != null; owner = owner.getContainer()) {
                if (owner instanceof AdsMethodDef) {
                    return (AdsMethodDef) owner;
                }
            }
            return null;
        }

        @Override
        public boolean isReadOnly() {
            AdsMethodDef method = getOwnerMethod();
            if (method != null) {
                return method.isReadOnly();
            } else {
                return false;
            }
        }

        @Override
        public boolean needsDocumentation() {
            AdsMethodDef ownerMethod = getOwnerMethod();
            if (ownerMethod == null) {
                return false;
            }
            return ownerMethod.needsDocumentation();
        }
    }

    public ILocationDescriptor getLocationDescriptor(ERuntimeEnvironmentType env) {
        return new DefaultLocationDescriptor(this);
    }

    public boolean isTypeAllowed(EValType type) {
        return true;
    }

    /**
     * Meta method factory
     */
    public abstract static class Factory {

        /**
         * Loads method definition from xml Return value depends on content of
         * given xml definition and mey be on of null null null null null null
         * null null null null         {@linkplain AdsCommandHandlerMethod}, {@linkplain AdsUserMethodDef}, {@linkplain AdsTransparentMethodDef},
         * {@linkplain AdsConstructorDef},
         */
        public static AdsMethodDef loadFrom(AbstractMethodDefinition xMethod) {
            if (xMethod.getAccessRules() != null && xMethod.getAccessRules().isSetTransparence()) {
                return new AdsTransparentMethodDef(xMethod);
            } else {
                EMethodNature nature = xMethod.getNature();
                switch (nature) {
                    case USER_DEFINED:
                        return new AdsUserMethodDef(xMethod);
                    case COMMAND_HANDLER:
                        return new AdsCommandHandlerMethodDef(xMethod);
                    case PRESENTATION_SLOT:
                        return new AdsPresentationSlotMethodDef(xMethod);
                    case ALGO_METHOD:
                        return new AdsAlgoMethodDef(xMethod);
                    case ALGO_START:
                        return new AdsAlgoStartMethodDef(xMethod);
                    case ALGO_STROB:
                        return new AdsAlgoStrobMethodDef(xMethod);
                    case RPC:
                        return new AdsRPCMethodDef(xMethod);
                    case SYSTEM:
                        return AdsSystemMethodDef.Factory.loadFrom(xMethod);
//                    case ALGO_BLOCK:
//                        return new AdsUserMethodDef(xMethod);
                    default:
                        throw new UnsupportedOperationException("method upload");
                }
            }
        }

        private static AdsMethodDef loadFromCopy(MethodDefinition xMethod) {
            if (xMethod.getAccessRules() != null && xMethod.getAccessRules().isSetTransparence()) {
                return new AdsUserMethodDef(xMethod);
            } else {
                EMethodNature nature = xMethod.getNature();
                AdsMethodDef method = null;
                switch (nature) {
                    case USER_DEFINED:
                        method = new AdsUserMethodDef(xMethod);
                        break;
                    case COMMAND_HANDLER:
                        method = new AdsCommandHandlerMethodDef(xMethod);
                        break;
                    case PRESENTATION_SLOT:
                        method = new AdsPresentationSlotMethodDef(xMethod);
                        break;
                    case ALGO_METHOD:
                        method = new AdsAlgoMethodDef(xMethod);
                        break;
                    case ALGO_START:
                        method = new AdsAlgoStartMethodDef(xMethod);
                        break;
                    case ALGO_STROB:
                        method = new AdsAlgoStrobMethodDef(xMethod);
                        break;
                    case RPC:
                        method = new AdsRPCMethodDef(xMethod);
                        break;
                    case SYSTEM:
                        return AdsSystemMethodDef.Factory.loadFrom(xMethod);
//                        throw new UnsupportedOperationException("System method copying is not allowed");
//                    case ALGO_BLOCK:
//                        return new AdsUserMethodDef(xMethod);
                    default:
                        throw new UnsupportedOperationException("method upload");
                }
                if (method.getUsageEnvironment() == ERuntimeEnvironmentType.EXPLORER || method.getUsageEnvironment() == ERuntimeEnvironmentType.WEB) {
                    method.setUsageEnvironment(null);
                }
                return method;
            }
        }
    }

    public class Profile extends RadixObject implements IAccessible {

        private final AdsMethodParameters parameters;
        private MethodReturnValue returnValue;
        private final AdsMethodThrowsList throwsList;
        protected final AdsAccessFlags accessFlags;

        protected Profile() {
            this((MethodDefinition) null);
        }

        protected Profile(AbstractMethodDefinition xMethod) {
            if (xMethod == null) {
                this.parameters = AdsMethodParameters.Factory.newInstance(AdsMethodDef.this);
                if (!AdsMethodDef.this.isConstructor) {
                    this.returnValue = new MethodReturnValue(AdsMethodDef.this);
                }
                this.accessFlags = AdsAccessFlags.Factory.newInstance(AdsMethodDef.this);
                this.throwsList = AdsMethodThrowsList.Factory.emptyList(AdsMethodDef.this);
            } else {
                this.parameters = AdsMethodParameters.Factory.loadFrom(AdsMethodDef.this, xMethod.getParameters());
                this.accessFlags = AdsAccessFlags.Factory.loadFrom(AdsMethodDef.this, xMethod.getAccessRules());
                if (xMethod.getAccessRules() != null && xMethod.getAccessRules().getIsFinal()) {
                    AdsMethodDef.this.setFinal(true);
                }
                this.throwsList = AdsMethodThrowsList.Factory.loadFrom(AdsMethodDef.this, xMethod.getThrownExceptions());
                if (!xMethod.isSetIsConstructor() || !xMethod.getIsConstructor()) {
                    this.returnValue = new MethodReturnValue(AdsMethodDef.this, xMethod.getReturnType());
                }
            }
        }

        protected Profile(Profile source) {
            this.parameters = AdsMethodParameters.Factory.newInstance(AdsMethodDef.this);
            this.returnValue = new MethodReturnValue(AdsMethodDef.this);
            this.returnValue.setType(source.getReturnValue().getType());

            for (MethodParameter p : source.parameters) {
                this.parameters.add(MethodParameter.Factory.newInstance(p.getName(), p.getDescription(), null, p.getType(), p.isVariable()));
            }
            this.accessFlags = AdsAccessFlags.Factory.newCopy(AdsMethodDef.this, source.getAccessFlags());
            this.accessFlags.setAbstract(false);
            this.throwsList = AdsMethodThrowsList.Factory.emptyList(AdsMethodDef.this);
        }

        private final class SignatureLink extends ObjectLink<char[]> {

            @Override
            protected char[] search() {
                return JavaSignatures.generateSignature(context != null ? context : getOwnerClass(), AdsMethodDef.this, true);
            }
            private AdsClassDef context;

            public char[] find(AdsClassDef context) {
                this.context = context;
                final char[] sign = find();
                return sign != null ? sign : update();
            }
        };
        private final SignatureLink signature = new SignatureLink();

        public char[] getSignature(AdsClassDef context) {
            synchronized (this) {
                return signature.find(context);
            }
        }

        public MethodParameter findParamById(Id id) {
            return parameters.findById(id);
        }
        /*
         * Return method profile as array of arguments with return type as last
         * element
         */

        public AdsTypeDeclaration[] getNormalizedProfile() {
            final AdsMethodParameters params = getParametersList();
            AdsTypeDeclaration[] decls = new AdsTypeDeclaration[params.size() + 1];
            for (int i = 0, len = params.size(); i < len; i++) {
                decls[i] = params.get(i).getType();
            }
            final MethodReturnValue result = getReturnValue();
            decls[decls.length - 1] = result == null || result.getType() == null || result.getType().getTypeId() == null ? AdsTypeDeclaration.VOID : result.getType();
            return decls;
        }

        @Override
        public void visitChildren(IVisitor visitor, VisitorProvider provider) {
            getAccessFlags().visit(visitor, provider);
            if (getReturnValue() != null) {
                getReturnValue().visit(visitor, provider);
            }
            this.getParametersList().visit(visitor, provider);
            this.getThrowsList().visit(visitor, provider);
        }

        /**
         * Returns method's access flags
         */
        @Override
        public AdsAccessFlags getAccessFlags() {
            return accessFlags;
        }

        public void appendTo(AbstractMethodDefinition xMethod) {

            AccessRules rules = xMethod.getAccessRules();
            if (rules == null) {
                rules = xMethod.addNewAccessRules();
            }
            getAccessFlags().appendTo(rules);

            if (getReturnValue() != null) {
                getReturnValue().appendTo(xMethod);
            }
            if (!getParametersList().isEmpty()) {
                getParametersList().appendTo(xMethod.addNewParameters());
            }
            if (!getThrowsList().isEmpty()) {
                getThrowsList().appendTo(xMethod.addNewThrownExceptions());
            }
        }

        @Override
        public void collectDependences(List<Definition> list) {
            if (getReturnValue() != null) {
                getReturnValue().collectDependences(list);

            }
            for (MethodParameter p : this.getParametersList()) {
                p.collectDependences(list);
            }
            for (AdsMethodThrowsList.ThrowsListItem item : throwsList) {
                item.collectDependences(list);
            }

        }

        /**
         * Returns method's parameters list
         */
        public AdsMethodParameters getParametersList() {
            return this.parameters;
        }

        /**
         * Returns method's return type
         */
        public MethodReturnValue getReturnValue() {
            return this.returnValue;
        }

        public AdsMethodThrowsList getThrowsList() {
            return throwsList;
        }

        @Override
        public ENamingPolicy getNamingPolicy() {
            return ENamingPolicy.CALC;
        }

        public String getNameForSelector(String selector) {
            StringBuilder profile = new StringBuilder();

            if (getReturnValue() != null) {
                profile.append(getReturnValue().getType().getQualifiedName(AdsMethodDef.this));
                profile.append(' ');
            }

            profile.append(selector);

            profile.append('(');

            getParametersList().printProfileString(profile, true);

            profile.append(')');

            getThrowsList().printSignatureString(profile);

            return profile.toString();
        }

        @Override
        public String getName() {
            return getNameForSelector(AdsMethodDef.this.getName());
        }

        @Override
        public String getQualifiedName() {
            StringBuilder profile = new StringBuilder();

            if (getReturnValue() != null) {
                profile.append(getReturnValue().getType().getQualifiedName(AdsMethodDef.this));
                profile.append(' ');
            }

            profile.append(AdsMethodDef.this.getQualifiedName());

            profile.append('(');

            getParametersList().printProfileString(profile, true);

            profile.append(')');

            getThrowsList().printSignatureString(profile);

            return profile.toString();
        }

        public String getProfileForQtSlotDescription() {
            StringBuilder profile = new StringBuilder();
            profile.append(AdsMethodWriter.comuteEffectiveJavaName(AdsMethodDef.this));

            profile.append('(');

            if (!getParametersList().printCommaSeparatedTypeNames(profile)) {
                return null;
            }

            profile.append(')');
            return profile.toString();
        }

        public String getProfileHtml() {
            StringBuilder b = new StringBuilder();
            preintProfileHtml(b);
            return b.toString();
        }

        private void preintProfileHtml(StringBuilder profile) {
            if (getReturnValue() != null) {
                profile.append(getReturnValue().getType().getHtmlName(AdsMethodDef.this, false));
                profile.append(' ');
            }

            if (AdsMethodDef.this instanceof AdsLibUserFuncWrapper) {
                profile.append(((AdsLibUserFuncWrapper) AdsMethodDef.this).getLibName());
                profile.append("::");
            }
            profile.append(AdsMethodDef.this.getName());

            profile.append('(');

            AdsMethodParameters.printProfileHtml(AdsMethodDef.this, false, getParametersList(), profile);

            profile.append(')');

            getThrowsList().printSignatureHtml(profile);
        }

        @Override
        public boolean isReadOnly() {
            return AdsMethodDef.this.getNature() == EMethodNature.PRESENTATION_SLOT || AdsMethodDef.this.isReadOnly();
        }

        private List<MethodParameter> findMethodParameterByType(List<MethodParameter> source, AdsTypeDeclaration type) {
            if (type == null || source == null || source.isEmpty()) {
                return null;
            }

            List<MethodParameter> result = new ArrayList<>();
            AdsClassDef ownerClass = getOwnerClass();
            for (MethodParameter parameter : source) {
                if (parameter.getType().equalsTo(ownerClass, type)) {
                    result.add(parameter);
                }
            }
            return result.isEmpty() ? null : result;
        }

        private List<MethodParameter> findMethodParameterByName(List<MethodParameter> source, String name) {
            if (name == null || name.isEmpty() || source == null || source.isEmpty()) {
                return null;
            }

            List<MethodParameter> result = new ArrayList<>();
            for (int i = 0; i < source.size(); i++) {
                MethodParameter o = source.get(i);
                if (name.equals(o.getName())) {
                    result.add(o);
                }
            }
            return result.isEmpty() ? null : result;
        }

        public boolean syncTo(AdsMethodDef source) {
            if (source == null) {
                return false;
            }
            Profile baseProfile = source.getProfile();

            AdsClassDef ownerClass = getOwnerClass();
            AdsClassDef baseClass = source.getOwnerClass();

            if (!AdsMethodDef.this.isConstructor()) {
                returnValue.setType(baseProfile.getReturnValue().getType().getActualType(baseClass, ownerClass));
            }

            ArrayList<MethodParameter> oldParams = new ArrayList<>(parameters.list());
            this.parameters.clear();
            List<MethodParameter> baseParameters = baseProfile.getParametersList().list();
            for (MethodParameter p : baseParameters) {
                String parameterName = p.getName();
                AdsTypeDeclaration type = p.getType().getActualType(baseClass, ownerClass);
                final boolean vararg = p.isVariable();
                List<MethodParameter> parametersWithEqualsType = findMethodParameterByType(oldParams, type);
                if (parametersWithEqualsType != null) {
                    for (MethodParameter parameter : parametersWithEqualsType) {
                        List<MethodParameter> parametersWithEqualsName = findMethodParameterByName(baseParameters, parameter.getName());
                        if (parametersWithEqualsName == null) {
                            parameterName = parameter.getName();
                            oldParams.remove(parameter);
                        }
                    }
                }
                this.parameters.add(MethodParameter.Factory.newInstance(parameterName, type, vararg));
            }

            for (AdsMethodThrowsList.ThrowsListItem item : throwsList) {
                AdsType exeptionType = item.getException().resolve(AdsMethodDef.this).get();
                if (exeptionType instanceof AdsClassType) {
                    AdsClassDef throwListItemClass = ((AdsClassType) exeptionType).getSource();
                    if (throwListItemClass instanceof AdsExceptionClassDef) {
                        if (!ProfileUtilities.isCorrectExeptionInThrowList((AdsExceptionClassDef) throwListItemClass, AdsMethodDef.this, baseProfile.throwsList.list())) {
                            this.throwsList.remove(item);
                        }
                    }
                }
            }

            setEditState(EEditState.MODIFIED);
            return true;
        }

        public boolean syncToOvr() {
            AdsMethodDef ovr = AdsMethodDef.this.getHierarchy().findOverwritten().get();
            if (ovr == null) {
                ovr = AdsMethodDef.this.getHierarchy().findOverridden().get();
            }

            return syncTo(ovr);
        }
    }
//    private List<String> suppressedWarnings;
    private boolean isOverride;
    private final boolean isConstructor;
    private final Profile profile;
    protected boolean reflectiveCallable = false;
    protected final AdsProfileSupport profileSupport = new AdsProfileSupport(this);
    private ERuntimeEnvironmentType usageEnvironment;

    protected AdsMethodDef(AdsMethodDef source, boolean overwrite) {
        super(source.getId(), source.getName());

        this.isConstructor = source.isConstructor;
        this.profile = new Profile(source.getProfile());
        this.isOverride = true;
        this.isOverwrite = overwrite;

//        if (source.suppressedWarnings != null) {
//            this.suppressedWarnings = new ArrayList<String>(suppressedWarnings);
//        }
    }

    public boolean isReflectiveCallable() {
        return reflectiveCallable;
    }

    public void setReflectiveCallable(boolean reflectiveCallable) {
        if (this.reflectiveCallable != reflectiveCallable) {
            this.reflectiveCallable = reflectiveCallable;
            if (reflectiveCallable) {
                setAccessMode(EAccess.PUBLIC);
                AdsClassDef clazz = getOwnerClass();
                if (clazz != null) {
                    clazz.setAccessMode(EAccess.PUBLIC);
                }
            }
            setEditState(EEditState.MODIFIED);
        }
    }

    @SuppressWarnings("unchecked")
    protected AdsMethodDef(AbstractMethodDefinition xDef) {
        super(xDef);
        this.isConstructor = xDef.isSetIsConstructor() && xDef.getIsConstructor();
        this.profile = new Profile(xDef);
        if (xDef.isSetIsReflectiveCallable()) {
            reflectiveCallable = xDef.getIsReflectiveCallable();
        } else {
            reflectiveCallable = false;
        }
        if (xDef instanceof MethodDefinition) {
            MethodDefinition xMethod = (MethodDefinition) xDef;
            this.isOverride = xMethod.isSetIsOverride() && xMethod.getIsOverride();

            if (xMethod.getProfileInfo() != null) {
                profileSupport.loadFrom(xMethod.getProfileInfo());
            }
            if (xMethod.isSetSuppressedWarnings()) {
                List<Integer> list = xMethod.getSuppressedWarnings();
                if (!list.isEmpty()) {
                    this.warningsSupport = new AdsMethodProblems(this, list);
                }
            }
        }
        if (xDef.getEnvironment() != null) {
            this.usageEnvironment = xDef.getEnvironment();
        }

    }

    protected AdsMethodDef(Id id, String name) {
        this(id, name, false);
    }

    protected AdsMethodDef(Id id, String name, boolean isConstructor) {
        super(id, name);
        this.isConstructor = isConstructor;
        this.isOverride = false;
        this.profile = new Profile();
    }

    protected AdsMethodDef(String name, boolean isConstructor) {
        this(Id.Factory.newInstance(EDefinitionIdPrefix.ADS_CLASS_METHOD), name, isConstructor);
    }

    @Override
    public AdsAccessFlags getAccessFlags() {
        return getProfile().getAccessFlags();
    }

    public Profile getProfile() {
        return profile;
    }

    public abstract EMethodNature getNature();

    protected void appendHeaderTo(AbstractMethodDefinition xMethod, ESaveMode saveMode) {
        super.appendTo(xMethod, saveMode);
        xMethod.setNature(this.getNature());
        if (isConstructor) {
            xMethod.setIsConstructor(isConstructor);
        }
        if (isReflectiveCallable()) {
            xMethod.setIsReflectiveCallable(true);
        }
        getProfile().appendTo(xMethod);
        if (usageEnvironment != null) {
            xMethod.setEnvironment(usageEnvironment);
        }
    }

    @Override
    public void appendToUsage(UsageDescription xDef) {
        super.appendToUsage(xDef);
        appendHeaderTo(xDef.addNewMethod(), ESaveMode.USAGE);
    }
    private AdsMethodProblems warningsSupport;

    @Override
    public WarningSuppressionSupport getWarningSuppressionSupport(boolean createIfAbsent) {
        synchronized (this) {
            if (warningsSupport == null && createIfAbsent) {
                warningsSupport = new AdsMethodProblems(this, null);
            }
            return warningsSupport;
        }
    }

    @Override
    public ProblemFixSupport getProblemFixSupport() {
        synchronized (this) {
            if (warningsSupport == null) {
                return new AdsMethodProblems(this, null);
            } else {
                return warningsSupport;
            }
        }
    }

    public void appendTo(MethodDefinition xMethod, ESaveMode saveMode) {
        appendHeaderTo(xMethod, saveMode);
        xMethod.setIsOverride(isOverride());
//        if (suppressedWarnings != null && !suppressedWarnings.isEmpty()) {
//            xMethod.setSuppressedWarnings(suppressedWarnings);
//        }

        if (getTransparence() != null) {
            getTransparence().appendTo(xMethod.getAccessRules().addNewTransparence());
        }
        if (isProfileable() && profileSupport.isProfiled()) {
            profileSupport.appendTo(xMethod.addNewProfileInfo());
        }
        if (saveMode == ESaveMode.NORMAL) {
            if (warningsSupport != null && !warningsSupport.isEmpty()) {
                int[] warnings = warningsSupport.getSuppressedWarnings();
                List<Integer> list = new ArrayList<>(warnings.length);
                for (int w : warnings) {
                    list.add(Integer.valueOf(w));
                }
                xMethod.setSuppressedWarnings(list);
            }
        }
    }

    public boolean isConstructor() {
        return isConstructor;
    }

    @Override
    public String getName() {
        if (isConstructor) {
            if (this.getOwnerClass() != null) {
                return getOwnerClass().getName();
            } else {
                return "<init>";
            }

        } else {
            return super.getName();
        }
    }

    @SuppressWarnings("unchecked")
    public void supportForSelfCheckOnPublishing(final Id newId) {
        final RadixObjects<? super AdsMethodDef> c = (RadixObjects<? super AdsMethodDef>) getContainer();
        delete();
        setId(newId);
        c.add(this);
    }

    public boolean isOverride() {
        return isOverride;
    }

    public void setOverride(boolean isOverride) {
        this.isOverride = isOverride;
        setEditState(EEditState.MODIFIED);
    }
    private final Hierarchy<AdsMethodDef> hierarchy = new MemberHierarchy<AdsMethodDef>(this, true) {
        @Override
        protected AdsMethodDef findMember(AdsClassDef clazz, Id id, EScope scope) {
            return clazz.getMethods().findById(id, scope).get();
        }
    };

    @Override
    @SuppressWarnings("unchecked")
    public Hierarchy<AdsMethodDef> getHierarchy() {
        return hierarchy;
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.Method.calcIcon(this);
    }

    /**
     * Returns transparence description for the method if method supports
     * transparence otherwise null
     *
     */
    @Override
    public AdsTransparence getTransparence() {
        return null;
    }

    protected class MethodJavaSourceSupport
            extends JavaSourceSupport {

        public MethodJavaSourceSupport(AdsMethodDef method) {
            super(method);
        }

        @Override
        @SuppressWarnings("unchecked")
        public CodeWriter getCodeWriter(UsagePurpose purpose) {
            return new AdsMethodWriter(this, AdsMethodDef.this, purpose);
        }
    }

    @Override
    public JavaSourceSupport getJavaSourceSupport() {
        return new MethodJavaSourceSupport(this);
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);
        // getProfile().collectDependences(list);
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        getProfile().visitChildren(visitor, provider);
    }

    @Override
    public ClipboardSupport<? extends AdsMethodDef> getClipboardSupport() {
        return new AdsClipboardSupport<AdsMethodDef>(this) {
            @Override
            protected XmlObject copyToXml() {
                MethodDefinition xDef = MethodDefinition.Factory.newInstance();
                AdsMethodDef.this.appendTo(xDef, ESaveMode.NORMAL);
                return xDef;
            }

            @Override
            protected AdsMethodDef loadFrom(XmlObject xmlObject) {
                if (xmlObject instanceof MethodDefinition) {
                    return Factory.loadFromCopy((MethodDefinition) xmlObject);
                }

                return super.loadFrom(xmlObject);
            }

            @Override
            public boolean canCopy() {
                return true;
            }

            @Override
            protected boolean isIdChangeRequired(RadixObject copyRoot) {
                if (copyRoot != AdsMethodDef.this) {
                    if (super.isIdChangeRequired(copyRoot)) {
                        AdsMethodDef ovr = getHierarchy().findOverridden().get();
                        if (ovr != null) {
                            return false;
                        } else {
                            return true;
                        }
                    } else {
                        return false;
                    }
                } else {
                    return super.isIdChangeRequired(copyRoot);
                }
            }

            @Override
            public boolean isEncodedFormatSupported() {
                return true;
            }

            @Override
            protected Method getDecoderMethod() {
                try {
                    return Factory.class.getDeclaredMethod("loadFrom", AbstractMethodDefinition.class);
                } catch (NoSuchMethodException | SecurityException ex) {
                    Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                }
                return null;
            }
        };
    }

    @Override
    public boolean isSuitableContainer(AdsDefinitions collection) {
        if (collection instanceof Methods.LocalMethods) {
            AdsClassDef ownerClass = (AdsClassDef) ((Methods.LocalMethods) collection).getOwnerDefinition();
            if (ownerClass != null) {
                AdsTransparence t = ownerClass.getTransparence();
                if (t != null && t.isTransparent()) {
                    if (t.isExtendable()) {
                        return true;
                    } else {
                        return (this instanceof AdsTransparentMethodDef);
                    }

                } else {
                    return true;
                }
            } else {
                return false;
            }
        } else {
            return (collection instanceof MethodGroups);
        }
    }

    @Override
    public EDefType getDefinitionType() {
        return EDefType.CLASS_METHOD;
    }

    @Override
    public boolean delete() {
        AdsClassDef ownerClass = getOwnerClass();
        if (super.delete()) {
            if (ownerClass != null) {
                ownerClass.getMethodGroup().memberDeleted(this);
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    public String getToolTip() {
        return getToolTip(EIsoLanguage.ENGLISH);
    }

    @Override
    public String getToolTip(EIsoLanguage language) {
        final StringBuilder sb = new StringBuilder();
        final String typeTitle = getTypeTitle();
        sb.append("<html><b>").append(typeTitle).append(" '");
        getProfile().preintProfileHtml(sb);
        sb.append("'</b>");
        appendLocationToolTip(sb);

        AdsMethodDef ovr = getHierarchy().findOverridden().get();
        if (ovr != null) {
            Utils.appendReferenceToolTipHtml(ovr, "Overrides", sb);
        }

        ovr = getHierarchy().findOverwritten().get();
        if (ovr != null) {
            Utils.appendReferenceToolTipHtml(ovr, "Overwrites", sb);
        }
        sb.append("<br><b>Parameters:</b>&nbsp;");
        for (MethodParameter p : getProfile().getParametersList()) {
            sb.append("<br>&nbsp;");
            sb.append(p.getType().getHtmlName(this, true));
            sb.append("&nbsp;");
            sb.append(p.getName());
            final String desc = getLocalizedDescriptionForToolTip(p, language);
            if (desc != null && !desc.isEmpty()) {
                sb.append("&nbsp;-&nbsp;");
                sb.append(StringEscapeUtils.escapeHtml(desc).replace("\n", "<br>&nbsp;"));
            }
        }
        if (getProfile().getReturnValue() != null && getProfile().getReturnValue().getType() != null) {
            sb.append("<br><b>Return value:</b><br>&nbsp;");
            sb.append(getProfile().getReturnValue().getType().getHtmlName(this, true));
            sb.append("&nbsp;");

            final String desc = getLocalizedDescriptionForToolTip(getProfile().getReturnValue(), language);
            if (desc != null && !desc.isEmpty()) {
                sb.append("&nbsp;-&nbsp;");
                sb.append(StringEscapeUtils.escapeHtml(desc).replace("\n", "<br>&nbsp;"));
            }
        }

        if (isInBranch()) { // otherwise, appendAdditionalToolTip can throw NullPointerException.
            appendAdditionalToolTip(sb);
        }

        final String description = StringEscapeUtils.escapeHtml(getDescriptionForToolTip(language)).replace("\n", "<br>&nbsp;");
        if (description != null && !description.isEmpty()) {
            sb.append("<br><b>Description: </b><br>&nbsp;").append(description);
        }

        return sb.toString();
    }

    @Override
    public ENamingPolicy getNamingPolicy() {
        return ENamingPolicy.IDENTIFIER; // profile must be unique, not function name
    }

    @Override
    public boolean canChangeAccessMode() {
        return true;
    }

    @Override
    public void setFinal(boolean isFinal) {
        super.setFinal(isFinal);
        if (isFinal && getProfile() != null) {
            if (getProfile().getAccessFlags().isAbstract()) {
                getProfile().getAccessFlags().setAbstract(false);
            }
        }
    }

    @Override
    public boolean canBeFinal() {
        return super.canBeFinal() && !getProfile().getAccessFlags().isAbstract() && !isConstructor;
    }

    @Override
    public EAccess getMinimumAccess() {
        if (isReflectiveCallable() && getNature() != EMethodNature.PRESENTATION_SLOT) {
            return EAccess.PUBLIC;
        } else {
            AdsClassDef owner = getOwnerClass();
            if (owner != null && owner.getClassDefType() == EClassType.INTERFACE) {
                return EAccess.PUBLIC;
            } else if (isConstructor()) {
                if (owner != null) {
                    switch (owner.getClassDefType()) {
                        case ALGORITHM:
                        case APPLICATION:
                        //case ENTITY:
                        case FORM_HANDLER:
                        case PRESENTATION_ENTITY_ADAPTER:
                            return EAccess.DEFAULT;
                    }
                }
            }
            return EAccess.PRIVATE;
        }
    }

    @Override
    public boolean isFinal() {
        if (isConstructor || getProfile().getAccessFlags().isStatic()) {
            return false;
        } else if (getAccessMode() == EAccess.PRIVATE) {
            return true;
        } else {
            return super.isFinal();
        }
    }

    @Override
    public boolean canChangeFinality() {
        if (isConstructor || getAccessMode() == EAccess.PRIVATE || getProfile().getAccessFlags().isStatic()) {
            return false;
        } else {
            return super.canChangeFinality();
        }
    }

    @Override
    public AdsProfileSupport getProfileSupport() {
        if (isProfileable()) {
            return profileSupport;
        } else {
            return null;
        }
    }

    @Override
    public boolean isProfileable() {
        return getUsageEnvironment() == ERuntimeEnvironmentType.SERVER;
    }

    @Override
    public AdsDefinition getAdsDefinition() {
        return this;
    }

    @Override
    public EAccess getAccessMode() {
        if (isInterfaceMethod()) {
            return EAccess.PUBLIC;
        } else if (isEnumClassConstructor()) {
            return EAccess.PRIVATE;
        } else {
            return super.getAccessMode();
        }
    }

    public boolean isEnumClassConstructor() {
        AdsClassDef clazz = getOwnerClass();
        return isConstructor() && clazz != null && clazz.getClassDefType() == EClassType.ENUMERATION;
    }

    public boolean isInterfaceMethod() {
        AdsClassDef clazz = getOwnerClass();
        return clazz != null && clazz.getClassDefType() == EClassType.INTERFACE;
    }

    @Override
    public boolean isDeprecated() {
        return getProfile().getAccessFlags().isDeprecated();
    }

    @Override
    protected void afterSetIdSourceItem(Id oldId) {
        super.afterSetIdSourceItem(oldId);
        AdsClassDef clazz = getOwnerClass();
        if (clazz != null) {
            AdsMethodGroup mg = clazz.getMethodGroup().findGroupByMethodId(oldId);
            if (mg != null) {
                mg.removeMemberId(oldId);
                mg.addMember(this);
            }
        }
    }

    @Override
    public ERuntimeEnvironmentType getUsageEnvironment() {
        if (usageEnvironment == null) {
            return super.getUsageEnvironment();
        } else {
            if (getOwnerClass() == null || getOwnerClass().isDual()) {
                return usageEnvironment;
            } else {
                return getOwnerClass().getUsageEnvironment();
            }
        }
    }
 
    @Override
    public void collectUsedMlStringIds(final Collection<MultilingualStringInfo> ids) {
        super.collectUsedMlStringIds(ids);

        class DescriptionMultilingualStringInfo extends MultilingualStringInfo {

            private final ILocalizedDescribable definition;

            public DescriptionMultilingualStringInfo(ILocalizedDescribable definition, ILocalizedDef owner) {
                super(owner);
                this.definition = definition;
            }

            @Override
            public String getContextDescription() {
                return " Description";
            }

            @Override
            public Id getId() {
                return definition.getDescriptionId();
            }

            @Override
            public EAccess getAccess() {
                return AdsMethodDef.this.getAccessMode();
            }

            @Override
            public void updateId(Id newId) {
                definition.setDescriptionId(newId);
            }

            @Override
            public boolean isPublished() {
                return AdsMethodDef.this.isPublished();
            }

            @Override
            public boolean canChangeId() {
                return !(definition instanceof ILocalizedDescribable.ILocalizedCalculatedDef) || !((ILocalizedDescribable.ILocalizedCalculatedDef) definition).isDescriptionIdCalculated();
            }

            @Override
            public EMultilingualStringKind getKind() {
                return EMultilingualStringKind.DESCRIPTION;
            }
            
        }

        if (getProfile().getReturnValue() != null && getProfile().getReturnValue().getDescriptionId() != null) {
            ids.add(new DescriptionMultilingualStringInfo(getProfile().getReturnValue(), this));
        }

        for (MethodParameter parameter : getProfile().getParametersList()) {
            if (parameter.getDescriptionId() != null) {
                ids.add(new DescriptionMultilingualStringInfo(parameter, this));
            }
        }

        for (ThrowsListItem throwsListItem : getProfile().getThrowsList()) {
            if (throwsListItem.getDescriptionId() != null) {
                ids.add(new DescriptionMultilingualStringInfo(throwsListItem, this));
            }
        }
    }

    public void setUsageEnvironment(ERuntimeEnvironmentType env) {
        if (this.usageEnvironment != env) {
            if (env == getUsageEnvironment() && usageEnvironment != null) {
                this.usageEnvironment = null;
                setEditState(EEditState.MODIFIED);
            } else {
                this.usageEnvironment = env;
                setEditState(EEditState.MODIFIED);
            }
        }
    }

    @Override
    public boolean needsDocumentation() {
        AdsMethodDef ovr = getHierarchy().findOverridden().get();
        
        if (ovr == null) {
            ovr = getHierarchy().findOverwritten().get();
        }
        if (ovr == null) {
            return true;
        } else {
            return false;
        }
    }
}
