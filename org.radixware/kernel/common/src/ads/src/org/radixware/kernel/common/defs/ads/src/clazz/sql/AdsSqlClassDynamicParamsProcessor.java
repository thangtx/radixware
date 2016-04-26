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

package org.radixware.kernel.common.defs.ads.src.clazz.sql;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.eclipse.jdt.internal.compiler.ast.AdsTypeReference;
import org.eclipse.jdt.internal.compiler.ast.LocalDeclaration;
import org.eclipse.jdt.internal.compiler.ast.Statement;
import org.eclipse.jdt.internal.compiler.ast.TaggedSingleNameReference;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsDynamicPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsParameterPropertyDef;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.enums.EParamDirection;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.scml.Scml.Tag;
import org.radixware.kernel.common.scml.Scml.Text;
import org.radixware.kernel.common.sqml.tags.ParameterTag;
import org.radixware.kernel.common.types.Id;


public class AdsSqlClassDynamicParamsProcessor extends AdsSqlClassSourceProcessor {

    private final CodePrinter cp;
    private final List<Statement> stmts;
    private final Set<AdsDynamicPropertyDef> processedParams = new HashSet<>();

    public AdsSqlClassDynamicParamsProcessor(final List<Statement> stmts) {
        super(CodePrinter.Factory.newNullPrinter());
        this.cp = null;
        this.stmts = stmts;
    }

    public AdsSqlClassDynamicParamsProcessor(final CodePrinter cp) {
        super(cp);
        this.cp = cp;
        this.stmts = null;
    }

    @Override
    protected void processTag(Tag tag) {
        if (tag instanceof ParameterTag) {
            super.processTag(tag);
        }
    }

    @Override
    protected void processParam(AdsDynamicPropertyDef param, Id pkParamPropId, EParamDirection direction, boolean isLiteral) {
        if (!(param instanceof AdsParameterPropertyDef)) {
            if (processedParams.add(param)) {
                if (this.stmts != null) {
                    LocalDeclaration decl = new LocalDeclaration(param.getName().toCharArray(), 0, 0);
                    decl.type = new AdsTypeReference(param, param.getValue().getType());
                    decl.initialization = new TaggedSingleNameReference(param, param, false);
                    stmts.add(decl);
                } else {
                    final AdsTypeDeclaration type = param.getValue().getType();
                    cp.print("final ");
                    type.getJavaSourceSupport().getCodeWriter(UsagePurpose.SERVER_EXECUTABLE).writeUsage(cp, type, param);
                    cp.print(" ");
                    cp.print(param.getName());
                    cp.print(" = ");
                    param.getJavaSourceSupport().getCodeWriter(UsagePurpose.SERVER_EXECUTABLE).writeUsage(cp);
                    cp.println(";");
                }
            }
        }
    }

    @Override
    protected void processText(Text text) {
        // NOTHING
    }
}