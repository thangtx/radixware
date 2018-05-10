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

package org.radixware.kernel.common.jml;

import java.text.MessageFormat;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.WriterUtils;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.schemas.xscml.JmlType;
import org.radixware.schemas.xscml.JmlType.Item;


public class JmlTagLiteral extends Jml.Tag {

    private String literal;

    JmlTagLiteral(JmlType.Item.Literal literal) {
        super(literal);
        this.literal = literal.getLiteral();
    }

    public String getLiteral() {
        return literal;
    }

    @Override
    public void appendTo(Item item) {
        Item.Literal lit = item.addNewLiteral();
        lit.setLiteral(literal);
    }

    @Override
    public String toString() {
        return MessageFormat.format("[tag literal={0}]", literal);
    }

    @Override
    public String getTypeTitle() {
        return "Literal Reference";
    }

    @Override
    public String getTypesTitle() {
        return "Literal References";
    }

    @Override
    public String getDisplayName() {
        return literal;
    }

    @Override
    public JavaSourceSupport getJavaSourceSupport() {
        return new JavaSourceSupport(this) {

            @Override
            public CodeWriter getCodeWriter(UsagePurpose purpose) {
                return new JmlTagWriter(this, purpose, JmlTagLiteral.this) {

                    @Override
                    public boolean writeCode(CodePrinter printer) {
                        super.writeCode(printer);
                        WriterUtils.enterHumanUnreadableBlock(printer);
                        printer.print(getLiteral());
                        WriterUtils.leaveHumanUnreadableBlock(printer);
                        return true;
                    }

                    @Override
                    public void writeUsage(CodePrinter printer) {
                    }
                };
            }
        };
    }

    @Override
    public void check(IProblemHandler problemHandler,Jml.IHistory h) {
        //always ok
    }
}
