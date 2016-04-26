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

package org.eclipse.jdt.internal.compiler.lookup;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.internal.compiler.ast.ASTNode;
import org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.AbstractVariableDeclaration;
import org.eclipse.jdt.internal.compiler.ast.ConstructorDeclaration;
import org.eclipse.jdt.internal.compiler.ast.ExplicitConstructorCall;
import org.eclipse.jdt.internal.compiler.ast.FieldDeclaration;
import org.eclipse.jdt.internal.compiler.ast.MethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.QualifiedAllocationExpression;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.eclipse.jdt.internal.compiler.ast.TypeParameter;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.env.AccessRestriction;
import static org.eclipse.jdt.internal.compiler.lookup.Scope.CLASS_SCOPE;
import static org.eclipse.jdt.internal.compiler.lookup.Scope.METHOD_SCOPE;
import org.eclipse.jdt.internal.compiler.util.HashtableOfObject;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.type.AdsClassType;
import org.radixware.kernel.common.defs.ads.type.AdsEnumType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.JavaClassType;
import org.radixware.kernel.common.defs.ads.type.RadixType;
import org.radixware.kernel.common.defs.ads.type.XmlType;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.compiler.core.AdsLookup;
import org.radixware.kernel.common.compiler.core.ast.JMLFieldDeclaration;
import org.radixware.kernel.common.compiler.core.ast.Java2JmlConverter;
import org.radixware.kernel.common.compiler.core.ast.RadixObjectLocator;
//import org.radixware.kernel.common.compiler.core.lookup.PropertyBinding;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.xml.AdsXmlSchemeDef;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.common.jml.JmlTagInvocation;


public class AdsDefinitionScope extends ClassScope implements IAdsScope {

    public Definition definition;
    public RadixObjectLocator locator;

    public AdsDefinitionScope(Scope parent, AdsTypeDeclaration context) {
        super(parent, context);
        this.locator = context.locator;
        this.definition = context.definition;
    }

    public AdsDefinitionScope(AdsDefinitionScope parent, TypeDeclaration context) {
        super(parent, context);
        this.locator = parent.locator;
        this.definition = parent.definition;
    }

    public AdsTypeDeclaration adsContext() {
        return (AdsTypeDeclaration) referenceContext();
    }

//    private void checkAndSetModifiersForProperty(PropertyBinding binding, AdsPropertyDeclaration decl) {
//        binding.modifiers ^= ExtraCompilerModifiers.AccUnresolved;
//    }
    @Override
    void buildFields() {
        SourceTypeBinding sourceType = this.referenceContext.binding;
        if (sourceType.areFieldsInitialized()) {
            return;
        }
        AdsPropertyDeclaration[] properties = adsContext().properties;
        if (properties != null) {
            List<FieldDeclaration> bss = new LinkedList<>();
            for (int i = 0; i < properties.length; i++) {
                AdsPropertyDeclaration decl = properties[i];
                FieldDeclaration[] fields = decl.backstoreFields(this);
                if (fields != null && fields.length > 0) {
                    for (FieldDeclaration f : fields) {
                        bss.add(f);
                    }
                }
            }
            if (!bss.isEmpty()) {
                FieldDeclaration[] fields;
                if (referenceContext.fields != null) {
                    fields = new FieldDeclaration[referenceContext.fields.length + bss.size()];
                    System.arraycopy(referenceContext.fields, 0, fields, 0, referenceContext.fields.length);
                    for (int i = referenceContext.fields.length, c = 0; c < bss.size(); i++, c++) {
                        fields[i] = bss.get(c);
                    }
                    referenceContext.fields = fields;
                } else {
                    referenceContext.fields = bss.toArray(new FieldDeclaration[bss.size()]);
                }
            }
        }


        FieldDeclaration[] fields = adsContext().fields;
        if (fields != null) {
            Java2JmlConverter converter = new Java2JmlConverter(((AdsTypeDeclaration) this.referenceContext()).locator);
            for (int i = 0; i < fields.length; i++) {
                if (!(fields[i] instanceof JMLFieldDeclaration)) {
                    fields[i] = (FieldDeclaration) converter.convertToJML(fields[i], adsContext().initializerScope);
                }
            }
        }
        super.buildFields();
    }

    @Override
    void buildMethods() {

        SourceTypeBinding sourceType = this.referenceContext.binding;
        if (sourceType.areMethodsInitialized()) {
            return;
        }

        boolean isEnum = TypeDeclaration.kind(this.referenceContext.modifiers) == TypeDeclaration.ENUM_DECL;
        if (this.referenceContext.methods == null && !isEnum) {
            this.referenceContext.binding.setMethods(Binding.NO_METHODS);
            return;
        }

        AbstractMethodDeclaration[] methods = this.referenceContext.methods;

        final List<MethodBinding> bindings = new ArrayList<>();
        if (isEnum) {
            bindings.add(sourceType.addSyntheticEnumMethod(TypeConstants.VALUES));
            bindings.add(sourceType.addSyntheticEnumMethod(TypeConstants.VALUEOF));
        }
        boolean hasNativeMethods = false;
        if (methods != null) {

            if (sourceType.isAbstract()) {
                for (AbstractMethodDeclaration methodDecl : methods) {
                    if (!methodDecl.isClinit()) {
                        MethodBinding methodBinding = bindMethod(methodDecl);
                        if (methodBinding != null) { // is null if binding could not be created
                            bindings.add(methodBinding);
                            hasNativeMethods = hasNativeMethods || methodBinding.isNative();
                        }
                    }
                }
            } else {
                boolean abstractMethodsFounds = false;
                for (final AbstractMethodDeclaration methodDecl : methods) {
                    if (!methodDecl.isClinit()) {
                        MethodBinding methodBinding = bindMethod(methodDecl);
                        if (methodBinding != null) {
                            bindings.add(methodBinding);
                            abstractMethodsFounds = abstractMethodsFounds || methodBinding.isAbstract();
                            hasNativeMethods = hasNativeMethods || methodBinding.isNative();
                        }
                    }
                }
                if (abstractMethodsFounds) {
                    problemReporter().abstractMethodInConcreteClass(sourceType);
                }
            }
        }

        sourceType.tagBits &= ~(TagBits.AreMethodsSorted | TagBits.AreMethodsComplete);
        sourceType.setMethods(bindings.toArray(new MethodBinding[bindings.size()]));

        if (hasNativeMethods) {
            for (MethodBinding binding : bindings) {
                binding.modifiers |= ExtraCompilerModifiers.AccLocallyUsed;
            }
            FieldBinding[] fields = sourceType.unResolvedFields();
            for (int i = 0; i < fields.length; i++) {
                fields[i].modifiers |= ExtraCompilerModifiers.AccLocallyUsed;
            }
        }

    }

    private MethodBinding bindMethod(AbstractMethodDeclaration methodDecl) {
        MethodScope scope = new MethodScope(this, methodDecl, false);

        final Java2JmlConverter converter = new Java2JmlConverter(locator);

        if (methodDecl.thrownExceptions != null) {
            converter.convertToJML(methodDecl.thrownExceptions, scope);
        }
        if (methodDecl.annotations != null) {
            converter.convertToJML(methodDecl.annotations, scope);
        }
        if (methodDecl.arguments != null) {
            converter.convertToJML(methodDecl.arguments, scope);
        }
        if (methodDecl instanceof MethodDeclaration) {
            if (((MethodDeclaration) methodDecl).typeParameters != null) {
                converter.convertToJML(((MethodDeclaration) methodDecl).typeParameters, scope);
            }
        }
        if (methodDecl instanceof ConstructorDeclaration) {
            ConstructorDeclaration init = (ConstructorDeclaration) methodDecl;
            if (init.constructorCall != null) {
                init.constructorCall = (ExplicitConstructorCall) converter.convertToJML(init.constructorCall, scope);
            }
        }

        if (methodDecl.statements != null) {
            converter.convertToJML(methodDecl.statements, scope);
        }
        return scope.createMethod(methodDecl);
    }

    @Override
    SourceTypeBinding buildType(SourceTypeBinding enclosingType, PackageBinding packageBinding, AccessRestriction accessRestriction) {
        // provide the typeDeclaration with needed scopes
        this.referenceContext.scope = this;
        this.referenceContext.staticInitializerScope = new MethodScope(this, this.referenceContext, true);
        this.referenceContext.initializerScope = new MethodScope(this, this.referenceContext, false);

        adsContext().attachInnerDeclarations(this);

        if (enclosingType == null) {
            //char[][] className = CharOperation.arrayConcat(packageBinding.compoundName, this.referenceContext.name);
            this.referenceContext.binding = adsContext().createBinding(packageBinding, this, null);
        } else {
            char[][] className = CharOperation.deepCopy(enclosingType.compoundName);
            className[className.length - 1] =
                    CharOperation.concat(className[className.length - 1], this.referenceContext.name, '$');
            ReferenceBinding existingType = packageBinding.getType0(className[className.length - 1]);
            if (existingType != null) {
                if (existingType instanceof UnresolvedReferenceBinding) {
                    // its possible that a BinaryType referenced the member type before its enclosing source type was built
                    // so just replace the unresolved type with a new member type
                } else {
                    // report the error against the parent - its still safe to answer the member type
                    this.parent.problemReporter().duplicateNestedType(this.referenceContext);
                }
            }

            this.referenceContext.binding = adsContext().createBinding(packageBinding, this, enclosingType);

        }

        SourceTypeBinding sourceType = this.referenceContext.binding;
        environment().setAccessRestriction(sourceType, accessRestriction);

        sourceType.fPackage.addType(sourceType);

        checkAndSetModifiers();
        buildTypeVariables();
        buildMemberTypes(accessRestriction);
        return sourceType;
    }

    private void buildTypeVariables() {
        SourceTypeBinding sourceType = this.referenceContext.binding;
        TypeParameter[] typeParameters = this.referenceContext.typeParameters;
        // https://bugs.eclipse.org/bugs/show_bug.cgi?id=324850, If they exist at all, process type parameters irrespective of source level.
        if (typeParameters == null || typeParameters.length == 0) {
            sourceType.typeVariables = Binding.NO_TYPE_VARIABLES;
            return;
        }
        sourceType.typeVariables = Binding.NO_TYPE_VARIABLES; // safety

        if (sourceType.id == TypeIds.T_JavaLangObject) { // handle the case of redefining java.lang.Object up front
            problemReporter().objectCannotBeGeneric(this.referenceContext);
            return;
        }
        sourceType.typeVariables = createTypeVariables(typeParameters, sourceType);
        sourceType.modifiers |= ExtraCompilerModifiers.AccGenericSignature;
    }

    private void checkAndSetModifiers() {
        SourceTypeBinding sourceType = this.referenceContext.binding;
        int modifiers = sourceType.modifiers;
        if ((modifiers & ExtraCompilerModifiers.AccAlternateModifierProblem) != 0) {
            problemReporter().duplicateModifierForType(sourceType);
        }
        ReferenceBinding enclosingType = sourceType.enclosingType();
        boolean isMemberType = sourceType.isMemberType();
        if (isMemberType) {
            modifiers |= (enclosingType.modifiers & (ExtraCompilerModifiers.AccGenericSignature | ClassFileConstants.AccStrictfp));
            // checks for member types before local types to catch local members
            if (enclosingType.isInterface()) {
                modifiers |= ClassFileConstants.AccPublic;
            }
            if (sourceType.isEnum()) {
                if (!enclosingType.isStatic()) {
                    problemReporter().nonStaticContextForEnumMemberType(sourceType);
                } else {
                    modifiers |= ClassFileConstants.AccStatic;
                }
            }
        } else if (sourceType.isLocalType()) {
            if (sourceType.isEnum()) {
                problemReporter().illegalLocalTypeDeclaration(this.referenceContext);
                sourceType.modifiers = 0;
                return;
            }
            if (sourceType.isAnonymousType()) {
                modifiers |= ClassFileConstants.AccFinal;
                // set AccEnum flag for anonymous body of enum constants
                if (this.referenceContext.allocation.type == null) {
                    modifiers |= ClassFileConstants.AccEnum;
                }
            }
            Scope scope = this;
            do {
                switch (scope.kind) {
                    case METHOD_SCOPE:
                        MethodScope methodScope = (MethodScope) scope;
                        if (methodScope.isInsideInitializer()) {
                            SourceTypeBinding type = ((TypeDeclaration) methodScope.referenceContext).binding;

                            // inside field declaration ? check field modifier to see if deprecated
                            if (methodScope.initializedField != null) {
                                // currently inside this field initialization
                                if (methodScope.initializedField.isViewedAsDeprecated() && !sourceType.isDeprecated()) {
                                    modifiers |= ExtraCompilerModifiers.AccDeprecatedImplicitly;
                                }
                            } else {
                                if (type.isStrictfp()) {
                                    modifiers |= ClassFileConstants.AccStrictfp;
                                }
                                if (type.isViewedAsDeprecated() && !sourceType.isDeprecated()) {
                                    modifiers |= ExtraCompilerModifiers.AccDeprecatedImplicitly;
                                }
                            }
                        } else {
                            MethodBinding method = ((AbstractMethodDeclaration) methodScope.referenceContext).binding;
                            if (method != null) {
                                if (method.isStrictfp()) {
                                    modifiers |= ClassFileConstants.AccStrictfp;
                                }
                                if (method.isViewedAsDeprecated() && !sourceType.isDeprecated()) {
                                    modifiers |= ExtraCompilerModifiers.AccDeprecatedImplicitly;
                                }
                            }
                        }
                        break;
                    case CLASS_SCOPE:
                        // local member
                        if (enclosingType.isStrictfp()) {
                            modifiers |= ClassFileConstants.AccStrictfp;
                        }
                        if (enclosingType.isViewedAsDeprecated() && !sourceType.isDeprecated()) {
                            modifiers |= ExtraCompilerModifiers.AccDeprecatedImplicitly;
                        }
                        break;
                }
                scope = scope.parent;
            } while (scope != null);
        }

        // after this point, tests on the 16 bits reserved.
        int realModifiers = modifiers & ExtraCompilerModifiers.AccJustFlag;

        if ((realModifiers & ClassFileConstants.AccInterface) != 0) { // interface and annotation type
            // detect abnormal cases for interfaces
            if (isMemberType) {
                final int UNEXPECTED_MODIFIERS =
                        ~(ClassFileConstants.AccPublic | ClassFileConstants.AccPrivate | ClassFileConstants.AccProtected | ClassFileConstants.AccStatic | ClassFileConstants.AccAbstract | ClassFileConstants.AccInterface | ClassFileConstants.AccStrictfp | ClassFileConstants.AccAnnotation);
                if ((realModifiers & UNEXPECTED_MODIFIERS) != 0) {
                    if ((realModifiers & ClassFileConstants.AccAnnotation) != 0) {
                        problemReporter().illegalModifierForAnnotationMemberType(sourceType);
                    } else {
                        problemReporter().illegalModifierForMemberInterface(sourceType);
                    }
                }
                /*
                 } else if (sourceType.isLocalType()) { //interfaces cannot be defined inside a method
                 int unexpectedModifiers = ~(AccAbstract | AccInterface | AccStrictfp);
                 if ((realModifiers & unexpectedModifiers) != 0)
                 problemReporter().illegalModifierForLocalInterface(sourceType);
                 */
            } else {
                final int UNEXPECTED_MODIFIERS = ~(ClassFileConstants.AccPublic | ClassFileConstants.AccAbstract | ClassFileConstants.AccInterface | ClassFileConstants.AccStrictfp | ClassFileConstants.AccAnnotation);
                if ((realModifiers & UNEXPECTED_MODIFIERS) != 0) {
                    if ((realModifiers & ClassFileConstants.AccAnnotation) != 0) {
                        problemReporter().illegalModifierForAnnotationType(sourceType);
                    } else {
                        problemReporter().illegalModifierForInterface(sourceType);
                    }
                }
            }
            /*
             * AccSynthetic must be set if the target is greater than 1.5. 1.5 VM don't support AccSynthetics flag.
             */
            if (sourceType.sourceName == TypeConstants.PACKAGE_INFO_NAME && compilerOptions().targetJDK > ClassFileConstants.JDK1_5) {
                modifiers |= ClassFileConstants.AccSynthetic;
            }
            modifiers |= ClassFileConstants.AccAbstract;
        } else if ((realModifiers & ClassFileConstants.AccEnum) != 0) {
            // detect abnormal cases for enums
            if (isMemberType) { // includes member types defined inside local types
                final int UNEXPECTED_MODIFIERS = ~(ClassFileConstants.AccPublic | ClassFileConstants.AccPrivate | ClassFileConstants.AccProtected | ClassFileConstants.AccStatic | ClassFileConstants.AccStrictfp | ClassFileConstants.AccEnum);
                if ((realModifiers & UNEXPECTED_MODIFIERS) != 0) {
                    problemReporter().illegalModifierForMemberEnum(sourceType);
                    modifiers &= ~ClassFileConstants.AccAbstract; // avoid leaking abstract modifier
                    realModifiers &= ~ClassFileConstants.AccAbstract;
//					modifiers &= ~(realModifiers & UNEXPECTED_MODIFIERS);
//					realModifiers = modifiers & ExtraCompilerModifiers.AccJustFlag;
                }
            } else if (sourceType.isLocalType()) {
                // each enum constant is an anonymous local type and its modifiers were already checked as an enum constant field
            } else {
                final int UNEXPECTED_MODIFIERS = ~(ClassFileConstants.AccPublic | ClassFileConstants.AccStrictfp | ClassFileConstants.AccEnum);
                if ((realModifiers & UNEXPECTED_MODIFIERS) != 0) {
                    problemReporter().illegalModifierForEnum(sourceType);
                }
            }
            if (!sourceType.isAnonymousType()) {
                checkAbstractEnum:
                {
                    // does define abstract methods ?
                    if ((this.referenceContext.bits & ASTNode.HasAbstractMethods) != 0) {
                        modifiers |= ClassFileConstants.AccAbstract;
                        break checkAbstractEnum;
                    }
                    // body of enum constant must implement any inherited abstract methods
                    // enum type needs to implement abstract methods if one of its constants does not supply a body
                    TypeDeclaration typeDeclaration = this.referenceContext;
                    FieldDeclaration[] fields = typeDeclaration.fields;
                    int fieldsLength = fields == null ? 0 : fields.length;
                    if (fieldsLength == 0) {
                        break checkAbstractEnum; // has no constants so must implement the method itself
                    }
                    AbstractMethodDeclaration[] methods = typeDeclaration.methods;
                    int methodsLength = methods == null ? 0 : methods.length;
                    // TODO (kent) cannot tell that the superinterfaces are empty or that their methods are implemented
                    boolean definesAbstractMethod = typeDeclaration.superInterfaces != null;
                    for (int i = 0; i < methodsLength && !definesAbstractMethod; i++) {
                        definesAbstractMethod = methods[i].isAbstract();
                    }
                    if (!definesAbstractMethod) {
                        break checkAbstractEnum; // all methods have bodies
                    }
                    boolean needAbstractBit = false;
                    for (int i = 0; i < fieldsLength; i++) {
                        FieldDeclaration fieldDecl = fields[i];
                        if (fieldDecl.getKind() == AbstractVariableDeclaration.ENUM_CONSTANT) {
                            if (fieldDecl.initialization instanceof QualifiedAllocationExpression) {
                                needAbstractBit = true;
                            } else {
                                break checkAbstractEnum;
                            }
                        }
                    }
                    // tag this enum as abstract since an abstract method must be implemented AND all enum constants define an anonymous body
                    // as a result, each of its anonymous constants will see it as abstract and must implement each inherited abstract method
                    if (needAbstractBit) {
                        modifiers |= ClassFileConstants.AccAbstract;
                    }
                }
                // final if no enum constant with anonymous body
                checkFinalEnum:
                {
                    TypeDeclaration typeDeclaration = this.referenceContext;
                    FieldDeclaration[] fields = typeDeclaration.fields;
                    if (fields != null) {
                        for (int i = 0, fieldsLength = fields.length; i < fieldsLength; i++) {
                            FieldDeclaration fieldDecl = fields[i];
                            if (fieldDecl.getKind() == AbstractVariableDeclaration.ENUM_CONSTANT) {
                                if (fieldDecl.initialization instanceof QualifiedAllocationExpression) {
                                    break checkFinalEnum;
                                }
                            }
                        }
                    }
                    modifiers |= ClassFileConstants.AccFinal;
                }
            }
        } else {
            // detect abnormal cases for classes
            if (isMemberType) { // includes member types defined inside local types
                final int UNEXPECTED_MODIFIERS = ~(ClassFileConstants.AccPublic | ClassFileConstants.AccPrivate | ClassFileConstants.AccProtected | ClassFileConstants.AccStatic | ClassFileConstants.AccAbstract | ClassFileConstants.AccFinal | ClassFileConstants.AccStrictfp);
                if ((realModifiers & UNEXPECTED_MODIFIERS) != 0) {
                    problemReporter().illegalModifierForMemberClass(sourceType);
                }
            } else if (sourceType.isLocalType()) {
                final int UNEXPECTED_MODIFIERS = ~(ClassFileConstants.AccAbstract | ClassFileConstants.AccFinal | ClassFileConstants.AccStrictfp);
                if ((realModifiers & UNEXPECTED_MODIFIERS) != 0) {
                    problemReporter().illegalModifierForLocalClass(sourceType);
                }
            } else {
                final int UNEXPECTED_MODIFIERS = ~(ClassFileConstants.AccPublic | ClassFileConstants.AccAbstract | ClassFileConstants.AccFinal | ClassFileConstants.AccStrictfp);
                if ((realModifiers & UNEXPECTED_MODIFIERS) != 0) {
                    problemReporter().illegalModifierForClass(sourceType);
                }
            }

            // check that Final and Abstract are not set together
            if ((realModifiers & (ClassFileConstants.AccFinal | ClassFileConstants.AccAbstract)) == (ClassFileConstants.AccFinal | ClassFileConstants.AccAbstract)) {
                problemReporter().illegalModifierCombinationFinalAbstractForClass(sourceType);
            }
        }

        if (isMemberType) {
            // test visibility modifiers inconsistency, isolate the accessors bits
            if (enclosingType.isInterface()) {
                if ((realModifiers & (ClassFileConstants.AccProtected | ClassFileConstants.AccPrivate)) != 0) {
                    problemReporter().illegalVisibilityModifierForInterfaceMemberType(sourceType);

                    // need to keep the less restrictive
                    if ((realModifiers & ClassFileConstants.AccProtected) != 0) {
                        modifiers &= ~ClassFileConstants.AccProtected;
                    }
                    if ((realModifiers & ClassFileConstants.AccPrivate) != 0) {
                        modifiers &= ~ClassFileConstants.AccPrivate;
                    }
                }
            } else {
                int accessorBits = realModifiers & (ClassFileConstants.AccPublic | ClassFileConstants.AccProtected | ClassFileConstants.AccPrivate);
                if ((accessorBits & (accessorBits - 1)) > 1) {
                    problemReporter().illegalVisibilityModifierCombinationForMemberType(sourceType);

                    // need to keep the less restrictive so disable Protected/Private as necessary
                    if ((accessorBits & ClassFileConstants.AccPublic) != 0) {
                        if ((accessorBits & ClassFileConstants.AccProtected) != 0) {
                            modifiers &= ~ClassFileConstants.AccProtected;
                        }
                        if ((accessorBits & ClassFileConstants.AccPrivate) != 0) {
                            modifiers &= ~ClassFileConstants.AccPrivate;
                        }
                    } else if ((accessorBits & ClassFileConstants.AccProtected) != 0 && (accessorBits & ClassFileConstants.AccPrivate) != 0) {
                        modifiers &= ~ClassFileConstants.AccPrivate;
                    }
                }
            }

            // static modifier test
            if ((realModifiers & ClassFileConstants.AccStatic) == 0) {
                if (enclosingType.isInterface()) {
                    modifiers |= ClassFileConstants.AccStatic;
                }
            } else if (!enclosingType.isStatic()) {
                // error the enclosing type of a static field must be static or a top-level type
                problemReporter().illegalStaticModifierForMemberType(sourceType);
            }
        }

        sourceType.modifiers = modifiers;

    }

    private void buildMemberTypes(AccessRestriction accessRestriction) {
        SourceTypeBinding sourceType = this.referenceContext.binding;
        ReferenceBinding[] memberTypeBindings = Binding.NO_MEMBER_TYPES;
        if (this.referenceContext.memberTypes != null) {
            int length = this.referenceContext.memberTypes.length;
            memberTypeBindings = new ReferenceBinding[length];
            int count = 0;
            nextMember:
            for (int i = 0; i < length; i++) {
                TypeDeclaration memberContext = this.referenceContext.memberTypes[i];
                if (!(memberContext instanceof AdsTypeDeclaration)) {
                    this.referenceContext.memberTypes[i] = memberContext = new AdsSourceTypeDeclaration(adsContext(), memberContext);
                }

                switch (TypeDeclaration.kind(memberContext.modifiers)) {
                    case TypeDeclaration.INTERFACE_DECL:
                    case TypeDeclaration.ANNOTATION_TYPE_DECL:
                        if (sourceType.isNestedType()
                                && sourceType.isClass() // no need to check for enum, since implicitly static
                                && !sourceType.isStatic()) {
                            problemReporter().illegalLocalTypeDeclaration(memberContext);
                            continue nextMember;
                        }
                        break;
                }
                ReferenceBinding type = sourceType;
                // check that the member does not conflict with an enclosing type
                do {
                    if (CharOperation.equals(type.sourceName, memberContext.name)) {
                        problemReporter().typeCollidesWithEnclosingType(memberContext);
                        continue nextMember;
                    }
                    type = type.enclosingType();
                } while (type != null);
                // check that the member type does not conflict with another sibling member type
                for (int j = 0; j < i; j++) {
                    if (CharOperation.equals(this.referenceContext.memberTypes[j].name, memberContext.name)) {
                        problemReporter().duplicateNestedType(memberContext);
                        continue nextMember;
                    }
                }
                ClassScope memberScope;

                memberScope = new AdsDefinitionScope((Scope) this, (AdsTypeDeclaration) memberContext);


                memberTypeBindings[count++] = memberScope.buildType(sourceType, sourceType.fPackage, accessRestriction);
            }
            if (count != length) {
                System.arraycopy(memberTypeBindings, 0, memberTypeBindings = new ReferenceBinding[count], 0, count);
            }
        }
        sourceType.memberTypes = memberTypeBindings;
    }

    @Override
    void buildLocalTypeBinding(SourceTypeBinding enclosingType) {

        LocalTypeBinding localType = buildLocalType(enclosingType, enclosingType.fPackage);
        connectTypeHierarchy();
        if (compilerOptions().sourceLevel >= ClassFileConstants.JDK1_5) {
            checkParameterizedTypeBounds();
            checkParameterizedSuperTypeCollisions();
        }
        buildFieldsAndMethods();
        localType.faultInTypesForFieldsAndMethods();

        this.referenceContext.binding.verifyMethods(environment().methodVerifier());
    }

    private LocalTypeBinding buildLocalType(SourceTypeBinding enclosingType, PackageBinding packageBinding) {

        this.referenceContext.scope = this;
        this.referenceContext.staticInitializerScope = new MethodScope(this, this.referenceContext, true);
        this.referenceContext.initializerScope = new MethodScope(this, this.referenceContext, false);

        // build the binding or the local type
        LocalTypeBinding localType = new LocalTypeBinding(this, enclosingType, innermostSwitchCase());
        this.referenceContext.binding = localType;
        checkAndSetModifiers();
        buildTypeVariables();

        // Look at member types
        ReferenceBinding[] memberTypeBindings = Binding.NO_MEMBER_TYPES;
        if (this.referenceContext.memberTypes != null) {
            int size = this.referenceContext.memberTypes.length;
            memberTypeBindings = new ReferenceBinding[size];
            int count = 0;
            nextMember:
            for (int i = 0; i < size; i++) {
                TypeDeclaration memberContext = this.referenceContext.memberTypes[i];
                switch (TypeDeclaration.kind(memberContext.modifiers)) {
                    case TypeDeclaration.INTERFACE_DECL:
                    case TypeDeclaration.ANNOTATION_TYPE_DECL:
                        problemReporter().illegalLocalTypeDeclaration(memberContext);
                        continue nextMember;
                }
                ReferenceBinding type = localType;
                // check that the member does not conflict with an enclosing type
                do {
                    if (CharOperation.equals(type.sourceName, memberContext.name)) {
                        problemReporter().typeCollidesWithEnclosingType(memberContext);
                        continue nextMember;
                    }
                    type = type.enclosingType();
                } while (type != null);
                // check the member type does not conflict with another sibling member type
                for (int j = 0; j < i; j++) {
                    if (CharOperation.equals(this.referenceContext.memberTypes[j].name, memberContext.name)) {
                        problemReporter().duplicateNestedType(memberContext);
                        continue nextMember;
                    }
                }
                AdsDefinitionScope memberScope = new AdsDefinitionScope(this, this.referenceContext.memberTypes[i]);
                LocalTypeBinding memberBinding = memberScope.buildLocalType(localType, packageBinding);
                memberBinding.setAsMemberType();
                memberTypeBindings[count++] = memberBinding;
            }
            if (count != size) {
                System.arraycopy(memberTypeBindings, 0, memberTypeBindings = new ReferenceBinding[count], 0, count);
            }
        }
        localType.memberTypes = memberTypeBindings;
        return localType;
    }

    @Override
    public TypeBinding getType(Definition referenceContext, org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration adsType) {
        AdsType type = adsType.resolve(referenceContext.getDefinition()).get();

        if (type != null) {
            return getType(referenceContext.getDefinition(), type, adsType);
        }
        return null;
    }

    public ERuntimeEnvironmentType getEnvironmentType() {
        return ((AdsCompilationUnitScope) compilationUnitScope()).getEnvType();
    }
    /*
     private TypeBinding getType(char[][] compoundName) {

     TypeBinding result = compilationUnitScope().environment().getType(compoundName);

     if (result == null) {//try nested type lookup
     int count = compoundName.length - 1;
     while (count > 0) {
     char[][] cn = new char[count][];
     for (int i = 0; i < count; i++) {
     cn[i] = compoundName[i];
     }
     result = compilationUnitScope().environment().getType(cn);
     if (result instanceof ReferenceBinding) {
     ReferenceBinding ref = (ReferenceBinding) result;

     for (int n = count; n < compoundName.length; n++) {
     ref = ref.getMemberType(compoundName[n]);
     if (ref == null) {
     break;
     }
     }
     result = ref;

     }

     if (result != null) {
     break;
     }
     count--;
     }
     }
     return result;
     }*/

    @Override
    public TypeBinding getType(Definition referencedDefinition, boolean meta) {
        TypeBinding result = null;
        if (referencedDefinition instanceof AdsClassDef) {
            AdsClassDef clazz = (AdsClassDef) referencedDefinition;
            if (clazz.getTransparence() != null && clazz.getTransparence().isTransparent() && !clazz.getTransparence().isExtendable()) {//return platform class instead
                char[][] compoundName = CharOperation.splitOn('.', clazz.getTransparence().getPublishedName().toCharArray());
                result = getType(compoundName);
            } else {
                result = ((AdsCompilationUnitScope) compilationUnitScope()).findType(clazz, meta);
            }
        } else if (referencedDefinition instanceof AdsEnumDef) {
            AdsEnumDef clazz = (AdsEnumDef) referencedDefinition;
            if (clazz.isPlatformEnumPublisher() && !clazz.isExtendable()) {//return platform class instead
                char[][] compoundName = CharOperation.splitOn('.', clazz.getPublishedPlatformEnumName().toCharArray());
                result = getType(compoundName);
            } else {
                result = ((AdsCompilationUnitScope) compilationUnitScope()).findType(clazz, meta);
            }
        } else if (referencedDefinition instanceof AdsPropertyDef) {
            TypeBinding binding = getType(((AdsPropertyDef) referencedDefinition).getOwnerClass(), meta);
            if (binding instanceof AdsClassTypeBinding) {
                result = ((AdsClassTypeBinding) binding).findMember((AdsPropertyDef) referencedDefinition);
            }
        } else if (referencedDefinition instanceof AdsDefinition) {
            result = ((AdsCompilationUnitScope) compilationUnitScope()).findType((AdsDefinition) referencedDefinition, meta);
        }

        if (result == null) {
            result = new ProblemReferenceBinding(getCompoundNameForType(referencedDefinition), null, ProblemReasons.NotFound);
        }
        return result;
    }

    private TypeBinding getType(Definition referenceContext, AdsType type, org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration hint) {
        TypeBinding result = null;
        if (type instanceof AdsClassType) {
            AdsClassDef clazz = ((AdsClassType) type).getSource();
            if (clazz != null) {
                result = getType(clazz, false);
            }
        } else if (type instanceof RadixType) {
            if (AdsLookup.RADIX_TYPE_IDs.contains(((RadixType) type).getTypeId())) {
                result = compilationUnitScope().environment().getType(
                        CharOperation.arrayConcat(AdsLookup.RADIX_TYPES_PACKAGE, type.getName().toCharArray()));
            } else {
                result = compilationUnitScope().environment().getType(
                        CharOperation.splitOn('.',
                        type.getFullJavaClassName(JavaSourceSupport.UsagePurpose.getPurpose(getEnvironmentType(), JavaSourceSupport.CodeType.EXCUTABLE))));
            }
        } else if (type instanceof JavaClassType) {
            result = getType(
                    CharOperation.splitOn('.',
                    type.getFullJavaClassName(JavaSourceSupport.UsagePurpose.getPurpose(getEnvironmentType(), JavaSourceSupport.CodeType.EXCUTABLE))));


        } else if (type instanceof XmlType) {
            XmlType xml = (XmlType) type;
            AdsDefinition definition = null;
            if (xml.getSource() instanceof AdsXmlSchemeDef) {
                AdsXmlSchemeDef adsdef = (AdsXmlSchemeDef) xml.getSource();
                if (!adsdef.isTransparent()) {
                    definition = adsdef;
                }
            }
            if (definition != null) {
                String suffix = xml.getSuffix();
                if (suffix != null) {
                    String[] names = suffix.split("\\.");
                    result = ((AdsCompilationUnitScope) compilationUnitScope()).findType(definition, names[0]);
                    if (result instanceof ReferenceBinding && result.isValidBinding()) {
                        ReferenceBinding reference = (ReferenceBinding) result;
                        ReferenceBinding init = reference;
                        for (int i = 1; i < names.length; i++) {
                            reference = reference.getMemberType(names[i].toCharArray());
                            if (reference == null || !reference.isValidBinding()) {
                                if (reference == null) {
                                    char[][] qname = new char[init.fPackage.compoundName.length + names.length][];
                                    for (int n = 0; n < reference.fPackage.compoundName.length; n++) {
                                        qname[n] = reference.fPackage.compoundName[n];
                                    }
                                    for (int n = reference.fPackage.compoundName.length, t = 0; t < names.length; n++, t++) {
                                        qname[t] = names[n].toCharArray();
                                    }
                                    return new ProblemReferenceBinding(qname, init, ProblemReasons.NotFound);
                                }
                            }
                        }
                        return reference;
                    } else {
                        return result;
                    }
                } else {
                    result = ((AdsCompilationUnitScope) compilationUnitScope()).findType(definition, null);
                }
            } else {
                result = getType(
                        CharOperation.splitOn('.',
                        type.getFullJavaClassName(JavaSourceSupport.UsagePurpose.getPurpose(getEnvironmentType(), JavaSourceSupport.CodeType.EXCUTABLE))));
            }

        } else if (type instanceof AdsEnumType) {
            result = getType(((AdsEnumType) type).getSource(), false);
        }
        if (result == null) {
            result = new ProblemReferenceBinding(getCompoundNameForType(referenceContext, type), null, ProblemReasons.NotFound);

        }
        return result;
    }

    private char[][] getCompoundNameForType(Definition definition) {
        return new char[][]{("`" + definition.getQualifiedName() + "`").toCharArray()};
    }

    private char[][] getCompoundNameForType(Definition rc, AdsType definition) {
        return new char[][]{("`" + definition.getQualifiedName(rc) + "`").toCharArray()};
    }

    private TypeBinding getType(char[][] compoundName) {

        Binding binding = getPackage(compoundName);
        if (!(binding instanceof PackageBinding)) {
            return null;
        }

        PackageBinding packageBinding = (PackageBinding) binding;
        int start = packageBinding.compoundName.length;
        TypeBinding localType = packageBinding.getType(compoundName[start]);
        if (localType != null) {
            for (int i = start + 1; i < compoundName.length; i++) {
                if (localType instanceof ReferenceBinding) {
                    TypeBinding result = ((ReferenceBinding) localType).getMemberType(compoundName[i]);
                    localType = result;
                } else {
                    return null;
                }
            }
        }
        return localType;
    }

    public void buildAnonymousType(SourceTypeBinding enclosingType, ReferenceBinding supertype) {
        buildAnonymousTypeBinding(enclosingType, supertype);
    }
}
