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

import org.eclipse.jdt.internal.compiler.ads.syntetics.BaseGenerator;
import org.eclipse.jdt.internal.compiler.ast.Argument;
import org.eclipse.jdt.internal.compiler.ast.Block;
import org.eclipse.jdt.internal.compiler.ast.Statement;
import org.eclipse.jdt.internal.compiler.ast.TryStatement;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsSqlClassDef;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.src.clazz.AdsMethodWriter;
import org.radixware.kernel.common.scml.CodePrinter;


public class AdsSqlClassSetExecuteBatchWriter extends AdsMethodWriter<AdsMethodDef> {

    public AdsSqlClassSetExecuteBatchWriter(JavaSourceSupport support, AdsMethodDef method, UsagePurpose usagePurpose) {
        super(support, method, usagePurpose);
    }

    @Override
    protected void writeBody(CodePrinter cp) {
        cp.enterBlock();
        cp.println("$preparedStatementsCache.setBatchSize(size);");
        cp.leaveBlock();
    }

    public static Statement[] createStatements(AdsSqlClassDef sqlClass) {

        return new Statement[]{
            BaseGenerator.createMessageSend(SqlClassVariables.nameRef("$preparedStatementsCache"), "setBatchSize", SqlClassVariables.nameRef("size"))
        };
    }
}