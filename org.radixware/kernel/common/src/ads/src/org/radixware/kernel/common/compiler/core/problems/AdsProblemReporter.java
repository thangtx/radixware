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
package org.radixware.kernel.common.compiler.core.problems;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import org.eclipse.jdt.core.compiler.CategorizedProblem;
import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.IErrorHandlingPolicy;
import org.eclipse.jdt.internal.compiler.ast.ASTNode;
import org.eclipse.jdt.internal.compiler.ast.AllocationExpression;
import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.FieldDeclaration;
import org.eclipse.jdt.internal.compiler.ast.MessageSend;
import org.eclipse.jdt.internal.compiler.ast.NameReference;
import org.eclipse.jdt.internal.compiler.ast.QualifiedNameReference;
import org.eclipse.jdt.internal.compiler.ast.Statement;
import org.eclipse.jdt.internal.compiler.ast.SwitchStatement;
import org.eclipse.jdt.internal.compiler.impl.ReferenceContext;
import org.eclipse.jdt.internal.compiler.lookup.AdsBinaryTypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.AdsCompilationUnitDeclaration;
import org.eclipse.jdt.internal.compiler.lookup.AdsMissingTypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.AdsTypeBinding;
import org.radixware.kernel.common.compiler.lookup.AdsWorkspace;
import org.eclipse.jdt.internal.compiler.lookup.ArrayBinding;
import org.eclipse.jdt.internal.compiler.lookup.Binding;
import org.eclipse.jdt.internal.compiler.lookup.CompilationUnitScope;
import org.eclipse.jdt.internal.compiler.lookup.FieldBinding;
import org.eclipse.jdt.internal.compiler.lookup.MethodBinding;
import org.eclipse.jdt.internal.compiler.lookup.ParameterizedTypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.ProblemReasons;
import org.eclipse.jdt.internal.compiler.lookup.ProblemReferenceBinding;
import org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;
import org.eclipse.jdt.internal.compiler.lookup.SourceTypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.TypeIds;
import org.eclipse.jdt.internal.compiler.problem.ProblemReporter;
import org.eclipse.jdt.internal.compiler.problem.ProblemSeverities;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.compiler.core.ast.JMLMessageSend;
import org.radixware.kernel.common.compiler.core.ast.JMLQualifiedNameReference;
import org.radixware.kernel.common.compiler.core.ast.JMLSingleNameReference;
import org.radixware.kernel.common.compiler.core.ast.JMLSwitchStatement;
import org.radixware.kernel.common.compiler.core.ast.RadixObjectLocator;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsEnumClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsEnumClassFieldDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumItemDef;
import org.radixware.kernel.common.defs.ads.module.AdsPath;
import org.radixware.kernel.common.defs.ads.platform.IPlatformClassPublisher;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.xml.AbstractXmlDefinition;
import org.radixware.kernel.common.jml.JmlTagTypeDeclaration;
import org.radixware.kernel.common.repository.ads.AdsSegment;
import org.radixware.kernel.common.types.Id;

public class AdsProblemReporter extends ProblemReporter {

    public AdsProblemReporter() {
        super(new IErrorHandlingPolicy() {
            @Override
            public boolean proceedOnErrors() {
                return true;
            }

            @Override
            public boolean stopOnFirstError() {
                return false;
            }
        }, AdsWorkspace.getCompilerOptions(), new AdsProblemFactory());

    }
    private IProblemHandler adshandler;
    private boolean hasErrors = false;
    private boolean hasWarnings = false;
    public AdsWorkspace workspace;

    public AdsProblemReporter(IProblemHandler adshandler) {
        this();
        this.adshandler = adshandler;
    }

    public IProblemHandler getRadixProblemHandler() {
        return adshandler;
    }
    
    

    public void unresolvedDefinitionReference(Definition context, AdsPath path, CompilationUnitScope scope) {
        Definition some = path.resolveSomething(context);
        if (some == null) {
            record(new RadixLevelProblem(context, "Unresolved definition reference: #" + path.toString(), ProblemSeverities.Error), ((CompilationUnitDeclaration) scope.referenceContext()).compilationResult, scope.referenceContext(), false);
        } else {
            record(new RadixLevelProblem(context, "Unresolved reference to subdefinition of " + some.getQualifiedName() + ": #" + path.toString(), ProblemSeverities.Error), ((CompilationUnitDeclaration) scope.referenceContext()).compilationResult, scope.referenceContext(), false);
        }
        // hasErrors = true;
    }

    public void unresolveableParentPropertyParentPath(AdsPropertyDef property, CompilationResult result, ReferenceContext referenceContext) {
        record(new RadixLevelProblem(property, "Impossible to build parent entity calculation chain", ProblemSeverities.Error), result, referenceContext, false);
    }

    public void readOnlyProperty(JMLMessageSend messageSend, MethodBinding getter) {
        char[] selector = messageSend.getPropertyName();
        AdsCompilationUnitDeclaration decl = this.getCompilationUnitDeclaration(referenceContext);
        if (decl != null) {
            RadixObject obj = messageSend.findRadixObject(decl);
            if (obj != null) {
                selector = ("`" + obj.getName() + "`").toCharArray();
            }
        }
        this.handle(
                IAdsProblem.ReadOnlyProperty,
                new String[]{
                    new String(getter.declaringClass.readableName()),
                    new String(selector),},
                new String[]{
                    new String(getter.declaringClass.shortReadableName()),
                    new String(selector)},
                (int) (messageSend.nameSourcePosition >>> 32),
                (int) messageSend.nameSourcePosition);
    }

    @Override
    public void record(CategorizedProblem problem, CompilationResult unitResult, ReferenceContext referenceContext, boolean optionalError) {
        if (problem.getID() == IProblem.UnsafeGenericCast || problem.getID() == IProblem.RawTypeReference || problem.getID() == IProblem.UnsafeTypeConversion || problem.getID() == IProblem.UnsafeRawMethodInvocation) {
            return;
        }

        if (problem.getID() == IProblem.MissingSerialVersion) {
            return;
        }
        if (currentSwitch != null) {
            currentSwitch.missingCaseProblem = problem;
            currentSwitch = null;
        }
        super.record(problem, unitResult, referenceContext, optionalError);
    }

    public class InvalidMethodHandler {

        private Map<MessageSend, MethodBinding> records = null;

        public void invalidMethod(MessageSend messageSend, MethodBinding method) {
            if (records == null) {
                records = new HashMap<>();
            }
            records.put(messageSend, method);
        }

        public void post() {
            handlers.pop();
            if (records == null) {
                return;
            }
            for (MessageSend ms : records.keySet()) {
                AdsProblemReporter.this.invalidMethod(ms, records.get(ms));
            }
        }

        public void reject() {
            handlers.pop();
        }
    }

    private Stack<InvalidMethodHandler> handlers = new Stack<>();

    public InvalidMethodHandler pushIvalidMethodHandler() {
        InvalidMethodHandler handler = new InvalidMethodHandler();
        handlers.push(handler);
        return handler;

    }

    private AdsCompilationUnitDeclaration getCompilationUnitDeclaration(ReferenceContext rc) {
        return (AdsCompilationUnitDeclaration) rc.getCompilationUnitDeclaration();
    }

    @Override
    public void isClassPathCorrect(char[][] wellKnownTypeName, CompilationUnitDeclaration compUnitDecl, Object location) {

        super.isClassPathCorrect(wellKnownTypeName, compUnitDecl, location); //To change body of generated methods, choose Tools | Templates.
    }

    private static class BindingWrapper extends Binding {

        private String name;

        public BindingWrapper(String name) {
            this.name = name;
        }

        @Override
        public int kind() {
            return VARIABLE | TYPE;
        }

        @Override
        public char[] readableName() {
            return name.toCharArray();
        }

    }

    @Override
    public void unresolvableReference(NameReference nameRef, Binding binding) {
        String substName = null;
        if (nameRef instanceof JMLQualifiedNameReference) {
            substName = ((JMLQualifiedNameReference) nameRef).getReadableName();
        } else if (nameRef instanceof JMLSingleNameReference) {
            substName = ((JMLSingleNameReference) nameRef).getReadableName();
        }
        if (substName != null) {
            super.unresolvableReference(nameRef, new BindingWrapper(substName));
        } else {
            super.unresolvableReference(nameRef, binding);
        }
    }

    @Override
    public void missingTypeInMethod(MessageSend messageSend, MethodBinding method) {
        List missingTypes = method.collectMissingTypes(null);
        if (missingTypes == null) {
            System.err.println("The method " + method + " is wrongly tagged as containing missing types"); //$NON-NLS-1$ //$NON-NLS-2$
            return;
        }
        char[] selectorStr = method.selector;

        TypeBinding missingType = (TypeBinding) missingTypes.get(0);
        if (messageSend instanceof JMLMessageSend) {
            JMLMessageSend ms = (JMLMessageSend) messageSend;
            AdsCompilationUnitDeclaration decl = this.getCompilationUnitDeclaration(referenceContext);
            if (decl != null) {
                RadixObject obj = ms.findRadixObject(decl);
                if (obj != null) {
                    selectorStr = ("`" + obj.getName() + "`").toCharArray();
                }
            }
            if (ms.isProperty()) {
                this.handle(
                        IAdsProblem.MissingTypeInProperty,
                        new String[]{
                            new String(method.declaringClass.readableName()),
                            new String(selectorStr),
                            new String(missingType.readableName()),},
                        new String[]{
                            new String(method.declaringClass.shortReadableName()),
                            new String(selectorStr),
                            new String(missingType.shortReadableName()),},
                        (int) (messageSend.nameSourcePosition >>> 32),
                        (int) messageSend.nameSourcePosition);
            } else {
                this.handle(
                        IProblem.MissingTypeInMethod,
                        new String[]{
                            new String(method.declaringClass.readableName()),
                            new String(selectorStr),
                            typesAsString(method, false),
                            new String(missingType.readableName()),},
                        new String[]{
                            new String(method.declaringClass.shortReadableName()),
                            new String(selectorStr),
                            typesAsString(method, true),
                            new String(missingType.shortReadableName()),},
                        (int) (messageSend.nameSourcePosition >>> 32),
                        (int) messageSend.nameSourcePosition);
            }
        } else {
            super.missingTypeInMethod(messageSend, method);
        }
    }

    @Override
    public void invalidConstructor(Statement statement, MethodBinding targetConstructor) {
        super.invalidConstructor(statement, targetConstructor); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean hasErrors() {
        return hasErrors;
    }

    public boolean hasWarnings() {
        return hasWarnings;
    }

    @Override
    public void autoboxing(Expression expression, TypeBinding originalType, TypeBinding convertedType) {
        if (expression.implicitConversion == TypeIds.UNBOXING) {
            CompilationUnitDeclaration unitDecl = this.referenceContext == null ? null : this.referenceContext.getCompilationUnitDeclaration();
            if (unitDecl instanceof AdsCompilationUnitDeclaration) {
                AdsCompilationUnitDeclaration decl = (AdsCompilationUnitDeclaration) unitDecl;
                if (decl.getContextDefinition() instanceof AbstractXmlDefinition) {
                    return;
                }
            }
            if (expression instanceof QualifiedNameReference && originalType != null && originalType.id == TypeIds.T_JavaLangBoolean && convertedType != null && convertedType.id == TypeIds.T_boolean) {
                final QualifiedNameReference ref = (QualifiedNameReference) expression;
                if (ref.binding != null && ref.binding.isValidBinding() && ref.binding instanceof FieldBinding) {
                    final FieldBinding fb = ref.fieldBinding();
                    if (fb.declaringClass == originalType && (CharOperation.equals("FALSE".toCharArray(), fb.name) || CharOperation.equals("TRUE".toCharArray(), fb.name))) {
                        return;
                    }
                }
            }
            this.handle(IProblem.UnboxingConversion,
                    new String[]{new String(originalType.readableName()), new String(convertedType.readableName()),},
                    0,
                    new String[]{new String(originalType.shortReadableName()), new String(convertedType.shortReadableName()),},
                    ProblemSeverities.Error,
                    expression.sourceStart,
                    expression.sourceEnd,
                    this.referenceContext, this.referenceContext == null ? null : this.referenceContext.compilationResult());
        } else {
            super.autoboxing(expression, originalType, convertedType);
        }

    }

    @Override
    public void invalidType(ASTNode location, TypeBinding type) {
        AdsCompilationUnitDeclaration cu = getCompilationUnitDeclaration(referenceContext);
        if (cu != null && cu.radixObjectLookup != null) {
            RadixObjectLocator.RadixObjectData[] data = cu.radixObjectLookup.take(location.sourceStart, location.sourceEnd);
            if (data != null && data.length == 1 && data[0].radixObject instanceof JmlTagTypeDeclaration) {
                AdsType adsType = ((JmlTagTypeDeclaration) data[0].radixObject).getType().resolve(cu.getContextDefinition()).get();
                if (adsType != null) {
                    ((AdsProblemFactory) problemFactory).addRenameRule(String.valueOf(type.shortReadableName()), "`" + adsType.getQualifiedName(cu.getContextDefinition()) + "`");
                    ((AdsProblemFactory) problemFactory).addRenameRule(String.valueOf(type.readableName()), "`" + adsType.getQualifiedName(cu.getContextDefinition()) + "`");
                }
            } else {
            }
        }
        if (type instanceof ProblemReferenceBinding) {
            ProblemReferenceBinding binding = (ProblemReferenceBinding) type;
            if (binding.closestMatch() instanceof AdsMissingTypeBinding) {
                ReferenceBinding closestMatch = binding.closestReferenceMatch();
                ((AdsProblemFactory) problemFactory).addRenameRule(String.valueOf(type.shortReadableName()), String.valueOf(closestMatch.shortReadableName()));
                ((AdsProblemFactory) problemFactory).addRenameRule(String.valueOf(type.readableName()), String.valueOf(closestMatch.readableName()));
            } else {
                //System.out.println("");
            }
        }
        super.invalidType(location, type); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void invalidMethod(MessageSend messageSend, MethodBinding method) {
        InvalidMethodHandler handler = handlers.isEmpty() ? null : handlers.peek();
        if (handler != null) {
            handler.invalidMethod(messageSend, method);
            return;
        }
        if (messageSend instanceof JMLMessageSend) {
            final JMLMessageSend ms = (JMLMessageSend) messageSend;
            if (ms.isProperty()) {
                boolean report = false;
                int id = method.problemId();
                final char[] selector = ms.getPropertyName();

                switch (method.problemId()) {
                    case ProblemReasons.NotVisible:
                        id = IAdsProblem.NotVisibleProperty;
                        report = true;
                        break;
                }
                if (report) {
                    this.handle(
                            id,
                            new String[]{
                                new String(method.declaringClass.readableName()),
                                new String(selector),},
                            new String[]{
                                new String(method.declaringClass.shortReadableName()),
                                new String(selector)},
                            (int) (messageSend.nameSourcePosition >>> 32),
                            (int) messageSend.nameSourcePosition);
                    return;
                }
            }
            if (method != null && method.declaringClass != null) {
                if (method.declaringClass.isMemberType()) {
                    ReferenceBinding owner = method.declaringClass;
                    Definition definition = null;
                    List<String> path = new LinkedList<>();
                    while (owner != null) {
                        if (owner instanceof AdsBinaryTypeBinding) {
                            definition = ((AdsBinaryTypeBinding) owner).getDefinition();
                        } else if (owner instanceof AdsTypeBinding) {
                            definition = ((AdsTypeBinding) owner).getDefinition();
                        }
                        if (definition != null) {
                            break;
                        }
                        path.add(String.valueOf(owner.sourceName));
                        owner = owner.enclosingType();
                    }

                    while (definition instanceof AdsDefinition && !path.isEmpty()) {
                        Id p = Id.Factory.loadFrom(path.remove(path.size() - 1));
                        definition = ((AdsDefinition) definition).findComponentDefinition(p).get();
                    }
                    if (path.isEmpty() && definition != null) {
                        ((AdsProblemFactory) problemFactory).addRenameRule(String.valueOf(method.declaringClass.readableName()), "`" + definition.getQualifiedName() + "`");
                        ((AdsProblemFactory) problemFactory).addRenameRule(String.valueOf(method.declaringClass.shortReadableName()), "`" + definition.getQualifiedName() + "`");
                    }
                }
            }

            RadixObject referencedDef = ms.findRadixObject(getCompilationUnitDeclaration(referenceContext));
            if (referencedDef != null) {
                ((AdsProblemFactory) problemFactory).addRenameRule(String.valueOf(ms.selector), "`" + referencedDef.getName() + "`");//     
            }
        }

        super.invalidMethod(messageSend, method);
    }

    @Override
    public void abstractMethodMustBeImplemented(SourceTypeBinding type, MethodBinding abstractMethod) {

        Definition def = getDefinition(abstractMethod.declaringClass);
        if (def instanceof AdsClassDef) {
            Id idcandidate = Id.Factory.loadFrom(String.valueOf(abstractMethod.selector));
            AdsMethodDef method = ((AdsClassDef) def).getMethods().findById(idcandidate, ExtendableDefinitions.EScope.ALL).get();
            if (method != null) {
                ((AdsProblemFactory) problemFactory).addRenameRule(String.valueOf(abstractMethod.selector), "`" + method.getName() + "`");//     
            }
        }
        if (type.isEnum() && type.isLocalType()) {
            FieldBinding field = type.scope.enclosingMethodScope().initializedField;
            String name = getEnumField(field);
            ((AdsProblemFactory) problemFactory).addRenameRule(String.valueOf(field.name), "`" + name + "`");
        }
        super.abstractMethodMustBeImplemented(type, abstractMethod);

    }

    @Override
    public void abstractMethodMustBeImplemented(SourceTypeBinding type, MethodBinding abstractMethod, MethodBinding concreteMethod) {
        Definition def = getDefinition(abstractMethod.declaringClass);
        if (def instanceof AdsClassDef) {
            Id idcandidate = Id.Factory.loadFrom(String.valueOf(abstractMethod.selector));
            AdsMethodDef method = ((AdsClassDef) def).getMethods().findById(idcandidate, ExtendableDefinitions.EScope.ALL).get();
            if (method != null) {
                ((AdsProblemFactory) problemFactory).addRenameRule(String.valueOf(abstractMethod.selector), "`" + method.getName() + "`");//     
            }
        }
        def = getDefinition(concreteMethod.declaringClass);
        if (def instanceof AdsClassDef) {
            Id idcandidate = Id.Factory.loadFrom(String.valueOf(concreteMethod.selector));
            AdsMethodDef method = ((AdsClassDef) def).getMethods().findById(idcandidate, ExtendableDefinitions.EScope.ALL).get();
            if (method != null) {
                ((AdsProblemFactory) problemFactory).addRenameRule(String.valueOf(concreteMethod.selector), "`" + method.getName() + "`");//     
            }
        }
        super.abstractMethodMustBeImplemented(type, abstractMethod, concreteMethod);
        
    }

    @Override
    public void deprecatedField(FieldBinding field, ASTNode location) {
        Definition def = getDefinition(field.declaringClass);
        if (def instanceof AdsEnumDef) {
            Id idcandidate = Id.Factory.loadFrom(String.valueOf(field .name));
            AdsEnumItemDef item = ((AdsEnumDef) def).getItems().findById(idcandidate, ExtendableDefinitions.EScope.ALL).get();
            if (item != null) {
                ((AdsProblemFactory) problemFactory).addRenameRule(String.valueOf(field.name), "`" + item.getName() + "`");//     
            }
        }
        super.deprecatedField(field, location); 
    }

    
    private void handle(
            int problemId,
            String[] problemArguments,
            int elaborationId,
            String[] messageArguments,
            int severity,
            int problemStartPosition,
            int problemEndPosition) {
        this.handle(
                problemId,
                problemArguments,
                elaborationId,
                messageArguments,
                severity,
                problemStartPosition,
                problemEndPosition,
                this.referenceContext,
                this.referenceContext == null ? null : this.referenceContext.compilationResult());
        this.referenceContext = null;
    }

    private void handle(
            int problemId,
            String[] problemArguments,
            String[] messageArguments,
            int severity,
            int problemStartPosition,
            int problemEndPosition) {

        this.handle(
                problemId,
                problemArguments,
                0, // no elaboration
                messageArguments,
                severity,
                problemStartPosition,
                problemEndPosition);

    }

    private void deprecatedMethod(MethodBinding methodBinding, AdsMethodDef method, ASTNode location) {
        boolean isConstructor = methodBinding.isConstructor();
        int severity = computeSeverity(isConstructor ? IProblem.UsingDeprecatedConstructor : IProblem.UsingDeprecatedMethod);
        if (severity == ProblemSeverities.Ignore) {
            return;
        }
        if (isConstructor) {
            int start = -1;
            if (location instanceof AllocationExpression) {
                // omit the new keyword from the warning marker
                // https://bugs.eclipse.org/bugs/show_bug.cgi?id=300031
                AllocationExpression allocationExpression = (AllocationExpression) location;
                if (allocationExpression.enumConstant != null) {
                    start = allocationExpression.enumConstant.sourceStart;
                } else {
                    start = allocationExpression.type.sourceStart;
                }
            }
            this.handle(
                    IProblem.UsingDeprecatedConstructor,
                    new String[]{new String(methodBinding.declaringClass.readableName()), typesAsString(methodBinding, false)},
                    new String[]{new String(methodBinding.declaringClass.shortReadableName()), typesAsString(methodBinding, true)},
                    severity,
                    (start == -1) ? location.sourceStart : start,
                    location.sourceEnd);
        } else {
            int start = -1;
            if (location instanceof MessageSend) {
                // start the warning marker from the location where the name of the method starts
                // https://bugs.eclipse.org/bugs/show_bug.cgi?id=300031
                start = (int) (((MessageSend) location).nameSourcePosition >>> 32);
            }
            this.handle(
                    IProblem.UsingDeprecatedMethod,
                    new String[]{new String(methodBinding.declaringClass.readableName()), "`" + method.getName() + "`", typesAsString(methodBinding, false)},
                    new String[]{new String(methodBinding.declaringClass.shortReadableName()), "`" + method.getName() + "`", typesAsString(methodBinding, true)},
                    severity,
                    (start == -1) ? location.sourceStart : start,
                    location.sourceEnd);
        }
    }

    private void deprecatedProperty(MethodBinding methodBinding, AdsPropertyDef property, ASTNode location) {
        int severity = computeSeverity(IProblem.UsingDeprecatedMethod);
        if (severity == ProblemSeverities.Ignore) {
            return;
        }

        int start = -1;
        if (location instanceof MessageSend) {
            // start the warning marker from the location where the name of the method starts
            // https://bugs.eclipse.org/bugs/show_bug.cgi?id=300031
            start = (int) (((MessageSend) location).nameSourcePosition >>> 32);
        }
        this.handle(
                IAdsProblem.DeprecatedProperty,
                new String[]{new String(methodBinding.declaringClass.readableName()), "`" + property.getName() + "`"},
                new String[]{new String(methodBinding.declaringClass.shortReadableName()), "`" + property.getName() + "`"},
                severity,
                (start == -1) ? location.sourceStart : start,
                location.sourceEnd);

    }
      public void THISusage(ASTNode location) {
        int severity = computeSeverity(IProblem.UsingDeprecatedMethod);
        if (severity == ProblemSeverities.Ignore) {
            return;
        }

        int start = -1;
        if (location instanceof MessageSend) {
            // start the warning marker from the location where the name of the method starts
            // https://bugs.eclipse.org/bugs/show_bug.cgi?id=300031
            start = (int) (((MessageSend) location).nameSourcePosition >>> 32);
        }
        this.handle(
                IAdsProblem.THIS_Usage,
                null,
                null,
                severity,
                (start == -1) ? location.sourceStart : start,
                location.sourceEnd);

    }

    @Override
    public void deprecatedMethod(MethodBinding method, ASTNode location) {
        Definition def = getDefinition(method.declaringClass);
        if (def instanceof AdsClassDef) {
            String selector = String.valueOf(method.selector);
            AdsMethodDef methodDef = ((AdsClassDef) def).getMethods().findById(Id.Factory.loadFrom(selector), ExtendableDefinitions.EScope.ALL).get();
            if (methodDef != null) {
                deprecatedMethod(method, methodDef, location);
                return;
            } else if (selector.startsWith("get") || selector.startsWith("set")) {
                String propIdCandidate = selector.substring(3);
                AdsPropertyDef propDef = ((AdsClassDef) def).getProperties().findById(Id.Factory.loadFrom(propIdCandidate), ExtendableDefinitions.EScope.ALL).get();
                if (propDef != null) {
                    ((AdsProblemFactory) problemFactory).addRenameRule(String.valueOf(method.selector), "`" + propDef.getName() + "`");//     
                    deprecatedProperty(method, propDef, location);
                    return;
                }
            }
        }

        super.deprecatedMethod(method, location); //To change body of generated methods, choose Tools | Templates.
    }

    private String getParameterizedTypeReadableName(ParameterizedTypeBinding parameterizedType) {

        StringBuilder nameBuffer = new StringBuilder();
        if (parameterizedType.isMemberType()) {
            nameBuffer.append(CharOperation.concat(parameterizedType.enclosingType().readableName(), parameterizedType.sourceName, '.'));
        } else {
            ReferenceBinding genericType = parameterizedType.genericType();
            if (genericType instanceof AdsBinaryTypeBinding && ((AdsBinaryTypeBinding) genericType).getDefinition() != null) {
                nameBuffer.append(genericType.readableName());
            } else {
                nameBuffer.append(CharOperation.concatWith(genericType.compoundName, '.'));
            }
        }
        if (parameterizedType.arguments != null) {
            nameBuffer.append('<');
            for (int i = 0, length = parameterizedType.arguments.length; i < length; i++) {
                if (i > 0) {
                    nameBuffer.append(',');
                }
                nameBuffer.append(parameterizedType.arguments[i].readableName());
            }
            nameBuffer.append('>');
        }
        return nameBuffer.toString();
    }

    private void handle(
            int problemId,
            String[] problemArguments,
            String[] messageArguments,
            int problemStartPosition,
            int problemEndPosition) {
        this.handle(
                problemId,
                problemArguments,
                0,
                messageArguments,
                computeSeverity(problemId),
                problemStartPosition,
                problemEndPosition,
                this.referenceContext,
                this.referenceContext == null ? null : this.referenceContext.compilationResult());
    }

    private String typesAsString(MethodBinding methodBinding, boolean makeShort) {
        return typesAsString(methodBinding, methodBinding.parameters, makeShort);
    }

    private String typesAsString(MethodBinding methodBinding, TypeBinding[] parameters, boolean makeShort) {
        if (methodBinding.isPolymorphic()) {
            // get the original polymorphicMethod method
            TypeBinding[] types = methodBinding.original().parameters;
            StringBuffer buffer = new StringBuffer(10);
            for (int i = 0, length = types.length; i < length; i++) {
                if (i != 0) {
                    buffer.append(", "); //$NON-NLS-1$
                }
                TypeBinding type = types[i];
                boolean isVarargType = i == length - 1;
                if (isVarargType) {
                    type = ((ArrayBinding) type).elementsType();
                }
                buffer.append(new String(makeShort ? type.shortReadableName() : type.readableName()));
                if (isVarargType) {
                    buffer.append("..."); //$NON-NLS-1$
                }
            }
            return buffer.toString();
        }
        StringBuffer buffer = new StringBuffer(10);
        for (int i = 0, length = parameters.length; i < length; i++) {
            if (i != 0) {
                buffer.append(", "); //$NON-NLS-1$
            }
            TypeBinding type = parameters[i];
            boolean isVarargType = methodBinding.isVarargs() && i == length - 1;
            if (isVarargType) {
                type = ((ArrayBinding) type).elementsType();
            }
            buffer.append(new String(makeShort ? type.shortReadableName() : type.readableName()));
            if (isVarargType) {
                buffer.append("..."); //$NON-NLS-1$
            }
        }
        return buffer.toString();
    }

    private String typesAsString(TypeBinding[] types, boolean makeShort) {
        StringBuffer buffer = new StringBuffer(10);
        for (int i = 0, length = types.length; i < length; i++) {
            if (i != 0) {
                buffer.append(", "); //$NON-NLS-1$
            }
            TypeBinding type = types[i];
            buffer.append(new String(makeShort ? type.shortReadableName() : type.readableName()));
        }
        return buffer.toString();
    }

    private JMLSwitchStatement currentSwitch = null;

    @Override
    public void missingEnumConstantCase(SwitchStatement switchStatement, FieldBinding enumConstant) {
        //check needs for name replacement
        String constantName = getEnumField(enumConstant);

        if (switchStatement instanceof JMLSwitchStatement) {
            currentSwitch = (JMLSwitchStatement) switchStatement;
            if (currentSwitch.missingCaseProblem != null) {
                if (currentSwitch.missingCaseProblem instanceof AdsProblemFactory.MutableProblem) {
                    //TODO: update message
                }
                return;
            }
        }

        this.handle(
                switchStatement.defaultCase == null ? IProblem.MissingEnumConstantCase : IProblem.MissingEnumConstantCaseDespiteDefault,
                new String[]{new String(enumConstant.declaringClass.readableName()), constantName},
                new String[]{new String(enumConstant.declaringClass.shortReadableName()), constantName},
                switchStatement.expression.sourceStart,
                switchStatement.expression.sourceEnd);
    }

    private Definition getDefinition(ReferenceBinding receiverType) {
        boolean isPub = false;
        Definition definition = null;
        if (receiverType instanceof AdsTypeBinding) {
            definition = ((AdsTypeBinding) receiverType).getDefinition();
        } else if (receiverType instanceof AdsBinaryTypeBinding) {
            definition = ((AdsBinaryTypeBinding) receiverType).getDefinition();
            if (definition == null) {
                final IPlatformClassPublisher pub = ((AdsSegment) this.workspace.getContextLayer().getAds()).getBuildPath().getPlatformPublishers().findPublisherByName(String.valueOf(CharOperation.concatWith(receiverType.compoundName, '.')).replace("$", "."));
                if (pub instanceof Definition) {
                    definition = (Definition) pub;
                    isPub = true;
                }
            }
        }
        if (definition == null && receiverType.isMemberType()) {
            ReferenceBinding type = receiverType;
            List<String> subdefinitions = new LinkedList<>();
            while (type != null) {

                if (type instanceof AdsTypeBinding) {
                    definition = ((AdsTypeBinding) type).getDefinition();
                } else if (type instanceof AdsBinaryTypeBinding) {
                    definition = ((AdsBinaryTypeBinding) type).getDefinition();
                }
                if (definition != null) {
                    break;
                } else {
                    subdefinitions.add(String.valueOf(type.sourceName));
                }
                type = type.enclosingType();
            }
            if (definition instanceof AdsDefinition) {
                AdsDefinition def = (AdsDefinition) definition;
                for (String subdef : subdefinitions) {
                    Id id = Id.Factory.loadFrom(subdef);
                    if (id.getPrefix() == null) {
                        def = null;
                        break;
                    }
                    def = def.findComponentDefinition(id).get();
                    if (def == null) {
                        break;
                    }
                }
//               
                definition = def;
            }
        }
        return definition;
    }
    
    private String getEnumField(FieldBinding enumConstant) {
        String constantName = String.valueOf(enumConstant.name);
        if (enumConstant.name.length >= 29) {
            Definition definition = getDefinition(enumConstant.declaringClass);

            if (definition instanceof AdsEnumDef) {
                AdsEnumItemDef item = ((AdsEnumDef) definition).getItems().findById(Id.Factory.loadFrom(String.valueOf(constantName)), ExtendableDefinitions.EScope.ALL).get();
                if (item != null) {
                    constantName = item.getName();
                }
            } else if (definition instanceof AdsEnumClassDef) {
                AdsEnumClassFieldDef field = ((AdsEnumClassDef) definition).getFields().findById(Id.Factory.loadFrom(String.valueOf(constantName)), ExtendableDefinitions.EScope.ALL).get();
                if (field != null) {
                    constantName = field.getName();
                }
            }
        }
        return constantName;
    }

    @Override
    public void nonStaticAccessToStaticMethod(ASTNode location, MethodBinding method) {
       renameMethod(method);
       super.nonStaticAccessToStaticMethod(location, method);
    }
    
    private void renameMethod(MethodBinding abstractMethod) {
        Definition def = getDefinition(abstractMethod.declaringClass);
        if (def instanceof AdsClassDef) {
            Id idcandidate = Id.Factory.loadFrom(String.valueOf(abstractMethod.selector));
            AdsMethodDef method = ((AdsClassDef) def).getMethods().findById(idcandidate, ExtendableDefinitions.EScope.ALL).get();
            if (method != null) {
                ((AdsProblemFactory) problemFactory).addRenameRule(String.valueOf(abstractMethod.selector), "`" + method.getName() + "`");//     
            }
        }
    }
}
