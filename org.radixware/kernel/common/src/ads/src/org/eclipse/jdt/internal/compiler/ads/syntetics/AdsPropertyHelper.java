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
import org.eclipse.jdt.internal.compiler.ast.FieldDeclaration;
import org.eclipse.jdt.internal.compiler.ast.MethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.Statement;
import org.eclipse.jdt.internal.compiler.impl.ReferenceContext;
import org.eclipse.jdt.internal.compiler.lookup.AdsPropertyDeclaration;
import org.eclipse.jdt.internal.compiler.lookup.CompilationUnitScope;
import org.radixware.kernel.common.compiler.core.problems.AdsProblemReporter;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import static org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP;
import static org.radixware.kernel.common.enums.EPropNature.DYNAMIC;
import static org.radixware.kernel.common.enums.EPropNature.EVENT_CODE;
import static org.radixware.kernel.common.enums.EPropNature.EXPRESSION;
import static org.radixware.kernel.common.enums.EPropNature.FIELD;
import static org.radixware.kernel.common.enums.EPropNature.FIELD_REF;
import static org.radixware.kernel.common.enums.EPropNature.GROUP_PROPERTY;
import static org.radixware.kernel.common.enums.EPropNature.INNATE;
import static org.radixware.kernel.common.enums.EPropNature.PARENT_PROP;
import static org.radixware.kernel.common.enums.EPropNature.PROPERTY_PRESENTATION;
import static org.radixware.kernel.common.enums.EPropNature.SQL_CLASS_PARAMETER;
import static org.radixware.kernel.common.enums.EPropNature.USER;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;


public class AdsPropertyHelper {

    public static Statement[] createGetterBody(Definition contextDefinition, AdsPropertyDeclaration decl, CompilationResult result, ReferenceContext context, AdsProblemReporter reporter) {
        PropertyGetterGenerator generator = createGenerator(contextDefinition, decl, result, context);
        return generator.createGetterBody(decl.getProperty(), decl, reporter);
    }

    public static Statement[] createVisibleGetterBody(CompilationUnitScope scope, Definition contextDefinition, PropertyGeneratorFlowInfo flowInfo, AdsPropertyDeclaration decl, CompilationResult result, ReferenceContext context, AdsProblemReporter reporter) {
        PropertyGetterGenerator generator = createGenerator(contextDefinition, decl, result, context);
        return generator.createVisibleGetterBody(flowInfo, scope, decl, reporter);
    }

    private static PropertyGetterGenerator createGenerator(Definition contextDefinition, AdsPropertyDeclaration decl, CompilationResult result, ReferenceContext context) {
        PropertyGetterGenerator generator = null;
        AdsPropertyDef prop = decl.getProperty();
        switch (prop.getNature()) {
            case PROPERTY_PRESENTATION:
                throw new UnsupportedOperationException("Not supported yet");

            case DYNAMIC:
            case EVENT_CODE:
            case SQL_CLASS_PARAMETER:
            case GROUP_PROPERTY:
                generator = new NonPersistentPropertyGetterGenerator(contextDefinition, result, context);
                break;
            case USER:
                generator = new UserPropertyGetterGenerator(contextDefinition, result, context);
                break;
            case DETAIL_PROP:
            case EXPRESSION:
            case INNATE:
                generator = new InnatePropertyGetterGenerator(contextDefinition, result, context);
                break;
            case FIELD:
            case FIELD_REF:
                generator = new FieldPropertyGetterGenerator(contextDefinition, result, context);
                break;
            case PARENT_PROP:
                generator = new ParentPropertyGetterGenerator(contextDefinition, result, context);
                break;
            default:
                throw new UnsupportedOperationException("Not supported yet");
        }
        if (generator == null) {
            throw new UnsupportedOperationException("Not supported yet");
        }
        return generator;
    }
    private static final FieldDeclaration[] NO_BACKSTORE = new FieldDeclaration[0];

    public static FieldDeclaration[] createBackstoreFields(AdsPropertyDef property, AdsPropertyDeclaration propDecl, CompilationResult result, ReferenceContext context) {
        PropertyGetterGenerator generator = createGenerator(property, propDecl, result, context);
        List<FieldDeclaration> fields = new LinkedList<>();
        generator.createBackstoreFields(property, propDecl, fields);
        return fields.isEmpty() ? NO_BACKSTORE : fields.toArray(new FieldDeclaration[fields.size()]);
    }
}
