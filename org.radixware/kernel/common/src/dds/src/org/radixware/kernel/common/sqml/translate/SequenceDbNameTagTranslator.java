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

import org.radixware.kernel.common.defs.dds.DdsSequenceDef;
import org.radixware.kernel.common.exceptions.TagTranslateError;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.sqml.providers.SqmlVisitorProviderFactory;
import org.radixware.kernel.common.sqml.tags.SequenceDbNameTag;

class SequenceDbNameTagTranslator<T extends SequenceDbNameTag> extends SqmlTagTranslator<T> {

    @Override
    public void translate(T tag, CodePrinter cp) {
        final DdsSequenceDef sequence = tag.getSequence();

        if (!SqmlVisitorProviderFactory.newSequenceDbNameTagProvider().isTarget(sequence)) {
            throw new TagTranslateError(tag, "Illegal sequence used: '" + sequence.getQualifiedName() + "'.");
        }

        cp.print(sequence.getDbName());

        final SequenceDbNameTag.Postfix postfix = tag.getPostfix();

        switch (postfix) {
            case CUR_VAL:
                cp.print(".CurVal");
                break;
            case NEXT_VAL:
                cp.print(".NextVal");
                break;
            case NONE:
                break;
            default:
                throw new TagTranslateError(tag, "Illegal postfix in sequence tag: " + String.valueOf(postfix));
        }
    }
}
