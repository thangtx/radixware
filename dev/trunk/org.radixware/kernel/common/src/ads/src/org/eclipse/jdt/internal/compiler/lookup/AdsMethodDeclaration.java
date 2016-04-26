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

import org.eclipse.jdt.internal.compiler.CompilationResult;
import static org.eclipse.jdt.internal.compiler.ast.ASTNode.printAnnotations;
import static org.eclipse.jdt.internal.compiler.ast.ASTNode.printIndent;
import static org.eclipse.jdt.internal.compiler.ast.ASTNode.printModifiers;
import org.eclipse.jdt.internal.compiler.ast.Argument;
import org.eclipse.jdt.internal.compiler.ast.MethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.TypeParameter;
import org.eclipse.jdt.internal.compiler.ast.TypeReference;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import static org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants.AccAbstract;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.MethodParameter;

import org.radixware.kernel.common.defs.ads.type.AdsAccessFlags;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;

import org.eclipse.jdt.internal.compiler.ast.AdsTypeReference;
import org.radixware.kernel.common.compiler.core.JmlHelper;
import org.radixware.kernel.common.compiler.core.ast.Java2JmlConverter;
import org.radixware.kernel.common.compiler.core.ast.RadixObjectLocator;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsSystemMethodDef;
import static org.radixware.kernel.common.defs.ads.clazz.members.AdsSystemMethodDef.ID_REPORT_EXECUTE;
import static org.radixware.kernel.common.defs.ads.clazz.members.AdsSystemMethodDef.ID_REPORT_OPEN;
import static org.radixware.kernel.common.defs.ads.clazz.members.AdsSystemMethodDef.ID_STMT_EXECUTE;
import static org.radixware.kernel.common.defs.ads.clazz.members.AdsSystemMethodDef.ID_STMT_SEND_BATCH;
import static org.radixware.kernel.common.defs.ads.clazz.members.AdsSystemMethodDef.ID_STMT_SET_EXECUTE_BATCH;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsSqlClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.ads.src.clazz.AdsSystemMethodWriter;
import org.radixware.kernel.common.defs.ads.src.clazz.sql.AdsSqlClassExecuteReportWriter;
import org.radixware.kernel.common.defs.ads.src.clazz.sql.AdsSqlClassExecuteWriter;
import org.radixware.kernel.common.defs.ads.src.clazz.sql.AdsSqlClassSendBatchWriter;
import org.radixware.kernel.common.defs.ads.src.clazz.sql.AdsSqlClassSetExecuteBatchWriter;
import org.radixware.kernel.common.types.Id;


public class AdsMethodDeclaration extends MethodDeclaration implements ClassFileConstants {

    private final AdsMethodDef method;
    private final ERuntimeEnvironmentType env;
    private final RadixObjectLocator locator;

    public AdsMethodDeclaration(AdsClassDef clazz, AdsMethodDef methodDef, ERuntimeEnvironmentType env, CompilationResult compilationResult, RadixObjectLocator locator) {
        super(compilationResult);
        this.locator = locator;
        this.env = env;
        this.modifiers = 0;
        this.method = methodDef;
        AdsAccessFlags acc = methodDef.getAccessFlags();
        if (acc.isAbstract()) {
            modifiers |= AccAbstract;
            modifiers |= ExtraCompilerModifiers.AccSemicolonBody;
        }
        if (acc.isFinal()) {
            modifiers |= AccFinal;
        }
        if (acc.isPrivate()) {
            modifiers |= AccPrivate;
        }
        if (acc.isPublic()) {
            modifiers |= AccPublic;
        }
        if (acc.isProtected()) {
            modifiers |= AccProtected;
        }
        if (acc.isStatic()) {
            modifiers |= AccStatic;
        }
        this.selector = methodDef.getId().toCharArray();
        this.arguments = new Argument[methodDef.getProfile().getParametersList().size()];
        int index = 0;
        for (MethodParameter p : methodDef.getProfile().getParametersList()) {
            int argMod = 0;
            if (p.isVariable()) {
                //   modifiers |= AccVarargs;
            }
            this.arguments[index++] = new Argument(p.getName().toCharArray(), 0l, new AdsTypeReference(clazz, p.getType(), 0, 0), argMod);
        }
        if (methodDef.getProfile().getReturnValue() != null && methodDef.getProfile().getReturnValue().getType() != null && !methodDef.getProfile().getReturnValue().getType().isVoid()) {
            this.returnType = new AdsTypeReference(clazz, methodDef.getProfile().getReturnValue().getType(), 0, 0);
        } else {
            this.returnType = TypeReference.baseTypeReference(TypeIds.T_void, 0);
        }
    }

    public AdsMethodDef getMethod() {
        return method;
    }
    private static boolean DEBUG = false;

    private AdsDefinitionScope adsScope() {
        Scope s = scope;
        while (s != null) {
            if (s instanceof AdsDefinitionScope) {
                return (AdsDefinitionScope) s;
            }
            s = s.parent;
        }
        return null;
    }

    @Override
    public void resolveStatements() {
        if (statements == null) {
            if (method instanceof AdsSystemMethodDef) {
                AdsClassDef clazz = method.getOwnerClass();
                if (clazz instanceof AdsSqlClassDef) {
                    final Id methodId = method.getId();
                    AdsSqlClassDef sqlClass = (AdsSqlClassDef) clazz;
                    if (methodId == ID_STMT_EXECUTE || methodId == ID_REPORT_OPEN) {
                        statements = AdsSqlClassExecuteWriter.createStatements(sqlClass, true);
                    } else if (methodId == ID_STMT_SET_EXECUTE_BATCH) {
                        statements = AdsSqlClassSetExecuteBatchWriter.createStatements(sqlClass);
                    } else if (methodId == ID_STMT_SEND_BATCH) {
                        statements = AdsSqlClassSendBatchWriter.createStatements(sqlClass);
                    } else if (methodId == ID_REPORT_EXECUTE && sqlClass instanceof AdsReportClassDef) {
                        statements = AdsSqlClassExecuteReportWriter.createStatements((AdsReportClassDef) clazz);
                    }
                } else {
                    statements = AdsSystemMethodWriter.createStatements((AdsSystemMethodDef) method);
                }
            }
            if (statements == null) {
                AdsDefinitionScope enclosingAdsScope = adsScope();
                JmlHelper.attachMethodBody(enclosingAdsScope, this);
            }
        }

        if (DEBUG) {
            System.out.println("-----------------------  ORIGINAL  -------------------------");
            StringBuffer buffer = new StringBuffer();
            this.print(5, buffer);
            System.out.println(buffer);
        }
        final Java2JmlConverter converter = new Java2JmlConverter(locator);
        converter.convertToJML(statements, null);

        if (DEBUG) {
            System.out.println("-----------------------  CONVERTED  -------------------------");
            StringBuffer buffer = new StringBuffer();
            this.print(5, buffer);
            System.out.println(buffer);
            System.out.println("------------------------------------------------------------");
        }
        super.resolveStatements();
    }

    @Override
    public StringBuffer print(int tab, StringBuffer output) {
        if (this.javadoc != null) {
            this.javadoc.print(tab, output);
        }
        printIndent(tab, output);
        printModifiers(this.modifiers, output);
        if (this.annotations != null) {
            printAnnotations(this.annotations, output);
        }

        TypeParameter[] typeParams = typeParameters();
        if (typeParams != null) {
            output.append('<');
            int max = typeParams.length - 1;
            for (int j = 0; j < max; j++) {
                typeParams[j].print(0, output);
                output.append(", ");//$NON-NLS-1$
            }
            typeParams[max].print(0, output);
            output.append('>');
        }

        printReturnType(0, output).append("`").append(this.method.getName()).append("`").append('(');
        if (this.arguments != null) {
            for (int i = 0; i < this.arguments.length; i++) {
                if (i > 0) {
                    output.append(", "); //$NON-NLS-1$
                }
                this.arguments[i].print(0, output);
            }
        }
        output.append(')');
        if (this.thrownExceptions != null) {
            output.append(" throws "); //$NON-NLS-1$
            for (int i = 0; i < this.thrownExceptions.length; i++) {
                if (i > 0) {
                    output.append(", "); //$NON-NLS-1$
                }
                this.thrownExceptions[i].print(0, output);
            }
        }
        printBody(tab + 1, output);
        return output;
    }
}
