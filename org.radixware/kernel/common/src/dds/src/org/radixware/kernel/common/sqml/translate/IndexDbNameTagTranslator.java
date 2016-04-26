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
import org.radixware.kernel.common.defs.dds.DdsIndexDef;
import org.radixware.kernel.common.exceptions.TagTranslateError;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.sqml.providers.SqmlVisitorProviderFactory;
import org.radixware.kernel.common.sqml.tags.IndexDbNameTag;

class IndexDbNameTagTranslator<T extends IndexDbNameTag> extends SqmlTagTranslator<T> {

    public IndexDbNameTagTranslator(IProblemHandler problemHandler) {
        super(problemHandler);
    }

    @Override
    public void translate(T tag, CodePrinter cp) {
        final DdsIndexDef index = tag.getIndex();

        if (!SqmlVisitorProviderFactory.newIndexDbNameTagProvider().isTarget(index)) {
            throw new TagTranslateError(tag, "Illegal index used: '" + index.getQualifiedName() + "'.");
        }

        checkDepecation(tag, index);

        cp.print(index.getDbName());
    }
}
