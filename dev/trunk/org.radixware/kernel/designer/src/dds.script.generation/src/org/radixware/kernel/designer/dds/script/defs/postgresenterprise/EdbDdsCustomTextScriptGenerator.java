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

package org.radixware.kernel.designer.dds.script.defs.postgresenterprise;

import org.radixware.kernel.common.defs.dds.DdsCustomTextDef;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.designer.dds.script.DdsScriptGeneratorUtils;
import org.radixware.kernel.common.scml.ScmlCodePrinter;


class EdbDdsCustomTextScriptGenerator {

    public static void getScript(CodePrinter cp, DdsCustomTextDef customText) {
        final Sqml text = customText.getText();

        cp.enterBlock(1);
        if (cp instanceof ScmlCodePrinter) {
            final ScmlCodePrinter scmlCodePrinter = (ScmlCodePrinter) cp;
            scmlCodePrinter.print(text);
        } else {
            final String sql = DdsScriptGeneratorUtils.translateSqml(text);
            cp.print(sql);
        }
        cp.leaveBlock(1);
//        String sqlLines[] = sql.split("\\n");
//        boolean linePrintedFlag = false;
//        for (String sqlLine : sqlLines) {
//            if (linePrintedFlag) {
//                cp.println();
//            } else {
//                linePrintedFlag = true;
//            }
//            cp.print('\t');
//            cp.print(sqlLine);
//        }
    }
}
