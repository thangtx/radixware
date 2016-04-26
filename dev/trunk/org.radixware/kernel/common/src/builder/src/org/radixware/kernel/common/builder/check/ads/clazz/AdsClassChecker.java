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
import java.util.HashMap;
import java.util.List;
import org.radixware.kernel.common.builder.check.ads.AdsDefinitionChecker;
import org.radixware.kernel.common.builder.check.ads.CheckUtils;
import static org.radixware.kernel.common.builder.check.common.RadixObjectChecker.error;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IFilter;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef.SourcePart;
import org.radixware.kernel.common.defs.ads.clazz.Inheritance;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.command.AdsCommandModelClassDef;
import org.radixware.kernel.common.defs.ads.common.AdsUtils;
import org.radixware.kernel.common.defs.ads.common.JavaSignatures;
import org.radixware.kernel.common.defs.ads.platform.IPlatformClassPublisher;
import org.radixware.kernel.common.defs.ads.platform.RadixPlatformClass;
import org.radixware.kernel.common.defs.ads.type.*;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.repository.ads.AdsSegment;
import org.radixware.kernel.common.types.Id;


@RadixObjectCheckerRegistration
public class AdsClassChecker<T extends AdsClassDef> extends AdsDefinitionChecker<T> {

    public AdsClassChecker() {
    }

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return AdsClassDef.class;
    }

    @Override
    public void check(T clazz, IProblemHandler problemHandler) {
        super.check(clazz, problemHandler);
        checkInheritance(clazz, problemHandler);
        checkMethodNameDuplication(clazz, problemHandler);
        if (clazz.getClassDefType() != EClassType.INTERFACE) {
            AdsTransparence transparence = clazz.getTransparence();
            if (transparence != null && transparence.isTransparent() && !transparence.isExtendable()) {
                if (clazz instanceof IPlatformClassPublisher) {
                    IPlatformClassPublisher.IPlatformClassPublishingSupport support = ((IPlatformClassPublisher) clazz).getPlatformClassPublishingSupport();
                    if (support != null && support.isPlatformClassPublisher()) {
                        AdsSegment segment = (AdsSegment) clazz.getModule().getSegment();
                        RadixPlatformClass pc = segment.getBuildPath().getPlatformLibs().getKernelLib(clazz.getUsageEnvironment()).findPlatformClass(support.getPlatformClassName());
                        if (pc != null) {
                            if (pc.isInterface()) {
                                error(clazz, problemHandler, "Class " + clazz.getQualifiedName() + " must be interface because wrapped class " + support.getPlatformClassName() + " is interface");
                            } else {
                                if (pc.isAbstract() != clazz.getAccessFlags().isAbstract()) {
                                    if (pc.isAbstract()) {
                                        error(clazz, problemHandler, "Class " + clazz.getQualifiedName() + " must be abstract because wrapped class " + support.getPlatformClassName() + " is abstract");
                                    } else {
                                        error(clazz, problemHandler, "Class " + clazz.getQualifiedName() + " must be concrete because wrapped class " + support.getPlatformClassName() + " is concrete");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        checkNestedClasses(clazz, problemHandler);

        if (!clazz.isCodeEditable()) {
            if (!clazz.getMethods().getLocal().isEmpty()) {
                error(clazz, problemHandler, "Methods are not allowed for class " + clazz.getQualifiedName());
            }
            if (!clazz.getProperties().getLocal().isEmpty()) {
                error(clazz, problemHandler, "Properties are not allowed for class " + clazz.getQualifiedName());
            }
            if (!clazz.getNestedClasses().getLocal().isEmpty()) {
                error(clazz, problemHandler, "Nested classes are not allowed for class " + clazz.getQualifiedName());
            }
            for (AdsClassDef.SourcePart src : clazz.getHeader()) {
                if (!src.getItems().isEmpty()) {
                    error(src, problemHandler, "Outer code (header) is not allowed for class " + clazz.getQualifiedName());
                    break;
                }
            }
            for (AdsClassDef.SourcePart src : clazz.getBody()) {
                if (!src.getItems().isEmpty()) {
                    error(src, problemHandler, "Inner code (body) is not allowed for class " + clazz.getQualifiedName());
                    break;
                }
            }

        }

    }

    private void checkMethodNameDuplication(AdsClassDef clazz, IProblemHandler problemHandler) {
        List<AdsMethodDef> methods = clazz.getMethods().get(EScope.ALL);
        HashMap<String, HashMap<Id, AdsMethodDef>> methodsByName = new HashMap<String, HashMap<Id, AdsMethodDef>>();

        for (AdsMethodDef method : methods) {
            if (method.isConstructor() || method.getProfile().getAccessFlags().isStatic()) {
                continue;
            }
            String name = method.getName() + ":" + method.getProfile().getParametersList().size();
            HashMap<Id, AdsMethodDef> sameName = methodsByName.get(name);
            if (sameName == null) {
                sameName = new HashMap<Id, AdsMethodDef>();
                methodsByName.put(name, sameName);
            }
            sameName.put(method.getId(), method);
        }

        HashMap<String, HashMap<Id, AdsMethodDef>> methodsBySignature = new HashMap<String, HashMap<Id, AdsMethodDef>>();
        for (HashMap<Id, AdsMethodDef> sameName : methodsByName.values()) {
            if (sameName.size() > 1) {
                methodsBySignature.clear();
                for (AdsMethodDef method : sameName.values()) {
                    String sig = new String(JavaSignatures.generateSignature(clazz, method, false));
                    HashMap<Id, AdsMethodDef> sameSig = methodsBySignature.get(sig);
                    if (sameSig == null) {
                        sameSig = new HashMap<Id, AdsMethodDef>();
                        methodsBySignature.put(sig, sameSig);
                    }
                    sameSig.put(method.getId(), method);
                }
                for (HashMap<Id, AdsMethodDef> sameSig : methodsBySignature.values()) {
                    if (sameSig.size() > 1) {
                        Object[] names = new String[sameSig.size()];
                        StringBuilder message = new StringBuilder();
                        int counter = 0;
                        AdsMethodDef atLeastOnInOurClass = null;
                        for (AdsMethodDef method : sameSig.values()) {
                            if (method.getOwnerClass() == clazz) {
                                atLeastOnInOurClass = method;
                            }
                            if (counter > 0 && counter <= sameSig.size() - 1) {
                                message.append(", ");
                            }
                            message.append("{").append(String.valueOf(counter)).append("}");
                            names[counter] = method.getQualifiedName() + "()";
                            counter++;
                        }
                        if (atLeastOnInOurClass != null) {
                            String messageStr = MessageFormat.format(message.toString(), names);
                            if (!clazz.isWarningSuppressed(AdsClassDef.Problems.METHODS_WITH_SAME_PROFILE_AND_DIFFERENT_IDS)) {
                                warning(clazz, problemHandler, AdsClassDef.Problems.METHODS_WITH_SAME_PROFILE_AND_DIFFERENT_IDS, messageStr);
                            }
                        }
                    }
                }
            }
        }
    }

    protected void checkSuperclassDetails(T clazz, AdsClassDef resolvedBaseClass, IProblemHandler problemHandler) {
        if (resolvedBaseClass.isFinal()) {
            error(clazz, problemHandler, "Can not extend final class " + resolvedBaseClass.getQualifiedName());
        }
        switch (clazz.getClassDefType()) {
            case DYNAMIC:
                if (resolvedBaseClass.getClassDefType() != EClassType.DYNAMIC) {
                    error(clazz, problemHandler, "Dynamic class may extend dynamic classes only");
                }
                break;
            case EXCEPTION:
                if (resolvedBaseClass.getClassDefType() != EClassType.EXCEPTION) {
                    error(clazz, problemHandler, "Exception class may extend exception classes only");
                }
                break;
            default:
                break;
        }
        if (resolvedBaseClass != null) {
            AdsUtils.checkAccessibility(clazz, resolvedBaseClass, false, problemHandler);
            CheckUtils.checkExportedApiDatails(clazz.getInheritance(), resolvedBaseClass, problemHandler);
        }
    }

    protected void unresolvedSuperclassReferenceDetails(T clazz, AdsType resolvedType, IProblemHandler problemHandler) {
    }

    protected void nullSuperclassReferenceDetails(T clazz, IProblemHandler problemHandler) {
    }

    private void checkInheritance(T clazz, IProblemHandler problemHandler) {
        Inheritance inheritance = clazz.getInheritance();
        ERuntimeEnvironmentType env = clazz.getUsageEnvironment();
        ERuntimeEnvironmentType clientEnv = clazz.getClientEnvironment();
        //-------------------- check transparence features -------------------------
        AdsTransparence transparence = clazz.getTransparence();
        if (transparence != null && transparence.isTransparent()) {
            PlatformClassCache cache = this.getHistory().findItemByClass(PlatformClassCache.class);
            if (cache == null) {
                cache = new PlatformClassCache();
                this.getHistory().registerItemByClass(cache);
            }

            String publishedClassName = transparence.getPublishedName();

            RadixPlatformClass system = cache.findPlatformClass(clazz, publishedClassName);
            if (system == null) {
                error(clazz, problemHandler, "Wrapped platform class not found: " + publishedClassName);
            } else {
                AdsTypeDeclaration sysdecl = system.getDeclaration();
                if (sysdecl.getGenericArguments() != null) {
                    if (!clazz.getTypeArguments().equalsTo(clazz, sysdecl.getGenericArguments())) {
                        error(clazz, problemHandler, "Type arguments does not match to wrapped class arguments. Expected " + sysdecl.getGenericArguments().getQualifiedName(clazz) + " got " + clazz.getTypeArguments().getQualifiedName(clazz));
                    }
                }
            }
        } else {
            final AdsTypeDeclaration ref = inheritance.getSuperClassRef();
            if (ref != null) {

                final AdsType resolvedType = ref.resolve(clazz).get(AdsDefinitionChecker.<AdsType>getSearchDuplicatesChecker(clazz, problemHandler));

                if (resolvedType instanceof AdsDefinitionType) {
                    if (resolvedType instanceof AdsClassType) {
                        final AdsClassDef resolvedClass = ((AdsClassType) resolvedType).getSource();
                        checkEnv(env, clazz, resolvedClass, problemHandler);

                        checkClientEnv(clientEnv, clazz, resolvedClass, problemHandler);
                        checkSuperclassDetails(clazz, resolvedClass, problemHandler);
                    } else {
                        if (clazz.getClassDefType() != EClassType.COMMAND_MODEL || !(resolvedType instanceof AdsCommandType)) {
                            error(clazz, problemHandler, "Supeclass is resolved as non-class definition " + resolvedType.getQualifiedName(null));
                            unresolvedSuperclassReferenceDetails(clazz, resolvedType, problemHandler);
                        }
                    }
                } else {
                    if (resolvedType instanceof JavaClassType) {
                        final IPlatformClassPublisher publisher = ((AdsSegment) clazz.getModule().getSegment()).getBuildPath().getPlatformPublishers().findPublisherByName(ref.getExtStr());
                        if (publisher == null) {
                            error(clazz, problemHandler, MessageFormat.format("Super class reference is resolved to platform class. But {0} is not wrapped", resolvedType.getQualifiedName(null)));
                        } else if (publisher instanceof AdsClassDef) {
                            final IPlatformClassPublisher.IPlatformClassPublishingSupport support = publisher.getPlatformClassPublishingSupport();
                            if (support != null && support.isExtendablePublishing()) {
                                error(clazz, problemHandler, MessageFormat.format("Super class reference is resolved to platform class, but {0} is not wrapped with extensibility. Should directly extend {1}", resolvedType.getQualifiedName(null), ((AdsDefinition) publisher).getQualifiedName()));
                            } else {
                                error(clazz, problemHandler, MessageFormat.format("Super class reference is resolved to platform class. But {0} is not wrapped as {1}", resolvedType.getQualifiedName(null), ((AdsDefinition) publisher).getTypeTitle()));
                            }
                        }
                    } else {
                        if (resolvedType == null) {
                            error(clazz, problemHandler, "Superclass reference can not be resolved");
                        } else {
                            error(clazz, problemHandler, MessageFormat.format("Superclass reference resolved to unrecognized type {0}", resolvedType.getQualifiedName(null)));
                        }
                    }
                    unresolvedSuperclassReferenceDetails(clazz, resolvedType, problemHandler);
                }

            } else {
                nullSuperclassReferenceDetails(clazz, problemHandler);
            }
        }
        //--------------------------------------------------------------------------
        List<AdsTypeDeclaration> interfaces = inheritance.getInerfaceRefList(EScope.LOCAL_AND_OVERWRITE);
        if (transparence != null && transparence.isTransparent() && !transparence.isExtendable()) {
            if (!interfaces.isEmpty()) {
                error(clazz, problemHandler, "Non extendable publishings can not implement additional interfaces");
            }
        } else {
            for (AdsTypeDeclaration type : interfaces) {
                AdsType resolvedType = type.resolve(clazz).get(AdsDefinitionChecker.<AdsType>getSearchDuplicatesChecker(clazz, problemHandler));
                if (resolvedType instanceof AdsClassType) {
                    AdsClassDef resolvedClass = ((AdsClassType) resolvedType).getSource();
                    if (resolvedClass.getClassDefType() != EClassType.INTERFACE) {
                        error(clazz, problemHandler, MessageFormat.format("Class {0} is not an interface. Remove it from implements list", resolvedClass.getQualifiedName()));
                    }


                    if (env != ERuntimeEnvironmentType.COMMON_CLIENT || !clazz.isDual() || !resolvedClass.getUsageEnvironment().isClientEnv()) {
                        checkEnv(env, clazz, resolvedClass, problemHandler);
                        checkClientEnv(clientEnv, clazz, resolvedClass, problemHandler);
                    }

                    AdsUtils.checkAccessibility(clazz, resolvedClass, false, problemHandler);
                    CheckUtils.checkExportedApiDatails(clazz.getInheritance(), resolvedClass, problemHandler);
                } else if (resolvedType instanceof JavaClassType) {
                    error(clazz, problemHandler, MessageFormat.format("Super interface reference is resolved to platform class. Possibly {0} is not wrapped or not in module dependencies", resolvedType.getQualifiedName(null)));
                } else if (resolvedType != null) {
                    error(clazz, problemHandler, MessageFormat.format("Implements list of class should contain only references to interface classes, but contains {0}", resolvedType.getQualifiedName(null)));
                } else {
                    error(clazz, problemHandler, "Implemented interface can not be resolved");
                }
            }
        }
    }

    protected AdsClassDef findBaseHandler(T clazz, Id id) {
        ClassCache cache = getHistory().findItemByClass(ClassCache.class);
        if (cache == null) {
            cache = new ClassCache(null);
            getHistory().registerItemByClass(clazz);
        }
        return cache.findById(clazz, id);
    }

    private void checkEnv(ERuntimeEnvironmentType env, AdsClassDef clazz, AdsClassDef resolvedClass, IProblemHandler problemHandler) {
        if (env != resolvedClass.getUsageEnvironment()) {
            if (resolvedClass.getUsageEnvironment() != ERuntimeEnvironmentType.COMMON) {
                if (env.isClientEnv()) {
                    if (resolvedClass.getUsageEnvironment() != ERuntimeEnvironmentType.COMMON_CLIENT) {
                        error(clazz, problemHandler, "Class with environment type " + env.getName() + " must not extend or implement class with environment " + resolvedClass.getUsageEnvironment().getName());
                    }
                } else {
                    error(clazz, problemHandler, "Class with environment type " + env.getName() + " must not extend or implement class with environment " + resolvedClass.getUsageEnvironment().getName());
                }
            }
        }

    }

    private void checkClientEnv(ERuntimeEnvironmentType clientEnv, AdsClassDef clazz, AdsClassDef superClass, IProblemHandler problemHandler) {
        ERuntimeEnvironmentType superEnv = superClass.getClientEnvironment();
        if (superEnv != ERuntimeEnvironmentType.COMMON_CLIENT) {
            if (clientEnv != superEnv) {
                error(clazz, problemHandler, "Class with client environment " + clientEnv.getName() + " can not extend or implement class with client environment " + superEnv.getName());
            }
        } else {
            if (clientEnv != ERuntimeEnvironmentType.COMMON_CLIENT) {
                if (clazz.getClassDefType() == EClassType.ENTITY_MODEL && superClass.getClassDefType() == EClassType.ENTITY_MODEL) {
                    warning(clazz, problemHandler, "Common-client entity model " + superClass.getQualifiedName() + " must not be extended by " + clientEnv.getName() + " entity model class");
                }
            }
        }

    }

    private void checkNestedClasses(T clazz, IProblemHandler problemHandler) {
        if (clazz.isNested()) {
            final AdsClassDef ownerClass = clazz.getOwnerClass();

            final List<SourcePart> parts = clazz.getHeader().list();
            if (!parts.isEmpty()) {
                for (AdsClassDef.SourcePart sourcePart : parts) {
                    if (!sourcePart.getItems().isEmpty()) {
                        warning(clazz.getHeader(), problemHandler, "Nested class contains header, bun it will be ignored");
                        break;
                    }
                }
            }

            if (ownerClass != null) {
                if (clazz.getAccessFlags().isStatic()) {
                    if (ownerClass.isInner()) {
                        error(clazz, problemHandler, "Can't create static definition in non-static context");
                    }
                }

                if (!ownerClass.isDual()) {
                    if (!ERuntimeEnvironmentType.compatibility(ownerClass.getUsageEnvironment(), clazz.getUsageEnvironment())) {
                        error(clazz, problemHandler, "Usage environment of nested class is not compatible with usage environment of enclosing class");
                    }
                } else {
                    if (ownerClass.getUsageEnvironment() != clazz.getUsageEnvironment()
                            && ERuntimeEnvironmentType.EXPLORER != clazz.getUsageEnvironment()
                            && ERuntimeEnvironmentType.WEB != clazz.getUsageEnvironment()) {
                        error(clazz, problemHandler, "Usage environment of nested class is not compatible with usage environment of enclosing class");
                    }
                }
            }
            if (clazz.isAnonymous()) {
                if (clazz.hasConstructors()) {
                    error(clazz, problemHandler, "The anonymous class can't contains constructors");
                }
            }
        }
    }

    public static AdsMethodDef fingDefaultConstructor(AdsClassDef cls) {
        final List<AdsMethodDef> constructors = cls.getMethods().get(ExtendableDefinitions.EScope.LOCAL_AND_OVERWRITE, new IFilter<AdsMethodDef>() {
            @Override
            public boolean isTarget(AdsMethodDef radixObject) {
                return radixObject.isConstructor();
            }
        });

        for (final AdsMethodDef construct : constructors) {
            if (construct.getProfile().getParametersList().isEmpty()) {
                return construct;
            }
        }
        return null;
    }
}
