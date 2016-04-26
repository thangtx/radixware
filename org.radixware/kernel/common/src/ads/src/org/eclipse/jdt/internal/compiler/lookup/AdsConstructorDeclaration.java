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
import org.eclipse.jdt.internal.compiler.ast.Argument;
import org.eclipse.jdt.internal.compiler.ast.ConstructorDeclaration;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import static org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants.AccAbstract;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.compiler.parser.Parser;
import org.eclipse.jdt.internal.compiler.problem.ProblemReporter;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.MethodParameter;

import org.radixware.kernel.common.defs.ads.type.AdsAccessFlags;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.eclipse.jdt.internal.compiler.ast.AdsTypeReference;
import org.radixware.kernel.common.compiler.core.StatetementListPrinter;
import org.radixware.kernel.common.compiler.core.ast.Java2JmlConverter;
import org.radixware.kernel.common.compiler.core.ast.RadixObjectLocator;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;


public class AdsConstructorDeclaration extends ConstructorDeclaration implements ClassFileConstants {

    private final AdsMethodDef method;
    private final ERuntimeEnvironmentType env;
    private final RadixObjectLocator locator;

    public AdsConstructorDeclaration(AdsClassDef clazz, AdsMethodDef methodDef, ERuntimeEnvironmentType env, CompilationResult compilationResult, RadixObjectLocator locator) {
        super(compilationResult);
        this.env = env;
        this.modifiers = 0;
        this.method = methodDef;
        this.locator = locator;
        final AdsAccessFlags acc = methodDef.getAccessFlags();
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
    }

    public AdsMethodDef getMethod() {
        return method;
    }
    private static boolean DEBUG = false;

    @Override
    public void resolveStatements() {
        final Java2JmlConverter converter = new Java2JmlConverter(locator);
        if (DEBUG) {
            System.out.println("-----------------------  ORIGINAL  -------------------------");
            StringBuffer buffer = new StringBuffer();
            this.print(5, buffer);
            System.out.println(buffer);
        }
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
}
