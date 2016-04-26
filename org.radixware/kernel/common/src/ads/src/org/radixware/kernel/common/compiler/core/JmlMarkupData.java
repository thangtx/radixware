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

package org.radixware.kernel.common.compiler.core;

import java.util.Collections;
import org.eclipse.jdt.internal.compiler.ast.AdsTypeReference;
import java.util.LinkedList;
import java.util.List;
import org.eclipse.jdt.internal.compiler.ASTVisitor;
import org.eclipse.jdt.internal.compiler.ads.syntetics.BaseGenerator;
import org.eclipse.jdt.internal.compiler.ads.syntetics.JavaKeywords;
import org.eclipse.jdt.internal.compiler.ast.AND_AND_Expression;
import org.eclipse.jdt.internal.compiler.ast.ASTNode;
import org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.AdsArrayTypeReference;
import org.eclipse.jdt.internal.compiler.ast.AllocationExpression;
import org.eclipse.jdt.internal.compiler.ast.Argument;
import org.eclipse.jdt.internal.compiler.ast.Assignment;
import org.eclipse.jdt.internal.compiler.ast.BinaryExpression;
import org.eclipse.jdt.internal.compiler.ast.Block;
import org.eclipse.jdt.internal.compiler.ast.CastExpression;
import org.eclipse.jdt.internal.compiler.ast.EqualExpression;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.FieldDeclaration;
import org.eclipse.jdt.internal.compiler.ast.FieldReference;
import org.eclipse.jdt.internal.compiler.ast.LocalDeclaration;
import org.eclipse.jdt.internal.compiler.ast.MessageSend;
import org.eclipse.jdt.internal.compiler.ast.MethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.OR_OR_Expression;
import org.eclipse.jdt.internal.compiler.ast.QualifiedNameReference;
import org.eclipse.jdt.internal.compiler.ast.QualifiedTypeReference;
import org.eclipse.jdt.internal.compiler.ast.ReturnStatement;
import org.eclipse.jdt.internal.compiler.ast.SingleNameReference;
import org.eclipse.jdt.internal.compiler.ast.SingleTypeReference;
import org.eclipse.jdt.internal.compiler.ast.Statement;
import org.eclipse.jdt.internal.compiler.ast.AdsMessageSend;
import org.eclipse.jdt.internal.compiler.ast.TaggedQualifiedTypeReference;
import org.eclipse.jdt.internal.compiler.ast.TaggedQuelifiedNameReference;
import org.eclipse.jdt.internal.compiler.ast.TaggedSingleNameReference;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.eclipse.jdt.internal.compiler.ast.TypeReference;
import org.eclipse.jdt.internal.compiler.ast.AdsAssignment;
import org.eclipse.jdt.internal.compiler.ast.AdsBinaryExpression;
import org.eclipse.jdt.internal.compiler.ast.AdsCombinedBinaryExpression;
import org.eclipse.jdt.internal.compiler.ast.AdsEqualExpression;
import org.eclipse.jdt.internal.compiler.ast.AdsParameterizedQualifiedTypeReference;
import org.eclipse.jdt.internal.compiler.ast.AdsParameterizedSingleTypeReference;
import org.eclipse.jdt.internal.compiler.ast.AdsPropertyReference;
import org.eclipse.jdt.internal.compiler.ast.Annotation;
import org.eclipse.jdt.internal.compiler.ast.ArrayAllocationExpression;
import org.eclipse.jdt.internal.compiler.ast.ArrayInitializer;
import org.eclipse.jdt.internal.compiler.ast.ArrayTypeReference;
import org.eclipse.jdt.internal.compiler.ast.ClassLiteralAccess;
import org.eclipse.jdt.internal.compiler.ast.CombinedBinaryExpression;
import org.eclipse.jdt.internal.compiler.ast.ConditionalExpression;
import org.eclipse.jdt.internal.compiler.ast.ConstructorDeclaration;
import org.eclipse.jdt.internal.compiler.ast.DoStatement;
import org.eclipse.jdt.internal.compiler.ast.ForStatement;
import org.eclipse.jdt.internal.compiler.ast.ForeachStatement;
import org.eclipse.jdt.internal.compiler.ast.IfStatement;
import org.eclipse.jdt.internal.compiler.ast.ParameterizedQualifiedTypeReference;
import org.eclipse.jdt.internal.compiler.ast.ParameterizedSingleTypeReference;
import org.eclipse.jdt.internal.compiler.ast.ReenterableQualifiedNameReference;
import org.eclipse.jdt.internal.compiler.ast.ReenterableSingleNameReference;
import org.eclipse.jdt.internal.compiler.lookup.AdsMethodDeclaration;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.ClassScope;
import org.eclipse.jdt.internal.compiler.lookup.CompilationUnitScope;
import org.eclipse.jdt.internal.compiler.lookup.MethodScope;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.compiler.core.problems.AdsProblemReporter;
import org.radixware.kernel.common.defs.ads.ICompilable;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.common.jml.JmlTagId;
import org.radixware.kernel.common.jml.JmlTagInvocation;
import org.radixware.kernel.common.jml.JmlTagTypeDeclaration;
import org.radixware.kernel.common.scml.Scml;


public class JmlMarkupData {

    public static class TagDescriptor {

        public final Jml.Tag tag;
        public final Definition definition;
        public final boolean isIdReference;
        public final boolean isInvocation;
        public final boolean isTypeDeclaration;
        public String replacingText;
        public final int start, end;
        public AdsTypeDeclaration typeDeclaration;
        public boolean isInternalPropAccessor;

        public TagDescriptor(Jml.Tag tag, Definition definition, AdsTypeDeclaration typeDeclaration, boolean isIdReference, boolean isInvocation, boolean isTypeDeclaration, boolean isInternalPropAccessor, int start) {
            this.tag = tag;

            this.typeDeclaration = typeDeclaration;
            this.definition = definition;
            this.isIdReference = isIdReference;
            this.isInvocation = isInvocation;
            this.isTypeDeclaration = isTypeDeclaration;
            this.start = start;
            this.isInternalPropAccessor = isInternalPropAccessor;
            computeReplacingText();
            this.end = start + replacingText.length();

        }

        private void computeReplacingText() {
            String dn = tag.getDisplayName();
            //check java keyword
            if (isIdReference) {
                StringBuilder sb = new StringBuilder();
                int pad_count = dn.length() - 4;
                sb.append("idof");
                for (int i = 0; i < pad_count; i++) {
                    sb.append("$");
                }
                replacingText = sb.toString();
            } else {
                replacingText = JavaKeywords.checkKeyword(tag.getDisplayName().replace(":", "$").replace(".", "$").replace("<", "$").replace(">", "$"));
                if (isInternalPropAccessor) {
                    replacingText = replacingText.replace("[", "$").replace("]", "$");
                }
            }
        }
    }
    public char[] content;
    public final List<TagDescriptor> tagInfo;
    private final Definition context;
    // private final Scope mainScope;

    public JmlMarkupData(Definition context,/* Scope mainScope, */ char[] content, List<TagDescriptor> tagInfo) {
        this.content = content;
        this.tagInfo = tagInfo;
        this.context = context;
        //  this.mainScope = mainScope;
    }

    public JmlMarkupData(Definition context) {
        this.content = new char[0];
        this.tagInfo = Collections.emptyList();
        this.context = context;
        //  this.mainScope = mainScope;
    }

    public static JmlMarkupData build(CompilationUnitScope scope, Definition context, Jml[] srcs, AdsProblemReporter reporter) {
        try {
            StringBuilder content = new StringBuilder();
            boolean hasUnresolvedDefinitons = false;
            List<TagDescriptor> descriptors = new LinkedList<>();
            for (Jml src : srcs) {
                for (Scml.Item item : src.getItems()) {
                    if (item instanceof Scml.Text) {
                        content.append(((Scml.Text) item).getText());
                    } else {
                        Jml.Tag tag = (Jml.Tag) item;
                        TagDescriptor desc = null;
                        if (tag instanceof JmlTagTypeDeclaration) {
                            JmlTagTypeDeclaration decl = (JmlTagTypeDeclaration) tag;
                            desc = new TagDescriptor(tag, context.getDefinition(), decl.getType(), false, !false, true, false, content.length());
                        } else if (tag instanceof JmlTagId) {
                            JmlTagId id = (JmlTagId) tag;
                            Definition def = id.resolve(context.getDefinition());
                            if (def == null) {
                                reporter.unresolvedDefinitionReference(context.getDefinition(), id.getPath(), scope);
                                hasUnresolvedDefinitons = true;
                                continue;
                            }

                            boolean isIdRef = true;
                            boolean isInternalPropAccessor = false;

                            if (tag instanceof JmlTagInvocation) {
                                isIdRef = false;
                                isInternalPropAccessor = ((JmlTagInvocation) tag).isInternalPropertyAccessor();
                            }
                            desc = new TagDescriptor(tag, def, null, isIdRef, !isIdRef, false, isInternalPropAccessor, content.length());

                        } else {
                            desc = new TagDescriptor(tag, null, null, false, false, false, false, content.length());
                        }
                        descriptors.add(desc);
                        content.append(desc.replacingText);
                    }
                }
            }
            if (hasUnresolvedDefinitons) {
                return null;
            }

            for (int i = 0, len = content.length(); i < len; i++) {
                char c = content.charAt(i);
                if (!Character.isSpaceChar(c) && c != '\n' && c != '\r' && c != '\t') {
                    return new JmlMarkupData(context, /*scope, */ content.toString().toCharArray(), descriptors);
                }
            }
            return null;

        } catch (Throwable e) {
            return null;
        }

    }

    public void transform(Statement[] statements) {
        if (statements != null) {
            for (int i = 0; i < statements.length; i++) {
                statements[i] = (Statement) transform(statements[i]);
            }
        }
    }

    public ASTNode transform(AbstractMethodDeclaration stmt) {
        stmt.traverse(new MainVisitor(), (ClassScope) null);
        return stmt;
    }

    public ASTNode transform(TypeDeclaration stmt) {
        stmt.traverse(new MainVisitor(), (ClassScope) null);
        return stmt;
    }

    public ASTNode transform(FieldDeclaration stmt) {
        stmt.traverse(new MainVisitor(), null);
        return stmt;
    }

    public ASTNode transform(ASTNode stmt) {
        if (stmt instanceof Expression) {
            stmt = processExpression((Expression) stmt);
        }
        stmt.traverse(new MainVisitor(), null);
        return stmt;
    }

    private TypeReference createParameterizedType(AdsTypeDeclaration decl, int dimensions) {
        if (decl.getGenericArguments() != null && !decl.getGenericArguments().isEmpty()) {
            TypeReference[] arguments = new TypeReference[decl.getGenericArguments().getArgumentList().size()];
            for (int i = 0; i < arguments.length; i++) {
                arguments[i] = createParameterizedType(decl.getGenericArguments().getArgumentList().get(i).getType(), 0);
            }
            return new AdsParameterizedSingleTypeReference(context, arguments, dimensions, decl.toRawType());
        } else {
            return new AdsTypeReference(context, decl);
        }

    }

    private TypeReference processTypeReference(TypeReference typeRef) {

        if (typeRef instanceof ParameterizedSingleTypeReference) {
            //process parameters first;
            ParameterizedSingleTypeReference pt = (ParameterizedSingleTypeReference) typeRef;
            TypeReference[] references = new TypeReference[pt.typeArguments.length];
            for (int i = 0; i < references.length; i++) {
                references[i] = processTypeReference(pt.typeArguments[i]);
            }

            TagDescriptor tag = getDescriptor(typeRef.sourceStart, typeRef.sourceEnd);

            if (tag != null) {
                if (tag.isTypeDeclaration) {
                    return new AdsParameterizedSingleTypeReference(context, pt, references, tag.typeDeclaration);
                } else {
                    if (tag.definition instanceof AdsMethodDef && ((AdsMethodDef) tag.definition).isConstructor()) {
                        return new AdsParameterizedSingleTypeReference(context, pt, references, ((AdsMethodDef) tag.definition).getOwnerClass());
                    }
                }
            } else {
                return new ParameterizedSingleTypeReference(pt.token, references, pt.dimensions, (long) pt.sourceStart << 32 | pt.sourceEnd);
            }

        } else if (typeRef instanceof SingleTypeReference) {
            TagDescriptor tag = getDescriptor(typeRef.sourceStart, typeRef.sourceEnd);

            if (tag != null) {

                if (tag.isTypeDeclaration) {
                    if (tag.typeDeclaration.getGenericArguments() != null && !tag.typeDeclaration.getGenericArguments().isEmpty()) {
                        return createParameterizedType(tag.typeDeclaration, typeRef.dimensions());
                    } else {
                        if (typeRef instanceof ArrayTypeReference && typeRef.dimensions() > 0) {
                            return new AdsArrayTypeReference((ArrayTypeReference) typeRef, context, tag.typeDeclaration);
                        } else {
                            return new AdsTypeReference(context, tag.typeDeclaration, typeRef.sourceStart, typeRef.sourceEnd);
                        }
                    }
                } else {
                    if (tag.definition instanceof AdsMethodDef && ((AdsMethodDef) tag.definition).isConstructor()) {
                        return new AdsTypeReference(context, ((AdsMethodDef) tag.definition).getOwnerClass(), typeRef.sourceStart, typeRef.sourceEnd);
                    }
                }
            }

        } else if (typeRef instanceof QualifiedTypeReference) {
            QualifiedTypeReference qr = (QualifiedTypeReference) typeRef;
            AdsTypeDeclaration[] typeTokens = new AdsTypeDeclaration[qr.sourcePositions.length];
            char[][] charTokens = new char[qr.sourcePositions.length][];
            boolean hasTags = false;
            Definition definition = null;
            TypeReference[][] paramTypes = null;
            ParameterizedQualifiedTypeReference pt = null;
            if (qr instanceof ParameterizedQualifiedTypeReference) {
                pt = (ParameterizedQualifiedTypeReference) qr;
            }
            if (pt != null) {
                paramTypes = new TypeReference[pt.typeArguments.length][];
            }

            for (int i = 0; i < qr.sourcePositions.length; i++) {
                int start = (int) (qr.sourcePositions[i] >>> 32);
                int end = (int) qr.sourcePositions[i];

                TagDescriptor tag = getDescriptor(start, end);

                if (tag == null) {
                    charTokens[i] = qr.tokens[i];
                } else {
                    if (tag.isTypeDeclaration) {
                        typeTokens[i] = tag.typeDeclaration;
                        hasTags = true;
                        qr.tokens[i] = ("`" + tag.tag.getDisplayName() + "`").toCharArray();
                    }
                }
            }
            if (paramTypes != null) {
                for (int i = 0; i < paramTypes.length; i++) {
                    if (paramTypes[i] != null) {
                        for (int j = 0; j < paramTypes[i].length; j++) {
                            paramTypes[i][j] = processTypeReference(paramTypes[i][j]);
                        }
                    }
                }
            }

            if (paramTypes != null) {
                return new AdsParameterizedQualifiedTypeReference(context.getDefinition(), pt, paramTypes, typeTokens, charTokens);
            } else {
                return new TaggedQualifiedTypeReference(qr, null, typeTokens);
            }
        }


        return typeRef;
    }

    private Expression processExpression(Expression e) {
        return processExpression(e, false);
    }

    private Expression processExpression(Expression e, boolean isRecv) {

        if (e instanceof EqualExpression) {
            return new AdsEqualExpression((EqualExpression) e);
        }
        if (e instanceof CombinedBinaryExpression) {
            return new AdsCombinedBinaryExpression((CombinedBinaryExpression) e);
        }
        if (e instanceof BinaryExpression) {
            AdsBinaryExpression exp = new AdsBinaryExpression((BinaryExpression) e);
            exp.left = processExpression(exp.left);
            exp.right = processExpression(exp.right);
            return exp;
        }
        if (e instanceof Assignment) {
            return new AdsAssignment((Assignment) e);
        }
        if (e instanceof FieldReference) {
            FieldReference fr = (FieldReference) e;
            //ms.receiver = processExpression(ms.receiver);
            int nameSourceStart = (int) (fr.nameSourcePosition >> 32);

            TagDescriptor tag = getDescriptor(nameSourceStart, e.sourceEnd);
            if (tag == null) {
                return e;
            }

            if (tag.isInvocation) {
                if (tag.definition instanceof AdsPropertyDef) {
                    AdsPropertyReference result = new AdsPropertyReference(context, (AdsPropertyDef) tag.definition, (FieldReference) e);
                    result.receiver = ((FieldReference) e).receiver;
                    return result;
                } else {
                    System.err.println("");
                }
            }
        } else if (e instanceof SingleNameReference) {
            TagDescriptor tag = getDescriptor(e.sourceStart, e.sourceEnd);
            if (tag != null) {

                if (tag.isTypeDeclaration) {
                    if (isRecv) {
                        //field or messageSend recv has to be name reference, not type reference
                        return new TaggedSingleNameReference((SingleNameReference) e, context, tag.typeDeclaration);
                    }
                    return new AdsTypeReference(tag.definition, tag.typeDeclaration, e.sourceStart, e.sourceEnd);
                }
                if (tag.definition != null) {
                    if (tag.isInvocation) {
                        return new TaggedSingleNameReference((SingleNameReference) e, this.context, tag.definition, tag.isInternalPropAccessor);
                    } else {//id reference
                        Expression ref = BaseGenerator.createIdInvocation(tag.definition.getId());
//                        ref.receiver = new QualifiedNameReference(BaseGenerator.IDFACTORY_TYPE_NAME, new long[BaseGenerator.IDFACTORY_TYPE_NAME.length], e.sourceStart, e.sourceEnd);
//                        ref.selector = "loadFrom".toCharArray();
//                        ref.arguments = new Expression[]{
//                            new StringLiteral(tag.definition.getId().toCharArray(), 0, 0, 0)
//                        };
                        return ref;
                    }
                }
            }
            return new ReenterableSingleNameReference((SingleNameReference) e);
        } else if (e instanceof QualifiedNameReference) {
            QualifiedNameReference qr = (QualifiedNameReference) e;
            Jml.Tag[] defs = new Jml.Tag[qr.tokens.length];
            boolean hasDefs = false;
            for (int i = 0; i < qr.sourcePositions.length; i++) {
                int start = (int) (qr.sourcePositions[i] >>> 32);
                int end = (int) qr.sourcePositions[i];
                TagDescriptor tag = getDescriptor(start, end);
                if (tag != null && tag.isInvocation) {
                    defs[i] = tag.tag;
                    qr.tokens[i] = ("`" + tag.tag.getDisplayName() + "`").toCharArray();
                    hasDefs = true;
                }
            }
            if (hasDefs) {
                return new TaggedQuelifiedNameReference(qr, context, defs);
            } else {
                return new ReenterableQualifiedNameReference((QualifiedNameReference) e);
            }
        } else if (e instanceof MessageSend) {

            MessageSend ms = (MessageSend) e;
            //ms.receiver = processExpression(ms.receiver);
            int nameSourceStart = (int) (ms.nameSourcePosition >> 32);

            TagDescriptor tag = getDescriptor(nameSourceStart, e.sourceEnd);
            if (tag == null) {
                return e;
            } else {
                ms = new AdsMessageSend(ms, tag.definition, (JmlTagInvocation) tag.tag);
                /*if (scope instanceof ClassScope) {
                 ms.traverse(new MainVisitor(), (ClassScope) scope);
                 } else {
                 ms.traverse(new MainVisitor(), (BlockScope) scope);
                 }*/
                return ms;
            }
        }

        return e;
    }

    private TagDescriptor getDescriptor(int start, int end) {
        for (TagDescriptor desc : this.tagInfo) {
            if (desc.start > start) {
                return null;
            }
            if (desc.start == start) {
                return desc;
            }
        }
        return null;
    }

    private class MainVisitor extends ASTVisitor {

        @Override
        public void endVisit(MessageSend ms, BlockScope scope) {
            if (ms.arguments != null) {
                for (int i = 0; i < ms.arguments.length; i++) {
                    ms.arguments[i] = processExpression(ms.arguments[i]);
                }
            }
            if (ms.receiver != null) {
                ms.receiver = processExpression(ms.receiver, true);
            }
            super.endVisit(ms, scope);
        }

        @Override
        public void endVisit(FieldReference fieldReference, BlockScope scope) {
            if (fieldReference.receiver != null) {
                fieldReference.receiver = processExpression(fieldReference.receiver, true);
            }
            super.endVisit(fieldReference, scope);
        }

        @Override
        public void endVisit(FieldReference fieldReference, ClassScope scope) {
            if (fieldReference.receiver != null) {
                fieldReference.receiver = processExpression(fieldReference.receiver, true);
            }
        }

        @Override
        public void endVisit(ClassLiteralAccess classLiteral, BlockScope scope) {
            classLiteral.type = processTypeReference(classLiteral.type);
            super.endVisit(classLiteral, scope); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void endVisit(FieldDeclaration fieldDeclaration, MethodScope scope) {
            if (fieldDeclaration.type != null) {
                fieldDeclaration.type = processTypeReference(fieldDeclaration.type);
            }
            if (fieldDeclaration.initialization != null) {
                fieldDeclaration.initialization = processExpression(fieldDeclaration.initialization);
            }
        }

        @Override
        public void endVisit(Block block, BlockScope scope) {
            if (block.statements != null) {
                for (int i = 0; i < block.statements.length; i++) {
                    if (block.statements[i] instanceof Expression) {
                        block.statements[i] = processExpression((Expression) block.statements[i]);
                    }
                }
            }
            super.endVisit(block, scope); //To change body of generated methods, choose Tools | Templates.
        }

        private void processMethodParts(AbstractMethodDeclaration methodDeclaration) {
            if (methodDeclaration.annotations != null) {
                int annotationsLength = methodDeclaration.annotations.length;
                for (int i = 0; i < annotationsLength; i++) {
                    methodDeclaration.annotations[i] = (Annotation) processExpression(methodDeclaration.annotations[i]);
                }
            }


            if (methodDeclaration.thrownExceptions != null) {
                int thrownExceptionsLength = methodDeclaration.thrownExceptions.length;
                for (int i = 0; i < thrownExceptionsLength; i++) {
                    methodDeclaration.thrownExceptions[i] = processTypeReference(methodDeclaration.thrownExceptions[i]);
                }
            }

            if (methodDeclaration.statements != null) {
                for (int i = 0; i < methodDeclaration.statements.length; i++) {
                    if (methodDeclaration.statements[i] instanceof MessageSend) {
                        methodDeclaration.statements[i] = processExpression((Expression) methodDeclaration.statements[i]);
                    }
                }
            }
        }

        @Override
        public void endVisit(MethodDeclaration methodDeclaration, ClassScope scope) {
            processMethodParts(methodDeclaration);
            if (methodDeclaration.returnType != null) {
                methodDeclaration.returnType = processTypeReference(methodDeclaration.returnType);
            }
            super.endVisit(methodDeclaration, scope);
        }

        @Override
        public void endVisit(ConstructorDeclaration methodDeclaration, ClassScope scope) {
            processMethodParts(methodDeclaration);
            super.endVisit(methodDeclaration, scope);
        }

        @Override
        public void endVisit(TypeDeclaration localTypeDeclaration, BlockScope scope) {
            super.endVisit(localTypeDeclaration, scope);
        }

        @Override
        public void endVisit(TypeDeclaration memberTypeDeclaration, ClassScope scope) {
            super.endVisit(memberTypeDeclaration, scope);
        }

        @Override
        public void endVisit(TypeDeclaration typeDeclaration, CompilationUnitScope scope) {
            super.endVisit(typeDeclaration, scope);
        }

        @Override
        public void endVisit(Assignment assignment, BlockScope scope) {
            assignment.lhs = processExpression(assignment.lhs);
            assignment.expression = processExpression(assignment.expression);
            super.endVisit(assignment, scope);
        }

        @Override
        public void endVisit(BinaryExpression binaryExpression, BlockScope scope) {
            binaryExpression.left = processExpression(binaryExpression.left);
            binaryExpression.right = processExpression(binaryExpression.right);
            super.endVisit(binaryExpression, scope);
        }

        @Override
        public void endVisit(AND_AND_Expression and_and_Expression, BlockScope scope) {
            and_and_Expression.left = processExpression(and_and_Expression.left);
            and_and_Expression.right = processExpression(and_and_Expression.right);
            super.endVisit(and_and_Expression, scope);
        }

        @Override
        public void endVisit(EqualExpression equalExpression, BlockScope scope) {
            equalExpression.left = processExpression(equalExpression.left);
            equalExpression.right = processExpression(equalExpression.right);
            super.endVisit(equalExpression, scope);
        }

        @Override
        public void endVisit(OR_OR_Expression or_or_Expression, BlockScope scope) {
            or_or_Expression.left = processExpression(or_or_Expression.left);
            or_or_Expression.right = processExpression(or_or_Expression.right);
            super.endVisit(or_or_Expression, scope);
        }

        @Override
        public void endVisit(LocalDeclaration localDeclaration, BlockScope scope) {
            localDeclaration.type = processTypeReference(localDeclaration.type);
            if (localDeclaration.initialization != null) {
                localDeclaration.initialization = processExpression(localDeclaration.initialization);
            }
            super.endVisit(localDeclaration, scope);
        }

        @Override
        public void endVisit(AllocationExpression allocationExpression, BlockScope scope) {
            allocationExpression.type = processTypeReference(allocationExpression.type);
            if (allocationExpression.arguments != null) {
                for (int i = 0; i < allocationExpression.arguments.length; i++) {
                    allocationExpression.arguments[i] = processExpression(allocationExpression.arguments[i]);
                }
            }
            super.endVisit(allocationExpression, scope);
        }

        @Override
        public void endVisit(ArrayAllocationExpression allocationExpression, BlockScope scope) {
            allocationExpression.type = processTypeReference(allocationExpression.type);

            super.endVisit(allocationExpression, scope);
        }

        @Override
        public void endVisit(ArrayInitializer arrayInitializer, BlockScope scope) {
            if (arrayInitializer.expressions != null) {
                for (int i = 0; i < arrayInitializer.expressions.length; i++) {
                    arrayInitializer.expressions[i] = processExpression(arrayInitializer.expressions[i]);
                }
            }

            super.endVisit(arrayInitializer, scope);
        }

        @Override
        public void endVisit(Argument argument, BlockScope scope) {
            argument.type = processTypeReference(argument.type);
            super.endVisit(argument, scope);
        }

        @Override
        public void endVisit(CastExpression castExpression, BlockScope scope) {
            castExpression.type = processTypeReference(castExpression.type);
            super.endVisit(castExpression, scope);
        }

        @Override
        public void endVisit(ReturnStatement returnStatement, BlockScope scope) {
            if (returnStatement.expression != null) {
                returnStatement.expression = processExpression(returnStatement.expression);
            }
            super.endVisit(returnStatement, scope); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void endVisit(IfStatement ifStatement, BlockScope scope) {
            if (ifStatement.condition != null) {
                ifStatement.condition = processExpression(ifStatement.condition);
            }
            if (ifStatement.thenStatement instanceof Expression) {
                ifStatement.thenStatement = processExpression((Expression) ifStatement.thenStatement);
            }
            if (ifStatement.elseStatement instanceof Expression) {
                ifStatement.elseStatement = processExpression((Expression) ifStatement.elseStatement);
            }
            super.endVisit(ifStatement, scope);
        }

        @Override
        public void endVisit(Argument argument, ClassScope scope) {
            if (argument.initialization != null) {
                argument.initialization = processExpression(argument.initialization);
            }
            super.endVisit(argument, scope);
        }

        @Override
        public void endVisit(DoStatement doStatement, BlockScope scope) {
            if (doStatement.action instanceof Expression) {
                doStatement.action = processExpression((Expression) doStatement.action);
            }
            if (doStatement.condition != null) {
                doStatement.condition = processExpression(doStatement.condition);
            }
            super.endVisit(doStatement, scope);
        }

        @Override
        public void endVisit(ForeachStatement forStatement, BlockScope scope) {
            if (forStatement.action instanceof Expression) {
                forStatement.action = processExpression((Expression) forStatement.action);
            }
            if (forStatement.collection != null) {
                forStatement.collection = processExpression(forStatement.collection);
            }

            super.endVisit(forStatement, scope);
        }

        @Override
        public void endVisit(ForStatement forStatement, BlockScope scope) {
            if (forStatement.action instanceof Expression) {
                forStatement.action = processExpression((Expression) forStatement.action);
            }
            if (forStatement.condition != null) {
                forStatement.condition = processExpression(forStatement.condition);
            }
            if (forStatement.initializations != null) {
                for (int i = 0; i < forStatement.initializations.length; i++) {
                    if (forStatement.initializations[i] instanceof Expression) {
                        forStatement.initializations[i] = processExpression((Expression) forStatement.initializations[i]);
                    }
                }
            }
            if (forStatement.increments != null) {
                for (int i = 0; i < forStatement.increments.length; i++) {
                    if (forStatement.increments[i] instanceof Expression) {
                        forStatement.increments[i] = processExpression((Expression) forStatement.increments[i]);
                    }
                }
            }
            super.endVisit(forStatement, scope);
        }

        @Override
        public void endVisit(ConditionalExpression conditionalExpression, BlockScope scope) {
            if (conditionalExpression.condition != null) {
                conditionalExpression.condition = processExpression(conditionalExpression.condition);
            }
            if (conditionalExpression.valueIfTrue != null) {
                conditionalExpression.valueIfTrue = processExpression(conditionalExpression.valueIfTrue);
            }
            if (conditionalExpression.valueIfFalse != null) {
                conditionalExpression.valueIfFalse = processExpression(conditionalExpression.valueIfFalse);
            }
            super.endVisit(conditionalExpression, scope);
        }
    }
}
