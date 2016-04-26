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

package org.radixware.kernel.common.sqml.translate;

import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.IParameterDef;
import org.radixware.kernel.common.exceptions.TagTranslateError;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.sqml.tags.ParameterTag;


class ParameterTagTranslator<T extends ParameterTag> extends SqmlTagTranslator<T> {

    @Override
    public void translate(final T tag, CodePrinter cp) {

        final IParameterDef param = tag.getParameter(); // for check
        // parameter tag translated into java code in AdsSqlClass*Processor
        if (param.isMulty()) {
            cp.print("(");
        }

        if (!param.canBeUsedInSqml()) {
            throw new TagTranslateError(tag, "Parameter " + param.getName() + " can not be used in SQML");
        }
//        performCheckIfPossible(new Checker() {
//
//            @Override
//            public void check(IProblemHandler problemHandler) {
//                if (!param.canBeUsedInSqml()) {
//                    problemHandler.accept(RadixProblem.Factory.newError(tag, "Parameter " + param.getName() + " can not be used in SQML"));
//                }
//            }
//        });

        cp.print(":");
        cp.print(param.getName());
        if (param.isMulty()) {
            cp.print(")");
        }
    }
}
