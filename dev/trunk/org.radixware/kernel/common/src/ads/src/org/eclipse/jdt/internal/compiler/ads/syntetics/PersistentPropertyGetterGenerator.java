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

import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.ast.AllocationExpression;
import org.eclipse.jdt.internal.compiler.ast.Argument;
import org.eclipse.jdt.internal.compiler.ast.Assignment;
import org.eclipse.jdt.internal.compiler.ast.Block;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.FalseLiteral;
import org.eclipse.jdt.internal.compiler.ast.FieldReference;
import org.eclipse.jdt.internal.compiler.ast.IfStatement;
import org.eclipse.jdt.internal.compiler.ast.LocalDeclaration;
import org.eclipse.jdt.internal.compiler.ast.MessageSend;
import org.eclipse.jdt.internal.compiler.ast.NullLiteral;
import org.eclipse.jdt.internal.compiler.ast.QualifiedNameReference;
import org.eclipse.jdt.internal.compiler.ast.QualifiedTypeReference;
import org.eclipse.jdt.internal.compiler.ast.ReturnStatement;
import org.eclipse.jdt.internal.compiler.ast.SingleNameReference;
import org.eclipse.jdt.internal.compiler.ast.Statement;
import org.eclipse.jdt.internal.compiler.ast.StringLiteral;
import org.eclipse.jdt.internal.compiler.ast.ThisReference;
import org.eclipse.jdt.internal.compiler.ast.TryStatement;
import org.eclipse.jdt.internal.compiler.impl.ReferenceContext;
import org.eclipse.jdt.internal.compiler.lookup.AdsPropertyDeclaration;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.ICompilable;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsUserPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.ColumnProperty;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.enums.EValType;


abstract class PersistentPropertyGetterGenerator<T extends AdsPropertyDef> extends PropertyGetterGenerator<T> {

    public PersistentPropertyGetterGenerator(Definition referenceContext, CompilationResult result, ReferenceContext context) {
        super(referenceContext, result, context);
    }

    protected Expression createServerSideArteAccessMethodInvocation() {
        return null;
    }

    private MessageSend createSrvValAsStrToStrCall(char[] varName) {
        MessageSend call = new MessageSend();
        call.receiver = new QualifiedNameReference(SRVVALASSTR_TYPE_NAME, new long[SRVVALASSTR_TYPE_NAME.length], 0, 0);
        call.selector = "toStr".toCharArray();

        FieldReference clobEnumField = new FieldReference("CLOB".toCharArray(), 0);
        clobEnumField.receiver = new QualifiedNameReference(EVALTYPE_TYPE_NAME, new long[EVALTYPE_TYPE_NAME.length], 0, 0);
        call.arguments = new Expression[]{
            createServerSideArteAccessMethodInvocation(),
            new SingleNameReference(varName, 0),
            clobEnumField
        };
        return call;
    }

    private Expression createXmlOptions() {
        MessageSend setCharEncoding = new MessageSend();
        AllocationExpression alloc = new AllocationExpression();
        alloc.type = new QualifiedTypeReference(XMLOPTIONS_TYPE_NAME, new long[XMLOPTIONS_TYPE_NAME.length]);
        setCharEncoding.receiver = alloc;

        setCharEncoding.arguments = new Expression[]{
            new StringLiteral("UTF-8".toCharArray(), 0, 0, 0)
        };
        return setCharEncoding;
    }

    @Override
    protected Statement createExtendedValueConversionAfterGetFromSystemHandler(char[] varName, T property, AdsPropertyDeclaration propDecl, AdsType type) {
        if (property.getValue().getType().getTypeId() == EValType.STR && property instanceof ColumnProperty) {
            DdsColumnDef c = ((ColumnProperty) property).getColumnInfo().findColumn();
            if (c == null) {
                return null;
            }
            EValType realType = c.getValType();

            if (realType == EValType.CLOB) {
                IfStatement typeCheckCondition = new IfStatement(
                        createInstanceOfCheck(varName, propDecl.type),
                        new ReturnStatement(createCastExpression(varName, propDecl.type), 0, 0),
                        new IfStatement(createNotNullCheck(varName),
                        new Assignment(new SingleNameReference(varName, 0),
                        createSrvValAsStrToStrCall(varName), 0), 0, 0), 0, 0);

                return typeCheckCondition;
            }
        }
        if (property.getValue().getType().getTypeId() == EValType.XML && (property instanceof ColumnProperty || property instanceof AdsUserPropertyDef)) {
            EValType realType;
            if (property instanceof ColumnProperty) {
                DdsColumnDef c = ((ColumnProperty) property).getColumnInfo().findColumn();
                if (c == null) {
                    return null;
                }
                realType = c.getValType();
            } else {
                realType = EValType.CLOB;
            }

            if (realType == EValType.CLOB || realType == EValType.BLOB) {

                LocalDeclaration input = new LocalDeclaration("stream".toCharArray(), 0, 0);
                input.initialization = new NullLiteral(0, 0);
                input.type = new QualifiedTypeReference(realType == EValType.BLOB ? INPUTSTREAM_TYPE_NAME : READER_TYPE_NAME, new long[3]);

                TryStatement tryStmt = new TryStatement();
                MessageSend streamInitCall = new MessageSend();
                if (realType == EValType.CLOB) {
                    streamInitCall.selector = "getCharacterStream".toCharArray();
                    streamInitCall.receiver = createCastExpression(varName, new QualifiedTypeReference(CLOB_TYPE_NAME, new long[3]));
                } else {
                    streamInitCall.selector = "getBinaryStream".toCharArray();
                    streamInitCall.receiver = createCastExpression(varName, new QualifiedTypeReference(BLOB_TYPE_NAME, new long[3]));
                }


                //stream = ((CLOB or BLOB)var).getStream();

                //var = XMLTYPE.Factory.parse(stream,new XmlOptions().setCharacterEncoding("UTF_8"))
                MessageSend parseStreamCall = new MessageSend();
                parseStreamCall.selector = "parse".toCharArray();

                //TEMPORARY DESIGION FOR XML TYPES
                char[][] xmlTypeName = CharOperation.splitOn('.', type.getFullJavaClassName(JavaSourceSupport.UsagePurpose.getPurpose(ERuntimeEnvironmentType.COMMON, JavaSourceSupport.CodeType.EXCUTABLE)));
                char[][] factoryName = new char[xmlTypeName.length + 1][];
                System.arraycopy(xmlTypeName, 0, factoryName, 0, xmlTypeName.length);
                factoryName[xmlTypeName.length] = "Factory".toCharArray();
                parseStreamCall.receiver = new QualifiedNameReference(factoryName, new long[factoryName.length], 0, 0);

                parseStreamCall.arguments = new Expression[]{
                    new SingleNameReference(varName, 0),
                    createXmlOptions()};

                Assignment writeParseResult = new Assignment(new SingleNameReference(varName, 0), parseStreamCall, 0);

                //refinePropVal
                MessageSend refine = new MessageSend();
                refine.selector = "refinePropVal".toCharArray();
                refine.receiver = ThisReference.implicitThis();
                refine.arguments = new Expression[]{
                    createIdAccessExpression(property),
                    new SingleNameReference(varName, 0),
                    new FalseLiteral(0, 0)
                };


                tryStmt.tryBlock = createBlock(new Assignment(new SingleNameReference(varName, 0), streamInitCall, 0), writeParseResult, refine);

                tryStmt.catchArguments = new Argument[]{
                    new Argument("e".toCharArray(), 0, new QualifiedTypeReference(SQLEXCEPTION_TYPE_NAME, new long[3]), 0),
                    new Argument("e".toCharArray(), 1, new QualifiedTypeReference(IOEXCEPTION_TYPE_NAME, new long[3]), 0),
                    new Argument("e".toCharArray(), 2, new QualifiedTypeReference(XMLEXCEPTION_TYPE_NAME, new long[XMLEXCEPTION_TYPE_NAME.length]), 0),};


                tryStmt.catchBlocks = new Block[]{
                    createBlock(createThrow(DATABASEERROR_TYPE_NAME, "Unable to parse xml data", true)),
                    createBlock(createThrow(WRONGFORMATERROR_TYPE_NAME, "Unable to parse xml data", true)),
                    createBlock(createThrow(WRONGFORMATERROR_TYPE_NAME, "Unable to parse xml data", true))
                };

                TryStatement tryCloseInput = new TryStatement();
                MessageSend closeInput = new MessageSend();
                closeInput.receiver = new SingleNameReference(input.name, 0);
                closeInput.selector = "close".toCharArray();
                tryCloseInput.tryBlock = createBlock(closeInput);
                tryCloseInput.catchArguments = new Argument[]{
                    new Argument("e".toCharArray(), 0, new QualifiedTypeReference(IOEXCEPTION_TYPE_NAME, new long[3]), 0)
                };
                tryCloseInput.catchBlocks = new Block[]{new Block(0)};

                tryStmt.finallyBlock = createBlock(new IfStatement(createNotNullCheck(input.name), tryCloseInput, 0, 0));

                Block valueConversionBlock = createBlock(tryStmt);
                IfStatement typeCheckCondition = new IfStatement(
                        createInstanceOfCheck(varName, propDecl.type),
                        new ReturnStatement(createCastExpression(varName, propDecl.type), 0, 0),
                        new IfStatement(createNotNullCheck(varName),
                        valueConversionBlock, 0, 0), 0, 0);

                return typeCheckCondition;
            }
        }
        return super.createExtendedValueConversionAfterGetFromSystemHandler(varName, property, propDecl, type);
    }
}
