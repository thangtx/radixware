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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.internal.compiler.ClassFile;
import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.ads.syntetics.AdsDefinitionHelper;
import org.eclipse.jdt.internal.compiler.ads.syntetics.BaseGenerator;
import org.eclipse.jdt.internal.compiler.ads.syntetics.PropertyGeneratorFlowInfo;
import org.eclipse.jdt.internal.compiler.ads.syntetics.ReportClassGenerator;
import org.eclipse.jdt.internal.compiler.ads.syntetics.SqlClassGenerator;
import org.eclipse.jdt.internal.compiler.ast.ASTNode;
import org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.AdsPropertyAccessorDeclaration;
import org.eclipse.jdt.internal.compiler.ast.FieldDeclaration;
import org.eclipse.jdt.internal.compiler.ast.MethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.NullLiteral;
import org.eclipse.jdt.internal.compiler.ast.QualifiedAllocationExpression;
import org.eclipse.jdt.internal.compiler.ast.QualifiedTypeReference;
import org.eclipse.jdt.internal.compiler.ast.ReturnStatement;
import org.eclipse.jdt.internal.compiler.ast.Statement;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.eclipse.jdt.internal.compiler.ast.TypeReference;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.problem.AbortType;
import org.eclipse.jdt.internal.compiler.util.Util;
import org.radixware.kernel.common.compiler.core.JMLClassFile;
import org.radixware.kernel.common.compiler.core.ast.RadixObjectLocator;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.compiler.core.problems.AdsProblemReporter;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsSqlClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.types.Id;


public abstract class AdsTypeDeclaration extends TypeDeclaration {

    public Definition definition;
    protected ERuntimeEnvironmentType env;
    AdsPropertyDeclaration[] properties = null;
    private Map<Id, AbstractMethodDeclaration> idAccessors = new HashMap<>();
    protected final RadixObjectLocator locator;

    public AdsTypeDeclaration(AdsCompilationUnitScope scope, Definition definition, CompilationResult compilationResult, RadixObjectLocator locator) {
        this(scope, definition, null, compilationResult, locator);
    }

    public AdsTypeDeclaration(AdsTypeDeclaration enclosingType) {
        this((AdsCompilationUnitScope) enclosingType.scope.compilationUnitScope(), enclosingType.definition, enclosingType.compilationResult, enclosingType.locator);
        this.enclosingType = enclosingType;
    }
    public AdsTypeDeclaration(AdsCompilationUnitScope compilationUnitScope,AdsTypeDeclaration enclosingType) {
        this(compilationUnitScope, enclosingType.definition, enclosingType.compilationResult, enclosingType.locator);
        this.enclosingType = enclosingType;
    }

    public AdsTypeDeclaration(QualifiedAllocationExpression allocation, Scope scope) {
        this((AdsCompilationUnitScope) scope.compilationUnitScope(), null, scope.referenceContext().compilationResult(), ((AdsTypeDeclaration) scope.classScope().referenceContext()).locator);
        this.allocation = allocation;
    }

    public AdsTypeDeclaration(AdsCompilationUnitScope scope, Definition definition, Object flowInfo, CompilationResult compilationResult, RadixObjectLocator locator) {
        this(scope, definition, flowInfo, compilationResult, locator, false);
    }

    public AdsTypeDeclaration(AdsCompilationUnitScope scope, Definition definition, Object flowInfo, CompilationResult compilationResult, RadixObjectLocator locator, boolean initFeatures) {
        super(compilationResult);
        this.locator = locator;
        this.definition = definition;
        this.env = scope.getEnvType();
        if (definition != null) {
            this.name = definition.getId().toCharArray();
        }
        if (initFeatures) {
            this.properties = initializeProperties();

            InitInfo initInfo = initializeMembers(definition, scope, flowInfo, (AdsProblemReporter) scope.problemReporter());
            if (initInfo != null) {
                this.methods = initInfo.methods;
                this.fields = initInfo.fields;
                this.memberTypes = initInfo.innerTypes;
            }
            this.superInterfaces = new TypeReference[0];
        }
        finalizeInitialization(scope, flowInfo);
        boolean hasConstructors = false;


        if (methods != null) {

            for (AbstractMethodDeclaration method : methods) {
                if (method.isConstructor()) {
                    hasConstructors = true;
                    break;
                }
            }
        }
        if (!hasConstructors && !(this instanceof AdsXmlTypeDeclaration)) {
            createDefaultConstructor(true, true);
        }
    }

    /**
     * last chance to do something more complex than simple member creation
     */
    protected void finalizeInitialization(Scope scope, Object flowInfo) {
    }

    protected void attachInnerDeclarations(Scope scope) {
    }

    protected void attachOuterDeclarations(AdsCompilationUnitDeclaration cu, Scope scope) {
    }

    protected AdsClassDef lookForClassDefinition() {
        return null;
    }

    protected AdsPropertyDeclaration[] initializeProperties() {
        List<AdsPropertyDeclaration> propsDecls = new LinkedList<>();
        AdsClassDef clazz = lookForClassDefinition();
        if (clazz != null) {
            List<AdsPropertyDef> props = clazz.getProperties().get(ExtendableDefinitions.EScope.LOCAL_AND_OVERWRITE);
            for (AdsPropertyDef prop : props) {
                propsDecls.add(new AdsPropertyDeclaration(prop, env));
            }
        }
        if (definition != null) {
            initializeOtherDeclarations(definition, propsDecls);
        }
        return propsDecls.toArray(new AdsPropertyDeclaration[propsDecls.size()]);
    }

    protected void initializeOtherDeclarations(Definition definition, List<AdsPropertyDeclaration> proplist) {
    }

    protected class InitInfo {

        AbstractMethodDeclaration[] methods;
        TypeDeclaration[] innerTypes;
        FieldDeclaration[] fields;

        public InitInfo(AbstractMethodDeclaration[] methods, FieldDeclaration[] fields, TypeDeclaration[] innerTypes) {
            this.methods = methods;
            this.innerTypes = innerTypes;
            this.fields = fields;
        }
    }

    protected final InitInfo initializeMembers(Definition compilable, CompilationUnitScope scope, Object flowInfo, AdsProblemReporter reporter) {
        List<AbstractMethodDeclaration> methodDecls = new LinkedList<>();
        List<TypeDeclaration> innerTypes = new LinkedList<>();
        List<FieldDeclaration> fieldsDeclarations = new LinkedList<>();
        AdsClassDef clazz = lookForClassDefinition();
        if (clazz != null) {
            List<AdsMethodDef> adsMethods = clazz.getMethods().get(ExtendableDefinitions.EScope.LOCAL_AND_OVERWRITE);
            for (AdsMethodDef method : adsMethods) {
                AbstractMethodDeclaration decl;
                if (method.isConstructor()) {
                    decl = new AdsConstructorDeclaration(clazz, method, env, compilationResult, locator);
                } else {
                    decl = new AdsMethodDeclaration(clazz, method, env, compilationResult, locator);
                }

                methodDecls.add(decl);
            }
        }
        if (compilable instanceof Definition) {
            final Definition definition = (Definition) compilable;
            initializeOtherMemberTypesAndMethods(definition, scope, flowInfo, methodDecls, fieldsDeclarations, innerTypes);
            if (this.properties != null) {
                for (AdsPropertyDeclaration decl : this.properties) {
                    AdsPropertyDef prop = decl.getProperty();
                    if (prop == null) {
                        continue;
                    }
                    final PropertyGeneratorFlowInfo getterFlow = new PropertyGeneratorFlowInfo(prop, true);
                    AdsPropertyAccessorDeclaration getter = new AdsPropertyAccessorDeclaration(definition, decl, compilationResult, getterFlow);
                    methodDecls.add(getter);
                    MethodDeclaration hiddenGetter = getter.getHiddenAccessorDeclaration();
                    if (hiddenGetter != null) {
                        methodDecls.add(hiddenGetter);
                    }
                    if (!prop.isConst()) {
                        AdsPropertyAccessorDeclaration setter = new AdsPropertyAccessorDeclaration(definition, decl, compilationResult, new PropertyGeneratorFlowInfo(getterFlow, false));
                        methodDecls.add(setter);
                        MethodDeclaration hiddenSetter = setter.getHiddenAccessorDeclaration();
                        if (hiddenSetter != null) {
                            methodDecls.add(hiddenSetter);
                        }
                    }
                }
            }
        }
        return new InitInfo(methodDecls.toArray(new AbstractMethodDeclaration[methodDecls.size()]), fieldsDeclarations.toArray(new FieldDeclaration[fieldsDeclarations.size()]), innerTypes.toArray(new TypeDeclaration[innerTypes.size()]));
    }

    protected void initializeOtherMemberTypesAndMethods(Definition definition, CompilationUnitScope scope, Object flowInfo, List<AbstractMethodDeclaration> methods, List<FieldDeclaration> fields, List<TypeDeclaration> innerTypes) {
        AdsDefinitionHelper.generateAdditionalMethods(definition, this, env, methods);
        if (definition instanceof AdsReportClassDef) {
            new ReportClassGenerator(compilationResult, this).addSpecificInnerTypesAndMethods((AdsReportClassDef) definition, scope, innerTypes, methods, fields);
        } else if (definition instanceof AdsSqlClassDef) {
            new SqlClassGenerator(compilationResult, this).addSpecificInnerTypesAndMethods((AdsSqlClassDef) definition, scope, innerTypes, methods, fields);
        }
    }

    protected boolean isMetaType() {
        return false;
    }

    public AdsMethodDeclaration findMethod(AdsMethodDef method) {
        if (methods == null) {
            return null;
        } else {
            for (int i = 0; i < methods.length; i++) {
                if (methods[i] instanceof AdsMethodDeclaration) {
                    AdsMethodDeclaration decl = (AdsMethodDeclaration) methods[i];
                    if (decl.getMethod() == method) {
                        return decl;
                    }
                }
            }
            return null;
        }
    }

    void movePropsToFields() {
        if (this.properties != null && this.properties.length > 0) {
            int size = this.properties.length;
            List<FieldDeclaration> backstores = new LinkedList<>();

            size += backstores.size();
            if (this.fields != null) {
                size += this.fields.length;
            }
            FieldDeclaration[] newFields = new FieldDeclaration[size];

            for (int i = 0; i < backstores.size(); i++) {
                newFields[i] = backstores.get(i);
            }

            if (this.fields != null) {
                System.arraycopy(this.fields, 0, newFields, backstores.size(), this.fields.length);
                System.arraycopy(this.properties, 0, newFields, backstores.size() + this.fields.length, this.properties.length);
            } else {
                System.arraycopy(this.properties, 0, newFields, backstores.size(), this.properties.length);
            }
            this.fields = newFields;
            this.properties = null;
        }
    }

    public Definition getDefinition() {
        return definition;
    }

    char[][] getCompoundName() {
        char[][] pkg = JavaSourceSupport.getPackageNameComponents((Definition) definition, false, JavaSourceSupport.UsagePurpose.getPurpose(env, JavaSourceSupport.CodeType.EXCUTABLE));
        char[][] result = new char[pkg.length + 1][];
        System.arraycopy(pkg, 0, result, 0, pkg.length);
        result[pkg.length] = name;
        return result;
    }

    public abstract SourceTypeBinding createBinding(PackageBinding packageBinding, AdsDefinitionScope scope, SourceTypeBinding enclosingType);

    public AbstractMethodDeclaration addIdAccessor(Id id, ClassScope scope) {
        AbstractMethodDeclaration method = idAccessors.get(id);
        if (method != null) {
            return method;
        } else {
            method = new MethodDeclaration(compilationResult);

            idAccessors.put(id, method);
            method.selector = ("$idof$_" + id.toString()).toCharArray();
            method.modifiers = ClassFileConstants.AccStatic | ClassFileConstants.AccPrivate;
            ((MethodDeclaration) method).returnType = new QualifiedTypeReference(BaseGenerator.ID_TYPE_NAME, new long[BaseGenerator.ID_TYPE_NAME.length]);
            method.statements = new Statement[]{
                new ReturnStatement(new NullLiteral(0, 0), 0, 0)
            };
            addMethod(method, scope);
            return method;
        }
    }

    private void addMethod(AbstractMethodDeclaration method, ClassScope scope) {
        AbstractMethodDeclaration[] decls = new AbstractMethodDeclaration[methods.length + 1];
        System.arraycopy(methods, 0, decls, 0, methods.length);
        decls[methods.length] = method;
        methods = decls;
        ((AdsTypeBinding) this.binding).addMethod(method, scope);
    }
    private boolean wasResolved;

    @Override
    public void resolve(BlockScope blockScope) {
        if (wasResolved) {
            return;
        }
        if ((this.bits & ASTNode.IsAnonymousType) == 0) {
            Binding existing = blockScope.getType(this.name);
            if (existing instanceof ReferenceBinding
                    && existing != this.binding
                    && existing.isValidBinding()) {
                ReferenceBinding existingType = (ReferenceBinding) existing;
                if (existingType instanceof TypeVariableBinding) {
                    blockScope.problemReporter().typeHiding(this, (TypeVariableBinding) existingType);
                    // https://bugs.eclipse.org/bugs/show_bug.cgi?id=312989, check for collision with enclosing type.
                    Scope outerScope = blockScope.parent;
                    checkOuterScope:
                    while (outerScope != null) {
                        Binding existing2 = outerScope.getType(this.name);
                        if (existing2 instanceof TypeVariableBinding && existing2.isValidBinding()) {
                            TypeVariableBinding tvb = (TypeVariableBinding) existingType;
                            Binding declaringElement = tvb.declaringElement;
                            if (declaringElement instanceof ReferenceBinding
                                    && CharOperation.equals(((ReferenceBinding) declaringElement).sourceName(), this.name)) {
                                blockScope.problemReporter().typeCollidesWithEnclosingType(this);
                                break checkOuterScope;
                            }
                        } else if (existing2 instanceof ReferenceBinding
                                && existing2.isValidBinding()
                                && outerScope.isDefinedInType((ReferenceBinding) existing2)) {
                            blockScope.problemReporter().typeCollidesWithEnclosingType(this);
                            break checkOuterScope;
                        } else if (existing2 == null) {
                            break checkOuterScope;
                        }
                        outerScope = outerScope.parent;
                    }
                } else if (existingType instanceof LocalTypeBinding
                        && ((LocalTypeBinding) existingType).scope.methodScope() == blockScope.methodScope()) {
                    // dup in same method
                    blockScope.problemReporter().duplicateNestedType(this);
                } else if (blockScope.isDefinedInType(existingType)) {
                    //	collision with enclosing type
                    blockScope.problemReporter().typeCollidesWithEnclosingType(this);
                } else if (blockScope.isDefinedInSameUnit(existingType)) { // only consider hiding inside same unit
                    // hiding sibling
                    blockScope.problemReporter().typeHiding(this, existingType);
                }
            }
            addToBlock(blockScope);
        }

        if (this.binding != null) {
            blockScope.referenceCompilationUnit().record((LocalTypeBinding) this.binding);
            resolve();
            updateMaxFieldCount();
        }
    }

    private void addToBlock(BlockScope scope) {
        AdsDefinitionScope localTypeScope = new AdsDefinitionScope(scope, this);
        scope.addSubscope(localTypeScope);
        localTypeScope.buildLocalTypeBinding(scope.enclosingSourceType());
    }

    void updateMaxFieldCount() {
        if (this.binding == null) {
            return; // error scenario
        }
        TypeDeclaration outerMostType = this.scope.outerMostClassScope().referenceType();
        if (this.maxFieldCount > outerMostType.maxFieldCount) {
            outerMostType.maxFieldCount = this.maxFieldCount; // up
        } else {
            this.maxFieldCount = outerMostType.maxFieldCount; // down
        }
    }

    @Override
    public void resolve() {
        if (wasResolved) {
            return;
        }
        wasResolved = true;
        super.resolve();
    }

    @Override
    public void resolve(ClassScope upperScope) {
        if (wasResolved) {
            return;
        }
        super.resolve(upperScope);
    }

    @Override
    public void resolve(CompilationUnitScope upperScope) {
        if (wasResolved) {
            return;
        }
        super.resolve(upperScope);
    }

    @Override
    public void generateCode(ClassFile enclosingClassFile) {
        if ((this.bits & ASTNode.HasBeenGenerated) != 0) {
            return;
        }
        this.bits |= ASTNode.HasBeenGenerated;
        if (this.ignoreFurtherInvestigation) {
            if (this.binding == null) {
                return;
            }
            ClassFile.createProblemType(this, scope.referenceCompilationUnit().compilationResult);
            return;
        }
        try {
            final ClassFile classFile = new JMLClassFile(binding);
            classFile.initialize(binding, enclosingClassFile, false);

            if (binding.isMemberType()) {
                classFile.recordInnerClasses(this.binding);
            } else if (binding.isLocalType()) {
                enclosingClassFile.recordInnerClasses(this.binding);
                classFile.recordInnerClasses(this.binding);
            }
            for (final TypeVariableBinding tvb : binding.typeVariables()) {
                if ((tvb.tagBits & TagBits.ContainsNestedTypeReferences) != 0) {
                    Util.recordNestedType(classFile, tvb);
                }
            }
            classFile.addFieldInfos();
            if (memberTypes != null) {
                for (final TypeDeclaration member : memberTypes) {
                    classFile.recordInnerClasses(member.binding);
                    member.generateCode(scope, classFile);
                }
            }
            // generate all methods
            classFile.setForMethodInfos();
            if (this.methods != null) {
                for (final AbstractMethodDeclaration mth : methods) {
                    mth.generateCode(scope, classFile);
                }
            }
            classFile.addSpecialMethods();
            if (this.ignoreFurtherInvestigation) {
                throw new AbortType(scope.referenceCompilationUnit().compilationResult, null);
            }
            classFile.addAttributes();
            this.scope.referenceCompilationUnit().compilationResult.record(binding.constantPoolName(), classFile);
        } catch (AbortType e) {
            if (binding == null) {
                return;
            }
            ClassFile.createProblemType(this, scope.referenceCompilationUnit().compilationResult);
        }
    }
}
