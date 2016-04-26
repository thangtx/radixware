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
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlEditorPane.VTag;


public abstract class SqmlVTag<T extends Sqml.Tag> extends VTag<T> {

    public SqmlVTag(T tag) {
        super(tag);
    }

    protected abstract void printTitle(CodePrinter cp);

    @Override
    public final String getTitle() {
        final CodePrinter cp = CodePrinter.Factory.newSqlPrinter();
        printTitle(cp);
        final String title = cp.toString();
        return title;
    }

    @Override
    public String getToolTip() {
        final T tag = getTag();
        return tag.getToolTip();
    }
}
