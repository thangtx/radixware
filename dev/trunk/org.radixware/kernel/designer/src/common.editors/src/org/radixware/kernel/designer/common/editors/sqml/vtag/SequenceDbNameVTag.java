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

import org.radixware.kernel.common.defs.dds.DdsSequenceDef;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.sqml.tags.SequenceDbNameTag;


public class SequenceDbNameVTag<T extends SequenceDbNameTag> extends SqmlVTag<T> {

    public SequenceDbNameVTag(T tag) {
        super(tag);
    }

    @Override
    protected void printTitle(CodePrinter cp) {
        final T tag = getTag();

        final DdsSequenceDef sequence = tag.findSequence();

        if (sequence != null) {
            cp.print(sequence.getName());
        } else {
            cp.printError();
        }

        switch (tag.getPostfix()) {
            case CUR_VAL:
                cp.print(".CurVal");
                break;
            case NEXT_VAL:
                cp.print(".NextVal");
                break;
        }
    }

    @Override
    public String getTokenName() {
        return "tag-sequence-db-name";
    }
}
