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
import org.eclipse.jdt.internal.compiler.ads.syntetics.BaseGenerator;
import org.eclipse.jdt.internal.compiler.ast.SingleTypeReference;
import org.eclipse.jdt.internal.compiler.ast.Statement;
import org.eclipse.jdt.internal.compiler.ast.TypeReference;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsDynamicPropertyDef;
import org.radixware.kernel.common.enums.EParamDirection;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.scml.Scml.Text;
import org.radixware.kernel.common.sqml.tags.ParameterTag;
import org.radixware.kernel.common.types.Id;

/**
 * Introduses variables to contain index of each output params.
 *
 */
public class AdsSqlClassOutParamsIdxProcessor extends AdsSqlClassSourceProcessor {

    private final CodePrinter cp;
    private Set<AdsDynamicPropertyDef> processedParams = new HashSet<AdsDynamicPropertyDef>();
    private final List<Statement> stmts;

    public AdsSqlClassOutParamsIdxProcessor(List<Statement> stmts) {
        super(CodePrinter.Factory.newNullPrinter());
        this.cp = null;
        this.stmts = stmts;
    }

    public AdsSqlClassOutParamsIdxProcessor(CodePrinter cp) {
        super(cp);
        this.cp = cp;
        this.stmts = null;
    }

    @Override
    protected void processTag(Scml.Tag tag) {
        if (tag instanceof ParameterTag) {
            super.processTag(tag);
        }
    }

    @Override
    protected void processParam(AdsDynamicPropertyDef parameter, Id pkParamPropId, EParamDirection direction, boolean isLiteral) {
        if (direction == EParamDirection.OUT || direction == EParamDirection.BOTH) {
            if (processedParams.add(parameter)) {
                if (stmts != null) {
                    stmts.add(BaseGenerator.createDeclaration("$" + parameter.getId() + "_Idx", new SingleTypeReference(TypeReference.INT, 0), BaseGenerator.createIntConstant(0)));
                } else {
                    final String paramIdxVarName = "$" + parameter.getId() + "_Idx";
                    cp.print("int ");
                    cp.print(paramIdxVarName);
                    cp.println("=0;");
                }
            }
        }
    }

    @Override
    protected void processText(Text text) {
        // NOTHING
    }
}