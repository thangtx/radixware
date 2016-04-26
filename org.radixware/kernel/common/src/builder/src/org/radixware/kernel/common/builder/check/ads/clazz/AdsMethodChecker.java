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
package org.radixware.kernel.common.builder.check.ads.clazz;

import java.text.MessageFormat;
import java.util.List;
import org.radixware.kernel.common.builder.check.ads.AdsDefinitionChecker;
import org.radixware.kernel.common.builder.check.ads.CheckUtils;
import static org.radixware.kernel.common.builder.check.common.RadixObjectChecker.error;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsExceptionClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.*;
import org.radixware.kernel.common.defs.ads.common.AdsUtils;
import org.radixware.kernel.common.defs.ads.type.*;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.enums.EMethodNature;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;

@RadixObjectCheckerRegistration
public class AdsMethodChecker<T extends AdsMethodDef> extends AdsDefinitionChecker<T> {

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return AdsMethodDef.class;
    }

    private interface Messages {

        String undefinedType();

        String voidType();

        String unresolvedType();

        String conflictServerClient();

        String conflictClientServer();
    }

    private static class SuffixMessages implements Messages {

        private String value;

        SuffixMessages(String value) {
            this.value = value;
        }

        @Override
        public String undefinedType() {
            return MessageFormat.format("{0} type is undefined", value);
        }

        @Override
        public String voidType() {
            return MessageFormat.format("{0} type should not be void", value);
        }

        @Override
        public String unresolvedType() {
            return MessageFormat.format("{0} type can not be resolved", value);
        }

        @Override
        public String conflictServerClient() {
            return MessageFormat.format("{0} type is resolved as server side class, but method is in client side class", value);
        }

        @Override
        public String conflictClientServer() {
            return MessageFormat.format("{0} type is resolved as client side class, but method is in server side class", value);
        }
    }

    private static class PrefixMessages implements Messages {

        private String value;

        PrefixMessages(String value) {
            this.value = value;
        }

        @Override
        public String undefinedType() {
            return MessageFormat.format("Type of {0} is undefined", value);
        }

        @Override
        public String voidType() {
            return MessageFormat.format("Type of {0} should not be void", value);
        }

        @Override
        public String unresolvedType() {
            return MessageFormat.format("Type of {0} can not be resolved", value);
        }

        @Override
        public String conflictServerClient() {
            return MessageFormat.format("Type of {0} is resolved as server side class, but method is in client side class", value);
        }

        @Override
        public String conflictClientServer() {
            return MessageFormat.format("Type of {0} is resolved as client side class, but method is in server side class", value);
        }
    }

    @Override
    public void check(T method, IProblemHandler problemHandler) {
        super.check(method, problemHandler);
        checkMethodProfile(method, problemHandler);
    }

    private void checkMethodProfile(AdsMethodDef method, IProblemHandler problemHandler) {
        if (!(method.isConstructor())) {
            checkParameterDeclaration(method.getProfile().getReturnValue(), true, method, problemHandler, new SuffixMessages("Return value"));
        }

        AdsAccessFlags accf = method.getProfile().getAccessFlags();

        if (accf.isAbstract()) {
            if (accf.isFinal()) {
                error(method, problemHandler, "Abstract methods must not be final");
            }
            if (accf.isStatic()) {
                error(method, problemHandler, "Abstract methods must not be static");
            }
            if (!method.isPublished()) {
                if (method.getOwnerClass().isPublished()) {
                    if (!method.isWarningSuppressed(AdsMethodProblems.UNPUBLISHED_ABSTRACT_METHOD)) {
                        warning(method, problemHandler, AdsMethodProblems.UNPUBLISHED_ABSTRACT_METHOD);
                    }
                }
            }
        }

        AdsMethodParameters parameters = method.getProfile().getParametersList();

        for (MethodParameter p : parameters) {
            checkParameterDeclaration(p, false, method, problemHandler, new PrefixMessages("parameter \"" + p.getName() + "\""));
        }

        AdsMethodThrowsList throwsList = method.getProfile().getThrowsList();

        int counter = 1;
        for (AdsMethodThrowsList.ThrowsListItem decl : throwsList.list()) {
            AdsType type = decl.getException() == null ? null : decl.getException().resolve(method).get(AdsDefinitionChecker.<AdsType>getSearchDuplicatesChecker(method, problemHandler));

            if (type == null) {
                error(method, problemHandler, MessageFormat.format("Throws list item #{0} can not be resolved to a type", counter));
                continue;
            }
            if (type instanceof AdsClassType) {
                AdsClassDef clazz = ((AdsClassType) type).getSource();
                AdsUtils.checkAccessibility(method, clazz, false, problemHandler);
                CheckUtils.checkExportedApiDatails(decl, clazz, problemHandler);

                if (clazz.getClassDefType() == EClassType.EXCEPTION) {

                    if (method.getUsageEnvironment() != clazz.getUsageEnvironment()) {
                        if (clazz.getUsageEnvironment() != ERuntimeEnvironmentType.COMMON) {
                            if (clazz.getUsageEnvironment().isClientEnv()) {
                                if (clazz.getUsageEnvironment() != ERuntimeEnvironmentType.COMMON_CLIENT) {
                                    error(method, problemHandler, MessageFormat.format("Throws list item #{0} environment type mismatch: class {1} is {2} class but method is {3}", counter, clazz.getQualifiedName(method), clazz.getUsageEnvironment().getName(), method.getUsageEnvironment().getName()));
                                    continue;
                                }
                            } else {
                                error(method, problemHandler, MessageFormat.format("Throws list item #{0} environment type mismatch: class {1} is {2} class but method is {3}", counter, clazz.getQualifiedName(method), clazz.getUsageEnvironment().getName(), method.getUsageEnvironment().getName()));
                                continue;
                            }
                        }
                    }

                } else {
                    error(method, problemHandler, MessageFormat.format("Throws list item #{0} is not an exception class reference", counter));
                    continue;
                }
            } else {
                if (type instanceof JavaClassType) {
                    JavaClassType javaClassType = (JavaClassType) type;
                    warning(method, problemHandler, MessageFormat.format("Throws list item #{0} is not ADS class reference: {1}", counter, javaClassType.getJavaClassName()));
                } else {
                    error(method, problemHandler, MessageFormat.format("Throws list item #{0} is not ADS or platform class reference", counter));
                }
            }
            counter++;
        }

        if (method.isReflectiveCallable()) {
            if (method.getAccessMode() != EAccess.PUBLIC && method.getNature() != EMethodNature.PRESENTATION_SLOT) {
                problemHandler.accept(RadixProblem.Factory.newError(method, "Method is accessible through reflection and must be public"));
            }
            AdsClassDef clazz = method.getOwnerClass();
            if (clazz != null && clazz.getAccessMode() != EAccess.PUBLIC && !(clazz instanceof AdsModelClassDef) /*
                     * && !(clazz instanceof AdsEntityObjectClassDef)
                     */ && clazz.getClassDefType() != EClassType.ALGORITHM) {
                problemHandler.accept(RadixProblem.Factory.newError(clazz, "Method " + method.getName() + " accessible through reflection and it's owner class must be public"));
            }
        }
        AdsMethodDef ovrt = method.getHierarchy().findOverwritten().get();
        if (ovrt != null) {
            EAccess acc = method.getAccessMode();
            EAccess acc2 = ovrt.getAccessMode();

            if (acc.isLess(acc2)) {
                error(method, problemHandler, "Accessibility of overwritten method " + ovrt.getName() + " can not be reduced. Should be not less than " + acc2);
            }
        }
        AdsMethodDef ovr = method.getHierarchy().findOverridden().get();
        if (ovr != null) {
            if (ovr.isReflectiveCallable()) {
                if (!method.isReflectiveCallable()) {
                    problemHandler.accept(RadixProblem.Factory.newError(method, "Method must be accessible through reflection"));
                }
            }
            if (ovr.isFinal()) {
                problemHandler.accept(RadixProblem.Factory.newError(AdsMethodProblems.OVERRIDE_FINAL_METHOD, method, ovr.getQualifiedName()));
            }

            AdsUtils.checkAccessibility(method, ovr, true, problemHandler);
            CheckUtils.checkExportedApiDatails(method, ovr, problemHandler);

            EAccess acc = method.getAccessMode();
            EAccess acc2 = ovr.getAccessMode();

            if (acc.isLess(acc2)) {
                error(method, problemHandler, "Accessibility of overriden method " + ovr.getName() + " can not be reduced. Should be not less than " + acc2);
            }
            if (!method.isOverride() && !method.isConstructor() && !method.getAccessFlags().isStatic()) {
                warning(method, problemHandler, MessageFormat.format("The method overrides method {0}. Turn on \"Override\" option", ovr.getQualifiedName()));
            }
            if (ovr.isConstructor()) {
                if (!(method.isConstructor())) {
                    error(method, problemHandler, MessageFormat.format("The method overrides constructor {0}", ovr.getQualifiedName()));
                }
            } else {
                AdsTypeDeclaration[] ovrProfile = ovr.getProfile().getNormalizedProfile();
                AdsClassDef baseClass = ovr.getOwnerClass();
                AdsClassDef ownerClass = method.getOwnerClass();
                for (int i = 0; i < ovrProfile.length; i++) {
                    ovrProfile[i] = ovrProfile[i].getActualType(baseClass, ownerClass);
                }
                checkProfileCompatibility(method, method.getProfile().getNormalizedProfile(), ovrProfile, problemHandler, true);
            }
            
            List<AdsMethodThrowsList.ThrowsListItem> throwList = method.getProfile().getThrowsList().list();
            for (AdsMethodThrowsList.ThrowsListItem item : throwsList) {
                AdsType exeptionType = item.getException().resolve(method).get();
                if (exeptionType instanceof AdsClassType) {
                    AdsClassDef throwListItemClass = ((AdsClassType) exeptionType).getSource();
                    if (throwListItemClass instanceof AdsExceptionClassDef) {
                        if (!ProfileUtilities.isCorrectExeptionInThrowList((AdsExceptionClassDef) throwListItemClass, method , ovr.getProfile().getThrowsList().list())) {
                            error(method, problemHandler, MessageFormat.format("Invalid exception: {0}", throwListItemClass.getName()));
                        }
                    }
                }
            }

            if (ovr.getProfile().getAccessFlags().isAbstract() || ovr.getOwnerClass().getClassDefType() == EClassType.INTERFACE && !method.getProfile().getAccessFlags().isAbstract()) {
                if (method.getOwnerClass().isDual() && method.getUsageEnvironment() != ERuntimeEnvironmentType.COMMON_CLIENT && ovr.getUsageEnvironment() == ERuntimeEnvironmentType.COMMON_CLIENT) {
                    error(method, problemHandler, "The method overrides abstract method " + ovr.getQualifiedName() + " and has concrete implementation for " + method.getUsageEnvironment().getName() + " environment only. Make this method " + ERuntimeEnvironmentType.COMMON_CLIENT + " and separate sources if necessary");
                }
            }
            if (ovr.getUsageEnvironment() != method.getUsageEnvironment()) {
                boolean error = false;
                if (ovr.getUsageEnvironment().isClientEnv() != method.getUsageEnvironment().isClientEnv()) {
                    if (ovr.getUsageEnvironment() != ERuntimeEnvironmentType.COMMON) {
                        error = true;
                    }
                } else {
                    if (method.getUsageEnvironment().isClientEnv()) {
                        if (ovr.getUsageEnvironment() != ERuntimeEnvironmentType.COMMON_CLIENT) {
                            error = true;
                        } else {
                            if (method.getOwnerClass().getUsageEnvironment() == ERuntimeEnvironmentType.COMMON_CLIENT) {
                                if (!method.isWarningSuppressed(AdsMethodProblems.MISSING_IMPLEMENTATION_ON_OVERRIDE)) {
                                    ERuntimeEnvironmentType misEnv;
                                    switch (method.getUsageEnvironment()) {
                                        case WEB:
                                            misEnv = ERuntimeEnvironmentType.EXPLORER;
                                            break;
                                        default:
                                            misEnv = ERuntimeEnvironmentType.WEB;
                                    }
                                    warning(method, problemHandler, AdsMethodProblems.MISSING_IMPLEMENTATION_ON_OVERRIDE, new String[]{misEnv.getName(), ovr.getQualifiedName()});
                                }
                            }
                        }
                    } else {
                        if (ovr.getUsageEnvironment() != ERuntimeEnvironmentType.COMMON) {
                            error = true;
                        }
                    }
                }
                if (error) {
                    error(method, problemHandler, "The method of environment " + method.getUsageEnvironment().getName() + " can not override method of environment " + ovr.getUsageEnvironment().getName());
                }
            }
        } else {
            if (method.isOverride()) {
                error(method, problemHandler, "The method does not overrides any method from superclass. Turn off \"Override\" option");
            }
        }

        // check default constructor without parameters for Entity classes
        if (method.isConstructor() && method.getAccessFlags().isPrivate()) {
            final AdsClassDef ownerClass = method.getOwnerClass();

            if (ownerClass.getClassDefType() == EClassType.ENTITY) {
                error(method, problemHandler, "Minimum access mode of the default constructor of entity class must be internal");
            }
        }

        // A varargs parameter must be the last parameter in a parameter list.
        checkVarargs(method.getProfile().getParametersList(), problemHandler);
    }

    private void checkVarargs(AdsMethodParameters parameters, IProblemHandler problemHandler) {
        final int size = parameters.size();
        for (int i = 0; i < size; i++) {
            final MethodParameter parameter = parameters.get(i);

            if (i < size - 1) {
                if (parameter.isVariable()) {
                    error(parameter, problemHandler, "A varargs parameter must be the last parameter in a parameter list");
                }
            }

            if (parameter.isVariable() && !parameter.getType().isArray()) {
                error(parameter, problemHandler, "A varargs parameter must be array type");
            }
        }
    }

    public AdsMethodChecker() {
    }

    private void checkParameterDeclaration(AdsMethodDef.MethodValue value, boolean mayBeVoid, AdsMethodDef context, IProblemHandler problemHandler, Messages messages) {
        checkDocumentation(value, problemHandler);
        AdsTypeDeclaration decl = value.getType();
        if (decl == null || decl == AdsTypeDeclaration.Factory.undefinedType()) {
            error(context, problemHandler, messages.undefinedType());
        } else {
            if (decl == AdsTypeDeclaration.Factory.voidType()) {
                if (!mayBeVoid) {
                    error(context, problemHandler, messages.voidType());
                }
            }
            AdsType type = decl.resolve(context).get(AdsDefinitionChecker.<AdsType>getSearchDuplicatesChecker(context, problemHandler));
            if (type == null) {
                error(context, problemHandler, messages.unresolvedType());
            } else {
                type.check(context, problemHandler);
                if (type instanceof AdsDefinitionType) {
                    Definition src = ((AdsDefinitionType) type).getSource();
                    if (src instanceof AdsDefinition) {
                        AdsUtils.checkAccessibility(value, (AdsDefinition) src, false, problemHandler);
                        CheckUtils.checkExportedApiDatails(value, (AdsDefinition) src, problemHandler);
                    }
                }
            }
        }
    }

    protected boolean checkProfileCompatibility(AdsMethodDef source, AdsTypeDeclaration[] what, AdsTypeDeclaration[] with, IProblemHandler problemHandler) {
        return checkProfileCompatibility(source, what, with, problemHandler, false);
    }

    protected boolean checkProfileCompatibility(AdsMethodDef source, AdsTypeDeclaration[] what, AdsTypeDeclaration[] with, IProblemHandler problemHandler, boolean forOverrideSuite) {
        ProfileUtilities.ProfileCompareResults results = ProfileUtilities.compareProfiles(source, what, with, forOverrideSuite);
        if (!results.ok()) {
            boolean result = true;
            if (results.returnTypeError != null) {
                problemHandler.accept(RadixProblem.Factory.newError(source, MessageFormat.format("Invalid return value type: expected {0} got {1}", results.returnTypeError.expected.getName(source), results.returnTypeError.got.getName(source))));
                result = false;
            }
            for (ProfileUtilities.ProfileCompareError e : results.problems) {
                if (e.got != null && e.expected != null) {
                    problemHandler.accept(RadixProblem.Factory.newError(source, MessageFormat.format("Invalid type of parameter {0}: expected {1} got {2}", e.paramIndex + 1, e.expected.getName(source), e.got.getName(source))));
                    result = false;
                } else {
                    if (e.got == null && e.expected != null) {
                        problemHandler.accept(RadixProblem.Factory.newError(source, MessageFormat.format("Missing parameter of type {0}", e.expected.getName(source))));
                        result = false;
                    } else if (e.expected == null && e.got != null) {
                        problemHandler.accept(RadixProblem.Factory.newError(source, MessageFormat.format("Redundant parameter of type {0}", e.got.getName(source))));
                        result = false;

                    }
                }
            }
            return result;
        } else {
            return true;
        }
    }
}
