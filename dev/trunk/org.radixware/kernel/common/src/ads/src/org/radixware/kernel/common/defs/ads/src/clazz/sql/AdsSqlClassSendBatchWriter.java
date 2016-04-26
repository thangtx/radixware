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
import org.radixware.kernel.common.defs.ads.src.WriterUtils;
import org.radixware.kernel.common.defs.ads.src.clazz.AdsMethodWriter;
import org.radixware.kernel.common.scml.CodePrinter;


public class AdsSqlClassSendBatchWriter extends AdsMethodWriter<AdsMethodDef> {

    public AdsSqlClassSendBatchWriter(JavaSourceSupport support, AdsMethodDef method, UsagePurpose usagePurpose) {
        super(support, method, usagePurpose);
    }

    @Override
    protected void writeBody(CodePrinter cp) {
        cp.enterBlock();
        cp.println("try {");
        cp.enterBlock();
        cp.print("$preparedStatementsCache.sendBatch(");
        cp.print(WriterUtils.GET_ARTE_INVOCATON);
        cp.println(");");
        cp.leaveBlock();
        cp.println("} catch (java.sql.SQLException ex) {");
        cp.enterBlock();
        cp.println("throw new org.radixware.kernel.server.exceptions.DatabaseError(ex);");
        cp.leaveBlock();
        cp.println("}");
        cp.leaveBlock();
    }

    public static Statement[] createStatements(AdsSqlClassDef sqlClass) {
        TryStatement tryBlock = new TryStatement();
        tryBlock.tryBlock = BaseGenerator.createBlock(
                BaseGenerator.createMessageSend(SqlClassVariables.nameRef("$preparedStatementsCache"), "sendBatch", BaseGenerator.createArteAccessMethodInvocation(sqlClass)));
        tryBlock.catchArguments = new Argument[]{
            new Argument("e".toCharArray(), 0, BaseGenerator.createQualifiedType(BaseGenerator.SQLEXCEPTION_TYPE_NAME), 0)
        };
        tryBlock.catchBlocks = new Block[]{
            BaseGenerator.createBlock(
            BaseGenerator.createThrow(BaseGenerator.DATABASEERROR_TYPE_NAME, null, true))
        };
        return new Statement[]{
            tryBlock
        };
    }
}