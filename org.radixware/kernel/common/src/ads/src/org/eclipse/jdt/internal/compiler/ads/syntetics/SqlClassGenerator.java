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
import static org.eclipse.jdt.internal.compiler.ads.syntetics.BaseGenerator.createAlloc;
import static org.eclipse.jdt.internal.compiler.ads.syntetics.BaseGenerator.createBooleanConstant;
import static org.eclipse.jdt.internal.compiler.ads.syntetics.BaseGenerator.createEnumFieldRef;
import static org.eclipse.jdt.internal.compiler.ads.syntetics.BaseGenerator.createIntConstant;
import static org.eclipse.jdt.internal.compiler.ads.syntetics.BaseGenerator.createMessageSend;
import org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.AdsTypeReference;
import org.eclipse.jdt.internal.compiler.ast.Annotation;
import org.eclipse.jdt.internal.compiler.ast.Argument;
import org.eclipse.jdt.internal.compiler.ast.Clinit;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.FieldDeclaration;
import org.eclipse.jdt.internal.compiler.ast.MethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.ReturnStatement;
import org.eclipse.jdt.internal.compiler.ast.SingleNameReference;
import org.eclipse.jdt.internal.compiler.ast.SingleTypeReference;
import org.eclipse.jdt.internal.compiler.ast.Statement;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.eclipse.jdt.internal.compiler.ast.TypeReference;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.impl.ReferenceContext;
import org.eclipse.jdt.internal.compiler.lookup.CompilationUnitScope;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsParameterPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsSystemMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.MethodParameter;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsCursorClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsSqlClassDef;
import org.radixware.kernel.common.defs.ads.src.clazz.sql.AdsSqlClassExecuteWriter;
import static org.radixware.kernel.common.defs.ads.src.clazz.sql.AdsSqlClassWriter.TEST_METHOD_NAME;
import org.radixware.kernel.common.enums.EClassType;


public class SqlClassGenerator extends BaseGenerator {

    public SqlClassGenerator(CompilationResult compilationResult, ReferenceContext referenceContext) {
        super(compilationResult, referenceContext);
    }

    public void addSpecificInnerTypesAndMethods(AdsSqlClassDef sqlClass, CompilationUnitScope scope, List<TypeDeclaration> types, List<AbstractMethodDeclaration> methods, List<FieldDeclaration> fields) {
        createPreparedStatementCacheInstantiation(sqlClass, methods, fields);

        if (sqlClass.getClassDefType() == EClassType.SQL_STATEMENT) {
            createStaticCacheRegistration(sqlClass, methods);
        }
        createTestPreparationMethod(sqlClass, methods);
    }

    private void createPreparedStatementCacheInstantiation(AdsSqlClassDef sqlClass, List<AbstractMethodDeclaration> methods, List<FieldDeclaration> fields) {
        FieldDeclaration cacheField = new FieldDeclaration("$preparedStatementsCache".toCharArray(), 0, 0);
        cacheField.modifiers = ClassFileConstants.AccPrivate | ClassFileConstants.AccStatic | ClassFileConstants.AccFinal;
        cacheField.type = new SingleTypeReference("PreparedStatementsCache".toCharArray(), 0);
        final EClassType classType = sqlClass.getClassDefType();

        List<Expression> initArgs = new LinkedList<>();

        initArgs.add(createEnumFieldRef(classType));
        initArgs.add(createIntConstant(sqlClass.getCacheSize()));


        if (classType == EClassType.SQL_CURSOR || classType == EClassType.REPORT) {
            boolean uniDirectCursor = (classType == EClassType.REPORT || ((AdsCursorClassDef) sqlClass).isUniDirect());
            initArgs.add(createBooleanConstant(uniDirectCursor));

            boolean readOnlyCursor = (classType == EClassType.REPORT || ((AdsCursorClassDef) sqlClass).isDbReadOnly());
            initArgs.add(createBooleanConstant(readOnlyCursor));
        }

        cacheField.initialization = createAlloc(new SingleTypeReference("PreparedStatementsCache".toCharArray(), 0), initArgs.toArray(new Expression[initArgs.size()]));

        fields.add(cacheField);

        if (classType == EClassType.SQL_CURSOR || classType == EClassType.REPORT) {
            MethodDeclaration method = createMethod("getPreparedStatementsCache", true, new SingleTypeReference("PreparedStatementsCache".toCharArray(), 0), ClassFileConstants.AccProtected);
            method.statements = new Statement[]{
                new ReturnStatement(new SingleNameReference("$preparedStatementsCache".toCharArray(), 0), 0, 0)
            };
            methods.add(method);
        }
    }

    private void createStaticCacheRegistration(AdsSqlClassDef sqlClass, List<AbstractMethodDeclaration> methods) {
        Clinit staticInit = new Clinit(compilationResult);
        staticInit.statements = new Statement[]{
            createMessageSend(createArteAccessMethodInvocation(sqlClass), "registerStatementsCache", new SingleNameReference("$preparedStatementsCache".toCharArray(), 0))
        };
        methods.add(staticInit);
    }

    private void createTestPreparationMethod(AdsSqlClassDef sqlClass, List<AbstractMethodDeclaration> methods) {
        MethodDeclaration test = new MethodDeclaration(compilationResult);

        test.selector = TEST_METHOD_NAME.toCharArray();
        test.modifiers = ClassFileConstants.AccPrivate;
        test.annotations = new Annotation[]{
            createSuppressWarningsAnnotation("unused")
        };

        if (sqlClass.getClassDefType() == EClassType.REPORT) {
            test.returnType = new SingleTypeReference(TypeReference.VOID, 0);
            test.arguments = new Argument[sqlClass.getInputParameters().size()];
            for (int i = 0; i < test.arguments.length; i++) {
                AdsParameterPropertyDef param = sqlClass.getInputParameters().get(i);
                test.arguments[i] = new Argument(param.getName().toCharArray(), 0, new AdsTypeReference(sqlClass, param.getValue().getType()), 0);
            }
        } else {
            AdsMethodDef method = sqlClass.getMethods().findById(AdsSystemMethodDef.ID_STMT_EXECUTE, ExtendableDefinitions.EScope.LOCAL).get();
            if (method != null) {
                test.returnType = new AdsTypeReference(sqlClass, method.getProfile().getReturnValue().getType());
                if (method.getAccessFlags().isStatic()) {
                    test.modifiers |= ClassFileConstants.AccStatic;
                }
                test.arguments = new Argument[method.getProfile().getParametersList().size()];
                for (int i = 0; i < test.arguments.length; i++) {
                    MethodParameter p = method.getProfile().getParametersList().get(i);
                    test.arguments[i] = new Argument(p.getName().toCharArray(), 0, new AdsTypeReference(sqlClass, p.getType()), 0);
                }
            } else {
                return;
            }
        }

        test.statements = AdsSqlClassExecuteWriter.createStatements(sqlClass, true);
        methods.add(test);
    }
}
