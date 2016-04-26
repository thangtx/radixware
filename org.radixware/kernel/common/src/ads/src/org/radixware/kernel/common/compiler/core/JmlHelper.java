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

import java.util.LinkedList;
import java.util.List;
import org.eclipse.jdt.internal.compiler.ast.ASTNode;
import org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.jdt.internal.compiler.ast.FieldDeclaration;
import org.eclipse.jdt.internal.compiler.ast.ImportReference;
import org.eclipse.jdt.internal.compiler.ast.Statement;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import static org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants.AccAbstract;
import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;
import org.eclipse.jdt.internal.compiler.impl.ReferenceContext;
import org.eclipse.jdt.internal.compiler.lookup.AdsClassDeclaration;
import org.eclipse.jdt.internal.compiler.lookup.AdsCompilationUnitDeclaration;
import org.eclipse.jdt.internal.compiler.lookup.AdsCompilationUnitScope;
import org.eclipse.jdt.internal.compiler.lookup.AdsDefinitionScope;
import org.eclipse.jdt.internal.compiler.lookup.AdsMethodDeclaration;
import org.eclipse.jdt.internal.compiler.lookup.AdsTypeDeclaration;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.CompilationUnitScope;
import org.eclipse.jdt.internal.compiler.lookup.Scope;
import org.eclipse.jdt.internal.compiler.parser.Parser;
import org.radixware.kernel.common.compiler.core.problems.AdsProblemReporter;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.ICompilable;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsUserMethodDef;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.jml.Jml;


public class JmlHelper {

    private static final boolean DEBUG = false;

    private interface SrcProivder {

        public AdsClassDef.ClassSource getSources(AdsClassDef clazz);
    }

    private static Jml[] computeJmls(AdsCompilationUnitDeclaration cu, AdsClassDef clazz, SrcProivder provider) {
        List<Jml> bodies = new LinkedList<>();

        for (AdsClassDef c = clazz; c != null; c = c.getHierarchy().findOverwritten().get()) {
            AdsClassDef.ClassSource src = provider.getSources(c);
            for (AdsClassDef.SourcePart sp : src) {
                if (sp.getUsageEnvironment() == ((AdsCompilationUnitScope) cu.scope).getEnvType() && !sp.getItems().isEmpty()) {
                    bodies.add(sp);
                }
            }
        }
        if (bodies.isEmpty()) {
            return null;
        }
        return bodies.toArray(new Jml[bodies.size()]);
    }

    public static void attachInnerDeclarations(AdsClassDeclaration clazzDecl, Scope scope) {
        AdsClassDef clazz = (AdsClassDef) clazzDecl.getDefinition();
        Jml[] srcs = computeJmls((AdsCompilationUnitDeclaration) clazzDecl.scope.compilationUnitScope().referenceContext(), clazz, new SrcProivder() {
            @Override
            public AdsClassDef.ClassSource getSources(AdsClassDef clazz) {
                return clazz.getBody();
            }
        });
        if (srcs != null && srcs.length > 0) {
            JmlMarkupData body = JmlMarkupData.build(scope.compilationUnitScope(), clazz, srcs, (AdsProblemReporter) clazzDecl.scope.problemReporter());
            if (body != null && body.content.length > 0) {                
                clazzDecl.bodyEnd = body.content.length - 1;
                List<FieldDeclaration> parsedFields = new LinkedList<>();
                List<AbstractMethodDeclaration> parsedMethods = new LinkedList<>();
                List<TypeDeclaration> parsedTypes = new LinkedList<>();

                char[] texttoparse = /*new char[body.content.length + 2];
                         System.arraycopy(body.content, 0, texttoparse, 1, body.content.length);
                         texttoparse[0] = '{';
                         texttoparse[texttoparse.length - 1] = '}';*/ body.content;
                Parser parser = new Parser(clazzDecl.scope.problemReporter(), false);
                CompilationUnitDeclaration decl = new CompilationUnitDeclaration(clazzDecl.scope.problemReporter(), clazzDecl.compilationResult, texttoparse.length);
                ASTNode[] nodes = parser.parseClassBodyDeclarations(texttoparse, 0, texttoparse.length, decl);

                if (DEBUG) {

                    System.out.println("-------------------------- parsed inner declarations -------------------------");
                }
                StringBuffer sb = null;
                if (DEBUG) {
                    sb = new StringBuffer();
                }
                for (int i = 0; i < nodes.length; i++) {
                    ASTNode node = nodes[i];
                    if (DEBUG) {
                        node.print(10, sb);
                    }
                    if (node instanceof FieldDeclaration) {
                        node = body.transform((FieldDeclaration) node);
                        parsedFields.add((FieldDeclaration) node);
                        nodes[i] = node;
                    }
                    if (node instanceof AbstractMethodDeclaration) {
                        node = body.transform((AbstractMethodDeclaration) node);
                        parsedMethods.add((AbstractMethodDeclaration) node);
                        nodes[i] = node;
                    }
                    if (node instanceof TypeDeclaration) {
                        node = body.transform((TypeDeclaration) node);
                        parsedTypes.add((TypeDeclaration) node);
                        nodes[i] = node;
                    }
                }
                if (DEBUG) {
                    System.out.println(sb);
                    System.out.println("-------------------------- converted inner declarations -------------------------");
                    sb.setLength(0);
                    for (int i = 0; i < nodes.length; i++) {
                        ASTNode node = nodes[i];
                        node.print(10, sb);
                    }
                    System.out.println(sb);
                    System.out.println("-------------------------- ---------------------------- -------------------------");

                }
                if (!parsedFields.isEmpty()) {
                    clazzDecl.fields = parsedFields.toArray(new FieldDeclaration[parsedFields.size()]);
                }

                if (!parsedMethods.isEmpty()) {
                    AbstractMethodDeclaration[] newMethods = new AbstractMethodDeclaration[clazzDecl.methods.length + parsedMethods.size()];
                    System.arraycopy(clazzDecl.methods, 0, newMethods, 0, clazzDecl.methods.length);
                    System.arraycopy(parsedMethods.toArray(new AbstractMethodDeclaration[parsedMethods.size()]), 0, newMethods, clazzDecl.methods.length, parsedMethods.size());
                    clazzDecl.methods = newMethods;
                }

                if (!parsedTypes.isEmpty()) {
                    TypeDeclaration[] newMemberTypes = new TypeDeclaration[clazzDecl.memberTypes.length + parsedTypes.size()];
                    System.arraycopy(clazzDecl.memberTypes, 0, newMemberTypes, 0, clazzDecl.memberTypes.length);
                    System.arraycopy(parsedTypes.toArray(new TypeDeclaration[parsedTypes.size()]), 0, newMemberTypes, clazzDecl.memberTypes.length, parsedTypes.size());
                    clazzDecl.memberTypes = newMemberTypes;
                }
            }
        }
    }

    public static void attachOuterDeclarations(AdsCompilationUnitDeclaration cu, Scope scope, AdsClassDeclaration clazzDecl) {
        AdsClassDef clazz = (AdsClassDef) clazzDecl.getDefinition();

        Jml[] srcs = computeJmls(cu, clazz, new SrcProivder() {
            @Override
            public AdsClassDef.ClassSource getSources(AdsClassDef clazz) {
                return clazz.getHeader();
            }
        });
        if (srcs != null && srcs.length > 0) {
            final JmlMarkupData header = JmlMarkupData.build(scope.compilationUnitScope(), clazz, srcs, (AdsProblemReporter) cu.problemReporter);
            if (header != null && header.content.length > 0) {
                clazzDecl.bodyEnd = header.content.length - 1;

                Parser parser = new Parser(cu.problemReporter, false);
                ICompilationUnit dummy = new ICompilationUnit() {
                    @Override
                    public char[] getContents() {
                        return header.content;
                    }

                    @Override
                    public char[] getMainTypeName() {
                        return null;
                    }

                    @Override
                    public char[][] getPackageName() {
                        return null;
                    }

                    @Override
                    public boolean ignoreOptionalProblems() {
                        return false;
                    }

                    @Override
                    public char[] getFileName() {
                        return null;
                    }
                };
                CompilationUnitDeclaration decl = parser.parse(dummy, clazzDecl.compilationResult, 0, header.content.length);
                if (DEBUG) {
                    System.out.println("-------------------------- HEADER PARSE RESULT -------------------------");
                }
                if (decl != null) {

                    if (decl.imports != null) {
                        cu.imports = decl.imports;
                    }
                    if (decl.types != null) {
                        cu.types = decl.types;
                    }
                    if (DEBUG) {
                        StringBuffer sb = new StringBuffer();
                        if (cu.imports != null) {
                            for (ImportReference imp : cu.imports) {
                                imp.print(5, sb);
                            }
                        }
                        if (cu.types != null) {
                            for (TypeDeclaration td : cu.types) {
                                td.print(5, sb);
                            }
                        }
                        System.out.println(sb);
                    }

                }
                if (DEBUG) {
                    System.out.println("--------------------------END HEADER PARSE RESULT -------------------------");
                }
            }
        }
    }

    //JmlHelper.attachMethodBody(enclosingAdsScope.adsContext(), this, enclosingAdsScope.getEnvironmentType(), (AdsProblemReporter) enclosingAdsScope.problemReporter(), enclosingAdsScope);
    public static void attachMethodBody(AdsDefinitionScope adsScope, AdsMethodDeclaration method) {
        attachMethodBody(adsScope.adsContext(), method, adsScope.getEnvironmentType(), (AdsProblemReporter) adsScope.problemReporter(), method.scope);
    }

    public static void attachMethodBody(AdsTypeDeclaration enclosingType, AdsMethodDeclaration method, ERuntimeEnvironmentType env, AdsProblemReporter reporter, Scope scope) {
        if ((method.modifiers & AccAbstract) != 0) {
            return;
        }
        if (method.getMethod() instanceof AdsUserMethodDef) {
            JmlMarkupData body = JmlMarkupData.build(scope.compilationUnitScope(), method.getMethod(), new Jml[]{((AdsUserMethodDef) method.getMethod()).getSource(env)}, reporter);
            if (body == null) {
                return;
            }
            Parser parser = new Parser(reporter, true);

            method.bodyStart = 0;
            method.bodyEnd = body.content.length - 1;
            parser.scanner.source = body.content;
            parser.parse(method, enclosingType.getCompilationUnitDeclaration());

            if (DEBUG) {
                System.out.println("-----------------------  ORIGINAL  -------------------------");
                StringBuffer buffer = new StringBuffer();
                method.print(5, buffer);
                System.out.println(buffer);
            }
            body.transform(method.statements);
            if (DEBUG) {
                System.out.println("-----------------------  MODIFIED  -------------------------");
                StringBuffer buffer = new StringBuffer();
                method.print(5, buffer);
                System.out.println(buffer);
                System.out.println("------------------------------------------------------------");
            }
        }
    }

    public static Statement[] parseStatements(CompilationUnitScope scope, Definition contextDefinition, ReferenceContext referenceContext, CompilationUnitDeclaration cu, Jml snippet) {
        JmlMarkupData body = JmlMarkupData.build(scope, contextDefinition, new Jml[]{snippet}, (AdsProblemReporter) cu.problemReporter);

        if (body == null) {
            return null;
        }
        final List<Statement> statements = new LinkedList<>();
        Parser parser = new Parser(cu.problemReporter, true) {
            @Override
            public void parseStatements(ReferenceContext rc, int start, int end, TypeDeclaration[] types, CompilationUnitDeclaration unit) {
                super.parseStatements(rc, start, end, types, unit); //To change body of generated methods, choose Tools | Templates.
                for (int i = 0; i <= astPtr; i++) {
                    statements.add((Statement) astStack[i]);
                }
            }
        };

        parser.scanner.source = body.content;
        parser.parseStatements(referenceContext, 0, body.content.length - 1, null, cu);

        if (DEBUG) {
            System.out.println("-----------------------  ORIGINAL  -------------------------");
            StringBuffer buffer = new StringBuffer();
            for (Statement stmt : statements) {
                stmt.print(5, buffer);
            }
            System.out.println(buffer);
        }
        Statement[] stmts = statements.toArray(new Statement[statements.size()]);
        body.transform(stmts);
        if (DEBUG) {
            System.out.println("-----------------------  MODIFIED  -------------------------");
            StringBuffer buffer = new StringBuffer();
            for (Statement stmt : statements) {
                stmt.print(5, buffer);
            }
            System.out.println(buffer);
            System.out.println("------------------------------------------------------------");
        }

        return stmts;
    }
}
