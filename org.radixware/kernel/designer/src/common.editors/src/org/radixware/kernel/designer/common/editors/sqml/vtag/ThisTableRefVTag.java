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

package org.radixware.kernel.designer.common.editors.sqml.vtag;

import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.sqml.tags.ThisTableRefTag;
import org.radixware.kernel.common.sqml.translate.ThisTableRefTagTranslator;


public class ThisTableRefVTag<T extends ThisTableRefTag> extends SqmlVTag<T> {

    public ThisTableRefVTag(T tag) {
        super(tag);
    }

    @Override
    protected void printTitle(CodePrinter cp) {
        try {
            new ThisTableRefTagTranslator(null).translate(getTag(), cp);
        } catch (Exception ex) {//printTitle should not throw any exception
            cp.printError();
        }
    }

    @Override
    public String getTokenName() {
        return "tag-this-table-ref";
    }
}
