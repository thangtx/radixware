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
import org.radixware.kernel.common.sqml.tags.XPathTag;


public class XPathVTag<T extends XPathTag> extends SqmlVTag<T> {

    public XPathVTag(T tag) {
        super(tag);
    }

    @Override
    public String getTokenName() {
        return "tag-xpath";
    }

    @Override
    protected void printTitle(CodePrinter cp) {
        final T tag = getTag();

        if (tag.getItems().isEmpty()) {
            cp.printError();
            return;
        }

        cp.print("'");
        boolean itemPrinted = false;

        for (XPathTag.Item item : tag.getItems()) {
            if (itemPrinted) {
                cp.print("/");
            } else {
                itemPrinted = true;
            }
            if (item.isAttribute()) {
                cp.print("@");
            }
            final String name = item.getName();
            if (name != null && !name.isEmpty()) {
                cp.print(name);
            } else {
                cp.printError();
            }
            if (!item.isAttribute() && item.getIndex() != null) {
                cp.print("[");
                cp.print(item.getIndex().longValue());
                cp.print("]");
            }
            final String condition = item.getCondition();
            if (condition != null && !condition.isEmpty()) {
                cp.print("[");
                cp.print(condition);
                cp.print("]");
            }
        }
        cp.print("'");
    }
}
