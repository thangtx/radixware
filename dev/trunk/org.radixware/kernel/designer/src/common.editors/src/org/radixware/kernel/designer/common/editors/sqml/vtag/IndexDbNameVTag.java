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

import org.radixware.kernel.common.defs.dds.DdsIndexDef;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.sqml.tags.IndexDbNameTag;


public class IndexDbNameVTag<T extends IndexDbNameTag> extends SqmlVTag<T> {

    public IndexDbNameVTag(T tag) {
        super(tag);
    }

    @Override
    protected void printTitle(CodePrinter cp) {
        final T tag = getTag();
        final DdsIndexDef index = tag.findIndex();
        if (index != null) {
            cp.print(index.getName());
        } else {
            cp.printError();
        }
    }

    @Override
    public String getTokenName() {
        return "tag-index-db-name";
    }
}
