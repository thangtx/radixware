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

package org.eclipse.jdt.internal.compiler.ads.syntetics;

import java.util.LinkedList;
import java.util.List;
import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.ast.AllocationExpression;
import org.eclipse.jdt.internal.compiler.ast.Block;
import org.eclipse.jdt.internal.compiler.ast.CastExpression;
import org.eclipse.jdt.internal.compiler.ast.ConditionalExpression;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.IfStatement;
import org.eclipse.jdt.internal.compiler.ast.LocalDeclaration;
import org.eclipse.jdt.internal.compiler.ast.MessageSend;
import org.eclipse.jdt.internal.compiler.ast.NullLiteral;
import org.eclipse.jdt.internal.compiler.ast.ReturnStatement;
import org.eclipse.jdt.internal.compiler.ast.SingleNameReference;
import org.eclipse.jdt.internal.compiler.ast.SingleTypeReference;
import org.eclipse.jdt.internal.compiler.ast.Statement;
import org.eclipse.jdt.internal.compiler.ast.TaggedSingleNameReference;
import org.eclipse.jdt.internal.compiler.ast.ThisReference;
import org.eclipse.jdt.internal.compiler.ast.TypeReference;
import org.eclipse.jdt.internal.compiler.impl.ReferenceContext;
import org.eclipse.jdt.internal.compiler.lookup.AdsPropertyDeclaration;
import org.eclipse.jdt.internal.compiler.ast.AdsTypeReference;
import org.eclipse.jdt.internal.compiler.ast.FieldDeclaration;
import org.eclipse.jdt.internal.compiler.ast.SuperReference;
import org.eclipse.jdt.internal.compiler.lookup.CompilationUnitScope;
import org.radixware.kernel.common.compiler.core.JmlHelper;
import org.radixware.kernel.common.compiler.core.problems.AdsProblemReporter;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.type.AdsEnumType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.enums.EPropNature;
import static org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP;
import static org.radixware.kernel.common.enums.EPropNature.INNATE;
import static org.radixware.kernel.common.enums.EPropNature.PROPERTY_PRESENTATION;
import static org.radixware.kernel.common.enums.EPropNature.USER;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.jml.Jml;


public abstract class PropertyGetterGenerator<T extends AdsPropertyDef> extends BaseGenerator {

    protected final Definition contextDefinition;

    public PropertyGetterGenerator(Definition referenceContext, CompilationResult result, ReferenceContext context) {
        super(result, context);
        this.contextDefinition = referenceContext;

    }

    protected boolean isIdRequiredForSystemValueGet() {
        return true;
    }

    protected Expression createIdAccessExpression(T property) {
        return createIdInvocation(property.getId());
    }

    private Expression createSetterInvocation(T property, char[] variableName) {
        return null;
    }

    private Expression createGetterInvocation(T property, char[] variableName) {
        return null;
    }

    protected char[] getNativeAccessorSelector() {
        return null;
    }

    private Expression createNativeGetterInvocation(T property) {
        MessageSend getter = new MessageSend();
        getter.receiver = ThisReference.implicitThis();
        getter.selector = getNativeAccessorSelector();

        if (isIdRequiredForSystemValueGet()) {
            getter.arguments = new Expression[]{
                createIdAccessExpression(property)
            };
        }
        return getter;
    }

    protected final Statement[] createVisibleGetterBody(PropertyGeneratorFlowInfo flowInfo, CompilationUnitScope scope, AdsPropertyDeclaration propDecl, AdsProblemReporter reporter) {
        List<Statement> stmts = new LinkedList<>();
        final AdsPropertyDef.Getter getter = flowInfo.findGetter();
        boolean written = false;
        if (getter != null) {
            Jml src = getter.getSource(propDecl.getEnvironmentType());
            if (src != null) {
                Statement[] parsedBody = JmlHelper.parseStatements(scope, flowInfo.getPropertyDef(), referenceContext, referenceContext.getCompilationUnitDeclaration(), src);
                wrapWithProfilerCode(getter, parsedBody, stmts);
                written = true;
            }
        }
        if (!written) {
            Expression whatToReturn;

            AdsPropertyDef ovr = flowInfo.findOverriddenProp();
            AdsPropertyDef prop = flowInfo.getPropertyDef();
            if (ovr != null && prop.getNature() == EPropNature.PROPERTY_PRESENTATION) {
                whatToReturn = createMessageSend(new SuperReference(0, 0), ("get" + prop.getId().toString()));
            } else {
                whatToReturn = createMessageSend(ThisReference.implicitThis(), ("get" + prop.getId().toString() + "$$$"));
            }
            if (flowInfo.isRefine()) {
                whatToReturn = createCastExpression(whatToReturn, propDecl.type);
            }

            stmts.add(new ReturnStatement(whatToReturn, 0, 0));
        }
        return stmts.toArray(new Statement[stmts.size()]);
    }

    protected Statement[] createGetterBody(T property, AdsPropertyDeclaration propDecl, AdsProblemReporter reporter) {
        final AdsType type = property.getValue().getType().resolve(contextDefinition.getDefinition()).get();

        if (type == null) {
            return null;
        }
        List<Statement> stmts = new LinkedList<>();
        if (type instanceof AdsEnumType) {
            LocalDeclaration dummyDeclaration = new LocalDeclaration("dummy".toCharArray(), 0, 0);
            dummyDeclaration.initialization = createNativeGetterInvocation(property);
            dummyDeclaration.type = new SingleTypeReference(TypeReference.OBJECT, 0);
            stmts.add(dummyDeclaration);

            Block actionIfDummyTypeIsIncorrect = new Block(0);

            IfStatement instanceofOnDummy = new IfStatement(
                    createInstanceOfCheck(dummyDeclaration.name, propDecl.type),
                    new ReturnStatement(
                    new CastExpression(new SingleNameReference(dummyDeclaration.name, 0), propDecl.type), 0, 0),
                    actionIfDummyTypeIsIncorrect, 0, 0);
            IfStatement choiceOnDummy = new IfStatement(createNullCheck(dummyDeclaration.name), new ReturnStatement(new NullLiteral(0, 0), 0, 0), instanceofOnDummy, 0, 0);
            stmts.add(choiceOnDummy);

            //fill value conversion and refinement
            Expression actionIfDummyTypeIsIncorrectExpression;
            char[] dummy2 = "dummy2".toCharArray();

            switch (property.getNature()) {
                case INNATE:
                case USER:
                case DETAIL_PROP:
                    MessageSend refinePropVal = new MessageSend();
                    refinePropVal.selector = "refinePropVal".toCharArray();
                    refinePropVal.receiver = ThisReference.implicitThis();
                    refinePropVal.arguments = new Expression[]{
                        createIdAccessExpression(property),
                        new SingleNameReference(dummy2, 0)
                    };
                    actionIfDummyTypeIsIncorrectExpression = refinePropVal;
                    break;
                case PROPERTY_PRESENTATION:
                    MessageSend refineValue = new MessageSend();
                    refineValue.selector = "refineValue".toCharArray();
                    refineValue.receiver = ThisReference.implicitThis();
                    refineValue.arguments = new Expression[]{
                        new SingleNameReference(dummy2, 0)
                    };
                    actionIfDummyTypeIsIncorrectExpression = refineValue;
                    break;
                default:
                    actionIfDummyTypeIsIncorrectExpression = createSetterInvocation(property, dummy2);
            }
            actionIfDummyTypeIsIncorrect.statements = new Statement[]{
                createEnumBasedTypeValueConversion(type, dummyDeclaration.name, dummy2, false, propDecl),
                actionIfDummyTypeIsIncorrectExpression,
                new ReturnStatement(new SingleNameReference(dummy2, 0), 0, 0)
            };
        } else {
            LocalDeclaration result = new LocalDeclaration("result".toCharArray(), 0, 0);
            result.initialization = createNativeGetterInvocation(property);
            result.type = new SingleTypeReference(TypeReference.OBJECT, 0);
            stmts.add(result);
            Statement conversion = createExtendedValueConversionAfterGetFromSystemHandler("result".toCharArray(), property, propDecl, type);
            if (conversion != null) {
                stmts.add(conversion);
            }

            stmts.add(new ReturnStatement(
                    new CastExpression(
                    new SingleNameReference(result.name, 0),
                    propDecl.type), 0, 0));


        }
        return stmts.toArray(new Statement[stmts.size()]);
    }

    protected final Statement createEnumBasedTypeValueConversion(AdsType type, final char[] srcName, final char[] recvName, final boolean fullCheck, AdsPropertyDeclaration propDecl) {
        LocalDeclaration recv = new LocalDeclaration(recvName, 0, 0);
        recv.type = propDecl.type;
        Expression typeConversion;

        if (type instanceof AdsEnumType.Array) {
            AllocationExpression alloc = new AllocationExpression();
            alloc.type = propDecl.type;
            AdsTypeReference simpleArrayType = new AdsTypeReference(contextDefinition, AdsTypeDeclaration.Factory.newInstance(((AdsEnumType) type).getSource().getItemType().getArrayType()));
            alloc.arguments = new Expression[]{
                createCastExpression(srcName, simpleArrayType)
            };
            typeConversion = alloc;
        } else {
            final AdsEnumType enumType = (AdsEnumType) type;
            AdsEnumDef source = enumType.getSource();
            AdsTypeReference simpleType = new AdsTypeReference(contextDefinition, AdsTypeDeclaration.Factory.newInstance(source.getItemType()));

            MessageSend ms = new MessageSend();
            ms.selector = "getForValue".toCharArray();
            ms.receiver = new TaggedSingleNameReference(contextDefinition, source, false);
            Expression argument = createCastExpression(srcName, simpleType);
            if (source.getItemType() == EValType.INT) {
                MessageSend longValue = new MessageSend();
                longValue.selector = "longValue".toCharArray();
                longValue.receiver = argument;
                argument = longValue;
            }
            ms.arguments = new Expression[]{
                argument
            };

            typeConversion = ms;
        }

        if (fullCheck) {
            ConditionalExpression conditionOnSrc = new ConditionalExpression(createNullCheck(srcName), new NullLiteral(0, 0),
                    new ConditionalExpression(createInstanceOfCheck(srcName, propDecl.type), createCastExpression(srcName, propDecl.type), typeConversion));
            recv.initialization = conditionOnSrc;
        } else {
            recv.initialization = typeConversion;
        }
        return recv;
    }

    protected Statement createExtendedValueConversionAfterGetFromSystemHandler(char[] varName, T property, AdsPropertyDeclaration propDecl, AdsType type) {
        return null;
    }

    public void createBackstoreFields(AdsPropertyDef property, AdsPropertyDeclaration propDecl, List<FieldDeclaration> fields) {
    }
}